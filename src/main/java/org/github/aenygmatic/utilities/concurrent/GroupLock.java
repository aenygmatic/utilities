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

import java.util.concurrent.TimeUnit;

/**
 * Barrier object which organized threads to groups and allows only one thread
 * to run of the same group.
 *
 * @param <T> the type of the object which is used for grouping threads
 * ({@link Object#equals(Object)} is used).
 *
 * @author Balazs Berkes
 */
public interface GroupLock<T> {

    static final int DEFAULT_TIMEOUT = 60;
    static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * Locks the current group in the barrier and await the lock to be released
     * when it's held by and other thread in with the same group id.
     *
     * @param groupId object which is used for grouping threads. This key will
     * identify which group the thread should queue up to.
     * @return {@code true} if the thread could get the lock in the default
     * timeout, otherwise {@code false}
     */
    boolean tryAndAwaitGroup(T groupId);

    /**
     * Unlocks the current group allowing to the queued threads to execute. When
     * no other thread in in the current group the barrier for that group will
     * be cleaned up.
     *
     * @param groupId object which is used for grouping threads. This key will
     * identify which group the thread should queue up to.
     */
    void unlockGroup(T groupId);
}
