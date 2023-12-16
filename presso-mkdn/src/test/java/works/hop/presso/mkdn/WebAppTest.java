package works.hop.presso.mkdn;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WebAppTest {

    @Test
    void listFiles() {
        String dirPath = "build/reports";
        File reportsDir = new File(dirPath);
        Map<String, List<Object>> listing = new LinkedHashMap<>();
        WebApp.listFiles(reportsDir, listing, name -> name.substring(name.indexOf("build")));
        assertThat(listing).hasSize(1);
    }
}