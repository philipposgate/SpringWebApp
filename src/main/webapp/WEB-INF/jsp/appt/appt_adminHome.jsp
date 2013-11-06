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
			events: "/appts/admin/ajaxLoadAppts",
			allDaySlot:false,
			editable:true,
			eventAfterAllRender:function(view) {
				var events = $("#calendar").fullCalendar("clientEvents");
				events.sort(function (a,b){
					if (a.start < b.start)
						return -1;
					if (a.start > b.start)
						return 1;
					return 0;
				});
				$("#apptList").empty();
				for (var i=0;i<events.length;i++)
				{
					var e = events[i];
					if (!e.past)
					{
						var apptModel = {
							datetime: e.start.format("dddd, mmm d, yyyy @ h:mm tt"),
							title: e.title
						};
						var appt = $$(apptItem, apptModel);
						$$.document.append(appt, "#apptList");
					}
				}
			}
		});
		
		$("#refreshGCalBtn").on("click", function(){$.ajax("/appts/admin/adminAjaxRefreshGoogleCalendar/");});
	});
</script>

<div class="row-fluid">
	<div class="span8">
		<H1>Manage Appointments</H1>
	</div>
	<div class="span4">
		<div class="topBottomMargin">
			<a class="btn btn-small pull-right" href="/appts/admin/adminConfig/"><i class="icon-cog"></i> Configurations</a>
		</div>
	</div>
</div>
<HR>
<div class="row-fluid">
	<div class="span8">
		<div id="calendar"></div>
	</div>
	<div class="span4">
		<h2>Upcoming Appointments</h2>
		<div id="apptList"></div>
	</div>
</div>


