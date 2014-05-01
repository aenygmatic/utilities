/*
 * Copyright 2014 Balázs.
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
package org.github.aenygmatic.utilities.collections;

import java.util.Map;

/**
 * A container which contains elements with a complex identifier. This container provides a unified interface for
 * searching in key-value pairs where the keys are in hierarchical structure.
 * <p>
 * @author Balazs Berkes
 * @param <K> type of the key
 * @param <V> type of the value
 */
public interface ComplexKeyMap<K, V> {

    /**
     * Gets the element of the given key. If no element found with the exact key the search continues in the {@code n-1}
     * level of the hierarchy upto the highest level.
     * <p>
     * @param key the complex key
     * @return the element belongs to the key or its closest association in the key hierarchy. If no element found in
     * the highest level it returns {@code null}.
     */
    V get(K key);

    V put(K key, V value);

    void putAll(Map<? extends K, ? extends V> m);
}
