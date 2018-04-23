<%@ page language="java"  import="com.kmzyc.search.facade.config.Configuration"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.kmb2b.com/functions" prefix="km" %> 
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/html/common/b2b/version.jsp"%>
<%
	String title = "康美中药城，中国首个线上药材市场\n";
	String keywords = "康美中药城，中国首个线上药材市场";
	String description = "康美中药城 -中国首个线上药材市场!集网上药店、虚拟医院为一体的服务平台。国家药监局认证！发货迅速,为您提供愉悦的线上健康服务体验!";
    
    String staticUrl = Configuration.getContextProperty("cssAndJsPath_B2B");
   	System.err.println("staticUrl: " + staticUrl);
    String jspPath = request.getRequestURI();
    boolean rootPage = jspPath.indexOf("WEB-INF/") == -1; //是否为根路径下的jsp，如error.jsp、index.jsp等
    String templatePath = null;
    if(!rootPage){
        templatePath  = jspPath.substring(jspPath.indexOf("WEB-INF/")+"WEB-INF/".length(), jspPath.lastIndexOf("."));
    }
   
    String debug = Configuration.getString("static.resource.type");//1:开发版本 0：线上版本
    String jsBaseUrl = "1".equals(debug) ? "script/" : "js/";
    String cssBaseUrl = "1".equals(debug) ? "style/default/" : "css/default/";
    String imageBaseUrl = "images/";
    
%>

<meta name="keywords" content="<%=keywords%>"/>
<meta name="description" content="<%=description%>"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title><%=title%></title>
<link rel="shortcut icon" href="<%=staticUrl%>/res/<%=imageBaseUrl%>kmzl.ico" type="image/x-icon"/>
<link rel="stylesheet" href="<%=staticUrl%>/resshop/css/default/core/header.css"/>
<link rel="stylesheet" href="<%=staticUrl%>/resshop/css/default/core/font-min.css"/>
<link rel="stylesheet" href="<%=staticUrl%>/resshop/css/default/common.css"/>
<link rel="stylesheet" href="<%=staticUrl%>/resshop/css/default/pages/search.css"/>
<link rel="stylesheet" href="<%=staticUrl%>/resshop/css/default/pages/new-template.css">

<script type="text/javascript">
    var KM = KM || {};
    KM.VERSION = '<%=version%>';//项目版本号
</script>
<script type="text/javascript" src="<%=staticUrl%>/res/<%=jsBaseUrl%>seajs/2.0.2/sea.js"></script>
<%
    if("1".equals(debug)){
%>
<script type="text/javascript" src="<%=staticUrl%>/res/<%=jsBaseUrl%>config.js"></script>
<%
    }
%>
<script type="text/javascript">
    seajs.use(['<%=staticUrl%>/res/<%=jsBaseUrl%>view/common.js']);
    <%
        if(!rootPage){
    %>
    seajs.use(['<%=staticUrl%>/res/<%=jsBaseUrl%>view/<%=templatePath%>.js']);
    <%
        }
    %>
    seajs.use('<%=staticUrl%>/res/<%=jsBaseUrl%>view/supply.js');
</script>
</head>

