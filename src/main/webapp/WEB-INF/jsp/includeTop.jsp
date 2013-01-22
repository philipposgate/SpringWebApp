<c:if test="${empty pageTitle}">
	<c:set var="pageTitle" value="Spring Web App" />
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<title>${pageTitle}</title>
	
	<style type="text/css" media="all">
		@import	url("/assets/jquery/jquery-ui-1.9.0.custom/css/smoothness/jquery-ui-1.9.0.custom.min.css");
		@import url("/assets/app.css");
	</style>
	<script type="text/javascript" src="/assets/jquery/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="/assets/jquery/jquery-ui-1.9.0.custom/js/jquery-ui-1.9.0.custom.min.js"></script>
	<script type="text/javascript" src="/assets/app.js"></script>
	
	<%-- CKEditor & CKFinder --%>
	<c:if test="${loggedIn && isAdmin}">
		<script type="text/javascript" src="/assets/ckeditor/ckeditor.js"></script>
		<script type="text/javascript" src="/assets/ckeditor/adapters/jquery.js"></script>
		<script type="text/javascript" src="/assets/ckfinder/ckfinder.js"></script>
	</c:if>
	
</head>
<body style="background-color: #333; margin: 0px; padding: 0px; font-family: Arial, Helvetica, Verdana, sans-serif; font-size: 11px; color: #000;">