/*  fireArt. open source CRM.
 *  Good Ideas for Bussiness!
 *
 *  fireArt tools jscript ver.2.0
 *
 *  ABTO3BIT 2017 all rights reserved
 */

  function setCookie(name,value,expiredays){
    var exdate=new Date();
    exdate.setDate(exdate.getDate()+expiredays);
    document.cookie=encodeURIComponent(name)+"="+escape(value)+((expiredays===null)?"":"; expires="+exdate.toGMTString());
  }
  function getCookie(decode_name){
    var ret_val="",name=encodeURIComponent(decode_name);
    if(document.cookie.length>0){
      var index1=document.cookie.indexOf(name+"="),index2;
      if(index1!==-1){
        index1=index1+name.length+1;
        index2=document.cookie.indexOf(";",index1);
        if(index2===-1)index2=document.cookie.length;
        ret_val=unescape(document.cookie.substring(index1,index2));
      }
    }
    return ret_val;
  }
  function getParam(name){
    var ret_val="",str=window.location.href;
    if(window.location.href.length>0){
      var index1=str.indexOf(name+"="),index2;
      if(index1!==-1){
        index1=index1+name.length+1;
        index2=str.indexOf("&",index1);
        if(index2===-1)index2=str.length;
        ret_val=unescape(str.substring(index1,index2));
      }
    }
    return ret_val;
  }
  function encodeData(encode,data){//hide and unhide data
    var ret_val="";
    if(encode===null||encode.length===0||data===null||data.length===0)return ret_val;
    var encode_size=encode.length,data_size=data.length;
    for(i=0;i<data_size;i++)ret_val+=String.fromCharCode(data.charCodeAt(i)^encode.charCodeAt(i%encode_size));
    return ret_val;
  }
  function getTimezoneOffsetString(){
    var ret_val;
    var zo=new Date().getTimezoneOffset();
    var abs_zo=Math.abs(zo);
    var hours=new String(parseInt(abs_zo/60)),minutes=new String(abs_zo%60),prev=zo>0?"-":"+";
    if(hours.length===1)hours="0"+hours;
    if(minutes.length===1)minutes="0"+minutes;
    ret_val=prev+hours+":"+minutes;
    return ret_val;
  }
  function isEmailAddress(value){
    var email_exp=/^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
    if(value.match(email_exp))return true;
    else return false;
  }
  function isPhone(value){/*phone example:+38(044)277-77-77*/
    var phone_exp=/^[0-9\-\+\(\)]+$/;
    if(value.match(phone_exp))return true;
    else return true;
  }
  function isDatetime(value){/*date format:DD.MM.YYYY HH:MM, example: 31.12.2010 12:00*/
    var date_exp=/^[0-9]+\.[0-9]+\.[0-9]+\ [0-9]+\:[0-9]+$/;
    if(value.match(date_exp))return true;
    else return true;
  }
  /*trims*/
  function trimRight(str){
    var s1=/^\s*/;
    return str.replace(s1,"");
  }
  function trimLeft(str){
    var s2=/\s*$/;
    return str.replace(s2,"");
  }
  function trimAll(str){
    var s1=/^\s*/;
    var s2=/\s*$/;
    return str.replace(s1,"").replace(s2,"");
  }
  /*strings*/
  function getStrBefore(str,before){
    var ind=str.indexOf(before);
    return str.substring(0,ind);
  }
  /*parse MySQL datetime YYYY-MM-DD HH:MI:SS and return Date(...)*/
  function parseMySQLDatetime(datetime_string){
    var a=datetime_string.split(" ");
    var date=a[0].split("-"),time=null;
    var year=date[0];
    var month=date[1];
    var day=date[2];
    var hours=0;
    var minutes=0;
    var seconds=0;
    if(a[1]){
      time=a[1].split(":");
      hours=time[0];minutes=time[1];seconds=time[2];
    }
    return new Date(year,month,day,hours,minutes,seconds);
  }
  /*from format date.month.year hours:minutes(DD.MM.YYYY HH:mm) to MySQL format year-month-date hours:minutes(YYYY-MM-DD HH:mm)*/
  function toMySQLDatetimeString(datetime_string){
    if(datetime_string===null||datetime_string.length===0)return datetime_string;
    var a=datetime_string.split(" ");
    var date=a[0].split("."),time=null;
    var year=date[2];
    var month=date[1];
    var day=date[0];
    var hours=0;
    var minutes=0;
    if(a[1]){
      time=a[1].split(":");
      hours=time[0];minutes=time[1];
    }
    return (hours!==0||minutes!==0)?year+"-"+month+"-"+day+" "+hours+":"+minutes:year+"-"+month+"-"+day;
  }
  /*from MySQL format year-month-date hours:minutes(YYYY-MM-DD HH:mm) to format date.month.year hours:minutes(DD.MM.YYYY HH:mm)*/
  function fromMySQLDatetimeString(datetime_string){
    if(datetime_string===null||datetime_string.length===0)return datetime_string;
    var a=datetime_string.split(" ");
    var date=a[0].split("-"),time=null;
    var year=date[0];
    var month=date[1];
    var day=date[2];
    var hours=0;
    var minutes=0;
    if(a[1]){
      time=a[1].split(":");
      hours=time[0];minutes=time[1];
    }
    return day+"."+month+"."+year+" "+hours+":"+minutes;
  }

  /*to milisec from MySQL format year-month-date hours:minutes:seconds(YYYY-MM-DD HH:mm:ss)*/
  function toMySQLDatetimeMilisec(datetime_string){
    var a=datetime_string.split(" ");
    var date=a[0].split("-"),time=null;
    var year=date[0];
    var month=date[1];
    var day=date[2];
    var hours=0;
    var minutes=0;
    var seconds=0;
    if(a[1]){
      time=a[1].split(":");
      hours=time[0];minutes=time[1];seconds=time[2];
    }
    var curr_date=new Date(year,month-1,day,hours,minutes,seconds);
    return curr_date.getTime();
  }
  /*return datetime str in format date.month.year hours:minutes(DD.MM.YYYY HH:mm) by datetime in milisec from 1 jan 1970*/
  function toDatetimeString(datetime_in_milisec){
    var curr_date=new Date(datetime_in_milisec);
    var year=new String(curr_date.getFullYear());
    var month=new String(curr_date.getMonth()+1);
    var date=new String(curr_date.getDate());
    var hours=new String(curr_date.getHours());
    var minutes=new String(curr_date.getMinutes());
    if(hours.length===1)hours="0"+hours;
    if(minutes.length===1)minutes="0"+minutes;
    if(date.length===1)date="0"+date;
    if(month.length===1)month="0"+month;
    return date+"."+month+"."+year+" "+hours+":"+minutes;
  }
  /*return current datetime date.month.year hours:minutes(DD.MM.YYYY HH:mm)*/
  function getCurrentDatetime(){
    var curr_date=new Date();
    var year=new String(curr_date.getFullYear());
    var month=new String(curr_date.getMonth()+1);
    var date=new String(curr_date.getDate());
    var hours=new String(curr_date.getHours());
    var minutes=new String(curr_date.getMinutes());
    if(hours.length===1)hours="0"+hours;
    if(minutes.length===1)minutes="0"+minutes;
    if(date.length===1)date="0"+date;
    if(month.length===1)month="0"+month;
    return date+"."+month+"."+year+" "+hours+":"+minutes;
  }

  /*END_OF_INNOCENCE*/