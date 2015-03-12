package com.kwoksys.biz.tape.core;

import com.kwoksys.action.tape.TapeSearchForm;
import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.biz.tape.dao.TapeQueries;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.SqlUtils;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is for building search queries.
 */
public class TapeSearch extends BaseSearch {

    public static final String TAPE_ID_EQUALS = "tapeIdEquals";

    public static final String TAPE_ID_NOT_EQUALS = "tapeIdNotEquals";

    public static final String TAPE_NAME_EQUALS = "tapeNameEquals";

    public static final String TAPE_NAME_BEGINS_WITH = "tapeNameBeginsWith";

    public static final String MEDIA_TYPE_CONTAINS = "tapeTypeContains";

    public static final String TAPE_PURCHASED_BEFORE = "purchasedBefore";

    public static final String TAPE_PURCHASED_AFTER = "purchasedAfter";

    public static final String TAPE_MODEL_NAME_CONTAINS = "modelNameContains";
    public static final String TAPE_MODEL_NAME_EQUALS = "modelNameEquals";

    public static final String TAPE_MODEL_NUMBER_EQUALS = "modelNumberEquals";

    public static final String TAPE_SERIAL_NUMBER_CONTAINS = "serialNumberContains";
    public static final String TAPE_SERIAL_NUMBER_EQUALS = "serialNumberEquals";
    public static final String TAPE_BARCODE_NUMBER_CONTAINS = "barcodeNumberContains";
    public static final String TAPE_BARCODE_NUMBER_EQUALS = "barcodeNumberEquals";

    public static final String TAPE_WARRANTY_EXPIRED = "warrantyExpired";
    public static final String TAPE_WARRANTY_NOT_EXPIRED = "warrantyNotExpired";
    public static final String TAPE_WARRANTY_NOT_SET = "warrantyNotSet";

    public static final String TAPE_VENDOR_EQUALS = "vendorIdEquals";
    public static final String TAPE_MANUFACTURER_EQUALS = "manufacturerIdEquals";

    public TapeSearch(RequestContext requestContext, String sessionKey) {
        super(requestContext, sessionKey);
    }

    public TapeSearch() {}

