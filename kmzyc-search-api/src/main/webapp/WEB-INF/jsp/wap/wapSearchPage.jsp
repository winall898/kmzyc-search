<%@ page language="java"  import="com.kmzyc.search.facade.config.Configuration"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML>
<html>
	<head>
	    <jsp:include page="/WEB-INF/jsp/common/wapTemplate.jsp"></jsp:include>
	    <div class="header-inner">
	        <div class="pages-hd">
	            <a class="back icon-uniE61E"></a>
	            <h2>产品搜索</h2>
	            <div class="sortlist">
	                <span class="icon-uniE63B fristSpan"></span>
	            </div>
	        </div>
    	</div>
    	<%-- <%@ include file="/html/common/wap/search-head.jsp" %> --%>
    	<jsp:include page="/html/common/wap/search-head.jsp"/>
	</head>
	<body>
        <div class="header-search nav-pages">
		    <form>
		        <input class="search-txt" type="text" placeholder="搜索你想找的商品" autocomplete="off" style="color: #666;">
		        <span class="search-icon icon-uniE036 j_wapSearch"></span>
		    </form>
		</div>
		<div class="container">
    		<section class="category-box">
    			<!-- 排序 -->
				<%@ include file="template/sortBar.jsp" %>
				<!-- 产品列表 -->
	            <%@ include file="template/productsBox.jsp" %>  
			</section>
		</div>
		 
	    <!-- 筛选 -->
	    <%@ include file="template/screening.jsp" %>  
		<!-- 底部导航链接 -->
		<%-- <%@ include file="/html/common/wap/search-foot.jsp" %> --%>
		<jsp:include page="/html/common/wap/search-foot.jsp"/>
		<div id="_mask" style="position:absolute;left:0px;top:0px;background-color:rgb(13, 13, 13);filter:alpha(opacity=60);opacity: 0.6;width:100%;height:2028px;z-index:8888;display: none;"></div>
	</body>
</html>