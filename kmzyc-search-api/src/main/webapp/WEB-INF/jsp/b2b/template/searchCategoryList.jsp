<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.kmb2b.com/functions" prefix="km" %> 

<c:if test="${cateList != null && fn:length(cateList) > 0}">
<div class="active">
	<h3 class="twocls-tit">分类筛选</h3>
</div>
<ul class="twocls-submenu-list">
	<c:forEach items="${cateList}" var="parent" varStatus="status">
		<li class="twocls-submenu-item">
			<c:choose>
				<c:when test="${status.index == 0}">
					<i name="list-ico" class="ico-minus-green" index='${status.count}' ></i><a href="${km:qsReplace(km:qsRemove(km:qsRemove(km:qsRemove(searchURL, 'f'), 'sort'), 'pn'), 'c2', parent.code) }" style="cursor:pointer;">${parent.name}</a>
				</c:when>
				<c:otherwise>
					<i name="list-ico" class="ico-plus" index='${status.count}' ></i><a href="${km:qsReplace(km:qsRemove(km:qsRemove(km:qsRemove(searchURL, 'f'), 'sort'), 'pn'), 'c2', parent.code) }" style="cursor:pointer;">${parent.name}</a>
				</c:otherwise>
			</c:choose>
		</li>
		<c:choose>
			<c:when test="${status.index == 0}">
				<div class="twocls-link fn-clear" id='child_${status.count}' >
			</c:when>
			<c:otherwise>
				<div class="twocls-link fn-clear" id='child_${status.count}' style="display: none;">
			</c:otherwise>
		</c:choose>
			<c:forEach items="${parent.children}" var="child" varStatus="status">
				<ul>
		          <li><a name="thirdLink" style="text-decoration:none" href="${km:qsReplace(km:qsRemove(km:qsRemove(km:qsRemove(searchURL, 'f'), 'sort'), 'pn'), 'c3', child.code) }" >${child.name}</a></li>
		        </ul>
			</c:forEach>
		</div>
	    
	</c:forEach>
</ul>
</c:if>