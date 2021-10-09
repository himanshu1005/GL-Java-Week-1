package com.greatlearning.forkjoin;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0){
            System.out.println("Please configure urls in program arguments"); //e.g. https://economictimes.indiatimes.com/ https://www.ft.com/ https://www.moneycontrol.com/
            System.exit(0);
        }else {
            ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
            UrlConnectionReader urlConnectionReader = new UrlConnectionReader(args);
            String output = forkJoinPool.invoke(urlConnectionReader);
//             printing the output received after the process of Tasks
            System.out.println("Url Output : \n" + output);
//            shutdown to clear pool connections
            forkJoinPool.shutdown();
        }
    };
}
