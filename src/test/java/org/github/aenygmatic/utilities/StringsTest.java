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
package org.github.aenygmatic.utilities;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Unit test for {@code Strings}.
 * <p>
 * @author Balazs Berkes
 */
public class StringsTest {

    @Test
    public void testThroughCharacters() {
        List<Character> expected = Arrays.asList('S', 't', 'r', 'i', 'n', 'g');
        int index = 0;

        for (Character c : Strings.throughCharacters("String")) {
            assertEquals(expected.get(index), c);
            index++;
        }
    }

    @Test
    public void testThroughElements() {
        List<String> expected = Arrays.asList("S", "t", "r", "i", "n", "g");
        int index = 0;

        for (String s : Strings.throughElements("String")) {
            assertEquals(expected.get(index), s);
            index++;
        }
    }

}
