/*
 * ====================================================================
 * Copyright 2005-2014 Wai-Lun Kwok
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
package com.kwoksys.biz.admin.core.dataimport;

import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.core.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.Attribute;
import com.kwoksys.biz.admin.dto.ImportItem;
import com.kwoksys.biz.system.core.AttributeManager;
import com.kwoksys.biz.system.core.ObjectTypes;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.properties.Localizer;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserImport extends ImportManager {

    private static final Logger logger = Logger.getLogger(UserImport.class.getName());

    public UserImport(RequestContext requestContext) {
        super(requestContext);
    }

    protected void importData(List<String[]> data, boolean validateOnly) throws Exception {
        Iterator<String[]> rowIter = data.iterator();

        // First row contains headers.
        List<String> columnHeaders = Arrays.asList(rowIter.next());

        AdminService adminService = ServiceProvider.getAdminService(requestContext);

        // Get custom field values from csv.
        Map<Integer, Attribute> typeCustomFields = new AttributeManager(requestContext).getCustomFieldMap(ObjectTypes.USER);

        while (rowIter.hasNext()) {
            boolean overwrite = true;
            ImportItem importItem = new ImportItem();
            importItem.setRowNum(++rowNum);

            try {
                String[] columns = rowIter.next();
                AccessUser accessUser = null;

                // If overwrite is enabled, get hardware record by name.
                if (overwrite) {
                    int colNumber = columnHeaders.indexOf(AccessUser.USERNAME);
                    if (colNumber != -1) {
                        String username = columns[colNumber];
                        accessUser = adminService.getUserByUsername(username);
                    }
                }

                // When AccessUser is null, update only when there is exactly one record.
                if (accessUser == null) {
                    accessUser = new AccessUser();
                    overwrite = false;
                }

                Map<String, String> importCustomFields = new HashMap();

                ActionMessages errors = new ActionMessages();

                for (int i = 0; i<columns.length; i++) {
                    String key = columnHeaders.get(i).trim();
                    String value = columns[i].trim();
                    //System.out.println(key + ": " + value);

                    if (key.equals(AccessUser.USERNAME)) {
                        accessUser.setUsername(value);
                        importItem.setTitle(value);

                    } else if (key.equals("first_name")) {
                        accessUser.setFirstName(value);

                    } else if (key.equals("last_name")) {
                        accessUser.setLastName(value);

                    } else if (key.equals("display_name")) {
                        accessUser.setDisplayName(value);

                    } else if (key.equals("email")) {
                        accessUser.setEmail(value);

                    } else if (key.equals("password")) {
                        accessUser.setPasswordNew(value);

                    } else {
                        importCustomFields.put(key, value);
                    }
                }

                AdminUtils.populateCustomFieldValues(importCustomFields, accessUser, typeCustomFields);

                if (errors.isEmpty()) {
                    if (validateOnly) {
                        errors.add(adminService.validateUser(accessUser, null, typeCustomFields));
                    } else {
                        if (overwrite) {
                            errors.add(adminService.updateUser(accessUser, null, null, typeCustomFields));
                        } else {
                            errors.add(adminService.addUser(accessUser, null, null, typeCustomFields));
                        }
                    }
                }

                if (!errors.isEmpty()) {
                    for (Iterator<ActionMessage> iter = errors.get(); iter.hasNext();) {
                        ActionMessage error = iter.next();
                        importItem.getErrorMessages().add(Localizer.getText(requestContext, error.getKey(), error.getValues()));
                    }
                    importItem.setAction(ImportItem.ACTION_ERROR);
                } else {
                    importItem.setAction(overwrite ? ImportItem.ACTION_UPDATE : ImportItem.ACTION_ADD);
                }

            } catch (Exception e) {
                logger.log(Level.WARNING, "Problem importing user", e);
                importItem.getErrorMessages().add(e.getMessage());
                importItem.setAction(ImportItem.ACTION_ERROR);
            }

            addImportItem(importItem);
        }

        buildImportResultsMessage();
    }
}
