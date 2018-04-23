<%@page contentType="text/html;charset=UTF-8"%>
<%@page import="com.kmzyc.search.facade.config.Configuration"  pageEncoding="UTF-8"%>
<script>
	try {
		document.domain = '.kmb2b.com';
	} catch(e) { }
</script>

<input type="hidden" id="cssAndJsPath" data_id="<%=Configuration.getContextProperty("cssAndJsPath_B2B")%>"/>
<input type="hidden" id="cmsPath" data_id="<%=Configuration.getContextProperty("cmsPath_B2B")%>"/>
<input type="hidden" id="picPath" data_id="<%=Configuration.getContextProperty("picPath_B2B")%>"/>
<input type="hidden" id="detailPath" data_id="<%=Configuration.getContextProperty("detailPath_B2B")%>"/>
<input type="hidden" id="advPath" data_id="<%=Configuration.getContextProperty("advPath_B2B")%>"/>
<input type="hidden" id="portalPath" data_id="<%=Configuration.getContextProperty("portalPath_B2B")%>"/>
<input type="hidden" id="facade_path" data_id="<%=Configuration.getContextProperty("searchPath_B2B")%>"/>
<input type="hidden" id="gysPortalPath" data_id="http://gys.kmb2b.com">
  
<script>		
    seajs.use(['<%=Configuration.getContextProperty("cssAndJsPath_B2B")%>/res/js/view/index2015.js',
    '<%=Configuration.getContextProperty("cssAndJsPath_B2B")%>/res/js/base/base/parabola.js',
    '<%=Configuration.getContextProperty("cssAndJsPath_B2B")%>/res/js/view/navigation_shopcart.js']);
</script> 

<div class="i-topbar">
	<div class="l-w fn-clear">
      <p class="fn-left loginbar" id="loginbar">
        欢迎来到康美中药城&emsp;&emsp;
        
        <a href="javascript:void(0);" id="displayLogin2">请登录</a>&emsp;&emsp;
        <a href="http://www.kmb2b.com/html/regist.htm">免费注册</a>
      </p>

		<ul class="fn-right topmenu ">
          <li class="topmenu-item topmenu-item-app"><a href="<%=Configuration.getContextProperty("portalPath_B2B")%>/">官方首页</a></li>
          <li class="topmenu-item topmenu-item-app"><a href="http://www.kmzyw.com.cn" target="_blank">中药网</a></li>
          <li class="topmenu-item topmenu-item-app"><a href="<%=Configuration.getContextProperty("portalPath_B2B")%>/member/goHome.action">会员中心</a></li>
          <li class="topmenu-item topmenu-item-app"><a href="<%=Configuration.getContextProperty("portalPath_B2B")%>/cart/listShopCar.action">采购单</a></li>
          <li class="topmenu-item topmenu-item-app"><a href="<%=Configuration.getContextProperty("portalPath_B2B")%>/member/queryFavoriteList.action?_r=1493023202878">我的收藏</a></li>
          <li class="topmenu-item topmenu-item-app"><a href="javascript:void(0);" class="j_settle">我是卖家</a></li>
          <li class="topmenu-item topmenu-item-app"><a href="http://chat8.live800.com/live800/chatClient/chatbox.jsp?companyID=801308&configID=151087&jid=8884359534">客服中心</a></li>
          <li class="topmenu-item topmenu-item-app"><a href="http://www.kmb2b.com/">网站导航</a></li>
		</ul>
	</div>
</div>

<div class="i-head-logo-all">

	<div class="i-head-logo fn-clear">
		<!--首页标志窗口-->
      <div class="logo fn-left j_adminDiv" data-ids="160363" data-type="1">
		<a href="http://www.kmb2b.com" target="_blank">
			<img src="<%=Configuration.getContextProperty("cmsPath_B2B")%>/2017/03/17/1705057.png" alt="康美中药城" title="返回首页">
		</a>
    
</div>
		<!--搜索栏-->
		<div class="i-search fn-right">
			<form onsubmit="if(!this.action) return false;" method="POST" id="searchForm">

				<div class="search-cont">
					<div class="form">
						<span class="button-left"><i style="margin-left: 14px;">产品</i><em></em></span>
						<input name="search" id="keyword" type="text" class="text" state="begin" onclick="if(value==defaultValue){value='';this.style.color='#333'}" onblur="if(!value){value=defaultValue;this.style.color='#ccc'}" />
						<input type="button" id="searchBtn" value="搜  索" class="button" />
					</div>
				</div>
			</form>
			<!--热门搜索窗口-->

		</div>

	</div>
