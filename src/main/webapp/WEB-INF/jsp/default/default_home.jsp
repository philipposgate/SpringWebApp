<%@ include file="/WEB-INF/jsp/include.jsp"%>

<div class="row-fluid">
	<div class="span11">
		<h1>${empty pathElement.title ? "Default Controller" : pathElement.title}</h1>
	</div>
	<div class="span1">
		<a href="/rest/pe/"><img class="img-rounded pull-right" src="/assets/images/classy_mustach_dude.jpg" style="max-height:50px;"></a>
	</div>
</div>

<%@ include file="/WEB-INF/jsp/pe/pe_view.jsp"%>

