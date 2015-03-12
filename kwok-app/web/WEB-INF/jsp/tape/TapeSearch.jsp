<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>


<%-- Tape search --%>
<h3><bean:message key="itMgmt.index.tapeSearchHeader"/></h3>

<jsp:include page="/WEB-INF/jsp/tape/TapeSearchTemplate.jsp"/>