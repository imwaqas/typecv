package com.typecv.command;

import com.typecv.model.CV;
import com.typecv.renderer.TemplateEngine;
import com.typecv.renderer.TypstRunner;
import com.typecv.util.YamlParser;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(
    name = "render",
    description = "Render a CV YAML file to PDF"
)
public class RenderCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "Path to the CV YAML file")
    private File yamlFile;

    @Override
    public Integer call() {
        if (!yamlFile.exists()) {
            System.err.println("Error: File not found: " + yamlFile.getAbsolutePath());
            return 1;
        }

        if (!yamlFile.getName().endsWith(".yaml") && !yamlFile.getName().endsWith(".yml")) {
            System.err.println("Error: File must be a YAML file (.yaml or .yml)");
            return 1;
        }

        try {
            // Parse the YAML file
            System.out.println("Reading: " + yamlFile.getName());
            YamlParser parser = new YamlParser();
            CV cv = parser.parse(yamlFile);
            
            // Render to Typst
            System.out.println("Generating Typst...");
            TemplateEngine engine = new TemplateEngine();
            String typstContent = engine.render(cv);
            
            // Write the .typ file
            String baseName = yamlFile.getName().replaceAll("\\.(yaml|yml)$", "");
            Path typstFile = Path.of(baseName + ".typ");
            Files.writeString(typstFile, typstContent, StandardCharsets.UTF_8);
            System.out.println("Created: " + typstFile.getFileName());
            
            // Compile to PDF
            System.out.println("Compiling PDF...");
            Path pdfFile = Path.of(baseName + ".pdf");
            
            TypstRunner runner = new TypstRunner();
            runner.initialize();
            runner.compile(typstFile, pdfFile);
            
            System.out.println("Created: " + pdfFile.getFileName());
            System.out.println();
            System.out.println("Done! Your CV is ready: " + pdfFile);
            
            // Optionally delete the intermediate .typ file
            // Files.deleteIfExists(typstFile);
            
            return 0;
            
        } catch (Exception e) {
            System.err.println("Error rendering CV: " + e.getMessage());
            if (System.getProperty("typecv.debug") != null) {
                e.printStackTrace();
            }
            return 1;
        }
    }
}
