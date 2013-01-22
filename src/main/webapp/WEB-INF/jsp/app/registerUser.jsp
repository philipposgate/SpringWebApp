<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/app/includeTop.jsp"%>

<div class="container">
	<div class="row">
		<div class="span6 offset3">
			<div class="well">
				<h1>Create New Account</h1>
				<form id="registrationForm" action="/app/registerUser" method="POST">
					<table class="table table-striped">
						<tbody>
							<tr>
								<td>First Name</td>
								<td><input type="text" name="firstName" value="" autocomplete="off"></td>
							</tr>
							<tr>
								<td>Last Name</td>
								<td><input type="text" name="lastName" value="" autocomplete="off"></td>
							</tr>
							<tr>
								<td>Email</td>
								<td><input type="text" name="email" value="" autocomplete="off"></td>
							</tr>
							<tr>
								<td>Username</td>
								<td><input type="text" name="username" value="" autocomplete="off"></td>
							</tr>
							<tr>
								<td>Password</td>
								<td><input type="password" name="password" value="" autocomplete="off"></td>
							</tr>
							<tr>
								<td>Confirm Password</td>
								<td><input type="password" name="confirmPassword" value="" autocomplete="off"></td>
							</tr>
						</tbody>
					</table>
					<p>
						<input type="submit" class="btn btn-primary btn-large" value="Create Account">
						<a href="/app/" class="btn btn-large">Cancel</a>
					</p>
				</form>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $("#registrationForm").validate({
    		rules: {
    		    firstName: "required",
    		    lastName: "required",
    			username: {
    				required: true,
    				minlength: 3,
    				remote: {
    				    url: "/app/checkUsername",
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

<%@ include file="/WEB-INF/jsp/app/includeBottom.jsp"%>
