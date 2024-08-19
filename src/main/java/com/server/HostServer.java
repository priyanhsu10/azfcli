package com.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class HostServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(5001)
                .addService(new AzfService())
                .build();
        server.start();
        System.out.println("server started at 5001");
        server.awaitTermination();
    }
}
