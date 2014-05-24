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

import java.util.Iterator;

/**
 * Contains utility functions for {@code Integet} based operations.
 * <p>
 * @author Balazs Berkes
 */
public class Integers {

    /**
     * Provides an {@code Iterable} object to go through a line of {@code Integers}. It's a comfortable form to use in
     * for loops to make it more readable. The following code will iterate from {@code zero} (included) to {@code 100}
     * (excluded).
     * <p>
     * [0, 1, 2... 98, 99]
     * <pre>
     *     for(Integer i : range(100)) {
     *         // Do something with the index
     *     }
     * </pre>
     *
     * @param end the end of the iteration. This number will never be reached.
     * @return {@code Iterable} object of integers
     */
    public static Iterable<Integer> range(int end) {
        return new IntegerRage(0, end);
    }

    /**
     * Provides an {@code Iterable} object to go through a line of {@code Integers}. It's a comfortable form to use in
     * for loops to make it more readable. The following code will iterate from {@code 10} (included) to {@code 20}
     * (excluded).
     * <p>
     * [10, 11, 12... 18, 19]
     * <pre>
     *     for(Integer i : range(10, 20)) {
     *         // Do something with the index
     *     }
     * </pre>
     *
     * @param start the start of the iteration. This number will never be the first element in the iteration.
     * @param end the end of the iteration. This number will never be reached.
     * @return {@code Iterable} object of integers
     */
    public static Iterable<Integer> range(int start, int end) {
        return new IntegerRage(start, end);
    }

    /**
     * Provides an {@code Iterable} object to go through a line of {@code Integers}. It's a comfortable form to use in
     * for loops to make it more readable. The following code will iterate through every second number from {@code 0}
     * (included) to {@code 20} (excluded).
     * <p>
     * [0, 2, 4... 16, 18]
     * <pre>
     * for(Integer i : range(0, 20, 2)) {
     *         // Do something with the index
     * }
     * </pre>
     *
     * @param start the start of the iteration. This number will never be the first element in the iteration.
     * @param end the end of the iteration. This number will never be reached.
     * @param step the value the iteration will use to increment the index
     * @return {@code Iterable} object of integers
     */
    public static Iterable<Integer> range(int start, int end, int step) {
        return new SteppingIntegerRage(start, end, step);
    }

    private Integers() {
    }

    private static class IntegerRage implements Iterable<Integer>, Iterator<Integer> {

        protected int start;
        private int end;

        private IntegerRage(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean hasNext() {
            return start < end;
        }

        @Override
        public Integer next() {
            return start++;
        }

        @Override
        public Iterator<Integer> iterator() {
            return this;
        }
    }

    private static class SteppingIntegerRage extends IntegerRage {

        private int step;

        private SteppingIntegerRage(int start, int end, int step) {
            super(start, end);
            this.step = step;
        }

        @Override
        public Integer next() {
            int current = start;
            start = start + step;
            return current;
        }
    }
}
