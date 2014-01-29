<%@ include file="/WEB-INF/jsp/include.jsp"%>

<h2>${pathElement.title}</h2>
<h3>Domain Error!</h3>

<table>
	<tr>
		<td>
			Path Element:
		</td>
		<td>
			${pathElement}
		</td>
	</tr>
	<tr>
		<td>
			Controller Bean:
		</td>
		<td>
			${pathElement.controllerBeanName}
		</td>
	</tr>
	<tr>
		<td>
			Domain Entity:
		</td>
		<td>
			${domainClass.name}
		</td>
	</tr>
	<tr>
		<td>
			Domain ID:
		</td>
		<td>
			${pathElement.domainId}
		</td>
	</tr>
</table>