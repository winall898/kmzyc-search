<%@ page language="java"  import="com.kmzyc.search.facade.config.Configuration"	pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	    <jsp:include page="/WEB-INF/jsp/common/b2bTemplate.jsp"/>
	</head>
	<body>
        <jsp:include flush="true" page="/WEB-INF/jsp/common/b2bHeadTemplate.jsp"/>
		<div class="l-w sec-nogoods">
			<div class="nogoods-cont">
				<span class="fn-left ico-warn"></span>
				<div class="fn-block">
					<c:choose>
						<c:when test="${empty keyword }">
							<p>很抱歉！没有找到符合条件的商品。</p>
							<dl>
								<dt><strong>建议您：</strong></dt>
								<dd>1.输入商品名称或关键字进行搜索</dd>
								<dd>2.咨询在线客服</dd>
							</dl>
						</c:when>
						<c:otherwise>
							<p>很抱歉！没有找到与"<span class="fn-red">${keyword}</span>"相关的商品,试着重新搜索一下吧。</p>
							<dl>
								<dt><strong>建议您：</strong></dt>
								<dd>1、看看输入的文字是否有误</dd>
								<dd>2、调整输入的关键字</dd>
								<dd>3、通过类目进行筛选</dd>
							</dl>
						</c:otherwise>
					</c:choose>
					
				</div>
			</div>
		</div>
										
		<!-- 底部导航链接 -->
		<jsp:include flush="true" page="/WEB-INF/jsp/common/b2bFootTemplate.jsp"/>
	</body>
</html>