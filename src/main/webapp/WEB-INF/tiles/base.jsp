<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:if test="${empty pageTitle}">
	<c:set var="pageTitle" value="Spring Web App" />
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<title>${pageTitle}</title>
	
	<link href="/assets/jquery/jquery-ui-1.10.3.custom/css/smoothness/jquery-ui-1.10.3.custom.min.css" rel="stylesheet">
	<link href="/assets/bootstrap-2.3.2/css/bootstrap.css" rel="stylesheet">
	<link href="/assets/css/font-awesome/css/font-awesome.css" rel="stylesheet">
	<!--[if IE 7]>
	  <link rel="stylesheet" href="/assets/css/font-awesome/css/font-awesome-ie7.min.css">
	<![endif]-->
	<link href="/assets/app.css" rel="stylesheet">
	
	<script type="text/javascript" src="/assets/jquery/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="/assets/jquery/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
	<script type="text/javascript" src="/assets/bootstrap-2.3.2/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/assets/app.js"></script>
	
	<%-- CKEditor & CKFinder --%>
	<c:if test="${loggedIn && isAdmin}">
		<script type="text/javascript" src="/assets/ckeditor/ckeditor.js"></script>
		<script type="text/javascript" src="/assets/ckeditor/adapters/jquery.js"></script>
		<script type="text/javascript" src="/assets/ckfinder/ckfinder.js"></script>
	</c:if>

</head>
<body>

	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container-fluid">
				<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> 
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span>
				</a> 
				<a class="brand" href="/">Spring Web App</a>
				
				<div class="nav-collapse collapse">
					<ul class="nav">
						<li class="${homeNav=='home' ? 'active' : ''}"><a href="/">Home</a></li>
						<li class="${homeNav=='about' ? 'active' : ''}"><a href="/about">About</a></li>
						<li class="${homeNav=='contact' ? 'active' : ''}"><a href="/contact">Contact</a></li>
						<li class="${homeNav=='contact' ? 'active' : ''}"><a href="/appts/">Appointments</a></li>
						<c:if test="${loggedIn}">
							<c:if test="${isAdmin}">
								<li class="dropdown ${not empty adminNav ? 'active' : ''}">
									<a href="#" class="dropdown-toggle" data-toggle="dropdown">Administration <b class="caret"></b></a>
									<ul class="dropdown-menu">
										<li class="${adminNav=='adminHome' ? 'active' : ''}">
											<a href="/admin/">Admin Home</a>
										</li>
										<li class="${adminNav=='userMgt' ? 'active' : ''}">
											<a href="/admin/users">Manage Users</a>
										</li>
										<li class="${adminNav=='apptMgt' ? 'active' : ''}">
											<a href="/appts/admin/adminHome/">Manage Appointments</a>
										</li>
										<li class="${adminNav=='emailMgt' ? 'active' : ''}">
											<a href="/admin/emailMgt">Email Config</a>
										</li>
										<li class="${adminNav=='endPoints' ? 'active' : ''}">
											<a href="/admin/endPoints">End Points</a>
										</li>
									</ul>
								</li>
							</c:if>
							<c:if test="${!isAdmin}">
								<li class="dropdown ${not empty userNav ? 'active' : ''}">
									<a href="#" class="dropdown-toggle" data-toggle="dropdown">My Account <b class="caret"></b></a>
									<ul class="dropdown-menu">
										<li class="${userNav=='userHome' ? 'active' : ''}">
											<a href="/user/">My Account Details</a>
										</li>
									</ul>
								</li>
							</c:if>
						</c:if>
					</ul>
					<div class="pull-right">
						<ul class="nav pull-right">
							<c:if test="${loggedIn}">
								<li class="dropdown">
									<a href="#" class="dropdown-toggle" data-toggle="dropdown">
										Logged in as <B><sec:authentication	property="principal.username" /></B> <b class="caret"></b>
									</a>
									<ul class="dropdown-menu">
										<c:choose>
											<c:when test="${isAdmin}">
												<li><a href="/admin/">Site Administration</a></li>
											</c:when>
											<c:otherwise>
												<li><a href="/user/">My Account</a></li>
											</c:otherwise>
										</c:choose>
										<li><a href="/j_spring_security_logout">Logout</a></li>
									</ul>
								</li>
							</c:if>
							<c:if test="${!loggedIn}">
								<li><a href="/login">Login</a></li>
							</c:if>
						</ul>
					</div>
				</div>

			</div>
		</div>
	</div>

	<div class="container" style="margin-top: 60px;">

		<tiles:insertAttribute name="body" />

	</div>

</body>
</html>
