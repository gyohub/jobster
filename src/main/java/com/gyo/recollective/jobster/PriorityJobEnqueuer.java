package com.gyo.recollective.jobster;

import com.gyo.recollective.jobster.model.Job;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Job enqueuer implementation that manages execution priority through priority queues.
 * It keeps a <code>ExecutorService</code> running and serving as a poller for the priority queue.
 * Any jobs that are enqueued will be immediately picked and submitted for execution.
 */
public class PriorityJobEnqueuer extends AbstractJobEnqueuer {
    private final Integer poolSize;
    private final Integer queueSize;
    private ExecutorService priorityJobExecutorService;
    private ExecutorService priorityJobScheduler;
    private PriorityBlockingQueue<Job> priorityQueue;

    public PriorityJobEnqueuer(Integer poolSize, Integer queueSize) {
        this.poolSize = poolSize;
        this.queueSize = queueSize;
    }

    @Override
    public void enqueue(Job job) {
        System.out.println(">> ENQUEUEING " + job.toString());
        priorityQueue.add(job);
    }

    @Override
    public void start() {
        priorityJobScheduler = Executors.newSingleThreadExecutor();
        priorityJobExecutorService = Executors.newFixedThreadPool(poolSize);
        priorityQueue = new PriorityBlockingQueue<>(
                queueSize,
                Comparator.comparing(Job::priority)
        );
        priorityJobScheduler.execute(() -> {
            while (true) {
                try {
                    // SLEEP HERE IN ORDER TO ALLOW FOR MULTIPLE JOBS TO BE ENQUEUED BEFORE EXECUTION SO THAT
                    // WE CAN SEE THE PRIORITIZED EXECUTION WORKING.
                    TimeUnit.SECONDS.sleep(1);
                    priorityJobExecutorService.execute(new JobRunner(priorityQueue.take()));
                } catch (InterruptedException e) {
                    // Terminates the job scheduler on any interruption.
                    // TODO: Act upon certain types of exceptions when retry jobs logic is in place.
                    throw new RuntimeException("The job was interrupted", e);
                }
            }
        });
    }

    @Override
    ExecutorService getExecutorService() {
        return priorityJobExecutorService;
    }

    @Override
    public void shutdown() {
        super.shutdown();
        priorityJobScheduler.shutdownNow();
    }
}
