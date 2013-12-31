<%@ include file="/WEB-INF/jsp/include.jsp"%>

<table class="table table-condensed table-hover">
	<col width="25%">
	<tr>
		<td>Path Element ID</td>
		<td>${pathElement.id}</td>
	</tr>
	<tr>
		<td style="vertical-align:middle">Title</td>
		<td>${pathElement.title}</td>
	</tr>
	<c:if test="${!pathElement.root}">
		<tr>
			<td style="vertical-align:middle">Path</td>
			<td>${pathElement.path}</td>
		</tr>
		<tr>
			<td style="vertical-align:middle">Controller</td>
			<td>${not empty pathElement.controllerLabel ? pathElement.controllerLabel : pathElement.controller}</td>
		</tr>
	</c:if>
	<tr>
		<td>Full Path</td>
		<td><a href="${pathElement.fullPath}">${pathElement.fullPath}</a></td>
	</tr>
	<tr>
		<td>Login is required</td>
		<td><span class="label ${pathElement.authRequired ? "label-inverse" : ""}">${pathElement.authRequired ? "YES" : "NO"}</span></td>
	</tr>
	<c:if test="${pathElement.authRequired}">
		<tr>
			<td>
				Required user role${fn:length(pathElement.roles) > 1 ? "s" : ""} 
				<c:if test="${fn:length(pathElement.roles) > 1 }">
					<span class="label label-important">${pathElement.allRolesRequired ? "All required" : "One required"}</span>
				</c:if>
			</td>
			<td>
				<c:if test="${not empty pathElement.roles}">
					<c:forEach var="r" items="${pathElement.roles}">
						<span class="label label-inverse">${r.role}</span>
					</c:forEach>
				</c:if>
				<c:if test="${empty pathElement.roles}">
					<span class="label">NONE</span>
				</c:if>
			</td>
		</tr>
		<tr>
			<td>Hide nav-link until permitted</td>
			<td><span class="label ${pathElement.hideNavWhenUnauthorized ? "label-inverse" : ""}">${pathElement.hideNavWhenUnauthorized ? "YES" : "NO"}</span></td>
		</tr>
	</c:if>
	<tr>
		<td>Parent</td>
		<td><ul><li><a href="${pathElement.parent.fullPath}">${pathElement.parent.fullPath}</a> (${pathElement.parent.title})</li></ul></td>
	</tr>
	<c:if test="${not empty pathElement.children}">
		<tr>
			<td>Children</td>
			<td>
				<ul>
					<c:forEach var="child" items="${pathElement.children}">
						<li><a href="${child.fullPath}">${child.fullPath}</a> (${child.title})</li>
					</c:forEach>
				</ul>
			</td>
		</tr>
	</c:if>
</table>
