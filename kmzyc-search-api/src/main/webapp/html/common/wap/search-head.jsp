<%@page contentType="text/html;charset=UTF-8"%>

<%@page import="com.kmzyc.search.facade.config.Configuration" %>
 <!--头部-->
    <div class="navigation" style="display: none;">
            <ul>
                <li>
                    <a href="<%=Configuration.getContextProperty("staticPath_WAP")%>/index.html" class="on">
                        <span class="menu-icon icon-uniE63E"></span>
                        <span class="menu-txt">首页</span>
                    </a>
                </li>
                <li>
                    <a href="<%=Configuration.getContextProperty("staticPath_WAP")%>/wap/new_category.html">
                        <span class="menu-icon icon-uniE63A"></span>
                        <span class="menu-txt">分类</span>
                    </a>
                </li>
                <li>
                    <a href="<%=Configuration.getContextProperty("staticPath_WAP")%>/cart/listWapShopCar.action">
                        <span class="menu-icon icon-uniE62D"></span>
                        <span class="menu-txt">采购单</span>
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0);" class="j_goToMemerCenter">
                        <span class="menu-icon icon-uniE611"></span>
                        <span class="menu-txt">我的</span>
                    </a>
                </li>
            </ul>
        </div>


<input type="hidden" id="cssAndJsPath" data_id="<%=Configuration.getContextProperty("cssAndJsPath_WAP")%>"/>

<input type="hidden" id="cmsPath" data_id="<%=Configuration.getContextProperty("cmsPath_WAP")%>"/>

<input type="hidden" id="picPath" data_id="<%=Configuration.getContextProperty("picPath_WAP")%>"/>

<input type="hidden" id="detailPath" data_id="<%=Configuration.getContextProperty("detailPath_WAP")%>"/>

<input type="hidden" id="advPath" data_id="<%=Configuration.getContextProperty("advPath_WAP")%>"/>

<input type="hidden" id="portalPath" data_id="<%=Configuration.getContextProperty("portalPath_WAP")%>"/> 

<input type="hidden" id="staticPath" data_id="<%=Configuration.getContextProperty("staticPath_WAP")%>"/>

<input type="hidden" id="facade_path" data_id="<%=Configuration.getContextProperty("searchPath_WAP")%>"/> 

<input type="hidden" id="searchPath" data_id="<%=Configuration.getContextProperty("facadePath_WAP")%>"/>