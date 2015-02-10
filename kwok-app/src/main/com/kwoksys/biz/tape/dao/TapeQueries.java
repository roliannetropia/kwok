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
package com.kwoksys.biz.tape.dao;

import com.kwoksys.biz.tape.dto.Tape;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.biz.system.core.configs.ConfigManager;

/**
 * Tape queries
 */
public class TapeQueries {

    public static String getOrderByColumn(String column) {
        if (column.equals(Tape.TAPE_NAME)) {
            return "lower(tape_name)";

        } else if (column.equals(Tape.LOCATION)) {
            return "lower(hwloc.attribute_field_name)";

        } else if (column.equals("software_name")) {
            return "lower(software_name)";

        } else if (column.equals("license_key")) {
            return "lower(asl.license_key)";

        } else if (column.equals("tape_serial_number")) {
            return "lower(tape_serial_number)";

        } else if (column.equals(Tape.MODEL_NAME)) {
            return "lower(tape_model_name)";

        } else if (column.equals(Tape.MODEL_NUMBER)) {
            return "lower(tape_model_number)";

        } else if (column.equals(Tape.OWNER_NAME)) {
            return ConfigManager.system.getUsernameDisplay().equals(AdminUtils.USER_USERNAME) ?
                    "lower(tape_owner_username)" : "lower(tape_owner_display_name)";

        } else if (column.equals("attribute_field_name")) {
            return "lower(af.attribute_field_name)";

        } else if (column.equals("comp_name")) {
            // Tape component name
            return "lower(af.attribute_field_name)";

        } else {
            return column;
        }
    }

    /**
     * Return all tape.
     */
    public static String selectTapeListQuery(QueryBits query) {
        return "select ah.tape_id, ah.tape_number, ah.tape_name, ah.tape_description, " +
                "ah.tape_owner_display_name, ah.tape_owner_username, ah.tape_owner_id, " +
                "ah.tape_type, ah.tape_status, ah.tape_model_name, " +
                "ah.tape_model_number, ah.tape_serial_number, ah.tape_cost as tape_purchase_price, ah.tape_last_service_date, " +
                "ah.tape_purchase_date, ah.tape_warranty_expire_date, ah.tape_location, " +
                "ah.software_count, ah.file_count, ah.component_count," +
                "ah.manufacturer_company_id, mftr.company_name as tape_manufacturer_name, " +
                "ah.vendor_company_id, vndr.company_name as tape_vendor_name, " +
                "ah.creator, ah.creation_date, ah.creator_username, ah.creator_display_name, " +
                "ah.modifier, ah.modification_date, ah.modifier_username, ah.modifier_display_name " +
                "from asset_tape_view ah " +
                "left outer join company mftr on ah.manufacturer_company_id = mftr.company_id " +
                "left outer join company vndr on ah.vendor_company_id = vndr.company_id " +
                "left outer join attribute_field_view hwloc on hwloc.attribute_field_id = ah.tape_location "
                + query.createWhereClause();
    }

    /**
     * Return all linked Tape.
     */
    public static String selectLinkedTapeListQuery(QueryBits query) {
        return selectTapeListQuery(new QueryBits()) +
                "where ah.tape_id in (select om.linked_object_id from object_map om where om.object_id=? and om.object_type_id=? " +
                "and om.linked_object_type_id=?) "
                + query.createWhereClause();
    }

    public static String selectObjectTapeListQuery(QueryBits query) {
        return selectTapeListQuery(new QueryBits()) +
                "where ah.tape_id in (select om.object_id from object_map om where om.linked_object_id=? and om.linked_object_type_id=? " +
                "and om.object_type_id=?) "
                + query.createAndClause();
    }

    /**
     * Return Tape count.
     *
     * @return ..
     */
    public static String getTapeCountQuery(QueryBits query) {
        return "select count(ah.tape_id) as row_count " +
                "from asset_tape_view ah " +
                "left outer join company mftr on ah.manufacturer_company_id = mftr.company_id " +
                "left outer join company vndr on ah.vendor_company_id = vndr.company_id "
                + query.createWhereCountClause();
    }

    /**
     * Return detail for a specific tape.
     */
    public static String selectTapeDetailQuery() {
        return selectTapeListQuery(new QueryBits()) +
                "where ah.tape_id = ?";
    }

    /**
     * Return number of tape grouped by type.
     */
    public static String selectTapeTypeCountQuery(QueryBits query) {
        return "select h.tape_type, count(h.tape_id) as tape_count " +
                "from asset_tape h " +
                "left outer join (select af.attribute_field_id, af.attribute_field_name " +
                "from attribute_view an, attribute_field_view af " +
                "where an.object_key='tape' and an.attribute_name='tape_type' " +
                "and an.attribute_id = af.attribute_id) af on h.tape_type = af.attribute_field_id " +
                "group by tape_type, af.attribute_field_name " + query.createClause();
    }

