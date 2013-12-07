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