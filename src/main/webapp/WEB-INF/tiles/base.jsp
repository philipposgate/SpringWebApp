<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:if test="${empty pageTitle}">
	<c:set var="pageTitle" value="Spring Web App" />
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<title>${pageTitle}</title>
	
	<style type="text/css" media="all">
		@import url("/assets/jquery/jquery-ui-1.9.0.custom/css/smoothness/jquery-ui-1.9.0.custom.min.css");
		@import url("/assets/bootstrap-2.2.2/css/bootstrap.css");
		@import url("/assets/app.css");
	</style>
	<script type="text/javascript" src="/assets/jquery/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="/assets/jquery/jquery-ui-1.9.0.custom/js/jquery-ui-1.9.0.custom.min.js"></script>
	<script type="text/javascript" src="/assets/bootstrap-2.2.2/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/assets/app.js"></script>
	
	<%-- CKEditor & CKFinder --%>
	<c:if test="${loggedIn && isAdmin}">
		<script type="text/javascript" src="/assets/ckeditor/ckeditor.js"></script>
		<script type="text/javascript" src="/assets/ckeditor/adapters/jquery.js"></script>
		<script type="text/javascript" src="/assets/ckfinder/ckfinder.js"></script>
	</c:if>

</head>
<body>

	<script type="text/javascript">
		$(document).ready(function() {
		    var activePath = "/app/";
		    if (document.location.pathname != "/")
		    {
		        activePath = document.location.pathname;
		    }
		    
		    $('ul.nav > li > a[href="' + activePath + '"]').parent().addClass('active');
		});
	</script>
	
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
				<li><a href="/app/">Home</a></li>
				<li><a href="/app/about">About</a></li>
				<li><a href="/app/contact">Contact</a></li>
			</ul>
            <div class="pull-right">
	            <ul class="nav pull-right">
					<c:if test="${loggedIn}">
		              <li class="dropdown">
		                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Logged in as <B><sec:authentication property="principal.username"/></B> <b class="caret"></b></a>
		                <ul class="dropdown-menu">
		                  <c:choose>
			                  <c:when test="${isAdmin}">
				                  <li><a href="/app/admin/">Site Administration</a></li>
			                  </c:when>
			                  <c:otherwise>
				                  <li><a href="/app/user/">My Account</a></li>
			                  </c:otherwise>
		                  </c:choose>
		                  <li><a href="/j_spring_security_logout">Logout</a></li>
		                </ul>
		              </li>
					</c:if>
					<c:if test="${!loggedIn}">
						<li><a href="/app/login">Login</a></li>
					</c:if>
	            </ul>
            </div>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

	<div class="container" style="margin-top: 60px;">
	
		<tiles:insertAttribute name="body" />
	
	</div> 
	
</body>
</html>
	