<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.kmb2b.com/functions" prefix="km" %> 

<div class="fn-tr fn-t10">
	<div class="ui-page fn-right">
    	<!-- 上一页 -->
    	<c:choose>
    		<c:when test="${pn eq 1 }">
    			<a href="javascript:void(0)" class="ui-page-item ui-page-disabled ui-page-item-prev">上一页</a>
    		</c:when>
    		<c:when test="${pc eq 1 }">
    			<a href="javascript:void(0)" class="ui-page-item ui-page-disabled ui-page-item-prev">上一页</a>
    		</c:when>
    		<c:otherwise>
    			<a href="${km:qsReplace(searchURL, 'pn', (pn-1)) }" class="ui-page-item ui-page-item-prev">上一页</a>
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
          			<span class="ui-page-item ui-page-text">...</span>
          		</c:otherwise>
        			</c:choose>
       				<a href="${km:qsReplace(searchURL, 'pn', pc) }" class="ui-page-item">${pc }</a>
        		</c:when>
    			<c:otherwise>
    				<a href="${km:qsReplace(searchURL, 'pn', 1) }" class="ui-page-item">1</a>
       				<a href="${km:qsReplace(searchURL, 'pn', 2) }" class="ui-page-item">2</a>
       				<span class="ui-page-item ui-page-text">...</span>
       				<a href="${km:qsReplace(searchURL, 'pn', pn-2) }" class="ui-page-item">${pn-2 }</a>
       				<a href="${km:qsReplace(searchURL, 'pn', pn-1) }" class="ui-page-item">${pn-1 }</a>
       				<a href="javascript:void(0)" class="ui-page-item ui-page-item-current">${pn }</a>
       				
       				<c:choose>
 					<c:when test="${(pn+3) <= pc }">
 						<a href="${km:qsReplace(searchURL, 'pn', pn+1) }" class="ui-page-item">${pn+1 }</a>
 						<a href="${km:qsReplace(searchURL, 'pn', pn+2) }" class="ui-page-item">${pn+2 }</a>
 						<span class="ui-page-item ui-page-text">...</span>
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
   			<a href="javascript:void(0)" class="ui-page-item ui-page-disabled ui-page-item-next">下一页</a>
   		</c:when>
   		<c:when test="${pc eq 1 }">
   			<a href="javascript:void(0)" class="ui-page-item ui-page-disabled ui-page-item-next">下一页</a>
   		</c:when>
   		<c:otherwise>
   			<a href="${km:qsReplace(searchURL, 'pn', (pn+1)) }" class="ui-page-item ui-page-item-next">下一页</a>
   		</c:otherwise>
   		</c:choose>
    	<!-- 跳转 -->
        <span><input id="inputPageNo" name="pageNo" type="text" value="${pn }" title="" class="ui-page-number"></span>
        <span><input id="pageBtn" type="button" class="ui-page-item" value="GO"></span>
        <input type="hidden" id="pageCount" value="${pc }" />
    </div>
</div>