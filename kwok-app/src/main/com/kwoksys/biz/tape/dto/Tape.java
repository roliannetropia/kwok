package com.kwoksys.biz.tape.dto;

import com.kwoksys.action.tape.TapeForm;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.base.BaseObject;
import com.kwoksys.biz.system.core.Attributes;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.session.CacheManager;
import com.kwoksys.framework.util.CurrencyUtils;
import com.kwoksys.framework.util.DatetimeUtils;

import java.util.Date;

/**
 * Tape Object.
 */
public class Tape extends BaseObject {

    public static final String ID = "tape_id";
    public static final String TAPE_NAME = "tape_name";
    public static final String TAPE_BARCODE_NUMBER = "tape_barcode_number";
    public static final String TAPE_SERIAL_NUMBER = "tape_serial_number";

//    public static final String TAPE_DESCRIPTION = "tape_description";
//    public static final String MODEL_NAME = "tape_model_name";
//
//    public static final String MODEL_NUMBER = "tape_model_number";
//
//    public static final String SERVICE_DATE = "tape_last_service_date";

    public static final String MEDIA_TYPE = "media_type";
    public static final String TAPE_STATUS = "tape_status";
    public static final String TAPE_LOCATION = "tape_location";
//
//    public static final String PURCAHSE_PRICE = "tape_purchase_price";
//    public static final String PURCHASE_DATE = "tape_purchase_date";
//    public static final String WARRANTY_EXPIRATION = "tape_warranty_expire_date";

    public static final String MANUFACTURER_NAME = "tape_manufacturer_name";
//    public static final String MANUFACTURED_DATE = "tapeManufacturedDate";
    public static final String VENDOR_NAME = "tape_vendor_name";

    public static final String TAPE_RETENTION = "tape_retention";
    public static final String TAPE_SYSTEM = "tape_system";
//
//    public static final String OWNER_NAME = "tape_owner_name";
//    public static final String OWNER_USERNAME = "tape_owner_username";
//    public static final String OWNER_DISPLAY_NAME = "tape_owner_display_name";
//    public static final String OWNER_ID = "tape_owner_id";

    private String tapeName;
    private String tapeSerialNumber;
    private String tapeBarcodeNumber;

    private Integer manufacturerId;
    private String manufacturerName;
    private Integer vendorId;
    private String vendorName;

    private Integer tapeRetention;
    private Integer tapeSystem;

    private Integer tapeLocation;
    private Integer mediaType;
    private Integer tapeStatus;

//    private String modelName;
//    private String modelNumber;
//    private String description;
//    private AccessUser owner;
//    private Integer ownerId;

//    private Date tapePurchaseDate;
//    private String purchaseDate = "";
//    private String purchaseMonth = "";
//    private String purchaseYear = "";

//    private Date tapeManufacturedDate;
//    private String manufacturedDate = "";
//    private String manufacturedMonth = "";
//    private String manufacturedYear = "";

//    private Date warrantyExpireDate;
//    private String warrantyDate = "";
//    private String warrantyMonth = "";
//    private String warrantyYear = "";
//    private Date lastServicedOn;
//    private int resetLastServiceDate;
//    private String purchasePrice;
//    private double purchasePriceRaw;

//    private int countSoftware;
//    private int countComponent;
//    private int countFile;
//    private Integer fileId;
//    private boolean validTapeCost = true;

    public Tape() throws DatabaseException {
        super(ObjectTypes.TAPE);

        tapeName = "";
        tapeSerialNumber = "";
        tapeBarcodeNumber = "";
//
//        modelName = "";
//        modelNumber = "";

        manufacturerId = 0;
        vendorId = 0;

        tapeRetention = 0;
        tapeSystem = 0;
//
//        resetLastServiceDate = 0;

        tapeLocation = 0;
//        mediaType = new CacheManager().getSystemAttrCache(Attributes.MEDIA_TYPE).getDefaultAttrFieldId();
        tapeStatus = 0;
//
//        countSoftware = 0;
//        countComponent = 0;
//        countFile = 0;
//        fileId = 0;
    }

