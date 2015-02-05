/*
 * ====================================================================
 * Copyright 2005-2013 Wai-Lun Kwok
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
package com.kwoksys.biz.base;

import com.kwoksys.framework.http.RequestContext;

/**
 * BaseForm
 */
public abstract class BaseForm implements java.io.Serializable {

    private boolean isResubmit;

    public abstract void setRequest(RequestContext requestContext);

    public boolean isResubmit() {
        return isResubmit;
    }

    public void setResubmit(boolean resubmit) {
        isResubmit = resubmit;
    }
}