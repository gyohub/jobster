package com.gyo.recollective.jobster;

import com.gyo.recollective.jobster.model.ExecutionPriority;
import com.gyo.recollective.jobster.model.Job;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JobRunnerTest {

    @Test
    void itRequiresAJobObject() {
        Job job = mock(Job.class);
        new JobRunner(job);
    }

    @Test
    void itExecutesTheJob() throws Exception {
        Job job = mock(Job.class);
        JobRunner jobRunner = new JobRunner(job);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(jobRunner).get();
        executorService.shutdown();

        // Since there's no logic in the job, we check if the durationInSeconds field has been called
        // within the JobRunner
        verify(job).durationInSeconds();
    }

    @Test
    void itCatchesInterruptExceptionAndThrowsRuntimeException() {
        assertThrows(RuntimeException.class, () -> {
            TimeUnit mockedSeconds = mock(TimeUnit.class);
            when(TimeUnit.SECONDS).thenReturn(mockedSeconds);
            doThrow(InterruptedException.class).when(mockedSeconds).sleep(anyLong());
            Job job = Job.autoIDJob(1, ExecutionPriority.HIGH);
            JobRunner jobRunner = new JobRunner(job);

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(jobRunner).get();
        });
    }
}