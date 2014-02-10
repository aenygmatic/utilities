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
 *
 * @param <T> the type of the object which is used for grouping threads
 * ({@link Object#equals(Object)} is used).
 *
 * @author Balazs Berkes
 */
public class GroupBarrier<T> {

    private final Map<T, ReentrantLock> groupLocks = new HashMap<>();
    private final Map<T, Integer> groupMemberCount = new HashMap<>();

    private final int timeout;
    private final TimeUnit timeUnit;

    public GroupBarrier() {
        this(60, TimeUnit.SECONDS);
    }

    public GroupBarrier(int timeout) {
        this(timeout, TimeUnit.SECONDS);
    }

    public GroupBarrier(int timeout, TimeUnit timeUnit) {
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    /**
     * Locks the current group in the barrier and await the lock to be released
     * when it's held by and other thread in with the same group id.
     *
     * @param groupId object which is used for grouping threads. This key will
     * identify which group the thread should queue up to.
     * @return {@code true} if the thread could get the lock in the default
     * timeout, otherwise {@code false}
     */
    public boolean tryAndAwaitGroup(T groupId) {
        Lock lock = getLock(groupId);
        return interruptableWait(lock);
    }

    /**
     * Unlocks the current group allowing to the queued threads to execute. When
     * no other thread in in the current group the barrier for that group will
     * be cleaned up.
     *
     * @param groupId object which is used for grouping threads. This key will
     * identify which group the thread should queue up to.
     */
    public synchronized void unlockGroup(T groupId) {
        ReentrantLock lock;
        lock = groupLocks.get(groupId);
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
