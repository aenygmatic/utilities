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
 * Contains utility functions for {@code String} based operations.
 * <p>
 * @author Balazs Berkes
 */
public class Strings {

    /**
     * Iterates through the characters of a {@code String}.
     * <p>
     * ['S', 't', 'r', 'i', 'n', 'g']
     * <pre>
     *     for(Character c : range("String")) {
     *         // Do something with the character
     *     }
     * </pre>
     * <p>
     * @param string string to iterate through
     * @return {@code Iterable} object of Characters
     */
    public static Iterable<Character> through(String string) {
        return new CharacterIterator(string);
    }

    /**
     * Iterates through the characters of a {@code String} as String objects.
     * <p>
     * ["S", "t", "r", "i", "n", "g"]
     * <pre>
     *     for(String s : throughElements("String")) {
     *         // Do something with the string
     *     }
     * </pre>
     * <p>
     * @param string string to iterate through
     * @return {@code Iterable} object of Strings
     */
    public static Iterable<String> throughElements(String string) {
        return new StringIterator(string);
    }

    private static class CharacterIterator implements Iterable<Character>, Iterator<Character> {

        private final String string;
        private int end;
        private int start;

        private CharacterIterator(String string) {
            end = string.length();
            this.string = string;
        }

        @Override
        public boolean hasNext() {
            return start < end;
        }

        @Override
        public Character next() {
            return string.charAt(start++);
        }

        @Override
        public Iterator<Character> iterator() {
            return this;
        }
    }

    private static class StringIterator implements Iterable<String>, Iterator<String> {

        private final CharacterIterator characterIterator;

        private StringIterator(String string) {
            characterIterator = new CharacterIterator(string);
        }

        @Override
        public boolean hasNext() {
            return characterIterator.hasNext();
        }

        @Override
        public String next() {
            return characterIterator.next().toString();
        }

        @Override
        public Iterator<String> iterator() {
            return this;
        }
    }
}
