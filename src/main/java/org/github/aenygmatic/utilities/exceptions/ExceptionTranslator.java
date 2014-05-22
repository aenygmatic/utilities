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

import java.util.List;

/**
 * Translate the caught exception into the given type.
 * <p>
 * @author Balazs Berkes
 * @param <T> type which the exception will be translated to
 */
public class ExceptionTranslator<T> {

    private List<ExceptionRelation<T>> fallback;
    private T defaultValue;

    public T translate(Exception exception) {
        T translated = defaultValue;

        for (ExceptionRelation<T> relation : fallback) {
            if (relation.getException().isInstance(exception)) {
                translated = relation.getRelated();
                break;
            }
        }

        return translated;
    }

    public void setFallback(List<ExceptionRelation<T>> fallback) {
        this.fallback = fallback;
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }
}
