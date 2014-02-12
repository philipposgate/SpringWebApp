<%@ include file="/WEB-INF/jsp/include.jsp"%>

<style>
	.qtip {max-width:none;}
	.editTipWhen, .viewTipWhen {white-space:nowrap;}
	.editTipWhatExample {font-size:85%;}
</style>

<script type="text/javascript">
	app.addComponent("fullCalendar");
	app.addComponent("qtip");
	
	<%-- GLOBAL VARS --%>
	var viewTip; // qtip api
	var editTip; // qtip api
	var selection = new Object(); // populated when user selects on day(s)
	
	$(document).ready(function() {
		
		<%-- SHOW viewTip ON EVENT CLICK --%>
		viewTip = $('<div/>').qtip({
			id: 'viewTip',
			content: {
				text: ' ',
				title: {
					text: 'Create Event',
					button: true
				}
			},
			position: {
				my: 'bottom center',
				at: 'top center',
				target: 'mouse',
				viewport: $('#calendar'),
				adjust: {
					mouse: false,
					scroll: false
				}
			},
			show: false,
			hide: false,
			style: 'qtip-bootstrap'
		}).qtip('api');

		
		<%-- SHOW editTip ON DAY CLICK --%>
		editTip = $('<div/>').qtip({
			id: 'editTip',
			show: false,
			hide: false,
			content: {
				text: ' ',
				title: {
					text: 'Create Event',
					button: true
				}
			},
			position: {
				my: 'bottom center',
				at: 'top center',
				target: 'mouse',
				viewport: $('#calendar'),
				adjust: {
					mouse: false,
					scroll: false
				}
			},
			style: {
				classes: 'qtip-bootstrap'
			},
			events: {
				show: onShow_editTip,
				move: onShow_editTip
			}
		}).qtip('api');


		<%-- FULLCALENDAR WIDGET --%>
		$("#calendar").fullCalendar({
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'month,agendaWeek,agendaDay'
			},
			events: "/rest/calendar/${domain.id}/userEvents",
			allDaySlot: true,
			selectable: true,
			editable: true,
			eventClick: eventClick,
			dayClick: dayClick,
			eventResize: eventResize,
			eventDrop: eventDrop,
			select: select,
			viewDisplay: function() { $(".qtip").hide(); },
			eventResizeStart: function() { $(".qtip").hide(); },
			eventDragStart: function() { $(".qtip").hide(); }
		});
		
		$("body").on("click", ".editEventBtn", function(){ app.buildForm({action:"displayEventEdit",eventId:$(this).data("eventId")}).submit(); });
		$("body").on("click", ".createEventBtn", createEvent);
		$("body").on("click", ".deleteEventBtn", deleteEvent);
		
// 		$(document).click(function(event) {
// 		    if (!$(event.target).closest("#calendar .fc-content").length) {
// 				viewTip.hide();
// 				editTip.hide();
// 		    }
// 		});		
	});
	
	function eventResize(event) 
	{ 
		updateEventDateTime(event.id, event.start, event.end);
	}
	
	function eventDrop(event) 
	{ 
		updateEventDateTime(event.id, event.start, event.end);
	}
	
	function select( startDate, endDate, allDay, jsEvent, view ) 
	{
		viewTip.hide();
		
		selection.startDate = startDate;
		selection.endDate = endDate;
		selection.allDay = allDay;

		var content = $(".editTipContent");
		$(content).find(".editTipWhen").html(getWhenText(startDate, endDate, allDay));
		$(content).find("input.editTipWhat").val("");
		
		editTip.set('content.text', content);
		editTip.reposition(jsEvent).show(jsEvent);
	}
	
	function getWhenText(startDate, endDate, allDay)
	{
		if (allDay)
		{
			if ($.fullCalendar.formatDate(startDate, 'yyyyMMdd') == $.fullCalendar.formatDate(endDate, 'yyyyMMdd'))
			{
				return $.fullCalendar.formatDate(startDate, 'ddd, MMMM d');
			}
			else
			{
				return $.fullCalendar.formatDate(startDate, 'ddd, MMMM d') + " to " + $.fullCalendar.formatDate(endDate, 'ddd, MMMM d');
			}
		}
		else
		{
			if ($.fullCalendar.formatDate(startDate, 'yyyyMMdd') == $.fullCalendar.formatDate(endDate, 'yyyyMMdd'))
			{
				return $.fullCalendar.formatDate(startDate, 'ddd, MMMM d, h:mm tt') + " to " + $.fullCalendar.formatDate(endDate, 'h:mm tt');
			}
			else
			{
				return $.fullCalendar.formatDate(startDate, 'ddd, MMMM d, h:mm tt') + " to " + $.fullCalendar.formatDate(endDate, 'ddd, MMMM d, h:mm tt');
			}
		}
	}
	
	function dayClick( date, allDay, jsEvent, view ) 
	{ 
		viewTip.hide();
		
		selection.startDate = date;
		selection.allDay = allDay;

		var content = $(".editTipContent");
		$(content).find(".editTipWhen").html($.fullCalendar.formatDate(date, 'ddd, MMMM d'));
		$(content).find("input.editTipWhat").val("");
		
		editTip.set('content.text', content);
		editTip.reposition(jsEvent).show(jsEvent);
	}
	
	function onShow_editTip()
	{
		$("body").stepDelay(500, function() {$("#qtip-editTip").find("input.editTipWhat").focus();});
	}
	
	function eventClick(data, jsEvent, view)
	{
		editTip.hide();

		var content = '<div class="viewTipWhen">' +
		'<h4>'+data.title+'</h4>' + 
		'<p>' + getWhenText(data.start, data.end, data.allDay) + '</p>' + 
		'<br /><a href="javascript:void(0)" class="btn btn-small btn-inverse editEventBtn" data-event-id="' + data.id + '">Edit Event</a>' +
		' <a href="javascript:void(0)" class="btn btn-small btn-danger deleteEventBtn">Delete Event</a>' +
		'</div>';

		viewTip.set('content.title', data.calendar.title);
		viewTip.set('content.text', content);
		viewTip.set('event_data', data);
		viewTip.reposition(jsEvent).show(jsEvent);
	}
	
	function createEvent()
	{
		var request = $("form", "#qtip-editTip").serializeFormToObject();
		request.startDate = $.fullCalendar.formatDate(selection.startDate, 'yyyy-MM-dd HH:mm');
		request.endDate = $.fullCalendar.formatDate(selection.endDate, 'yyyy-MM-dd HH:mm');
		request.allDay = selection.allDay;
		request.rnd = Math.floor(Math.random()*10000000);

		$.post("/rest/calendar/${domain.id}/createEvent", request, function(data, status, xhr) {
			editTip.hide();
			$("#calendar").fullCalendar("refetchEvents");
		}, "json");
	}
	
	function deleteEvent()
	{
		var eventData = viewTip.get('event_data');
		var request = {
			eventId : eventData.id,
			rnd : Math.floor(Math.random()*10000000)
		};

		$.post("/rest/calendar/${domain.id}/deleteEvent", request, function(data, status, xhr) {
			viewTip.hide();
			$("#calendar").fullCalendar("refetchEvents");
		}, "json");
	}
	
	function updateEventDateTime(eventId, startDate, endDate)
	{
		var request = new Object();
		request.eventId = eventId;
		request.startDate = $.fullCalendar.formatDate(startDate, 'yyyy-MM-dd HH:mm');
		request.endDate = $.fullCalendar.formatDate(endDate, 'yyyy-MM-dd HH:mm');
		request.rnd = Math.floor(Math.random()*10000000);

		$.post("/rest/calendar/${domain.id}/updateEventDateTime", request, function(data, status, xhr) {
			$("#calendar").fullCalendar("refetchEvents");
		}, "json");
	}

