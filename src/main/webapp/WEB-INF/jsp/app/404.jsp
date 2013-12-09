<%@ include file="/WEB-INF/jsp/include.jsp"%>

<tiles:insertDefinition name="base" flush="true">
	<tiles:putAttribute name="body">
		
		<div class="container">
			<div class="row">
				<div class="span6 offset3">
					<div class="well">
						<h1>404!</h1>
		
						<P>Page not found.</P>
						
						<P><a href="/" class="btn btn-large btn-inverse">Return to Home Page</a></P>
						
					</div>
				</div>
			</div>
		</div>
		
	</tiles:putAttribute>
</tiles:insertDefinition>