    /**
     * This would generate searchCriteriaMap.
     *
     * @return ..
     */
    public void prepareMap(TapeSearchForm tapeSearchForm) {
        String cmd = requestContext.getParameterString("cmd");
        String attrId = tapeSearchForm.getAttrId();
        String attrValue = tapeSearchForm.getAttrValue();
        String compTypeId = tapeSearchForm.getCompTypeId();
        String compValue = tapeSearchForm.getCompValue();
        String tapeNameCriteria = tapeSearchForm.getTapeNameCriteria();
        String tapeName = tapeSearchForm.getTapeName();
        String description = tapeSearchForm.getDescription();

        if (!cmd.isEmpty()) {
            if (cmd.equals("filter")) {
                Integer mediaType = requestContext.getParameterInteger("mediaType");
                if (mediaType != null) {
                    tapeSearchForm.setMediaTypes(Arrays.asList(mediaType));
                    tapeSearchForm.setMediaType(mediaType);
                    searchCriteriaMap.put(TapeSearch.MEDIA_TYPE_CONTAINS, tapeSearchForm.getMediaTypes());
                }
            } else {
                reset();
                tapeSearchForm.setRequest(requestContext);
                if (cmd.equals("groupBy")) {
                    Integer mediaType = tapeSearchForm.getMediaType();
                    if (mediaType != null) {
                        tapeSearchForm.setMediaTypes(Arrays.asList(mediaType));
                        tapeSearchForm.setMediaType(mediaType);
                        searchCriteriaMap.put(com.kwoksys.biz.tape.core.TapeSearch.MEDIA_TYPE_CONTAINS, tapeSearchForm.getMediaTypes());
                    }
                    Integer tapeSystem = requestContext.getParameterInteger("tapeSystem");
                    if (tapeSystem != null) {
                        tapeSearchForm.setTapeSystem(Arrays.asList(tapeSystem));
                        searchCriteriaMap.put("tapeSystemContains", tapeSearchForm.getTapeSystem());
                    }
                    Integer tapeStatus = requestContext.getParameterInteger("tapeStatus");
                    if (tapeStatus != null) {
                        tapeSearchForm.setTapeStatus(Arrays.asList(tapeStatus));
                        searchCriteriaMap.put("tapeStatusContains", tapeSearchForm.getTapeStatus());
                    }
                    Integer tapeLocation = requestContext.getParameterInteger("tapeLocation");
                    if (tapeLocation != null) {
                        tapeSearchForm.setTapeLocation(Arrays.asList(tapeLocation));
                        searchCriteriaMap.put("tapeLocationContains", tapeSearchForm.getTapeLocation());
                    }

                } else if (cmd.equals("search")) {
                    // Search by Tape Id equals something.
//                    String tapeId = tapeSearchForm.getTapeId();
//                    if (!tapeId.isEmpty()) {
//                        searchCriteriaMap.put(TAPE_ID_EQUALS, tapeId);
//                    }

                    // Search by Tape model name equals something.
//                    if (!tapeName.isEmpty()) {
//                        if (tapeNameCriteria.equals("equals")) {
//                            searchCriteriaMap.put(TAPE_NAME_EQUALS, tapeName);
//                        } else if (tapeNameCriteria.equals("contains")) {
//                            searchCriteriaMap.put("tapeNameContains", tapeName);
//                        } else if (tapeNameCriteria.equals("begins")) {
//                            searchCriteriaMap.put(TAPE_NAME_BEGINS_WITH, tapeName);
//                        }
//                    }
                    // Search by tape description
//                    if (!description.isEmpty()) {
//                        searchCriteriaMap.put("tapeDescription", description);
//                    }
                    // Search by Tape model name equals something.
//                    String tapeModelName = tapeSearchForm.getTapeModelName();
//                    if (!tapeModelName.isEmpty()) {
//                        searchCriteriaMap.put(TAPE_MODEL_NAME_CONTAINS, tapeModelName);
//                    }

                    // Search by Tape model number equals something.
//                    String tapeModelNumber = tapeSearchForm.getTapeModelNumber();
//                    if (!tapeModelNumber.isEmpty()) {
//                        searchCriteriaMap.put("modelNumberContains", tapeModelNumber);
//                    }

                    // Search by Tape serial number equals something.
                    String tapeSerialNumber = tapeSearchForm.getSerialNumber();
                    if (!tapeSerialNumber.isEmpty()) {
                        searchCriteriaMap.put(TAPE_SERIAL_NUMBER_CONTAINS, tapeSerialNumber);
                    }

                    // Search by Tape serial number equals something.
                    String tapeBarcodeNumber = tapeSearchForm.getBarcodeNumber();
                    if (!tapeBarcodeNumber.isEmpty()) {
                        searchCriteriaMap.put(TAPE_BARCODE_NUMBER_CONTAINS, tapeBarcodeNumber);
                    }

                    // Search by Tape Owner (userDisplayName) contains something.
//                    String tapeOwner = tapeSearchForm.getTapeOwner();
//                    if (!tapeOwner.isEmpty()) {
//                        searchCriteriaMap.put("tapeOwnerContains", tapeOwner);
//                    }

                    // Search by Media type.
//                    List<Integer> typeList = tapeSearchForm.getMediaTypes();
//                    if (!typeList.isEmpty()) {
//                        searchCriteriaMap.put(MEDIA_TYPE_CONTAINS, typeList);
//                    }
//                    // Search by Tape status.
//                    List<Integer> statusList = tapeSearchForm.getTapeStatus();
//                    if (!statusList.isEmpty()) {
//                        searchCriteriaMap.put("tapeStatusContains", statusList);
//                    }
//                    // Search by Tape status.
//                    List<Integer> systemList = tapeSearchForm.getTapeSystem();
//                    if (!systemList.isEmpty()) {
//                        searchCriteriaMap.put("tapeSystemContains", systemList);
//                    }
//                    // Search by Tape location.
//                    List<Integer> locationList = tapeSearchForm.getTapeLocation();
//                    if (!locationList.isEmpty()) {
//                        searchCriteriaMap.put("tapeLocationContains", locationList);
//                    }
//                    // Search by Tape Manufacturer
//                    Integer manufacturerId = tapeSearchForm.getManufacturerId();
//                    if (manufacturerId != 0) {
//                        searchCriteriaMap.put(TAPE_MANUFACTURER_EQUALS, manufacturerId);
//                    }
//                    // Search by Tape Manufacturer
//                    Integer vendorId = tapeSearchForm.getVendorId();
//                    if (vendorId != 0) {
//                        searchCriteriaMap.put(TAPE_VENDOR_EQUALS, vendorId);
//                    }

                    // Search by purchase date
//                    String purchasedAfterDate = tapeSearchForm.getPurchasedAfterDate();
//                    String purchasedAfterMonth = tapeSearchForm.getPurchasedAfterMonth();
//                    String purchasedAfterYear = tapeSearchForm.getPurchasedAfterYear();

//                    if (DatetimeUtils.isValidDate(purchasedAfterYear, purchasedAfterMonth, purchasedAfterDate)) {
//                        searchCriteriaMap.put(TAPE_PURCHASED_AFTER, purchasedAfterYear + "-" + purchasedAfterMonth + "-" + purchasedAfterDate);
//                    }

                    // Search by purchased before
//                    String purchasedBeforeDate = tapeSearchForm.getPurchasedBeforeDate();
//                    String purchasedBeforeMonth = tapeSearchForm.getPurchasedBeforeMonth();
//                    String purchasedBeforeYear = tapeSearchForm.getPurchasedBeforeYear();

//                    if (DatetimeUtils.isValidDate(purchasedBeforeYear, purchasedBeforeMonth, purchasedBeforeDate)) {
//                        searchCriteriaMap.put(TAPE_PURCHASED_BEFORE, purchasedBeforeYear + "-" + purchasedBeforeMonth + "-" + purchasedBeforeDate);
//                    }

//                    boolean warrantyExpired = requestContext.getParameterBoolean(TAPE_WARRANTY_EXPIRED);
//                    boolean warrantyNotExpired = requestContext.getParameterBoolean(TAPE_WARRANTY_NOT_EXPIRED);
//                    boolean warrantyNotSet = requestContext.getParameterBoolean(TAPE_WARRANTY_NOT_SET);

//                    if (warrantyExpired && warrantyNotExpired && warrantyNotSet) {
//                        Search all
//                    } else {
//                        if (warrantyExpired) {
//                            searchCriteriaMap.put(TAPE_WARRANTY_EXPIRED, true);
//                        }
//                        if (warrantyNotExpired) {
//                            searchCriteriaMap.put(TAPE_WARRANTY_NOT_EXPIRED, true);
//                        }
//                        if (warrantyNotSet) {
//                            searchCriteriaMap.put(TAPE_WARRANTY_NOT_SET, true);
//                        }
//                    }

                    // Search by custom fields
//                    if (!attrId.isEmpty() && !attrValue.isEmpty()) {
//                        searchCriteriaMap.put("attrId", attrId);
//                        searchCriteriaMap.put("attrValue", attrValue);
//                    }
//                    // Search by tape components
//                    if (!compTypeId.isEmpty() && !compValue.isEmpty()) {
//                        searchCriteriaMap.put("compTypeId", compTypeId);
//                        searchCriteriaMap.put("compValue", compValue);
//                    }
                }
            }
        }
    }

