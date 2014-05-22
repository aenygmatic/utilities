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
package org.github.aenygmatic.utilities.exceptions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link ExceptionTranslator}.
 * <p>
 * @author Balazs Berkes
 */
public class ExceptionTranslatorTest {

    private List<ExceptionRelation<String>> fallback;
    private Exception exception;

    private ExceptionTranslator<String> underTest;

    @Before
    public void setUp() {
        initializeTestData();
        initializeTestedClass();
    }

    @Test
    public void testTranslateToFirstFallback() {
        givenExcetion(new IllegalArgumentException());

        String actual = underTest.translate(exception);

        assertEquals(actual, "IllegalArgumentException");
    }

    @Test
    public void testTranslateToSecondFallback() {
        givenExcetion(new RuntimeException());

        String actual = underTest.translate(exception);

        assertEquals(actual, "RuntimeException");
    }

    @Test
    public void testTranslateToDefault() {
        givenExcetion(new Exception());

        String actual = underTest.translate(exception);

        assertEquals(actual, "Default");
    }

    private void initializeTestedClass() {
        underTest = new ExceptionTranslator<>();
        underTest.setFallback(fallback);
        underTest.setDefaultValue("Default");
    }

    private void initializeTestData() {
        fallback = new ArrayList<>();
        fallback.add(relation(IllegalArgumentException.class, "IllegalArgumentException"));
        fallback.add(relation(RuntimeException.class, "RuntimeException"));
    }

    private void givenExcetion(Exception ex) {
        exception = ex;
    }

    private ExceptionRelation<String> relation(Class<? extends Exception> ex, String related) {
        ExceptionRelation<String> r = new ExceptionRelation<>();
        r.setException(ex);
        r.setRelated(related);
        return r;
    }

}
