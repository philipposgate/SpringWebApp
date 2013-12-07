<%@ include file="/WEB-INF/jsp/include.jsp"%>

<h1>Default Controller</h1>

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
			<td><a href="${pathElement.fullPath}.htm">${pathElement.fullPath}.htm</a></td>
			<td>${pathElement.controller}</td>
		</tr>
		</tbody>
	</table>
</div>
