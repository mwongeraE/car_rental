/*  fireArt. open source CRM.
 *  Good Ideas for Bussiness!
 *
 *  fireArt messages jscript ver.2.0
 *
 *  ABTO3BIT 2017 all rights reserved
 */

  /*const&var*/
  var COOKIE_LANGUAGE="LANGUAGE";
  var COOKIE_SAVE_ME="SAVE_ME";
  var COOKIE_CURRENT_USER="CURRENT_USER";
  var COOKIE_CURRENT_TOKEN="CURRENT_TOKEN";
  var COOKIE_CURRENT_API_KEY="CURRENT_API_KEY";
  var COOKIE_MAP_PROVIDER="MAP_PROVIDER";
  var COOKIE_TIMEZONE_OFFSET="TIMEZONE_OFFSET";

  var LANGUAGE_NAME_ENGLISH="ENGLISH";
  var LANGUAGE_NAME_RUSSIAN="RUSSIAN";
  var LANGUAGE_NAME_UKRAINIAN="UKRAINIAN";
  /*not used*/
  var LANGUAGE_NAME_FRENCH="FRENCH";
  var LANGUAGE_NAME_GERMAN="GERMAN";
  var LANGUAGE_NAME_ITALIAN="ITALIAN";
  var LANGUAGE_NAME_PORTUGAL="PORTUGAL";
  var LANGUAGE_NAME_SPANISH="SPANISH";
  var LANGUAGE_NAME_POLISH="POLISH";
  var LANGUAGE_NAME_SERBIAN="SERBIAN";

  var DEFAULT_LANGUAGE_NAME=LANGUAGE_NAME_ENGLISH;

  var GOOGLE_API_KEY="AIzaSyDwuGi5Hf8e2J0X5C2EzwE04O6IXdPAyfM";/*google places apikey, empty key no use*/

  var MAP_PROVIDER_GOOGLE="google";
  var MAP_PROVIDER_YANDEX="yandex";
  var MAP_PROVIDER_OSM="osm";

  var DEFAULT_MAP_PROVIDER=MAP_PROVIDER_GOOGLE;

  var MAP_PROVIDER_GOOGLE_URL="https://maps.google.com/maps/api/js?"+(GOOGLE_API_KEY!==""?"key="+GOOGLE_API_KEY+"&libraries=places&":"")+"language=";
  var MAP_PROVIDER_YANDEX_URL="https://api-maps.yandex.ru/2.1/?load=package.standard&lang=";
  var MAP_PROVIDER_OSM_JS_URL="js/leaflet.js";
  var MAP_PROVIDER_OSM_CSS_URL="css/leaflet.css";

  var DEFAULT_USERNAME="customer";
  var DEFAULT_PASSWORD="";

  var DEFAULT_ADMIN_CALLNAME="admin";

  var USER_TYPE_ADMIN=1;
  var USER_TYPE_WORKER=2;
  var USER_TYPE_CUSTOMER=3;
  var USER_TYPE_CLIENT=USER_TYPE_CUSTOMER;
  var USER_TYPE_DRIVER=USER_TYPE_WORKER;

  var DISCOUNT_IN_PERCENT=1;
  var DISCOUNT_IN_VALUE=2;
  var INCREASE_IN_PERCENT=3;
  var INCREASE_IN_VALUE=4;

  var MESSAGE_TYPE_INPUT=1;
  var MESSAGE_TYPE_OUTPUT=2;

  function getLanguageCode(name){
    if(name===LANGUAGE_NAME_ENGLISH)return "en";
    else if(name===LANGUAGE_NAME_RUSSIAN)return "ru";
    else if(name===LANGUAGE_NAME_UKRAINIAN)return "uk";
    return name;
  }
  function updateLanguageObject(language,select_language_object){
    var select_language=document.getElementById(select_language_object);
    if(language!==null){
      var size=select_language.options.length;
      if(size>0){
        for(var i=0;i<size;i++){
          if(select_language.options[i].value===language){select_language.options[i].selected=true;break;}
        }
      }
    }
    else{
      select_language.selectedIndex=-1;
      language=DEFAULT_LANGUAGE_NAME;
    }
    return language;
  }
  function updateCookieLanguage(select_language_object){
    var language=DEFAULT_LANGUAGE_NAME;
    var select_language=document.getElementById(select_language_object);
    var index=select_language.selectedIndex;
    if(index>=0)language=select_language.options[index].value;
    setCookie(COOKIE_LANGUAGE,language,365);
    return language;
  }
  function getMessage(language,message_code){
    var ret_val="Unknown";
    if(language===LANGUAGE_NAME_ENGLISH){
      switch(message_code){
        /*runtime errors*/
        case -2001:ret_val="Java error java.lang.NullPointerException";break;
        case -2000:ret_val="Java error java.lang.Throwable";break;
        case -500:ret_val="Error 500. Internal Server Error";break;
        case -404:ret_val="Error 404. Page not found";break;

        case -1000:ret_val="Service template not found";break;
        case -1001:ret_val="Database blacklist blocking";break;
        case -1002:ret_val="Database connection failed";break;
        case -1003:ret_val="Content data failed";break;
        case -1004:ret_val="Cookie data failed";break;
        case -1005:ret_val="Java script failed";break;
        case -1006:ret_val="SQL query failed";break;
        case -1007:ret_val="SQL message during execution";break;

        case -200:ret_val="Runtime Error jscript object failed";break;
        case -100:ret_val="Runtime Error loading data";break;
        case -12:ret_val="Payment failed";break;
        case -11:ret_val="Update data failed";break;
        case -10:ret_val="Data not found";break;

        case -5:ret_val="recipient not exist";break;
        case -4:ret_val="operation will not be completed";break;
        case -3:ret_val="not available privileges";break;
        case -2:ret_val="username already exist";break;
        case -1:ret_val="username or password do not exist";break;

        case 0:ret_val="success";break;
        /*User Interface*/

        /*usertype*/
        case 1:ret_val="Administrator";break;
        case 2:ret_val="Driver";break;
        case 3:ret_val="Client";break;

        /*discount_type*/
        case 11:ret_val="Discount in percent";break;
        case 12:ret_val="Discount in value add";break;
        case 13:ret_val="Increase in percent";break;
        case 14:ret_val="Increase in value";break;

        /*message_type*/
        case 21:ret_val="Inbox";break;
        case 22:ret_val="Outbox";break;

        /*times*/
        case 101:ret_val="just moment";break;
        case 102:ret_val="min. ago";break;
        case 103:ret_val="hr. ago";break;
        case 104:ret_val="d. ago";break;
        case 105:ret_val="min.";break;
        case 106:ret_val="hr.";break;

        /*distance*/
        case 111:ret_val="km";break;
        case 112:ret_val="km/h";break;
        case 113:ret_val="m";break;

        /*header*/
        case 1000:ret_val="fireArt";break;
        case 1001:ret_val="English";break;
        case 1002:ret_val="Russian";break;
        case 1003:ret_val="Ukrainian";break;
        case 1010:ret_val="open source CRM";break;

        /*Forms*/

        /*register*/
        case 1011:ret_val="Username";break;
        case 1012:ret_val="Password";break;
        case 1013:ret_val="First name";break;
        case 1014:ret_val="Last name";break;
        case 1015:ret_val="Email address";break;
        case 1016:ret_val="Phone number";break;
        case 1017:ret_val="Special code";break;
        case 1018:ret_val="Register";break;
        case 1019:ret_val="Save me for next visit";break;
        case 1020:ret_val="Perform";break;/*button*/
        case 1021:ret_val="Perform registration";break;
        case 1022:ret_val="Already registered?";break;
        case 1023:ret_val="Login";break;
        case 1024:ret_val="Personal login";break;
        case 1025:ret_val="API key";break;

        /*login*/
        case 1051:ret_val="Login";break;
        case 1052:ret_val="Perform login";break;
        case 1053:ret_val="Not yet registered?";break;
        case 1054:ret_val="Register";break;
        case 1055:ret_val="Personal registration";break;

        /*start*/
        case 1061:ret_val="Your Taxi service";break;
        case 1062:ret_val="Book a trip";break;
        case 1063:ret_val="Book a trip now";break;
        case 1064:ret_val="Book transport";break;
        case 1065:ret_val="Book transport now";break;
        case 1066:ret_val="WELCOME TO TAXI SERVICE!";break;
        case 1067:ret_val="Transportation solutions from economic taxi booking, VIP car, classy taxi,\nshared car, bet. Cities service to rental car service";break;
        case 1068:ret_val="CAR RENTAL";break;
        case 1069:ret_val="Car rental service for drive transport";break;
        case 1070:ret_val="DOWNLOAD the Mobile App for TAXI SERVICE!";break;

        /*order*/
        case 1081:ret_val="From";break;
        case 1082:ret_val="To";break;
        case 1083:ret_val="Fast as service can";break;
        case 1084:ret_val="Order";break;
        case 1085:ret_val="Order on time";break;
        case 1086:ret_val="Service";break;
        case 1087:ret_val="Calculate";break;
        case 1088:ret_val="Calculate the cost";break;
        case 1089:ret_val="Send";break;
        case 1090:ret_val="Send an order";break;
        case 1091:ret_val="Route length";break;
        case 1092:ret_val="Route time";break;
        case 1093:ret_val="Order amount";break;
        case 1094:ret_val="Route";break;
        case 1095:ret_val="Additional conditions";break;
        case 1096:ret_val="User discount";break;
        case 1097:ret_val="Call phone";break;

        /*transport_order*/
        case 1101:ret_val="Order transport";break;
        case 1102:ret_val="Order hourly";break;

        /*sent_order*/
        case 1111:ret_val="Sent order";break;
        case 1112:ret_val="Cancel";break;
        case 1113:ret_val="Order cancelation";break;
        case 1114:ret_val="Complete the order";break;
        case 1115:ret_val="Choose a car...";break;
        case 1116:ret_val="Map";break;
        case 1117:ret_val="Map activity";break;
        case 1118:ret_val="Reset a status";break;
        case 1119:ret_val="Choose order status...";break;

        /*map_activity*/
        case 1121:ret_val="Report on trips";break;
        case 1122:ret_val="No report";break;

        /*message*/
        case 1131:ret_val="Message text";break;
        case 1132:ret_val="Send message";break;
        case 1133:ret_val="To";break;
        case 1134:ret_val="Recipient of the message";break;
        case 1135:ret_val="Can using callname, email or phone";break;

        /*profile*/
        case 1140:ret_val="User profile";break;
        case 1141:ret_val="New picture";break;
        case 1142:ret_val="Save user";break;
        case 1143:ret_val="Change password";break;
        case 1144:ret_val="Save transport";break;
        case 1145:ret_val="Old password";break;
        case 1146:ret_val="New password";break;
        case 1147:ret_val="Callname";break;
        case 1148:ret_val="Transport name";break;
        case 1149:ret_val="License plate";break;
        case 1150:ret_val="Transport color";break;
        case 1151:ret_val="Choose transport color...";break;

        /*cabinet*/
        case 1153:ret_val="Sent orders";break;
        case 1154:ret_val="Report";break;
        case 1155:ret_val="Messages";break;
        case 1156:ret_val="User profile";break;
        case 1157:ret_val="Remove order";break;
        case 1158:ret_val="Remove complete";break;
        case 1159:ret_val="Payment order";break;
        case 1160:ret_val="Payment completed";break;

        /*profile*/
        case 1171:ret_val="GPS sensor";break;
        case 1172:ret_val="Choose GPS sensor...";break;

        /*map*/
        case 1201:ret_val="Map activity";break;
        case 1202:ret_val="No activity";break;
        case 1203:ret_val="Show transport";break;
        case 1204:ret_val="Show transport on map";break;
        case 1205:ret_val="Watching";break;

        /*error*/
        case 1401:ret_val="You must create an account";break;
        case 1402:ret_val="User is banned";break;
        case 1403:ret_val="Insufficient privileges";break;

        /*menu*/
        case 1501:ret_val="Login";break;
        case 1502:ret_val="Register";break;
        case 1503:ret_val="Cabinet";break;
        case 1504:ret_val="Exit";break;
        case 1505:ret_val="Home";break;
        case 1506:ret_val="Services";break;

        /*footer*/
        case 2001:ret_val="Bussiness Platforms";break;
        case 2002:ret_val="Taxi Booking service";break;
        case 2003:ret_val="Trip and Transportation";break;
        case 2004:ret_val="Products Delivery service";break;
        case 2005:ret_val="Booking service by Location";break;
        case 2006:ret_val="Find Accommodations service";break;
        case 2007:ret_val="How to Join";break;
        case 2008:ret_val="Join is Free. Get API key Easy";break;
        case 2009:ret_val="Contacts";break;
        case 2010:ret_val="Ukraine, Kiev";break;

        case 2021:ret_val="Main menu";break;
        case 2022:ret_val="About us";break;
        case 2023:ret_val="Get social";break;
        case 2024:ret_val="Get us with Social";break;

        /*discount_type*/
        case 3001:ret_val="Discount in percent";break;
        case 3002:ret_val="Discount in value";break;
        case 3003:ret_val="Add in percent";break;
        case 3004:ret_val="Additional value";break;

        /*admin panel*/
        case 9000:ret_val="Search";break;
        case 9001:ret_val="Select Menu Object";break;
        case 9002:ret_val="Search data by fields";break;
        case 9003:ret_val="Price from";break;
        case 9004:ret_val="Price to";break;
        case 10000:ret_val="Admin Panel";break;
        case 10001:ret_val="Users and Features";break;
        case 10002:ret_val="User";break;
        case 10003:ret_val="Transport";break;
        case 10004:ret_val="Transport type part";break;
        case 10005:ret_val="Sensor";break;
        case 10006:ret_val="Discount";break;
        case 10007:ret_val="Tax";break;
        case 10008:ret_val="Message";break;
        case 10009:ret_val="Product review";break;
        case 10010:ret_val="Transport review";break;
        case 10011:ret_val="Track";break;
        case 10012:ret_val="Track part";break;
        case 10013:ret_val="Sensor circle";break;
        case 10014:ret_val="Sensor group";break;
        case 10015:ret_val="Sensor place";break;
        case 10016:ret_val="User review";break;

        case 10101:ret_val="Types and Attr";break;
        case 10102:ret_val="Attr";break;
        case 10103:ret_val="Attr part";break;
        case 10104:ret_val="Color";break;
        case 10105:ret_val="Currency";break;
        case 10106:ret_val="Language";break;
        case 10107:ret_val="Delivery type";break;
        case 10108:ret_val="Track type";break;
        case 10109:ret_val="Order status";break;
        case 10110:ret_val="Product param";break;
        case 10111:ret_val="Product type";break;
        case 10112:ret_val="Transport type";break;
        case 10113:ret_val="Pickup status";break;

        case 10201:ret_val="Trade and Services";break;
        case 10202:ret_val="Stock";break;
        case 10203:ret_val="Stock invoice";break;
        case 10204:ret_val="Stock invoice part";break;
        case 10205:ret_val="Store";break;
        case 10206:ret_val="Store part";break;
        case 10207:ret_val="Order";break;
        case 10208:ret_val="Order part";break;
        case 10209:ret_val="Manufacture";break;
        case 10210:ret_val="Product";break;
        case 10211:ret_val="Product param part";break;
        case 10212:ret_val="Product type part";break;
        case 10213:ret_val="Purchase";break;
        case 10214:ret_val="Payment";break;
        case 10215:ret_val="Prepaid card";break;
        case 10216:ret_val="Order product";break;
        case 10217:ret_val="Order user";break;
        case 10218:ret_val="Order job";break;

        case 10301:ret_val="Statistics";break;
        case 10302:ret_val="Audit";break;
        case 10303:ret_val="Settings";break;
        case 10304:ret_val="Project";break;

        /*admin table*/
        case 20000:ret_val="Download";break;
        case 20001:ret_val="Remove";break;
        case 20002:ret_val="Update";break;
        case 20003:ret_val="Attribute";break;
        case 20004:ret_val="Color";break;
        case 20005:ret_val="Name";break;
        case 20006:ret_val="Value";break;
        case 20007:ret_val="Code";break;
        case 20008:ret_val="Activity";break;
        case 20009:ret_val="Description";break;
        case 20010:ret_val="ObjectName";break;
        case 20011:ret_val="Language";break;
        case 20012:ret_val="Last update";break;
        case 20013:ret_val="Attr";break;
        case 20014:ret_val="Picture";break;
        case 20015:ret_val="Transport";break;
        case 20016:ret_val="Transport type";break;
        case 20017:ret_val="Username";break;
        case 20018:ret_val="Type";break;
        case 20019:ret_val="Discount";break;
        case 20020:ret_val="Firstname";break;
        case 20021:ret_val="Lastname";break;
        case 20022:ret_val="Callname";break;
        case 20023:ret_val="Email";break;
        case 20024:ret_val="Phone";break;
        case 20025:ret_val="Create date";break;
        case 20026:ret_val="Rate";break;
        case 20027:ret_val="License plate";break;
        case 20028:ret_val="Serial number";break;
        case 20029:ret_val="Device name";break;
        case 20030:ret_val="Discount type";break;
        case 20031:ret_val="Start date";break;
        case 20032:ret_val="Finish date";break;
        case 20033:ret_val="Product type";break;
        case 20034:ret_val="Sender(Id)";break;
        case 20035:ret_val="Receiver(Id)";break;
        case 20036:ret_val="Message";break;
        case 20037:ret_val="Lat";break;
        case 20038:ret_val="Lng";break;
        case 20039:ret_val="Time";break;
        case 20040:ret_val="Alt";break;
        case 20041:ret_val="Accu";break;
        case 20042:ret_val="Bear";break;
        case 20043:ret_val="Speed";break;
        case 20044:ret_val="Sat";break;
        case 20045:ret_val="Bat";break;
        case 20046:ret_val="Timezone";break;
        case 20047:ret_val="Product";break;
        case 20048:ret_val="Store";break;
        case 20049:ret_val="Count";break;
        case 20050:ret_val="Invoice code";break;
        case 20051:ret_val="Invoice date";break;
        case 20052:ret_val="Total price";break;
        case 20053:ret_val="Total tax";break;
        case 20054:ret_val="Supplier";break;
        case 20055:ret_val="Delivery code";break;
        case 20056:ret_val="Delivery date";break;
        case 20057:ret_val="Delivery price";break;
        case 20058:ret_val="Payment date";break;
        case 20059:ret_val="Payment info";break;
        case 20060:ret_val="Payment amount";break;
        case 20061:ret_val="Tax";break;
        case 20062:ret_val="Price";break;
        case 20063:ret_val="Latitude";break;
        case 20064:ret_val="Longitude";break;
        case 20065:ret_val="Phone1";break;
        case 20066:ret_val="Phone2";break;
        case 20067:ret_val="Address";break;
        case 20068:ret_val="City";break;
        case 20069:ret_val="Postcode";break;
        case 20070:ret_val="Status";break;
        case 20071:ret_val="Distance";break;
        case 20072:ret_val="Duration";break;
        case 20073:ret_val="A.Latitude";break;
        case 20074:ret_val="A.Longitude";break;
        case 20075:ret_val="A.Address";break;
        case 20076:ret_val="B.Latitude";break;
        case 20077:ret_val="B.Longitude";break;
        case 20078:ret_val="B.Address";break;
        case 20079:ret_val="Delivery type";break;
        case 20080:ret_val="Reserved date";break;
        case 20081:ret_val="Reserved hours";break;
        case 20082:ret_val="Purchase";break;
        case 20083:ret_val="Product discount";break;
        case 20084:ret_val="Product param";break;
        case 20085:ret_val="Product count";break;
        case 20086:ret_val="Manufacture";break;
        case 20087:ret_val="Stock count";break;
        case 20088:ret_val="Order date";break;
        case 20089:ret_val="Order info";break;
        case 20090:ret_val="Amount";break;
        case 20091:ret_val="Currency";break;
        case 20092:ret_val="Sensor(Id)";break;
        case 20093:ret_val="User(Id)";break;
        case 20094:ret_val="Product(Id)";break;
        case 20095:ret_val="Transport(Id)";break;
        case 20096:ret_val="Delivery(Id)";break;
        case 20097:ret_val="SensorA[Callname](Id)";break;
        case 20098:ret_val="SensorB[Callname](Id)";break;
        case 20099:ret_val="Prepaid amount";break;
        case 20100:ret_val="Purchase Id";break;
        case 20101:ret_val="User Id";break;
        case 20102:ret_val="Delivery Id";break;
        case 20103:ret_val="Order Id";break;
        case 20104:ret_val="Product Id";break;
        case 20105:ret_val="Param Id";break;
        case 20106:ret_val="Store Id";break;
        case 20107:ret_val="Stock invoice Id";break;
        case 20108:ret_val="Object Id";break;
        case 20109:ret_val="Parent Id";break;
        case 20110:ret_val="Type Id";break;
        case 20111:ret_val="Transport Id";break;
        case 20112:ret_val="Sensor Id";break;
        case 20113:ret_val="Transaction id";break;
        case 20114:ret_val="Prepaid code";break;
        case 20115:ret_val="Review user(Id)";break;
        case 20116:ret_val="API key";break;
        case 20117:ret_val="Schema name";break;
        case 20118:ret_val="Amount paid";break;
        case 20119:ret_val="IP address";break;

        /*dashboard*/
        case 20501:ret_val="Last events";break;
        case 20502:ret_val="New messages";break;
        case 20503:ret_val="Contacts";break;
        case 20504:ret_val="User profile";break;
        case 20505:ret_val="Update profile";break;
        case 20506:ret_val="More info";break;

        case 20601:ret_val="Summary";break;
        case 20602:ret_val="Profile";break;
        case 20603:ret_val="Audit";break;
        case 20604:ret_val="Messages";break;
        case 20605:ret_val="Booking";break;
        case 20606:ret_val="Sales";break;
        case 20607:ret_val="Orders";break;
        case 20608:ret_val="Transports";break;
        case 20609:ret_val="Map";break;
        case 20610:ret_val="Signin";break;
        case 20611:ret_val="Signup";break;

        case 20701:ret_val="Summary information";break;
        case 20702:ret_val="User profile data";break;
        case 20703:ret_val="Audit of actions";break;
        case 20704:ret_val="User chat messages";break;
        case 20705:ret_val="Booking now";break;
        case 20706:ret_val="Sales list";break;
        case 20707:ret_val="Orders history";break;
        case 20708:ret_val="Transports for delivery";break;
        case 20709:ret_val="Map of movements";break;
        case 20710:ret_val="Login to backend";break;
        case 20711:ret_val="Register a new user";break;

        /*stat*/
        case 30001:ret_val="Audit";break;
        case 30002:ret_val="Drivers";break;
        case 30003:ret_val="Clients";break;
        case 30004:ret_val="Drivers online";break;
        case 30005:ret_val="Clients online";break;
        case 30006:ret_val="Transports";break;
        case 30007:ret_val="Manufactures";break;
        case 30008:ret_val="Products";break;
        case 30009:ret_val="Stores";break;
        case 30010:ret_val="Orders";break;
        case 30011:ret_val="Orders completed";break;
        case 30012:ret_val="Orders cancelled";break;
        case 30013:ret_val="Orders pending";break;
        case 30014:ret_val="Orders today";break;
        case 30015:ret_val="Orders yesterday";break;
        case 30016:ret_val="Orders week";break;
        case 30017:ret_val="Orders month";break;
        case 30018:ret_val="Orders today completed";break;
        case 30019:ret_val="Orders today cancelled";break;
        case 30020:ret_val="Orders today panding";break;
        case 30021:ret_val="Orders yesterday completed";break;
        case 30022:ret_val="Orders yesterday cancelled";break;
        case 30023:ret_val="Orders yesterday pending";break;
        case 30024:ret_val="Orders total price";break;
        case 30025:ret_val="Purchase amount";break;
        case 30026:ret_val="Payment amount";break;
        case 30027:ret_val="Driver orders";break;
        case 30028:ret_val="Driver orders completed";break;
        case 30029:ret_val="Driver orders pending";break;
        case 30030:ret_val="Driver orders total price";break;
        case 30031:ret_val="Driver purchase amount";break;
        case 30032:ret_val="Input messages";break;
        case 30033:ret_val="Output messages";break;
        case 30034:ret_val="Audit messages";break;

      }
    }
    else if(language===LANGUAGE_NAME_RUSSIAN){
      switch(message_code){
        /*runtime errors*/
        case -2001:ret_val="Ошибка Java java.lang.NullPointerException";break;
        case -2000:ret_val="Ошибка Java java.lang.Throwable";break;
        case -500:ret_val="Ошибка 500. Внутренняя ошибка сервера";break;
        case -404:ret_val="Ошибка 404. Страница не существует";break;

        case -1000:ret_val="Шаблон службы не создан";break;
        case -1001:ret_val="Блокирование черного списка";break;
        case -1002:ret_val="Соединение с базой данных не установлено";break;
        case -1003:ret_val="Данные контента не получены";break;
        case -1004:ret_val="Данные куки не получены";break;
        case -1005:ret_val="Скрипт Java не выполнен";break;
        case -1006:ret_val="SQL запрос не выполнен";break;
        case -1007:ret_val="Получено сообщение во время выполнения SQL запроса";break;

        case -200:ret_val="Ошибка выполнения обработки объектов jscript";break;
        case -100:ret_val="Ошибка выполнения загрузки данных";break;
        case -12:ret_val="Оплата не выполнена";break;
        case -11:ret_val="Изменение данных не выполнено";break;
        case -10:ret_val="Нет данных";break;

        case -5:ret_val="получатели не найдены";break;
        case -4:ret_val="операция не может быть выполнена";break;
        case -3:ret_val="нет привилегий для доступа";break;
        case -2:ret_val="пользователь с таким именем уже есть";break;
        case -1:ret_val="пользователь или пароль не найден";break;

        case 0:ret_val="успешно";break;
        /*User Interface*/

        /*usertype*/
        case 1:ret_val="Администратор";break;
        case 2:ret_val="Водитель";break;
        case 3:ret_val="Клиент";break;

        /*discount_type*/
        case 11:ret_val="Скидка в процентах";break;
        case 12:ret_val="Скидка в сумме";break;
        case 13:ret_val="Добавление в процентах";break;
        case 14:ret_val="Добавление в сумме";break;

        /*message_type*/
        case 21:ret_val="Входящие";break;
        case 22:ret_val="Исходящие";break;

        /*times*/
        case 101:ret_val="только что";break;
        case 102:ret_val="мин. назад";break;
        case 103:ret_val="час. назад";break;
        case 104:ret_val="дн. назад";break;
        case 105:ret_val="мин.";break;
        case 106:ret_val="час.";break;

        /*distance*/
        case 111:ret_val="км";break;
        case 112:ret_val="км/час";break;
        case 113:ret_val="м";break;

        /*header*/
        case 1000:ret_val="fireArt";break;
        case 1001:ret_val="Английский";break;
        case 1002:ret_val="Русский";break;
        case 1003:ret_val="Украинский";break;
        case 1010:ret_val="open source CRM";break;

        /*Forms*/

        /*register*/
        case 1011:ret_val="Пользователь";break;
        case 1012:ret_val="Пароль";break;
        case 1013:ret_val="Фамилия";break;
        case 1014:ret_val="Имя";break;
        case 1015:ret_val="Электронная почта";break;
        case 1016:ret_val="Телефон";break;
        case 1017:ret_val="Специальный код";break;
        case 1018:ret_val="Регистрация";break;
        case 1019:ret_val="Сохранить меня";break;
        case 1020:ret_val="Выполнить";break;
        case 1021:ret_val="Выполнить регистрацию";break;
        case 1022:ret_val="Уже зарегистрирован?";break;
        case 1023:ret_val="Войти";break;
        case 1024:ret_val="Войти как пользователь";break;
        case 1025:ret_val="API ключ";break;

        /*login*/
        case 1051:ret_val="Вход";break;
        case 1052:ret_val="Выполнить вход";break;
        case 1053:ret_val="Еще не зарегистрирован?";break;
        case 1054:ret_val="Регистрация";break;
        case 1055:ret_val="Личная регистрация";break;

        /*start*/
        case 1061:ret_val="Ваша служба такси";break;
        case 1062:ret_val="Заказать";break;
        case 1063:ret_val="Заказать такси";break;
        case 1064:ret_val="Резервирование";break;
        case 1065:ret_val="Резервирование транпорта";break;
        case 1066:ret_val="ДОБРО ПОЖАЛОВАТЬ В СЛУЖБУ ТАКСИ!";break;
        case 1067:ret_val="Служба для экономных такси поездок, VIP заказов, классическое такси,\nсовместных поездок, междугородних поездок и аренды автомобилей";break;
        case 1068:ret_val="АРЕНДА АВТОМОБИЛЯ";break;
        case 1069:ret_val="Аренда автомобиля для управления транспортом";break;
        case 1070:ret_val="ЗАГРУЗИТЬ мобильные приложения для СЛУЖБЫ ТАКСИ";break;

        /*order*/
        case 1081:ret_val="Откуда";break;
        case 1082:ret_val="Куда";break;
        case 1083:ret_val="Как можно быстрее";break;
        case 1084:ret_val="Заказ";break;
        case 1085:ret_val="Заказ на время";break;
        case 1086:ret_val="Услуга";break;
        case 1087:ret_val="Рассчитать";break;
        case 1088:ret_val="Рассчитать стоимость";break;
        case 1089:ret_val="Отправить";break;
        case 1090:ret_val="Отправить заказ";break;
        case 1091:ret_val="Длина маршрута";break;
        case 1092:ret_val="Время в пути";break;
        case 1093:ret_val="Стоимость заказа";break;
        case 1094:ret_val="Маршрут";break;
        case 1095:ret_val="Дополнительные условия";break;
        case 1096:ret_val="Скидка пользователя";break;
        case 1097:ret_val="Контактный телефон";break;

        /*transport_order*/
        case 1101:ret_val="Заказ транспорта";break;
        case 1102:ret_val="Заказ часов";break;

        /*sent_order*/
        case 1111:ret_val="Отправленный заказ";break;
        case 1112:ret_val="Отменить";break;
        case 1113:ret_val="Отменить заказ";break;
        case 1114:ret_val="Выполнить заказ";break;
        case 1115:ret_val="Выбрать транспорт...";break;
        case 1116:ret_val="Карта";break;
        case 1117:ret_val="Карта передвижения";break;
        case 1118:ret_val="Установить статус";break;
        case 1119:ret_val="Выбрать статус заказа...";break;

        /*report*/
        case 1121:ret_val="Отчет по поездкам";break;
        case 1122:ret_val="Нет отчета";break;

        /*message*/
        case 1131:ret_val="Текст сообщения";break;
        case 1132:ret_val="Отправить сообщение";break;
        case 1133:ret_val="Кому";break;
        case 1134:ret_val="Получатель сообщения";break;
        case 1135:ret_val="Использовать позывной, электронную почту или телефон";break;

        /*profile*/
        case 1140:ret_val="Профиль пользователя";break;
        case 1141:ret_val="Новое изображение";break;
        case 1142:ret_val="Сохранить пользователя";break;
        case 1143:ret_val="Изменить пароль";break;
        case 1144:ret_val="Сохранить транспорт";break;
        case 1145:ret_val="Старый пароль";break;
        case 1146:ret_val="Новый пароль";break;
        case 1147:ret_val="Позывной";break;
        case 1148:ret_val="Марка транспорта";break;
        case 1149:ret_val="Номерной знак";break;
        case 1150:ret_val="Цвет транспорта";break;
        case 1151:ret_val="Выбрать цвет транспорта...";break;

        /*cabinet*/
        case 1153:ret_val="Отправленные заказы";break;
        case 1154:ret_val="Отчет";break;
        case 1155:ret_val="Сообщения";break;
        case 1156:ret_val="Профиль пользователя";break;
        case 1157:ret_val="Удалить заказ";break;
        case 1158:ret_val="Удалить выполнение";break;
        case 1159:ret_val="Оплата заказа";break;
        case 1160:ret_val="Оплата выполнена";break;

        /*profile*/
        case 1171:ret_val="GPS сенсор";break;
        case 1172:ret_val="Выбрать GPS сенсор...";break;

        /*map*/
        case 1201:ret_val="Карта передвижения";break;
        case 1202:ret_val="Нет передвижения";break;
        case 1203:ret_val="Показать транспорт";break;
        case 1204:ret_val="Показать транспорт на карте";break;
        case 1205:ret_val="Наблюдение";break;

        /*error*/
        case 1401:ret_val="Необходимо зарегистрироваться";break;
        case 1402:ret_val="Пользователь заблокирован";break;
        case 1403:ret_val="Недостаточные привилегии";break;

        /*menu*/
        case 1501:ret_val="Вход";break;
        case 1502:ret_val="Регистрация";break;
        case 1503:ret_val="Кабинет";break;
        case 1504:ret_val="Выход";break;
        case 1505:ret_val="Начальная";break;
        case 1506:ret_val="Службы";break;

        /*footer*/
        case 2001:ret_val="Направления бизнеса";break;
        case 2002:ret_val="Служба заказа такси";break;
        case 2003:ret_val="Поездки и Перевозки";break;
        case 2004:ret_val="Служба Доставки товаров";break;
        case 2005:ret_val="Заказ услуги по местонахождению";break;
        case 2006:ret_val="Поиск услуги проживания";break;
        case 2007:ret_val="Условия подключения";break;
        case 2008:ret_val="Подключение бесплатно. Получи свой API ключ Легко";break;
        case 2009:ret_val="Контакты";break;
        case 2010:ret_val="Украина, Киев";break;

        case 2021:ret_val="Главное меню";break;
        case 2022:ret_val="Про нас";break;
        case 2023:ret_val="Социальные сети";break;
        case 2024:ret_val="Мы в социальных сетях";break;

        /*discount_type*/
        case 3001:ret_val="Скидка в процентах";break;
        case 3002:ret_val="Скидка в сумме";break;
        case 3003:ret_val="Добавить в процентах";break;
        case 3004:ret_val="Добавить к сумме";break;

        /*admin panel*/
        case 9000:ret_val="Поиск";break;
        case 9001:ret_val="Необходимо выбрать пункт меню";break;
        case 9002:ret_val="Поиск данных по полям";break;
        case 9003:ret_val="Цена от";break;
        case 9004:ret_val="Цена до";break;
        case 10000:ret_val="Админ панель";break;
        case 10001:ret_val="Пользователи и функции";break;
        case 10002:ret_val="Пользователь";break;
        case 10003:ret_val="Транспорт";break;
        case 10004:ret_val="Транспорт типа";break;
        case 10005:ret_val="Сенсор";break;
        case 10006:ret_val="Скидка";break;
        case 10007:ret_val="Налог";break;
        case 10008:ret_val="Сообщение";break;
        case 10009:ret_val="Оценка продукта";break;
        case 10010:ret_val="Оценка транспорта";break;
        case 10011:ret_val="Точка местоположения";break;
        case 10012:ret_val="Данные точки";break;
        case 10013:ret_val="Сенсор рядом";break;
        case 10014:ret_val="Сенсор группа";break;
        case 10015:ret_val="Сенсор место";break;
        case 10016:ret_val="Оценка пользователя";break;

        case 10101:ret_val="Типы и атрибуты";break;
        case 10102:ret_val="Атрибут";break;
        case 10103:ret_val="Атрибут описание";break;
        case 10104:ret_val="Цвет";break;
        case 10105:ret_val="Валюта";break;
        case 10106:ret_val="Язык";break;
        case 10107:ret_val="Доставка тип";break;
        case 10108:ret_val="Точка тип";break;
        case 10109:ret_val="Заказ статус";break;
        case 10110:ret_val="Продукт описание";break;
        case 10111:ret_val="Продукт тип";break;
        case 10112:ret_val="Транспорт тип";break;
        case 10113:ret_val="Клиент статус";break;

        case 10201:ret_val="Торговля и Службы";break;
        case 10202:ret_val="Наличие";break;
        case 10203:ret_val="Документ по наличию";break;
        case 10204:ret_val="Документ данные";break;
        case 10205:ret_val="Магазин";break;
        case 10206:ret_val="Магазин описание";break;
        case 10207:ret_val="Заказ";break;
        case 10208:ret_val="Заказ данные";break;
        case 10209:ret_val="Производитель";break;
        case 10210:ret_val="Продукт";break;
        case 10211:ret_val="Продукт описание";break;
        case 10212:ret_val="Продукт тип";break;
        case 10213:ret_val="Продажа";break;
        case 10214:ret_val="Оплата";break;
        case 10215:ret_val="Карта предоплаты";break;
        case 10216:ret_val="Заказ продукт";break;
        case 10217:ret_val="Заказ клиент";break;
        case 10218:ret_val="Заказ работы";break;

        case 10301:ret_val="Статистика";break;
        case 10302:ret_val="Аудит";break;
        case 10303:ret_val="Установки";break;
        case 10304:ret_val="Проект";break;

        /*admin table*/
        case 20000:ret_val="Загрузить";break;
        case 20001:ret_val="Удалить";break;
        case 20002:ret_val="Обновить";break;
        case 20003:ret_val="Атрибут";break;
        case 20004:ret_val="Цвет";break;
        case 20005:ret_val="Наименование";break;
        case 20006:ret_val="Значение";break;
        case 20007:ret_val="Код";break;
        case 20008:ret_val="Активный";break;
        case 20009:ret_val="Описание";break;
        case 20010:ret_val="Имя объекта";break;
        case 20011:ret_val="Язык";break;
        case 20012:ret_val="Обновление";break;
        case 20013:ret_val="Имя атрибута";break;
        case 20014:ret_val="Изображение";break;
        case 20015:ret_val="Транспорт";break;
        case 20016:ret_val="Тип транспорта";break;
        case 20017:ret_val="Пользователь";break;
        case 20018:ret_val="Тип";break;
        case 20019:ret_val="Скидка";break;
        case 20020:ret_val="Имя";break;
        case 20021:ret_val="Фамилия";break;
        case 20022:ret_val="Позывной";break;
        case 20023:ret_val="Почта";break;
        case 20024:ret_val="Телефон";break;
        case 20025:ret_val="Создание";break;
        case 20026:ret_val="Рейтинг";break;
        case 20027:ret_val="Номерной знак";break;
        case 20028:ret_val="Серийный номер";break;
        case 20029:ret_val="Устройство";break;
        case 20030:ret_val="Тип скидки";break;
        case 20031:ret_val="Начало";break;
        case 20032:ret_val="Окончание";break;
        case 20033:ret_val="Тип продукции";break;
        case 20034:ret_val="Отправитель(Id)";break;
        case 20035:ret_val="Получатель(Id)";break;
        case 20036:ret_val="Сообщение";break;
        case 20037:ret_val="Широта";break;
        case 20038:ret_val="Долгота";break;
        case 20039:ret_val="Время";break;
        case 20040:ret_val="Высота";break;
        case 20041:ret_val="Точность";break;
        case 20042:ret_val="Отклонение";break;
        case 20043:ret_val="Скорость";break;
        case 20044:ret_val="Спутники";break;
        case 20045:ret_val="Батарея";break;
        case 20046:ret_val="Временная зона";break;
        case 20047:ret_val="Продукт";break;
        case 20048:ret_val="Магазин";break;
        case 20049:ret_val="Количество";break;
        case 20050:ret_val="Входящий док.";break;
        case 20051:ret_val="Входящая дата";break;
        case 20052:ret_val="Полная цена";break;
        case 20053:ret_val="Полный налог";break;
        case 20054:ret_val="Поставщик";break;
        case 20055:ret_val="Код доставки";break;
        case 20056:ret_val="Дата доставки";break;
        case 20057:ret_val="Цена доставки";break;
        case 20058:ret_val="Дата оплаты";break;
        case 20059:ret_val="Инфо оплаты";break;
        case 20060:ret_val="Всего оплаты";break;
        case 20061:ret_val="Налог";break;
        case 20062:ret_val="Цена";break;
        case 20063:ret_val="Широта";break;
        case 20064:ret_val="Долгота";break;
        case 20065:ret_val="Телефон 1";break;
        case 20066:ret_val="Телефон 2";break;
        case 20067:ret_val="Адрес";break;
        case 20068:ret_val="Город";break;
        case 20069:ret_val="Почтовый код";break;
        case 20070:ret_val="Статус";break;
        case 20071:ret_val="Расстояние";break;
        case 20072:ret_val="Длительность";break;
        case 20073:ret_val="A.Широта";break;
        case 20074:ret_val="A.Долгота";break;
        case 20075:ret_val="A.Адрес";break;
        case 20076:ret_val="B.Широта";break;
        case 20077:ret_val="B.Долгота";break;
        case 20078:ret_val="B.Адрес";break;
        case 20079:ret_val="Тип доставки";break;
        case 20080:ret_val="Заказ на";break;
        case 20081:ret_val="Заказано часов";break;
        case 20082:ret_val="Покупка";break;
        case 20083:ret_val="Скидка на продукт";break;
        case 20084:ret_val="Описание продукта";break;
        case 20085:ret_val="Кол-во продукта";break;
        case 20086:ret_val="Производитель";break;
        case 20087:ret_val="Наличие";break;
        case 20088:ret_val="Дата заказа";break;
        case 20089:ret_val="Инфо заказа";break;
        case 20090:ret_val="Всего";break;
        case 20091:ret_val="Валюта";break;
        case 20092:ret_val="Сенсор(Id)";break;
        case 20093:ret_val="Пользователь(Id)";break;
        case 20094:ret_val="Продукт(Id)";break;
        case 20095:ret_val="Транспорт(Id)";break;
        case 20096:ret_val="Доставка(Id)";break;
        case 20097:ret_val="СенсорA[Позывной](Id)";break;
        case 20098:ret_val="СенсорB[Позывной](Id)";break;
        case 20099:ret_val="Предоплачено";break;
        case 20100:ret_val="Продажа Id";break;
        case 20101:ret_val="Пользователь Id";break;
        case 20102:ret_val="Доставка Id";break;
        case 20103:ret_val="Заказ Id";break;
        case 20104:ret_val="Продукт Id";break;
        case 20105:ret_val="Описание Id";break;
        case 20106:ret_val="Магазин Id";break;
        case 20107:ret_val="Наличие документ Id";break;
        case 20108:ret_val="Объект Id";break;
        case 20109:ret_val="Входит в Id";break;
        case 20110:ret_val="Тип Id";break;
        case 20111:ret_val="Транспорт Id";break;
        case 20112:ret_val="Сенсор Id";break;
        case 20113:ret_val="Номер транзакции";break;
        case 20114:ret_val="Код предоплаты";break;
        case 20115:ret_val="Оценка пользователя(Id)";break;
        case 20116:ret_val="API ключ";break;
        case 20117:ret_val="Имя схемы";break;
        case 20118:ret_val="Всего оплачено";break;
        case 20119:ret_val="IP адрес";break;

        /*dashboard*/
        case 20501:ret_val="Последние события";break;
        case 20502:ret_val="Новое сообщение";break;
        case 20503:ret_val="Контакты для сообщения";break;
        case 20504:ret_val="Профиль пользователя";break;
        case 20505:ret_val="Обновить профиль";break;
        case 20506:ret_val="Подробнее";break;

        case 20601:ret_val="Общие данные";break;
        case 20602:ret_val="Профиль";break;
        case 20603:ret_val="Аудит";break;
        case 20604:ret_val="Сообщения";break;
        case 20605:ret_val="Заказать";break;
        case 20606:ret_val="Продажи";break;
        case 20607:ret_val="Заказы";break;
        case 20608:ret_val="Транспорт";break;
        case 20609:ret_val="Карта";break;
        case 20610:ret_val="Войти";break;
        case 20611:ret_val="Регистрация";break;

        case 20701:ret_val="Общие данные по работе";break;
        case 20702:ret_val="Профиль пользователя";break;
        case 20703:ret_val="Аудит действий";break;
        case 20704:ret_val="Сообщения пользователя";break;
        case 20705:ret_val="Заказать сейчас";break;
        case 20706:ret_val="Список продаж";break;
        case 20707:ret_val="История заказов";break;
        case 20708:ret_val="Транспорт для доставки";break;
        case 20709:ret_val="Карта передвижений";break;
        case 20710:ret_val="Подключиться к системе";break;
        case 20711:ret_val="Регистрация нового пользователя";break;
        /*stat*/
        case 30001:ret_val="Аудит";break;
        case 30002:ret_val="Водителей";break;
        case 30003:ret_val="Клиентов";break;
        case 30004:ret_val="Активных водителей";break;
        case 30005:ret_val="Активных клиентов";break;
        case 30006:ret_val="Транспорта";break;
        case 30007:ret_val="Производителей";break;
        case 30008:ret_val="Продуктов";break;
        case 30009:ret_val="Магазинов";break;
        case 30010:ret_val="Заказов";break;
        case 30011:ret_val="Заказов выполненных";break;
        case 30012:ret_val="Заказов отмененных";break;
        case 30013:ret_val="Заказов выполняющихся";break;
        case 30014:ret_val="Заказов сегодня";break;
        case 30015:ret_val="Заказов вчера";break;
        case 30016:ret_val="Заказов на неделе";break;
        case 30017:ret_val="Заказов за месяц";break;
        case 30018:ret_val="Заказов сегодня выполнено";break;
        case 30019:ret_val="Заказов сегодня отменено";break;
        case 30020:ret_val="Заказов сегодня выполняющихся";break;
        case 30021:ret_val="Заказов вчера выполнено";break;
        case 30022:ret_val="Заказов вчера отменено";break;
        case 30023:ret_val="Заказов вчера выполняющихся";break;
        case 30024:ret_val="Полная стоимость заказов";break;
        case 30025:ret_val="Покупок всего";break;
        case 30026:ret_val="Оплата всего";break;
        case 30027:ret_val="Заказов водителя";break;
        case 30028:ret_val="Заказов водителя выполнено";break;
        case 30029:ret_val="Заказов водителя выполняющихся";break;
        case 30030:ret_val="Заказов водителя стоимость";break;
        case 30031:ret_val="Заказов водителя куплено";break;
        case 30032:ret_val="Входящих сообщений";break;
        case 30033:ret_val="Исходящих сообщений";break;
        case 30034:ret_val="Сообщений аудита";break;

      }
    }
    else if (language===LANGUAGE_NAME_UKRAINIAN){
      switch(message_code){
        /*runtime errors*/
        case -2001:ret_val="Помилка Java java.lang.NullPointerException";break;
        case -2000:ret_val="Помилка Java java.lang.Throwable";break;
        case -500:ret_val="Помилка 500. Внутрішня помилка серверу";break;
        case -404:ret_val="Помилка 404. Сторінка не існує";break;

        case -1000:ret_val="Шаблону служби не створено";break;
        case -1001:ret_val="Блокування чорного списку";break;
        case -1002:ret_val="З'єднання з базою даних не встановлено";break;
        case -1003:ret_val="Дані контенту не отримані";break;
        case -1004:ret_val="Дані кукі не отримані";break;
        case -1005:ret_val="Скріпт Java не виконано";break;
        case -1006:ret_val="SQL запит не виконано";break;
        case -1007:ret_val="Отримано повідомлення під час виконання SQL запиту";break;

        case -200:ret_val="Помилка виконання обробки об'єктів jscript";break;
        case -100:ret_val="Помилка виконання завантаження даних";break;
        case -12:ret_val="Сплату не виконано";break;
        case -11:ret_val="Зміна даних не виконана";break;
        case -10:ret_val="Немає даних";break;

        case -5:ret_val="одержувачі не знайдені";break;
        case -4:ret_val="операція не може бути виконана";break;
        case -3:ret_val="немає привілеїв для доступу";break;
        case -2:ret_val="користувач з таким найменуванням вже існує";break;
        case -1:ret_val="користувача або пароль не знайдено";break;

        case 0:ret_val="успішно";break;
        /*User Interface*/

        /*usertype*/
        case 1:ret_val="Адміністратор";break;
        case 2:ret_val="Водій";break;
        case 3:ret_val="Клієнт";break;

        /*discount_type*/
        case 11:ret_val="Знижка у відсотках";break;
        case 12:ret_val="Знижка в суммі";break;
        case 13:ret_val="Додавання у відсотках";break;
        case 14:ret_val="Додавання в сумі";break;

        /*message_type*/
        case 21:ret_val="Вхідні";break;
        case 22:ret_val="Вихідні";break;

        /*times*/
        case 101:ret_val="тільки що";break;
        case 102:ret_val="хв. тому";break;
        case 103:ret_val="год. тому";break;
        case 104:ret_val="дн. тому";break;
        case 105:ret_val="хв.";break;
        case 106:ret_val="год.";break;

        /*distance*/
        case 111:ret_val="км";break;
        case 112:ret_val="км/год";break;
        case 113:ret_val="м";break;

        /*header*/
        case 1000:ret_val="fireArt";break;
        case 1001:ret_val="Англійська";break;
        case 1002:ret_val="Російська";break;
        case 1003:ret_val="Українська";break;
        case 1010:ret_val="open source CRM";break;

        /*Forms*/

        /*register*/
        case 1011:ret_val="Користувач";break;
        case 1012:ret_val="Пароль";break;
        case 1013:ret_val="Прізвище";break;
        case 1014:ret_val="Ім'я";break;
        case 1015:ret_val="Електронна пошта";break;
        case 1016:ret_val="Телефон";break;
        case 1017:ret_val="Спеціальний код";break;
        case 1018:ret_val="Реєстрація";break;
        case 1019:ret_val="Зберігти мене";break;
        case 1020:ret_val="Виконати";break;
        case 1021:ret_val="Виконати реєстрацію";break;
        case 1022:ret_val="Вже зареєстрований?";break;
        case 1023:ret_val="Увійти";break;
        case 1024:ret_val="Увійти як користувач";break;
        case 1025:ret_val="API ключ";break;

        /*login*/
        case 1051:ret_val="Вхід";break;
        case 1052:ret_val="Виконати вхід";break;
        case 1053:ret_val="Ще не зареєстрований?";break;
        case 1054:ret_val="Реєстрація";break;
        case 1055:ret_val="Особиста реєстрація";break;

        /*start*/
        case 1061:ret_val="Ваша служба таксі";break;
        case 1062:ret_val="Замовити";break;
        case 1063:ret_val="Замовити таксі";break;
        case 1064:ret_val="Резервування";break;
        case 1065:ret_val="Резервування транспорту";break;
        case 1066:ret_val="ВІТАЄМО ВАС В СЛУЖБІ ТАКСІ!";break;
        case 1067:ret_val="Служба для заощадливих таксі поїздок, VIP замовлень, класичне таксі,\nспільних поїздок, міжміських поїздок та аренда автомобілів";break;
        case 1068:ret_val="АРЕНДА АВТОМОБІЛЯ";break;
        case 1069:ret_val="Аренда автомобіля для управління транспортом";break;
        case 1070:ret_val="ЗАВАНТАЖИТИ мобільні додатки до СЛУЖБИ ТАКСІ";break;

        /*order*/
        case 1081:ret_val="Звідки";break;
        case 1082:ret_val="Куди";break;
        case 1083:ret_val="Якомога швидше";break;
        case 1084:ret_val="Замовлення";break;
        case 1085:ret_val="Замовлення на годину";break;
        case 1086:ret_val="Послуга";break;
        case 1087:ret_val="Розрахувати";break;
        case 1088:ret_val="Розрахувати вартість";break;
        case 1089:ret_val="Надіслати";break;
        case 1090:ret_val="Надіслати замовлення";break;
        case 1091:ret_val="Довжина маршрута";break;
        case 1092:ret_val="Час в дорозі";break;
        case 1093:ret_val="Вартість замовлення";break;
        case 1094:ret_val="Маршрут";break;
        case 1095:ret_val="Додаткові умови";break;
        case 1096:ret_val="Знижка користувача";break;
        case 1097:ret_val="Контактний телефон";break;

        /*transport_order*/
        case 1101:ret_val="Замовлення транспорту";break;
        case 1102:ret_val="Замовлення годин";break;

        /*sent_order*/
        case 1111:ret_val="Надіслане замовлення";break;
        case 1112:ret_val="Скасувати";break;
        case 1113:ret_val="Скасувати замовлення";break;
        case 1114:ret_val="Виконати замовлення";break;
        case 1115:ret_val="Обрати транспорт...";break;
        case 1116:ret_val="Карта";break;
        case 1117:ret_val="Карта переміщення";break;
        case 1118:ret_val="Встановити статус";break;
        case 1119:ret_val="Обрати статус замовлення...";break;

        /*report*/
        case 1121:ret_val="Звіт по поїздках";break;
        case 1122:ret_val="Немає звіту";break;

        /*message*/
        case 1131:ret_val="Текст повідомлення";break;
        case 1132:ret_val="Надіслати повідомлення";break;
        case 1133:ret_val="До кого";break;
        case 1134:ret_val="Отримувач повідомлення";break;
        case 1135:ret_val="Використовувати позивний, електронну пошту або телефон";break;

        /*profile*/
        case 1140:ret_val="Профіль користувача";break;
        case 1141:ret_val="Нове зображення";break;
        case 1142:ret_val="Зберігти користувача";break;
        case 1143:ret_val="Змінити пароль";break;
        case 1144:ret_val="Зберігти транспорт";break;
        case 1145:ret_val="Старий пароль";break;
        case 1146:ret_val="Новий пароль";break;
        case 1147:ret_val="Позивний";break;
        case 1148:ret_val="Марка транспорту";break;
        case 1149:ret_val="Номерний знак";break;
        case 1150:ret_val="Колір транспорту";break;
        case 1151:ret_val="Обрати колір транспорту...";break;

        /*cabinet*/
        case 1153:ret_val="Надіслані замовлення";break;
        case 1154:ret_val="Звіт";break;
        case 1155:ret_val="Повідомлення";break;
        case 1156:ret_val="Профіль користувача";break;
        case 1157:ret_val="Видалити замовлення";break;
        case 1158:ret_val="Видалити виконання";break;
        case 1159:ret_val="Сплата замовлення";break;
        case 1160:ret_val="Сплату виконано";break;

        /*profile*/
        case 1171:ret_val="GPS сенсор";break;
        case 1172:ret_val="Обрати GPS сенсор...";break;

        /*map*/
        case 1201:ret_val="Карта переміщення";break;
        case 1202:ret_val="Немає руху";break;
        case 1203:ret_val="Показати транспорт";break;
        case 1204:ret_val="Показати транспорт на карті";break;
        case 1205:ret_val="Наглядати";break;

        /*error*/
        case 1401:ret_val="Необхідно зареєструватися";break;
        case 1402:ret_val="Користувач заблокований";break;
        case 1403:ret_val="Недостатні привілеї";break;

        /*menu*/
        case 1501:ret_val="Увійти";break;
        case 1502:ret_val="Реєстрація";break;
        case 1503:ret_val="Кабінет";break;
        case 1504:ret_val="Вихід";break;
        case 1505:ret_val="Початкова";break;
        case 1506:ret_val="Служби";break;

        /*footer*/
        case 2001:ret_val="Напрямки бізнесу";break;
        case 2002:ret_val="Служба замовлення таксі";break;
        case 2003:ret_val="Поїздки та Перевезення";break;
        case 2004:ret_val="Служба Доставки товарів";break;
        case 2005:ret_val="Замовлення послуги за місцезнаходженням";break;
        case 2006:ret_val="Пошук послуги проживання";break;
        case 2007:ret_val="Умови підключення";break;
        case 2008:ret_val="Підключення безкоштовно. Отримай свій API ключ Легко";break;
        case 2009:ret_val="Контакти";break;
        case 2010:ret_val="Україна, Київ";break;

        case 2021:ret_val="Головне меню";break;
        case 2022:ret_val="Про нас";break;
        case 2023:ret_val="Соціальні мережі";break;
        case 2024:ret_val="Ми у соціальних мережах";break;

        /*discount_type*/
        case 3001:ret_val="Знижка у відсотках";break;
        case 3002:ret_val="Знижка в сумі";break;
        case 3003:ret_val="Додати у відсотках";break;
        case 3004:ret_val="Додати до суми";break;

        /*admin panel*/
        case 9000:ret_val="Пошук";break;
        case 9001:ret_val="Необхідно обрати пункт меню";break;
        case 9002:ret_val="Пошук даних за полями";break;
        case 9003:ret_val="Ціна від";break;
        case 9004:ret_val="Ціна до";break;
        case 10000:ret_val="Адмін Панель";break;
        case 10001:ret_val="Користувачі та функції";break;
        case 10002:ret_val="Користувач";break;
        case 10003:ret_val="Транспорт";break;
        case 10004:ret_val="Транспорт типа";break;
        case 10005:ret_val="Сенсор";break;
        case 10006:ret_val="Знижка";break;
        case 10007:ret_val="Податок";break;
        case 10008:ret_val="Повідомлення";break;
        case 10009:ret_val="Оцінка продукта";break;
        case 10010:ret_val="Оцінка транспорта";break;
        case 10011:ret_val="Точка месцезнаходження";break;
        case 10012:ret_val="Дані точки";break;
        case 10013:ret_val="Сенсор поряд";break;
        case 10014:ret_val="Сенсор група";break;
        case 10015:ret_val="Сенсор місце";break;
        case 10016:ret_val="Оцінка користувача";break;

        case 10101:ret_val="Типі та атрибути";break;
        case 10102:ret_val="Атрибут";break;
        case 10103:ret_val="Атрибут опис";break;
        case 10104:ret_val="Колір";break;
        case 10105:ret_val="Валюта";break;
        case 10106:ret_val="Мова";break;
        case 10107:ret_val="Доставка тип";break;
        case 10108:ret_val="Точка тип";break;
        case 10109:ret_val="Замовлення статус";break;
        case 10110:ret_val="Продукт опис";break;
        case 10111:ret_val="Продукт тип";break;
        case 10112:ret_val="Транспорт тип";break;

        case 10201:ret_val="Торгівля та Служби";break;
        case 10202:ret_val="Наявність";break;
        case 10203:ret_val="Документ наявності";break;
        case 10204:ret_val="Документ дані";break;
        case 10205:ret_val="Магазин";break;
        case 10206:ret_val="Магазин опис";break;
        case 10207:ret_val="Замовлення";break;
        case 10208:ret_val="Замовлення дані";break;
        case 10209:ret_val="Виробник";break;
        case 10210:ret_val="Продукт";break;
        case 10211:ret_val="Продукт опис";break;
        case 10212:ret_val="Продукт тип";break;
        case 10213:ret_val="Продаж";break;
        case 10214:ret_val="Сплата";break;
        case 10101:ret_val="Типы и атрибуты";break;
        case 10102:ret_val="Атрибут";break;
        case 10103:ret_val="Атрибут описание";break;
        case 10104:ret_val="Цвет";break;
        case 10105:ret_val="Валюта";break;
        case 10106:ret_val="Язык";break;
        case 10107:ret_val="Доставка тип";break;
        case 10108:ret_val="Точка тип";break;
        case 10109:ret_val="Заказ статус";break;
        case 10110:ret_val="Продукт описание";break;
        case 10111:ret_val="Продукт тип";break;
        case 10112:ret_val="Транспорт тип";break;
        case 10113:ret_val="Клієнт статус";break;

        case 10201:ret_val="Торгівля та Служби";break;
        case 10202:ret_val="Наявність";break;
        case 10203:ret_val="Документ з наявності";break;
        case 10204:ret_val="Документ дані";break;
        case 10205:ret_val="Магазин";break;
        case 10206:ret_val="Магазин опис";break;
        case 10207:ret_val="Замовлення";break;
        case 10208:ret_val="Замовлення дані";break;
        case 10209:ret_val="Виробник";break;
        case 10210:ret_val="Продукт";break;
        case 10211:ret_val="Продукт опис";break;
        case 10212:ret_val="Продукт тип";break;
        case 10213:ret_val="Продаж";break;
        case 10214:ret_val="Сплата";break;
        case 10215:ret_val="Карта передплати";break;
        case 10216:ret_val="Замовлення продукт";break;
        case 10217:ret_val="Замовлення кліент";break;
        case 10218:ret_val="Замовлення роботи";break;

        case 10301:ret_val="Статистика";break;
        case 10302:ret_val="Аудит";break;
        case 10303:ret_val="Налаштування";break;
        case 10304:ret_val="Проект";break;

        /*admin table*/
        case 20000:ret_val="Завантажити";break;
        case 20001:ret_val="Видалити";break;
        case 20002:ret_val="Оновити";break;
        case 20003:ret_val="Атрибут";break;
        case 20004:ret_val="Колір";break;
        case 20005:ret_val="Назва";break;
        case 20006:ret_val="Значення";break;
        case 20007:ret_val="Код";break;
        case 20008:ret_val="Активний";break;
        case 20009:ret_val="Опис";break;
        case 20010:ret_val="Назва об'єкту";break;
        case 20011:ret_val="Мова";break;
        case 20012:ret_val="Оновлення";break;
        case 20013:ret_val="Назва атрибуту";break;
        case 20014:ret_val="Зображення";break;
        case 20015:ret_val="Транспорт";break;
        case 20016:ret_val="Тип транспорту";break;
        case 20017:ret_val="Користувач";break;
        case 20018:ret_val="Тип";break;
        case 20019:ret_val="Знижка";break;
        case 20020:ret_val="Ім'я";break;
        case 20021:ret_val="Прізвище";break;
        case 20022:ret_val="Позивний";break;
        case 20023:ret_val="Пошта";break;
        case 20024:ret_val="Телефон";break;
        case 20025:ret_val="Створено";break;
        case 20026:ret_val="Рейтинг";break;
        case 20027:ret_val="Номерний знак";break;
        case 20028:ret_val="Серійний номер";break;
        case 20029:ret_val="Пристрій";break;
        case 20030:ret_val="Тип знижки";break;
        case 20031:ret_val="Початок";break;
        case 20032:ret_val="Закінчення";break;
        case 20033:ret_val="Тип продукції";break;
        case 20034:ret_val="Відправник(Id)";break;
        case 20035:ret_val="Отримувач(Id)";break;
        case 20036:ret_val="Повідомлення";break;
        case 20037:ret_val="Широта";break;
        case 20038:ret_val="Довгота";break;
        case 20039:ret_val="Час";break;
        case 20040:ret_val="Висота";break;
        case 20041:ret_val="Точність";break;
        case 20042:ret_val="Відхилення";break;
        case 20043:ret_val="Швидкість";break;
        case 20044:ret_val="Супутники";break;
        case 20045:ret_val="Батарея";break;
        case 20046:ret_val="Часова зона";break;
        case 20047:ret_val="Продукт";break;
        case 20048:ret_val="Магазин";break;
        case 20049:ret_val="Кількість";break;
        case 20050:ret_val="Вхідний док.";break;
        case 20051:ret_val="Вхідна дата";break;
        case 20052:ret_val="Повна ціна";break;
        case 20053:ret_val="Повний податок";break;
        case 20054:ret_val="Постачальник";break;
        case 20055:ret_val="Код доставки";break;
        case 20056:ret_val="Дата доставки";break;
        case 20057:ret_val="Ціна доставки";break;
        case 20058:ret_val="Дата сплати";break;
        case 20059:ret_val="Инфо сплати";break;
        case 20060:ret_val="Всього сплати";break;
        case 20061:ret_val="Податок";break;
        case 20062:ret_val="Ціна";break;
        case 20063:ret_val="Широта";break;
        case 20064:ret_val="Довгота";break;
        case 20065:ret_val="Телефон 1";break;
        case 20066:ret_val="Телефон 2";break;
        case 20067:ret_val="Адреса";break;
        case 20068:ret_val="Місто";break;
        case 20069:ret_val="Поштовий код";break;
        case 20070:ret_val="Статус";break;
        case 20071:ret_val="Відстань";break;
        case 20072:ret_val="Тривалість";break;
        case 20073:ret_val="A.Широта";break;
        case 20074:ret_val="A.Довгота";break;
        case 20075:ret_val="A.Адреса";break;
        case 20076:ret_val="B.Широта";break;
        case 20077:ret_val="B.Довгота";break;
        case 20078:ret_val="B.Адреса";break;
        case 20079:ret_val="Тип доставки";break;
        case 20080:ret_val="Замовлення на";break;
        case 20081:ret_val="Замовлено часів";break;
        case 20082:ret_val="Покупка";break;
        case 20083:ret_val="Знижка на продукт";break;
        case 20084:ret_val="Опис продукту";break;
        case 20085:ret_val="Кіл-ть продукту";break;
        case 20086:ret_val="Виробник";break;
        case 20087:ret_val="Наявність";break;
        case 20088:ret_val="Дата замовлення";break;
        case 20089:ret_val="Инфо замовлення";break;
        case 20090:ret_val="Всього";break;
        case 20091:ret_val="Валюта";break;
        case 20092:ret_val="Сенсор(Id)";break;
        case 20093:ret_val="Користувач(Id)";break;
        case 20094:ret_val="Продукт(Id)";break;
        case 20095:ret_val="Транспорт(Id)";break;
        case 20096:ret_val="Доставка(Id)";break;
        case 20097:ret_val="СенсорA[Позивний](Id)";break;
        case 20098:ret_val="СенсорB[Позивний](Id)";break;
        case 20099:ret_val="Передплата";break;
        case 20100:ret_val="Продаж Id";break;
        case 20101:ret_val="Користувач Id";break;
        case 20102:ret_val="Доставка Id";break;
        case 20103:ret_val="Замовлення Id";break;
        case 20104:ret_val="Продукт Id";break;
        case 20105:ret_val="Опис Id";break;
        case 20106:ret_val="Магазин Id";break;
        case 20107:ret_val="Наявність документ Id";break;
        case 20108:ret_val="Об'єкт Id";break;
        case 20109:ret_val="Входить до Id";break;
        case 20110:ret_val="Тип Id";break;
        case 20111:ret_val="Транспорт Id";break;
        case 20112:ret_val="Сенсор Id";break;
        case 20113:ret_val="Номер транзакції";break;
        case 20114:ret_val="Код передплати";break;
        case 20115:ret_val="Оцінка користувача(Id)";break;
        case 20116:ret_val="API ключ";break;
        case 20117:ret_val="Ім'я схеми";break;
        case 20118:ret_val="Всього сплачено";break;
        case 20119:ret_val="IP адреса";break;

        /*dashboard*/
        case 20501:ret_val="Останні події";break;
        case 20502:ret_val="Нові повідомлення";break;
        case 20503:ret_val="Контакти для повідомлення";break;
        case 20504:ret_val="Профіль користувача";break;
        case 20505:ret_val="Оновити профіль";break;
        case 20506:ret_val="Детальніше";break;

        case 20601:ret_val="Підсумок";break;
        case 20602:ret_val="Профіль";break;
        case 20603:ret_val="Аудит";break;
        case 20604:ret_val="Повідомлення";break;
        case 20605:ret_val="Замовити";break;
        case 20606:ret_val="Продажі";break;
        case 20607:ret_val="Замовлення";break;
        case 20608:ret_val="Транспорт";break;
        case 20609:ret_val="Карта";break;
        case 20610:ret_val="Вхід";break;
        case 20611:ret_val="Реєстрація";break;

        case 20701:ret_val="Підсумок роботи";break;
        case 20702:ret_val="Профіль користувача";break;
        case 20703:ret_val="Аудит подій";break;
        case 20704:ret_val="Повідомлення користувача";break;
        case 20705:ret_val="Замовити зараз";break;
        case 20706:ret_val="Список продажів";break;
        case 20707:ret_val="Історія замовлень";break;
        case 20708:ret_val="Транспорт для доставки";break;
        case 20709:ret_val="Карта переміщення";break;
        case 20710:ret_val="Підключитися до системи";break;
        case 20711:ret_val="Реєстрація нового користувача";break;
        /*stat*/
        case 30001:ret_val="Аудит";break;
        case 30002:ret_val="Водіїв";break;
        case 30003:ret_val="Клієнтів";break;
        case 30004:ret_val="Активних водіїв";break;
        case 30005:ret_val="Активних клієнтів";break;
        case 30006:ret_val="Транспорту";break;
        case 30007:ret_val="Виробників";break;
        case 30008:ret_val="Продуктів";break;
        case 30009:ret_val="Магазинів";break;
        case 30010:ret_val="Замовлень";break;
        case 30011:ret_val="Замовлень виконаних";break;
        case 30012:ret_val="Замовлень скасованих";break;
        case 30013:ret_val="Замовлень виконується";break;
        case 30014:ret_val="Замовлень сьогодні";break;
        case 30015:ret_val="Замовлень вчора";break;
        case 30016:ret_val="Замовлень на тіжні";break;
        case 30017:ret_val="Замовлень за місяць";break;
        case 30018:ret_val="Замовлень сьогодні виконаних";break;
        case 30019:ret_val="Замовлень сьогодні скасованих";break;
        case 30020:ret_val="Замовлень сьогодні виконується";break;
        case 30021:ret_val="Замовлень вчора виконаних";break;
        case 30022:ret_val="Замовлень вчора скасованих";break;
        case 30023:ret_val="Замовлень вчора виконується";break;
        case 30024:ret_val="Повна вартість замовлень";break;
        case 30025:ret_val="Покупок всього";break;
        case 30026:ret_val="Сплата всього";break;
        case 30027:ret_val="Замовлень водія";break;
        case 30028:ret_val="Замовлень водія виконано";break;
        case 30029:ret_val="Замовлень водія виконується";break;
        case 30030:ret_val="Замовлень водія вартість";break;
        case 30031:ret_val="Замовлень водія куплено";break;
        case 30032:ret_val="Вхідних повідомлень";break;
        case 30033:ret_val="Вихідних повідомлень";break;
        case 30034:ret_val="Повідомлень аудита";break;

      }
    }
    return ret_val;
  }
  function getTranslateName(language,name){
    if(name==="Upload")return getMessage(language,20000);
    else if(name==="Remove")return getMessage(language,20001);
    else if(name==="Update")return getMessage(language,20002);
    else if(name==="Attribute")return getMessage(language,20003);
    else if(name==="Color")return getMessage(language,20004);
    else if(name==="Name")return getMessage(language,20005);
    else if(name==="Value")return getMessage(language,20006);
    else if(name==="Code")return getMessage(language,20007);
    else if(name==="Activity")return getMessage(language,20008);
    else if(name==="Description")return getMessage(language,20009);
    else if(name==="Object name")return getMessage(language,20010);
    else if(name==="Language")return getMessage(language,20011);
    else if(name==="Last update")return getMessage(language,20012);
    else if(name==="Attr")return getMessage(language,20013);
    else if(name==="Picture")return getMessage(language,20014);
    else if(name==="Transport")return getMessage(language,20015);
    else if(name==="Transport type")return getMessage(language,20016);
    else if(name==="Username")return getMessage(language,20017);
    else if(name==="Type")return getMessage(language,20018);
    else if(name==="Discount")return getMessage(language,20019);
    else if(name==="Firstname")return getMessage(language,20020);
    else if(name==="Lastname")return getMessage(language,20021);
    else if(name==="Callname")return getMessage(language,20022);
    else if(name==="Email")return getMessage(language,20023);
    else if(name==="Phone")return getMessage(language,20024);
    else if(name==="Create date")return getMessage(language,20025);
    else if(name==="Rate")return getMessage(language,20026);
    else if(name==="License plate")return getMessage(language,20027);
    else if(name==="Serial number")return getMessage(language,20028);
    else if(name==="Device name")return getMessage(language,20029);
    else if(name==="Discount type")return getMessage(language,20030);
    else if(name==="Start date")return getMessage(language,20031);
    else if(name==="Finish date")return getMessage(language,20032);
    else if(name==="Product type")return getMessage(language,20033);
    else if(name==="Sender(Id)")return getMessage(language,20034);
    else if(name==="Receiver(Id)")return getMessage(language,20035);
    else if(name==="Message")return getMessage(language,20036);
    else if(name==="Lat")return getMessage(language,20037);
    else if(name==="Lng")return getMessage(language,20038);
    else if(name==="Time")return getMessage(language,20039);
    else if(name==="Alt")return getMessage(language,20040);
    else if(name==="Accu")return getMessage(language,20041);
    else if(name==="Bear")return getMessage(language,20042);
    else if(name==="Speed")return getMessage(language,20043);
    else if(name==="Sat")return getMessage(language,20044);
    else if(name==="Bat")return getMessage(language,20045);
    else if(name==="Timezone")return getMessage(language,20046);
    else if(name==="Product")return getMessage(language,20047);
    else if(name==="Store")return getMessage(language,20048);
    else if(name==="Count")return getMessage(language,20049);
    else if(name==="Invoice code")return getMessage(language,20050);
    else if(name==="Invoice date")return getMessage(language,20051);
    else if(name==="Total price")return getMessage(language,20052);
    else if(name==="Total tax")return getMessage(language,20053);
    else if(name==="Supplier")return getMessage(language,20054);
    else if(name==="Delivery code")return getMessage(language,20055);
    else if(name==="Delivery date")return getMessage(language,20056);
    else if(name==="Delivery price")return getMessage(language,20057);
    else if(name==="Payment date")return getMessage(language,20058);
    else if(name==="Payment info")return getMessage(language,20059);
    else if(name==="Payment amount")return getMessage(language,20060);
    else if(name==="Tax")return getMessage(language,20061);
    else if(name==="Price")return getMessage(language,20062);
    else if(name==="Latitude")return getMessage(language,20063);
    else if(name==="Longitude")return getMessage(language,20064);
    else if(name==="Phone1")return getMessage(language,20065);
    else if(name==="Phone2")return getMessage(language,20066);
    else if(name==="Address")return getMessage(language,20067);
    else if(name==="City")return getMessage(language,20068);
    else if(name==="Postcode")return getMessage(language,20069);
    else if(name==="Status")return getMessage(language,20070);
    else if(name==="Distance")return getMessage(language,20071);
    else if(name==="Duration")return getMessage(language,20072);
    else if(name==="A.Latitude")return getMessage(language,20073);
    else if(name==="A.Longitude")return getMessage(language,20074);
    else if(name==="A.Address")return getMessage(language,20075);
    else if(name==="B.Latitude")return getMessage(language,20076);
    else if(name==="B.Longitude")return getMessage(language,20077);
    else if(name==="B.Address")return getMessage(language,20078);
    else if(name==="Delivery type")return getMessage(language,20079);
    else if(name==="Reserved date")return getMessage(language,20080);
    else if(name==="Reserved hours")return getMessage(language,20081);
    else if(name==="Purchase")return getMessage(language,20082);
    else if(name==="Product discount")return getMessage(language,20083);
    else if(name==="Product param")return getMessage(language,20084);
    else if(name==="Product count")return getMessage(language,20085);
    else if(name==="Manufacture")return getMessage(language,20086);
    else if(name==="Stock count")return getMessage(language,20087);
    else if(name==="Order date")return getMessage(language,20088);
    else if(name==="Order info")return getMessage(language,20089);
    else if(name==="Amount")return getMessage(language,20090);
    else if(name==="Currency")return getMessage(language,20091);
    else if(name==="Sensor(Id)")return getMessage(language,20092);
    else if(name==="User(Id)")return getMessage(language,20093);
    else if(name==="Product(Id)")return getMessage(language,20094);
    else if(name==="Transport(Id)")return getMessage(language,20095);
    else if(name==="Delivery(Id)")return getMessage(language,20096);
    else if(name==="SensorA[Callname](Id)")return getMessage(language,20097);
    else if(name==="SensorB[Callname](Id)")return getMessage(language,20098);
    else if(name==="Prepaid amount")return getMessage(language,20099);
    else if(name==="PurchaseId")return getMessage(language,20100);
    else if(name==="UserId")return getMessage(language,20101);
    else if(name==="DeliveryId")return getMessage(language,20102);
    else if(name==="OrderId")return getMessage(language,20103);
    else if(name==="ProductId")return getMessage(language,20104);
    else if(name==="ParamId")return getMessage(language,20105);
    else if(name==="StoreId")return getMessage(language,20106);
    else if(name==="StockInvoiceId")return getMessage(language,20107);
    else if(name==="ObjectId")return getMessage(language,20108);
    else if(name==="ParentId")return getMessage(language,20109);
    else if(name==="TypeId")return getMessage(language,20110);
    else if(name==="TransportId")return getMessage(language,20111);
    else if(name==="SensorId")return getMessage(language,20112);
    else if(name==="Transaction id")return getMessage(language,20113);
    else if(name==="Prepaid code")return getMessage(language,20114);
    else if(name==="ReviewUser(Id)")return getMessage(language,20115);
    else if(name==="API key")return getMessage(language,20116);
    else if(name==="Schema name")return getMessage(language,20117);
    else if(name==="Amount paid")return getMessage(language,20118);
    else if(name==="IP address")return getMessage(language,20119);
    else return name;
  }
  function getTranslateStatName(language,name){
    if(name==="Audit")return getMessage(language,30001);
    else if(name==="Drivers")return getMessage(language,30002);
    else if(name==="Clients")return getMessage(language,30003);
    else if(name==="Drivers online")return getMessage(language,30004);
    else if(name==="Clients online")return getMessage(language,30005);
    else if(name==="Transports")return getMessage(language,30006);
    else if(name==="Manufactures")return getMessage(language,30007);
    else if(name==="Products")return getMessage(language,30008);
    else if(name==="Stores")return getMessage(language,30009);
    else if(name==="Orders")return getMessage(language,30010);
    else if(name==="Orders completed")return getMessage(language,30011);
    else if(name==="Orders cancelled")return getMessage(language,30012);
    else if(name==="Orders pending")return getMessage(language,30013);
    else if(name==="Orders today")return getMessage(language,30014);
    else if(name==="Orders yesterday")return getMessage(language,30015);
    else if(name==="Orders week")return getMessage(language,30016);
    else if(name==="Orders month")return getMessage(language,30017);
    else if(name==="Orders today completed")return getMessage(language,30018);
    else if(name==="Orders today cancelled")return getMessage(language,30019);
    else if(name==="Orders today panding")return getMessage(language,30020);
    else if(name==="Orders yesterday completed")return getMessage(language,30021);
    else if(name==="Orders yesterday cancelled")return getMessage(language,30022);
    else if(name==="Orders yesterday pending")return getMessage(language,30023);
    else if(name==="Orders total price")return getMessage(language,30024);
    else if(name==="Purchase amount")return getMessage(language,30025);
    else if(name==="Payment amount")return getMessage(language,30026);
    else if(name==="Driver orders")return getMessage(language,30027);
    else if(name==="Driver orders completed")return getMessage(language,30028);
    else if(name==="Driver orders pending")return getMessage(language,30029);
    else if(name==="Driver orders total price")return getMessage(language,30030);
    else if(name==="Driver purchase amount")return getMessage(language,30031);
    else if(name==="Input messages")return getMessage(language,30032);
    else if(name==="Output messages")return getMessage(language,30033);
    else if(name==="Audit messages")return getMessage(language,30034);
    else return name;
  }
  function getOrderInfo(language,order/*class faOrder*/){
    var info='<i class="fa fa-comment-o"></i><big> '+order.user.call_name+' </big>'+
    (order.user.phone?'<i class="fa fa-phone"></i> '+order.user.phone:'')+'<br>'+
    (order.status_attr_list.length>0?'<i>'+order.status_attr_list[0].value+'</i>':order.order_status_name)+'<br>'+
    (order.order_lat&&order.order_lon?'<i class="fa fa-map-marker"></i> '+order.order_lat+' '+order.order_lon+'<br>':'')+
    (order.order_address?order.order_address+'<br>':'')+
    (order.reserved_date?'<i class="fa fa-calendar-check-o"></i> '+fromMySQLDatetimeString(order.reserved_date)+' ':'')+
    (order.reserved_hours?order.reserved_hours+getMessage(language,106):'')+
    (order.reserved_date||order.reserved_hours?'<br>':'')+
    (order.transport?'<i class="fa fa-cab"></i> '+order.transport.name+' '+
    '<i class="fa fa-circle" style="font-size:12px;color:'+order.transport.color+'"></i> '+order.transport.license_plate+'<br>':'')+

    getTimesAgoFrom(toMySQLDatetimeMilisec(order.create_date),language);
    return info;
  }
  function getMarkerInfo(language,track/*class faTrack*/){
    var battery_value=track.battery?parseInt(track.battery):-1;
    var info='<i class="fa fa-comment-o"></i><big> '+track.sensor.user.call_name+' </big>'+
    (track.sensor.user.phone?'<i class="fa fa-phone"></i> '+track.sensor.user.phone:'')+'<br>'+
    (track.type_attr_list.length>0?'<i>'+track.type_attr_list[0].value+'</i>':track.track_type_name)+'<br>'+
    (track.latitude&&track.longitude?'<i class="fa fa-map-marker"></i> '+track.latitude+' '+track.longitude+'<br>':'')+
    (track.bearing?'<i class="fa fa-location-arrow"></i> '+track.bearing+'&deg; ':'')+
    (track.speed?'<i class="fa fa-tachometer"></i> '+(track.speed*3.6).toFixed(2)+getMessage(language,112)+' ':'')+
    (track.altitude?'<i class="fa fa-arrows-v"></i> '+track.altitude+getMessage(language,113)+' ':'')+
    (track.accuracy?'<i class="fa fa-circle-thin"></i> '+track.accuracy+getMessage(language,113)+' ':'')+
    (track.bearing||track.speed||track.altitude||track.accuracy?'<br>':'')+
    (track.time&&track.time>0?'<i class="fa fa-clock-o"></i> '+toDatetimeString(parseInt(track.time*1000))+' ':'')+
    (track.timezone_offset?'<i class="fa fa-history"></i> '+track.timezone_offset+getMessage(language,105)+' ':'')+
    (track.satellites?'<img src="'+picture_sat+'" style="width:7%;weight:7%"> '+track.satellites+' ':'')+
    (battery_value>-1?(battery_value>90?'<i class="fa fa-battery-full"></i> ':
                      (battery_value>75?'<i class="fa fa-battery-three-quarters"></i> ':
                      (battery_value>50?'<i class="fa fa-battery-half"></i> ':
                      (battery_value>25?'<i class="fa fa-battery-quarter"></i> ':'<i class="fa fa-battery-empty"></i> '))))+track.battery+'% ':'')+
    (track.time||track.timezone_offset||track.satellites||track.battery?'<br>':'')+
    (track.transport?'<i class="fa fa-cab"></i> '+track.transport.name+' '+
    '<i class="fa fa-circle" style="font-size:12px;color:'+track.transport.color+'"></i> '+track.transport.license_plate+'<br>':'')+

    getTimesAgoFrom(toMySQLDatetimeMilisec(track.create_date),language);
    return info;
  }
  function getDiscountByType(discount_type,discount_value,language,currency){
    var ret_val="",type=parseInt(discount_type);
    switch(type){
      case DISCOUNT_IN_PERCENT:ret_val=getMessage(language,3001)+": "+discount_value+" %";break;
      case DISCOUNT_IN_VALUE:ret_val=getMessage(language,3002)+": "+discount_value+" "+currency;break;
      case INCREASE_IN_PERCENT:ret_val=getMessage(language,3003)+": "+discount_value+" %";break;
      case INCREASE_IN_VALUE:ret_val=getMessage(language,3004)+": "+discount_value+" "+currency;break;
    }
    return ret_val;
  }
  function getUserTypenameByType(user_type,language){
    var ret_val="",type=parseInt(user_type);
    switch(type){
      case USER_TYPE_ADMIN:ret_val=getMessage(language,1);break;
      case USER_TYPE_WORKER:ret_val=getMessage(language,2);break;
      case USER_TYPE_CUSTOMER:ret_val=getMessage(language,3);break;
    }
    return ret_val;
  }
  function getTimesAgoFrom(milisec,language){
    var curr_date=new Date();
    var curr_time=curr_date.getTime();
    var times_ago=(curr_time-milisec)/1000;
    if(times_ago<60)return getMessage(language,101);
    else if(times_ago<3600)return (times_ago/60).toFixed()+" "+getMessage(language,102);
    else if(times_ago<86400)return (times_ago/3600).toFixed()+" "+getMessage(language,103);
    return (times_ago/86400).toFixed()+" "+getMessage(language,104);
  }

  /*END_OF_INNOCENCE*/