<%@ include file="/WEB-INF/jsp/include.jsp"%>

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
				<ul class="nav">

					<c:forEach var="mi" items="${menuItems}">
						<c:if test="${empty mi.children}">
							<li class="${mi.active ? 'active' : ''}"><a href="${mi.url}">${mi.name}</a></li>
						</c:if>
						<c:if test="${not empty mi.children}">
							<li class="dropdown ${mi.active ? 'active' : ''}">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown">${mi.name} <b class="caret"></b></a>
								<ul class="dropdown-menu">
									<li class="${mi.url == pathElement.fullPath ? 'active' : ''}"><a href="${mi.url}">${mi.name}</a></li>
									<c:forEach var="miChild" items="${mi.children}">
										<c:set var="subMenuItem" value="${miChild}" scope="request" />
										<jsp:include page="baseHeaderSubMenuItem.jsp"/>
									</c:forEach>
								</ul>
							</li>
						</c:if>
					</c:forEach>

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
							</ul>
						</li>
					</shiro:authenticated>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
</div>
