//package com.server;
//
//import com.server.models.AzfServiceGrpc;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//import java.io.*;
//
//public class CommandClient {
//    public static void main(String[] args) {
//         AzfServiceGrpc.AzfServiceStub azfServiceStub;
//        ManagedChannel managedChannel = ManagedChannelBuilder
//                .forAddress("localhost", 5001)
//                .usePlaintext()
//                .build();
//      azfServiceStub = AzfServiceGrpc.newStub(managedChannel);
//
//
//
//
//    }
//}
//class GitStatusChecker {
//    public static void main(String[] args) {
//        // Specify the directory where the git repository is located
//        String repoPath = "/Users/priyanshuparate/projects/java/basics/basics";
//
//        try {
//            List<String> modifiedFiles = new ArrayList<>();
//            List<String> addedFiles = new ArrayList<>();
//            List<String> deletedFiles = new ArrayList<>();
//
//            // Execute git status command
//            ProcessBuilder processBuilder = new ProcessBuilder();
//            processBuilder.command("git", "status", "--porcelain");
//            processBuilder.directory(new File(repoPath));
//
//            Process process = processBuilder.start();
//
//            // Capture the output of the command
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Parse the output to find modified, added, and deleted files
//                if (line.startsWith(" M ") ||line.startsWith("AM")) {
//                    modifiedFiles.add(line.substring(3));
//                } else if (line.startsWith("A ")) {
//                    addedFiles.add(line.substring(2));
//                } else if (line.startsWith(" D ")) {
//                    deletedFiles.add(line.substring(3));
//                }
//                System.out.println(line);
//            }
//
//            int exitCode = process.waitFor();
//            if (exitCode == 0) {
//                // Print the results
//                System.out.println("Modified files:");
//                for (String file : modifiedFiles) {
//                    System.out.println(file);
//                }
//
//                System.out.println("Added files:");
//                for (String file : addedFiles) {
//                    System.out.println(file);
//                }
//
//                System.out.println("Deleted files:");
//                for (String file : deletedFiles) {
//                    System.out.println(file);
//                }
//            } else {
//                System.err.println("Error executing git status command. Exit code: " + exitCode);
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}
//class ExecuteLinuxCommand {
//    public static void main(String[] args) {
//        // Command to be executed
//        String command = "ls -l";
//
//        // File to which the output will be written
//        File outputFile = new File("output.txt");
//
//        // Use ProcessBuilder to execute the command
//        ProcessBuilder processBuilder = new ProcessBuilder();
//        processBuilder.command("bash", "-c", command);
//
//        try {
//            // Start the process
//            Process process = processBuilder.start();
//
//            // Capture the output in an InputStream
//            InputStream inputStream = process.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            // Write the output to the file
//            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                writer.write(line);
//                writer.newLine();
//            }
//
//            // Close resources
//            writer.close();
//            reader.close();
//
//            // Wait for the process to complete
//            int exitCode = process.waitFor();
//            System.out.println("Exited with code: " + exitCode);
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}