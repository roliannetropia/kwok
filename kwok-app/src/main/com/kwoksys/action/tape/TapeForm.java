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
    private String serialNumber;
    private String barcodeNumber;
//    private String tapeModelName;
//    private String tapeModelNumber;

//    private String tapeCost;
//    private int lastServicedOn;
//    private String purchaseDate;
//    private String purchaseMonth;
//    private String purchaseYear;
//    private String warrantyDate;
//    private String warrantyMonth;
//    private String warrantyYear;
//    private String warrantyDuration;
//    private String tapeDescription;

//    private Integer tapeLocation;
//    private Integer mediaType;
//    private Integer tapeStatus;

//    private Integer tapeOwner;

//    private Integer manufacturerId;
//    private Integer vendorId;

//    private Integer retention;
//    private Integer system;

//    private String issueId;
//    private String contactId;
//    private String formContactId;
//    private String relationshipDescription;

    @Override
    public void setRequest(RequestContext requestContext) {
        tapeId = requestContext.getParameterInteger("tapeId");
        copyTapeId = requestContext.getParameterInteger("copyTapeId");

        tapeName = requestContext.getParameterString("tapeName");
        serialNumber = requestContext.getParameterString("serialNumber");
        barcodeNumber = requestContext.getParameterString("barcodeNumber");
//        tapeModelName = requestContext.getParameterString("tapeModelName");
//        tapeModelNumber = requestContext.getParameterString("tapeModelNumber");

//        tapeCost = requestContext.getParameterString("tapeCost");
//        lastServicedOn = requestContext.getParameter("lastServicedOn");
//        purchaseDate = requestContext.getParameterString("purchaseDate");
//        purchaseMonth = requestContext.getParameterString("purchaseMonth");
//        purchaseYear = requestContext.getParameterString("purchaseYear");
//        warrantyDate = requestContext.getParameterString("warrantyDate");
//        warrantyMonth = requestContext.getParameterString("warrantyMonth");
//        warrantyYear = requestContext.getParameterString("warrantyYear");
//        warrantyDuration = requestContext.getParameterString("warrantyDuration");
//        tapeDescription = requestContext.getParameterString("tapeDescription");
//        tapeLocation = requestContext.getParameterInteger("tapeLocation");
//        mediaType = requestContext.getParameterInteger("mediaType");
//        tapeStatus = requestContext.getParameterInteger("tapeStatus");
//        tapeOwner = requestContext.getParameterInteger("tapeOwner");
//        manufacturerId = requestContext.getParameterInteger("manufacturerId");
//        vendorId = requestContext.getParameterInteger("vendorId");
//
//        retention = requestContext.getParameterInteger("retention");
//        system = requestContext.getParameterInteger("system");
//        issueId = requestContext.getParameterString("issueId");
//        contactId = requestContext.getParameterString("contactId");
//        formContactId = requestContext.getParameterString("formContactId");
//        relationshipDescription = requestContext.getParameterString("relationshipDescription");
    }

    public void setTape(Tape tape) {
//        tapeDescription = tape.getDescription();
//        tapeOwner = tape.getOwner() != null ? tape.getOwner().getId() : 0;
        tapeName = tape.getTapeName();
        serialNumber = tape.getTapeSerialNumber();
        barcodeNumber = tape.getTapeBarcodeNumber();

//        tapeLocation = tape.getTapeLocation();
//        mediaType = tape.getMediaType();
//        tapeStatus = tape.getTapeStatus();

//        tapeModelName = tape.getModelName();
//        tapeModelNumber = tape.getModelNumber();
//        tapeCost = tape.getPurchasePrice();
//        lastServicedOn = tape.getResetLastServiceDate();
//        manufacturerId = tape.getManufacturerId();
//        vendorId = tape.getVendorId();
//        purchaseYear = tape.getPurchaseYear();
//        purchaseMonth = tape.getPurchaseMonth();
//        purchaseDate = tape.getPurchaseDate();
//        warrantyYear = tape.getWarrantyYear();
//        warrantyMonth = tape.getWarrantyMonth();
//        warrantyDate = tape.getWarrantyDate();
//        warrantyDuration = "";
//        system = tape.getTapeSystem();
//        retention = tape.getTapeRetention();
    }

    public Integer getTapeId() {
        return tapeId;
    }
    public void setTapeId(Integer tapeId) {
        this.tapeId = tapeId;
    }
    public Integer getCopyTapeId() {
        return copyTapeId;
    }

//    public String getTapeModelName() {
//        return tapeModelName;
//    }

//    public String getTapeModelNumber() {
//        return tapeModelNumber;
//    }
    public String getTapeName() {
    return tapeName;
}
    public String getSerialNumber() {
        return serialNumber;
    }
    public String getBarcodeNumber(){
        return barcodeNumber;
    }

//    public String getTapeCost() {
//        return tapeCost;
//    }

//    public int getLastServicedOn() {
//        return lastServicedOn;
//    }

//    public String getPurchaseDate() {
//        return purchaseDate;
//    }

//    public String getPurchaseMonth() {
//        return purchaseMonth;
//    }

//    public String getPurchaseYear() {
//        return purchaseYear;
//    }

//    public String getWarrantyDate() {
//        return warrantyDate;
//    }

//    public String getWarrantyYear() {
//        return warrantyYear;
//    }

//    public String getWarrantyMonth() {
//        return warrantyMonth;
//    }

//    public String getTapeDescription() {
//        return tapeDescription;
//    }

//    public Integer getTapeLocation() {
//        return tapeLocation;
//    }
//    public Integer getMediaType() {
//        return mediaType;
//    }
//    public Integer getTapeStatus() {
//        return tapeStatus;
//    }

//    public Integer getTapeOwner() {
//        return tapeOwner;
//    }

//    public Integer getManufacturerId() {
//        return manufacturerId;
//    }
//    public Integer getVendorId() {
//        return vendorId;
//    }
//
//    public Integer getRetention() {
//        return retention;
//    }
//
//    public Integer getSystem() {
//        return system;
//    }

    //    public String getWarrantyDuration() {
//        return warrantyDuration;
//    }
//
//    public String getIssueId() {
//        return issueId;
//    }
//
//    public String getContactId() {
//        return contactId;
//    }
//
//    public String getRelationshipDescription() {
//        return relationshipDescription;
//    }
//
//    public String getFormContactId() {
//        return formContactId;
//    }
}