package works.hop.presso.mkdn;

import org.apache.commons.cli.*;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.servable.IStaticOptionsBuilder;
import works.hop.presso.jett.Espresso;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

public class WebApp {

    public static void main(String[] args) throws ParseException {
        final Options options = new Options();
        System.out.println(args.length);
        Arrays.stream(args).forEach(System.out::println);
        options.addOption("h", "home", true, "home dir path");
        options.addOption("p", "pages", true, "pages folder");
        options.addOption("w", "welcome", true, "html landing page");
        options.addOption("port", true, "listening port");
        options.addOption("host", true, "application host");

        CommandLineParser cmdParser = new DefaultParser();
        CommandLine cmd = cmdParser.parse(options, args);

        String baseDir = cmd.getOptionValue("h", "presso-mkdn/www");
        String pagesDir = cmd.getOptionValue("p", "pages");
        String welcomeFile = cmd.getOptionValue("w", "index.html");
        String portNum = cmd.getOptionValue("port", "9080");
        String hostName = cmd.getOptionValue("host", "localhost");

        IApplication app = Espresso.express();
        app.use("/", IStaticOptionsBuilder.newBuilder()
                .baseDirectory(baseDir)
                .welcomeFiles(welcomeFile)
                .build());
        app.use(Espresso.json());

        app.get("/.*\\.md", (req, res, next) -> {
            String page = req.path();
            Path resource = Path.of(baseDir, pagesDir, page);
            try {
                Parser parser = Parser.builder().build();
                Node document = parser.parse(Files.readString(resource));
                HtmlRenderer renderer = HtmlRenderer.builder().build();
                String markup = renderer.render(document);
                res.send(markup);
            } catch (Exception e) {
                next.error("404", new RuntimeException(e.getMessage()));
            }
        });

        app.get("/.*\\.list", (req, res, next) -> {
            String path = req.path().replace(".list", "");
            File directory = Path.of(baseDir, pagesDir, path).toFile();
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

        int port = Integer.parseInt(portNum);
        app.listen(hostName, port, System.out::println);
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