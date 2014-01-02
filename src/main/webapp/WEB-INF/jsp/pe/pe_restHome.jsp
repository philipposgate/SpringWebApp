<%@ include file="/WEB-INF/jsp/include.jsp"%>

<style>
	<%-- jstree bug fix - see: https://github.com/vakata/jstree/issues/174 --%>
	#jstree-marker-line {pointer-events: none;}
</style>

<c:set var="pageTitle" value="Web Content Management" scope="request" />
<tiles:insertDefinition name="base" flush="true">
	<tiles:putAttribute name="body">
		
		<script type="text/javascript">
			app.addComponent("jsTree");
			
			$(document).ready(function() {
			
				$(document).on("click", "input[name=authRequired]", function() {
					if ($(this).is(":checked"))
						$(".securityConfig").removeClass("hidden");
					else
						$(".securityConfig").addClass("hidden");
				});
				
				initTree();
			});

			function initTree()
			{
				$("#peTree").jstree({
					plugins : [ "themes", "json_data", "ui", "crrm", "contextmenu", "dnd" ],
					
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
			            progressive_render: true,
			            progressive_unload: false
					},
					
					contextmenu : {
				        items: function (node) {
				            return {
				                create: {
				                    label : "Create Node",
				                    action : function (obj) {
				                        this.create(obj);
				                    }
				                },
				                "delete" : {
				                    label : "Delete Node",
				                    action : function (obj) {
				                       	this.remove(obj);
				                    }
				                }
				            };
				        }
				    },
				    
				    crrm : {
				    	move : {
					    	check_move : function(m) {
					    		if ($(m.np).attr("id") == "peTree") return false;
					    		return true;
					    	}
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
			    			$(document).trigger("reload.nav");
			            }
			        });
			    })
				.bind("remove.jstree", function (e, data) {
			        $.ajax({
			            url: "/rest/pe/treeNode/" + data.rslt.obj[0].id, 
			            type: "DELETE",
			            success: function(){
			    			$(document).trigger("reload.nav");
			            }
			        });
			    })
			    .bind("move_node.jstree", function(e, data) {
			    	var prevParent = $(data.rslt.op).attr("id");
			    	var prevChildren = new Array();
			    	$(data.rslt.op).find("> ul > li").each(function(){
			    		prevChildren.push($(this).attr("id"));
			    	});

			    	var newParent = $(data.rslt.np).attr("id");
			    	var newChildren = new Array();
			    	$(data.rslt.np).find("> ul > li").each(function(){
			    		newChildren.push($(this).attr("id"));
			    	});
			    	
			    	$.post("/rest/pe/moveTreeNode", {
			    		prevParentId: prevParent,
			    		prevChildren: prevChildren,
			    		newParentId: newParent,
			    		newChildren: newChildren
			    	}, function() {
		    			$(document).trigger("reload.nav");
			    	});
			    });
			}
			
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
		    			$(document).trigger("reload.nav");
		            }
		        });
			}

		</script>
		
		<h1><img class="img-rounded" src="/assets/images/classy_mustach_dude.jpg" style="max-height:50px;"> Web Content Management</h1>
		
		<div class="content-fluid">
			<div class="row-fluid">
				<div class="span4">
					<div class="well well-small"><B>Web Content Tree</B></div>
					<div id="peTree"></div>
					<BR>
				</div>
				<div class="span8">
					<div class="well well-small"><B>Content Element Configuration</B></div>
					<div id="pathElementPanel"></div>
				</div>
			</div>
		</div>
		
	</tiles:putAttribute>
</tiles:insertDefinition>