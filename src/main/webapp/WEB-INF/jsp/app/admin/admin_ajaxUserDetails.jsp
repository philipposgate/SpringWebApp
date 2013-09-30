<%@ include file="/WEB-INF/jsp/include.jsp"%>
<form id="userForm" action="/admin/users/${user.id}" method="POST">
	<table class="table table-striped">
		<tbody>
			<tr>
				<td>Account Created On</td>
				<td><fmt:formatDate pattern="MMM dd, yyyy" value="${user.createDate}" /></td>
			</tr>
			<tr>
				<td>First Name</td>
				<td><input type="text" name="firstName" value="${user.firstName}" autocomplete="off"></td>
			</tr>
			<tr>
				<td>Last Name</td>
				<td><input type="text" name="lastName" value="${user.lastName}" autocomplete="off"></td>
			</tr>
			<tr>
				<td>Email</td>
				<td><input type="text" name="email" value="${user.email}" autocomplete="off"></td>
			</tr>
			<tr>
				<td>Username</td>
				<td><input type="text" name="username" value="${user.username}" autocomplete="off"></td>
			</tr>
			<tr>
				<td>Password</td>
				<td><input type="password" name="password" value="${user.password}" autocomplete="off"></td>
			</tr>
			<tr>
				<td>Confirm Password</td>
				<td><input type="password" name="confirmPassword" value="${user.password}" autocomplete="off"></td>
			</tr>
			<%-- 
			<tr>
				<td>Account Expired</td>
				<td><input type="checkbox" name="accountNonExpired" ${user.accountNonExpired ? "" : "checked"}></td>
			</tr>
			<tr>
				<td>Account Locked</td>
				<td><input type="checkbox" name="accountNonLocked" ${user.accountNonLocked ? "" : "checked"}></td>
			</tr>
			<tr>
				<td>Account Credentials Expired</td>
				<td><input type="checkbox" name="credentialsNonExpired" ${user.credentialsNonExpired ? "" : "checked"}></td>
			</tr>
			--%>
			<tr>
				<td>Account Enabled</td>
				<td><input type="checkbox" name="enabled" ${user.enabled ? "checked" : ""}></td>
			</tr>
			<tr>
				<td>Chat Enabled</td>
				<td><input type="checkbox" name="chatEnabled" ${chatEnabled ? "checked" : ""}></td>
			</tr>
		</tbody>
	</table>
</form>

<script type="text/javascript">
    $(document).ready(function() {
        $("#userForm", "#userDialog").validate({
    		rules: {
    		    firstName: "required",
    		    lastName: "required",
    			username: {
    				required: true,
    				minlength: 3,
    				remote: {
    				    url: "/checkUsername/${user.id}",
    				    type: "GET"
    				}
    			},
    			password: {
    				required: true,
    				minlength: 5
    			},
    			confirmPassword: {
    				required: true,
    				minlength: 5,
    				equalTo: "[name=password]"
    			},
    			email: {
    				required: true,
    				email: true
    			}
    		},
    		messages: {
    		    firstName: "Please enter your first name",
    		    lastName: "Please enter your last name",
    			email: "Please enter a valid email address",
    			username: {
    				required: "Please enter a username",
    				minlength: "Your username must consist of at least 3 characters",
    				remote: "Please choose a different username"
    			},
    			password: {
    				required: "Please provide a password",
    				minlength: "Your password must be at least 5 characters long"
    			},
    			confirmPassword: {
    				required: "Please provide a password",
    				minlength: "Your password must be at least 5 characters long",
    				equalTo: "Please enter the same password as above"
    			}
    		}
    	});
    });
</script>

