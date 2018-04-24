package autozvit.com.fireart.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import autozvit.com.fireart.R;
import autozvit.com.fireart.forms.StartActivity;
import autozvit.com.fireart.objects.Attr;
import autozvit.com.fireart.objects.OrderABStatus;
import autozvit.com.fireart.objects.Track;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.tools.T;
import autozvit.com.fireart.tools.TypedCallback;

public class DispatcherService extends Service{
  public final static int NOTIFICATION_SERVICE_ID=10000;
  public final static String INTENT_PARAM_ACTIVATE="activate";
  public final static String INTENT_PARAM_MAP_MODE="map_mode";

  public final static String BROADCAST_CLASS_DISPATCHER_SERVICE="autozvit.com.fireart.service.DispatcherService";
  public final static String BROADCAST_CLASS_ORDER_STATUS="autozvit.com.fireart.forms.OrderStatusFragment";
  public final static String BROADCAST_CLASS_MAP="autozvit.com.fireart.forms.Map2Fragment";
  public final static String BROADCAST_INTENT_ORDER_STATUS="status";
  //public final static String BROADCAST_INTENT_TRACKER="tracker";

  public static final int RESTART_TIMEOUT=30000;//30 sec
  public static final int SERVICE_THREAD_SLEEP_TIMEOUT=1000;//1 sec
  public static final int SERVICE_THREAD_SLEEP_COUNT=30;//30 times

  public static final int MILLISEC_IN_SEC=1000;

  private Manager manager=null;
  private Context context;
  private Handler handler;
  private ServiceThread serviceThread=null;
  private Intent broadcastIntent;
  //private BroadcastReceiver broadcastReceiver=null;
  //private Notification notification;
  private NotificationManager notificationManager;
  private LocationManager locationManager=null;
  private LocationListener locationListener=null;
  private SatellitesListener satellitesListener=null;

  private int satellitesCount,batteryLevel;
  private boolean needService=true,sensorActivity=false;
  private int notificationRequestCode=100000;

  private class SatellitesListener implements GpsStatus.Listener{
    @Override
    public void onGpsStatusChanged(int event){
      switch(event){
        case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
          break;
        case GpsStatus.GPS_EVENT_STOPPED:
          //stopSatellitesListener();
          break;
      }
    }
  }
  private class LocationListener implements android.location.LocationListener{
    @Override
    public void onLocationChanged(Location location){
      //for Android 3.0+(on NetBeans works no thread, but AndroidStudio critical needs!)
      //try{saveLocation(location);}catch(Exception e){}/*works for NetBeans*/
      new Thread(new onLocationChanged(location)).start();/*works for AndroidStudio*/
    }
    /*status=0-"OUT_OF_SERVICE" 1-"TEMPORARILY_UNAVAILABLE" 2-"AVAILABLE"*/
    @Override
    public void onStatusChanged(String provider,int status,Bundle extras){}
    @Override
    public void onProviderEnabled(String provider){}
    @Override
    public void onProviderDisabled(String provider){
      //stopLocationListener();
      //stopSatellitesListener();
    }
  }
  //use for version Android 3.0+ (saveLocation in Thread)
  private class onLocationChanged implements Runnable{
    Location location;
    public onLocationChanged(Location location){
      this.location=location;
    }
    public void run(){
      try{saveLocation(location);}catch(Exception e){}
    }
  }

