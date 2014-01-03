<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:set var="pageTitle" value="User Management" scope="request" />
<tiles:insertDefinition name="base" flush="true">
	<tiles:putAttribute name="body">
		
		<script>
			app.addComponent("jqueryValidate");
			
			$(document).ready(function(){
			    $("#userDialog").on("hidden", function() {
			        $(this).removeData("modal");
			    });
			});
			
			function editUser(userId)
			{
			    $("#userDialog").modal("show");
			}
			
			function updateUser() 
			{
			    if ($("#userForm", "#userDialog").valid())
			    {
				    $("#userForm", "#userDialog").submit();
			    }
			}
		</script>
		
		<c:if test="${not empty userUpdated}">
			<div class="successFadeout"><B>User Account Updated!</B></div>
		</c:if>
		
		<h2>User Management</h2>
		
		<table class="table table-condensed">
			<thead>
				<tr>
					<th></th>
					<th>ID</th>
					<th>Username</th>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="user" items="${users}">
					<tr>
						<td><a data-toggle="modal" href="/rest/admin/users/${user.id}" data-target="#userDialog" class="btn btn-mini" >edit</a></td>
						<td>${user.id}</td>
						<td>${user.username}</td>
						<td>${user.firstName}</td>
						<td>${user.lastName}</td>
						<td>${user.email}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<div id="userDialog" class="modal hide fade" style="max-height:1000px;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h3>User Details</h3>
			</div>
			<div class="modal-body">
				loading...
			</div>
			<div class="modal-footer">
				<a href="#" class="btn" data-dismiss="modal">Close</a> 
				<a href="javascript:void(0)" onclick="updateUser()" class="btn btn-primary">Save changes</a>
			</div>
		</div>
	

	</tiles:putAttribute>
</tiles:insertDefinition>
