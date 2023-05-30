package com.gyo.recollective.jobster;

import com.gyo.recollective.jobster.model.Job;

import java.util.concurrent.TimeUnit;

/**
 * Wrapper class that encapsulates the <code>Job</code> containing the
 * code to be executed. Facts about this class:
 * - It implements the <code>Runnable</code> interface so that it runs within a Thread.
 */
public class JobRunner implements Runnable {
    private final Job job;

    public JobRunner(Job job) {
        this.job = job;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(job.durationInSeconds());
            System.out.println("\tJob " + job + " completed.");
        } catch (InterruptedException e) {
            System.out.println("INTERRUPTED");
            Thread.currentThread().interrupt();
            throw new RuntimeException("The job was interrupted");
        }
    }

}
