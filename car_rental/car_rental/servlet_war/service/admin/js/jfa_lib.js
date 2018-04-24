/*  fireArt. open source CRM.
 *  Good Ideas for Bussiness!
 *
 *  fireArt library jscript ver.2.0
 *
 *  ABTO3BIT 2017 all rights reserved
 */

/*var encodedString = Base64.encode(string);var decodedString = Base64.decode(encodedString);*/
var Base64={_keyStr:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",encode:function(e){var t="";var n,r,i,s,o,u,a;var f=0;e=Base64._utf8_encode(e);while(f<e.length){n=e.charCodeAt(f++);r=e.charCodeAt(f++);i=e.charCodeAt(f++);s=n>>2;o=(n&3)<<4|r>>4;u=(r&15)<<2|i>>6;a=i&63;if(isNaN(r)){u=a=64;}else if(isNaN(i)){a=64;}t=t+this._keyStr.charAt(s)+this._keyStr.charAt(o)+this._keyStr.charAt(u)+this._keyStr.charAt(a)}return t;},decode:function(e){var t="";var n,r,i;var s,o,u,a;var f=0;e=e.replace(/[^A-Za-z0-9\+\/\=]/g,"");while(f<e.length){s=this._keyStr.indexOf(e.charAt(f++));o=this._keyStr.indexOf(e.charAt(f++));u=this._keyStr.indexOf(e.charAt(f++));a=this._keyStr.indexOf(e.charAt(f++));n=s<<2|o>>4;r=(o&15)<<4|u>>2;i=(u&3)<<6|a;t=t+String.fromCharCode(n);if(u!=64){t=t+String.fromCharCode(r);}if(a!=64){t=t+String.fromCharCode(i);}}t=Base64._utf8_decode(t);return t;},_utf8_encode:function(e){e=e.replace(/\r\n/g,"\n");var t="";for(var n=0;n<e.length;n++){var r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r);}else if(r>127&&r<2048){t+=String.fromCharCode(r>>6|192);t+=String.fromCharCode(r&63|128);}else{t+=String.fromCharCode(r>>12|224);t+=String.fromCharCode(r>>6&63|128);t+=String.fromCharCode(r&63|128);}}return t;},_utf8_decode:function(e){var t="";var n=0;var r=c1=c2=0;while(n<e.length){r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r);n++;}else if(r>191&&r<224){c2=e.charCodeAt(n+1);t+=String.fromCharCode((r&31)<<6|c2&63);n+=2;}else{c2=e.charCodeAt(n+1);c3=e.charCodeAt(n+2);t+=String.fromCharCode((r&15)<<12|(c2&63)<<6|c3&63);n+=3;}}return t;}};
/*
var fa=new fA();

fa.map_provider="osm";
fa.language="ENGLISH";
fa.timezone_offset="+03:00";

class a
function a(){}
method b of the class a
a.prototype.b = function(){}

a_ptr=new a();
if(a_ptr instanceof a)alert('true');

var call = eval("method_name").call(args);

class c override the class a
function c(){}
c.prototype=a;

a new calling to win: order.getTransport().getSensor().getUser().id
*/

  /*objects*/
  function faAudit(){
    this.id=null;
    this.name=null;
    this.description=null;
    this.username=null;
    this.message=null;
    this.create_date=null;
    this.last_update=null;
  }
  function faSettings(){
    this.id=null;/*ap*/
    this.code=null;
    this.name=null;
    this.value=null;
  }
  function faProject(){
    this.id=null;/*ap*/
    this.api_key=null;
    this.schema_name=null;
    this.active=null;
    this.amount_paid=null;
    this.currency=null;
    this.name=null;
    this.description=null;
    this.ip_address=null;
    this.create_date=null;
    this.last_update=null;
  }
  /*object_type ext: product_type, transport_type*/
  function faObjectTypeExt(){
    this.id=null;
    this.parent_id=null;
    this.name=null;
    this.description=null;
    this.language=null;
    this.last_update=null;
    this.attr_list=[];
  }
  /*object_type: delivery_type, track_type, order_status*/
  function faObjectType(){
    this.id=null;
    this.temp_id=null;/*ap*/
    this.name=null;
    this.language=null;
    this.last_update=null;
    this.attr_list=[];
  }
  /*object_name: attr,color*/
  function faObjectName(){
    this.name=null;
  }
  /*object_review: product_review, transport_review*/
  function faObjectReview(){
    this.id=null;/*ap*/
    this.order_id=null;/*review by order*/
    this.user_id=null;
    this.user=null;/*class faUser*/
    this.object_id=null;
    this.object=null;/*class of reviewer Object*/
    this.description=null;
    this.value=null;
    this.last_update=null;
    this.getUser=function(){return this.user;};
    this.getObject=function(){return this.object;};
  }

  /*MANUFACTURE*/
  function faManufacture(){
    this.id=null;
    this.name=null;
    this.description=null;
    this.picture=null;
    this.last_update=null;
    this.attr_list=[];
  }
  /*PRODUCT*/
  function faProduct(){
    this.id=null;
    this.manufacture_id=null;
    this.discount_code=null;
    this.discount=null;/*class faDiscount*/
    this.name=null;
    this.description=null;
    this.code=null;
    this.price=null;
    this.picture=null;
    this.rate=null;
    this.stock_count=null;
    this.last_update=null;
    this.attr_list=[];
    this.type_list=[];
    this.param_list=[];
    this.getDiscount=function(){return this.discount;};
    this.shopping_cart_count=0;
  }
  function faProductParam(){
    this.id=null;
    this.parent_id=null;
    this.name=null;
    this.value=null;
    this.picture=null;
    this.language=null;
    this.last_update=null;
    this.attr_list=[];
  }
  function faProductParamPart(){/*using as faOrderABProductParamPart for order*/
    this.id=null;/*ap*/
    this.product_id=null;
    this.product_param_id=null;
    this.product_param=null;/*class faProductParam*/
    this.value=null;
    this.count=null; /*for order (not used faOrderABProductParamPart object)*/
    this.price=null;
    this.picture=null;
    this.last_update=null;
    this.getProductParam=function(){return this.product_param;};
  }
  function faProductTypePart(){
    this.id=null;/*ap*/
    this.product_id=null;
    this.product_type_id=null;
    this.product_type=null;/*class faObjectTypeExt*/
    this.last_update=null;
    this.getProductType=function(){return this.product_type;};
  }
  function faProductCircle(){
    this.productA_id=null;
    this.productB_id=null;
    this.productB=null;/*class faProduct*/
    this.create_date=null;
    this.getProductB=function(){return this.productB;};
  }
  /*DELIVERY*/
  function faDelivery(){
    this.id=null;
    this.user_id=null;
    this.first_name=null;
    this.last_name=null;
    this.call_name=null;
    this.user_phone=null;
    this.transport_id=null;
    this.transport_name=null;
    this.transport_color=null;
    this.license_plate=null;
    this.latitude=null;
    this.longitude=null;
    this.address=null;
    this.create_date=null;
    this.last_update=null;
  }
  /*TRANSPORT*/
  function faTransport(){
    this.id=null;
    this.user_id=null;
    this.sensor_id=null;
    this.sensor_active=null;
    this.sensor=null;/*class faSensor*/
    this.name=null;
    this.color=null;
    this.license_plate=null;
    this.picture=null;
    this.rate=null;
    this.reserved=null;
    this.last_update=null;
    this.type_list=[];
    this.getSensor=function(){return this.sensor;};
  }
  function faTransportProductPart(){
    this.transport_id=null;
    this.product_id=null;
    this.product=null;/*class faProduct*/
    this.last_update=null;
    this.getProduct=function(){return this.product;};
  }
  function faTransportTypePart(){
    this.id=null;/*ap*/
    this.transport_id=null;
    this.transport_type_id=null;
    this.transport_type=null;/*class faObjectTypeExt*/
    this.last_update=null;
    this.getTransportType=function(){return this.transport_type;};
  }
  /*STORE*/
  function faStore(){
    this.id=null;
    this.name=null;
    this.description=null;
    this.picture=null;
    this.active=null;
    this.last_update=null;
    this.attr_list=[];
    this.part_list=[];
  }
  function faStorePart(){
    this.id=null;
    this.store_id=null;
    this.name=null;
    this.description=null;
    this.picture=null;
    this.latitude=null;
    this.longitude=null;
    this.email=null;
    this.phone1=null;
    this.phone2=null;
    this.address=null;
    this.city=null;
    this.postcode=null;
    this.language=null;
    this.last_update=null;
  }
  /*ORDER*/
  /*order_status=faObjectType*/
  function faOrderAB(){
    this.id=null;
    this.user_id=null;
    this.user=null;/*class faUser*/
    this.transport_id=null;
    this.transport=null;/*class faTransport*/
    this.order_status_id=null;
    this.order_status_name=null;
    this.total_price=null;
    this.route_distance=null;
    this.route_duration=null;
    this.route_data=null;
    this.order_lat=null;
    this.order_lon=null;
    this.order_address=null;
    this.delivery_lat=null;
    this.delivery_lon=null;
    this.delivery_address=null;
    this.delivery_type_id=null;
    this.delivery_type_name=null;
    this.reserved_date=null;
    this.reserved_hours=null;
    this.create_date=null;
    this.last_update=null;
    this.part_list=[];/*not used*/
    this.status_attr_list=[];
    this.delivery_type_attr_list=[];
    this.purchase_list=[];
    this.getUser=function(){return this.user;};
    this.getTransport=function(){return this.transport;};
  }
  function faOrderABPart(){/*faOrderABProductPart*/
    this.id=null;/*ap*/
    this.order_id=null;
    this.product_id=null;
    this.product=null;/*class faProduct*/
    this.total_price=null;
    this.product_count=null;
    this.last_update=null;
    this.getProduct=function(){return this.product;};
  }
  function faOrderABProductPart(){}
  faOrderABProductPart.prototype=faOrderABPart;
  function faOrderABUserPart(){
    this.id=null;/*ap*/
    this.order_id=null;
    this.user_id=null;
    this.product_param_id_1=null;
    this.product_param_id_2=null;
    this.pickup_status_id=null;
    this.pickup_lat=null;
    this.pickup_lon=null;
    this.pickup_address=null;
    this.pickup_date=null;
    this.pickup_info=null;
    this.price=null;
    this.create_date=null;
    this.last_update=null;
  }
  function faOrderABProductParamPart(){/*not used (for order using faProductParamPart)*/
    this.order_id=null;
    this.product_id=null;
    this.product_param_id=null;
    this.count=null;
    this.price=null;
    this.last_update=null;
  }
  /*STOCK*/
  function faStock(){
    this.id=null;/*ap*/
    this.product_id=null;
    this.store_id=null;
    this.count=null;
    this.last_update=null;
  }
  function faStockInvoice(){
    this.id=null;
    this.invoice_code=null;
    this.invoice_date=null;
    this.total_price=null;
    this.total_tax=null;
    this.supplier=null;
    this.phone=null;
    this.delivery_id=null;
    this.delivery_code=null;
    this.delivery_type_id=null;
    this.delivery_type_name=null;
    this.delivery_date=null;
    this.delivery_price=null;
    this.payment_date=null;
    this.payment_info=null;
    this.payment_amount=null;
    this.create_date=null;
    this.last_update=null;
  }
  function faStockInvoicePart(){
    this.id=null;/*ap*/
    this.stock_invoice_id=null;
    this.product_id=null;
    this.tax_id=null;
    this.count=null;
    this.price=null;
    this.last_update=null;
  }
  /*PURCHASE*/
  function faPurchase(){
    this.id=null;
    this.user_id=null;
    this.invoice_code=null;
    this.invoice_date=null;
    this.total_price=null;
    this.total_tax=null;
    this.order_id=null;
    this.order_date=null;
    this.order_info=null;
    this.delivery_id=null;
    this.delivery_code=null;
    this.delivery_type_id=null;
    this.delivery_type_name=null;
    this.delivery_date=null;
    this.delivery_price=null;
    this.payment_date=null;
    this.payment_info=null;
    this.payment_amount=null;
    this.create_date=null;
    this.last_update=null;
    this.delivery_type_attr_list=[];
  }
  /*PAYMENT*/
  function faPayment(){
    this.id=null;/*ap*/
    this.purchase_id=null;
    this.amount=null;
    this.currency=null;
    this.description=null;
    this.status=null;
    this.transaction_id=null;
    this.phone=null;
    this.create_date=null;
    this.last_update=null;
  }
  /*PREPAID CARD*/
  function faPrepaidCard(){
    this.id=null;
    this.serial_number=null;
    this.prepaid_code=null;
    this.amount=null;
    this.active=null;
    this.create_date=null;
  }
  /*SENSOR*/
  function faSensor(){
    this.id=null;
    this.user_id=null;
    this.user=null;/*class faUser*/
    this.name=null;
    this.serial_number=null;
    this.device_name=null;
    this.phone=null;
    this.active=null;
    this.create_date=null;
    this.last_update=null;
    this.sensor_circle=[];
    this.getUser=function(){return this.user;};
  }
  function faSensorCircle(){
    this.id=null;/*ap*/
    this.sensorA_id=null;
    this.sensorA=null;/*class faSensor*/
    this.sensorB_id=null;
    this.sensorB=null;/*class faSensor*/
    this.create_date=null;
    this.getSensorA=function(){return this.sensorA;};
    this.getSensorB=function(){return this.sensorB;};
  }
  function faSensorGroup(){
    this.sensor_id=null;
    this.group_name=null;
    this.start_date=null;
    this.finish_date=null;
    this.create_date=null;
    this.last_update=null;
  }
  function faSensorGroupPart(){
    this.group_id=null;
    this.sensor_id=null;
  }
  function faSensorPlace(){
    this.id=null;
    this.sensor_id=null;
    this.latitude=null;
    this.longitude=null;
    this.name=null;
    this.description=null;
    this.language=null;
    this.picture=null;
    this.last_update=null;
  }
  /*TRACK*/
  function faTrack(){
    this.id=null;
    this.user_id=null;
    this.user_callname=null;
    this.user_phone=null;
    this.sensor_id=null;
    this.sensor=null;/*class faSensor*/
    this.transport_id=null;
    this.transport=null;/*class faTransport*/
    this.type_id=null;
    this.type_name=null;
    this.latitude=null;
    this.longitude=null;
    this.time=null;
    this.altitude=null;
    this.accuracy=null;
    this.bearing=null;
    this.speed=null;
    this.satellites=null;
    this.battery=null;
    this.timezone_offset=null;
    this.create_date=null;
    this.type_attr_list=[];
    this.part_list=[];
    this.getSensor=function(){return this.sensor;};
    this.getTransport=function(){return this.transport;};
  }
  function faTrackPart(){
    this.id=null;
    this.track_id=null;
    this.name=null;
    this.description=null;
    this.language=null;
    this.picture=null;
    this.last_update=null;
  }
  /*USER*/
  function faUser(){
    this.id=null;
    this.discount_code=null;
    this.discount=null;/*class faDiscount*/
    this.type=null;
    this.first_name=null;
    this.last_name=null;
    this.call_name=null;
    this.email=null;
    this.phone=null;
    this.prepaid_amount=null;
    this.rate=null;
    this.username=null;
    this.password=null;
    this.picture=null;
    this.active=null;
    this.create_date=null;
    this.last_update=null;
  }
  /*ATTR*/
  function faAttrPart(){
    this.id=null;
    this.object_id=null;
    this.object_name=null;
    this.name=null;
    this.value=null;
    this.language=null;
    this.last_update=null;
  }
  /*DISCOUNT*/
  function faDiscount(){
    this.id=null;
    this.product_type_id=null;
    this.type=null;
    this.code=null;
    this.name=null;
    this.value=null;
    this.language=null;
    this.start_date=null;
    this.finish_date=null;
    this.create_date=null;
    this.last_update=null;
    this.attr_list=[];
  }
  /*CURRENCY*/
  function faCurrency(){
    this.id=null;
    this.name=null;
    this.description=null;
    this.value=null;
    this.active=null;
    this.language=null;
    this.last_update=null;
    this.attr_list=[];
  }
  /*LANGUAGE*/
  function faLanguage(){
    this.id=null;
    this.temp_id=null;/*ap*/
    this.name=null;
    this.value=null;
    this.code=null;
    this.active=null;
  }
  /*TAX*/
  function faTax(){
    this.id=null;
    this.code=null;
    this.name=null;
    this.value=null;
    this.language=null;
    this.last_update=null;
    this.attr_list=[];
  }
  /*MESSAGE*/
  function faMessage(){
    this.id=null;
    this.type=null;
    this.user_id=null;/*finder param*/
    this.recipient=null;/*ap*/
    this.userA_id=null;
    this.userA=null;/*class faUser*/
    this.userB_id=null;
    this.userB=null;/*class faUser*/
    this.message=null;
    this.create_date=null;
    this.getUserA=function(){return this.userA;};
    this.getUserB=function(){return this.userB;};
  }

  /*others*/
  function retVal(){
    this.status=null;
    this.session_id=null;
    this.message=null;
    this.message_code=null;
    this.database_message=null;
    this.name=null;
    this.value=null;
  }
  function repVal(){
    this.id=null;
    this.date_from=null;
    this.date_to=null;
  }
  function projectVal(){/*function is_project results*/
    this.project_created=null;
    this.add_project_audit=null;
  }
  function foundRowsObject(){
    this.object_name=null;
    this.found_rows=null;
  }

  /*Google, Yandex providers Route Directions service
    varnames in Java style : firstnameLastname(one's time)*/
  function orderRoute(){
    this.data=null;
    this.distance=null;
    this.duration=null;
    this.distanceText=null;
    this.durationText=null;
    this.startAddress=null;
    this.finishAddress=null;
    this.startLocationLat=null;
    this.startLocationLon=null;
    this.finishLocationLat=null;
    this.finishLocationLon=null;
    this.error=null;
  }

  function mapMarker(){
    this.id=null;
    this.track=null;/*class faTrack*/
    this.order=null;/*class faOrderAB*/
    this.map=null;
    this.marker=null;
    this.latitude=null;
    this.longitude=null;
    this.image_width=null;
    this.image_height=null;
  }
  function mapPolyline(){
    this.id=null;
    this.order=null;/*class faOrderAB*/
    this.map=null;
    this.polyline=null;
    this.list=null;
  }

  /*main*/
  function fA(hostname){

    /*const*/
    var EMPTY="";
    var SPACE=" ";
    var POINT=".";
    var DOUBLE_POINT=":";
    var UPPER="\'";
    var DOUBLE_UPPER="\"";
    var OPEN_DOOR="(";
    var CLOSE_DOOR=")";

    var DEFAULT_YANDEX_ZOOM=18;
    var DEFAULT_YANDEX_FIXED=20;
    var DEFAULT_YANDEX_MARKER_OFFSET=[-16,-30];/*for x32, [-32,-60] for x64*/
    var DEFAULT_OSM_MARKER_OFFSET=[16,30];/*for x32, [32,60] for x64*/

    var provider_list=["google","yandex","osm"];

    /*discount*/
    this.DISCOUNT_IN_PERCENT=1;
    this.DISCOUNT_IN_VALUE=2;
    this.INCREASE_IN_PERCENT=3;
    this.INCREASE_IN_VALUE=4;

    /*main*/
    var version="2.0";
    var api_key=null;
    this.username=null;
    this.password=null;
    this.token=null;
    this.map_provider=null; /*provider of map*/
    this.language=null; /*language support*/
    this.timezone_offset=null; /*getTimezoneOffsetString();*/ /*client's timezone offset*/

    this.offset=null; /*offset position of row*/
    this.rows=null; /*selected rows count*/

    var getLimit=function(offset,rows){
      var ret_val={};
      if(offset!==null){ret_val=$.extend(ret_val,{'offset':offset});}
      if(rows!==null)ret_val=$.extend(ret_val,{'rows':rows});
      return ret_val;
    };

    /*last found rows*/
    var found_rows=null;/*found rows for limit*/
    this.getFoundRows=function(){
      return found_rows;
    };

    /*found_rows history for object's list (actual for multiply queries)*/
    var save_found_rows=false;
    this.isSaveFoundRows=function(){
      return save_found_rows;
    }
    this.setSaveFoundRows=function(save){
      save_found_rows=true;
    }
    var found_rows_objects=[];
    this.getFoundRowsObjects=function(){
      return found_rows_objects;
    }
    var saveFoundRows=function(found_rows,save_ptr){
      var found_rows_object=new foundRowsObject();
      found_rows_object.found_rows=found_rows;
      found_rows_object.object_name=save_ptr;
      found_rows_objects.push(found_rows_object);
    };

    /*query result*/
    var query_status=null;
    var result_database_message=null;
    var result_message_code=null;
    var result_message=null;
    var result_value=null;
    this.getQueryStatus=function(){
      return query_status;
    };
    this.getResultDatabaseMessage=function(){
      return result_database_message;
    };
    this.getResultMessageCode=function(){
      return result_message_code;
    };
    this.getResultMessage=function(){
      return result_message;
    };
    this.getResultValue=function(){
      return result_value;
    };

    /*order_route*/
    this.order_route_list=[];

    /*map*/
    var current_marker=null;/*last clicked marker after addMarkerWindow*/
    this.getCurrentMarker=function(){
      return current_marker;
    };

    this.map_list=[];
    this.marker_list=[];/*list of class mapMarker*/
    this.polyline_list=[];/*list of class mapPolyline*/
    /*this.marker_listener_list=[];*/

    this.removeFromMarkerList=function(map_marker){
      var m;
      for(var j=0;j<this.marker_list.length;j++){
        m=this.marker_list[j];
        if(m&&m===map_marker){this.marker_list.splice(j,1);break;}
      }
    };
    this.removeFromPolylineList=function(map_polyline){
      var p;
      for(var j=0;j<this.polyline_list.length;j++){
        p=this.polyline_list[j];
        if(p&&p===map_polyline){this.polyline_list.splice(j,1);break;}
      }
    };
    this.getMapMarker=function(marker){
      var m,ret_val=null;
      for(var j=0;j<this.marker_list.length;j++){
        m=this.marker_list[j];
        if(m&&m.marker===marker){ret_val=m;break;}
      }
      return ret_val;
    };

/*
Google(waypoints) <script type="text/javascript" src="http://maps.google.com/maps/api/js?language=en|ru|uk"></script>
*/
    this.routeGoogle=function(from,to,message_ptr,callback,callback_ptr){
      var list=this.order_route_list;
      var directionsService=new google.maps.DirectionsService;
      directionsService.route({
        origin:from,
        destination:to,
        travelMode:google.maps.TravelMode.DRIVING
      },function(response,status){
        if(status===google.maps.DirectionsStatus.OK){
          /*response.routes[0].warnings;*/
          /*response.routes[0].overview_path;(Array<LatLng>)*/
          var route=response.routes[0].legs[0];
          var order_route=new orderRoute();
          order_route.distance=route.distance.value;
          order_route.duration=route.duration.value;
          order_route.distanceText=route.distance.text;
          order_route.durationText=route.duration.text;
          order_route.startAddress=route.start_address;
          order_route.finishAddress=route.end_address;
          order_route.startLocationLat=route.start_location.lat();
          order_route.startLocationLon=route.start_location.lng();
          order_route.finishLocationLat=route.end_location.lat();
          order_route.finishLocationLon=route.end_location.lng();
          /*route_format:
                         <start_lat>;<start_lng>;
                         <finish_lat>;<finish_lng>;
                         <distance>;<duration>;
                         <instructions>;*/
          var s=EMPTY;
          for(var i=0;i<route.steps.length;i++){
            s+=route.steps[i].start_location.lat()+";";
            s+=route.steps[i].start_location.lng()+";";
            s+=route.steps[i].end_location.lat()+";";
            s+=route.steps[i].end_location.lng()+";";
            s+=route.steps[i].distance.value+";";
            s+=route.steps[i].duration.value+";";
            s+=Base64.encode(route.steps[i].instructions)+";";
          }
          order_route.data=s;
          /*Encoded Polyline Algorithm Format
          order_route.data=response.routes[0].overview_polyline;*/

          list.push(order_route);
          if(callback!==null)callback(callback_ptr);
        }
        else{/*(INVALID_REQUEST,MAX_WAYPOINTS_EXCEEDED,NOT_FOUND,OVER_QUERY_LIMIT,REQUEST_DENIED,UNKNOWN_ERROR,ZERO_RESULTS)*/
          if(message_ptr!==null)$(message_ptr).html(status);
        }
      });
    };
/*
Yandex(waypoints)  <script src="http://api-maps.yandex.ru/2.1/?load=package.standard&lang==en_US|ru-RU|uk_UA" type="text/javascript"></script>
*/
    this.routeYandex=function(from,to,message_ptr,callback,callback_ptr){
      var list=this.order_route_list;
      ymaps.route([from,to]).then(function(router){
				var route=router;
        var order_route=new orderRoute();
        order_route.distance=route.getLength();
        order_route.duration=route.getTime();
        order_route.distanceText=route.getHumanLength();
        order_route.durationText=route.getHumanTime();
        var points=route.getWayPoints();
        order_route.startAddress=null;
        order_route.finishAddress=null;
        order_route.startLocationLat=points.get(0).geometry.getCoordinates()[0];
        order_route.startLocationLon=points.get(0).geometry.getCoordinates()[1];
        order_route.finishLocationLat=points.get(1).geometry.getCoordinates()[0];
        order_route.finishLocationLon=points.get(1).geometry.getCoordinates()[1];
        /*route_format:
                       <start_lat>;<start_lng>;
                       <finish_lat>;<finish_lng>;
                       <distance>;<duration>;
                       <instructions>;*/
        var way=route.getPaths().get(0);
        var s=EMPTY,segments=way.getSegments();
        for(var i=0;i<segments.length;i++){
          points=segments[i].getCoordinates();
          s+=points[0][0]+";";
          s+=points[0][1]+";";
          s+=points[points.length-1][0]+";";
          s+=points[points.length-1][1]+";";
          s+=segments[i].getLength()+";";
          s+=segments[i].getTime()+";";
          s+=Base64.encode(segments[i].getStreet()+" "+segments[i].getHumanAction())+";";
        }
        order_route.data=s;

        list.push(order_route);
        if(callback!==null)callback(callback_ptr);
      },
      function(error){
        if(message_ptr!==null)$(message_ptr).html(error.message);
      });
    };

    /*cookies*/
    this.updateCookies=function(){
      document.cookie="TIMEZONE_OFFSET="+escape(this.timezone_offset);
      document.cookie="MAP_PROVIDER="+this.map_provider;
    };

    /*list of objects*/
    this.stat_list=[];
    this.audit_list=[];
    this.settings_list=[];
    this.project_list=[];

    this.temp_list=[];
    this.attr_list=[];
    this.color_list=[];
    this.language_list=[];
    this.attr_part_list=[];

    this.delivery_type_list=[];
    this.track_type_list=[];
    this.product_param_list=[];
    this.product_type_list=[];
    this.transport_type_list=[];
    this.user_review_list=[];
    this.product_review_list=[];
    this.transport_review_list=[];

    this.product_param_part_list=[];
    this.product_type_part_list=[];
    this.transport_type_part_list=[];

    this.usertype_list=[];
    this.user_list=[];
    this.discount_list=[];
    this.tax_list=[];
    this.sensor_list=[];
    this.sensor_circle_list=[];
    this.order_AB_list=[];
    this.order_AB_part_list=[];
    this.order_AB_product_part_list=this.order_AB_part_list;
    this.order_AB_user_part_list=[];
    this.order_status_list=[];
    this.pickup_status_list=[];
    this.purchase_list=[];
    this.purchase_part_list=[];
    this.payment_list=[];
    this.product_list=[];
    this.prepaid_card_list=[];
    this.stock_list=[];
    this.stock_invoice_list=[];
    this.stock_invoice_part_list=[];
    this.store_list=[];
    this.store_part_list=[];
    this.manufacture_list=[];
    this.currency_list=[];
    this.transport_list=[];
    this.transport_product_part_list=[];
    this.message_list=[];
    this.track_list=[];

    /*return values list*/
    this.ret_val_list=[];

    /*shopping cart (list of faOrderABPart and faOrderABProductParamPart)*/
    this.shopping_cart_list=[];

    /*picture objects*/
    this.picture_manufacture="manufacture";
    this.picture_product="product";
    this.picture_product_param="product_param";
    this.picture_sensor_place="sensor_place";
    this.picture_store="store";
    this.picture_store_part="store_part";
    this.picture_transport="transport";

    /*url*/
    var url_get_token=hostname+"/service/start?name=json/get_token";
    var url_get_stat=hostname+"/service/start?name=json/get_stat";
    var url_get_audit=hostname+"/service/start?name=json/get_audit";
    var url_get_settings=hostname+"/service/start?name=json/get_settings";
    var url_get_project=hostname+"/service/start?name=json/get_project";
    var url_is_project=hostname+"/service/start?name=json/is_project";

    var url_get_attr=hostname+"/service/start?name=json/get_attr";
    var url_get_color=hostname+"/service/start?name=json/get_color";
    var url_get_language=hostname+"/service/start?name=json/get_language";
    var url_get_attr_part=hostname+"/service/start?name=json/get_attr_part";

    var url_get_delivery_type=hostname+"/service/start?name=json/get_delivery_type";
    var url_get_track_type=hostname+"/service/start?name=json/get_track_type";
    var url_get_order_status=hostname+"/service/start?name=json/get_order_status";
    var url_get_pickup_status=hostname+"/service/start?name=json/get_pickup_status";
    var url_get_product_param=hostname+"/service/start?name=json/get_product_param";
    var url_get_product_type=hostname+"/service/start?name=json/get_product_type";
    var url_get_transport_type=hostname+"/service/start?name=json/get_transport_type";
    var url_get_user_review=hostname+"/service/start?name=json/get_user_review";
    var url_get_product_review=hostname+"/service/start?name=json/get_product_review";
    var url_get_transport_review=hostname+"/service/start?name=json/get_transport_review";

    var url_get_product_param_part=hostname+"/service/start?name=json/get_product_param_part";
    var url_get_product_type_part=hostname+"/service/start?name=json/get_product_type_part";
    var url_get_transport_type_part=hostname+"/service/start?name=json/get_transport_type_part";

    var url_get_picture=hostname+"/service/start?name=json/get_picture";
    var url_get_user_picture=hostname+"/service/start?name=json/get_user_picture";
    var url_get_product_param_part_picture=hostname+"/service/start?name=json/get_product_param_part_picture";
    var url_get_track_part_picture=hostname+"/service/start?name=json/get_track_part_picture";

    var url_get_user_type=hostname+"/service/start?name=json/get_user_type";
    var url_get_user=hostname+"/service/start?name=json/get_user";
    var url_get_discount=hostname+"/service/start?name=json/get_discount";
    var url_get_tax=hostname+"/service/start?name=json/get_tax";
    var url_get_sensor=hostname+"/service/start?name=json/get_sensor";
    var url_get_sensor_circle=hostname+"/service/start?name=json/get_sensor_circle";
    var url_get_order_AB=hostname+"/service/start?name=json/get_order_AB";
    var url_get_order_AB_part=hostname+"/service/start?name=json/get_order_AB_part";
    var url_get_order_AB_product_part=hostname+"/service/start?name=json/get_order_AB_product_part";
    var url_get_order_AB_user_part=hostname+"/service/start?name=json/get_order_AB_user_part";
    var url_get_purchase=hostname+"/service/start?name=json/get_purchase";
    var url_get_payment=hostname+"/service/start?name=json/get_payment";
    var url_get_prepaid_card=hostname+"/service/start?name=json/get_prepaid_card";
    var url_get_product=hostname+"/service/start?name=json/get_product";
    var url_get_stock=hostname+"/service/start?name=json/get_stock";
    var url_get_stock_invoice=hostname+"/service/start?name=json/get_stock_invoice";
    var url_get_stock_invoice_part=hostname+"/service/start?name=json/get_stock_invoice_part";
    var url_get_store=hostname+"/service/start?name=json/get_store";
    var url_get_store_part=hostname+"/service/start?name=json/get_store_part";
    var url_get_manufacture=hostname+"/service/start?name=json/get_manufacture";
    var url_get_currency=hostname+"/service/start?name=json/get_currency";
    var url_get_transport=hostname+"/service/start?name=json/get_transport";
    var url_get_transport_product_part=hostname+"/service/start?name=json/get_transport_product_part";
    var url_get_message=hostname+"/service/start?name=json/get_message";
    var url_get_max_circle_track=hostname+"/service/start?name=json/get_max_circle_track";
    var url_get_max_track=hostname+"/service/start?name=json/get_max_track";
    var url_get_track=hostname+"/service/start?name=json/get_track";

    var url_get_report=hostname+"/service/start?name=xls/";

    var url_add_settings=hostname+"/service/start?name=json/add_settings";
    var url_add_project=hostname+"/service/start?name=json/add_project";
    var url_add_user=hostname+"/service/start?name=json/add_user";
    var url_add_sensor=hostname+"/service/start?name=json/add_sensor";
    var url_add_order_AB=hostname+"/service/start?name=json/add_order_AB";
    var url_add_order_AB_part=hostname+"/service/start?name=json/add_order_AB_part";
    var url_add_order_AB_product_part=hostname+"/service/start?name=json/add_order_AB_product_part";
    var url_add_order_AB_user_part=hostname+"/service/start?name=json/add_order_AB_user_part";
    var url_add_order_AB_product_param_part=hostname+"/service/start?name=json/add_order_AB_product_param_part";
    var url_add_purchase=hostname+"/service/start?name=json/add_purchase";
    var url_add_payment=hostname+"/service/start?name=json/add_payment";
    var url_add_prepaid_card=hostname+"/service/start?name=json/add_prepaid_card";
    var url_add_product=hostname+"/service/start?name=json/add_product";
    var url_add_stock=hostname+"/service/start?name=json/add_stock";
    var url_add_stock_invoice=hostname+"/service/start?name=json/add_stock_invoice";
    var url_add_stock_invoice_part=hostname+"/service/start?name=json/add_stock_invoice_part";
    var url_add_store=hostname+"/service/start?name=json/add_store";
    var url_add_store_part=hostname+"/service/start?name=json/add_store_part";
    var url_add_manufacture=hostname+"/service/start?name=json/add_manufacture";
    var url_add_currency=hostname+"/service/start?name=json/add_currency";
    var url_add_transport=hostname+"/service/start?name=json/add_transport";
    var url_add_transport_product_part=hostname+"/service/start?name=json/add_transport_product_part";

    var url_add_product_circle=hostname+"/service/start?name=json/add_product_circle";
    var url_add_sensor_circle=hostname+"/service/start?name=json/add_sensor_circle";
    var url_add_sensor_circle_to_user=hostname+"/service/start?name=json/add_sensor_circle_to_user";
    var url_add_sensor_group_part=hostname+"/service/start?name=json/add_sensor_group_part";

    var url_add_message=hostname+"/service/start?name=json/add_message";

    var url_add_delivery_type=hostname+"/service/start?name=json/add_delivery_type";
    var url_add_track_type=hostname+"/service/start?name=json/add_track_type";
    var url_add_order_status=hostname+"/service/start?name=json/add_order_status";
    var url_add_pickup_status=hostname+"/service/start?name=json/add_pickup_status";
    var url_add_product_param=hostname+"/service/start?name=json/add_product_param";
    var url_add_product_type=hostname+"/service/start?name=json/add_product_type";
    var url_add_transport_type=hostname+"/service/start?name=json/add_transport_type";
    var url_add_user_review=hostname+"/service/start?name=json/add_user_review";
    var url_add_product_review=hostname+"/service/start?name=json/add_product_review";
    var url_add_transport_review=hostname+"/service/start?name=json/add_transport_review";

    var url_add_product_param_part=hostname+"/service/start?name=json/add_product_param_part";
    var url_add_product_type_part=hostname+"/service/start?name=json/add_product_type_part";
    var url_add_transport_type_part=hostname+"/service/start?name=json/add_transport_type_part";

    var url_add_attr=hostname+"/service/start?name=json/add_attr";
    var url_add_color=hostname+"/service/start?name=json/add_color";
    var url_add_language=hostname+"/service/start?name=json/add_language";
    var url_add_attr_part=hostname+"/service/start?name=json/add_attr_part";
    var url_add_discount=hostname+"/service/start?name=json/add_discount";
    var url_add_tax=hostname+"/service/start?name=json/add_tax";
    var url_add_track=hostname+"/service/start?name=json/add_track";

    var url_update_picture=hostname+"/service/start?name=json/update_picture";
    var url_update_user_picture=hostname+"/service/start?name=json/update_user_picture";
    var url_update_product_param_part_picture=hostname+"/service/start?name=json/update_product_param_part_picture";
    var url_update_track_part_picture=hostname+"/service/start?name=json/update_track_part_picture";

    var url_update_user=hostname+"/service/start?name=json/update_user";
    var url_update_user_password=hostname+"/service/start?name=json/update_user_password";
    var url_update_user_activity=hostname+"/service/start?name=json/update_user_activity";
    var url_update_user_discount=hostname+"/service/start?name=json/update_user_discount";
    var url_update_user_prepaid_amount=hostname+"/service/start?name=json/update_user_prepaid_amount";
    var url_increase_user_prepaid_amount=hostname+"/service/start?name=json/increase_user_prepaid_amount";
    var url_descrease_user_prepaid_amount=hostname+"/service/start?name=json/decrease_user_prepaid_amount";
    var url_increase_user_prepaid_amount_by_prepaid_code=hostname+"/service/start?name=json/increase_user_prepaid_amount_by_prepaid_code";

    var url_update_settings=hostname+"/service/start?name=json/update_settings";
    var url_update_prepaid_card_activity=hostname+"/service/start?name=json/update_prepaid_card_activity";
    var url_update_project_activity=hostname+"/service/start?name=json/update_project_activity";

    var url_update_language=hostname+"/service/start?name=json/update_language";
    var url_update_language_activity=hostname+"/service/start?name=json/update_language_activity";
    var url_update_currency=hostname+"/service/start?name=json/update_currency";
    var url_update_currency_activity=hostname+"/service/start?name=json/update_currency_activity";
    var url_update_attr_part=hostname+"/service/start?name=json/update_attr_part";

    var url_update_delivery_type=hostname+"/service/start?name=json/update_delivery_type";
    var url_update_track_type=hostname+"/service/start?name=json/update_track_type";
    var url_update_order_status=hostname+"/service/start?name=json/update_order_status";
    var url_update_pickup_status=hostname+"/service/start?name=json/update_pickup_status";
    var url_update_product_param=hostname+"/service/start?name=json/update_product_param";
    var url_update_product_type=hostname+"/service/start?name=json/update_product_type";
    var url_update_transport_type=hostname+"/service/start?name=json/update_transport_type";
    var url_update_user_review=hostname+"/service/start?name=json/update_user_review";//not used
    var url_update_product_review=hostname+"/service/start?name=json/update_product_review";//not used
    var url_update_transport_review=hostname+"/service/start?name=json/update_transport_review";//not used

    var url_update_order_AB_status=hostname+"/service/start?name=json/update_order_AB_status";
    var url_update_order_AB_transport=hostname+"/service/start?name=json/update_order_AB_transport";
    var url_update_order_AB=hostname+"/service/start?name=json/update_order_AB";
    var url_update_order_AB_part=hostname+"/service/start?name=json/update_order_AB_part";
    var url_update_order_AB_product_part=hostname+"/service/start?name=json/update_order_AB_product_part";
    var url_update_order_AB_product_param_part=hostname+"/service/start?name=json/update_order_AB_product_param_part";
    var url_update_order_AB_user_part=hostname+"/service/start?name=json/update_order_AB_user_part";
    var url_update_order_AB_user_part_status=hostname+"/service/start?name=json/update_order_AB_user_part_status";
    var url_update_purchase=hostname+"/service/start?name=json/update_purchase";
    var url_update_payment=hostname+"/service/start?name=json/update_payment";//not used

    var url_update_transport=hostname+"/service/start?name=json/update_transport";
    var url_update_sensor=hostname+"/service/start?name=json/update_sensor";
    var url_update_sensor_activity=hostname+"/service/start?name=json/update_sensor_activity";
    var url_update_product=hostname+"/service/start?name=json/update_product";
    var url_update_product_discount=hostname+"/service/start?name=json/update_product_discount";
    var url_update_stock=hostname+"/service/start?name=json/update_stock";
    var url_update_stock_invoice=hostname+"/service/start?name=json/update_stock_invoice";
    var url_update_stock_invoice_part=hostname+"/service/start?name=json/update_stock_invoice_part";
    var url_update_store=hostname+"/service/start?name=json/update_store";
    var url_update_store_part=hostname+"/service/start?name=json/update_store_part";
    var url_update_store_activity=hostname+"/service/start?name=json/update_store_activity";
    var url_update_manufacture=hostname+"/service/start?name=json/update_manufacture";
    var url_update_discount=hostname+"/service/start?name=json/update_discount";
    var url_update_tax=hostname+"/service/start?name=json/update_tax";

    var url_remove_object=hostname+"/service/start?name=json/remove_object";
    var url_remove_settings=hostname+"/service/start?name=json/remove_settings";
    var url_remove_project=hostname+"/service/start?name=json/remove_project";
    var url_remove_sensor_circle=hostname+"/service/start?name=json/remove_sensor_circle";
    var url_remove_sensor_circle_to_user=hostname+"/service/start?name=json/remove_sensor_circle_to_user";
    var url_remove_attr=hostname+"/service/start?name=json/remove_attr";
    var url_remove_color=hostname+"/service/start?name=json/remove_color";
    var url_remove_stock=hostname+"/service/start?name=json/remove_stock";
    var url_remove_stock_invoice_part=hostname+"/service/start?name=json/remove_stock_invoice_part";
    var url_remove_order_AB_part=hostname+"/service/start?name=json/remove_order_AB_part";
    var url_remove_order_AB_product_part=hostname+"/service/start?name=json/remove_order_AB_product_part";
    var url_remove_order_AB_user_part=hostname+"/service/start?name=json/remove_order_AB_user_part";
    var url_remove_order_AB_product_param_part=hostname+"/service/start?name=json/remove_order_AB_product_param_part";
    var url_remove_user_review=hostname+"/service/start?name=json/remove_user_review";
    var url_remove_product_review=hostname+"/service/start?name=json/remove_product_review";
    var url_remove_transport_review=hostname+"/service/start?name=json/remove_transport_review";

    var url_remove_product_param_part=hostname+"/service/start?name=json/remove_product_param_part";
    var url_remove_product_type_part=hostname+"/service/start?name=json/remove_product_type_part";
    var url_remove_transport_type_part=hostname+"/service/start?name=json/remove_transport_type_part";

    this.getHostname=function(){
      return hostname;
    };

    /*tools*/

    /*get discount for price, discount_value, discount_type args is a string integer values*/
    this.getDiscountPrice=function(discount_type,discount_value,price){
      var ret_val=price;
      if(discount_type.length>0&&discount_value.length>0){
        var type=parseInt(discount_type),value=parseFloat(discount_value);
        var part;
        switch(type){
          case this.DISCOUNT_IN_PERCENT:
            part=price*(value/100);
            price-=part;
            break;
          case this.DISCOUNT_IN_VALUE:
            price-=value;
            break;
          case this.INCREASE_IN_PERCENT:
            part=price*(value/100);
            price+=part;
          case this.INCREASE_IN_VALUE:
            price+=value;
            break;
          default:
        }
        ret_val=price;
      }
      return ret_val;
    };

    /*login&token&apikey*/

    var getLoginParam=function(username,password,token){
      var login_param;
      if(token!==null)login_param={'token':token};
      else if(username!==null&&password!==null)login_param={'user':username,'pass':password};
      if(api_key!==null)login_param=$.extend(login_param,{'api_key':api_key});
      return login_param;
    };

    this.getVersion=function(){
      return version;
    };
    this.setApiKey=function(apikey){
      api_key=apikey;
    };
    this.getApiKey=function(){
      return api_key;
    };

    this.getToken=function(message_ptr,callback,callback_ptr){
      var request_param={};
      getData(this.username,this.password,this.token,this.language,url_get_token,request_param,message_ptr,saveToken,this.temp_list,callback,callback_ptr);
    };
    var saveToken=function(a,list){
      list.push(a[0].token);
    };

    /*get,post data*/

    var getData=function(username,password,token,language,url,request_param,message_ptr,save,save_ptr,callback,callback_ptr){
      if(token!==null||username!==null&&password!==null){
        var a,login_param=getLoginParam(username,password,token);
        var request_data=$.extend(login_param,request_param);
        $.ajax({url:url,crossDomain:true,data:request_data,
          dataFilter:function(data){
            return data.replace(/\n|\r/g,'<br>');
          },
          success:function(data){
            /*warning! on other sites type of data can be a string or an object. alert(typeof(data));*/
            try{a=jQuery.parseJSON(typeof data=="object"?JSON.stringify(data):data);}catch(e){alert("url="+url+";"+e.name);return;}
            query_status=a.status;
            if(a.status==="ERROR"){
              var message=a.results[0].message+(a.results[0].database_message?
                                                POINT+SPACE+a.results[0].database_message:EMPTY)+
                                               (a.results[0].message_code?
                                                SPACE+OPEN_DOOR+getMessage(language,parseInt(a.results[0].message_code))+CLOSE_DOOR:EMPTY);
              result_database_message=a.results[0].database_message?a.results[0].database_message:null;
              result_message_code=a.results[0].message_code?a.results[0].message_code:null;
              result_message=a.results[0].message?a.results[0].message:null;
              result_value=null;
              if(message_ptr)$(message_ptr).html(getMessage(language,-100)+DOUBLE_POINT+SPACE+message);
            }
            else if(a.status==="SUCCESS"){
              if(a.rows){
                found_rows=a.rows;
                if(save_found_rows)saveFoundRows(found_rows,save_ptr);
              }
              if(save!==null)save(a.results,save_ptr);
              if(callback!==null)callback(callback_ptr);
            }
          },
          error:function(){if(message_ptr)$(message_ptr).html(getMessage(language,-100)+DOUBLE_POINT+SPACE+'INTERNET ERROR');}
        });
      }
    };

    var postData=function(username,password,token,language,url,request_param,message_ptr,save,save_ptr,callback,callback_ptr){
      if(token!==null||username!==null&&password!==null){
        var a,login_param=getLoginParam(username,password,token);
        var request_data=$.extend(login_param,request_param);
        $.ajax({type:"POST",url:url,crossDomain:true,contentType:"application/json",data:request_data,
          success:function(data){
            try{a=jQuery.parseJSON(typeof data=="object"?JSON.stringify(data):data);}catch(e){alert("url="+url+";"+e.name);return;}
            query_status=a.status;
            if(a.status==="ERROR"){
              var message=a.results[0].value?
                          getMessage(language,parseInt(a.results[0].value)):
                          a.results[0].message+(a.results[0].database_message?POINT+SPACE+a.results[0].database_message:EMPTY);
              result_database_message=a.results[0].database_message?a.results[0].database_message:null;
              result_message_code=a.results[0].message_code?a.results[0].message_code:null;
              result_message=a.results[0].message?a.results[0].message:null;
              result_value=a.results[0].value?a.results[0].value:null;
              if(message_ptr)$(message_ptr).html(getMessage(language,-100)+DOUBLE_POINT+SPACE+message);
              if(save!==null)save(a,save_ptr);
              if(callback!==null)callback(callback_ptr);
            }
            else if(a.status==="SUCCESS"){
              if(save!==null)save(a,save_ptr);
              if(callback!==null)callback(callback_ptr);
            }
          },
          error:function(){if(message_ptr)$(message_ptr).html(getMessage(language,-100)+DOUBLE_POINT+SPACE+'INTERNET ERROR');}
        });
      }
    };

    /*save objects*/
    var saveStat=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=a[j];
        list.push(obj);
      }
    };

    var saveAudit=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faAudit();
        obj.id=a[j].audit_id;
        obj.name=a[j].audit_name;
        obj.description=a[j].description;
        obj.username=a[j].username;
        obj.message=a[j].message;
        obj.create_date=a[j].create_date;
        obj.last_update=a[j].last_update;
        list.push(obj);
      }
    };

    var saveSettings=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faSettings();
        obj.code=a[j].settings_code;
        obj.name=a[j].settings_name;
        obj.value=a[j].settings_value;
        list.push(obj);
      }
    };

    var saveProject=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faProject();
        obj.api_key=a[j].api_key;
        obj.schema_name=a[j].schema_name;
        obj.active=a[j].active?1:0;
        obj.amount_paid=a[j].amount_paid;
        obj.currency=a[j].currency;
        obj.name=a[j].project_name;
        obj.description=a[j].description;
        obj.ip_address=a[j].ip_address;
        obj.create_date=a[j].create_date;
        obj.last_update=a[j].last_update;
        list.push(obj);
      }
    };

    var saveObjectName=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faObjectName();
        obj.name=a[j].name;
        list.push(obj);
      }
    };

    var savePicture=function(a,ptr){
      if(ptr){
        ptr.picture=a[0].picture;
      }
    };

    var saveRetVal=function(a,list){
      var ret_val=new retVal(),res=a.results[0];
      ret_val.status=a.status;
      ret_val.session_id=a.session_id;
      if(res){
        if(res.message)ret_val.message=res.message;
        if(res.message_code)ret_val.message_code=res.message_code;
        if(res.database_message)ret_val.database_message=res.database_message;
        if(res.name&&res.value){
          ret_val.name=res.name;ret_val.value=res.value;
        }
      }
      list.push(ret_val);
    };
    var saveProjectVal=function(a,list){
      var pro_val=new projectVal(),res=a[0];
      pro_val.status=a.status;
      pro_val.session_id=a.session_id;
      if(res){
        pro_val.project_created=res.project_created;
        pro_val.add_project_audit=res.add_project_audit;
      }
      list.push(pro_val);
    };

    var saveLanguage=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faLanguage();
        obj.id=a[j].language_id;
        obj.name=a[j].language_name;
        obj.value=a[j].language_value;
        obj.code=a[j].language_code;
        obj.active=a[j].active?1:0;
        list.push(obj);
      }
    };

    var saveUserType=function(a,list){
      var ret_val=new retVal();
      ret_val.status=a.status;
      ret_val.session_id=a.session_id;
      if(a.message)ret_val.message=a.message;
      if(a.message_code)ret_val.message_code=a.message_code;
      if(a&&a[0]){
        ret_val.name="user_type";ret_val.value=a[0].user_type;
      }
      list.push(ret_val);
    };

    var saveUser=function(a,list){/*short user in getSesnor,getMessage*/
      var obj,j,discount_list=[];
      for(j=0;j<a.length;j++){
        obj=new faUser();
        obj.id=a[j].user_id;
        obj.discount_code=a[j].discount_code;
        obj.type=a[j].user_type;
        obj.first_name=a[j].first_name;
        obj.last_name=a[j].last_name;
        obj.call_name=a[j].call_name;
        obj.email=a[j].email;
        obj.phone=a[j].phone;
        if(a[j].prepaid_amount!==undefined)obj.prepaid_amount=a[j].prepaid_amount;
        if(a[j].user_rate!==undefined)obj.rate=a[j].user_rate;
        obj.username=a[j].username;
        obj.active=a[j].active?1:0;
        obj.create_date=a[j].create_date;
        obj.last_update=a[j].last_update;
        discount_list.length=0;
        saveDiscount(a[j].discount,discount_list);
        obj.discount=discount_list[0];/*one of discount*/
        list.push(obj);
      }
    };

    var getSensorObject=function(sensor){
      var obj;
      obj=new faSensor();
      obj.id=sensor.sensor_id;
      obj.user_id=sensor.user_id;
      obj.name=sensor.sensor_name;
      obj.serial_number=sensor.serial_number;
      obj.device_name=sensor.device_name;
      obj.phone=sensor.phone;
      obj.active=sensor.active?1:0;
      obj.create_date=sensor.create_date;
      obj.last_update=sensor.last_update;
      return obj;
    };
    var saveSensor=function(a,list){
      var obj,j,i,sensor,sensor_circle,user_list=[];
      for(j=0;j<a.length;j++){
        obj=getSensorObject(a[j]);
        user_list.length=0;
        saveUser(a[j].user,user_list);
        obj.user=user_list[0];/*one of user*/
        /*save sensor circle*/
        sensor_circle=a[j].sensor_circle;
        for(i=0;i<sensor_circle.length;i++){
          sensor=getSensorObject(sensor_circle[i]);
          obj.sensor_circle.push(sensor);
        }
        list.push(obj);
      }
    };
    var saveSensorCircle=function(a,list){
      var obj,j,sensor_list=[];
      for(j=0;j<a.length;j++){
        obj=new faSensorCircle();
        obj.sensorA_id=a[j].sensorA_id;
        sensor_list.length=0;
        saveSensor(a[j].sensorA,sensor_list);
        obj.sensorA=sensor_list[0];/*one of sensor*/
        obj.sensorB_id=a[j].sensorB_id;
        sensor_list.length=0;
        saveSensor(a[j].sensorB,sensor_list);
        obj.sensorB=sensor_list[0];/*one of sensor*/
        obj.create_date=a[j].create_date;
        list.push(obj);
      }
    };

    var saveCurrency=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faCurrency();
        obj.id=a[j].currency_id;
        obj.name=a[j].currency_name;
        obj.description=a[j].description;
        obj.value=a[j].currency_value;
        obj.active=a[j].active?1:0;
        obj.language=a[j].language;
        obj.last_update=a[j].last_update;
        saveObjectAttr(a[j].currency_attr,obj.attr_list);
        list.push(obj);
      }
    };

    var saveDiscount=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faDiscount();
        obj.id=a[j].discount_id;
        obj.product_type_id=a[j].product_type_id;
        obj.type=a[j].discount_type;
        obj.code=a[j].discount_code;
        obj.name=a[j].discount_name;
        obj.value=a[j].discount_value;
        obj.language=a[j].language;
        obj.start_date=a[j].start_date;
        obj.finish_date=a[j].finish_date;
        obj.create_date=a[j].create_date;
        obj.last_update=a[j].last_update;
        saveObjectAttr(a[j].discount_attr,obj.attr_list);
        list.push(obj);
      }
    };

    var saveTax=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faTax();
        obj.id=a[j].tax_id;
        obj.code=a[j].tax_code;
        obj.name=a[j].tax_name;
        obj.value=a[j].tax_value;
        obj.language=a[j].language;
        obj.last_update=a[j].last_update;
        saveObjectAttr(a[j].tax_attr,obj.attr_list);
        list.push(obj);
      }
    };

    var saveOrderAB=function(a,list){
      var obj,j,user_list=[],transport_list=[];
      for(j=0;j<a.length;j++){
        obj=new faOrderAB();
        obj.id=a[j].order_id;
        obj.user_id=a[j].user_id;
        obj.transport_id=a[j].transport_id;
        obj.order_status_id=a[j].status_id;
        obj.order_status_name=a[j].status_name;
        obj.route_distance=a[j].route_distance;
        obj.route_duration=a[j].route_duration;
        obj.route_data=a[j].route_data;
        obj.total_price=a[j].total_price;
        obj.order_lat=a[j].order_lat;
        obj.order_lon=a[j].order_lon;
        obj.order_address=a[j].order_address;
        obj.delivery_lat=a[j].delivery_lat;
        obj.delivery_lon=a[j].delivery_lon;
        obj.delivery_address=a[j].delivery_address;
        obj.delivery_type_id=a[j].delivery_type_id;
        obj.delivery_type_name=a[j].delivery_type_name;
        obj.reserved_date=a[j].reserved_date;
        obj.reserved_hours=a[j].reserved_hours;
        obj.create_date=a[j].create_date;
        obj.last_update=a[j].last_update;
        user_list.length=0;
        saveUser(a[j].user,user_list);
        obj.user=user_list[0];/*one of user*/
        transport_list.length=0;
        saveTransport(a[j].transport,transport_list);
        obj.transport=transport_list[0];/*one of transport*/
        savePurchase(a[j].purchase,obj.purchase_list);
        saveObjectAttr(a[j].status_attr,obj.status_attr_list);
        saveObjectAttr(a[j].delivery_type_attr,obj.delivery_type_attr_list);
        list.push(obj);
      }
    };

    var saveOrderABPart=function(a,list){
      var obj,j,product_list=[];
      for(j=0;j<a.length;j++){
        obj=new faOrderABPart();
        obj.order_id=a[j].order_id;
        obj.product_id=a[j].product_id;
        obj.total_price=a[j].total_price;
        obj.product_count=a[j].product_count;
        obj.last_update=a[j].last_update;
        product_list.length=0;
        saveProduct(a[j].product,product_list);
        obj.product=product_list[0];/*one of product*/
        list.push(obj);
      }
    };
    var saveOrderABProductPart=saveOrderABPart;

    var saveOrderABUserPart=function(a,list){
      var obj,j,product_list=[];
      for(j=0;j<a.length;j++){
        obj=new faOrderABUserPart();
        obj.order_id=a[j].order_id;
        obj.user_id=a[j].user_id;
        obj.product_param_id_1=a[j].product_param_id_1;
        obj.product_param_id_2=a[j].product_param_id_2;
        obj.pickup_status_id=a[j].pickup_status_id;
        obj.pickup_lat=a[j].pickup_lat;
        obj.pickup_lon=a[j].pickup_lon;
        obj.pickup_address=a[j].pickup_address;
        obj.pickup_date=a[j].pickup_date;
        obj.pickup_info=a[j].pickup_info;
        obj.price=a[j].price;
        obj.create_date=a[j].create_date;
        obj.last_update=a[j].last_update;
        list.push(obj);
      }
    };

    var saveOrderStatus=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faObjectType();
        obj.id=a[j].status_id;
        obj.name=a[j].status_name;
        obj.last_update=a[j].last_update;
        saveObjectAttr(a[j].status_attr,obj.attr_list);
        list.push(obj);
      }
    };
    var savePickupStatus=saveOrderStatus;

    var savePurchase=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faPurchase();
        obj.id=a[j].purchase_id;
        obj.user_id=a[j].user_id;
        obj.invoice_code=a[j].invoice_code;
        obj.invoice_date=a[j].invoice_date;
        obj.total_price=a[j].total_price;
        obj.total_tax=a[j].total_tax;
        obj.order_id=a[j].order_id;
        obj.order_date=a[j].order_date;
        obj.order_info=a[j].order_info;
        obj.delivery_id=a[j].delivery_id;
        obj.delivery_code=a[j].delivery_code;
        obj.delivery_type_id=a[j].delivery_type_id;
        obj.delivery_type_name=a[j].delivery_type_name;
        obj.delivery_date=a[j].delivery_date;
        obj.delivery_price=a[j].delivery_price;
        obj.payment_date=a[j].payment_date;
        obj.payment_info=a[j].payment_info;
        obj.payment_amount=a[j].payment_amount;
        obj.create_date=a[j].create_date;
        obj.last_update=a[j].last_update;
        saveObjectAttr(a[j].delivery_type_attr,obj.delivery_type_attr_list);
        list.push(obj);
      }
    };

    var savePayment=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faPayment();
        obj.purchase_id=a[j].purchase_id;
        obj.amount=a[j].payment_amount;
        obj.currency=a[j].payment_currency;
        obj.description=a[j].description;
        obj.status=a[j].payment_status;
        obj.transaction_id=a[j].transaction_id;
        obj.phone=a[j].phone;
        obj.create_date=a[j].create_date;
        obj.last_update=a[j].last_update;
        list.push(obj);
      }
    };

    var savePrepaidCard=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faPrepaidCard();
        obj.id=a[j].prepaid_card_id;
        obj.serial_number=a[j].serial_number;
        obj.prepaid_code=a[j].prepaid_code;
        obj.amount=a[j].amount;
        obj.active=a[j].active;
        obj.create_date=a[j].create_date;
        list.push(obj);
      }
    };

    var saveProductInfo=function(a,obj){
      saveObjectTypeExt_AsPart(a,obj);
      if(a.param_part){
        var obj_param,obj_param_part,o=a.param_part,i;
        for(i=0;i<o.length;i++){
          obj_param=new faProductParam();
          obj_param_part=new faProductParamPart();
          obj_param.id=o[i].param_id;
          obj_param.name=o[i].param_name;
          obj_param.value=o[i].param_value;
          obj_param_part.value=o[i].param_part_value;
          obj_param_part.count=o[i].param_part_count
          obj_param_part.price=o[i].param_part_price;
          obj_param_part.product_param=obj_param;
          saveObjectAttr(o[i].param_attr,obj_param.attr_list);
          obj.param_list.push(obj_param_part);
        }
      }
      saveObjectAttr(a.product_attr,obj.attr_list);
    };
    var saveProduct=function(a,list){
      var obj,j,discount_list=[];
      for(j=0;j<a.length;j++){
        obj=new faProduct();
        obj.id=a[j].product_id;
        obj.manufacture_id=a[j].manufacture_id;
        obj.discount_code=a[j].discount_code;
        obj.name=a[j].product_name;
        obj.description=a[j].description;
        obj.code=a[j].product_code;
        obj.price=a[j].product_price;
        obj.rate=a[j].product_rate;
        obj.stock_count=a[j].stock_count;
        obj.last_update=a[j].last_update;
        discount_list.length=0;
        saveDiscount(a[j].discount,discount_list);
        obj.discount=discount_list[0];/*one of discount*/
        saveProductInfo(a[j],obj);
        list.push(obj);
      }
    };
    var saveProductParam=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faProductParam();
        obj.id=a[j].param_id;
        obj.parent_id=a[j].param_parent_id;
        obj.name=a[j].param_name;
        obj.value=a[j].param_value;
        obj.language=a[j].language;
        obj.last_update=a[j].last_update;
        saveObjectAttr(a[j].param_attr,obj.attr_list);
        list.push(obj);
      }
    };
    var saveProductParam_AsOne=function(a){
      var obj;
      obj=new faProductParam();
      obj.id=a.param_id;
      obj.parent_id=a.param_parent_id;
      obj.name=a.param_name;
      obj.value=a.param_value;
      obj.language=a.language;
      obj.last_update=a.last_update;
      saveObjectAttr(a.param_attr,obj.attr_list);
      return obj;
    };
    var saveProductParamPart=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faProductParamPart();
        obj.product_id=a[j].product_id;
        obj.product_param_id=a[j].param_id;
        obj.value=a[j].param_part_value;
        obj.price=a[j].param_part_price;
        obj.last_update=a[j].param_part_last_update;
        obj.product_param=saveProductParam_AsOne(a[j]);
        list.push(obj);
      }
    };
    var saveProductTypePart=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faProductTypePart();
        obj.product_id=a[j].product_id;
        obj.product_type_id=a[j].type_id;
        obj.last_update=a[j].type_part_last_update;
        obj.product_type=saveObjectTypeExt_AsOne(a[j]);
        list.push(obj);
      }
    };

    var saveStock=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faStock();
        obj.product_id=a[j].product_id;
        obj.store_id=a[j].store_id;
        obj.count=a[j].count;
        obj.last_update=a[j].last_update;
        list.push(obj);
      }
    };

    var saveStockInvoice=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faStockInvoice();
        obj.id=a[j].stock_invoice_id;
        obj.invoice_code=a[j].invoice_code;
        obj.invoice_date=a[j].invoice_date;
        obj.total_price=a[j].total_price;
        obj.total_tax=a[j].total_tax;
        obj.supplier=a[j].supplier;
        obj.phone=a[j].phone;
        obj.delivery_id=a[j].delivery_id;
        obj.delivery_code=a[j].delivery_code;
        obj.delivery_type_id=a[j].delivery_type_id;
        obj.delivery_type_name=a[j].delivery_type_name;
        obj.delivery_date=a[j].delivery_date;
        obj.delivery_price=a[j].delivery_price;
        obj.payment_date=a[j].payment_date;
        obj.payment_info=a[j].payment_info;
        obj.payment_amount=a[j].payment_amount;
        obj.create_date=a[j].create_date;
        obj.last_update=a[j].last_update;
        list.push(obj);
      }
    };

    var saveStockInvoicePart=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faStockInvoicePart();
        obj.stock_invoice_id=a[j].stock_invoice_id;
        obj.product_id=a[j].product_id;
        obj.tax_id=a[j].tax_id;
        obj.count=a[j].stock_invoice_count;
        obj.price=a[j].stock_invoice_price;
        obj.last_update=a[j].last_update;
        list.push(obj);
      }
    };

    var saveStore=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faStore();
        obj.id=a[j].store_id;
        obj.name=a[j].store_name;
        obj.description=a[j].description;
        obj.active=a[j].active?1:0;
        obj.last_update=a[j].last_update;
        /* inner function example
        if(a[j].store_part){
          var obj_part,o=a[j].store_part,i;
          for(i=0;i<o.length;i++){
            obj_part=new faStorePart();
            obj_part.id=o[i].part_id;
            obj_part.name=o[i].part_name;
            obj_part.description=o[i].description;
            obj_part.latitude=o[i].latitude;
            obj_part.longitude=o[i].longitude;
            obj_part.email=o[i].email;
            obj_part.phone1=o[i].phone1;
            obj_part.phone2=o[i].phone2;
            obj_part.address=o[i].address;
            obj_part.city=o[i].city;
            obj_part.postcode=o[i].postcode;
            obj.part_list.push(obj_part);
          }
        }*/
        saveStorePart(a[j].store_part,obj.part_list);
        saveObjectAttr(a[j].store_attr,obj.attr_list);
        list.push(obj);
      }
    };

    var saveStorePart=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faStorePart();
        obj.id=a[j].part_id;
        obj.store_id=a[j].store_id;
        obj.name=a[j].part_name;
        obj.description=a[j].description;
        obj.latitude=a[j].latitude;
        obj.longitude=a[j].longitude;
        obj.email=a[j].email;
        obj.phone1=a[j].phone1;
        obj.phone2=a[j].phone2;
        obj.address=a[j].address;
        obj.city=a[j].city;
        obj.postcode=a[j].postcode;
        obj.language=a[j].language;
        obj.last_update=a[j].last_update;
        list.push(obj);
      }
    };

    var saveManufacture=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faManufacture();
        obj.id=a[j].manufacture_id;
        obj.name=a[j].manufacture_name;
        obj.description=a[j].description;
        obj.last_update=a[j].last_update;
        saveObjectAttr(a[j].manufacture_attr,obj.attr_list);
        list.push(obj);
      }
    };

    var saveTransport=function(a,list){
      var obj,j,sensor_list=[];
      for(j=0;j<a.length;j++){
        obj=new faTransport();
        obj.id=a[j].transport_id;
        obj.user_id=a[j].user_id;
        obj.sensor_id=a[j].sensor_id;
        obj.sensor_active=a[j].sensor_active?1:0;
        obj.name=a[j].transport_name;
        obj.color=a[j].transport_color;
        obj.license_plate=a[j].license_plate;
        obj.rate=a[j].transport_rate;
        obj.reserved=a[j].transport_reserved;
        obj.last_update=a[j].last_update;
        sensor_list.length=0;
        saveSensor(a[j].sensor,sensor_list);
        obj.sensor=sensor_list[0];/*one of sensor*/
        saveObjectTypeExt_AsPart(a[j],obj);
        list.push(obj);
      }
    };

    var saveTransportProductPart=function(a,list){
      var obj,j,product_list=[];
      for(j=0;j<a.length;j++){
        obj=new faTransportProductPart();
        obj.transport_id=a[j].transport_id;
        obj.product_id=a[j].product_id;
        obj.last_update=a[j].last_update;
        product_list.length=0;
        saveProduct(a[j].product,product_list);
        obj.product=product_list[0];/*one of product*/
        list.push(obj);
      }
    };

    var saveTransportTypePart=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faTransportTypePart();
        obj.transport_id=a[j].transport_id;
        obj.transport_type_id=a[j].type_id;
        obj.last_update=a[j].type_part_last_update;
        obj.transport_type=saveObjectTypeExt_AsOne(a[j]);
        list.push(obj);
      }
    };

    var saveMessage=function(a,list){
      var obj,j,user_list=[];
      for(j=0;j<a.length;j++){
        obj=new faMessage();
        obj.id=a[j].message_id;
        obj.type=a[j].message_type;
        obj.userA_id=a[j].userA_id;
        obj.userB_id=a[j].userB_id;
        obj.message=a[j].message;
        obj.create_date=a[j].create_date;
        user_list.length=0;
        saveUser(a[j].userA,user_list);
        obj.userA=user_list[0];/*one of user*/
        user_list.length=0;
        saveUser(a[j].userB,user_list);
        obj.userB=user_list[0];/*one of user*/
        list.push(obj);
      }
    };

    var saveTrack=function(a,list){
      var obj,j,transport_list=[],sensor_list=[],track_part_list=[];
      for(j=0;j<a.length;j++){
        obj=new faTrack();
        obj.id=a[j].track_id;
        obj.user_id=a[j].user_id;
        obj.sensor_id=a[j].sensor_id;
        obj.type_id=a[j].type_id;
        obj.type_name=a[j].type_name;
        obj.latitude=a[j].latitude;
        obj.longitude=a[j].longitude;
        obj.time=a[j].track_time;
        obj.altitude=a[j].altitude;
        obj.accuracy=a[j].accuracy;
        obj.bearing=a[j].bearing;
        obj.speed=a[j].speed;
        obj.satellites=a[j].satellites;
        obj.battery=a[j].battery;
        obj.timezone_offset=a[j].timezone_offset;
        obj.create_date=a[j].create_date;
        if(a[j].transport_id){
          obj.transport_id=a[j].transport_id;
          transport_list.length=0;
          saveTransport(a[j].transport,transport_list);
          obj.transport=transport_list[0];/*one of transport*/
        }
        sensor_list.length=0;
        saveSensor(a[j].sensor,sensor_list);
        obj.sensor=sensor_list[0];/*one of sensor*/
        track_part_list.length=0;
        saveTrackPart(a[j].track_part,track_part_list);
        obj.part_list=track_part_list;/*can be many parts*/
        saveObjectAttr(a[j].type_attr,obj.type_attr_list);
        list.push(obj);
      }
    };
    var saveTrackPart=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faTrackPart();
        obj.id=a[j].track_part_id;
        obj.track_id=a[j].track_id;
        obj.name=a[j].track_name;
        obj.description=a[j].description;
        obj.language=a[j].language;
        obj.last_update=a[j].last_update;
        list.push(obj);
      }
    };
    var saveAttrPart=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faAttrPart();
        obj.id=a[j].attr_part_id;
        obj.object_id=a[j].object_id;
        obj.object_name=a[j].object_name;
        obj.name=a[j].attr_name;
        obj.value=a[j].attr_value;
        obj.language=a[j].language;
        obj.last_update=a[j].last_update;
        list.push(obj);
      }
    };

    /*save type,typeExt,attr*/
    var saveObjectTypeExt=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faObjectTypeExt();
        obj.id=a[j].type_id;
        obj.parent_id=a[j].type_parent_id;
        obj.name=a[j].type_name;
        obj.description=a[j].description;
        obj.language=a[j].language;
        obj.last_update=a[j].last_update;
        saveObjectAttr(a[j].type_attr,obj.attr_list);
        list.push(obj);
      }
    };
    var saveObjectType=function(a,list){
      var obj,j;
      for(j=0;j<a.length;j++){
        obj=new faObjectType();
        obj.id=a[j].type_id;
        obj.name=a[j].type_name;
        obj.language=a[j].language;
        obj.last_update=a[j].last_update;
        saveObjectAttr(a[j].type_attr,obj.attr_list);
        list.push(obj);
      }
    };
    var saveObjectTypeExt_AsPart=function(a,obj){
      if(a.type_part){
        var obj_part,o=a.type_part,i;
        for(i=0;i<o.length;i++){
          obj_part=new faObjectTypeExt();
          obj_part.id=o[i].type_id;
          obj_part.name=o[i].type_name;
          obj_part.description=o[i].description;
          saveObjectAttr(o[i].type_attr,obj_part.attr_list);
          obj.type_list.push(obj_part);
        }
      }
    };
    var saveObjectType_AsPart=function(a,obj){
      if(a.type_part){
        var obj_part,o=a.type_part,i;
        for(i=0;i<o.length;i++){
          obj_part=new faObjectType();
          obj_part.id=o[i].type_id;
          obj_part.name=o[i].type_name;
          saveObjectAttr(o[i].type_attr,obj_part.attr_list);
          obj.type_list.push(obj_part);
        }
      }
    };
    var saveObjectTypeExt_AsOne=function(a){
      var obj;
      obj=new faObjectTypeExt();
      obj.id=a.type_id;
      obj.parent_id=a.type_parent_id;
      obj.name=a.type_name;
      obj.description=a.description;
      obj.language=a.language;
      obj.last_update=a.last_update;
      saveObjectAttr(a.type_attr,obj.attr_list);
      return obj;
    };
    var saveObjectAttr=function(attr,list){
      if(attr){
        var obj_attr,o=attr,i;
        for(i=0;i<o.length;i++){
          obj_attr=new faAttrPart();
          obj_attr.id=o[i].attr_part_id;
          obj_attr.name=o[i].attr_name;;
          obj_attr.value=o[i].attr_value;;
          list.push(obj_attr);
        }
      }
    };
    var saveUserReview=function(a,list){
      var obj,j,user_list=[],review_user_list=[];
      for(j=0;j<a.length;j++){
        obj=new faObjectReview();
        obj.user_id=a[j].user_id;
        obj.object_id=a[j].review_user_id;
        obj.description=a[j].description;
        obj.value=a[j].review_value;
        obj.last_update=a[j].last_update;
        user_list.length=0;
        saveUser(a[j].user,user_list);
        obj.user=user_list[0];/*one of user*/
        review_user_list.length=0;
        saveUser(a[j].review_user,review_user_list);
        obj.object=review_user_list[0];/*one of review user*/
        list.push(obj);
      }
    };
    var saveProductReview=function(a,list){
      var obj,j,user_list=[],product_list=[];
      for(j=0;j<a.length;j++){
        obj=new faObjectReview();
        obj.user_id=a[j].user_id;
        obj.object_id=a[j].product_id;
        obj.description=a[j].description;
        obj.value=a[j].review_value;
        obj.last_update=a[j].last_update;
        user_list.length=0;
        saveUser(a[j].user,user_list);
        obj.user=user_list[0];/*one of user*/
        product_list.length=0;
        saveProduct(a[j].product,product_list);
        obj.object=product_list[0];/*one of product*/
        list.push(obj);
      }
    };
    var saveTransportReview=function(a,list){
      var obj,j,user_list=[],transport_list=[];
      for(j=0;j<a.length;j++){
        obj=new faObjectReview();
        obj.order_id=a[j].order_id;
        obj.user_id=a[j].user_id;
        obj.object_id=a[j].transport_id;
        obj.description=a[j].description;
        obj.value=a[j].review_value;
        obj.last_update=a[j].last_update;
        user_list.length=0;
        saveUser(a[j].user,user_list);
        obj.user=user_list[0];/*one of user*/
        transport_list.length=0;
        saveTransport(a[j].transport,transport_list);
        obj.object=transport_list[0];/*one of transport*/
        list.push(obj);
      }
    };

    /*write/read shopping cart to/from cookies*/
    this.writeToShoppingCart=function(list){
      /*to json from list*/
      var json_data=JSON.stringify(list);
      /*write to cookie*/
      document.cookie="SHOPPING_CART="+/*encodeURIComponent(json_data)*/json_data+"; expires=-1";
    };
    this.readFromShoppingCart=function(){
      /*read from cookie*/
      var list=null;
      var name="SHOPPING_CART",json_data;
      if(document.cookie.length>0){
        var index1=document.cookie.indexOf(name+"="),index2;
        if(index1!==-1){
          index1=index1+name.length+1;
          index2=document.cookie.indexOf(";",index1);
          if(index2===-1)index2=document.cookie.length;
          /*json_data=decodeURIComponent(document.cookie.substring(index1,index2));*/
          json_data=document.cookie.substring(index1,index2);
          /*from json to list*/
          list=jQuery.parseJSON(json_data);
        }
      }
      return list;
    };

    /*endof savetool*/

    /*get objects*/
    this.getStat=function(message_ptr,callback,callback_ptr){
      var request_param={};
      getData(this.username,this.password,this.token,this.language,url_get_stat,request_param,message_ptr,saveStat,this.stat_list,callback,callback_ptr);
    };
    this.getAudit=function(username,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(username!==null&&username.length!==0)request_param=$.extend(request_param,{'username':username});
      getData(this.username,this.password,this.token,this.language,url_get_audit,request_param,message_ptr,saveAudit,this.audit_list,callback,callback_ptr);
    };
    this.getSettings=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.code!==null&&ptr.code.length!==0)request_param=$.extend(request_param,{'settings_code':ptr.code});
      getData(this.username,this.password,this.token,this.language,url_get_settings,request_param,message_ptr,saveSettings,this.settings_list,callback,callback_ptr);
    };
    this.getProject=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.schema_name!==null&&ptr.schema_name.length!==0)request_param=$.extend(request_param,{'schema_name':ptr.schema_name});
      getData(this.username,this.password,this.token,this.language,url_get_project,request_param,message_ptr,saveProject,this.project_list,callback,callback_ptr);
    };
    this.isProject=function(project_name,message_ptr,callback,callback_ptr){
      var request_param={};
      if(project_name!==null&&project_name.length!==0)request_param=$.extend(request_param,{'project_name':project_name});
      getData(this.username,this.password,this.token,this.language,url_is_project,request_param,message_ptr,saveProjectVal,this.ret_val_list,callback,callback_ptr);
    };

    this.getAttr=function(message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      getData(this.username,this.password,this.token,this.language,url_get_attr,request_param,message_ptr,saveObjectName,this.attr_list,callback,callback_ptr);
    };
    this.getColor=function(message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      getData(this.username,this.password,this.token,this.language,url_get_color,request_param,message_ptr,saveObjectName,this.color_list,callback,callback_ptr);
    };
    this.getLanguage=function(message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      getData(this.username,this.password,this.token,this.language,url_get_language,request_param,message_ptr,saveLanguage,this.language_list,callback,callback_ptr);
    };
    this.getAttrPart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.object_id!==null)request_param=$.extend(request_param,{'object_id':ptr.object_id});
      if(ptr.object_name!==null&&ptr.object_name.length!==0)request_param=$.extend(request_param,{'object_name':ptr.object_name});
      if(ptr.language!==null&&ptr.language.length!==0)request_param=$.extend(request_param,{'language':ptr.language});
      getData(this.username,this.password,this.token,this.language,url_get_attr_part,request_param,message_ptr,saveAttrPart,this.attr_part_list,callback,callback_ptr);
    };

    this.getDeliveryType=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'type_id':ptr.id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_delivery_type,request_param,message_ptr,saveObjectType,this.delivery_type_list,callback,callback_ptr);
    };
    this.getTrackType=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'type_id':ptr.id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_track_type,request_param,message_ptr,saveObjectType,this.track_type_list,callback,callback_ptr);
    };
    this.getProductParam=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'param_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'param_name':ptr.name});
      if(ptr.value!==null&&ptr.value.length!==0)request_param=$.extend(request_param,{'param_value':ptr.value});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_product_param,request_param,message_ptr,saveProductParam,this.product_param_list,callback,callback_ptr);
    };
    this.getProductType=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'type_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'type_name':ptr.name});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_product_type,request_param,message_ptr,saveObjectTypeExt,this.product_type_list,callback,callback_ptr);
    };
    this.getTransportType=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'type_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'type_name':ptr.name});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_transport_type,request_param,message_ptr,saveObjectTypeExt,this.transport_type_list,callback,callback_ptr);
    };

    this.getProductParamPart=function(ptr,language,message_ptr,callback,callback_ptr){/*ptr is class of ProductParam*/
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'product_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'param_name':ptr.name});
      if(ptr.value!==null&&ptr.value.length!==0)request_param=$.extend(request_param,{'param_value':ptr.value});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_product_param_part,request_param,message_ptr,saveProductParamPart,this.product_param_part_list,callback,callback_ptr);
    };
    this.getProductTypePart=function(ptr,language,message_ptr,callback,callback_ptr){/*ptr is class of faObjectTypeExt*/
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'product_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'type_name':ptr.name});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_product_type_part,request_param,message_ptr,saveProductTypePart,this.product_type_part_list,callback,callback_ptr);
    };
    this.getTransportTypePart=function(ptr,language,message_ptr,callback,callback_ptr){/*ptr is class of faObjectTypeExt*/
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'transport_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'type_name':ptr.name});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_transport_type_part,request_param,message_ptr,saveTransportTypePart,this.transport_type_part_list,callback,callback_ptr);
    };

    /*GET picture:manufacture,product,product_param,sensor_place,store,store_part,transport*/
    this.getPicture=function(ptr,picture_id,object_name,message_ptr,callback,callback_ptr){
      var request_param={};
      if(picture_id!==null)request_param=$.extend(request_param,{'picture_id':picture_id});
      if(object_name!==null&&object_name.length!==0)request_param=$.extend(request_param,{'object_name':object_name});
      getData(this.username,this.password,this.token,this.language,url_get_picture,request_param,message_ptr,savePicture,ptr,callback,callback_ptr);
    };
    this.getUserPicture=function(ptr,user_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(user_id!==null)request_param=$.extend(request_param,{'user_id':user_id});
      getData(this.username,this.password,this.token,this.language,url_get_user_picture,request_param,message_ptr,savePicture,ptr,callback,callback_ptr);
    };
    this.getProductParamPartPicture=function(ptr,product_id,param_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(product_id!==null)request_param=$.extend(request_param,{'product_id':product_id});
      if(param_id!==null)request_param=$.extend(request_param,{'param_id':param_id});
      getData(this.username,this.password,this.token,this.language,url_get_product_param_part_picture,request_param,message_ptr,savePicture,ptr,callback,callback_ptr);
    };
    this.getTrackPartPicture=function(ptr,track_part_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(track_part_id!==null)request_param=$.extend(request_param,{'track_part_id':track_part_id});
      getData(this.username,this.password,this.token,this.language,url_get_track_part_picture,request_param,message_ptr,savePicture,ptr,callback,callback_ptr);
    };

    this.getUserType=function(message_ptr,callback,callback_ptr){
      var request_param={};
      getData(this.username,this.password,this.token,this.language,url_get_user_type,request_param,message_ptr,saveUserType,this.ret_val_list,callback,callback_ptr);
    };
    this.getUserTypeByParam=function(username,password,message_ptr,callback,callback_ptr){
      var request_param={};
      getData(username,password,null,this.language,url_get_user_type,request_param,message_ptr,saveUserType,this.ret_val_list,callback,callback_ptr);
    };

    this.getUser=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={},find_name=EMPTY;
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'user_id':ptr.id});
      if(ptr.type!==null)request_param=$.extend(request_param,{'user_type':ptr.type});
      if(ptr.first_name!==null&&ptr.first_name.length!==0)find_name+=ptr.first_name;
      if(ptr.last_name!==null&&ptr.last_name.length!==0)find_name+=ptr.last_name;
      if(find_name.length!==0)request_param=$.extend(request_param,{'find_name':find_name});
      if(ptr.call_name!==null&&ptr.call_name.length!==0)request_param=$.extend(request_param,{'call_name':ptr.call_name});
      if(ptr.username!==null&&ptr.username.length!==0)request_param=$.extend(request_param,{'user_name':ptr.username});
      if(ptr.email!==null&&ptr.email.length!==0)request_param=$.extend(request_param,{'email':ptr.email});
      if(ptr.phone!==null&&ptr.phone.length!==0)request_param=$.extend(request_param,{'phone':ptr.phone});
      if(ptr.active!==null&&ptr.active.length!==0)request_param=$.extend(request_param,{'active':ptr.active});
      if(ptr.create_date!==null&&ptr.create_date.length!==0)request_param=$.extend(request_param,{'create_date':ptr.create_date});
      if(ptr.discount_code!==null&&ptr.discount_code.length!==0)request_param=$.extend(request_param,{'discount_code':ptr.discount_code});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_user,request_param,message_ptr,saveUser,this.user_list,callback,callback_ptr);
    };

    this.getDiscount=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.code!==null&&ptr.code.length!==0)request_param=$.extend(request_param,{'discount_code':ptr.code});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'discount_name':ptr.name});
      if(ptr.product_type_id!==null)request_param=$.extend(request_param,{'product_type_id':ptr.product_type_id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_discount,request_param,message_ptr,saveDiscount,this.discount_list,callback,callback_ptr);
    };
    this.getTax=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'tax_id':ptr.id});
      if(ptr.code!==null&&ptr.code.length!==0)request_param=$.extend(request_param,{'tax_code':ptr.code});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'tax_name':ptr.name});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_tax,request_param,message_ptr,saveTax,this.tax_list,callback,callback_ptr);
    };
    this.getUserReview=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.user_id!==null)request_param=$.extend(request_param,{'user_id':ptr.user_id});
      if(ptr.object_id!==null)request_param=$.extend(request_param,{'review_user_id':ptr.object_id});
      getData(this.username,this.password,this.token,this.language,url_get_user_review,request_param,message_ptr,saveUserReview,this.user_review_list,callback,callback_ptr);
    };
    this.getProductReview=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.user_id!==null)request_param=$.extend(request_param,{'user_id':ptr.user_id});
      if(ptr.object_id!==null)request_param=$.extend(request_param,{'product_id':ptr.object_id});
      getData(this.username,this.password,this.token,this.language,url_get_product_review,request_param,message_ptr,saveProductReview,this.product_review_list,callback,callback_ptr);
    };
    this.getTransportReview=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.order_id!==null)request_param=$.extend(request_param,{'order_id':ptr.order_id});
      if(ptr.user_id!==null)request_param=$.extend(request_param,{'user_id':ptr.user_id});
      if(ptr.object_id!==null)request_param=$.extend(request_param,{'transport_id':ptr.object_id});
      getData(this.username,this.password,this.token,this.language,url_get_transport_review,request_param,message_ptr,saveTransportReview,this.transport_review_list,callback,callback_ptr);
    };

    this.getSensor=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'sensor_id':ptr.id});
      if(ptr.user_id!==null)request_param=$.extend(request_param,{'user_id':ptr.user_id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'sensor_name':ptr.name});
      if(ptr.device_name!==null&&ptr.device_name.length!==0)request_param=$.extend(request_param,{'device_name':ptr.device_name});
      if(ptr.phone!==null&&ptr.phone.length!==0)request_param=$.extend(request_param,{'phone':ptr.phone});
      if(ptr.active!==null&&ptr.active.length!==0)request_param=$.extend(request_param,{'active':ptr.active});
      if(ptr.create_date!==null&&ptr.create_date.length!==0)request_param=$.extend(request_param,{'create_date':ptr.create_date});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_sensor,request_param,message_ptr,saveSensor,this.sensor_list,callback,callback_ptr);
    };
    this.getSensorCircle=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.sensorA_id!==null)request_param=$.extend(request_param,{'sensor_id':ptr.sensorA_id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_sensor_circle,request_param,message_ptr,saveSensorCircle,this.sensor_circle_list,callback,callback_ptr);
    };

    this.getCurrency=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'currency_id':ptr.id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_currency,request_param,message_ptr,saveCurrency,this.currency_list,callback,callback_ptr);
    };

    this.getOrderAB=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'order_id':ptr.id});
      if(ptr.user_id!==null)request_param=$.extend(request_param,{'user_id':ptr.user_id});
      if(ptr.transport_id!==null)request_param=$.extend(request_param,{'transport_id':ptr.transport_id});
      if(ptr.order_status_id!==null)request_param=$.extend(request_param,{'status_id':ptr.order_status_id});
      if(ptr.order_address!==null&&ptr.order_address.length!==0)request_param=$.extend(request_param,{'order_address':ptr.order_address});
      if(ptr.delivery_address!==null&&ptr.delivery_address.length!==0)request_param=$.extend(request_param,{'delivery_address':ptr.delivery_address});
      if(ptr.create_date!==null&&ptr.create_date.length!==0)request_param=$.extend(request_param,{'create_date':ptr.create_date});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_order_AB,request_param,message_ptr,saveOrderAB,this.order_AB_list,callback,callback_ptr);
    };

    this.getOrderABPart=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.order_id!==null)request_param=$.extend(request_param,{'order_id':ptr.order_id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_order_AB_part,request_param,message_ptr,saveOrderABPart,this.order_AB_part_list,callback,callback_ptr);
    };
    this.getOrderABProductPart=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.order_id!==null)request_param=$.extend(request_param,{'order_id':ptr.order_id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_order_AB_product_part,request_param,message_ptr,saveOrderABProductPart,this.order_AB_product_part_list,callback,callback_ptr);
    };
    this.getOrderABUserPart=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.order_id!==null)request_param=$.extend(request_param,{'order_id':ptr.order_id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_order_AB_user_part,request_param,message_ptr,saveOrderABUserPart,this.order_AB_user_part_list,callback,callback_ptr);
    };

    this.getOrderStatus=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'status_id':ptr.id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_order_status,request_param,message_ptr,saveOrderStatus,this.order_status_list,callback,callback_ptr);
    };
    this.getPickupStatus=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'status_id':ptr.id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_pickup_status,request_param,message_ptr,savePickupStatus,this.pickup_status_list,callback,callback_ptr);
    };

    this.getPurchase=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'purchase_id':ptr.id});
      if(ptr.invoice_code!==null&&ptr.invoice_code.length!==0)request_param=$.extend(request_param,{'invoice_code':ptr.invoice_code});
      if(ptr.invoice_date!==null&&ptr.invoice_date.length!==0)request_param=$.extend(request_param,{'invoice_date':ptr.invoice_date});
      if(ptr.order_id!==null)request_param=$.extend(request_param,{'order_id':ptr.order_id});
      if(ptr.order_date!==null&&ptr.order_date.length!==0)request_param=$.extend(request_param,{'order_date':ptr.order_date});
      if(ptr.delivery_id!==null)request_param=$.extend(request_param,{'delivery_id':ptr.delivery_id});
      if(ptr.delivery_date!==null&&ptr.delivery_date.length!==0)request_param=$.extend(request_param,{'delivery_date':ptr.delivery_date});
      if(ptr.payment_date!==null&&ptr.payment_date.length!==0)request_param=$.extend(request_param,{'payment_date':ptr.payment_date});
      if(ptr.create_date!==null&&ptr.create_date.length!==0)request_param=$.extend(request_param,{'create_date':ptr.create_date});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_purchase,request_param,message_ptr,savePurchase,this.purchase_list,callback,callback_ptr);
    };
    this.getPayment=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.purchase_id!==null)request_param=$.extend(request_param,{'purchase_id':ptr.purchase_id});
      getData(this.username,this.password,this.token,this.language,url_get_payment,request_param,message_ptr,savePayment,this.payment_list,callback,callback_ptr);
    };
    this.getPrepaidCard=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'prepaid_card_id':ptr.id});
      if(ptr.serial_number!==null&&ptr.serial_number.length!==0)request_param=$.extend(request_param,{'serial_number':ptr.serial_number});
      getData(this.username,this.password,this.token,this.language,url_get_prepaid_card,request_param,message_ptr,savePrepaidCard,this.prepaid_card_list,callback,callback_ptr);
    };

    this.getProduct=function(ptr,language,price_from,price_to,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'product_id':ptr.id});
      if(ptr.manufacture_id!==null)request_param=$.extend(request_param,{'manufacture_id':ptr.manufacture_id});
      if(ptr.discount_id!==null)request_param=$.extend(request_param,{'discount_id':ptr.discount_id});
      if(ptr.code!==null&&ptr.code.length!==0)request_param=$.extend(request_param,{'product_code':ptr.code});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'product_name':ptr.name});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(price_from!==null&&price_from.length!==0)request_param=$.extend(request_param,{'price_from':price_from});
      if(price_to!==null&&price_to!==0)request_param=$.extend(request_param,{'price_to':price_to});
      if(ptr.stock_count!==null)request_param=$.extend(request_param,{'stock_count':ptr.stock_count});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_product,request_param,message_ptr,saveProduct,this.product_list,callback,callback_ptr);
    };

    this.getStock=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.product_id!==null)request_param=$.extend(request_param,{'product_id':ptr.product_id});
      if(ptr.store_id!==null)request_param=$.extend(request_param,{'store_id':ptr.store_id});
      getData(this.username,this.password,this.token,this.language,url_get_stock,request_param,message_ptr,saveStock,this.stock_list,callback,callback_ptr);
    };
    this.getStockInvoice=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'stock_invoice_id':ptr.id});
      if(ptr.invoice_code!==null&&ptr.invoice_code.length!==0)request_param=$.extend(request_param,{'invoice_code':ptr.invoice_code});
      if(ptr.invoice_date!==null&&ptr.invoice_date.length!==0)request_param=$.extend(request_param,{'invoice_date':ptr.invoice_date});
      if(ptr.supplier!==null&&ptr.supplier.length!==0)request_param=$.extend(request_param,{'supplier':ptr.supplier});
      if(ptr.phone!==null&&ptr.phone.length!==0)request_param=$.extend(request_param,{'phone':ptr.phone});
      if(ptr.delivery_id!==null)request_param=$.extend(request_param,{'delivery_id':ptr.delivery_id});
      if(ptr.delivery_date!==null&&ptr.delivery_date.length!==0)request_param=$.extend(request_param,{'delivery_date':ptr.delivery_date});
      if(ptr.create_date!==null&&ptr.create_date.length!==0)request_param=$.extend(request_param,{'create_date':ptr.create_date});
      getData(this.username,this.password,this.token,this.language,url_get_stock_invoice,request_param,message_ptr,saveStockInvoice,this.stock_invoice_list,callback,callback_ptr);
    };
    this.getStockInvoicePart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.stock_invoice_id!==null)request_param=$.extend(request_param,{'stock_invoice_id':ptr.stock_invoice_id});
      if(ptr.product_id!==null)request_param=$.extend(request_param,{'product_id':ptr.product_id});
      getData(this.username,this.password,this.token,this.language,url_get_stock_invoice_part,request_param,message_ptr,saveStockInvoicePart,this.stock_invoice_part_list,callback,callback_ptr);
    };

    this.getStore=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'store_id':ptr.id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_store,request_param,message_ptr,saveStore,this.store_list,callback,callback_ptr);
    };
    this.getStorePart=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.store_id!==null)request_param=$.extend(request_param,{'store_id':ptr.store_id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_store_part,request_param,message_ptr,saveStorePart,this.store_part_list,callback,callback_ptr);
    };

    this.getManufacture=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'manufacture_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'manufacture_name':ptr.name});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_manufacture,request_param,message_ptr,saveManufacture,this.manufacture_list,callback,callback_ptr);
    };

    this.getTransport=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'transport_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'transport_name':ptr.name});
      if(ptr.license_plate!==null&&ptr.license_plate.length!==0)request_param=$.extend(request_param,{'license_plate':ptr.license_plate});
      if(ptr.sensor_id!==null)request_param=$.extend(request_param,{'sensor_id':ptr.sensor_id});
      if(ptr.sensor_active!==null&&ptr.sensor_active.length!==0)request_param=$.extend(request_param,{'sensor_active':ptr.sensor_active});
      if(ptr.user_id!==null)request_param=$.extend(request_param,{'user_id':ptr.user_id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_transport,request_param,message_ptr,saveTransport,this.transport_list,callback,callback_ptr);
    };

    this.getMessage=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.id!==null)request_param=$.extend(request_param,{'message_id':ptr.id});
      if(ptr.type!==null)request_param=$.extend(request_param,{'message_type':ptr.type});
      if(ptr.user_id!==null)request_param=$.extend(request_param,{'user_id':ptr.user_id});
      if(ptr.create_date!==null&&ptr.create_date.length!==0)request_param=$.extend(request_param,{'create_date':ptr.create_date});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_message,request_param,message_ptr,saveMessage,this.message_list,callback,callback_ptr);
    };

    this.getTrack=function(ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr.sensor_id!==null)request_param=$.extend(request_param,{'sensor_id':ptr.sensor_id});
      if(ptr.time!==null&&ptr.time.length!==0)request_param=$.extend(request_param,{'track_time':ptr.time});
      if(ptr.create_date!==null&&ptr.create_date.length!==0)request_param=$.extend(request_param,{'create_date':ptr.create_date});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_track,request_param,message_ptr,saveTrack,this.track_list,callback,callback_ptr);
    };

    /*get max track*/
    this.getMaxCircleTrack=function(language,message_ptr,callback,callback_ptr){
      var request_param={};
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_max_circle_track,request_param,message_ptr,saveTrack,this.track_list,callback,callback_ptr);
    };
    this.getMaxTrack=function(sensor_id,language,message_ptr,callback,callback_ptr){
      var request_param={};
      if(sensor_id!==null)request_param=$.extend(request_param,{'sensor_id':sensor_id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_max_track,request_param,message_ptr,saveTrack,this.track_list,callback,callback_ptr);
    };

    /*get elemental*/
    this.getTransportProductPart=function(transport_id,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(transport_id!==null)request_param=$.extend(request_param,{'transport_id':transport_id});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      getData(this.username,this.password,this.token,this.language,url_get_transport_product_part,request_param,message_ptr,saveTransportProductPart,this.transport_product_part_list,callback,callback_ptr);
    };

    /*get report filepath by report url AS POST (ptr is repVal)*/
    this.getReport=function(report_name,ptr,language,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLimit(this.offset,this.rows);
      if(ptr!==null&&ptr.id!==null)request_param=$.extend(request_param,{'id':ptr.id});
      if(ptr!==null&&ptr.date_from!==null&&ptr.date_from.length!==0)request_param=$.extend(request_param,{'date_from':ptr.date_from});
      if(ptr!==null&&ptr.date_to!==null&&ptr.date_to.length!==0)request_param=$.extend(request_param,{'date_to':ptr.date_to});
      if(language!==null&&language.length!==0)request_param=$.extend(request_param,{'language':language});
      postData(this.username,this.password,this.token,this.language,url_get_report+report_name,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    /*add objects*/
    var getSettings_RP=function(ptr,request_param){
      if(ptr.code!==null&&ptr.code.length!==0)request_param=$.extend(request_param,{'settings_code':ptr.code});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'settings_name':ptr.name});
      if(ptr.value!==null&&ptr.value.length!==0)request_param=$.extend(request_param,{'settings_value':ptr.value});
      return request_param;
    };
    this.addSettings=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getSettings_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_settings,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    var getProject_RP=function(ptr,request_param){
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'project_name':ptr.name});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(ptr.ip_address!==null&&ptr.ip_address.length!==0)request_param=$.extend(request_param,{'ip_address':ptr.ip_address});
      return request_param;
    };
    this.addProject=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getProject_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_project,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getUser_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'user_id':ptr.id});
      if(ptr.first_name!==null&&ptr.first_name.length!==0)request_param=$.extend(request_param,{'first_name':ptr.first_name});
      if(ptr.last_name!==null&&ptr.last_name.length!==0)request_param=$.extend(request_param,{'last_name':ptr.last_name});
      if(ptr.call_name!==null&&ptr.call_name.length!==0)request_param=$.extend(request_param,{'call_name':ptr.call_name});
      if(ptr.type!==null)request_param=$.extend(request_param,{'user_type':ptr.type});
      else request_param=$.extend(request_param,{'user_type':0});
      if(ptr.email!==null&&ptr.email.length!==0)request_param=$.extend(request_param,{'email':ptr.email});
      if(ptr.phone!==null&&ptr.phone.length!==0)request_param=$.extend(request_param,{'phone':ptr.phone});
      return request_param;
    };
    this.addUser=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getUser_RP(ptr,request_param);
      if(ptr.username!==null&&ptr.username.length!==0)request_param=$.extend(request_param,{'new_user':ptr.username});
      if(ptr.password!==null&&ptr.password.length!==0)request_param=$.extend(request_param,{'new_pass':ptr.password});
      postData(this.username,this.password,this.token,this.language,url_add_user,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getSensor_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'sensor_id':ptr.id});
      if(ptr.user_id!==null)request_param=$.extend(request_param,{'user_id':ptr.user_id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'sensor_name':ptr.name});
      if(ptr.serial_number!==null&&ptr.serial_number.length!==0)request_param=$.extend(request_param,{'serial_number':ptr.serial_number});
      if(ptr.device_name!==null&&ptr.device_name.length!==0)request_param=$.extend(request_param,{'device_name':ptr.device_name});
      if(ptr.phone!==null&&ptr.phone.length!==0)request_param=$.extend(request_param,{'phone':ptr.phone});
      return request_param;
    };
    this.addSensor=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getSensor_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_sensor,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getOrderAB_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'order_id':ptr.id});
      if(ptr.user_id!==null)request_param=$.extend(request_param,{'user_id':ptr.user_id});
      if(ptr.transport_id!==null)request_param=$.extend(request_param,{'transport_id':ptr.transport_id});
      if(ptr.order_status_id!==null)request_param=$.extend(request_param,{'status_id':ptr.order_status_id});
      if(ptr.total_price!==null&&ptr.total_price.length!==0)request_param=$.extend(request_param,{'total_price':ptr.total_price});
      if(ptr.route_distance!==null&&ptr.route_distance.length!==0)request_param=$.extend(request_param,{'route_distance':ptr.route_distance});
      if(ptr.route_duration!==null&&ptr.route_duration.length!==0)request_param=$.extend(request_param,{'route_duration':ptr.route_duration});
      if(ptr.route_data!==null&&ptr.route_data.length!==0)request_param=$.extend(request_param,{'route_data':ptr.route_data});
      if(ptr.order_lat!==null&&ptr.order_lat.length!==0)request_param=$.extend(request_param,{'order_lat':ptr.order_lat});
      if(ptr.order_lon!==null&&ptr.order_lon.length!==0)request_param=$.extend(request_param,{'order_lon':ptr.order_lon});
      if(ptr.order_address!==null&&ptr.order_address.length!==0)request_param=$.extend(request_param,{'order_address':ptr.order_address});
      if(ptr.delivery_lat!==null&&ptr.delivery_lat.length!==0)request_param=$.extend(request_param,{'delivery_lat':ptr.delivery_lat});
      if(ptr.delivery_lon!==null&&ptr.delivery_lon.length!==0)request_param=$.extend(request_param,{'delivery_lon':ptr.delivery_lon});
      if(ptr.delivery_address!==null&&ptr.delivery_address.length!==0)request_param=$.extend(request_param,{'delivery_address':ptr.delivery_address});
      if(ptr.delivery_type_id!==null)request_param=$.extend(request_param,{'delivery_type_id':ptr.delivery_type_id});
      if(ptr.reserved_date!==null&&ptr.reserved_date.length!==0)request_param=$.extend(request_param,{'reserved_date':ptr.reserved_date});
      if(ptr.reserved_hours!==null&&ptr.reserved_hours.length!==0)request_param=$.extend(request_param,{'reserved_hours':ptr.reserved_hours});
      return request_param;
    };
    this.addOrderAB=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getOrderAB_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_order_AB,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getOrderABPart_RP=function(ptr,request_param){
      if(ptr.order_id!==null)request_param=$.extend(request_param,{'order_id':ptr.order_id});
      if(ptr.product_id!==null)request_param=$.extend(request_param,{'product_id':ptr.product_id});
      if(ptr.product_count!==null&&ptr.product_count.length!==0)request_param=$.extend(request_param,{'product_count':ptr.product_count});
      if(ptr.total_price!==null&&ptr.total_price.length!==0)request_param=$.extend(request_param,{'total_price':ptr.total_price});
      return request_param;
    };
    this.addOrderABPart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getOrderABPart_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_order_AB_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.addOrderABProductPart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getOrderABPart_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_order_AB_product_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getOrderABUserPart_RP=function(ptr,request_param){
      if(ptr.order_id!==null)request_param=$.extend(request_param,{'order_id':ptr.order_id});
      if(ptr.user_id!==null)request_param=$.extend(request_param,{'user_id':ptr.user_id});
      if(ptr.pickup_status_id!==null)request_param=$.extend(request_param,{'pickup_status_id':ptr.pickup_status_id});
      if(ptr.product_param_id_1!==null)request_param=$.extend(request_param,{'product_param_id_1':ptr.product_param_id_1});
      if(ptr.product_param_id_2!==null)request_param=$.extend(request_param,{'product_param_id_2':ptr.product_param_id_2});
      if(ptr.pickup_lat!==null)request_param=$.extend(request_param,{'pickup_lat':ptr.pickup_lat});
      if(ptr.pickup_lon!==null)request_param=$.extend(request_param,{'pickup_lon':ptr.pickup_lon});
      if(ptr.pickup_address!==null)request_param=$.extend(request_param,{'pickup_address':ptr.pickup_address});
      if(ptr.pickup_date!==null)request_param=$.extend(request_param,{'pickup_date':ptr.pickup_date});
      if(ptr.pickup_info!==null)request_param=$.extend(request_param,{'pickup_info':ptr.pickup_info});
      if(ptr.price!==null&&ptr.price.length!==0)request_param=$.extend(request_param,{'price':ptr.price});
      return request_param;
    };
    this.addOrderABUserPart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getOrderABUserPart_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_order_AB_user_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getOrderABProductParamPart_RP=function(ptr,request_param){
      if(ptr.order_id!==null)request_param=$.extend(request_param,{'order_id':ptr.order_id});
      if(ptr.product_id!==null)request_param=$.extend(request_param,{'product_id':ptr.product_id});
      if(ptr.product_param_id!==null)request_param=$.extend(request_param,{'param_id':ptr.product_param_id});
      if(ptr.count!==null&&ptr.count.length!==0)request_param=$.extend(request_param,{'param_part_count':ptr.count});
      if(ptr.price!==null&&ptr.price.length!==0)request_param=$.extend(request_param,{'param_part_price':ptr.price});
      return request_param;
    };
    this.addOrderABProductParamPart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getOrderABProductParamPart_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_order_AB_product_param_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getTransport_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'transport_id':ptr.id});
      if(ptr.sensor_id!==null)request_param=$.extend(request_param,{'sensor_id':ptr.sensor_id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'transport_name':ptr.name});
      if(ptr.color!==null&&ptr.color.length!==0)request_param=$.extend(request_param,{'transport_color':ptr.color});
      if(ptr.license_plate!==null&&ptr.license_plate.length!==0)request_param=$.extend(request_param,{'license_plate':ptr.license_plate});
      return request_param;
    };
    this.addTransport=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getTransport_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_transport,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getPurchase_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'purchase_id':ptr.id});
      if(ptr.user_id!==null)request_param=$.extend(request_param,{'user_id':ptr.user_id});
      if(ptr.invoice_code!==null&&ptr.invoice_code.length!==0)request_param=$.extend(request_param,{'invoice_code':ptr.invoice_code});
      if(ptr.invoice_date!==null&&ptr.invoice_date.length!==0)request_param=$.extend(request_param,{'invoice_date':ptr.invoice_date});
      if(ptr.total_price!==null&&ptr.total_price.length!==0)request_param=$.extend(request_param,{'total_price':ptr.total_price});
      if(ptr.total_tax!==null&&ptr.total_tax.length!==0)request_param=$.extend(request_param,{'total_tax':ptr.total_tax});
      if(ptr.order_id!==null)request_param=$.extend(request_param,{'order_id':ptr.order_id});
      if(ptr.order_date!==null&&ptr.order_date.length!==0)request_param=$.extend(request_param,{'order_date':ptr.order_date});
      if(ptr.order_info!==null&&ptr.order_info.length!==0)request_param=$.extend(request_param,{'order_info':ptr.order_info});
      if(ptr.delivery_id!==null)request_param=$.extend(request_param,{'delivery_id':ptr.delivery_id});
      if(ptr.delivery_code!==null&&ptr.delivery_code.length!==0)request_param=$.extend(request_param,{'delivery_code':ptr.delivery_code});
      if(ptr.delivery_type_id!==null)request_param=$.extend(request_param,{'delivery_type_id':ptr.delivery_type_id});
      if(ptr.delivery_date!==null&&ptr.delivery_date.length!==0)request_param=$.extend(request_param,{'delivery_date':ptr.delivery_date});
      if(ptr.delivery_price!==null&&ptr.delivery_price.length!==0)request_param=$.extend(request_param,{'delivery_price':ptr.delivery_price});
      if(ptr.payment_date!==null&&ptr.payment_date.length!==0)request_param=$.extend(request_param,{'payment_date':ptr.payment_date});
      if(ptr.payment_info!==null&&ptr.payment_info.length!==0)request_param=$.extend(request_param,{'payment_info':ptr.payment_info});
      if(ptr.payment_amount!==null&&ptr.payment_amount.length!==0)request_param=$.extend(request_param,{'payment_amount':ptr.payment_amount});
      return request_param;
    };
    this.addPurchase=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getPurchase_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_purchase,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    var getPayment_RP=function(ptr,request_param){
      if(ptr.purchase_id!==null)request_param=$.extend(request_param,{'purchase_id':ptr.purchase_id});
      if(ptr.amount!==null&&ptr.amount.length!==0)request_param=$.extend(request_param,{'payment_amount':ptr.amount});
      if(ptr.currency!==null&&ptr.currency.length!==0)request_param=$.extend(request_param,{'payment_currency':ptr.currency});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(ptr.status!==null&&ptr.status.length!==0)request_param=$.extend(request_param,{'payment_status':ptr.status});
      if(ptr.transaction_id!==null&&ptr.transaction_id.length!==0)request_param=$.extend(request_param,{'transaction_id':ptr.transaction_id});
      if(ptr.phone!==null&&ptr.phone.length!==0)request_param=$.extend(request_param,{'phone':ptr.phone});
      return request_param;
    };
    this.addPayment=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getPayment_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_payment,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getPrepaidCard_RP=function(ptr,request_param){
      if(ptr.serial_number!==null)request_param=$.extend(request_param,{'serial_number':ptr.serial_number});
      if(ptr.prepaid_code!==null&&ptr.prepaid_code.length!==0)request_param=$.extend(request_param,{'prepaid_code':ptr.prepaid_code});
      if(ptr.amount!==null&&ptr.amount.length!==0)request_param=$.extend(request_param,{'amount':ptr.amount});
      return request_param;
    };
    this.addPrepaidCard=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getPrepaidCard_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_prepaid_card,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getStock_RP=function(ptr,request_param){
      if(ptr.product_id!==null)request_param=$.extend(request_param,{'product_id':ptr.product_id});
      if(ptr.store_id!==null)request_param=$.extend(request_param,{'store_id':ptr.store_id});
      if(ptr.count!==null&&ptr.count.length!==0)request_param=$.extend(request_param,{'count':ptr.count});
      return request_param;
    };
    this.addStock=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getStock_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_stock,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    var getStockInvoice_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'stock_invoice_id':ptr.id});
      if(ptr.invoice_code!==null&&ptr.invoice_code.length!==0)request_param=$.extend(request_param,{'invoice_code':ptr.invoice_code});
      if(ptr.invoice_date!==null&&ptr.invoice_date.length!==0)request_param=$.extend(request_param,{'invoice_date':ptr.invoice_date});
      if(ptr.total_price!==null&&ptr.total_price.length!==0)request_param=$.extend(request_param,{'total_price':ptr.total_price});
      if(ptr.total_tax!==null&&ptr.total_tax.length!==0)request_param=$.extend(request_param,{'total_tax':ptr.total_tax});
      if(ptr.supplier!==null&&ptr.supplier.length!==0)request_param=$.extend(request_param,{'supplier':ptr.supplier});
      if(ptr.phone!==null&&ptr.phone.length!==0)request_param=$.extend(request_param,{'phone':ptr.phone});
      if(ptr.delivery_id!==null)request_param=$.extend(request_param,{'delivery_id':ptr.delivery_id});
      if(ptr.delivery_code!==null&&ptr.delivery_code.length!==0)request_param=$.extend(request_param,{'delivery_code':ptr.delivery_code});
      if(ptr.delivery_type_id!==null)request_param=$.extend(request_param,{'delivery_type_id':ptr.delivery_type_id});
      if(ptr.delivery_date!==null&&ptr.delivery_date.length!==0)request_param=$.extend(request_param,{'delivery_date':ptr.delivery_date});
      if(ptr.delivery_price!==null&&ptr.delivery_price.length!==0)request_param=$.extend(request_param,{'delivery_price':ptr.delivery_price});
      if(ptr.payment_date!==null&&ptr.payment_date.length!==0)request_param=$.extend(request_param,{'payment_date':ptr.payment_date});
      if(ptr.payment_info!==null&&ptr.payment_info.length!==0)request_param=$.extend(request_param,{'payment_info':ptr.payment_info});
      if(ptr.payment_amount!==null&&ptr.payment_amount.length!==0)request_param=$.extend(request_param,{'payment_amount':ptr.payment_amount});
      return request_param;
    };
    this.addStockInvoice=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getStockInvoice_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_stock_invoice,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    var getStockInvoicePart_RP=function(ptr,request_param){
      if(ptr.stock_invoice_id!==null)request_param=$.extend(request_param,{'stock_invoice_id':ptr.stock_invoice_id});
      if(ptr.product_id!==null)request_param=$.extend(request_param,{'product_id':ptr.product_id});
      if(ptr.tax_id!==null)request_param=$.extend(request_param,{'tax_id':ptr.tax_id});
      if(ptr.count!==null&&ptr.count.length!==0)request_param=$.extend(request_param,{'stock_invoice_count':ptr.count});
      if(ptr.price!==null&&ptr.price.length!==0)request_param=$.extend(request_param,{'stock_invoice_price':ptr.price});
      return request_param;
    };
    this.addStockInvoicePart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getStockInvoicePart_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_stock_invoice_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    var getStore_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'store_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'store_name':ptr.name});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      return request_param;
    };
    this.addStore=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getStore_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_store,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    var getStorePart_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'part_id':ptr.id});
      if(ptr.store_id!==null)request_param=$.extend(request_param,{'store_id':ptr.store_id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'part_name':ptr.name});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(ptr.latitude!==null&&ptr.latitude.length!==0)request_param=$.extend(request_param,{'latitude':ptr.latitude});
      if(ptr.longitude!==null&&ptr.longitude.length!==0)request_param=$.extend(request_param,{'longitude':ptr.longitude});
      if(ptr.email!==null&&ptr.email.length!==0)request_param=$.extend(request_param,{'email':ptr.email});
      if(ptr.phone1!==null&&ptr.phone1.length!==0)request_param=$.extend(request_param,{'phone1':ptr.phone1});
      if(ptr.phone2!==null&&ptr.phone2.length!==0)request_param=$.extend(request_param,{'phone2':ptr.phone2});
      if(ptr.address!==null&&ptr.address.length!==0)request_param=$.extend(request_param,{'address':ptr.address});
      if(ptr.city!==null&&ptr.city.length!==0)request_param=$.extend(request_param,{'city':ptr.city});
      if(ptr.postcode!==null&&ptr.postcode.length!==0)request_param=$.extend(request_param,{'postcode':ptr.postcode});
      if(ptr.language!==null&&ptr.language.length!==0)request_param=$.extend(request_param,{'language':ptr.language});
      return request_param;
    };
    this.addStorePart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getStorePart_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_store_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getManufacture_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'manufacture_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'manufacture_name':ptr.name});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      return request_param;
    };
    this.addManufacture=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getManufacture_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_manufacture,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getProduct_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'product_id':ptr.id});
      if(ptr.manufacture_id!==null)request_param=$.extend(request_param,{'manufacture_id':ptr.manufacture_id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'product_name':ptr.name});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(ptr.code!==null&&ptr.code.length!==0)request_param=$.extend(request_param,{'product_code':ptr.code});
      if(ptr.price!==null&&ptr.price.length!==0)request_param=$.extend(request_param,{'product_price':ptr.price});
      return request_param;
    };
    this.addProduct=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getProduct_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_product,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.addProductParamPart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      if(ptr.product_id!==null)request_param=$.extend(request_param,{'product_id':ptr.product_id});
      if(ptr.product_param_id!==null)request_param=$.extend(request_param,{'param_id':ptr.product_param_id});
      if(ptr.price!==null&&ptr.price.length!==0)request_param=$.extend(request_param,{'param_part_price':ptr.price});
      if(ptr.value!==null&&ptr.value.length!==0)request_param=$.extend(request_param,{'param_part_value':ptr.value});
      postData(this.username,this.password,this.token,this.language,url_add_product_param_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    /*add elemental*/
    this.addProductTypePart=function(product_id,type_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(product_id!==null)request_param=$.extend(request_param,{'product_id':product_id});
      if(type_id!==null)request_param=$.extend(request_param,{'type_id':type_id});
      postData(this.username,this.password,this.token,this.language,url_add_product_type_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.addProductCircle=function(productA_id,productB_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(productA_id!==null)request_param=$.extend(request_param,{'productA_id':productA_id});
      if(productB_id!==null)request_param=$.extend(request_param,{'productB_id':productB_id});
      postData(this.username,this.password,this.token,this.language,url_add_product_circle,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.addSensorCircle=function(sensorA_id,sensorB_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(sensorA_id!==null)request_param=$.extend(request_param,{'sensorA_id':sensorA_id});
      if(sensorB_id!==null)request_param=$.extend(request_param,{'sensorB_id':sensorB_id});
      postData(this.username,this.password,this.token,this.language,url_add_sensor_circle,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.addSensorCircleToUser=function(sensor_id,user_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(sensor_id!==null)request_param=$.extend(request_param,{'sensor_id':sensor_id});
      if(user_id!==null)request_param=$.extend(request_param,{'user_id':user_id});
      postData(this.username,this.password,this.token,this.language,url_add_sensor_circle_to_user,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.addSensorGroupPart=function(group_id,sensor_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(group_id!==null)request_param=$.extend(request_param,{'group_id':group_id});
      if(sensor_id!==null)request_param=$.extend(request_param,{'sensor_id':sensor_id});
      postData(this.username,this.password,this.token,this.language,url_add_sensor_group_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.addTransportProductPart=function(transport_id,product_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(transport_id!==null)request_param=$.extend(request_param,{'transport_id':transport_id});
      if(product_id!==null)request_param=$.extend(request_param,{'product_id':product_id});
      postData(this.username,this.password,this.token,this.language,url_add_transport_product_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.addTransportTypePart=function(transport_id,type_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(transport_id!==null)request_param=$.extend(request_param,{'transport_id':transport_id});
      if(type_id!==null)request_param=$.extend(request_param,{'type_id':type_id});
      postData(this.username,this.password,this.token,this.language,url_add_transport_type_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    /*add exclusive*/
    this.addMessage=function(recipient,message,message_ptr,callback,callback_ptr){
      var request_param={};
      if(recipient!==null&&recipient.length!==0)request_param=$.extend(request_param,{'recipient':recipient});
      if(message!==null&&message.length!==0)request_param=$.extend(request_param,{'message':message});
      postData(this.username,this.password,this.token,this.language,url_add_message,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    /*add types*/
    var getObjectType_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'type_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'type_name':ptr.name});
      if(ptr.language!==null&&ptr.language.length!==0)request_param=$.extend(request_param,{'language':ptr.language});
      return request_param;
    };
    this.addDeliveryType=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getObjectType_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_delivery_type,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.addTrackType=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getObjectType_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_track_type,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    var getOrderStatus_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'status_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'status_name':ptr.name});
      if(ptr.language!==null&&ptr.language.length!==0)request_param=$.extend(request_param,{'language':ptr.language});
      return request_param;
    };
    this.addOrderStatus=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getOrderStatus_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_order_status,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    var getPickupStatus_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'status_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'status_name':ptr.name});
      if(ptr.language!==null&&ptr.language.length!==0)request_param=$.extend(request_param,{'language':ptr.language});
      return request_param;
    };
    this.addPickupStatus=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getOrderStatus_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_pickup_status,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    var getProductParam_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'param_id':ptr.id});
      if(ptr.parent_id!==null)request_param=$.extend(request_param,{'param_parent_id':ptr.parent_id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'param_name':ptr.name});
      if(ptr.value!==null&&ptr.value.length!==0)request_param=$.extend(request_param,{'param_value':ptr.value});
      if(ptr.language!==null&&ptr.language.length!==0)request_param=$.extend(request_param,{'language':ptr.language});
      return request_param;
    };
    this.addProductParam=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getProductParam_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_product_param,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    var getObjectTypeExt_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'type_id':ptr.id});
      if(ptr.parent_id!==null)request_param=$.extend(request_param,{'type_parent_id':ptr.parent_id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'type_name':ptr.name});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(ptr.language!==null&&ptr.language.length!==0)request_param=$.extend(request_param,{'language':ptr.language});
      return request_param;
    };
    this.addProductType=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getObjectTypeExt_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_product_type,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.addTransportType=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getObjectTypeExt_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_transport_type,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    /*substitute keywords*/
    this.addAttr=function(name,message_ptr,callback,callback_ptr){
      var request_param={};
      if(name!==null&&name.length!==0)request_param=$.extend(request_param,{'attr_name':name});
      postData(this.username,this.password,this.token,this.language,url_add_attr,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.addColor=function(name,message_ptr,callback,callback_ptr){
      var request_param={};
      if(name!==null&&name.length!==0)request_param=$.extend(request_param,{'color_name':name});
      postData(this.username,this.password,this.token,this.language,url_add_color,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    var getAttrPart_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'attr_part_id':ptr.id});
      if(ptr.object_id!==null)request_param=$.extend(request_param,{'object_id':ptr.object_id});
      if(ptr.object_name!==null&&ptr.object_name.length!==0)request_param=$.extend(request_param,{'object_name':ptr.object_name});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'attr_name':ptr.name});
      if(ptr.value!==null&&ptr.value.length!==0)request_param=$.extend(request_param,{'attr_value':ptr.value});
      if(ptr.language!==null&&ptr.language.length!==0)request_param=$.extend(request_param,{'language':ptr.language});
      return request_param;
    };
    this.addAttrPart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getAttrPart_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_attr_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getLanguage_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'language_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'language_name':ptr.name});
      if(ptr.value!==null&&ptr.value.length!==0)request_param=$.extend(request_param,{'language_value':ptr.value});
      if(ptr.code!==null&&ptr.code.length!==0)request_param=$.extend(request_param,{'language_code':ptr.code});
      return request_param;
    };
    this.addLanguage=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLanguage_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_language,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getCurrency_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'currency_id':ptr.id});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'currency_name':ptr.name});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(ptr.value!==null&&ptr.value.length!==0)request_param=$.extend(request_param,{'currency_value':ptr.value});
      if(ptr.language!==null&&ptr.language.length!==0)request_param=$.extend(request_param,{'language':ptr.language});
      return request_param;
    };
    this.addCurrency=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getCurrency_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_currency,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getDiscount_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'discount_id':ptr.id});
      if(ptr.product_type_id!==null)request_param=$.extend(request_param,{'product_type_id':ptr.product_type_id});
      if(ptr.type!==null&&ptr.type.length!==0)request_param=$.extend(request_param,{'discount_type':ptr.type});
      if(ptr.code!==null&&ptr.code.length!==0)request_param=$.extend(request_param,{'discount_code':ptr.code});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'discount_name':ptr.name});
      if(ptr.value!==null&&ptr.value.length!==0)request_param=$.extend(request_param,{'discount_value':ptr.value});
      if(ptr.language!==null&&ptr.language.length!==0)request_param=$.extend(request_param,{'language':ptr.language});
      if(ptr.start_date!==null&&ptr.start_date.length!==0)request_param=$.extend(request_param,{'start_date':ptr.start_date});
      if(ptr.finish_date!==null&&ptr.finish_date.length!==0)request_param=$.extend(request_param,{'finish_date':ptr.finish_date});
      return request_param;
    };
    this.addDiscount=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getDiscount_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_discount,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    var getTax_RP=function(ptr,request_param){
      if(ptr.id!==null)request_param=$.extend(request_param,{'tax_id':ptr.id});
      if(ptr.code!==null&&ptr.code.length!==0)request_param=$.extend(request_param,{'tax_code':ptr.code});
      if(ptr.name!==null&&ptr.name.length!==0)request_param=$.extend(request_param,{'tax_name':ptr.name});
      if(ptr.value!==null&&ptr.value.length!==0)request_param=$.extend(request_param,{'tax_value':ptr.value});
      if(ptr.language!==null&&ptr.language.length!==0)request_param=$.extend(request_param,{'language':ptr.language});
      return request_param;
    };
    this.addTax=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getTax_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_add_tax,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.addUserReview=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      if(ptr.user_id!==null)request_param=$.extend(request_param,{'user_id':ptr.user_id});
      if(ptr.object_id!==null)request_param=$.extend(request_param,{'review_user_id':ptr.object_id});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(ptr.value!==null&&ptr.value.length!==0)request_param=$.extend(request_param,{'review_value':ptr.value});
      postData(this.username,this.password,this.token,this.language,url_add_user_review,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.addProductReview=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      if(ptr.user_id!==null)request_param=$.extend(request_param,{'user_id':ptr.user_id});
      if(ptr.object_id!==null)request_param=$.extend(request_param,{'product_id':ptr.object_id});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(ptr.value!==null&&ptr.value.length!==0)request_param=$.extend(request_param,{'review_value':ptr.value});
      postData(this.username,this.password,this.token,this.language,url_add_product_review,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.addTransportReview=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      if(ptr.order_id!==null)request_param=$.extend(request_param,{'order_id':ptr.order_id});
      if(ptr.user_id!==null)request_param=$.extend(request_param,{'user_id':ptr.user_id});
      if(ptr.object_id!==null)request_param=$.extend(request_param,{'transport_id':ptr.object_id});
      if(ptr.description!==null&&ptr.description.length!==0)request_param=$.extend(request_param,{'description':ptr.description});
      if(ptr.value!==null&&ptr.value.length!==0)request_param=$.extend(request_param,{'review_value':ptr.value});
      postData(this.username,this.password,this.token,this.language,url_add_transport_review,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.addTrack=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      if(ptr.sensor_id!==null)request_param=$.extend(request_param,{'sensor_id':ptr.sensor_id});
      if(ptr.type_id!==null)request_param=$.extend(request_param,{'type_id':ptr.type_id});
      if(ptr.latitude!==null&&ptr.latitude.length!==0)request_param=$.extend(request_param,{'latitude':ptr.latitude});
      if(ptr.longitude!==null&&ptr.longitude.length!==0)request_param=$.extend(request_param,{'longitude':ptr.longitude});
      if(ptr.time!==null&&ptr.time.length!==0)request_param=$.extend(request_param,{'track_time':ptr.time});
      if(ptr.altitude!==null&&ptr.altitude.length!==0)request_param=$.extend(request_param,{'altitude':ptr.altitude});
      if(ptr.accuracy!==null&&ptr.accuracy.length!==0)request_param=$.extend(request_param,{'accuracy':ptr.accuracy});
      if(ptr.bearing!==null&&ptr.bearing.length!==0)request_param=$.extend(request_param,{'bearing':ptr.bearing});
      if(ptr.speed!==null&&ptr.speed.length!==0)request_param=$.extend(request_param,{'speed':ptr.speed});
      if(ptr.satellites!==null&&ptr.satellites.length!==0)request_param=$.extend(request_param,{'satellites':ptr.satellites});
      if(ptr.battery!==null&&ptr.battery.length!==0)request_param=$.extend(request_param,{'battery':ptr.battery});
      if(ptr.timezone_offset!==null&&ptr.timezone_offset.length!==0)request_param=$.extend(request_param,{'timezone_offset':ptr.timezone_offset});
      postData(this.username,this.password,this.token,this.language,url_add_track,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    /*update objects*/
    this.updateSettings=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getSettings_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_settings,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateProjectActivity=function(schema_name,activity,message_ptr,callback,callback_ptr){
      var request_param={};
      if(schema_name!==null&&schema_name.length!==0)request_param=$.extend(request_param,{'schema_name':schema_name});
      if(activity!==null&&activity.length!==0)request_param=$.extend(request_param,{'activity':activity});
      postData(this.username,this.password,this.token,this.language,url_update_project_activity,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    /*UPDATE picture: manufacture,product,product_param,sensor_place,store,store_part,transport*/
    this.updatePicture=function(picture_id,picture,object_name,message_ptr,callback,callback_ptr){
      var request_param={};
      if(picture_id!==null)request_param=$.extend(request_param,{'picture_id':picture_id});
      if(picture!==null&&picture.length!==0)request_param=$.extend(request_param,{'picture':picture});
      if(object_name!==null&&object_name.length!==0)request_param=$.extend(request_param,{'object_name':object_name});
      postData(this.username,this.password,this.token,this.language,url_update_picture,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateUserPicture=function(user_id,picture,message_ptr,callback,callback_ptr){
      var request_param={};
      if(user_id!==null)request_param=$.extend(request_param,{'user_id':picture_id});
      if(picture!==null&&picture.length!==0)request_param=$.extend(request_param,{'picture':picture});
      postData(this.username,this.password,this.token,this.language,url_update_user_picture,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateProductParamPartPicture=function(product_id,param_id,picture,message_ptr,callback,callback_ptr){
      var request_param={};
      if(product_id!==null)request_param=$.extend(request_param,{'product_id':product_id});
      if(param_id!==null)request_param=$.extend(request_param,{'param_id':param_id});
      if(picture!==null&&picture.length!==0)request_param=$.extend(request_param,{'picture':picture});
      postData(this.username,this.password,this.token,this.language,url_update_product_param_part_picture,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateTrackPartPicture=function(track_part_id,picture,message_ptr,callback,callback_ptr){
      var request_param={};
      if(track_part_id!==null)request_param=$.extend(request_param,{'track_part_id':track_part_id});
      if(picture!==null&&picture.length!==0)request_param=$.extend(request_param,{'picture':picture});
      postData(this.username,this.password,this.token,this.language,url_update_track_part_picture,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updateUser=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getUser_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_user,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateUserPassword=function(password,message_ptr,callback,callback_ptr){
      var request_param={};
      if(password!==null&&password.length!==0)request_param=$.extend(request_param,{'new_password':password});
      postData(this.username,this.password,this.token,this.language,url_update_user_password,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateUserActivity=function(user_id,activity,message_ptr,callback,callback_ptr){
      var request_param={};
      if(user_id!==null)request_param=$.extend(request_param,{'user_id':user_id});
      if(activity!==null&&activity.length!==0)request_param=$.extend(request_param,{'activity':activity});
      postData(this.username,this.password,this.token,this.language,url_update_user_activity,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateUserDiscount=function(user_id,discount_code,message_ptr,callback,callback_ptr){
      var request_param={};
      if(user_id!==null)request_param=$.extend(request_param,{'user_id':user_id});
      if(discount_code!==null&&discount_code.length!==0)request_param=$.extend(request_param,{'discount_code':discount_code});
      postData(this.username,this.password,this.token,this.language,url_update_user_discount,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateUserPrepaidAmount=function(user_id,prepaid_amount,message_ptr,callback,callback_ptr){
      var request_param={};
      if(user_id!==null)request_param=$.extend(request_param,{'user_id':user_id});
      if(prepaid_amount!==null&&prepaid_amount.length!==0)request_param=$.extend(request_param,{'prepaid_amount':prepaid_amount});
      postData(this.username,this.password,this.token,this.language,url_update_user_prepaid_amount,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.increaseUserPrepaidAmount=function(user_id,amount,message_ptr,callback,callback_ptr){
      var request_param={};
      if(user_id!==null)request_param=$.extend(request_param,{'user_id':user_id});
      if(amount!==null&&amount.length!==0)request_param=$.extend(request_param,{'amount':amount});
      postData(this.username,this.password,this.token,this.language,url_increase_user_prepaid_amount,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.decreaseUserPrepaidAmount=function(user_id,amount,message_ptr,callback,callback_ptr){
      var request_param={};
      if(user_id!==null)request_param=$.extend(request_param,{'user_id':user_id});
      if(amount!==null&&amount.length!==0)request_param=$.extend(request_param,{'amount':amount});
      postData(this.username,this.password,this.token,this.language,url_decrease_user_prepaid_amount,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.increaseUserPrepaidAmountByPrepaidCode=function(user_id,prepaid_code,message_ptr,callback,callback_ptr){
      var request_param={};
      if(prepaid_code!==null&&prepaid_code.length!==0)request_param=$.extend(request_param,{'prepaid_code':prepaid_code});
      postData(this.username,this.password,this.token,this.language,url_increase_user_prepaid_amount_by_prepaid_code,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updateLanguage=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getLanguage_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_language,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateLanguageActivity=function(language_id,activity,message_ptr,callback,callback_ptr){
      var request_param={};
      if(language_id!==null)request_param=$.extend(request_param,{'language_id':language_id});
      if(activity!==null&&activity.length!==0)request_param=$.extend(request_param,{'activity':activity});
      postData(this.username,this.password,this.token,this.language,url_update_language_activity,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateCurrency=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getCurrency_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_currency,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateCurrencyActivity=function(currency_id,activity,message_ptr,callback,callback_ptr){
      var request_param={};
      if(currency_id!==null)request_param=$.extend(request_param,{'currency_id':currency_id});
      if(activity!==null&&activity.length!==0)request_param=$.extend(request_param,{'activity':activity});
      postData(this.username,this.password,this.token,this.language,url_update_currency_activity,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateAttrPart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getAttrPart_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_attr_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    /*update types*/
    this.updateDeliveryType=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getObjectType_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_delivery_type,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateTrackType=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getObjectType_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_track_type,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateOrderStatus=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getOrderStatus_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_order_status,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updatePickupStatus=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getPickupStatus_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_pickup_status,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateProductParam=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getProductParam_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_product_param,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateProductType=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getObjectTypeExt_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_product_type,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateTransportType=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getObjectTypeExt_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_transport_type,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updateOrderABStatus=function(order_id,status_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(order_id!==null)request_param=$.extend(request_param,{'order_id':order_id});
      if(status_id!==null)request_param=$.extend(request_param,{'status_id':status_id});
      postData(this.username,this.password,this.token,this.language,url_update_order_AB_status,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateOrderABUserPartStatus=function(order_id,user_id,status_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(order_id!==null)request_param=$.extend(request_param,{'order_id':order_id});
      if(user_id!==null)request_param=$.extend(request_param,{'user_id':user_id});
      if(status_id!==null)request_param=$.extend(request_param,{'status_id':status_id});
      postData(this.username,this.password,this.token,this.language,url_update_order_AB_user_part_status,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updateOrderABTransport=function(order_id,transport_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(order_id!==null)request_param=$.extend(request_param,{'order_id':order_id});
      if(transport_id!==null)request_param=$.extend(request_param,{'transport_id':transport_id});
      postData(this.username,this.password,this.token,this.language,url_update_order_AB_transport,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updateOrderAB=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getOrderAB_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_order_AB,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updateOrderABPart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getOrderABPart_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_order_AB_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateOrderABProductPart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getOrderABPart_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_order_AB_product_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateOrderABUserPart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getOrderABUserPart_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_order_AB_user_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updateOrderABProductParamPart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getOrderABProductParamPart_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_order_AB_product_param_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updatePurchase=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getPurchase_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_purchase,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updatePayment=function(ptr,message_ptr,callback,callback_ptr){//not used
      var request_param={};
      request_param=getPayment_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_payment,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updatePrepaidCardActivity=function(prepaid_card_id,activity,message_ptr,callback,callback_ptr){
      var request_param={};
      if(prepaid_card_id!==null)request_param=$.extend(request_param,{'prepaid_card_id':prepaid_card_id});
      if(activity!==null&&activity.length!==0)request_param=$.extend(request_param,{'activity':activity});
      postData(this.username,this.password,this.token,this.language,url_update_prepaid_card_activity,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updateTransport=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getTransport_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_transport,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updateSensor=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getSensor_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_sensor,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateSensorActivity=function(sensor_id,activity,message_ptr,callback,callback_ptr){
      var request_param={};
      if(sensor_id!==null)request_param=$.extend(request_param,{'sensor_id':sensor_id});
      if(activity!==null&&activity.length!==0)request_param=$.extend(request_param,{'activity':activity});
      postData(this.username,this.password,this.token,this.language,url_update_sensor_activity,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updateStock=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getStock_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_stock,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateStockInvoice=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getStockInvoice_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_stock_invoice,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateStockInvoicePart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getStockInvoicePart_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_stock_invoice_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updateStore=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getStore_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_store,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateStoreActivity=function(store_id,activity,message_ptr,callback,callback_ptr){
      var request_param={};
      if(store_id!==null)request_param=$.extend(request_param,{'store_id':store_id});
      if(activity!==null&&activity.length!==0)request_param=$.extend(request_param,{'activity':activity});
      postData(this.username,this.password,this.token,this.language,url_update_store_activity,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateStorePart=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getStorePart_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_store_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updateManufacture=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getManufacture_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_manufacture,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updateProduct=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getProduct_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_product,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateProductDiscount=function(product_id,discount_code,message_ptr,callback,callback_ptr){
      var request_param={};
      if(product_id!==null)request_param=$.extend(request_param,{'product_id':product_id});
      if(discount_code!==null&&discount_code.length!==0)request_param=$.extend(request_param,{'discount_code':discount_code});
      postData(this.username,this.password,this.token,this.language,url_update_product_discount,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.updateDiscount=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getDiscount_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_discount,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.updateTax=function(ptr,message_ptr,callback,callback_ptr){
      var request_param={};
      request_param=getTax_RP(ptr,request_param);
      postData(this.username,this.password,this.token,this.language,url_update_tax,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    /*remove objects*/
    this.removeSettings=function(settings_code,message_ptr,callback,callback_ptr){
      var request_param={};
      if(settings_code!==null&&settings_code.length!==0)request_param=$.extend(request_param,{'settings_code':settings_code});
      postData(this.username,this.password,this.token,this.language,url_remove_settings,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeProject=function(schema_name,message_ptr,callback,callback_ptr){
      var request_param={};
      if(schema_name!==null&&schema_name.length!==0)request_param=$.extend(request_param,{'schema_name':schema_name});
      postData(this.username,this.password,this.token,this.language,url_remove_project,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    /*REMOVE object: attr_part,currency,delivery,delivery_type,discount,manufacture,message,order_AB,product,product_param,product_type,prepaid_card
                     purchase,order_status,pickup_status,sensor,sensor_group,sensor_place,stock_invoice,store,tax,track,track_part,transport,transport_type,user*/
    this.removeObject=function(object_id,object_name,message_ptr,callback,callback_ptr){
      var request_param={};
      if(object_id!==null)request_param=$.extend(request_param,{'object_id':object_id});
      if(object_name!==null&&object_name.length!==0)request_param=$.extend(request_param,{'object_name':object_name});
      postData(this.username,this.password,this.token,this.language,url_remove_object,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeSensorCircle=function(sensorA_id,sensorB_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(sensorA_id!==null)request_param=$.extend(request_param,{'sensorA_id':sensorA_id});
      if(sensorB_id!==null)request_param=$.extend(request_param,{'sensorB_id':sensorB_id});
      postData(this.username,this.password,this.token,this.language,url_remove_sensor_circle,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeSensorCircleToUser=function(sensor_id,user_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(sensor_id!==null)request_param=$.extend(request_param,{'sensor_id':sensor_id});
      if(user_id!==null)request_param=$.extend(request_param,{'user_id':user_id});
      postData(this.username,this.password,this.token,this.language,url_remove_sensor_circle_to_user,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeAttr=function(attr_name,message_ptr,callback,callback_ptr){
      var request_param={};
      if(attr_name!==null&&attr_name.length!==0)request_param=$.extend(request_param,{'attr_name':attr_name});
      postData(this.username,this.password,this.token,this.language,url_remove_attr,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeColor=function(color_name,message_ptr,callback,callback_ptr){
      var request_param={};
      if(color_name!==null&&color_name.length!==0)request_param=$.extend(request_param,{'color_name':color_name});
      postData(this.username,this.password,this.token,this.language,url_remove_color,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeStock=function(product_id,store_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(product_id!==null)request_param=$.extend(request_param,{'product_id':product_id});
      if(store_id!==null)request_param=$.extend(request_param,{'store_id':store_id});
      postData(this.username,this.password,this.token,this.language,url_remove_stock,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeStockInvoicePart=function(stock_invoice_id,product_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(stock_invoice_id!==null)request_param=$.extend(request_param,{'stock_invoice_id':stock_invoice_id});
      if(product_id!==null)request_param=$.extend(request_param,{'product_id':product_id});
      postData(this.username,this.password,this.token,this.language,url_remove_stock_invoice_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeOrderABPart=function(order_id,product_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(order_id!==null)request_param=$.extend(request_param,{'order_id':order_id});
      if(product_id!==null)request_param=$.extend(request_param,{'product_id':product_id});
      postData(this.username,this.password,this.token,this.language,url_remove_order_AB_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeOrderABProductPart=function(order_id,product_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(order_id!==null)request_param=$.extend(request_param,{'order_id':order_id});
      if(product_id!==null)request_param=$.extend(request_param,{'product_id':product_id});
      postData(this.username,this.password,this.token,this.language,url_remove_order_AB_product_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeOrderABUserPart=function(order_id,user_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(order_id!==null)request_param=$.extend(request_param,{'order_id':order_id});
      if(user_id!==null)request_param=$.extend(request_param,{'user_id':user_id});
      postData(this.username,this.password,this.token,this.language,url_remove_order_AB_user_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    this.removeOrderABProductParamPart=function(order_id,product_id,param_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(order_id!==null)request_param=$.extend(request_param,{'order_id':order_id});
      if(product_id!==null)request_param=$.extend(request_param,{'product_id':product_id});
      if(param_id!==null)request_param=$.extend(request_param,{'param_id':param_id});
      postData(this.username,this.password,this.token,this.language,url_remove_order_AB_product_param_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeUserReview=function(user_id,review_user_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(user_id!==null)request_param=$.extend(request_param,{'user_id':user_id});
      if(review_user_id!==null)request_param=$.extend(request_param,{'review_user_id':review_user_id});
      postData(this.username,this.password,this.token,this.language,url_remove_user_review,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeProductReview=function(user_id,product_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(user_id!==null)request_param=$.extend(request_param,{'user_id':user_id});
      if(product_id!==null)request_param=$.extend(request_param,{'product_id':product_id});
      postData(this.username,this.password,this.token,this.language,url_remove_product_review,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeTransportReview=function(order_id,user_id,transport_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(order_id!==null)request_param=$.extend(request_param,{'order_id':order_id});
      if(user_id!==null)request_param=$.extend(request_param,{'user_id':user_id});
      if(transport_id!==null)request_param=$.extend(request_param,{'transport_id':transport_id});
      postData(this.username,this.password,this.token,this.language,url_remove_transport_review,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeProductParamPart=function(product_id,param_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(product_id!==null)request_param=$.extend(request_param,{'product_id':product_id});
      if(param_id!==null)request_param=$.extend(request_param,{'param_id':param_id});
      postData(this.username,this.password,this.token,this.language,url_remove_product_param_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeProductTypePart=function(product_id,type_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(product_id!==null)request_param=$.extend(request_param,{'product_id':product_id});
      if(type_id!==null)request_param=$.extend(request_param,{'type_id':type_id});
      postData(this.username,this.password,this.token,this.language,url_remove_product_type_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };
    this.removeTransportTypePart=function(transport_id,type_id,message_ptr,callback,callback_ptr){
      var request_param={};
      if(transport_id!==null)request_param=$.extend(request_param,{'transport_id':transport_id});
      if(type_id!==null)request_param=$.extend(request_param,{'type_id':type_id});
      postData(this.username,this.password,this.token,this.language,url_remove_transport_type_part,request_param,message_ptr,saveRetVal,this.ret_val_list,callback,callback_ptr);
    };

    /*MAPS are Google,Yandex,OSM supported*/
    /*Warning! Before using this functions downloads provider support library*/
    /*
    this.name=function(){
      if(this.map_provider===null||this.map_provider===provider_list[0]){//google
      }
      else if(this.map_provider===provider_list[1]){//yandex
      }
      else if(this.map_provider===provider_list[2]){//osm
      }
    };
    */
    this.createMap=function(canvas,lat,lng,zoom){
      var map=null,map_position,map_options;
      if(this.map_provider===null||this.map_provider===provider_list[0]){/*google*/
        map_position=new google.maps.LatLng(lat,lng);
        map_options={center:map_position,zoom:zoom,mapTypeId:google.maps.MapTypeId.ROADMAP};
        map=new google.maps.Map(document.getElementById(canvas),map_options);
      }
      else if(this.map_provider===provider_list[1]){/*yandex*/
        map_position=new ymaps.geometry.Point([lat,lng]);
        map_options={center:map_position.getCoordinates(),zoom:zoom,controls:['zoomControl','fullscreenControl']};
        map=new ymaps.Map(canvas,map_options);
      }
      else if(this.map_provider===provider_list[2]){/*osm*/
        map_position=L.latLng(lat,lng);
        map_options={center:map_position,zoom:zoom};
        map=L.map(canvas,map_options);
        L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png',{attribution:'&copy; <a href="http://osm.org/copyright">OpenStreetMap</a>'}).addTo(map);
      }
      if(map!==null)this.map_list.push(map);
      return map;
    };
    this.createMarker=function(map,lat,lng,image,image_width,image_height,title,id,track,order){
      var marker=null,marker_position,marker_options,icon_options;
      if(this.map_provider===null||this.map_provider===provider_list[0]){/*google*/
        icon_options={url:image};
        if(image_width!==null&&image_height!==null){
          icon_options=$.extend(icon_options,{scaledSize:new google.maps.Size(image_width,image_height)});
        }
        marker_position=new google.maps.LatLng(lat,lng);
        marker_options={/*map:map,*/icon:icon_options,position:marker_position,title:title};
        marker=new google.maps.Marker(marker_options);
        marker.setMap(map);
      }
      else if(this.map_provider===provider_list[1]){/*yandex*/
        var image_offset_width=(image_width!==null?(image_width/2)*(-1).toFixed():DEFAULT_YANDEX_MARKER_OFFSET[0]);
        var image_offset_height=(image_height!==null?image_height*(-1).toFixed()+2:DEFAULT_YANDEX_MARKER_OFFSET[1]);
        var balloon_offset_width=0,
            balloon_offset_height=(image_height!==null?image_height*(-1):DEFAULT_YANDEX_MARKER_OFFSET[1]);
        icon_options={iconLayout:'default#image',iconImageHref:image,iconImageOffset:[image_offset_width,image_offset_height],balloonOffset:[balloon_offset_width,balloon_offset_height],hideIconOnBalloonOpen:false,openEmptyBalloon:true};
        if(image_width!==null&&image_height!==null){
          icon_options=$.extend(icon_options,{iconImageSize:[image_width,image_height]});
        }
        marker_position=[lat,lng];
        marker=new ymaps.GeoObject({
          geometry:{type:'Point',coordinates:marker_position},
          properties:{hintContent:title,balloonContentBody:null}},icon_options);
        map.geoObjects.add(marker);
      }
      else if(this.map_provider===provider_list[2]){/*osm*/
        icon_options={iconUrl:image};
        if(image_width!==null&&image_height!==null){
          var anchor_offset_width=(image_width/2).toFixed(),anchor_offset_height=image_height-2;
          icon_options=$.extend(icon_options,{iconSize:[image_width,image_height],iconAnchor:[anchor_offset_width,anchor_offset_height]});
        }
        else icon_options=$.extend(icon_options,{iconAnchor:DEFAULT_OSM_MARKER_OFFSET});
        var icon=L.icon(icon_options);
        marker_position=L.latLng(lat,lng);
        marker_options={icon:icon,title:title};
        marker=L.marker(marker_position,marker_options);
        marker.addTo(map);
      }
      if(marker!==null){
        var map_marker=new mapMarker();
        map_marker.map=map;
        map_marker.marker=marker;
        map_marker.latitude=lat;
        map_marker.longitude=lng;
        map_marker.image_width=image_width;
        map_marker.image_height=image_height;
        if(id)map_marker.id=id;
        if(track)map_marker.track=track;
        if(order)map_marker.order=order;
        this.marker_list.push(map_marker);
      }
      return marker;
    };
    /*list is array of position[lat,lng]*/
    this.createPolyline=function(map,list,color,opacity,weight,id,order){
      var polyline=null,polyline_positions=[],polyline_options;
      if(this.map_provider===null||this.map_provider===provider_list[0]){/*google*/
        for(var j=0;j<list.length;j++){
          polyline_positions.push(new google.maps.LatLng(list[j][0],list[j][1]));
        }
        polyline_options={/*map:map,*/path:polyline_positions,strokeColor:color,strokeOpacity:opacity,strokeWeight:weight};
        polyline=new google.maps.Polyline(polyline_options);
        polyline.setMap(map);
      }
      else if(this.map_provider===provider_list[1]){/*yandex*/
        polyline_positions=list;
        polyline=new ymaps.GeoObject({geometry:{type:'LineString',coordinates:polyline_positions},properties:{hintContent:''}},
                                     {draggable:false,strokeColor:color,strokeOpacity:opacity,strokeWidth:weight});
        map.geoObjects.add(polyline);
      }
      else if(this.map_provider===provider_list[2]){/*osm*/
        for(var j=0;j<list.length;j++){
          polyline_positions.push(L.latLng([list[j][0],list[j][1]]));
        }
        polyline_options={color:color,opacity:opacity,weight:weight};
        polyline=L.polyline(polyline_positions,polyline_options);
        polyline.addTo(map);
      }
      if(polyline!==null){
        var map_polyline=new mapPolyline();
        map_polyline.map=map;
        map_polyline.polyline=polyline;
        map_polyline.list=list;
        if(id)map_polyline.id=id;
        if(order)map_polyline.order=order;
        this.polyline_list.push(map_polyline);
      }
      return polyline;
    };
    this.fitLocation=function(map,lat1,lng1,lat2,lng2){
      var bounds=null,position1,position2;
      if(this.map_provider===null||this.map_provider===provider_list[0]){/*google*/
        if(lat2&&lng2){
          position1=new google.maps.LatLng(lat1,lng1);
          position2=new google.maps.LatLng(lat2,lng2);
          bounds=new google.maps.LatLngBounds();
          bounds.extend(position1);
          bounds.extend(position2);
          map.fitBounds(bounds);
        }
        else{
          position1=new google.maps.LatLng(lat1,lng1);
          bounds=new google.maps.LatLngBounds();
          bounds.extend(position1);
          map.fitBounds(bounds);
        }
      }
      else if(this.map_provider===provider_list[1]){/*yandex*/
        if(lat2&&lng2){
          var lng;
          lat1=lat1.toFixed(DEFAULT_YANDEX_FIXED);lng1=lng1.toFixed(DEFAULT_YANDEX_FIXED);
          lat2=lat2.toFixed(DEFAULT_YANDEX_FIXED);lng2=lng2.toFixed(DEFAULT_YANDEX_FIXED);
          if(lng1<0&&lng2<0&&lng1<lng2){lng=lng1;lng1=lng2;lng2=lng;}
          else if(lng1>0&&lng2>0&&lng1>lng2){lng=lng1;lng1=lng2;lng2=lng;}
          position1=[lat1,lng1];
          position2=[lat2,lng2];
          bounds=[];
          bounds.push(position1);
          bounds.push(position2);
          map.setBounds(bounds/*[[lat1,lng1],[lat2,lng2]]*/,{checkZoomRange:true});
        }
        else{
          position1=[lat1,lng1];
          bounds=$.extend(bounds,position1);
          map.setCenter(bounds/*[lat1,lng1]*/,DEFAULT_YANDEX_ZOOM,{checkZoomRange:true});
        }
      }
      else if(this.map_provider===provider_list[2]){/*osm*/
        if(lat2&&lng2){
          position1=L.latLng(lat1,lng1);
          position2=L.latLng(lat2,lng2);
          bounds=L.latLngBounds(position1,position2);
          bounds.extend(position1,position2);
          map.fitBounds(bounds);
        }
        else{
          position1=L.latLng(lat1,lng1);
          bounds=L.latLngBounds(position1);
          bounds.extend(position1);
          map.fitBounds(bounds);
        }
      }
      return bounds;
    };
    this.addMarkerWindow=function(marker,window_info,max_window_width){
      if(this.map_provider===null||this.map_provider===provider_list[0]){//google
        var window=new google.maps.InfoWindow({content:window_info,maxWidth:max_window_width});
        marker.window=window;/*addon param*/
        google.maps.event.addListener(marker,'click',function(){
          window.open(marker.getMap(),marker);/*marker.get('map')*/
          current_marker=marker;
        });
      }
      else if(this.map_provider===provider_list[1]){//yandex
        marker.properties.set('balloonContentBody',window_info);
        marker.options.set('preset',{balloonMaxWidth:max_window_width});
        marker.events.add('balloonopen',function(){current_marker=marker;});
      }
      else if(this.map_provider===provider_list[2]){//osm
        var map_marker=this.getMapMarker(marker);
        var popup_offset_height=(map_marker!==null&&map_marker.image_height!==null?(map_marker.image_height*0.75).toFixed()*(-1):(DEFAULT_OSM_MARKER_OFFSET[1]*0.75).toFixed()*(-1));
        var popup_offset_width=0;
        var window=L.popup({maxWidth:max_window_width,offset:[popup_offset_width,popup_offset_height]});
        window.setContent(window_info);marker.bindPopup(window);
        marker.addEventListener('click',function(){current_marker=marker;});
      }
    };
    this.removeMarkerWindow=function(marker){
      if(this.map_provider===null||this.map_provider===provider_list[0]){//google
        marker.window.close();/*addon param*/
        google.maps.event.clearListeners(marker,'click');
      }
      else if(this.map_provider===provider_list[1]){//yandex
        marker.properties.set('balloonContentBody',null);
        marker.events.remove('balloonopen');
      }
      else if(this.map_provider===provider_list[2]){//osm
        marker.closePopup();marker.unbindPopup();
        marker.removeEventListener('click');
      }
    };
    this.setMarkerLocation=function(map_marker,lat,lng,save_location){
      var marker_position;
      if(this.map_provider===null||this.map_provider===provider_list[0]){//google
        marker_position=new google.maps.LatLng(lat,lng);
        map_marker.marker.setPosition(marker_position);
      }
      else if(this.map_provider===provider_list[1]){//yandex
        marker_position=new ymaps.geometry.Point([lat,lng]);
        map_marker.marker.geometry.setCoordinates(marker_position.getCoordinates());
      }
      else if(this.map_provider===provider_list[2]){//osm
        marker_position=L.latLng(lat,lng);
        map_marker.marker.setLatLng(marker_position);
      }
      if(save_location){map_marker.latitude=lat;map_marker.longitude=lng;}
    };
    this.setMarkerImage=function(map_marker,image,image_width,image_height){
      if(this.map_provider===null||this.map_provider===provider_list[0]){//google
        var icon={url:image,scaledSize:new google.maps.Size(32,32)};
        map_marker.marker.setIcon(icon);
      }
      else if(this.map_provider===provider_list[1]){//yandex
        map_marker.marker.options.set('preset',{iconImageHref:image,iconImageSize:[32,32]/*64,64*/,iconImageOffset:[-16,-30]/*-32,-60*/,balloonOffset:[0,-32]/*0,-40*/});
      }
      else if(this.map_provider===provider_list[2]){//osm
        var icon=L.icon({iconUrl:image,iconSize:[32,32],iconAnchor:[16,30]});
        map_marker.marker.setIcon(icon);
      }
      map_marker.image_width=image_width;
      map_marker.image_height=image_height;
    };
    this.removeMarker=function(map,map_marker){
      var marker=map_marker.marker;
      if(this.map_provider===null||this.map_provider===provider_list[0]){//google
        marker.setMap(null);
      }
      else if(this.map_provider===provider_list[1]){//yandex
        map.geoObjects.remove(marker);
      }
      else if(this.map_provider===provider_list[2]){//osm
        marker.removeFrom(map);
      }
      this.removeFromMarkerList(map_marker);
    };
    this.removePolyline=function(map,map_polyline){
      var polyline=map_polyline.polyline;
      if(this.map_provider===null||this.map_provider===provider_list[0]){//google
        polyline.setMap(null);
      }
      else if(this.map_provider===provider_list[1]){//yandex
        map.geoObjects.remove(polyline);
      }
      else if(this.map_provider===provider_list[2]){//osm
        polyline.removeFrom(map);
      }
      this.removeFromPolylineList(map_polyline);
    };
  }

  /*Admin panel*/
  /*force_power-iDEA
  ptr: pointer to list or object
  pattern:
    replace all %%<field_name>%%
    <select> tag attr: options="<option_list>"
                       options_selected="%%<field_name>%%"
                       option_name="<name in option_list>"
                       option_value="<value in option_list>"
  */
  function faParsePattern(ptr,pattern){
    var ret_val="";
    var part_list=pattern.split("%%");/*0-subpart1 1-value 2-subpart2 3-value...*/
    if(ptr instanceof Array){/*table_type*/
      for(var j=0;j<ptr.length;j++)
        ret_val+=faParsePartList(ptr[j],part_list);
    }
    else{/*card_type*/
      ret_val+=faParsePartList(ptr,part_list);
    }
    if(ret_val==="")ret_val=pattern;
    return faParsePatternSelect(ret_val);
  }
  function faParsePartList(obj,part_list){
    var value,ret_val="";
    if(obj&&part_list.length>0){
      for(var j=0;j<part_list.length;j++){
        if(j%2===0)ret_val+=part_list[j];
        else{
          var name=part_list[j];
          value=(name.indexOf("obj.")>=0?eval(name):eval("obj."+name));
          if(value===null)value="";else if(typeof value=="string")value=value.replace(/"/g,"&quot;").replace(/'/g,"&#39;");
          ret_val+=value;
        }
      }
    }
    return ret_val;
  }
  /*parse of select pattern: <select options="select_list" selected="id">*/
  function faParsePatternSelect(pattern){
    var options,option_selected,option_value,option_name,value,name;
    var html=$.parseHTML(pattern),obj_list,obj;
    $(html).find("select").each(function(){
      options=$(this).attr("options");
      option_selected=$(this).attr("option_selected");
      option_value=$(this).attr("option_value");
      option_name=$(this).attr("option_name");
      obj_list=eval(options);
      if(obj_list instanceof Array){
        var s="";
        for(var j=0;j<obj_list.length;j++){
          obj=obj_list[j];
          if(obj){
            value=eval("obj."+option_value);
            name=(option_name.indexOf("obj.")>=0?eval(option_name):eval("obj."+option_name));
            s+="<option value='"+value+"'"+(option_selected==value?" selected":"")+">"+name+"</option>";
          }
        }
        $(this).append(s);
      }
    });
    return html;
  }

  /*END_OF_INNOCENCE*/