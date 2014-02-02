<%@ include file="/WEB-INF/jsp/include.jsp"%>

<script type="text/javascript">
	app.addComponent("fullCalendar");
	app.addComponent("qtip");
	app.addComponent("agilityjs");
	app.addComponent("dateFormat");
	
	$(document).ready(function() {
		
		var tooltip = $('<div/>').qtip({
			id: 'fullcalendar',
			prerender: true,
			content: {
				text: ' ',
				title: {
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
			eventClick: function(data, event, view) {
				var content = '<h3>'+data.title+'</h3>' + 
				'<p><b>Start:</b> '+data.start+'<br />' + 
				(data.end && '<p><b>End:</b> '+data.end+'</p>' || '') + 
				'<br /><a href="javascript:void(0)" class="btn btn-mini eventEditBtn" data-event-id="' + data.id + '">Edit</a>';
	
				tooltip.set({
					'content.text': content
				})
				.reposition(event).show(event);
			},
			dayClick: function() { tooltip.hide(); },
			eventResizeStart: function() { tooltip.hide(); },
			eventDragStart: function() { tooltip.hide(); },
			viewDisplay: function() { tooltip.hide(); },
			select: function( startDate, endDate, allDay, jsEvent, view ) {}
		});
		
		$("body").on("click", ".eventEditBtn", function(){ app.buildForm({action:"displayEventEdit",eventId:$(this).data("eventId")}).submit(); });
		
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
					<div class="calLabel" style="margin-left:10px;">${c.title}</div>
				</c:forEach>
			</div>
		</c:forEach>
	</div>
	<div class="span8">
		<div id="calendar"></div>
	</div>
</div>

