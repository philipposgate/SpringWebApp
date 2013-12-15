<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:set var="pageTitle" value="Access Denied" scope="request" />
<tiles:insertDefinition name="base" flush="true">
	<tiles:putAttribute name="body">

		<center>
		
			<H2>ACCESS DENIED</H2>
		
			<P><a href="/" class="btn btn-large btn-inverse">Return to Home Page</a></P>
			
		</center>

	</tiles:putAttribute>
</tiles:insertDefinition>