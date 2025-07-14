
package com.example.media;

import java.util.concurrent.ThreadLocalRandom;

public class EncoderWorker implements Runnable {
    private final Job job;

    public EncoderWorker(Job job) {
        this.job = job;
    }

    @Override
    public void run() {
        Logger.log("Starting job: " + job.id + " input: " + job.input);
        Metrics.increment("encoder.jobs.total");

        try {
            int n = ThreadLocalRandom.current().nextInt(5, 61);
            int failure = ThreadLocalRandom.current().nextInt(1, 100);

            if( failure <= 5 )
            {
                throw new RuntimeException("Simulated failure: " + failure);
            }
            ProcessBuilder pb = new ProcessBuilder(
                "bash", "-c",
                "echo encoding " + job.input + " && sleep " + n + " && touch " + job.output
            );
            pb.inheritIO(); // So output is visible

            Process p = pb.start();
            int exitCode = p.waitFor();

            if (exitCode == 0) {
                Logger.log(" Job " + job.id + " finished: " + job.output);
                Metrics.increment("encoder.jobs.success");
            } else {
                Logger.log(" Job " + job.id + " failed with code: " + exitCode);
                Metrics.increment("encoder.jobs.failed");
            }
        } catch (Exception e) {
            Logger.log("Job failed: " + job.id + " reason: " + e.getMessage());
            Metrics.increment("encoder.jobs.failed");
        }
    }
}
