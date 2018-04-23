function on_input(objname){
	var obj=document.getElementById(objname);
	obj.className="d_on";
}
function checkCustName(){
	var userName=document.getElementById("custName");
	var divUserName=document.getElementById("d_custName");
	// 过滤空格
	userName.value=userName.value.replace(/\s/ig,"");
	// 验证文本是否为空，长度
	if (!check_text(userName,4,16,"长度不符，应在4-16个字符间！",divUserName)){
		return false;
	}
	// 是否为非法字符(特殊字符)
	if (!havegg(userName.value)){
			divUserName.className="d_err";
			divUserName.innerHTML='含有非法字符，请重新填写！';
			return false;
	}
	// 是否为非法字符（网站关键字）
	if (!check_username(userName.value,divUserName)){
		 return false;
	}else{// 是否为非法字符（数据库关键字）
		if(check(userName,divUserName)){
			return checkCustNameAjax();
		}else{
			return false;
		}
	}
}
// 用户名是否存在
function checkCustNameAjax(){
var custName=document.getElementById("custName");
var divCustName=document.getElementById("d_custName");
if(custName.value==''){
	divCustName.className="d_err";
	divCustName.innerHTML='您还没有填写！';
	return false;
}
var result= $.ajax({
	  url: "/cust/checkCustName.action?custName="+encodeURI(custName.value),
	  async: false
	 }).responseText;
if(result.indexOf("0")>-1){
	   divCustName.className="d_err";
	   divCustName.innerHTML='用户名已存在！';
	 return false;
	}else{
		divCustName.className="d_ok";
		divCustName.innerHTML='用户名可用！';
		 return true;
	}
}



// 检查密码
function checkPwd(){
	var password=document.getElementById("custPwd1");
	var divpwd=document.getElementById("d_custPwd1");

	if (!check_text(password,6,15,"长度不符，应在6-15个字符间！",divpwd)  ||  !check1(password,divpwd)){
		return false;
	}
	else{
		divpwd.className="d_ok";
		divpwd.innerHTML='ok';
		return true;
		
	}
}

// 检查密码确认
function checkRePwd(){
	var password=document.getElementById("custPwd1");
	var rePassword=document.getElementById("reCustPwd");
	var divpwd=document.getElementById("d_custPwd");
	if (!check_password(password,rePassword,6,15,"长度应在6-15个字符间！",divpwd)){
		return false;
	} else{
		divpwd.className="d_ok";
		divpwd.innerHTML='ok';
		return true;
	}
}


// 验证码
function checkCode(){
		var verifycode=document.getElementById("verifycode");
		var divCode=document.getElementById("d_verifycode");
		if (verifycode.value.length !=4 ){
				divCode.className="d_err";
		    	divCode.innerHTML='验证码有误，请刷新重填！';
		  		return false;
		  	} else{
		  		var result= $.ajax({
					  url: "/cust/checkCode.action?verifycode="+verifycode.value,
					  async: false
					 }).responseText;
		  		if(result.indexOf("1")>-1){
		  			divCode.className="d_ok";
			  		divCode.innerHTML='ok';
		    		return true;
		  		}else{
		  			divCode.className="d_err";
		  			divCode.innerHTML='验证码有误，请刷新重填！';
			  		return false;
		  		}
			}
}


function chk_reg(){
	if(!checkCustName()||!checkRePwd()||!checkCode()){
		return false;
	}else{
		var agree=document.getElementById("agree");
		if(!agree.checked){
			alert('必须认可注册协议才能注册！');
			return false;
			}
		}
	return true;
}

// 刷新验证码
function reloadCode(){
	var verify = document.getElementById('randImage');	
	verify.src="/cust/getVerifycode.action?"+new Date().getTime();
}

// 检查真实名字
function checkCustReal(){
	var custReal=document.getElementById("custReal");
	var divCustReal=document.getElementById("d_custReal");
	if(custReal.value.Tlength()<=0){
		return true;
	}
	if(custReal.value.Tlength()>0 && custReal.value.Tlength() < 4){
		divCustReal.className="d_err";
		divCustReal.innerHTML='姓名不应少于2个汉字！';
		return false;
	}
	return check(custReal,divCustReal);
	
}

