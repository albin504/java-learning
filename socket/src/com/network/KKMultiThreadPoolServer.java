package com.network;

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class KKMultiThreadPoolServer {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java KKMultiServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        ExecutorService executor = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                System.out.println("pool size" + ((ThreadPoolExecutor)executor).getPoolSize());
                System.out.println("active count" + ((ThreadPoolExecutor)executor).getActiveCount());
                executor.submit(new KKMultiServerRunnable(serverSocket.accept()));
//                new KKMultiServerThread(serverSocket.accept()).start();
            }
            executor.shutdown();
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}