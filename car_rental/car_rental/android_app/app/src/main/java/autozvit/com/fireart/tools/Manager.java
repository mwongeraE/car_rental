package autozvit.com.fireart.tools;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.app.AlertDialog;
//import android.support.v7.app.AlertDialog;//accent colors
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.matesnetwork.Cognalys.VerifyMobile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Source;
import com.stripe.android.model.SourceParams;
import com.stripe.android.model.Token;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import autozvit.com.fireart.R;
import autozvit.com.fireart.forms.ProductFragment;
import autozvit.com.fireart.other_package.forms.StartClientActivity;
import autozvit.com.fireart.other_package.forms.StartDriverActivity;
import autozvit.com.fireart.forms.CartFragment;
import autozvit.com.fireart.forms.DriverActivity;
import autozvit.com.fireart.forms.DropoffFragment;
import autozvit.com.fireart.forms.ErrorFragment;
import autozvit.com.fireart.forms.FindFragment;
import autozvit.com.fireart.forms.InquiryFragment;
import autozvit.com.fireart.forms.Map2Fragment;
//import autozvit.com.fireart.forms.MapFragment;
import autozvit.com.fireart.forms.ImageFragment;
import autozvit.com.fireart.forms.InfoFragment;
import autozvit.com.fireart.forms.ListFragment;
import autozvit.com.fireart.forms.OrderStatusFragment;
import autozvit.com.fireart.forms.PaymentFragment;
import autozvit.com.fireart.forms.PickupFragment;
import autozvit.com.fireart.forms.RegisterFragment;
import autozvit.com.fireart.forms.InvoiceFragment;
import autozvit.com.fireart.forms.RideFragment;
import autozvit.com.fireart.forms.SettingsActivity;
import autozvit.com.fireart.forms.SigninFragment;
import autozvit.com.fireart.forms.SignupFragment;
import autozvit.com.fireart.forms.SplashActivity;
import autozvit.com.fireart.forms.StartActivity;
import autozvit.com.fireart.forms.StartFragment;
import autozvit.com.fireart.forms.StatFragment;
import autozvit.com.fireart.forms.SuccessFragment;
import autozvit.com.fireart.forms.TaximeterFragment;
import autozvit.com.fireart.forms.TipFragment;
import autozvit.com.fireart.forms.TripFragment;
import autozvit.com.fireart.forms.list.MessageListAdapter;
import autozvit.com.fireart.forms.list.MessageListContent;
import autozvit.com.fireart.forms.list.OrderABListAdapter;
import autozvit.com.fireart.forms.list.OrderABListContent;
import autozvit.com.fireart.forms.list.ProductListAdapter;
import autozvit.com.fireart.forms.list.ProductListContent;
import autozvit.com.fireart.objects.Attr;
import autozvit.com.fireart.objects.Color;
import autozvit.com.fireart.objects.Currency;
import autozvit.com.fireart.objects.Direction;
import autozvit.com.fireart.objects.Geocode;
import autozvit.com.fireart.objects.Message;
import autozvit.com.fireart.objects.OrderAB;
import autozvit.com.fireart.objects.OrderABPart;
import autozvit.com.fireart.objects.OrderABStatus;
import autozvit.com.fireart.objects.Product;
import autozvit.com.fireart.objects.ProductParam;
import autozvit.com.fireart.objects.Purchase;
import autozvit.com.fireart.objects.RetVal;
import autozvit.com.fireart.objects.Stat;
import autozvit.com.fireart.objects.Tax;
import autozvit.com.fireart.objects.Track;
import autozvit.com.fireart.objects.Transport;
import autozvit.com.fireart.objects.TransportReview;
import autozvit.com.fireart.objects.Type;
import autozvit.com.fireart.objects.User;
import autozvit.com.fireart.service.DispatcherService;
import autozvit.com.fireart.storage.RemoteStorage;
import autozvit.com.location.LatLng;

import static android.content.Context.ACTIVITY_SERVICE;

/*
Base class for from package manager
forms starting, preferences saving, others
*/
public class Manager implements Cloneable{
  public static boolean DEBUG=false;//debug app
  public static int TYPE_OF_APP=0;//0=fireart app
  public static boolean VERIFY_PHONE_NUMBER=true,VERIFY_PHONE_SIMULATE=true;//phone verify
  public static boolean FAST_LOCATION_PROVIDER=true;//location provider (true for PASSIVE_PROVIDER)
  public static boolean NO_TOOLBAR=true;//toolbar visibility
  public static boolean CAR_RENTAL=true;//car rental app (for delivery and easy mode)
  //not change it (default keywords for empty keys)
  public static final String DEFAULT_API_KEY="put_key_here";
  public static final String DEFAULT_COGNALYS_APP_ID="put_app_id_number_here";
  public static final String DEFAULT_COGNALYS_ACCESS_TOKEN="put_access_token_here";
  public static final String DEFAULT_GOOGLE_MAPS_KEY="put_key_here";
  public static final String DEFAULT_PAYPAL_KEY="put_key_here";
  public static final String DEFAULT_STRIPE_KEY="put_key_here";

  //type of app (other package app)
  public static final int DRIVER_APP=1;//start as other package driver app
  public static final int CLIENT_APP=2;//start as other package client app

  //as array pref_list_values_booking_style[0..1]
  public static final int BOOKING_STYLE_TRIP=1;//trip booking style (tap A and B trip points on map)
  public static final int BOOKING_STYLE_DELIVERY=2;//product booking style (tap a product on list and A point for delivery)
  public static final int BOOKING_STYLE_EASY=3;//uber similar style
  public static final int BOOKING_STYLE_EASY_AB=4;//pickup and dropoff address input with google PlaceAutocompleteFragment

  public static final int EASY_BOOKING_PICKUP=1,EASY_BOOKING_DROPOFF=2,EASY_BOOKING_RIDE=3,EASY_BOOKING_INVOICE=4;
  public static final int DEFAULT_BOOKING_STYLE=BOOKING_STYLE_EASY;//earlier style BOOKING_STYLE_EASY

  /*under revision*/
  public static final int LIST_STYLE_ORDER_AB=1,LIST_STYLE_ORDER_ROUTE=2,LIST_STYLE_ORDER_LOCATION=3;
  public static final int LIST_STYLE_PRODUCT=1,LIST_STYLE_PRODUCT_DELIVERY=2, LIST_STYLE_PRODUCT_RENTAL=3;//style for delivery booking style
  public static final int DEFAULT_LIST_STYLE_ORDER=LIST_STYLE_ORDER_AB;
  public static final int DEFAULT_LIST_STYLE_PRODUCT=LIST_STYLE_PRODUCT_DELIVERY;

  public static int TYPE_OF_ORDER_LIST=DEFAULT_LIST_STYLE_ORDER;
  public static int TYPE_OF_PRODUCT_LIST=DEFAULT_LIST_STYLE_PRODUCT;

  //as array pref_list_values_map_provider[0..1]
  public static final int MAP_PROVIDER_GOOGLE=Map.MAP_PROVIDER_GOOGLE;
  public static final int MAP_PROVIDER_OSM=Map.MAP_PROVIDER_OSM;
  public static final int DEFAULT_MAP_PROVIDER=MAP_PROVIDER_GOOGLE;

  public static final int PAYMENT_PROVIDER_GOOGLE=0;//not support (only for static purchases for support and development)
  public static final int PAYMENT_PROVIDER_PAYPAL=1;//com.paypal.sdk:paypal-android-sdk:2.15.3 (https://github.com/paypal/PayPal-Android-SDK/blob/master/docs/single_payment.md)
  public static final int PAYMENT_PROVIDER_STRIPE=2;//com.stripe:stripe-android:4.0.1 (https://stripe.com/docs/mobile/android)
  public static final int DEFAULT_PAYMENT_PROVIDER=PAYMENT_PROVIDER_PAYPAL;

  public static final int DELIVERY_BY_SERVICE_TRANSPORT=1;
  public static final int DIRECTIONS_ROUTE_LOCATION=0,DIRECTIONS_CLIENT_DRIVER=1,DIRECTIONS_ROUTE_ADDRESS=2;
  public static final int PRODUCT_LIST_COL_COUNT=1,ORDER_LIST_COL_COUNT=1,MESSAGE_LIST_COL_COUNT=1;
  public static final int LIST_COUNT=10;

  public static final int DISCOUNT_IN_PERCENT=1;
  public static final int DISCOUNT_IN_VALUE=2;
  public static final int INCREASE_IN_PERCENT=3;
  public static final int INCREASE_IN_VALUE=4;

  public static final int MAP_CLIENT=Map2Fragment.CALLBACK_COMMAND_CLIENT,MAP_DRIVER=Map2Fragment.CALLBACK_COMMAND_DRIVER;
  public static final String INTENT_PARAM_MESSAGE="message";
  public static final String INTENT_PARAM_RESULT="result";
  public static final int SET_PERMISSIONS_REQUEST_CODE=1;

/*
java.text.SimpleDateFormat
http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html

The following examples show how date and time patterns are interpreted in the U.S. locale. The given date and time are 2001-07-04 12:08:56 local time in the U.S. Pacific Time time zone.

Date and Time Pattern 	Result
"yyyy.MM.dd G 'at' HH:mm:ss z" 	2001.07.04 AD at 12:08:56 PDT
"EEE, MMM d, ''yy" 	Wed, Jul 4, '01
"h:mm a" 	12:08 PM
"hh 'o''clock' a, zzzz" 	12 o'clock PM, Pacific Daylight Time
"K:mm a, z" 	0:08 PM, PDT
"yyyyy.MMMMM.dd GGG hh:mm aaa" 	02001.July.04 AD 12:08 PM
"EEE, d MMM yyyy HH:mm:ss Z" 	Wed, 4 Jul 2001 12:08:56 -0700
"yyMMddHHmmssZ" 	010704120856-0700
"yyyy-MM-dd'T'HH:mm:ss.SSSZ" 	2001-07-04T12:08:56.235-0700
*/

  public static final String PRICE_FORMAT="%.2f";
  public static final String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";//MySQL format(24 hours)
  public static final String DATETIME_FLOAT_FORMAT="yyyy-MM-dd HH:mm";//no sec
  public static final String DATE_FLOAT_FORMAT="yyyy-[M]M-[d]d hh:mm:ss[.f...]";//check for work
  public static final String EASY_PRODUCT_LIST_ADAPTER_CLASS=".forms.list.EasyProductListAdapter";
  public static final String PRODUCT_LIST_ADAPTER_CLASS=".forms.list.ProductListAdapter";
  public static final String ORDER_LIST_ADAPTER_CLASS=".forms.list.OrderABListAdapter";
  public static final String MESSAGE_LIST_ADAPTER_CLASS=".forms.list.MessageListAdapter";

  public static final String ORDER_INFO="trip duration:%s distance:%s";
  public static final String ORDER_REVIEW="review for order:%s";
  public static final String PAYMENT_INFO="device:%s phone:%s";
  public static final String PAYMENT_AMOUNT="%s%s %s";
  public static final String HOURS_MINUTES="%02d:%02d";
  public static final String GOOGLE_DIRECTIONS_ENCODING="UTF-8";
  public static final String NO_VALUE="-1";
  public static final String DEFAULT_DELIVERY_TYPE="1";
  public static final String DEFAULT_TRACK_TYPE="1";
  public static final String NO_MONEY="0.00";
  public static final String NO_TIME="0:01";
  public static final String ZERO="0";

  /*Google Map route url*/
  //first string source address, second string destination address
  public String GOOGLE_MAP_ROUTE="https://www.google.com.ua/maps/dir/%s/%s";

  /*  Open in browser google map route from source to destination

      Uri address = Uri.parse(String.format(GOOGLE_MAP_ROUTE,source,destination));
      Intent openlink = new Intent(Intent.ACTION_VIEW, address);
      startActivity(openlink); //Intent.createChooser(openlink, "Browser")
  */
  /*api keys*/
  private String cognalysAppId=DEFAULT_COGNALYS_APP_ID;
  public void setCognalysAppId(String cognalis_app_id){cognalysAppId=cognalis_app_id;}
  private String cognalysAccessToken=DEFAULT_COGNALYS_ACCESS_TOKEN;
  public void setCognalysAccessToken(String cognalys_access_token){cognalysAccessToken=cognalys_access_token;}
  private String googleMapsKey=DEFAULT_GOOGLE_MAPS_KEY;
  public void setGoogleMapsKey(String google_maps_key){googleMapsKey=google_maps_key;}
  public String getGoogleMapsKey(){return googleMapsKey;}
  private String paypalKey=DEFAULT_PAYPAL_KEY;
  public void setPaypalKey(String paypal_key){paypalKey=paypal_key;}
  public String getPaypalKey(){return paypalKey;}
  private String stripeKey=DEFAULT_STRIPE_KEY;
  public void setStripeKey(String stripe_key){stripeKey=stripe_key;}
  public String getStripeKey(){return stripeKey;}

