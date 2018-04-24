package autozvit.com.fireart.forms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import autozvit.com.fireart.R;
import autozvit.com.fireart.objects.Direction;
import autozvit.com.fireart.objects.Geocode;
import autozvit.com.fireart.objects.OrderAB;
import autozvit.com.fireart.objects.OrderABStatus;
import autozvit.com.fireart.objects.RetVal;
import autozvit.com.fireart.objects.Track;
import autozvit.com.fireart.service.DispatcherService;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.tools.Map;
import autozvit.com.fireart.tools.T;
import autozvit.com.fireart.tools.TypedCallback;

public class Map2Fragment extends Fragment{
  private static final boolean PREVIEW_MY_LOCATION=false;

  private static final String ARG_CALLBACK_COMMAND="callback_command";
  private static final String ARG_MAP_PROVIDER="map_provider";
  private int callbackCommand,mapProvider;
  public int getCallbackCommand(){return callbackCommand;}
  public void setCallbackCommand(int callback_command){callbackCommand=callback_command;}
  public static final int CALLBACK_COMMAND_CLIENT=1,CALLBACK_COMMAND_DRIVER=2;

  private Context context;
  private Manager manager;
  private String currency;
  private BroadcastReceiver broadcastReceiver=null;
  private LocationManager locationManager=null;

  private Map.Location lastLocation=null;
  private Map map;
  private Object localMarker=null,remoteMarker=null;
  private ArrayList<Object> markerList=null;

  private int BOOK_TRIP_NO_PICKUP=0,BOOK_TRIP_PICKUP_A=1,BOOK_TRIP_PICKUP_AB=2;
  private Object tripMarkerA=null,tripMarkerB=null;
  private Object deliveryMarker=null;
  private int bookTrip=BOOK_TRIP_NO_PICKUP;//0-no book trip 1-pickup maker A 2-pickup markerA and markerB
  private boolean needShowLocation=true;//need to show (until start booking in easy)
  public boolean isNeedShowLocation(){return needShowLocation;}
  public void setNeedShowLocation(boolean need_show_location){needShowLocation=need_show_location;}

