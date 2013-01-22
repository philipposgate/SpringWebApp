<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${empty editorId}"><c:set var="editorId" value="editor"/></c:if>
<c:if test="${empty editorValue}"><c:set var="editorValue" value=""/></c:if>
<c:if test="${empty useFinder}"><c:set var="useFinder" value="false"/></c:if>
<c:if test="${empty editorToolbar}"><c:set var="editorToolbar" value="default"/></c:if>
<c:if test="${empty editorHeight}"><c:set var="editorHeight" value="200"/></c:if>
<c:if test="${empty editorWidth}"><c:set var="editorWidth" value="100%"/></c:if>
<c:if test="${empty editorLocale}"><c:set var="editorLocale" value="${orbisLocale}"/></c:if>
<c:set var="edtrId" value="${fn:replace(editorId, '.', '_')}" />

<script type="text/javascript">
	$(document).ready(function(){
		$("#${edtrId}").htmlEditor(${useFinder}, "${editorToolbar}", '${editorHeight}', '${editorWidth}', "${editorLocale}");
	});
</script>
<textarea id="${edtrId}" name="${editorId}">${editorValue}</textarea>	
