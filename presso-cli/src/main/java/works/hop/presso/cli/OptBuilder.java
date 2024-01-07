package works.hop.presso.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class OptBuilder {

    private final Options options;

    private OptBuilder() {
        this.options = new Options();
        // add default Espresso server
        this.add("port", true, "listening port");
        this.add("host", true, "application host");
        this.add("securePort", true, "secure listening port", false);
        this.add("redirectSecure", true, "redirect from http to https", false);
        this.add("deployEnv", true, "deployment environment (dev, prod, test, int, stage)", false);

        // keystore
        this.add("keystorePass", true, "keystore password", false);
        this.add("keystorePath", true, "path to key store", false);

        // plugins
        this.add("pluginsHome", true, "plugins home directory", false);
        this.add("viewEngines", true, "view engine plugins sub-directory", false);
        this.add("bodyParsers", true, "body parser plugins sub-directory", false);
        this.add("routerHandles", true, "router handle plugins sub-directory", false);
        this.add("ctxExtensions", true, "context extension plugins sub-directory", false);
        this.add("watch", false, "watching plugins dir for changes", false);

        // static resources
        this.add("resourcesCtx", true, "context path to use for looking up static files", false);
        this.add("baseDirectory", true, "base directory for static files", false);
        this.add("welcomeFiles", true, "comma-separated names of welcome files", false);
        this.add("acceptRanges", true, "accept ranges when looking up resources", false);
        this.add("listDirectories", true, "list directory content when a folder is reached", false);
    }

    public static OptBuilder newBuilder() {
        return new OptBuilder();
    }

    public OptBuilder add(String opt, String description) {
        this.options.addOption(new Option(opt, description));
        return this;
    }

    public OptBuilder add(String opt, boolean hasArgs, String description) {
        this.options.addOption(new Option(opt, hasArgs, description));
        return this;
    }

    public OptBuilder add(String opt, boolean hasArgs, String description, boolean required) {
        if (required) {
            this.options.addRequiredOption(opt, opt, hasArgs, description);
            return this;
        }
        return this.add(opt, hasArgs, description);
    }

    public OptBuilder add(String opt, String longOpt, boolean hasArgs, String description) {
        this.options.addOption(new Option(opt, longOpt, hasArgs, description));
        return this;
    }

    public OptBuilder add(String opt, String longOpt, boolean hasArgs, String description, boolean required) {
        if (required) {
            this.options.addRequiredOption(opt, longOpt, hasArgs, description);
            return this;
        }
        return this.add(opt, longOpt, hasArgs, description);
    }

    public Options build() {
        return options;
    }
}