  private Object circle=null;
  private Object tripRoute=null;
  //////////////////////////////////////////////////////////////////////////////////////////////////
  private LocationListener locationListener=new LocationListener(){
    @Override
    public void onLocationChanged(Location location){if(manager.isCalcDistance())saveDistance(location);saveLocation(location);showLocation(location);}
    @Override
    public void onProviderDisabled(String provider){}
    @Override
    public void onProviderEnabled(String provider){/*showLocation(locationManager.getLastKnownLocation(provider),true);*/}
    @Override
    public void onStatusChanged(String provider,int status,Bundle extras){}
  };
  //////////////////////////////////////////////////////////////////////////////////////////////////
  private void saveLocation(Location location){
    lastLocation=map.newLocationInstance(location.getLatitude(),location.getLongitude());
    String lat=Double.toString(location.getLatitude()),lng=Double.toString(location.getLongitude());
    manager.putLatitude(lat);manager.putLongitude(lng);manager.getEditor().commit();
    if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY)manager.googleGeocode(lat,lng,manager.showPickupAddressCallbackHandler);
  }
  private void saveDistance(Location location){
    if(lastLocation!=null){//saving in saveLocation
      long trip_distance=manager.getTripDistance();
      trip_distance+=T.getDistanceBetweenTwoMarkers(lastLocation.latitude,lastLocation.longitude,location.getLatitude(),location.getLongitude());
      manager.putTripDistance(trip_distance);
      manager.getEditor().commit();
      //preview the distance
      if(manager.getCurrentOrderAB()!=null)manager.showTaximeterFragmentCallbackHandler.execute(manager.getCurrentOrderAB());
    }
  }
  //show my or remote location
  private void showLocation(Map.Location location,boolean for_me,float bearing){
    //animate moving to (client & driver)
    map.toLocation(location);

    if(needShowLocation||!for_me){
      int icon_res;
      Object marker=for_me?localMarker:remoteMarker;
      boolean is_car=(for_me&&callbackCommand==CALLBACK_COMMAND_DRIVER||!for_me&&callbackCommand==CALLBACK_COMMAND_CLIENT)?true:false;

      //icon
      if(!is_car){
        icon_res=(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY&&
                  manager.getOrderId()==-1)?R.drawable.pickup:R.drawable.people;
      }
      else icon_res=getCar(bearing);

      //marker
      if(marker!=null){
        map.setMarkerPosition(marker,location);
        if(is_car){
          map.setMarkerIcon(marker,getCar(bearing));
          //marker.setRotation(bearing);
        }
      }
      else{
        try{marker=map.addMarker(location,icon_res);}catch(Exception e){}
        if(for_me)localMarker=marker;else remoteMarker=marker;
      }

      //circle
      if(callbackCommand==CALLBACK_COMMAND_CLIENT&&for_me||callbackCommand==CALLBACK_COMMAND_DRIVER&&manager.isTripMode()){
        if(circle!=null){
          map.setCirclePosition(circle,T.MAP_DEFAULT_CIRCLE_RADIUS,location);
        }
        else{
          try{
            circle=map.addCircle(location,T.MAP_DEFAULT_CIRCLE_COLOR,T.MAP_DEFAULT_CIRCLE_STROKE_COLOR,T.MAP_DEFAULT_CIRCLE_STROKE,T.MAP_DEFAULT_CIRCLE_RADIUS);
          }catch(Exception e){}
        }
      }

      //need to calculate time from localMarker to remoteMarker
      if(/*!is_car&&*/icon_res==R.drawable.pickup&&localMarker!=null&&remoteMarker!=null){
        Map.Location local=map.getMarkerLocation(localMarker);
        Map.Location remote=map.getMarkerLocation(remoteMarker);
        String time_duration=manager.getEstimateTimeDuration(local.latitude,local.longitude,remote.latitude,remote.longitude);
        if(time_duration!=null){
          map.setMarkerIcon(localMarker,icon_res,time_duration);
        }
      }
    }
  }
  //show my location with vibration
  private void showLocation(Location location){
    Map.Location show_location=map.newLocationInstance(location.getLatitude(),location.getLongitude());
    showLocation(show_location,true,location.getBearing());
    //vibrate
    if(manager.isVibrate()){
      Vibrator vibr=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
      vibr.vibrate(T.TASK_VIBRATE_SHORT_TIMEOUT);
    }
  }
  //show client location
  private Object showLocation(Map.Location location,Object marker){
    if(marker!=null){
      map.setMarkerPosition(marker,location);
    }
    else{
      try{marker=map.addMarker(location,R.drawable.people);}catch(Exception e){}
    }
    return marker;
  }
  //callback
  public TypedCallback showChangingColorCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      final Object show_circle=circle;
      final Integer color=(Integer)obj;
      getActivity().runOnUiThread(new Runnable(){
        @Override
        public void run(){
          if(show_circle!=null){
            if(map.getCircleColor(show_circle)!=color.intValue())map.setCircleColor(show_circle,color.intValue());
            else map.setCircleColor(show_circle,T.MAP_DEFAULT_CIRCLE_COLOR);
          }
        }
      });
    }
  };
  public TypedCallback showStatusColorCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      final String status=(String)obj;
      getActivity().runOnUiThread(new Runnable(){
        @Override
        public void run(){
          if(status!=null&&circle!=null){
            int val=Integer.parseInt(status);
            if(val<0)map.setCircleColor(circle,T.MAP_CANCEL_CIRCLE_COLOR);
            else if(val==OrderABStatus.ORDER_STATUS_ACCEPTED)map.setCircleColor(circle,T.MAP_ACCEPTED_CIRCLE_COLOR);
            else if(val==OrderABStatus.ORDER_STATUS_PROCESSED)map.setCircleColor(circle,T.MAP_PROCESSING_CIRCLE_COLOR);
            else if(val==OrderABStatus.ORDER_STATUS_LANDING)map.setCircleColor(circle,T.MAP_LANDING_CIRCLE_COLOR);
            else if(val>OrderABStatus.ORDER_STATUS_LANDING)map.setCircleColor(circle,T.MAP_TRIP_CIRCLE_COLOR);
            else map.setCircleColor(circle,T.MAP_DEFAULT_CIRCLE_COLOR);
          }
        }
      });
    }
  };

  Manager.TimeStarter colorTimeStarter=null,vibrTimeStarter=null;
  public TypedCallback showOrderABStatusCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      String status=(String)obj;
      int status_id=Integer.parseInt(status);
      if(status_id==OrderABStatus.ORDER_STATUS_LANDING){
        //vibrate
        if(vibrTimeStarter==null&&manager.isVibrate())
          vibrTimeStarter=manager.doVibrate(T.TASK_VIBRATE_15_COUNT);
        //color
        if(colorTimeStarter==null)
          colorTimeStarter=manager.timeStarter(showChangingColorCallbackHandler,new Integer(T.MAP_LANDING_CIRCLE_COLOR),T.TASK_SHOW_15_COUNT);
      }
      else{
        if(vibrTimeStarter!=null){vibrTimeStarter.setNeedClose(true);vibrTimeStarter=null;}
        if(colorTimeStarter!=null){colorTimeStarter.setNeedClose(true);colorTimeStarter=null;}
        //statusColor
        manager.timeStarter(showChangingColorCallbackHandler,new Integer(manager.getColorByOrderStatus(status_id)),T.TASK_SHOW_3_COUNT);
        //manager.starter(showStatusColorCallbackHandler,status);
      }

      if(callbackCommand==CALLBACK_COMMAND_CLIENT){
        //update tip message
        if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY||manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB){
          if(status_id>OrderABStatus.ORDER_STATUS_PROCESSED)map.setMarkerIcon(localMarker,R.drawable.pickup);//remove time_duration from icon
          else stopCarsNear();//stop cars near simulator
        }
        else manager.updateTipMessage(status_id);//change after get order too.
        //check order for driver transport (and start tracker)
        manager.getOrderABRequest(manager.showOrderABCallbackHandler,manager.getOrderId(),-1,-1);
        //estimate trip
        if(status_id==OrderABStatus.ORDER_STATUS_COMPLETED&&manager.getCurrentOrderAB()!=null){
          OrderAB order=manager.getCurrentOrderAB();
          String transport=(order.transport!=null?order.transport.name+T.SPACE+order.transport.license_plate:T.EMPTY);
          manager.removeAllWithoutMapFragment();
          //manager.removeFragment(T.FRAGMENT_NAME_ORDER_STATUS);//may be a leave?
          //manager.removeFragment(T.FRAGMENT_NAME_RIDE);//easyStyle

          //invoice fragment here
          if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY||manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB)
            manager.showInvoiceFragmentCallbackHandler.execute(new Boolean(true));//client review driver
          else{
            manager.makeTripRatingDialog(order.id,order.user_id,order.transport_id,(byte)-1,null,null,transport);
          }

          closeStarters();
          clearTripMarkers();
          map.clear();
        }
      }
      else if(callbackCommand==CALLBACK_COMMAND_DRIVER&&manager.isTripMode()){
        if(status_id<0||status_id==OrderABStatus.ORDER_STATUS_COMPLETED){
          T.messageBox(context,getString(R.string.message_warning_title),
                       getString(R.string.order_status_changed)+T.NEXT_LINE+
                       manager.getTipMessageByOrderStatus(status_id),getString(R.string.button_agree));
          //cancel trip order
          RetVal ret_val=new RetVal();
          ret_val.value=0;
          ret_val.temp_id=-1;//trip order_id
          ret_val.temp_long_value=-1;//trip transport_id
          ret_val.temp_long_value_2=-1;//trip order_user_id
          manager.afterUpdateOrderABTransportCallbackHandler.execute(ret_val);
        }
        else if(status_id==OrderABStatus.ORDER_STATUS_ACCEPTED){
          T.messageBox(context,getString(R.string.message_warning_title),
                       getString(R.string.order_status_changed),getString(R.string.button_agree));
          //check the tripOrder (may be driver's transport failed?)
          manager.getOrderABRequest(manager.showOrderABCallbackHandler,manager.getTripOrderId(),-1,-1);
        }
      }
    }
  };

  Manager.WaitStarter trackerTimeStarter=null,orderNearTimeStarter=null;
  public TypedCallback showRemoteLocationCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      Track track=(Track)obj;
      if(track.id>0){//track got
        Map.Location location=map.newLocationInstance(track.latitude,track.longitude);
        showLocation(location,false,track.bearing);
      }
    }
  };
  Manager.WaitStarter addressStarter=null;
  private Map.Location centerLocation=null;
  public Map.Location getCenterLocation(){return centerLocation;}
  public void setCenterLocation(Map.Location location){centerLocation=location;}
  public TypedCallback doAddressCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      getActivity().runOnUiThread(new Runnable(){
        @Override
        public void run(){
          if(map!=null){
            Map.Location location=map.getMapCenter();
            if(/*(centerLocation==null&&location!=null)||*/(centerLocation!=null&&location!=null&&
            centerLocation.latitude!=location.latitude&&centerLocation.longitude!=location.longitude)){
              manager.putOrderLatitude(String.valueOf(location.latitude));
              manager.putOrderLongitude(String.valueOf(location.longitude));
              manager.getEditor().commit();
              manager.googleGeocode(Double.toString(location.latitude),Double.toString(location.longitude),manager.showPickupAddressCallbackHandler);
            }
            centerLocation=location;
          }
        }
      });
    }
  };

  //show clients by client's orders (client's orders list)
  public TypedCallback showClientLocationCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      ArrayList<OrderAB> order_list=(ArrayList)obj;
      OrderAB order,marker_order,last_order=null;
      long last_order_datetime=0;
      Object marker;
      Map.Location location;
      Map.Location max=null,min=null;
      Iterator i;
      double max_lat=0,max_lon=0,min_lat=0,min_lon=0;

      //remove all markers and return
      if(order_list==null||order_list.size()==0){
        //clear markers
        if(markerList!=null&&markerList.size()>0){
          i=markerList.iterator();
          while(i.hasNext()){
            marker=i.next();
            map.removeMarker(marker);
          }
          markerList.clear();markerList=null;
        }
        return;
      }

      //[max/min latitude/lon]
      //initByOrder
      /*if(order_list.size()>0){
        order=order_list.get(0);
        max_lat=order.order_lat;max_lon=order.order_lon;
        min_lat=order.order_lat;min_lon=order.order_lon;
      }*/
      //initByCar
      String str_lat=manager.getLatitude(),str_lon=manager.getLongitude();
      double lat=str_lat!=null?Double.parseDouble(str_lat):T.MAP_DEFAULT_LAT,
             lon=str_lon!=null?Double.parseDouble(str_lon):T.MAP_DEFAULT_LNG;
      max_lat=lat;max_lon=lon;
      min_lat=lat;min_lon=lon;

      //markerList processing
      if(markerList!=null){
        ArrayList removing=new ArrayList();
        i=markerList.iterator();
        while(i.hasNext()){
          marker=i.next();
          marker_order=(OrderAB)map.getMarkerObject(marker);
          if(marker!=null&&marker_order!=null){
            order=findOrderById(order_list,marker_order.id);
            if(order!=null){
              if(marker_order.order_lat!=order.order_lat||
                 marker_order.order_lon!=order.order_lon){//change location
                location=map.newLocationInstance(order.order_lat,order.order_lon);
                map.setMarkerPosition(marker,location);
              }
              //max/min latitude/lon
              if(order.order_lat>max_lat)max_lat=order.order_lat;
              else if(order.order_lat<min_lat)min_lat=order.order_lat;
              if(order.order_lon>max_lon)max_lon=order.order_lon;
              else if(order.order_lon<min_lon)min_lon=order.order_lon;
              map.setMarkerObject(marker,order);
            }
            else{map.removeMarker(marker);removing.add(marker);}//notIn
          }
        }
        markerList.removeAll(removing);//remove left
      }
      else markerList=new ArrayList();
      //order_list processing
      i=order_list.iterator();
      while(i.hasNext()){
        order=(OrderAB)i.next();
        if(order!=null&&order.order_lat!=0&&order.order_lon!=0){
          if(isNewOrder(order.id)){
            location=map.newLocationInstance(order.order_lat,order.order_lon);
            try{
              marker=map.addMarker(location,R.drawable.marker_a);
              map.setMarkerObject(marker,order);
              markerList.add(marker);
            }catch(Exception e){}
            //max/min latitude/lon
            if(order.order_lat>max_lat)max_lat=order.order_lat;
            else if(order.order_lat<min_lat)min_lat=order.order_lat;
            if(order.order_lon>max_lon)max_lon=order.order_lon;
            else if(order.order_lon<min_lon)min_lon=order.order_lon;
          }
          //order is mine! (seek last driver's order)
          if(order.transport_id==manager.getDriverTransportId()){
            long datetime=manager.getDatetimeMilisec(order.create_date);
            if(last_order!=null){
              if(last_order_datetime<datetime){last_order=order;last_order_datetime=datetime;}
            }
            else{last_order=order;last_order_datetime=datetime;}
          }
        }
      }

      //goto area (by saved max/min latitude/lon values)
      if(max_lat!=0&&max_lon!=0)max=map.newLocationInstance(max_lat,max_lon);
      if(min_lat!=0&&min_lon!=0)min=map.newLocationInstance(min_lat,min_lon);
      if(max!=null&&min!=null&&order_list.size()>0){//markerList.size()>0 ?
        map.toLocation(max,min);
      }

      //vibrate
      if(manager.isVibrate()){
        Vibrator vibr=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibr.vibrate(T.TASK_VIBRATE_SHORT_TIMEOUT);
      }

      //driver has an order
      if(!manager.isTripMode()&&last_order!=null){/*no trip mode but driver has an order*/
        manager.makeOrderDialog(last_order);
      }

      //select nearest order (if moving and get lastLocation)
      if(!manager.isTripMode()&&lastLocation!=null&&(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY||
                                                     manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB)&&
                                                    !manager.isOpenFragment(T.FRAGMENT_NAME_INQUIRY)){
        //need to select nearest and pick a point
        OrderAB nearest_order=manager.selectNearestOrder(order_list,lastLocation.latitude,lastLocation.latitude);
        manager.showInquiryFragmentCallbackHandler.execute(nearest_order);
      }
    }
  };

  //marker_tools
  private OrderAB findOrderById(ArrayList<OrderAB> order_list,long id){
    OrderAB order=null;
    Iterator i;
    if(order_list!=null){
      OrderAB o;
      i=order_list.iterator();
      while(i.hasNext()){
        o=(OrderAB)i.next();
        if(o!=null&&o.id==id){order=o;break;}
      }
    }
    return order;
  }
  private boolean isNewOrder(long id){
    boolean ret_val=true;
    if(markerList!=null){
      Object marker;
      Iterator i=markerList.iterator();
      while(i.hasNext()){
        marker=i.next();
        if(marker!=null&&map.getMarkerObject(marker)!=null&&
          ((OrderAB)map.getMarkerObject(marker)).id==id){ret_val=false;break;}
      }
    }
    return ret_val;
  }

