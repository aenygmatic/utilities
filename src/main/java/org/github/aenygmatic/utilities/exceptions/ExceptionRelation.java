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

/**
 * Stores the relation between an exception type and the given type which the exception will be translated to.
 * <p>
 * @author Balazs Berkes
 * @param <T> type which the exception will be translated to
 */
public class ExceptionRelation<T> {

    private Class<? extends Exception> exception;
    private T related;

    public Class<? extends Exception> getException() {
        return exception;
    }

    public void setException(Class<? extends Exception> exception) {
        this.exception = exception;
    }

    public T getRelated() {
        return related;
    }

    public void setRelated(T related) {
        this.related = related;
    }

}
