<%@ include file="/WEB-INF/jsp/include.jsp"%>

<style>
	input.date, input.time {
		width: 8em;
	}
</style>

<script type="text/javascript">
	$(document).ready(function() {
		$('#editorTabs a:first').tab('show');
		$("input[name=allDay]", "#eventForm").on("click", allDayClick);
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
	
	function allDayClick()
	{
		if ($("input[name=allDay]", "#eventForm").is(":checked"))
		{
			$(".time").hide();
		}
		else
		{
			$("input[name=startTime]", "#eventForm").val("10:00 am");
			$("input[name=endTime]", "#eventForm").val("11:00 am");
			$(".time").show();	
		} 
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
			<div>Event Title</div>
			<input type="text" name="title" value="${event.title}" style="width: 45em;">
		</div>

		<div>
			<input class="date" type="text" name="startDay" value="<fmt:formatDate value='${event.startDate}' pattern='yyyy/MM/dd'/>"> 
			<input class="time" type="text" name="startTime" value="<fmt:formatDate value='${event.startDate}' pattern='h:mm a'/>" style="${event.allDay ? 'display:none;' : ''}"> 
			to 
			<input class="time" type="text" name="endTime" value="<fmt:formatDate value='${event.endDate}' pattern='h:mm a'/>" style="${event.allDay ? 'display:none;' : ''}"> 
			<input class="date" type="text" name="endDay" value="<fmt:formatDate value='${event.endDate}' pattern='yyyy/MM/dd'/>">
		</div>

		<div>
			<input type="checkbox" name="allDay" ${event.allDay ? "checked" : ""}> All Day 
			&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
			<input type="checkbox" name="repeat"> Repeat...
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
							<td>Location</td>
							<td>
								<input type="text" name="location" value="${event.location}">
							</td>
						</tr>
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
