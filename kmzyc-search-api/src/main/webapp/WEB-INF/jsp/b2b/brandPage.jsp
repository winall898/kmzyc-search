<%@ page language="java"  import="com.kmzyc.search.facade.config.Configuration"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE HTML>
<html>
	<head>
	    <jsp:include page="/WEB-INF/jsp/common/b2bTemplate.jsp"></jsp:include>
	</head>
	<body>
        <jsp:include page="/WEB-INF/jsp/common/b2bHeadTemplate.jsp"></jsp:include>
        <!-- 品牌广告 -->
        <div class="brand-detailed-banner">
        	<div class="banner-m">
        		<script src="<%=Configuration.getContextProperty("advPath_B2B")%>/prodBrand_${brand.brandId}.js"></script>
        	</div>
        </div>
        <!-- 品牌广告 end -->
        
        <!-- 品牌介绍 -->
        <div class="l-w">
        	<div class="brand-introduction fn-t10">
            	<h2>${brand.brandName}</h2>
            	<c:choose>
	            	<c:when test="${brand.introducePics != null && fn:length(brand.introducePics) > 0 }">
		                <div class="img-slid">
		                	<ul>
		                		<c:forEach items="${brand.introducePics}" var="pic" varStatus="status">
			                    	<li class="slide-li"><a href="javascript:void(0)"><img src="<%=Configuration.getContextProperty("picPath_B2B")%>${pic}" width="380" height="252"></a></li>
		                		</c:forEach>
		                    </ul>
		                </div>
	            	</c:when>
	            	<c:otherwise>
	            	<!-- 视频 -->
	            		<c:if test="${brand.introduceFilePath != null}">
			           		<div class="img-video">
				            	<div class="video">
				          			<embed width="380" height="250" type="application/x-shockwave-flash" src="<%=Configuration.getContextProperty("picPath_B2B")%>${brand.introduceFilePath}" flashvars="scene=taobao_shop" allowscriptaccess="never" />
				                </div>
			        		</div>
	        			</c:if>
	            	</c:otherwise>
            	</c:choose>
                <div class="content-m">
                	<h3><img width="142" height="50" title="logo" alt="logo" src="<%=Configuration.getContextProperty("picPath_B2B")%>${brand.logoPath}"></h3>
                    <p>${brand.des}</p>
                    <p class="fn-t10">
                    <c:forEach items="${brand.contactList}" var="contact" varStatus="status">
                    	${contact}<br>
                    </c:forEach>
                    </p>
                </div>
          </div>
    </div><!-- 品牌介绍  end -->
    
    	<c:if test="${productList != null && fn:length(productList) > 0}">
			<!-- 商品展示 -->
			<div class="l-w i-Search-page fn-clear">
				<!-- 资质证书 -->
				<div class="l-left fn-right fn-t10">
					<div class="m-w fn-b10">
						<div class="wh"><h3>新闻资讯</h3></div>
						<div class="wb" id="wb">
						</div>
					</div>
					<c:if test="${brand.honorList != null && fn:length(brand.honorList) > 0}">
					<div class="twocls-submenu">
						<div class="active">
							<h3 class="twocls-tit">资质证书</h3>	
						</div>
						<ul class="brand-detailed-imglist">
							<c:forEach items="${brand.honorList}" var="honor" varStatus="status">
		                    	<li><a target="_blank" href="<%=Configuration.getContextProperty("picPath_B2B")%>${honor}"><img src="<%=Configuration.getContextProperty("picPath_B2B")%>${honor}" width="176" title="资质证书"></a></li>
							</c:forEach>
	                    </ul>
					</div>
					</c:if><!-- 资质证书  end -->
				</div>
				
				<!-- 产品展示 -->
				<div class="l-right fn-left">
	                <!--搜索排序-->
					<tiles:insertDefinition name="b2bSearchSort"/>
					<tiles:insertDefinition name="b2bSearchProductList"/>
					<!--产品分页-->
					<tiles:insertDefinition name="b2bSearchPagination"/>
				</div><!-- 产品展示 end -->
			</div><!-- 商品展示  end -->
			<!-- oneclassify END -->
	    </c:if>
										
		<!-- 底部导航链接 -->
		<jsp:include page="/WEB-INF/jsp/common/b2bFootTemplate.jsp"></jsp:include>	
		<input type="hidden" id="brandId" value="${brand.brandId}"/>
	</body>
</html>