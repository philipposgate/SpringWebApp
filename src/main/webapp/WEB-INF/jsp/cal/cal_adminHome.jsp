<%@ include file="/WEB-INF/jsp/include.jsp"%>

<script type="text/javascript">
	app.addComponent("fullCalendar");
	app.addComponent("agilityjs");
	app.addComponent("dateFormat");

	var apptItem = $$({
		view: {
			format: '<div class="well"> \
						<div data-bind="datetime"></div> \
						<div data-bind="title"></div> \
					 </div>',
			style: '&:hover { background-color:#EEE; cursor:pointer;}'
		}
	});
	
	$(document).ready(function() {
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
			dayClick: function(date, allDay, jsEvent, view) {alert("dayClick:" + date);},
			eventClick: function(calEvent, jsEvent, view) {alert('eventClick: ' + calEvent.title);},
			select: function( startDate, endDate, allDay, jsEvent, view ) {alert('select from ' + startDate + ' to ' + endDate);}
		});
		
		$("#refreshGCalBtn").on("click", function(){$.ajax("/appts/admin/adminAjaxRefreshGoogleCalendar/");});
	});
</script>

<div class="row-fluid">
	<div class="span8">
		<H1>${domain.domainName}</H1>
	</div>
	<div class="span4">
		<div class="topBottomMargin">
			<a class="btn btn-small pull-right" href=""><i class="icon-cog"></i> Configurations</a>
		</div>
	</div>
</div>
<HR>
<div class="row-fluid">
	<div class="span2">
		<c:forEach var="calList" items="${calLists}">
			<div>
				<div><B>${calList.title}</B></div>
				<c:forEach var="c" items="${calList.calendars}">
					<div class="calLabel" style="margin-left:10px;"><span>${c.title}</div>
				</c:forEach>
			</div>
		</c:forEach>
	</div>
	<div class="span10">
		<div id="calendar"></div>
	</div>
</div>

