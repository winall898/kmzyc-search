<%@ page language="java"  import="com.kmzyc.search.facade.config.Configuration"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML>
<html>
	<head>
	<%
	
		int a = 0;
	%>
	    <jsp:include page="/WEB-INF/jsp/common/b2bTemplate.jsp"></jsp:include>
	</head>
	<body>
       <%@ include file="/WEB-INF/jsp/common/b2bHeadTemplate.jsp" %>
		<div class="i-page l-w">
    <div class="search-tit">
        <h2>找到与“<span class="fn-red">${keyword }</span>”相关店铺 ${shopCount } 家</h2>
        <div class="textright">
            <span>${pageNo }/${pageCount }</span>
            <a href="javascript:void(0)" class="btn-box">下一页</a>
        </div>
    </div>
    <div class="search-main">
        <div class="i-filter fn-clear">
            <strong>默认</strong>
            <div id="filterBar" class="filter-chose fn-left">
                <span>
                    <select id="stSelect">
                        <option value="0">店铺类型</option>
                        <option value="1">旗舰店</option>
                        <option value="2">专营店</option>
                        <option value="3">专卖店</option>
                    </select>
                </span>
            </div>
        </div>
        <div class="search-main">
            <c:forEach items="${shopList }" var="shop" varStatus="status">
           	<div class="shopitem">
                <div class="hd">
                    <div class="fn-left"><h2><span class="labe labe-khaki">${shop.typeName }<s></s></span><a href="<%=Configuration.getContextProperty("staticPath_B2B")%>/supply/${shop.id}/index.html" target="_blank" >${shop.name }</a></h2></div>
                    <div class="fn-right">
                        <a href="javascript:void(0)" class="s-btn fn-r5 j_consultation" data_type="${shop.contacType }" data_id="${shop.contact }" ><i class="advisory"></i>在线客服</a>
                        <a class="s-btn" href="<%=Configuration.getContextProperty("staticPath_B2B")%>/supply/${shop.id}/index.html" target="_blank" ><i class="into"></i>进入店铺</a>
                    </div>
                </div>
                <div class="bd">
                    <div class="shop-information">
                        <div class="toptext">
                            <p><label>所在地： </label>${shop.province } ${shop.city }</p>
                            <p><label>主营品牌： </label>${shop.manageBrand }</p>
                            <p>
                                <label>综合评分：</label>
                                <span class="sml-star">
                                    <span class="sml-star-default">
                                    	<c:choose>
											<c:when test="${1 <= shop.comprehensive && shop.comprehensive < 2 }">
												<span class="sml-star-present s1"></span>
											</c:when>
											<c:when test="${2 <= shop.comprehensive && shop.comprehensive < 3 }">
												<span class="sml-star-present s2"></span>
											</c:when>
											<c:when test="${3 <= shop.comprehensive && shop.comprehensive < 4 }">
												<span class="sml-star-present s3"></span>
											</c:when>
											<c:when test="${4 <= shop.comprehensive && shop.comprehensive < 5 }">
												<span class="sml-star-present s4"></span>
											</c:when>
											<c:when test="${shop.comprehensive >= 5 }">
												<span class="sml-star-present s5"></span>
											</c:when>
										</c:choose>
                                    </span>
                                </span>
                            </p>
                        </div>
                        <ul class="divided">
                            <li>宝贝描述相符：<fmt:formatNumber type="number" value="${shop.descScore }" pattern="0.00" /> 分</li>
                            <li>卖家发货速度：<fmt:formatNumber type="number" value="${shop.speedScore }" pattern="0.00" /> 分</li>
                            <li>物流配送速度：<fmt:formatNumber type="number" value="${shop.distScore }" pattern="0.00" /> 分</li>
                            <li>售前售后服务：<fmt:formatNumber type="number" value="${shop.saleScore }" pattern="0.00" /> 分</li>
                        </ul>
                    </div>
                    <ul class="products-lst">
                    	<c:forEach items="${shop.products }" var="product" varStatus="status">
                    	<li>
                            <div class="p-img">
	                            <a title="${product.subTitle}" href="<%=Configuration.getContextProperty("detailPath_B2B")%>${product.skuId}.shtml" target="_blank"> 
	                            <img alt="${product.subTitle}" src="<%=Configuration.getContextProperty("picPath_B2B")%>${product.image}"  onerror="this.src='<%=Configuration.getContextProperty("CSS_JS_PATH_B2B")%>images/default__logo_err170_170.jpg'">
	                            </a>
                            </div>
                            <div class="p-price">￥<fmt:formatNumber type="number" value="${product.price}" pattern="0.00" /></div>
                        </li>
                    	</c:forEach>
                    </ul>
                </div>
            </div>
            </c:forEach>
        </div>
    </div>
    <!--翻页-->
	<input id="shopType" type="hidden" value="${shopType }" />
	<input type="hidden" id="pageNo" name="pageNo" value="${pageNo}"/>
	<input type="hidden" id="pageCount" name="pageCount"  value="${pageCount}"/>
	<div class="fn-tr fn-t10 fn-clear">
		<div class="ui-page fn-right">
		</div>
	</div>
</div>
										
		<!-- 底部导航链接 -->
			<%@ include file="/WEB-INF/jsp/common/b2bFootTemplate.jsp" %>  	
	</body>
</html>