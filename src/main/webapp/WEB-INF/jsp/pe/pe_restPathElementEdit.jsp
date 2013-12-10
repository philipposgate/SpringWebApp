<%@ include file="/WEB-INF/jsp/include.jsp"%>

<form id="pathElementForm">
	<input type="hidden" name="pathElementId" value="${pathElement.id}">
	
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
			<td><input type="text" name="title" value="${pathElement.title}"></td>
		</tr>
		<c:if test="${!pathElement.root}">
		<tr>
			<td style="vertical-align:middle">Path Element</td>
			<td><input type="text" name="path" value="${pathElement.path}"></td>
		</tr>
		<tr>
			<td style="vertical-align:middle">Controller</td>
			<td>
				<select name="controller" style="width:auto;">
					<c:forEach var="controller" items="${controllers}">
						<option value="${controller.key}" ${pathElement.controller == controller.key ? "selected" : ""}>${controller.value.label}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		</c:if>
	</table>
</form>
<a class="btn btn-primary" href="javascript:void(0)" onclick="savePE(${pathElement.id})">Save</a>
<a class="btn" href="javascript:void(0)" onclick="viewPE(${pathElement.id})">Cancel</a>