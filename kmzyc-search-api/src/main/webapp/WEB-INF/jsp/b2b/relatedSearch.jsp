<%@ page language="java" import="com.kmzyc.search.facade.config.Configuration" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <jsp:include page="/WEB-INF/jsp/common/b2bTemplate.jsp"></jsp:include>
</head>
<body>
	<jsp:include flush="true" page="/html/common/b2b/search-head.jsp"/>
	<div class="l-w sec-nogoods">
		<div class="nogoods-cont">
			<span class="fn-left ico-warn"></span>
			<div class="fn-block fn-t10">
				<p>很抱歉！没有找到与"<span class="fn-red">${keyword}</span>"相关的搜索结果，为您推荐以下结果</p>
			</div>
		</div>
	</div>
	<div class="l-w">
		<c:forEach items="${recommends }" var="recommend" varStatus="status">
		<div class="common-rec">
			<div class="mt">
				<h3 class="break-words">${recommend.warpQuery} <em>相关结果如下：</em></h3>
			</div>
			<div class="mc">
				<div class="rec-goods">
				<ul class="fn-clear m-prosales-list">
					<c:forEach items="${recommend.productList }" var="product" varStatus="status">
					<li name="p_item" class="m-prosales-item" >
						<div class="p-img">
							<a href="<%=Configuration.getContextProperty("detailPath_B2B")%>${product.skuId}.shtml" hidefocus="true" target="_blank">
								<c:if test="${product.tags!=null && fn:length(product.tags) > 0 }">
									<b class="ico-sale">${product.tags[0]}</b>
								</c:if>
								<img alt="" src="<%=Configuration.getContextProperty("picPath_B2B")%>${product.image}" onerror="this.src='<%=Configuration.getContextProperty("CSS_JS_PATH_B2B")%>images/default__logo_err240_240.jpg'">
							</a>
						</div>
						<div class="p-name">
							<a href="<%=Configuration.getContextProperty("detailPath_B2B")%>${product.skuId}.shtml" target="_blank" >
							<c:if test="${product.prodType == 3 }">
								<strong class="fn-red">[处方药]</strong>
							</c:if>
							${product.title}
							<span class="fn-red fn-l5">${product.subTitle}</span></a>
						</div>
						<div class="p-price">
							<strong>￥<fmt:formatNumber type="number" value="${product.price}" pattern="0.00" /></strong>
						</div>
						<div class="p-btns">
							<c:choose>
								<c:when test="${product.prodType == 3 }">
									<a class="btn-red30" href="<%=Configuration.getContextProperty("detailPath_B2B")%>${product.skuId}.shtml">查看详情</a>
									<a name="favoriteBtn" class="btn-collection fn-l10" href="javascript:void(0);" sid='${product.skuCode }' pri='${product.price }'><strong>+</strong>收藏</a>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${product.stock <=0}">
											<a name="outOfStockBtn" class="btn-gray" style="cursor:pointer;" disabled="true" data-sid='${product.stock }' >暂时缺货</a>
											<a name="favoriteBtn" class="btn-collection fn-l10" href="javascript:void(0);" sid='${product.skuCode }' pri='${product.price }'><strong>+</strong>收藏</a>
										</c:when>
										<c:otherwise>				
											<div id="flyItem" class="fly_item" data-center="1288,-805" style="left: 335px; top: 998px; visibility: hidden; transform: translate(1288px, -805px);">
												<img src="<%=Configuration.getContextProperty("CSS_JS_PATH_B2B")%>images/item-pic.jpg" width="40" height="40">
											</div>
											<a name="favoriteBtn" class="btn-collection" href="javascript:void(0);" sid='${product.skuCode }' pri='${product.price }'><strong>+</strong>收藏</a>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="shop-name">
						<c:choose>
							<c:when test="${product.supType != '2'}">
								<span>康美自营</span>
							</c:when>
							<c:otherwise>
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/shop/s-index-${product.shopId}.html" target="_blank" >${product.shopName }</a>
							</c:otherwise>
						</c:choose>
						</div>
					</li>
					</c:forEach>
               	</ul>
				</div>
				<a class="cr-showall" href="javascript:void(0)" term="${recommend.text }">查看全部&nbsp;&gt;</a>
				
			</div>
		</div>
		</c:forEach>
		<div class="bottom-search">
				<div class="fg-clear re-search">
					<div class="fg-line-key"> <b>重新搜索：</b>
					</div>
					<div class="fg-line-value">
						<input type="text" value="${keyword}" placeholder="请输入搜索关键字" class="input-txt input-XL" id="key-re-search">			
						<a id="re-search" href="javascript:void(0)" class="key-re-search"><span id="reSearch" >搜索</span></a>
					</div>
				</div>
		</div>

	</div>
	<!-- 底部 -->
	<jsp:include flush="true" page="/WEB-INF/jsp/common/b2bFootTemplate.jsp"/>
	<input type="hidden" id="originalQuery" value="${keyword }" />
</body>
</html>
