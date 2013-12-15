<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<c:if test="${not empty successMessage}"><div class="successFadeout"><B>${successMessage}</B></div></c:if>
<c:if test="${not empty errorMessage}"><div class="errorFadeout"><B>${errorMessage}</B></div></c:if>