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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Barrier object which organized threads to groups and allows only one thread
 * to run of the same group.
 * <p>
 * @author Balazs Berkes
 */
public class GroupBarrier {

    public static <T> GroupLock<T> newGroupLock() {
        return newGroupLock(GroupLock.DEFAULT_TIMEOUT, GroupLock.DEFAULT_TIME_UNIT);
    }

    public static <T> GroupLock<T> newGroupLock(int timeout) {
        return newGroupLock(timeout, GroupLock.DEFAULT_TIME_UNIT);
    }

    public static <T> GroupLock<T> newGroupLock(int timeout, TimeUnit timeUnit) {
        return new DoubleMappingGroupLock<>(timeout, timeUnit);
    }

    private static class DoubleMappingGroupLock<T> implements GroupLock<T> {

        private final Map<T, ReentrantLock> groupLocks = new HashMap<>();
        private final Map<T, Integer> groupMemberCount = new HashMap<>();

        private final int timeout;
        private final TimeUnit timeUnit;

        private DoubleMappingGroupLock() {
            this(DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT);
        }

        private DoubleMappingGroupLock(int timeout) {
            this(timeout, DEFAULT_TIME_UNIT);
        }

        private DoubleMappingGroupLock(int timeout, TimeUnit timeUnit) {
            this.timeout = timeout;
            this.timeUnit = timeUnit;
        }

        @Override
        public boolean tryAndAwaitGroup(T groupId) {
            Lock lock = getLock(groupId);
            return interruptableWait(lock);
        }

        @Override
        public synchronized void unlockGroup(T groupId) {
            ReentrantLock lock = groupLocks.get(groupId);
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
            cleanUpIfNoThreadsAreWaiting(groupId);
        }

        private synchronized Lock getLock(T groupIdentifier) {
            incrementGroupMembers(groupIdentifier);
            return receiveLock(groupIdentifier);
        }

        private boolean interruptableWait(Lock lock) {
            boolean available = false;
            try {
                available = lock.tryLock(timeout, timeUnit);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return available;
        }

        private Lock receiveLock(T groupId) {
            ReentrantLock lock = groupLocks.get(groupId);
            if (lock == null) {
                lock = new ReentrantLock();
                groupLocks.put(groupId, lock);
            }
            return lock;
        }

        private void incrementGroupMembers(T groupId) {
            Integer current = groupMemberCount.get(groupId);
            if (current == null) {
                current = 1;
            } else {
                current++;
            }
            groupMemberCount.put(groupId, current);
        }

        private void cleanUpIfNoThreadsAreWaiting(T groupIdentifier) {
            Integer members = groupMemberCount.get(groupIdentifier);
            members--;
            if (members == 0) {
                groupLocks.remove(groupIdentifier);
                groupMemberCount.remove(groupIdentifier);
            } else {
                groupMemberCount.put(groupIdentifier, members);
            }
        }
    }

    private static class SingleMappingGroupLock<T> implements GroupLock<T> {

        private final Map<T, GroupLock> groupLocks = new HashMap<>();

        private final int timeout;
        private final TimeUnit timeUnit;

        private SingleMappingGroupLock() {
            this(DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT);
        }

        private SingleMappingGroupLock(int timeout) {
            this(timeout, DEFAULT_TIME_UNIT);
        }

        private SingleMappingGroupLock(int timeout, TimeUnit timeUnit) {
            this.timeout = timeout;
            this.timeUnit = timeUnit;
        }

        @Override
        public boolean tryAndAwaitGroup(T groupId) {
            Lock lock = receiveLock(groupId);
            return interruptableWait(lock);
        }

        @Override
        public synchronized void unlockGroup(T groupId) {
            GroupLock groupLock = groupLocks.get(groupId);
            if (groupLock.lock.isHeldByCurrentThread()) {
                groupLock.lock.unlock();
            }
            cleanUpIfNoThreadsAreWaiting(groupId, groupLock);
        }

        private boolean interruptableWait(Lock lock) {
            boolean available = false;
            try {
                available = lock.tryLock(timeout, timeUnit);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return available;
        }

        private synchronized Lock receiveLock(T groupId) {
            GroupLock grouplock = groupLocks.get(groupId);
            if (grouplock == null) {
                grouplock = new GroupLock();
                grouplock.lock = new ReentrantLock();
                groupLocks.put(groupId, grouplock);
            } else {
                grouplock.members++;
            }
            return grouplock.lock;
        }

        private void cleanUpIfNoThreadsAreWaiting(T groupIdentifier, GroupLock groupLock) {
            groupLock.members--;
            if (groupLock.members == 0) {
                groupLocks.remove(groupIdentifier);
            }
        }

        private static class GroupLock {

            private ReentrantLock lock;
            private int members;

        }
    }
}
