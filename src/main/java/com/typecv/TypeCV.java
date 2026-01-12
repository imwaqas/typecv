package com.typecv;

import com.typecv.command.NewCommand;
import com.typecv.command.RenderCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "typecv",
    mixinStandardHelpOptions = true,
    version = "TypeCV 1.0.0",
    description = "A Java-based CV/Resume generator using Typst",
    subcommands = {
        NewCommand.class,
        RenderCommand.class
    }
)
public class TypeCV implements Runnable {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new TypeCV()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.println("TypeCV - CV/Resume Generator");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  new <name>       Create a new CV YAML file with sample content");
        System.out.println("  render <file>    Render a CV YAML file to PDF");
        System.out.println();
        System.out.println("Use 'typecv <command> --help' for more information on a command.");
        System.out.println("Use 'typecv --help' for all options.");
    }
}
