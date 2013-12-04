<div style="margin-left:10px;">
	${element.fullPath}.htm <i class="icon-arrow-right"></i> ${element.controller}
	<c:forEach var="element" items="${element.children}">
		<c:set var="element" value="${element}" scope="request" />
<%--         <jsp:include page="pe_renderElement.jsp"/> --%>
<%-- 		<%@ include file="pe_renderElement.jsp"%> --%>
	</c:forEach>
</div>