  @Override
  public void onCreate(){
    super.onCreate();
  }
  @Override
  public void onDestroy(){
    if(serviceThread!=null)serviceThread.setNeedClose(true);

    /*if(broadcastIntent!=null){
      broadcastIntent.putExtra("status",1);
      sendBroadcast(broadcastIntent);
    }*/

    if(needService){
      Intent restart=new Intent(getApplicationContext(),this.getClass());
      restart.setPackage(getPackageName());
      PendingIntent pending=PendingIntent.getService(getApplicationContext(),1,restart,PendingIntent.FLAG_ONE_SHOT);
      AlarmManager alarm=(AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
      alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+RESTART_TIMEOUT,pending);
    }
    super.onDestroy();
  }
  @Override
  public int onStartCommand(Intent intent,int flags,int startId){
    //flags is signaling for reactivation of service after died
    //flags:START_FLAG_RETRY | START_FLAG_REDELIVERY.

    boolean activate=intent.getBooleanExtra(INTENT_PARAM_ACTIVATE,false);

    if(activate){
      //T.updateLanguage(language,getApplicationContext());
      context=this;
      //manager
      if(manager==null){
        manager=new Manager(context);
        manager.getPrefData();
      }
      else manager.seekPrefData();
      //sensor
      if(manager.getSensorId()==-1)manager.addSensorRequest();
      //mode
      manager.setMapMode(intent.getIntExtra(INTENT_PARAM_MAP_MODE,Manager.MAP_CLIENT));
      //location
      if(locationManager==null)locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
      //notification
      if(notificationManager==null)notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
      //thread.setPriority(Thread.MAX_PRIORITY);
      if(serviceThread==null){
        handler=new Handler();
        serviceThread=new ServiceThread(startId);
        Thread thread=new Thread(serviceThread);
        thread.start();
      }
      else if(!serviceThread.isStarted()){
        serviceThread.setNeedClose(false);
        Thread thread=new Thread(serviceThread);
        thread.start();
      }
      //receiver
      /*if(broadcastReceiver==null){
        broadcastReceiver=manager.startBroadcastReceiver(
        DispatcherService.BROADCAST_CLASS_DISPATCHER_SERVICE,
        DispatcherService.BROADCAST_INTENT_TRACKER,
        trackerCallbackHandler);
      }*/
      //super.onStartCommand(intent,flags,startId);//system variant
    }
    else{
      if(serviceThread!=null)serviceThread.setNeedClose(true);
    }
    //START_NOT_STICKY[not reactivated]|START_STICKY[reactivate after died]
    //START_REDELIVER_INTENT[reactivate with all param got from startService]
    //return /*activate?START_REDELIVER_INTENT:START_NOT_STICKY*/START_NOT_STICKY;
    return START_REDELIVER_INTENT;
  }

  @Override
  public IBinder onBind(Intent intent){
    return null;
  }

  //location
  private void seekSatellitesAndBattery(){
    //sat
    int count=0;
    GpsStatus gps_status=null;
    if(locationManager!=null){
      try{
        gps_status=locationManager.getGpsStatus(null);
      }catch(SecurityException e){}
    }
    if(gps_status!=null){
      Iterable<GpsSatellite> satellites=gps_status.getSatellites();
      Iterator<GpsSatellite> i=satellites.iterator();
      while(i.hasNext()){i.next();count++;}
      satellitesCount=count;
    }
    //bat
    IntentFilter bat_filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    Intent battery=registerReceiver(null,bat_filter);
    int level=battery.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
    int scale=battery.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
    batteryLevel=(int)(100*level)/scale;//percent value
  }

  private void saveLocation(Location location){
    if(location==null)return;

    float accu=location.getAccuracy();//точность в meters. 0.0 - нет показаний (для всех ниже)
    double alti=location.getAltitude();//высота над морем в meters
    float bear=location.getBearing();//направление, относительно сервера 0.0 - 360.0
    float speed=location.getSpeed();//скорость в метрах/секунда
    double lat=location.getLatitude();
    double lng=location.getLongitude();
    long time=location.getTime();

    sensorActivity=true;

    seekSatellitesAndBattery();

    /*if(broadcastIntent!=null){//for driver taximeter
      broadcastIntent.putExtra("status",1).putExtra("latitude",latitude).putExtra("longitude",longitude).putExtra("speed",speed);
      broadcastIntent.putExtra("satellites_count",satellitesCount).putExtra("battery_level",batteryLevel);
      sendBroadcast(broadcastIntent);
    }*/

    //save to server
    if(manager!=null){
      Track track=new Track();
      track.accuracy=accu;
      track.altitude=alti;
      track.bearing=bear;
      track.speed=speed;
      track.latitude=lat;
      track.longitude=lng;
      track.track_time=time/MILLISEC_IN_SEC;//in sec
      track.satellites=satellitesCount;
      track.battery=batteryLevel;
      //not need is addSensor
      //manager.seekPrefData();//re-read param
      manager.setCurrentTrack(track);
      manager.addTrackRequest();//addTrackRequest(track)
    }
  }

