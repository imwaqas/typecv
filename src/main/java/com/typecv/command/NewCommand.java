package com.typecv.command;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Command(
    name = "new",
    description = "Create a new CV YAML file with sample content"
)
public class NewCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "Your full name (e.g., \"John Doe\")")
    private String fullName;

    @Override
    public Integer call() {
        // Convert name to filename format: "John Doe" -> "John_Doe_CV.yaml"
        String fileName = fullName.replace(" ", "_") + "_CV.yaml";
        Path outputPath = Path.of(fileName);
        
        // Check if file already exists
        if (Files.exists(outputPath)) {
            System.err.println("Error: File already exists: " + fileName);
            System.err.println("Please delete or rename the existing file first.");
            return 1;
        }
        
        try {
            // Load the sample template from resources
            String template = loadSampleTemplate();
            
            // Replace the name placeholder
            String content = template.replace("name: John Doe", "name: " + fullName);
            
            // Write to file
            Files.writeString(outputPath, content, StandardCharsets.UTF_8);
            
            System.out.println("Created: " + fileName);
            System.out.println();
            System.out.println("Next steps:");
            System.out.println("  1. Edit " + fileName + " with your information");
            System.out.println("  2. Run: typecv render " + fileName);
            
            return 0;
            
        } catch (IOException e) {
            System.err.println("Error creating CV file: " + e.getMessage());
            return 1;
        }
    }
    
    private String loadSampleTemplate() throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("sample_content.yaml")) {
            if (is == null) {
                throw new IOException("Could not find sample_content.yaml in resources");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }
}
