package com.kwoksys.biz.admin.core.dataimport;

import com.kwoksys.biz.admin.dto.ImportItem;
import com.kwoksys.framework.http.RequestContext;
import com.kwoksys.framework.exceptions.DatabaseException;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Company;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.framework.properties.Localizer;
import com.kwoksys.framework.session.SessionManager;

import java.util.*;

/**
 * ImportManager
 */
public abstract class ImportManager {

    public static final String IMPORT_TYPE_HARDWARE = "hardware_import";

    public static final String IMPORT_TYPE_USER = "user_import";

    public static final List<String> IMPORT_TYPES = Arrays.asList(IMPORT_TYPE_HARDWARE, IMPORT_TYPE_USER);

    private Map<String, Integer> companyIdMap = new HashMap();

    protected List<ImportItem> importItems = new ArrayList();

    protected RequestContext requestContext;

    private Map<String, Integer> counts = new HashMap();

    protected int rowNum = 1;

    public ImportManager(RequestContext requestContext) {
        this.requestContext = requestContext;
        requestContext.getRequest().getSession().setAttribute(SessionManager.IMPORT_RESULTS, importItems);
    }

    public static ImportManager newInstance(RequestContext requestContext, String importType) {
        if (importType.equals(IMPORT_TYPE_HARDWARE)) {
            return new HardwareImport(requestContext);

        } else if (importType.equals(IMPORT_TYPE_USER)) {
            return new UserImport(requestContext);
        }
        return null;
    }

    public Integer getCompanyId(String value) throws DatabaseException {
        ContactService contactService = ServiceProvider.getContactService(requestContext);

        Integer companyId = null;
        if (companyIdMap.containsKey(value)) {
            companyId = companyIdMap.get(value);
        } else {
            Company company = contactService.getSingleCompanyByName(value);
            if (company != null) {
                companyId = company.getId();
            }
            companyIdMap.put(value, companyId);
        }
        return companyId;
    }

    protected void addImportItem(ImportItem importItem) {
        Integer count = counts.get(importItem.getAction());
        if (count == null) {
            count = 0;
        }
        counts.put(importItem.getAction(), count + 1);
        importItems.add(importItem);
    }

    protected void buildImportResultsMessage() {
        StringBuilder sb = new StringBuilder();
        if (counts.containsKey(ImportItem.ACTION_ADD)) {
            sb.append(Localizer.getText(requestContext, "import.result.message.ADD", new Object[]{counts.get(ImportItem.ACTION_ADD)})).append(" ");
        }
        if (counts.containsKey(ImportItem.ACTION_UPDATE)) {
            sb.append(Localizer.getText(requestContext, "import.result.message.UPDATE", new Object[]{counts.get(ImportItem.ACTION_UPDATE)})).append(" ");
        }
        if (counts.containsKey(ImportItem.ACTION_ERROR)) {
            sb.append(Localizer.getText(requestContext, "import.result.message.ERROR", new Object[]{counts.get(ImportItem.ACTION_ERROR)}));
        }
        requestContext.getRequest().getSession().setAttribute(SessionManager.IMPORT_RESULTS_MESSAGE, sb.toString());
    }

    public List<ImportItem> getImportItems() {
        return importItems;
    }

    public void validate(List<String[]> data) throws Exception {
        boolean validateOnly = true;
        importData(data, validateOnly);
    }

    public void execute(List<String[]> data) throws Exception {
        boolean validateOnly = false;
        importData(data, validateOnly);
    }

    protected abstract void importData(List<String[]> data, boolean validateOnly) throws Exception;
}
