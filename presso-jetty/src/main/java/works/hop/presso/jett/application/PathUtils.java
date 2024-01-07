package works.hop.presso.jett.application;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PathUtils {

    private PathUtils() {
    }

    public static String pathToRegex(String path) {
        Pattern pattern = Pattern.compile("(:.+?\\b)");
        Matcher matcher = pattern.matcher(path);
        StringBuilder regex = new StringBuilder("/");
        int start = 1;
        while (matcher.find()) {
            regex.append(path, start, matcher.start());
            String matched = matcher.group(1);
            extractMatched(regex, matched);
            start = matcher.end();
        }
        if (start < path.length()) {
            regex.append(path, start, path.length());
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
        String pathRegex = ":(.+?\\b)";
        Matcher pathMatcher = Pattern.compile(pathRegex).matcher(path);

        String pathInfoRegex = PathUtils.pathToRegex(path);
        Matcher pathInfoMatcher = Pattern.compile(pathInfoRegex).matcher(pathInfo);

        Map<String, String> matches = new LinkedHashMap<>();
        if (pathInfoMatcher.find()) {
            int i = 0;
            while (i++ < pathInfoMatcher.groupCount()) {
                if (pathInfoMatcher.groupCount() > 1 && pathMatcher.find()) {
                    String group = pathInfoMatcher.group(i);
                    String key = pathMatcher.group(1);
                    matches.put(key, group);
                }
            }
        }
        return matches;
    }

    public static Map<String, List<String>> extractQueryVariables(String queryString) {
        return queryString == null || queryString.trim().isEmpty() ? Collections.emptyMap() :
                Arrays.stream(queryString.split("&")).reduce(new LinkedHashMap<>(), (acc, curr) -> {
                    String[] entry = curr.split("=");
                    if (acc.containsKey(entry[0])) {
                        acc.get(entry[0]).add(entry[0]);
                    } else {
                        List<String> values = new LinkedList<>();
                        values.add(entry[1]);
                        acc.put(entry[0], values);
                    }
                    return acc;
                }, (x, y) -> x);
    }
}
