package com.server.services;

import com.server.models.AzfRepoZipRequest;
import com.server.models.Output;
import io.grpc.stub.StreamObserver;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BuildObserver implements StreamObserver<AzfRepoZipRequest> {

    StreamObserver<Output> responseObserver;
    private final   String clientId;
   private final String basePath ;
    public BuildObserver(StreamObserver<Output> responseObserver, String clientId, String path) {
        this.responseObserver = responseObserver;
        this.clientId = clientId;
        this.basePath=path+"/"+clientId;
    }

    FileOutputStream fileOutputStream;


    String zipPath = null;

    @Override
    public void onNext(AzfRepoZipRequest azfRepoZipRequest) {

        try {
            if (fileOutputStream == null) {
                File path = Path.of(basePath).toFile();
                if(!path.exists()){
                  path.createNewFile();
                }
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
        responseObserver.onCompleted();
    }

    @Override
    public void onCompleted() {
        try {
            fileOutputStream.close();


           Output o = Output.newBuilder().
                    setOutput("zip  Uploaded and build is starting ").build();
            responseObserver.onNext(o);
       o = Output.newBuilder().
                    setOutput("setup process starting ..... ").build();
            responseObserver.onNext(o);
            prepareEnvironment(responseObserver);

            responseObserver.onCompleted();



        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void prepareEnvironment(StreamObserver<Output> responseObserver) throws IOException, InterruptedException {
        //unzip the zip  ,
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands= new ArrayList<>();
        commands.add("bash")  ;
        commands.add("-c");
        System.out.println("unzip the zip");
        commands.add("unzip "+ zipPath);
        commands.add("cd " +basePath.substring(0,basePath.length()-4));

        basicSetup(commands);
        // setup.sh
        System.out.println("run  setup.sh");
        commands.add("sh "+"setup.sh");
        //run.sh --check
        System.out.println("run.sh --check");
        processBuilder.command(commands);
        Process process = processBuilder.start();

        InputStream inputStream = process.getInputStream();
        // Capture the output in an InputStream
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // Write the output to the file
        String line;
        while ((line = reader.readLine()) != null) {
            responseObserver.onNext(Output.newBuilder().setOutput(line).build());
        }
        inputStream.close();
        int exitCode = process.waitFor();
        responseObserver.onCompleted();

    }

    private static void basicSetup(List<String> commands) {
        System.out.println(" setup environement for eva");
        commands.add("export  VAULT_USERNAME ="+"test");
        commands.add("export  VAULT_PASSWORD ="+"test");
        commands.add("export  VAULT_ADDR ="+"test");
        commands.add("chmod  777 "+"setup.sh");
        commands.add("chmod  777 "+"run.sh");


        // setup environement for eva

        // az login with service principle
        System.out.println("az login with service principle");
        commands.add("az login with service principle");
    }


}
