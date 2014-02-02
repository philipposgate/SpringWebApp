<%@ include file="/WEB-INF/jsp/include.jsp"%>

<div class="row-fluid">
	<div class="span8">
		<H1>Edit Event</H1>
	</div>
	<div class="span4">
		<div class="topBottomMargin">
			<a class="btn btn-small pull-right" href=""><i class="icon-cog"></i> Back to Calendar</a>
		</div>
	</div>
</div>
<HR>
<div class="row-fluid">
	<div class="span2">
		<div><B>Event Details</B></div>
	</div>
	<div class="span8">
		<form id="eventForm" action="${pathElement.fullPath}" method="POST">
			<input type="hidden" name="action" value="saveEvent">
			<input type="hidden" name="eventId" value="${event.id}">
			<table class="table table-striped">
				<tbody>
					<tr>
						<td>Event Title</td>
						<td><input type="text" name="title" value="${event.title}" autocomplete="off"></td>
					</tr>
				</tbody>
			</table>
			<input class="btn btn-primary" type="submit" value="Save">
		</form>
	</div>
</div>
