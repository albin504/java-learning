package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, ExecutionException, InterruptedException {
	    // write your code here
        InputStream input = Main.class.getResourceAsStream("/input.txt");
        Scanner in = new Scanner(input);
        String directory = in.nextLine();
        String keyword = in.nextLine();
        int n = Integer.parseInt(args[0]);
//        ExecutorService pool = Executors.newCachedThreadPool();
        ExecutorService pool = Executors.newFixedThreadPool(n);
        MatchCounter counter = new MatchCounter(new File(directory),keyword,pool);
        Future<Integer> result =  pool.submit(counter);
        System.out.println(result.get() + " matching files");
        pool.shutdown();
        int size = ((ThreadPoolExecutor)pool).getLargestPoolSize();
        System.out.println("size: " + size);
    }
}

class MatchCounter implements Callable<Integer>
{
    public File directory;
    public String keyword;
    public ExecutorService pool;
    public MatchCounter(File directory, String keyword, ExecutorService pool) {
        this.directory = directory;
        this.keyword = keyword;
        this.pool = pool;
    }

    @Override
    public Integer call() throws Exception {
        int count = 0;
        List<Future<Integer>> results = new ArrayList<>();
        if (directory.isDirectory())
        {
            File[] files = directory.listFiles();
            for (File file : files) {
                MatchCounter counter = new MatchCounter(file,keyword,pool);
                Future<Integer> result =  pool.submit(counter);
                results.add(result);
            }
        } else {
            if (search(directory)) {
                System.out.println("found in :" + directory);
                count++;
            }
        }
        for (Future<Integer> result : results) {
            count += result.get();
        }

        return  count;
    }

    public  boolean search(File file) throws FileNotFoundException {
        System.out.println("file: " + file);
        Scanner in = new Scanner(file, "UTF-8");
        boolean found = false;
        while ( !found && in.hasNextLine())
        {
            String line = in.nextLine();
//            System.out.println(line);
            if (line.contains(keyword)) {
                found = true;
            }
        }
        return  found;
    }
}
