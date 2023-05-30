package com.gyo.recollective.jobster;

import com.gyo.recollective.jobster.model.Job;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Default implementation of the <code>JobEnqueuer</code> via extending <code>AbstractJobEnqueuer</code> class.
 * It will submit the jobs without managing their priorities.
 */
public class DefaultJobEnqueuer extends AbstractJobEnqueuer {
    private ExecutorService executorService;
    private final int poolSize;

    public DefaultJobEnqueuer(int poolSize) {
        this.poolSize = poolSize;
    }

    @Override
    public void enqueue(Job job) {
        // TODO: if executorService is not initialized then call start()
        executorService.submit(new JobRunner(job));
    }

    @Override
    public void start() {
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    @Override
    ExecutorService getExecutorService() {
        return executorService;
    }
}
