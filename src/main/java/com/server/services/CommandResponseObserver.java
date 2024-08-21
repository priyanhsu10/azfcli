package com.server.services;

import com.server.models.CommandRequest;
import com.server.models.Output;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class CommandResponseObserver implements StreamObserver<Output> {
    private final CountDownLatch latch;

    public CommandResponseObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(Output output) {
        System.out.println(output.getOutput());
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
