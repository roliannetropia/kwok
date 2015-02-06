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
package com.kwoksys.action.tape;

import com.kwoksys.biz.base.BaseObjectForm;
import com.kwoksys.biz.tape.dto.Tape;
import com.kwoksys.framework.http.RequestContext;

/**
 * ActionForm class adding/editing tape.
 */
public class TapeForm extends BaseObjectForm {

    private Integer tapeId;
    private Integer copyTapeId;
    private String tapeName;
    private String tapeModelName;
    private String tapeModelNumber;
    private String serialNumber;
    private String tapeCost;
    private int lastServicedOn;
    private String purchaseDate;
    private String purchaseMonth;
    private String purchaseYear;
    private String warrantyDate;
    private String warrantyMonth;
    private String warrantyYear;
    private String warrantyDuration;
    private Integer tapeLocation;
    private String tapeDescription;
    private Integer tapeType;
    private Integer tapeStatus;
    private Integer tapeOwner;
    private Integer manufacturerId;
    private Integer vendorId;
    private String issueId;
    private String contactId;
    private String formContactId;
    private String relationshipDescription;

    @Override
    public void setRequest(RequestContext requestContext) {
        tapeId = requestContext.getParameterInteger("tapeId");
        copyTapeId = requestContext.getParameterInteger("copyTapeId");
        tapeName = requestContext.getParameterString("tapeName");
        tapeModelName = requestContext.getParameterString("tapeModelName");
        tapeModelNumber = requestContext.getParameterString("tapeModelNumber");
        serialNumber = requestContext.getParameterString("serialNumber");
        tapeCost = requestContext.getParameterString("tapeCost");
        lastServicedOn = requestContext.getParameter("lastServicedOn");
        purchaseDate = requestContext.getParameterString("purchaseDate");
        purchaseMonth = requestContext.getParameterString("purchaseMonth");
        purchaseYear = requestContext.getParameterString("purchaseYear");
        warrantyDate = requestContext.getParameterString("warrantyDate");
        warrantyMonth = requestContext.getParameterString("warrantyMonth");
        warrantyYear = requestContext.getParameterString("warrantyYear");
        warrantyDuration = requestContext.getParameterString("warrantyDuration");
        tapeLocation = requestContext.getParameterInteger("tapeLocation");
        tapeDescription = requestContext.getParameterString("tapeDescription");
        tapeType = requestContext.getParameterInteger("tapeType");
        tapeStatus = requestContext.getParameterInteger("tapeStatus");
        tapeOwner = requestContext.getParameterInteger("tapeOwner");
        manufacturerId = requestContext.getParameterInteger("manufacturerId");
        vendorId = requestContext.getParameterInteger("vendorId");
        issueId = requestContext.getParameterString("issueId");
        contactId = requestContext.getParameterString("contactId");
        formContactId = requestContext.getParameterString("formContactId");
        relationshipDescription = requestContext.getParameterString("relationshipDescription");
    }

    public void setTape(Tape tape) {
        tapeName = tape.getName();
        tapeDescription = tape.getDescription();
        tapeOwner = tape.getOwner() != null ? tape.getOwner().getId() : 0;
        tapeType = tape.getType();
        tapeStatus = tape.getStatus();
        tapeModelName = tape.getModelName();
        tapeModelNumber = tape.getModelNumber();
        serialNumber = tape.getSerialNumber();
        tapeCost = tape.getPurchasePrice();
        lastServicedOn = tape.getResetLastServiceDate();
        tapeLocation = tape.getLocation();
        manufacturerId = tape.getManufacturerId();
        vendorId = tape.getVendorId();
        purchaseYear = tape.getPurchaseYear();
        purchaseMonth = tape.getPurchaseMonth();
        purchaseDate = tape.getPurchaseDate();
        warrantyYear = tape.getWarrantyYear();
        warrantyMonth = tape.getWarrantyMonth();
        warrantyDate = tape.getWarrantyDate();
        warrantyDuration = "";
    }

    public Integer getTapeId() {
        return tapeId;
    }
    public void setTapeId(Integer tapeId) {
        this.tapeId = tapeId;
    }
    public String getTapeName() {
        return tapeName;
    }

    public String getTapeModelName() {
        return tapeModelName;
    }

    public String getTapeModelNumber() {
        return tapeModelNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getTapeCost() {
        return tapeCost;
    }

    public int getLastServicedOn() {
        return lastServicedOn;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getPurchaseMonth() {
        return purchaseMonth;
    }

    public String getPurchaseYear() {
        return purchaseYear;
    }

    public String getWarrantyDate() {
        return warrantyDate;
    }

    public String getWarrantyYear() {
        return warrantyYear;
    }

    public String getWarrantyMonth() {
        return warrantyMonth;
    }

    public Integer getTapeLocation() {
        return tapeLocation;
    }

    public String getTapeDescription() {
        return tapeDescription;
    }

    public Integer getTapeType() {
        return tapeType;
    }

    public Integer getTapeStatus() {
        return tapeStatus;
    }

    public Integer getTapeOwner() {
        return tapeOwner;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public Integer getCopyTapeId() {
        return copyTapeId;
    }

    public String getWarrantyDuration() {
        return warrantyDuration;
    }

    public String getIssueId() {
        return issueId;
    }

    public String getContactId() {
        return contactId;
    }

    public String getRelationshipDescription() {
        return relationshipDescription;
    }

    public String getFormContactId() {
        return formContactId;
    }
}