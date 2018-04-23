<%@ page language="java"  import="com.kmzyc.search.facade.config.Configuration" pageEncoding="UTF-8"%>
<%@ include file="/html/common/wap/version.jsp"%>
<%
	String title = "康美中药城，一站式健康服务平台!网上药店、健康管理、虚拟医院一应俱全!\n";
	String keywords = "康美中药城,康美中药城网上药店，网上药店，网上购药，药品网,网络求医,健康管理，健康体检,专家答疑,远程会诊";
	String description = "康美中药城 -用心经营健康!集网上药店、虚拟医院为一体的服务平台，提供专业的网络求医，网上购药、专家答疑、健康评估等服务。国家药监局认证！发货迅速,为您提供愉悦的线上健康服务体验!";
    
    String staticUrl = Configuration.getContextProperty("CSS_JS_PATH_WAP");
   	
    String jspPath = request.getRequestURI();
    boolean rootPage = jspPath.indexOf("WEB-INF/") == -1; //是否为根路径下的jsp，如error.jsp、index.jsp等
    String templatePath = null;
    if(!rootPage){
        templatePath  = jspPath.substring(jspPath.indexOf("WEB-INF/")+"WEB-INF/".length(), jspPath.lastIndexOf("."));
    }
   
    String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	request.setAttribute("basePath",basePath);
    
    String debug = Configuration.getString("static.resource.type");//1:开发版本 0：线上版本
    String jsBaseUrl = "1".equals(debug) ? "script/" : "js/";
    String cssBaseUrl = "1".equals(debug) ? "style/default/" : "css/default/";
    String imageBaseUrl = "images/";
    
%>
<meta charset="utf-8">
<meta name="keywords" content="<%=keywords%>"/>
<meta name="description" content="<%=description%>"/>
<meta name="apple-itunes-app" content="app-id=432274380">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit" />
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
<meta http-equiv="Expires" CONTENT="-1">           
<meta http-equiv="Cache-Control" CONTENT="no-cache">           
<meta http-equiv="Pragma" CONTENT="no-cache">
    
<title><%=title%></title>
<link rel="shortcut icon" href="<%=staticUrl%><%=imageBaseUrl%>kmzl.ico" type="image/x-icon"/>
<link type="text/css" rel="stylesheet" href="<%=staticUrl%><%=cssBaseUrl%>common.css?version=<%=version%>"/><!--缓存头部共用样式 -->
<link type="text/css" rel="stylesheet" href="<%=basePath%>fonts/fonts.css?version=<%=version%>"/><!--缓存头部共用样式 -->
<%
    //if(!rootPage){
%>
<!--<link type="text/css" rel="stylesheet" href="<%=staticUrl%><%=cssBaseUrl%><%=templatePath%>.css?version=<%=version%>"/>-->
<%
    //}
%>
<!--[if lte IE 9]><style type="text/css">.css3pie{behavior: url(<%=staticUrl%>/style/css3pie/PIE.htc);}</style><![endif]-->
<script type="text/javascript">
    var KM = KM || {};
    KM.VERSION = '<%=version%>';//项目版本号
</script>

<script type="text/javascript" src="<%=staticUrl%><%=jsBaseUrl%>seajs/2.0.2/sea.js"></script>
<script type="text/javascript" src="<%=staticUrl%><%=jsBaseUrl%>config.js?version=<%=version%>"></script>
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
