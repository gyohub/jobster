package com.gyo.recollective.jobster;

import com.gyo.recollective.jobster.model.Job;

/**
 * Interface that describes the contract of a job enqueuer implementation.
 * A job enqueuer must be capable of start itself, enqueue and shutdown itself.
 */
public interface JobEnqueuer {
    void enqueue(Job job);

    void start();

    void shutdown();
}
