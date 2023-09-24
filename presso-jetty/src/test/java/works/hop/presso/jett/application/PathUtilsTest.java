package works.hop.presso.jett.application;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PathUtilsTest {

    @Test
    void convert_path_into_regex_pattern() {
        String path = "/ab*cd/num/ab+cd/:tri/ab?cd/ab(cd)?e";
        String regex = PathUtils.pathToRegex(path);
        assertThat(regex).isEqualTo("/(ab\\w*cd)/num/(ab+cd)/([\\w-]+)/(ab?cd)/(ab(cd)?e)");
    }

    @Test
    void convert_path_into_regex_map() {
        List<String> paths = List.of("/ab*cd/num/ab+cd/tri/:tri/ab?cd/ab(cd)?e");
        Map<String, String> patterns = PathUtils.pathToRegexMap(paths);
        assertThat(patterns).hasSize(1);
        assertThat(patterns.get(paths.get(0))).isEqualTo("/(ab\\w*cd)/num/(ab+cd)/tri/([\\w-]+)/(ab?cd)/(ab(cd)?e)");
    }

    @Test
    void extract_path_variables() {
        String path = "/ab?c/:dev/b+cd/:pro";
        String pathInfo = "/abc/development/bcd/staging";
        Map<String, String> params = PathUtils.extractPathVariables(path, pathInfo);
        Map<String, String> expected = Map.of("dev", "development", "pro", "staging");
        assertThat(params).isEqualTo(expected);
    }
}