<%@ include file="/WEB-INF/jsp/include.jsp"%>

<h2>Spring MVC Endpoints</h2>

<c:forEach var="entry" items="${handlerMethods}">
	<table class="table table-condensed table-bordered">
		<col width="150">
		<tbody>
			<tr style="background-color:#DDD;">
				<td style="font-weight:bold;">URL Pattern:</td>
				<td style="font-weight:bold;">
					<c:if test="${not empty entry.key.patternsCondition.patterns}">
						<c:forEach var="pattern" items="${entry.key.patternsCondition.patterns}" varStatus="loop">
							${pattern}<c:if test="${!loop.last}"><BR></c:if>
						</c:forEach>
					</c:if>
				</td>
			</tr>
			<tr>
				<td>Controller Class:</td>
				<td>
					${entry.value.method.declaringClass.name}
				</td>
			</tr>
			<tr>
				<td>Controller Method:</td>
				<td>
					${entry.value.method.name}()
				</td>
			</tr>
			<tr>
				<td>Request Methods:</td>
				<td>
					<c:if test="${not empty entry.key.methodsCondition.methods}">
						${entry.key.methodsCondition.methods}
					</c:if>
				</td>
			</tr>
			<tr>
				<td>Headers:</td>
				<td>
					<c:if test="${not empty entry.key.headersCondition.expressions}">
						${entry.key.headersCondition.expressions}
					</c:if>
				</td>
			</tr>
			<tr>
				<td>Parameters:</td>
				<td>
					<c:if test="${not empty entry.key.paramsCondition.expressions}">
						${entry.key.paramsCondition.expressions}
					</c:if>
				</td>
			</tr>
			<tr>
				<td>Consumes:</td>
				<td>
					<c:if test="${not empty entry.key.consumesCondition.expressions}">
						${entry.key.consumesCondition.expressions}
					</c:if>
				</td>
			</tr>
			<tr>
				<td>Produces:</td>
				<td>
					<c:if test="${not empty entry.key.producesCondition.expressions}">
						${entry.key.producesCondition.expressions}
					</c:if>
				</td>
			</tr>
		</tbody>
	</table>
</c:forEach>
