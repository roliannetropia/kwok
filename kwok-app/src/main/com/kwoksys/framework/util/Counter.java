/*
 * ====================================================================
 * Copyright 2005-2011 Wai-Lun Kwok
 *
 * http://www.kwoksys.com/LICENSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package com.kwoksys.framework.util;

/**
 * Counter helper.
 */
public class Counter {

    private int counter;
    private int incr;

    public Counter(int counter) {
        this.counter = counter;
        incr = 1;
    }

    public Counter() {
        counter = 0;
        incr = 1;
    }

    /**
     * Increments counter.
     *
     * @return ..
     */
    public int incrCounter() {
        counter += incr;
        return counter;
    }
}
