<link href="/assets/jquery/jquery-ui-1.10.3.custom/css/smoothness/jquery-ui-1.10.3.custom.min.css" rel="stylesheet">
<link href="/assets/bootstrap-2.3.2/css/bootstrap.css" rel="stylesheet">
<link href="/assets/css/font-awesome/css/font-awesome.css" rel="stylesheet">
<!--[if IE 7]>
  <link rel="stylesheet" href="/assets/css/font-awesome/css/font-awesome-ie7.min.css">
<![endif]-->
<link href="/assets/app.css" rel="stylesheet">

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
