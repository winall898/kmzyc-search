//得到Cookie
function getCookie(name){
	var cname = name + "=";
	var dc = document.cookie;
	if (dc.length > 0){
		begin = dc.indexOf(cname);
		if (begin != -1){
			begin += cname.length;
			end = dc.indexOf(";", begin);
			if (end == -1){
				end = dc.length;
			}
			return dc.substring(begin, end);
		}
	}
	return null;
}

//写Cookie 过期时间是一年
function writeCookie(name, value) { 
	var expire = ""; 
	var hours = 365;
	expire = new Date((new Date()).getTime() + hours * 3600000); 
	expire = ";path=/;expires=" + expire.toGMTString(); 
	document.cookie = name + "=" + escape(value) + expire; 
}