package com.server.services;

import com.server.models.CommandRequest;
import com.server.models.Output;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class CommandRequestObserver implements StreamObserver<CommandRequest> {
    private final CountDownLatch latch;

    public CommandRequestObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(CommandRequest output) {
        System.out.println(output.getCommand());

    }

    @Override
    public void onError(Throwable throwable) {
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("command executed uploaded successfully");
        latch.countDown();
    }
}