////////////////////////////////////////////////////////////////////////////////////////////////////
  //for easy style booking
  public TypedCallback dropoffAndRouteCalcCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      Map.Location location=null;
      Geocode geocode=null;
      if(obj instanceof Map.Location)location=(Map.Location)obj;
      else if(obj instanceof Geocode){
        geocode=(Geocode)obj;
        location=map.newLocationInstance(geocode.locationLat,geocode.locationLon);
      }
      if(location!=null){
        //if(tripMarkerA==null)tripMarkerA=localMarker;
        addDropoffMarker(location);
        //map.toLocation(location,lastLocation);
        //locations for order
        manager.putOrderLatitude(Double.toString(lastLocation.latitude));
        manager.putOrderLongitude(Double.toString(lastLocation.longitude));
        manager.putDeliveryLatitude(Double.toString(location.latitude));
        manager.putDeliveryLongitude(Double.toString(location.longitude));
        manager.getEditor().commit();
        //default product
        if(manager.getSelectedProduct()==null&&manager.getDefaultProduct()==null){
          manager.findDefaultProductCallbackHandler.execute(null);
        }
        //dropoff address
        if(geocode!=null)manager.showDropoffAddressCallbackHandler.execute(geocode);
        else manager.googleGeocode(Double.toString(location.latitude),Double.toString(location.longitude),manager.showDropoffAddressCallbackHandler);
        //calc route
        if(!Manager.CAR_RENTAL)manager.googleDirections(Manager.DIRECTIONS_ROUTE_LOCATION,manager.updateDropoffFragmentCallbackHandler);
        //calc rental
        else manager.updateDropoffFragmentForRentalCallbackHandler.execute(null);
      }
    }
  };

  public Map.LocationCallback onMapClickedCallbackHandler=new Map.LocationCallback(){
    @Override
    public void execute(Object obj){
      Map.Location location=(Map.Location)obj;
      if(callbackCommand==CALLBACK_COMMAND_CLIENT){//in client mode
        if(manager.getOrderId()==-1||manager.getOrderStatusId()==OrderABStatus.ORDER_STATUS_COMPLETED){//no order now
          if(manager.getBookingStyle()==Manager.BOOKING_STYLE_DELIVERY){//DELIVERY
            manager.putFindAddress(T.EMPTY);
            manager.putDeliveryAddress(T.EMPTY);
            manager.putDeliveryLatitude(String.valueOf(location.latitude));
            manager.putDeliveryLongitude(String.valueOf(location.longitude));
            manager.getEditor().commit();
            addDeliveryMarker(location);
          }
          else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_TRIP){//TRIP
            addTripMarker(location);
          }
          else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY){//EASY
            //pickup frag or dropoff location
            if(manager.getEasyBooking()==Manager.EASY_BOOKING_PICKUP||manager.getEasyBooking()==0/*no easy step*/){
              if(!manager.isOpenFragment(T.FRAGMENT_NAME_PICKUP)){
                //find box
                if(!manager.isOpenFragment(T.FRAGMENT_NAME_FIND))manager.showFindFragment(R.layout.fragment_dest,null,-1,manager.getPickupAddress(),manager.getDropoffAddress(),currency);
                //start pickup fragment
                manager.showPickupFragmentCallbackHandler.execute(null);
              }
              else{//move pickup location
                //clear dropoff location
                if(tripMarkerB!=null){
                  manager.clearOrderCalcValues();
                  manager.putDropoffName(null);
                  manager.putDropoffAddress(null);
                  clearTripMarkerB();
                }
                //last location simulation(with localMarker)
                moveLocalMarker(location);
                lastLocation=map.newLocationInstance(location.latitude,location.longitude);
                String lat=Double.toString(location.latitude),lng=Double.toString(location.longitude);
                manager.putLatitude(lat);manager.putLongitude(lng);manager.getEditor().commit();
                manager.googleGeocode(lat,lng,manager.showPickupAddressCallbackHandler);
              }
            }
            else if(manager.getEasyBooking()==Manager.EASY_BOOKING_DROPOFF&&manager.isOpenFragment(T.FRAGMENT_NAME_DROPOFF)){
              dropoffAndRouteCalcCallbackHandler.execute(location);
            }
          }
          else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB){//EASY_AB
            if(!manager.isOpenFragment(T.FRAGMENT_NAME_FIND))manager.showFindFragment(R.layout.fragment_places,null,-1,manager.getPickupAddress(),manager.getDropoffAddress(),currency);
          }
        }
      }
      else if(callbackCommand==CALLBACK_COMMAND_DRIVER){//in driver mode
        if(manager.isTripMode()&&!manager.isOpenFragment(T.FRAGMENT_NAME_TAXIMETER))
          manager.showTaximeterFragmentCallbackHandler.execute(manager.getCurrentOrderAB());
      }
    }
  };
  public Map.BooleanCallback onMarkerClickedCallbackHandler=new Map.BooleanCallback(){
    @Override
    public boolean execute(Object obj){
      Object marker=obj;
      if(localMarker!=null&&marker.equals(localMarker)){//click on my marker
        if(callbackCommand==CALLBACK_COMMAND_CLIENT){//figure (client)
          if(manager.getOrderId()!=-1)showOrderStatus();
          else{
            if(manager.getBookingStyle()==Manager.BOOKING_STYLE_DELIVERY){
              manager.putFindAddress(T.EMPTY);
              manager.putDeliveryAddress(T.EMPTY);
              Map.Location location=map.getMarkerLocation(marker);
              if(location!=null){
                manager.putDeliveryLatitude(String.valueOf(location.latitude));
                manager.putDeliveryLongitude(String.valueOf(location.longitude));
              }
              manager.getEditor().commit();
              try{addDeliveryMarker(map.getMarkerPosition(localMarker));}catch(Exception e){}
            }
            else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_TRIP){
              addTripMarker(map.getMarkerPosition(localMarker));
            }
            else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY||
                    manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB){
              //nothing
            }
          }
        }
        else if(callbackCommand==CALLBACK_COMMAND_DRIVER){//car (driver)
          //nothing
        }
      }
      else{//figure in driver mode
        if(marker!=null&&map.getMarkerObject(marker)!=null){
          OrderAB order=(OrderAB)map.getMarkerObject(marker);
          if(manager.DEBUG)Toast.makeText(context.getApplicationContext(),"order="+order.id,Toast.LENGTH_SHORT).show();
          ArrayList<OrderAB> order_list=new ArrayList();
          order_list.add(order);
          manager.showOrderABListCallbackHandler.execute(manager.newInstanceListWithOptions(order_list,true));
        }
      }
      return false;
    }
  };

  public TypedCallback mapStartCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      //map style
      map.setMapStyle(R.raw.google_map_greyscale_style);
      //start location
      doLocation();
      //message boxes for test(if map provider impl changes)
      if(map==null){
        //T.messageBox(context,"map critical error","null map","cancel");
        return;
      }
      int icon_res=0;
      if(callbackCommand==CALLBACK_COMMAND_CLIENT)//start in easy booking style with pickup marker, after if moving pickup -> people
        icon_res=(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY&&
                  manager.getOrderId()==-1)?R.drawable.pickup:R.drawable.people;
      else if(callbackCommand==CALLBACK_COMMAND_DRIVER)icon_res=getCar(0);

      //marker
      try{localMarker=map.addMarker(lastLocation,icon_res);}catch(Exception e){}

      //circle only for started order (for coloring order status)
      if(callbackCommand==CALLBACK_COMMAND_CLIENT&&manager.getCurrentOrderAB()!=null&&manager.getCurrentOrderAB().status_id>OrderABStatus.ORDER_STATUS_ACCEPTED){
        try{circle=map.addCircle(lastLocation,T.MAP_DEFAULT_CIRCLE_COLOR,T.MAP_DEFAULT_CIRCLE_STROKE_COLOR,T.MAP_DEFAULT_CIRCLE_STROKE,T.MAP_DEFAULT_CIRCLE_RADIUS);}catch(Exception e){}
      }

      try{map.toLocation(lastLocation);}catch(Exception e){
        //T.messageBox(context,"map exception","toLocation="+e.getMessage(),"cancel");
      }
      try{map.setOnMapClickListener(onMapClickedCallbackHandler);}catch(Exception e){
        //T.messageBox(context,"map exception","setOnMapClickListener="+e.getMessage(),"cancel");
      }
      try{map.setOnMarkerClickListener(onMarkerClickedCallbackHandler);}catch(Exception e){
        //T.messageBox(context,"map exception","setOnMarkerClickListener="+e.getMessage(),"cancel");
      }

      doAction();
    }
  };

  //////////////////////////////////////////////////////////////////////////////////////////////////

  public static Map2Fragment newInstance(int callback_command,int map_provider){
    Map2Fragment fragment=new Map2Fragment();
    Bundle args=new Bundle();
    args.putInt(ARG_CALLBACK_COMMAND,callback_command);
    args.putInt(ARG_MAP_PROVIDER,map_provider);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      callbackCommand=getArguments().getInt(ARG_CALLBACK_COMMAND);
      mapProvider=getArguments().getInt(ARG_MAP_PROVIDER);
    }
    //language=T.getLanguage(context);
    //T.updateLanguage(language,context);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    //Inflate the layout for this fragment
    int layout=R.layout.fragment_google_map;
    if(mapProvider==Manager.MAP_PROVIDER_GOOGLE)layout=R.layout.fragment_google_map;
    else if(mapProvider==Manager.MAP_PROVIDER_OSM)layout=R.layout.fragment_osm_map;
    return inflater.inflate(layout,container,false);
  }

  @Override
  public void onViewCreated(View view,Bundle savedInstanceState){
    super.onViewCreated(view,savedInstanceState);
    context=getActivity();
    if(context!=null){
      manager=((StartActivity)context).getManager();
      if(manager.getActiveCurrency()!=null){
        currency=manager.getActiveCurrencyTitle()!=null?manager.getActiveCurrencyTitle():manager.getActiveCurrency().name;
      }
      locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
      //if(manager.DEBUG)Toast.makeText(context.getApplicationContext(),"context",Toast.LENGTH_SHORT).show();
    }

    View map_view=view.findViewById(R.id.map_view);
    int zoom=(mapProvider==Manager.MAP_PROVIDER_OSM?T.MAP_DEFAULT_ZOOM_FOR_OSM:T.MAP_DEFAULT_ZOOM);

    //Context context,View map_view,int map_provider,boolean my_location,int zoom,int duration,final TypedCallback callback,Object callback_object
    map=new Map(context,map_view,mapProvider,PREVIEW_MY_LOCATION,zoom,T.MAP_DEFAULT_ANIMATE_CAMERA_DURATION,T.MAP_DEFAULT_PADDING);
    try{
      map.init(mapStartCallbackHandler);
    }catch(Exception e){manager.showErrorFragment(context.getString(R.string.map_error));}

    if(broadcastReceiver==null&&manager!=null){
      broadcastReceiver=manager.startBroadcastReceiver(
        DispatcherService.BROADCAST_CLASS_MAP,
        DispatcherService.BROADCAST_INTENT_ORDER_STATUS,
        showOrderABStatusCallbackHandler);
    }

    //test for automatic price calculation
    //double price=manager.calcOrder(1000/*this as a distance*/,manager.getCurrentOrderAB());//auto calc price by distance
    //Toast.makeText(context.getApplicationContext(),"order price="+price,Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onResume(){
    super.onResume();
    if(map!=null)map.onResume();
    if(locationManager!=null)
      try{locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000*T.GPS_PROVIDER_TIMEOUT_DEFAULT_VALUE,T.GPS_PROVIDER_METERS_DEFAULT_VALUE,locationListener);}catch(SecurityException se){}
    //if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))WARNING!
  }

  @Override
  public void onPause(){
    super.onPause();
    if(map!=null)map.onPause();
    if(locationManager!=null)
      try{locationManager.removeUpdates(locationListener);}catch(SecurityException se){}
  }

  @Override
  public void onDestroy(){
    super.onDestroy();
    //critical performance
    stopMoving();stopCarsNear();
    //all time starters closing
    closeStarters();
    //map
    if(map!=null)map.onDestroy();
    //receiver
    if(broadcastReceiver!=null&&manager!=null)manager.stopBroadcastReceiver(broadcastReceiver);
  }

  @Override
  public void onLowMemory(){
    super.onLowMemory();
    if(map!=null)map.onLowMemory();
  }

  //map tools
  private void doLocation(){//preset location
    double latitude=T.MAP_DEFAULT_LAT,longitude=T.MAP_DEFAULT_LNG;
    if(locationManager!=null){
      /*if use for knownLocation can be very long*/
      Criteria criteria=new Criteria();
      criteria.setAccuracy(Criteria.ACCURACY_FINE);
      //fast check location
      //may be it can't need? goto to real location got or default
      if(manager.DEBUG)Toast.makeText(context.getApplicationContext(),"locationPrepare",Toast.LENGTH_SHORT).show();
      //user passive provider in real works! (as code: provider=locationManager.PASSIVE_PROVIDER;)
      String provider=Manager.FAST_LOCATION_PROVIDER?locationManager.PASSIVE_PROVIDER:locationManager.getBestProvider(criteria,true);

      Location location=null;
      try{location=locationManager.getLastKnownLocation(provider);}catch(SecurityException se){}
      catch(Exception e){}

      if(manager.DEBUG)Toast.makeText(context.getApplicationContext(),"lastKnowLocation="+(location!=null?location.toString():T.EMPTY),Toast.LENGTH_SHORT).show();
      if(location!=null){latitude=location.getLatitude();longitude=location.getLongitude();}
      else if(manager.getLatitude()!=null&&manager.getLongitude()!=null){
        latitude=Double.parseDouble(manager.getLatitude());
        longitude=Double.parseDouble(manager.getLongitude());
      }
    }
    lastLocation=map.newLocationInstance(latitude,longitude);
  }
  private void doAction(){
    if(callbackCommand==CALLBACK_COMMAND_CLIENT)clientAction();
    else if(callbackCommand==CALLBACK_COMMAND_DRIVER)driverAction();

    //if error fragment found goto top
    if(manager.isServiceFragmentBeforeMapFragment()){manager.hideAllFragment();manager.showServiceFragment();}

    //driver found now!
    if(manager.getCurrentUser()!=null&&manager.getCurrentUser().type==T.USERTYPE_DRIVER&&manager.getDriverTransportId()==-1){
      manager.showInfoFragment(context.getString(R.string.message_driver_account));
    }

    if(callbackCommand==CALLBACK_COMMAND_CLIENT&&manager.getBookingStyle()==Manager.BOOKING_STYLE_DELIVERY){
      //deliveryMarker
      if(manager.getOrderId()==-1&&manager.getDeliveryLatitude()!=null&&manager.getDeliveryLongitude()!=null){
        Map.Location location=map.newLocationInstance(Double.parseDouble(manager.getDeliveryLatitude()),Double.parseDouble(manager.getDeliveryLongitude()));
        addDeliveryMarker(location);
        map.toLocation(lastLocation,location);
      }

      if(manager.getCartList()!=null&&manager.getCartList().size()>0){
        Fragment frag=manager.getFragment(T.FRAGMENT_NAME_CART);
        if(frag!=null)manager.addFragment(frag,T.FRAGMENT_NAME_CART,T.FRAGMENT_CART);
        else manager.showCartFragmentCallbackHandler.execute(manager.getCartList());
      }
    }

    //Toast.makeText(context.getApplicationContext(),"mapStarted",Toast.LENGTH_SHORT).show();
  }

  private void clientAction(){
    long status_id=manager.getOrderStatusId();
    ((StartActivity)context).getDriverFloatingActionButton().setImageResource(R.drawable.ic_local_taxi_white_24dp);
    //((StartActivity)context).getDriverFloatingActionButton().setBackgroundTintMode(PorterDuff.Mode.SRC);

    //TIP show fragment orderStatus or routeMarker
    if(!manager.isDoNotShow()){
      if(manager.getOrderId()!=-1){
        if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY||manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB){
          //no show tip
        }
        else{//other booking style with tips
          String tip_message=manager.getTipMessageByOrderStatus(status_id);
          manager.showTipFragment(tip_message,-1);
        }
      }
      else{
        if(manager.getBookingStyle()==Manager.BOOKING_STYLE_DELIVERY)
          manager.showTipFragment(context.getResources().getString(R.string.delivery_tip),R.drawable.pickup);
        else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_TRIP)
          manager.showTipFragment(context.getResources().getString(R.string.route_tip),R.drawable.pickup);
        else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY||manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB){
          //no show tip
        }
      }
    }

    //MARKER show orderStatus infoWindow with statusColor
    if(manager.getOrderId()!=-1){//order now
      showOrderStatus();
      //statusColor
      manager.starter(showStatusColorCallbackHandler,String.valueOf(status_id));
      OrderAB order=manager.getCurrentOrderAB();
      if(order!=null){
        //order's markers
        if(manager.getBookingStyle()==Manager.BOOKING_STYLE_TRIP)showDirection(order);
        else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_DELIVERY){
          addDeliveryMarker(order.order_lat,order.order_lon);
          toLocation(order.order_lat,order.order_lon);
        }
        if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY||manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB){
          addPickupMarker(order.order_lat,order.order_lon);
          if(order.delivery_lat!=0&&order.delivery_lon!=0){
            addDropoffMarker(order.delivery_lat,order.delivery_lon);
            toLocation(order.order_lat,order.order_lon,order.delivery_lat,order.delivery_lon);
          }
          else toLocation(order.order_lat,order.order_lon);
        }
      }
      //order's fragment
      if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY||manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB){
        //start riding fragment
        manager.showRideFragmentCallbackHandler.execute(null);
      }
    }
    else{
      map.toLocation(lastLocation);//very need when finished driver's mode(osm map)
      if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY){
        //find box
        manager.showFindFragment(R.layout.fragment_dest,null,-1,manager.getPickupAddress(),manager.getDropoffAddress(),currency);
        //start pickup fragment
        manager.showPickupFragmentCallbackHandler.execute(null);
        //cars near simulator
        startCarsNear();
      }
      else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB){
        //request
        manager.clearPickupAndDropoff();
        manager.showFindFragment(R.layout.fragment_places,null,-1,manager.getPickupAddress(),manager.getDropoffAddress(),currency);
        //cars near simulator
        startCarsNear();
        //center marker: finder for pickup address, marker always put on center
        startAddress();
      }
    }

    //tracker
    startTracker();
    //dancing (in trip and delivery mode)
    if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY||manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB);
    else startMoving();
    //fab
    manager.fabAction(callbackCommand);
  }

  private void driverAction(){
    ((StartActivity)context).getDriverFloatingActionButton().setImageResource(R.drawable.people);
    //((StartActivity)context).getDriverFloatingActionButton().setBackgroundTintMode(PorterDuff.Mode.DST);
    //TIP show fragment people
    if(!manager.isDoNotShow()){
      if(manager.getBookingStyle()!=Manager.BOOKING_STYLE_EASY&&manager.getBookingStyle()!=Manager.BOOKING_STYLE_EASY_AB){//no tips on easyStyle
        if(manager.isTripMode()){
          String tip_message=manager.getTipMessageByOrderStatus(manager.getTripOrderStatusId());
          manager.showTipFragment(tip_message,R.drawable.transport_0);
        }else manager.showTipFragment(context.getResources().getString(R.string.driver_tip),R.drawable.dropoff);
      }
    }
    //center marker
    if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB){
      //hide static marker on center
      ImageView marker=(ImageView)getView().findViewById(R.id.marker);
      if(marker!=null)marker.setVisibility(View.GONE);
      //not need stop thread stopAddress() it's closed in closeStarters()
    }
    //trip mpde
    if(manager.isTripMode()){
      //in tripMode orderList has one client's order
      showClientLocationCallbackHandler.execute(manager.getOrderABList());
      //trip route
      showTripRoute(manager.getCurrentOrderAB());
    }
    else{
      //transportOrder (check for a new order? in recycler action)
      //orderTransportTimeStarter=manager.waitStarter();
      manager.getOrderABTransportRequest(manager.showOrderABTransportCallbackHandler,manager.getDriverTransportId(),0,1);

      //one-shot action
      //manager.runnableStarter(manager.doOrderNearCallbackHandler,showClientLocationCallbackHandler);
      //recycler action
      if(orderNearTimeStarter==null)
        orderNearTimeStarter=manager.waitStarter(manager.doOrderNearCallbackHandler,showClientLocationCallbackHandler,T.TASK_SHOW_15_COUNT/*T.TASK_SERVICE_SLEEP_COUNT*/);//last param is delay
    }
    //fab
    manager.fabAction(callbackCommand);
  }

  //driver
  public void startDriver(){//[start driver] is a stop client
    if(map!=null){
      stopMoving();
      stopCarsNear();
      closeStarters();
      clearTripMarkers();
      map.clear();
      try{localMarker=map.addMarker(lastLocation,getCar(0));}catch(Exception e){}
      map.toLocation(lastLocation);
      driverAction();
    }
  }
  public void startClient(){//[stop driver] is a start client
    if(map!=null){
      closeStarters();
      clearTripMarkers();
      map.clear();
      int icon_res=(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY)?R.drawable.pickup:R.drawable.people;
      try{localMarker=map.addMarker(lastLocation,icon_res);}catch(Exception e){}
      map.toLocation(lastLocation);
      clientAction();
    }
  }

  //closers
  private void closeStarters(){
    if(trackerTimeStarter!=null){trackerTimeStarter.setNeedClose(true);trackerTimeStarter=null;}
    if(orderNearTimeStarter!=null){orderNearTimeStarter.setNeedClose(true);orderNearTimeStarter=null;}
    if(vibrTimeStarter!=null){vibrTimeStarter.setNeedClose(true);vibrTimeStarter=null;}
    if(colorTimeStarter!=null){colorTimeStarter.setNeedClose(true);colorTimeStarter=null;}
    if(addressStarter!=null){addressStarter.setNeedClose(true);addressStarter=null;}
  }
  public void closeOrderNearTimeStarter(){
    if(orderNearTimeStarter!=null){orderNearTimeStarter.setNeedClose(true);orderNearTimeStarter=null;}
  }
  public void closeTrackerTimeStarter(){
    if(trackerTimeStarter!=null){trackerTimeStarter.setNeedClose(true);trackerTimeStarter=null;}
  }
  public void closeAddressStarter(){
    if(addressStarter!=null){addressStarter.setNeedClose(true);addressStarter=null;}
  }

  /*direction*/
  public void clearTripMarkerA(){
    if(tripMarkerA!=null){
      map.removeMarker(tripMarkerA);tripMarkerA=null;
    }
  }
  public void clearTripMarkerB(){
    if(tripMarkerB!=null){
      map.removeMarker(tripMarkerB);tripMarkerB=null;
    }
  }
  public void clearTripRoute(){
    if(tripRoute!=null){
      map.removePolyline(tripRoute);tripRoute=null;
    }
  }
  public void clearDeliveryMarker(){
    if(deliveryMarker!=null){
      map.removeMarker(deliveryMarker);deliveryMarker=null;
    }
  }
  public void clearLocalMarker(){
    if(localMarker!=null){
      map.removeMarker(localMarker);localMarker=null;
    }
  }
  public void addTripRoute(Direction direction){
    try{tripRoute=map.addPolyline(direction,T.MAP_DEFAULT_POLYLINE_STROKE_COLOR,T.MAP_DEFAULT_POLYLINE_STROKE);}catch(Exception e){}
  }
  //tracker
  public void startTracker(){
    if(manager.getTransportSensorId()!=-1){
      //one-shot action
      //manager.runnableStarter(manager.doTransportTrackerCallbackHandler,showRemoteLocationCallbackHandler);
      //recycler action
      if(trackerTimeStarter==null)
        trackerTimeStarter=manager.waitStarter(manager.doTransportTrackerCallbackHandler,showRemoteLocationCallbackHandler,T.TASK_SERVICE_SLEEP_COUNT);
    }
  }
  //address
  public void startAddress(){
    //recycler action
    if(addressStarter==null){
      addressStarter=manager.waitStarter(doAddressCallbackHandler,null,T.TASK_SLEEP_COUNT);
      //show static marker on center
      ImageView marker=(ImageView)getView().findViewById(R.id.marker);
      if(marker!=null)marker.setVisibility(View.VISIBLE);
    }
  }
  public void stopAddress(){
    if(addressStarter!=null){
      addressStarter.setNeedClose(true);addressStarter=null;
      //hide static marker on center
      ImageView marker=(ImageView)getView().findViewById(R.id.marker);
      if(marker!=null)marker.setVisibility(View.GONE);
    }
  }

  //dancing
  private boolean needMoving;
  public void startMoving(){
    needMoving=true;
    new Thread(new Runnable(){
      Object marker=localMarker;
      public void run(){
        int times;
        while(needMoving){
          double moving_lat=0.0001, moving_lng=0.0001;
          long mili=System.currentTimeMillis()%2;
          long sec=new Date().getSeconds()%2;
          double ma_lat=moving_lat;
          double ma_lng=moving_lng;
          if(mili==1&&sec==1){
            ma_lat=ma_lat*(-1);ma_lng=ma_lng*(-1);
          }else if(mili==0&&sec==1){
            ma_lng=ma_lng*(-1);
          }else if(mili==1&&sec==0){
            ma_lat=ma_lat*(-1);
          }
          final double lat=ma_lat,lng=ma_lng;
          getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
              Map.Location current_location=lastLocation;//base position
              Map.Location dancing_location=map.newLocationInstance(current_location.latitude+lat,current_location.longitude+lng);
              if(marker!=null)map.setMarkerPosition(marker,dancing_location);
              //if(circle!=null)map.setCirclePosition(circle,dancing_location);
            }
          });
          times=0;
          while(times<T.TASK_DANCING_SLEEP_COUNT-mili-sec){//float count
            try{TimeUnit.MILLISECONDS.sleep(T.TASK_SLEEP_TIMEOUT);}catch(InterruptedException e){}
            if(!needMoving)break;
            times++;
          }
        }
      }
    }).start();
  }
  public void stopMoving(){
    needMoving=false;
  }

  //cars near
  private boolean needCarsNear;
  private ArrayList<Object> carsNearList=null;
  public void startCarsNear(){
    needCarsNear=true;
    new Thread(new Runnable(){
      public void run(){
        int times;

        //near cars init
        final Map.Location current_location=lastLocation;//base position
        final long cars_count=1+new Date().getSeconds()%2+System.currentTimeMillis()%2;//cars near count
        for(int i=0;i<cars_count;i++){
          final int index=i;
          getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
              Object marker;
              double base_lat=current_location.latitude;
              double base_lng=current_location.longitude;
              double checker_lat,checker_lng;
              int bearing,index_checker=index%2;
              long checker;
              bearing=new Date().getMinutes()+new Date().getSeconds();
              checker=System.currentTimeMillis()%2;
              if(checker==1)bearing=bearing*2;
              else bearing=bearing*3;
              checker=System.currentTimeMillis()%3;

              checker_lat=base_lat-(index_checker==1?((double)bearing/20000):-0.0001)+((double)checker)/1000;
              checker_lng=base_lng+(index_checker==0?((double)bearing/20000):-0.0001)-((double)checker)/1000;

              marker=null;
              try{marker=map.addMarker(checker_lat,checker_lng,getCar(bearing));}catch(Exception e){}
              if(marker!=null){
                if(carsNearList==null)carsNearList=new ArrayList();
                carsNearList.add(marker);
              }
            }
          });
        }

        while(needCarsNear){
          long mili=System.currentTimeMillis()%2;
          long sec=new Date().getSeconds()%2;
          getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
              int bearing;
              long index=0,move_index=cars_count-new Date().getSeconds()%2-System.currentTimeMillis()%2,
                           rotate_index=new Date().getSeconds()%2+System.currentTimeMillis()%2;
              long checker;
              for(Object item:carsNearList){
                if(item!=null){
                  double moving_lat=0.0001,moving_lng=0.0001;
                  long mili=System.currentTimeMillis()%2;
                  long sec=new Date().getSeconds()%2;

                  if(/*mili==0&&sec==0&&*/index==move_index||(sec==1&&index==rotate_index)){//move if exclusive
                    double ma_lat=moving_lat;
                    double ma_lng=moving_lng;
                    if(mili==1&&sec==1){
                      ma_lat=ma_lat*(-1);
                      ma_lng=ma_lng*(-1);
                    }else if(mili==0&&sec==1){
                      ma_lng=ma_lng*(-1);
                    }else if(mili==1&&sec==0){
                      ma_lat=ma_lat*(-1);
                    }
                    double lat=ma_lat,lng=ma_lng;

                    Map.Location current_location=(map.getMarkerLocation(item));//base position
                    Map.Location dancing_location=map.newLocationInstance(current_location.latitude+lat,current_location.longitude+lng);
                    map.setMarkerPosition(item,dancing_location);
                  }

                  if(mili==1&&/*sec==1&&*/index==rotate_index){//rotate if exclusive
                    bearing=new Date().getMinutes()+new Date().getSeconds();
                    checker=System.currentTimeMillis()%2;
                    if(checker==1)bearing=bearing*2;
                    else bearing=bearing*3;
                    map.setMarkerIcon(item,getCar(bearing));
                  }
                }
                index++;
              }
            }
          });
          times=0;
          while(times<T.TASK_DANCING_SLEEP_COUNT-mili-sec){//float count
            try{TimeUnit.MILLISECONDS.sleep(T.TASK_SLEEP_TIMEOUT);}catch(InterruptedException e){}
            if(!needCarsNear)break;
            times++;
          }
        }
      }
    }).start();
  }
  public void stopCarsNear(){
    needCarsNear=false;
    if(carsNearList!=null){
      for(Object item:carsNearList){
        map.removeMarker(item);
      }
      carsNearList.clear();carsNearList=null;
    }
  }

  public void clearTripMarkers(){
    clearTripMarkerA();
    clearTripMarkerB();
    clearTripRoute();
    bookTrip=BOOK_TRIP_NO_PICKUP;
  }

  public void addTripMarker(Map.Location location){
    addTripMarker(location,true);
  }
  public void addTripMarker(Map.Location location,boolean do_google_direction){
    if(bookTrip==BOOK_TRIP_NO_PICKUP||bookTrip==BOOK_TRIP_PICKUP_AB){
      if(tripMarkerA!=null)map.setMarkerPosition(tripMarkerA,location);
      else{
        try{tripMarkerA=map.addMarker(location,R.drawable.marker_a);}catch(Exception e){}
      }
      bookTrip=BOOK_TRIP_PICKUP_A;
      manager.putOrderLatitude(Double.toString(location.latitude));
      manager.putOrderLongitude(Double.toString(location.longitude));
      manager.getEditor().commit();
      //clear
      clearTripMarkerB();
      clearTripRoute();
      manager.removeFragment(T.FRAGMENT_NAME_TRIP);
      //vibrate
      if(manager.isVibrate())manager.doVibrate(T.TASK_VIBRATE_1_COUNT);
    }
    else if(bookTrip==BOOK_TRIP_PICKUP_A){
      if(tripMarkerB!=null)map.setMarkerPosition(tripMarkerB,location);
      else{
        try{tripMarkerB=map.addMarker(location,R.drawable.marker_b);}catch(Exception e){}
      }
      bookTrip=BOOK_TRIP_PICKUP_AB;
      manager.putDeliveryLatitude(Double.toString(location.latitude));
      manager.putDeliveryLongitude(Double.toString(location.longitude));
      manager.getEditor().commit();
      //goto
      Map.Location location1=map.getMarkerPosition(tripMarkerA),location2=map.getMarkerPosition(tripMarkerB);
      map.toLocation(location1,location2);
      //vibrate
      if(manager.isVibrate())manager.doVibrate(T.TASK_VIBRATE_1_COUNT);
    }
    if(bookTrip==BOOK_TRIP_PICKUP_AB&&do_google_direction){//calc route
      manager.googleDirections(Manager.DIRECTIONS_ROUTE_LOCATION);
    }
  }
  private void addTripMarkerA(double lat,double lng){//for driver(first A trip point preview)
    Map.Location location=map.newLocationInstance(lat,lng);
    try{tripMarkerA=map.addMarker(location,R.drawable.marker_a);}catch(Exception e){}
  }
  private void addTripMarkerB(double lat,double lng){//for driver(last B trip point preview)
    Map.Location location=map.newLocationInstance(lat,lng);
    try{tripMarkerB=map.addMarker(location,R.drawable.marker_b);}catch(Exception e){}
  }
  private void addDeliveryMarker(double lat,double lng){//point to delivery order
    Map.Location location=map.newLocationInstance(lat,lng);
    try{addDeliveryMarker(location);}catch(Exception e){}
  }
  private void addPickupMarker(double lat,double lng){
    Map.Location location=map.newLocationInstance(lat,lng);
    addPickupMarker(location);
  }
  private void addDropoffMarker(double lat,double lng){
    Map.Location location=map.newLocationInstance(lat,lng);
    addDropoffMarker(location);
  }
  public void addPickupMarker(Map.Location location){
    if(tripMarkerA!=null)map.setMarkerPosition(tripMarkerA,location.latitude,location.longitude);
    else try{tripMarkerA=map.addMarker(location,R.drawable.pickup);}catch(Exception e){}
  }
  public void addDropoffMarker(Map.Location location){
    if(tripMarkerB!=null)map.setMarkerPosition(tripMarkerB,location.latitude,location.longitude);
    else try{tripMarkerB=map.addMarker(location,R.drawable.dropoff);}catch(Exception e){}
  }
  public void addDeliveryMarker(Map.Location location){
    if(deliveryMarker!=null)map.setMarkerPosition(deliveryMarker,location.latitude,location.longitude);
    else {try{deliveryMarker=map.addMarker(location,R.drawable.marker_a);}catch(Exception e){}}
  }
  public void moveLocalMarker(Map.Location location){
    if(localMarker!=null)map.setMarkerPosition(localMarker,location.latitude,location.longitude);
  }
  public void toLocation(Map.Location location){if(map!=null)map.toLocation(location);}
  public void toLocation(double lat1,double lng1){if(map!=null){map.toLocation(lat1,lng1);}}
  public void toLocation(double lat1,double lng1,double lat2,double lng2){if(map!=null){map.toLocation(lat1,lng1,lat2,lng2);}}

  private int getCar(){
    return R.drawable.transport_0;
  }
  private int getCar(float bearing){
    int ret_val;
    if(bearing>340)ret_val=R.drawable.transport_0;//0
    else if(bearing>295)ret_val=R.drawable.transport_315;//0-45
    else if(bearing>250)ret_val=R.drawable.transport_270;//270
    else if(bearing>205)ret_val=R.drawable.transport_225;//180+45
    else if(bearing>160)ret_val=R.drawable.transport_180;//180
    else if(bearing>115)ret_val=R.drawable.transport_135;//180-45
    else if(bearing>70)ret_val=R.drawable.transport_90;//90
    else if(bearing>25)ret_val=R.drawable.transport_45;//0+45
    else ret_val=R.drawable.transport_0;//0
    return ret_val;
  }

  //show
  public void showTripRoute(OrderAB order){//for driver
    if(tripRoute==null&&order!=null&&order.delivery_lat!=0&&order.delivery_lon!=0){
      //one to one point(add only B marker)
      if(order.order_lat==order.delivery_lat&&order.order_lon==order.delivery_lon){
        if(order.delivery_lat!=0&&order.delivery_lon!=0)//and not null
          addTripMarkerB(order.delivery_lat,order.delivery_lon);
      }
      else{//others points (add A and B markers)
        if(order.order_lat!=0&&order.order_lon!=0){
          addTripMarkerA(order.order_lat,order.order_lon);
        }
        if(order.delivery_lat!=0&&order.delivery_lon!=0){
          addTripMarkerB(order.delivery_lat,order.delivery_lon);
        }
      }
      if(order.route_data!=null){
        Direction d=manager.routeDecode(order);
        addTripRoute(d);
      }
    }
  }
  private void showDirection(OrderAB order){
    //direction
    Direction d=manager.routeDecode(order);
    /*if(DEBUG)Toast.makeText(context.getApplicationContext(),
    d!=null?
    d.finishAddress+T.SPACE+d.distanceText+T.SPACE+d.durationText+(d.steps!=null?T.SPACE+" steps="+d.steps.size():T.EMPTY):"direction empty",
    Toast.LENGTH_SHORT).show();*/
    //route
    clearTripMarkers();
    if(d.startLocationLat!=0&&d.startLocationLon!=0)addTripMarker(map.newLocationInstance(d.startLocationLat,d.startLocationLon),false);
    if(d.finishLocationLat!=0&&d.finishLocationLon!=0)addTripMarker(map.newLocationInstance(d.finishLocationLat,d.finishLocationLon),false);
    addTripRoute(d);
  }
  private void showOrderStatus(){
    Fragment frag=manager.getFragment(T.FRAGMENT_NAME_ORDER_STATUS);
    if(frag==null){
      long status_id=manager.getOrderStatusId();
      String str=manager.getOrderStatusFromPref();
      boolean visibility=status_id<0||status_id>OrderABStatus.ORDER_STATUS_ACCEPTED?false:true;
      manager.showOrderStatusFragment(str,-1,visibility,manager.isShowLogo());
    }
    else manager.showFragment(T.FRAGMENT_NAME_ORDER_STATUS);
  }
}