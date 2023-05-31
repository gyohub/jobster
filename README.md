## JOBSTER
![](ghassets/Larry-the-Lobster-Guide.png)
Jobster is a Java application that processes jobs with different sizes and priorities. It provides a configurable mechanism to accept jobs, processes them concurrently, and ensures a balance between priority and receipt order.

### Features
- Accept jobs through a customizable interface `com.gyo.recollective.jobster.JobEnqueuer`.
- Support for jobs with different sizes and priorities.
- Concurrent processing of jobs with a configurable number of threads.
- Balancing of priority to ensure higher priority jobs are given precedence.
- Respect receipt order to maintain the order in which jobs are received.

## Assumptions
The following assumptions have been made to simplify the implementation:

- `Job Size`: The job size is considered as the duration it takes to complete the job. The system simulates job processing by waiting for the specified duration.
- `Priority Levels`: There are three priority levels: `LOW`, `MEDIUM`, and `HIGH`. These priority levels determine the order in which jobs are processed, with high priority jobs being given precedence over medium and low priority jobs.

### How It Works
Jobster is designed to efficiently process jobs based on their priorities while maintaining the receipt order and avoiding starvation. Here's a high-level overview of how it works:

*Job Submission*: Jobs are submitted to the system through a customizable interface. This interface allows clients to provide jobs with different sizes and priorities.

*Job Queue*: The submitted jobs are added to a job queue, which maintains the receipt order of the jobs. The job queue is implemented using a suitable data structure such as a priority queue or a linked list.

*Job Processing*: The system processes the jobs concurrently using a configurable number of threads. Each thread takes a job from the job queue and executes it. The system ensures that the jobs are processed according to their priorities, with high priority jobs being processed first.

*Balancing Priority*: To balance the priority of jobs, the system uses a scheduling algorithm that gives precedence to higher priority jobs. This ensures that high priority jobs are processed promptly without completely starving medium and low priority jobs.

### Usage
To use Jobster in your Java project, follow these steps:

1. Add the the Jobster library or module to your project's dependencies.
2. You can whether to use the existing implementations of the interface for job submission or to implement the `JobEnqueuer` interface based on your project's requirements. This interface should provide methods to accept jobs with different sizes and priorities.
3. Configure the number of threads for concurrent job processing based on the desired level of parallelism and system resources.
4. Initialize Jobster with the configured parameters and start accepting and processing jobs.
5. Monitor the job processing status, handle job completion, and perform any required actions based on your application's needs.

You can test the execution by whether running the `com.gyo.recollective.jobster.Main` class from you IDE or from the command line:
```console
%> ./gradlew clean jar
%> java -jar build/libs/jobster-1.0-SNAPSHOT.jar
```

### Features in the backlog
- Starvation Prevention: To avoid starvation, the system periodically checks the job queue and ensures that medium and low priority jobs are not ignored indefinitely. The exact mechanism for preventing starvation can be implemented based on specific requirements, such as periodically promoting lower priority jobs to a higher priority level or allocating a minimum processing time for each priority level. Check the `com.gyo.recollective.jobster.FairLockJobRunner` example class for one idea of how to prevent starvation.
- Retriable jobs: Allow failed jobs to be retried automatically, improving the resilience of the job processing system.
- Persist jobs in database with additional info e.g. Start/End timestamps, result.
- Add termination options to the job enqueue service.
    1. **Graceful**: It waits for all the jobs to complete before terminating.
    2. **Forceful**: It terminates right away interrupting any executing job.
- Add queues based on job types e.g. Reports, Batch data load/write, Email sender.
- Allow recurring jobs by using the `java.util.concurrent.ScheduledExecutorService`.
- If you want to trigger jobs from an event systems like Kafka, you can call the job enqueuer from a Kafka consumer.

[![](https://mermaid.ink/img/pako:eNpVjr0OgjAUhV-luav4Ah1YxEVjYuLa5UIPFaUt9mcwhHe3BBbvdHJzfr6ZOq9BkiI-Ga5DM7AJbJUT5a7cv1mcvIvZIhzr-nBDjGxw5xARpGgwwnCCsNtfTMF3RQ7ObA1__rXg4tuzK1N5je8qipdvqaKyYXnQhWZe04rSExaKZJEaPecxKVJuKVbOyT--riOZQkZFedIFY4cn2fMYsfwAZmFNGg?type=png)](https://mermaid.live/edit#pako:eNpVjr0OgjAUhV-luav4Ah1YxEVjYuLa5UIPFaUt9mcwhHe3BBbvdHJzfr6ZOq9BkiI-Ga5DM7AJbJUT5a7cv1mcvIvZIhzr-nBDjGxw5xARpGgwwnCCsNtfTMF3RQ7ObA1__rXg4tuzK1N5je8qipdvqaKyYXnQhWZe04rSExaKZJEaPecxKVJuKVbOyT--riOZQkZFedIFY4cn2fMYsfwAZmFNGg)

### Final Notes
- The project can ben packaged as a lib and be used in a web app.
- There are no tests for ensuring the execution order based on the priority since we rely on the `java.util.concurrent.PriorityBlockingQueue` object. Instead, we ensure that the implementation is creating an object of that type.
- `JobEnqueuer.shutdown()` for both implementations is not finished. I thought I'd not waste much time on it.
