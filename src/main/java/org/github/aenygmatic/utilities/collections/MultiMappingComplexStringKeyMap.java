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
package org.github.aenygmatic.utilities.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.github.aenygmatic.utilities.Integers;

/**
 * Stores elements behind complex {@code String} keys which has hierarchical relations. A complex key is made of key
 * fragments which are separated with the given key delimiter or by default {@code :}.
 * <p>
 * Getting an element for a key will uses fallback strategy which means for the key {@code A:B:C} the element of key
 * {@code A:B} will return if no direct association is made to {@code A:B:C}.
 * <p>
 * @author Balazs Berkes
 * @param <V> type of the stored element
 */
public class MultiMappingComplexStringKeyMap<V> implements ComplexKeyMap<String, V> {

    public static final String DEFAULT_DELIMITER = ":";

    private List<Map<String, V>> fallback;
    private String keyDelimiter;

    public MultiMappingComplexStringKeyMap() {
        this(DEFAULT_DELIMITER);
    }

    public MultiMappingComplexStringKeyMap(String keyDelimiter) {
        this.keyDelimiter = keyDelimiter;
    }

    public MultiMappingComplexStringKeyMap(Map<? extends String, ? extends V> map) {
        this();
        putAllElement(map);
    }

    public MultiMappingComplexStringKeyMap(Map<? extends String, ? extends V> map, String keyDelimiter) {
        this(keyDelimiter);
        putAllElement(map);
    }

    @Override
    public V get(String key) {
        V element = null;

        String keyFragment = key;
        for (int keyComplexity = complexityOf(key); keyComplexity >= 0 && element == null; keyComplexity--) {
            element = getElement(keyComplexity, keyFragment);
            keyFragment = removeLastFragment(keyFragment);
        }

        return element;
    }

    @Override
    public V put(String key, V value) {
        return putElement(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> map) {
        putAllElement(map);
    }

    private int complexityOf(String key) {
        return key.split(keyDelimiter).length - 1;
    }

    private V getElement(int complexityLevel, String fragment) {
        return fallback.size() < complexityLevel + 1 ? null : fallback.get(complexityLevel).get(fragment);
    }

    private String removeLastFragment(String fragment) {
        return fragment.contains(keyDelimiter) ? fragment.substring(0, fragment.lastIndexOf(keyDelimiter.charAt(0))) : null;
    }

    private void putAllElement(Map<? extends String, ? extends V> map) {
        fallback = new ArrayList<>();

        for (Map.Entry<? extends String, ? extends V> entry : map.entrySet()) {
            putElement(entry.getKey(), entry.getValue());
        }
    }

    private V putElement(final String key, final V value) {
        V previous = null;
        String[] keyHierarchy = key.split(keyDelimiter);

        StringBuilder fragments = new StringBuilder();
        for (Integer i : Integers.range(keyHierarchy.length)) {
            expandToNext(fragments, i, keyHierarchy[i]);

            Map<String, V> keyCache = getCacheLevel(i);

            if (isOverridingLevel(keyHierarchy.length, i)) {
                previous = keyCache.put(fragments.toString(), value);
            } else if (isOverridableLevel(fragments.toString(), keyCache)) {
                previous = keyCache.put(fragments.toString(), value);
            }
        }
        return previous;
    }

    private boolean isOverridingLevel(int keyComplexity, int currentLevel) {
        return keyComplexity - 1 == currentLevel;
    }

    private boolean isOverridableLevel(String key, Map<String, V> keyCache) {
        return !keyCache.containsKey(key);
    }

    private Map<String, V> getCacheLevel(Integer index) {
        Map<String, V> keyCache;
        if (fallback.size() == index) {
            keyCache = new HashMap<>();
            fallback.add(keyCache);
        } else {
            keyCache = fallback.get(index);
        }
        return keyCache;
    }

    private void expandToNext(StringBuilder fragments, Integer keyComplexity, String fragment) {
        if (keyComplexity > 0) {
            fragments.append(keyDelimiter);
        }
        fragments.append(fragment);
    }

    public void setKeyDelimiter(String keyDelimiter) {
        this.keyDelimiter = keyDelimiter;
    }
}
