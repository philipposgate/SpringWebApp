<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:forEach var="mi" items="${menuItems}">
	<c:if test="${empty mi.children}">
		<li class="${mi.active ? 'active' : ''}"><a href="${mi.url}">${mi.name}</a></li>
	</c:if>
	<c:if test="${not empty mi.children}">
		<li class="dropdown ${mi.active ? 'active' : ''}">
			<a href="#" class="dropdown-toggle" data-toggle="dropdown">${mi.name} <b class="caret"></b></a>
			<ul class="dropdown-menu">
				<li class="${mi.url == pathElement.fullPath ? 'active' : ''}"><a href="${mi.url}">${mi.name}</a></li>
				<c:forEach var="miChild" items="${mi.children}">
					<c:set var="subMenuItem" value="${miChild}" scope="request" />
					<jsp:include page="baseHeaderSubMenuItem.jsp"/>
				</c:forEach>
			</ul>
		</li>
	</c:if>
</c:forEach>
