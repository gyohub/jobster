package com.gyo.recollective.jobster;

import com.gyo.recollective.jobster.model.Job;

import java.util.concurrent.CompletableFuture;

/**
 * Wrapper class that demonstrates how a job runner can become retriable.
 * Please Note: IRL the retry mechanism should be managed by the job enqueuer.
 */
public class RetriableJobRunner implements Runnable {
    private final Job job;
    private final int maxRetries;
    private int retryCount;

    public RetriableJobRunner(Job job, int maxRetries) {
        this.job = job;
        this.maxRetries = maxRetries;
    }

    @Override
    public void run() {
        try {
            System.out.println("Processing job with priority: " + job.priority());
            Thread.sleep(job.durationInSeconds());
            System.out.println("Job with priority " + job.priority() + " completed.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            // Job failed, retry if maximum retries not reached
            if (retryCount < maxRetries) {
                retryCount++;
                System.err.println("Job with priority " + job.priority() + " failed. Retrying... (" + retryCount + "/" + maxRetries + ")");
                CompletableFuture.runAsync(this);
            } else {
                System.err.println("Job with priority " + job.priority() + " failed after " + maxRetries + " retries.");
            }
        }
    }
}
