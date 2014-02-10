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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

import static org.github.aenygmatic.utilities.Integers.range;

public class IntegersTest {

    @Test
    public void testRange() {
        assertThat(numbersUpTo(10), isListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        assertThat(inBetween(5, 9), isListOf(5, 6, 7, 8));
        assertThat(betweenWithSteps(0, 10, 2), isListOf(0, 2, 4, 6, 8));
    }

    private static Matcher<List<Integer>> isListOf(Integer... ints) {
        return is(Arrays.asList(ints));
    }

    private List<Integer> numbersUpTo(int upTo) {
        List<Integer> list = new ArrayList<>();
        for (Integer i : range(upTo)) {
            list.add(i);
        }
        return list;
    }

    private List<Integer> inBetween(int from, int to) {
        List<Integer> list = new ArrayList<>();
        for (Integer i : range(from, to)) {
            list.add(i);
        }
        return list;
    }

    private List<Integer> betweenWithSteps(int from, int to, int step) {
        List<Integer> list = new ArrayList<>();
        for (Integer i : range(from, to, step)) {
            list.add(i);
        }
        return list;
    }
}
