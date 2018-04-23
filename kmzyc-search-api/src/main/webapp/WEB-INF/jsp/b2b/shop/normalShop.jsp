<%@ page language="java"  import="com.kmzyc.search.facade.config.Configuration"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.kmb2b.com/functions" prefix="km" %> 
<!DOCTYPE HTML>
<html>
<head>
	<jsp:include flush="true" page="/WEB-INF/jsp/common/b2bTemplate.jsp"/>
</head>
<body>
	<jsp:include flush="true" page="/html/common/b2b/search-head.jsp"/>
     <div class="shops-name">
         <div class="l-w pos-r">
             <h1>${supplierInfo.shopName}</h1>
             <a href="javascript:void(0);" class="collect"></a>
         </div>
     </div>
     <div class="sec-main l-w">
         <div class="sec-left fn-left">
            <ul class="shops-card clearfix">
               <li class="title02 b-bottom">${supplierInfo.shopName}</li>
               <li class="title03 b-bottom clearfix">
                   <span>市场：</span>
                   <p>${supplierInfo.marketName}</p>
               </li>
               <li class="title03 b-bottom clearfix">
                   <span>档口：</span>
                   <p>${supplierInfo.stallsNo}</p>
               </li>
               <li class="m-news-icon clearfix">
                   <div class="fn-left"><em class="m-news-one"></em><p>实名认证</p></div>
                   <div class="fn-right"><em class="m-news-two"></em><p>诚信商家</p></div>
              </li>
            </ul>
            <ul class="shops-card">
               <li class="title01">店铺分类</li>
               <div class="menu" id="shopMenu"></div>
            </ul>
         </div>
         <div class="sec-right fn-right">
            <h1 class="all-pro">
            	<a href="http://search.kmb2b.com/10/shop/s-index-${supplierInfo.shopId}.html">所有产品</a>
            	<c:choose>
               	<c:when test="${isKeyword == true}">
               		<c:if test="${kw != null && kw != ''}">
               			><span>关键字</span>：<span>${kw}</span>
               		</c:if>
            	</c:when>
            	<c:otherwise>
            		<c:if test="${categoryName != null && categoryName != ''}">
               			><span>店铺分类</span>：<span>${categoryName}</span>
               		</c:if>
            	</c:otherwise>
            	</c:choose>
            </h1>
            <div class="i-filter clearfix">
               <ul id="sortBar" class="filter-order fn-left">
                   <c:choose>
                   	<c:when test="${sort == 5 }">
	                    <li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a><i></i></li>
						<li class="current contrl-upward"><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a><i></i></li>
					</c:when>
	                <c:when test="${sort == 6 }">
	                    <li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a><i></i></li>
						<li class="current"><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '5') }">销量</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a><i></i></li>
					</c:when>
					<c:when test="${sort == 1 }">
						<li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a><i></i></li>
						<li class="current contrl-upward"><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '2') }">价格</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a><i></i></li>
					</c:when>
	                <c:when test="${sort == 2 }">
	                    <li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a><i></i></li>
						<li class="current"><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a><i></i></li>
					</c:when>
					<c:when test="${sort == 3 }">
						<li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a><i></i></li>
						<li class="current contrl-upward"><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a><i></i></li>
					</c:when>
	                <c:when test="${sort == 4 }">
	                    <li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '3') }">价格</a><i></i></li>
						<li class="current"><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '3') }">时间</a><i></i></li>
					</c:when>
					<c:otherwise>
						<li class="current"><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a><i></i></li>
						<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a><i></i></li>
					</c:otherwise>
	               </c:choose>
               </ul>
               <div class="shops-search fn-right">
               	   <p class="pro-n fn-left">共<span><c:if test="${count == null}">0</c:if><c:if test="${count != null}">${count}</c:if></span>个商品<i class="c-d6000f">${pn}</i>/<i>${pc}</i></p>
                   <form action="/10/shop/search" method="get" accept-charset="utf-8" class="fn-right">
                   	  <input type="hidden" id="shopId" name="shopid" value="${supplierInfo.shopId}"/>
                      <input type="text" id="search_keyword" name="kw" value="${kw}" class="search" placeholder="店内搜索"/>
                      <input type="button" id="shopSearchBtn" name="search-button" value="搜索" class="button" />
                   </form>
               </div>
           </div>
           <c:choose>
           <c:when test="${!empty productList}">
           <ul class="fn-clear m-prosales-list clearfix">
               <c:forEach items="${productList }" var="product" varStatus="status">
               <li name="p_item" class="m-prosales-item">
                   <div class="p-img">
                       <a title="${product.subTitle}" href="<%=Configuration.getContextProperty("detailPath_B2B")%>${product.skuId}.shtml" hidefocus="true" target="_blank">
                           <img alt="${product.subTitle}" src="<%=Configuration.getContextProperty("picPath_B2B")%>${product.image}" onerror="this.src='<%=Configuration.getContextProperty("CSS_JS_PATH_B2B")%>images/default__logo_err240_240.jpg'">
                       </a>
                   </div>
                   <div class="p-name">
                       <a href="<%=Configuration.getContextProperty("detailPath_B2B")%>${product.skuId}.shtml" target="_blank">${product.title} ${product.attrVals }</a>
                   </div>
                   <div class="p-price">
                       <strong>￥<fmt:formatNumber type="number" value="${product.price}" pattern="0.00" /></strong>
                       <span>起批量&nbsp;${product.bottomAmount}${product.unit}</span>
                   </div>

               </li>
               </c:forEach>
           </ul>
	       </c:when>
	       <c:otherwise>
	       <!-- 没有商品时 展示的效果 -->
           <div class="pro-none">
               <p>抱歉，没有找到相关搜索结果</p>
               <img src="<%=Configuration.getContextProperty("CSS_JS_PATH_B2B")%>/images/kmb2b/shops/pro_none.png" alt="抱歉，没有找到相关搜索结果">
           </div>
	       </c:otherwise>
		   </c:choose>
           
           <div class="clearfix">
               <div class="ui-page fn-right">
                   <!-- 上一页 -->
                   <c:choose>
                   		<c:when test="${pn eq 1 }">
                   			<a href="javascript:void(0)" class="ui-page-item ui-page-disabled ui-page-item-prev">上一页</a>
                   		</c:when>
                   		<c:when test="${pc eq 1 }">
                   			<a href="javascript:void(0)" class="ui-page-item ui-page-disabled ui-page-item-prev">上一页</a>
                   		</c:when>
                   		<c:otherwise>
                   			<a href="${km:qsReplace(searchURL, 'pn', (pn-1)) }" class="ui-page-item ui-page-item-prev">上一页</a>
                   		</c:otherwise>
					</c:choose>
                   <!-- 页码 -->
                   <c:choose>
	                   	<c:when test="${pc > 10 }">
                   		<c:choose>
                   		<c:when test="${pn < 6 }">
                   			<c:forEach begin="0" end="7" varStatus="loop">
                   				<c:choose>
		                   		<c:when test="${pn eq loop.count }">
		                   				<a href="javascript:void(0)" class="ui-page-item ui-page-item-current">${loop.count }</a>
		                   		</c:when>
		                   		<c:otherwise>
		                   			<a href="${km:qsReplace(searchURL, 'pn', loop.count) }" class="ui-page-item">${loop.count }</a>
		                   		</c:otherwise>
                   				</c:choose>
                   			</c:forEach>
                   			<c:choose>
                   				<c:when test="${(pn+1) eq pc }">
                     			<a href="${km:qsReplace(searchURL, 'pn', pn+1) }" class="ui-page-item">${pn+1 }</a>
                     			</c:when>
                     			<c:otherwise>
                     			<span class="ui-page-item ui-page-text">...</span>
                     			</c:otherwise>
                   			</c:choose>
                  				<a href="${km:qsReplace(searchURL, 'pn', pc) }" class="ui-page-item">${pc }</a>
                   		</c:when>
               			<c:otherwise>
               				<a href="${km:qsReplace(searchURL, 'pn', 1) }" class="ui-page-item">1</a>
              				<a href="${km:qsReplace(searchURL, 'pn', 2) }" class="ui-page-item">2</a>
                  			<span class="ui-page-item ui-page-text">...</span>
                  			<a href="${km:qsReplace(searchURL, 'pn', pn-2) }" class="ui-page-item">${pn-2 }</a>
                  			<a href="${km:qsReplace(searchURL, 'pn', pn-1) }" class="ui-page-item">${pn-1 }</a>
                  			<a href="javascript:void(0)" class="ui-page-item ui-page-item-current">${pn }</a>
                			
                			<c:choose>
           					<c:when test="${(pn+3) <= pc }">
           						<a href="${km:qsReplace(searchURL, 'pn', pn+1) }" class="ui-page-item">${pn+1 }</a>
           						<a href="${km:qsReplace(searchURL, 'pn', pn+2) }" class="ui-page-item">${pn+2 }</a>
           						<span class="ui-page-item ui-page-text">...</span>
           						<a href="${km:qsReplace(searchURL, 'pn', pc) }" class="ui-page-item">${pc }</a>
           					</c:when>
           					<c:otherwise>
           						<c:if test="${pn != pc }">
            						<c:forEach begin="0" end="${pc-pn-1 }" varStatus="loop">
            							<a href="${km:qsReplace(searchURL, 'pn', pn+loop.count) }" class="ui-page-item">${pn+loop.count }</a>
            						</c:forEach>
           						</c:if>
           					</c:otherwise>
                 			</c:choose>		
               			</c:otherwise>
               			</c:choose>
               		</c:when>
               		<c:otherwise>
               			<c:forEach begin="0" end="${pc-1}" varStatus="loop">
               				<c:choose>
               				<c:when test="${pn eq loop.count }">
               					<a href="javascript:void(0)" class="ui-page-item ui-page-item-current">${loop.count }</a>
               				</c:when>
               				<c:otherwise>
               					<a href="${km:qsReplace(searchURL, 'pn', loop.count) }" class="ui-page-item">${loop.count }</a>
               				</c:otherwise>
               				</c:choose>
               			</c:forEach>
               		</c:otherwise>
               	</c:choose>
                <!-- 下一页 -->
                <c:choose>
           		<c:when test="${pn eq pc }">
           			<a href="javascript:void(0)" class="ui-page-item ui-page-disabled ui-page-item-next">下一页</a>
           		</c:when>
           		<c:when test="${pc eq 1 }">
           			<a href="javascript:void(0)" class="ui-page-item ui-page-disabled ui-page-item-next">下一页</a>
           		</c:when>
           		<c:otherwise>
           			<a href="${km:qsReplace(searchURL, 'pn', (pn+1)) }" class="ui-page-item ui-page-item-next">下一页</a>
           		</c:otherwise>
           		</c:choose>
                <!-- 跳转 -->
                <span><input id="inputPageNo" name="pageNo" type="text" value="${pn }" title="" class="ui-page-number"></span>
                <span><input id="pageBtn" type="button" class="ui-page-item" value="GO"/></span>
                <input type="hidden" id="pageCount" value="${pc }" />
               </div>
           </div>
           <div class="shops-int">
               <h1>店铺简介</h1>
               <div class="int-content">
                   <p>
                   	${supplierInfo.introduce}
                   </p>
               </div>
           </div>
       </div>
   </div>
</body>
</html>
