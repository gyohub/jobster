package com.gyo.recollective.jobster;

import com.gyo.recollective.jobster.model.ExecutionPriority;
import com.gyo.recollective.jobster.model.Job;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriorityJobEnqueuerTest {

    @Mock
    private ExecutorService priorityJobSchedulerMock;

    @Mock
    private ExecutorService priorityJobExecutorServiceMock;

    @Test
    void itStartsTheJobEnqueuerWithTheCorrectObjects() {
        try (MockedStatic<Executors> mocked = mockStatic(Executors.class)) {
            mocked.when(Executors::newSingleThreadExecutor).thenReturn(priorityJobSchedulerMock);
            mocked.when(() -> Executors.newFixedThreadPool(1)).thenReturn(priorityJobExecutorServiceMock);
            final Map<PriorityBlockingQueue, List<Object>> constructorArgs = new HashMap<>();
            try (MockedConstruction<PriorityBlockingQueue> mockedPriorityBlockingQueue = mockConstruction(PriorityBlockingQueue.class, (mock, context) -> constructorArgs.put(mock, new ArrayList<>(context.arguments())))) {
                try (MockedStatic<Comparator> mockedComparator = mockStatic(Comparator.class)) {
                    Comparator<ExecutionPriority> comparatorMock = mock(Comparator.class);
                    mockedComparator.when(() -> Comparator.comparing(Job::priority)).thenReturn(comparatorMock);
                    PriorityJobEnqueuer priorityJobEnqueuer = new PriorityJobEnqueuer(1, 1);
                    priorityJobEnqueuer.start();

                    mocked.verify(Executors::newSingleThreadExecutor, times(1));
                    mocked.verify(() -> Executors.newFixedThreadPool(1), times(1));

                    //Asserting the Priority queue creation.
                    assertEquals(mockedPriorityBlockingQueue.constructed().size(), 1);
                    PriorityBlockingQueue constructedQueue = mockedPriorityBlockingQueue.constructed().get(0);
                    assertEquals(constructorArgs.get(constructedQueue).size(), 2);
                    assertEquals(constructorArgs.get(constructedQueue).get(0), 1);
                    assertEquals(constructorArgs.get(constructedQueue).get(1), Comparator.comparing(Job::priority));
                }
            }
        }
    }

    @Test
    void itEnqueuesAJob() {
        try (MockedStatic<Executors> mocked = mockStatic(Executors.class)) {
            try (MockedConstruction<PriorityBlockingQueue> mockedPriorityBlockingQueue = mockConstruction(PriorityBlockingQueue.class)) {
                mocked.when(Executors::newSingleThreadExecutor).thenReturn(priorityJobSchedulerMock);
                mocked.when(() -> Executors.newFixedThreadPool(1)).thenReturn(priorityJobExecutorServiceMock);
                PriorityJobEnqueuer priorityJobEnqueuer = new PriorityJobEnqueuer(1, 1);
                priorityJobEnqueuer.start();
                Job jobMocked = mock(Job.class);
                priorityJobEnqueuer.enqueue(jobMocked);

                verify(mockedPriorityBlockingQueue.constructed().get(0)).add(jobMocked);
            }
        }
    }
}