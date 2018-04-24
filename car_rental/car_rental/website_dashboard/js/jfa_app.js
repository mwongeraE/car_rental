/*  fireArt. open source CRM.
 *  Good Ideas for Bussiness!
 *
 *  fireArt webapp jscript ver.2.0
 *
 *  ABTO3BIT 2017 all rights reserved
 */

  /*const&var*/
  var API_KEY="";/*api_key*/ /*put key here OR set null for no use OR empty for input*/
  var PRODUCT_TYPE_FIXED_PRICE=1;/*fixed price for trip*/
  var PRODUCT_TYPE_DISTANCE_PRICE=2;/*price for trip distance(per km/ml)*/
  var PRODUCT_TYPE_RENT_PRICE=3;/*price for trip duration(per hour)*/
  var PRODUCT_TYPE_ID_NO_CALCULATION_LIST=[PRODUCT_TYPE_FIXED_PRICE,PRODUCT_TYPE_RENT_PRICE];/*WARNING! database state: 1-fixed 3-rent*/
  var PRODUCT_TYPE_ID_CALCULATION_LIST=[PRODUCT_TYPE_DISTANCE_PRICE];/*WARNING! database state: 2-per km*/
  var ORDER_DELIVERY_TYPE_ID=1;
  var ORDER_ID=null;/*for find order by id in cabinet*/
  var ORDER_CALLBACK=null;/*callback after order sent*/
  var STATUS_ID_ACCEPTED=1,STATUS_ID_PROCESSED=2,STATUS_ID_LANDING=3,STATUS_ID_DELIVERING=4,STATUS_ID_COMPLETED=5;
  var STATUS_ID_CANCELLATION=-1,STATUS_ID_CANCELED=-2;
  /*interval task*/
  var interval_1_id,interval_2_id;
  /*map*/
  var map_load=null;
  var map_marker_index=0,map_polyline_index=0;
  /*icons*/
  var icon_color_red="<i class='fa fa-circle' style='font-size:12px;color:red'></i>";
  var icon_color_darkgrey="<i class='fa fa-circle' style='font-size:12px;color:darkgrey'></i>";
  var icon_color_green="<i class='fa fa-circle' style='font-size:12px;color:green'></i>";
  var icon_color_blue="<i class='fa fa-circle' style='font-size:12px;color:blue'></i>";
  var icon_close="<i class='fa fa-close' style='font-size:18px;color:red'></i>";
  var icon_check="<i class='fa fa-check' style='font-size:18px;color:green'></i>";
  var icon_callname="<i class='fa fa-comment-o'></i>";
  var icon_phone="<i class='fa fa-phone'></i>";
  /*marker*/
  var marker_width=32,marker_height=32;
  var marker_route_width=64,marker_route_height=64;
  var marker_max_window_width=256;
  var picture_marker_A=window.location.pathname+"images/service/marker_A.png";
  var picture_marker_B=window.location.pathname+"images/service/marker_B.png";
  var picture_marker_people=window.location.pathname+"images/service/people.png";
  /*var picture_marker_car_hor=window.location.pathname+"images/service/marker_car_hor.png";*/
  var picture_marker_car_45=window.location.pathname+"images/service/car_45d.png";
  var picture_marker_car_90=window.location.pathname+"images/service/car_90d.png";
  var picture_marker_car_135=window.location.pathname+"images/service/car_135d.png";
  var picture_marker_car_180=window.location.pathname+"images/service/car_180d.png";
  var picture_marker_car_225=window.location.pathname+"images/service/car_225d.png";
  var picture_marker_car_270=window.location.pathname+"images/service/car_270d.png";
  var picture_marker_car_315=window.location.pathname+"images/service/car_315d.png";
  var picture_marker_car_360=window.location.pathname+"images/service/car_360d.png";
  var picture_sat=window.location.pathname+"images/service/sat32.png";
  var picture_cancel=window.location.pathname+"images/service/Cancel.png";
  var picture_do_not_disturb=window.location.pathname+"images/service/ic_do_not_disturb_white_48dp_2x.png";
  var picture_do_not_disturb_black=window.location.pathname+"images/service/ic_do_not_disturb_black_48dp_2x.png";
  var picture_car_hor=window.location.pathname+"images/service/car_hor.png";
  /*polyline*/
  var color_blue="#0000FF";
  var color_green="#00FF00";
  var color_red="#FF0000";
  var opacity_light=0.3;
  var opacity_dark=0.6;
  var weight_small=6;
  var weight_middle=8;
  var weight_big=10;
  var map_zoom=4;
  var active_currency=null;
  var currency_title="";
  var currency_about="";
  var current_user=null;
  var order_status_attr_part_list=[];
  /*fA ia a fireArt main class*/
  /*var fa=new fA(window.location.protocol+"//taxi.autozvit.com:"+window.location.port);*/
  /*var fa=new fA(window.location.protocol+"//"+window.location.host);*/
  /*var fa=new fA(window.location.protocol+"//taxi.autozvit.com");*/
  var fa=new fA(window.location.protocol+"//"+window.location.host);
  /*page args: p-starting page name, l-language name of interface m-map provider name(google,yandex,osm)*/
  var page_name=getParam("p"),lang_name=getParam("l"),prov_name=getParam("m");
  var user_name=getParam("user"),pass_name=getParam("pass"),apikey_name=getParam("api_key");
  var user_type=null,call_name=null;
  /*pages block-number of pages set, curr-number of current page in set, rows-number of rows on page*/
  var PAGE_SET=0,PAGE_CURR=0,PAGE_ROWS=2,PAGE_COUNT=0;
  var TAB_CURR=0;
  var found_rows=null;
  var tab_page=function(found_rows,page_set,page_curr,page_rows,page_count){
    this.found_rows=found_rows;
    this.page_set=page_set;
    this.page_curr=page_curr;
    this.page_rows=page_rows;
    this.page_count=page_count;
  };
  var tab_page_list=[];/*tabs=[0..X] array of tab_page classes*/

  function faInit(){
    var temp="";
    var username=DEFAULT_USERNAME,password=DEFAULT_PASSWORD;
    var token=null;
    var apikey=null;
    var language=DEFAULT_LANGUAGE_NAME,map_provider=DEFAULT_MAP_PROVIDER;
    /*user=getParam("user");pass=getParam("pass");*/
    if(lang_name.length>0)temp=lang_name;
    else temp=getCookie(COOKIE_LANGUAGE);
    if(temp.length>0){/*language*/
      language=temp;
    }
    fa.language=language;
    temp="";
    if(prov_name.length>0)temp=prov_name;
    else temp=getCookie(COOKIE_MAP_PROVIDER);
    if(temp.length>0){/*map_provider*/
      map_provider=temp;
    }
    fa.map_provider=map_provider;
    temp="";
    if(user_name.length>0){
      username=user_name;
      password=pass_name;
    }
    else{
      temp=getCookie(COOKIE_CURRENT_USER);
      if(temp.length>0){/*username,password*/
        username=temp;
        temp=getCookie(username);
        password=encodeData(username,temp);
      }
      temp=getCookie(COOKIE_CURRENT_TOKEN);
      if(temp.length>0){/*token*/
        token=temp;
      }
    }
    temp="";
    if(apikey_name.length>0)API_KEY=apikey_name;
    if(API_KEY!==null&&API_KEY.length>0&&API_KEY!=="api_key"&&API_KEY!=="put_key_here"){
      apikey=API_KEY;
    }
    else temp=getCookie(COOKIE_CURRENT_API_KEY);
    if(temp.length>0){/*api_key*/
      apikey=temp;
    }

    fa.username=username;
    fa.password=password;
    if(token!==null)fa.token=token;
    if(apikey!==null)fa.setApiKey(apikey);
    /*alert("language="+fa.language+" user="+fa.username+" pass="+fa.password+" token="+fa.token);*/
    getUser(language);
    getCurrency(language);
    getOrderStatusAttrPart(language);
    getMapProvider(language);
  }

  /*tools*/
  function getHtml(url,ptr,callback){
    /*stop interval task*/
    clearInterval(interval_1_id);
    clearInterval(interval_2_id);
    /*load content*/
    $.ajax({url:url,crossDomain:true,
      success:function(data){
        $(ptr).html(data);
        if(callback!==null)callback();
      },
      error:function(){}
    });
  }
  function startHtml(){
    getHtml(window.location.pathname+"header.html","#div_header",null);
    /*default start page or "site_address?p=page_name"*/
    if(page_name.length>0)getHtml("/"+page_name+".html","#div_content",null);
    else{
      if(window.location.pathname.indexOf("/admin/")!==-1)gotoAdmin();
      else if(window.location.pathname.indexOf("/dashboard/")!==-1)gotoDashboard();
      else gotoStart();
    }
    getHtml(window.location.pathname+"footer.html","#div_footer",null);
  }
  function getMapProvider(language){
    /*$.ajaxSetup({cache:true});*/
    $.holdReady(true);
    if(fa.map_provider===MAP_PROVIDER_GOOGLE){
      $.getScript(MAP_PROVIDER_GOOGLE_URL+getLanguageCode(language))
      .done(function(script,textStatus){map_load=true;$.holdReady(false);}).fail(function(jqxhr,settings,exception){map_load=false;$.holdReady(false);});
    }
    else if(fa.map_provider===MAP_PROVIDER_YANDEX){
      $.getScript(MAP_PROVIDER_YANDEX_URL+getLanguageCode(language))
      .done(function(script,textStatus){map_load=true;$.holdReady(false);}).fail(function(jqxhr,settings,exception){map_load=false;$.holdReady(false);});
    }
    else if(fa.map_provider===MAP_PROVIDER_OSM){
      $('<link/>',{rel: 'stylesheet',type: 'text/css', href: MAP_PROVIDER_OSM_CSS_URL}).appendTo('head');
      $.getScript(MAP_PROVIDER_OSM_JS_URL)
      .done(function(script,textStatus){map_load=true;$.holdReady(false);}).fail(function(jqxhr,settings,exception){map_load=false;$.holdReady(false);});
    }
  }
  /*goto*/
  function gotoStart(){
    getHtml(window.location.pathname+"start.html","#div_content",null);
  }
  function gotoLogin(){
    getHtml(window.location.pathname+"login.html","#div_content",null);
  }
  function gotoRegister(){
    getHtml(window.location.pathname+"register.html","#div_content",null);
  }
  function gotoCabinet(){
    getHtml(window.location.pathname+"cabinet.html","#div_content",null);
  }
  function gotoOrder(){
    getHtml(window.location.pathname+"order.html","#div_content",null);
  }
  function gotoTransport(){
    getHtml(window.location.pathname+"transport.html","#div_content",null);
  }
  function gotoMap(){
    getHtml(window.location.pathname+"map.html","#div_content",null);
  }
  function gotoAdmin(){
    getHtml(window.location.pathname+"admin.html","#div_content",null);
  }
  function gotoDashboard(){
    getHtml(window.location.pathname+"dashboard.html","#div_content",null);
  }
  /*forever_tools: Pages,Tabs*/
  function pagesByRowsCount(rows_count){
    if(rows_count!==null){
      var index=0,visible,count=0;
      $("#pages li").each(function(){
        if(index===0&&PAGE_SET>0)visible=true;
        else if(index>0&&rows_count>PAGE_SET*PAGE_COUNT*PAGE_ROWS+(index-1)*PAGE_ROWS)visible=true;
        else visible=false;

        if(visible){$(this).show();count++;}else $(this).hide();

        if(PAGE_CURR===index-1)$(this).attr("class","active");
        else $(this).attr("class","");
        if(PAGE_COUNT>0){
          if(index>0&&index<=PAGE_COUNT)
            $(this).find("a").html(PAGE_SET*PAGE_COUNT+index);
        }
        index++;
      });
      PAGE_COUNT=index-2;
      if(count>1)$("#div_pages").show();else $("#div_pages").hide();
    }
    else $("#div_pages").hide();
  }
  function showPages(){
    pagesByRowsCount(found_rows);
  }
  function gotoPage(page_num,callback,callback_ptr){
    if(page_num===-1){PAGE_SET--;PAGE_CURR=0;}
    else if(page_num===-2){PAGE_SET++;PAGE_CURR=0;}
    else PAGE_CURR=page_num;
    if(typeof(callback)!=='undefined'&&callback!==null)callback(callback_ptr);
    else getData(fa.language);/*is_not_reload=false*/
  }
  function saveTabPages(){
    if(!tab_page_list[TAB_CURR]){
      var page=new tab_page(found_rows,PAGE_SET,PAGE_CURR,PAGE_ROWS,PAGE_COUNT);
      tab_page_list[TAB_CURR]=page;
    }
    else{
      var page=tab_page_list[TAB_CURR];
      page.found_rows=found_rows;
      page.page_set=PAGE_SET;
      page.page_curr=PAGE_CURR;
      page.page_rows=PAGE_ROWS;
      page.page_count=PAGE_COUNT;
    }
  }
  function tab(index,is_not_reload){
    saveTabPages();
    found_rows=null;
    PAGE_SET=0;PAGE_CURR=0;PAGE_COUNT=0;
    TAB_CURR=index;
    getData(fa.language,is_not_reload);
  }
  /*is*/
  /*order route, calculate*/
  function isNoCalculationTypeId(type_id){/*type_id as integer*/
    var j,ret_val=false;
    for(j=0;j<PRODUCT_TYPE_ID_NO_CALCULATION_LIST.length;j++){
      if(PRODUCT_TYPE_ID_NO_CALCULATION_LIST[j]===type_id){
        ret_val=true;break;
      }
    }
    return ret_val;
  }
  function isCalculationTypeId(type_id){/*type_id as integer*/
    var j,ret_val=false;
    for(j=0;j<PRODUCT_TYPE_ID_CALCULATION_LIST.length;j++){
      if(PRODUCT_TYPE_ID_CALCULATION_LIST[j]===type_id){
        ret_val=true;break;
      }
    }
    return ret_val;
  }
  /*get*/
  /*
  function getSensor(){
    var sensor=new faSensor();
    if(user_type===USER_TYPE_WORKER)sensor.user_id=user.id;
    else sensor.user_id="%";
    sensor.active=1;
    fa.getSensor(sensor,"#text_error_message",afterSensor);
  }
  function getLanguage(){
    fa.offset=null;fa.rows=null;
    var language=new faLanguage();
    fa.language_list.length=0;
    fa.getLanguage(language,null,afterLanguage);
  }
  function afterLanguage(){
    if(fa.language_list.length>0){
      var j,language;
      for(j=0;j<fa.langauge_list.length;j++){
        language=fa.language_list[j];
        $("#select_language").append("<option value="+language.id+">"+language.name+"</option>");
      }
    }
  }
  */
  function getCurrency(language){
    fa.offset=null;fa.rows=null;
    var currency=new faCurrency();
    currency.id="%";/*all currencies*/
    fa.currency_list.length=0;
    fa.getCurrency(currency,language,null,afterCurrency);
  }
  function afterCurrency(){
    var name,description,title=null,about=null;
    if(fa.currency_list.length>0){
      var j,k,currency,attr;
      for(j=0;j<fa.currency_list.length;j++){
        currency=fa.currency_list[j];
        if(parseInt(currency.active)===1){
          name=currency.name;description=currency.description;
          if(currency.attr_list.length>0){
            for(k=0;k<currency.attr_list.length;k++){
              attr=currency.attr_list[k];
              if(attr.name==="title")title=attr.value;
              else if(attr.name==="about")about=attr.value;
            }
          }
          active_currency=currency;
        }
      }
    }
    currency_title=(title!==null?title:name);
    currency_about=(about!==null?about:description);
  }
  function getUser(language){
    fa.offset=null;fa.rows=null;
    var user=new faUser();
    user.username=fa.username;
    fa.user_list.length=0;
    fa.getUser(user,language,null,afterUser);
  }
  function afterUser(){
    if(fa.user_list.length>0){
      current_user=fa.user_list[0];
      user_type=parseInt(fa.user_list[0].type);/*one of user*/
      call_name=fa.user_list[0].call_name;
    }
  }
  function getOrderStatusAttrPart(language){
    fa.offset=null;fa.rows=null;
    var attr_part=new faAttrPart();
    attr_part.object_id="%";/*all parts*/
    attr_part.object_name="order_status";
    attr_part.language=language;
    fa.attr_part_list.length=0;
    fa.getAttrPart(attr_part,null,afterOrderStatusAttrPart);
  }
  function afterOrderStatusAttrPart(){
    if(fa.attr_part_list.length>0){
      order_status_attr_part_list.length=0;
      order_status_attr_part_list=order_status_attr_part_list.concat(fa.attr_part_list);
    }
  }
  function getToken(){
    fa.temp_list.length=0;
    fa.getToken(null,afterToken);
  }
  function afterToken(){
    if(fa.temp_list.length>0){
      fa.token=fa.temp_list[0];
      setCookie(COOKIE_CURRENT_TOKEN,fa.token,365);
    }
  }
  /*parse*/
  function parseTypeList(type_list){/*<title (about)>*/
    var ret_val="",attr,type,title,about;
    if(type_list.length>0){
      var k,l;
      for(k=0;k<type_list.length;k++){
        type=type_list[k];
        if(type.attr_list.length>0){
          title="";about="";
          for(l=0;l<type.attr_list.length;l++){
            attr=type.attr_list[l];
            if(attr.name==="title")title=attr.value;
            else if(attr.name==="about")about=attr.value;
          }
          ret_val+=title+(about!==""?" ("+about+") ":about);
        }
        else{
          ret_val+=type.name+(type.description!==""?" ("+type.description+") ":type.description);
        }
      }
    }
    return ret_val;
  }
  function parseAttrList(attr_list){/*<title (about)>*/
    var ret_val="",attr,title="",about="";
    if(attr_list.length>0){
      for(var l=0;l<attr_list.length;l++){
        attr=attr_list[l];
        if(attr.name==="title")title=attr.value;
        else if(attr.name==="about")about=attr.value;
      }
      ret_val=title+(about!==""?" ("+about+") ":about);
    }
    return ret_val;
  }
  function parseAttrPartByObjectId(attr_list,object_id){/*<title (about)>*/
    var ret_val="",attr,title="",about="";
    if(attr_list.length>0){
      for(var l=0;l<attr_list.length;l++){
        attr=attr_list[l];
        if(parseInt(attr.object_id)===parseInt(object_id)){
          if(attr.name==="title")title=attr.value;
          else if(attr.name==="about")about=attr.value;
        }
      }
      ret_val=title+(about!==""?" ("+about+") ":about);
    }
    return ret_val;
  }

  function parseProduct(product){
    var ret_val="",attr_list,product_type_list;
    attr_list=product.attr_list;
    if(attr_list.length>0){
      ret_val=parseAttrList(attr_list);
    }
    else{
      ret_val=product.name+(product.description!==""?" ("+product.description+") ":product.description)+"\r\n";
    }
    product_type_list=product.type_list;
    ret_val+=parseTypeList(product_type_list)+" "+product.price+"\r\n";
    return "["+product.code+"] "+ret_val;
  }
  function parseProductParam(param_list){
    var ret_val="";
    if(param_list.length>0){
      var k,l,param,value,attr,title;
      for(k=0;k<param_list.length;k++){
        param=param_list[k];
        value=(param.value!==""?" ["+param.value+"]":
              (param.product_param.value!==""?" ["+param.product_param.value+"]":""));
        if(param.product_param.attr_list.length>0){
          title="";
          for(l=0;l<param.product_param.attr_list.length;l++){
            attr=param.product_param.attr_list[l];
            if(attr.name==="title")title=attr.value;
          }
          ret_val+=title+value+" "+param.price+"\r\n";
        }
        else{
          ret_val+=param.product_param.name+value+" "+param.price+"\r\n";
        }
      }
    }
    return ret_val;
  }
  function parseProductDiscount(product,language,currency){
    var ret_val="";
    if(product.discount){
      var curr_time=new Date().getTime(),is_actual=true;
      if(product.discount.start_date.length>0&&product.discount.finish_date.length>0){
        if(curr_time<parseMySQLDatetime(product.discount.start_date).getTime()||
           curr_time>parseMySQLDatetime(product.discount.finish_date).getTime())is_actual=false;
      }
      if(is_actual){
        ret_val=parseAttrList(product.discount.attr_list);
        ret_val+="\r\n"+getDiscountByType(product.discount.type,product.discount.value,language,currency);
      }
      else ret_val=product.discount.name;
    }
    return ret_val;
  }
  function parseUserDiscount(user,language,currency){
    var ret_val="";
    if(user.discount){
      var curr_time=new Date().getTime(),is_actual=true;
      if(user.discount.start_date.length>0&&user.discount.finish_date.length>0){
        if(curr_time<parseMySQLDatetime(user.discount.start_date).getTime()||
           curr_time>parseMySQLDatetime(user.discount.finish_date).getTime())is_actual=false;
      }
      if(is_actual)ret_val=getDiscountByType(user.discount.type,user.discount.value,language,currency);
    }
    return ret_val;
  }

  /*map*/
  function showMap(canvas,lat,lng,zoom,callback,callback_ptr){
    if(fa.map_provider===MAP_PROVIDER_YANDEX){
      ymaps.ready(function(){
        callback(fa.createMap(canvas,lat,lng,zoom),callback_ptr);
      });
    }
    else{
      callback(fa.createMap(canvas,lat,lng,zoom),callback_ptr);
    }
  }
  function showOrderRoute(map,order,no_fit){
    var marker_A=fa.createMarker(map,order.order_lat,order.order_lon,picture_marker_A,marker_route_width,marker_route_height,"A",++map_marker_index,null,order);
    var marker_B=fa.createMarker(map,order.delivery_lat,order.delivery_lon,picture_marker_B,marker_route_width,marker_route_height,"B",++map_marker_index,null,order);
    info=getOrderInfo(fa.language,order);
    fa.addMarkerWindow(marker_A,info,marker_max_window_width);
    fa.addMarkerWindow(marker_B,info,marker_max_window_width);
    if(!no_fit)fa.fitLocation(map,parseFloat(order.order_lat),parseFloat(order.order_lon),parseFloat(order.delivery_lat),parseFloat(order.delivery_lon));
    var route_data=order.route_data!==''?Base64.decode(order.route_data):'';
    if(route_data!==''){
      var data=route_data.split(";");
      var route_points=[],point,instructions="";
      var point_lat,point_lon;
      var instr;

      for(var j=0;j<data.length-1;j=j+7){
        point_lat=data[j];point_lon=data[j+1];
        if(point_lat&&point_lon){
          point=[point_lat,point_lon];
          route_points.push(point);
        }
        try{instr=Base64.decode(data[j+6])}catch(e){instr="";}
        instructions+=instr;
      }
      j=j-7;

      point_lat=data[j+2];point_lon=data[j+3];
      if(point_lat&&point_lon){
        point=[point_lat,point_lon];
        route_points.push(point);
      }

      fa.createPolyline(map,route_points,color_blue,opacity_light,weight_middle,++map_polyline_index,order);
      /*xls file:alert(instructions);*/
    }
  }

  /*find tools*/
  function getOrderById(order_id){
    var j,order,ret_val=null;
    for(j=0;j<fa.order_AB_list.length;j++){
      order=fa.order_AB_list[j];
      if(parseInt(order.id)===parseInt(order_id)){
        ret_val=order;
        break;
      }
    }
    return ret_val;
  }
  function getTransportById(transport_id){
    var j,transport,ret_val=null;
    for(j=0;j<fa.transport_list.length;j++){
      transport=fa.transport_list[j];
      if(parseInt(transport.id)===parseInt(transport_id)){
        ret_val=transport;
        break;
      }
    }
    return ret_val;
  }
  function getOrderTransport(order_id){
    var order=getOrderById(order_id),ret_val=null;
    if(order!==null){
      if(order.transport)ret_val=order.transport;
    }
    return ret_val;
  }
  function getOrderDriverId(order_id){
    var order=getOrderById(order_id),ret_val=null;
    if(order!==null){
      if(order.transport&&order.transport.sensor){
        /*beautiful example*/
        ret_val=order.getTransport().getSensor().getUser().id;
        /*ret_val=order.transport.sensor.user.id;*/
      }
    }
    return ret_val;
  }
  function getMapMarkerByOrderId(order_id){
    var j,map_marker,ret_val=null;
    for(j=0;j<fa.marker_list.length;j++){
      map_marker=fa.marker_list[j];
      if(map_marker.order!==null&&parseInt(map_marker.order.id)===parseInt(order_id)){
        ret_val=map_marker;
        break;
      }
    }
    return ret_val;
  }
  function getProductById(product_id){
    var j,product,ret_val=null;
    for(j=0;j<fa.product_list.length;j++){
      product=fa.product_list[j];
      if(parseInt(product.id)===parseInt(product_id)){
        ret_val=product;
        break;
      }
    }
    return ret_val;
  }

  /*add, remove circles*/

  /*add circle driver to customer*/
  function addCircleByOrderId(order_id){
    var order=getOrderById(order_id);
    if(order!==null&&order.transport&&order.transport.sensor){
      fa.addSensorCircleToUser(order.transport.sensor.id,order.user_id,null,null);
    }
  }
  /*remove circle driver to customer*/
  function removeCircleByOrderId(order_id){
    var order=getOrderById(order_id);
    if(order!==null&&order.transport&&order.transport.sensor){
      fa.removeSensorCircleToUser(order.transport.sensor.id,order.user_id,null,null);
    }
  }

  function addSensorCircle(transport_id){
    var j,transport;
    for(j=0;j<fa.transport_list.length;j++){
      transport=fa.transport_list[j];
      if(transport.sensor&&parseInt(transport.id)===parseInt(transport_id)){
        fa.addSensorCircleToUser(transport.sensor.id,fa.user_list[0].id,null,null);
        break;
      }
    }
  }
  function removeSensorCircle(transport_id){
    var j,transport;
    for(j=0;j<fa.transport_list.length;j++){
      transport=fa.transport_list[j];
      if(transport.sensor&&parseInt(transport.id)===parseInt(transport_id)){
        fa.removeSensorCircleToUser(transport.sensor.id,fa.user_list[0].id,null,null);
        break;
      }
    }
  }

  /*report*/
  function showReportFilepath(){
    var length=fa.ret_val_list.length;
    if(length>0){
      var ret_val=fa.ret_val_list[length-1];
      if(ret_val.name==="filepath"){
        window.location=fa.getHostname()+"//"+ret_val.value;/*$.fileDownload(ret_val.value);  window.open=ret_val.value*/
      }
    }
  }

  /*END_OF_INNOCENCE*/