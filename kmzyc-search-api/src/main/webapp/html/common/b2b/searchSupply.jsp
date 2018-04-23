<%@page import="com.kmzyc.search.facade.config.Configuration"  pageEncoding="UTF-8"%>
<div class="i-topbar">
    <div class="l-w fn-clear">
        <p class="fn-left loginbar" id="loginbar">
            您好，欢迎来到康美中药城商城！
        </p>
        <ul class="fn-right topmenu">
		
		<div class="fn-right minicart">
                <a class="minicart-go fn-right" href="javascript:void(0);" id="gotoSettlement"></a>
                <div id="shopcart" class="minicart-number fn-left">
                    <a href="<!--#echo var="portalPath" -->="" cart="" listshopcar.action"="">
                    采购单(<span class="number">0</span>)件
                    </a>
                    <div class="minicart-list fn-hide" id="nogoods">
                        <div class="nogoods">
                            <b></b>
                            <p class="fn-block">采购单中还没有商品，赶紧选购吧！<br>您还没有 <a href="javascript:void(0);">登录</a></p>
                        </div>
                    </div>
                </div>
            </div>
		
            <li class="topmenu-item topmenu-item-phone">
                <i class="ico-phone">
                </i>
                客服热线：
                <strong>
                    400-6600-518
                </strong>
            </li>
            <li class="topmenu-item">
				<b>
				</b>
			<a href="<!--#echo var="portalPath" -->/member/toMailSubscription.action">
				邮件订阅
			</a>
			</li>
            <li class="topmenu-item">
                <b>
                </b>
                <a href="javascript:void(0);" class="j_settle">商家入驻</a>
            </li>
            <li class="topmenu-item topmenu-item-collect">
                <b>
                </b>
                <i class="ico-collect">
                </i>
                <a href="javascript:void(0);" id="addCookie" title="康美之恋">
                    收藏本站
                </a>
            </li>
            <li class="topmenu-item">
                <b>
                </b>
                <a href="<!--#echo var="staticPath" -->/helps/index.shtml">
                    帮助中心
                </a>
            </li>
        </ul>
    </div>
</div>
<div class="i-head l-w">
        <div class="i-head-logo fn-clear">
			<!--首页标志窗口-->
			<div class="logo fn-left">   
<a href="http://www.kmb2b.com/index.html">
         <img src="<!--#echo var="cmsPath" -->/1411615307400.png" width="200" height="85" alt="康美之恋">    </a>
		<a href="http://www.kmb2b.com/secKillV2.html" target="_blank"><img src="<!--#echo var="cmsPath" -->/1415777808013.swf" alt="falsh活动小图标"></a>
</div>
            <div class="i-search fn-left">
                <div class="search-cont">
					<form id="searchForm" method="POST" onSubmit="if(!this.action) return false;">
                           <div class="search-triggers">
                <ul class="switchable-nav">
                    <li data-value="1" class="curr"><a>商品</a></li>
                    <li data-value="2"><a>店铺</a></li>
                </ul>
                <s class="icon-btn-arrow-h"></s>
            </div>
						<div class="form">
							<input id="keyword" value="请输入搜索关键字" type="text" class="text" state="begin" maxlength="60" />
							<input id="searchBtn" type="button" value="搜索" class="button" />
						</div>
					</form>
                </div>
				<!--热门搜索窗口-->
                <div class="search-hotwords">
                    <strong>热门搜索：</strong>
                                                  <a target="_blank" href="http://search.kmb2b.com/10/search?kw=%E6%96%B0%E9%B2%9C%E4%BA%BA%E5%8F%82">新鲜人参</a>
<a target="_blank" href="http://search.kmb2b.com/10/search?kw=%E8%A1%A5%E8%82%BE">补肾</a>
<a target="_blank" href="http://search.kmb2b.com/10/search.action?kw=%E8%A5%BF%E6%B4%8B%E5%8F%82">西洋参</a>
<a target="_blank" href="http://search.kmb2b.com/10/search?kw=%E7%87%95%E7%AA%9D">燕窝</a>
<a target="_blank" href="http://search.kmb2b.com/10/search.action?kw=%E8%8F%8A%E7%9A%87%E8%8C%B6">菊皇茶</a>
<a target="_blank" href="http://search.kmb2b.com/10/search.action?kw=%E7%BA%A2%E5%8F%82">新开河红参</a>
<a target="_blank" href="http://search.kmb2b.com/10/search.action?kw=%E7%8E%9B%E5%92%96">玛咖</a>
<a target="_blank" href="http://search.kmb2b.com/10/search.action?kw=%E6%9E%B8%E6%9D%9E">枸杞</a>
</div>
            </div>
            <ul class="security fn-right">
                <li class="s-icon2" style="width: 115px;"><i></i>康美药业<br><span class="fn-f12">旗下电商平台</span></li>
                <li class="s-icon3"><i></i>药师指导 放心购买</li>
                <li class="s-icon1"><i></i>PBS顺丰 服务保障</li>
            </ul>		
        </div>
    </div>
  
<input type="hidden" id="cssAndJsPath" data_id="http://jscss.kmb2b.com/">
<input type="hidden" id="cmsPath" data_id="http://img.kmb2b.com/cms">
<input type="hidden" id="picPath" data_id="http://img.kmb2b.com/product">
<input type="hidden" id="detailPath" data_id="http://www.kmb2b.com/products/">
<input type="hidden" id="advPath" data_id="http://www.kmb2b.com/adv">
<input type="hidden" id="portalPath" data_id="http://www.kmb2b.com">
<input type="hidden" id="staticPath" data_id="http://www.kmb2b.com">
<input type="hidden" id="facade_path" data_id="http://search.kmb2b.com/10">
<input type="hidden" id="gysPortalPath" data_id="http://gys.kmb2b.com">
<input type="hidden" id="logSysPath" data_id="http://kma.kmb2b.com/weblog/b2b">
