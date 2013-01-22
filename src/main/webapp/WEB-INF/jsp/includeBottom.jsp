	<div style="border-top:1px solid #555;padding:5px;margin-top:25px;position:absolute;bottom:0;text-align:right;">
		<c:if test="${loggedIn}">
			<a href="${isAdmin ? '/app/admin/' : '/app/user/'}">${isAdmin ? 'Admin Dashboard' : 'My Account'}</a>&nbsp;&nbsp;|&nbsp;&nbsp;
			<a href="/j_spring_security_logout">Logout</a>
		</c:if>
		<c:if test="${!loggedIn}">
			<a href="/app/login">Login</a>
		</c:if>
	</div>

</body>
</html>
