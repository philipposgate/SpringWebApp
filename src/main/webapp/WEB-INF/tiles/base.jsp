<%@ include file="/WEB-INF/jsp/include.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<c:if test="${empty pageTitle}"><c:set var="pageTitle" value="${pathElement.title}" /></c:if>
	<c:if test="${empty pageTitle}"><c:set var="pageTitle" value="Spring Web App" /></c:if>
	<title>${pageTitle}</title>

	<link href="/assets/jquery/jquery-ui-1.10.3.custom/css/smoothness/jquery-ui-1.10.3.custom.min.css" rel="stylesheet">
	<link href="/assets/bootstrap-2.3.2/css/bootstrap.css" rel="stylesheet">
	<link href="/assets/css/font-awesome/css/font-awesome.css" rel="stylesheet">
	<!--[if IE 7]>
	  <link rel="stylesheet" href="/assets/css/font-awesome/css/font-awesome-ie7.min.css">
	<![endif]-->
	<link href="/assets/app.css" rel="stylesheet">
    <link href="/assets/bootstrap-2.3.2/css/bootstrap-responsive.css" rel="stylesheet">
	
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
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="/">Spring Web App</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
				<li class="${pathElement.path=='index' ? 'active' : ''}"><a href="/index.htm">Home</a></li>
				<li class="${pathElement.path=='about' ? 'active' : ''}"><a href="/about.htm">About</a></li>
				<li class="${pathElement.path=='contact' ? 'active' : ''}"><a href="/contact.htm">Contact</a></li>
				<li class="${pathElement.path=='appts' ? 'active' : ''}"><a href="/rest/appts/">Appointments</a></li>
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
						<li class="${pathElement.path=='login' ? 'active' : ''}"><a href="/login.htm">Login</a></li>
					</c:if>
				</ul>
			</div>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

	<div class="container-fluid">

		<tiles:insertAttribute name="body" />

	</div>

</body>
</html>
