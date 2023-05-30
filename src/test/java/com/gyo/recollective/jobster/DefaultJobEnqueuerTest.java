package com.gyo.recollective.jobster;

import com.gyo.recollective.jobster.model.Job;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultJobEnqueuerTest {
    @Mock
    private ExecutorService executorService;

    @Test
    void itStartsTheJobEnqueuer() {
        try (MockedStatic<Executors> mocked = mockStatic(Executors.class)) {
            mocked.when(() -> Executors.newFixedThreadPool(1)).thenReturn(executorService);
            DefaultJobEnqueuer defaultJobEnqueuer = new DefaultJobEnqueuer(1);
            defaultJobEnqueuer.start();

            mocked.verify(() -> Executors.newFixedThreadPool(1), times(1));
        }
    }

    @Test
    void itEnqueuesAJob() {
        try (MockedStatic<Executors> mocked = mockStatic(Executors.class)) {
            mocked.when(() -> Executors.newFixedThreadPool(1)).thenReturn(executorService);
            DefaultJobEnqueuer defaultJobEnqueuer = new DefaultJobEnqueuer(1);
            defaultJobEnqueuer.start();
            defaultJobEnqueuer.enqueue(mock(Job.class));

            verify(executorService).submit(any(JobRunner.class));
        }
    }
}