</div>
  
  
<div class="i-nav">
		<div class="nav-cont fn-clear">
			<div id="sortGoodsList" class="sort">
				<h2 class="sort-link">
              <a href="http://www.kmb2b.com/">
                 货源采购类目
                 <b></b>
              </a>
				</h2>
				<ul id="otherGoodsList" class="sort-list sider-list" style="display: none;">
					<!--商品分类1楼-->
						<li class="sort-list-item j_adminDiv" id="id1" data-ids="11041" data-type="1">
		<div class="sort-list-title">
			<i class="ico-sort419 ico-co1" data-categoryId="16609"></i>
			<h3>根茎类</h3>
			<p>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E5%85%9A%E5%8F%82" target="_blank">党参</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E4%B8%89%E4%B8%83" target="_blank">三七</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E5%A4%AA%E5%AD%90%E5%8F%82" target="_blank">太子参</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E4%B8%B9%E5%8F%82" target="_blank">丹参</a></em>
			</p>
		</div>

		<div class="sortsub fn-clear" id="div1" style="display: none">
			<div class="sortsub-l">
				<dl class="sortsub-item">
					<dd>
						<ul class="sortsub-letter">
                      		<li>A</li>
                      		<li>B</li>
                      		<li>C</li>
                      		<li>D</li>
                      		<li>E</li>
                      		<li>F</li>                                  		
                      	</ul>
							<em class="1_1">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16612.html" target="_blank"> 北豆根</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>G</li>
                       		<li>H</li>
                       		<li>I</li>
                       		<li>J</li>
                       		<li>K</li>
                       		<li>L</li>                                  		
                      	</ul>
							<em class="1_2">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16616.html" target="_blank"> 葛根</a>
							</em>
							<em class="1_3">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16620.html" target="_blank"> 金果榄</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>M</li>
                       		<li>N</li>
                       		<li>O</li>
                       		<li>P</li>
                       		<li>Q</li>
                       		<li>R</li> 
                       		<li>S</li>                                 		
                      	</ul>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                       		<li>T</li>
                       		<li>U</li>
                       		<li>V</li>
                       		<li>W</li>
                       		<li>X</li>
                       		<li>Y</li>
                       		<li>Z</li>                                  		
                      	</ul>
							<em class="1_4">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16614.html" target="_blank"> 土贝母</a>
							</em>
							<em class="1_5">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16618.html" target="_blank"> 徐长卿</a>
							</em>
							<em class="1_6">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16610.html" target="_blank"> 夏天无</a>
							</em>
					</dd>
				</dl>
			</div>
		</div>
		<b class="sanjiao"></b>
	</li>

					<!--商品分类2楼-->
						<li class="sort-list-item j_adminDiv" id="id1" data-ids="11042" data-type="1">
		<div class="sort-list-title">

			<i class="ico-sort450 ico-co2" data-categoryId="16812"></i>
			<h3>果实籽仁类</h3>

			<p>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E6%9E%B8%E6%9D%9E" target="_blank">枸杞</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E8%BF%9E%E7%BF%98" target="_blank">连翘</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E8%96%8F%E8%8B%A1%E4%BB%81" target="_blank">薏苡仁</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E5%85%AB%E8%A7%92" target="_blank">八角</a></em>
			</p>
		</div>

		<div class="sortsub fn-clear" id="div1" style="display: none">
			<div class="sortsub-l">
				<dl class="sortsub-item">
					<dd>
						<ul class="sortsub-letter">
                      		<li>A</li>
                      		<li>B</li>
                      		<li>C</li>
                      		<li>D</li>
                      		<li>E</li>
                      		<li>F</li>                                  		
                      	</ul>
							<em class="1_1">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16909.html" target="_blank"> 巴豆</a>
							</em>
							<em class="1_2">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16949.html" target="_blank"> 八角</a>
							</em>
							<em class="1_3">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16823.html" target="_blank"> 柏子仁</a>
							</em>
							<em class="1_4">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16857.html" target="_blank"> 白果</a>
							</em>
							<em class="1_5">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16855.html" target="_blank"> 槟榔</a>
							</em>
							<em class="1_6">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16813.html" target="_blank"> 八角茴香</a>
							</em>
							<em class="1_7">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16877.html" target="_blank"> 赤小豆</a>
							</em>
							<em class="1_8">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16871.html" target="_blank"> 茺蔚子</a>
							</em>
							<em class="1_9">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16937.html" target="_blank"> 草豆蔻</a>
							</em>
							<em class="1_10">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16865.html" target="_blank"> 车前子</a>
							</em>
							<em class="1_11">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16885.html" target="_blank"> 陈皮</a>
							</em>
							<em class="1_12">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16827.html" target="_blank"> 草果</a>
							</em>
							<em class="1_13">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16941.html" target="_blank"> 苍耳子</a>
							</em>
							<em class="1_14">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16829.html" target="_blank"> 大枣</a>
							</em>
							<em class="1_15">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16867.html" target="_blank"> 地肤子</a>
							</em>
							<em class="1_16">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16961.html" target="_blank"> 淡豆豉</a>
							</em>
							<em class="1_17">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16955.html" target="_blank"> 豆蔻</a>
							</em>
							<em class="1_18">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16825.html" target="_blank"> 佛手</a>
							</em>
							<em class="1_19">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16915.html" target="_blank"> 覆盆子</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>G</li>
                       		<li>H</li>
                       		<li>I</li>
                       		<li>J</li>
                       		<li>K</li>
                       		<li>L</li>                                  		
                      	</ul>
							<em class="1_20">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16839.html" target="_blank"> 枸杞</a>
							</em>
							<em class="1_21">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16963.html" target="_blank"> 枸杞子</a>
							</em>
							<em class="1_22">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16817.html" target="_blank"> 胡椒</a>
							</em>
							<em class="1_23">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16873.html" target="_blank"> 黑枸杞</a>
							</em>
							<em class="1_24">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16897.html" target="_blank"> 黑芝麻</a>
							</em>
							<em class="1_25">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16899.html" target="_blank"> 化橘红</a>
							</em>
							<em class="1_26">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16901.html" target="_blank"> 花椒</a>
							</em>
							<em class="1_27">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16913.html" target="_blank"> 黑豆</a>
							</em>
							<em class="1_28">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16965.html" target="_blank"> 核桃仁</a>
							</em>
							<em class="1_29">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16859.html" target="_blank"> 金樱子</a>
							</em>
							<em class="1_30">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16943.html" target="_blank"> 决明子</a>
							</em>
							<em class="1_31">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16935.html" target="_blank"> 橘红</a>
							</em>
							<em class="1_32">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16931.html" target="_blank"> 橘核</a>
							</em>
							<em class="1_33">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16917.html" target="_blank"> 苦杏仁</a>
							</em>
							<em class="1_34">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16905.html" target="_blank"> 苦瓜</a>
							</em>
							<em class="1_35">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16889.html" target="_blank"> 槐角</a>
							</em>
							<em class="1_36">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16815.html" target="_blank"> 罗汉果</a>
							</em>
							<em class="1_37">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16953.html" target="_blank"> 荔枝核</a>
							</em>
							<em class="1_38">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16835.html" target="_blank"> 莱菔子</a>
							</em>
							<em class="1_39">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16831.html" target="_blank"> 连翘</a>
							</em>
							<em class="1_40">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16819.html" target="_blank"> 莲子</a>
							</em>
							<em class="1_41">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16933.html" target="_blank"> 绿豆</a>
							</em>
							<em class="1_42">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16919.html" target="_blank"> 莲子心</a>
							</em>
							<em class="1_43">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16893.html" target="_blank"> 龙眼肉</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>M</li>
                       		<li>N</li>
                       		<li>O</li>
                       		<li>P</li>
                       		<li>Q</li>
                       		<li>R</li> 
                       		<li>S</li>                                 		
                      	</ul>
							<em class="1_44">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16861.html" target="_blank"> 木瓜</a>
							</em>
							<em class="1_45">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16921.html" target="_blank"> 马兜铃</a>
							</em>
							<em class="1_46">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16837.html" target="_blank"> 南五味子</a>
							</em>
							<em class="1_47">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16883.html" target="_blank"> 女贞子</a>
							</em>
							<em class="1_48">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16951.html" target="_blank"> 胖大海</a>
							</em>
							<em class="1_49">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16947.html" target="_blank"> 青果</a>
							</em>
							<em class="1_50">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16847.html" target="_blank"> 芡实</a>
							</em>
							<em class="1_51">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16903.html" target="_blank"> 肉豆蔻</a>
							</em>
							<em class="1_52">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16879.html" target="_blank"> 酸枣仁</a>
							</em>
							<em class="1_53">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16939.html" target="_blank"> 蛇床子</a>
							</em>
							<em class="1_54">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16945.html" target="_blank"> 砂仁</a>
							</em>
							<em class="1_55">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16959.html" target="_blank"> 桑椹</a>
							</em>
							<em class="1_56">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16833.html" target="_blank"> 沙棘</a>
							</em>
							<em class="1_57">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16923.html" target="_blank"> 丝瓜络</a>
							</em>
							<em class="1_58">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16929.html" target="_blank"> 山楂</a>
							</em>
							<em class="1_59">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16895.html" target="_blank"> 山茱萸</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                       		<li>T</li>
                       		<li>U</li>
                       		<li>V</li>
                       		<li>W</li>
                       		<li>X</li>
                       		<li>Y</li>
                       		<li>Z</li>                                  		
                      	</ul>
							<em class="1_60">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16887.html" target="_blank"> 菟丝子</a>
							</em>
							<em class="1_61">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16907.html" target="_blank"> 甜瓜子</a>
							</em>
							<em class="1_62">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16849.html" target="_blank"> 桃仁</a>
							</em>
							<em class="1_63">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16863.html" target="_blank"> 乌梅</a>
							</em>
							<em class="1_64">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16875.html" target="_blank"> 吴茱萸</a>
							</em>
							<em class="1_65">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16881.html" target="_blank"> 五味子</a>
							</em>
							<em class="1_66">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16845.html" target="_blank"> 小茴香</a>
							</em>
							<em class="1_67">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16957.html" target="_blank"> 香橼</a>
							</em>
							<em class="1_68">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16821.html" target="_blank"> 益智</a>
							</em>
							<em class="1_69">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16841.html" target="_blank"> 益智仁</a>
							</em>
							<em class="1_70">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16869.html" target="_blank"> 野山楂</a>
							</em>
							<em class="1_71">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16851.html" target="_blank"> 薏苡仁</a>
							</em>
							<em class="1_72">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16891.html" target="_blank"> 郁李仁</a>
							</em>
							<em class="1_73">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16843.html" target="_blank"> 栀子</a>
							</em>
							<em class="1_74">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16927.html" target="_blank"> 枳实</a>
							</em>
							<em class="1_75">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16911.html" target="_blank"> 枣核</a>
							</em>
							<em class="1_76">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16925.html" target="_blank"> 紫苏子</a>
							</em>
							<em class="1_77">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16853.html" target="_blank"> 枳壳</a>
							</em>
					</dd>
				</dl>
			</div>
		</div>
		<b class="sanjiao"></b>
	</li>

					<!--商品分类3楼-->
						<li class="sort-list-item j_adminDiv" id="id1" data-ids="11043" data-type="1">
		<div class="sort-list-title">
			<i class="ico-sort392 ico-co6" data-categoryId="16967"></i>
			<h3>全草类</h3>

			<p>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E7%9F%B3%E6%96%9B" target="_blank">石斛</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E8%8D%B7%E5%8F%B6" target="_blank">荷叶</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E5%B9%BF%E8%97%BF%E9%A6%99" target="_blank">广藿香</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E9%87%91%E9%92%B1%E8%8D%89" target="_blank">金钱草</a></em>
			</p>
		</div>

		<div class="sortsub fn-clear" id="div1" style="display: none">
			<div class="sortsub-l">
				<dl class="sortsub-item">
					<dd>
						<ul class="sortsub-letter">
                      		<li>A</li>
                      		<li>B</li>
                      		<li>C</li>
                      		<li>D</li>
                      		<li>E</li>
                      		<li>F</li>                                  		
                      	</ul>
							<em class="1_1">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17028.html" target="_blank"> 矮地茶</a>
							</em>
							<em class="1_2">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17026.html" target="_blank"> 白花蛇舌草</a>
							</em>
							<em class="1_3">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16970.html" target="_blank"> 半枝莲</a>
							</em>
							<em class="1_4">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16998.html" target="_blank"> 半边莲</a>
							</em>
							<em class="1_5">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17010.html" target="_blank"> 薄荷</a>
							</em>
							<em class="1_6">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17024.html" target="_blank"> 穿心莲</a>
							</em>
							<em class="1_7">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16978.html" target="_blank"> 车前草</a>
							</em>
							<em class="1_8">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16986.html" target="_blank"> 灯心草</a>
							</em>
							<em class="1_9">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17006.html" target="_blank"> 鹅不食草</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>G</li>
                       		<li>H</li>
                       		<li>I</li>
                       		<li>J</li>
                       		<li>K</li>
                       		<li>L</li>                                  		
                      	</ul>
							<em class="1_10">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16988.html" target="_blank"> 广金钱草</a>
							</em>
							<em class="1_11">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17020.html" target="_blank"> 广藿香</a>
							</em>
							<em class="1_12">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16992.html" target="_blank"> 藿香</a>
							</em>
							<em class="1_13">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16990.html" target="_blank"> 金线莲</a>
							</em>
							<em class="1_14">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17022.html" target="_blank"> 卷柏</a>
							</em>
							<em class="1_15">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17008.html" target="_blank"> 金钱草</a>
							</em>
							<em class="1_16">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17004.html" target="_blank"> 鸡骨草</a>
							</em>
							<em class="1_17">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17034.html" target="_blank"> 苦地丁</a>
							</em>
							<em class="1_18">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16984.html" target="_blank"> 连钱草</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>M</li>
                       		<li>N</li>
                       		<li>O</li>
                       		<li>P</li>
                       		<li>Q</li>
                       		<li>R</li> 
                       		<li>S</li>                                 		
                      	</ul>
							<em class="1_19">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17002.html" target="_blank"> 马齿苋</a>
							</em>
							<em class="1_20">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16972.html" target="_blank"> 马鞭草</a>
							</em>
							<em class="1_21">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17030.html" target="_blank"> 蒲公英</a>
							</em>
							<em class="1_22">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17018.html" target="_blank"> 青蒿</a>
							</em>
							<em class="1_23">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17012.html" target="_blank"> 肉苁蓉</a>
							</em>
							<em class="1_24">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17014.html" target="_blank"> 石斛</a>
							</em>
							<em class="1_25">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16980.html" target="_blank"> 锁阳</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                       		<li>T</li>
                       		<li>U</li>
                       		<li>V</li>
                       		<li>W</li>
                       		<li>X</li>
                       		<li>Y</li>
                       		<li>Z</li>                                  		
                      	</ul>
							<em class="1_26">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16996.html" target="_blank"> 铁皮石斛</a>
							</em>
							<em class="1_27">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16994.html" target="_blank"> 天山雪莲</a>
							</em>
							<em class="1_28">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16974.html" target="_blank"> 巫山淫羊藿</a>
							</em>
							<em class="1_29">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16968.html" target="_blank"> 夏枯草</a>
							</em>
							<em class="1_30">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17016.html" target="_blank"> 益母草</a>
							</em>
							<em class="1_31">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16976.html" target="_blank"> 鱼腥草</a>
							</em>
							<em class="1_32">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17000.html" target="_blank"> 淫羊藿</a>
							</em>
							<em class="1_33">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17032.html" target="_blank"> 茵陈</a>
							</em>
							<em class="1_34">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-16982.html" target="_blank"> 紫花地丁</a>
							</em>
					</dd>
				</dl>
			</div>
		</div>
		<b class="sanjiao"></b>
	</li>

					<!--商品分类4楼-->
						<li class="sort-list-item j_adminDiv" id="id1" data-ids="11044" data-type="1">
		<div class="sort-list-title">

			<i class="ico-sort464 ico-co4" data-categoryId="17036"></i>
			<h3>花类</h3>

			<p>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E7%BA%A2%E8%8A%B1" target="_blank">红花</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E9%87%91%E9%93%B6%E8%8A%B1" target="_blank">金银花</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E8%BE%9B%E5%A4%B7%E8%8A%B1" target="_blank">辛夷花</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E8%8F%8A%E8%8A%B1" target="_blank">菊花</a></em>
			</p>
		</div>

		<div class="sortsub fn-clear" id="div1" style="display: none">
			<div class="sortsub-l">
				<dl class="sortsub-item">
					<dd>
						<ul class="sortsub-letter">
                      		<li>A</li>
                      		<li>B</li>
                      		<li>C</li>
                      		<li>D</li>
                      		<li>E</li>
                      		<li>F</li>                                  		
                      	</ul>
							<em class="1_1">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17037.html" target="_blank"> 丁香</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>G</li>
                       		<li>H</li>
                       		<li>I</li>
                       		<li>J</li>
                       		<li>K</li>
                       		<li>L</li>                                  		
                      	</ul>
							<em class="1_2">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17077.html" target="_blank"> 厚朴花</a>
							</em>
							<em class="1_3">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17057.html" target="_blank"> 合欢花</a>
							</em>
							<em class="1_4">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17045.html" target="_blank"> 红花</a>
							</em>
							<em class="1_5">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17083.html" target="_blank"> 金银花</a>
							</em>
							<em class="1_6">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17053.html" target="_blank"> 菊花</a>
							</em>
							<em class="1_7">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17051.html" target="_blank"> 鸡冠花</a>
							</em>
							<em class="1_8">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17059.html" target="_blank"> 款冬花</a>
							</em>
							<em class="1_9">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17081.html" target="_blank"> 槐花</a>
							</em>
							<em class="1_10">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17055.html" target="_blank"> 凌霄花</a>
							</em>
							<em class="1_11">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17067.html" target="_blank"> 莲须</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>M</li>
                       		<li>N</li>
                       		<li>O</li>
                       		<li>P</li>
                       		<li>Q</li>
                       		<li>R</li> 
                       		<li>S</li>                                 		
                      	</ul>
							<em class="1_12">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17069.html" target="_blank"> 玫瑰花</a>
							</em>
							<em class="1_13">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17047.html" target="_blank"> 玫瑰茄</a>
							</em>
							<em class="1_14">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17075.html" target="_blank"> 木棉花</a>
							</em>
							<em class="1_15">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17065.html" target="_blank"> 梅花</a>
							</em>
							<em class="1_16">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17063.html" target="_blank"> 茉莉花</a>
							</em>
							<em class="1_17">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17043.html" target="_blank"> 山银花</a>
							</em>
							<em class="1_18">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17061.html" target="_blank"> 三七花</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                       		<li>T</li>
                       		<li>U</li>
                       		<li>V</li>
                       		<li>W</li>
                       		<li>X</li>
                       		<li>Y</li>
                       		<li>Z</li>                                  		
                      	</ul>
							<em class="1_19">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17049.html" target="_blank"> 桃花</a>
							</em>
							<em class="1_20">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17071.html" target="_blank"> 旋覆花</a>
							</em>
							<em class="1_21">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17039.html" target="_blank"> 西红花</a>
							</em>
							<em class="1_22">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17041.html" target="_blank"> 辛夷</a>
							</em>
							<em class="1_23">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17073.html" target="_blank"> 月季花</a>
							</em>
							<em class="1_24">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17079.html" target="_blank"> 野菊花</a>
							</em>
					</dd>
				</dl>
			</div>
		</div>
		<b class="sanjiao"></b>
	</li>

					<!--商品分类5楼-->
						<li class="sort-list-item j_adminDiv" id="id1" data-ids="11045" data-type="1">
		<div class="sort-list-title">

			<i class="ico-sort464 ico-co5" data-categoryId="17085"></i>
			<h3>叶类</h3>

			<p>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E8%8D%B7%E5%8F%B6" target="_blank">荷叶</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E8%8D%B7%E5%8F%B6" target="_blank">银杏叶</a></em>
					<em><a href="" target="_blank">紫苏叶</a></em>
					<em><a href="" target="_blank">艾叶</a></em>
			</p>
		</div>

		<div class="sortsub fn-clear" id="div1" style="display: none">
			<div class="sortsub-l">
				<dl class="sortsub-item">
					<dd>
						<ul class="sortsub-letter">
                      		<li>A</li>
                      		<li>B</li>
                      		<li>C</li>
                      		<li>D</li>
                      		<li>E</li>
                      		<li>F</li>                                  		
                      	</ul>
							<em class="1_1">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17106.html" target="_blank"> 艾叶</a>
							</em>
							<em class="1_2">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17100.html" target="_blank"> 侧柏叶</a>
							</em>
							<em class="1_3">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17104.html" target="_blank"> 杜仲叶</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>G</li>
                       		<li>H</li>
                       		<li>I</li>
                       		<li>J</li>
                       		<li>K</li>
                       		<li>L</li>                                  		
                      	</ul>
							<em class="1_4">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17096.html" target="_blank"> 广东紫珠</a>
							</em>
							<em class="1_5">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17088.html" target="_blank"> 荷叶</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>M</li>
                       		<li>N</li>
                       		<li>O</li>
                       		<li>P</li>
                       		<li>Q</li>
                       		<li>R</li> 
                       		<li>S</li>                                 		
                      	</ul>
							<em class="1_6">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17110.html" target="_blank"> 枇杷叶</a>
							</em>
							<em class="1_7">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17102.html" target="_blank"> 人参叶</a>
							</em>
							<em class="1_8">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17092.html" target="_blank"> 桑叶</a>
							</em>
							<em class="1_9">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17094.html" target="_blank"> 四季青</a>
							</em>
							<em class="1_10">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17108.html" target="_blank"> 山楂叶</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                       		<li>T</li>
                       		<li>U</li>
                       		<li>V</li>
                       		<li>W</li>
                       		<li>X</li>
                       		<li>Y</li>
                       		<li>Z</li>                                  		
                      	</ul>
							<em class="1_11">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17086.html" target="_blank"> 银杏叶</a>
							</em>
							<em class="1_12">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17090.html" target="_blank"> 紫苏叶</a>
							</em>
							<em class="1_13">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17098.html" target="_blank"> 棕榈</a>
							</em>
					</dd>
				</dl>
			</div>
		</div>
		<b class="sanjiao"></b>
	</li>

					<!--商品分类6楼-->
						<li class="sort-list-item j_adminDiv" id="id1" data-ids="11046" data-type="1">
		<div class="sort-list-title">

			<i class="ico-sort524 ico-co7" data-categoryId="17112"></i>
			<h3>动物类</h3>

			<p>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E6%B5%B7%E9%A9%AC" target="_blank">海马</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E5%9C%B0%E9%BE%99" target="_blank">地龙</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E8%9C%88%E8%9A%A3" target="_blank">蜈蚣</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E9%B3%96%E7%94%B2" target="_blank">鳖甲</a></em>
			</p>
		</div>

		<div class="sortsub fn-clear" id="div1" style="display: none">
			<div class="sortsub-l">
				<dl class="sortsub-item">
					<dd>
						<ul class="sortsub-letter">
                      		<li>A</li>
                      		<li>B</li>
                      		<li>C</li>
                      		<li>D</li>
                      		<li>E</li>
                      		<li>F</li>                                  		
                      	</ul>
							<em class="1_1">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17141.html" target="_blank"> 阿胶</a>
							</em>
							<em class="1_2">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17117.html" target="_blank"> 鳖甲</a>
							</em>
							<em class="1_3">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17145.html" target="_blank"> 蝉蜕</a>
							</em>
							<em class="1_4">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17113.html" target="_blank"> 穿山甲</a>
							</em>
							<em class="1_5">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17176.html" target="_blank"> 地龙</a>
							</em>
							<em class="1_6">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17133.html" target="_blank"> 玳瑁</a>
							</em>
							<em class="1_7">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17149.html" target="_blank"> 蜂蜜</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>G</li>
                       		<li>H</li>
                       		<li>I</li>
                       		<li>J</li>
                       		<li>K</li>
                       		<li>L</li>                                  		
                      	</ul>
							<em class="1_8">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17155.html" target="_blank"> 龟甲</a>
							</em>
							<em class="1_9">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17186.html" target="_blank"> 蛤蚧</a>
							</em>
							<em class="1_10">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17159.html" target="_blank"> 海马</a>
							</em>
							<em class="1_11">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17123.html" target="_blank"> 哈蟆油</a>
							</em>
							<em class="1_12">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17174.html" target="_blank"> 海龙</a>
							</em>
							<em class="1_13">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17172.html" target="_blank"> 蛤壳</a>
							</em>
							<em class="1_14">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17115.html" target="_blank"> 鸡内金</a>
							</em>
							<em class="1_15">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17137.html" target="_blank"> 金钱白花蛇</a>
							</em>
							<em class="1_16">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17163.html" target="_blank"> 僵蚕</a>
							</em>
							<em class="1_17">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17182.html" target="_blank"> 九香虫</a>
							</em>
							<em class="1_18">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17135.html" target="_blank"> 鹿角</a>
							</em>
							<em class="1_19">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17147.html" target="_blank"> 鹿茸</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>M</li>
                       		<li>N</li>
                       		<li>O</li>
                       		<li>P</li>
                       		<li>Q</li>
                       		<li>R</li> 
                       		<li>S</li>                                 		
                      	</ul>
							<em class="1_20">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17139.html" target="_blank"> 美洲大蠊</a>
							</em>
							<em class="1_21">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17151.html" target="_blank"> 牡蛎</a>
							</em>
							<em class="1_22">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17143.html" target="_blank"> 牛黄</a>
							</em>
							<em class="1_23">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17121.html" target="_blank"> 全蝎</a>
							</em>
							<em class="1_24">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17169.html" target="_blank"> 蕲蛇</a>
							</em>
							<em class="1_25">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17178.html" target="_blank"> 麝香</a>
							</em>
							<em class="1_26">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17161.html" target="_blank"> 石决明</a>
							</em>
							<em class="1_27">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17127.html" target="_blank"> 水蛭</a>
							</em>
							<em class="1_28">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17180.html" target="_blank"> 水牛角</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                       		<li>T</li>
                       		<li>U</li>
                       		<li>V</li>
                       		<li>W</li>
                       		<li>X</li>
                       		<li>Y</li>
                       		<li>Z</li>                                  		
                      	</ul>
							<em class="1_29">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17157.html" target="_blank"> 蜈蚣</a>
							</em>
							<em class="1_30">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17153.html" target="_blank"> 五倍子</a>
							</em>
							<em class="1_31">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17184.html" target="_blank"> 乌梢蛇</a>
							</em>
							<em class="1_32">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17165.html" target="_blank"> 血余炭</a>
							</em>
							<em class="1_33">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17129.html" target="_blank"> 鱼鳔</a>
							</em>
							<em class="1_34">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17131.html" target="_blank"> 燕窝</a>
							</em>
							<em class="1_35">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17119.html" target="_blank"> 珍珠</a>
							</em>
							<em class="1_36">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17125.html" target="_blank"> 珍珠母</a>
							</em>
							<em class="1_37">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17167.html" target="_blank"> 猪胆粉</a>
							</em>
					</dd>
				</dl>
			</div>
		</div>
		<b class="sanjiao"></b>
	</li>

					<!--商品分类7楼-->
						<li class="sort-list-item j_adminDiv" id="id1" data-ids="11047" data-type="1">
		<div class="sort-list-title">

			<i class="ico-sort525 ico-co8" data-categoryId="17188"></i>
			<h3>树皮类</h3>

			<p>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E6%9D%9C%E4%BB%B2" target="_blank">杜仲</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E5%8E%9A%E6%9C%B4" target="_blank">厚朴</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E9%BB%84%E6%9F%8F" target="_blank">黄柏</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E8%82%89%E6%A1%82" target="_blank">肉桂</a></em>
			</p>
		</div>

		<div class="sortsub fn-clear" id="div1" style="display: none">
			<div class="sortsub-l">
				<dl class="sortsub-item">
					<dd>
						<ul class="sortsub-letter">
                      		<li>A</li>
                      		<li>B</li>
                      		<li>C</li>
                      		<li>D</li>
                      		<li>E</li>
                      		<li>F</li>                                  		
                      	</ul>
							<em class="1_1">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17213.html" target="_blank"> 椿皮</a>
							</em>
							<em class="1_2">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17209.html" target="_blank"> 川黄柏</a>
							</em>
							<em class="1_3">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17201.html" target="_blank"> 地骨皮</a>
							</em>
							<em class="1_4">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17197.html" target="_blank"> 杜仲</a>
							</em>
							<em class="1_5">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17195.html" target="_blank"> 冬瓜皮</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>G</li>
                       		<li>H</li>
                       		<li>I</li>
                       		<li>J</li>
                       		<li>K</li>
                       		<li>L</li>                                  		
                      	</ul>
							<em class="1_6">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17211.html" target="_blank"> 关黄柏</a>
							</em>
							<em class="1_7">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17199.html" target="_blank"> 黄柏</a>
							</em>
							<em class="1_8">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17207.html" target="_blank"> 厚朴</a>
							</em>
							<em class="1_9">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17189.html" target="_blank"> 合欢皮</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>M</li>
                       		<li>N</li>
                       		<li>O</li>
                       		<li>P</li>
                       		<li>Q</li>
                       		<li>R</li> 
                       		<li>S</li>                                 		
                      	</ul>
							<em class="1_10">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17203.html" target="_blank"> 牡丹皮</a>
							</em>
							<em class="1_11">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17191.html" target="_blank"> 肉桂</a>
							</em>
							<em class="1_12">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17215.html" target="_blank"> 桑寄生</a>
							</em>
							<em class="1_13">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17205.html" target="_blank"> 桑白皮</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                       		<li>T</li>
                       		<li>U</li>
                       		<li>V</li>
                       		<li>W</li>
                       		<li>X</li>
                       		<li>Y</li>
                       		<li>Z</li>                                  		
                      	</ul>
							<em class="1_14">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17193.html" target="_blank"> 五加皮</a>
							</em>
					</dd>
				</dl>
			</div>
		</div>
		<b class="sanjiao"></b>
	</li>

					<!--商品分类8楼-->
						<li class="sort-list-item j_adminDiv" id="id1" data-ids="11048" data-type="1">
		<div class="sort-list-title">

			<i class="ico-sort525 ico-co9" data-categoryId="17216"></i>
			<h3>藤木类</h3>

			<p>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E9%92%A9%E8%97%A4" target="_blank">钩藤</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E9%B8%A1%E8%A1%80%E8%97%A4" target="_blank">鸡血藤</a></em>
					<em><a href="" target="_blank">通草</a></em>
					<em><a href="" target="_blank">皂角刺</a></em>
			</p>
		</div>

		<div class="sortsub fn-clear" id="div1" style="display: none">
			<div class="sortsub-l">
				<dl class="sortsub-item">
					<dd>
						<ul class="sortsub-letter">
                      		<li>A</li>
                      		<li>B</li>
                      		<li>C</li>
                      		<li>D</li>
                      		<li>E</li>
                      		<li>F</li>                                  		
                      	</ul>
							<em class="1_1">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17219.html" target="_blank"> 通草</a>
							</em>
							<em class="1_2">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17229.html" target="_blank"> 沉香</a>
							</em>
							<em class="1_3">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17247.html" target="_blank"> 滇鸡血藤</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>G</li>
                       		<li>H</li>
                       		<li>I</li>
                       		<li>J</li>
                       		<li>K</li>
                       		<li>L</li>                                  		
                      	</ul>
							<em class="1_4">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17221.html" target="_blank"> 钩藤</a>
							</em>
							<em class="1_5">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17235.html" target="_blank"> 鸡血藤</a>
							</em>
							<em class="1_6">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17243.html" target="_blank"> 降香</a>
							</em>
							<em class="1_7">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17245.html" target="_blank"> 雷公藤</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>M</li>
                       		<li>N</li>
                       		<li>O</li>
                       		<li>P</li>
                       		<li>Q</li>
                       		<li>R</li> 
                       		<li>S</li>                                 		
                      	</ul>
							<em class="1_8">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17239.html" target="_blank"> 忍冬藤</a>
							</em>
							<em class="1_9">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17233.html" target="_blank"> 首乌藤</a>
							</em>
							<em class="1_10">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17223.html" target="_blank"> 苏木</a>
							</em>
							<em class="1_11">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17227.html" target="_blank"> 桑枝</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                       		<li>T</li>
                       		<li>U</li>
                       		<li>V</li>
                       		<li>W</li>
                       		<li>X</li>
                       		<li>Y</li>
                       		<li>Z</li>                                  		
                      	</ul>
							<em class="1_12">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17231.html" target="_blank"> 通关藤</a>
							</em>
							<em class="1_13">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17225.html" target="_blank"> 桃枝</a>
							</em>
							<em class="1_14">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17217.html" target="_blank"> 檀香</a>
							</em>
							<em class="1_15">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17237.html" target="_blank"> 小通草</a>
							</em>
							<em class="1_16">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17241.html" target="_blank"> 皂角刺</a>
							</em>
					</dd>
				</dl>
			</div>
		</div>
		<b class="sanjiao"></b>
	</li>

					 <!--商品分类9楼-->
						<li class="sort-list-item j_adminDiv" id="id1" data-ids="187928" data-type="1">
		<div class="sort-list-title">

			<i class="ico-sort525 ico-co10" data-categoryId="17249"></i>
			<h3>矿物菌脂类</h3>

			<p>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E8%8C%AF%E8%8B%93" target="_blank">茯苓</a></em>
					<em><a href="http://search.kmb2b.com/10/search?kw=%E7%AB%B9%E7%81%B5%E8%8A%9D" target="_blank">竹灵芝</a></em>
					<em><a href="" target="_blank">银耳</a></em>
					<em><a href="" target="_blank">白矾</a></em>
			</p>
		</div>

		<div class="sortsub fn-clear" id="div1" style="display: none">
			<div class="sortsub-l">
				<dl class="sortsub-item">
					<dd>
						<ul class="sortsub-letter">
                      		<li>A</li>
                      		<li>B</li>
                      		<li>C</li>
                      		<li>D</li>
                      		<li>E</li>
                      		<li>F</li>                                  		
                      	</ul>
							<em class="1_1">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17531.html" target="_blank"> 白矾</a>
							</em>
							<em class="1_2">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17536.html" target="_blank"> 赤石脂</a>
							</em>
							<em class="1_3">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17539.html" target="_blank"> 冬虫夏草</a>
							</em>
							<em class="1_4">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17408.html" target="_blank"> 茯苓</a>
							</em>
							<em class="1_5">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17548.html" target="_blank"> 茯苓皮</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>G</li>
                       		<li>H</li>
                       		<li>I</li>
                       		<li>J</li>
                       		<li>K</li>
                       		<li>L</li>                                  		
                      	</ul>
							<em class="1_6">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17540.html" target="_blank"> 海藻</a>
							</em>
							<em class="1_7">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17534.html" target="_blank"> 滑石粉</a>
							</em>
							<em class="1_8">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17299.html" target="_blank"> 滑石</a>
							</em>
							<em class="1_9">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17546.html" target="_blank"> 金蝉花</a>
							</em>
							<em class="1_10">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17542.html" target="_blank"> 螺旋藻</a>
							</em>
							<em class="1_11">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17541.html" target="_blank"> 灵芝</a>
							</em>
							<em class="1_12">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17530.html" target="_blank"> 硫黄</a>
							</em>
							<em class="1_13">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17535.html" target="_blank"> 炉甘石</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                      		<li>M</li>
                       		<li>N</li>
                       		<li>O</li>
                       		<li>P</li>
                       		<li>Q</li>
                       		<li>R</li> 
                       		<li>S</li>                                 		
                      	</ul>
							<em class="1_14">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17549.html" target="_blank"> 没药</a>
							</em>
							<em class="1_15">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17547.html" target="_blank"> 马勃</a>
							</em>
							<em class="1_16">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17550.html" target="_blank"> 乳香</a>
							</em>
							<em class="1_17">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17537.html" target="_blank"> 石膏</a>
							</em>
					</dd>
					<dd>
						<ul class="sortsub-letter">
                       		<li>T</li>
                       		<li>U</li>
                       		<li>V</li>
                       		<li>W</li>
                       		<li>X</li>
                       		<li>Y</li>
                       		<li>Z</li>                                  		
                      	</ul>
							<em class="1_18">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17543.html" target="_blank"> 云芝</a>
							</em>
							<em class="1_19">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17545.html" target="_blank"> 银耳</a>
							</em>
							<em class="1_20">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17544.html" target="_blank"> 猪苓</a>
							</em>
							<em class="1_21">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17533.html" target="_blank"> 朱砂</a>
							</em>
							<em class="1_22">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17532.html" target="_blank"> 赭石</a>
							</em>
							<em class="1_23">
								<a href="<%=Configuration.getContextProperty("searchPath_B2B")%>/c-list-17538.html" target="_blank"> 钟乳石</a>
							</em>
					</dd>
				</dl>
			</div>
		</div>
		<b class="sanjiao"></b>
	</li>

                  
					<div class="bottom-yinying"></div>
				</ul>
			</div>
			<!--导航标题窗口-->
			<ul id="navitems" class="navitems fn-clear j_adminDiv" data-ids="188023" data-type="1">
      	  <li class="nav-news" title="首页">
				<a href="http://www.kmb2b.com/" target="_blank">首页</a>
			</li>
      	  <li class="nav-news" title="道地产区">
				<a href="http://www.kmb2b.com/" target="_blank">道地产区</a>
			</li>
      	  <li class="nav-news" title="档口货源">
				<a href="http://www.kmb2b.com/" target="_blank">档口货源</a>
			</li>
      	  <li class="nav-news" title="大企业采购">
				<a href="http://www.kmb2b.com/" target="_blank">大企业采购</a>
			</li>
      	  <li class="nav-news" title="药金融">
				<a href="http://www.kmb2b.com/" target="_blank">药金融</a>
			</li>
  	<ul class="navitems-r">    
      <li class="nav-news-r" style="margin-right: 70px;"><em></em><a href="javascript:void(0);">产业服务</a></li>
      <li class="nav-news-r"><em></em><a href="javascript:void(0);">行业快讯</a></li>
   </ul>
</ul>
			
	
		</div>
	<!--i-nav end-->
</div>