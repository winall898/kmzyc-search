<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.kmb2b.com/functions" prefix="km" %> 
<div class="i-filter fn-clear">
	<ul id="sortBar" class="filter-order">
		<c:choose>
        	<c:when test="${sort == 5 }">
            	<li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
				<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a></li>
				<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a></li>					
				<li class="current contrl-upward"><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a></li>
			</c:when>
	       	<c:when test="${sort == 6 }">
	        	<li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
				<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a></li>
				<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a></li>
				<li class="current"><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '5') }">销量</a></li>
			</c:when>
			<c:when test="${sort == 1 }">
					<li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
					<li class="current contrl-upward"><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '2') }">价格</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a></li>
			</c:when>
            <c:when test="${sort == 2 }">
                    <li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
					<li class="current"><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a></li>
			</c:when>
			<c:when test="${sort == 3 }">
					<li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a></li>
					<li class="current contrl-upward"><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a></li>
			</c:when>
             <c:when test="${sort == 4 }">
                    <li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a></li>
					<li class="current"><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '3') }">时间</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a></li>
			</c:when>
				  <c:when test="${sort == 9 }">
                    <li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a></li>
			</c:when>
			<c:when test="${sort == 10 }">
                    <li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a></li>
			</c:when>
			<c:otherwise>
					<li class="current"><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a></li>
					<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a></li>
			</c:otherwise>
        </c:choose>
	</ul>
	<c:choose>
   		<c:when test="${km:fqContant(searchURL, 'stock') == true }">
   			<div class="filter-chose fn-left"><a href="${km:fqDel(km:qsRemove(searchURL, 'pn'), 'stock')}" class="filtrate_a selected"><b class="ico-checkbox"></b>不显示缺货商品</a></div>
   		</c:when>
   		<c:otherwise>
   			<div class="filter-chose fn-left"><a href="${km:fqAdd(km:qsRemove(searchURL, 'pn'), 'stock', '1-*')}" class="filtrate_a" title="${field.name}" ><b class="ico-checkbox"></b>不显示缺货商品</a></div>
   		</c:otherwise>
   	</c:choose>
   	&nbsp;&nbsp;&nbsp;
   	<c:choose>
   		<c:when test="${km:fqContant(searchURL, 'kmSelf') == true}">
   			<div class="filter-chose fn-left"><a href="${km:fqDel(km:qsRemove(searchURL, 'pn'), 'kmSelf')}" class="filtrate_a selected" style="color: #d0111f; font-weight: bold;"><b class="ico-checkbox"></b><b>康美自营</b></a></div>
   		</c:when>  
   		<c:otherwise>
   			<div class="filter-chose fn-left"><a href="${km:fqAdd(km:qsRemove(searchURL, 'pn'), 'kmSelf', '3-11')}" class="filtrate_a" style="color: #d0111f; font-weight: bold;"><b class="ico-checkbox"></b><b>康美自营</b></a></div>
   		</c:otherwise>
   	</c:choose>
	<div class="filter-total fn-right" id="topPagination">
	<span class="text"><i style="font-weight: normal;">${pn}</i>/${pc}</span>
	<c:choose>
   		<c:when test="${pn eq 1 }">
   			<span id="prev-span" class="prev-disabled">&lt;&lt;上一页</span>
   		</c:when>
   		<c:when test="${pc eq 1 }">
   			<span id="prev-span" class="prev-disabled">&lt;&lt;上一页</span>
   		</c:when>
   		<c:otherwise>
   			<a href="${km:qsReplace(searchURL, 'pn', (pn-1)) }" style="cursor:pointer;" class="next" style="text-decoration:none" >&lt;&lt;上一页</a>
   		</c:otherwise>
   	</c:choose>
	<c:choose>
   		<c:when test="${pn eq pc }">
   			<span id="next-span" class="prev-disabled">下一页&gt;&gt;</span>
   		</c:when>
   		<c:when test="${pc eq 1 }">
   			<span id="next-span" class="prev-disabled">下一页&gt;&gt;</span>
   		</c:when>
   		<c:otherwise>
   			<a href="${km:qsReplace(searchURL, 'pn', (pn+1)) }" style="cursor:pointer;" class="next" style="text-decoration:none" >下一页&gt;&gt;</a>
   		</c:otherwise>
    </c:choose>
	</div>
</div>
