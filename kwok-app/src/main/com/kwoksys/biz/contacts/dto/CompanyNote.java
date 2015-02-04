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
package com.kwoksys.biz.contacts.dto;

import com.kwoksys.biz.base.BaseObject;

/**
 * CompanyNote class.
 */
public class CompanyNote extends BaseObject {

    public static final String CREATION_DATE = "creation_date";

    private Integer noteId;
    private String noteName;
    private String noteDescription;
    private Integer noteTypeId;
    private Integer companyId;

    public CompanyNote(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getNoteId() {
        return noteId;
    }
    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }
    public String getNoteName() {
        return noteName;
    }
    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }
    public String getNoteDescription() {
        return noteDescription;
    }
    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }
    public Integer getNoteTypeId() {
        return noteTypeId;
    }
    public void setNoteTypeId(Integer noteTypeId) {
        this.noteTypeId = noteTypeId;
    }
    public Integer getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
