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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ComplexStringKeyMapTest {

    private Map<String, String> source;
    private ComplexStringKeyMap<String> underTest;

    @Before
    public void setUp() {
        underTest = new ComplexStringKeyMap<>();
        source = new HashMap<>();
    }

    @Test
    public void testDirectMappingWithOneLevelComplexity() {
        source.put("A", "A-value");
        source.put("B", "B-value");
        source.put("C", "C-value");
        underTest.putAll(source);

        String actual = underTest.get("A");

        assertEquals("A-value", actual);
    }

    @Test
    public void testDirectMappingWithTwoLevelComplexity() {
        source.put("A", "A-value");
        source.put("A:A", "AA-value");
        source.put("B", "B-value");
        source.put("C", "C-value");
        underTest.putAll(source);

        String actual = underTest.get("A:A");

        assertEquals("AA-value", actual);
    }

    @Test
    public void testGetWithHigerComplexity() {
        source.put("A", "A-value");
        source.put("A:A", "AA-value");
        source.put("B", "B-value");
        source.put("C", "C-value");
        underTest.putAll(source);

        String actual = underTest.get("A:A:B");

        assertEquals("AA-value", actual);
    }

    @Test
    public void testFallbackMappingWithTwoLevelComplexity() {
        source.put("A", "A-value");
        source.put("A:A", "AA-value");
        source.put("B", "B-value");
        source.put("C", "C-value");
        underTest.putAll(source);

        String actual = underTest.get("A:B");

        assertEquals("A-value", actual);
    }

    @Test
    public void testFallbackMappingToLowerWithTwoLevelComplexity() {
        source.put("A:A", "AA-value");
        source.put("B", "B-value");
        source.put("C", "C-value");
        underTest.putAll(source);

        String actual = underTest.get("A");

        assertEquals("AA-value", actual);
    }

    @Test
    public void testFallbackMappingWithTwoLevelComplexityWithConstructiorCreation() {
        source.put("A", "A-value");
        source.put("A:A", "AA-value");
        source.put("B", "B-value");
        source.put("C", "C-value");
        underTest = new ComplexStringKeyMap<>(source);

        String actual = underTest.get("A:A");

        assertEquals("AA-value", actual);
    }

    @Test
    public void testFallbackMappingWithTwoLevelComplexityWithConstructiorCreationWithCustomDelimiter() {
        source.put("A", "A-value");
        source.put("A-A", "AA-value");
        source.put("B", "B-value");
        source.put("C", "C-value");
        underTest = new ComplexStringKeyMap<>(source, "-");

        String actual = underTest.get("A-A");

        assertEquals("AA-value", actual);
    }

    @Test
    public void testFallbackMappingWithTwoLevelComplexityCustomDelimiter() {
        source.put("A", "A-value");
        source.put("A-A", "AA-value");
        source.put("B", "B-value");
        source.put("C", "C-value");
        underTest = new ComplexStringKeyMap<>("-");
        underTest.putAll(source);

        String actual = underTest.get("A-A");

        assertEquals("AA-value", actual);
    }

    @Test
    public void testContainsKeyWithNonGenericType() {
        source.put("A", "A-value");
        source.put("A:A", "AA-value");
        source.put("B", "B-value");
        underTest.putAll(source);

        boolean actual = underTest.containsKey(new Object());

        assertFalse(actual);
    }
}