<body>
<div class="food-body">
<%@ include file="/html/common/b2b/searchSupply.jsp" %>
<div id="wrapper">
 <div class="skin-box">
   <jsp:include page="/html/common/b2b/${shopid }/supplyTitle.jsp"></jsp:include>
            <div class="skin-box-bd">
                <div class="w mt10">
                    <!--动态数据-->
                    <div class="grid-m0">
                        <div class="col-sub left">
                            <div class="column-box">
                                <div class="co-hd">所有分类</div>
                                <div class="co-bd">
                                    <ul id="searchSubnav" class="subnav">
                                    </ul>
                                </div>
                            </div>
                            <div class="column-box">
                                <div class="co-hd">热销推荐</div>
                                <div class="co-bd">
                                    <ul id="searchShopRanking" class="shop-ranking">
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="col-main right">
                            <div class="col-main-item">
                                <div class="co-hd">
                                    <div class="search-cont">
                                        <label>所有产品 &gt;</label>
                                        <input type="text" placeholder="关键字" class="input-search" id="queryText" value="${keyword }" >
                                        <input type="button" class="btn-confirm" id="shopSearchBtn" value="搜索">
                                    </div>
                                </div>
                                <div class="co-bd">
                                    <div class="search-cont-lst">
                                    	<c:if test="${!empty selectedFilter}">
                                    	<dl>
                                            <dt>已选条件：</dt>
                                            <dd>
                                                <c:forEach items="${selectedFilter }" var="filter" varStatus="status">
                                                	<c:forEach items="${filter.value }" var="entry" varStatus="status">
	                                                <a href="${km:fqDelVal(km:qsRemove(km:qsRemove(searchURL, 'sort'), 'pn'), filter.key, entry.value)}" class="wrong-text"> ${entry.key }：
	                                                    <strong>${km:prFmat(entry.value) }</strong><b>x</b>
	                                                </a>
	                                                <a class="recall"></a>
	                                                </c:forEach>
												</c:forEach>
                                            </dd>
                                        </dl>
                                    	</c:if>
                                    	<c:if test="${productList != null && fn:length(productList) > 0 }">
											<c:forEach items="${facterList }" var="facter" varStatus="status">
												<c:choose>
												<c:when test="${facter.name == '价格'}">
												<dl>
		                                            <dt>${facter.name }:</dt>
		                                            <dd>
		                                            <c:forEach items="${facter.fields}" var="field" varStatus="status">
		                                            <a href="${km:fqAdd(km:fqDel(km:qsRemove(km:qsRemove(searchURL, 'sort'), 'pn'), field.code), field.code, field.name)}">${field.name}</a>
		                                            </c:forEach>
		                                             <input type="text" value="" title="最低价" class="price-range" id="min_price">
		                                                <span>-</span>
		                                             <input type="text" value="" title="最低价" class="price-range" id="max_price">
		                                             <input type="button" class="btn-confirm" id="priceBtn" value="确定">
		                                            </dd>
		                                        </dl>
												</c:when>
												<c:otherwise>
		                                        <dl>
		                                            <dt>${facter.name }:</dt>
		                                            <dd>
		                                            <c:forEach items="${facter.fields}" var="field" varStatus="status">
		                                            <a href="${km:fqAdd(km:qsRemove(km:qsRemove(searchURL, 'sort'), 'pn'), field.code, field.name)}">${field.name}</a>
		                                            </c:forEach>
		                                            </dd>
		                                        </dl>
												</c:otherwise>
	                                        	</c:choose>
	                                        </c:forEach>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                            <div class="col-main-item">
                                <div class="co-hd">
                                	<c:if test="${not empty keyword}">
                                		<span class="left">"<span class="f-red">${keyword }</span>" 找到 ${count } 件相关商品</span>
                                	</c:if>
                                    <ul class="ui-order right">
                                        <c:choose>
                                        	<c:when test="${sort == 5 }">
                                        		<li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }" class="curr">销量<i class="up"></i></a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a></li>
											</c:when>
                                        	<c:when test="${sort == 6 }">
                                        		<li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '5') }" class="curr">销量<i class="down"></i></a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a></li>
											</c:when>
											<c:when test="${sort == 1 }">
												<li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</i></a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '2') }" class="curr">价格<i class="up"></i></a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a></li>
											</c:when>
                                        	<c:when test="${sort == 2 }">
                                        		<li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }" class="curr">价格<i class="down"></i></a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a></li>
											</c:when>
											<c:when test="${sort == 3 }">
												<li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }" class="curr">时间<i class="up"></i></a></li>
											</c:when>
                                        	<c:when test="${sort == 4 }">
                                        		<li><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '3') }">价格</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '3') }" class="curr">时间<i class="down"></i></a></li>
											</c:when>
											<c:otherwise>
												<li class="white-bg"><a href="${km:qsRemove(km:qsRemove(searchURL, 'pn'), 'sort') }">综合</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '6') }">销量</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '1') }">价格</a></li>
												<li><a href="${km:qsReplace(km:qsRemove(searchURL, 'pn'), 'sort', '4') }">时间</a></li>
											</c:otherwise>
                                        </c:choose>
                                    </ul>
                                </div>
                                <div class="co-bd">
                                    <ul class="search-lst">
                                    	<c:forEach items="${productList }" var="product" varStatus="status">
                                        <li>
                                            <div class="p-img">
                                            <a title="${product.subTitle}" href="<%=Configuration.getContextProperty("detailPath_B2B")%>${product.skuId}.shtml" target="_blank">
                                            	<img alt="${product.subTitle}" width="170" height="170" src="<%=Configuration.getContextProperty("picPath_B2B")%>${product.image}" onerror="this.src='<%=Configuration.getContextProperty("CSS_JS_PATH_B2B")%>images/default__logo_err170_170.jpg'">
                                            </a>
                                            </div>
                                            <div class="p-name">
                                            <a href="<%=Configuration.getContextProperty("detailPath_B2B")%>${product.skuId}.shtml" target="_blank">${product.title} ${product.attrVals }</a>
                                            </div>
                                            <div class="p-price">
                                                <strong>￥<fmt:formatNumber type="number" value="${product.price}" pattern="0.00" /></strong>
                                            </div>
                                            <div class="extra">
                                                <span class="sales">总销量:<strong>${product.sales}</strong></span>
                                                |
                                                <span class="evaluation"> 评价:${product.pnum}</span>
                                            </div>
                                        </li>
                                        </c:forEach>
                                    </ul>
                                    <div class="clear p10">
                                        <div class="ui-page">
                                        	<!-- 上一页 -->
                                        	<c:choose>
                                        		<c:when test="${pn eq 1 }">
                                        			<a href="javascript:void(0)" class="ui-page-item ui-page-disabled ui-page-item-prev"></a>
                                        		</c:when>
                                        		<c:when test="${pc eq 1 }">
                                        			<a href="javascript:void(0)" class="ui-page-item ui-page-disabled ui-page-item-prev"></a>
                                        		</c:when>
                                        		<c:otherwise>
                                        			<a href="${km:qsReplace(searchURL, 'pn', (pn-1)) }" class="ui-page-item ui-page-item-prev"></a>
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
		                                            			<span class="ui-page-text">...</span>
		                                            		</c:otherwise>
                                            			</c:choose>
                                           				<a href="${km:qsReplace(searchURL, 'pn', pc) }" class="ui-page-item">${pc }</a>
                                            		</c:when>
                                        			<c:otherwise>
                                        				<a href="${km:qsReplace(searchURL, 'pn', 1) }" class="ui-page-item">1</a>
                                           				<a href="${km:qsReplace(searchURL, 'pn', 2) }" class="ui-page-item">2</a>
                                           				<span class="ui-page-text">...</span>
                                           				<a href="${km:qsReplace(searchURL, 'pn', pn-2) }" class="ui-page-item">${pn-2 }</a>
                                           				<a href="${km:qsReplace(searchURL, 'pn', pn-1) }" class="ui-page-item">${pn-1 }</a>
                                           				<a href="javascript:void(0)" class="ui-page-item ui-page-item-current">${pn }</a>
                                           				
                                           				<c:choose>
                                     					<c:when test="${(pn+3) <= pc }">
                                     						<a href="${km:qsReplace(searchURL, 'pn', pn+1) }" class="ui-page-item">${pn+1 }</a>
                                     						<a href="${km:qsReplace(searchURL, 'pn', pn+2) }" class="ui-page-item">${pn+2 }</a>
                                     						<span class="ui-page-text">...</span>
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
                                       			<a href="javascript:void(0)" class="ui-page-item ui-page-disabled ui-page-item-next"></a>
                                       		</c:when>
                                       		<c:when test="${pc eq 1 }">
                                       			<a href="javascript:void(0)" class="ui-page-item ui-page-disabled ui-page-item-next"></a>
                                       		</c:when>
                                       		<c:otherwise>
                                       			<a href="${km:qsReplace(searchURL, 'pn', (pn+1)) }" class="ui-page-item ui-page-item-next"></a>
                                       		</c:otherwise>
                                       		</c:choose>
                                        	<!-- 跳转 -->
                                            <span class="ui-page-item-info ui-page-text">跳转到</span>
                                            <span><input id="inputPageNo" name="pageNo" type="text" value="${pn }" title="" class="ui-page-number"></span>
                                            <span><input id="pageBtn" type="button" class="ui-page-item" value="GO"></span>
                                            <input type="hidden" id="pageCount" value="${pc }" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
     </div>
    <!--主体内容 end-->

    <!--底部 start-->
    <div id="footer">
    <!-- 底部导航链接 -->
		<%@ include file="/WEB-INF/jsp/common/b2bFootTemplate.jsp" %>  	
    </div>
    <!--底部 end-->
</div>
</body>
</html>
