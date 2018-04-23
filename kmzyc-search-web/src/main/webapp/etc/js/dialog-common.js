
//url 为路径地址加参数   例如（'/productRelation/relationQueryProduct.action?productTied.productSkuId='+trId）
//dialogTitle :窗口显示标题
// 宽度（单位默认为px）
// 高度
//  需要在jsp 文件中导入一下文件
 // <script language='JavaScript' src='/etc/js/artDialog4.1.7/artDialog.js?skin=default' type='text/javascript'></script>");
 // <script language='JavaScript' src='/etc/js/artDialog4.1.7/plugins/iframeTools.source.js' type='text/javascript'></script>");
// <script language='JavaScript' src='/etc/js/jquery.blockUI.js' type='text/javascript'></script>");
 //弹出框
var myDialog;
 function  popDialog(url,dialogTitle,widthLength,heigthLength){
 myDialog = art.dialog.open(url, {
		   title: dialogTitle,
		    width:widthLength,
			height:heigthLength,
			drag:true,
			close:function(){
			$.unblockUI();
			}
	   });
	   
 $.blockUI.defaults.overlayCSS.opacity = '0.5';
 $.blockUI({ message: "" }); 
}
 
 
 function closeThis(){
	 myDialog.close();
 }
 