<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:set var="pageTitle" value="Website Administration" scope="request" />
<tiles:insertDefinition name="base" flush="true">
	<tiles:putAttribute name="body">
		
		<div class="container">
			<div class="row">
				<div class="span6 offset3">
					<div class="well">
						<center><h1>Website Administration</h1></center>
						<ul>
							<li><a href="/rest/admin/pe">Web Content Management</a></li>
							<li><a href="/rest/admin/users">User Management</a></li>
							<li><a href="/rest/admin/emailMgt">Gmail Management</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		
	</tiles:putAttribute>
</tiles:insertDefinition>


