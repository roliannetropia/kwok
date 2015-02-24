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

//        } else if (column.equals(Tape.TAPE_LOCATION)) {
//            return "lower(tape_location)";
//
//        } else if (column.equals(Tape.MEDIA_TYPE)) {
//            return "lower(media_type)";
//
//        } else if (column.equals(Tape.TAPE_STATUS)) {
//            return "lower(tape_status)";
//
//        } else if (column.equals(Tape.TAPE_SYSTEM)) {
//            return "lower(tape_system)";

        } else {
            return column;
        }
    }

    /**
     * Return all tape.
     */
    public static String selectTapeListQuery(QueryBits query) {
        return "select at.tape_id, at.tape_name, at.serial_number, at.barcode_number, " +
                "at.manufacturer_company_id, at.vendor_company_id, " +
// at.media_type, " +
//                "at.manufactured_date, at.location, at.retention, at.system, at.status, " +
//                "at.transaction_date, at.transaction_time, at.date_move, at.date_expire" +
                "at.manufacturer_company_id, mftr.company_name as tape_manufacturer_name, " +
                "at.vendor_company_id, vndr.company_name as tape_vendor_name " +
                "from asset_tape_view at " +
                "left outer join company mftr on at.manufacturer_company_id = mftr.company_id " +
                "left outer join company vndr on at.vendor_company_id = vndr.company_id " +
//                "left outer join attribute_field_view hwloc on hwloc.attribute_field_id = at.tape_location "
//                + query.createWhereClause();
                 query.createWhereClause();
    }

    /**
     * Return all linked Tape.
     */
    public static String selectLinkedTapeListQuery(QueryBits query) {
        return selectTapeListQuery(new QueryBits()) +
                "where at.tape_id in (select om.linked_object_id from object_map om where om.object_id=? and om.object_type_id=? " +
                "and om.linked_object_type_id=?) "
                + query.createWhereClause();
    }

    public static String selectObjectTapeListQuery(QueryBits query) {
        return selectTapeListQuery(new QueryBits()) +
                "where at.tape_id in (select om.object_id from object_map om where om.linked_object_id=? and om.linked_object_type_id=? " +
                "and om.object_type_id=?) "
                + query.createAndClause();
    }

    /**
     * Return Tape count.
     *
     * @return ..
     */
    public static String getTapeCountQuery(QueryBits query) {
        return "select count(at.tape_id) as row_count " +
                "from asset_tape_view at " +
                "left outer join company mftr on at.manufacturer_company_id = mftr.company_id " +
                "left outer join company vndr on at.vendor_company_id = vndr.company_id "
                + query.createWhereCountClause();
    }

    /**
     * Return detail for a specific tape.
     */
    public static String selectTapeDetailQuery() {
        return selectTapeListQuery(new QueryBits()) +
                "where at.tape_id = ?";
    }

    /**
     * Return number of tape grouped by type.
     */
    public static String selectMediaTypeCountQuery(QueryBits query) {
        return "select t.media_type, count(t.tape_id) as tape_count " +
                "from asset_tape t " +
                "left outer join (select af.attribute_field_id, af.attribute_field_name " +
                "from attribute_view an, attribute_field_view af " +
                "where an.object_key='tape' and an.attribute_name='media_type' " +
                "and an.attribute_id = af.attribute_id) af on t.media_type = af.attribute_field_id " +
                "group by media_type, af.attribute_field_name " + query.createClause();
    }

    /**
     * Return number of tape grouped by status.
     */
    public static String selectTapeCountByStatusQuery(QueryBits query) {
        return "select t.status, count(t.tape_id) as tape_count " +
                "from asset_tape t " +
                "left outer join (select af.attribute_field_id, af.attribute_field_name " +
                "from attribute_view an, attribute_field_view af " +
                "where an.object_key='tape' and an.attribute_name='status' " +
                "and an.attribute_id = af.attribute_id) af on t.status = af.attribute_field_id " +
                "group by status, af.attribute_field_name " + query.createClause();
    }

    /**
     * Return number of tape grouped by location.
     */
    public static String selectTapeCountByLocationQuery(QueryBits query) {
        return "select t.location, count(t.tape_id) as tape_count " +
                "from asset_tape t " +
                "left outer join (select af.attribute_field_id, af.attribute_field_name " +
                "from attribute_view an, attribute_field_view af " +
                "where an.object_key='tape' and an.attribute_name='location' " +
                "and an.attribute_id = af.attribute_id) af on t.location = af.attribute_field_id " +
                "group by location, af.attribute_field_name " + query.createClause();
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
//  todo eto yung nag cacall ng function
    public static String insertTapeQuery() {
        return "{call sp_tape_add(?,?,?,?,?,?)}";
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
