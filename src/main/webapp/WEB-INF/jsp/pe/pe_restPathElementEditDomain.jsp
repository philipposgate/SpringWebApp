<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:if test="${not empty pathElement.controller.domainClass}">
	<c:if test="${empty pathElement.controllerDomain}">
		<span class="label label-important">WARNING: Domain Required!</span>
		<a class="btn btn-mini" href="javascript:void(0)" onclick="createNewDomain()">Create New Domain</a>
	</c:if>
	
	<c:if test="${not empty pathElement.controllerDomain}">
		<input type="text" name="domainName" value="${pathElement.controllerDomain.domainName}">
	</c:if>
</c:if>