    public void setForm(TapeForm actionForm) {
        setTapeName(actionForm.getTapeName());
        setTapeSerialNumber(actionForm.getSerialNumber());
        setTapeBarcodeNumber(actionForm.getBarcodeNumber());
//        setDescription(actionForm.getTapeDescription());
//        setOwnerId(actionForm.getTapeOwner());

        setManufacturerId(actionForm.getManufacturerId());
        setVendorId(actionForm.getVendorId());
//
//        setTapeLocation(actionForm.getTapeLocation());
//        setMediaType(actionForm.getMediaType());
//        setTapeStatus(actionForm.getTapeStatus());

//        setModelName(actionForm.getTapeModelName());
//        setModelNumber(actionForm.getTapeModelNumber());
//        setPurchasePrice(actionForm.getTapeCost());
//        setResetLastServiceDate(actionForm.getLastServicedOn());
//        setTapePurchaseDate(actionForm.getPurchaseYear(), actionForm.getPurchaseMonth(), actionForm.getPurchaseDate());
//        setTapeWarrantyExpireDate(actionForm.getWarrantyYear(), actionForm.getWarrantyMonth(), actionForm.getWarrantyDate());
    }

//    public void setTapePurchaseDate(String fullDate) {
//        if (!fullDate.isEmpty()) {
//            String[] dateArray = fullDate.split("-");
//
//            purchaseYear = dateArray[0];
//            purchaseMonth = dateArray[1];
//            purchaseDate = dateArray[2];
//        } else {
//            purchaseYear = "";
//            purchaseMonth = "";
//            purchaseDate = "";
//        }
//    }

//    public void setTapeManufacturedDate(String fullDate) {
//        if (!fullDate.isEmpty()) {
//            String[] dateArray = fullDate.split("-");
//
//            manufacturedYear = dateArray[0];
//            manufacturedMonth = dateArray[1];
//            manufacturedDate = dateArray[2];
//        } else {
//            manufacturedYear = "";
//            manufacturedMonth = "";
//            manufacturedDate = "";
//        }
//    }
//
//    public void setTapePurchaseDate(String year, String month, String date) {
//        purchaseYear = year;
//        purchaseMonth = month;
//        purchaseDate = date;
////    }
//
//    public void setTapeManufacturedDate(String year, String month, String date) {
//        manufacturedYear = year;
//        manufacturedMonth = month;
//        manufacturedDate = date;
//    }
////
//    public boolean hasTapePurchaseDate() {
//        return (!purchaseYear.isEmpty() || !purchaseMonth.isEmpty() || !purchaseDate.isEmpty());
//    }

//    public boolean hasTapeManufacturedDate() {
//        return (!manufacturedYear.isEmpty() || !manufacturedMonth.isEmpty() || !manufacturedDate.isEmpty());
//    }

    /**
     * Return whether purchase date is in valid format.
     *
     * @return ..
     */
//
//    public boolean isValidPurchaseDate() {
//        return DatetimeUtils.isValidDate(purchaseYear, purchaseMonth, purchaseDate);
//    }

//    public boolean isValidManufacturedDate() {
//        return DatetimeUtils.isValidDate(manufacturedYear, manufacturedMonth, manufacturedDate);
//    }
//
//    public void setTapeWarrantyExpireDate(String fullDate) {
//        if (!fullDate.isEmpty()) {
//            String[] dateArray = fullDate.split("-");
//
//            warrantyYear = dateArray[0];
//            warrantyMonth = dateArray[1];
//            warrantyDate = dateArray[2];
//        } else {
//            warrantyYear = "";
//            warrantyMonth = "";
//            warrantyDate = "";
//        }
//    }
//    public void setTapeWarrantyExpireDate(String year, String month, String date) {
//        warrantyYear = year;
//        warrantyMonth = month;
//        warrantyDate = date;
//    }
//    public boolean hasTapeWarrantyExpireDate() {
//        return (!warrantyYear.isEmpty() || !warrantyMonth.isEmpty() || !warrantyDate.isEmpty());
//    }

