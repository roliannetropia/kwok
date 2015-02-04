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
package com.kwoksys.biz.contracts.dao;

import com.kwoksys.biz.admin.dao.AttributeDao;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.contracts.dto.Contract;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.connections.database.QueryBits;
import com.kwoksys.framework.connections.database.QueryHelper;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.framework.exceptions.ObjectNotFoundException;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ContractDao class.
 */
public class ContractDao extends BaseDao {

    public ContractDao(RequestContext requestContext) {
        super(requestContext);
    }

    public List<Contract> getContracts(QueryBits query, QueryHelper queryHelper) throws DatabaseException {
        Connection conn = getConnection();

        if (queryHelper == null) {
            queryHelper = new QueryHelper(ContractQueries.selectContractsQuery(query));
        }

        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                Contract contract = new Contract();
                contract.setId(rs.getInt("contract_id"));
                contract.setName(rs.getString("contract_name"));
                contract.setTypeName(rs.getString("contract_type"));
                contract.setStage(rs.getInt("contract_stage"));

                contract.setOwner(new AccessUser());
                contract.getOwner().setId(rs.getInt(Contract.CONTRACT_OWNER_ID));
                contract.getOwner().setUsername(rs.getString(Contract.CONTRACT_OWNER_USERNAME));
                contract.getOwner().setDisplayName(rs.getString(Contract.CONTRACT_OWNER_DISPLAY_NAME));

                contract.setRenewalTypeName(rs.getString("contract_renewal_type"));
                contract.setEffectiveDate(DatetimeUtils.getDate(rs, "contract_effective_date"));
                contract.setExpireDate(DatetimeUtils.getDate(rs, "contract_expiration_date"));
                contract.setRenewalDate(DatetimeUtils.getDate(rs, "contract_renewal_date"));
                contract.setContractProviderName(rs.getString("contract_provider_name"));
                contract.setContractProviderId(rs.getInt(Contract.CONTRACT_PROVIDER_ID));
                list.add(contract);
            }
            return list;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    /**
     * Gets linked contracts
     * @param query
     * @param objectMap
     * @return
     * @throws DatabaseException
     */
    public List<Contract> getLinkedContracts(QueryBits query, ObjectLink objectMap) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContractQueries.selectLinkedContractsQuery(query));
        queryHelper.addInputInt(objectMap.getObjectTypeId());
        queryHelper.addInputInt(objectMap.getLinkedObjectId());
        queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());

        return getContracts(query, queryHelper);
    }

    public int getContractCount(QueryBits query) throws DatabaseException {
        return getRowCount(ContractQueries.getContractCountQuery(query));
    }

    public Contract getContract(Integer contractId) throws DatabaseException, ObjectNotFoundException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(ContractQueries.selectContractDetailQuery());
        queryHelper.addInputInt(contractId);

        try {
            ResultSet rs = queryHelper.executeQuery(conn);

            if (rs.next()) {
                Contract contract = new Contract();
                contract.setId(rs.getInt("contract_id"));
                contract.setName(StringUtils.replaceNull(rs.getString("contract_name")));
                contract.setDescription(StringUtils.replaceNull(rs.getString("contract_description")));
                contract.setType(rs.getInt("contract_type"));
                contract.setStage(rs.getInt("contract_stage"));

                contract.setOwner(new AccessUser());
                contract.getOwner().setId(rs.getInt(Contract.CONTRACT_OWNER_ID));
                contract.getOwner().setUsername(rs.getString(Contract.CONTRACT_OWNER_USERNAME));
                contract.getOwner().setDisplayName(rs.getString(Contract.CONTRACT_OWNER_DISPLAY_NAME));

                contract.setEffectiveDate(DatetimeUtils.getDate(rs, "contract_effective_date"));
                contract.setExpireDate(DatetimeUtils.getDate(rs, "contract_expiration_date"));
                contract.setRenewalType(rs.getInt("contract_renewal_type"));
                contract.setRenewalDate(DatetimeUtils.getDate(rs, "contract_renewal_date"));
                contract.setContractProviderName(rs.getString("contract_provider_name"));
                contract.setContractProviderId(rs.getInt("contract_provider_id"));
                contract.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                contract.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));

                contract.setCreator(new AccessUser());
                contract.getCreator().setId(rs.getInt("creator"));
                contract.getCreator().setUsername(rs.getString("creator_username"));
                contract.getCreator().setDisplayName(rs.getString("creator_display_name"));

                contract.setModifier(new AccessUser());
                contract.getModifier().setId(rs.getInt("modifier"));
                contract.getModifier().setUsername(rs.getString("modifier_username"));
                contract.getModifier().setDisplayName(rs.getString("modifier_display_name"));

                return contract;
            }
        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
        throw new ObjectNotFoundException();
    }

    public List getContractsSummary() throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(ContractQueries.selectContractsSummary());

        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);

            while (rs.next()) {
                Map map = new HashMap();
                map.put("interval", rs.getString("interval"));
                map.put("count", rs.getInt("count"));
                list.add(map);
            }
            return list;

        } catch (Exception e) {
            // Database problem
            throw new DatabaseException(e, queryHelper);

        } finally {
            queryHelper.closeRs();
            closeConnection(conn);
        }
    }

    public ActionMessages addContract(Contract contract) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(ContractQueries.addContractQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(contract.getName());
        queryHelper.addInputStringConvertNull(contract.getDescription());
        queryHelper.addInputInt(contract.getType());
        queryHelper.addInputInt(contract.getStage());

        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(
                contract.getEffectiveDateYear(), contract.getEffectiveDateMonth(), contract.getEffectiveDateDate()));

        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(
                contract.getExpireDateYear(), contract.getExpireDateMonth(), contract.getExpireDateDate()));

        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(
                contract.getRenewalDateYear(), contract.getRenewalDateMonth(), contract.getRenewalDateDate()));

        queryHelper.addInputInt(contract.getRenewalType());
        queryHelper.addInputIntegerConvertNull(contract.getContractProviderId());
        queryHelper.addInputIntegerConvertNull(contract.getOwnerId());
        queryHelper.addInputIntegerConvertNull(contract.getContractProviderContactId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);
            // Put some values in the result.
            contract.setId((Integer) queryHelper.getSqlOutputs().get(0));

            // Update custom fields
            if (!contract.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, contract.getId(), contract.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    public ActionMessages updateContract(Contract contract) throws DatabaseException {
        Connection conn = getConnection();

        QueryHelper queryHelper = new QueryHelper(ContractQueries.updateContractQuery());
        queryHelper.addInputInt(contract.getId());
        queryHelper.addInputStringConvertNull(contract.getName());
        queryHelper.addInputStringConvertNull(contract.getDescription());
        queryHelper.addInputInt(contract.getType());
        queryHelper.addInputInt(contract.getStage());

        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(
                contract.getEffectiveDateYear(), contract.getEffectiveDateMonth(), contract.getEffectiveDateDate()));

        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(
                contract.getExpireDateYear(), contract.getExpireDateMonth(), contract.getExpireDateDate()));

        queryHelper.addInputStringConvertNull(DatetimeUtils.createDatetimeString(
                contract.getRenewalDateYear(), contract.getRenewalDateMonth(), contract.getRenewalDateDate()));

        queryHelper.addInputInt(contract.getRenewalType());
        queryHelper.addInputIntegerConvertNull(contract.getContractProviderId());
        queryHelper.addInputIntegerConvertNull(contract.getOwnerId());
        queryHelper.addInputIntegerConvertNull(contract.getContractProviderContactId());
        queryHelper.addInputInt(requestContext.getUser().getId());

        try {
            queryHelper.executeProcedure(conn);

            // Update custom fields
            if (!contract.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao(requestContext);
                attributeDao.updateAttributeValue(conn, contract.getId(), contract.getCustomValues());
            }
        } catch (Exception e) {
            // Database problem
            handleError(e);

        } finally {
            closeConnection(conn);
        }
        return errors;
    }

    /**
     * Delete a contract.
     *
     * @return ..
     */
    public ActionMessages delete(Integer contractId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(ContractQueries.deleteContractQuery());
        queryHelper.addInputInt(ObjectTypes.CONTRACT);
        queryHelper.addInputInt(contractId);

        return executeProcedure(queryHelper);
    }
}
