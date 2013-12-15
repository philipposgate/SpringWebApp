<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:set var="pageTitle" value="Login" scope="request" />
<tiles:insertDefinition name="base" flush="true">
	<tiles:putAttribute name="body">
		
		<div class="container">
			<div class="row">
				<div class="span6 offset3">
					<div class="well">
						<h1>Login</h1>
		
						<shiro:notAuthenticated>
							<script type="text/javascript">
								$(document).ready(function(){
								    $("input[name=username]").focus();
								});
							</script>

							<c:if test="${not empty error}">
								<div class="errorFadeout"><B>Something bad happened</B></div>
							</c:if>
							
							<form action="" method="POST">
								<table class="table table-striped">
									<tr><td>User:</td><td><input type="text" name="username"> <code>eg: 'admin' or 'user'</code></td></tr>
									<tr><td>Password:</td><td><input type="password" name="password"> <code>eg: 1</code></td></tr>
									<tr>
										<td colspan="2">
											<input class="btn btn-primary btn-large" name="submit" type="submit" value="Login"/>
											<a href="/" class="btn btn-large">Cancel</a>
											<a href="/registerUser/" class="btn btn-primary btn-large pull-right">Register</a>
										</td>
									</tr>
								</table>
							</form>
						</shiro:notAuthenticated>
		
						<shiro:authenticated>
							<P>Dude, you're already logged in as <B><shiro:principal property="username" /></B>!</P>
							<P><a class="btn" href="/logout">Logout</a></P>
						</shiro:authenticated>
		
					</div>
				</div>
			</div>
		</div>
		
	</tiles:putAttribute>
</tiles:insertDefinition>

