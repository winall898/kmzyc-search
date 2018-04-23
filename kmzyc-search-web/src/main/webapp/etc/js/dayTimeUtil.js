var week= new Array("日", "一", "二", "三", "四", "五", "六");
var _oddsUitl = new Object();
_oddsUitl.getDayStr = function(dt) {
  return dt.getFullYear()+"-"+ (((dt.getMonth()+1)<10)?("0"+(dt.getMonth()+1)):(dt.getMonth()+1))+"-"+(dt.getDate()<10?("0"+dt.getDate()):dt.getDate());
}
_oddsUitl.getDayWeekStr = function(dt) {
  return dt.getFullYear()+"-"+ (((dt.getMonth()+1)<10)?("0"+(dt.getMonth()+1)):(dt.getMonth()+1))+"-"+(dt.getDate()<10?("0"+dt.getDate()):dt.getDate())+" (星期"+week[dt.getDay()]+")";
}
_oddsUitl.getTimeStr = function(dt) {
  return dt.getHours()+":"+(dt.getMinutes()<10?"0":"")+dt.getMinutes();
}
_oddsUitl.getDtStr = function(dt) {
  return (dt.getMonth()+1)+"-"+dt.getDate()+" "+(dt.getHours()<10?"0":"")+dt.getHours()+":"+(dt.getMinutes()<10?"0":"")+dt.getMinutes();
}
_oddsUitl.getDateTimeStr = function(dt) {
  return _oddsUitl.getDayStr(dt) +" "+(dt.getHours()<10?"0":"")+dt.getHours()+":"+(dt.getMinutes()<10?"0":"")+dt.getMinutes();
}
_oddsUitl.getMonthDayWeekStr = function(dt) {
  return (((dt.getMonth()+1)<10)?("0"+(dt.getMonth()+1)):(dt.getMonth()+1))+"-"+(dt.getDate()<10?("0"+dt.getDate()):dt.getDate())+"["+((dt.toString()==new Date().toString())?"今":week[dt.getDay()])+"]";
}
_oddsUitl.getDate = function(str) {
  var p = str.split("-");
  return new Date(p[0], parseInt(p[1],10)-1, p[2]);
}