  /*object*/
  private Context context;
  public Context getContext(){return context;}
  private Resources resources;
  public Resources getResources(){return resources;}
  private String packageName="autozvit.com.fireart";//manifest package name
  public String getPackageName(){return packageName;}
  private SharedPreferences defaultPreferences;
  public SharedPreferences getDefaultPreferences(){return defaultPreferences;}
  public SharedPreferences newInstanceDefaultPreferences(){return defaultPreferences=PreferenceManager.getDefaultSharedPreferences(context);}
  public SharedPreferences newUltimateInstanceDefaultPreferences(){
    return defaultPreferences=context.getSharedPreferences(context.getPackageName()+"_preferences",
                              Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
  }
  private SharedPreferences preferences;
  public SharedPreferences getPreferences(){return preferences;}
  public SharedPreferences newInstancePreferences(){return preferences=context.getSharedPreferences(T.FILENAME_PREFERENCES,Context.MODE_MULTI_PROCESS|Context.MODE_PRIVATE);}
  private SharedPreferences.Editor defaultEditor;//const
  public SharedPreferences.Editor getDefaultEditor(){return defaultEditor;}
  private SharedPreferences.Editor editor;//const
  public SharedPreferences.Editor getEditor(){return editor;}
  private RemoteStorage remoteStorage;
  public RemoteStorage getRemoteStorage(){return remoteStorage;}
  private boolean remoteStorageSuccessFragment=false;
  public boolean isRemoteStorageSuccessFragment(){return remoteStorageSuccessFragment;}
  public void setRemoteStorageSuccessFragment(boolean value){remoteStorageSuccessFragment=value;}
  private boolean remoteStorageErrorFragment=true;
  public boolean isRemoteStorageErrorFragment(){return remoteStorageErrorFragment;}
  public void setRemoteStorageErrorFragment(boolean value){remoteStorageErrorFragment=value;}

  private FragmentTransaction transaction;
  public FragmentTransaction getTransaction(){return transaction;}
  public FragmentTransaction getFragmentTransaction(){return transaction=((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();}
  private Fragment fragment=null;
  public Fragment getFragment(){return fragment;}
  private int fragmentType=T.FRAGMENT_UNKNOWN;
  public int getFragmentType(){return fragmentType;}
  private ArrayList<Integer> fragmentTypeList;
  public ArrayList<Integer> getFragmentTypeList(){return fragmentTypeList;}
  private ArrayList<Fragment> fragmentList;
  public ArrayList<Fragment> getFragmentList(){return fragmentList;}

  private Dialog splashDialog=null;//alternative for activity
  public Dialog getSplashDialog(){return splashDialog;}

  private TypedCallback onNullFragmentCallbackHandler=null;//call after remove last frag
  public void setOnNullFragmentCallbackHandler(TypedCallback callback_handler){onNullFragmentCallbackHandler=callback_handler;}
  private TypedCallback onAddFragmentCallbackHandler=null;//call before add frag
  public void setOnAddFragmentCallbackHandler(TypedCallback callback_handler){onAddFragmentCallbackHandler=callback_handler;}

  private int bookingStyle=DEFAULT_BOOKING_STYLE;//easy is default mode
  public int getBookingStyle(){return bookingStyle;}
  public void setBookingStyle(int style){bookingStyle=style;}
  private int mapMode=MAP_CLIENT;//client is default mode, driver mode starts after dialog
  public int getMapMode(){return mapMode;}
  public void setMapMode(int map_mode){mapMode=map_mode;}
  private boolean rideLater=false;
  public boolean isRideLater(){return rideLater;}
  public void setRideLater(boolean ride_later){rideLater=ride_later;}
  private int easyBooking=0;
  public int getEasyBooking(){return easyBooking;}
  public void setEasyBooking(int value){easyBooking=value;}

  private boolean tripMode=false;//where is driver takeOrder (is a tripMode needs to start driver mode)
  public boolean isTripMode(){return tripMode;}
  private boolean calcDistance=false;//where is delivering client
  public boolean isCalcDistance(){return calcDistance;}
  private boolean sensorActivated=false;
  public void setSensorActivated(boolean sensor_activated){sensorActivated=sensor_activated;}
  public boolean isSensorActivated(){return sensorActivated;}
  private boolean passwordRecovery=false;
  public void setPasswordRecovery(boolean password_recovery){passwordRecovery=password_recovery;}
  public boolean isPasswordRecovery(){return passwordRecovery;}

  //fab locations
  private float bookingX,bookingY;
  public float getBookingX(){return bookingX;}
  public float getBookingY(){return bookingY;}
  private float driverX,driverY;
  public float getDriverX(){return driverX;}
  public float getDriverY(){return driverY;}
  private float cartX,cartY;
  public float getCartX(){return cartX;}
  public float getCartY(){return cartY;}
  private float menuX,menuY;
  public float getMenuX(){return menuX;}
  public float getMenuY(){return menuY;}

  /*constructor*/
  public Manager(Context context){
    this.context=context;
    resources=context.getResources();
    defaultPreferences=PreferenceManager.getDefaultSharedPreferences(context);
    preferences=context.getSharedPreferences(T.FILENAME_PREFERENCES,Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    defaultEditor=defaultPreferences.edit();
    editor=preferences.edit();
    fragmentTypeList=new ArrayList<Integer>();
    fragmentList=new ArrayList<Fragment>();
  }
  /*clone*/
  public Manager clone() throws CloneNotSupportedException{
    return (Manager)super.clone();
  }
  /*
    defaultPreferences
    (doNotShow in preferences sync with key_notifications_show_tips in defaultPreferences)
    (?vibrate in preferences sync with key_notifications_vibrate in defaultPreferences) - notIn
  */

  public void prefSync(){
    boolean do_not_show=!defaultPreferences.getBoolean(context.getString(R.string.key_notifications_show_tips),true);
    putDoNotShow(do_not_show);
    int map_provider=Integer.parseInt(readMapProvider());
    putMapProvider(map_provider);
    int payment_provider=Integer.parseInt(readPaymentProvider());
    putPaymentProvider(payment_provider);
    String hostname=getHostname();
    if(!hostname.equalsIgnoreCase(serverName)){//need to clear data
      if(cartList!=null&&cartList.size()>0)clearCartDialog();
      putRegistered(false);
      putServerName(hostname);
      needToClearSensorAndDriverTransport=true;
      reloadDialog();
    }
    editor.commit();
    //vibrate (too ?)
  }

  public void putSharedPrefBooleanValue(String key,boolean value){
    defaultEditor.putBoolean(key,value);
    defaultEditor.commit();
  }
  public void putSharedPrefIntegerValue(String key,int value){
    defaultEditor.putInt(key,value);
    defaultEditor.commit();
  }
  public void putSharedPrefLongValue(String key,long value){
    defaultEditor.putLong(key,value);
    defaultEditor.commit();
  }
  public void putSharedPrefFloatValue(String key,float value){
    defaultEditor.putFloat(key,value);
    defaultEditor.commit();
  }

  public String getHostname(){
    return defaultPreferences.getString(context.getString(R.string.key_hostname),
                                        context.getString(R.string.pref_default_hostname));
  }
  public boolean isNewMessage(){
    return defaultPreferences.getBoolean(context.getString(R.string.key_notifications_new_message),true);
  }
  public boolean isVibrate(){
    return defaultPreferences.getBoolean(context.getString(R.string.key_notifications_new_message_vibrate),true);
  }
  public String getSound(){
    return defaultPreferences.getString(context.getString(R.string.key_notifications_new_message_ringtone),null);
  }
  public String getDistance(){
    return defaultPreferences.getString(context.getString(R.string.key_distance),null);
  }
  public String readMapProvider(){
    return defaultPreferences.getString(context.getString(R.string.key_map_provider),
           String.valueOf(Manager.DEFAULT_MAP_PROVIDER));
  }
  public String readPaymentProvider(){
    return defaultPreferences.getString(context.getString(R.string.key_payment_provider),
    String.valueOf(Manager.DEFAULT_PAYMENT_PROVIDER));
  }
  public String readApiKey(){
    return defaultPreferences.getString(context.getString(R.string.key_apikey),
    String.valueOf(Manager.DEFAULT_API_KEY));
  }
  public String getStyle(){
    return defaultPreferences.getString(context.getString(R.string.key_booking_style),
           String.valueOf(Manager.DEFAULT_BOOKING_STYLE));
  }
  public boolean isToolbar(){
    return defaultPreferences.getBoolean(context.getString(R.string.key_toolbar),!Manager.NO_TOOLBAR);
  }

  /*preferences*/
  private int countLoading;
  public int getCountLoading(){return countLoading;}
  private boolean registered;
  public boolean isRegistered(){return registered;}
  private boolean showLogo=true;//logo in status
  public boolean isShowLogo(){return showLogo;}
  private String serverName;
  public String getServerName(){return serverName;}
  private int language;//0-unknown 1-english 2-russian 3-ukrainian
  public int getLanguage(){return language;}
  private int mapProvider;//0-unknown 1-Google 2-OSM
  public void setMapProvider(int map_provider){mapProvider=map_provider;}
  public int getMapProvider(){return mapProvider;}
  private int paymentProvider;//0-google 1-stripe 2-paypal
  public void setPaymentProvider(int payment_provider){paymentProvider=payment_provider;}
  public int getPaymentProvider(){return paymentProvider;}
  private String latitude;
  public String getLatitude(){return latitude;}
  private String longitude;
  public String getLongitude(){return longitude;}
  private long landingDuration;//time to client's landing (for delivering order: currentTime-landingTime)
  public long getLandingDuration(){return landingDuration;}
  private long tripDuration;//time from client's landing (for completed order: currentTime-pickupTime)
  public long getTripDuration(){return tripDuration;}
  private long landingTime;//time from driver on pickup location
  public long getLandingTime(){return landingTime;}
  private long pickupTime;//time from pickup client
  public long getPickupTime(){return pickupTime;}
  private long tripDistance;//distance from pickup client
  public long getTripDistance(){return tripDistance;}

  private String orderLatitude=null;
  public String getOrderLatitude(){return orderLatitude;}
  private String orderLongitude=null;
  public String getOrderLongitude(){return orderLongitude;}

  private String findAddress=null;//finding(geocode address) for delivery
  public String getFindAddress(){return findAddress;}
  private String deliveryAddress;
  public String getDeliveryAddress(){return deliveryAddress;}
  private String deliveryLatitude;
  public String getDeliveryLatitude(){return deliveryLatitude;}
  private String deliveryLongitude;
  public String getDeliveryLongitude(){return deliveryLongitude;}

  private String origin=null;
  public String getOrigin(){return origin;}
  private String destination=null;
  public String getDestination(){return destination;}

  private String pickupName;
  public String getPickupName(){return pickupName;}
  private String pickupAddress;
  public String getPickupAddress(){return pickupAddress;}
  private String dropoffName;
  public String getDropoffName(){return dropoffName;}
  private String dropoffAddress;
  public String getDropoffAddress(){return dropoffAddress;}
  private long reservedTime;
  public long getReservedTime(){return reservedTime;}
  public void setReservedTime(long time){reservedTime=time;}

  private long userId;
  public long getUserId(){return userId;}
  private long sensorId;
  public long getSensorId(){return sensorId;}
  private long circleSensorId;
  public long getCircleSensorId(){return circleSensorId;}
  private long circleUserId;//circle doing by driver for client (to order.user_id)
  public long getCircleUserId(){return circleUserId;}
  private long trackId;
  public long getTrackId(){return trackId;}
  private long orderId;
  public long getOrderId(){return orderId;}
  private long orderStatusId;
  public long getOrderStatusId(){return orderStatusId;}
  private String orderStatusName;
  public String getOrderStatusName(){return orderStatusName;}
  private String orderStatusValue;
  public String getOrderStatusValue(){return orderStatusValue;}
  private long orderTime;
  public long getOrderTime(){return orderTime;}
  //trip is a took driver's order
  private long tripOrderId;
  public long getTripOrderId(){return tripOrderId;}
  private long tripOrderUserId;
  public long getTripOrderUserId(){return tripOrderUserId;}
  private long tripOrderStatusId;
  public long getTripOrderStatusId(){return tripOrderStatusId;}
  private String tripOrderStatusName;
  public String getTripOrderStatusName(){return tripOrderStatusName;}
  private String tripOrderStatusValue;
  public String getTripOrderStatusValue(){return tripOrderStatusValue;}
  private long tripOrderTime;
  public long getTripOrderTime(){return tripOrderTime;}

  private boolean doNotShow;
  public boolean isDoNotShow(){return doNotShow;}
  private String username;
  public String getUsername(){return username;}
  private int usertype;
  public int getUsertype(){return usertype;}
  private String callname;
  public String getCallname(){return callname;}
  private String firstname;
  public String getFirstname(){return firstname;}
  private String lastname;
  public String getLastname(){return lastname;}
  private String email;
  public String getEmail(){return email;}
  private String phone;
  public String getPhone(){return phone;}
  private String password;
  public String getPassword(){return password;}

  //orderTransport
  private long transportId;
  public long getTransportId(){return transportId;}
  private String transportName;
  public String getTransportName(){return transportName;}
  private String transportColor;
  public String getTransportColor(){return transportColor;}
  private String transportLicensePlate;
  public String getTransportLicensePlate(){return transportLicensePlate;}
  private String transportRate;
  public String getTransportRate(){return transportRate;}
  private long transportSensorId;
  public long getTransportSensorId(){return transportSensorId;}
  private String transportSensorName;
  public String getTransportSensorName(){return transportSensorName;}
  private String transportSensorPhone;
  public String getTransportSensorPhone(){return transportSensorPhone;}
  private long transportSensorUserId;
  public long getTransportSensorUserId(){return transportSensorUserId;}
  private String transportSensorUserCallname;
  public String getTransportSensorUserCallname(){return transportSensorUserCallname;}
  private String transportSensorUserPhone;
  public String getTransportSensorUserPhone(){return transportSensorUserPhone;}

  //driverTransport
  private long driverTransportId;
  public long getDriverTransportId(){return driverTransportId;}
  private String driverTransportName;
  public String getDriverTransportName(){return driverTransportName;}
  private String driverTransportColor;
  public String getDriverTransportColor(){return driverTransportColor;}
  private String driverTransportLicensePlate;
  public String getDriverTransportLicensePlate(){return driverTransportLicensePlate;}
  private String driverTransportRate;
  public String getDriverTransportRate(){return driverTransportRate;}

  private String currentTimezoneOffset=T.getCurrentTimezoneOffset();
  public String getCurrentTimezoneOffset(){return currentTimezoneOffset;}
  private int timezoneOffset=T.getTimezoneOffset();//in minutes
  public int getTimezoneOffset(){return timezoneOffset;}

  /*storage dataList*/
  private Stat stat=null;
  public Stat getStat(){return stat;}
  private ArrayList<User> userList=null;
  public ArrayList<User> getUserList(){return userList;}
  public ArrayList<User> newInstanceUserList(){return userList=new ArrayList();}
  private ArrayList<Currency> currencyList=null;
  public ArrayList<Currency> getCurrencyList(){return currencyList;}
  public ArrayList<Currency> newInstanceCurrencyList(){return currencyList=new ArrayList();}
  private ArrayList<Color> colorList=null;
  public ArrayList<Color> newInstanceColorList(){return colorList=new ArrayList();}
  public ArrayList<Color> getColorList(){return colorList;}
  private ArrayList<Product> productList=null;
  public ArrayList<Product> getProductList(){return productList;}
  public ArrayList<Product> newInstanceProductList(){return productList=new ArrayList();}
  private ArrayList<OrderAB> orderABList=null;
  public ArrayList<OrderAB> getOrderABList(){return orderABList;}
  public ArrayList<OrderAB> newInstanceOrderABList(){return orderABList=new ArrayList();}
  public ArrayList<OrderABPart> newInstanceOrderABPartList(OrderAB order){return order.orderABPartList=new ArrayList();}
  private ArrayList<OrderAB> orderABTransportList=null;
  public ArrayList<OrderAB> getOrderABTransportList(){return orderABTransportList;}
  public ArrayList<OrderAB> newInstanceOrderABTransportList(){return orderABTransportList=new ArrayList();}
  private ArrayList<Tax> taxList=null;
  public ArrayList<Tax> getTaxList(){return taxList;}
  public ArrayList<Tax> newInstanceTaxList(){return taxList=new ArrayList();}
  private ArrayList<Product> messageList=null;
  public ArrayList<Product> getMessageList(){return messageList;}
  public ArrayList<Product> newInstanceMessageList(){return messageList=new ArrayList();}

  //cart is for orders in delivery booking style
  private ArrayList<Product> cartList=null;
  public ArrayList<Product> getCartList(){return cartList;}
  public ArrayList<Product> newInstanceCartList(){return cartList=new ArrayList();}

  private String token=null;
  public String getToken(){return token;}
  public void setToken(String token){this.token=token;}

  public static final String DRIVER_FEE_NAME="driver fee";
  public static final String CLIENT_FEE_NAME="client fee";
  private Tax clientFee=null;
  public Tax getClientFee(){return clientFee;}
  private Tax driverFee=null;
  public Tax getDriverFee(){return driverFee;}
  private String clientFeeTitle=null;
  public String getClientFeeTitle(){return clientFeeTitle;}
  private String driverFeeTitle=null;
  public String getDriverFeeTitle(){return driverFeeTitle;}

  public void getPrefData(){
    countLoading=preferences.getInt(T.SETTINGS_COUNT_LOADING,0);
    serverName=preferences.getString(T.SETTINGS_SERVER_NAME,context.getString(R.string.pref_default_hostname));
    language=preferences.getInt(T.SETTINGS_LANGUAGE,T.SETTINGS_LANGUAGE_UNKNOWN);
    mapProvider=preferences.getInt(T.SETTINGS_MAP_PROVIDER,DEFAULT_MAP_PROVIDER);
    paymentProvider=preferences.getInt(T.SETTINGS_PAYMENT_PROVIDER,DEFAULT_PAYMENT_PROVIDER);
    registered=preferences.getBoolean(T.SETTINGS_REGISTERED,false);
    showLogo=preferences.getBoolean(T.SETTINGS_SHOW_LOGO,true);
    tripMode=preferences.getBoolean(T.SETTINGS_TRIP_MODE,false);
    token=preferences.getString(T.SETTINGS_TOKEN,null);

    orderLatitude=preferences.getString(T.SETTINGS_ORDER_LATITUDE,null);
    orderLongitude=preferences.getString(T.SETTINGS_ORDER_LONGITUDE,null);

    findAddress=preferences.getString(T.SETTINGS_FIND_ADDRESS,null);
    deliveryAddress=preferences.getString(T.SETTINGS_DELIVERY_ADDRESS,null);
    deliveryLatitude=preferences.getString(T.SETTINGS_DELIVERY_LATITUDE,null);
    deliveryLongitude=preferences.getString(T.SETTINGS_DELIVERY_LONGITUDE,null);

    origin=preferences.getString(T.SETTINGS_ORIGIN,null);
    destination=preferences.getString(T.SETTINGS_DESTINATION,null);

    pickupName=preferences.getString(T.SETTINGS_PICKUP_NAME,null);
    pickupAddress=preferences.getString(T.SETTINGS_PICKUP_ADDRESS,null);
    dropoffName=preferences.getString(T.SETTINGS_DROPOFF_NAME,null);
    dropoffAddress=preferences.getString(T.SETTINGS_DROPOFF_ADDRESS,null);

    username=preferences.getString(T.SETTINGS_USERNAME,null);
    usertype=preferences.getInt(T.SETTINGS_USERTYPE,-1);
    callname=preferences.getString(T.SETTINGS_CALLNAME,null);
    firstname=preferences.getString(T.SETTINGS_FIRSTNAME,null);
    lastname=preferences.getString(T.SETTINGS_LASTNAME,null);
    email=preferences.getString(T.SETTINGS_EMAIL,null);
    phone=preferences.getString(T.SETTINGS_PHONE,null);
    password=preferences.getString(T.SETTINGS_PASSWORD,null);

    userId=preferences.getLong(T.SETTINGS_USER_ID,-1);
    sensorId=preferences.getLong(T.SETTINGS_SENSOR_ID,-1);
    circleSensorId=preferences.getLong(T.SETTINGS_CIRCLE_SENSOR_ID,-1);
    circleUserId=preferences.getLong(T.SETTINGS_CIRCLE_USER_ID,-1);
    trackId=preferences.getLong(T.SETTINGS_TRACK_ID,-1);
    orderId=preferences.getLong(T.SETTINGS_ORDER_ID,-1);
    orderStatusId=preferences.getLong(T.SETTINGS_ORDER_STATUS_ID,0);//0-no status
    orderStatusName=preferences.getString(T.SETTINGS_ORDER_STATUS_NAME,null);
    orderStatusValue=preferences.getString(T.SETTINGS_ORDER_STATUS_VALUE,null);
    orderTime=preferences.getLong(T.SETTINGS_ORDER_TIME,0);
    tripOrderId=preferences.getLong(T.SETTINGS_TRIP_ORDER_ID,-1);
    tripOrderUserId=preferences.getLong(T.SETTINGS_TRIP_ORDER_USER_ID,-1);
    tripOrderStatusId=preferences.getLong(T.SETTINGS_TRIP_ORDER_STATUS_ID,0);//0-no status
    tripOrderStatusName=preferences.getString(T.SETTINGS_TRIP_ORDER_STATUS_NAME,null);
    tripOrderStatusValue=preferences.getString(T.SETTINGS_TRIP_ORDER_STATUS_VALUE,null);
    tripOrderTime=preferences.getLong(T.SETTINGS_TRIP_ORDER_TIME,0);

    doNotShow=preferences.getBoolean(T.SETTINGS_DO_NOT_SHOW,true);
    latitude=preferences.getString(T.SETTINGS_LATITUDE,null);
    longitude=preferences.getString(T.SETTINGS_LONGITUDE,null);
    landingDuration=preferences.getLong(T.SETTINGS_LANDING_DURATION,0);
    tripDuration=preferences.getLong(T.SETTINGS_TRIP_DURATION,0);
    landingTime=preferences.getLong(T.SETTINGS_LANDING_TIME,0);
    pickupTime=preferences.getLong(T.SETTINGS_PICKUP_TIME,0);
    tripDistance=preferences.getLong(T.SETTINGS_TRIP_DISTANCE,0);

    transportId=preferences.getLong(T.SETTINGS_TRANSPORT_ID,-1);
    transportName=preferences.getString(T.SETTINGS_TRANSPORT_NAME,null);
    transportColor=preferences.getString(T.SETTINGS_TRANSPORT_COLOR,null);
    transportLicensePlate=preferences.getString(T.SETTINGS_TRANSPORT_LICENSE_PLATE,null);
    transportRate=preferences.getString(T.SETTINGS_TRANSPORT_RATE,null);
    transportSensorId=preferences.getLong(T.SETTINGS_TRANSPORT_SENSOR_ID,-1);
    transportSensorName=preferences.getString(T.SETTINGS_TRANSPORT_SENSOR_NAME,null);
    transportSensorPhone=preferences.getString(T.SETTINGS_TRANSPORT_SENSOR_PHONE,null);
    transportSensorUserId=preferences.getLong(T.SETTINGS_TRANSPORT_SENSOR_USER_ID,-1);
    transportSensorUserCallname=preferences.getString(T.SETTINGS_TRANSPORT_SENSOR_USER_CALLNAME,null);
    transportSensorUserPhone=preferences.getString(T.SETTINGS_TRANSPORT_SENSOR_USER_PHONE,null);

    driverTransportId=preferences.getLong(T.SETTINGS_DRIVER_TRANSPORT_ID,-1);
    driverTransportName=preferences.getString(T.SETTINGS_DRIVER_TRANSPORT_NAME,null);
    driverTransportColor=preferences.getString(T.SETTINGS_DRIVER_TRANSPORT_COLOR,null);
    driverTransportLicensePlate=preferences.getString(T.SETTINGS_DRIVER_TRANSPORT_LICENSE_PLATE,null);
    driverTransportRate=preferences.getString(T.SETTINGS_DRIVER_TRANSPORT_RATE,null);

    //fab locations
    bookingX=preferences.getFloat(T.SETTINGS_BOOKING_X,0);
    bookingY=preferences.getFloat(T.SETTINGS_BOOKING_Y,0);
    driverX=preferences.getFloat(T.SETTINGS_DRIVER_X,0);
    driverY=preferences.getFloat(T.SETTINGS_DRIVER_Y,0);
    cartX=preferences.getFloat(T.SETTINGS_CART_X,0);
    cartY=preferences.getFloat(T.SETTINGS_CART_Y,0);
    menuX=preferences.getFloat(T.SETTINGS_MENU_X,0);
    menuY=preferences.getFloat(T.SETTINGS_MENU_Y,0);
  }
  public void seekPrefData(){//re-read pref_file and param (write in first thread, read in second)
    //PreferenceManager.getDefaultSharedPreferences(context);
    preferences=context.getSharedPreferences(T.FILENAME_PREFERENCES,Context.MODE_MULTI_PROCESS|Context.MODE_PRIVATE);
    getPrefData();
  }
  public void clearSensor(){
    //circleSensor is a transport sensor of order(order.transport.sensor_id)
    if(circleSensorId!=-1)removeSensorCircleRequest(circleSensorId);
    putSensorId(-1);
    editor.commit();
  }
  public void clearOrder(){
    putOrderId(-1);
    putOrderTime(0);
    putOrderStatusId(0);
    putOrderStatusName(null);
    putOrderStatusValue(null);
    editor.commit();
  }
  public void clearTripOrder(){
    //circleUser is a user of order(order.user_id)
    if(circleUserId!=-1)removeSensorCircleToUserRequest(circleUserId);
    putTripOrderId(-1);
    putTripOrderUserId(-1);
    putTripOrderTime(0);
    putTripOrderStatusId(0);
    putTripOrderStatusName(null);
    putTripOrderStatusValue(null);
    editor.commit();
  }
  public void clearPickup(){
    putPickupName(null);
    putPickupAddress(null);
    putOrderLatitude(null);
    putOrderLongitude(null);
    editor.commit();
  }
  public void clearDropoff(){
    putDropoffName(null);
    putDropoffAddress(null);
    putDeliveryLatitude(null);
    putDeliveryLongitude(null);
    editor.commit();
  }
  public void clearPickupAndDropoff(){
    clearPickup();clearDropoff();
  }
  public void clearLandingAndPickup(){
    putTripDistance(0);
    putLandingDuration(0);
    putTripDuration(0);
    putLandingTime(0);
    putPickupTime(0);
    editor.commit();
  }
  public void clearTransport(){
    putTransportId(-1);
    putTransportName(null);
    putTransportColor(null);
    putTransportLicensePlate(null);
    putTransportRate(null);
    editor.commit();
  }
  public void clearTransportSensor(){
    //circleSensor is a transport sensor of order(order.transport.sensor_id)
    if(circleSensorId!=-1)removeSensorCircleRequest(circleSensorId);
    putTransportSensorId(-1);
    putTransportSensorName(null);
    putTransportSensorPhone(null);
    putTransportSensorUserId(-1);
    putTransportSensorUserCallname(null);
    putTransportSensorUserPhone(null);
    editor.commit();
  }
  public void clearDriverTransport(){
    putDriverTransportId(-1);
    putDriverTransportName(null);
    putDriverTransportColor(null);
    putDriverTransportLicensePlate(null);
    putDriverTransportRate(null);
    editor.commit();
  }
  public void putLanguage(int language){this.language=language;editor.putInt(T.SETTINGS_LANGUAGE,language);}
  public void putMapProvider(int map_provider){mapProvider=map_provider;editor.putInt(T.SETTINGS_MAP_PROVIDER,mapProvider);}
  public void putPaymentProvider(int payment_provider){paymentProvider=payment_provider;editor.putInt(T.SETTINGS_PAYMENT_PROVIDER,paymentProvider);}
  public void putServerName(String server_name){this.serverName=server_name;editor.putString(T.SETTINGS_SERVER_NAME,serverName);}
  public void putRegistered(boolean registered){this.registered=registered;editor.putBoolean(T.SETTINGS_REGISTERED,registered);}
  public void putShowLogo(boolean show_logo){showLogo=show_logo;editor.putBoolean(T.SETTINGS_SHOW_LOGO,showLogo);}
  public void putTripMode(boolean trip_mode){tripMode=trip_mode;editor.putBoolean(T.SETTINGS_TRIP_MODE,tripMode);}
  public void putToken(String token){this.token=token;editor.putString(T.SETTINGS_TOKEN,token);}
  public void putOrderLatitude(String order_latitude){orderLatitude=order_latitude;editor.putString(T.SETTINGS_ORDER_LATITUDE,order_latitude);}
  public void putOrderLongitude(String order_longitude){orderLongitude=order_longitude;editor.putString(T.SETTINGS_ORDER_LONGITUDE,order_longitude);}
  public void putFindAddress(String find_address){findAddress=find_address;editor.putString(T.SETTINGS_FIND_ADDRESS,find_address);}
  public void putDeliveryAddress(String delivery_address){deliveryAddress=delivery_address;editor.putString(T.SETTINGS_DELIVERY_ADDRESS,delivery_address);}
  public void putDeliveryLatitude(String delivery_latitude){deliveryLatitude=delivery_latitude;editor.putString(T.SETTINGS_DELIVERY_LATITUDE,delivery_latitude);}
  public void putDeliveryLongitude(String delivery_longitude){deliveryLongitude=delivery_longitude;editor.putString(T.SETTINGS_DELIVERY_LONGITUDE,delivery_longitude);}
  public void putOrigin(String origin){this.origin=origin;editor.putString(T.SETTINGS_ORIGIN,origin);}
  public void putDestination(String destination){this.destination=destination;editor.putString(T.SETTINGS_DESTINATION,destination);}
  public void putPickupName(String pickup_name){pickupName=pickup_name;editor.putString(T.SETTINGS_PICKUP_NAME,pickup_name);}
  public void putPickupAddress(String pickup_address){pickupAddress=pickup_address;editor.putString(T.SETTINGS_PICKUP_ADDRESS,pickup_address);}
  public void putDropoffName(String dropoff_name){dropoffName=dropoff_name;editor.putString(T.SETTINGS_DROPOFF_NAME,dropoff_name);}
  public void putDropoffAddress(String dropoff_address){dropoffAddress=dropoff_address;editor.putString(T.SETTINGS_DROPOFF_ADDRESS,dropoff_address);}
  public void putUsername(String username){this.username=username;editor.putString(T.SETTINGS_USERNAME,username);}
  public void putUsertype(int usertype){this.usertype=usertype;editor.putInt(T.SETTINGS_USERTYPE,usertype);}
  public void putCallname(String callname){this.callname=callname;editor.putString(T.SETTINGS_CALLNAME,callname);}
  public void putFirstname(String firstname){this.firstname=firstname;editor.putString(T.SETTINGS_FIRSTNAME,firstname);}
  public void putLastname(String lastname){this.lastname=lastname;editor.putString(T.SETTINGS_LASTNAME,lastname);}
  public void putEmail(String email){this.email=email;editor.putString(T.SETTINGS_EMAIL,email);}
  public void putPhone(String phone){this.phone=phone;editor.putString(T.SETTINGS_PHONE,phone);}
  public void putPassword(String password){this.password=password;editor.putString(T.SETTINGS_PASSWORD,password);}
  public void putLatitude(String latitude){this.latitude=latitude;editor.putString(T.SETTINGS_LATITUDE,latitude);}
  public void putLongitude(String longitude){this.longitude=longitude;editor.putString(T.SETTINGS_LONGITUDE,longitude);}
  public void putLandingDuration(long landing_duration){landingDuration=landing_duration;editor.putLong(T.SETTINGS_LANDING_DURATION,landingDuration);}
  public void putTripDuration(long trip_duration){tripDuration=trip_duration;editor.putLong(T.SETTINGS_TRIP_DURATION,tripDuration);}
  public void putLandingTime(long landing_time){landingTime=landing_time;editor.putLong(T.SETTINGS_LANDING_TIME,landingTime);}
  public void putPickupTime(long pickup_time){pickupTime=pickup_time;editor.putLong(T.SETTINGS_PICKUP_TIME,pickupTime);}
  public void putTripDistance(long trip_distance){tripDistance=trip_distance;editor.putLong(T.SETTINGS_TRIP_DISTANCE,tripDistance);}
  public void putUserId(long user_id){userId=user_id;editor.putLong(T.SETTINGS_USER_ID,userId);}
  public void putSensorId(long sensor_id){sensorId=sensor_id;editor.putLong(T.SETTINGS_SENSOR_ID,sensorId);}
  public void putCircleSensorId(long circle_sensor_id){circleSensorId=circle_sensor_id;editor.putLong(T.SETTINGS_CIRCLE_SENSOR_ID,circleSensorId);}
  public void putCircleUserId(long circle_user_id){circleUserId=circle_user_id;editor.putLong(T.SETTINGS_CIRCLE_USER_ID,circleUserId);}
  public void putTrackId(long track_id){trackId=track_id;editor.putLong(T.SETTINGS_TRACK_ID,trackId);}
  public void putOrderId(long order_id){orderId=order_id;editor.putLong(T.SETTINGS_ORDER_ID,orderId);}
  public void putOrderStatusId(long order_status_id){orderStatusId=order_status_id;editor.putLong(T.SETTINGS_ORDER_STATUS_ID,orderStatusId);}
  public void putOrderStatusName(String order_status_name){orderStatusName=order_status_name;editor.putString(T.SETTINGS_ORDER_STATUS_NAME,orderStatusName);}
  public void putOrderStatusValue(String order_status_value){orderStatusValue=order_status_value;editor.putString(T.SETTINGS_ORDER_STATUS_VALUE,orderStatusValue);}
  public void putOrderTime(long order_time){orderTime=order_time;editor.putLong(T.SETTINGS_ORDER_TIME,orderTime);}
  public void putTripOrderId(long trip_order_id){tripOrderId=trip_order_id;editor.putLong(T.SETTINGS_TRIP_ORDER_ID,tripOrderId);}
  public void putTripOrderUserId(long order_user_id){tripOrderUserId=order_user_id;editor.putLong(T.SETTINGS_TRIP_ORDER_USER_ID,tripOrderUserId);}
  public void putTripOrderStatusId(long order_status_id){tripOrderStatusId=order_status_id;editor.putLong(T.SETTINGS_TRIP_ORDER_STATUS_ID,tripOrderStatusId);}
  public void putTripOrderStatusName(String order_status_name){tripOrderStatusName=order_status_name;editor.putString(T.SETTINGS_TRIP_ORDER_STATUS_NAME,tripOrderStatusName);}
  public void putTripOrderStatusValue(String order_status_value){tripOrderStatusValue=order_status_value;editor.putString(T.SETTINGS_TRIP_ORDER_STATUS_VALUE,tripOrderStatusValue);}
  public void putTripOrderTime(long order_time){tripOrderTime=order_time;editor.putLong(T.SETTINGS_TRIP_ORDER_TIME,tripOrderTime);}

  public void putCountLoading(int count_loading){editor.putInt(T.SETTINGS_COUNT_LOADING,count_loading);}
  public void putDoNotShow(boolean do_not_show){doNotShow=do_not_show;editor.putBoolean(T.SETTINGS_DO_NOT_SHOW,doNotShow);}
  public boolean getDoNotShow(){return doNotShow=preferences.getBoolean(T.SETTINGS_DO_NOT_SHOW,true);}

  public void putTransportId(long transport_id){transportId=transport_id;editor.putLong(T.SETTINGS_TRANSPORT_ID,transportId);}
  public void putTransportName(String transport_name){transportName=transport_name;editor.putString(T.SETTINGS_TRANSPORT_NAME,transportName);}
  public void putTransportColor(String transport_color){transportColor=transport_color;editor.putString(T.SETTINGS_TRANSPORT_COLOR,transportColor);}
  public void putTransportLicensePlate(String transport_license_plate){transportLicensePlate=transport_license_plate;editor.putString(T.SETTINGS_TRANSPORT_LICENSE_PLATE,transportLicensePlate);}
  public void putTransportRate(String transport_rate){transportRate=transport_rate;editor.putString(T.SETTINGS_TRANSPORT_RATE,transportRate);}
  public void putTransportSensorId(long transport_sensor_id){transportSensorId=transport_sensor_id;editor.putLong(T.SETTINGS_TRANSPORT_SENSOR_ID,transportSensorId);}
  public void putTransportSensorName(String transport_sensor_name){transportSensorName=transport_sensor_name;editor.putString(T.SETTINGS_TRANSPORT_SENSOR_NAME,transportSensorName);}
  public void putTransportSensorPhone(String transport_sensor_phone){transportSensorPhone=transport_sensor_phone;editor.putString(T.SETTINGS_TRANSPORT_SENSOR_PHONE,transportSensorPhone);}
  public void putTransportSensorUserId(long transport_sensor_user_id){transportSensorUserId=transport_sensor_user_id;editor.putLong(T.SETTINGS_TRANSPORT_SENSOR_USER_ID,transportSensorUserId);}
  public void putTransportSensorUserCallname(String transport_sensor_user_callname){transportSensorUserCallname=transport_sensor_user_callname;editor.putString(T.SETTINGS_TRANSPORT_SENSOR_USER_CALLNAME,transportSensorUserCallname);}
  public void putTransportSensorUserPhone(String transport_sensor_user_phone){transportSensorUserPhone=transport_sensor_user_phone;editor.putString(T.SETTINGS_TRANSPORT_SENSOR_USER_PHONE,transportSensorUserPhone);}

  public void putDriverTransportId(long transport_id){driverTransportId=transport_id;editor.putLong(T.SETTINGS_DRIVER_TRANSPORT_ID,driverTransportId);}
  public void putDriverTransportName(String transport_name){driverTransportName=transport_name;editor.putString(T.SETTINGS_DRIVER_TRANSPORT_NAME,driverTransportName);}
  public void putDriverTransportColor(String transport_color){driverTransportColor=transport_color;editor.putString(T.SETTINGS_DRIVER_TRANSPORT_COLOR,driverTransportColor);}
  public void putDriverTransportLicensePlate(String transport_license_plate){driverTransportLicensePlate=transport_license_plate;editor.putString(T.SETTINGS_DRIVER_TRANSPORT_LICENSE_PLATE,driverTransportLicensePlate);}
  public void putDriverTransportRate(String transport_rate){driverTransportRate=transport_rate;editor.putString(T.SETTINGS_DRIVER_TRANSPORT_RATE,driverTransportRate);}

  //fab locations
  public void putBookingX(float booking_x){bookingX=booking_x;editor.putFloat(T.SETTINGS_BOOKING_X,bookingX);}
  public void putBookingY(float booking_y){bookingY=booking_y;editor.putFloat(T.SETTINGS_BOOKING_Y,bookingY);}
  public void putDriverX(float driver_x){driverX=driver_x;editor.putFloat(T.SETTINGS_DRIVER_X,driverX);}
  public void putDriverY(float driver_y){driverY=driver_y;editor.putFloat(T.SETTINGS_DRIVER_Y,driverY);}
  public void putCartX(float cart_x){cartX=cart_x;editor.putFloat(T.SETTINGS_CART_X,cartX);}
  public void putCartY(float cart_y){cartY=cart_y;editor.putFloat(T.SETTINGS_CART_Y,cartY);}
  public void putMenuX(float menu_x){menuX=menu_x;editor.putFloat(T.SETTINGS_MENU_X,menuX);}
  public void putMenuY(float menu_y){menuY=menu_y;editor.putFloat(T.SETTINGS_MENU_Y,menuY);}

  /*api_key*/
  public String getApiKey(){
    String ret_val=null;
    String api_key=context.getString(R.string.fireart_key);//fireart API key(from xml file)
    if(api_key!=null&&api_key.length()>0&&!api_key.equalsIgnoreCase(DEFAULT_API_KEY)){
      ret_val=api_key;
      return ret_val;
    }
    api_key=readApiKey();//fireart API key(from default pref)
    if(api_key!=null&&api_key.length()>0&&!api_key.equalsIgnoreCase(DEFAULT_API_KEY)){
      ret_val=api_key;
    }
    return ret_val;
  }
  public String getPictureUrl(String object_name){
    String url;
    String api_key=getApiKey();
    if(api_key!=null)url=getHostname()+String.format(RemoteStorage.URL_GET_IMAGE_WITH_APIKEY,api_key,object_name);
    else url=getHostname()+String.format(RemoteStorage.URL_GET_IMAGE,object_name);
    /* with auth
    if(api_key!=null)url=getHostname()+String.format(RemoteStorage.URL_GET_IMAGE_WITH_AUTH_AND_APIKEY,api_key,username,password,object_name);
    else url=getHostname()+String.format(RemoteStorage.URL_GET_IMAGE_WITH_AUTH,object_name);
    */
    return url;
  }

  /*app*/
  private boolean needToExit=false;
  public boolean isNeedToExit(){return needToExit;}
  public void setNeedToExit(boolean need_to_exit){needToExit=need_to_exit;}
  private boolean needToClearSensorAndDriverTransport=false;
  public boolean isNeedToClearSensorAndDriverTransport(){return needToClearSensorAndDriverTransport;}

  /*internet*/
  private boolean needDisableMobile=false;
  public boolean isNeedDisableMobile(){return needDisableMobile;}
  public boolean isInternetOn(){
    /*WifiManager Wifi=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    boolean wifi_enabled=Wifi.isWifiEnabled();
    if(wifi_enabled)return true;*/
    final ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if(manager!=null){
      NetworkInfo mobile=manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
      NetworkInfo wifi=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
      if((mobile!=null&&mobile.isConnected())||(wifi!=null&&wifi.isConnected()))return true;
    }
    return false;
  }
  public void internetOn(){
    boolean wifi_enabled,wifi_connected=false;
    needDisableMobile=false;
    WifiManager Wifi=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    wifi_enabled=Wifi.isWifiEnabled();
    if(wifi_enabled){
      final ConnectivityManager manager1=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
      if(manager1!=null){
        NetworkInfo wifi=manager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifi!=null&&wifi.isConnected())wifi_connected=true;
      }
    }
    if(!wifi_connected){
      final ConnectivityManager manager2=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
      if(manager2!=null){
        NetworkInfo mobile=manager2.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(mobile==null||!mobile.isConnected()){
          try{
            Method setMobileDataEnabledMethod=ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled",Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(manager2,true);
          }catch(Exception e){}
          needDisableMobile=true;
        }
      }
    }
  }
  public void internetOff(){
    final ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if(manager!=null){
      try{
        Method setMobileDataEnabledMethod=ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled",Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);
        setMobileDataEnabledMethod.invoke(manager,false);
      }catch(Exception e){}
    }
  }

  /*fragmentControl*/
  public void showFragment(String name){
    Fragment frag=getFragment(name);
    if(frag!=null){
      FragmentTransaction transaction=getFragmentTransaction();
      transaction.show(frag);
      transaction.commit();
    }
  }
  public void hideFragment(String name){
    Fragment frag=getFragment(name);
    if(frag!=null){
      FragmentTransaction transaction=getFragmentTransaction();
      transaction.hide(frag);
      transaction.commit();
    }
  }
  public void hideAllFragment(){
    Fragment frag;
    FragmentTransaction transaction=getFragmentTransaction();
    Iterator i=fragmentList.iterator();
    while(i.hasNext()){
      frag=(Fragment)i.next();
      if(frag!=null)transaction.hide(frag);
    }
    transaction.commit();
  }
  public void showAllFragment(){
    Fragment frag;
    FragmentTransaction transaction=getFragmentTransaction();
    Iterator i=fragmentList.iterator();
    while(i.hasNext()){
      frag=(Fragment)i.next();
      if(frag!=null)transaction.show(frag);
    }
    transaction.commit();
  }
  public void showServiceFragment(){
    Fragment frag;
    FragmentTransaction transaction=getFragmentTransaction();
    Iterator i=fragmentList.iterator();
    while(i.hasNext()){
      frag=(Fragment)i.next();
      if(frag!=null&&
        (frag.getTag().equals(T.FRAGMENT_NAME_ERROR)||
         frag.getTag().equals(T.FRAGMENT_NAME_INFO)||
         frag.getTag().equals(T.FRAGMENT_NAME_SUCCESS)||
         frag.getTag().equals(T.FRAGMENT_NAME_IMAGE)))transaction.show(frag);
    }
    transaction.commit();
  }
  public boolean isServiceFragmentBeforeMapFragment(){
    boolean ret_val=false;
    Fragment frag;
    Iterator i=fragmentList.iterator();
    String tag;
    while(i.hasNext()){
      frag=(Fragment)i.next();
      if(frag!=null){
        tag=frag.getTag();
        if(tag.equals(T.FRAGMENT_NAME_MAP))break;
        else if(tag.equals(T.FRAGMENT_NAME_ERROR)||tag.equals(T.FRAGMENT_NAME_INFO)||
                tag.equals(T.FRAGMENT_NAME_SUCCESS)||tag.equals(T.FRAGMENT_NAME_IMAGE)){ret_val=true;break;}
      }
    }
    return ret_val;
  }
  public void addFragment(int view_id,FragmentTransaction transaction,Fragment fragment,String name,Integer type,ArrayList<Integer> type_list,ArrayList<Fragment> list){
    if(onAddFragmentCallbackHandler!=null)onAddFragmentCallbackHandler.execute(null);
    T.addFragment(view_id,transaction,fragment,name,type,type_list,list);
  }
  public void addFragment(Fragment fragment,String name,Integer type){
    transaction=getFragmentTransaction();
    T.addFragment(R.id.fragment,transaction,fragment,name,type,fragmentTypeList,fragmentList);
  }
  public void removeCurrFragment(){
    removeFragment(fragment);
  }
  public void removeAllFragment(){
    ArrayList<Fragment> list=(ArrayList<Fragment>)fragmentList.clone();
    for(Fragment item:list)removeFragment(item);
  }
  public void removeAllWithoutMapFragment(){
    ArrayList<Fragment> list=(ArrayList<Fragment>)fragmentList.clone();
    for(Fragment item:list){
      if(!item.getTag().equals(T.FRAGMENT_NAME_MAP))removeFragment(item);
    }
  }

  public void removeFragment(String name){
    Fragment remove_frag=((AppCompatActivity)context).getSupportFragmentManager().findFragmentByTag(name);
    removeFragment(remove_frag);
  }
  public void removeServiceFragment(){
    Fragment remove_frag=null;
    remove_frag=(remove_frag==null?((AppCompatActivity)context).getSupportFragmentManager().findFragmentByTag(T.FRAGMENT_NAME_ERROR):remove_frag);
    remove_frag=(remove_frag==null?((AppCompatActivity)context).getSupportFragmentManager().findFragmentByTag(T.FRAGMENT_NAME_SUCCESS):remove_frag);
    remove_frag=(remove_frag==null?((AppCompatActivity)context).getSupportFragmentManager().findFragmentByTag(T.FRAGMENT_NAME_INFO):remove_frag);
    remove_frag=(remove_frag==null?((AppCompatActivity)context).getSupportFragmentManager().findFragmentByTag(T.FRAGMENT_NAME_IMAGE):remove_frag);
    removeFragment(remove_frag);
  }
  public void removeListFragment(){
    removeFragment(T.FRAGMENT_NAME_ORDER_LIST);
    removeFragment(T.FRAGMENT_NAME_PRODUCT_LIST);
    removeFragment(T.FRAGMENT_NAME_CART_LIST);
    removeFragment(T.FRAGMENT_NAME_MESSAGE_LIST);
  }
  public void removeFragment(Fragment remove_frag){
    if(remove_frag!=null){
      transaction=getFragmentTransaction();
      T.fragment frag=T.removeFragment(transaction,remove_frag,fragmentTypeList,fragmentList);
      if(frag!=null&&frag.type!=T.FRAGMENT_UNKNOWN){
        fragment=frag.fragment;
        fragmentType=frag.type;
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"current="+frag.name,Toast.LENGTH_SHORT).show();
      }
      else{
        fragment=null;fragmentType=T.FRAGMENT_UNKNOWN;
        if(onNullFragmentCallbackHandler!=null)onNullFragmentCallbackHandler.execute(null);
      }
    }
  }
  public Fragment getFragment(String name){
    return ((AppCompatActivity)context).getSupportFragmentManager().findFragmentByTag(name);
  }
  public boolean isOpenFragment(String name){
    Fragment frag=getFragment(name);
    if(frag!=null){
      return true;
    }
    return false;
  }
  public boolean isOpenFragment(int type){
    Iterator iter=fragmentTypeList.iterator();
    while(iter.hasNext()){
      if(((Integer)iter.next()).intValue()==type)return true;
    }
    return false;
  }
  public void restoreBackstack(ArrayList<Integer> fragment_type_list,ArrayList<Fragment> fragment_list){
    fragmentTypeList=fragment_type_list;
    fragmentList=fragment_list;
    transaction=((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
    Integer type=T.FRAGMENT_UNKNOWN;
    Iterator type_iterator=fragmentTypeList.iterator(),iterator=fragmentList.iterator();
    while(type_iterator.hasNext()&&iterator.hasNext()){
      type=(Integer)type_iterator.next();
      fragment=(Fragment)iterator.next();
      switch(type){
        case T.FRAGMENT_MAP:
          transaction.add(fragment,T.FRAGMENT_NAME_MAP);
          break;
        case T.FRAGMENT_STAT:
          transaction.add(fragment,T.FRAGMENT_NAME_STAT);
          break;
        case T.FRAGMENT_START:
          transaction.add(fragment,T.FRAGMENT_NAME_START);
          break;
        case T.FRAGMENT_REGISTER:
          transaction.add(fragment,T.FRAGMENT_NAME_REGISTER);
          break;
        case T.FRAGMENT_SIGNIN:
          transaction.add(fragment,T.FRAGMENT_NAME_SIGNIN);
          break;
        case T.FRAGMENT_SIGNUP:
          transaction.add(fragment,T.FRAGMENT_NAME_SIGNUP);
          break;
        case T.FRAGMENT_TIP:
          transaction.add(fragment,T.FRAGMENT_NAME_TIP);
          break;
        case T.FRAGMENT_TRIP:
          transaction.add(fragment,T.FRAGMENT_NAME_TRIP);
          break;
        case T.FRAGMENT_FIND:
          transaction.add(fragment,T.FRAGMENT_NAME_FIND);
          break;
        case T.FRAGMENT_ORDER_STATUS:
          transaction.add(fragment,T.FRAGMENT_NAME_ORDER_STATUS);
          break;

        case T.FRAGMENT_ERROR:
          transaction.add(fragment,T.FRAGMENT_NAME_ERROR);
          break;
        case T.FRAGMENT_SUCCESS:
          transaction.add(fragment,T.FRAGMENT_NAME_SUCCESS);
          break;
        case T.FRAGMENT_INFO:
          transaction.add(fragment,T.FRAGMENT_NAME_INFO);
          break;
        case T.FRAGMENT_IMAGE:
          transaction.add(fragment,T.FRAGMENT_NAME_IMAGE);
          break;
        /*
        case T.FRAGMENT_GPS_WARNING:
          transaction.add(fragment,T.FRAGMENT_NAME_GPS_WARNING);
          break;*/

        case T.FRAGMENT_LIST:
          transaction.add(fragment,T.FRAGMENT_NAME_LIST);
          break;
        case T.FRAGMENT_ORDER_LIST:
          transaction.add(fragment,T.FRAGMENT_NAME_ORDER_LIST);
          break;
        case T.FRAGMENT_PRODUCT_LIST:
          transaction.add(fragment,T.FRAGMENT_NAME_PRODUCT_LIST);
          break;
        case T.FRAGMENT_CART_LIST:
          transaction.add(fragment,T.FRAGMENT_NAME_CART_LIST);
          break;
        case T.FRAGMENT_MESSAGE_LIST:
          transaction.add(fragment,T.FRAGMENT_NAME_MESSAGE_LIST);
          break;

        case T.FRAGMENT_TAXIMETER:
          transaction.add(fragment,T.FRAGMENT_NAME_TAXIMETER);
          break;
        case T.FRAGMENT_CART:
          transaction.add(fragment,T.FRAGMENT_NAME_CART);
          break;
        case T.FRAGMENT_PAYMENT:
          transaction.add(fragment,T.FRAGMENT_NAME_PAYMENT);
          break;
        case T.FRAGMENT_PICKUP:
          transaction.add(fragment,T.FRAGMENT_NAME_PICKUP);
          break;
        case T.FRAGMENT_DROPOFF:
          transaction.add(fragment,T.FRAGMENT_NAME_DROPOFF);
          break;
        case T.FRAGMENT_RIDE:
          transaction.add(fragment,T.FRAGMENT_NAME_RIDE);
          break;
        case T.FRAGMENT_INVOICE:
          transaction.add(fragment,T.FRAGMENT_NAME_INVOICE);
          break;
        case T.FRAGMENT_INQUIRY:
          transaction.add(fragment,T.FRAGMENT_NAME_INQUIRY);
          break;
        case T.FRAGMENT_PRODUCT:
          transaction.add(fragment,T.FRAGMENT_NAME_PRODUCT);
          break;

        default:
      }
    }
    fragmentType=type;
  }

  /*showActivity*/
  public void showStartActivity(int flags){//Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NO_HISTORY
    Intent intent=new Intent(context,StartActivity.class);
    if(flags>=0)intent.addFlags(flags);
    context.startActivity(intent);
  }
  private boolean splashStarted=false;//activity
  public void showSplashActivity(int flags){
    splashStarted=true;
    Intent intent=new Intent(context,SplashActivity.class);
    if(flags>=0)intent.addFlags(flags);
    ((Activity)context).startActivityForResult(intent,T.ACTIVITY_SPLASH);
  }
  public void showSettingsActivity(){//settings
    Intent intent=new Intent(context,SettingsActivity.class);
    ((Activity)context).startActivityForResult(intent,T.ACTIVITY_SETTINGS);
  }
  public void showDriverActivity(){//profile
    Intent intent=new Intent(context,DriverActivity.class);
    ((Activity)context).startActivityForResult(intent,T.ACTIVITY_DRIVER);
  }
  /*public void showMessagesActivity(){//messages
    Intent intent=new Intent(context,MessagesActivity.class);
    ((Activity)context).startActivityForResult(intent,T.ACTIVITY_MESSAGES);
  }*/
  /*showFragment*/
  public void showFatalError(String message){
    transaction=getFragmentTransaction();
    fragment=ErrorFragment.newInstance(context.getString(R.string.message_fatal_error_title),message);
    fragmentType=T.FRAGMENT_ERROR;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_ERROR,T.FRAGMENT_ERROR,fragmentTypeList,fragmentList);
  }
  public void showErrorFragment(String message){
    transaction=getFragmentTransaction();
    fragment=ErrorFragment.newInstance(context.getString(R.string.message_error_title),message);
    fragmentType=T.FRAGMENT_ERROR;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_ERROR,T.FRAGMENT_ERROR,fragmentTypeList,fragmentList);
  }
  public void showSuccessFragment(String message){
    transaction=getFragmentTransaction();
    fragment=SuccessFragment.newInstance(context.getString(R.string.message_info_title),message);
    fragmentType=T.FRAGMENT_SUCCESS;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_SUCCESS,T.FRAGMENT_SUCCESS,fragmentTypeList,fragmentList);
  }
  public void showInfoFragment(String message){
    transaction=getFragmentTransaction();
    fragment=InfoFragment.newInstance(context.getString(R.string.message_info_title),message);
    fragmentType=T.FRAGMENT_INFO;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_INFO,T.FRAGMENT_INFO,fragmentTypeList,fragmentList);
  }
  //byte[] dec_data=Base64.decode(image_str,Base64.DEFAULT); <-IS A DECODE IMAGE STRING BEFORE
  public void showImageFragment(byte[] image){
    fragment=ImageFragment.newInstance(image);
    fragmentType=T.FRAGMENT_IMAGE;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_IMAGE,T.FRAGMENT_IMAGE,fragmentTypeList,fragmentList);
  }
  private boolean mapStarted=false;
  public boolean isMapStarted(){return mapStarted;}
  public void setMapStarted(boolean map_started){mapStarted=map_started;}
  public void showMapFragment(int callback_command,int map_provider){
    mapStarted=true;
    transaction=getFragmentTransaction();
    //the old style of map fragment (one main map provider)
    //fragment=MapFragment.newInstance(callback_command,map_provider);//killed in action!
    fragment=Map2Fragment.newInstance(callback_command,map_provider);
    fragmentType=T.FRAGMENT_MAP;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_MAP,T.FRAGMENT_MAP,fragmentTypeList,fragmentList);
  }
  public void showStatFragment(){
    transaction=getFragmentTransaction();
    fragment=StatFragment.newInstance();
    fragmentType=T.FRAGMENT_STAT;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_STAT,T.FRAGMENT_STAT,fragmentTypeList,fragmentList);
  }
  public void showStartFragment(){
    transaction=getFragmentTransaction();
    fragment=StartFragment.newInstance();
    fragmentType=T.FRAGMENT_START;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_START,T.FRAGMENT_START,fragmentTypeList,fragmentList);
  }
  public void showRegisterFragment(String firstname,String lastname,String callname,String email,String phone,String password){
    transaction=getFragmentTransaction();
    fragment=RegisterFragment.newInstance(firstname,lastname,callname,email,phone,password);
    fragmentType=T.FRAGMENT_REGISTER;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_REGISTER,T.FRAGMENT_REGISTER,fragmentTypeList,fragmentList);
  }
  public void showSigninFragment(String phone){
    transaction=getFragmentTransaction();
    fragment=SigninFragment.newInstance(phone,null);
    fragmentType=T.FRAGMENT_SIGNIN;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_SIGNIN,T.FRAGMENT_SIGNIN,fragmentTypeList,fragmentList);
  }
  public void showSignupFragment(String phone){
    transaction=getFragmentTransaction();
    /* now always show phone number
    if(!DEBUG)fragment=SignupFragment.newInstance(phone,null);
    else fragment=SignupFragment.newInstance(RemoteStorage.DEMO_USERNAME,RemoteStorage.DEMO_PASSWORD);
    */
    fragment=SignupFragment.newInstance(phone,null);
    fragmentType=T.FRAGMENT_SIGNUP;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_SIGNUP,T.FRAGMENT_SIGNUP,fragmentTypeList,fragmentList);
  }
  public void showTipFragment(String tip_message,int picture_id){
    transaction=getFragmentTransaction();
    fragment=TipFragment.newInstance(tip_message,picture_id);
    fragmentType=T.FRAGMENT_TIP;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_TIP,T.FRAGMENT_TIP,fragmentTypeList,fragmentList);
  }
  public void showTripFragment(String duration,String distance,String price,String discount,String currency){
    transaction=getFragmentTransaction();
    fragment=TripFragment.newInstance(duration,distance,price,discount,currency);
    fragmentType=T.FRAGMENT_TRIP;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_TRIP,T.FRAGMENT_TRIP,fragmentTypeList,fragmentList);
  }
  public void showFindFragment(int layout,String tip_message,int picture_id,String pickup_address,String destination_address,String currency){
    transaction=getFragmentTransaction();
    fragment=FindFragment.newInstance(layout,tip_message,picture_id,pickup_address,destination_address,currency);
    fragmentType=T.FRAGMENT_FIND;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_FIND,T.FRAGMENT_FIND,fragmentTypeList,fragmentList);
  }
  public void showListFragment(String name,int col_count,List list,String classname,int layout,String picture_url,TypedCallback callback,boolean next_enabled,boolean checking){
    transaction=getFragmentTransaction();
    fragment=ListFragment.newInstance(name,col_count,list,classname,layout,picture_url,next_enabled,checking);
    ((ListFragment)fragment).setCallback(callback);
    String fragment_name=T.FRAGMENT_NAME_LIST;
    int fragment_type=T.FRAGMENT_LIST;
    if(classname.endsWith(PRODUCT_LIST_ADAPTER_CLASS)){
      fragment_name=T.FRAGMENT_NAME_PRODUCT_LIST;fragment_type=T.FRAGMENT_PRODUCT_LIST;
    }
    else if(classname.endsWith(ORDER_LIST_ADAPTER_CLASS)){
      fragment_name=T.FRAGMENT_NAME_ORDER_LIST;fragment_type=T.FRAGMENT_ORDER_LIST;
    }
    fragmentType=fragment_type;
    addFragment(R.id.fragment,transaction,fragment,fragment_name,fragment_type,fragmentTypeList,fragmentList);
  }
  public void showCartListFragment(String name,int col_count,List list,String classname,int layout,String picture_url,TypedCallback callback,boolean next_enabled,boolean checking){
    transaction=getFragmentTransaction();
    fragment=ListFragment.newInstance(name,col_count,list,classname,layout,picture_url,next_enabled,checking);
    ((ListFragment)fragment).setCallback(callback);
    String fragment_name=T.FRAGMENT_NAME_CART_LIST;
    int fragment_type=T.FRAGMENT_CART_LIST;
    fragmentType=fragment_type;
    addFragment(R.id.fragment,transaction,fragment,fragment_name,fragment_type,fragmentTypeList,fragmentList);
  }
  public void showMessageListFragment(String name,int col_count,List list,String classname,int layout_input_message,int layout_output_message,String picture_url,TypedCallback callback,boolean next_enabled,boolean checking){
    transaction=getFragmentTransaction();
    fragment=ListFragment.newInstance(name,col_count,list,classname,layout_input_message,layout_output_message,picture_url,next_enabled,checking);
    ((ListFragment)fragment).setCallback(callback);
    String fragment_name=T.FRAGMENT_NAME_MESSAGE_LIST;
    int fragment_type=T.FRAGMENT_MESSAGE_LIST;
    fragmentType=fragment_type;
    addFragment(R.id.fragment,transaction,fragment,fragment_name,fragment_type,fragmentTypeList,fragmentList);
  }

  public void showOrderStatusFragment(String status_message,int picture_id,boolean visibility,boolean show_logo){
    transaction=getFragmentTransaction();
    fragment=OrderStatusFragment.newInstance(status_message,picture_id,visibility,show_logo);
    fragmentType=T.FRAGMENT_ORDER_STATUS;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_ORDER_STATUS,T.FRAGMENT_ORDER_STATUS,fragmentTypeList,fragmentList);
  }
  public void showTaximeterFragment(int layout,int dropoff_picture,String dropoff_name,String dropoff_address,long user_id,long status_id,long landing_duration,long trip_duration,long landing_time,long pickup_time,String picture_url,String order_id,String distance,String price,String status,String currency,String address,String chronometers_format,String trip_distance,float user_rate,String user_name){
    transaction=getFragmentTransaction();
    fragment=TaximeterFragment.newInstance(layout,dropoff_picture,dropoff_name,dropoff_address,user_id,status_id,landing_duration,trip_duration,landing_time,pickup_time,picture_url,order_id,distance,price,status,currency,address,chronometers_format,trip_distance,user_rate,user_name);
    fragmentType=T.FRAGMENT_TAXIMETER;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_TAXIMETER,T.FRAGMENT_TAXIMETER,fragmentTypeList,fragmentList);
  }
  public void showCartFragment(String count,String price,String discount,String currency){
    transaction=getFragmentTransaction();
    fragment=CartFragment.newInstance(count,price,discount,currency);
    fragmentType=T.FRAGMENT_CART;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_CART,T.FRAGMENT_CART,fragmentTypeList,fragmentList);
  }
  public void showPaymentFragment(int payment_provider,long order_id,double price,String currency){
    transaction=getFragmentTransaction();
    fragment=PaymentFragment.newInstance(payment_provider,order_id,price,currency);
    fragmentType=T.FRAGMENT_PAYMENT;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_PAYMENT,T.FRAGMENT_PAYMENT,fragmentTypeList,fragmentList);
  }
  public void showPickupFragment(String name,int col_count,List list,String classname,int layout,String picture_url,int pickup_picture,String pickup_name,String pickup_address){
    transaction=getFragmentTransaction();
    transaction.setCustomAnimations(R.anim.slide_up,0);
    fragment=PickupFragment.newInstance(name,col_count,list,classname,layout,picture_url,pickup_picture,pickup_name,pickup_address);
    fragmentType=T.FRAGMENT_PICKUP;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_PICKUP,T.FRAGMENT_PICKUP,fragmentTypeList,fragmentList);
  }
  public void showDropoffFragment(String name,int pickup_picture,String pickup_name,String pickup_address,int dropoff_picture,String dropoff_name,String dropoff_address,String duration,String distance,String price,String discount,String currency,String order_time){
    transaction=getFragmentTransaction();
    transaction.setCustomAnimations(R.anim.slide_up,0);
    fragment=DropoffFragment.newInstance(name,pickup_picture,pickup_name,pickup_address,dropoff_picture,dropoff_name,dropoff_address,duration,distance,price,discount,currency,order_time);
    fragmentType=T.FRAGMENT_DROPOFF;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_DROPOFF,T.FRAGMENT_DROPOFF,fragmentTypeList,fragmentList);
  }
  public void showRideFragment(String name,int dropoff_picture,String dropoff_name,String dropoff_address,String picture_url,String driver_name,float transport_review,String transport_color,String transport_name,String license_plate,float user_rate){
    transaction=getFragmentTransaction();
    fragment=RideFragment.newInstance(name,dropoff_picture,dropoff_name,dropoff_address,picture_url,driver_name,transport_review,transport_color,transport_name,license_plate,user_rate);
    fragmentType=T.FRAGMENT_RIDE;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_RIDE,T.FRAGMENT_RIDE,fragmentTypeList,fragmentList);
  }
  public void showInvoiceFragment(String name,int pickup_picture,String pickup_name,String pickup_address,int dropoff_picture,String dropoff_name,String dropoff_address,String price,String currency,String picture_url,String user_name,boolean review_driver){
    transaction=getFragmentTransaction();
    fragment=InvoiceFragment.newInstance(name,pickup_picture,pickup_name,pickup_address,dropoff_picture,dropoff_name,dropoff_address,price,currency,picture_url,user_name,review_driver);
    fragmentType=T.FRAGMENT_INVOICE;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_INVOICE,T.FRAGMENT_INVOICE,fragmentTypeList,fragmentList);
  }
  public void showInquiryFragment(String name,int pickup_picture,String pickup_name,String pickup_address,int dropoff_picture,String dropoff_name,String dropoff_address,String duration,String distance,String price,String discount,String currency,String order_time,String picture_url,long user_id,float user_rate,String user_name){
    transaction=getFragmentTransaction();
    fragment=InquiryFragment.newInstance(name,pickup_picture,pickup_name,pickup_address,dropoff_picture,dropoff_name,dropoff_address,duration,distance,price,discount,currency,order_time,picture_url,user_id,user_rate,user_name);
    fragmentType=T.FRAGMENT_INQUIRY;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_INQUIRY,T.FRAGMENT_INQUIRY,fragmentTypeList,fragmentList);
  }
  public void showProductFragment(int col_count,List list,String classname,int layout,String picture_url,int find_picture,String find_name){
    transaction=getFragmentTransaction();
    fragment=ProductFragment.newInstance(col_count,list,classname,layout,picture_url,find_picture,find_name);
    fragmentType=T.FRAGMENT_PRODUCT;
    addFragment(R.id.fragment,transaction,fragment,T.FRAGMENT_NAME_PRODUCT,T.FRAGMENT_PRODUCT,fragmentTypeList,fragmentList);
  }

  //typedCallback
  //getRequest->(show..) add,updateRequest->(after..)
  public TypedCallback showStatFragmentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(obj!=null){
        stat=(Stat)obj;
        showStatFragment();
      }
    }
  };
  public TypedCallback showPickupFragmentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      //start pickup fragment
      String title=context.getString(R.string.app_title_pickup);
      String url=getPictureUrl(T.IMAGE_PRODUCT);
      ProductListContent list_content=null;
      if(productList!=null)list_content=getProductListContent(productList);
      showPickupFragment(title,Manager.PRODUCT_LIST_COL_COUNT,
                         list_content!=null?list_content.getItemList():null,packageName+Manager.EASY_PRODUCT_LIST_ADAPTER_CLASS,R.layout.layout_easy_product,url,-1,pickupName!=null?pickupName:context.getString(R.string.gps_location),pickupAddress);
      easyBooking=EASY_BOOKING_PICKUP;
      //((StartActivity)context).setTitle(R.string.app_title_pickup);
    }
  };
  public TypedCallback showDropoffFragmentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      //start dropoff fragment
      String title=context.getString(R.string.app_title_dropoff);
      String discount=(currentUser!=null&&currentUser.discount!=null)?getDiscountPolicy(currentUser.discount.type,currentUser.discount.value):null;
      String currency=activeCurrencyTitle!=null?activeCurrencyTitle:activeCurrency.name;
      String price_text=getPrice(totalPrice!=0?totalPrice:orderPrice);
      showDropoffFragment(title,-1,pickupName!=null?pickupName:context.getString(R.string.gps_location),pickupName!=null?pickupAddress:T.EMPTY,-1,orderPrice!=0&&dropoffName!=null?dropoffName:context.getString(R.string.destination),orderPrice!=0?dropoffAddress:T.EMPTY,durationText,distanceText,price_text,discount,currency,null);
      easyBooking=EASY_BOOKING_DROPOFF;
      //((StartActivity)context).setTitle(R.string.app_title_dropoff);
    }
  };
  public TypedCallback showRideFragmentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      //start ride fragment
      String title=context.getString(R.string.app_title_dropoff);
      String url=getPictureUrl(T.IMAGE_USER);
      OrderAB order=currentOrderAB;
      if(order!=null&&order.transport!=null){
        String driver_name=(order.transport.sensor!=null&&order.transport.sensor.user!=null)?order.transport.sensor.user.call_name:null;
        showRideFragment(title,-1,dropoffName!=null?dropoffName:context.getString(R.string.destination_not_selected),dropoffAddress,url,driver_name,(float)order.transport.rate,order.transport.color,order.transport.name,order.transport.license_plate,(float)(order.user!=null?order.user.rate:0));
        easyBooking=EASY_BOOKING_RIDE;
        //((StartActivity)context).setTitle(R.string.app_title_ride);
      }
    }
  };
  public TypedCallback showInvoiceFragmentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      boolean is_driver_review=((Boolean)obj).booleanValue();
      //start invoice fragment
      OrderAB order=currentOrderAB;
      if(order!=null){
        String title=context.getString(R.string.app_title_invoice);
        String currency=activeCurrencyTitle!=null?activeCurrencyTitle:activeCurrency.name;
        String url=getPictureUrl(T.IMAGE_USER);

        String pickup_name,pickup_address;
        String dropoff_name,dropoff_address;

        User user=null;
        if(is_driver_review){
          pickup_name=pickupName;
          pickup_address=pickupAddress;
          dropoff_name=dropoffName;
          dropoff_address=dropoffAddress;
          if(order.transport!=null&&order.transport.sensor!=null)user=order.transport.sensor.user;
        }
        else{
          pickup_name=order.order_address;
          pickup_address=String.valueOf(order.order_lat)+T.COMA+T.SPACE+String.valueOf(order.order_lon);
          dropoff_name=order.delivery_address;
          dropoff_address=String.valueOf(order.delivery_lat)+T.COMA+T.SPACE+String.valueOf(order.delivery_lon);
          user=order.user;
        }

        String user_name=(user!=null?user.call_name:T.MINUS);

        showInvoiceFragment(title,-1,pickup_name!=null?pickup_name:T.MINUS,pickup_address,-1,dropoff_name!=null?dropoff_name:T.MINUS,dropoff_address,order!=null?getPrice(order.total_price):T.MINUS,currency,url,user_name,is_driver_review);
        easyBooking=EASY_BOOKING_INVOICE;
        //((StartActivity)context).setTitle(R.string.app_title_invoice);
      }
    }
  };
  public TypedCallback showInquiryFragmentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      //start inquiry fragment
      OrderAB order=selectedOrderAB;
      if(order!=null){
        String title=context.getString(R.string.app_title_invoice);
        String currency=activeCurrencyTitle!=null?activeCurrencyTitle:activeCurrency.name;
        String url=getPictureUrl(T.IMAGE_USER);

        User user=order.user;
        String pickup_name=order.order_address;
        String pickup_address=String.valueOf(order.order_lat)+T.COMA+T.SPACE+String.valueOf(order.order_lon);
        String dropoff_name=order.delivery_address;
        String dropoff_address=String.valueOf(order.delivery_lat)+T.COMA+T.SPACE+String.valueOf(order.delivery_lon);

        String duration_text=getDuration(order);
        String distance_text=getDistance(order);
        String discount=(order!=null&&order.user!=null&&order.user.discount!=null)?getDiscountPolicy(currentUser.discount.type,currentUser.discount.value):null;
        String price=order!=null?getPrice(order.total_price):T.MINUS;
        String order_time=(order!=null&&order.reserved_date!=null)?getTalkingDatetimeMessage(order.reserved_date):null;
        long user_id=(user!=null?user.id:-1);
        float user_rate=(user!=null?(float)user.rate:0);
        String user_name=(user!=null?user.call_name:T.MINUS);

        showInquiryFragment(title,-1,pickup_name!=null?pickup_name:T.MINUS,pickup_address,-1,dropoff_name!=null?dropoff_name:T.MINUS,dropoff_address,duration_text,distance_text,price,discount,currency,order_time,url,user_id,user_rate,user_name);
      }
    }
  };
  public TypedCallback showProductFragmentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      //start product fragment
      String url=getPictureUrl(T.IMAGE_PRODUCT);
      ProductListContent list_content=null;
      if(productList!=null)list_content=getProductListContent(productList);
      showProductFragment(Manager.PRODUCT_LIST_COL_COUNT,
                          list_content!=null?list_content.getItemList():null,packageName+Manager.EASY_PRODUCT_LIST_ADAPTER_CLASS,R.layout.layout_easy_product,url,-1,null);
    }
  };

  private TypedCallback closeActivityCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      ((Activity)context).finishActivity(((Integer)obj).intValue());
    }
  };
  public TypedCallback doNewMessageRequestCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      //simulate after success add message event (after send request)
      RetVal ret_val=new RetVal();
      ret_val.value=1;
      afterAddMessageCallbackHandler.execute(ret_val);
    }
  };
  public TypedCallback showSplashActivityUntilDoRequestCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      //start splash (splash bad in closing)
      //showSplashActivity(Intent.FLAG_ACTIVITY_NO_HISTORY);
      if(!mapStarted)splashDialog=makeSplashDialog();
      //user,token,currency,product
      getDefaultData();
      //order
      long order_id=tripMode?tripOrderId:orderId;
      if(order_id!=-1)getOrderABRequest(showOrderABCallbackHandler,order_id,-1,-1);//recv by id
      else getMaxOrderABRequest(showOrderABCallbackHandler);//lastOrder
      //close splash
      /*if(splashStarted){
        timeStarter(closeActivityCallbackHandler,new Integer(T.ACTIVITY_SPLASH),1);
        splashStarted=false;
      }*/
    }
  };
  public TypedCallback showMapFragmentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      showMapFragment(((Integer)obj).intValue(),mapProvider);
    }
  };
  public TypedCallback showTripFragmentAndRouteCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      Direction d=(Direction)obj;currentDirection=d;
      double order_price=calcOrder(d);
      orderPrice=order_price;totalPrice=0;
      String price_text=getPrice(order_price);
      distanceText=d.distanceText;durationText=d.durationText;
      if(DEBUG)Toast.makeText(context.getApplicationContext(),"orderPrice="+order_price,Toast.LENGTH_SHORT).show();
      Fragment frag=getFragment(T.FRAGMENT_NAME_TRIP);
      if(frag!=null){
        ((TripFragment)frag).updateDistance(distanceText);
        ((TripFragment)frag).updateDuration(durationText);
        ((TripFragment)frag).updatePrice(price_text);
      }
      else{
        String discount=T.EMPTY,currency=T.EMPTY;
        if(currentUser!=null&&currentUser.discount!=null){
          discount=getDiscountPolicy(currentUser.discount.type,currentUser.discount.value);
        }
        if(activeCurrency!=null){
          currency=activeCurrencyTitle!=null?activeCurrencyTitle:activeCurrency.name;
        }
        showTripFragment(durationText,distanceText,price_text,discount,currency);
      }
      //route
      frag=getFragment(T.FRAGMENT_NAME_MAP);
      if(frag!=null){
        //put marker
        if(lastDirectionType==DIRECTIONS_ROUTE_ADDRESS){
          Map map=new Map();
          ((Map2Fragment)frag).clearTripMarkers();//clearTripRoute,clearTripMarkerAB
          ((Map2Fragment)frag).addTripMarker(map.newLocationInstance(d.startLocationLat,d.startLocationLon),false);
          ((Map2Fragment)frag).addTripMarker(map.newLocationInstance(d.finishLocationLat,d.finishLocationLon),false);
          ((Map2Fragment)frag).addTripRoute(d);
        }
        else{
          ((Map2Fragment)frag).clearTripRoute();
          ((Map2Fragment)frag).addTripRoute(d);
        }
      }
    }
  };
  public TypedCallback updateDropoffFragmentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      Direction d=(Direction)obj;currentDirection=d;
      Product product=(selectedProduct!=null?selectedProduct:defaultProduct);
      double order_price=calcOrder(d);
      orderPrice=order_price;
      double total_price=getTotalPrice(order_price,product.paramList);
      totalPrice=total_price;
      distanceText=d.distanceText;durationText=d.durationText;
      String price_text=getPrice(total_price);
      if(DEBUG)Toast.makeText(context.getApplicationContext(),"orderPrice="+order_price,Toast.LENGTH_SHORT).show();
      Fragment frag=getFragment(T.FRAGMENT_NAME_DROPOFF);
      if(frag!=null){
        ((DropoffFragment)frag).updateDistance(distanceText);
        ((DropoffFragment)frag).updateDuration(durationText);
        ((DropoffFragment)frag).updatePrice(price_text);
      }
    }
  };
  public TypedCallback updateDropoffFragmentForRentalCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      //Product product=(selectedProduct!=null?selectedProduct:defaultProduct);
      //Toast.makeText(context.getApplicationContext(),"defaultProduct="+defaultProduct.id+" name="+defaultProduct.name+" price="+defaultProduct.price,Toast.LENGTH_SHORT).show();
      Product product=selectedProduct;//always selected product for rental(not can be default)
      if(product!=null){
        double order_price=calcOrder((Direction)null);//direction not found
        orderPrice=order_price;
        double total_price=getTotalPrice(order_price,product.paramList);
        totalPrice=total_price;
        String price_text=getPrice(total_price);
        if(DEBUG) Toast.makeText(context.getApplicationContext(),"orderPrice="+order_price,Toast.LENGTH_SHORT).show();
        Fragment frag=getFragment(T.FRAGMENT_NAME_DROPOFF);
        if(frag!=null){
          ((DropoffFragment)frag).updatePrice(price_text);
        }
      }
    }
  };
  public TypedCallback updateFindFragmentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      Direction d=(Direction)obj;currentDirection=d;
      Product product=(selectedProduct!=null?selectedProduct:defaultProduct);
      double order_price=calcOrder(d);
      orderPrice=order_price;
      double total_price=getTotalPrice(order_price,product.paramList);
      totalPrice=total_price;
      distanceText=d.distanceText;durationText=d.durationText;
      String price_text=getPrice(total_price);
      if(DEBUG)Toast.makeText(context.getApplicationContext(),"orderPrice="+order_price,Toast.LENGTH_SHORT).show();
      Fragment frag=getFragment(T.FRAGMENT_NAME_FIND);
      if(frag!=null){
        //updateDistance(distanceText);
        //updateDuration(durationText);
        ((FindFragment)frag).updatePrice(price_text);
      }
    }
  };
  public TypedCallback showGeocodeCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      Geocode geocode=(Geocode)obj;
      addGeocodeRequest(geocode.geocodeAddress,geocode.formattedAddress,geocode.locationLat,geocode.locationLon);
      putFindAddress(geocode.geocodeAddress);
      putDeliveryAddress(geocode.formattedAddress);
      putDeliveryLatitude(Double.toString(geocode.locationLat));
      putDeliveryLongitude(Double.toString(geocode.locationLon));
      editor.commit();
      if(DEBUG)Toast.makeText(context.getApplicationContext(),"deliveryAddress="+geocode.formattedAddress,Toast.LENGTH_SHORT).show();
      Map map=new Map();
      Map.Location location=map.newLocationInstance(geocode.locationLat,geocode.locationLon);
      showMapCallbackHandler.execute(location);
    }
  };
  public TypedCallback showGeocodeWithDropoffRouteCalcCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      Geocode geocode=(Geocode)obj;
      addGeocodeRequest(geocode.geocodeAddress,geocode.formattedAddress,geocode.locationLat,geocode.locationLon);
      Fragment frag=getFragment(T.FRAGMENT_NAME_MAP);
      if(frag!=null){
        ((Map2Fragment)frag).dropoffAndRouteCalcCallbackHandler.execute(geocode);
      }
    }
  };

  public TypedCallback showPickupAddressCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      Geocode geocode=(Geocode)obj;
      if(DEBUG)Toast.makeText(context.getApplicationContext(),"locationAddress="+geocode.formattedAddress,Toast.LENGTH_SHORT).show();
      String name=T.EMPTY;
      if(geocode.addressComponent!=null){
        if(geocode.addressComponent.size()>1){
          name=geocode.addressComponent.get(0)+T.COMA+T.SPACE+geocode.addressComponent.get(1);
        }
        else if(geocode.addressComponent.size()==1)name=geocode.addressComponent.get(0);
      }
      //name/address
      putPickupName(name);
      putPickupAddress(geocode.formattedAddress);
      editor.commit();

      Fragment frag;
      if(bookingStyle==BOOKING_STYLE_EASY){
        //pickup
        frag=getFragment(T.FRAGMENT_NAME_PICKUP);
        if(frag!=null) ((PickupFragment)frag).updatePickupNameAndAddress(pickupName,pickupAddress);
        //dropoff
        frag=getFragment(T.FRAGMENT_NAME_DROPOFF);
        if(frag!=null) ((DropoffFragment)frag).updatePickupNameAndAddress(pickupName,pickupAddress);
      }
      if(bookingStyle==BOOKING_STYLE_EASY_AB){
        //find
        frag=getFragment(T.FRAGMENT_NAME_FIND);
        if(frag!=null){
          ((FindFragment)frag).updatePickupNameAndAddress(pickupName,pickupAddress);
          ((FindFragment)frag).clearPriceAndHide();
          clearOrderCalcValues();
          if(orderLatitude!=null&&orderLongitude!=null&&deliveryLatitude!=null&&deliveryLongitude!=null&&pickupName!=null&&pickupName.length()>0)
            ((FindFragment)frag).routeCalculation();
        }
      }
    }
  };
  public TypedCallback showDropoffAddressCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      Geocode geocode=(Geocode)obj;
      if(DEBUG)Toast.makeText(context.getApplicationContext(),"locationAddress="+geocode.formattedAddress,Toast.LENGTH_SHORT).show();
      String name=T.EMPTY;
      if(geocode.addressComponent!=null){
        if(geocode.addressComponent.size()>1){
          name=geocode.addressComponent.get(0)+T.COMA+T.SPACE+geocode.addressComponent.get(1);
        }
        else if(geocode.addressComponent.size()==1)name=geocode.addressComponent.get(0);
      }
      //name/address
      putDropoffName(name);
      putDropoffAddress(geocode.formattedAddress);
      editor.commit();
      //dropoff
      Fragment frag=getFragment(T.FRAGMENT_NAME_DROPOFF);
      if(frag!=null)((DropoffFragment)frag).updateDropoffNameAndAddress(name,geocode.formattedAddress);
    }
  };
  public TypedCallback showMapCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(!mapStarted){
        removeAllFragment();
        //start map
        showMapFragment(new Integer(Map2Fragment.CALLBACK_COMMAND_CLIENT),mapProvider);
      }
      else{//started map
        if(obj!=null&&obj instanceof Map.Location){
          Fragment frag=getFragment(T.FRAGMENT_NAME_MAP);
          if(frag!=null){
            ((Map2Fragment)frag).addDeliveryMarker((Map.Location)obj);
            ((Map2Fragment)frag).toLocation((Map.Location)obj);
          }
        }
      }
    }
  };
  public TypedCallback showCartFragmentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      ArrayList<Product> product_list=(ArrayList)obj;
      Product product;
      double order_price=calcOrder(product_list);
      orderPrice=order_price;
      double total_price=order_price,price;
      Iterator iter=product_list.iterator();
      while(iter.hasNext()){
        product=(Product)iter.next();
        if(product.checked){
          price=getProductParamPrice(product.paramList);
          if(product.cart_count>1)price=price*product.cart_count;
          total_price+=price;
        }
      }
      totalPrice=total_price;
      String price_text=getPrice(total_price);
      String count_text=String.valueOf(product_list.size());
      if(DEBUG)Toast.makeText(context.getApplicationContext(),"cartPrice="+total_price,Toast.LENGTH_SHORT).show();
      Fragment frag=getFragment(T.FRAGMENT_NAME_CART);
      if(frag!=null){
        ((CartFragment)frag).updateCount(count_text);
        ((CartFragment)frag).updatePrice(price_text);
      }
      else{
        String discount=T.EMPTY,currency=T.EMPTY;
        if(currentUser!=null&&currentUser.discount!=null){
          discount=getDiscountPolicy(currentUser.discount.type,currentUser.discount.value);
        }
        if(activeCurrency!=null){
          currency=activeCurrencyTitle!=null?activeCurrencyTitle:activeCurrency.name;
        }
        showCartFragment(count_text,price_text,discount,currency);
      }
      fabAction(-1);
    }
  };

  public ProductListContent getProductListContent(ArrayList list){
    ProductListContent list_content=new ProductListContent();
    ProductListContent.Item item;
    Product product;

    for(int i=0;i<list.size();i++){
      product=(Product)list.get(i);
      if(product!=null){
        item=new ProductListContent.Item(product,activeCurrency);
        item.id=product.id;
        list_content.addItem(item);
      }
    }
    return list_content;
  }
  public TypedCallback showEasyProductListCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(obj!=null){
        ProductListContent list_content=getProductListContent((ArrayList)obj);
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"productSize="+productList.size(),Toast.LENGTH_SHORT).show();
        Fragment frag;
        frag=getFragment(T.FRAGMENT_NAME_PICKUP);
        if(frag!=null)((PickupFragment)frag).updateItemList(list_content.getItemList());
        frag=getFragment(T.FRAGMENT_NAME_PRODUCT);
        if(frag!=null)((ProductFragment)frag).updateItemList(list_content.getItemList());
      }
    }
  };

  public TypedCallback showProductListWithTrueOptionCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      showProductListCallbackHandler.execute(newInstanceListWithOptions((ArrayList)obj,true));
    }
  };
  public TypedCallback showProductListWithFalseOptionCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      showProductListCallbackHandler.execute(newInstanceListWithOptions((ArrayList)obj,false));
    }
  };
  public TypedCallback showProductListCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(obj!=null){
        ArrayList list_obj;
        boolean checking=false;
        if(obj instanceof ArrayList)list_obj=(ArrayList)obj;
        else if(obj instanceof ListWithOptions){
          list_obj=((ListWithOptions)obj).list;
          checking=((ListWithOptions)obj).checking;
        }
        else list_obj=newInstanceProductList();//need not null

        Fragment frag=getFragment(T.FRAGMENT_NAME_PRODUCT_LIST);
        ProductListContent list_content=new ProductListContent();
        ProductListContent.Item item;
        Object object;
        ArrayList list=list_obj/*(ArrayList)obj*/;
        Product product;
        //if(frag!=null)list_content.setItemList(((ListFragment)frag).getItemList());//no need to reload
        for(int i=0;i<list.size();i++){
          object=list.get(i);
          product=null;
          if(object instanceof Product)product=(Product)object;
          else if(object instanceof OrderABPart)product=((OrderABPart)object).product;
          if(product!=null){

            product.cart_count=getCartCount(product.id);
            if(product.cart_count>0)product.checked=true;

            item=new ProductListContent.Item(product,activeCurrency);
            item.id=product.id;
            list_content.addItem(item);
          }
        }
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"productSize="+productList.size(),Toast.LENGTH_SHORT).show();
        if(frag!=null){
          if(list.size()>0){
            ((ListFragment)frag).getItemList().addAll(list_content.getItemList());
            ((ProductListAdapter)((ListFragment)frag).getListAdapterObject()).notifyItemInserted(((ListFragment)frag).getItemList().size());
          }
          else ((ListFragment)frag).setNextEnabled(false);
        }
        else{
          int layout=R.layout.layout_product;
          if(bookingStyle==BOOKING_STYLE_DELIVERY){
            if(TYPE_OF_PRODUCT_LIST==LIST_STYLE_PRODUCT)layout=R.layout.layout_product;
            else if(TYPE_OF_PRODUCT_LIST==LIST_STYLE_PRODUCT_DELIVERY)layout=R.layout.layout_product_delivery;
            else if(TYPE_OF_PRODUCT_LIST==LIST_STYLE_PRODUCT_RENTAL)layout=R.layout.layout_product_rental;
          }

          String title=T.EMPTY;
          if(bookingStyle==BOOKING_STYLE_DELIVERY){
            if(list_obj==cartList)title=context.getString(R.string.cart_count);
            else title=context.getString(R.string.products_and_services);
          }
          else if(bookingStyle==BOOKING_STYLE_TRIP||bookingStyle==BOOKING_STYLE_EASY||bookingStyle==BOOKING_STYLE_EASY_AB){
            title=(list.size()>1?context.getString(R.string.services):context.getString(R.string.service));
          }
          //String picture_url=getHostname()+RemoteStorage.URL_GET_PRODUCT_PICTURE;
          String url=getPictureUrl(T.IMAGE_PRODUCT);
          showListFragment(title,PRODUCT_LIST_COL_COUNT,
                           list_content.getItemList(),packageName+PRODUCT_LIST_ADAPTER_CLASS,layout,url,null,
                           /*always get allProducts*/false,checking);
        }
      }
    }
  };
  public TypedCallback showCartListCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(obj!=null){
        boolean checking=false;

        ProductListContent list_content=new ProductListContent();
        ProductListContent.Item item;
        ArrayList list=(ArrayList)obj;
        Product product;

        for(int i=0;i<list.size();i++){
          product=(Product)list.get(i);
          if(product!=null){
            item=new ProductListContent.Item(product,activeCurrency);
            item.id=product.id;
            list_content.addItem(item);
          }
        }
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"cartSize="+cartList.size(),Toast.LENGTH_SHORT).show();

        int layout=R.layout.layout_product;
        if(bookingStyle==BOOKING_STYLE_DELIVERY){
          if(TYPE_OF_PRODUCT_LIST==LIST_STYLE_PRODUCT)layout=R.layout.layout_product;
          else if(TYPE_OF_PRODUCT_LIST==LIST_STYLE_PRODUCT_DELIVERY)layout=R.layout.layout_product_delivery;
          else if(TYPE_OF_PRODUCT_LIST==LIST_STYLE_PRODUCT_RENTAL)layout=R.layout.layout_product_rental;
        }

        String url=getPictureUrl(T.IMAGE_PRODUCT);
        showCartListFragment(context.getString(R.string.cart_count),PRODUCT_LIST_COL_COUNT,
                               list_content.getItemList(),packageName+PRODUCT_LIST_ADAPTER_CLASS,layout,url,null,
                               /*always get allProducts*/false,checking);
      }
    }
  };
  public TypedCallback showMessageListCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(obj!=null){
        Fragment frag=getFragment(T.FRAGMENT_NAME_MESSAGE_LIST);
        MessageListContent list_content=new MessageListContent();
        MessageListContent.Item item;
        ArrayList list=(ArrayList)obj;
        Message message;
        //for(int i=0;i<list.size();i++){//forward order
        for(int i=list.size()-1;i>=0;i--){//backward order
          message=(Message)list.get(i);
          if(message!=null){
            item=new MessageListContent.Item(message);
            item.id=message.id;
            list_content.addItem(item);
          }
        }
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"messageSize="+messageList.size(),Toast.LENGTH_SHORT).show();

        if(frag!=null){
          if(list.size()>0){
            ((MessageListAdapter)((ListFragment)frag).getListAdapterObject()).setLastBindedPosition(list_content.getItemList().size());//backward update order
            ((MessageListAdapter)((ListFragment)frag).getListAdapterObject()).setValueBindedPosition(-1);
            //((ListFragment)frag).getItemList().addAll(0,list_content.getItemList());//similar
            ((MessageListAdapter)((ListFragment)frag).getListAdapterObject()).getItemList().addAll(0,list_content.getItemList());
            ((MessageListAdapter)((ListFragment)frag).getListAdapterObject()).notifyItemRangeInserted(0,list_content.getItemList().size());
          }
          else ((ListFragment)frag).setNextEnabled(false);
        }
        else{
          String url=getPictureUrl(T.IMAGE_USER);
          showMessageListFragment(context.getString(R.string.messages),MESSAGE_LIST_COL_COUNT,
                                  list_content.getItemList(),packageName+MESSAGE_LIST_ADAPTER_CLASS,
                                  R.layout.layout_input_message,R.layout.layout_output_message,url,getMessageCallbackHandler,true,false);
        }
      }
    }
  };
  public TypedCallback showNewMessageListCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(obj!=null){
        Fragment frag=getFragment(T.FRAGMENT_NAME_MESSAGE_LIST);
        MessageListContent list_content=new MessageListContent();
        MessageListContent.Item item;
        ArrayList list=(ArrayList)obj;
        Message message;
        //for(int i=0;i<list.size();i++){//forward order
        for(int i=list.size()-1;i>=0;i--){//backward order
          message=(Message)list.get(i);
          if(message!=null){
            item=new MessageListContent.Item(message);
            item.id=message.id;
            list_content.addItem(item);
          }
        }
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"messageSize="+messageList.size(),Toast.LENGTH_SHORT).show();

        if(frag!=null){
          if(list.size()>0){
            int before_size=((ListFragment)frag).getItemList().size();
            ((MessageListAdapter)((ListFragment)frag).getListAdapterObject()).setLastBindedPosition(before_size-1);//forward update order
            ((MessageListAdapter)((ListFragment)frag).getListAdapterObject()).setValueBindedPosition(1);
            ((MessageListAdapter)((ListFragment)frag).getListAdapterObject()).getItemList().addAll(list_content.getItemList());
            ((MessageListAdapter)((ListFragment)frag).getListAdapterObject()).notifyItemRangeInserted(before_size,before_size+list_content.getItemList().size());
            //scroll to last message
            ((ListFragment)frag).smoothScrollToPosition(before_size-1);
          }
        }
      }
    }
  };

  //getProduct(page_number)
  public TypedCallback getProductCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      int page=((Integer)obj).intValue(),offset_value=LIST_COUNT*page,rows_value=LIST_COUNT;
      getProductRequest(showProductListWithTrueOptionCallbackHandler,T.PERCENT,offset_value,rows_value);
      if(DEBUG)Toast.makeText(context.getApplicationContext(),"getProductPage="+page,Toast.LENGTH_SHORT).show();
    }
  };
  //getMessage(page_number)
  public TypedCallback getMessageCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      int page=((Integer)obj).intValue(),offset_value=LIST_COUNT*page,rows_value=LIST_COUNT;
      getMessageRequest(showMessageListCallbackHandler,T.PERCENT,offset_value,rows_value);
      if(DEBUG)Toast.makeText(context.getApplicationContext(),"getMessagePage="+page,Toast.LENGTH_SHORT).show();
    }
  };
  public TypedCallback showOrderABListCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(obj!=null){
        ArrayList list_obj;
        boolean checking=false;
        if(obj instanceof ArrayList)list_obj=(ArrayList)obj;
        else if(obj instanceof ListWithOptions){
          list_obj=((ListWithOptions)obj).list;
          checking=((ListWithOptions)obj).checking;
        }
        else list_obj=newInstanceOrderABList();//need not null

        Fragment frag=getFragment(T.FRAGMENT_NAME_ORDER_LIST);
        OrderABListContent list_content=new OrderABListContent();
        OrderABListContent.Item item;
        ArrayList list=list_obj/*(ArrayList)obj*/;
        OrderAB order;
        //if(frag!=null)list_content.setItemList(((ListFragment)frag).getItemList());//no need to reload
        for(int i=0;i<list.size();i++){
          order=(OrderAB)list.get(i);
          if(order!=null){
            item=new OrderABListContent.Item(order,activeCurrency);
            item.id=order.id;
            list_content.addItem(item);
          }
        }
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"orderSize="+list.size(),Toast.LENGTH_SHORT).show();
        if(frag!=null){
          if(list.size()>0){
            ((ListFragment)frag).getItemList().addAll(list_content.getItemList());
            ((OrderABListAdapter)((ListFragment)frag).getListAdapterObject()).notifyItemInserted(((ListFragment)frag).getItemList().size());
          }
          else ((ListFragment)frag).setNextEnabled(false);
        }
        else{
          String url=getPictureUrl(T.IMAGE_TRANSPORT);
          showListFragment(list.size()>1?context.getString(R.string.orders):context.getString(R.string.order),
                           ORDER_LIST_COL_COUNT,list_content.getItemList(),packageName+ORDER_LIST_ADAPTER_CLASS,R.layout.layout_order,url,getOrderABCallbackHandler,
                           /*list.size()==(LIST_COUNT)?true:false*/
                           list.size()>1?true:false,checking);
        }
      }
    }
  };
  //getOrder(page_number)
  public TypedCallback getOrderABCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      int page=((Integer)obj).intValue(),offset_value=LIST_COUNT*page,rows_value=LIST_COUNT;
      getOrderABRequest(showOrderABListCallbackHandler,T.PERCENT,offset_value,rows_value);
      if(DEBUG)Toast.makeText(context.getApplicationContext(),"getOrderABPage="+page,Toast.LENGTH_SHORT).show();
    }
  };
  public TypedCallback getOrderABTransportCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      int page=((Integer)obj).intValue(),offset_value=LIST_COUNT*page,rows_value=LIST_COUNT;
      getOrderABTransportRequest(showOrderABListCallbackHandler,driverTransportId,offset_value,rows_value);
      if(DEBUG)Toast.makeText(context.getApplicationContext(),"getOrderABTransportPage="+page,Toast.LENGTH_SHORT).show();
    }
  };

  //getOrder(Id)
  public TypedCallback showOrderStatusCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      //obj == status_id (String)
    }
  };
  public TypedCallback showOrderABStatusCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      //obj not used
      String status=currentOrderAB!=null?getStatusName(currentOrderAB).toUpperCase():T.EMPTY;
      long order_id=currentOrderAB!=null?currentOrderAB.id:orderId;
      long order_status_id=currentOrderAB!=null?currentOrderAB.status_id:orderStatusId;
      Fragment frag=getFragment(T.FRAGMENT_NAME_ORDER_STATUS);
      if(frag!=null){
        ((OrderStatusFragment)frag).updateOrderStatus(
          String.format(context.getString(R.string.order_number),order_id)+T.NEXT_LINE+status/*+":"+order_status_id*/);
        if(order_status_id<0||order_status_id>OrderABStatus.ORDER_STATUS_ACCEPTED){
          ((OrderStatusFragment)frag).setProgressBarVisibility(false);
          //logo picture in status
          if(showLogo)((OrderStatusFragment)frag).setImageViewVisibility(true);
          else ((OrderStatusFragment)frag).setImageViewVisibility(false);
        }
        else{
          ((OrderStatusFragment)frag).setProgressBarVisibility(true);
          //logo picture in status
          ((OrderStatusFragment)frag).setImageViewVisibility(false);
        }
      }
      else{//always open ?
        /*boolean visibility=order_status_id<0||order_status_id>OrderABStatus.ORDER_STATUS_ACCEPTED?false:true;
        showOrderStatusFragment(String.format(context.getString(R.string.order_number),order_id)+T.NEXT_LINE+status,-1,visibility,showLogo);*/
      }
      //booking fab
      FloatingActionButton fab=((StartActivity)context).getBookingFloatingActionButton();
      if(bookingStyle!=BOOKING_STYLE_EASY&&bookingStyle!=BOOKING_STYLE_EASY_AB){
        if(orderId!=-1){
          fab.setImageResource(R.drawable.ic_clear_white_24dp);
          if(orderStatusId>OrderABStatus.ORDER_STATUS_LANDING||orderStatusId<0)fab.hide();
          else fab.show();
        }
        else{
          fab.setImageResource(R.drawable.ic_add_white_24dp);
          fab.show();
        }
      }
    }
  };
  public TypedCallback showTaximeterFragmentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      OrderAB order=(OrderAB)obj;
      String currency=T.EMPTY;
      if(activeCurrency!=null){
        currency=activeCurrencyTitle!=null?activeCurrencyTitle:activeCurrency.name;
      }
      String address=order.delivery_address!=null&&order.delivery_address.length()>0?order.delivery_address:context.getString(R.string.no_address);
      String distance_text=getDistance(order);
      String trip_distance=getTripDistance(tripDistance);
      String price_text=order.total_price>0?getPrice(order.total_price):
                        getPrice(tripDistance>0?calcOrder(tripDistance,order):0);
      String url=getPictureUrl(T.IMAGE_USER);
      Fragment frag=getFragment(T.FRAGMENT_NAME_TAXIMETER);
      if(frag!=null){
        //((TaximeterFragment)frag).updateLandingDurationFormat(null);
        //((TaximeterFragment)frag).updateTripDurationFormat(null);
        ((TaximeterFragment)frag).updateTripDistance(trip_distance);
        ((TaximeterFragment)frag).updateOrderStatus(order.status_id,getStatusName(order));
        if(order.total_price==0&&tripDistance>0)//new price calc
          ((TaximeterFragment)frag).updateTripPrice(price_text);
      }
      else{
        int layout=(bookingStyle==BOOKING_STYLE_EASY||bookingStyle==BOOKING_STYLE_EASY_AB)?R.layout.fragment_job:R.layout.fragment_taximeter;
        showTaximeterFragment(layout,-1,order.delivery_address!=null?order.delivery_address:context.getString(R.string.no_address),String.valueOf(order.delivery_lat)+T.COMA+T.SPACE+String.valueOf(order.delivery_lon),order.user_id,order.status_id,landingDuration,tripDuration,landingTime,pickupTime,url,String.valueOf(order.id),distance_text,price_text,getStatusName(order),currency,address,null,trip_distance,(float)order.user.rate,order.user.call_name);
      }
      if(bookingStyle==BOOKING_STYLE_TRIP)updateTipMessage(context.getString(R.string.riding_tip));
      else if(bookingStyle==BOOKING_STYLE_DELIVERY)updateTipMessage(context.getString(R.string.delivering_tip));
    }
  };
  public TypedCallback saveCurrentOrderABCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      ArrayList<OrderAB> order_list=(ArrayList)obj;
      if(order_list.size()>0)currentOrderAB=order_list.get(0);//one of order
    }
  };
  public TypedCallback showOrderABCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      //if(obj==null)return;
      ArrayList<OrderAB> order_list=(ArrayList)obj;
      if(order_list.size()>0)currentOrderAB=order_list.get(0);//one of order
      //temp for debug
      if(DEBUG)Toast.makeText(context.getApplicationContext(),currentOrderAB!=null?currentOrderAB.status_name+T.SPACE+currentOrderAB.id:"currentOrder empty",Toast.LENGTH_SHORT).show();
      //current order
      if(currentOrderAB!=null){
        if(driverTransportId!=-1&&currentOrderAB.transport_id==driverTransportId&&
           currentOrderAB.status_id>0&&currentOrderAB.status_id<OrderABStatus.ORDER_STATUS_COMPLETED){//driver check the trip
          putTripMode(true);
          editor.commit();
        }
        else{
          //remove trip mode
          if(tripMode){//force-major
            if(!mapStarted){
              currentOrderAB=null;          
              putTripMode(false);
              clearTripOrder();//commit all   
              //re-recv order for client
              if(orderId!=-1)getOrderABRequest(showOrderABCallbackHandler,orderId,-1,-1);//recv by id
              else getMaxOrderABRequest(showOrderABCallbackHandler);//lastOrder
            }
            else{
              currentOrderAB=null;
              RetVal ret_val=new RetVal();
              ret_val.value=0;
              ret_val.temp_id=-1;
              ret_val.temp_long_value=-1;
              ret_val.temp_long_value_2=-1;
              afterUpdateOrderABTransportCallbackHandler.execute(ret_val);
            }
            return;//go away
          }
        }
        //trip mode
        if(tripMode){//driver mode
          mapMode=MAP_DRIVER;
          //distance
          calcDistance=(currentOrderAB.status_id==OrderABStatus.ORDER_STATUS_DELIVERING);
          //trip order
          putTripOrderId(currentOrderAB.id);
          putTripOrderUserId(currentOrderAB.user_id);
          putTripOrderTime(getDatetimeMilisec(currentOrderAB.create_date));
          putTripOrderStatusId(currentOrderAB.status_id);
          putTripOrderStatusName(currentOrderAB.status_name);
          editor.commit();
          //circleUser
          long circle_user_id=circleUserId;
          if(circle_user_id==-1){
            addSensorCircleToUserRequest(currentOrderAB.user_id);
          }
          else if(circle_user_id!=currentOrderAB.user_id){//force-major(leave old circle)
            removeSensorCircleToUserRequest(circle_user_id);
            addSensorCircleToUserRequest(currentOrderAB.user_id);
          }
          //service
          startDispatcherService(mapMode);
          //google map
          if(!mapStarted){
            showMapFragment(new Integer(Map2Fragment.CALLBACK_COMMAND_DRIVER),mapProvider);
          }
          else{
            //preview client
            Fragment frag=getFragment(T.FRAGMENT_NAME_MAP);
            if(frag!=null){
              ((Map2Fragment)frag).closeOrderNearTimeStarter();//if started
              ((Map2Fragment)frag).showClientLocationCallbackHandler.execute(order_list);
              //trip route
              ((Map2Fragment)frag).showTripRoute(currentOrderAB);
              //tipFragment
              if(bookingStyle==BOOKING_STYLE_EASY||bookingStyle==BOOKING_STYLE_EASY_AB);//nothing
              else updateTipMessage(currentOrderAB.status_id);
            }
          }
          //goto taximeter fragment
          showTaximeterFragmentCallbackHandler.execute(currentOrderAB);
        }
        else{//client mode
          mapMode=MAP_CLIENT;
          if(currentOrderAB.status_id>0&&currentOrderAB.status_id<OrderABStatus.ORDER_STATUS_COMPLETED){//order actual
            //order
            putOrderId(currentOrderAB.id);
            putOrderTime(getDatetimeMilisec(currentOrderAB.create_date));
            putOrderStatusId(currentOrderAB.status_id);
            putOrderStatusName(currentOrderAB.status_name);
            if(currentOrderAB.transport!=null){
              putTransportId(currentOrderAB.transport.id);
              putTransportName(currentOrderAB.transport.name);
              putTransportColor(currentOrderAB.transport.color);
              putTransportLicensePlate(currentOrderAB.transport.license_plate);
              putTransportRate(String.valueOf(currentOrderAB.transport.rate));
              putTransportSensorId(currentOrderAB.transport.sensor_id);
              if(currentOrderAB.transport.sensor!=null){
                putTransportSensorName(currentOrderAB.transport.sensor.name);
                putTransportSensorPhone(currentOrderAB.transport.sensor.phone);
                putTransportSensorUserId(currentOrderAB.transport.sensor.user_id);
                if(currentOrderAB.transport.sensor.user!=null){
                  putTransportSensorUserCallname(currentOrderAB.transport.sensor.user.call_name);
                  putTransportSensorUserPhone(currentOrderAB.transport.sensor.user.phone);
                }
                //circleSensor
                long circle_sensor_id=circleSensorId;
                if(circle_sensor_id==-1){
                  addSensorCircleRequest(currentOrderAB.transport.sensor_id);
                }
                else if(circle_sensor_id!=currentOrderAB.transport.sensor_id){//force-major(leave old circle)
                  removeSensorCircleRequest(circle_sensor_id);
                  addSensorCircleRequest(currentOrderAB.transport.sensor_id);
                }
              }
              else{
                clearTransportSensor();
              }
            }
            else{
              clearTransport();
              clearTransportSensor();
            }
            if(currentOrderAB.statusAttrList!=null){
              Attr attr=currentOrderAB.statusAttrList.get(0);
              putOrderStatusValue(attr.value);
            }
            editor.commit();
            //service
            startDispatcherService(mapMode);
          }
          else{//order completed or cancelled
            clearOrder();
            clearTransport();
            clearTransportSensor();
          }

          //start map for tap
          if(!mapStarted){
            showMapFragment(new Integer(Map2Fragment.CALLBACK_COMMAND_CLIENT),mapProvider);
          }
          else{
            //statusFragment
            showOrderABStatusCallbackHandler.execute(null);
            //tipFragment
            if(bookingStyle==BOOKING_STYLE_EASY||bookingStyle==BOOKING_STYLE_EASY_AB);//nothing
            else updateTipMessage(currentOrderAB.status_id);
            //tracker
            Fragment frag=getFragment(T.FRAGMENT_NAME_MAP);
            if(frag!=null)((Map2Fragment)frag).startTracker();
          }
          if(bookingStyle==BOOKING_STYLE_EASY||bookingStyle==BOOKING_STYLE_EASY_AB){
            if(currentOrderAB.transport!=null&&currentOrderAB.status_id>=OrderABStatus.ORDER_STATUS_PROCESSED&&
                                               currentOrderAB.status_id<OrderABStatus.ORDER_STATUS_COMPLETED){//ride now
              //close on driver taken
              easyBooking=EASY_BOOKING_RIDE;
              removeFragment(T.FRAGMENT_NAME_DROPOFF);
              if(!isOpenFragment(T.FRAGMENT_NAME_RIDE))showRideFragmentCallbackHandler.execute(null);
            }
          }
        }
      }
      else{//no orders
        if(tripMode){
          putTripMode(false);
          clearTripOrder();//commit all
        }
        else{//if order removed
          clearOrder();
          clearTransport();
          clearTransportSensor();
          clearDropoff();
        }
        //booking
        if(bookingStyle==BOOKING_STYLE_DELIVERY){
          //no start map
        }
        else if(bookingStyle==BOOKING_STYLE_TRIP||bookingStyle==BOOKING_STYLE_EASY||bookingStyle==BOOKING_STYLE_EASY_AB){
          //start map for tap
          if(!mapStarted){
            showMapFragment(new Integer(Map2Fragment.CALLBACK_COMMAND_CLIENT),mapProvider);
          }
        }
      }

      //fab
      fabAction(mapMode);
    }
  };
  public TypedCallback showOrderABTransportCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      ArrayList<OrderAB> order_list=(ArrayList)obj;
      OrderAB order=null;
      if(order_list.size()>0)order=order_list.get(0);//one of order
      //temp for debug
      if(DEBUG)Toast.makeText(context.getApplicationContext(),order!=null?order.status_name+T.SPACE+order.id:"transportOrder empty",Toast.LENGTH_SHORT).show();
      if(order!=null&&(order.status_id>0&&order.status_id!=OrderABStatus.ORDER_STATUS_COMPLETED))
        makeOrderDialog(order);/*actual order for the driver*/
    }
  };
  public TypedCallback findDefaultProductCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      defaultProduct=findDefaultProduct();
    }
  };
  public TypedCallback showUserCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      currentUser=findCurrentUser();
      if(currentUser!=null){
        putUserId(currentUser.id);
        putUsertype(currentUser.type);
        putCallname(currentUser.call_name);
        putFirstname(currentUser.first_name);
        putLastname(currentUser.last_name);
        putEmail(currentUser.email);
        putPhone(currentUser.phone);
        editor.commit();
        //user in BAN
        if(!currentUser.active)banDialog();
      }
      //temp for debug
      if(DEBUG)Toast.makeText(context.getApplicationContext(),currentUser!=null?currentUser.call_name:"currentUser empty",Toast.LENGTH_SHORT).show();
    }
  };
  public TypedCallback showTaxCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      presetFee();
    }
  };
  public TypedCallback showCurrencyCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      activeCurrency=findActiveCurrency();
      presetActiveCurrencyRequisites();
      //temp for debug
      if(DEBUG)Toast.makeText(context.getApplicationContext(),activeCurrency!=null?activeCurrency.name:"activeCurrency empty",Toast.LENGTH_SHORT).show();
    }
  };
  public TypedCallback showTransportCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      Transport transport=(Transport)obj;
      if(transport!=null&&transport.id>0){//transport.id default 0 as Java primitive (long)
        driverTransport=transport;
        putDriverTransportId(driverTransport.id);
        putDriverTransportName(driverTransport.name);
        putDriverTransportColor(driverTransport.color);
        putDriverTransportLicensePlate(driverTransport.license_plate);
        putDriverTransportRate(String.valueOf(driverTransport.rate));
        editor.commit();
        //driver mode
        fabAction(-1);
      }
      else if(TYPE_OF_APP==DRIVER_APP){//open menu Settings -> DriverProfile
        showDriverActivity();
      }
      //temp for debug
      if(DEBUG)Toast.makeText(context.getApplicationContext(),driverTransport!=null?driverTransport.name:"driverTransport empty",Toast.LENGTH_SHORT).show();
    }
  };
  public TypedCallback showTransportReviewCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      TransportReview transport_review=(TransportReview)obj;
      if(transport_review!=null){
        String transport=(transport_review.transport!=null?transport_review.transport.name+T.SPACE+transport_review.transport.license_plate:transport_review.transport_name);
        //dialog for client or driver for preview
        if(mapMode==MAP_CLIENT||(mapMode==MAP_DRIVER&&transport_review.review_value!=-1))
          makeTripRatingDialog(transport_review.order_id,transport_review.user_id,transport_review.transport_id,transport_review.review_value,transport_review.description,getTalkingDatetimeMessage(transport_review.last_update),transport);
      }
    }
  };
  public TypedCallback showColorCallbackHandler=new TypedCallback<Object>(){//in DriverActivity?
    @Override
    public void execute(Object obj){
      //temp for debug
      if(DEBUG)Toast.makeText(context.getApplicationContext(),colorList!=null?"color="+colorList.size():"color empty",Toast.LENGTH_SHORT).show();
    }
  };
  public TypedCallback showUsertypeCallbackHandler=new TypedCallback<Object>(){//in DriverActivity?
    @Override
    public void execute(Object obj){
      //temp for debug
      if(DEBUG)Toast.makeText(context.getApplicationContext(),"usertype="+usertype,Toast.LENGTH_SHORT).show();

      if(usertype>0){
        //sensor
        addSensorRequest();//add transport after
        //get user,token,currency,product,order and show google map
        showSplashActivityUntilDoRequestCallbackHandler.execute(null);//login to
      }
      else showErrorFragment(context.getString(R.string.user_error));//user deactivated or bad usertype
    }
  };
  public TypedCallback showTokenCallbackHandler=new TypedCallback<Object>(){//in DriverActivity?
    @Override
    public void execute(Object obj){
      //temp for debug
      if(DEBUG)Toast.makeText(context.getApplicationContext(),token!=null?"token="+token:T.EMPTY,Toast.LENGTH_SHORT).show();
      //if security high (remove this)
      putToken(token);
      editor.commit();
    }
  };
  public TypedCallback showDataCallbackHandler=new TypedCallback<Object>(){//in DriverActivity?
    @Override
    public void execute(Object obj){
      if(userList!=null&&currencyList!=null&token!=null){
        showUserCallbackHandler.execute(userList);
        showCurrencyCallbackHandler.execute(currencyList);
        showTokenCallbackHandler.execute(token);
        //get products after data
        if(bookingStyle==BOOKING_STYLE_DELIVERY);//getProductCallbackHandler.execute(new Integer(0));//page_num
        else if(bookingStyle==BOOKING_STYLE_TRIP||bookingStyle==BOOKING_STYLE_EASY_AB)getProductRequest();//all product
        else if(bookingStyle==BOOKING_STYLE_EASY);//nothing
        //registered
        if(!registered){putRegistered(true);editor.commit();}
      }
      //splash
      if(splashDialog!=null){splashDialog.dismiss();splashDialog=null;}
    }
  };

  //after
  public TypedCallback afterAddUserCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      long id=ret_val.value;
      if(id>0){
        putUserId(id);
        putRegistered(true);
        putTripMode(false);
        putToken(null);
        //clear all (&commit all)
        clearOrder();
        clearTransport();
        clearTransportSensor();
        clearTripOrder();
        clearDriverTransport();
        clearLandingAndPickup();//commit all
        //remove all login's Fragments
        removeFragment(T.FRAGMENT_NAME_START);
        removeFragment(T.FRAGMENT_NAME_REGISTER);
        removeFragment(T.FRAGMENT_NAME_SIGNIN);
        removeFragment(T.FRAGMENT_NAME_SIGNUP);
        //sensor
        addSensorRequest();//add transport after
        //get user,token,currency,product,order and show google map
        showSplashActivityUntilDoRequestCallbackHandler.execute(null);
      }
      else if(String.valueOf(ret_val.value).equals(RemoteStorage.RETURN_CODE_USER_ALREADY_EXIST)){
        showErrorFragment(context.getString(R.string.message_user_profile_error));//already exist
      }
    }
  };
  //call from driverActivity
  public TypedCallback afterUpdateUserCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){//success
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"User updated success",Toast.LENGTH_SHORT).show();
        editor.commit();
        if(ret_val.temp_object!=null){
          ((Activity)ret_val.temp_object).setResult(Activity.RESULT_OK,new Intent().putExtra(INTENT_PARAM_MESSAGE,context.getString(R.string.message_user_profile_updated)));
          ((Activity)ret_val.temp_object).finish();
        }
      }
    }
  };
  //call from driverActivity
  public TypedCallback afterUpdateUserPictureCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){//success
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"User picture updated success",Toast.LENGTH_SHORT).show();
      }
    }
  };
  //call from driverActivity
  public TypedCallback afterUpdatePictureCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){//success
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"Picture updated success",Toast.LENGTH_SHORT).show();
      }
    }
  };
  public TypedCallback afterAddOrderABCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      long id=ret_val.value;
      if(id>0){
        currentOrderAB=null;//clear old order
        putOrderId(id);
        putOrderTime(System.currentTimeMillis());
        editor.commit();

        //close fragments
        if(bookingStyle==BOOKING_STYLE_EASY){
          removeFragment(T.FRAGMENT_NAME_FIND);//close find
        }
        else{
          //remove all fragments, without map
          removeAllWithoutMapFragment();
        }

        //map
        showMapCallbackHandler.execute(null);

        if(bookingStyle==BOOKING_STYLE_EASY_AB){
          //map address
          Fragment frag=getFragment(T.FRAGMENT_NAME_MAP);
          if(frag!=null)((Map2Fragment)frag).stopAddress();
        }

        //new tip about service
        if(!doNotShow){
          if(bookingStyle==BOOKING_STYLE_DELIVERY)showTipFragment(context.getString(R.string.order_delivery_reminder_tip),-1);
          else if(bookingStyle==BOOKING_STYLE_TRIP)showTipFragment(context.getString(R.string.order_trip_reminder_tip),-1);
          else if(bookingStyle==BOOKING_STYLE_EASY||bookingStyle==BOOKING_STYLE_EASY_AB);//no tips for easy booking
        }

        //dropoff
        if(bookingStyle==BOOKING_STYLE_EASY){
          Fragment frag=getFragment(T.FRAGMENT_NAME_DROPOFF);
          if(frag!=null){
            ((DropoffFragment)frag).enabledBookNowButton(false);
            ((DropoffFragment)frag).enabledPaymentButton(true);
            ((DropoffFragment)frag).enabledEditTextOrderTime(false);
          }
        }

        //orderStatus
        String str=String.format(context.getString(R.string.order_number),id)+T.NEXT_LINE+context.getString(R.string.order_status_waiting);
        showOrderStatusFragment(str,-1,true,showLogo);
        //add orderPart
        if(bookingStyle==BOOKING_STYLE_DELIVERY&&cartList!=null&&cartList.size()>0){
          for(Product item: cartList)addOrderABPartRequest(item,item.cart_count);
        }
        else if(bookingStyle==BOOKING_STYLE_TRIP||bookingStyle==BOOKING_STYLE_EASY||bookingStyle==BOOKING_STYLE_EASY_AB){
          Product product=selectedProduct!=null?selectedProduct:defaultProduct;
          if(product!=null)addOrderABPartRequest(product,1);//one product in order
        }

        //clear cart list
        if(cartList!=null){
          cartList.clear();
          cartList=null;
        }
        //clean product list
        uncheckedProductList();

        //service
        startDispatcherServiceCallbackHandler.execute(new Integer(MAP_CLIENT));//client with vibr
      }
    }
  };
  public TypedCallback afterAddOrderABPartCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0&&ret_val.temp_object!=null){
        Product product=(Product)ret_val.temp_object;
        if(product.paramList!=null){
          ProductParam product_param;
          Iterator iter=product.paramList.iterator();
          while(iter.hasNext()){
            product_param=(ProductParam)iter.next();
            if(product_param.checked)addOrderABProductParamPartRequest(product.id,product_param);
          }
        }
      }
    }
  };
  public TypedCallback afterAddOrderABProductParamPartCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      //nothing ?
      //RetVal ret_val=(RetVal)obj;
      //if(ret_val.value==0){//success
      //}
    }
  };
  public TypedCallback afterAddPurchaseCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      long id=ret_val.value;
      if(id>0||id==-3/*access not found*/){//close the trip order
        //nothing
      }
    }
  };
  public TypedCallback afterAddPaymentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      long id=ret_val.value;
      if(id>=0){
        removeFragment(T.FRAGMENT_NAME_PAYMENT);
        showSuccessFragment(context.getString(R.string.message_success_payment));
      }
    }
  };
  public TypedCallback afterAddSensorCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      long id=ret_val.value;
      if(id>0){
        putSensorId(id);
        editor.commit();
        getTransportRequest(id);
      }
    }
  };
  public TypedCallback afterAddTrackCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      long id=ret_val.value;
      if(id>0){
        putTrackId(id);
        editor.commit();
      }
    }
  };
  public TypedCallback afterAddSensorCircleCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){
        long sensor_id=ret_val.temp_long_value;//temporary saved sensor_id value
        putCircleSensorId(sensor_id);
        editor.commit();
      }
    }
  };
  public TypedCallback afterAddSensorCircleToUserCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){
        long user_id=ret_val.temp_long_value;//temporary saved user_id value
        putCircleUserId(user_id);
        editor.commit();
      }
    }
  };
  public TypedCallback afterRemoveSensorCircleCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){
        putCircleSensorId(-1);
        editor.commit();
      }
    }
  };
  public TypedCallback afterRemoveSensorCircleToUserCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){
        putCircleUserId(-1);
        editor.commit();
      }
    }
  };
  //call from driverActivity
  public TypedCallback afterAddTransportCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      long id=ret_val.value;
      if(id>0){
        putDriverTransportId(id);
        editor.commit();
        if(ret_val.temp_object!=null){
          ((Activity)ret_val.temp_object).setResult(Activity.RESULT_OK,new Intent().putExtra(INTENT_PARAM_MESSAGE,context.getString(R.string.message_transport_profile_created)));
          ((Activity)ret_val.temp_object).finish();
        }
      }
    }
  };
  public TypedCallback afterAddTransportReviewCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){
        showSuccessFragment(context.getString(R.string.message_review_created));
      }
    }
  };
  public TypedCallback afterAddUserReviewCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){
        showSuccessFragment(context.getString(R.string.message_review_created));
      }
    }
  };

  //call from driverActivity
  public TypedCallback afterUpdateTransportCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"Driver transport updated",Toast.LENGTH_SHORT).show();
        if(ret_val.temp_object!=null){
          ((Activity)ret_val.temp_object).setResult(Activity.RESULT_OK,new Intent().putExtra(INTENT_PARAM_MESSAGE,context.getString(R.string.message_transport_profile_updated)));
          ((Activity)ret_val.temp_object).finish();
        }
      }
    }
  };
  public TypedCallback afterUpdateOrderABStatusCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(DEBUG)Toast.makeText(context.getApplicationContext(),"Order status updated",Toast.LENGTH_SHORT).show();
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){
        long order_id=ret_val.temp_id;//temporary saved Id value
        getOrderABRequest(showOrderABCallbackHandler,order_id,-1,-1);
      }
    }
  };
  public TypedCallback afterUpdateOrderABTransportCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(DEBUG)Toast.makeText(context.getApplicationContext(),"Driver take the Order",Toast.LENGTH_SHORT).show();
      RetVal ret_val=(RetVal)obj;
      long order_id=ret_val.temp_id;//temporary saved orderId value
      long transport_id=ret_val.temp_long_value;//temporary saved transportId value
      long order_user_id=ret_val.temp_long_value_2;//temporary saved orderUserId value
      if(ret_val.value==0||(ret_val.value==-3/*access not found*/&&transport_id==-1/*force_major*/)){
        //no need to set status==processed (server set automatically in db)
        //updateOrderABStatusRequest(order_id,OrderABStatus.ORDER_STATUS_PROCESSED);
        //set to a tripMode after order transport update success!
        //currentOrder is a current work data for tripMode(tripOrderId) or Client's booked(orderId)
        if(transport_id!=-1){
          putTripMode(true);
          putTripOrderId(order_id);
          putTripOrderUserId(order_user_id);
          editor.commit();
          //close all
          removeAllWithoutMapFragment();
          //reset currentOrder(with tripOrderId)
          getOrderABRequest(showOrderABCallbackHandler,order_id,-1,-1);
        }
        else{//close the trip order
          closeTripMode();
        }
      }
    }
  };
  public TypedCallback afterUpdateSensorActivityCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){//success
        if(ret_val.temp_long_value==1)sensorActivated=true;
        else sensorActivated=false;
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"Sensor activated="+sensorActivated,Toast.LENGTH_SHORT).show();
      }
    }
  };

  public TypedCallback afterPasswordRecoveryCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){//success
        passwordRecoveryMessage();
      }
    }
  };
  public TypedCallback afterOrderInvoiceCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){//success
      }
    }
  };
  public TypedCallback afterIncreaseUserPrepaidAmountCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){//success
        increasePrepaidAccountMessage();
        //update slide panel user account value!
        getUserRequest();
      }
    }
  };
  public TypedCallback afterAddMessageCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      long id=ret_val.value;
      if(id>0){
        Fragment frag=getFragment(T.FRAGMENT_NAME_MESSAGE_LIST);
        if(frag!=null){
          ArrayList list=((ListFragment)frag).getItemList();
          if(list!=null&&list.size()>0){
            int last_index=list.size()-1;
            MessageListContent.Item item=(MessageListContent.Item)list.get(last_index);
            getMessageRequest(showNewMessageListCallbackHandler,item.id);
          }
          else getMessageCallbackHandler.execute(new Integer(0));//page_num
        }
      }
    }
  };

  //doActions
  public TypedCallback startDispatcherServiceCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      //vibrate
      doVibrate(T.TASK_VIBRATE_3_COUNT);
      //service
      Integer value=(Integer)obj;
      startDispatcherService(value.intValue());
    }
  };
  public TypedCallback doVibrateCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      ((Vibrator)obj).vibrate(T.TASK_VIBRATE_TIMEOUT);
    }
  };
  //for client for driver transport
  public TypedCallback doTransportTrackerCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(transportSensorId!=-1)
        getMaxTrackRequest(transportSensorId,(TypedCallback<Object>)obj,new Track());
    }
  };
  public TypedCallback doOrderNearCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      String distance=getDistance();
      if(distance!=null&&!distance.equals(NO_VALUE)){
        String lat=getLatitude(),lon=getLongitude();
        //seek if my location found
        if(lat!=null&&lon!=null)getOrderABNearRequest((TypedCallback<Object>)obj,OrderABStatus.ORDER_STATUS_ACCEPTED,lat,lon,distance,T.MAP_DISTANCE_KM);
        //if start in handler test
        //if(DEBUG)Toast.makeText(context.getApplicationContext(),"distance="+distance,Toast.LENGTH_SHORT).show();
      }
      else getOrderABByStatusRequest((TypedCallback<Object>)obj,OrderABStatus.ORDER_STATUS_ACCEPTED);
    }
  };

  public void startDispatcherService(int map_mode){
    Intent service_intent=new Intent(context,DispatcherService.class);
    service_intent.setFlags(Intent.FLAG_FROM_BACKGROUND);
    service_intent.putExtra(DispatcherService.INTENT_PARAM_ACTIVATE,true);
    service_intent.putExtra(DispatcherService.INTENT_PARAM_MAP_MODE,map_mode);
    context.startService(service_intent);
  }
  public void stopDispatcherService(){
    Intent service_intent=new Intent(context,DispatcherService.class);
    service_intent.setFlags(Intent.FLAG_FROM_BACKGROUND);
    service_intent.putExtra(DispatcherService.INTENT_PARAM_ACTIVATE,false);
    context.startService(service_intent);
  }
  public TimeStarter doVibrate(int time_count){
    Vibrator vibr=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    return timeStarter(doVibrateCallbackHandler,vibr,time_count);
  }
  public void closeTripMode(){
    putTripMode(false);
    clearTripOrder();//commit all
    clearLandingAndPickup();//commit all
    //goto driver(map must be in map_driver mode)
    removeFragment(T.FRAGMENT_NAME_TIP);
    removeFragment(T.FRAGMENT_NAME_TAXIMETER);
    Map2Fragment frag=(Map2Fragment)getFragment(T.FRAGMENT_NAME_MAP);
    if(frag!=null)frag.startDriver();
    startDispatcherService(mapMode);//for check tripMode in service
    //client's order preset(need to reset currentOrder with orderId)
    if(orderId!=-1)getOrderABRequest(saveCurrentOrderABCallbackHandler,orderId,-1,-1);
  }

  //updates
  public void updateTripFragmentPrice(Product product){
    double order_price=calcOrder(currentDirection);
    orderPrice=order_price;
    double total_price=getTotalPrice(order_price,product.paramList);
    totalPrice=total_price;
    Fragment frag=getFragment(T.FRAGMENT_NAME_TRIP);
    String price_text=getPrice(total_price);
    if(frag!=null)((TripFragment)frag).updatePrice(price_text);
  }
  public void updateCartFragmentPrice(ArrayList<Product> product_list){
    Product product;
    double order_price=calcOrder(product_list);
    orderPrice=order_price;
    double total_price=order_price,price;
    Iterator iter=product_list.iterator();
    while(iter.hasNext()){
      product=(Product)iter.next();
      if(product.checked){
        price=getProductParamPrice(product.paramList);
        if(product.cart_count>1)price=price*product.cart_count;
        total_price+=price;
      }
    }
    totalPrice=total_price;
    Fragment frag=getFragment(T.FRAGMENT_NAME_CART);
    String price_text=getPrice(total_price);
    if(frag!=null)((CartFragment)frag).updatePrice(price_text);
  }
  public void updateTipMessage(long status_id){
    if(!doNotShow){
      String tip_message=getTipMessageByOrderStatus(status_id);
      Fragment frag=getFragment(T.FRAGMENT_NAME_TIP);
      if(frag!=null)((TipFragment)frag).updateTipMessage(tip_message);
      else showTipFragment(tip_message,-1);
    }
  }
  public void updateTipMessage(String tip_message){
    if(!doNotShow){
      Fragment frag=getFragment(T.FRAGMENT_NAME_TIP);
      if(frag!=null)((TipFragment)frag).updateTipMessage(tip_message);
      else showTipFragment(tip_message,-1);
    }
  }

  public void updateProductForOrder(long id){
    Product product=findProduct(id);
    setSelectedProduct(product);
    updateTripFragmentPrice(product);
  }

  //networkRequest
  private ProgressBar progressBar;
  public void setProgressBar(ProgressBar progress_bar){progressBar=progress_bar;}
  public void getDefaultData(){//default data: user,currencies,products
    getDataRequest(showDataCallbackHandler,null);//get User,Currency,Token
    /*getUserRequest();
    getTokenRequest();
    getCurrencyRequest();*/
    //get products after data
  }
  public void addressFindRequest(Fragment fragment){
    View fragment_view=fragment.getView();
    if(fragment_view!=null){
      String value;
      EditText edit_text=(EditText)fragment_view.findViewById(R.id.edit_text_pickup_address);
      if(edit_text!=null){
        value=edit_text.getText().toString();
        if(value.isEmpty()){
          edit_text.setHintTextColor(resources.getColor(R.color.bad_text));
          edit_text.requestFocus();
          return;
        }else edit_text.setTextColor(resources.getColor(R.color.good_text));
        putOrigin(value);
      }
      edit_text=(EditText)fragment_view.findViewById(R.id.edit_text_destination_address);
      if(edit_text!=null){
        value=edit_text.getText().toString();
        if(value.isEmpty()){
          edit_text.setHintTextColor(resources.getColor(R.color.bad_text));
          edit_text.requestFocus();
          return;
        }else edit_text.setTextColor(resources.getColor(R.color.good_text));
        putDestination(value);
      }
      editor.commit();
      googleDirections(Manager.DIRECTIONS_ROUTE_ADDRESS);
      //close keyboard
      InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(edit_text.getWindowToken(),0);
      //remove findFragment (and tripFragment)
      removeFragment(fragment);
      removeFragment(T.FRAGMENT_NAME_TRIP);
    }
  }
  public void passwordRecoveryRequest(String username){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    param_list.add(T.JSON_PARAM_USERNAME);
    value_list.add(username);

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_PASSWORD_RECOVERY,getHostname(),param_list,value_list,afterPasswordRecoveryCallbackHandler,new RetVal());
  }
  public void orderInvoiceRequest(long order_id){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_ORDER_ID);
    value_list.add(String.valueOf(order_id));

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_PASSWORD_RECOVERY,getHostname(),param_list,value_list,afterOrderInvoiceCallbackHandler,new RetVal());
  }
  public void getStatRequest(){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_STAT,getHostname(),param_list,value_list,showStatFragmentCallbackHandler,new Stat());
  }

  public void addUserRequest(){signupRequest(fragment,false);}
  public void signupRequest(Fragment fragment,boolean is_demo){//addUserRequest
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    EditText edit_text;
    String value;
    View fragment_view=fragment.getView();
    if(fragment_view!=null){
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(TYPE_OF_APP==DRIVER_APP?RemoteStorage.ADMIN_USERNAME:RemoteStorage.DEFAULT_USERNAME);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(TYPE_OF_APP==DRIVER_APP?RemoteStorage.ADMIN_PASSWORD:RemoteStorage.DEFAULT_PASSWORD);
      param_list.add(T.JSON_PARAM_USERTYPE);
      value_list.add(TYPE_OF_APP==DRIVER_APP?RemoteStorage.WORKER_USERTYPE:RemoteStorage.DEFAULT_USERTYPE);

      if(!is_demo){
        //username
        edit_text=(EditText)fragment_view.findViewById(R.id.edit_text_phone);
        if(edit_text!=null){
          value=edit_text.getText().toString();
          if(value.isEmpty()){
            edit_text.setHintTextColor(resources.getColor(R.color.bad_text));
            edit_text.requestFocus();
            return;
          }else edit_text.setTextColor(resources.getColor(R.color.good_text));

          param_list.add(T.JSON_PARAM_NEW_USERNAME);
          value_list.add(value);
          param_list.add(T.JSON_PARAM_PHONE);
          value_list.add(value);
          //one phone number to username,callname,phone
          putUsername(value);
          putCallname(value);
          putPhone(value);

        }
        //other can be found
        edit_text=(EditText)fragment_view.findViewById(R.id.edit_text_callname);
        if(edit_text!=null&&edit_text.getText().length()>0){
          value=edit_text.getText().toString();
          param_list.add(T.JSON_PARAM_CALLNAME);
          value_list.add(value);
          putCallname(value);
        }
        else{//phone already found
          param_list.add(T.JSON_PARAM_CALLNAME);
          value_list.add(getPhone());
        }
        edit_text=(EditText)fragment_view.findViewById(R.id.edit_text_firstname);
        if(edit_text!=null){
          value=edit_text.getText().toString();
          param_list.add(T.JSON_PARAM_FIRSTNAME);
          value_list.add(value);
          putFirstname(value);
        }
        edit_text=(EditText)fragment_view.findViewById(R.id.edit_text_lastname);
        if(edit_text!=null){
          value=edit_text.getText().toString();
          param_list.add(T.JSON_PARAM_LASTNAME);
          value_list.add(value);
          putLastname(value);
        }
        edit_text=(EditText)fragment_view.findViewById(R.id.edit_text_email);
        if(edit_text!=null){
          value=edit_text.getText().toString();
          param_list.add(T.JSON_PARAM_EMAIL);
          value_list.add(value);
          putEmail(value);
        }
        //password
        edit_text=(EditText)fragment_view.findViewById(R.id.edit_text_password);
        if(edit_text!=null){
          value=edit_text.getText().toString();
          if(value.isEmpty()){
            edit_text.setHintTextColor(resources.getColor(R.color.bad_text));
            edit_text.requestFocus();
            return;
          }else edit_text.setTextColor(resources.getColor(R.color.good_text));
          param_list.add(T.JSON_PARAM_NEW_PASSWORD);
          value_list.add(value);
          putPassword(value);
        }
      }
      else{//demo
        String user=RemoteStorage.DEMO_USERNAME,pass=RemoteStorage.DEMO_PASSWORD,phone=getPhoneNumber();
        param_list.add(T.JSON_PARAM_NEW_USERNAME);
        value_list.add(user);
        param_list.add(T.JSON_PARAM_CALLNAME);
        value_list.add(user);
        param_list.add(T.JSON_PARAM_PHONE);
        value_list.add(phone);
        param_list.add(T.JSON_PARAM_NEW_PASSWORD);
        value_list.add(pass);
        putUsername(user);
        putCallname(user);
        putPhone(phone);
        putPassword(pass);
      }
      //no commit, if success it commit with all
      //editor.commit();

      remoteStorage=new RemoteStorage(this,progressBar);
      remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_USER,getHostname(),param_list,value_list,afterAddUserCallbackHandler,new RetVal());
    }
  }
  public void updateUserRequest(Activity activity){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    EditText edit_text;
    String value;
    if(activity!=null&&userId!=-1){
      if(token!=null){
        param_list.add(T.JSON_PARAM_TOKEN);
        value_list.add(token);
      }
      else{
        param_list.add(T.JSON_PARAM_USERNAME);
        value_list.add(username);
        param_list.add(T.JSON_PARAM_PASSWORD);
        value_list.add(password);
      }
      param_list.add(T.JSON_PARAM_USER_ID);
      value_list.add(String.valueOf(userId));
      param_list.add(T.JSON_PARAM_USERTYPE);
      value_list.add(String.valueOf(usertype));

      edit_text=(EditText)activity.findViewById(R.id.edit_text_firstname);
      if(edit_text!=null){
        value=edit_text.getText().toString();
        param_list.add(T.JSON_PARAM_FIRSTNAME);
        value_list.add(value);
        putFirstname(value);
      }
      edit_text=(EditText)activity.findViewById(R.id.edit_text_lastname);
      if(edit_text!=null){
        value=edit_text.getText().toString();
        param_list.add(T.JSON_PARAM_LASTNAME);
        value_list.add(value);
        putLastname(value);
      }
      edit_text=(EditText)activity.findViewById(R.id.edit_text_callname);
      if(edit_text!=null){
        value=edit_text.getText().toString();
        param_list.add(T.JSON_PARAM_CALLNAME);
        value_list.add(value);
        putCallname(value);
      }
      edit_text=(EditText)activity.findViewById(R.id.edit_text_email);
      if(edit_text!=null){
        value=edit_text.getText().toString();
        param_list.add(T.JSON_PARAM_EMAIL);
        value_list.add(value);
        putEmail(value);
      }
      edit_text=(EditText)activity.findViewById(R.id.edit_text_phone);
      if(edit_text!=null){
        value=edit_text.getText().toString();
        param_list.add(T.JSON_PARAM_PHONE);
        value_list.add(value);
        putPhone(value);
      }

      /*edit_text=(EditText)activity.findViewById(R.id.edit_text_password);
      if(edit_text!=null){
        value=edit_text.getText().toString();
        param_list.add(T.JSON_PARAM_NEW_PASSWORD);
        value_list.add(value);
        putPassword(value);
      }*/

      RetVal ret_val=new RetVal();
      ret_val.temp_object=activity;
      remoteStorage=new RemoteStorage(this,progressBar);
      remoteStorage.jsonRequest(RemoteStorage.COMMAND_UPDATE_USER,getHostname(),param_list,value_list,afterUpdateUserCallbackHandler,ret_val);
    }
  }
  public void updateUserPictureRequest(byte[] picture){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(picture!=null){
      if(token!=null){
        param_list.add(T.JSON_PARAM_TOKEN);
        value_list.add(token);
      }
      else{
        param_list.add(T.JSON_PARAM_USERNAME);
        value_list.add(username);
        param_list.add(T.JSON_PARAM_PASSWORD);
        value_list.add(password);
      }
      param_list.add(T.JSON_PARAM_USER_ID);
      value_list.add(String.valueOf(userId));
      param_list.add(T.JSON_PARAM_PICTURE);
      value_list.add(Base64.encodeToString(picture,Base64.NO_WRAP));

      remoteStorage=new RemoteStorage(this,progressBar);
      remoteStorage.jsonRequest(RemoteStorage.COMMAND_UPDATE_USER_PICTURE,getHostname(),param_list,value_list,afterUpdateUserPictureCallbackHandler,new RetVal());
    }
  }
  public void updatePictureRequest(byte[] picture,long picture_id,String object_name){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(picture!=null){
      if(token!=null){
        param_list.add(T.JSON_PARAM_TOKEN);
        value_list.add(token);
      }
      else{
        param_list.add(T.JSON_PARAM_USERNAME);
        value_list.add(username);
        param_list.add(T.JSON_PARAM_PASSWORD);
        value_list.add(password);
      }
      param_list.add(T.JSON_PARAM_PICTURE_ID);
      value_list.add(String.valueOf(picture_id));
      param_list.add(T.JSON_PARAM_PICTURE);
      value_list.add(Base64.encodeToString(picture,Base64.NO_WRAP));
      param_list.add(T.JSON_PARAM_OBJECT_NAME);
      value_list.add(object_name);

      remoteStorage=new RemoteStorage(this,progressBar);
      remoteStorage.jsonRequest(RemoteStorage.COMMAND_UPDATE_PICTURE,getHostname(),param_list,value_list,afterUpdatePictureCallbackHandler,new RetVal());
    }
  }
  public void getUserRequest(){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_USER,getHostname(),param_list,value_list,showUserCallbackHandler,newInstanceUserList());
  }
  public void getUsertypeRequest(){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    param_list.add(T.JSON_PARAM_USERNAME);
    value_list.add(username);
    param_list.add(T.JSON_PARAM_PASSWORD);
    value_list.add(password);

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_USER_TYPE,getHostname(),param_list,value_list,showUsertypeCallbackHandler,null);
  }
  public void getTokenRequest(){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    param_list.add(T.JSON_PARAM_USERNAME);
    value_list.add(username);
    param_list.add(T.JSON_PARAM_PASSWORD);
    value_list.add(password);

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_TOKEN,getHostname(),param_list,value_list,showTokenCallbackHandler,null);
  }
  public void getDataRequest(TypedCallback callback,Object callback_obj){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_DATA,getHostname(),param_list,value_list,callback,callback_obj);
  }
  public void getCurrencyRequest(){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_CURRENCY_ID);
    value_list.add(T.PERCENT);

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_CURRENCY,getHostname(),param_list,value_list,showCurrencyCallbackHandler,newInstanceCurrencyList());
  }
  public void getColorRequest(TypedCallback callback,Object callback_obj){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_COLOR,getHostname(),param_list,value_list,callback,callback_obj);
  }
  public void getProductRequest(){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_PRODUCT_ID);
    value_list.add(T.PERCENT);
    //selecting stock>=1 products
    param_list.add(T.JSON_PARAM_STOCK_COUNT);
    value_list.add(RemoteStorage.VALUE_ONE);

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_PRODUCT,getHostname(),param_list,value_list,findDefaultProductCallbackHandler,newInstanceProductList());
  }
  public void getProductRequest(TypedCallback callback){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_PRODUCT_ID);
    value_list.add(T.PERCENT);
    //selecting stock>=1 products
    param_list.add(T.JSON_PARAM_STOCK_COUNT);
    value_list.add(RemoteStorage.VALUE_ONE);

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_PRODUCT,getHostname(),param_list,value_list,callback,newInstanceProductList());
  }
  //fragment_list with button_next
  public void getProductRequest(TypedCallback callback,String product_id,int offset_value,int rows_value){
    String offset=offset_value>=0?String.valueOf(offset_value):null;
    String rows=rows_value>=0?String.valueOf(rows_value):null;
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_PRODUCT_ID);
    value_list.add(product_id);
    //selecting stock>=1 products
    param_list.add(T.JSON_PARAM_STOCK_COUNT);
    value_list.add(RemoteStorage.VALUE_ONE);
    if(offset!=null){
      param_list.add(T.JSON_PARAM_OFFSET);
      value_list.add(offset);
    }
    if(rows!=null){
      param_list.add(T.JSON_PARAM_ROWS);
      value_list.add(rows);
    }

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_PRODUCT,getHostname(),param_list,value_list,callback,newInstanceProductList());
  }
  //fragment_list with button_next
  public void getMessageRequest(TypedCallback callback,String message_id,int offset_value,int rows_value){
    String offset=offset_value>=0?String.valueOf(offset_value):null;
    String rows=rows_value>=0?String.valueOf(rows_value):null;
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }

    param_list.add(T.JSON_PARAM_MESSAGE_ID);
    value_list.add(message_id);

    if(offset!=null){
      param_list.add(T.JSON_PARAM_OFFSET);
      value_list.add(offset);
    }
    if(rows!=null){
      param_list.add(T.JSON_PARAM_ROWS);
      value_list.add(rows);
    }

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_MESSAGE,getHostname(),param_list,value_list,callback,newInstanceMessageList());
  }
  public void getMessageRequest(TypedCallback callback,long from_message_id){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }

    param_list.add(T.JSON_PARAM_MESSAGE_ID);
    value_list.add(T.PERCENT);
    param_list.add(T.JSON_PARAM_FROM_MESSAGE_ID);
    value_list.add(String.valueOf(from_message_id));

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_MESSAGE,getHostname(),param_list,value_list,callback,newInstanceMessageList());
  }
  public void getTransportRequest(long sensor_id){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_TRANSPORT_ID);
    value_list.add(T.PERCENT);
    param_list.add(T.JSON_PARAM_SENSOR_ID);
    value_list.add(String.valueOf(sensor_id));

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_TRANSPORT,getHostname(),param_list,value_list,showTransportCallbackHandler,new Transport());
  }
  public void getTransportReviewRequest(long order_id,long user_id,long transport_id,String transport_name){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_ORDER_ID);
    value_list.add(String.valueOf(order_id));
    param_list.add(T.JSON_PARAM_USER_ID);
    value_list.add(String.valueOf(user_id));
    param_list.add(T.JSON_PARAM_TRANSPORT_ID);
    value_list.add(String.valueOf(transport_id));

    TransportReview transport_review=new TransportReview();
    transport_review.order_id=order_id;
    transport_review.user_id=user_id;
    transport_review.transport_id=transport_id;
    transport_review.transport_name=transport_name;
    transport_review.review_value=-1;

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_TRANSPORT_REVIEW,getHostname(),param_list,value_list,showTransportReviewCallbackHandler,transport_review);
  }
  //fragment_list with button_next
  public void getOrderABRequest(TypedCallback callback,long order_id,int offset_value,int rows_value){
    getOrderABRequest(callback,String.valueOf(order_id),offset_value,rows_value);
  }
  public void getOrderABRequest(TypedCallback callback,String order_id,int offset_value,int rows_value){
    String offset=offset_value>=0?String.valueOf(offset_value):null;
    String rows=rows_value>=0?String.valueOf(rows_value):null;
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_USER_ID);
    value_list.add(tripMode?String.valueOf(tripOrderUserId):String.valueOf(userId));
    param_list.add(T.JSON_PARAM_ORDER_ID);
    value_list.add(order_id);
    if(offset!=null){
      param_list.add(T.JSON_PARAM_OFFSET);
      value_list.add(offset);
    }
    if(rows!=null){
      param_list.add(T.JSON_PARAM_ROWS);
      value_list.add(rows);
    }

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_ORDER_AB,getHostname(),param_list,value_list,callback,newInstanceOrderABList());
  }
  //driver request for client's orders(where driver's transport set)
  public void getOrderABTransportRequest(TypedCallback callback,long transport_id,int offset_value,int rows_value){
    String offset=offset_value>=0?String.valueOf(offset_value):null;
    String rows=rows_value>=0?String.valueOf(rows_value):null;
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_TRANSPORT_ID);
    value_list.add(String.valueOf(transport_id));
    if(offset!=null){
      param_list.add(T.JSON_PARAM_OFFSET);
      value_list.add(offset);
    }
    if(rows!=null){
      param_list.add(T.JSON_PARAM_ROWS);
      value_list.add(rows);
    }

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_ORDER_AB_TRANSPORT,getHostname(),param_list,value_list,callback,newInstanceOrderABTransportList());
  }
  //driver request for empty order
  public void getOrderABByStatusRequest(TypedCallback callback,long status_id){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_USER_ID);
    value_list.add(T.PERCENT);
    param_list.add(T.JSON_PARAM_STATUS_ID);
    value_list.add(String.valueOf(status_id));//now set auto to status_id=1(if user_id==%)

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_ORDER_AB,getHostname(),param_list,value_list,callback,newInstanceOrderABList());
  }
  //driver request client near
  public void getOrderABNearRequest(TypedCallback callback,long status_id,String lat,String lon,String distance,String distance_measure){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_USER_ID);
    value_list.add(T.PERCENT);
    param_list.add(T.JSON_PARAM_STATUS_ID);
    value_list.add(String.valueOf(status_id));//now set auto to status_id=1(if user_id==%)
    //location
    param_list.add(T.JSON_PARAM_ORDER_LAT);
    value_list.add(lat);
    param_list.add(T.JSON_PARAM_ORDER_LON);
    value_list.add(lon);
    //distance
    param_list.add(T.JSON_PARAM_DISTANCE);
    value_list.add(distance);
    param_list.add(T.JSON_PARAM_DISTANCE_MEASURE);
    value_list.add(distance_measure);

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_ORDER_AB_NEAR,getHostname(),param_list,value_list,callback,newInstanceOrderABList());
  }
  public void getMaxOrderABRequest(TypedCallback callback){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_MAX_ORDER_AB,getHostname(),param_list,value_list,callback,newInstanceOrderABList());
  }
  public void getOrderABPartRequest(TypedCallback callback,OrderAB order){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_ORDER_ID);
    value_list.add(String.valueOf(order.id));

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_ORDER_AB_PART,getHostname(),param_list,value_list,callback,newInstanceOrderABPartList(order));
  }
  //for service recycle request
  public void getOrderABStatusRequest(long user_id,long order_id,TypedCallback callback,Object callback_obj){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_USER_ID);
    value_list.add(String.valueOf(user_id));
    param_list.add(T.JSON_PARAM_ORDER_ID);
    value_list.add(String.valueOf(order_id));

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_ORDER_AB_STATUS,getHostname(),param_list,value_list,callback,callback_obj);
  }
  public void getMaxTrackRequest(long sensor_id,TypedCallback callback,Object callback_obj){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_SENSOR_ID);
    value_list.add(String.valueOf(sensor_id));

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_MAX_TRACK,getHostname(),param_list,value_list,callback,callback_obj);
  }
  public void getTaxRequest(){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_TAX_ID);
    value_list.add(T.PERCENT);

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GET_TAX,getHostname(),param_list,value_list,showTaxCallbackHandler,newInstanceTaxList());
  }

  public void addOrderABRequest(){
    addOrderABRequest(false);
  }
  //is_driver_call is a mode of easy bookingStyle for call Driver for Client location
  public void addOrderABRequest(boolean is_driver_call){//for delivery & trip,easy bookingStyle
    if(currentUser!=null&&
      (bookingStyle==BOOKING_STYLE_DELIVERY&&cartList!=null&&cartList.size()>0&&deliveryLatitude!=null&&deliveryLongitude!=null)||
      (bookingStyle==BOOKING_STYLE_TRIP&&currentDirection!=null&&(selectedProduct!=null||defaultProduct!=null))||
      ((bookingStyle==BOOKING_STYLE_EASY&&!CAR_RENTAL&&pickupAddress!=null&&latitude!=null&&longitude!=null)||
       (bookingStyle==BOOKING_STYLE_EASY&&CAR_RENTAL&&totalPrice!=0&&orderLatitude!=null&&orderLongitude!=null&&deliveryLatitude!=null&&deliveryLongitude!=null))||
      (bookingStyle==BOOKING_STYLE_EASY_AB&&pickupName!=null&&pickupAddress!=null&&dropoffName!=null&&dropoffAddress!=null)){
      ArrayList<String> param_list=new ArrayList();
      ArrayList<String> value_list=new ArrayList();
      if(token!=null){
        param_list.add(T.JSON_PARAM_TOKEN);
        value_list.add(token);
      }
      else{
        param_list.add(T.JSON_PARAM_USERNAME);
        value_list.add(username);
        param_list.add(T.JSON_PARAM_PASSWORD);
        value_list.add(password);
      }
      param_list.add(T.JSON_PARAM_USER_ID);
      value_list.add(String.valueOf(currentUser.id));
      param_list.add(T.JSON_PARAM_TRANSPORT_ID);
      value_list.add(NO_VALUE);
      param_list.add(T.JSON_PARAM_ORDER_STATUS_ID);
      value_list.add(NO_VALUE);
      param_list.add(T.JSON_PARAM_TOTAL_PRICE);
      String price=getPrice(totalPrice>0?totalPrice:orderPrice).replace(T.COMA,T.POINT);
      value_list.add(price);

      String distance=T.EMPTY,duration=T.EMPTY,route_data=T.EMPTY;
      String order_lat=T.EMPTY,order_lon=T.EMPTY,order_address=T.EMPTY;
      String delivery_lat=T.EMPTY,delivery_lon=T.EMPTY,delivery_address=T.EMPTY;

      if(bookingStyle==BOOKING_STYLE_DELIVERY){
        distance=ZERO;
        duration=ZERO;
        route_data=T.EMPTY;
        order_lat=deliveryLatitude!=null?deliveryLatitude:T.EMPTY;
        order_lon=deliveryLongitude!=null?deliveryLongitude:T.EMPTY;
        order_address=deliveryAddress!=null?deliveryAddress:T.EMPTY;
        delivery_lat=deliveryLatitude!=null?deliveryLatitude:T.EMPTY;
        delivery_lon=deliveryLongitude!=null?deliveryLongitude:T.EMPTY;
        delivery_address=deliveryAddress!=null?deliveryAddress:T.EMPTY;
      }
      else if(bookingStyle==BOOKING_STYLE_TRIP||bookingStyle==BOOKING_STYLE_EASY){
        if(!is_driver_call){//Client book Trip|Easy (currentDirection!=null)
          if(!CAR_RENTAL){
            distance=String.valueOf(currentDirection.distance);
            duration=String.valueOf(currentDirection.duration);
            route_data=routeEncode(currentDirection);
            order_lat=String.valueOf(currentDirection.startLocationLat);
            order_lon=String.valueOf(currentDirection.startLocationLon);
            order_address=(bookingStyle==BOOKING_STYLE_EASY?pickupAddress:currentDirection.startAddress);
            delivery_lat=String.valueOf(currentDirection.finishLocationLat);
            delivery_lon=String.valueOf(currentDirection.finishLocationLon);
            delivery_address=(bookingStyle==BOOKING_STYLE_EASY?dropoffAddress:currentDirection.finishAddress);
          }
          else{
            distance=ZERO;duration=ZERO;route_data=T.EMPTY;
            order_lat=orderLatitude;
            order_lon=orderLongitude;
            order_address=pickupAddress;
            delivery_lat=deliveryLatitude;
            delivery_lon=deliveryLongitude;
            delivery_address=dropoffAddress;
          }
        }
        else{//Client call Driver
          distance=ZERO;
          duration=ZERO;
          route_data=T.EMPTY;
          order_lat=latitude!=null?latitude:T.EMPTY;
          order_lon=longitude!=null?longitude:T.EMPTY;
          order_address=pickupAddress!=null?pickupAddress:T.EMPTY;
          delivery_lat=T.EMPTY;
          delivery_lon=T.EMPTY;
          delivery_address=T.EMPTY;
        }
      }
      else if(bookingStyle==BOOKING_STYLE_EASY_AB){
        if(currentDirection!=null){
          distance=String.valueOf(currentDirection.distance);
          duration=String.valueOf(currentDirection.duration);
          route_data=routeEncode(currentDirection);
        }
        else{distance=ZERO;duration=ZERO;route_data=T.EMPTY;}

        order_lat=orderLatitude!=null?orderLatitude:T.EMPTY;
        order_lon=orderLongitude!=null?orderLongitude:T.EMPTY;
        order_address=pickupAddress!=null?pickupAddress:T.EMPTY;
        delivery_lat=deliveryLatitude!=null?deliveryLatitude:T.EMPTY;
        delivery_lon=deliveryLongitude!=null?deliveryLongitude:T.EMPTY;
        delivery_address=dropoffAddress!=null?dropoffAddress:T.EMPTY;
      }

      param_list.add(T.JSON_PARAM_ROUTE_DISTANCE);
      value_list.add(distance);
      param_list.add(T.JSON_PARAM_ROUTE_DURATION);
      value_list.add(duration);
      param_list.add(T.JSON_PARAM_ROUTE_DATA);
      value_list.add(route_data);
      param_list.add(T.JSON_PARAM_ORDER_LAT);
      value_list.add(order_lat);
      param_list.add(T.JSON_PARAM_ORDER_LON);
      value_list.add(order_lon);
      param_list.add(T.JSON_PARAM_ORDER_ADDRESS);
      value_list.add(order_address);
      param_list.add(T.JSON_PARAM_DELIVERY_LAT);
      value_list.add(delivery_lat);
      param_list.add(T.JSON_PARAM_DELIVERY_LON);
      value_list.add(delivery_lon);
      param_list.add(T.JSON_PARAM_DELIVERY_ADDRESS);
      value_list.add(delivery_address);

      param_list.add(T.JSON_PARAM_DELIVERY_TYPE_ID);
      value_list.add(DEFAULT_DELIVERY_TYPE);
      if(reservedTime>0){
        param_list.add(T.JSON_PARAM_RESERVED_DATE);
        SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT);
        value_list.add(format.format(new Date(reservedTime)));//NO_VALUE for current time
      }

      param_list.add(T.JSON_PARAM_RESERVED_HOURS);
      value_list.add(rentalTimeDuration>0?String.valueOf(rentalTimeDuration):NO_VALUE);

      remoteStorage=new RemoteStorage(this,progressBar);
      remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_ORDER_AB,getHostname(),param_list,value_list,afterAddOrderABCallbackHandler,new RetVal());
    }
  }
  public void addOrderABPartRequest(Product product,int count){
    if(product!=null){
      ArrayList<String> param_list=new ArrayList();
      ArrayList<String> value_list=new ArrayList();
      if(token!=null){
        param_list.add(T.JSON_PARAM_TOKEN);
        value_list.add(token);
      }
      else{
        param_list.add(T.JSON_PARAM_USERNAME);
        value_list.add(username);
        param_list.add(T.JSON_PARAM_PASSWORD);
        value_list.add(password);
      }
      param_list.add(T.JSON_PARAM_ORDER_ID);
      value_list.add(String.valueOf(orderId));
      param_list.add(T.JSON_PARAM_PRODUCT_ID);
      value_list.add(String.valueOf(product.id));
      param_list.add(T.JSON_PARAM_PRODUCT_COUNT);
      value_list.add(String.valueOf(count));
      param_list.add(T.JSON_PARAM_TOTAL_PRICE);
      value_list.add(String.valueOf(product.price));

      remoteStorage=new RemoteStorage(this,progressBar);
      RetVal ret_val=new RetVal();
      ret_val.temp_object=product;
      remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_ORDER_AB_PART,getHostname(),param_list,value_list,afterAddOrderABPartCallbackHandler,ret_val);
    }
  }
  public void addOrderABProductParamPartRequest(long product_id,ProductParam product_param){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_ORDER_ID);
    value_list.add(String.valueOf(orderId));
    param_list.add(T.JSON_PARAM_PRODUCT_ID);
    value_list.add(String.valueOf(product_id));
    param_list.add(T.JSON_PARAM_PRODUCT_PARAM_ID);
    value_list.add(String.valueOf(product_param.id));
    param_list.add(T.JSON_PARAM_PRODUCT_PARAM_PART_COUNT);
    value_list.add(String.valueOf(product_param.part_count));
    param_list.add(T.JSON_PARAM_PRODUCT_PARAM_PART_PRICE);
    value_list.add(String.valueOf(product_param.part_price));

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_ORDER_AB_PRODUCT_PARAM_PART,getHostname(),param_list,value_list,afterAddOrderABProductParamPartCallbackHandler,new RetVal());
  }
  public void addPurchaseRequest(long trip_duration,long trip_distance,long delivery_time,long payment_time,String payment_amount,TypedCallback callback,Object callback_obj){
    if(currentOrderAB!=null){
      ArrayList<String> param_list=new ArrayList();
      ArrayList<String> value_list=new ArrayList();
      if(token!=null){
        param_list.add(T.JSON_PARAM_TOKEN);
        value_list.add(token);
      }else{
        param_list.add(T.JSON_PARAM_USERNAME);
        value_list.add(username);
        param_list.add(T.JSON_PARAM_PASSWORD);
        value_list.add(password);
      }
      param_list.add(T.JSON_PARAM_USER_ID);
      value_list.add(String.valueOf(userId));//user who add the purchase
      //invoice_code,invoice_date not found
      param_list.add(T.JSON_PARAM_ORDER_ID);
      value_list.add(String.valueOf(currentOrderAB.id));
      param_list.add(T.JSON_PARAM_TOTAL_PRICE);
      value_list.add(String.valueOf(currentOrderAB.total_price));
      param_list.add(T.JSON_PARAM_TOTAL_TAX);
      value_list.add(NO_MONEY);//tax not found
      param_list.add(T.JSON_PARAM_ORDER_DATE);
      value_list.add(currentOrderAB.create_date);
      param_list.add(T.JSON_PARAM_ORDER_INFO);
      value_list.add(String.format(ORDER_INFO,getTripDuration(trip_duration),getTripDistance(trip_distance)));
      param_list.add(T.JSON_PARAM_DELIVERY_ID);
      value_list.add(NO_VALUE);
      param_list.add(T.JSON_PARAM_DELIVERY_CODE);
      value_list.add(T.EMPTY);
      param_list.add(T.JSON_PARAM_DELIVERY_PRICE);
      value_list.add(T.EMPTY);
      param_list.add(T.JSON_PARAM_DELIVERY_TYPE_ID);
      value_list.add(String.valueOf(DELIVERY_BY_SERVICE_TRANSPORT));
      param_list.add(T.JSON_PARAM_DELIVERY_DATE);
      value_list.add(getMySQLDatetime(delivery_time));
      param_list.add(T.JSON_PARAM_PAYMENT_DATE);
      value_list.add(getMySQLDatetime(payment_time));
      param_list.add(T.JSON_PARAM_PAYMENT_INFO);
      value_list.add(String.format(PAYMENT_INFO,getDeviceName(),getPhoneNumber()));
      param_list.add(T.JSON_PARAM_PAYMENT_AMOUNT);
      value_list.add(payment_amount);

      remoteStorage=new RemoteStorage(this,progressBar);
      remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_PURCHASE,getHostname(),param_list,value_list,callback,callback_obj);
    }
  }
  public void addPaymentRequest(String purchase_id,String amount,String currency,String description,String status,String transaction_id,String phone,TypedCallback callback,Object callback_obj){
    if(currentOrderAB!=null){
      ArrayList<String> param_list=new ArrayList();
      ArrayList<String> value_list=new ArrayList();
      if(token!=null){
        param_list.add(T.JSON_PARAM_TOKEN);
        value_list.add(token);
      }else{
        param_list.add(T.JSON_PARAM_USERNAME);
        value_list.add(username);
        param_list.add(T.JSON_PARAM_PASSWORD);
        value_list.add(password);
      }
      param_list.add(T.JSON_PARAM_PURCHASE_ID);
      value_list.add(purchase_id);
      param_list.add(T.JSON_PARAM_PAYMENT_AMOUNT);
      value_list.add(amount);//as double with 2 after (12.34)
      param_list.add(T.JSON_PARAM_PAYMENT_CURRENCY);
      value_list.add(currency);
      param_list.add(T.JSON_PARAM_DESCRIPTION);
      value_list.add(description);
      param_list.add(T.JSON_PARAM_PAYMENT_STATUS);
      value_list.add(status);
      param_list.add(T.JSON_PARAM_TRANSACTION_ID);
      value_list.add(transaction_id);
      param_list.add(T.JSON_PARAM_PHONE);
      value_list.add(phone);

      remoteStorage=new RemoteStorage(this,progressBar);
      remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_STRIPE_PAYMENT,getHostname(),param_list,value_list,callback,callback_obj);
    }
  }
  public void addPaymentByStripeProviderRequest(String token_id,long order_id,long amount,String currency){
    if(currentOrderAB!=null){
      ArrayList<String> param_list=new ArrayList();
      ArrayList<String> value_list=new ArrayList();
      if(token!=null){
        param_list.add(T.JSON_PARAM_TOKEN);
        value_list.add(token);
      }else{
        param_list.add(T.JSON_PARAM_USERNAME);
        value_list.add(username);
        param_list.add(T.JSON_PARAM_PASSWORD);
        value_list.add(password);
      }
      param_list.add(T.JSON_PARAM_TOKEN_ID);
      value_list.add(token_id);
      param_list.add(T.JSON_PARAM_USER_ID);
      value_list.add(String.valueOf(userId));//user who add the payment
      param_list.add(T.JSON_PARAM_ORDER_ID);
      value_list.add(String.valueOf(order_id));
      param_list.add(T.JSON_PARAM_AMOUNT);
      value_list.add(String.valueOf(amount));//in cents
      param_list.add(T.JSON_PARAM_CURRENCY);
      value_list.add(currency);

      remoteStorage=new RemoteStorage(this,progressBar);
      remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_STRIPE_PAYMENT,getHostname(),param_list,value_list,afterAddPaymentCallbackHandler,new RetVal());
    }
  }
  public void addTransportReviewRequest(long order_id,long user_id,long transport_id,byte review_value){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_ORDER_ID);
    value_list.add(String.valueOf(order_id));
    param_list.add(T.JSON_PARAM_USER_ID);
    value_list.add(String.valueOf(user_id));
    param_list.add(T.JSON_PARAM_TRANSPORT_ID);
    value_list.add(String.valueOf(transport_id));
    param_list.add(T.JSON_PARAM_DESCRIPTION);
    value_list.add(String.format(ORDER_REVIEW,order_id));
    param_list.add(T.JSON_PARAM_REVIEW_VALUE);
    value_list.add(String.valueOf(review_value));

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_TRANSPORT_REVIEW,getHostname(),param_list,value_list,afterAddTransportReviewCallbackHandler,new RetVal());
  }
  public void addUserReviewRequest(long order_id,long user_id,long review_user_id,byte review_value){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_USER_ID);
    value_list.add(String.valueOf(user_id));
    param_list.add(T.JSON_PARAM_REVIEW_USER_ID);
    value_list.add(String.valueOf(review_user_id));
    param_list.add(T.JSON_PARAM_DESCRIPTION);
    value_list.add(String.format(ORDER_REVIEW,order_id));
    param_list.add(T.JSON_PARAM_REVIEW_VALUE);
    value_list.add(String.valueOf(review_value));

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_USER_REVIEW,getHostname(),param_list,value_list,afterAddUserReviewCallbackHandler,new RetVal());
  }
  public void addSensorRequest(){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_USER_ID);
    value_list.add(String.valueOf(userId));

    param_list.add(T.JSON_PARAM_SENSOR_NAME);
    value_list.add(username);
    param_list.add(T.JSON_PARAM_SERIAL_NUMBER);
    value_list.add(getSerailNumber());
    param_list.add(T.JSON_PARAM_DEVICE_NAME);
    value_list.add(getDeviceName());
    param_list.add(T.JSON_PARAM_PHONE);
    value_list.add(getPhoneNumber());

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_SENSOR,getHostname(),param_list,value_list,afterAddSensorCallbackHandler,new RetVal());
  }
  public void addTrackRequest(){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_SENSOR_ID);
    value_list.add(String.valueOf(sensorId));
    param_list.add(T.JSON_PARAM_TYPE_ID);
    value_list.add(DEFAULT_TRACK_TYPE);
    if(currentTrack!=null){
      param_list.add(T.JSON_PARAM_LATITUDE);
      value_list.add(String.valueOf(currentTrack.latitude));
      param_list.add(T.JSON_PARAM_LONGITUDE);
      value_list.add(String.valueOf(currentTrack.longitude));
      param_list.add(T.JSON_PARAM_TRACK_TIME);
      value_list.add(String.valueOf(currentTrack.track_time));
      param_list.add(T.JSON_PARAM_ALTITUDE);
      value_list.add(String.valueOf(currentTrack.altitude));
      param_list.add(T.JSON_PARAM_ACCURACY);
      value_list.add(String.valueOf(currentTrack.accuracy));
      param_list.add(T.JSON_PARAM_BEARING);
      value_list.add(String.valueOf(currentTrack.bearing));
      param_list.add(T.JSON_PARAM_SPEED);
      value_list.add(String.valueOf(currentTrack.speed));
      param_list.add(T.JSON_PARAM_SATELLITES);
      value_list.add(String.valueOf(currentTrack.satellites));
      param_list.add(T.JSON_PARAM_BATTERY);
      value_list.add(String.valueOf(currentTrack.battery));
      param_list.add(T.JSON_PARAM_TIMEZONE_OFFSET);
      value_list.add(String.valueOf(timezoneOffset));
    }
    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_TRACK,getHostname(),param_list,value_list,afterAddTrackCallbackHandler,new RetVal());
  }
  public void updateSensorActivityRequest(long sensor_id,byte activity){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_SENSOR_ID);
    value_list.add(String.valueOf(sensor_id));
    param_list.add(T.JSON_PARAM_ACTIVITY);
    value_list.add(String.valueOf(activity));

    remoteStorage=new RemoteStorage(this,progressBar);
    RetVal ret_val=new RetVal();
    ret_val.temp_long_value=activity;
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_UPDATE_SENSOR_ACTIVITY,getHostname(),param_list,value_list,afterUpdateSensorActivityCallbackHandler,ret_val);
  }
  public void addSensorCircleRequest(long sensor_id){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_SENSOR_A_ID);
    value_list.add(String.valueOf(sensorId));
    param_list.add(T.JSON_PARAM_SENSOR_B_ID);
    value_list.add(String.valueOf(sensor_id));

    remoteStorage=new RemoteStorage(this,progressBar);
    RetVal ret_val=new RetVal();
    ret_val.temp_long_value=sensor_id;//temporary saving sensor_id value
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_SENSOR_CIRCLE,getHostname(),param_list,value_list,afterAddSensorCircleCallbackHandler,ret_val);
  }
  public void addSensorCircleToUserRequest(long user_id){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_SENSOR_ID);
    value_list.add(String.valueOf(sensorId));
    param_list.add(T.JSON_PARAM_USER_ID);
    value_list.add(String.valueOf(user_id));

    remoteStorage=new RemoteStorage(this,progressBar);
    RetVal ret_val=new RetVal();
    ret_val.temp_long_value=user_id;//temporary saving user_id value
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_SENSOR_CIRCLE_TO_USER,getHostname(),param_list,value_list,afterAddSensorCircleToUserCallbackHandler,ret_val);
  }
  public void removeSensorCircleRequest(long sensor_id){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_SENSOR_A_ID);
    value_list.add(String.valueOf(sensorId));
    param_list.add(T.JSON_PARAM_SENSOR_B_ID);
    value_list.add(String.valueOf(sensor_id));

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_REMOVE_SENSOR_CIRCLE,getHostname(),param_list,value_list,afterRemoveSensorCircleCallbackHandler,new RetVal());
  }
  public void removeSensorCircleToUserRequest(long user_id){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_SENSOR_ID);
    value_list.add(String.valueOf(sensorId));
    param_list.add(T.JSON_PARAM_USER_ID);
    value_list.add(String.valueOf(user_id));

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_REMOVE_SENSOR_CIRCLE_TO_USER,getHostname(),param_list,value_list,afterRemoveSensorCircleToUserCallbackHandler,new RetVal());
  }
  public void addOrUpdateDriverTransportRequest(Activity activity){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    EditText edit_text;
    String value;
    if(activity!=null&&sensorId!=-1){
      if(token!=null){
        param_list.add(T.JSON_PARAM_TOKEN);
        value_list.add(token);
      }
      else{
        param_list.add(T.JSON_PARAM_USERNAME);
        value_list.add(username);
        param_list.add(T.JSON_PARAM_PASSWORD);
        value_list.add(password);
      }

      edit_text=(EditText)activity.findViewById(R.id.edit_text_transport_name);
      if(edit_text!=null){
        value=edit_text.getText().toString();
        if(value.isEmpty()){
          edit_text.setHintTextColor(resources.getColor(R.color.bad_text));
          edit_text.requestFocus();
          return;
        }else edit_text.setTextColor(resources.getColor(R.color.good_text));
        param_list.add(T.JSON_PARAM_TRANSPORT_NAME);
        value_list.add(value);
        putDriverTransportName(value);
      }

      edit_text=(EditText)activity.findViewById(R.id.edit_text_license_plate);
      if(edit_text!=null){
        value=edit_text.getText().toString();
        if(value.isEmpty()){
          edit_text.setHintTextColor(resources.getColor(R.color.bad_text));
          edit_text.requestFocus();
          return;
        }else edit_text.setTextColor(resources.getColor(R.color.good_text));
        param_list.add(T.JSON_PARAM_LICENSE_PLATE);
        value_list.add(value);
        putDriverTransportLicensePlate(value);
      }
      //color
      DriverActivity a=(DriverActivity)activity;
      String color=a.getColorList().get(a.getColorPosition()).name;
      param_list.add(T.JSON_PARAM_TRANSPORT_COLOR);
      value_list.add(color);
      putDriverTransportColor(color);
      editor.commit();

      //sensor
      param_list.add(T.JSON_PARAM_SENSOR_ID);
      value_list.add(String.valueOf(sensorId));

      remoteStorage=new RemoteStorage(this,progressBar);
      RetVal ret_val=new RetVal();
      ret_val.temp_object=activity;
      if(driverTransportId!=-1){
        param_list.add(T.JSON_PARAM_TRANSPORT_ID);
        value_list.add(String.valueOf(driverTransportId));
        remoteStorage.jsonRequest(RemoteStorage.COMMAND_UPDATE_TRANSPORT,getHostname(),param_list,value_list,afterUpdateTransportCallbackHandler,ret_val);
      }
      else remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_TRANSPORT,getHostname(),param_list,value_list,afterAddTransportCallbackHandler,ret_val);
    }
  }
  public void updateOrderABTransportRequest(long order_id,long transport_id,long order_user_id){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_ORDER_ID);
    value_list.add(String.valueOf(order_id));
    param_list.add(T.JSON_PARAM_TRANSPORT_ID);
    value_list.add(String.valueOf(transport_id));

    remoteStorage=new RemoteStorage(this,progressBar);
    RetVal ret_val=new RetVal();
    ret_val.temp_id=order_id;//temporary saving orderId value
    ret_val.temp_long_value=transport_id;//temporary saving transportId value
    ret_val.temp_long_value_2=order_user_id;//temorary saving orderUserId Value
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_UPDATE_ORDER_AB_TRANSPORT,getHostname(),param_list,value_list,afterUpdateOrderABTransportCallbackHandler,ret_val);
  }
  public void updateOrderABStatusRequest(long order_id,long status_id){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_ORDER_ID);
    value_list.add(String.valueOf(order_id));
    param_list.add(T.JSON_PARAM_STATUS_ID);
    value_list.add(String.valueOf(status_id));

    remoteStorage=new RemoteStorage(this,progressBar);
    RetVal ret_val=new RetVal();
    ret_val.temp_id=order_id;//temporary saving Id value
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_UPDATE_ORDER_AB_STATUS,getHostname(),param_list,value_list,afterUpdateOrderABStatusCallbackHandler,ret_val);
  }
  public void increaseUserPrepaidAmountRequest(String prepaid_code){
    if(userId>0){
      ArrayList<String> param_list=new ArrayList();
      ArrayList<String> value_list=new ArrayList();
      if(token!=null){
        param_list.add(T.JSON_PARAM_TOKEN);
        value_list.add(token);
      }else{
        param_list.add(T.JSON_PARAM_USERNAME);
        value_list.add(username);
        param_list.add(T.JSON_PARAM_PASSWORD);
        value_list.add(password);
      }

      param_list.add(T.JSON_PARAM_PREPAID_CODE);
      value_list.add(prepaid_code);

      remoteStorage.jsonRequest(RemoteStorage.COMMAND_INCREASE_USER_PREPAID_AMOUNT_BY_PREPAID_CODE,getHostname(),param_list,value_list,afterIncreaseUserPrepaidAmountCallbackHandler,new RetVal());
    }
  }
  public void addMessageRequest(String call_name,String new_message){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_RECIPIENT);
    value_list.add(String.valueOf(call_name));
    param_list.add(T.JSON_PARAM_MESSAGE);
    value_list.add(String.valueOf(new_message));

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_MESSAGE,getHostname(),param_list,value_list,afterAddMessageCallbackHandler,new RetVal());
  }

  public void addGeocodeRequest(String geocode_address,String formatted_address,double latitude,double longitude){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    if(token!=null){
      param_list.add(T.JSON_PARAM_TOKEN);
      value_list.add(token);
    }
    else{
      param_list.add(T.JSON_PARAM_USERNAME);
      value_list.add(username);
      param_list.add(T.JSON_PARAM_PASSWORD);
      value_list.add(password);
    }
    param_list.add(T.JSON_PARAM_GEOCODE_ADDRESS);
    value_list.add(geocode_address);
    param_list.add(T.JSON_PARAM_FORMATTED_ADDRESS);
    value_list.add(formatted_address);
    param_list.add(T.JSON_PARAM_LATITUDE);
    value_list.add(String.valueOf(latitude));
    param_list.add(T.JSON_PARAM_LONGITUDE);
    value_list.add(String.valueOf(longitude));

    remoteStorage=new RemoteStorage(this,progressBar);
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_ADD_GEOCODE,getHostname(),param_list,value_list,null,new RetVal());
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  /*
    There are two languages.
    Default language of OS: Locale.getDefault().getDisplayLanguage();
    Current language of Application: getResources().getConfiguration().locale.getDisplayLanguage();

Locale.getDefault().getLanguage()       ---> en
Locale.getDefault().getISO3Language()   ---> eng
Locale.getDefault().getCountry()        ---> US
Locale.getDefault().getISO3Country()    ---> USA
Locale.getDefault().getDisplayCountry() ---> United States
Locale.getDefault().getDisplayName()    ---> English (United States)
Locale.getDefault().toString()          ---> en_US
Locale.getDefault().getDisplayLanguage()---> English
  */

  /*googleDirections*/
  /*
    directions type:
    0 - from markerA to markerB, order locations - delivery locations
    1 - from client locations - driver locations
    2 - from pickup address to destination address - delivery address
  */
  private Direction currentDirection=null;
  public Direction getCurrentDirection(){return currentDirection;}
  public void setCurrentDirection(Direction direction){currentDirection=direction;}
  private int lastDirectionType=-1;
  public int getLastDirectionType(){return lastDirectionType;}
  public void googleDirections(int type){
    googleDirections(type,showTripFragmentAndRouteCallbackHandler);
  }
  public void googleDirections(int type,TypedCallback callback){
    String from_lat=null,from_lng=null,to_lat=null,to_lng=null;
    String from=null,to=null;
    lastDirectionType=type;
    if(type==DIRECTIONS_ROUTE_LOCATION){
      from_lat=orderLatitude;
      from_lng=orderLongitude;
      to_lat=deliveryLatitude;
      to_lng=deliveryLongitude;
    }
    else if(type==DIRECTIONS_CLIENT_DRIVER){
      from_lat=preferences.getString(T.SETTINGS_CLIENT_LATITUDE,null);
      from_lng=preferences.getString(T.SETTINGS_CLIENT_LONGITUDE,null);
      to_lat=preferences.getString(T.SETTINGS_DRIVER_LATITUDE,null);
      to_lng=preferences.getString(T.SETTINGS_DRIVER_LONGITUDE,null);
    }
    else if(type==DIRECTIONS_ROUTE_ADDRESS){
      from=origin;
      to=destination;
    }
    if((from_lat!=null&&from_lng!=null&&to_lat!=null&&to_lng!=null)||(from!=null&&to!=null)){
      ArrayList<String> param_list=new ArrayList();
      ArrayList<String> value_list=new ArrayList();
      param_list.add(T.JSON_GOOGLE_ORIGIN);
      if(from_lat!=null&&from_lng!=null)value_list.add(from_lat+T.COMA+from_lng);
      else if(from!=null)value_list.add(from);
      param_list.add(T.JSON_GOOGLE_DESTINATION);
      if(to_lat!=null&&to_lng!=null)value_list.add(to_lat+T.COMA+to_lng);
      else if(to!=null)value_list.add(to);
      param_list.add(T.JSON_GOOGLE_LANGUAGE);
      value_list.add(Locale.getDefault().getLanguage());
      //value_list.add(context.getResources().getConfiguration().locale.getLanguage());
      //param_list.add(T.JSON_GOOGLE_DIRECTIONS_KEY);
      //value_list.add(getString(R.string.google_maps_key));
      remoteStorage=new RemoteStorage(this,progressBar);
      remoteStorage.jsonRequest(RemoteStorage.COMMAND_GOOGLE_DIRECTIONS,RemoteStorage.GOOGLE_HOSTNAME,param_list,value_list,callback,new Direction());
      //last googleDirections calc(for not restart directions if both coords not changed)
      if(from_lat!=null&&from_lng!=null&&to_lat!=null&&to_lng!=null){
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"orderLatLng="+from_lat+":"+from_lng,Toast.LENGTH_SHORT).show();
        editor.putString(T.SETTINGS_ORIGIN_LATITUDE,from_lat);//not used
        editor.putString(T.SETTINGS_ORIGIN_LONGITUDE,from_lng);//not used
        editor.putString(T.SETTINGS_DESTINATION_LATITUDE,to_lat);//not used
        editor.putString(T.SETTINGS_DESTINATION_LONGITUDE,to_lng);//not used
        editor.commit();
      }
      else if(DEBUG)Toast.makeText(context.getApplicationContext(),"orderFromTo="+from+"-"+to,Toast.LENGTH_SHORT).show();
    }
  }
  public void googleGeocode(String address,TypedCallback callback){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    param_list.add(T.JSON_GOOGLE_ADDRESS);
    value_list.add(address);
    param_list.add(T.JSON_GOOGLE_LANGUAGE);
    value_list.add(Locale.getDefault().getLanguage());//send ua for my phone?

    //value_list.add(context.getResources().getConfiguration().locale.getLanguage());
    //param_list.add(T.JSON_GOOGLE_GEOCODE_KEY);
    //value_list.add(getString(R.string.google_maps_key));
    remoteStorage=new RemoteStorage(this,progressBar);
    Geocode geocode=new Geocode();
    geocode.geocodeAddress=address;
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GOOGLE_GEOCODE,RemoteStorage.GOOGLE_HOSTNAME,param_list,value_list,callback,geocode);
  }
  public void googleGeocode(String lat,String lng,TypedCallback callback){
    ArrayList<String> param_list=new ArrayList();
    ArrayList<String> value_list=new ArrayList();
    param_list.add(T.JSON_GOOGLE_LATLNG);
    value_list.add(lat+T.COMA+lng);
    param_list.add(T.JSON_GOOGLE_LANGUAGE);
    value_list.add(Locale.getDefault().getLanguage());//send ua for my phone?

    //value_list.add(context.getResources().getConfiguration().locale.getLanguage());
    //param_list.add(T.JSON_GOOGLE_GEOCODE_KEY);
    //value_list.add(getString(R.string.google_maps_key));
    remoteStorage=new RemoteStorage(this,progressBar);
    Geocode geocode=new Geocode();
    remoteStorage.jsonRequest(RemoteStorage.COMMAND_GOOGLE_GEOCODE,RemoteStorage.GOOGLE_HOSTNAME,param_list,value_list,callback,geocode);
  }

  /*
          route_format:
                         <start_lat>;<start_lng>;
                         <finish_lat>;<finish_lng>;
                         <distance>;<duration>;
                         <instructions>;
  */
  public String routeEncode(Direction d){
    String ret_val=null;
    if(d!=null){
      ret_val=T.EMPTY;
      byte[] instructions;
      for(int i=0;i<d.steps.size();i++){
        ret_val+=d.steps.get(i).startLocationLat+T.POINT_COMA;
        ret_val+=d.steps.get(i).startLocationLon+T.POINT_COMA;
        ret_val+=d.steps.get(i).finishLocationLat+T.POINT_COMA;
        ret_val+=d.steps.get(i).finishLocationLon+T.POINT_COMA;
        ret_val+=d.steps.get(i).distance+T.POINT_COMA;
        ret_val+=d.steps.get(i).duration+T.POINT_COMA;
        instructions=null;
        try{instructions=d.steps.get(i).instructions.getBytes(GOOGLE_DIRECTIONS_ENCODING);}catch(UnsupportedEncodingException ue_e){}
        //instructions=currentDirection.steps.get(i).instructions.getBytes();
        ret_val+=(instructions!=null?Base64.encodeToString(instructions,Base64.DEFAULT):T.EMPTY)+T.POINT_COMA;
      }
    }
    return ret_val;
  }
  public Direction routeDecode(OrderAB order){
    Direction d=new Direction();
    d.startLocationLat=order.order_lat;
    d.startLocationLon=order.order_lon;
    d.finishLocationLat=order.delivery_lat;
    d.finishLocationLon=order.delivery_lon;
    d.distance=order.route_distance;
    d.distanceText=getDistance(order);
    d.duration=order.route_duration;
    d.durationText=getDuration(order);
    d.startAddress=order.order_address;
    d.finishAddress=order.delivery_address;
    if(order.route_data!=null&&order.route_data.length()>0){
      try{parseRouteStringToken(new String(Base64.decode(order.route_data,Base64.DEFAULT)),d);}catch(Exception e){
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"Direction Exception="+e.toString(),Toast.LENGTH_SHORT).show();
      }
    }
    return d;
  }
  private Direction parseRouteStringToken(String route_data,Direction direction)throws Exception{
    Direction d=new Direction();
    String str;
    int i=0;
    //if(DEBUG)Toast.makeText(context.getApplicationContext(),"route_data="+route_data,Toast.LENGTH_SHORT).show();
    StringTokenizer st=new StringTokenizer(route_data,T.POINT_COMA);
    while(true){
      if(st.hasMoreTokens()){
        str=st.nextToken().trim();
        if(i==0){
          if(str.length()>0)d.startLocationLat=Double.parseDouble(str);
        }
        else if(i==1){
          if(str.length()>0)d.startLocationLon=Double.parseDouble(str);
        }
        else if(i==2){
          if(str.length()>0)d.finishLocationLat=Double.parseDouble(str);
        }
        else if(i==3){
          if(str.length()>0)d.finishLocationLon=Double.parseDouble(str);
        }
        else if(i==4){
          if(str.length()>0)d.distance=Integer.parseInt(str);
          //d.distanceText=getDistance(d.distance);
        }
        else if(i==5){
          if(str.length()>0)d.duration=Integer.parseInt(str);
          //d.durationText=getDuration(d.duration);
        }
        else if(i==6){
          //d.instructions=new String(Base64.decode(str,Base64.DEFAULT));
          if(direction.steps==null)direction.steps=new ArrayList();
          direction.steps.add(d);
          i=-1;
          d=new Direction();
        }
        i++;
      }
      else break;
    }
    return direction;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  /*order*/
  private double round(double number,int scale){//1.129->1.13
    int pow=10;
    for(int i=1;i<scale;i++)pow*=10;
    double tmp=number*pow;
    return (double)(int)((tmp-(int)tmp)>=0.5?tmp+1:tmp)/pow;
  }
  public double formatDouble(double d,int scale){//1.129->1.12
    double dd=Math.pow(10,scale);
    return Math.round(d*dd)/dd;
  }
  int[] PRODUCT_TYPE_ID_TRANSPORTATION_LIST={2};//transportation type
  int[] PRODUCT_TYPE_ID_RENTAL_LIST={3};//rental type
  public boolean isTransportationTypeId(long type_id){
    boolean ret_val=false;
    for(int j=0;j<PRODUCT_TYPE_ID_TRANSPORTATION_LIST.length;j++){
      if(PRODUCT_TYPE_ID_TRANSPORTATION_LIST[j]==type_id){
        ret_val=true;break;
      }
    }
    return ret_val;
  }
  public boolean isRentalTypeId(long type_id){
    boolean ret_val=false;
    for(int j=0;j<PRODUCT_TYPE_ID_RENTAL_LIST.length;j++){
      if(PRODUCT_TYPE_ID_RENTAL_LIST[j]==type_id){
        ret_val=true;break;
      }
    }
    return ret_val;
  }
  public String getCurrencyTitle(Currency currency){
    if(activeCurrency.attrList!=null&&activeCurrency.attrList.size()>0){//work with first
      Attr attr;
      Iterator iter=activeCurrency.attrList.iterator();
      while(iter.hasNext()){
        attr=(Attr)iter.next();
        if(attr.name.equals(T.JSON_PARAM_ATTR_NAME_VALUE_TITLE))return attr.value;
      }
    }
    return null;
  }
  public String getCurrencyAbout(Currency currency){
    if(activeCurrency.attrList!=null&&activeCurrency.attrList.size()>0){//work with first
      Attr attr;
      Iterator iter=activeCurrency.attrList.iterator();
      while(iter.hasNext()){
        attr=(Attr)iter.next();
        if(attr.name.equals(T.JSON_PARAM_ATTR_NAME_VALUE_ABOUT))return attr.value;
      }
    }
    return null;
  }
  public void presetActiveCurrencyRequisites(){
    if(activeCurrency.attrList!=null&&activeCurrency.attrList.size()>0){//work with first
      Attr attr;
      Iterator iter=activeCurrency.attrList.iterator();
      while(iter.hasNext()){
        attr=(Attr)iter.next();
        if(attr.name.equals(T.JSON_PARAM_ATTR_NAME_VALUE_TITLE))activeCurrencyTitle=attr.value;
        else if(attr.name.equals(T.JSON_PARAM_ATTR_NAME_VALUE_ABOUT))activeCurrencyAbout=attr.value;
      }
    }
  }

  public void presetFee(){
    //select client/driver fee
    if(taxList!=null&&taxList.size()>0){
      Tax tax;
      Iterator i=taxList.iterator();
      while(i.hasNext()){
        tax=(Tax)i.next();
        if(tax!=null){
          if(tax.name.equalsIgnoreCase(CLIENT_FEE_NAME))clientFee=tax;
          else if(tax.name.equalsIgnoreCase(DRIVER_FEE_NAME))driverFee=tax;
        }
      }
    }
    //title for client fee
    if(clientFee!=null&&clientFee.attrList!=null&&clientFee.attrList.size()>0){
      Attr attr;
      Iterator iter=clientFee.attrList.iterator();
      while(iter.hasNext()){
        attr=(Attr)iter.next();
        if(attr.name.equals(T.JSON_PARAM_ATTR_NAME_VALUE_TITLE))clientFeeTitle=attr.value;
      }
    }
    //title for driver fee
    if(driverFee!=null&&driverFee.attrList!=null&&driverFee.attrList.size()>0){
      Attr attr;
      Iterator iter=driverFee.attrList.iterator();
      while(iter.hasNext()){
        attr=(Attr)iter.next();
        if(attr.name.equals(T.JSON_PARAM_ATTR_NAME_VALUE_TITLE))driverFeeTitle=attr.value;
      }
    }
  }
  public String getDiscountPolicy(int discount_type,double discount_value){
    String ret_val=T.EMPTY;
    //length after comma:
    //return String.valueOf(f).split("\\.")[1].length();
    String value;
    if(discount_value%1>0)value=getPrice(discount_value);
    else value=Integer.toString((int)discount_value);
    switch(discount_type){
      case DISCOUNT_IN_PERCENT:
        ret_val=T.MINUS+value+T.PERCENT;
        break;
      case DISCOUNT_IN_VALUE:
        ret_val=T.MINUS+value+T.SPACE+(activeCurrencyTitle!=null?activeCurrencyTitle:activeCurrency.name);
        break;
      case INCREASE_IN_PERCENT:
        ret_val=T.PLUS+value+T.PERCENT;
        break;
      case INCREASE_IN_VALUE:
        ret_val=T.PLUS+value+T.SPACE+(activeCurrencyTitle!=null?activeCurrencyTitle:activeCurrency.name);
        break;
      default:
    }
    return ret_val;
  };
  public double getDiscountPrice(int discount_type,double discount_value,double price){
    double ret_val;
    double discount;
    switch(discount_type){
      case DISCOUNT_IN_PERCENT:
        discount=price*(discount_value/100);
        price-=discount;
        break;
      case DISCOUNT_IN_VALUE:
        price-=discount_value;
        break;
      case INCREASE_IN_PERCENT:
        discount=price*(discount_value/100);
        price+=discount;
      case INCREASE_IN_VALUE:
        price+=discount_value;
        break;
      default:
    }
    ret_val=price;
    return ret_val;
  };
  private double orderPrice=0;
  public double getOrderPrice(){return orderPrice;}
  private double totalPrice=0;
  public double getTotalPrice(){return totalPrice;}
  private String distanceText=null,durationText=null;
  private long rentalReserved=0;
  public long getRentalReserved(){return rentalReserved;}
  public void setRentalReserved(long time){rentalReserved=time;}
  private int rentalTimeDuration=0;//reserved hours for order
  public int getRentalTimeDuration(){return rentalTimeDuration;}
  public void setRentalTimeDuration(int time){rentalTimeDuration=time;}
  public void clearOrderCalcValues(){
    orderPrice=0;totalPrice=0;
    distanceText=null;durationText=null;
  }
  public boolean isActualProductDiscount(Product product){
    boolean ret_val=false;
    if(product!=null&&product.discount!=null){
      long curr_time=System.currentTimeMillis();
      boolean is_actual=true;
      if(product.discount.start_date.length()>0&&product.discount.finish_date.length()>0){
        SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT);
        Date start_date=null,finish_date=null;
        try{
          start_date=format.parse(product.discount.start_date);
          finish_date=format.parse(product.discount.finish_date);
        }catch(java.text.ParseException pe){}
        if((start_date!=null&&curr_time<start_date.getTime())||
        (finish_date!=null&&curr_time>finish_date.getTime()))is_actual=false;
      }
      ret_val=is_actual;
    }
    return ret_val;
  }
  public boolean isActualUserDiscount(User user){
    boolean ret_val=false;
    if(user!=null&&user.discount!=null){
      long curr_time=System.currentTimeMillis();
      boolean is_actual=true;
      if(user.discount.start_date.length()>0&&user.discount.finish_date.length()>0){
        SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT);
        Date start_date=null,finish_date=null;
        try{
          start_date=format.parse(user.discount.start_date);
          finish_date=format.parse(user.discount.finish_date);
        }catch(java.text.ParseException pe){}
        if(curr_time<start_date.getTime()||curr_time>finish_date.getTime())is_actual=false;
      }
      ret_val=is_actual;
    }
    return ret_val;
  }
  public double calcOrder(Direction d){
    boolean need_trans_calc=false,need_rent_calc=false;
    double order_price,product_price;
    Product product=(selectedProduct!=null?selectedProduct:defaultProduct);
    product_price=(product!=null?product.price:0);
    //type for calc
    if(product!=null&&product.typeList!=null&&product.typeList.size()>0){
      Type t;
      Iterator i=product.typeList.iterator();
      while(i.hasNext()){
        t=(Type)i.next();
        if(t!=null){
          if(isTransportationTypeId(t.id))need_trans_calc=true;
          else if(isRentalTypeId(t.id))need_rent_calc=true;
        }
      }
    }
    if(d!=null&&need_trans_calc)order_price=(d.distance*product_price)/1000;//in meters
    else if(rentalTimeDuration!=0&&need_rent_calc)order_price=(product_price*rentalTimeDuration);
    else order_price=product_price;
    //product_discount
    if(isActualProductDiscount(product))order_price=getDiscountPrice(product.discount.type,product.discount.value,order_price);
    //user discount
    if(isActualUserDiscount(currentUser))order_price=getDiscountPrice(currentUser.discount.type,currentUser.discount.value,order_price);
    return /*round*/formatDouble(order_price,2);
  }
  public double calcOrder(ArrayList<Product> product_list){//non trip or rental (delivery product's list), no check product type
    Product product;
    double order_price=0,product_price;
    Iterator iter=product_list.iterator();
    while(iter.hasNext()){
      product=(Product)iter.next();
      if(product.checked){
        product_price=product.price;
        if(product.cart_count>1)product_price=product_price*product.cart_count;
        //product_discount
        if(isActualProductDiscount(product))product_price=getDiscountPrice(product.discount.type,product.discount.value,product_price);
        order_price+=product_price;
      }
    }
    //user discount
    if(isActualUserDiscount(currentUser))order_price=getDiscountPrice(currentUser.discount.type,currentUser.discount.value,order_price);
    return /*round*/formatDouble(order_price,2);
  }
  public double calcOrder(long trip_distance,OrderAB order){//order must not null! (need to review for right works)
    OrderABPart part;
    Product product;
    boolean need_trans_calc,need_rent_calc;
    double order_price=0,product_price;
    ArrayList<OrderABPart> part_list=order.orderABPartList;
    if(part_list!=null&&part_list.size()>0){
      Iterator iter=part_list.iterator();
      while(iter.hasNext()){
        part=(OrderABPart)iter.next();
        product=part.product;
        need_trans_calc=false;
        need_rent_calc=false;
        if(product!=null&&product.typeList!=null&&product.typeList.size()>0){
          Type t;
          Iterator i=product.typeList.iterator();
          while(i.hasNext()){
            t=(Type)i.next();
            if(t!=null){
              if(isTransportationTypeId(t.id))need_trans_calc=true;
              else if(isRentalTypeId(t.id))need_rent_calc=true;
            }
          }
          product_price=product.price;
          if(trip_distance>0&&need_trans_calc)order_price+=(trip_distance*product_price)/1000;//in meters
          else if(order.reserved_hours!=0&&need_rent_calc)order_price=(product_price*order.reserved_hours);
          else order_price+=product_price;
          //product_discount
          if(isActualProductDiscount(product))order_price=getDiscountPrice(product.discount.type,product.discount.value,order_price);
        }
      }
      //user discount
      if(isActualUserDiscount(order.user))order_price=getDiscountPrice(currentUser.discount.type,currentUser.discount.value,order_price);
    }
    return /*round*/formatDouble(order_price,2);
  }
  public double calcPurchase(ArrayList<Purchase> list){
    double ret_val=0;
    if(list!=null){
      for(Purchase item: list){
        if(item!=null)ret_val+=item.payment_amount;
      }
    }
    return ret_val;
  }
  public int getCartCount(long product_id){
    int count=0;
    if(cartList!=null){
      Product product;
      Iterator iter=cartList.iterator();
      while(iter.hasNext()){
        product=(Product)iter.next();
        if(product!=null&&product.id==product_id)count++;
      }
    }
    return count;
  }
  public void clearCartPictures(){
    if(cartList!=null){
      Product product;
      Iterator iter=cartList.iterator();
      while(iter.hasNext()){
        product=(Product)iter.next();
        if(product!=null&&product.picture!=null)product.picture=null;
      }
    }
  }
  public void calcOrderPrice(Direction direction,Product product){//by direction and product
    orderPrice=calcOrder(direction);
    totalPrice=getTotalPrice(orderPrice,product.paramList);
  }
  //////////////////////////////////////////////////////////////////////////////////////////////////
  private User currentUser=null;
  public User getCurrentUser(){return currentUser;}
  public void writeCurrentUser()throws IOException{
    if(currentUser!=null){
      FileOutputStream fos=context.openFileOutput(T.FILENAME_CURRENT_USER,Context.MODE_PRIVATE);
      ObjectOutputStream os=new ObjectOutputStream(fos);
      os.writeObject(currentUser);
      os.close();fos.close();
    }
  }
  public User readCurrentUser()throws IOException,ClassNotFoundException{
    FileInputStream fis=context.openFileInput(T.FILENAME_CURRENT_USER);
    ObjectInputStream is=new ObjectInputStream(fis);
    currentUser=(User)is.readObject();
    is.close();fis.close();
    return currentUser;
  }
  public User findCurrentUser(){//by username
    User user=null;
    if(userList!=null&&userList.size()>0){
      User u;
      Iterator i=userList.iterator();
      while(i.hasNext()){
        u=(User)i.next();
        if(u!=null&&u.username.equals(username)){
          user=u;break;//only one username
        }
      }
    }
    return user;
  }

  private Transport driverTransport=null;
  public Transport getDriverTransport(){return driverTransport;}
  public void writeDriverTransport()throws IOException{
    if(driverTransport!=null){
      FileOutputStream fos=context.openFileOutput(T.FILENAME_DRIVER_TRANSPORT,Context.MODE_PRIVATE);
      ObjectOutputStream os=new ObjectOutputStream(fos);
      os.writeObject(driverTransport);
      os.close();fos.close();
    }
  }
  public Transport readDriverTransport()throws IOException,ClassNotFoundException{
    FileInputStream fis=context.openFileInput(T.FILENAME_DRIVER_TRANSPORT);
    ObjectInputStream is=new ObjectInputStream(fis);
    driverTransport=(Transport)is.readObject();
    is.close();fis.close();
    return driverTransport;
  }

  private Currency activeCurrency=null;
  public Currency getActiveCurrency(){return activeCurrency;}
  private String activeCurrencyTitle=null;
  public String getActiveCurrencyTitle(){return activeCurrencyTitle;}
  private String activeCurrencyAbout=null;
  public String getActiveCurrencyAbout(){return activeCurrencyAbout;}
  public void writeActiveCurrency()throws IOException{
    if(activeCurrency!=null){
      FileOutputStream fos=context.openFileOutput(T.FILENAME_ACTIVE_CURRENCY,Context.MODE_PRIVATE);
      ObjectOutputStream os=new ObjectOutputStream(fos);
      os.writeObject(activeCurrency);
      os.close();fos.close();
    }
  }
  public Currency readActiveCurrency()throws IOException,ClassNotFoundException{
    FileInputStream fis=context.openFileInput(T.FILENAME_ACTIVE_CURRENCY);
    ObjectInputStream is=new ObjectInputStream(fis);
    activeCurrency=(Currency)is.readObject();
    is.close();fis.close();
    return activeCurrency;
  }
  public Currency findActiveCurrency(){//by active
    Currency currency=null;
    if(currencyList!=null&&currencyList.size()>0){
      Currency c;
      Iterator i=currencyList.iterator();
      while(i.hasNext()){
        c=(Currency)i.next();
        if(c!=null&&c.active){
          currency=c;break;//only one active
        }
      }
    }
    return currency;
  }
  private Product selectedProduct=null;
  public Product getSelectedProduct(){return selectedProduct;}
  public void setSelectedProduct(Product product){selectedProduct=product;}
  private Product defaultProduct=null;
  public Product getDefaultProduct(){return defaultProduct;}
  public Product findDefaultProduct(){//product with a lowest price
    Product product=(productList!=null?productList.get(0):null);//first product
    if(productList!=null&&productList.size()>0){
      Product p;
      double price=productList.get(0).price;//first price
      Iterator i=productList.iterator();
      while(i.hasNext()){
        p=(Product)i.next();
        if(p!=null&&p.price<price){
          product=p;
        }
      }
    }
    return product;
  }
  public Product findProduct(long id){
    Product product=null;
    if(productList!=null&&productList.size()>0){
      Product p;
      Iterator i=productList.iterator();
      while(i.hasNext()){
        p=(Product)i.next();
        if(p!=null&&p.id==id){
          product=p;
        }
      }
    }
    return product;
  }
  public Product findProduct(long id,ArrayList<Product> product_list){
    Product product=null;
    if(product_list!=null&&product_list.size()>0){
      Product p;
      Iterator i=product_list.iterator();
      while(i.hasNext()){
        p=(Product)i.next();
        if(p!=null&&p.id==id){
          product=p;
        }
      }
    }
    return product;
  }
  public void uncheckedProductList(){
    if(productList!=null&&productList.size()>0){
      Product product;
      Iterator i=productList.iterator();
      while(i.hasNext()){
        product=(Product)i.next();
        if(product!=null){
          product.checked=false;product.cart_count=0;
        }
      }
    }
  }

  public void writeCartList()throws IOException{
    if(cartList!=null){
      FileOutputStream fos=context.openFileOutput(T.FILENAME_CART_LIST,Context.MODE_PRIVATE);
      ObjectOutputStream os=new ObjectOutputStream(fos);
      os.writeObject(cartList);
      os.close();fos.close();
    }
  }
  public ArrayList<Product> readCartList()throws IOException,ClassNotFoundException{
    FileInputStream fis=context.openFileInput(T.FILENAME_CART_LIST);
    ObjectInputStream is=new ObjectInputStream(fis);
    cartList=(ArrayList<Product>)is.readObject();
    is.close();fis.close();
    return cartList;
  }

  private OrderAB currentOrderAB=null,selectedOrderAB=null,tempOrderAB=null;//temp order ptr is seleted order from list
  public OrderAB getCurrentOrderAB(){return currentOrderAB;}
  public void setSelectedOrderAB(OrderAB order){selectedOrderAB=order;}
  public OrderAB getSelectedOrderAB(){return selectedOrderAB;}
  public void setTempOrderAB(OrderAB order){tempOrderAB=order;}
  public OrderAB getTempOrderAB(){return tempOrderAB;}

  //select nearest order for lat,lng location
  public OrderAB selectNearestOrder(ArrayList<OrderAB> order_list,double lat,double lng){
    OrderAB order;
    int length=order_list.size();
    double distance_list[]=new double[length];
    int index,min_index=0;
    double min_distance;
    for(index=0;index<length;index++){
      order=order_list.get(index);
      distance_list[index]=getPseudoDistance(order.order_lat,order.order_lon,lat,lng);
    }
    min_distance=distance_list[min_index];
    for(index=1;index<length;index++){
      if(distance_list[index]>-1/*location is far*/&&min_distance>distance_list[index]){
        min_distance=distance_list[index];
        min_index=index;
      }
    }
    selectedOrderAB=order_list.get(min_index);
    return selectedOrderAB;
  }

  //many subclasses in orderAB (need to simple saving)
  public void writeCurrentOrderAB()throws IOException{
    if(currentOrderAB!=null){
      FileOutputStream fos=context.openFileOutput(T.FILENAME_CURRENT_ORDER_AB,Context.MODE_PRIVATE);
      ObjectOutputStream os=new ObjectOutputStream(fos);
      os.writeObject(currentOrderAB);
      os.close();fos.close();
    }
  }
  public OrderAB readCurrentOrderAB()throws IOException,ClassNotFoundException{
    FileInputStream fis=context.openFileInput(T.FILENAME_CURRENT_ORDER_AB);
    ObjectInputStream is=new ObjectInputStream(fis);
    currentOrderAB=(OrderAB)is.readObject();
    is.close();fis.close();
    return currentOrderAB;
  }

  private Track currentTrack=null;
  public Track getCurrentTrack(){return currentTrack;}
  public void setCurrentTrack(Track track){currentTrack=track;}
  public void writeCurrentTrack()throws IOException{
    if(currentTrack!=null){
      FileOutputStream fos=context.openFileOutput(T.FILENAME_CURRENT_TRACK,Context.MODE_PRIVATE);
      ObjectOutputStream os=new ObjectOutputStream(fos);
      os.writeObject(currentTrack);
      os.close();fos.close();
    }
  }
  public Track readCurrentTrack()throws IOException,ClassNotFoundException{
    FileInputStream fis=context.openFileInput(T.FILENAME_CURRENT_TRACK);
    ObjectInputStream is=new ObjectInputStream(fis);
    currentTrack=(Track)is.readObject();
    is.close();fis.close();
    return currentTrack;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  /*thread*/
  //One shot starter(one time after task_sleep_count)
  public Starter starter(TypedCallback<Object> callback,Object callback_obj){
    //start thread
    Starter starter=new Starter(callback,callback_obj);
    Thread thread=new Thread(starter);
    thread.start();
    return starter;
  }
  public class Starter implements Runnable{
    private TypedCallback<Object> callback;
    private Object callbackObject;
    private boolean needClose=false;
    public void setNeedClose(boolean need_close){needClose=need_close;}
    public Starter(TypedCallback<Object> callback,Object callback_obj){
      this.callback=callback;
      this.callbackObject=callback_obj;
    }
    public void run(){
      int times=0;
      while(times<T.TASK_SLEEP_COUNT){
        try{TimeUnit.MILLISECONDS.sleep(T.TASK_SLEEP_TIMEOUT);}catch(InterruptedException e){}
        if(needToExit||needClose)break;
        times++;
      }
      if(!needToExit&&callback!=null)callback.execute(callbackObject);
    }
  }
  //Many times starter(count times after one time sleep)
  public TimeStarter timeStarter(TypedCallback<Object> callback,Object callback_obj,int time_count){
    //start thread
    TimeStarter time_starter=new TimeStarter(callback,callback_obj,time_count);
    Thread thread=new Thread(time_starter);
    thread.start();
    return time_starter;
  }
  public class TimeStarter implements Runnable{
    private TypedCallback<Object> callback;
    private Object callbackObject;
    private int timeCount;
    private boolean needClose=false;
    public void setNeedClose(boolean need_close){needClose=need_close;}
    public TimeStarter(TypedCallback<Object> callback,Object callback_obj,int time_count){
      this.callback=callback;
      this.callbackObject=callback_obj;
      this.timeCount=time_count;
    }
    public void run(){
      int times=0;
      while(times<timeCount){
        try{TimeUnit.MILLISECONDS.sleep(T.TASK_SLEEP_TIMEOUT);}catch(InterruptedException e){}
        if(needToExit||needClose)break;
        if(callback!=null)callback.execute(callbackObject);
        times++;
      }
    }
  }
  //Many times starter(after wait_count sleep)
  public WaitStarter waitStarter(TypedCallback<Object> callback,Object callback_obj,int wait_count){
    //start thread
    WaitStarter wait_starter=new WaitStarter(callback,callback_obj,wait_count);
    Thread thread=new Thread(wait_starter);
    thread.start();
    return wait_starter;
  }
  public class WaitStarter implements Runnable{
    private TypedCallback<Object> callback;
    private Object callbackObject;
    private int waitCount;
    private boolean needClose=false;
    public void setNeedClose(boolean need_close){needClose=need_close;}
    public WaitStarter(TypedCallback<Object> callback,Object callback_obj,int wait_count){
      this.callback=callback;
      this.callbackObject=callback_obj;
      this.waitCount=wait_count;
    }
    public void run(){
      int times=0;
      while(!needToExit&&!needClose){
        while(times<waitCount){
          try{TimeUnit.MILLISECONDS.sleep(T.TASK_SLEEP_TIMEOUT);}catch(InterruptedException e){}
          if(needToExit||needClose)break;
          times++;
        }
        if(callback!=null&&!needToExit&&!needClose)callback.execute(callbackObject);
        times=0;
      }
    }
  }
  //One shot starter(one time after one time sleep)
  public Handler runnableStarter(final TypedCallback<Object> callback,final Object callback_obj){
    Handler handler=new Handler();
    handler.post(new Runnable(){
      @Override
      public void run(){
        try{TimeUnit.MILLISECONDS.sleep(T.TASK_SLEEP_TIMEOUT);}catch(InterruptedException e){}
        if(callback!=null&&!needToExit)callback.execute(callback_obj);
      }
    });
    return handler;
  }
  /*asyncTask*/
  public AsyncTask taskStarter(TypedCallback<Object> back_callback,Object back_obj,TypedCallback<Object> post_callback,Object post_obj){
    //start async task
    AsyncTask task_starter=new TaskStarter(back_callback,back_obj,post_callback,post_obj).execute();
    return task_starter;
  }
  public class TaskStarter extends AsyncTask<Void,Void,Void>{
    private boolean finished=true;
    public boolean isFinished(){return finished;}
    private TypedCallback<Object> backCallback,postCallback;
    private Object backObject,postObject;
    public TaskStarter(final TypedCallback<Object> back_callback,final Object back_obj,final TypedCallback<Object> post_callback,final Object post_obj){
      this.backCallback=back_callback;
      this.postCallback=post_callback;
      this.backObject=back_obj;
      this.postObject=post_obj;
    }
    @Override
    protected void onPreExecute(){
      super.onPreExecute();
      finished=false;
    }
    @Override
    protected Void doInBackground(Void... params){
      if(backCallback!=null)backCallback.execute(backObject);
      return null;
    }
    @Override
    protected void onPostExecute(Void result){
      super.onPostExecute(result);
      if(postCallback!=null)postCallback.execute(postObject);
      finished=true;
    }
  }
  /*picasso*/
  private void picassoImage(String url,final ImageView image_view,final Object saver){
    /*Picasso.Builder picasso=new Picasso.Builder(context);
    picasso.listener(new Picasso.Listener(){
      @Override
      public void onImageLoadFailed(Picasso picasso,Uri uri,Exception exception){
        if(DEBUG)Toast.makeText(context.getApplicationContext(),"exception="+exception.toString(),Toast.LENGTH_SHORT).show();
      }
    });*/
    //if(DEBUG)Toast.makeText(context.getApplicationContext(),"url="+url,Toast.LENGTH_SHORT).show();
    try{
      final Picasso picasso=Picasso.with(context);
      picasso/*.build()*/.load(url)
      .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
      .networkPolicy(NetworkPolicy.NO_CACHE)
      .placeholder(R.drawable.ic_do_not_disturb_alt_white_48pt_3x)
      //.error(R.drawable.ic_error_outline_red_48pt_3x)
      .fit()./*tag(context).*/into(image_view,new Callback(){
        @Override
        public void onSuccess(){
          if(saver!=null){/*saving for no double receivind data (can't use for driverActivity)*/
            if(saver instanceof User)((User)saver).picture=image_view.getDrawable();
            else if(saver instanceof Transport)((Transport)saver).picture=image_view.getDrawable();
            else if(saver instanceof Product)((Product)saver).picture=image_view.getDrawable();
          }
        }
        @Override
        public void onError(){/*picasso.cancelRequest(image_view);cancelTag(context);*/}
      });
    }catch(Exception e){}
  }
  private void picassoImage(Uri uri,ImageView image_view){
    try{
      final Picasso picasso=Picasso.with(context);
      picasso.load(uri)
      .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
      .placeholder(R.drawable.ic_do_not_disturb_alt_white_48pt_3x)
      .error(R.drawable.ic_error_outline_red_48pt_3x)
      .fit()/*.tag(context)*/.into(image_view,new Callback(){
        @Override
        public void onSuccess(){}
        @Override
        public void onError(){/*picasso.cancelTag(context);*/}
      });
    }catch(Exception e){}
  }
  private void picassoImage(String url,Target target){
    try{
      final Picasso picasso=Picasso.with(context);
      picasso/*.build()*/.load(url)./*tag(context).*/into(target);
    }catch(Exception e){}
  }
  public void loadImage(final Object path,final ImageView image_view,final Object saver){
    if(path instanceof String)picassoImage((String)path,image_view,saver);
    else if(path instanceof Uri)picassoImage((Uri)path,image_view);
    /*Handler handler=new Handler();
    handler.post(new Runnable(){
      @Override
      public void run(){picassoImage(path,image_view);}
    });
    */
  }
  public void loadImage(String url,Target target){
    picassoImage(url,target);
  }
  /*broadcast*/
  private BroadcastReceiver broadcastReceiver=null;//last receiver, may be not need ptr
  public BroadcastReceiver getBroadcastReceiver(){return broadcastReceiver;}
  public BroadcastReceiver startBroadcastReceiver(String classname,final String param,final TypedCallback<Object> callback){
    broadcastReceiver=new BroadcastReceiver(){
      public void onReceive(Context context,Intent intent){
        String str=intent.getStringExtra(param);
        if(callback!=null)callback.execute(str);
      }
    };
    IntentFilter intent_filter=new IntentFilter(classname);
    context.registerReceiver(broadcastReceiver,intent_filter);
    return broadcastReceiver;
  }
  public void stopBroadcastReceiver(BroadcastReceiver broadcast_receiver){
    if(broadcast_receiver!=null){context.unregisterReceiver(broadcast_receiver);}
  }
  private BroadcastReceiver orderStatusBroadcastReceiver=null;
  public BroadcastReceiver getOrderStatusBroadcastReceiver(){return orderStatusBroadcastReceiver;}
  public BroadcastReceiver startOrderStatusBroadcastReceiver(){//not used (status set after update getOrder)
    if(orderStatusBroadcastReceiver==null){
      orderStatusBroadcastReceiver=startBroadcastReceiver(
        DispatcherService.BROADCAST_CLASS_ORDER_STATUS,
        DispatcherService.BROADCAST_INTENT_ORDER_STATUS,showOrderStatusCallbackHandler);
    }
    return orderStatusBroadcastReceiver;
  }
  public void stopOrderStatusBroadcastReceiver(){
    if(orderStatusBroadcastReceiver!=null){context.unregisterReceiver(orderStatusBroadcastReceiver);}
  }
  //cognalys verify phone number
  // - create a new app https://www.cognalys.com/customer/manage/apps/
  // - get App ID	& Access Token
  // - put to cognalys_api.xml
  //cognalys api use apache lib (is not supported any more in SDK (API level) #23).
  /*Add these two lines under dependencies
  compile 'org.apache.httpcomponents:httpcore:4.4.1'
  compile 'org.apache.httpcomponents:httpclient:4.5'*/
  public void startVerifyPhoneNumber(Activity activity,String phone_number){
    Intent intent=new Intent(activity,VerifyMobile.class);

    String app_id=(cognalysAppId!=null&&cognalysAppId.length()>0&&!cognalysAppId.equals(DEFAULT_COGNALYS_APP_ID)?
                   cognalysAppId:context.getString(R.string.cognalys_app_id));
    String access_token=(cognalysAccessToken!=null&&cognalysAccessToken.length()>0&&!cognalysAccessToken.equals(DEFAULT_COGNALYS_ACCESS_TOKEN)?
                         cognalysAccessToken:context.getString(R.string.cognalys_access_token));

    intent.putExtra("app_id",app_id);
    intent.putExtra("access_token",access_token);

    intent.putExtra("mobile",phone_number);
    intent.putExtra("test_mode",VERIFY_PHONE_SIMULATE);//is my param for test cognalys
    activity.startActivityForResult(intent,VerifyMobile.REQUEST_CODE);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////

  //photo_source
  private Uri photoFile=null;
  public Uri getPhotoFile(){return photoFile;}
  public void openCamera(Activity activity){
    File dir=createPhotoDirectory();
    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    photoFile=generateFileUri(dir,T.LOCAL_PICTURE);
    intent.putExtra(MediaStore.EXTRA_OUTPUT,photoFile);
    activity.startActivityForResult(intent,T.ACTIVITY_CORE_PHOTO);
  }
  public void openPhotoAlbum(Activity activity){
    if(Build.VERSION.SDK_INT<=19){
      Intent intent=new Intent();
      intent.setType(T.FILE_TYPE_IMAGE);
      intent.setAction(Intent.ACTION_GET_CONTENT);
      intent.addCategory(Intent.CATEGORY_OPENABLE);
      activity.startActivityForResult(intent,T.ACTIVITY_PHOTO_ALBUM);
    }
    else if (Build.VERSION.SDK_INT>19){
      Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
      activity.startActivityForResult(intent,T.ACTIVITY_PHOTO_ALBUM);
    }
  }
  //photo_data
  private File createPhotoDirectory(){
    File directory=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),context.getString(R.string.app_name));
    if(!directory.exists())directory.mkdirs();
    return directory;
  }
  private Uri generateFileUri(File directory,String name){
    File file=new File(directory.getPath()+T.LOCAL_DELIM+name+T.UNDERSCORE+System.currentTimeMillis()+T.FILE_EXT_JPG);
    return Uri.fromFile(file);
  }
  public String getPathFromUri(Uri contentUri,String datatype){
    String path;
    String[] projection={datatype};
    Cursor cursor=context.getContentResolver().query(contentUri,projection,null,null,null);
    if(cursor!=null&&cursor.moveToFirst()){
      int index=cursor.getColumnIndex(datatype);
      path=cursor.getString(index);
      cursor.close();
    }
    else path=contentUri.getPath();
    return path;
  }
  //check permission
  public boolean isPermission(String permission){
    boolean ret_val=false;
    int permission_code=ContextCompat.checkSelfPermission(context,permission);
    if(permission_code!=PackageManager.PERMISSION_GRANTED){
      ActivityCompat.requestPermissions((Activity)context,new String[]{permission},SET_PERMISSIONS_REQUEST_CODE);//define this constant yourself
    }
    else{
      ret_val=true;
    }
    return ret_val;
  }
  /*
    https://developer.android.com/training/permissions/requesting.html

   If the device is running Android 5.1 (API level 22) or lower, or the app's targetSdkVersion is 22 or lower, the system asks the user to grant the permissions at install time. Once again, the system just tells the user what permission groups the app needs, not the individual permissions.

   How to resolve the problem ?

   http://stackoverflow.com/documentation/android/1525/runtime-permissions-in-api-23#t=201705180737316372827

  String[] permissions = new String[] {
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.CAMERA,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
  };

  private boolean checkPermissions() {
    int result;
    List<String> listPermissionsNeeded = new ArrayList<>();
    for (String p:permissions) {
        result = ContextCompat.checkSelfPermission(getActivity(),p);
        if (result != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(p);
        }
    }
    if (!listPermissionsNeeded.isEmpty()) {
        ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
        return false;
    }
    return true;
  }

  */
  public void checkPermissions(){
    ArrayList<String> list=new ArrayList();
    if(ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
      list.add(Manifest.permission.ACCESS_FINE_LOCATION);
    }
    if(ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
      list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
    }
    if(ContextCompat.checkSelfPermission(context,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
      list.add(Manifest.permission.READ_PHONE_STATE);
    }
    if(ContextCompat.checkSelfPermission(context,Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED){
      list.add(Manifest.permission.READ_SMS);
    }
    if(ContextCompat.checkSelfPermission(context,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
      list.add(Manifest.permission.CALL_PHONE);
    }
    int size=list.size();
    if(size>0){
      String[] str_list=new String[size];
      for(int i=0;i<size;i++){str_list[i]=list.get(0);}
      ActivityCompat.requestPermissions((Activity)context,str_list,SET_PERMISSIONS_REQUEST_CODE);
    }
  }
  //phone
  public String getPhoneNumber(){
    TelephonyManager tem=(TelephonyManager)context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
    String phoneNumber=T.EMPTY;
    try{
      if(isPermission(Manifest.permission.READ_SMS))phoneNumber=tem.getLine1Number();
    }catch(SecurityException se){}
    return phoneNumber;
  }
  public String getSerailNumber(){
    String serial_number="UNKNOWN_DEVICE_NUMBER";
    String sernu=null,serno=null;
    try{
      Class<?>c=Class.forName("android.os.SystemProperties");
      Method get=c.getMethod("get",String.class,String.class);
      sernu=(String)get.invoke(c,"ril.serialnumber","-1");
      serno=(String)get.invoke(c,"ro.serialno","-1");
    }catch(Exception e){}
    if(sernu.length()==0||sernu.equals("-1")||sernu.startsWith("00000")){
      String s1=Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
      sernu=(s1!=null&&s1.length()>0&&!s1.equalsIgnoreCase("unknown")?s1:"UNKNOWN_DEVICE_NUMBER");
    }
    serial_number=(serno.length()==0||serno.equals("-1"))?sernu:serno;
    return serial_number;
  }
  public String getDeviceName(){
    String brand=T.EMPTY,model=T.EMPTY;
    try{
      Class<?>c=Class.forName("android.os.SystemProperties");
      Method get=c.getMethod("get",String.class,String.class);
      brand=(String)get.invoke(c,"ro.product.brand","-1");
      model=(String)get.invoke(c,"ro.product.model","-1");
    }catch(Exception e){}
    return brand+T.SPACE+model;
  }
  //////////////////////////////////////////////////////////////////////////////////////////////////
  public boolean isServiceStarted(String service_class_name){
    boolean ret_val=false;
    ActivityManager m=(ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
    for(ActivityManager.RunningServiceInfo service:m.getRunningServices(Integer.MAX_VALUE)){
      if(service_class_name.equals(service.service.getClassName())){ret_val=true;break;}
    }
    return ret_val;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  public String getOrderStatusFromPref(){
    String status;
    long order_id=getOrderId();
    String status_name=getOrderStatusName();
    String status_value=getOrderStatusValue();
    if(status_value!=null&&status_value.length()>0)status=status_value;
    else if(status_name!=null&&status_name.length()>0)status=status_name;
    else status=context.getString(R.string.order_status_waiting);
    status=status.toUpperCase();
    String str=String.format(context.getString(R.string.order_number),order_id)+T.NEXT_LINE+status/*+":"+status_id*/;
    return str;
  }
  public String getTipMessageByOrderStatus(long status_id){//no tips for easy booking
    String tip_message=(bookingStyle==BOOKING_STYLE_TRIP||bookingStyle==BOOKING_STYLE_EASY||bookingStyle==BOOKING_STYLE_EASY_AB?
                        context.getString(R.string.order_trip_reminder_tip):
                        context.getString(R.string.order_delivery_reminder_tip));
    if(tripMode){
      if(bookingStyle==BOOKING_STYLE_DELIVERY){
        if(status_id==OrderABStatus.ORDER_STATUS_CANCELLATION)tip_message=context.getResources().getString(R.string.driver_delivery_order_cancellation_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_CANCELED)tip_message=context.getResources().getString(R.string.driver_delivery_order_canceled_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_ACCEPTED)tip_message=context.getString(R.string.driver_delivery_order_accepted_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_PROCESSED)tip_message=context.getString(R.string.driver_delivery_order_processed_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_LANDING)tip_message=context.getString(R.string.driver_delivery_order_landing_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_DELIVERING)tip_message=context.getString(R.string.driver_delivery_order_delivering_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_COMPLETED)tip_message=context.getString(R.string.driver_delivery_order_completed_tip);
      }
      else if(bookingStyle==BOOKING_STYLE_TRIP){
        if(status_id==OrderABStatus.ORDER_STATUS_CANCELLATION)tip_message=context.getResources().getString(R.string.driver_trip_order_cancellation_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_CANCELED)tip_message=context.getResources().getString(R.string.driver_trip_order_canceled_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_ACCEPTED)tip_message=context.getString(R.string.driver_trip_order_accepted_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_PROCESSED)tip_message=context.getString(R.string.driver_trip_order_processed_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_LANDING)tip_message=context.getString(R.string.driver_trip_order_landing_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_DELIVERING)tip_message=context.getString(R.string.driver_trip_order_delivering_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_COMPLETED)tip_message=context.getString(R.string.driver_trip_order_completed_tip);
      }
    }
    else{
      if(bookingStyle==BOOKING_STYLE_DELIVERY){
        if(status_id==OrderABStatus.ORDER_STATUS_CANCELLATION)tip_message=context.getResources().getString(R.string.order_delivery_cancellation_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_CANCELED)tip_message=context.getResources().getString(R.string.order_delivery_canceled_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_ACCEPTED)tip_message=context.getString(R.string.order_delivery_accepted_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_PROCESSED){
          if(transportName!=null)tip_message=String.format(context.getString(R.string.order_delivery_transport_tip),transportName+(transportLicensePlate!=null?T.SPACE+transportLicensePlate:T.EMPTY));
          else tip_message=context.getString(R.string.order_delivery_processed_tip);
        }else if(status_id==OrderABStatus.ORDER_STATUS_LANDING)tip_message=context.getString(R.string.order_delivery_landing_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_DELIVERING)tip_message=context.getString(R.string.order_delivery_delivering_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_COMPLETED)tip_message=context.getString(R.string.order_delivery_completed_tip);
      }
      else if(bookingStyle==BOOKING_STYLE_TRIP){
        if(status_id==OrderABStatus.ORDER_STATUS_CANCELLATION)tip_message=context.getResources().getString(R.string.order_trip_cancellation_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_CANCELED)tip_message=context.getResources().getString(R.string.order_trip_canceled_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_ACCEPTED)tip_message=context.getString(R.string.order_trip_accepted_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_PROCESSED){
          if(transportName!=null)tip_message=String.format(context.getString(R.string.order_trip_transport_tip),transportName+(transportLicensePlate!=null?T.SPACE+transportLicensePlate:T.EMPTY));
          else tip_message=context.getString(R.string.order_trip_processed_tip);
        }else if(status_id==OrderABStatus.ORDER_STATUS_LANDING)tip_message=context.getString(R.string.order_trip_landing_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_DELIVERING)tip_message=context.getString(R.string.order_trip_delivering_tip);
        else if(status_id==OrderABStatus.ORDER_STATUS_COMPLETED)tip_message=context.getString(R.string.order_trip_completed_tip);
      }
    }
    return tip_message;
  }
  public int getColorByOrderStatus(long status_id){
    int color=T.MAP_DEFAULT_CIRCLE_COLOR;
    if(status_id<0)color=T.MAP_CANCEL_CIRCLE_COLOR;
    else if(status_id==OrderABStatus.ORDER_STATUS_ACCEPTED)color=T.MAP_ACCEPTED_CIRCLE_COLOR;
    else if(status_id==OrderABStatus.ORDER_STATUS_PROCESSED)color=T.MAP_PROCESSING_CIRCLE_COLOR;
    else if(status_id==OrderABStatus.ORDER_STATUS_LANDING)color=T.MAP_LANDING_CIRCLE_COLOR;
    else if(status_id>OrderABStatus.ORDER_STATUS_LANDING)color=T.MAP_TRIP_CIRCLE_COLOR;
    return color;
  }
  public String getStatusName(OrderAB order){
    String str=order.status_name;
    if(order.statusAttrList!=null&&order.statusAttrList.size()>0){
      Attr attr;
      Iterator iter=order.statusAttrList.iterator();
      while(iter.hasNext()){
        attr=(Attr)iter.next();
        if(attr.name.equals(T.JSON_PARAM_ATTR_NAME_VALUE_TITLE)){str=attr.value;break;}//title attr
      }
    }
    return str;
  }
  public String getAttrByName(ArrayList<Attr> list,String name){
    String str=null;
    if(list!=null&&list.size()>0){
      Attr attr;
      Iterator iter=list.iterator();
      while(iter.hasNext()){
        attr=(Attr)iter.next();
        if(attr.name.equals(name)){str=attr.value;break;}//title attr
      }
    }
    return str;
  }
  public String getParamValue(String param_value,String part_value){
    String str=T.EMPTY;
    if(part_value!=null&&part_value.length()>0)str=T.OPEN_DOOR+part_value+T.CLOSE_DOOR;
    else if(param_value!=null&&param_value.length()>0)str=T.OPEN_DOOR+param_value+T.CLOSE_DOOR;
    return str;
  }
  public double getTotalPrice(double order_price,ArrayList<ProductParam> list){//total price of Order(by orderPrice distance cost start)
    double total_price=0;
    if(list!=null&&list.size()>0){
      ProductParam product_param;
      Iterator iter=list.iterator();
      while(iter.hasNext()){
        product_param=(ProductParam)iter.next();
        if(product_param.checked)total_price+=product_param.part_price*product_param.part_count;
      }
    }
    if(isActualUserDiscount(currentUser))total_price=getDiscountPrice(currentUser.discount.type,currentUser.discount.value,total_price);
    return total_price+order_price;
  }
  public String getProductName(Product product){
    String ret_val=product.name;
    if(product.attrList!=null&&product.attrList.size()>0){
      Attr attr;
      Iterator iter=product.attrList.iterator();
      while(iter.hasNext()){
        attr=(Attr)iter.next();
        if(attr.name.equals(T.JSON_PARAM_ATTR_NAME_VALUE_TITLE))ret_val=attr.value;
      }
    }
    return ret_val;
  }
  public double getProductParamPrice(ArrayList<ProductParam> list){//price of product param list
    double price=0;
    if(list!=null&&list.size()>0){
      ProductParam product_param;
      Iterator iter=list.iterator();
      while(iter.hasNext()){
        product_param=(ProductParam)iter.next();
        if(product_param.checked)price+=product_param.part_price*product_param.part_count;
      }
    }
    if(isActualUserDiscount(currentUser))price=getDiscountPrice(currentUser.discount.type,currentUser.discount.value,price);
    return price;
  }
  public String getPrice(double value){
    return value>0?String.format(PRICE_FORMAT,value):NO_MONEY;
  }
  public String getDistance(int distance){
    return distance>0?
           String.format(context.getString(R.string.order_distance),(float)distance/1000):context.getString(R.string.no_distance);
  }
  public String getTripDistance(long distance){
    return distance>0?
    String.format(context.getString(R.string.order_distance),(float)distance/1000):ZERO;
  }
  public String getDistance(OrderAB order){
    return getDistance(order.route_distance);
  }
  public String getDuration(int duration){
    return duration>0?
           String.format(context.getString(R.string.order_duration),duration/3600,(duration%3600)/60):context.getString(R.string.no_duration);
  }
  public String getTripDuration(long duration){//in milisec
    return duration>0?
    String.format(context.getString(R.string.order_duration),duration/3600000,((duration/1000)%3600)/60):ZERO;
  }
  public String getDuration(OrderAB order){
    return getDuration(order.route_duration);
  }

  public long getDatetimeMilisec(String datetime){
    long ret_val=-1;
    SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT);
    Date date=null;
    try{
      date=format.parse(datetime);
    }catch(java.text.ParseException pe){}
    if(date!=null)ret_val=date.getTime();
    return ret_val;
  }
  public String getMySQLDatetime(long datetime){
    Date date=new Date(datetime);
    SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT);
    return format.format(date);
  }

  public String getTalkingDatetimeMessage(String datetime){//to like view: 1 minute ago, 13:27, 5 march 2015 12:01
    String ret_val=datetime;
    if(datetime!=null&&datetime.length()>0){
      Date curr_date=new Date();
      int curr_day=curr_date.getDate();
      int curr_month=curr_date.getMonth()+1;
      int curr_year=curr_date.getYear()+1900;
      int curr_hours=curr_date.getHours();
      int curr_minutes=curr_date.getMinutes();
      int day=0,month=0,year=0,hours=0,minutes=0;
      String hours_str=T.EMPTY,minutes_str=T.EMPTY;
      //parse date
      SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT);
      Date parse_date=null;
      try{
        parse_date=format.parse(datetime);
      }catch(java.text.ParseException pe){}
      if(parse_date!=null){
        day=parse_date.getDate();
        month=parse_date.getMonth()+1;
        year=parse_date.getYear()+1900;
        hours=parse_date.getHours();
        minutes=parse_date.getMinutes();
        hours_str=String.valueOf(hours);minutes_str=String.valueOf(minutes);
        if(hours_str.length()<2)hours_str=ZERO+hours_str;
        if(minutes_str.length()<2)minutes_str=ZERO+minutes_str;
      }
      if(curr_day==day&&curr_month==month&&curr_year==year){
        if((curr_hours==hours&&curr_minutes>=minutes)||(curr_hours==hours+1&&curr_minutes<minutes)){//in hour->minutes ago
          if(curr_minutes==minutes){
            ret_val=context.getString(R.string.datetime_0_minute_ago);
          }
          else{
            int minutes_count;
            if(curr_minutes>minutes){//0->59
              minutes_count=curr_minutes-minutes;
            }
            else{//59->0->
              minutes_count=curr_minutes+60-minutes;
            }
            if(minutes_count==1)//1 min
              ret_val=context.getString(R.string.datetime_1_minute_ago);
            else if(minutes_count<5)//<5 min
              ret_val=String.format(context.getString(R.string.datetime_2_4_minutes_ago),String.valueOf(minutes_count));
            else{//>=5 min
              minutes_str=String.valueOf(minutes_count);
              if(minutes_count>20&&minutes_str.endsWith("1"))
                ret_val=String.format(context.getString(R.string.datetime_x_1_minutes_ago),String.valueOf(minutes_count));
              else if(minutes_count>20&&(minutes_str.endsWith("2")||minutes_str.endsWith("3")||minutes_str.endsWith("4")))
                ret_val=String.format(context.getString(R.string.datetime_2_4_minutes_ago),String.valueOf(minutes_count));
              else ret_val=String.format(context.getString(R.string.datetime_minutes_ago),String.valueOf(minutes_count));
            }
          }
        }
        else{//in day->hours:minutes
          ret_val=hours_str+T.DOUBLE_POINT+minutes_str;
        }
      }
      else{//day month year hours:minutes
        ret_val=day+T.SPACE+getMonthName(month)+T.SPACE+year+T.SPACE+hours_str+T.DOUBLE_POINT+minutes_str;
      }
    }
    return ret_val;
  }
  public String getTalkingDateMessage(String datetime){//to like view: 5 march 2015
    String ret_val=datetime;
    if(datetime!=null&&datetime.length()>0){
      Date curr_date=new Date();
      int curr_day=curr_date.getDate();
      int curr_month=curr_date.getMonth()+1;
      int curr_year=curr_date.getYear()+1900;
      int day=0,month=0,year=0;
      //parse date
      SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT);
      Date parse_date=null;
      try{
        parse_date=format.parse(datetime);
      }catch(java.text.ParseException pe){}
      if(parse_date!=null){
        day=parse_date.getDate();
        month=parse_date.getMonth()+1;
        year=parse_date.getYear()+1900;
      }
      ret_val=day+T.SPACE+getMonthName(month)+T.SPACE+year;

    }
    return ret_val;
  }
  private String getMonthName(int month){
    String ret_val;
    switch(month){
      case 1:ret_val=context.getString(R.string.datetime_month_1);break;
      case 2:ret_val=context.getString(R.string.datetime_month_2);break;
      case 3:ret_val=context.getString(R.string.datetime_month_3);break;
      case 4:ret_val=context.getString(R.string.datetime_month_4);break;
      case 5:ret_val=context.getString(R.string.datetime_month_5);break;
      case 6:ret_val=context.getString(R.string.datetime_month_6);break;
      case 7:ret_val=context.getString(R.string.datetime_month_7);break;
      case 8:ret_val=context.getString(R.string.datetime_month_8);break;
      case 9:ret_val=context.getString(R.string.datetime_month_9);break;
      case 10:ret_val=context.getString(R.string.datetime_month_10);break;
      case 11:ret_val=context.getString(R.string.datetime_month_11);break;
      case 12:ret_val=context.getString(R.string.datetime_month_12);break;
      default:
        ret_val=T.EMPTY;
    }
    return ret_val;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////

  //get trip time between two locations (pseudo algorithm)
  //calculates as two sides of triangle, goto from first to second for triangle catenaries
  //(direct for Hypotenuse of a triangle not use)
  //latitude   : -90...90
  //longitude  : -180...180
  public String getEstimateTimeDuration(double lat1,double lng1,double lat2,double lng2){
    String time_duration=null;
    double distance=getPseudoDistance(lat1,lng1,lat2,lng2);
    if(distance!=-1)time_duration=getTimeDurationByDistance(distance);
    return time_duration;
  }
  //distance is the sum of two catedus (two triangle sides)
  private double getPseudoDistance(double lat1,double lng1,double lat2,double lng2){
    double distance=-1;

    double lat1_meters=java.lang.Math.abs(lat1),lng1_meters=java.lang.Math.abs(lng1);
    double lat2_meters=java.lang.Math.abs(lat2),lng2_meters=java.lang.Math.abs(lng2);

    if(lat1_meters<0||lat1_meters>90||lng1_meters<0||lng1_meters>180||
    lat2_meters<0||lat2_meters>90||lng2_meters<0||lng2_meters>180)return distance;//check for error

    if(java.lang.Math.abs(lat2_meters-lat1_meters)>1||java.lang.Math.abs(lng2_meters-lng1_meters)>1)
      return distance;//too long distance for (more then 1 degree is more then 111 km)

    if(lng1_meters>90){//more then 90 is similar for less 90
      lng1_meters=90-(lng1_meters-90);
    }
    if(lng2_meters>90){//more then 90 is similar for less 90
      lng2_meters=90-(lng2_meters-90);
    }

    lat1_meters=LatLng.LatMeters[(int)lat1_meters];
    lng1_meters=LatLng.LngMeters[(int)lng1_meters];
    lat2_meters=LatLng.LatMeters[(int)lat2_meters];
    lng2_meters=LatLng.LngMeters[(int)lng2_meters];

    double lat_meters=(lat1_meters!=lat2_meters?(lat2_meters-lat1_meters)/2:lat1_meters),
    lng_meters=(lng1_meters!=lng2_meters?(lng2_meters-lng2_meters)/2:lng1_meters);
    double delta_lat=lat2-lat1,delta_lng=lng2-lng1;
    distance=lat_meters*delta_lat+lng_meters*delta_lng;

    return distance;
  }

  private String getTimeDurationByDistance(double distance){
    String time_duration=null;
    if(distance>100000);
    else if(distance<=100000&&distance>50000)time_duration=String.format(context.getString(R.string.short_minute),60);
    else if(distance<=50000&&distance>20000)time_duration=String.format(context.getString(R.string.short_minute),30);
    else if(distance<=20000&&distance>10000)time_duration=String.format(context.getString(R.string.short_minute),15);
    else if(distance<=10000&&distance>5000)time_duration=String.format(context.getString(R.string.short_minute),10);
    else if(distance<=5000&&distance>4000)time_duration=String.format(context.getString(R.string.short_minute),8);
    else if(distance<=4000&&distance>3000)time_duration=String.format(context.getString(R.string.short_minute),6);
    else if(distance<=3000&&distance>2000)time_duration=String.format(context.getString(R.string.short_minute),5);
    else if(distance<=2000&&distance>1000)time_duration=String.format(context.getString(R.string.short_minute),4);
    else if(distance<=1000&&distance>800)time_duration=String.format(context.getString(R.string.short_minute),3);
    else if(distance<=800&&distance>500)time_duration=String.format(context.getString(R.string.short_minute),2);
    else time_duration=String.format(context.getString(R.string.short_minute),1);
    return time_duration;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //buffered write bytes data to stream
  public void bufferedWriter(byte[] byte_array,OutputStream output_stream)throws IOException{
    int buf_size=T.SIZE_BUFFER_READ,bytes_read;
    byte[] buffer=new byte[buf_size];
    ByteArrayInputStream is=new ByteArrayInputStream(byte_array);
    do{
      bytes_read=is.read(buffer,0,buf_size);
      if(bytes_read==-1)break;
      output_stream.write(buffer,0,bytes_read);
      output_stream.flush();
    }while(bytes_read==buf_size);
  }
  public void bufferedWriter(InputStream input_stream,OutputStream output_stream)throws IOException{
    int buf_size=T.SIZE_BUFFER_READ,bytes_read;
    byte[] buffer=new byte[buf_size];
    InputStream is=input_stream;
    do{
      bytes_read=is.read(buffer,0,buf_size);
      if(bytes_read==-1)break;
      output_stream.write(buffer,0,bytes_read);
      output_stream.flush();
    }while(bytes_read==buf_size);
  }
  //////////////////////////////////////////////////////////////////////////////////////////////////
  //dialog
  private void toLargeDialogSize(Dialog dialog){
    //display
    DisplayMetrics metrics=new DisplayMetrics();//get metrics of screen
    ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
    int height=(int)(metrics.heightPixels*1.0);//set height to 100% of total
    int width=(int)(metrics.widthPixels*1.0);//set width to 100% of total
    dialog.getWindow().setLayout(width,height);//set layout_calendar_dialog
  }
  public Dialog makeSplashDialog(){
    return T.dialogContentBox(context,R.layout.layout_splash,-1,null);
  }
  public Dialog socialNetworkDialog(){
    return T.dialogContentBox(context,R.layout.layout_social_network,-1,null);
  }
  public Dialog prepaidAccountDialog(){
    View.OnClickListener listener=null;
    final Dialog dialog=T.dialogContentBox(context,R.layout.layout_prepaid_account,-1,listener);
    //listener
    listener=new View.OnClickListener(){//button
      public void onClick(View view){
        String prepaid_code=null;
        EditText edit_text_prepaid_code=(EditText)dialog.findViewById(R.id.edit_text_prepaid_code);
        if(edit_text_prepaid_code!=null){
          prepaid_code=edit_text_prepaid_code.getText().toString();
          if(prepaid_code.isEmpty()){
            edit_text_prepaid_code.setHintTextColor(resources.getColor(R.color.bad_text));
            edit_text_prepaid_code.requestFocus();
            return;
          }else edit_text_prepaid_code.setTextColor(resources.getColor(R.color.good_text));
        }
        //increase prepaid amount
        if(prepaid_code!=null){
          increaseUserPrepaidAmountRequest(prepaid_code);
          dialog.cancel();
        }
      }
    };
    Button button_prepaid_code=(Button)dialog.findViewById(R.id.button_prepaid_code);
    if(button_prepaid_code!=null)button_prepaid_code.setOnClickListener(listener);
    return dialog;
  }
  public Dialog makeTripPaymentDialog(){
    View.OnClickListener listener=null;
    final Dialog dialog=T.dialogContentBox(context,R.layout.layout_trip_payment,R.id.button_save,listener);
    toLargeDialogSize(dialog);
    //listener
    listener=new View.OnClickListener(){//button
      public void onClick(View view){
        String payment=T.EMPTY;
        EditText edit_text_payment=(EditText)dialog.findViewById(R.id.edit_text_payment);
        if(edit_text_payment!=null)payment=edit_text_payment.getText().toString().replace(T.COMA,T.POINT);
        //payment by cash(add purchase)
        addPurchaseRequest(tripDuration,tripDistance,pickupTime+tripDuration,System.currentTimeMillis(),payment,afterAddPurchaseCallbackHandler,new RetVal());
        dialog.cancel();
      }
    };
    Button button_save=(Button)dialog.findViewById(R.id.button_save);
    if(button_save!=null)button_save.setOnClickListener(listener);
    EditText edit_text_payment=(EditText)dialog.findViewById(R.id.edit_text_payment);
    if(edit_text_payment!=null&&currentOrderAB!=null)edit_text_payment.setText(getPrice(currentOrderAB.total_price));
    return dialog;
  }
  public Dialog makeTripRatingDialog(final long order_id,final long user_id,final long transport_id,final byte review_value,final String description,final String datetime,final String message){
    Button.OnClickListener listener=null;
    final Dialog dialog=T.dialogContentBox(context,R.layout.layout_trip_rating,R.id.button_save,listener);
    if(review_value==-1){
      listener=new View.OnClickListener(){//button
        public void onClick(View view){
          byte new_review_value=0;
          RatingBar rating_bar=(RatingBar)dialog.findViewById(R.id.rating_bar);
          if(rating_bar!=null)new_review_value=(byte)rating_bar.getRating();
          if(new_review_value!=0){//need a real value!
            //review rating
            addTransportReviewRequest(order_id,user_id,transport_id,new_review_value);
            dialog.cancel();
          }
          }
      };
      Button button_save=(Button)dialog.findViewById(R.id.button_save);
      if(button_save!=null)button_save.setOnClickListener(listener);
      TextView text_view_message=(TextView)dialog.findViewById(R.id.text_view_message);
      if(text_view_message!=null)text_view_message.setText(String.format(context.getString(R.string.estimate_trip),message));
    }
    else{
      RatingBar rating_bar=(RatingBar)dialog.findViewById(R.id.rating_bar);
      if(rating_bar!=null){
        rating_bar.setRating(review_value);
        rating_bar.setIsIndicator(true);
      }
      //set button_save not enabled
      Button button_save=(Button)dialog.findViewById(R.id.button_save);
      if(button_save!=null){
        button_save.setEnabled(false);
        button_save.setTextColor(context.getResources().getColor(R.color.DarkGray));
      }
      TextView text_view_datetime=(TextView)dialog.findViewById(R.id.text_view_datetime);
      if(text_view_datetime!=null)text_view_datetime.setText(datetime);
      TextView text_view_message=(TextView)dialog.findViewById(R.id.text_view_message);
      if(text_view_message!=null)text_view_message.setText(String.format(context.getString(R.string.transport_reviewed),message));
    }
    return dialog;
  }
  public AlertDialog updateOrderStatusDialog(final long order_id,final long status_id,final Fragment fragment){//for driver
    return T.dialogBox(context,context.getString(R.string.message_warning_title),
    context.getString(R.string.update_order_status),
    context.getString(R.string.button_agree),
    context.getString(R.string.button_no),
    new DialogInterface.OnClickListener(){//agree
      public void onClick(DialogInterface dialog,int id){
        updateOrderABStatusRequest(order_id,status_id);
        //trip
        if(status_id==OrderABStatus.ORDER_STATUS_LANDING){//wait for client
          putLandingTime(System.currentTimeMillis());
          editor.commit();
          //restart chronometer
          if(fragment!=null){
            ((TaximeterFragment)fragment).restartLandingDuration();
            ((TaximeterFragment)fragment).stopTripDuration();
            ((TaximeterFragment)fragment).updateOrderStatusButtonName(context.getString(R.string.button_delivering));//job fragment
          }
        }
        else if(status_id==OrderABStatus.ORDER_STATUS_DELIVERING){//pickupTime
          calcDistance=true;
          putTripDistance(0);
          putLandingDuration(System.currentTimeMillis()-landingTime);
          putPickupTime(System.currentTimeMillis());
          editor.commit();
          //stop/restart chronometer
          if(fragment!=null){
            ((TaximeterFragment)fragment).stopLandingDuration();
            ((TaximeterFragment)fragment).restartTripDuration();
            ((TaximeterFragment)fragment).updateOrderStatusButtonName(context.getString(R.string.button_completed));//job fragment
          }
        }
        else if(status_id==OrderABStatus.ORDER_STATUS_COMPLETED){//tripDuration
          calcDistance=false;
          putTripDuration(System.currentTimeMillis()-pickupTime);
          editor.commit();
          //stop chronometer
          if(fragment!=null){
            ((TaximeterFragment)fragment).stopLandingDuration();
            ((TaximeterFragment)fragment).stopTripDuration();
            ((TaximeterFragment)fragment).updateOrderStatusButtonEnabled(false);//job fragment
          }
          //invoice fragment here
          showInvoiceFragmentCallbackHandler.execute(new Boolean(false));
          //client cash payment by trip
          makeTripPaymentDialog();
        }
      }
    },
    new DialogInterface.OnClickListener(){//no
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
      }
    });
  }
  public AlertDialog takeOrderDialog(final long order_id,final long transport_id,final long order_user_id,final View callback_view){
    return T.dialogBox(context,context.getString(R.string.message_warning_title),
    context.getString(R.string.take_order),
    context.getString(R.string.button_agree),
    context.getString(R.string.button_no),
    new DialogInterface.OnClickListener(){//agree
      public void onClick(DialogInterface dialog,int id){
        //removeFragment(T.FRAGMENT_NAME_ORDER_LIST);
        removeListFragment();
        updateOrderABTransportRequest(order_id,transport_id,order_user_id);//take the Order
      }
    },
    new DialogInterface.OnClickListener(){//no
      public void onClick(DialogInterface dialog,int id){
        if(callback_view!=null){
          ((SwitchCompat)callback_view).setChecked(false);
          ((SwitchCompat)callback_view).invalidate();
        }
        dialog.cancel();
      }
    });
  }
  public AlertDialog payOrderDialog(final long order_id,final double total_price){
    return T.dialogBox(context,context.getString(R.string.message_warning_title),
    context.getString(R.string.pay_order),
    context.getString(R.string.button_agree),
    context.getString(R.string.button_no),
    new DialogInterface.OnClickListener(){//agree
      public void onClick(DialogInterface dialog,int id){
        //payment order (may be user like to pay by card?)
        if(paymentProvider!=-1){
          String currency=T.EMPTY;
          if(activeCurrency!=null){
            currency=activeCurrencyTitle!=null?activeCurrencyTitle:activeCurrency.name;
          }
          removeListFragment();//close all opened list
          showPaymentFragment(paymentProvider,order_id,total_price,currency);
        }
      }
    },
    new DialogInterface.OnClickListener(){//no
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
      }
    });
  }
  public AlertDialog makeOrderDialog(final OrderAB order){
    return T.dialogBox(context,context.getString(R.string.message_warning_title),
    context.getString(R.string.make_order)+T.NEXT_LINE+T.OPEN_DOOR+order.id+T.CLOSE_DOOR+T.SPACE+order.delivery_address,
    context.getString(R.string.button_agree),
    context.getString(R.string.button_no),
    new DialogInterface.OnClickListener(){//agree
      public void onClick(DialogInterface dialog,int id){
        //make the Order
        putTripMode(true);
        putTripOrderId(order.id);
        editor.commit();

        ArrayList list_obj=newInstanceOrderABList();
        list_obj.add(order);
        showOrderABCallbackHandler.execute(list_obj);
        //getOrderABRequest(showOrderABCallbackHandler,order.id,-1,-1);
        removeFragment(T.FRAGMENT_NAME_ORDER_LIST);
      }
    },
    new DialogInterface.OnClickListener(){//no
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
      }
    });
  }
  public AlertDialog sendOrderDialog(){
    String text_message=(findAddress!=null&&findAddress.length()>0)?findAddress:context.getString(R.string.delivery_place);
    return T.dialogBox(context,context.getString(R.string.message_warning_title),
    context.getString(R.string.send_order)+T.NEXT_LINE+text_message,
    context.getString(R.string.button_agree),
    context.getString(R.string.button_no),
    new DialogInterface.OnClickListener(){//agree
      public void onClick(DialogInterface dialog,int id){
        //send the Order
        addOrderABRequest();
      }
    },
    new DialogInterface.OnClickListener(){//no
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
      }
    });
  }
  public AlertDialog cancelOrderDialog(final long order_id,final View callback_view){
    return T.dialogBox(context,context.getString(R.string.message_warning_title),
    context.getString(R.string.cancel_order),
    context.getString(R.string.button_agree),
    context.getString(R.string.button_no),
    new DialogInterface.OnClickListener(){//agree
      public void onClick(DialogInterface dialog,int id){
        if(tripMode){
          updateOrderABTransportRequest(order_id,-1,-1);
        }
        else{
          updateOrderABStatusRequest(order_id,OrderABStatus.ORDER_STATUS_CANCELLATION);
        }
        removeFragment(T.FRAGMENT_NAME_ORDER_LIST);
        //remove dropoff
        clearOrderCalcValues();
        putDropoffAddress(null);
        putDropoffName(null);
        editor.commit();
      }
    },
    new DialogInterface.OnClickListener(){//no
      public void onClick(DialogInterface dialog,int id){
        if(callback_view!=null){
          ((SwitchCompat)callback_view).setChecked(true);
          ((SwitchCompat)callback_view).invalidate();
        }
        dialog.cancel();
      }
    });
  }
  public AlertDialog clearCartDialog(){
    return T.dialogBox(context,context.getString(R.string.message_warning_title),
    context.getString(R.string.clear_cart),
    context.getString(R.string.button_agree),
    context.getString(R.string.button_no),
    new DialogInterface.OnClickListener(){//agree
      public void onClick(DialogInterface dialog,int id){
        //clear cart list
        if(cartList!=null){
          cartList.clear();
          cartList=null;
        }
        //clean product list
        uncheckedProductList();
        //close
        removeFragment(T.FRAGMENT_NAME_CART);
        removeListFragment();
      }
    },
    new DialogInterface.OnClickListener(){//no
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
      }
    });
  }
  public AlertDialog cancelTipDialog(){
    return T.dialogBox(context,context.getString(R.string.message_warning_title),
    context.getString(R.string.do_not_show),
    context.getString(R.string.button_yes),
    context.getString(R.string.button_no),
    new DialogInterface.OnClickListener(){//yes
      public void onClick(DialogInterface dialog,int id){
        boolean do_not_show=true;
        putDoNotShow(do_not_show);
        editor.commit();
        putSharedPrefBooleanValue(context.getString(R.string.key_notifications_show_tips),!do_not_show);
        removeFragment(T.FRAGMENT_NAME_TIP);
      }
    },
    new DialogInterface.OnClickListener(){//no
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
      }
    });
  }
  public AlertDialog startDriverDialog(){
    return T.dialogBox(context,context.getString(R.string.message_warning_title),
    context.getString(R.string.start_driver),
    context.getString(R.string.button_yes),
    context.getString(R.string.button_no),
    new DialogInterface.OnClickListener(){//yes
      public void onClick(DialogInterface dialog,int id){
        mapMode=MAP_DRIVER;
        startDispatcherService(mapMode);
        removeAllWithoutMapFragment();
        //hideFragment(T.FRAGMENT_NAME_ORDER_STATUS);
        //removeFragment(T.FRAGMENT_NAME_TIP);
        Map2Fragment frag=(Map2Fragment)getFragment(T.FRAGMENT_NAME_MAP);
        if(frag!=null){
          frag.setCallbackCommand(Map2Fragment.CALLBACK_COMMAND_DRIVER);
          frag.startDriver();
        }
        else showMapFragment(Map2Fragment.CALLBACK_COMMAND_DRIVER,mapProvider);
      }
    },
    new DialogInterface.OnClickListener(){//no
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
      }
    });
  }
  public AlertDialog stopDriverDialog(){
    return T.dialogBox(context,context.getString(R.string.message_warning_title),
    context.getString(R.string.stop_driver),
    context.getString(R.string.button_yes),
    context.getString(R.string.button_no),
    new DialogInterface.OnClickListener(){//yes
      public void onClick(DialogInterface dialog,int id){
        mapMode=MAP_CLIENT;
        if(orderId!=-1)startDispatcherService(mapMode);
        else stopDispatcherService();
        removeAllWithoutMapFragment();
        //removeFragment(T.FRAGMENT_NAME_TIP);
        Map2Fragment frag=(Map2Fragment)getFragment(T.FRAGMENT_NAME_MAP);
        if(frag!=null){
          frag.setCallbackCommand(Map2Fragment.CALLBACK_COMMAND_CLIENT);
          frag.startClient();
        }
        else showMapFragment(Map2Fragment.CALLBACK_COMMAND_CLIENT,mapProvider);
      }
    },
    new DialogInterface.OnClickListener(){//no
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
      }
    });
  }
  //dialog for select product param
  public AlertDialog selectOptionsDialog(final ArrayList<ProductParam> list,CharSequence[] items,final boolean[] checked_items,DialogInterface.OnMultiChoiceClickListener listener){
    return T.dialogListBox(context,
    context.getString(R.string.select_options),
    context.getString(R.string.button_agree),
    context.getString(R.string.button_cancel),
    new DialogInterface.OnClickListener(){//agree
      public void onClick(DialogInterface dialog,int id){
        ProductParam product_param;
        for(int i=0;i<list.size();i++){
          product_param=list.get(i);
          product_param.checked=checked_items[i];
          product_param.part_count=checked_items[i]?1:0;
        }
        double order_price=orderPrice;
        double total_price=getTotalPrice(order_price,list);
        totalPrice=total_price;
        String price_text=getPrice(total_price);
        Fragment frag;

        frag=getFragment(T.FRAGMENT_NAME_TRIP);
        if(frag!=null)((TripFragment)frag).updatePrice(price_text);

        frag=getFragment(T.FRAGMENT_NAME_FIND);
        if(frag!=null)((FindFragment)frag).updatePrice(price_text);
      }
    },
    new DialogInterface.OnClickListener(){//cancel
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
      }
    },items,checked_items,listener);
  }
  public AlertDialog banDialog(){
    return T.dialogBox(context,context.getString(R.string.message_error_title),
    context.getString(R.string.account_blocked),
    context.getString(R.string.button_yes),
    context.getString(R.string.button_no),
    new DialogInterface.OnClickListener(){//yes
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
        System.exit(0);
      }
    },
    new DialogInterface.OnClickListener(){//no
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
        System.exit(0);
      }
    });
  }
  public AlertDialog reloadDialog(){
    return T.dialogBox(context,context.getString(R.string.message_warning_title),
    context.getString(R.string.reload_app),
    context.getString(R.string.button_yes),
    context.getString(R.string.button_no),
    new DialogInterface.OnClickListener(){//yes
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
        if(TYPE_OF_APP==DRIVER_APP)reloadApp(StartDriverActivity.class,context.getPackageName());
        else if(TYPE_OF_APP==CLIENT_APP)reloadApp(StartClientActivity.class,context.getPackageName());
        else reloadApp(StartActivity.class,packageName);
        ((StartActivity)context).finish();
        //System.exit(0);
      }
    },
    new DialogInterface.OnClickListener(){//no
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
      }
    });
  }
  public AlertDialog callToServiceDialog(){
    return T.dialogBox(context,context.getString(R.string.message_warning_title),
    context.getString(R.string.call_to_service),
    context.getString(R.string.button_yes),
    context.getString(R.string.button_no),
    new DialogInterface.OnClickListener(){//yes
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
        phone=context.getString(R.string.app_phone);
        if(DEBUG)Toast.makeText(context,"CALL:"+(phone!=null?phone:T.EMPTY),Toast.LENGTH_SHORT).show();
        try{
          if(phone!=null&&phone.length()>0)context.startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone)));
        }catch(SecurityException se){}

      }
    },
    new DialogInterface.OnClickListener(){//no
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
      }
    });
  }
  public AlertDialog driverCandidateDialog(){
    return T.dialogBox(context,context.getString(R.string.message_info_title),
    context.getString(R.string.driver_candidate_info),
    context.getString(R.string.button_agree),
    context.getString(R.string.button_cancel),
    new DialogInterface.OnClickListener(){//yes
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
      }
    },
    new DialogInterface.OnClickListener(){//no
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
      }
    });
  }

  public AlertDialog passwordRecoveryMessage(){
    return T.messageBox(context,context.getString(R.string.message_info_title),
    context.getString(R.string.password_recovery),
    context.getString(R.string.button_agree));
  }
  public AlertDialog phoneNumberMessage(){
    return T.messageBox(context,context.getString(R.string.message_warning_title),
    context.getString(R.string.phone_number),
    context.getString(R.string.button_agree));
  }
  public AlertDialog increasePrepaidAccountMessage(){
    return T.messageBox(context,context.getString(R.string.message_info_title),
    context.getString(R.string.increase_prepaid_account),
    context.getString(R.string.button_agree));
  }

  //goto internet page
  public void openUrl(String url){
    Uri uri_page=Uri.parse(url);
    Intent intent=new Intent(Intent.ACTION_VIEW,uri_page);
    if(intent.resolveActivity(context.getPackageManager())!=null)context.startActivity(intent);
  }

  //reload app
  public void reloadApp(Class classname,String package_name){
    Intent restart=new Intent(context.getApplicationContext(),classname);
    restart.setPackage(package_name);
    //PendingIntent pending=PendingIntent.getService(context.getApplicationContext(),1,restart,PendingIntent.FLAG_ONE_SHOT);
    PendingIntent pending=PendingIntent.getActivity(context.getApplicationContext(),0,restart,PendingIntent.FLAG_CANCEL_CURRENT);
    AlarmManager alarm=(AlarmManager)context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)//>=23
      alarm.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+T.TASK_RELOAD_TIMEOUT,pending);
    else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)//>=19
      alarm.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+T.TASK_RELOAD_TIMEOUT,pending);
    else //others
      alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+T.TASK_RELOAD_TIMEOUT,pending);
  }
  //fab
  public void fabAction(int mode){
    FloatingActionButton fab;
    //fab.bringToFront(); for Android 4.0 (samsung phones tested)
    //booking fab
    fab=((StartActivity)context).getBookingFloatingActionButton();
    if(bookingStyle!=BOOKING_STYLE_EASY&&bookingStyle!=BOOKING_STYLE_EASY_AB){
      if(mode==MAP_CLIENT){
        if(orderId!=-1)fab.setImageResource(R.drawable.ic_clear_white_24dp);
        else fab.setImageResource(R.drawable.ic_add_white_24dp);
        if(orderStatusId>OrderABStatus.ORDER_STATUS_LANDING||orderStatusId<0)fab.hide();
        else{
          fab.bringToFront();
          fab.show();
        }
      }
      else if(mode==MAP_DRIVER){
        if(!tripMode)fab.hide();
        else{
          fab.setImageResource(R.drawable.ic_clear_white_24dp);
          if(tripOrderStatusId>OrderABStatus.ORDER_STATUS_LANDING||tripOrderStatusId<0)fab.hide();
          else{
            fab.bringToFront();
            fab.show();
          }
        }
      }
    }
    else fab.hide();//no view on easy booking and easy booking AB
    //driver fab
    fab=((StartActivity)context).getDriverFloatingActionButton();
    if(tripMode)fab.hide();
    else if(driverTransportId!=-1){fab.bringToFront();fab.show();}
    //cart fab
    fab=((StartActivity)context).getCartFloatingActionButton();
    if(bookingStyle!=BOOKING_STYLE_EASY&&bookingStyle!=BOOKING_STYLE_EASY_AB){
      if(mode==MAP_CLIENT&&orderId==-1){//not order now
        fab.bringToFront();
        fab.show();//always show
        //if(cartList!=null&&cartList.size()>0&&!isOpenFragment(T.FRAGMENT_CART))fab.show();else fab.hide();
      }
      else if(mode==MAP_DRIVER){
        fab.hide();//always hide
      }
    }
    else fab.hide();//no view on easy booking and easy booking AB
  }

  //private classes
  private class ListWithOptions{ArrayList list=null;boolean checking=false;}
  public ListWithOptions newInstanceListWithOptions(ArrayList list,boolean checking){
    ListWithOptions list_with_options=new ListWithOptions();
    list_with_options.list=list;
    list_with_options.checking=checking;
    return list_with_options;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //payment jungles

  //strip provider
  public boolean validateCardByStripProvider(Card card){
    return card.validateCard();
  }
  private Token stripeProviderToken =null;
  public Token getStripeProviderToken(){return stripeProviderToken;}
  public String getStripeProviderTokenId(){return stripeProviderToken.getId();}
  public Token createTokenByStripeProvider(Card card,final TypedCallback callback){
    if(card==null){
      stripeProviderToken =null;
    }
    else{
      //warning: need to use in real release stripe_current_key!
      String key=(stripeKey!=null&&stripeKey.length()>0&&!stripeKey.equals(DEFAULT_STRIPE_KEY)?
                  stripeKey:context.getString(R.string.stripe_test_key));//"pk_test_6pRNASCoBOKtIshFeQd4XMUh"
      Stripe stripe=new Stripe(context,key);
      stripe.createToken(card,new TokenCallback(){
        public void onSuccess(Token token){
          stripeProviderToken=token;
          if(DEBUG)Toast.makeText(context.getApplicationContext(),"TokenId="+token.getId(),Toast.LENGTH_SHORT).show();
          //Send the token to your server (they want it)
          if(callback!=null)callback.execute(stripeProviderToken);
        }
        public void onError(Exception error){
          stripeProviderToken =null;
          T.messageBox(context,context.getString(R.string.message_error_title),
                               error.getLocalizedMessage(),
                               context.getString(R.string.button_cancel));
        }
      });
    }
    return stripeProviderToken;
  }
  private Source stripeProviderSource =null;
  public Source getStripeProviderSource(){return stripeProviderSource;}
  public String getStripProviderSourceId(){return stripeProviderSource.getId();}
  public Source createSourceByStripeProvider(Card card, double price, String currency, String return_url){
    if(card==null){
      stripeProviderSource =null;
    }
    //3d security card
    String card_id=card.getId();
    SourceParams params=SourceParams.createThreeDSecureParams((int)price*100/*in cents*/,currency,return_url,card_id);
    //warning: need to use in real release stripe_current_key!
    String key=(stripeKey!=null&&stripeKey.length()>0&&!stripeKey.equals(DEFAULT_STRIPE_KEY)?
                stripeKey:context.getString(R.string.stripe_test_key));//"pk_test_6pRNASCoBOKtIshFeQd4XMUh"
    Stripe stripe=new Stripe(context,key);
    Source source=null;
    try{source=stripe.createSourceSynchronous(params);}catch(Exception e){
      T.messageBox(context,context.getString(R.string.message_error_title),
      e.getLocalizedMessage(),
      context.getString(R.string.button_cancel));
    }
    stripeProviderSource=source;
    return stripeProviderSource;
  }

  //google play (donate & contract for development payment)
    /*
    algorithm :
    1. Instantuate GooglePlayPayment (new GooglePlayPayment(); or googlePlayPaymentNewInstance() run)
    2. Receiving getPaymentItems() in thread, after add items to dialog's items list
    3. Start Payment dialog (prepare getLastBillingInfo() run and check billing_purchase_time==0, else addPayment to server)
    4. User select payment item and click to pay button
    5. Run payForItem() in thread and wait for activity intent result
    */
  public GooglePlayPayment googlePlayPaymentNewInstance(TypedCallback callback){return new GooglePlayPayment(context,callback);}
  public TypedCallback afterGooglePlayPaymentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      GooglePlayPayment payment=(GooglePlayPayment)obj;
      int responce_code=-1;
      try{responce_code=payment.getPaymentItems();}catch(Exception e){
        T.messageBox(context,context.getString(R.string.message_error_title),
        e.getLocalizedMessage(),
        context.getString(R.string.button_cancel));
      }
      if(responce_code==GooglePlayPayment.BILLING_RESPONSE_RESULT_OK){
        selectGooglePlayPaymentDialog(payment,new DialogInterface.OnMultiChoiceClickListener(){
          @Override
          public void onClick(DialogInterface dialog,int which,boolean is_checked){
            //nothing
          }
        });
      }
      else showErrorFragment(payment.getErrorMessage(responce_code));
    }
  };
  public AlertDialog selectGooglePlayPaymentDialog(final GooglePlayPayment payment,DialogInterface.OnMultiChoiceClickListener listener){
    if(payment==null||payment.getPaymentItemsList()==null)return null;

    final int size=payment.getPaymentItemsList().length;
    final CharSequence[] items=payment.getPaymentItemsList();
    final boolean[] checked_items=new boolean[size];

    return T.dialogListBox(context,
    context.getString(R.string.select_google_play_payment),
    context.getString(R.string.button_agree),
    context.getString(R.string.button_cancel),
    new DialogInterface.OnClickListener(){//agree
      public void onClick(DialogInterface dialog,int id){
        boolean is_pay;
        String sku,payload;
        for(int i=0;i<size;i++){
          is_pay=checked_items[i];
          if(is_pay){
            sku=payment.getPaymentItemsSku()[i];
            payload=payment.getPaymentItemsList()[i];
            try{payment.payForItem(sku,payload);}catch(Exception e){
              T.messageBox(context,context.getString(R.string.message_error_title),
                           e.getLocalizedMessage(),
                           context.getString(R.string.button_cancel));
            }
          }
        }
      }
    },
    new DialogInterface.OnClickListener(){//cancel
      public void onClick(DialogInterface dialog,int id){
        dialog.cancel();
      }
    },items,checked_items,listener);
  }

  //save google payment info on backend server (not used)
  private boolean paymentSaved=true;
  public TypedCallback addGooglePlayPaymentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      GooglePlayPayment payment=(GooglePlayPayment)obj;
      if(!paymentSaved){
        RetVal ret_val=new RetVal();
        ret_val.temp_object=payment;
        payment.readBillingInfo();
        addPaymentRequest(ZERO,payment.getBillingPayload(),activeCurrency.name,payment.BILLING_SERVICE_NAME,String.valueOf(payment.getBillingPurchaseState()),payment.getBillingPurchaseToken(),T.EMPTY,afterAddGooglePlayPaymentCallbackHandler,ret_val);
      }
    }
  };
  public TypedCallback afterAddGooglePlayPaymentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      RetVal ret_val=(RetVal)obj;
      if(ret_val.value==0){
        showSuccessFragment(context.getString(R.string.message_success_payment));
        if(ret_val.temp_object!=null){
          paymentSaved=true;
        }
      }
    }
  };
}

