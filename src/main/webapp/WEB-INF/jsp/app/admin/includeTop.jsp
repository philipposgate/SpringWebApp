<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/app/includeTop.jsp"%>

	<script>
		var activeNav = "${activeNav}";
		$(document).ready(function(){
		    if (activeNav)
		    {
		        $("li." + activeNav).addClass("active");
		    }
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
          <a class="brand" href="/app/admin/">App Administration</a>
          <div class="nav-collapse collapse">
            <div class="pull-right">
	            <ul class="nav pull-right">
	              <li class="dropdown">
	                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Logged in as <B><sec:authentication property="principal.username"/></B> <b class="caret"></b></a>
	                <ul class="dropdown-menu">
	                  <li><a href="/j_spring_security_logout">Logout</a></li>
	                </ul>
	              </li>
	            </ul>
            </div>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

    <div class="container-fluid">
      <div class="row-fluid">
        <div class="span2">
          <div class="well sidebar-nav">
            <ul class="nav nav-list">
              <li class="nav-header">Navigation</li>
              <li class="adminHome"><a href="/app/admin/">Admin Home</a></li>
              <li class="userMgt"><a href="/app/admin/users">Manage Users</a></li>
              <li class="appHome"><a href="/app/">App Homepage</a></li>
            </ul>
          </div>
        </div>
        <div class="span10">
          <div class="well">