package com.kwoksys.biz.tape.core;

import com.kwoksys.action.hardware.TapeSearchForm;
import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.biz.hardware.dao.TapeQueries;
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

    public static final String HARDWARE_ID_EQUALS = "hardwareIdEquals";

    public static final String HARDWARE_ID_NOT_EQUALS = "hardwareIdNotEquals";

    public static final String HARDWARE_NAME_EQUALS = "hardwareNameEquals";

    public static final String HARDWARE_NAME_BEGINS_WITH = "hardwareNameBeginsWith";

    public static final String HARDWARE_TYPE_CONTAINS = "hardwareTypeContains";

    public static final String HARDWARE_PURCHASED_BEFORE = "purchasedBefore";

    public static final String HARDWARE_PURCHASED_AFTER = "purchasedAfter";

    public static final String HARDWARE_MODEL_NAME_CONTAINS = "modelNameContains";
    public static final String HARDWARE_MODEL_NAME_EQUALS = "modelNameEquals";

    public static final String HARDWARE_MODEL_NUMBER_EQUALS = "modelNumberEquals";

    public static final String HARDWARE_SERIAL_NUMBER_CONTAINS = "serialNumberContains";
    public static final String HARDWARE_SERIAL_NUMBER_EQUALS = "serialNumberEquals";

    public static final String HARDWARE_WARRANTY_EXPIRED = "warrantyExpired";
    public static final String HARDWARE_WARRANTY_NOT_EXPIRED = "warrantyNotExpired";
    public static final String HARDWARE_WARRANTY_NOT_SET = "warrantyNotSet";

    public static final String HARDWARE_VENDOR_EQUALS = "vendorIdEquals";
    public static final String HARDWARE_MANUFACTURER_EQUALS = "manufacturerIdEquals";

    public TapeSearch(RequestContext requestContext, String sessionKey) {
        super(requestContext, sessionKey);
    }

    public TapeSearch() {}

    /**
     * This would generate searchCriteriaMap.
     *
     * @return ..
     */
    public void prepareMap(TapeSearchForm hardwareSearchForm) {
        String cmd = requestContext.getParameterString("cmd");
        String attrId = hardwareSearchForm.getAttrId();
        String attrValue = hardwareSearchForm.getAttrValue();
        String compTypeId = hardwareSearchForm.getCompTypeId();
        String compValue = hardwareSearchForm.getCompValue();
        String hardwareNameCriteria = hardwareSearchForm.getTapeNameCriteria();
        String hardwareName = hardwareSearchForm.getTapeName();
        String description = hardwareSearchForm.getDescription();

        if (!cmd.isEmpty()) {
            if (cmd.equals("filter")) {
                Integer hardwareType = requestContext.getParameterInteger("hardwareType");
                if (hardwareType != null) {
                    hardwareSearchForm.setTapeTypes(Arrays.asList(hardwareType));
                    hardwareSearchForm.setTapeType(hardwareType);
                    searchCriteriaMap.put(TapeSearch.HARDWARE_TYPE_CONTAINS, hardwareSearchForm.getTapeTypes());
                }
            } else {
                reset();
                hardwareSearchForm.setRequest(requestContext);

                if (cmd.equals("groupBy")) {
                    Integer hardwareType = hardwareSearchForm.getTapeType();
                    if (hardwareType != null) {
                        hardwareSearchForm.setTapeTypes(Arrays.asList(hardwareType));
                        hardwareSearchForm.setTapeType(hardwareType);
                        searchCriteriaMap.put(com.kwoksys.biz.hardware.core.TapeSearch.HARDWARE_TYPE_CONTAINS, hardwareSearchForm.getTapeTypes());
                    }
                    Integer hardwareStatus = requestContext.getParameterInteger("hardwareStatus");
                    if (hardwareStatus != null) {
                        hardwareSearchForm.setTapeStatus(Arrays.asList(hardwareStatus));
                        searchCriteriaMap.put("hardwareStatusContains", hardwareSearchForm.getTapeStatus());
                    }
                    Integer hardwareLocation = requestContext.getParameterInteger("hardwareLocation");
                    if (hardwareLocation != null) {
                        hardwareSearchForm.setTapeLocation(Arrays.asList(hardwareLocation));
                        searchCriteriaMap.put("hardwareLocationContains", hardwareSearchForm.getTapeLocation());
                    }

                } else if (cmd.equals("search")) {
                    // Search by Tape Id equals something.
                    String hardwareId = hardwareSearchForm.getTapeId();
                    if (!hardwareId.isEmpty()) {
                        searchCriteriaMap.put(HARDWARE_ID_EQUALS, hardwareId);
                    }

                    // Search by Tape model name equals something.
                    if (!hardwareName.isEmpty()) {
                        if (hardwareNameCriteria.equals("equals")) {
                            searchCriteriaMap.put(HARDWARE_NAME_EQUALS, hardwareName);
                        } else if (hardwareNameCriteria.equals("contains")) {
                            searchCriteriaMap.put("hardwareNameContains", hardwareName);
                        } else if (hardwareNameCriteria.equals("begins")) {
                            searchCriteriaMap.put(HARDWARE_NAME_BEGINS_WITH, hardwareName);
                        }
                    }
                    // Search by hardware description
                    if (!description.isEmpty()) {
                        searchCriteriaMap.put("hardwareDescription", description);
                    }
                    // Search by Tape model name equals something.
                    String hardwareModelName = hardwareSearchForm.getTapeModelName();
                    if (!hardwareModelName.isEmpty()) {
                        searchCriteriaMap.put(HARDWARE_MODEL_NAME_CONTAINS, hardwareModelName);
                    }

                    // Search by Tape model number equals something.
                    String hardwareModelNumber = hardwareSearchForm.getTapeModelNumber();
                    if (!hardwareModelNumber.isEmpty()) {
                        searchCriteriaMap.put("modelNumberContains", hardwareModelNumber);
                    }

                    // Search by Tape serial number equals something.
                    String hardwareSerialNumber = hardwareSearchForm.getSerialNumber();
                    if (!hardwareSerialNumber.isEmpty()) {
                        searchCriteriaMap.put(HARDWARE_SERIAL_NUMBER_CONTAINS, hardwareSerialNumber);
                    }

                    // Search by Tape Owner (userDisplayName) contains something.
                    String hardwareOwner = hardwareSearchForm.getTapeOwner();
                    if (!hardwareOwner.isEmpty()) {
                        searchCriteriaMap.put("hardwareOwnerContains", hardwareOwner);
                    }

                    // Search by Tape type.
                    List<Integer> typeList = hardwareSearchForm.getTapeTypes();
                    if (!typeList.isEmpty()) {
                        searchCriteriaMap.put(HARDWARE_TYPE_CONTAINS, typeList);
                    }
                    // Search by Tape status.
                    List<Integer> statusList = hardwareSearchForm.getTapeStatus();
                    if (!statusList.isEmpty()) {
                        searchCriteriaMap.put("hardwareStatusContains", statusList);
                    }
                    // Search by Tape location.
                    List<Integer> locationList = hardwareSearchForm.getTapeLocation();
                    if (!locationList.isEmpty()) {
                        searchCriteriaMap.put("hardwareLocationContains", locationList);
                    }
                    // Search by Tape Manufacturer
                    Integer manufacturerId = hardwareSearchForm.getManufacturerId();
                    if (manufacturerId != 0) {
                        searchCriteriaMap.put(HARDWARE_MANUFACTURER_EQUALS, manufacturerId);
                    }
                    // Search by Tape Manufacturer
                    Integer vendorId = hardwareSearchForm.getVendorId();
                    if (vendorId != 0) {
                        searchCriteriaMap.put(HARDWARE_VENDOR_EQUALS, vendorId);
                    }

                    // Search by purchase date
                    String purchasedAfterDate = hardwareSearchForm.getPurchasedAfterDate();
                    String purchasedAfterMonth = hardwareSearchForm.getPurchasedAfterMonth();
                    String purchasedAfterYear = hardwareSearchForm.getPurchasedAfterYear();

                    if (DatetimeUtils.isValidDate(purchasedAfterYear, purchasedAfterMonth, purchasedAfterDate)) {
                        searchCriteriaMap.put(HARDWARE_PURCHASED_AFTER, purchasedAfterYear + "-" + purchasedAfterMonth + "-" + purchasedAfterDate);
                    }

                    // Search by purchased before
                    String purchasedBeforeDate = hardwareSearchForm.getPurchasedBeforeDate();
                    String purchasedBeforeMonth = hardwareSearchForm.getPurchasedBeforeMonth();
                    String purchasedBeforeYear = hardwareSearchForm.getPurchasedBeforeYear();

                    if (DatetimeUtils.isValidDate(purchasedBeforeYear, purchasedBeforeMonth, purchasedBeforeDate)) {
                        searchCriteriaMap.put(HARDWARE_PURCHASED_BEFORE, purchasedBeforeYear + "-" + purchasedBeforeMonth + "-" + purchasedBeforeDate);
                    }

                    boolean warrantyExpired = requestContext.getParameterBoolean(HARDWARE_WARRANTY_EXPIRED);
                    boolean warrantyNotExpired = requestContext.getParameterBoolean(HARDWARE_WARRANTY_NOT_EXPIRED);
                    boolean warrantyNotSet = requestContext.getParameterBoolean(HARDWARE_WARRANTY_NOT_SET);

                    if (warrantyExpired && warrantyNotExpired && warrantyNotSet) {
                        // Search all
                    } else {
                        if (warrantyExpired) {
                            searchCriteriaMap.put(HARDWARE_WARRANTY_EXPIRED, true);
                        }
                        if (warrantyNotExpired) {
                            searchCriteriaMap.put(HARDWARE_WARRANTY_NOT_EXPIRED, true);
                        }
                        if (warrantyNotSet) {
                            searchCriteriaMap.put(HARDWARE_WARRANTY_NOT_SET, true);
                        }
                    }

                    // Search by custom fields
                    if (!attrId.isEmpty() && !attrValue.isEmpty()) {
                        searchCriteriaMap.put("attrId", attrId);
                        searchCriteriaMap.put("attrValue", attrValue);
                    }
                    // Search by hardware components
                    if (!compTypeId.isEmpty() && !compValue.isEmpty()) {
                        searchCriteriaMap.put("compTypeId", compTypeId);
                        searchCriteriaMap.put("compValue", compValue);
                    }
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
        if (searchCriteriaMap.containsKey(HARDWARE_ID_EQUALS)) {
            query.appendWhereClause("ah.hardware_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(HARDWARE_ID_EQUALS)));
        }
        // For custom fields
        if (searchCriteriaMap.containsKey("attrId") && searchCriteriaMap.containsKey("attrValue")) {
            query.appendWhereClause("ah.hardware_id in (select object_id from object_attribute_value where attribute_id = "+
                    SqlUtils.encodeInteger(searchCriteriaMap.get("attrId")) + " and lower(attr_value) like lower('%"
                    + SqlUtils.encodeString(searchCriteriaMap.get("attrValue")) +"%'))");
        }
        // For hardware components
        if (searchCriteriaMap.containsKey("compTypeId") && searchCriteriaMap.containsKey("compValue")) {
            query.appendWhereClause("ah.hardware_id in (select hardware_id from asset_hardware_component where hardware_component_type = "+
                    SqlUtils.encodeInteger(searchCriteriaMap.get("compTypeId")) + " and lower(comp_description) like lower('%"
                    + SqlUtils.encodeString(searchCriteriaMap.get("compValue")) +"%'))");
        }
        // For Tape name (without "%")
        if (searchCriteriaMap.containsKey(HARDWARE_NAME_EQUALS)) {
            query.appendWhereClause("lower(ah.hardware_name) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_NAME_EQUALS)) + "')");
        }

        if (searchCriteriaMap.containsKey(HARDWARE_ID_NOT_EQUALS)) {
            query.appendWhereClause("ah.hardware_id != " + SqlUtils.encodeInteger(searchCriteriaMap.get(HARDWARE_ID_NOT_EQUALS)));
        }        

        // For Tape name
        if (searchCriteriaMap.containsKey("hardwareNameContains")) {
            query.appendWhereClause("lower(ah.hardware_name) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("hardwareNameContains")) + "%')");
        }

        // For Tape name
        if (searchCriteriaMap.containsKey(HARDWARE_NAME_BEGINS_WITH)) {
            query.appendWhereClause("lower(ah.hardware_name) like lower('" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_NAME_BEGINS_WITH)) + "%')");
        }

        // For hardware description
        if (searchCriteriaMap.containsKey("hardwareDescription")) {
            query.appendWhereClause("lower(ah.hardware_description) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("hardwareDescription")) + "%')");
        }
        // For Tape model name
        if (searchCriteriaMap.containsKey(HARDWARE_MODEL_NAME_CONTAINS)) {
            query.appendWhereClause("lower(ah.hardware_model_name) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_MODEL_NAME_CONTAINS)) + "%')");
        }
        if (searchCriteriaMap.containsKey(HARDWARE_MODEL_NAME_EQUALS)) {
            if (((String)searchCriteriaMap.get(HARDWARE_MODEL_NAME_EQUALS)).isEmpty()) {
                query.appendWhereClause("ah.hardware_model_name is null");
            } else {
                query.appendWhereClause("lower(ah.hardware_model_name) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_MODEL_NAME_EQUALS)) + "')");
            }
        }
        // For Tape model number
        if (searchCriteriaMap.containsKey("modelNumberContains")) {
            query.appendWhereClause("lower(ah.hardware_model_number) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("modelNumberContains")) + "%')");
        }
        if (searchCriteriaMap.containsKey(HARDWARE_MODEL_NUMBER_EQUALS)) {
            if (((String)searchCriteriaMap.get(HARDWARE_MODEL_NUMBER_EQUALS)).isEmpty()) {
                query.appendWhereClause("ah.hardware_model_number is null");
            } else {
                query.appendWhereClause("lower(ah.hardware_model_number) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_MODEL_NUMBER_EQUALS)) + "')");
            }
        }
        // For Tape serial number
        if (searchCriteriaMap.containsKey(HARDWARE_SERIAL_NUMBER_CONTAINS)) {
            query.appendWhereClause("lower(ah.hardware_serial_number) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("serialNumberContains")) + "%')");
        }
        if (searchCriteriaMap.containsKey(HARDWARE_SERIAL_NUMBER_EQUALS)) {
            query.appendWhereClause("lower(ah.hardware_serial_number) = lower('" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_SERIAL_NUMBER_EQUALS)) + "')");
        }
        // For Tape owner number
        if (searchCriteriaMap.containsKey("hardwareOwnerContains")) {
            query.appendWhereClause(TapeQueries.getOrderByColumn("hardware_owner_name") + " like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("hardwareOwnerContains")) + "%')");
        }
        if (searchCriteriaMap.containsKey("hardwareOwnerId")) {
            query.appendWhereClause("ah.hardware_owner_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get("hardwareOwnerId")));
        }
        // For Tape type
        if (searchCriteriaMap.containsKey(HARDWARE_TYPE_CONTAINS)) {
            query.appendWhereClause("ah.hardware_type in (" + SqlUtils.encodeIntegers((List<Integer>) searchCriteriaMap.get(HARDWARE_TYPE_CONTAINS)) + ")");
        }
        // For Tape status
        if (searchCriteriaMap.containsKey("hardwareStatusContains")) {
            query.appendWhereClause("ah.hardware_status in (" + SqlUtils.encodeIntegers((List<Integer>) searchCriteriaMap.get("hardwareStatusContains")) + ")");
        }
        // For Hardware location
        if (searchCriteriaMap.containsKey("hardwareLocationContains")) {
            query.appendWhereClause("ah.hardware_location in (" + SqlUtils.encodeIntegers((List<Integer>) searchCriteriaMap.get("hardwareLocationContains")) + ")");
        }
        // For manufacturer by id
        if (searchCriteriaMap.containsKey(HARDWARE_MANUFACTURER_EQUALS)) {
            if (searchCriteriaMap.get(HARDWARE_MANUFACTURER_EQUALS).equals(0)) {
                query.appendWhereClause("mftr.company_id is null");
            } else {
                query.appendWhereClause("mftr.company_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(HARDWARE_MANUFACTURER_EQUALS)));
            }
        }
        // For vendor by id
        if (searchCriteriaMap.containsKey(HARDWARE_VENDOR_EQUALS)) {
            if (searchCriteriaMap.get(HARDWARE_VENDOR_EQUALS).equals(0)) {
                query.appendWhereClause("vndr.company_id is null");
            } else {
                query.appendWhereClause("vndr.company_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(HARDWARE_VENDOR_EQUALS)));
            }
        }

        // Hardware purchased after this date.
        if (searchCriteriaMap.containsKey(HARDWARE_PURCHASED_AFTER)) {
            query.appendWhereClause("ah.hardware_purchase_date >= '" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_PURCHASED_AFTER)) + "'");
        }
        // Hardware purchased before this date.
        if (searchCriteriaMap.containsKey(HARDWARE_PURCHASED_BEFORE)) {
            query.appendWhereClause("ah.hardware_purchase_date <= '" + SqlUtils.encodeString(searchCriteriaMap.get(HARDWARE_PURCHASED_BEFORE)) + "'");
        }

        // Warranty expiration filter
        Set<String> warranty = new HashSet();
        if (searchCriteriaMap.containsKey(HARDWARE_WARRANTY_EXPIRED)) {
            warranty.add("ah.hardware_warranty_expire_date < now()");
        }
        if (searchCriteriaMap.containsKey(HARDWARE_WARRANTY_NOT_EXPIRED)) {
            warranty.add("ah.hardware_warranty_expire_date > now()");

        } else if (searchCriteriaMap.containsKey(HARDWARE_WARRANTY_NOT_SET)) {
            warranty.add("ah.hardware_warranty_expire_date is null");
        }

        String warrantyWhereClause = StringUtils.join(warranty, " or ");

        if (!warrantyWhereClause.isEmpty()) {
            query.appendWhereClause("(" + warrantyWhereClause + ")");
        }
    }
}