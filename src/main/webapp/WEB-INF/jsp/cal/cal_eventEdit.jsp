<%@ include file="/WEB-INF/jsp/include.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		$('#editorTabs a:first').tab('show');
	});
	
	function saveEvent()
	{
		$("form#eventForm").submit();
	}
	
	function deleteEvent()
	{
		var request = {
			eventId : ${event.id},
			rnd : Math.floor(Math.random()*10000000)
		};

		$.post("/rest/calendar/${domain.id}/deleteEvent", request, function(data, status, xhr) {
			app.buildForm({action:'displayHome', successMessage: 'Event deleted.'}, '${pathElement.fullPath}').submit();
		}, "json");
	}
</script>

<div class="row-fluid">
	<button class="btn" onclick="app.buildForm({action:'displayHome'}, '${pathElement.fullPath}').submit()"><i class="icon-hand-left"></i></button>
	<button class="btn btn-danger" onclick="saveEvent()">Save</button>
	<button class="btn" onclick="app.buildForm({action:'displayHome'}, '${pathElement.fullPath}').submit()">Disgard	Changes</button>
	<button class="btn" onclick="deleteEvent()">Delete</button>
</div>

<HR>

<div class="row-fluid">
	<form id="eventForm" action="${pathElement.fullPath}" method="POST">
		<input type="hidden" name="action" value="saveEvent"> 
		<input type="hidden" name="eventId" value="${event.id}">

		<div>
			<input type="text" name="title" value="${event.title}" autocomplete="off" style="width: 450px;">
		</div>

		<div>
			<input type="text" name="startDate"	value="<fmt:formatDate value='${event.startDate}' pattern='yyyy/MM/dd'/>" autocomplete="off"> 
			to 
			<input type="text" name="endDate" value="<fmt:formatDate value='${event.endDate}' pattern='yyyy/MM/dd'/>" autocomplete="off">
		</div>

		<div>
			<input type="checkbox" name="allDay" autocomplete="off"	${event.allDay ? "checked" : ""}> All Day 
			&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; 
			<input type="checkbox" name="repeat" autocomplete="off"> Repeat...
		</div>

		<BR>

		<ul id="editorTabs" class="nav nav-tabs">
			<li><a href="#home" data-toggle="tab">Event Details</a></li>
			<li><a href="#profile" data-toggle="tab">Schedule</a></li>
		</ul>
		<div class="tab-content">
			<div class="tab-pane" id="home">
				<table>
					<tbody>
						<tr>
							<td>Calendar</td>
							<td>
								<select>
									<option>${event.calendar.title}</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>Event ID</td>
							<td>${event.id}</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="tab-pane" id="profile">...</div>
		</div>

	</form>
</div>
