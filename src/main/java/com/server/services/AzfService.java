package com.server.services;


import com.server.BuildObserver;
import com.server.models.AzfRepoZipRequest;
import com.server.models.AzfServiceGrpc;
import com.server.models.Output;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
@GrpcService
public class AzfService extends AzfServiceGrpc.AzfServiceImplBase {

    @Override
    public StreamObserver<AzfRepoZipRequest> build(StreamObserver<Output> responseObserver) {


       return  new BuildObserver(responseObserver);
    }
}



