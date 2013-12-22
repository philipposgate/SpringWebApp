<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:set var="pageTitle" value="Web Content Management" scope="request" />
<tiles:insertDefinition name="base" flush="true">
	<tiles:putAttribute name="body">
		
		<script type="text/javascript">
			app.addComponent("jsTree");
			
			$(document).ready(function() {
			
				$("#peTree").jstree({
					plugins : [ "themes", "json_data", "ui", "crrm", "contextmenu" ],
					
					themes : {
						theme : "classic",
						icons : false,
						dots : true
					},
					
					json_data : {
						ajax : {
							url : "/rest/pe/treeNodes",
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
				            return {
				                "create": {
				                    "label": "Create Node",
				                    "action": function (obj) {
				                        this.create(obj);
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
					viewPE(data.rslt.obj[0].id);
				})
				.bind("create.jstree", function (e, data) {
			        $.ajax({
			            url: "/rest/pe/treeNode", 
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
			            url: "/rest/pe/treeNode/" + data.rslt.obj[0].id, 
			            type: "DELETE"
			        });
			    });
			});

			function viewPE(id)
			{
				$("#pathElementPanel").load("/rest/pe/pathElementView/" + id);
			}

			function editPE(id)
			{
				$("#pathElementPanel").load("/rest/pe/pathElementEdit/" + id);
			}

			function savePE(id)
			{
		        $.ajax({
		            url: "/rest/pe/pathElement/" + id, 
		            type: "PUT",
		            data: $("form#pathElementForm", "#pathElementPanel").serialize(),
		            success: function (result) {
		            	var node = $.parseJSON(result);
		            	$("#peTree").jstree("rename_node", "#" + id, node.data);
		            	viewPE(id);
		            }
		        });
			}

		</script>
		
		<h1><img class="img-rounded" src="/assets/images/classy_mustach_dude.jpg" style="max-height:50px;"> Web Content Management</h1>
		
		<div class="content-fluid">
			<div class="row-fluid">
				<div class="span4">
					<div class="well well-small"><B>Path Element Tree</B></div>
					<div id="peTree"></div>
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