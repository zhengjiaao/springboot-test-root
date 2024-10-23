package com.zja.pandoc.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-10-22 15:14
 */
public class PandocUtil {
    private static final String PANDOC_PATH_ENV_VAR = "PANDOC_PATH";
    private static final String PANDOC_COMMAND = "pandoc";

    private PandocUtil() {
    }

    private static String getPandocPath() {
        String path = System.getenv(PANDOC_PATH_ENV_VAR);
        if (path == null || path.isEmpty()) {
            path = PANDOC_COMMAND; // Fallback to default command
        }
        return path;
    }

    public static class PandocException extends Exception {
        public PandocException(String message) {
            super(message);
        }

        public PandocException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static String convertFile(String preCommand, String inputFile, String postCommand, String outputFile) throws PandocException {
        String pandocPath = getPandocPath();
        List<String> commandList = new ArrayList<>();
        commandList.add(pandocPath);

        if (preCommand != null && !preCommand.isEmpty()) {
            commandList.add(preCommand);
        }

        if (inputFile != null && !inputFile.isEmpty()) {
            commandList.add(inputFile);
        }

        if (postCommand != null && !postCommand.isEmpty()) {
            commandList.add(postCommand);
        }

        if (outputFile != null && !outputFile.isEmpty()) {
            commandList.add(outputFile);
        }

        String[] command = commandList.toArray(new String[0]);
        return executeCommand(command);
    }

    public static void convertFile(String inputFile, String outputFile) throws PandocException {
        convertFile(null, inputFile, "-o", outputFile);
    }

    public static void convertFileV2(String inputFile, String outputFile) throws PandocException {
        String pandocPath = getPandocPath();
        String[] command = {pandocPath, inputFile, "-o", outputFile};
        executeCommand(command);
    }

    public static String executeCommand(String command) throws PandocException {
        String[] commands = command.split(" ");
        return executeCommand(commands);
    }

    private static String executeCommand(String[] command) throws PandocException {
        try {
            Process process = Runtime.getRuntime().exec(command);
            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new PandocException("Pandoc command failed with exit code " + exitCode + ". Error: " + errorOutput.toString() + ". Command: " + String.join(" ", command));
            }

            return output.toString();
        } catch (Exception e) {
            throw new PandocException("Error executing Pandoc command: " + e.getMessage(), e);
        }
    }

    /*public static void main(String[] args) {
        try {
            String markdown = "D:\\temp\\md\\test.md";
            String outputPath = "D:\\temp\\md\\test_output.html";
            PandocUtil.convertFile(markdown, outputPath);
            // PandocUtil.executeCommand("pandoc " + markdown + " -o " + outputPath);
        } catch (PandocException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }*/
}
