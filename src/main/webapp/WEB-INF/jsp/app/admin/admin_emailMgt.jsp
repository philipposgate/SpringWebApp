<%@ include file="/WEB-INF/jsp/include.jsp"%>

<h2>Email Management</h2>

<div class="well">
	<B>Gmail Account Settings</B>
	
	<form action="/admin/saveEmailSettings" class="form-horizontal" method="POST">
		<div class="control-group">
			<label class="control-label" for="gmailUsername"><B>GMail Username</B></label>
			<div class="controls">
				<input type="text" id="gmailUsername" name="gmailUsername" value="${gmailUsername}" class="required email" style="width:300px;" maxlength="255">
			</div>
		</div>
	
		<div class="control-group">
			<label class="control-label" for="gmailPassword"><B>GMail Password</B></label>
			<div class="controls">
				<input type="password" id="gmailPassword" name="gmailPassword" value="${gmailPassword}" class="required email" style="width:300px;" maxlength="255">
			</div>
		</div>

		<input class="btn btn-primary" type="submit" value="Save Settings">	

	</form>
</div>