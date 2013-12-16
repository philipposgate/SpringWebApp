<%@ include file="/WEB-INF/jsp/include.jsp"%>

<table class="table table-condensed">
	<col width="20%">
	<tr>
		<td>ID</td>
		<td>${pathElement.id}</td>
	</tr>
	<tr>
		<td>Full Path</td>
		<td><a href="${pathElement.fullPath}">${pathElement.fullPath}</a></td>
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
			<td>${pathElement.controllerLabel}</td>
		</tr>
	</c:if>
	<tr>
		<td>Authentication Required</td>
		<td><span class="badge ${pathElement.authRequired ? "badge-important" : ""}">${pathElement.authRequired ? "YES" : "NO"}</span></td>
	</tr>
</table>
<a class="btn btn-primary" href="javascript:void(0)" onclick="editPE(${pathElement.id})">Edit</a>