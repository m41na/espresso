package works.hop.presso.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class OptBuilder {

    private final Options options;

    private OptBuilder() {
        this.options = new Options();
        // add default espresso server
        this.add("keystorePass", true, "keystore password", false);
        this.add("keystorePath", true, "path to key store", false);
        this.add("securePort", true, "secure listening port", false);
        this.add("redirectSecure", true, "redirect from http to https", false);
        this.add("port", true, "listening port");
        this.add("host", true, "application host");
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
            this.options.addOption(new Option(opt, hasArgs, description));
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