</script>

<div class="row-fluid">
	<div class="span2">
		<c:forEach var="calList" items="${calLists}">
			<div>
				<div><B>${calList.title}</B></div>
				<c:forEach var="c" items="${calList.calendars}">
					<div class="calLabel" style="margin-left:10px;">${c.title}</div>
				</c:forEach>
			</div>
		</c:forEach>
	</div>
	<div class="span8">
		<div id="calendar"></div>
	</div>
</div>

<%-- QTIP CONTENT --%>
<div style="display:none;">
	<div class="editTipContent">
		<form>
			<table>
				<tr>
					<td>When</td>
					<td class="editTipWhen"></td>
				</tr>
				<tr>
					<td>What</td>
					<td>
						<input class="editTipWhat" name="title" type="text">
						<div class="editTipWhatExample">e.g., 7pm Dinner at Pancho's</div>
					</td>
				</tr>
				<tr>
					<td>Calendar</td>
					<td class="editTipCalendar">
						<select name="calendarId">
							<c:forEach var="calList" items="${calLists}">
								<c:forEach var="c" items="${calList.calendars}">
									<option value="${c.id}">${c.title}</option>
								</c:forEach>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
		</form>
		<a href="javascript:void(0)" class="btn btn-small btn-inverse createEventBtn">Create Event</a>
	</div>
</div>

