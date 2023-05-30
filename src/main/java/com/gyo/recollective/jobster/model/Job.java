package com.gyo.recollective.jobster.model;

import java.util.UUID;

/**
 * Record class representing a Job.
 */
public record Job(String id, Integer durationInSeconds, ExecutionPriority priority) {
    public static Job autoIDJob(Integer durationInSeconds, ExecutionPriority priority) {
        return new Job(UUID.randomUUID().toString(), durationInSeconds, priority);

    }
}