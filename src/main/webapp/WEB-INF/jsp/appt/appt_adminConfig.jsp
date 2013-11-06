<%@ include file="/WEB-INF/jsp/include.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		$("#refreshGCalBtn").on("click", function(){$.ajax("/appts/admin/ajaxRefreshGoogleCalendar/");});
	});
</script>

<div class="row-fluid">
	<div class="span8">
		<H1>Appointment Configuration</H1>
	</div>
	<div class="span4">
		<div class="topBottomMargin">
			<a class="btn btn-small pull-right" href="/appts/admin/adminHome/"><i class="icon-hand-left"></i> Back to Appointment Management</a>
		</div>
	</div>
</div>

<HR>
<div class="well">
	<a id="refreshGCalBtn" class="btn btn-small btn-inverse" href="javascript:void(0)">Refresh Google Calendar</a>
</div>

<div class="well">
	<i class="icon-info-sign icon-large"></i> The appointment calendar synchronizes with your Google Calendar.  The means certain configurations are required
	
	<form class="form-horizontal" action="/appts/admin/adminSaveConfig/" method="POST">

		<div class="control-group">
			<label class="control-label" for="apptGoogleEnabled"><B>Google Calendar Enabled</B></label>
			<div class="controls">
				<input type="checkbox" id="apptGoogleEnabled" name="apptGoogleEnabled">
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="apptGoogleCalendarName"><B>Google Calendar Name</B></label>
			<div class="controls">
				
				<input type="text" id="apptGoogleCalendarName" name="apptGoogleCalendarName" style="width:300px;" maxlength="255">
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="apptGoogleCalendarLocation"><B>Google Calendar Location</B></label>
			<div class="controls">
				<input type="text" id="apptGoogleCalendarLocation" name="apptGoogleCalendarLocation" style="width:300px;" maxlength="255">
			</div>
		</div>
	
		<input type="submit" class="btn btn-primary" value="Save">
	</form>
</div>
