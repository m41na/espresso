package com.akilisha.espresso.cli;

import lombok.Getter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

@Getter
public class StartUp {

    private static final Object lock = new Object();
    private static StartUp instance;
    private final String defaultPass = "changeit";
    private final CommandLine args;

    private StartUp(CommandLine args) {
        this.args = args;
    }

    public static StartUp load(OptBuilder options, String... args) {
        synchronized (lock) {
            if (instance == null) {
                try {
                    // parse options passed during start
                    CommandLineParser cmdParser = new DefaultParser();
                    CommandLine cmd = cmdParser.parse(options.build(), args);
                    instance = new StartUp(cmd);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return instance;
    }

    public static StartUp instance() {
        // if start up instance is missing, no args will be parsed
        return load(OptBuilder.newBuilder());
    }

    public String cacertsPath() {
        return this.cacertsPath(null, null);
    }

    public String cacertsPath(String exec, String flag) {
        try {
            // git bash on windows - C:\Program Files\Git\bin\bash.exe
            String os = System.getProperty("os.name");
            Process process = exec != null
                    ?
                    new ProcessBuilder(exec, flag, "echo %JAVA_HOME%").start()
                    :
                    os.matches("([Ww])indows.*")
                            ?
                            new ProcessBuilder("cmd.exe", "/C", "echo %JAVA_HOME%").start()
                            :
                            new ProcessBuilder("/bin/bash", "-c", "echo $JAVA_HOME").start();
            try (BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String path = output.readLine();
                File file = Path.of(path, "lib/security/cacerts").toFile();
                return file.getPath();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getOptionValue(String key) {
        return this.getArgs().getOptionValue(key);
    }

    public <T> T getOptionValue(String key, Function<String, T> converter) {
        return this.getOrDefault(key, converter, null);
    }

    public String getOrDefault(String key, String defaultValue) {
        return this.getArgs().getOptionValue(key, defaultValue);
    }

    public <T> T getOrDefault(String key, Function<String, T> converter, T defaultValue) {
        return Optional.ofNullable(this.getOptionValue(key)).map(converter).orElse(defaultValue);
    }

    public String defaultPass() {
        return defaultPass;
    }
}