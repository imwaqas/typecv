package com.typecv.renderer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Handles extraction and execution of the bundled Typst binary.
 */
public class TypstRunner {
    
    private static final String TYPST_VERSION = "0.12.0";
    private static final String APP_NAME = "typecv";
    
    private Path typstBinary;
    
    /**
     * Initialize the Typst runner, extracting the binary if necessary.
     */
    public void initialize() throws IOException {
        typstBinary = getOrExtractTypst();
    }
    
    /**
     * Compile a Typst file to PDF.
     * 
     * @param typstFile Path to the .typ file
     * @param outputPdf Path to the output PDF file
     * @throws IOException if compilation fails
     */
    public void compile(Path typstFile, Path outputPdf) throws IOException, InterruptedException {
        if (typstBinary == null) {
            initialize();
        }
        
        ProcessBuilder pb = new ProcessBuilder(
            typstBinary.toString(),
            "compile",
            typstFile.toString(),
            outputPdf.toString()
        );
        
        pb.inheritIO();
        Process process = pb.start();
        
        boolean finished = process.waitFor(120, TimeUnit.SECONDS);
        
        if (!finished) {
            process.destroyForcibly();
            throw new IOException("Typst compilation timed out after 120 seconds");
        }
        
        if (process.exitValue() != 0) {
            throw new IOException("Typst compilation failed with exit code: " + process.exitValue());
        }
    }
    
    /**
     * Get the path to the Typst binary, extracting it if necessary.
     */
    private Path getOrExtractTypst() throws IOException {
        Path appDataDir = getAppDataDir();
        Path typstDir = appDataDir.resolve("typst-" + TYPST_VERSION);
        Path binary = typstDir.resolve(getTypstBinaryName());
        
        if (Files.exists(binary)) {
            return binary;
        }
        
        // Create directory
        Files.createDirectories(typstDir);
        
        // Try to extract bundled binary
        String resourcePath = "typst/" + getTypstResourceName();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is != null) {
                Files.copy(is, binary, StandardCopyOption.REPLACE_EXISTING);
                makeExecutable(binary);
                return binary;
            }
        }
        
        // If bundled binary not found, try to find system typst
        Path systemTypst = findSystemTypst();
        if (systemTypst != null) {
            return systemTypst;
        }
        
        throw new IOException(
            "Typst binary not found. Please install Typst (https://typst.app) or ensure " +
            "the bundled binary is available at: " + resourcePath
        );
    }
    
    /**
     * Find Typst in the system PATH.
     */
    private Path findSystemTypst() {
        String[] commands = isWindows() 
            ? new String[]{"where", "typst"}
            : new String[]{"which", "typst"};
        
        try {
            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            String output = new String(process.getInputStream().readAllBytes()).trim();
            boolean finished = process.waitFor(5, TimeUnit.SECONDS);
            
            if (finished && process.exitValue() == 0 && !output.isEmpty()) {
                // Get the first line (in case there are multiple results)
                String firstLine = output.split("\\r?\\n")[0].trim();
                Path typstPath = Path.of(firstLine);
                if (Files.exists(typstPath)) {
                    return typstPath;
                }
            }
        } catch (Exception e) {
            // Ignore and return null
        }
        
        return null;
    }
    
    /**
     * Get the application data directory for storing extracted binaries.
     */
    private Path getAppDataDir() {
        String os = System.getProperty("os.name").toLowerCase();
        String home = System.getProperty("user.home");
        
        if (os.contains("win")) {
            String appData = System.getenv("LOCALAPPDATA");
            if (appData != null) {
                return Path.of(appData, APP_NAME);
            }
            return Path.of(home, "AppData", "Local", APP_NAME);
        } else if (os.contains("mac")) {
            return Path.of(home, "Library", "Application Support", APP_NAME);
        } else {
            // Linux and others
            String xdgData = System.getenv("XDG_DATA_HOME");
            if (xdgData != null) {
                return Path.of(xdgData, APP_NAME);
            }
            return Path.of(home, ".local", "share", APP_NAME);
        }
    }
    
    /**
     * Get the name of the Typst binary for the current platform.
     */
    private String getTypstBinaryName() {
        if (isWindows()) {
            return "typst.exe";
        }
        return "typst";
    }
    
    /**
     * Get the resource name of the Typst binary for the current platform.
     */
    private String getTypstResourceName() {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        
        if (os.contains("win")) {
            return "typst-x86_64-pc-windows-msvc.exe";
        } else if (os.contains("mac")) {
            if (arch.contains("aarch64") || arch.contains("arm")) {
                return "typst-aarch64-apple-darwin";
            }
            return "typst-x86_64-apple-darwin";
        } else {
            // Linux
            if (arch.contains("aarch64") || arch.contains("arm")) {
                return "typst-aarch64-unknown-linux-gnu";
            }
            return "typst-x86_64-unknown-linux-gnu";
        }
    }
    
    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
    
    /**
     * Make a file executable on Unix-like systems.
     */
    private void makeExecutable(Path path) {
        try {
            if (!isWindows()) {
                Set<PosixFilePermission> perms = Files.getPosixFilePermissions(path);
                perms.add(PosixFilePermission.OWNER_EXECUTE);
                perms.add(PosixFilePermission.GROUP_EXECUTE);
                perms.add(PosixFilePermission.OTHERS_EXECUTE);
                Files.setPosixFilePermissions(path, perms);
            }
        } catch (Exception e) {
            // Ignore on Windows or if POSIX permissions not supported
        }
    }
}
