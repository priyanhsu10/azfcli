package com.server;


import com.server.models.AzfRepoZipRequest;
import com.server.models.AzfServiceGrpc;
import com.server.models.Output;
import io.grpc.stub.StreamObserver;

public class AzfService extends AzfServiceGrpc.AzfServiceImplBase {

    @Override
    public StreamObserver<AzfRepoZipRequest> build(StreamObserver<Output> responseObserver) {


       return  new BuildObserver(responseObserver);
    }
}



