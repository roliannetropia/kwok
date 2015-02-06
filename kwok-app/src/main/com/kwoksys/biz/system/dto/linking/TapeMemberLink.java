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
 * TapeMemberLink
 */
public class TapeMemberLink extends BaseObject {

    private Integer tapeId;
    private Integer memberTapeId;

    public TapeMemberLink() {}

    public TapeMemberLink(Integer tapeId) {
        this.tapeId = tapeId;
    }

    public ObjectLink createObjectMap() {
        ObjectLink objectMap = new ObjectLink();
        objectMap.setObjectId(tapeId);
        objectMap.setObjectTypeId(ObjectTypes.TAPE);
        objectMap.setLinkedObjectId(memberTapeId);
        objectMap.setLinkedObjectTypeId(ObjectTypes.TAPE);
        return objectMap;
    }

    public Integer getTapeId() {
        return tapeId;
    }
    public void setTapeId(Integer tapeId) {
        this.tapeId = tapeId;
    }
    public Integer getMemberTapeId() {
        return memberTapeId;
    }
    public void setMemberTapeId(Integer memberTapeId) {
        this.memberTapeId = memberTapeId;
    }
}