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
package com.kwoksys.biz.contracts.core;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.auth.core.Access;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.SystemUtils;
import com.kwoksys.biz.system.core.*;
import com.kwoksys.framework.util.Counter;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.biz.system.core.configs.ConfigManager;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.ui.Link;
import com.kwoksys.framework.ui.RowStyle;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.HtmlUtils;

import java.util.*;

/**
 * ContractUtil
 */
public class ContractUtils {

    public static final String CONTACTS_TAB = "contactsTab";

    public static final String FILES_TAB = "filesTab";

    public static final String HARDWARE_TAB = "hardwareTab";

    public static String[] getContractsColumnsDefault() {
        return new String[] {Contract.ROWNUM, Contract.NAME, Contract.TYPE, Contract.STAGE, Contract.OWNER, Contract.CONTRACT_EXPIRE_DATE,
                Contract.CONTRACT_EFFECT_DATE, Contract.RENEWAL_TYPE, Contract.CONTRACT_RENEWAL_DATE};
    }

    /**
     * Speficify the sortable columns allowed.
     */
    public static List<String> getSortableColumns() {
        return Arrays.asList(Contract.NAME, Contract.ID, Contract.OWNER, Contract.CONTRACT_EXPIRE_DATE,
                Contract.CONTRACT_EFFECT_DATE);
    }

    /**
     * Check whether a column is sortable.
     *
     * @param columnName
     * @return ..
     */
    public static boolean isSortableColumn(String columnName) {
        return getSortableColumns().contains(columnName);
    }

    /**
     * Specify the column header for the list page.
     */
    public static List getColumnHeaderList() {
        return Arrays.asList(ConfigManager.app.getContractsColumns());
    }

    public static String formatExpirationDate(RequestContext requestContext, Long unixTimestamp, Date expirationDate) {
        return SystemUtils.formatExpirationDate(requestContext, unixTimestamp, expirationDate, ConfigManager.app.getContractsExpireCountdown());
    }

    /**
     * This is for generating contract tabs.
     *
     * @param request
     * @param contract
     * @return ..
     */
    public static List contractTabList(RequestContext requestContext, Contract contract)
            throws DatabaseException {

        SystemService systemService = ServiceProvider.getSystemService(requestContext);
        List linkedTypes = Arrays.asList(String.valueOf(ObjectTypes.HARDWARE), String.valueOf(ObjectTypes.SOFTWARE));
        int relationshipCount = systemService.getLinkedObjectMapCount(linkedTypes, contract.getId(), ObjectTypes.CONTRACT);

        AccessUser user = requestContext.getUser();

        List tabList = new ArrayList();

        // Link to contract attachments view.
        if (Access.hasPermission(user, AppPaths.CONTRACTS_DETAIL)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", FILES_TAB);
            tabMap.put("tabPath", AppPaths.CONTRACTS_DETAIL + "?contractId=" + contract.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "itMgmt.tab.contractFile"));
            tabList.add(tabMap);
        }

        // Link to contract hardware tab.
        if (Access.hasPermission(user, AppPaths.CONTRACTS_ITEMS)) {
            Map tabMap = new HashMap();
            tabMap.put("tabName", HARDWARE_TAB);
            tabMap.put("tabPath", AppPaths.CONTRACTS_ITEMS + "?contractId=" + contract.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "common.tab.relationships", new Object[]{relationshipCount}));
            tabList.add(tabMap);
        }

        if (Access.hasPermission(user, AppPaths.CONTRACTS_CONTACTS)) {
            linkedTypes = Arrays.asList(String.valueOf(ObjectTypes.CONTACT));
            int contactsCount = systemService.getLinkedObjectMapCount(linkedTypes, contract.getId(), ObjectTypes.CONTRACT);

            Map tabMap = new HashMap();
            tabMap.put("tabName", CONTACTS_TAB);
            tabMap.put("tabPath", AppPaths.CONTRACTS_CONTACTS + "?contractId=" + contract.getId());
            tabMap.put("tabText", Localizer.getText(requestContext, "common.linking.tab.linkedContacts", new Object[]{contactsCount}));
            tabList.add(tabMap);
        }
        return tabList;
    }

    public static List formatContractList(RequestContext requestContext, List<Contract> contractDataset, Counter counter) throws Exception {
        AccessUser user = requestContext.getUser();
        boolean hasContractAccess = Access.hasPermission(user, AppPaths.CONTRACTS_DETAIL);
        boolean hasUserDetailAccess = Access.hasPermission(user, AppPaths.ADMIN_USER_DETAIL);

        // Sysdate timestamp
        long unixTimestamp = requestContext.getSysdate().getTime();

        List list = new ArrayList();
        List<String> columnHeaders = ContractUtils.getColumnHeaderList();
        RowStyle ui = new RowStyle();

        for (Contract contract : contractDataset) {
            List columns = new ArrayList();

            for (String column : columnHeaders) {
                if (column.equals(Contract.ROWNUM)) {
                    columns.add(counter.incrCounter() + ".");

                } else if (column.equals(Contract.NAME)) {
                    Link link = new Link(requestContext);
                    link.setTitle(contract.getName());

                    if (hasContractAccess) {
                        link.setAjaxPath(AppPaths.CONTRACTS_DETAIL + "?contractId=" + contract.getId());
                    }
                    columns.add(link.getString());

                } else if (column.equals(Contract.TYPE)) {
                    columns.add(HtmlUtils.encode(contract.getTypeName()));

                } else if (column.equals(Contract.STAGE)) {
                    columns.add(new AttributeManager(requestContext).getAttrFieldNameCache(Attributes.CONTRACT_STAGE, contract.getStage()));

                } else if (column.equals(Contract.OWNER)) {
                    columns.add(Links.getUserIconLink(requestContext, contract.getOwner(), hasUserDetailAccess, true));

                } else if (column.equals(Contract.RENEWAL_TYPE)) {
                    columns.add(HtmlUtils.encode(contract.getRenewalTypeName()));

                } else if (column.equals(Contract.CONTRACT_EXPIRE_DATE)) {
                    columns.add(ContractUtils.formatExpirationDate(requestContext, unixTimestamp, contract.getExpireDate()));

                } else if (column.equals(Contract.CONTRACT_EFFECT_DATE)) {
                    columns.add(DatetimeUtils.toShortDate(contract.getEffectiveDate()));

                } else if (column.equals(Contract.CONTRACT_RENEWAL_DATE)) {
                    columns.add(DatetimeUtils.toShortDate(contract.getRenewalDate()));
                }
            }
            Map map = new HashMap();
            map.put("contractId", contract.getId());
            map.put("rowClass", ui.getRowClass());
            map.put("columns", columns);
            list.add(map);
        }
        return list;
    }
}
