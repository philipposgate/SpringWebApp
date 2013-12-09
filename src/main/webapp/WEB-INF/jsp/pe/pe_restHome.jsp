<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:set var="pageTitle" value="Web Content Management" scope="request" />
<tiles:insertDefinition name="base" flush="true">
	<tiles:putAttribute name="body">
		
		<script type="text/javascript">
			app.addComponent("jsTree");
			
			$(document).ready(function() {
			
				$("#jsTree").jstree({
					plugins : [ "themes", "json_data", "ui", "crrm", "contextmenu" ],
					
					themes : {
						theme : "classic",
						icons : false,
						dots : true
					},
					
					json_data : {
						data : [${rootElement}],
						ajax : {
							url : "/rest/pe/jstree",
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
				.bind("select_node.jstree", function(e, data) {
					$("#pathElementPanel").load("/rest/pe/pathElementPanel/" + data.rslt.obj[0].id);
				})
				.bind("create.jstree", function (e, data) {
			        $.ajax({
			            url: "/rest/pe/pathElement", 
			            type: "POST",
			            data: {
			            	"parentId" : data.rslt.parent[0].id,
			                "name" : data.rslt.name
			            },
			            success: function (result) {
			            	var newNode = $.parseJSON(result);
			            	data.rslt.obj[0].id = newNode.attr.id;
			            }
			        });
			    })
				.bind("remove.jstree", function (e, data) {
			        $.ajax({
			            url: "/rest/pe/pathElement/" + data.rslt.obj[0].id, 
			            type: "DELETE"
			        });
			    })
				.bind("rename.jstree", function (e, data) {
			        $.ajax({
			            url: "/rest/pe/pathElement/" + data.rslt.obj[0].id, 
			            type: "PUT",
			            data: {
			                "name" : data.rslt.new_name
			            }
			        });
			    });
			});
			
		</script>
		
		<h1>Web Content Management</h1>
		
		<div class="content-fluid">
			<div class="row-fluid">
				<div class="span4">
					<div class="well well-small"><B>Path Element Tree</B></div>
					<div id="jsTree"></div>
					<BR>
				</div>
				<div class="span8">
					<div class="well well-small"><B>Path Element Details</B></div>
					<div id="pathElementPanel"></div>
				</div>
			</div>
		</div>
		
	</tiles:putAttribute>
</tiles:insertDefinition>