package com.gyo.recollective.jobster;

import com.gyo.recollective.jobster.model.ExecutionPriority;
import com.gyo.recollective.jobster.model.Job;

public class Main {
    public static void main(String[] args) {
        System.out.println("Let's try Jobster...");
        // Default implementation - For comparision
        // JobEnqueuer jobEnqueuer = new DefaultJobEnqueuer(1);

        // Prioritized execution implementation
        JobEnqueuer jobEnqueuer = new PriorityJobEnqueuer(1, 2);

        jobEnqueuer.start();

        System.out.println("Started!");

        jobEnqueuer.enqueue(Job.autoIDJob(2, ExecutionPriority.HIGH));
        jobEnqueuer.enqueue(Job.autoIDJob(2, ExecutionPriority.LOW));
        jobEnqueuer.enqueue(Job.autoIDJob(3, ExecutionPriority.LOW));
        jobEnqueuer.enqueue(Job.autoIDJob(2, ExecutionPriority.HIGH));
        jobEnqueuer.enqueue(Job.autoIDJob(2, ExecutionPriority.MEDIUM));

        System.out.println("\n");
    }
}