    /**
     * Return number of tape grouped by status.
     */
    public static String selectTapeCountByStatusQuery(QueryBits query) {
        return "select h.tape_status, count(h.tape_id) as tape_count " +
                "from asset_tape h " +
                "left outer join (select af.attribute_field_id, af.attribute_field_name " +
                "from attribute_view an, attribute_field_view af " +
                "where an.object_key='tape' and an.attribute_name='tape_status' " +
                "and an.attribute_id = af.attribute_id) af on h.tape_status = af.attribute_field_id " +
                "group by tape_status, af.attribute_field_name " + query.createClause();
    }

    /**
     * Return number of tape grouped by location.
     */
    public static String selectTapeCountByLocationQuery(QueryBits query) {
        return "select h.tape_location, count(h.tape_id) as tape_count " +
                "from asset_tape h " +
                "left outer join (select af.attribute_field_id, af.attribute_field_name " +
                "from attribute_view an, attribute_field_view af " +
                "where an.object_key='tape' and an.attribute_name='tape_location' " +
                "and an.attribute_id = af.attribute_id) af on h.tape_location = af.attribute_field_id " +
                "group by tape_location, af.attribute_field_name " + query.createClause();
    }

    /**
     * Return all Software in the system.
     *
     * @return ..
     */
    public static String selectTapeAvailableSoftwareQuery(QueryBits query) {
        return "select software_id, software_name from asset_software "+ query.createWhereClause();
    }

    public static String selectTapeAvailableLicensesQuery(QueryBits query) {
        return "select asl.software_id, asl.license_id, asl.license_key, asl.license_note, asl.license_entitlement " +
                "from asset_software_licenses asl " +
                "left outer join asset_map am on asl.license_id = am.license_id " +
                "where asl.software_id = ? " +
                "group by asl.software_id, asl.license_id, asl.license_key, asl.license_note, asl.license_entitlement " +
                "having count(am.license_entitlement) < asl.license_entitlement " + query.createClause();
    }

    /**
     * Return Software Licenses installed on this Tape.
     *
     * @return ..
     */
    public static String selectInstalledLicenseQuery(QueryBits query) {
        return "select am.map_id, am.software_id, am.license_id, s.software_name, asl.license_key, asl.license_note " +
                "from asset_map am " +
                "left outer join asset_software s on am.software_id = s.software_id " +
                "left outer join asset_software_licenses asl on am.license_id = asl.license_id " +
                "where am.tape_id  =?" + query.createAndClause();
    }

    /**
     * Return Components for a particular Tape.
     *
     * @return ..
     */
    public static String selectTapeComponentsQuery(QueryBits query) {
        return "select hc.comp_id, af.attribute_field_name as comp_name, comp_description " +
                "from asset_tape_component hc " +
                "left outer join attribute_field_view af on hc.tape_component_type = af.attribute_field_id " +
                "where hc.tape_id = ?" + query.createAndClause();
    }

    /**
     * Return a specific tape component.
     *
     * @return ..
     */
    public static String selectTapeComponentDetailQuery() {
        return "select hc.comp_id, hc.tape_id, hc.tape_component_type, hc.comp_description " +
                "from asset_tape_component hc " +
                "where hc.tape_id = ? and hc.comp_id = ?";
    }

    /**
     * Add a new Tape.
     *
     * @return ..
     */
    public static String insertTapeQuery() {
        return "{call sp_tape_add(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    }

    /**
     * Update the detail for a specific tape.
     *
     * @return ..
     */
    public static String updateTapeQuery() {
        return "{call sp_tape_update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    }

    /**
     * Delete tape.
     */
    public static String deleteTapeQuery() {
        return "{call sp_tape_delete(?,?)}";
    }

    /**
     * This would assign a software license to a tape.
     *
     * @return ..
     */
    public static String insertAssignLicenseQuery() {
        return "{call sp_software_license_assign(?,?,?,?,?)}";
    }

    /**
     * This would unassign a software license to a tape.
     */
    public static String deleteAssignedLicenseQuery() {
        return "{call sp_asset_map_delete(?)}";
    }

    /**
     * Update the detail for a specific tape component.
     *
     * @return ..
     */
    public static String insertTapeComponentQuery() {
        return "{call sp_tape_component_add(?,?,?,?,?)}";
    }

    /**
     * Update the detail for a specific tape component.
     *
     * @return ..
     */
    public static String updateTapeComponentQuery() {
        return "{call sp_tape_component_update(?,?,?,?,?)}";
    }

    /**
     * Delete a tape component.
     *
     * @return ..
     */
    public static String deleteTapeComponentQuery() {
        return "{call sp_tape_component_delete(?,?,?)}";
    }

    /**
     * Reset Tape assigned Software counter.
     *
     * @return ..
     */
    public static String updateTapeSoftwareCountQuery() {
        return "{call sp_tape_count_software_update(?)}";
    }

    /**
     * Reset Tape File counter.
     *
     * @return ..
     */
    public static String updateTapeFileCountQuery() {
        return "{call sp_tape_count_file_update(?,?)}";
    }

    /**
     * Reset Tape Component counter.
     *
     * @return ..
     */
    public static String updateTapeComponentCountQuery() {
        return "{call sp_tape_count_component_update(?)}";
    }
}
