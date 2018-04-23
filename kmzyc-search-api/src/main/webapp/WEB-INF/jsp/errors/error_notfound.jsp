<%@ page language="java" import="com.kmzyc.search.facade.config.Configuration" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html>
<html>
<head>
<%
String channel = (String)request.getAttribute(Configuration.getString("request.site.param"));
if (null == channel) {
	channel = "b2b";
}
%>
<title>404</title>
<%
    if("b2b".equals(channel)){
%>
<jsp:include page="/WEB-INF/jsp/common/b2bTemplate.jsp"></jsp:include>
<%
    }else if ("wap".equals(channel)) {
%>
<jsp:include page="/WEB-INF/jsp/common/wapTemplate.jsp"></jsp:include>
<%
    }
%>
</head>
<body>
<%
    if("b2b".equals(channel)){
%>
<jsp:include page="/WEB-INF/jsp/common/b2bHeadTemplate.jsp"></jsp:include>
<%
    }else if ("wap".equals(channel)) {
%>
<jsp:include page="/html/common/wap/search-head.jsp"></jsp:include>
<%
    }
%>
<div class="l-w">
    <div class="system system-404">
        <h2>404</h2>
        <dl>
            <dt>非常抱歉！无法替您找到页面</dt>
            <dd>哎呀！你想访问的网页不存在。</dd>
            <dd>可能网页地址输入错了，请再核对下吧。</dd>
        </dl>
    </div>
</div>
<!--底部-->
<%
    if("b2b".equals(channel)){
%>
<jsp:include page="/WEB-INF/jsp/common/b2bFootTemplate.jsp"></jsp:include>
<%
    }else if ("wap".equals(channel)) {
%>
<jsp:include page="/html/common/wap/search-foot.jsp"></jsp:include>
<%
    }
%>
<%
	String referer = request.getHeader("Referer");
	String staticUrl = "";
	if ("b2b".equals(channel)) {
		staticUrl = Configuration.getContextProperty("CSS_JS_PATH_B2B");
	}else if ("wap".equals(channel)) {
		staticUrl = Configuration.getContextProperty("cssAndJsPath_B2B");
	}
	String debug = Configuration.getString("static.resource.type");//1:开发版本 0：线上版本
	String jsBaseUrl = "1".equals(debug) ? "script/" : "js/";
%>
<script>
    seajs.use(['<%=staticUrl%><%=jsBaseUrl%>base/jquery/1.8.3/jquery.js',
        '<%=staticUrl%><%=jsBaseUrl%>common/common/config.js'],function($, Config){
        var timer = 1;
        var redirectSeconds = 5;//跳转剩余秒数
        var interval = 1000;//计时器间隔，单位毫秒
        $(document).ready(function(){
            $("#timer").text(redirectSeconds)
            timer = setTimeout(countDown, interval);

            $("#back_id").on("click", function(){
                goBack();
            });
        });

        function countDown(){
            if(redirectSeconds == 0){ //达到0秒时跳转页面
                goBack();
                return;
            }
            redirectSeconds--;
            $("#timer").text(redirectSeconds);
            timer = setTimeout(countDown, interval)
        }

        function goBack(){
            var hasReferer = <%=!(referer == null || "".equals(referer))%>;
            if(hasReferer){
                window.history.back();
            }else{
                window.location.href = Config.CMS_PATH + "index.html";
            }

        }
    })
</script>
</body>
</html>
