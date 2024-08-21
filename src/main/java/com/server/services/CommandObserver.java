package com.server.services;

import com.server.models.CommandRequest;
import com.server.models.Output;
import io.grpc.stub.StreamObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandObserver implements StreamObserver<CommandRequest> {
    private final CommandRequest commandRequest;
    private final StreamObserver<Output> observer;


    public CommandObserver(CommandRequest commandRequest, StreamObserver<Output> observer,String clientId) {
        this.commandRequest = commandRequest;
        this.observer = observer;

    }

    public void execute() {
        try {
            // Start the process
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", commandRequest.getCommand());
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            // Capture the output in an InputStream
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Write the output to the file
            String line;
            while ((line = reader.readLine()) != null) {
                observer.onNext(Output.newBuilder().setOutput(line).build());
            }
            inputStream.close();
            int exitCode = process.waitFor();
            // Close resources
            observer.onCompleted();

        } catch (IOException e) {
            e.printStackTrace();
            observer.onNext(Output.newBuilder().setOutput(e.getLocalizedMessage()).build());
            observer.onCompleted();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onNext(CommandRequest commandRequest) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
