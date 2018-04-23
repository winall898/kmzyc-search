<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="menu-sidebar" style="display: none;">
    <div class="list-content-mask"></div>
    <div class="sidebar-content">
        <div class="sidebar-header">
            <div class="sidebar-header-btn">
                <a href="#" class="btn btn-primary j_popclear">清空筛选</a>
                <a href="#" class="btn btn-success j_popsubmit">确定</a>
            </div>
        </div>
        <ul class="sidebar-list">
            <li><a href="#">价格<span></span></a>
                <div>
                    <ul class="tab-con price">
                        <li class="all" data-code="">查看全部<c:if test="${facterList[0].fields[0].name==null || facterList[0].fields[0].name eq ''}"><i class="icon-uniE635" style="color:#fff;"></i></c:if></li>
                        <li data-code="0_50">0-50元<c:if test="${facterList[0].fields[0].name!=null && facterList[0].fields[0].name eq '0_50'}"><i class="icon-uniE635" style="color:#fff;"></i></c:if></li>
                        <li data-code="50_100">50-100元<c:if test="${facterList[0].fields[0].name!=null && facterList[0].fields[0].name eq '50_100'}"><i class="icon-uniE635" style="color:#fff;"></i></c:if></li>
                        <li data-code="100_200">100-200元<c:if test="${facterList[0].fields[0].name!=null && facterList[0].fields[0].name eq '100_200'}"><i class="icon-uniE635" style="color:#fff;"></i></c:if></li>
                        <li data-code="200_500">200-500元<c:if test="${facterList[0].fields[0].name!=null && facterList[0].fields[0].name eq '200_500'}"><i class="icon-uniE635" style="color:#fff;"></i></c:if></li>
                        <li data-code="500_1000">500-1000元<c:if test="${facterList[0].fields[0].name!=null && facterList[0].fields[0].name eq '500_1000'}"><i class="icon-uniE635" style="color:#fff;"></i></c:if></li>
                        <li data-code="1000_">1000元以上<c:if test="${facterList[0].fields[0].name!=null && facterList[0].fields[0].name eq '1000_'}"><i class="icon-uniE635" style="color:#fff;"></i></c:if></li>
                    </ul>
                </div>
            </li>
            <li><a href="#">类目<span></span></a>
                <div>
                    <ul class="tab-con category">
                        <li class="all" data-code="">查看全部<c:if test="${facterList[1].fields[1].name==null || facterList[1].fields[1].name eq ''}"><i class="icon-uniE635" style="color:#fff;"></i></c:if></li>
                        <c:forEach items="${cateList}" var="parent" varStatus="status">
	                        <li>${parent.name}</li>
		                        <li>
		                            <ul class="tab-con-box category2">
		                            	<c:forEach items="${parent.children}" var="child" varStatus="childStatus">
		                                	<li class="category3" data-code="${child.code}">${child.name}<c:if test="${child.selected}"><i class="icon-uniE635" style="color:#fff;"></i></c:if></li>
		                                </c:forEach>
		                            </ul>
		                        </li>
	                    </c:forEach>
                    </ul>
                </div>
            </li>
            <li><a href="#">商家<span></span></a>
                <div>
                    <ul class="tab-con supply">
                        <li class="all" data-code="">查看全部<c:if test="${facterList[2].fields[2].name==null || facterList[2].fields[2].name eq ''}"><i class="icon-uniE635" style="color:#fff;"></i></c:if></li>
                        <li data-code="13">自营<c:if test="${facterList[2].fields[2].name!=null && facterList[2].fields[2].name eq '13'}"><i class="icon-uniE635" style="color:#fff;"></i></c:if></li>
                        <li data-code="2">第三方<c:if test="${facterList[2].fields[2].name!=null && facterList[2].fields[2].name eq '2'}"><i class="icon-uniE635" style="color:#fff;"></i></c:if></li>
                    </ul>
                </div>
            </li>
        </ul>
    </div>
</div>
