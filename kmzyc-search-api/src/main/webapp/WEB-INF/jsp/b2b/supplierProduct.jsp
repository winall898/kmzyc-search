<%@ page language="java"  import="com.kmzyc.search.facade.config.Configuration"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.kmb2b.com/functions" prefix="km" %> 
<!DOCTYPE HTML>
<html>
<head>
    <jsp:include page="/WEB-INF/jsp/common/b2bTemplate.jsp"></jsp:include>
</head>

<body>
<!-- 头部 -->
  <%@ include file="/WEB-INF/jsp/common/b2bHeadTemplate.jsp" %>
<div class="template-body">
	<div class="head">
		<jsp:include page="/html/common/b2b/SearchColumnWindow_${shopId}.jsp"></jsp:include>
        <div class="sp">
        	<h2>共${skuCount }个商品</h2>
        </div>
    </div>
    <input type="hidden" id="skuCount" name="skuCount" value="${skuCount}"/>
	<input type="hidden" id="sort" name="sort" value="${sort}"/>
	<input type="hidden" id="filtrate" name="filtrate" value="${filtrate}"/>
    <div class="main-all">
    	<div class="title">
        	<ul id="sortBar" class="filter-order-1">
            	<li name="zh" sn="" class="Comprehensive">
                	<a href="javascript:void(0)">综合</a>
                </li>
                <li name="sales" sn="sales" class="sale">
                	<a href="javascript:void(0)">销量</a>
                </li>
                <li name="price" sn="price_1" class="price">
                	<a href="javascript:void(0)">价格</a>
                </li>
                <li name="time" sn="time" class="Newest">
                	<a href="javascript:void(0)">最新</a>
                </li>
			</ul>
			<div class="Newest-2">
            	<input id="filter-checkbox" name="fa" code="f1" type="checkbox" value="">
            	<a href="javascript:void(0)">不显示缺货商品</a>
            </div>
			<div class="fn-right">
            	<div class="fy-1">
                	<a id="top-prev-btn" href="javascript:void(0)">上一页</a>
                </div>
				<div class="fy-2">
                	<a id="top-next-btn" href="javascript:void(0)">下一页</a>
                </div>
            </div>
        </div>
        <div class="main-1"> 
			<ul>
				<c:forEach items="${productList }" var="product" varStatus="status">
            	<li>
                	<div class="p-img">
                		<a href="<%=Configuration.getContextProperty("detailPath_B2B")%>${product.skuId}.shtml">
                		<img src="<%=Configuration.getContextProperty("picPath_B2B")%>${product.image}" onerror="this.src='<%=Configuration.getContextProperty("CSS_JS_PATH_B2B")%>images/default__logo_err240_240.jpg'">
                		</a>
                	</div>
                    <div class="p-name"><a href="javascript:void(0)" class="mc">${product.title} ${product.attrVals }</a></div>
                    <div class="Price">
                    	<strong>
                        	<span>￥</span>${km:prHead(product.price)}<span>${km:prLast(product.price)}</span>
                        </strong>
                    	<a name="shopcartBtn" class="btn-2 shopcart" data-sid='${product.skuId }' ></a>
                    </div>
                </li>
                </c:forEach>
            </ul>
        </div>
		<div class="fn-tr fn-t10">
			<form id="searchForm" method="POST" onsubmit="if(!this.action) return false;">
			<div class="ui-page fn-right">
			
			</div>
			</form>
		</div>
		<input type="hidden" id="pageNo" name="pageNo" value="${pageNo}"/>
		<input type="hidden" id="pageCount" name="pageCount"  value="${pageCount}"/>
    </div>
</div>
	<%@ include file="/WEB-INF/jsp/common/b2bFootTemplate.jsp" %>  	
</body>
</html>
