<%@ include file="/WEB-INF/jsp/include.jsp"%>

<div class="container">
	<div class="row">
		<div class="span12">
			<div class="well">
				<c:if test="${not empty saved}">
					<div class="successFadeout"><B>Account Saved!</B></div>
				</c:if>
				<h1>My Account Details</h1>
				<form id="accountForm" action="/user/<sec:authentication property="principal.id"/>" method="POST">
					<table class="table table-striped">
						<tbody>
							<tr>
								<td>Account Created On</td>
								<td><sec:authentication property="principal.createDate" var="createDate" /> <fmt:formatDate pattern="MMM dd, yyyy" value="${createDate}" /></td>
							</tr>
							<tr>
								<td>First Name</td>
								<td><input type="text" name="firstName" value="<sec:authentication property="principal.firstName"/>" autocomplete="off"></td>
							</tr>
							<tr>
								<td>Last Name</td>
								<td><input type="text" name="lastName" value="<sec:authentication property="principal.lastName"/>" autocomplete="off"></td>
							</tr>
							<tr>
								<td>Email</td>
								<td><input type="text" name="email" value="<sec:authentication property="principal.email"/>" autocomplete="off"></td>
							</tr>
							<tr>
								<td>Username</td>
								<td><input type="text" name="username" value="<sec:authentication property="principal.username"/>" autocomplete="off"></td>
							</tr>
							<tr>
								<td>Password</td>
								<td><input type="password" name="password" value="<sec:authentication property="principal.password"/>" autocomplete="off"></td>
							</tr>
							<tr>
								<td>Confirm Password</td>
								<td><input type="password" name="confirmPassword" value="<sec:authentication property="principal.password"/>" autocomplete="off"></td>
							</tr>
						</tbody>
					</table>
					<p>
						<input type="submit" class="btn btn-primary btn-large" value="Update">
					</p>
				</form>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $("#accountForm").validate({
    		rules: {
    		    firstName: "required",
    		    lastName: "required",
    			username: {
    				required: true,
    				minlength: 3,
    				remote: {
    				    url: "/checkUsername",
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