// 身份证号码
function checkCardno(){
	 var custCardno=document.getElementById('custCardno').value;
	 var d_custCardno=document.getElementById('d_custCardno');
	 if(custCardno==''){
			return true;
		}
	 if(isIDno(custCardno)){
			d_custCardno.className="d_ok";
			d_custCardno.innerHTML='ok';
			return true;
		}else{
			d_custCardno.className="d_err";
			d_custCardno.innerHTML='请填写正确的身份证号码！';
			return false;
		}
}

function checkEmail(){
	 var custEmail=document.getElementById('custEmail');
	 var d_custEmail=document.getElementById('d_custEmail');
	 if(custEmail.value.Tlength()<=0){
		 return true;
	 }else{
		 var pattern = /^([.a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/; 
			flag = pattern.test(custEmail.value); 
			if(!flag){
				d_custEmail.className="d_err";
				d_custEmail.innerHTML='您填写的邮箱格式有误！'; 
				return false;
			}else{
				return check(custEmail,d_custEmail);
			}
	 }
}



function chk_regInfo(){
	if(!checkCustReal()||!checkCardno()||!checkEmail()){
		return false;
	}
	return true;
}

function chk_regPwd(){
	if(!checkRePwd()){
		return false;
	}
	return true;
}


// 密码是否正确
function checkPassword(){
		var pwd=document.getElementById("pwd");
		var d_pwd=document.getElementById("d_pwd");
		if (!check_text(pwd,6,15,"长度不符，应在6-15个字符间！",d_pwd)  ||  !check1(pwd,d_pwd)){
			return false;
		}else{
			var result= $.ajax({
					  url: "/cust/checkPwd.action?pwd="+pwd.value,
					  async: false
					 }).responseText;
		  		if(result.indexOf("1")>-1){
		  			d_pwd.className="d_ok";
			  		d_pwd.innerHTML='ok';
		    		return true;
		  		}else{
		  			d_pwd.className="d_err";
		  			d_pwd.innerHTML='密码错误，清重新填写！';
			  		return false;
		  		}
		}
}
// 该函数用于检验文本框
function check_text(Item, MinLength, MaxLength, ItemCaption,ItemShow){
	if (!is_item_not_null(Item, ItemCaption,ItemShow)){
		return false;
	}
	if (!check_length_asc(Item, MinLength, MaxLength, ItemCaption,ItemShow)){
		return false;
	}
	return true;
}

/*
 * 该函数将判断表单中元素值是否为空 该函数将调用is_only_space Item: 表单元素名称 ItemCaption: Item的说明
 */
function is_item_not_null(Item, ItemCaption,ItemShow){
	if ((Item.value == "") || is_only_space(Item.value)){
		ItemShow.className="d_err";
		ItemShow.innerHTML=ItemCaption + "不能为空！";
		// Item.focus();
		return false;
		}
	return true;
}

function check_length_asc(Item, MinLength, MaxLength, ItemCaption,ItemShow){
   // 该函数使用字符的长度
	if ((MinLength == 0) && (MaxLength == 0)){
		return true;
	}
	if (MaxLength < MinLength){
		alert("\"check_length_asc\"函数调用错误。");
		return false;
	}
	if ((Item.value.Tlength() < MinLength) || (Item.value.Tlength() > MaxLength)){
		ItemShow.className="d_err";
		ItemShow.innerHTML=ItemCaption;
		// Item.focus();
		return false;
	}
	else{
		return true;
	}
}

/*
 * 该函数将判断某一变量是否为空字符串 该函数将被函数is_textbox_null调用 str:被测试的字符串变量
 */
function is_only_space(str){
	for(i=0;i<=str.length-1;i++){
		if (str.charAt(i) != " ") 
			return false;
	}
	return true;
}	

// trim函数
String.prototype.trim = function(){
    return this.replace(/(^[\s,，‘'　]*)|([\s,，'’　]*$)/g, "");
}
// 取字符串实际长度
String.prototype.Tlength = function(){var arr=this.match(/[^\x00-\xff]/ig);return this.length+(arr==null?0:arr.length);}

// 字符串左取
String.prototype.left = function(num,mode){if(!/\d+/.test(num))return(this);var str = this.substr(0,num);if(!mode) return str;var n = str.Tlength() - str.length;num = num - parseInt(n/2);return this.substr(0,num);}

// 字符串右取
String.prototype.right = function(num,mode){if(!/\d+/.test(num))return(this);var str = this.substr(this.length-num);if(!mode) return str;var n = str.Tlength() - str.length;num = num - parseInt(n/2);return this.substr(this.length-num);}

// 含有非法字符(特殊符号，包括搜狗输入法的特殊图形字符)
function havegg(elem){
  var str = "$()*+-.[]?\^{\|}~`!@#%&_=<>/\",';";
  var reg = /^(\w|[\u4E00-\u9FA5])*$/;
  for(i=0;i<elem.length;i++)
   if (str.indexOf(elem.charAt(i)) !=-1){
          return false;
	}
   if(arr=elem.match(reg)){
			return true;
	}else{
		return false;
	}
}
// 用户名中禁止包含的字符
function check_username(username,ItemShow){	
	keyword = "大师|fuck|你妈|奶奶的|傻B|法轮|不得好死|骗钱|骗子|合买|合买群|专家|群"; 
	keyarry = keyword.split("|"); 
	for(i=0;i<keyarry.length;i++){
		if(username.indexOf(keyarry[i])>-1){
			ItemShow.className="d_err";
			ItemShow.innerHTML='含有非法字符，请重新填写';
			return false;
		}
	}
	return true;
}

// 屏蔽关键字符
function check(id,divShow){
	keyword = "and|exec|insert|select|delete|update|count|chr|mid|master|truncate|char|declare"; 
	keyarry = keyword.split("|"); 
	for(i=0;i<keyarry.length;i++) {
	if(id.value.indexOf(keyarry[i])>-1){
		divShow.className="d_err";
		divShow.innerHTML='含有非法字符，请重新填写！';
		return false;
		} 
	}
	divShow.className="d_ok";
	divShow.innerHTML='ok';
	return true;
}
// 只能为数字字母下划线
function check1(inputValue,divShow){
	var reg = /^([a-zA-Z]|\d|_)*$/;
    if(!reg.test(inputValue.value)){
    	divShow.className="d_err";
    	divShow.innerHTML='密码需由 (0-9 a-z A-Z _) 组成！';
    	return false;
    }
    divShow.className="d_ok";
    divShow.innerHTML='ok';
	return true;
}


// check cardId
function checkCard(number) {
	var date, Ai;
	var verify = "10x98765432";
	var Wi = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
	var area = ["", "", "", "", "", "", "", "", "", "", "", "\u5317\u4eac", "\u5929\u6d25", "\u6cb3\u5317", "\u5c71\u897f", "\u5185\u8499\u53e4", "", "", "", "", "", "\u8fbd\u5b81", "\u5409\u6797", "\u9ed1\u9f99\u6c5f", "", "", "", "", "", "", "", "\u4e0a\u6d77", "\u6c5f\u82cf", "\u6d59\u6c5f", "\u5b89\u5fae", "\u798f\u5efa", "\u6c5f\u897f", "\u5c71\u4e1c", "", "", "", "\u6cb3\u5357", "\u6e56\u5317", "\u6e56\u5357", "\u5e7f\u4e1c", "\u5e7f\u897f", "\u6d77\u5357", "", "", "", "\u91cd\u5e86", "\u56db\u5ddd", "\u8d35\u5dde", "\u4e91\u5357", "\u897f\u85cf", "", "", "", "", "", "", "\u9655\u897f", "\u7518\u8083", "\u9752\u6d77", "\u5b81\u590f", "\u65b0\u7586", "", "", "", "", "", "\u53f0\u6e7e", "", "", "", "", "", "", "", "", "", "\u9999\u6e2f", "\u6fb3\u95e8", "", "", "", "", "", "", "", "", "\u56fd\u5916"];
	var re = number.match(/^(\d{2})\d{4}(((\d{2})(\d{2})(\d{2})(\d{3}))|((\d{4})(\d{2})(\d{2})(\d{3}[x\d])))$/i);
	if (re == null) {
		return false;
	}
	if (re[1] >= area.length || area[re[1]] == "") {
		return false;
	}
	if (re[2].length == 12) {
		Ai = number.substr(0, 17);
		date = [re[9], re[10], re[11]].join("-");
	} else {
		Ai = number.substr(0, 6) + "19" + number.substr(6);
		date = ["19" + re[4], re[5], re[6]].join("-");
	}
	if (!this.IsDate(date, "ymd")) {
		return false;
	}
	var sum = 0;
	for (var i = 0; i <= 16; i++) {
		sum += Ai.charAt(i) * Wi[i];
	}
	Ai += verify.charAt(sum % 11);
	return (number.length == 15 || number.length == 18 && number == Ai);
}
function IsDate(op, formatString) {
	formatString = formatString || "ymd";
	var m, year, month, day;
	switch (formatString) {
	  case "ymd":
		m = op.match(new RegExp("^((\\d{4})|(\\d{2}))([-./])(\\d{1,2})\\4(\\d{1,2})$"));
		if (m == null) {
			return false;
		}
		day = m[6];
		month = m[5] * 1;
		year = (m[2].length == 4) ? m[2] : GetFullYear(parseInt(m[3], 10));
		break;
	  case "dmy":
		m = op.match(new RegExp("^(\\d{1,2})([-./])(\\d{1,2})\\2((\\d{4})|(\\d{2}))$"));
		if (m == null) {
			return false;
		}
		day = m[1];
		month = m[3] * 1;
		year = (m[5].length == 4) ? m[5] : GetFullYear(parseInt(m[6], 10));
		break;
	  default:
		break;
	}
	if (!parseInt(month)) {
		return false;
	}
	month = month == 0 ? 12 : month;
	var date = new Date(year, month - 1, day);
	return (typeof (date) == "object" && year == date.getFullYear() && month == (date.getMonth() + 1) && day == date.getDate());
	function GetFullYear(y) {
		return ((y < 30 ? "20" : "19") + y) | 0;
	}
}
/*
 * function check_password(Pass1, Pass2, MinLength, MaxLength) 该函数用于检验密码
 */
function check_password(Pass1, Pass2, MinLength, MaxLength,ItemCaption,ItemShow){
	ItemCaption=(ItemCaption==null)?"密码":ItemCaption;
	if (!check_text(Pass1, MinLength, MaxLength,ItemCaption,ItemShow)){
		return false;
		}

	if (!check_text(Pass2, MinLength, MaxLength, "第二次输入的"+ItemCaption,ItemShow)){
		return false;
	}

	if (Pass1.value != Pass2.value){
		ItemShow.className="d_err";
		ItemShow.innerHTML='两次输入密码不同，请重新确认！';
		Pass1.value = "";
		Pass2.value = "";
		Pass1.focus();
		return false;
		}
	return true;
}

/*
 * ==================================================================
 * 功能：验证身份证号码是否有效 提示信息：输入身份证号不正确！ 使用：isIDno(obj) 返回：bool
 * ==================================================================
 */
function isIDno(idcard) {
    var Errors=new Array("验证通过!",
                         "身份证号码位数不对!",
                         "身份证号码出生日期超出范围或含有非法字符!",
                         "身份证号码校验错误!",
                         "身份证地区非法!"
                        );
  
    var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}
    
    var idcard,Y,JYM;
    var S,M;
    var idcard_array = new Array();
    idcard_array = idcard.split("");
    // 地区检验
    if(area[parseInt(idcard.substr(0,2))]==null) {
       // alert(Errors[4]);
        return false;
    }
    // 身份号码位数及格式检验
    switch(idcard.length){
    case 15:
        // 15位身份号码检测
        if ( (parseInt(idcard.substr(6,2))+1900) % 4 == 0 || ((parseInt(idcard.substr(6,2))+1900) % 100 == 0 && (parseInt(idcard.substr(6,2))+1900) % 4 == 0 )){
            ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;// 测试出生日期的合法性
        } else {
            ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;// 测试出生日期的合法性
        }
        if(!ereg.test(idcard)) {
           // alert(Errors[2]);
            return false;
        } else {
            return true;
        }
    break;
    case 18:
        // 18位身份号码检测
        // 出生日期的合法性检查
        // 闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
        // 平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
        if ( parseInt(idcard.substr(6,4)) % 4 == 0 || (parseInt(idcard.substr(6,4)) % 100 == 0 && parseInt(idcard.substr(6,4))%4 == 0 )){
            ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年出生日期的合法性正则表达式
        } else {
            ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;// 平年出生日期的合法性正则表达式
        }
        if(ereg.test(idcard)) { // 测试出生日期的合法性
            // 计算校验位
            S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
            + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
            + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
            + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
            + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
            + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
            + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
            + parseInt(idcard_array[7]) * 1 
            + parseInt(idcard_array[8]) * 6
            + parseInt(idcard_array[9]) * 3 ;
            Y = S % 11;
            M = "F";
            JYM = "10X98765432";
            M = JYM.substr(Y,1);// 判断校验位
            if(M !== idcard_array[17]) {
                // alert(Errors[3]);
                return false;
            }
            return true;
        } else {
           // alert(Errors[2]);
            return false;
        } 
        break;
    default:
       // alert(Errors[1]);
        return false;
        break;
    }
}