    /**
     * Return whether warranty expiration date is in valid format.
     *
     * @return ..
     */
//
//    public boolean isValidWarrantyExpireDate() {
//        return DatetimeUtils.isValidDate(warrantyYear, warrantyMonth, warrantyDate);
//    }
//
//    public boolean isAttrEmpty(String attrName) {
//        if (attrName.equals(Tape.TAPE_STATUS)) {
//            return tapeStatus == 0;
//
//        } else if (attrName.equals(Tape.TAPE_LOCATION)) {
//            return tapeLocation == 0;
//
//        } else if (attrName.equals(Tape.MEDIA_TYPE)) {
//            return mediaType == 0;
//        }
//        return false;
//    }

//    public String getWarrantyExpireDateString() {
//        return DatetimeUtils.createDatetimeString(warrantyYear, warrantyMonth, warrantyDate);
//    }
//
//    public String getTapePurchaseDateString() {
//        return DatetimeUtils.createDatetimeString(purchaseYear, purchaseMonth, purchaseDate);
//    }



    /**
     * Checks whether tapeCost is in valid format.
     *
     * @return ..
     */
//
//    public boolean isValidTapeCost() {
//        return validTapeCost;
//    }
//
//    public void setPurchasePrice(String purchasePrice) {
//        if (!purchasePrice.isEmpty()) {
//            purchasePrice = purchasePrice.replace(",", "");
//            if (CurrencyUtils.isValidFormat(purchasePrice)) {
//                this.purchasePriceRaw = Double.parseDouble(purchasePrice);
//            } else {
//                validTapeCost = false;
//            }
//            this.purchasePrice = purchasePrice;
//        } else {
//            this.purchasePriceRaw = 0;
//        }
//    }

    //
    // Getter and Setter
    //
    public String getTapeName() {
        return tapeName;
    }
    public void setTapeName(String tapeName) {
        this.tapeName = tapeName;
    }
    public String getTapeSerialNumber() {
        return tapeSerialNumber;
    }
    public void setTapeSerialNumber(String tapeSerialNumber) {
        this.tapeSerialNumber = tapeSerialNumber;
    }
//    public String getModelName() {
//        return modelName;
//    }
//    public void setModelName(String modelName) {
//        this.modelName = modelName;
//    }
//    public String getModelNumber() {
//        return modelNumber;
//    }
//    public void setModelNumber(String modelNumber) {
//        this.modelNumber = modelNumber;
//    }
//    public String getDescription() {
//        return description;
//    }
//    public void setDescription(String description) {
//        this.description = description;
//    }
//    public String getLastServicedOn() {
//        return DatetimeUtils.toLocalDate(lastServicedOn);
//    }
//    public void setLastServicedOn(Date lastServicedOn) {
//        this.lastServicedOn = lastServicedOn;
//    }

//    public String getTapeManufacturedDateString() {
//    return DatetimeUtils.createDatetimeString(manufacturedYear, manufacturedMonth, manufacturedDate);
//}
//    public Date getTapeManufacturedDate() {
//        return tapeManufacturedDate;
//    }
//    public String getManufacturedDate() {
//        return manufacturedDate;
//    }
//    public String getManufacturedMonth() {
//        return manufacturedMonth;
//    }
//    public String getManufacturedYear() {
//        return manufacturedYear;
//    }
//    public void setTapeManufacturedDate(Date tapeManufacturedDate) {
//        this.tapeManufacturedDate = tapeManufacturedDate;
//    }
//    public void setManufacturedDate(String manufacturedDate) {
//        this.manufacturedDate = manufacturedDate;
//    }
//    public void setManufacturedMonth(String manufacturedMonth) {
//        this.manufacturedMonth = manufacturedMonth;
//    }
//    public void setManufacturedYear(String manufacturedYear) {
//        this.manufacturedYear = manufacturedYear;
//    }
//
//    public String getWarrantyDate() {
//        return warrantyDate;
//    }
//    public String getWarrantyYear() {
//        return warrantyYear;
//    }
//    public String getWarrantyMonth() {
//        return warrantyMonth;
//    }
//    public double getPurchasePriceRaw() {
//        return purchasePriceRaw;
//    }
//    public String getPurchasePrice() {
//        return purchasePrice;
//    }

//    public Integer getTapeLocation() {
//        return tapeLocation;
//    }
//    public void setTapeLocation(Integer tapeLocation) {
//        this.tapeLocation = tapeLocation;
//    }
//    public Integer getMediaType() {
//        return mediaType;
//    }
//    public void setMediaType(Integer mediaType) {
//        this.mediaType = mediaType;
//    }
//    public Integer getTapeStatus() {
//        return tapeStatus;
//    }
//    public void setTapeStatus(Integer tapeStatus) {
//        this.tapeStatus = tapeStatus;
//    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }
    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }
    public String getManufacturerName() {
        return manufacturerName;
    }
    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }
