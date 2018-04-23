<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${relevant != null && fn:length(relevant) > 0}">
<div class="search-sub">
	<div class="search-s">
		<span>你是不是想找：</span>
		<c:forEach items="${relevant}" var="suggest" varStatus="status">
			<a href="${baseURL}<c:url value=""><c:param name="kw" value="${suggest.name}"/></c:url>">${suggest.name}</a>
			<c:if test="${!status.last}">
				 | 
     		</c:if>
		</c:forEach>
    </div>
</div>
</c:if>