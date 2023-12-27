package works.hop.presso.mkdn;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.servable.IStaticOptionsBuilder;
import works.hop.presso.cli.OptBuilder;
import works.hop.presso.cli.StartUp;
import works.hop.presso.jett.Espresso;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class WebApp {

    public static void main(String[] args) {
        final OptBuilder options = OptBuilder.newBuilder();
        System.out.println(args.length);
        options.add("d", "drive", true, "drive name for home dir");
        options.add("h", "home", true, "home dir path");
        options.add("p", "pages", true, "pages folder");
        options.add("w", "welcome", true, "html landing page");
        StartUp props = StartUp.load(options, args);

        String dirDrive = props.getOptionValue("d");
        String baseDir = props.getOrDefault("h", "U:\\11ty-site\\public");
        String pagesDir = props.getOrDefault("p", "posts");
        String welcomeFile = props.getOrDefault("w", "index.html");
        int httpPort = props.getOrDefault("port", 9080, Integer::parseInt);
        String hostName = props.getOrDefault("host", "localhost");

        IApplication app = Espresso.express();
        app.use("/", IStaticOptionsBuilder.newBuilder()
                .baseDirectory(baseDir)
                .welcomeFiles(welcomeFile)
                .build());
        app.use(Espresso.json());

        app.get("/.*\\.md", (req, res, next) -> {
            String page = req.path();
            Path resourcePath = dirDrive != null
                    ? Path.of(dirDrive, baseDir, pagesDir, page)
                    : Path.of(baseDir, pagesDir, page);
            try {
                Parser parser = Parser.builder().build();
                Node document = parser.parse(Files.readString(resourcePath));
                HtmlRenderer renderer = HtmlRenderer.builder().build();
                String markup = renderer.render(document);
                res.send(markup);
            } catch (Exception e) {
                next.error("404", new RuntimeException(e.getMessage()));
            }
        });

        app.get("/.*\\.list", (req, res, next) -> {
            String path = req.path().replace(".list", "");
            Path resourcePath = dirDrive != null
                    ? Path.of(dirDrive, baseDir, pagesDir, path)
                    : Path.of(baseDir, pagesDir, path);
            File directory = resourcePath.toFile();
            try {
                Map<String, List<Object>> listing = new LinkedHashMap<>();
                listFiles(
                        directory,
                        listing,
                        filePath -> filePath.substring(filePath.indexOf(Path.of(pagesDir).toString())));
                res.json(listing);
            } catch (Exception e) {
                next.error("404", new RuntimeException(e.getMessage()));
            }
        });

        app.listen(hostName, httpPort, System.out::println);
    }

    static void listFiles(File directory, Map<String, List<Object>> listing, Function<String, String> process) {
        if (directory.isDirectory() && !directory.isHidden()) {
            listing.put(directory.getName(), new LinkedList<>());
            File[] list = directory.listFiles();
            if (list != null) {
                for (File file : list) {
                    if (file.isFile()) {
                        listing.get(directory.getName()).add(process.apply(file.getAbsolutePath()));
                    } else if (file.isDirectory()) {
                        Map<String, List<Object>> sublist = new LinkedHashMap<>();
                        listFiles(file, sublist, process);
                        listing.get(directory.getName()).add(sublist);
                    }
                }
            }
        }
    }
}