//
    public String getVendorName() {
        return vendorName;
    }
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    public Integer getVendorId() {
        return vendorId;
    }
    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

//
//    public int getResetLastServiceDate() {
//        return resetLastServiceDate;
//    }
//    public void setResetLastServiceDate(int resetLastServiceDate) {
//        this.resetLastServiceDate = resetLastServiceDate;
//    }
//    public int getCountSoftware() {
//        return countSoftware;
//    }
//    public int getCountFile() {
//        return countFile;
//    }
//    public Integer getFileId() {
//        return fileId;
//    }
//    public void setTapePurchaseDate(Date tapePurchaseDate) {
//        this.tapePurchaseDate = tapePurchaseDate;
//    }
//    public void setCountSoftware(int countSoftware) {
//        this.countSoftware = countSoftware;
//    }
//    public void setCountFile(int countFile) {
//        this.countFile = countFile;
//    }
//    public int getCountComponent() {
//        return countComponent;
//    }
//    public void setCountComponent(int countComponent) {
//        this.countComponent = countComponent;
//    }
//    public Date getWarrantyExpireDate() {
//        return warrantyExpireDate;
//    }
//    public void setWarrantyExpireDate(Date warrantyExpireDate) {
//        this.warrantyExpireDate = warrantyExpireDate;
//    }
//    public AccessUser getOwner() {
//        return owner;
//    }
//    public void setOwner(AccessUser owner) {
//        this.owner = owner;
//    }
//    public Integer getOwnerId() {
//        return ownerId;
//    }
//    public void setOwnerId(Integer ownerId) {
//        this.ownerId = ownerId;
//    }


//    public Integer getTapeRetention() {
//        return tapeRetention;
//    }
//    public void setTapeRetention(Integer tapeRetention) {
//        this.tapeRetention = tapeRetention;
//    }
//    public Integer getTapeSystem() {
//        return tapeSystem;
//    }
//    public void setTapeSystem(Integer tapeSystem) {
//        this.tapeSystem = tapeSystem;
//    }
    public String getTapeBarcodeNumber() {
        return tapeBarcodeNumber;
    }
    public void setTapeBarcodeNumber(String tapeBarcodeNumber) {
        this.tapeBarcodeNumber = tapeBarcodeNumber;
    }

//    public String getPurchaseYear() {
//        return purchaseYear;
//    }
//
//    public String getPurchaseMonth() {
//        return purchaseMonth;
//    }
//
//    public String getPurchaseDate() {
//        return purchaseDate;
//    }
//
//    public Date getTapePurchaseDate() {
//        return tapePurchaseDate;
//    }
}
