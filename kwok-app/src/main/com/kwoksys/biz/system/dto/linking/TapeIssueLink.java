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
package com.kwoksys.biz.system.dto.linking;

import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.biz.system.dto.linking.ObjectLink;

/**
 * TapeIssueLink
 */
public class TapeIssueLink extends BaseObject {

    private Integer tapeId;
    private Integer issueId;

    public TapeIssueLink() {}

    public TapeIssueLink(Integer tapeId) {
        this.tapeId = tapeId;
    }

    public ObjectLink createObjectMap() {
        ObjectLink objectMap = new ObjectLink();
        objectMap.setObjectId(tapeId);
        objectMap.setObjectTypeId(ObjectTypes.TAPE);
        objectMap.setLinkedObjectId(issueId);
        objectMap.setLinkedObjectTypeId(ObjectTypes.ISSUE);
        return objectMap;
    }

    public Integer getTapeId() {
        return tapeId;
    }
    public void setTapeId(Integer tapeId) {
        this.tapeId = tapeId;
    }
    public Integer getIssueId() {
        return issueId;
    }
    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }
}