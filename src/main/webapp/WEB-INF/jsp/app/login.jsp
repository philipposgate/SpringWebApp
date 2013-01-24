<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/includeTop.jsp"%>

<script type="text/javascript">
	$(document).ready(function(){
	    $("input[name=j_username]").focus();
	});
</script>
<div class="container">
	<div class="row">
		<div class="span6 offset3">
			<div class="well">
				<h1>Login</h1>

				<c:if test="${not empty error}">
					<div class="errorFadeout"><B>${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}</B></div>
				</c:if>
				
				<form name="f" action="/j_spring_security_check" method="POST">
					<table class="table table-striped">
						<tr><td>User:</td><td><input type="text" name="j_username"> <code>eg: 'admin' or 'user'</code></td></tr>
						<tr><td>Password:</td><td><input type="password" name="j_password"> <code>eg: 1</code></td></tr>
						<tr>
							<td colspan="2">
								<input class="btn btn-primary btn-large" name="submit" type="submit" value="Login"/>
								<a href="/app/" class="btn btn-large">Cancel</a>
								<a href="/app/registerUser/" class="btn btn-primary btn-large pull-right">Register</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</div>


<%@ include file="/WEB-INF/jsp/app/includeBottom.jsp"%>
