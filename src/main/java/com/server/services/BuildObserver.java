package com.server;

import com.server.models.AzfRepoZipRequest;
import com.server.models.Output;
import io.grpc.stub.StreamObserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class BuildObserver implements StreamObserver<AzfRepoZipRequest> {

    StreamObserver<Output> responseObserver;

    public BuildObserver(StreamObserver<Output> responseObserver) {
        this.responseObserver = responseObserver;
    }

    FileOutputStream fileOutputStream;

    String basePath = "/Users/priyanshuparate/projects/java/basics/dump";
    String zipPath = null;

    @Override
    public void onNext(AzfRepoZipRequest azfRepoZipRequest) {

        try {
            if (fileOutputStream == null) {
                zipPath = basePath + "/" + azfRepoZipRequest.getId()+".zip";
                fileOutputStream = new FileOutputStream(Paths.get(zipPath).toString());
            }
            fileOutputStream.write(azfRepoZipRequest.getContent().toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onCompleted() {
        try {
            fileOutputStream.close();
           Output o = Output.newBuilder().
                    setOutput("zip  Uploaded and build is starting ").build();
            responseObserver.onNext(o);
            responseObserver.onCompleted();

            prepareEnvironment();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void prepareEnvironment() {
        //unzip the zip  ,
        System.out.println("unzip the zip");
        // setup environement for eva
        System.out.println(" setup environement for eva");
        // az login with service principle
        System.out.println("az login with service principle");
        // setup.sh
        System.out.println("run  setup.sh");
        //run.sh --check
        System.out.println("run.sh --check");
        //stream the output from here to response
        System.out.println("stream the output for run.sh from here to response");
    }


}
