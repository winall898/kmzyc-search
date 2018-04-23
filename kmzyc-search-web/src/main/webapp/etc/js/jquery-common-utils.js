/**
 * author LEON
 * 2012-02-24
 */
(function($){
	//jquery扩展
	$.extend($, {
		/**
		 * json转化为字符串
		 */
		jsonToString : function(obj){
			switch(typeof(obj)) 
	        {
	            case 'object':
	                var ret = [];
	                if (obj instanceof Array) 
	                {
	                    for (var i = 0, len = obj.length; i < len; i++) 
	                    {
	                        ret.push($.jsonToString(obj[i]));
	                    }
	                    return '[' + ret.join(',') + ']';
	                } 
	                else if (obj instanceof RegExp) 
	                {
	                    return obj.toString();
	                } 
	                else 
	                {
	                    for (var a in obj) 
	                    {
	                        ret.push('"' + a + '":' + $.jsonToString(obj[a]));
	                    }
	                    return '{' + ret.join(',') + '}';
	                }
	            case 'function':
	                return 'function() {}';
	            case 'number':
	                return obj.toString();
	            case 'string':
	                return "\"" + obj.replace(/(\\|\")/g, "\\$1").replace(/\n|\r|\t/g, function(a) {return ("\n"==a)?"\\n":("\r"==a)?"\\r":("\t"==a)?"\\t":"";}) + "\"";
	            case 'boolean':
	                return obj.toString();
	            default:
	                return obj.toString();
	        }
		},
		/**
		 * 获得this对象，event指js原生
		 */
		getThis : function(event){
			if($.browser.msie){
				return event.srcElement
			}else{
				return event.target
			}
		},
		/**
		 * 获得屏幕可视高度
		 */
		offHeight : function(){
			if($.browser.msie)return document.documentElement.clientHeight;
			return window.innerHeight;
		},
		/**
		 * 浏览器复制操作
		 */
		copyText : function(text,callback){
			if (window.clipboardData) {
				  window.clipboardData.setData("Text", text);
			} else if (window.netscape) {
				try{
					netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
				}catch(e){
					alert("您的浏览器安全限制限制您进行复制操作，请手动复制代码。");
					return;
				}
				var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
				if (!clip)
					return;
				var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
				if (!trans)
					return;
				trans.addDataFlavor('text/unicode');
				var str = new Object();
				var len = new Object();
				var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
				var copytext = text;
				str.data = copytext;
				trans.setTransferData("text/unicode", str, copytext.length * 2);
				var clipid = Components.interfaces.nsIClipboard;
				if (!clip)
					return false;
				clip.setData(trans, null, clipid.kGlobalClipboard);
			}
			alert('复制成功！');
		},
		/**
		 * 阻止浏览器默认事件
		 */
		stopDefault : function( e ) { 
		    if ( e && e.preventDefault ) 
		        e.preventDefault(); 
		    else 
		        window.event.returnValue = false; 
		    return false; 
		},
		/**
		 * 阻止冒泡事件
		 */
		stopBubble : function(e){
			if ( e && e.stopPropagation ){
				 e.stopPropagation();
			} else {
	        	window.event.cancelBubble = true;
	        }
		},
		/**
		 * cookie相关操作
		 */
		setCookie : function(setting) {
			var opt = $.extend( {
				key : '',
				value : '',
				path : '/',
				domain : '',
				// day>0正常,day<0会话,day=0删除
				expireDay : -1,
				// today是否截至00:00:00,需与expireDay配合
				today : false,
				// https安全cookie
				secure : false
			}, setting);
			if (opt.key != '') {
				var ret = new Array();
				ret.push(opt.key);
				ret.push("=");
				ret.push(encodeURIComponent(opt.value));
				ret.push("; path=");
				ret.push(opt.path);
				if (opt.domain != '') {
					ret.push("; domain=");
					ret.push(opt.domain);
				}
				if (opt.expireDay > 0) {
					var date = new Date();
					if (opt.today) {
						date.setHours(0, 0, 0, 0);
					}
					date.setTime(date.getTime() + opt.expireDay * 24 * 60 * 60
							* 1000+1000);
					ret.push("; expires=");
					ret.push(date.toGMTString());
				} else if (opt.expireDay == 0) {
					var date = new Date();
					date.setTime(date.getTime() - 1);
					ret.push("; expires=");
					ret.push(date.toGMTString());
				}
				if (opt.secure) {
					ret.push("; secure");
				}
				document.cookie = ret.join("");
			}
		},
		getCookie : function(key) {
			var value = document.cookie.match('(?:^|;)\\s*' + key.replace(
					/([.*+?^${}()|[\]\/\\])/g, '\\$1') + '=([^;]*)');
			return value ? decodeURIComponent(value[1]) : false;
		},
		delCookie : function(key) {
			$.setCookie( {
				key : key,
				expireDay : 0
			});
		}
	});
	
})(jQuery);