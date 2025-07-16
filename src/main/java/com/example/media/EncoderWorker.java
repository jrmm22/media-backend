
package com.example.media;

import java.util.concurrent.ThreadLocalRandom;

public class EncoderWorker implements Runnable {
    private final Job job;
	private final JobDAO jobDao;

    public EncoderWorker(Job job, JobDAO jobDao) {
        this.job = job;
		this.jobDao = jobDao;
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
			jobDao.updateStatus(job.id(), "done");
        } catch (Exception e) {
            Logger.log("Job failed: " + job.id + " reason: " + e.getMessage());
            Metrics.increment("encoder.jobs.failed");
			jobDao.updateStatus(job.id(), "failed");
        }
    }
}
