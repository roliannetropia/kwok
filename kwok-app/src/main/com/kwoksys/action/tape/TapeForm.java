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

//    private String tapeName;
    private String serialNumber;
    private String barcodeNumber;
//    private String tapeModelName;
//    private String tapeModelNumber;

//    private String tapeCost;
//    private int lastServicedOn;
//    private String purchaseDate;
//    private String purchaseMonth;
//    private String purchaseYear;
    private String manufacturedDate;
    private String manufacturedMonth;
    private String manufacturedYear;

    private String transactionDate;
    private String transactionMonth;
    private String transactionYear;

    private String transactionHour;
    private String transactionMin;

    private String moveDate;
    private String moveMonth;
    private String moveYear;

    private String expireDate;
    private String expireMonth;
    private String expireYear;

//    private String warrantyDate;
//    private String warrantyMonth;
//    private String warrantyYear;
//    private String warrantyDuration;
//    private String tapeDescription;

    private Integer manufacturerId;
    private Integer vendorId;

    private Integer mediaType;
    private Integer tapeLocation;
    private Integer tapeRetention;

    private Integer tapeSystem;
    private Integer tapeStatus;

//    private Integer tapeOwner;

//    private Integer system;

//    private String issueId;
//    private String contactId;
//    private String formContactId;
//    private String relationshipDescription;

    @Override
    public void setRequest(RequestContext requestContext) {
        tapeId = requestContext.getParameterInteger("tapeId");
        copyTapeId = requestContext.getParameterInteger("copyTapeId");

//        tapeName = requestContext.getParameterString("tapeName");
        serialNumber = requestContext.getParameterString("serialNumber");
        barcodeNumber = requestContext.getParameterString("barcodeNumber");
//        tapeModelName = requestContext.getParameterString("tapeModelName");
//        tapeModelNumber = requestContext.getParameterString("tapeModelNumber");

        mediaType = requestContext.getParameterInteger("mediaType");
        tapeLocation = requestContext.getParameterInteger("tapeLocation");
        tapeRetention = requestContext.getParameterInteger("tapeRetention");

        manufacturerId = requestContext.getParameterInteger("manufacturerId");
        vendorId = requestContext.getParameterInteger("vendorId");

        tapeSystem = requestContext.getParameterInteger("tapeSystem");
        tapeStatus = requestContext.getParameterInteger("tapeStatus");
//        tapeCost = requestContext.getParameterString("tapeCost");
//        lastServicedOn = requestContext.getParameter("lastServicedOn");
//        purchaseDate = requestContext.getParameterString("purchaseDate");
//        purchaseMonth = requestContext.getParameterString("purchaseMonth");
//        purchaseYear = requestContext.getParameterString("purchaseYear");
        manufacturedDate = requestContext.getParameterString("manufacturedDate");
        manufacturedMonth = requestContext.getParameterString("manufacturedMonth");
        manufacturedYear = requestContext.getParameterString("manufacturedYear");

        transactionDate = requestContext.getParameterString("transactionDate");
        transactionMonth = requestContext.getParameterString("transactionMonth");
        transactionYear = requestContext.getParameterString("transactionYear");

        transactionHour = requestContext.getParameterString("transactionHour");
        transactionMin = requestContext.getParameterString("transactionMin");

        moveDate = requestContext.getParameterString("moveDate");
        moveMonth = requestContext.getParameterString("moveMonth");
        moveYear = requestContext.getParameterString("moveYear");

        expireDate = requestContext.getParameterString("expireDate");
        expireMonth = requestContext.getParameterString("expireMonth");
        expireYear = requestContext.getParameterString("expireYear");
//        warrantyDate = requestContext.getParameterString("warrantyDate");
//        warrantyMonth = requestContext.getParameterString("warrantyMonth");
//        warrantyYear = requestContext.getParameterString("warrantyYear");
//        warrantyDuration = requestContext.getParameterString("warrantyDuration");
//        tapeDescription = requestContext.getParameterString("tapeDescription");

//        tapeOwner = requestContext.getParameterInteger("tapeOwner");

//
//        system = requestContext.getParameterInteger("system");
//        issueId = requestContext.getParameterString("issueId");
//        contactId = requestContext.getParameterString("contactId");
//        formContactId = requestContext.getParameterString("formContactId");
//        relationshipDescription = requestContext.getParameterString("relationshipDescription");
    }

    public void setTape(Tape tape) {
//        tapeDescription = tape.getDescription();
//        tapeOwner = tape.getOwner() != null ? tape.getOwner().getId() : 0;
//        tapeName = tape.getTapeName();
        serialNumber = tape.getTapeSerialNumber();
        barcodeNumber = tape.getTapeBarcodeNumber();

        manufacturerId = tape.getManufacturerId();
        vendorId = tape.getVendorId();

        mediaType = tape.getMediaType();
        tapeLocation = tape.getTapeLocation();
        tapeRetention = tape.getTapeRetention();

        tapeSystem = tape.getTapeSystem();
        tapeStatus = tape.getTapeStatus();

//        tapeModelName = tape.getModelName();
//        tapeModelNumber = tape.getModelNumber();
//        tapeCost = tape.getPurchasePrice();
//        lastServicedOn = tape.getResetLastServiceDate();

//        purchaseYear = tape.getPurchaseYear();
//        purchaseMonth = tape.getPurchaseMonth();
//        purchaseDate = tape.getPurchaseDate();
        manufacturedYear = tape.getManufacturedYear();
        manufacturedMonth = tape.getManufacturedMonth();
        manufacturedDate = tape.getManufacturedDate();

        transactionYear = tape.getTransactionYear();
        transactionMonth = tape.getTransactionMonth();
        transactionDate = tape.getTransactionDate();

        transactionHour = tape.getTransactionHour();
        transactionMin = tape.getTransactionMin();

        moveYear = tape.getMoveYear();
        moveMonth = tape.getMoveMonth();
        moveDate = tape.getMoveDate();

        expireYear = tape.getExpireYear();
        expireMonth = tape.getExpireMonth();
        expireDate = tape.getExpireDate();

//        warrantyYear = tape.getWarrantyYear();
//        warrantyMonth = tape.getWarrantyMonth();
//        warrantyDate = tape.getWarrantyDate();
//        warrantyDuration = "";
//        system = tape.getTapeSystem();
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
//    public String getTapeName() {
//    return tapeName;
//}
    public String getSerialNumber() {
        return serialNumber;
    }
    public String getBarcodeNumber(){
        return barcodeNumber;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }
    public Integer getVendorId() {
        return vendorId;
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

    public String getManufacturedDate() {
        return manufacturedDate;
    }
    public String getManufacturedMonth() {
        return manufacturedMonth;
    }
    public String getManufacturedYear() {
        return manufacturedYear;
    }

    public String getTransactionDate() {
        return transactionDate;
    }
    public String getTransactionMonth() {
        return transactionMonth;
    }
    public String getTransactionYear() {
        return transactionYear;
    }

    public String getTransactionHour() {
        return transactionHour;
    }
    public String getTransactionMin() {
        return transactionMin;
    }

    public String getMoveDate() {
        return moveDate;
    }
    public String getMoveMonth() {
        return moveMonth;
    }
    public String getMoveYear() {
        return moveYear;
    }

    public String getExpireDate() {
        return expireDate;
    }
    public String getExpireMonth() {
        return expireMonth;
    }
    public String getExpireYear() {
        return expireYear;
    }

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
    public Integer getMediaType() {
        return mediaType;
    }
    public Integer getTapeLocation() {
        return tapeLocation;
    }
    public Integer getTapeRetention() {
        return tapeRetention;
    }

    public Integer getTapeSystem() {
        return tapeSystem;
    }
    public Integer getTapeStatus() {
        return tapeStatus;
    }
//    public Integer getTapeOwner() {
//        return tapeOwner;
//    }

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