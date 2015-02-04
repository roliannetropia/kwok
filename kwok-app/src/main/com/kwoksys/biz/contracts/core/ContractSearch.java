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

import com.kwoksys.action.contracts.ContractSearchForm;
import com.kwoksys.biz.base.BaseSearch;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.SqlUtils;
import com.kwoksys.framework.http.RequestContext;

/**
 * This is for building search queries.
 */
public class ContractSearch extends BaseSearch {
    public static final String CONTRACT_PROVIDER_ID_KEY = "contractProviderId";

    public static final String CONTRACT_STAGE_KEY = "stage";

    public ContractSearch(RequestContext requestContext, String sessionKey) {
        super(requestContext, sessionKey);
    }

    public ContractSearch() {}

    /**
     * Generates searchCriteriaMap.
     *
     * @return ..
     */
    public void prepareMap(ContractSearchForm contractSearchForm) {
        String cmd = requestContext.getParameterString("cmd");
        String attrValue = contractSearchForm.getAttrValue();

        // Getting search criteria map from session variable.
        if (!cmd.isEmpty()) {
            if (cmd.equals("filter")) {
                Integer stageFilter = requestContext.getParameterInteger("stage");
                if (stageFilter != null) {
                    searchCriteriaMap.put(ContractSearch.CONTRACT_STAGE_KEY, stageFilter);
                }
            } else {
                reset();
                contractSearchForm.setRequest(requestContext);

                if (cmd.equals("groupBy")) {
                    String contractExpire = requestContext.getParameterString("contractExpire");
                    if (!contractExpire.isEmpty()) {
                        // ContractExpire url variable is in the format of "start_end", e.g. "30_60" means contract expires
                        // between 30 to 60 days. Or in the format of "end", e.g. "30", meaning contract expires in 30 days.
                        String[] interval = contractExpire.split("_");
                        if (interval.length == 1) {
                            searchCriteriaMap.put("contractExpireBefore", interval[0]);
                        } else {
                            searchCriteriaMap.put("contractExpireAfter", interval[0]);
                            searchCriteriaMap.put("contractExpireBefore", interval[1]);
                        }
                    }
                } else if (cmd.equals("showNonExpired")) {
                    searchCriteriaMap.put("nonExpiredContracts", "nonExpiredContracts");

                } else if (cmd.equals("search")) {
                    // We are expecting user to enter some search criteria.
                    if (!contractSearchForm.getContractName().isEmpty()) {
                        searchCriteriaMap.put("contractNameContains", contractSearchForm.getContractName());
                    }
                     if (!contractSearchForm.getDescription().isEmpty()) {
                        searchCriteriaMap.put("description", contractSearchForm.getDescription());
                    }
                    if (contractSearchForm.getContractTypeId() != 0) {
                        searchCriteriaMap.put("contractTypeId", contractSearchForm.getContractTypeId());
                    }
                    if (contractSearchForm.getContractProviderId() != 0) {
                        searchCriteriaMap.put(CONTRACT_PROVIDER_ID_KEY, contractSearchForm.getContractProviderId());
                    }
                    if (contractSearchForm.getStage() != 0) {
                        searchCriteriaMap.put(CONTRACT_STAGE_KEY, contractSearchForm.getStage());
                    }
                    // Search by custom fields
                    if (!contractSearchForm.getAttrId().isEmpty() && !attrValue.isEmpty()) {
                        searchCriteriaMap.put("attrId", contractSearchForm.getAttrId());
                        searchCriteriaMap.put("attrValue", attrValue);
                    }
                }
            }
        }
    }

    public void applyMap(QueryBits query) {
        if (searchCriteriaMap == null) {
            return;
        }
        // Search by non-expired contracts.
        if (searchCriteriaMap.containsKey("nonExpiredContracts")) {
            query.appendWhereClause("c.contract_expiration_date is null or c.contract_expiration_date>now()");
        }
        // For contract name
        if (searchCriteriaMap.containsKey("contractNameContains")) {
            query.appendWhereClause("lower(c.contract_name) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("contractNameContains")) + "%')");
        }
        if (searchCriteriaMap.containsKey("contractTypeId")) {
            query.appendWhereClause("c.contract_type = " + SqlUtils.encodeInteger(searchCriteriaMap.get("contractTypeId")));
        }
        if (searchCriteriaMap.containsKey(CONTRACT_PROVIDER_ID_KEY)) {
            query.appendWhereClause("c.contract_provider_id = " + SqlUtils.encodeInteger(searchCriteriaMap.get(CONTRACT_PROVIDER_ID_KEY)));
        }
        if (searchCriteriaMap.containsKey(CONTRACT_STAGE_KEY)) {
            query.appendWhereClause("c.contract_stage = " + SqlUtils.encodeInteger(searchCriteriaMap.get(CONTRACT_STAGE_KEY)));
        }
        // For contract description
        if (searchCriteriaMap.containsKey("description")) {
            query.appendWhereClause("lower(c.contract_description) like lower('%" + SqlUtils.encodeString(searchCriteriaMap.get("description")) + "%')");
        }
        // For custom fields
        if (searchCriteriaMap.containsKey("attrId") && searchCriteriaMap.containsKey("attrValue")) {
            query.appendWhereClause("c.contract_id in (select object_id from object_attribute_value where attribute_id = "+
                    SqlUtils.encodeInteger(searchCriteriaMap.get("attrId")) + " and lower(attr_value) like lower('%"
                    + SqlUtils.encodeString(searchCriteriaMap.get("attrValue")) +"%'))");
        }
        if (searchCriteriaMap.containsKey("contractExpireAfter")) {
            query.appendWhereClause("c.contract_expiration_date > now()::timestamp + '+"
                    + SqlUtils.encodeInteger(searchCriteriaMap.get("contractExpireAfter")) +" day'::interval");
        }
        if (searchCriteriaMap.containsKey("contractExpireBefore")) {
            query.appendWhereClause("c.contract_expiration_date < now()::timestamp + '+"
                    + SqlUtils.encodeInteger(searchCriteriaMap.get("contractExpireBefore")) +" day'::interval");
        }
    }
}