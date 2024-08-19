package com.server;

import com.server.models.AzfServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CommandClient {
    public static void main(String[] args) {
         AzfServiceGrpc.AzfServiceStub azfServiceStub;
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress("localhost", 5001)
                .usePlaintext()
                .build();
      azfServiceStub = AzfServiceGrpc.newStub(managedChannel);




    }
}