/*
  handler=new Handler();
  handler.post(new Runnable(){
    @Override
    public void run(){}
  });
*/

/*
  class task extends AsyncTask<Void,Void,Void>{
    private Handler handler;
    private boolean finished=true;
    @Override
    protected void onPreExecute(){
      super.onPreExecute();
      finished=false;
      handler=new Handler();
    }
    @Override
    protected Void doInBackground(Void... params){
      try{

      }catch(Exception e){}
      return null;
    }
    @Override
    protected void onPostExecute(Void result){
      super.onPostExecute(result);
      finished=true;
    }
  }
*/

/* Picasso with OkHttpClient (? questions about right works) [compile 'com.squareup.okhttp3:okhttp:3.4.1']

    Picasso picasso=new Picasso.Builder(context).downloader(new Downloader(){
      OkHttpClient client=new OkHttpClient();
      Call call=null;
      @Override
      public Response load(Uri uri,int networkPolicy)throws IOException{
        Request request=new Request.Builder().url(uri.toString()).build();
        call=client.newCall(request);
        okhttp3.Response response=call.execute();
        String str=response.body().string(),pict=null;
        try{
          JSONObject res=new JSONObject(str);
          JSONArray json_array=res.getJSONArray(T.JSON_PARAM_RESULTS);
          String status=res.getString(T.JSON_PARAM_STATUS);
          if(status.equals(T.JSON_VALUE_SUCCESS)){
            JSONObject json_object=json_array.getJSONObject(0);
            pict=json_object.getString(T.JSON_PARAM_PICTURE);
          }
        }catch(JSONException json_e){}
        byte[] data=null;
        if(pict!=null)data=Base64.decode(pict,Base64.DEFAULT);
        return data!=null?new Response(new ByteArrayInputStream(data),false):null;
      }
      @Override
      public void shutdown(){
        if(call!=null){call.cancel();call=null;}
      }
    }).build();
*/