package com.example.media;

import java.util.List;

public class JobDispatcher implements Runnable {
    private final JobDAO jobDao;

    public JobDispatcher(JobDAO jobDao) {
        this.jobDao = jobDao;
    }

    @Override
    public void run() {
        while (true) {
            try {
                List<Job> pendingJobs = jobDao.findByStatus("pending");

                for (Job job : pendingJobs) {
                    jobDao.updateStatus(job.id(), "processing");
                    JobOrchestrator.pool.submit(new EncoderWorker(job, jobDao));
                }

                Thread.sleep(2000);  // poll every 2 seconds
            } catch (Exception e) {
                Logger.log("Dispatcher error: " + e.getMessage());
            }
        }
    }
}
