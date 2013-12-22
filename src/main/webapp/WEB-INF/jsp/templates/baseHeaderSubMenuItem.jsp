<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:if test="${empty subMenuItem.children}">
	<li class="${subMenuItem.active ? 'active' : ''}"><a href="${subMenuItem.url}">${subMenuItem.name}</a></li>
</c:if>
<c:if test="${not empty subMenuItem.children}">
	<li class="dropdown-submenu ${subMenuItem.active ? 'active' : ''}">
		<a tabindex="-1" href="${subMenuItem.url}">${subMenuItem.name}</a>
		<ul class="dropdown-menu">
			<c:forEach var="mi" items="${subMenuItem.children}">
				<c:set var="subMenuItem" value="${mi}" scope="request" />
				<jsp:include page="baseHeaderSubMenuItem.jsp"/>
			</c:forEach>
		</ul>
	</li>
</c:if>