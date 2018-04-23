<%@ page language="java"  import="com.kmzyc.search.facade.config.Configuration"	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	
	<head>
<%
	String title = "康美中药城，中国首个线上药材市场，康美药业股份有限公司旗下\n";
	String keywords = "康美中药城，中国首个线上药材市场，康美药业股份有限公司旗下";
	String description = "康美中药城，康美中药城健康第一商城,中国保健养生健康方面最专业的电子商务平台,康美药业股份有限公司旗下";
    String staticUrl = Configuration.getContextProperty("CSS_JS_PATH_B2B");
   	
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
    String version = "201401201536";
    
    String kmb2bHost = Configuration.getString("searchPath_B2B");
%>
<meta name="keywords" content="<%=keywords%>"/>
<meta name="description" content="<%=description%>"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" /><!-- 防止IE8,7进入怪异模式 -->
<title><%=title%></title>
<link rel="shortcut icon" href="<%=staticUrl%><%=imageBaseUrl%>logos/favicon.png" type="image/x-icon"/>
<link type="text/css" rel="stylesheet" href="<%=staticUrl%><%=cssBaseUrl%>common.css?version=<%=version%>"/><!--缓存头部共用样式 -->
<%
    if(!rootPage){
%>
<link type="text/css" rel="stylesheet" href="<%=staticUrl%><%=cssBaseUrl%><%=templatePath%>.css?version=<%=version%>"/>
<%
    }
%>
<link type="text/css" rel="stylesheet" href="<%=staticUrl%><%=cssBaseUrl%>pages/search.css?version=<%=version%>"/>
<script type="text/javascript">
    var KM = KM || {};
    KM.VERSION = '<%=version%>';//项目版本号
</script>
<script type="text/javascript" src="<%=staticUrl%><%=jsBaseUrl%>seajs/2.0.2/sea.js?version=<%=version%>"></script>
<%
    if("1".equals(debug)){
%>
<script type="text/javascript" src="<%=staticUrl%><%=jsBaseUrl%>config.js?version=<%=version%>"></script>
<%
    }
%>
<script type="text/javascript">
    seajs.use(['<%=staticUrl%><%=jsBaseUrl%>view/common.js']);
    <%
        if(!rootPage){
    %>
    seajs.use(['<%=staticUrl%><%=jsBaseUrl%>view/<%=templatePath%>.js']);
    <%
        }
    %>
</script>
<input type="hidden" id="facade_path" data_id="<%=kmb2bHost%>"/>
</head>
	<body>
		<div class="global-search">
				<div class="kmzl-logo">
					<a href="http://www.kmb2b.com/" hidefocus="true"><img  src="<%=staticUrl%><%=imageBaseUrl%>common/logo.png" alt="康美中药城" /></a>
				</div>
				<div class="i-search-form">
					<div class="search-box">
						<ul class="km-switchable-nav">
							<li id="kmb2b" class="selected"><a hidefocus="true" href="javascript:;">康美中药城</a></li>
						</ul>
						<div class="search-cont">
							<div class="form">
								<input id="keyword" type="text" class="text" autofocus x-webkit-speech />
								<input id="search_btn" type="button" value="搜索" class="button"  />
							</div>
						</div>
					</div>
				</div>
				<div class="search-links">
					<a href="http://www.kmb2b.com/helps/help-about.shtml" target="_blank" rel="nofollow">关于康美</a>| <a href="http://www.kmb2b.com/helps/help-contact.shtml" target="_blank" rel="nofollow">联系我们</a>| <a href="http://www.kmb2b.com/helps/help-qualification.shtml" target="_blank" rel="nofollow">专业资质</a>| <a href="http://www.kmb2b.com/helps/help-promises.shtml" target="_blank" rel="nofollow">服务承诺</a>| <a href="/helps/index.shtml" target="_blank" rel="nofollow">帮助中心</a>| <a href="/member/showOrderTrail.action" target="_blank" rel="nofollow">订单跟踪</a>
				</div>
				<div class="search-ft">©2013-2017 康美中药材数据有限公司 版权所有
					<br />
					 经营性备案/许可证号：粤ICP备10204402号-2
				</div>
		</div>
	</body>
	<script type="text/javascript">
	seajs.use(['<%=staticUrl%><%=jsBaseUrl%>base/jquery/1.8.3/jquery.js'], function($){
		$(function(){
			// 切换TAB
			$('#kmb2b').click(function(){
				$(this).attr("class", "selected");
				$('div.kmzl-logo').show();
				$('#facade_path').attr('data_id','<%=kmb2bHost %>');
			});
			// 点击搜索
			$('#search_btn').click(function(){
				var host = '<%=kmb2bHost %>';
				var kw = $('#keyword').val();
				window.location.href = host + "/search?kw=" + kw;
			});
		});
	});
	</script>
</html>
