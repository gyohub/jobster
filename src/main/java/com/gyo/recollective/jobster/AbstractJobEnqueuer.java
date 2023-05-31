package com.gyo.recollective.jobster;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Abstract class that contains boilerplate code for the <code>JobEnqueue</code> implementations.
 * This might look like early optimization, but it's just to demonstrate some applications of DRY principle.
 */
public abstract class AbstractJobEnqueuer implements JobEnqueuer {

    /**
     * This code is just to demonstrate how the job enqueuer should be shut down.
     * However, it's incomplete because it should offer 2 types of termination:
     * 1. Graceful - Waits for all the jobs to complete
     * 2. Forceful - Shuts down immediately interrupting any running jobs.
     * Also, the job enqueuer implementation must be capable of awaiting for all the jobs before 
     * shutting itself down.
     */
    @Override
    public void shutdown() {
        System.out.println("Shutting Jobster down");
        getExecutorService().shutdown();
        try {
            /*
             * TODO: awaitTerminationInSeconds may be converted into an object for flexibility e.g.
             * <code>record WaitTime (int value,  TimeUnit unit){}</code>
             */
            if (!getExecutorService().awaitTermination(5, SECONDS)) {
                getExecutorService().shutdownNow();
                if (!getExecutorService().awaitTermination(5, SECONDS)) {
                    System.err.println("Unable to shutdown job processing system.");
                }
            }
        } catch (InterruptedException e) {
            getExecutorService().shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    abstract ExecutorService getExecutorService();
}
