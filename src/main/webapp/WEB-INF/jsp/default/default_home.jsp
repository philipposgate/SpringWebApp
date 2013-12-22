<%@ include file="/WEB-INF/jsp/include.jsp"%>

<div class="row-fluid">
	<div class="span11">
		<h1>${empty pathElement.title ? "Default Controller" : pathElement.title}</h1>
	</div>
	<div class="span1">
		<a href="/rest/pe/"><img class="img-rounded pull-right" src="/assets/images/classy_mustach_dude.jpg" style="max-height:50px;"></a>
	</div>
</div>

<div class="well well-small">
	<table class="table table-condensed">
		<thead>
		<tr>
			<th>Title</th>
			<th>URL</th>
			<th>Controller</th>
			<th>Path Element ID</th>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td>${pathElement.title}</td>
			<td><a href="${pathElement.fullPath}">${pathElement.fullPath}</a></td>
			<td>${pathElement.controller}</td>
			<td>${pathElement.id}</td>
		</tr>
		</tbody>
	</table>
</div>