  private void startLocationListener(){
    if(locationListener==null&&locationManager!=null
      /*&&locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)*/){
      handler.post(new Runnable(){
        @Override
        public void run(){
          locationListener=new LocationListener();
          try{locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MILLISEC_IN_SEC*T.GPS_PROVIDER_TIMEOUT_DEFAULT_VALUE,T.GPS_PROVIDER_METERS_DEFAULT_VALUE,locationListener);}catch(SecurityException se){}
          catch(Exception e){}
        }
      });
    }
  }
  private void startSatellitesListener(){
    if(satellitesListener==null&&locationManager!=null
      /*&&locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)*/){
      handler.post(new Runnable(){
        @Override
        public void run(){
          satellitesListener=new SatellitesListener();
          try{locationManager.addGpsStatusListener(satellitesListener);}catch(SecurityException se){}
          catch(Exception e){}
        }
      });
    }
  }
  private void stopLocationListener(){
    if(locationManager!=null&&locationListener!=null){
      handler.post(new Runnable(){
        @Override
        public void run(){
          try{locationManager.removeUpdates(locationListener);}catch(SecurityException se){}
          catch(Exception e){}
          locationListener=null;
        }
      });
    }
  }
  private void stopSatellitesListener(){
    if(locationManager!=null&&satellitesListener!=null){
      handler.post(new Runnable(){
        @Override
        public void run(){
          locationManager.removeGpsStatusListener(satellitesListener);
          satellitesListener=null;
        }
      });
    }
  }

  //tools
  private void sendServiceNotification(String title,String message,boolean is_vibr,String sound,int logo,int icon){
    Intent intent=new Intent(this,StartActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent pending=PendingIntent.getActivity(this,++notificationRequestCode,intent,PendingIntent.FLAG_CANCEL_CURRENT);
    //create notification
    Bitmap bitmap=BitmapFactory.decodeResource(getResources(),logo);
    NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
    //.setSmallIcon(R.mipmap.app_logo)
    .setSmallIcon(icon)
    .setLargeIcon(bitmap)
    .setContentTitle(title).setContentText(message);

    int defaults=0;
    //builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
    if(sound!=null)builder.setSound(Uri.parse(sound));
    else defaults|=Notification.DEFAULT_SOUND;
    if(is_vibr)defaults|=Notification.DEFAULT_VIBRATE;//defaults=defaults|Notification.DEFAULT_VIBRATE
    builder.setDefaults(defaults);
    builder.setAutoCancel(true);
    builder.setContentIntent(pending);
    notificationManager.notify(NOTIFICATION_SERVICE_ID,builder.build());
  }
  private void sendBroadcast(String classname,String param,String value){
    broadcastIntent=new Intent(classname);
    broadcastIntent.putExtra(param,value);
    sendBroadcast(broadcastIntent);
  }
  public TypedCallback showOrderABStatusServiceCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      OrderABStatus order_AB_status=(OrderABStatus)obj;
      //if(manager.DEBUG)toast("appStatus="+manager.getOrderStatusId()+" dbStatus="+order_AB_status.status_id);

      if(manager.getMapMode()==Manager.MAP_CLIENT){//client (check the orderId)
        if(order_AB_status.order_id!=-1&&order_AB_status.order_id==manager.getOrderId()&&
           order_AB_status.status_id!=manager.getOrderStatusId()){
          String str=null;
          manager.putOrderStatusId(order_AB_status.status_id);
          manager.putOrderStatusName(order_AB_status.name);
          if(order_AB_status.attrList!=null){
            Attr attr=order_AB_status.attrList.get(0);
            manager.putOrderStatusValue(attr.value);
            str=attr.value;
          }
          manager.getEditor().commit();
          if(str==null)str=order_AB_status.name;
          str=str.toUpperCase();

          //send broadcast
          String str_status=String.valueOf(order_AB_status.status_id);
          //sendBroadcast(BROADCAST_CLASS_ORDER_STATUS,BROADCAST_INTENT_ORDER_STATUS,str_status);
          sendBroadcast(BROADCAST_CLASS_MAP,BROADCAST_INTENT_ORDER_STATUS,str_status);

          //sound & vibr defaultPreferences
          //manager.newInstanceDefaultPreferences();//other thread can change
          manager.newUltimateInstanceDefaultPreferences();
          boolean is_new_message=manager.isNewMessage();
          boolean is_vibr=manager.isVibrate();
          String sound=manager.getSound();

          //send notification
          if(is_new_message)sendServiceNotification(getString(R.string.app_name),str,is_vibr,sound,R.drawable.app_logo,R.mipmap.ic_local_taxi_black_36dp);
        }
      }
      else if(manager.getMapMode()==Manager.MAP_DRIVER&&manager.isTripMode()){//driver (check the tripOrderId)
        //trip status allways more then
        if(order_AB_status.order_id!=-1&&order_AB_status.order_id==manager.getTripOrderId()&&
           order_AB_status.status_id!=manager.getTripOrderStatusId()){//force-major

          manager.putTripOrderStatusId(order_AB_status.status_id);
          manager.getEditor().commit();

          //send broadcast
          String str_status=String.valueOf(order_AB_status.status_id);
          sendBroadcast(BROADCAST_CLASS_MAP,BROADCAST_INTENT_ORDER_STATUS,str_status);

          //sound & vibr defaultPreferences
          manager.newUltimateInstanceDefaultPreferences();
          boolean is_new_message=manager.isNewMessage();
          boolean is_vibr=manager.isVibrate();
          String sound=manager.getSound();

          //send notification
          if(is_new_message)sendServiceNotification(getString(R.string.app_name),getString(R.string.order_status_changed),is_vibr,sound,R.drawable.app_logo,R.drawable.ic_highlight_off_pink_36dp);
        }
      }
    }
  };
  /*public TypedCallback trackerCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
    }
  };*/

  class ServiceThread implements Runnable{
    private int startId;
    private boolean needClose=false,started=false;
    private int times;
    public void setNeedClose(boolean need_close){needClose=need_close;}
    public boolean isStarted(){return started;}
    public ServiceThread(int start_id){
      startId=start_id;
    }
    public void run(){
      if(manager.DEBUG)toast("start service");

      started=true;

      //start gps
      startLocationListener();
      startSatellitesListener();

      //Looper.prepare();

      while(!needClose){
        try{
          times=0;
          while(times<SERVICE_THREAD_SLEEP_COUNT/*T.TASK_SERVICE_SLEEP_COUNT*/){//may be sleep goto to end?
            try{TimeUnit.MILLISECONDS.sleep(SERVICE_THREAD_SLEEP_TIMEOUT/*T.TASK_SLEEP_TIMEOUT*/);}catch(InterruptedException e){}
            if(needClose)break;
            times++;
          }
          /*handler.post(new Runnable(){
              @Override
              public void run(){
                Looper.loop();}});*/
          if(needClose)break;

          //sensor activated
          if(sensorActivity&&!manager.isSensorActivated())manager.updateSensorActivityRequest(manager.getSensorId(),(byte)1);

          if(manager.getMapMode()==Manager.MAP_CLIENT){//client
            if(manager.DEBUG)toast("service to client");
            //old order(more then one day)
            //ORDER TIME CAN BE UPDATED in other THREAD!
            long time=manager.getOrderTime();
            if(time!=0&&time+T.MILISEC_IN_DAY<System.currentTimeMillis()){
              needClose=true;break;
            }
            //order is lost
            if(manager.getOrderStatusId()<0||manager.getOrderStatusId()==OrderABStatus.ORDER_STATUS_COMPLETED){
              needClose=true;break;
              //remove sensor circle with?
              //manager.removeSensorCircle(circleSensorId);
            }

            //current order for client (may be a admin|driver cancel it)
            if(manager.getOrderId()!=-1){
              OrderABStatus order_status=new OrderABStatus();
              order_status.order_id=-1;
              manager.getOrderABStatusRequest(manager.getUserId(),manager.getOrderId(),showOrderABStatusServiceCallbackHandler,order_status);
            }
          }
          else if(manager.getMapMode()==Manager.MAP_DRIVER){//driver
            //if(manager.DEBUG)toast("service to driver");
            //tripMode
            if(manager.isTripMode()){
              if(manager.DEBUG)toast("service trip mode");
              //need to check orderStatus (may be admin|client cancel it)
              if(manager.getTripOrderId()!=-1){
                OrderABStatus order_status=new OrderABStatus();
                order_status.order_id=-1;
                manager.getOrderABStatusRequest(manager.getTripOrderUserId(),manager.getTripOrderId(),showOrderABStatusServiceCallbackHandler,order_status);
              }
            }
          }

        }catch(Exception e){if(manager.DEBUG)errorToast(e.toString());}
      }

      //stop gps
      stopLocationListener();
      stopSatellitesListener();
      //sensor deactivated
      if(sensorActivity&&manager.isSensorActivated())manager.updateSensorActivityRequest(manager.getSensorId(),(byte)0);
      sensorActivity=false;
      started=false;//thread not started
      needService=!needClose;//normal closing
      stopSelfResult(startId);//return boolean is service died
      if(manager.DEBUG)toast("stop service");
    }
    private void errorToast(final String error_message){
	    handler.post(new Runnable(){
		  @Override
		  public void run(){
			  Toast.makeText(getApplicationContext(),error_message,Toast.LENGTH_LONG).show();
		  }});
    }
  }

  private void toast(final String message){
    handler.post(new Runnable(){
      @Override
      public void run(){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
      }});
  }
}