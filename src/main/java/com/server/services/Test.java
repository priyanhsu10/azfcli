package com.server.services;

import com.server.models.AzfServiceGrpc;
import com.server.models.CommandRequest;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.StreamObserver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class Test {

        public static void main(String[] args) throws UnknownHostException {

            ManagedChannel managedChannel = ManagedChannelBuilder
                    .forAddress("localhost", 9090)
                    .usePlaintext()
                    .build();
            AzfServiceGrpc.AzfServiceStub azfServiceStub= AzfServiceGrpc.newStub(managedChannel);
            Metadata metadata=getMetadata();
            ClientInterceptor clientInterceptor = MetadataUtils.newAttachHeadersInterceptor(metadata);
            azfServiceStub = azfServiceStub.withInterceptors(clientInterceptor);
            Scanner scanner = new Scanner(System.in);
            String input;


            System.out.println("Welcome to the SimpleREPL. Type 'exit' to quit.");

            // The REPL loop
            while (true) {

                System.out.print("azf#:> ");
                input = scanner.nextLine();

                // Check if the user wants to exit
                if ("exit".equalsIgnoreCase(input.trim())) {
                    System.out.println("Goodbye!");
                    break;
                }

                // Evaluate the command and print the result
                try {
                   evaluateCommand(input,azfServiceStub);
                  //  System.out.println(result);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            scanner.close();
        }

    private static Metadata getMetadata() throws UnknownHostException {
        Metadata metadata = new Metadata();
        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("Local IP Address: " + inetAddress.getHostAddress());
        String clientId =inetAddress.getHostAddress();
        Metadata.Key<String> clientIdKey = Metadata.Key.of("client-id", Metadata.ASCII_STRING_MARSHALLER);
        metadata.put(clientIdKey, clientId);
        return metadata;
    }

    // A simple evaluator that handles basic commands
        private static void evaluateCommand(String command, AzfServiceGrpc.AzfServiceStub azfServiceStub) throws Exception {
            command = command.trim().toLowerCase();

            // Add logic for each command
            if(command.equals("help")) {
                System.out.println("Available commands: help, hello, exit");
            }
            CountDownLatch latch = new CountDownLatch(1);
            CommandResponseObserver responseObserver = new CommandResponseObserver(latch);
            azfServiceStub.execute(CommandRequest.newBuilder().setCommand(command).build(),responseObserver);
            latch.await();
            }

}
