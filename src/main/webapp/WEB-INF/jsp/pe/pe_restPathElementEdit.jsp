<%@ include file="/WEB-INF/jsp/include.jsp"%>

<script type="text/javascript">
	var currentControllerBeanName = "${pathElement.controllerBeanName}";
	
	$(document).ready(function(){
		$("select[name=controller]").change(function(){
			if ($(this).val() == currentControllerBeanName)
			{
				$("div#controllerDomain").show();
			}
			else
			{
				$("div#controllerDomain").hide();
			}
		});
	});
	
	function createNewDomain()
	{
		$.post("/rest/admin/pe/${pathElement.id}/createNewDomain",{},function(){
			editPE(${pathElement.id});
		});
	}
</script>

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
							<option value="${controller.key}" ${pathElement.controllerBeanName == controller.key ? "selected" : ""}>${controller.value.label}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</c:if>
		<tr>
			<td>Domain</td>
			<td>
				<%@ include file="pe_restPathElementEditDomain.jsp"%>
			</td>
		</tr>
		<tr>
			<td>Login is required</td>
			<td><input type="checkbox" name="authRequired" ${pathElement.authRequired ? "checked" : ""} /></td>
		</tr>
		<tr class="securityConfig ${!pathElement.authRequired ? 'hidden' : ''}">
			<td>Required user role(s)</td>
			<td>
				<div style="margin-bottom:5px;">
					<input type="radio" name="allRolesRequired" value="false" ${!pathElement.allRolesRequired ? "checked" : ""} style="margin-top:-4px;" /> Any
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" name="allRolesRequired" value="true" ${pathElement.allRolesRequired ? "checked" : ""} style="margin-top:-4px;" /> All
				</div>
				<select size="6" name="roleId" multiple="multiple">
					<c:forEach var="rm" items="${roleMap}">
						<option value="${rm.key.id}" ${rm.value ? "selected" : ""}>${rm.key.role}</option>
					</c:forEach>
				</select>
				<BR><a href="javascript:void(0)" class="btn btn-mini" onclick="$('option:selected', 'select[name=roleId]').removeAttr('selected')">clear</a>
			</td>
		</tr>
		<tr class="securityConfig ${!pathElement.authRequired ? 'hidden' : ''}">
			<td>Hide nav-link until permitted</td>
			<td><input type="checkbox" name="hideNavWhenUnauthorized" ${pathElement.hideNavWhenUnauthorized ? "checked" : ""} /></td>
		</tr>
	</table>
</form>
<a class="btn btn-primary" href="javascript:void(0)" onclick="savePE(${pathElement.id})">Save</a>
<a class="btn" href="javascript:void(0)" onclick="viewPE(${pathElement.id})">Cancel</a>