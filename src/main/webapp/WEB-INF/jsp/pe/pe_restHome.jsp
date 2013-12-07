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
				plugins : [ "themes", "json_data", "ui", "crrm", "contextmenu" ],
				json_data : {
					data : [${rootElement}],
					ajax : {
						url : "/rest/pe/json",
						type : "GET",
						data : function(n) {
							return {id : n.attr ? n.attr("id") : 0 }
						}
					},
		            'progressive_render': true,
		            'progressive_unload': false
				},
				contextmenu : {
			        "items": function (node) {
			        	console.log(node[0].id);
			            return {
			                "create": {
			                    "label": "Create Node",
			                    "action": function (obj) {
			                        this.create(obj);
			                    }
			                },
			                "rename": {
			                    "label": "Rename Node",
			                    "action": function (obj) {
			                        this.rename(obj);
			                    }
			                },
			                "delete": {
			                    "label": "Delete Node",
			                    "action": function (obj) {
			                        this.remove(obj);
			                    }
			                }
			            };
			        }
			    }
			})
			.bind("create.jstree", function (e, data) {
				console.log(e);
				console.log(data);
				
		        $.ajax({
		            url: "/rest/pe/pathElement", 
		            type: "POST",
		            data: {
		            	"parentId" : data.rslt.parent[0].id,
		                "name" : data.rslt.name
		            },
		            success: function (result) {
		            	console.log(result);
		            }
		        });
		    })
			.bind("remove.jstree", function (e, data) {
				console.log(e);
				console.log(data);
				
		        $.ajax({
		            url: "/rest/pe/pathElement/" + data.rslt.obj[0].id, 
		            type: "DELETE",
		            success: function (result) {
		            	console.log(result);
		            }
		        });
		    })
			.bind("rename.jstree", function (e, data) {
				console.log(e);
				console.log(data);
				
		        $.ajax({
		            url: "/rest/pe/pathElement/" + data.rslt.obj[0].id, 
		            type: "PUT",
		            data: {
		                "name" : data.rslt.new_name
		            },
		            success: function (result) {
		            	console.log(result);
		            }
		        });
		    });
		});
	</script>
</head>
<body>

<h1>Path Element Admin</h1>

<div id="peTree"></div>

</body>
</html>