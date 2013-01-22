<c:choose>
	<c:when test="${loggedIn && isAdmin}">
		<c:if test="${editMode}">
			<script type="text/javascript">
				$(document).ready(function(){
				    $("#saveHtmlBtn").click(function(){
				        $("#saveContentForm").submit();
				    });
				    $("#cancelHtmlBtn").click(function(){
				        app.buildForm({}, "${pageURL}", "GET").submit();
				    });
				});
			</script>
 			<div style="border-bottom:1px solid #CCC;margin-bottom:5px;">
				<button id="saveHtmlBtn">Save</button><button id="cancelHtmlBtn">Cancel</button>
			</div>
			<form id="saveContentForm" action="${pageURL}" method="POST">
				<input type="hidden" name="pageContentAction" value="save">
				<c:set var="editorId" value="htmlContent"/>
				<c:set var="editorValue" value="${pageContent}"/>
				<c:set var="editorToolbar" value="default"/>
				<c:set var="editorHeight" value="500"/>
				<c:set var="editorWidth" value="100%"/>
				<c:set var="editorLocale" value="en"/>
				<c:set var="useFinder" value="true"/>
				<%@ include file="/WEB-INF/jsp/htmlEditor.jsp"%>
			</form>
		</c:if>
		<c:if test="${!editMode}">
			<script type="text/javascript">
				$(document).ready(function(){
				    $("#editHtmlBtn").click(function(){
				        app.buildForm({pageContentAction:"edit"}, "${pageURL}", "GET").submit();
				    });
				});
			</script>
			<div style="float:right;cursor:pointer;">
				<img id="editHtmlBtn" src="/assets/images/edit.png" title="edit page">
			</div>
			${pageContent}
		</c:if>
	</c:when>
	<c:otherwise>
		${pageContent}
	</c:otherwise>
</c:choose>