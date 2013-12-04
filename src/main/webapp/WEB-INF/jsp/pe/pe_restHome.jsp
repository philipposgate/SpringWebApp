<%@ include file="/WEB-INF/jsp/include.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Path Element Admin</title>
	<%@ include file="/WEB-INF/jsp/includeAssets.jsp"%>
	
	<script type="text/javascript">
		app.addComponent("jsTree");
		
		$(document).ready(function() {
		
			$("#peTree").jstree({
				"plugins" : [ "themes", "json_data", "ui", "crrm" ],
				"json_data" : {
					ajax : {
						url : "/rest/pe/json",
						data : function(n) {
							return {id : n.attr ? n.attr("id") : 0 }
						}
					}
				}
			});
			
			$("#addNode").on("click", function() {
				$("#peTree").jstree("create");
			});
		});
	</script>
</head>
<body>

<h1>Path Element Admin</h1>

<div>
	<a id="addNode" class="btn btn-mini" href="javascript:void(0)">Add Node</a>
</div>
<BR>
<div id="peTree"></div>

</body>
</html>