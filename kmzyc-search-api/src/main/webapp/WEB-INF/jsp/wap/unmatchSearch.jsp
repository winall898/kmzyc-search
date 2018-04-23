<%@ page language="java"  import="com.kmzyc.search.facade.config.Configuration"	pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
		<div class="l-w sec-nogoods">
			<div class="nogoods-cont">
				<span class="fn-left ico-warn"></span>
				<div class="fn-block">
					<c:choose>
						<c:when test="${empty keyword }">
							<div class="container">
							    <div class="ibox float-e-margins"> 
					               <div class="list-box ">
					                   <span class="text-danger">很抱歉！</span><span>没有找到符合条件的商品。</span>
					                   <dl> 
					                   <dt>建议您：</dt>
					                     <dl>1.输入商品名称或关键字进行搜索</dl>
					                     <dl>2.咨询在线客服</p></dl>
					                   </dl>
					               </div>
					            </div>
							</div>
						</c:when>
						<c:otherwise>
						
							<div class="container">
							    <div class="ibox float-e-margins"> 
					               <div class="list-box ">
					                   <span>很抱歉！没有找到与"</span><span class="text-danger">${keyword}</span>"<span>没有找到符合条件的商品。</span>
					                   <dl> 
					                   <dt>建议您：</dt>
					                     <dl>1、看看输入的文字是否有误</dl>
					                     <dl>2、调整输入的关键字</p></dl>
					                     <dl>3、通过类目进行筛选</p></dl>
					                   </dl>
					               </div>
					            </div>
							</div>
						</c:otherwise>
					</c:choose>
					
				</div>
			</div>
		</div>
										
		<!-- 底部导航链接 -->
		<%-- <%@ include file="/html/common/wap/search-foot.jsp" %> --%>
		<jsp:include page="/html/common/wap/search-foot.jsp"/>
		<div id="_mask" style="position:absolute;left:0px;top:0px;background-color:rgb(13, 13, 13);filter:alpha(opacity=60);opacity: 0.6;width:100%;height:2028px;z-index:8888;display: none;"></div>
	</body>
</html>