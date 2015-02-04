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
package com.kwoksys.framework.ui;

/**
 * UI functions.
 */
public class RowStyle {

    private static final String ROW1 = "row1";
    private static final String ROW2 = "row2";

    private String currentStyle = ROW1;

    /**
     * Set row class, we want 2 different colors for list pages.
     */
    private void setRowClass() {
        if (currentStyle.equals(ROW1)) {
            currentStyle = ROW2;
        } else {
            currentStyle = ROW1;
        }
    }
    /**
     * Get row class.
     *
     * @return ..
     */
    public String getRowClass() {
        setRowClass();
        return currentStyle;
    }
}
