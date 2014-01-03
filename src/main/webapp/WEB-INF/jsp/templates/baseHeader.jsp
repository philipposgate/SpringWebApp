<%@ include file="/WEB-INF/jsp/include.jsp"%>

<shiro:hasRole name="ROLE_ADMIN">
	<script type="text/javascript">
		$(document).ready(function(){
			$(document).on("reload.nav", function(){
				$("ul#reloadableNav").load("/rest/pe/reloadNav");
			});
		});
	</script>
</shiro:hasRole>

<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container-fluid">
			<button type="button" class="btn btn-navbar" data-toggle="collapse"	data-target=".nav-collapse">
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span>
			</button>
			<a class="brand" href="/">Spring Web App</a>
			<div class="nav-collapse collapse">
				<ul id="reloadableNav" class="nav">

					<%@ include file="baseHeaderMenuItems.jsp"%>

				</ul>
				<ul class="nav pull-right">
					<shiro:notAuthenticated>
						<li>
							<a href="/login">Login</a>
						</li>
					</shiro:notAuthenticated>
					<shiro:authenticated>
						<li class="dropdown">
							<a href="#" class="dropdown-toggle"	data-toggle="dropdown"> 
								Logged in as <B><shiro:principal property="username" /></B> <b class="caret"></b>
							</a>
							<ul class="dropdown-menu">
								<li><a href="/logout">Logout</a></li>
								<shiro:hasRole name="ROLE_ADMIN">
									<li><a href="/rest/admin/">Site Administration</a></li>
								</shiro:hasRole>
							</ul>
						</li>
					</shiro:authenticated>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
</div>
