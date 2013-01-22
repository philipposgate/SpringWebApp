<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:set var="activeNav" value="programSchedule"/>
<%@ include file="/WEB-INF/jsp/app/admin/includeTop.jsp"%>

<h2>Program Schedule</h2>

<a href="/app/admin/displayProgramEdit" class="btn btn-primary">Add a Program</a>

<HR>

<c:if test="${not empty programDeleted}">
	<div class="successFadeout">
		Program Deleted
	</div>
</c:if>
	
<div class="container-fluid">
	<div class="row-fluid">

		<c:if test="${not empty programs}">
			<table class="table table-striped table-compressed" cellpadding="0" cellspacing="0">
				<thead>
					<tr>
						<th>
							Program Name
						</th>
						<th>
							Genre
						</th>
						<th>
							Starts at
						</th>
						<th>
							Ends at
						</th>
						<th>
							Host
						</th>
	
						<th>
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="g" items="${programs}">
						<tr>
							<td>
								${g.name}
							</td>
							<td>
								${g.genre}
							</td>
							<td>
								${g.startDay} at ${g.startTime}
							</td>
							<td>
								${g.endDay} at ${g.endTime}
							</td>
							<td>
								${g.hosts}
							</td>
	
							<td>
								<a href="/app/admin/displayProgramEdit/${g.id}" class="btn btn-inverse btn-mini">Edit</a>
								<a href="#" class="btn btn-inverse btn-mini" onclick="if (confirm('Are you sure you wish to delete this entry?')) {$('#mtForm${g.id}').submit();} ">Delete</a>
								<a href="/app/admin/displayProgramArchive?programId=${g.id}" class="btn btn-inverse btn-mini">Manage Archive</a>
	
								<form id="mtForm${g.id}" action="/app/admin/deleteProgram">
									<input type="hidden" name="programId" value="${g.id}">
								</form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
		<c:if test="${empty programs}">
			There are no Programs.
		</c:if>
	</div>
</div>
	
<%@ include file="/WEB-INF/jsp/app/admin/includeBottom.jsp"%>


