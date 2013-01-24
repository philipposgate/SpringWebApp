<c:if test="${empty pageTitle}"><c:set var="pageTitle" value="Spring Web App - Administration"/></c:if>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>${pageTitle}</title>
	<style type="text/css" media="all">
		@import url("/assets/jquery/jquery-ui-1.9.0.custom/css/smoothness/jquery-ui-1.9.0.custom.min.css");
		@import url("/assets/bootstrap-2.2.2/css/bootstrap.css");
		@import url("/assets/app.css");
		.modal-body {
		    max-height: 800px;
		}
    </style>
	<script src="/assets/jquery/jquery-1.8.2.min.js"></script>
	<script src="/assets/jquery/jquery-ui-1.9.0.custom/js/jquery-ui-1.9.0.custom.min.js"></script>
	<script src="/assets/bootstrap-2.2.2/js/bootstrap.min.js"></script>
	<script src="/assets/scripts/jquery-validation-1.10.0/jquery.validate.min.js"></script>
	<script src="/assets/scripts/jquery-validation-1.10.0/additional-methods.min.js"></script>
	<script src="/assets/app.js"></script>
	
	<style>
		
		a,
		a:link,
		a:active,
		a:visited {
			color:#08C;
			text-decoration:none;
		}
		
	</style>
</head>
<body style="margin-top:60px;">
