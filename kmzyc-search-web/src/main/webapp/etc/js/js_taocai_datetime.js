//获取中文日期格式
function d2cd(){
	return new Date().toLocaleDateString()+" 星期"+"日一二三四五六".charAt( new Date().getDay() );
}

//字节数格式化
function formatByte(num){
    return num<1<<10 ? num+" B" : (num<1<<20 ? (num/(1<<10)).toFixed(2)+" KB" : (num/(1<<20)).toFixed(2)+" MB");
}

//位数不满自动补零
function formatNo(m,n){
	return Array(n).join("0").concat(m).slice(-n);
}

//数字格式化
function formatNum(m,n){
	m = Number(m);
	var s = m.toLocaleString();
    return isNaN(n) ? s : ( n==0 ? m.toFixed(0) : s.split(".")[0]+"."+m.toFixed(n).split(".")[1] );
}

//数字格式化
function formatRMB (m,n){
	m = Number(m)
	m = m.toLocaleString().split(".")[0]+(n==0?"":"." + m.toFixed(n||2).split(".")[1]);
    return "￥" + m.replace(/^\./, '0.');
}

//中文数字转化为阿拉伯数字
function gb2num (s){
	return "零一二三四五六七八九十".indexOf(s);
}

//毫秒转换为秒
function ms2s(num){
	return (num/60000+":"+num/1000%60).replace(/\.\d+/g,"").replace(/(^|:)(\d)(?!\d)/g,"$10$2");
}

//数字转化为繁体中文
function num2big(i){
	return "零壹贰叁肆伍陆柒捌玖拾".split("")[i];
}

//阿拉伯数字转化为中文数字
function num2gb(i){
	return "零一二三四五六七八九".split("")[i];
}

//人民币反格式化
function rmb2number(rmb){
	return Number(rmb.replace(/[^\d\.]/g,""));
}

//秒转换为毫秒
function s2ms(str){
	var t = str.split(":");
	return t[0] * 60000 + t[1] * 1000;
}


//返回日期差距

function lotteryDiffDate(d1,d2){
	d1 = typeof(d1)=="string" ? new Date(d1.replace(/\-/g,"/")) : d1;
	d2 = typeof(d2)=="string" ? new Date(d2.replace(/\-/g,"/")) : d2;
	if (d1>d2) d1=[d2,d2=d1][0];

	//相差年数
	this.diffYears = function(){
		return d2.getFullYear() - d1.getFullYear();
	}

	//相差天数
	this.diffDays = function(){
		return Math.floor((d2.getTime()-d1.getTime())/86400000);
	}

	//相差详细时间
	this.diffTime = function(){
		var o = this.diffData();
		return o.year+"年"+o.month+"个月"+o.day+"天"+o.hour+"小时"+o.minute+"分"+o.second+"秒";
	}

	//相差的数据
	this.diffData = function(){
		var years = 0;
		var y = d1.getFullYear();
		while(d1<=d2){
			d1.setFullYear(++years+y);
		}
		d1.setFullYear(--years+y);
		var months = 0;
		var m = d1.getMonth();
		while(d1<=d2){
			d1.setFullYear(years+y,++months+m);
		}
		d1.setFullYear(years+y,--months+m);

		var t = d2.getTime() - d1.getTime();
		var d = Math.floor(t/86400000);
		t -= 86400000 * d;
		var h = Math.floor(t/3600000);
		t -= 3600000 * h;
		var m = Math.floor(t/60000);
		t -= 60000 * m;
		var s = Math.floor(t/1000);
		return { year:years, month:months, day:d, hour:h, minute:m, second:s};
	}
}

function reverTime(){
	this.count = 0,
	this.init = function (dayObj, timeObj, serverTime, stopTime){
	    this.offset = serverTime.getTime() - new Date().getTime();
	    this.obj = {day :dayObj,time : timeObj};
	    this.f = function(n){n='00'+n; return n.substr(n.length-2);};
	    this.timer = null;
	    this.start(stopTime);
	  }
	this.start = function(st){//倒计时开始
		window.clearInterval(this.timer);
    	var now = new Date().getTime() + this.offset;
    	this.count = Math.floor((st.getTime()-now)/1000)+1; //总秒数
    	this.run();
    	var M = this;
    	this.timer = window.setInterval(function(){M.run();}, 1000);
	}
	this.run = function (){//运行倒计时
	    var o = this.diff(this.count--);
	    if (this.obj.day && this.obj.day.innerHTML!=o.day){
	      this.obj.day.innerHTML = o.day;
	    }
	    if (o.hour!=0||o.minut!=0||o.second!=0||this.obj.time.innerHTML!="00:00:00"){
	      this.obj.time.innerHTML = this.f(o.hour) + ":" + this.f(o.minute) + ":" + this.f(o.second);
	    }
	  },
	 this.diff = function (t){//返回日期差距
	    return t>0 ? {
	      day : Math.floor(t/86400),
	      hour : Math.floor(t%86400/3600),
	      minute : Math.floor(t%3600/60),
	      second : Math.floor(t%60)
	    } : {day:0,hour:0,minute:0,second:0};
	  }

	
}

