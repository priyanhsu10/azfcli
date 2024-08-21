package com.server;

import com.server.models.AzfRepoZipRequest;
import com.server.models.AzfServiceGrpc;
import com.server.models.Output;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BuildServiceTest {
    private AzfServiceGrpc.AzfServiceStub azfServiceStub;

    public BuildServiceTest() {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        this.azfServiceStub = AzfServiceGrpc.newStub(managedChannel);
    }

    @BeforeAll
    public void setup() {

    }

    @Test
    public void testupload() throws FileNotFoundException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        FileUploadResponse response = new FileUploadResponse();
        StreamObserver<AzfRepoZipRequest> request = this.azfServiceStub.build(response);

        Path p = Paths.get("/Users/priyanshuparate/projects/java/basics/source/test.zip");
        try (FileChannel inputChannel = FileChannel.open(p, StandardOpenOption.READ)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (inputChannel.read(byteBuffer) > 0) {
                byteBuffer.flip();
                byte[] b = byteBuffer.array();
              AzfRepoZipRequest repoZipRequest = AzfRepoZipRequest.newBuilder()
                        .setId("test")
                        .setContent(com.google.protobuf.ByteString.copyFrom(b))
                        .build();
                request.onNext(repoZipRequest);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.onCompleted();
        latch.await();
    }

}

class FileUploadResponse implements StreamObserver<Output> {
    private final CountDownLatch latch;

    public FileUploadResponse(CountDownLatch latch) {
        this.latch = latch;
    }
    @Override
    public void onNext(Output output) {
        System.out.println(output.getOutput());
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        System.out.println("file uploaded successfully");
        latch.countDown();
    }
}