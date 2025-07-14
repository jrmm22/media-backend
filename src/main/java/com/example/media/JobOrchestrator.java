
package com.example.media;

import java.util.concurrent.*;
import java.io.IOException;

public class JobOrchestrator {
    public static final ExecutorService pool = Executors.newFixedThreadPool(100);

    public static void main(String[] args) throws InterruptedException, IOException  {

        ApiServer.start();

        /*for (int i = 0; i < 20; i++) {
            Job job = new Job("job-" + i, "raw_" + i + ".mp4");
            pool.submit(new EncoderWorker(job));
        }*/

        //pool.shutdown();
        //pool.awaitTermination(600, TimeUnit.SECONDS);

        //System.out.println("\n=== METRICS ===");
        //System.out.println(Metrics.snapshot());
    }
}
