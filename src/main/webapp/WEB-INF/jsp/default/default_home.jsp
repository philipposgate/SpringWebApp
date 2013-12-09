<%@ include file="/WEB-INF/jsp/include.jsp"%>

<div class="row-fluid">
	<div class="span11">
		<h1>${empty pathElement.title ? "Default Controller" : pathElement.title}</h1>
	</div>
	<div class="span1">
		<img class="img-rounded pull-right" src="/assets/images/classy_mustach_dude.jpg" style="max-height:50px;">
	</div>
</div>

<div class="well well-small">
	<B>Path Element Info</B><BR>
	<table class="table table-condensed">
		<thead>
		<tr>
			<th>ID</th>
			<th>Title</th>
			<th>Path Element</th>
			<th>Full URL</th>
			<th>Controller</th>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td>${pathElement.id}</td>
			<td>${pathElement.title}</td>
			<td>${pathElement.path}</td>
			<td><a href="${pathElement.fullPath}">${pathElement.fullPath}</a></td>
			<td>${pathElement.controller}</td>
		</tr>
		</tbody>
	</table>
</div>