    /**
     * This would take searchCriteriaMap and compose the sql queries.
     */
    public void applyMap(QueryBits query) {
        if (searchCriteriaMap == null) {
            return;
        }
        // For Tape Id
//        if (searchCriteriaMap.containsKey(TAPE_ID_EQUALS)) {
//            query.appendWhereClause("at.tape_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(TAPE_ID_EQUALS)));
//        }
//        // For custom fields
//        if (searchCriteriaMap.containsKey("attrId") && searchCriteriaMap.containsKey("attrValue")) {
//            query.appendWhereClause("at.tape_id in (select object_id from object_attribute_value where attribute_id = "+
//                    SqlUtils.encodeInteger(searchCriteriaMap.get("attrId")) + " and lower(attr_value) like lower('%"
//                    + SqlUtils.encodeString(searchCriteriaMap.get("attrValue")) +"%'))");
//        }
//        // For tape components
//        if (searchCriteriaMap.containsKey("compTypeId") && searchCriteriaMap.containsKey("compValue")) {
//            query.appendWhereClause("at.tape_id in (select tape_id from asset_tape_component where tape_component_type = "+
//                    SqlUtils.encodeInteger(searchCriteriaMap.get("compTypeId")) + " and lower(comp_description) like lower('%"
//                    + SqlUtils.encodeString(searchCriteriaMap.get("compValue")) +"%'))");
//        }
        // For Tape name (without "%")
//        if (searchCriteriaMap.containsKey(TAPE_NAME_EQUALS)) {
//            query.appendWhereClause("lower(at.tape_name) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(TAPE_NAME_EQUALS)) + "')");
//        }
//
//        if (searchCriteriaMap.containsKey(TAPE_ID_NOT_EQUALS)) {
//            query.appendWhereClause("at.tape_id != " + SqlUtils.encodeInteger(searchCriteriaMap.get(TAPE_ID_NOT_EQUALS)));
//        }
//
//        // For Tape name
//        if (searchCriteriaMap.containsKey("tapeNameContains")) {
//            query.appendWhereClause("lower(at.tape_name) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("tapeNameContains")) + "%')");
//        }
//
//        // For Tape name
//        if (searchCriteriaMap.containsKey(TAPE_NAME_BEGINS_WITH)) {
//            query.appendWhereClause("lower(at.tape_name) like lower('" + SqlUtils.encodeString(searchCriteriaMap.get(TAPE_NAME_BEGINS_WITH)) + "%')");
//        }

        // For tape description
//        if (searchCriteriaMap.containsKey("tapeDescription")) {
//            query.appendWhereClause("lower(at.tape_description) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("tapeDescription")) + "%')");
//        }
        // For Tape model name
//        if (searchCriteriaMap.containsKey(TAPE_MODEL_NAME_CONTAINS)) {
//            query.appendWhereClause("lower(ah.tape_model_name) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get(TAPE_MODEL_NAME_CONTAINS)) + "%')");
//        }
//        if (searchCriteriaMap.containsKey(TAPE_MODEL_NAME_EQUALS)) {
//            if (((String)searchCriteriaMap.get(TAPE_MODEL_NAME_EQUALS)).isEmpty()) {
//                query.appendWhereClause("ah.tape_model_name is null");
//            } else {
//                query.appendWhereClause("lower(ah.tape_model_name) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(TAPE_MODEL_NAME_EQUALS)) + "')");
//            }
//        }
        // For Tape model number
//        if (searchCriteriaMap.containsKey("modelNumberContains")) {
//            query.appendWhereClause("lower(ah.tape_model_number) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("modelNumberContains")) + "%')");
//        }
//        if (searchCriteriaMap.containsKey(TAPE_MODEL_NUMBER_EQUALS)) {
//            if (((String)searchCriteriaMap.get(TAPE_MODEL_NUMBER_EQUALS)).isEmpty()) {
//                query.appendWhereClause("ah.tape_model_number is null");
//            } else {
//                query.appendWhereClause("lower(ah.tape_model_number) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(TAPE_MODEL_NUMBER_EQUALS)) + "')");
//            }
//        }
        // For Tape serial number
        if (searchCriteriaMap.containsKey(TAPE_SERIAL_NUMBER_CONTAINS)) {
            query.appendWhereClause("lower(at.serial_number) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("serialNumberContains")) + "%')");
        }
        if (searchCriteriaMap.containsKey(TAPE_SERIAL_NUMBER_EQUALS)) {
            query.appendWhereClause("lower(at.serial_number) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(TAPE_SERIAL_NUMBER_EQUALS)) + "')");
        }

        // For Tape barcode number
        if (searchCriteriaMap.containsKey(TAPE_BARCODE_NUMBER_CONTAINS)) {
            query.appendWhereClause("lower(at.barcode_number) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("barcodeNumberContains")) + "%')");
        }
        if (searchCriteriaMap.containsKey(TAPE_BARCODE_NUMBER_EQUALS)) {
            query.appendWhereClause("lower(at.barcode_number) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(TAPE_BARCODE_NUMBER_EQUALS)) + "')");
        }
        // For Tape owner number
//        if (searchCriteriaMap.containsKey("tapeOwnerContains")) {
//            query.appendWhereClause(TapeQueries.getOrderByColumn("tape_owner_name") + " like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("tapeOwnerContains")) + "%')");
//        }
//        if (searchCriteriaMap.containsKey("tapeOwnerId")) {
//            query.appendWhereClause("ah.tape_owner_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get("tapeOwnerId")));
//        }
        // For media type
//        if (searchCriteriaMap.containsKey(MEDIA_TYPE_CONTAINS)) {
//            query.appendWhereClause("at.media_type in (" + SqlUtils.encodeIntegers((List<Integer>) searchCriteriaMap.get(MEDIA_TYPE_CONTAINS)) + ")");
//        }
//        // For Tape system
//        if (searchCriteriaMap.containsKey("tapeSystemContains")) {
//            query.appendWhereClause("at.system in (" + SqlUtils.encodeIntegers((List<Integer>) searchCriteriaMap.get("tapeSystemContains")) + ")");
//        }
//        // For Tape status
//        if (searchCriteriaMap.containsKey("tapeStatusContains")) {
//            query.appendWhereClause("at.status in (" + SqlUtils.encodeIntegers((List<Integer>) searchCriteriaMap.get("tapeStatusContains")) + ")");
//        }
//        // For Tape location
//        if (searchCriteriaMap.containsKey("tapeLocationContains")) {
//            query.appendWhereClause("at.location in (" + SqlUtils.encodeIntegers((List<Integer>) searchCriteriaMap.get("tapeLocationContains")) + ")");
//        }
//        // For manufacturer by id
//        if (searchCriteriaMap.containsKey(TAPE_MANUFACTURER_EQUALS)) {
//            if (searchCriteriaMap.get(TAPE_MANUFACTURER_EQUALS).equals(0)) {
//                query.appendWhereClause("mftr.company_id is null");
//            } else {
//                query.appendWhereClause("mftr.company_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(TAPE_MANUFACTURER_EQUALS)));
//            }
//        }
//        // For vendor by id
//        if (searchCriteriaMap.containsKey(TAPE_VENDOR_EQUALS)) {
//            if (searchCriteriaMap.get(TAPE_VENDOR_EQUALS).equals(0)) {
//                query.appendWhereClause("vndr.company_id is null");
//            } else {
//                query.appendWhereClause("vndr.company_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(TAPE_VENDOR_EQUALS)));
//            }
//        }

        // Tape purchased after this date.
//        if (searchCriteriaMap.containsKey(TAPE_PURCHASED_AFTER)) {
//            query.appendWhereClause("ah.tape_purchase_date >= '" + SqlUtils.encodeString(searchCriteriaMap.get(TAPE_PURCHASED_AFTER)) + "'");
//        }
        // Tape purchased before this date.
//        if (searchCriteriaMap.containsKey(TAPE_PURCHASED_BEFORE)) {
//            query.appendWhereClause("ah.tape_purchase_date <= '" + SqlUtils.encodeString(searchCriteriaMap.get(TAPE_PURCHASED_BEFORE)) + "'");
//        }

        // Warranty expiration filter
//        Set<String> warranty = new HashSet();
//        if (searchCriteriaMap.containsKey(TAPE_WARRANTY_EXPIRED)) {
//            warranty.add("ah.tape_warranty_expire_date < now()");
//        }
//        if (searchCriteriaMap.containsKey(TAPE_WARRANTY_NOT_EXPIRED)) {
//            warranty.add("ah.tape_warranty_expire_date > now()");
//
//        } else if (searchCriteriaMap.containsKey(TAPE_WARRANTY_NOT_SET)) {
//            warranty.add("ah.tape_warranty_expire_date is null");
//        }

//        String warrantyWhereClause = StringUtils.join(warranty, " or ");

//        if (!warrantyWhereClause.isEmpty()) {
//            query.appendWhereClause("(" + warrantyWhereClause + ")");
//        }
    }
}