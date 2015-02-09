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

import com.kwoksys.action.reports.ReportForm;
import com.kwoksys.framework.http.RequestContext;

import java.util.List;

/**
 * ActionForm for Tape index page.
 */
public class TapeSearchForm extends ReportForm {

    private String cmd;
    private String tapeId;
    private String serialNumber;
    private String barcodeNumber;
    private String tapeName;
    private String description;
    private String tapeModelName;
    private String tapeModelNumber;
    private String tapeOwner;
    private List<Integer> tapeTypes;
    private Integer tapeType;
    private List<Integer> tapeStatus;
    private List<Integer> tapeLocation;
    private String tapeNameCriteria;
    private Integer manufacturerId;
    private Integer vendorId;
    private String attrId;
    private String attrValue;
    private String compTypeId;
    private String compValue;
    private String purchasedAfterMonth;
    private String purchasedAfterDate;
    private String purchasedAfterYear;
    private String purchasedBeforeMonth;
    private String purchasedBeforeDate;
    private String purchasedBeforeYear;

    @Override
    public void setRequest(RequestContext requestContext) {
        cmd = requestContext.getParameterString("cmd");
        tapeId = requestContext.getParameterString("tapeId");
        serialNumber = requestContext.getParameterString("serialNumber");
        barcodeNumber = requestContext.getParameterString("barcodeNumber");
        tapeName = requestContext.getParameterString("tapeName");
        description = requestContext.getParameterString("description");
        tapeModelName = requestContext.getParameterString("tapeModelName");
        tapeModelNumber = requestContext.getParameterString("tapeModelNumber");
        tapeOwner = requestContext.getParameterString("tapeOwner");
        tapeTypes = requestContext.getParameters("tapeTypes");
        tapeType = requestContext.getParameterInteger("tapeType");
        tapeStatus = requestContext.getParameters("tapeStatus");
        tapeLocation = requestContext.getParameters("tapeLocation");
        tapeNameCriteria = requestContext.getParameterString("tapeNameCriteria");
        manufacturerId = requestContext.getParameterInteger("manufacturerId");
        vendorId = requestContext.getParameterInteger("vendorId");
        attrId = requestContext.getParameterString("attrId");
        attrValue = requestContext.getParameterString("attrValue");
        compTypeId = requestContext.getParameterString("compTypeId");
        compValue = requestContext.getParameterString("compValue");
        purchasedAfterMonth = requestContext.getParameterString("purchasedAfterMonth");
        purchasedAfterDate = requestContext.getParameterString("purchasedAfterDate");
        purchasedAfterYear = requestContext.getParameterString("purchasedAfterYear");
        purchasedBeforeMonth = requestContext.getParameterString("purchasedBeforeMonth");
        purchasedBeforeDate = requestContext.getParameterString("purchasedBeforeDate");
        purchasedBeforeYear = requestContext.getParameterString("purchasedBeforeYear");
    }

    public String getCmd() {
        return cmd;
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public String getTapeId() {
        return tapeId;
    }
    public void setTapeId(String tapeId) {
        this.tapeId = tapeId;
    }
    public String getTapeName() {
        return tapeName;
    }

    public String getDescription() {
        return description;
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

    public String getBarcodeNumber() {
        return barcodeNumber;
    }

    public String getTapeOwner() {
        return tapeOwner;
    }

    public List<Integer> getTapeTypes() {
        return tapeTypes;
    }

    public void setTapeTypes(List<Integer> tapeTypes) {
        this.tapeTypes = tapeTypes;
    }

    public List<Integer> getTapeStatus() {
        return tapeStatus;
    }

    public List<Integer> getTapeLocation() {
        return tapeLocation;
    }

    public String getTapeNameCriteria() {
        return tapeNameCriteria;
    }

    public String getAttrId() {
        return attrId;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public Integer getTapeType() {
        return tapeType;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public String getCompTypeId() {
        return compTypeId;
    }

    public String getCompValue() {
        return compValue;
    }

    public String getPurchasedAfterMonth() {
        return purchasedAfterMonth;
    }

    public String getPurchasedAfterDate() {
        return purchasedAfterDate;
    }

    public String getPurchasedAfterYear() {
        return purchasedAfterYear;
    }

    public String getPurchasedBeforeMonth() {
        return purchasedBeforeMonth;
    }

    public String getPurchasedBeforeDate() {
        return purchasedBeforeDate;
    }

    public String getPurchasedBeforeYear() {
        return purchasedBeforeYear;
    }

    public void setTapeType(Integer tapeType) {
        this.tapeType = tapeType;
    }

    public void setTapeStatus(List<Integer> tapeStatus) {
        this.tapeStatus = tapeStatus;
    }

    public void setTapeLocation(List<Integer> tapeLocation) {
        this.tapeLocation = tapeLocation;
    }
}
