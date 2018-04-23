<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.kmb2b.com/functions" prefix="km" %> 
<!--商品筛选-->

<div class="m-w m-w-noborder i-select">
	<div class="wb select-cont">
		<c:if test="${!empty productList && fn:length(productList) > 0 }">
		<c:forEach items="${facterList }" var="facter" varStatus="status">
			<c:choose>
				<c:when test="${facter.name eq '药材市场'}">
					<dl class="select-first wAuto">
					<dt>${facter.name }：</dt>
						<dd>
							<c:forEach items="${facter.fields}" var="field" varStatus="status">
								<a href="${km:fqAdd(km:qsRemove(km:qsRemove(searchURL, 'sort'), 'pn'), field.code, field.name)}" class="filtrate_a" title="${field.name}" selected='${field.selected}' style="text-decoration:none">${field.name}</a>
							</c:forEach>
						</dd>
						<span class="lots">更多<i></i></span>
            			<span class="lots-open">收起<i></i></span>
					</dl>
				</c:when>
				<c:otherwise>
					<!-- 属性集 -->
					<dl class="select-first borderB">
						<dt>${facter.name}：</dt>
						<dd>
							<c:forEach items="${facter.fields}" var="field" varStatus="status">
								<a href="${km:fqAdd(km:qsRemove(km:qsRemove(searchURL, 'sort'), 'pn'), facter.code, km:qsJoin('_', facter.name, field.name))}" class="filtrate_a" selected='${field.selected}' style="text-decoration:none">${field.name}</a>
							</c:forEach>
						</dd>
						<span class="lots">更多<i></i></span>
            			<span class="lots-open">收起<i></i></span>
					</dl>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		</c:if>
	</div>
</div>
<!--商品筛选结束-->
