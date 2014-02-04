<%@ include file="/WEB-INF/jsp/include.jsp"%>
<script type="text/javascript">
	window.onload = function() { document.getElementById("repostForm").submit(); }
</script>
<form id="repostForm" method="POST" action="${pathElement.fullPath}">
	<c:forEach var="rp" items="${reqParams}">
		<c:forEach var="rpValue" items="${rp.value}">
			<input type="hidden" name="${rp.key}" value="${rpValue}">
		</c:forEach>
	</c:forEach>
</form>