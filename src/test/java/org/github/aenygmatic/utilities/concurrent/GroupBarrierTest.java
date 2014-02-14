/*
 * Copyright 2014 Balazs Berkes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.github.aenygmatic.utilities.concurrent;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import static org.github.aenygmatic.utilities.Integers.range;

/**
 * Test for GroupBarrier.
 * <p>
 * @author Balazs Berkes
 */
public class GroupBarrierTest {

    private static final int TOTAL_THREADS = 500;

    private BarrierAwareService barrierAwareService;
    private ExecutorService executorService;

    @Before
    public void setUp() {
        barrierAwareService = new BarrierAwareService();
    }

    @Test
    public void testPerformance() throws InterruptedException {
        //executorService = Executors.newCachedThreadPool();
        executorService = Executors.newFixedThreadPool(TOTAL_THREADS);
        for (Integer group : range(100)) {
            for (Integer member : range(100)) {
                executorService.submit(new Caller(barrierAwareService, new Request(group, member)));
            }
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
    }

    private static class BarrierAwareService {

        private GroupLock<Integer> barrier = GroupBarrier.newGroupLock();

        private void process(Request request) throws InterruptedException {
            barrier.tryAndAwaitGroup(request.id);
            log("enter", request.id, request.secondId);
            Thread.sleep((long) (10));
            log("exit", request.id, request.secondId);
            barrier.unlockGroup(request.id);
        }

        private static void log(String event, int id, int secondId) {
            //System.out.println(new StringBuilder().append(id).append(":").append(secondId).append(" - ").append(event).toString());
        }
    }

    private static class Request {

        private int id;
        private int secondId;

        public Request(int id, int secondId) {
            this.id = id;
            this.secondId = secondId;
        }
    }

    private static class Caller implements Runnable {

        private BarrierAwareService barrierAwareService;
        private Request request;

        public Caller(BarrierAwareService barrierAwareService, Request request) {
            this.barrierAwareService = barrierAwareService;
            this.request = request;
        }

        @Override
        public void run() {
            try {
                barrierAwareService.process(request);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(new Date());
        GroupBarrierTest test = new GroupBarrierTest();
        test.setUp();
        test.testPerformance();
        System.out.println(new Date());
    }
}
