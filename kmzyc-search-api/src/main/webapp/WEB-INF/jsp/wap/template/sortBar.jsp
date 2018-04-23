<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

	<ul class="tabs-nav">
            <li id="zh_sort"><a href="${href }">综合</a></li>
            <c:choose>
				<c:when test="${sort == 1 }">
					<li id="xl_sort"><a href="${href }&sort=2">销量</a></li>
					<li id="jg_sort"><a href="${href }&sort=3">价格</a></li>
				</c:when>
				<c:when test="${sort == 2 }">
					<li id="xl_sort"><a href="${href }&sort=1">销量</a></li>
					<li id="jg_sort"><a href="${href }&sort=3">价格</a></li>
				</c:when>
				<c:when test="${sort == 3 }">
					<li id="xl_sort"><a href="${href }&sort=1">销量</a></li>
					<li id="jg_sort"><a href="${href }&sort=4">价格<i class="icon-long-arrow-up"></i></a></li>
				</c:when>
				<c:when test="${sort == 4 }">
					<li id="xl_sort"><a href="${href }&sort=1">销量</a></li>
					<li id="jg_sort"><a href="${href }&sort=3">价格</a></li>
				</c:when>
				<c:otherwise>
					<li id="xl_sort"><a href="${href }&sort=1">销量</a></li>
					<li id="jg_sort"><a href="${href }&sort=3">价格</a></li>
				</c:otherwise>
			</c:choose>
			<li class="j_pop">筛选</li>
    </ul>
