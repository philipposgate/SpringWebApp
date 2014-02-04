<%@ include file="/WEB-INF/jsp/include.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

	<c:if test="${empty pageTitle}"><c:set var="pageTitle" value="${pathElement.title}" /></c:if>
	<c:if test="${empty pageTitle}"><c:set var="pageTitle" value="Spring Web App" /></c:if>
	<title>${pageTitle}</title>
	
	<link href="/assets/jquery/jquery-ui-1.10.3.custom/css/smoothness/jquery-ui-1.10.3.custom.min.css" rel="stylesheet">
	<link href="/assets/bootstrap-2.3.2/css/bootstrap.css" rel="stylesheet">
	<link href="/assets/css/font-awesome/css/font-awesome.css" rel="stylesheet">
	<!--[if IE 7]> <link rel="stylesheet" href="/assets/css/font-awesome/css/font-awesome-ie7.min.css">	<![endif]-->
	<link href="/assets/app.css" rel="stylesheet">
	<link href="/assets/bootstrap-2.3.2/css/bootstrap-responsive.css" rel="stylesheet">
	
	<script type="text/javascript" src="/assets/jquery/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="/assets/jquery/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
	<script type="text/javascript" src="/assets/bootstrap-2.3.2/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/assets/app.js"></script>
	
	<%-- CKEditor & CKFinder --%>
	<c:if test="${loggedIn && isAdmin}">
		<script type="text/javascript" src="/assets/ckeditor/ckeditor.js"></script>
		<script type="text/javascript" src="/assets/ckeditor/adapters/jquery.js"></script>
		<script type="text/javascript" src="/assets/ckfinder/ckfinder.js"></script>
	</c:if>
	
</head>
<body>

	<tiles:insertAttribute name="header" />

	<%-- Global "success" and "error" message handlers --%>
	<c:if test="${not empty successMessage || not empty param.successMessage}"><div class="container-fluid"><div class="successFadeout"><B>${not empty successMessage ? successMessage : param.successMessage}</B></div></div></c:if>
	<c:if test="${not empty errorMessage || not empty param.errorMessage}"><div class="container-fluid"><div class="errorFadeout"><B>${not empty errorMessage ? errorMessage : param.errorMessage}</B></div></div></c:if>

	<div class="container-fluid">

		<tiles:insertAttribute name="body" />

	</div>

</body>
</html>
