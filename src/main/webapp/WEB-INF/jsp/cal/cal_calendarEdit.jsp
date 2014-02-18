<%@ include file="/WEB-INF/jsp/include.jsp"%>

<style>
	input.date, input.time {
		width: 8em;
	}
	.colorPik {float:left;margin:3px;width:15px;height:15px;}
	.colorPik:hover {cursor:pointer;}
</style>

<script type="text/javascript">
	$(document).ready(function() {
		$(document).on("click", ".colorPik", function() {
			$("input[name=colorTheme]", "#calendarForm").val($(this).data("theme"));
		});
	});
	
	function saveCalendar()
	{
		$("form#calendarForm").submit();
	}
</script>

<div class="row-fluid">
	<button class="btn" onclick="app.buildForm({action:'displayHome'}, '${pathElement.fullPath}').submit()"><i class="icon-hand-left"></i></button>
	<button class="btn btn-danger" onclick="saveCalendar()">Save</button>
	<button class="btn" onclick="app.buildForm({action:'displayHome'}, '${pathElement.fullPath}').submit()">Disgard	Changes</button>
	<button class="btn" onclick="deleteCalendar()">Delete</button>
</div>

<HR>

<div class="row-fluid">
	<form id="calendarForm" action="${pathElement.fullPath}" method="POST">
		<input type="hidden" name="action" value="saveCalendar"> 
		<input type="hidden" name="calendarListId" value="${calendarList.id}">
		<input type="hidden" name="calendarId" value="${calendar.id}">
		<input type="hidden" name="colorTheme" value="">

		<table class="table table-condensed">
			<tr>
				<td>Calendar List</td>
				<td><B>${calendarList.title}</B></td>
			</tr>
			<tr>
				<td>Calendar Title</td>
				<td><input type="text" name="title" value="${calendar.title}" style="width: 45em;"></td>
			</tr>
			<tr>
				<td>Color</td>
				<td>
					<div>
						<c:forEach var="color" items="${colorThemes}">
							<div class="colorPik" style="background-color:${color.background};" data-theme="${color}"></div>
						</c:forEach>
						<div style="clear:both;"></div>
					</div>
				</td>
			</tr>
		</table>

	</form>
</div>
