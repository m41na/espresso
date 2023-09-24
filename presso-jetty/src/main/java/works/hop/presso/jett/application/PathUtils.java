package works.hop.presso.jett.application;

import org.eclipse.jetty.util.MultiMap;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PathUtils {

    private PathUtils() {
    }

    public static String pathToRegex(String path) {
        Pattern pattern = Pattern.compile("(:?\\b.+?)(/)|(:?\\b.+$)");
        Matcher matcher = pattern.matcher(path);
        StringBuilder regex = new StringBuilder("/");
        while (matcher.find()) {
            String found = matcher.group();
            if (found.startsWith(":")) {
                if (matcher.group(1) != null) {
                    String matched = matcher.group(1);
                    extractMatched(regex, matched);
                    regex.append(matcher.group(2));
                } else {
                    String matched = matcher.group(3);
                    extractMatched(regex, matched);
                }
            } else if (found.endsWith("/")) {
                String matched = matcher.group(1);
                extractMatched(regex, matched);
                regex.append(matcher.group(2));
            } else {
                String matched = matcher.group(3);
                extractMatched(regex, matched);
            }
        }
        return regex.toString();
    }

    private static void extractMatched(StringBuilder regex, String matched) {
        if (matched.contains("*")) {
            matched = matched.replaceAll("\\*", "\\\\w*");
            regex.append("(").append(matched).append(")");
        } else if (matched.contains("?") || matched.contains("+")) {
            regex.append("(").append(matched).append(")");
        } else if (matched.startsWith(":")) {
            regex.append("(").append("[\\w-]+").append(")");
        } else {
            regex.append(matched);
        }
    }

    public static Map<String, String> pathToRegexMap(List<String> paths) {
        return paths.stream().collect(Collectors.toMap(
                path -> path,
                PathUtils::pathToRegex,
                (s1, s2) -> s1,
                LinkedHashMap::new));
    }

    public static Set<String> longestPathPrefix(Collection<String> paths) {
        return paths.stream().map(path -> {
            int regexIndex = path.indexOf("(");
            if (regexIndex > -1) {
                String pathPrefix = path.substring(0, regexIndex);
                if (pathPrefix.length() > 1 && pathPrefix.endsWith("/")) {
                    return pathPrefix.substring(0, pathPrefix.length() - 1);
                }
                return pathPrefix;
            }
            return path;
        }).collect(Collectors.toSet());
    }

    public static Map<String, String> extractPathVariables(String path, String pathInfo) {
        Map<String, String> map = new LinkedHashMap<>();
        String[] pathParts = path.split("/");
        String[] pathInfoParts = pathInfo.split("/");
        if (pathParts.length != pathInfoParts.length) {
            throw new RuntimeException("Mapped path does not match the length of supplied pathInfo");
        }
        for (int i = 0; i < pathParts.length; i++) {
            String pathPart = pathParts[i];
            String pathInfoPart = pathInfoParts[i];
            if (pathPart.startsWith(":")) {
                map.put(pathPart.substring(1), pathInfoPart);
            }
        }
        return map;
    }

    public static Map<String, List<String>> extractQueryVariables(MultiMap<String> queryParams) {
        return queryParams == null ? Collections.emptyMap() :
                queryParams.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (x, y) -> x));
    }
}
