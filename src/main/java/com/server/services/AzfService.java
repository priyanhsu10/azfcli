package com.server.services;


import com.server.models.AzfRepoZipRequest;
import com.server.models.AzfServiceGrpc;
import com.server.models.CommandRequest;
import com.server.models.Output;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@GrpcService
public class AzfService extends AzfServiceGrpc.AzfServiceImplBase {

@Value("${dump.basePath}")
private String path;

    @Override
    public StreamObserver<AzfRepoZipRequest> build(StreamObserver<Output> responseObserver) {
        Metadata metadata = MetadataInterceptor.METADATA_CONTEXT_KEY.get();
        Metadata.Key<String> clientIdKey = Metadata.Key.of("client-id", Metadata.ASCII_STRING_MARSHALLER);
        String clientId = metadata.get(clientIdKey);
        clientId=clientId.replaceAll(".","_");
        System.out.println(clientId);

       return  new BuildObserver(responseObserver,clientId,path);
    }

    @Override
    public void execute(CommandRequest request, StreamObserver<Output> responseObserver) {

        Metadata metadata = MetadataInterceptor.METADATA_CONTEXT_KEY.get();
        Metadata.Key<String> clientIdKey = Metadata.Key.of("client-id", Metadata.ASCII_STRING_MARSHALLER);
        String clientId = metadata.get(clientIdKey);
        System.out.println(clientId);

        new CommandObserver(request,responseObserver,clientId).execute();
    }
}

@Component
@GrpcGlobalServerInterceptor
class MetadataInterceptor implements ServerInterceptor {

    public static final Context.Key<Metadata> METADATA_CONTEXT_KEY = Context.key("metadata");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        // Store the Metadata in the context
        Context context = Context.current().withValue(METADATA_CONTEXT_KEY, headers);

        // Proceed with the call using the new context
        return Contexts.interceptCall(context, call, headers, next);
    }
}



