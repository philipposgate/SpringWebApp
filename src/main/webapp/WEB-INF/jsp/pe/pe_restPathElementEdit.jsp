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
				<td style="vertical-align:middle">Path</td>
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
		<tr>
			<td>Authentication Required</td>
			<td><input type="checkbox" name="authRequired" ${pathElement.authRequired ? "checked" : ""} /></td>
		</tr>
		<tr>
			<td>Required User Role(s)</td>
			<td>
				<select size="6" name="roleId" multiple="multiple">
					<c:forEach var="rm" items="${roleMap}">
						<option value="${rm.key.id}" ${rm.value ? "selected" : ""}>${rm.key.role}</option>
					</c:forEach>
				</select>
				<BR><a href="javascript:void(0)" class="btn btn-mini" onclick="$('option:selected', 'select[name=roleId]').removeAttr('selected')">clear</a>
			</td>
		</tr>
	</table>
</form>
<a class="btn btn-primary" href="javascript:void(0)" onclick="savePE(${pathElement.id})">Save</a>
<a class="btn" href="javascript:void(0)" onclick="viewPE(${pathElement.id})">Cancel</a>