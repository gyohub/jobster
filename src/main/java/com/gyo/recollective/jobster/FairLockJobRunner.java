package com.gyo.recollective.jobster;

import com.gyo.recollective.jobster.model.Job;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * EXPERIMENTAL - not yet used.
 * Wrapper class that encapsulates the <code>Job</code> containing the
 * code to be executed. Facts about this class:
 * - It implements the <code>Runnable</code> interface so that it runs within a Thread.
 * - It uses the fair lock mode in order to guarantee every job is executed avoiding starvation.
 *   NOTE: Although fair lock can guarantee the execution it slows down the execution.
 */
public class FairLockJobRunner implements Runnable {
    private static final Lock FAIR_LOCK = new ReentrantLock(true);
    private final Job job;

    public FairLockJobRunner(Job job) {
        this.job = job;
    }

    @Override
    public void run() {
        System.out.println(">> RUNNING JOB " + job.toString());
        try {
            FAIR_LOCK.lock();
            TimeUnit.SECONDS.sleep(job.durationInSeconds());
            System.out.println("\tJob " + job + " completed.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            FAIR_LOCK.unlock();
        }
    }

}
