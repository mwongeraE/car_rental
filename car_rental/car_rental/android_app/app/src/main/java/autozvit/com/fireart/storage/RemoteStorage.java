package autozvit.com.fireart.storage;

import autozvit.com.fireart.R;
import autozvit.com.fireart.objects.Attr;
import autozvit.com.fireart.objects.Color;
import autozvit.com.fireart.objects.Currency;
import autozvit.com.fireart.objects.Direction;
import autozvit.com.fireart.objects.Discount;
import autozvit.com.fireart.objects.Geocode;
import autozvit.com.fireart.objects.Message;
import autozvit.com.fireart.objects.OrderAB;
import autozvit.com.fireart.objects.OrderABPart;
import autozvit.com.fireart.objects.OrderABStatus;
import autozvit.com.fireart.objects.Product;
import autozvit.com.fireart.objects.ProductParam;
import autozvit.com.fireart.objects.Purchase;
import autozvit.com.fireart.objects.RetVal;
import autozvit.com.fireart.objects.Sensor;
import autozvit.com.fireart.objects.Stat;
import autozvit.com.fireart.objects.Tax;
import autozvit.com.fireart.objects.Track;
import autozvit.com.fireart.objects.Transport;
import autozvit.com.fireart.objects.TransportReview;
import autozvit.com.fireart.objects.Type;
import autozvit.com.fireart.objects.User;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.tools.T;
import autozvit.com.fireart.tools.TypedCallback;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class RemoteStorage{
  private static boolean WRITE_TO_LOG=false;
  private static boolean DEBUG_TOAST=false;

  public static final String DEFAULT_USERNAME="customer";
  public static final String DEFAULT_PASSWORD="";
  public static final String DEFAULT_USERTYPE=String.valueOf(User.USER_TYPE_CUSTOMER);
  public static final String DEMO_USERNAME="Demouser";
  public static final String DEMO_PASSWORD="demouser";
  public static final String ADMIN_USERNAME="admin";
  public static final String ADMIN_PASSWORD="";
  public static final String WORKER_USERTYPE=String.valueOf(User.USER_TYPE_WORKER);

  public static final String VALUE_ONE="1";

  //returned_code > 1000 fatal database/connection error
  public static final String RETURN_CODE_SUCCESS="0";
  public static final String RETURN_CODE_USER_DO_NOT_EXIST="-1";
  public static final String RETURN_CODE_USER_ALREADY_EXIST="-2";
  public static final String RETURN_CODE_NOT_AVAIL_PRIV="-3";
  public static final String RETURN_CODE_OPER_WILL_NOT_BE_COMP="-4";
  public static final String RETURN_CODE_RECEP_NOT_EXIST="-5";
  public static final String RETURN_CODE_DATA_NOT_FOUND="-10";
  public static final String RETURN_CODE_UPDATE_DATA_FAILED="-11";
  public static final String RETURN_CODE_PAYMENT_FAILED="-12";

  private static final String RETURN_MESSAGE_SUCCESS="Success";
  private static final String RETURN_MESSAGE_USER_DO_NOT_EXIST="Username or password do not exist";
  private static final String RETURN_MESSAGE_USER_ALREADY_EXIST="Username already exist";
  private static final String RETURN_MESSAGE_NOT_AVAIL_PRIV="Not available privileges";
  private static final String RETURN_MESSAGE_OPER_WILL_NOT_BE_COMP="Operation will not be completed";
  private static final String RETURN_MESSAGE_RECEP_NOT_EXIST="Recipient not exist";
  private static final String RETURN_MESSAGE_DATA_NOT_FOUND="Data not found";
  private static final String RETURN_MESSAGE_UPDATE_DATA_FAILED="Update data failed";
  private static final String RETURN_MESSAGE_PAYMENT_FAILED="Payment failed";

  private long timestamp=System.currentTimeMillis();

  private static String LOG_FILENAME=Environment.getExternalStorageDirectory().getPath()+T.LOCAL_DELIM+T.FILENAME_LOG;//"/mnt/sdcard/fireArt.log"
  private FileOutputStream logFile=null;

  private byte[] buffer=null;
  public void setBuffer(byte[] buffer){this.buffer=buffer;}

  /*google*/
  public static final String GOOGLE_HOSTNAME="http://maps.googleapis.com";
  public static final int COMMAND_GOOGLE_DIRECTIONS=-1;
  private static final String URL_GOOGLE_DIRECTIONS="/maps/api/directions/json?";
  public static final int COMMAND_GOOGLE_GEOCODE=-2;
  private static final String URL_GOOGLE_GEOCODE="/maps/api/geocode/json?";

  /*GET*/
  public static final int COMMAND_GET_PICTURE=1;
  public static final int COMMAND_GET_USER_PICTURE=2;

  public static final int COMMAND_GET_USER_TYPE=9;
  public static final int COMMAND_GET_TOKEN=10;
  public static final int COMMAND_GET_USER=11;
  public static final int COMMAND_GET_CURRENCY=12;
  public static final int COMMAND_GET_TAX=13;
  public static final int COMMAND_GET_PRODUCT=21;
  public static final int COMMAND_GET_ORDER_AB=22;
  public static final int COMMAND_GET_MAX_ORDER_AB=23;
  public static final int COMMAND_GET_ORDER_AB_STATUS=24;
  public static final int COMMAND_GET_ORDER_AB_PART=25;
  public static final int COMMAND_GET_ORDER_AB_NEAR=26;
  public static final int COMMAND_GET_ORDER_AB_NEAR2=27;
  public static final int COMMAND_GET_ORDER_AB_TRANSPORT=28;
  public static final int COMMAND_GET_TRANSPORT=29;
  public static final int COMMAND_GET_MAX_TRACK=31;
  public static final int COMMAND_GET_COLOR=41;
  public static final int COMMAND_GET_TRANSPORT_REVIEW=51;
  public static final int COMMAND_GET_MESSAGE=61;

  public static final int COMMAND_GET_GEOCODE=91;
  public static final int COMMAND_GET_DATA=98;
  public static final int COMMAND_GET_STAT=99;
  /*POST*/
  public static final int COMMAND_ADD_USER=101;
  public static final int COMMAND_ADD_ORDER_AB=102;
  public static final int COMMAND_ADD_ORDER_AB_PART=103;
  public static final int COMMAND_ADD_ORDER_AB_PRODUCT_PARAM_PART=104;
  public static final int COMMAND_ADD_SENSOR=111;
  public static final int COMMAND_ADD_TRACK=112;
  public static final int COMMAND_ADD_SENSOR_CIRCLE=113;
  public static final int COMMAND_ADD_SENSOR_CIRCLE_TO_USER=114;
  public static final int COMMAND_ADD_TRANSPORT=121;
  public static final int COMMAND_ADD_TRANSPORT_REVIEW=122;
  public static final int COMMAND_ADD_USER_REVIEW=123;
  public static final int COMMAND_ADD_PURCHASE=131;
  public static final int COMMAND_ADD_MESSAGE=141;
  public static final int COMMAND_ADD_GEOCODE=191;

  public static final int COMMAND_UPDATE_USER=201;
  public static final int COMMAND_UPDATE_USER_PASSWORD=202;
  public static final int COMMAND_UPDATE_USER_PICTURE=203;
  public static final int COMMAND_UPDATE_PICTURE=204;
  public static final int COMMAND_UPDATE_TRANSPORT=211;

  public static final int COMMAND_UPDATE_ORDER_AB=221;
  public static final int COMMAND_UPDATE_ORDER_AB_STATUS=222;
  public static final int COMMAND_UPDATE_ORDER_AB_TRANSPORT=223;

  public static final int COMMAND_UPDATE_SENSOR_ACTIVITY=231;

  public static final int COMMAND_REMOVE_SENSOR_CIRCLE=301;
  public static final int COMMAND_REMOVE_SENSOR_CIRCLE_TO_USER=302;
  public static final int COMMAND_REMOVE_MESSAGE=311;

  /*payment*/
  public static final int COMMAND_ADD_GOOGLE_PAYMENT=10000;//not used
  public static final int COMMAND_ADD_STRIPE_PAYMENT=10001;

  public static final int COMMAND_INCREASE_USER_PREPAID_AMOUNT_BY_PREPAID_CODE=10901;
  /*email*/
  public static final int COMMAND_PASSWORD_RECOVERY=20001;
  public static final int COMMAND_ORDER_INVOICE=20002;

  /*URL*/
  public static String URL_GET_IMAGE_WITH_AUTH="/service/start?name=pict/get_picture&user=%s&pass=%s&object_name=%s&picture_id=%d";//with auth
  public static String URL_GET_IMAGE_WITH_AUTH_AND_APIKEY="/service/start?name=pict/get_picture&api_key=%s&user=%s&pass=%s&object_name=%s&picture_id=%d";//with auth and api_key
  public static String URL_GET_IMAGE="/service/start?name=pict/get_image&param1=%s&param2=";//no auth
  public static String URL_GET_IMAGE_WITH_APIKEY="/service/start?name=pict/get_image&api_key=%s&param1=%s&param2=";//no auth with api_key

  private String URL_GET_PICTURE="/service/start?name=json/get_picture"+T.AND;
  private String URL_GET_USER_PICTURE="/service/start?name=json/get_user_picture"+T.AND;

  private String URL_GET_USER_TYPE="/service/start?name=json/get_user_type"+T.AND;
  private String URL_GET_TOKEN="/service/start?name=json/get_token"+T.AND;
  private String URL_GET_USER="/service/start?name=json/get_user"+T.AND;
  private String URL_GET_CURRENCY="/service/start?name=json/get_currency"+T.AND;
  private String URL_GET_TAX="/service/start?name=json/get_tax"+T.AND;
  private String URL_GET_PRODUCT="/service/start?name=json/get_product"+T.AND;
  private String URL_GET_ORDER_AB="/service/start?name=json/get_order_AB"+T.AND;
  private String URL_GET_MAX_ORDER_AB="/service/start?name=json/get_max_order_AB"+T.AND;
  private String URL_GET_ORDER_AB_STATUS="/service/start?name=json/get_order_AB_status"+T.AND;
  private String URL_GET_ORDER_AB_PART="/service/start?name=json/get_order_AB_part"+T.AND;
  private String URL_GET_ORDER_AB_NEAR="/service/start?name=json/get_order_AB_near"+T.AND;
  private String URL_GET_ORDER_AB_NEAR2="/service/start?name=json/get_order_AB_near2"+T.AND;
  private String URL_GET_ORDER_AB_TRANSPORT="/service/start?name=json/get_order_AB_transport"+T.AND;
  private String URL_GET_TRANSPORT="/service/start?name=json/get_transport"+T.AND;
  private String URL_GET_MAX_TRACK="/service/start?name=json/get_max_track"+T.AND;
  private String URL_GET_COLOR="/service/start?name=json/get_color"+T.AND;
  private String URL_GET_TRANSPORT_REVIEW="/service/start?name=json/get_transport_review"+T.AND;
  private String URL_GET_MESSAGE="/service/start?name=json/get_message"+T.AND;

  private String URL_GET_GEOCODE="/service/start?name=json/get_geocode"+T.AND;
  private String URL_GET_DATA="/service/start?name=json/get_data"+T.AND;
  private String URL_GET_STAT="/service/start?name=json/get_stat"+T.AND;

  private String URL_ADD_USER="/service/start?name=json/add_user";
  private String URL_ADD_ORDER_AB="/service/start?name=json/add_order_AB";
  private String URL_ADD_ORDER_AB_PART="/service/start?name=json/add_order_AB_part";
  private String URL_ADD_ORDER_AB_PRODUCT_PARAM_PART="/service/start?name=json/add_order_AB_product_param_part";
  private String URL_ADD_SENSOR="/service/start?name=json/add_sensor";
  private String URL_ADD_TRACK="/service/start?name=json/add_track";
  private String URL_ADD_SENSOR_CIRCLE="/service/start?name=json/add_sensor_circle";
  private String URL_ADD_SENSOR_CIRCLE_TO_USER="/service/start?name=json/add_sensor_circle_to_user";
  private String URL_ADD_TRANSPORT="/service/start?name=json/add_transport";
  private String URL_ADD_TRANSPORT_REVIEW="/service/start?name=json/add_transport_review";
  private String URL_ADD_USER_REVIEW="/service/start?name=json/add_user_review";
  private String URL_ADD_PURCHASE="/service/start?name=json/add_purchase";
  private String URL_ADD_MESSAGE="/service/start?name=json/add_message";
  private String URL_ADD_GEOCODE="/service/start?name=json/add_geocode";

  private String URL_UPDATE_USER="/service/start?name=json/update_user";
  private String URL_UPDATE_USER_PASSWORD="/service/start?name=json/update_user_password";
  private String URL_UPDATE_USER_PICTURE="/service/start?name=json/update_user_picture";
  private String URL_UPDATE_PICTURE="/service/start?name=json/update_picture";
  private String URL_UPDATE_TRANSPORT="/service/start?name=json/update_transport";

  private String URL_UPDATE_ORDER_AB="/service/start?name=json/update_order_AB";
  private String URL_UPDATE_ORDER_AB_STATUS="/service/start?name=json/update_order_AB_status";
  private String URL_UPDATE_ORDER_AB_TRANSPORT="/service/start?name=json/update_order_AB_transport";

  private String URL_UPDATE_SENSOR_ACTIVITY="/service/start?name=json/update_sensor_activity";

  private String URL_REMOVE_SENSOR_CIRCLE="/service/start?name=json/remove_sensor_circle";
  private String URL_REMOVE_SENSOR_CIRCLE_TO_USER="/service/start?name=json/remove_sensor_circle_to_user";
  private String URL_REMOVE_MESSAGE="/service/start?name=json/remove_message";

  private String URL_ADD_STRIPE_PAYMENT="/service/start?name=json/add_stripe_payment";
  private String URL_INCREASE_USER_PREPAID_AMOUNT_BY_PREPAID_CODE="/service/start?name=json/increase_user_prepaid_amount_by_prepaid_code";

  private String URL_PASSWORD_RECOVERY="/service/start?name=json/password_recovery"+T.AND;
  private String URL_ORDER_INVOICE="/service/start?name=json/order_invoice"+T.AND;

  private String url=null;
  private String authorization=null;
  public void setAuthorization(String authorization){this.authorization=authorization;}

  private String hostname;
  private Context context;
  private Manager manager;
  private ProgressBar progressBar;

  public RemoteStorage(Manager manager,ProgressBar progress_bar){
    this.manager=manager;
    this.context=manager.getContext();
    this.progressBar=progress_bar;
    try{if(WRITE_TO_LOG)logFile=new FileOutputStream(LOG_FILENAME,true);}catch(Exception e){}
  }
  private String getLanguageParam(){
    return T.JSON_PARAM_LANGUAGE+T.EQUAL+T.getDefaultLanguageName(context);
  }
  private String getURLByCommand(int command){
    String ret_val=T.EMPTY;
    switch(command){
      /*default*/
      case COMMAND_GET_PICTURE:ret_val=URL_GET_PICTURE;break;
      case COMMAND_GET_USER_PICTURE:ret_val=URL_GET_USER_PICTURE;break;

      case COMMAND_GET_USER_TYPE:ret_val=URL_GET_USER_TYPE;break;
      case COMMAND_GET_TOKEN:ret_val=URL_GET_TOKEN;break;
      case COMMAND_GET_USER:ret_val=URL_GET_USER+getLanguageParam()+T.AND;break;
      case COMMAND_GET_CURRENCY:ret_val=URL_GET_CURRENCY+getLanguageParam()+T.AND;break;
      case COMMAND_GET_TAX:ret_val=URL_GET_TAX+getLanguageParam()+T.AND;break;
      case COMMAND_GET_PRODUCT:ret_val=URL_GET_PRODUCT+getLanguageParam()+T.AND;break;
      case COMMAND_GET_ORDER_AB:ret_val=URL_GET_ORDER_AB+getLanguageParam()+T.AND;break;
      case COMMAND_GET_MAX_ORDER_AB:ret_val=URL_GET_MAX_ORDER_AB+getLanguageParam()+T.AND;break;
      case COMMAND_GET_ORDER_AB_STATUS:ret_val=URL_GET_ORDER_AB_STATUS+getLanguageParam()+T.AND;break;
      case COMMAND_GET_ORDER_AB_PART:ret_val=URL_GET_ORDER_AB_PART+getLanguageParam()+T.AND;break;
      case COMMAND_GET_ORDER_AB_NEAR:ret_val=URL_GET_ORDER_AB_NEAR+getLanguageParam()+T.AND;break;
      case COMMAND_GET_ORDER_AB_NEAR2:ret_val=URL_GET_ORDER_AB_NEAR2+getLanguageParam()+T.AND;break;
      case COMMAND_GET_ORDER_AB_TRANSPORT:ret_val=URL_GET_ORDER_AB_TRANSPORT+getLanguageParam()+T.AND;break;
      case COMMAND_GET_TRANSPORT:ret_val=URL_GET_TRANSPORT+getLanguageParam()+T.AND;break;
      case COMMAND_GET_MAX_TRACK:ret_val=URL_GET_MAX_TRACK+getLanguageParam()+T.AND;break;
      case COMMAND_GET_COLOR:ret_val=URL_GET_COLOR;break;
      case COMMAND_GET_TRANSPORT_REVIEW:ret_val=URL_GET_TRANSPORT_REVIEW+getLanguageParam()+T.AND;break;
      case COMMAND_GET_MESSAGE:ret_val=URL_GET_MESSAGE;break;

      case COMMAND_GET_GEOCODE:ret_val=URL_GET_GEOCODE;break;
      case COMMAND_GET_DATA:ret_val=URL_GET_DATA+getLanguageParam()+T.AND;break;
      case COMMAND_GET_STAT:ret_val=URL_GET_STAT;break;

      case COMMAND_ADD_USER:ret_val=URL_ADD_USER;break;
      case COMMAND_ADD_ORDER_AB:ret_val=URL_ADD_ORDER_AB;break;
      case COMMAND_ADD_ORDER_AB_PART:ret_val=URL_ADD_ORDER_AB_PART;break;
      case COMMAND_ADD_ORDER_AB_PRODUCT_PARAM_PART:ret_val=URL_ADD_ORDER_AB_PRODUCT_PARAM_PART;break;
      case COMMAND_ADD_SENSOR:ret_val=URL_ADD_SENSOR;break;
      case COMMAND_ADD_TRACK:ret_val=URL_ADD_TRACK;break;
      case COMMAND_ADD_SENSOR_CIRCLE:ret_val=URL_ADD_SENSOR_CIRCLE;break;
      case COMMAND_ADD_SENSOR_CIRCLE_TO_USER:ret_val=URL_ADD_SENSOR_CIRCLE_TO_USER;break;
      case COMMAND_ADD_TRANSPORT:ret_val=URL_ADD_TRANSPORT;break;
      case COMMAND_ADD_TRANSPORT_REVIEW:ret_val=URL_ADD_TRANSPORT_REVIEW;break;
      case COMMAND_ADD_USER_REVIEW:ret_val=URL_ADD_USER_REVIEW;break;
      case COMMAND_ADD_PURCHASE:ret_val=URL_ADD_PURCHASE;break;
      case COMMAND_ADD_MESSAGE:ret_val=URL_ADD_MESSAGE;break;
      case COMMAND_ADD_GEOCODE:ret_val=URL_ADD_GEOCODE;break;

      case COMMAND_UPDATE_USER:ret_val=URL_UPDATE_USER;break;
      case COMMAND_UPDATE_USER_PASSWORD:ret_val=URL_UPDATE_USER_PASSWORD;break;
      case COMMAND_UPDATE_USER_PICTURE:ret_val=URL_UPDATE_USER_PICTURE;break;
      case COMMAND_UPDATE_PICTURE:ret_val=URL_UPDATE_PICTURE;break;
      case COMMAND_UPDATE_TRANSPORT:ret_val=URL_UPDATE_TRANSPORT;break;

      case COMMAND_UPDATE_ORDER_AB:ret_val=URL_UPDATE_ORDER_AB;break;
      case COMMAND_UPDATE_ORDER_AB_STATUS:ret_val=URL_UPDATE_ORDER_AB_STATUS;break;
      case COMMAND_UPDATE_ORDER_AB_TRANSPORT:ret_val=URL_UPDATE_ORDER_AB_TRANSPORT;break;

      case COMMAND_UPDATE_SENSOR_ACTIVITY:ret_val=URL_UPDATE_SENSOR_ACTIVITY;break;

      case COMMAND_REMOVE_SENSOR_CIRCLE:ret_val=URL_REMOVE_SENSOR_CIRCLE;break;
      case COMMAND_REMOVE_SENSOR_CIRCLE_TO_USER:ret_val=URL_REMOVE_SENSOR_CIRCLE_TO_USER;break;
      case COMMAND_REMOVE_MESSAGE:ret_val=URL_REMOVE_MESSAGE;break;
      /*google*/
      case COMMAND_GOOGLE_DIRECTIONS:ret_val=URL_GOOGLE_DIRECTIONS;break;
      case COMMAND_GOOGLE_GEOCODE:ret_val=URL_GOOGLE_GEOCODE;break;
      /*payment*/
      case COMMAND_ADD_STRIPE_PAYMENT:ret_val=URL_ADD_STRIPE_PAYMENT;break;
      case COMMAND_INCREASE_USER_PREPAID_AMOUNT_BY_PREPAID_CODE:ret_val=URL_INCREASE_USER_PREPAID_AMOUNT_BY_PREPAID_CODE;break;
      /*email*/
      case COMMAND_PASSWORD_RECOVERY:ret_val=URL_PASSWORD_RECOVERY+getLanguageParam()+T.AND;break;
      case COMMAND_ORDER_INVOICE:ret_val=URL_ORDER_INVOICE+getLanguageParam()+T.AND;break;
    }
    return hostname+ret_val;
  }
  //force
  private String forceStringParam(ArrayList<String> param_list,ArrayList<String> value_list){
    String obj=T.EMPTY;
    for(int j=0;j<param_list.size();j++){
      obj+=param_list.get(j)+T.EQUAL+Uri.encode(value_list.get(j))+(j+1<param_list.size()?T.AND:T.EMPTY);
    }
    return obj;
  }
  private String forceStringParamNotUriEncode(ArrayList<String> param_list,ArrayList<String> value_list){
    String obj=T.EMPTY;
    for(int j=0;j<param_list.size();j++){
      obj+=param_list.get(j)+T.EQUAL+value_list.get(j)+(j+1<param_list.size()?T.AND:T.EMPTY);
    }
    return obj;
  }
  private JSONObject forceJsonParam(ArrayList<String> param_list,ArrayList<String> value_list){
    JSONObject obj=new JSONObject();/*{"id":"1","name":"value"}*/
    try{
      for(int j=0;j<param_list.size();j++){
        obj.put(param_list.get(j),value_list.get(j));
      }
    }catch(org.json.JSONException e){}
    return obj;
  }
  private JSONObject forceJsonParam(ArrayList<String> param_list,ArrayList<String> value_list,int from,int to){
    JSONObject obj=new JSONObject();/*{"id":"1","name":"value"}*/
    try{
      for(int j=from;j<to;j++){
        obj.put(param_list.get(j),value_list.get(j));
      }
    }catch(org.json.JSONException e){}
    return obj;
  }
  //message
  private void errorMessage(String message){
    if(manager!=null){
      message=message.replaceAll("&#39;","\"");
      manager.showErrorFragment(message);
    }
  }
  private void successMessage(String message){
    if(manager!=null){
      manager.showSuccessFragment(message);
    }
  }
  private void infoToast(final String info_message){
    if(DEBUG_TOAST)Toast.makeText(context.getApplicationContext(),info_message,Toast.LENGTH_LONG).show();
  }
  //json errorMessage
  private void jsonErrorMessage(JSONObject json_object,int command)throws JSONException{
    boolean is_error_fragment=(manager!=null?manager.isRemoteStorageErrorFragment():false);
    if(!is_error_fragment)return;
    String str;
    if(json_object.has(T.JSON_PARAM_NAME)){
      String name=json_object.getString(T.JSON_PARAM_NAME);
      String value=json_object.getString(T.JSON_PARAM_VALUE);//json_object.getInt(T.JSON_PARAM_VALUE);
      str=name+T.NEXT_LINE+value;
      if(value.equals(RETURN_CODE_USER_ALREADY_EXIST));//==RETURN_CODE_USER_ALREADY_EXIST
      else errorMessage(str+T.SPACE+T.OPEN_DOOR+command+T.CLOSE_DOOR);
    }
    else if(json_object.has(T.JSON_PARAM_MESSAGE)){
      String message=json_object.getString(T.JSON_PARAM_MESSAGE);
      String db_message=json_object.has(T.JSON_PARAM_DATABASE_MESSAGE)?json_object.getString(T.JSON_PARAM_DATABASE_MESSAGE):null;
      String message_code=json_object.getString(T.JSON_PARAM_MESSAGE_CODE);//json_object.getInt(T.JSON_PARAM_VALUE);
      str=message+T.NEXT_LINE+(db_message!=null?db_message:message_code);
      if(message_code.equals(RETURN_CODE_DATA_NOT_FOUND));//==RETURN_CODE_DATA_NOT_FOUND
      else errorMessage(str+T.SPACE+T.OPEN_DOOR+command+T.CLOSE_DOOR);
    }
  }
  //request
  public void jsonRequest(final int command,String hostname,final ArrayList<String> param_list,final ArrayList<String> value_list,final TypedCallback<Object> callback,final Object callback_obj){
  try{
    if(hostname!=null)this.hostname=hostname;
    final boolean is_success_fragment=(manager!=null?manager.isRemoteStorageSuccessFragment():false);

    //large bar need bring to front (in thread)
    if(progressBar!=null){
      ((Activity)manager.getContext()).runOnUiThread(new Runnable(){
        @Override
        public void run(){
          //start progress_bar
          progressBar.bringToFront();
          progressBar.setVisibility(View.VISIBLE);
          progressBar.setProgress(0);
        }
      });
    }
    //api_key
    if(command>0){
      String api_key=(manager!=null?manager.getApiKey():null);
      if(api_key!=null){
        if(param_list!=null&&value_list!=null){
          param_list.add(T.JSON_PARAM_APIKEY);
          value_list.add(api_key);
        }
      }
    }
    //request
    JSONObject request_param=null;
    Object request;
    int request_method=command<100?Request.Method.GET:Request.Method.POST;
    url=command<100?getURLByCommand(command)+forceStringParam(param_list,value_list):getURLByCommand(command);
    if(command<100){/*GET json_object, such as COMMAND_GOOGLE_DIRECTIONS*/
      request=new JsonObjectRequest(request_method,url,request_param,new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject res){
          //stop progress_bar
          if(progressBar!=null){
            progressBar.setProgress(progressBar.getMax());
            progressBar.setVisibility(View.INVISIBLE);
          }
          //response parsing
          try{
            if(command==COMMAND_GOOGLE_DIRECTIONS){//GOOGLE COMMAND
              //infoToast("googleResponseSuccess");
              Direction direction=(Direction)callback_obj;
              String status=res.getString(T.JSON_GOOGLE_PARAM_STATUS);
              if(status.equals(T.JSON_GOOGLE_VALUE_OK)){
                JSONArray routes=res.getJSONArray(T.JSON_GOOGLE_PARAM_ROUTES);
                JSONObject route=(JSONObject)routes.get(0);//first route
                if(route!=null){
                  JSONArray legs=route.getJSONArray(T.JSON_GOOGLE_PARAM_LEGS);
                  JSONObject leg=(JSONObject)legs.get(0);//first leg
                  if(leg!=null){
                    parseDirectionObject(direction,leg);
                    JSONArray steps=leg.getJSONArray(T.JSON_GOOGLE_PARAM_STEPS);
                    direction.steps=new ArrayList();
                    Direction step_direction;
                    for(int i=0;i<steps.length();i++){
                      JSONObject step=steps.getJSONObject(i);
                      step_direction=new Direction();
                      parseDirectionObject(step_direction,step);
                      direction.steps.add(step_direction);
                    }
                  }
                }
                //route.getJSONObject(T.JSON_GOOGLE_PARAM_OVERVIEW_POLYLINE);
                //direction.data=obj.getString(T.JSON_GOOGLE_PARAM_POINTS);
              }
              if(is_success_fragment)successMessage(context.getString(R.string.message_success_title)+res);
            }
            else if(command==COMMAND_GOOGLE_GEOCODE){//GOOGLE COMMAND
              Geocode geocode=(Geocode)callback_obj;
              String status=res.getString(T.JSON_GOOGLE_PARAM_STATUS);
              if(status.equals(T.JSON_GOOGLE_VALUE_OK)){
                JSONArray results=res.getJSONArray(T.JSON_GOOGLE_PARAM_RESULTS);
                JSONObject result=(JSONObject)results.get(0);//first result
                if(result!=null){
                  geocode.formattedAddress=result.getString(T.JSON_GOOGLE_PARAM_FORMATTED_ADDRESS);
                  JSONObject geometry=result.getJSONObject(T.JSON_GOOGLE_PARAM_GEOMETRY);
                  if(geometry!=null){
                    JSONObject bounds=geometry.has(T.JSON_GOOGLE_PARAM_BOUNDS)?geometry.getJSONObject(T.JSON_GOOGLE_PARAM_BOUNDS):null;
                    JSONObject location=geometry.has(T.JSON_GOOGLE_PARAM_LOCATION)?geometry.getJSONObject(T.JSON_GOOGLE_PARAM_LOCATION):null;
                    if(bounds!=null){
                      JSONObject northeast=bounds.getJSONObject(T.JSON_GOOGLE_PARAM_NORTHEAST);
                      JSONObject southwest=bounds.getJSONObject(T.JSON_GOOGLE_PARAM_SOUTHWEST);
                      if(northeast!=null){
                        geocode.northeastLat=northeast.getDouble(T.JSON_GOOGLE_PARAM_LAT);
                        geocode.northeastLon=northeast.getDouble(T.JSON_GOOGLE_PARAM_LNG);
                      }
                      if(southwest!=null){
                        geocode.southwestLat=southwest.getDouble(T.JSON_GOOGLE_PARAM_LAT);
                        geocode.southwestLon=southwest.getDouble(T.JSON_GOOGLE_PARAM_LNG);
                      }
                    }
                    if(location!=null){
                      geocode.locationLat=location.getDouble(T.JSON_GOOGLE_PARAM_LAT);
                      geocode.locationLon=location.getDouble(T.JSON_GOOGLE_PARAM_LNG);
                    }
                  }
                  JSONArray addr_comp=result.getJSONArray(T.JSON_GOOGLE_PARAM_ADDRESS_COMPONENTS);
                  if(addr_comp!=null){
                    JSONObject addr_comp_obj;
                    geocode.addressComponent=new ArrayList();
                    for(int i=0;i<addr_comp.length();i++){
                      addr_comp_obj=addr_comp.getJSONObject(i);
                      if(addr_comp_obj!=null){
                        String name=addr_comp_obj.has(T.JSON_GOOGLE_PARAM_LONG_NAME)?addr_comp_obj.getString(T.JSON_GOOGLE_PARAM_LONG_NAME):T.EMPTY;
                        geocode.addressComponent.add(name);
                      }
                    }
                  }
                }
              }
              if(is_success_fragment)successMessage(context.getString(R.string.message_success_title)+res);
            }
            else{//others COMMAND
              ///////////////////////////////////////////////////////////////////////////
              JSONArray json_array=res.getJSONArray(T.JSON_PARAM_RESULTS);
              String status=res.getString(T.JSON_PARAM_STATUS);
              if(status.equals(T.JSON_VALUE_SUCCESS)&&json_array.length()>0){
                if(command==COMMAND_GET_PICTURE||command==COMMAND_GET_USER_PICTURE){
                  JSONObject json_object=json_array.length()>0?json_array.getJSONObject(0):null;
                  if(json_object!=null){
                    String image_str=json_object.getString(T.JSON_PARAM_PICTURE);
                    byte[] dec_data=Base64.decode(image_str,Base64.DEFAULT);
                    if(manager!=null){
                      manager.showImageFragment(dec_data);
                    }
                  }
                }
                else if(command==COMMAND_GET_GEOCODE){
                  //nothing now
                }
                else if(command==COMMAND_GET_DATA){
                  //object[0]-user object[1]-currency object[2]-token (object[3]-api_keys)
                  if(json_array.length()>=3){
                    //user
                    JSONObject json_object=json_array.getJSONObject(0);//one of
                    JSONArray ja=json_object.getJSONArray(T.JSON_PARAM_USER);
                    if(ja.length()>0){
                      json_object=ja.getJSONObject(0);//only one of object
                      User user=getUserByJSONObject(json_object);
                      if(manager!=null){
                        ArrayList<User> user_list=manager.newInstanceUserList();
                        user_list.add(user);
                      }
                    }
                    //currency
                    json_object=json_array.getJSONObject(1);
                    ja=json_object.getJSONArray(T.JSON_PARAM_CURRENCY);
                    if(ja.length()>0&&manager!=null){
                      Currency currency;
                      ArrayList<Currency> currency_list=manager.newInstanceCurrencyList();
                      for(int i=0;i<ja.length();i++){
                        json_object=ja.getJSONObject(i);
                        currency=getCurrencyByJSONObject(json_object);
                        currency_list.add(currency);
                      }
                    }
                    //token
                    json_object=json_array.getJSONObject(2);
                    String token=json_object.getString(T.JSON_PARAM_TOKEN);
                    if(manager!=null)manager.setToken(token);
                    //api keys
                    if(json_array.length()>3){
                      json_object=json_array.getJSONObject(3);
                      String cognalys_app_id=json_object.getString(T.JSON_PARAM_COGNALYS_APP_ID);
                      String cognalys_access_token=json_object.getString(T.JSON_PARAM_COGNALYS_ACCESS_TOKEN);
                      String google_maps_key=json_object.getString(T.JSON_PARAM_GOOGLE_MAPS_KEY);
                      String paypal_key=json_object.has(T.JSON_PARAM_PAYPAL_KEY)?json_object.getString(T.JSON_PARAM_PAYPAL_KEY):null;
                      String stripe_key=json_object.has(T.JSON_PARAM_STRIPE_KEY)?json_object.getString(T.JSON_PARAM_STRIPE_KEY):null;

                      if(manager!=null){
                        if(cognalys_app_id!=null&&!cognalys_app_id.equals(Manager.DEFAULT_COGNALYS_APP_ID))manager.setCognalysAppId(cognalys_app_id);
                        if(cognalys_access_token!=null&&!cognalys_access_token.equals(Manager.DEFAULT_COGNALYS_ACCESS_TOKEN))manager.setCognalysAccessToken(cognalys_access_token);
                        if(google_maps_key!=null&&!google_maps_key.equals(Manager.DEFAULT_GOOGLE_MAPS_KEY))manager.setGoogleMapsKey(google_maps_key);
                        if(paypal_key!=null&&!paypal_key.equals(Manager.DEFAULT_PAYPAL_KEY))manager.setPaypalKey(paypal_key);
                        if(stripe_key!=null&&!stripe_key.equals(Manager.DEFAULT_STRIPE_KEY))manager.setStripeKey(paypal_key);
                      }
                    }
                  }
                }
                else if(command==COMMAND_GET_STAT){
                  JSONObject json_object;
                  json_object=json_array.getJSONObject(0);//one of
                  Stat stat=(Stat)callback_obj;
                  saveStat(stat,json_object);
                }
                else if(command==COMMAND_GET_USER_TYPE){
                  JSONObject json_object;
                  json_object=json_array.getJSONObject(0);//one of
                  int user_type=json_object.getInt(T.JSON_PARAM_USER_TYPE);
                  if(manager!=null)manager.putUsertype(user_type);
                }
                else if(command==COMMAND_GET_TOKEN){
                  JSONObject json_object;
                  json_object=json_array.getJSONObject(0);//one of
                  String token=json_object.getString(T.JSON_PARAM_TOKEN);
                  if(manager!=null)manager.setToken(token);
                }
                else if(command==COMMAND_GET_USER){
                  JSONObject json_object;
                  User user;
                  ArrayList<User> user_list=(ArrayList)callback_obj;
                  //int row_count=res.isNull(T.JSON_PARAM_ROWS)?-1:res.getInt(T.JSON_PARAM_ROWS);
                  for(int i=0;i<json_array.length();i++){
                    json_object=json_array.getJSONObject(i);
                    user=getUserByJSONObject(json_object);
                    user_list.add(user);
                  }
                  if(is_success_fragment)successMessage(context.getString(R.string.message_success_title));
                  infoToast(res.toString());
                }
                else if(command==COMMAND_GET_CURRENCY){
                  JSONObject json_object;
                  Currency currency;
                  ArrayList<Currency> currency_list=(ArrayList)callback_obj;
                  for(int i=0;i<json_array.length();i++){
                    json_object=json_array.getJSONObject(i);
                    currency=getCurrencyByJSONObject(json_object);
                    currency_list.add(currency);
                  }
                  if(is_success_fragment)successMessage(context.getString(R.string.message_success_title));
                  infoToast(res.toString());
                }
                else if(command==COMMAND_GET_TAX){
                  JSONObject json_object;
                  Tax tax;
                  ArrayList<Tax> tax_list=(ArrayList)callback_obj;
                  //int row_count=res.isNull(T.JSON_PARAM_ROWS)?-1:res.getInt(T.JSON_PARAM_ROWS);
                  for(int i=0;i<json_array.length();i++){
                    json_object=json_array.getJSONObject(i);
                    tax=getTaxByJSONObject(json_object);
                    tax_list.add(tax);
                  }

                  if(is_success_fragment)successMessage(context.getString(R.string.message_success_title));
                  infoToast(res.toString());
                }
                else if(command==COMMAND_GET_TRANSPORT){
                  JSONObject json_object;
                  Transport transport=(Transport)callback_obj;
                  json_object=json_array.length()>0?json_array.getJSONObject(0):null;//only one of object
                  if(json_object!=null&&json_object.length()!=0){
                    //type part & sensor saving in
                    saveTransport(transport,json_object);
                  }
                  if(is_success_fragment)successMessage(context.getString(R.string.message_success_title));
                  infoToast(res.toString());
                }
                else if(command==COMMAND_GET_PRODUCT){
                  JSONObject json_object;
                  Product product;
                  ArrayList<Product> product_list=(ArrayList)callback_obj;
                  //int row_count=res.isNull(T.JSON_PARAM_ROWS)?-1:res.getInt(T.JSON_PARAM_ROWS);
                  for(int i=0;i<json_array.length();i++){
                    json_object=json_array.getJSONObject(i);
                    product=getProductByJSONObject(json_object);
                    product_list.add(product);
                  }
                  if(is_success_fragment)successMessage(context.getString(R.string.message_success_title));
                  infoToast(res.toString());
                }
                else if(command==COMMAND_GET_ORDER_AB||command==COMMAND_GET_MAX_ORDER_AB||
                        command==COMMAND_GET_ORDER_AB_NEAR||command==COMMAND_GET_ORDER_AB_NEAR2||
                        command==COMMAND_GET_ORDER_AB_TRANSPORT){
                  JSONObject json_object;
                  OrderAB order_AB;
                  ArrayList<OrderAB> order_AB_list=(ArrayList)callback_obj;
                  for(int i=0;i<json_array.length();i++){
                    json_object=json_array.getJSONObject(i);
                    order_AB=getOrderABByJSONObject(json_object);
                    order_AB_list.add(order_AB);
                  }
                  if(is_success_fragment)successMessage(context.getString(R.string.message_success_title));
                  infoToast(res.toString());
                }
                else if(command==COMMAND_GET_ORDER_AB_STATUS){
                  JSONObject json_object;
                  JSONArray ja;
                  OrderABStatus order_AB_status=(OrderABStatus)callback_obj;
                  json_object=json_array.length()>0?json_array.getJSONObject(0):null;//only one of object
                  if(json_object!=null&&json_object.length()!=0){
                    saveOrderABStatus(order_AB_status,json_object);
                    ja=json_object.getJSONArray(T.JSON_PARAM_STATUS_ATTR);
                    if(ja.length()>0){
                      order_AB_status.attrList=new ArrayList();
                      saveAttrList(order_AB_status.attrList,ja);
                    }
                  }
                  if(is_success_fragment)successMessage(context.getString(R.string.message_success_title));
                  infoToast(res.toString());
                }
                else if(command==COMMAND_GET_ORDER_AB_PART){
                  JSONObject json_object;
                  OrderABPart order_AB_part;
                  ArrayList<OrderABPart> order_AB_part_list=(ArrayList)callback_obj;
                  for(int i=0;i<json_array.length();i++){
                    json_object=json_array.getJSONObject(i);
                    order_AB_part=getOrderABPartByJSONObject(json_object);
                    order_AB_part_list.add(order_AB_part);
                  }
                  if(is_success_fragment)successMessage(context.getString(R.string.message_success_title));
                  infoToast(res.toString());
                }
                else if(command==COMMAND_GET_MAX_TRACK){
                  JSONObject json_object;
                  JSONArray ja;
                  Track track=(Track)callback_obj;
                  json_object=json_array.length()>0?json_array.getJSONObject(0):null;//only one of object
                  if(json_object!=null&&json_object.length()!=0){
                    saveTrack(track,json_object);
                    ja=json_object.getJSONArray(T.JSON_PARAM_TYPE_ATTR);
                    if(ja.length()>0){
                      track.typeAttrList=new ArrayList();
                      saveAttrList(track.typeAttrList,ja);
                    }
                  }
                  if(is_success_fragment)successMessage(context.getString(R.string.message_success_title));
                  infoToast(res.toString());
                }
                else if(command==COMMAND_GET_COLOR){
                  JSONObject json_object;
                  Color color;
                  ArrayList<Color> color_list=(ArrayList)callback_obj;
                  for(int i=0;i<json_array.length();i++){
                    json_object=json_array.getJSONObject(i);
                    color=new Color();
                    color.name=json_object.getString(T.JSON_PARAM_NAME);
                    color_list.add(color);
                  }
                  if(is_success_fragment)successMessage(context.getString(R.string.message_success_title));
                  infoToast(res.toString());
                }
                else if(command==COMMAND_GET_TRANSPORT_REVIEW){
                  JSONObject json_object;
                  TransportReview transport_review=(TransportReview)callback_obj;
                  json_object=json_array.length()>0?json_array.getJSONObject(0):null;//only one of object
                  if(json_object!=null&&json_object.length()!=0){
                    //user & transport saving in
                    saveTransportReview(transport_review,json_object);
                  }
                  if(is_success_fragment)successMessage(context.getString(R.string.message_success_title));
                  infoToast(res.toString());
                }
                else if(command==COMMAND_GET_MESSAGE){
                  JSONObject json_object;
                  Message message;
                  ArrayList<Message> message_list=(ArrayList)callback_obj;
                  for(int i=0;i<json_array.length();i++){
                    json_object=json_array.getJSONObject(i);
                    message=getMessageByJSONObject(json_object);
                    message_list.add(message);
                  }
                  if(is_success_fragment)successMessage(context.getString(R.string.message_success_title));
                  infoToast(res.toString());
                }
                //next command here ...

              }
              else if(status.equals(T.JSON_VALUE_ERROR)){
                JSONObject json_object=json_array.length()>0?json_array.getJSONObject(0):null;//only one of object
                if(json_object!=null){//ret_val not found(preview error)
                  jsonErrorMessage(json_object,command);
                }
              }
              if(logFile!=null){
                String log_data=new Date(System.currentTimeMillis()).toString()+T.SPACE+url+T.NEXT_LINE+res.toString()+T.NEXT_LINE;
                try{logFile.write(log_data.getBytes());/*logFile.close();*/}catch(Exception e){}
              }
              ///////////////////////////////////////////////////////////////////////////
            }
            //CALLBACK
            if(callback!=null)callback.execute(callback_obj);
          }catch(JSONException e){errorMessage(e.getLocalizedMessage()+T.SPACE+res.toString()+T.SPACE+T.OPEN_DOOR+command+T.CLOSE_DOOR);}
        }
      },new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error){
          //stop progress_bar
          if(progressBar!=null){
            progressBar.setProgress(progressBar.getMax());
            progressBar.setVisibility(View.INVISIBLE);
          }
          parseError(error);
          //show error_page
          NetworkResponse error_response=error.networkResponse;
          if(error_response!=null){
            errorMessage("status code:"+error_response.statusCode+
                        (error_response.data!=null?T.SPACE+new String(error_response.data):T.EMPTY)+T.SPACE+T.OPEN_DOOR+command+T.CLOSE_DOOR);
          }
        }
      });
    }//GET json_object, such as COMMAND_GOOGLE_DIRECTIONS
    else{//POST string_object
      request=new StringRequest(request_method,url,new Response.Listener<String>(){
        @Override
        public void onResponse(String res){
          //stop progress_bar
          if(progressBar!=null){
            progressBar.setProgress(progressBar.getMax());
            progressBar.setVisibility(View.INVISIBLE);
          }
          //response parsing
          try{
            JSONObject response=new JSONObject(res);
            JSONArray json_array=response.getJSONArray(T.JSON_PARAM_RESULTS);
            String status=response.getString(T.JSON_PARAM_STATUS);
            RetVal ret_val=(RetVal)callback_obj;
            ret_val.status=status;
            ret_val.session_id=response.getLong(T.JSON_PARAM_SESSION_ID);
            if(status.equals(T.JSON_VALUE_SUCCESS)){
              if(command==COMMAND_ADD_USER||command==COMMAND_ADD_ORDER_AB||command==COMMAND_ADD_ORDER_AB_PART||
                 command==COMMAND_ADD_ORDER_AB_PRODUCT_PARAM_PART||
                 command==COMMAND_ADD_SENSOR||command==COMMAND_ADD_TRACK||
                 command==COMMAND_ADD_SENSOR_CIRCLE||command==COMMAND_ADD_SENSOR_CIRCLE_TO_USER||
                 command==COMMAND_ADD_TRANSPORT||command==COMMAND_ADD_TRANSPORT_REVIEW||command==COMMAND_ADD_USER_REVIEW||
                 command==COMMAND_ADD_PURCHASE||command==COMMAND_ADD_GEOCODE||
                 command==COMMAND_REMOVE_SENSOR_CIRCLE||command==COMMAND_REMOVE_SENSOR_CIRCLE_TO_USER||
                 command==COMMAND_UPDATE_SENSOR_ACTIVITY||
                 command==COMMAND_UPDATE_PICTURE||command==COMMAND_UPDATE_USER_PICTURE||
                 command==COMMAND_UPDATE_USER||command==COMMAND_UPDATE_USER_PASSWORD||
                 command==COMMAND_UPDATE_TRANSPORT||command==COMMAND_UPDATE_ORDER_AB||
                 command==COMMAND_UPDATE_ORDER_AB_STATUS||command==COMMAND_UPDATE_ORDER_AB_TRANSPORT||
                 command==COMMAND_ADD_STRIPE_PAYMENT||command==COMMAND_ADD_MESSAGE||command==COMMAND_REMOVE_MESSAGE||
                 command==COMMAND_PASSWORD_RECOVERY||command==COMMAND_ORDER_INVOICE||
                 command==COMMAND_INCREASE_USER_PREPAID_AMOUNT_BY_PREPAID_CODE){
                JSONObject json_object=json_array.length()>0?json_array.getJSONObject(0):null;//only one of object
                if(json_object!=null){
                  String name=json_object.has(T.JSON_PARAM_NAME)?json_object.getString(T.JSON_PARAM_NAME):null;
                  long value=json_object.has(T.JSON_PARAM_VALUE)?json_object.getLong(T.JSON_PARAM_VALUE):-1;
                  ret_val.name=name;
                  ret_val.value=value;//always return number value
                  if(is_success_fragment)successMessage(context.getString(R.string.message_success_title));
                }
              }
              //next command here ...

            }
            else if(status.equals(T.JSON_VALUE_ERROR)){
              JSONObject json_object=json_array.length()>0?json_array.getJSONObject(0):null;//only one of object
              if(json_object!=null){
                ret_val.name=json_object.has(T.JSON_PARAM_NAME)?json_object.getString(T.JSON_PARAM_NAME):null;
                ret_val.value=json_object.has(T.JSON_PARAM_VALUE)?json_object.getLong(T.JSON_PARAM_VALUE):-1;
                ret_val.message=json_object.has(T.JSON_PARAM_MESSAGE)?json_object.getString(T.JSON_PARAM_MESSAGE):null;
                ret_val.database_message=json_object.has(T.JSON_PARAM_DATABASE_MESSAGE)?json_object.getString(T.JSON_PARAM_DATABASE_MESSAGE):null;
                ret_val.message_code=json_object.has(T.JSON_PARAM_MESSAGE_CODE)?json_object.getLong(T.JSON_PARAM_MESSAGE_CODE):-1;
                //deocode error for dublicate address
                if(command!=COMMAND_ADD_GEOCODE)jsonErrorMessage(json_object,command);
              }
            }
            if(logFile!=null){
              String log_data=new Date(System.currentTimeMillis()).toString()+T.SPACE+url+T.NEXT_LINE+res+T.NEXT_LINE;
              try{logFile.write(log_data.getBytes());/*logFile.close();*/}catch(Exception e){}
            }
            //CALLBACK
            if(callback!=null)callback.execute(callback_obj);
          }catch(Exception e){errorMessage(e.getLocalizedMessage()+T.SPACE+T.OPEN_DOOR+command+T.CLOSE_DOOR);}
        }
      },new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error){
          //stop progress_bar
          if(progressBar!=null){
            progressBar.setProgress(progressBar.getMax());
            progressBar.setVisibility(View.INVISIBLE);
          }
          parseError(error);
          //show error_page
          NetworkResponse error_response=error.networkResponse;
          if(error_response!=null){
            errorMessage("status code:"+error_response.statusCode+
            (error_response.data!=null?T.SPACE+new String(error_response.data):T.EMPTY)+T.SPACE+T.OPEN_DOOR+command+T.CLOSE_DOOR);
          }
        }
      }){
        @Override
        public byte[] getBody(){
          if(buffer!=null){
            return createMultipart(buffer);
          }
          else{
            String body=forceStringParam(param_list,value_list);
            /*String body=(command==COMMAND_UPDATE_USER_PICTURE||command==COMMAND_UPDATE_PICTURE)?
                           forceStringParamNotUriEncode(param_list,value_list):forceStringParam(param_list,value_list);*/
            if(logFile!=null){
              String log_data=new Date(System.currentTimeMillis()).toString()+T.SPACE+url+T.SPACE+authorization+T.NEXT_LINE+body+T.NEXT_LINE;
              try{logFile.write(log_data.getBytes());}catch(Exception e){}
            }
            return body.getBytes();
          }
        }
        @Override
        public String getBodyContentType(){return "application/json; charset=utf-8";}
        //Authorization: <token> | Multipart send
        /*public Map<String,String> getHeaders() throws AuthFailureError{
          Map<String,String> header=new HashMap<>();
          if(buffer!=null){
            header.put("Content-Type","multipart/form-data;boundary=picture-"+timestamp);
            //header.put("Content-Disposition","form-data; name=\"picture\"; filename=\"picture.jpg\"");
            //header.put("Content-Type","image/png");
          }
          if(authorization!=null){
            header.put("Authorization",authorization);
          }
          return header;
        }*/
      };
    }
    //Access the RequestQueue through your singleton class.
    Singleton.getInstance(context).addToRequestQueue((com.android.volley.Request)request);
  }catch(Exception e){/*errorMessage(e.toString());*/}//temp to debug
  }

  //parse error
  private void parseError(VolleyError error){
    if(error instanceof NetworkError)errorMessage(context.getString(R.string.network_error));
    else if(error instanceof ServerError)errorMessage(context.getString(R.string.server_error));
    else if(error instanceof AuthFailureError)errorMessage(context.getString(R.string.auth_failure_error));
    else if(error instanceof ParseError)errorMessage(context.getString(R.string.parse_error));
    else if(error instanceof NoConnectionError)errorMessage(context.getString(R.string.no_connection_error));
    else if(error instanceof TimeoutError)errorMessage(context.getString(R.string.timeout_error));
  }
  //multipart
  private byte[] createMultipart(byte[] buffer){
    DataOutputStream dos=null;
    String line_end="\r\n";
    String boundary="picture-"+timestamp;
    String two_hyphens="--";
    ByteArrayOutputStream bos=new ByteArrayOutputStream();
    dos = new DataOutputStream(bos);
    try{
      dos.writeBytes(two_hyphens+boundary+line_end);
      dos.writeBytes("Content-Disposition: form-data; name=\"pic\"; filename=\"picture.jpg\""+line_end);
      dos.writeBytes(line_end);
      dos.write(buffer);
      dos.writeBytes(line_end);
      dos.writeBytes(two_hyphens+boundary+two_hyphens+line_end);
    }catch(IOException io_e){}
    return bos.toByteArray();
  }
  //direction
  private Direction parseDirectionObject(Direction direction,JSONObject obj) throws JSONException{
    JSONObject duration_obj=obj.getJSONObject(T.JSON_GOOGLE_PARAM_DURATION);
    JSONObject distance_obj=obj.getJSONObject(T.JSON_GOOGLE_PARAM_DISTANCE);
    if(duration_obj!=null){
      direction.duration=duration_obj.getInt(T.JSON_GOOGLE_PARAM_VALUE);
      direction.durationText=duration_obj.getString(T.JSON_GOOGLE_PARAM_TEXT);
    }
    if(distance_obj!=null){
      direction.distance=distance_obj.getInt(T.JSON_GOOGLE_PARAM_VALUE);
      direction.distanceText=distance_obj.getString(T.JSON_GOOGLE_PARAM_TEXT);
    }
    JSONObject start_location_obj=obj.getJSONObject(T.JSON_GOOGLE_PARAM_START_LOCATION);
    JSONObject end_location_obj=obj.getJSONObject(T.JSON_GOOGLE_PARAM_END_LOCATION);
    if(start_location_obj!=null){
      direction.startLocationLat=start_location_obj.getDouble(T.JSON_GOOGLE_PARAM_LAT);
      direction.startLocationLon=start_location_obj.getDouble(T.JSON_GOOGLE_PARAM_LNG);
    }
    if(end_location_obj!=null){
      direction.finishLocationLat=end_location_obj.getDouble(T.JSON_GOOGLE_PARAM_LAT);
      direction.finishLocationLon=end_location_obj.getDouble(T.JSON_GOOGLE_PARAM_LNG);
    }
    if(obj.has(T.JSON_GOOGLE_PARAM_START_ADDRESS))
      direction.startAddress=obj.getString(T.JSON_GOOGLE_PARAM_START_ADDRESS);
    if(obj.has(T.JSON_GOOGLE_PARAM_END_ADDRESS))
      direction.finishAddress=obj.getString(T.JSON_GOOGLE_PARAM_END_ADDRESS);
    if(obj.has(T.JSON_GOOGLE_PARAM_HTML_INSTRUCTIONS))
      direction.instructions=obj.getString(T.JSON_GOOGLE_PARAM_HTML_INSTRUCTIONS);
    return direction;
  }
  //get
  private User getUserByJSONObject(JSONObject json_object)throws JSONException{
    User user=new User();
    saveUser(user,json_object);
    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_DISCOUNT);
    if(json_array.length()>0){
      JSONObject json_obj=(JSONObject)json_array.get(0);//one of discount
      if(json_obj.length()>0){
        user.discount=new Discount();
        saveDiscount(user.discount,json_obj);
      }
    }
    return user;
  }
  private Tax getTaxByJSONObject(JSONObject json_object)throws JSONException{
    Tax tax=new Tax();
    saveTax(tax,json_object);
    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_TAX_ATTR);
    if(json_array.length()>0){
      tax.attrList=new ArrayList();
      saveAttrList(tax.attrList,json_array);
    }
    return tax;
  }
  private Currency getCurrencyByJSONObject(JSONObject json_object)throws JSONException{
    Currency currency=new Currency();
    saveCurrency(currency,json_object);
    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_CURRENCY_ATTR);
    if(json_array.length()>0){
      currency.attrList=new ArrayList();
      saveAttrList(currency.attrList,json_array);
    }
    return currency;
  }
  private Message getMessageByJSONObject(JSONObject json_object)throws JSONException{
    Message message=new Message();
    message.id=json_object.isNull(T.JSON_PARAM_MESSAGE_ID)?-1:json_object.getLong(T.JSON_PARAM_MESSAGE_ID);
    message.type=json_object.isNull(T.JSON_PARAM_MESSAGE_TYPE)?0:json_object.getInt(T.JSON_PARAM_MESSAGE_TYPE);
    message.userA_id=json_object.isNull(T.JSON_PARAM_USERA_ID)?-1:json_object.getLong(T.JSON_PARAM_USERA_ID);
    message.userB_id=json_object.isNull(T.JSON_PARAM_USERB_ID)?-1:json_object.getLong(T.JSON_PARAM_USERB_ID);
    message.message=json_object.getString(T.JSON_PARAM_MESSAGE);
    message.create_date=json_object.getString(T.JSON_PARAM_CREATE_DATE);

    //getUserA
    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_USERA);
    if(json_array.length()>0){
      JSONObject json_obj=json_array.getJSONObject(0);//one of
      if(json_obj.length()>0){//not empty
        message.userA=new User();
        saveUser(message.userA,json_obj);
      }
    }

    //getUserB
    json_array=json_object.getJSONArray(T.JSON_PARAM_USERB);
    if(json_array.length()>0){
      JSONObject json_obj=json_array.getJSONObject(0);//one of
      if(json_obj.length()>0){//not empty
        message.userB=new User();
        saveUser(message.userB,json_obj);
      }
    }

    return message;
  }
  private Product getProductByJSONObject(JSONObject json_object)throws JSONException{
    Product product=new Product();
    saveProduct(product,json_object);
    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_DISCOUNT);
    if(json_array.length()>0){
      JSONObject json_obj=(JSONObject)json_array.get(0);//one of discount
      if(json_obj.length()>0){
        product.discount=new Discount();
        saveDiscount(product.discount,json_obj);
      }
    }
    json_array=json_object.getJSONArray(T.JSON_PARAM_PARAM_PART);
    if(json_array.length()>0){
      product.paramList=new ArrayList();
      saveProductParamList(product.paramList,json_array);
    }
    json_array=json_object.getJSONArray(T.JSON_PARAM_TYPE_PART);
    if(json_array.length()>0){
      product.typeList=new ArrayList();
      saveTypeList(product.typeList,json_array);
    }
    json_array=json_object.getJSONArray(T.JSON_PARAM_PRODUCT_ATTR);
    if(json_array.length()>0){
      product.attrList=new ArrayList();
      saveAttrList(product.attrList,json_array);
    }
    return product;
  }
  private OrderAB getOrderABByJSONObject(JSONObject json_object)throws JSONException{
    OrderAB order_AB=new OrderAB();
    saveOrderAB(order_AB,json_object);
    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_STATUS_ATTR);
    if(json_array.length()>0){
      order_AB.statusAttrList=new ArrayList();
      saveAttrList(order_AB.statusAttrList,json_array);
    }
    return order_AB;
  }
  private OrderABPart getOrderABPartByJSONObject(JSONObject json_object)throws JSONException{
    OrderABPart order_AB_part=new OrderABPart();
    order_AB_part.order_id=json_object.isNull(T.JSON_PARAM_ORDER_ID)?-1:json_object.getLong(T.JSON_PARAM_ORDER_ID);
    order_AB_part.product_id=json_object.isNull(T.JSON_PARAM_PRODUCT_ID)?-1:json_object.getLong(T.JSON_PARAM_PRODUCT_ID);
    order_AB_part.product_count=json_object.isNull(T.JSON_PARAM_PRODUCT_COUNT)?0:json_object.getLong(T.JSON_PARAM_PRODUCT_COUNT);
    order_AB_part.total_price=json_object.isNull(T.JSON_PARAM_TOTAL_PRICE)?0.00:json_object.getDouble(T.JSON_PARAM_TOTAL_PRICE);
    order_AB_part.last_update=json_object.getString(T.JSON_PARAM_LAST_UPDATE);
    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_PRODUCT);
    if(json_array.length()>0){
      order_AB_part.product=getProductByJSONObject(json_array.getJSONObject(0));//one of product
    }
    return order_AB_part;
  }
  //save
  private void saveAttrPart(Attr obj,JSONObject json_object)throws JSONException{
    obj.id=json_object.isNull(T.JSON_PARAM_ATTR_PART_ID)?-1:json_object.getLong(T.JSON_PARAM_ATTR_PART_ID);
    obj.name=json_object.getString(T.JSON_PARAM_ATTR_NAME);
    obj.value=json_object.getString(T.JSON_PARAM_ATTR_VALUE);
    if(json_object.has(T.JSON_PARAM_LANGUAGE))obj.language=json_object.getString(T.JSON_PARAM_LANGUAGE);
    if(json_object.has(T.JSON_PARAM_LAST_UPDATE))obj.last_update=json_object.getString(T.JSON_PARAM_LAST_UPDATE);
  }
  private void saveAttrList(ArrayList<Attr> list,JSONArray json_array)throws JSONException{
    Attr attr_part;
    for(int i=0;i<json_array.length();i++){
      attr_part=new Attr();
      saveAttrPart(attr_part,(JSONObject)json_array.get(i));
      list.add(attr_part);
    }
  }
  private void saveProductParam(ProductParam obj,JSONObject json_object)throws JSONException{
    obj.id=json_object.isNull(T.JSON_PARAM_PRODUCT_PARAM_ID)?-1:json_object.getLong(T.JSON_PARAM_PRODUCT_PARAM_ID);
    if(json_object.has(T.JSON_PARAM_PARAM_PARENT_ID))obj.parent_id=json_object.isNull(T.JSON_PARAM_PARAM_PARENT_ID)?-1:json_object.getLong(T.JSON_PARAM_PARAM_PARENT_ID);
    obj.name=json_object.getString(T.JSON_PARAM_PRODUCT_PARAM_NAME);
    obj.value=json_object.getString(T.JSON_PARAM_PRODUCT_PARAM_VALUE);
    if(json_object.has(T.JSON_PARAM_LANGUAGE))obj.language=json_object.getString(T.JSON_PARAM_LANGUAGE);
    if(json_object.has(T.JSON_PARAM_LAST_UPDATE))obj.last_update=json_object.getString(T.JSON_PARAM_LAST_UPDATE);
    obj.part_value=json_object.getString(T.JSON_PARAM_PRODUCT_PARAM_PART_VALUE);
    obj.part_count=(!json_object.has(T.JSON_PARAM_PRODUCT_PARAM_PART_COUNT)||json_object.isNull(T.JSON_PARAM_PRODUCT_PARAM_PART_COUNT))?0:json_object.getInt(T.JSON_PARAM_PRODUCT_PARAM_PART_COUNT);
    obj.part_price=json_object.isNull(T.JSON_PARAM_PRODUCT_PARAM_PART_PRICE)?0.00:json_object.getDouble(T.JSON_PARAM_PRODUCT_PARAM_PART_PRICE);

    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_PARAM_ATTR);
    if(json_array.length()>0){
      obj.attrList=new ArrayList();
      saveAttrList(obj.attrList,json_array);
    }
  }
  private void saveProductParamList(ArrayList<ProductParam> list,JSONArray json_array)throws JSONException{
    ProductParam product_param;
    for(int i=0;i<json_array.length();i++){
      product_param=new ProductParam();
      saveProductParam(product_param,(JSONObject)json_array.get(i));
      list.add(product_param);
    }
  }
  private void saveType(Type obj,JSONObject json_object)throws JSONException{
    obj.id=json_object.isNull(T.JSON_PARAM_TYPE_ID)?-1:json_object.getLong(T.JSON_PARAM_TYPE_ID);
    if(json_object.has(T.JSON_PARAM_TYPE_PARENT_ID))obj.parent_id=json_object.isNull(T.JSON_PARAM_TYPE_PARENT_ID)?-1:json_object.getLong(T.JSON_PARAM_TYPE_PARENT_ID);
    obj.name=json_object.getString(T.JSON_PARAM_TYPE_NAME);
    obj.description=json_object.getString(T.JSON_PARAM_DESCRIPTION);
    if(json_object.has(T.JSON_PARAM_LANGUAGE))obj.language=json_object.getString(T.JSON_PARAM_LANGUAGE);
    if(json_object.has(T.JSON_PARAM_LAST_UPDATE))obj.last_update=json_object.getString(T.JSON_PARAM_LAST_UPDATE);

    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_TYPE_ATTR);
    if(json_array.length()>0){
      obj.attrList=new ArrayList();
      saveAttrList(obj.attrList,json_array);
    }
  }
  private void saveTypeList(ArrayList<Type> list,JSONArray json_array)throws JSONException{
    Type type;
    for(int i=0;i<json_array.length();i++){
      type=new Type();
      saveType(type,(JSONObject)json_array.get(i));
      list.add(type);
    }
  }
  private void saveDiscount(Discount obj,JSONObject json_object)throws JSONException{
    obj.id=json_object.isNull(T.JSON_PARAM_DISCOUNT_ID)?-1:json_object.getLong(T.JSON_PARAM_DISCOUNT_ID);
    obj.product_type_id=json_object.isNull(T.JSON_PARAM_PRODUCT_TYPE_ID)?-1:json_object.getLong(T.JSON_PARAM_PRODUCT_TYPE_ID);
    obj.type=json_object.isNull(T.JSON_PARAM_DISCOUNT_TYPE)?0:json_object.getInt(T.JSON_PARAM_DISCOUNT_TYPE);
    obj.code=json_object.getString(T.JSON_PARAM_DISCOUNT_CODE);
    obj.name=json_object.getString(T.JSON_PARAM_DISCOUNT_NAME);
    obj.value=json_object.isNull(T.JSON_PARAM_DISCOUNT_VALUE)?0:json_object.getDouble(T.JSON_PARAM_DISCOUNT_VALUE);
    obj.language=json_object.getString(T.JSON_PARAM_LANGUAGE);
    obj.start_date=json_object.getString(T.JSON_PARAM_START_DATE);
    obj.finish_date=json_object.getString(T.JSON_PARAM_FINISH_DATE);
    if(json_object.has(T.JSON_PARAM_CREATE_DATE))obj.create_date=json_object.getString(T.JSON_PARAM_CREATE_DATE);
    if(json_object.has(T.JSON_PARAM_LAST_UPDATE))obj.last_update=json_object.getString(T.JSON_PARAM_LAST_UPDATE);

    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_DISCOUNT_ATTR);
    if(json_array.length()>0){
      obj.attrList=new ArrayList();
      saveAttrList(obj.attrList,json_array);
    }
  }
  private void saveUser(User obj,JSONObject json_object)throws JSONException{
    obj.id=json_object.isNull(T.JSON_PARAM_USER_ID)?-1:json_object.getLong(T.JSON_PARAM_USER_ID);
    obj.type=json_object.isNull(T.JSON_PARAM_USER_TYPE)?0:json_object.getInt(T.JSON_PARAM_USER_TYPE);
    obj.discount_code=json_object.getString(T.JSON_PARAM_DISCOUNT_CODE);
    obj.first_name=json_object.getString(T.JSON_PARAM_FIRSTNAME);
    obj.last_name=json_object.getString(T.JSON_PARAM_LASTNAME);
    obj.call_name=json_object.getString(T.JSON_PARAM_CALLNAME);
    obj.email=json_object.getString(T.JSON_PARAM_EMAIL);
    obj.phone=json_object.getString(T.JSON_PARAM_PHONE);
    if(json_object.has(T.JSON_PARAM_USER_USERNAME))obj.username=json_object.getString(T.JSON_PARAM_USER_USERNAME);
    if(json_object.has(T.JSON_PARAM_PREPAID_AMOUNT))obj.prepaid_amount=json_object.getDouble(T.JSON_PARAM_PREPAID_AMOUNT);
    if(json_object.has(T.JSON_PARAM_USER_RATE))obj.rate=json_object.getDouble(T.JSON_PARAM_USER_RATE);
    obj.active=json_object.getBoolean(T.JSON_PARAM_ACTIVE);
    if(json_object.has(T.JSON_PARAM_CREATE_DATE))obj.create_date=json_object.getString(T.JSON_PARAM_CREATE_DATE);
    if(json_object.has(T.JSON_PARAM_LAST_UPDATE))obj.last_update=json_object.getString(T.JSON_PARAM_LAST_UPDATE);
  }
  private void saveTax(Tax obj,JSONObject json_object)throws JSONException{
    obj.id=json_object.isNull(T.JSON_PARAM_TAX_ID)?-1:json_object.getLong(T.JSON_PARAM_TAX_ID);
    if(json_object.has(T.JSON_PARAM_TAX_CODE))obj.code=json_object.getString(T.JSON_PARAM_TAX_CODE);
    obj.name=json_object.getString(T.JSON_PARAM_TAX_NAME);
    obj.value=json_object.isNull(T.JSON_PARAM_TAX_VALUE)?0.00:json_object.getDouble(T.JSON_PARAM_TAX_VALUE);
    obj.language=json_object.getString(T.JSON_PARAM_LANGUAGE);
    if(json_object.has(T.JSON_PARAM_LAST_UPDATE))obj.last_update=json_object.getString(T.JSON_PARAM_LAST_UPDATE);

    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_TAX_ATTR);
    if(json_array.length()>0){
      obj.attrList=new ArrayList();
      saveAttrList(obj.attrList,json_array);
    }
  }
  private void saveCurrency(Currency obj,JSONObject json_object)throws JSONException{
    obj.id=json_object.isNull(T.JSON_PARAM_CURRENCY_ID)?-1:json_object.getLong(T.JSON_PARAM_CURRENCY_ID);
    obj.name=json_object.getString(T.JSON_PARAM_CURRENCY_NAME);
    obj.description=json_object.getString(T.JSON_PARAM_DESCRIPTION);
    obj.value=json_object.isNull(T.JSON_PARAM_CURRENCY_VALUE)?0.00:json_object.getDouble(T.JSON_PARAM_CURRENCY_VALUE);
    obj.active=json_object.getBoolean(T.JSON_PARAM_ACTIVE);
    obj.language=json_object.getString(T.JSON_PARAM_LANGUAGE);
    obj.last_update=json_object.getString(T.JSON_PARAM_LAST_UPDATE);
  }
  private void saveProduct(Product obj,JSONObject json_object)throws JSONException{
    obj.id=json_object.isNull(T.JSON_PARAM_PRODUCT_ID)?-1:json_object.getLong(T.JSON_PARAM_PRODUCT_ID);
    obj.manufacture_id=json_object.isNull(T.JSON_PARAM_MANUFACTURE_ID)?-1:json_object.getLong(T.JSON_PARAM_MANUFACTURE_ID);
    obj.discount_code=json_object.getString(T.JSON_PARAM_DISCOUNT_CODE);
    obj.name=json_object.getString(T.JSON_PARAM_PRODUCT_NAME);
    obj.description=json_object.getString(T.JSON_PARAM_DESCRIPTION);
    obj.code=json_object.getString(T.JSON_PARAM_PRODUCT_CODE);
    obj.price=json_object.isNull(T.JSON_PARAM_PRODUCT_PRICE)?0.00:json_object.getDouble(T.JSON_PARAM_PRODUCT_PRICE);
    //now rate is number always
    obj.rate=json_object.isNull(T.JSON_PARAM_PRODUCT_RATE)?0:json_object.getDouble(T.JSON_PARAM_PRODUCT_RATE);
    obj.stock_count=json_object.isNull(T.JSON_PARAM_STOCK_COUNT)?0:json_object.getLong(T.JSON_PARAM_STOCK_COUNT);
    if(json_object.has(T.JSON_PARAM_LAST_UPDATE))obj.last_update=json_object.getString(T.JSON_PARAM_LAST_UPDATE);
  }
  private void saveOrderABStatus(OrderABStatus obj,JSONObject json_object)throws JSONException{
    obj.order_id=json_object.isNull(T.JSON_PARAM_ORDER_ID)?-1:json_object.getLong(T.JSON_PARAM_ORDER_ID);
    obj.status_id=json_object.isNull(T.JSON_PARAM_STATUS_ID)?0:json_object.getLong(T.JSON_PARAM_STATUS_ID);
    obj.name=json_object.getString(T.JSON_PARAM_STATUS_NAME);
  }
  private void saveOrderAB(OrderAB obj,JSONObject json_object)throws JSONException{
    obj.id=json_object.isNull(T.JSON_PARAM_ORDER_ID)?-1:json_object.getLong(T.JSON_PARAM_ORDER_ID);
    obj.user_id=json_object.isNull(T.JSON_PARAM_USER_ID)?-1:json_object.getLong(T.JSON_PARAM_USER_ID);
    obj.status_id=json_object.isNull(T.JSON_PARAM_STATUS_ID)?0:json_object.getLong(T.JSON_PARAM_STATUS_ID);
    obj.status_name=json_object.getString(T.JSON_PARAM_STATUS_NAME);
    obj.transport_id=json_object.isNull(T.JSON_PARAM_TRANSPORT_ID)?-1:json_object.getLong(T.JSON_PARAM_TRANSPORT_ID);
    obj.total_price=json_object.isNull(T.JSON_PARAM_TOTAL_PRICE)?0.00:json_object.getDouble(T.JSON_PARAM_TOTAL_PRICE);
    obj.route_distance=json_object.isNull(T.JSON_PARAM_ROUTE_DISTANCE)?0:json_object.getInt(T.JSON_PARAM_ROUTE_DISTANCE);
    obj.route_duration=json_object.isNull(T.JSON_PARAM_ROUTE_DURATION)?0:json_object.getInt(T.JSON_PARAM_ROUTE_DURATION);
    obj.route_data=json_object.getString(T.JSON_PARAM_ROUTE_DATA);
    obj.order_lat=json_object.isNull(T.JSON_PARAM_ORDER_LAT)?0:json_object.getDouble(T.JSON_PARAM_ORDER_LAT);
    obj.order_lon=json_object.isNull(T.JSON_PARAM_ORDER_LON)?0:json_object.getDouble(T.JSON_PARAM_ORDER_LON);
    obj.order_address=json_object.getString(T.JSON_PARAM_ORDER_ADDRESS);
    obj.delivery_lat=json_object.isNull(T.JSON_PARAM_DELIVERY_LAT)?0:json_object.getDouble(T.JSON_PARAM_DELIVERY_LAT);
    obj.delivery_lon=json_object.isNull(T.JSON_PARAM_DELIVERY_LON)?0:json_object.getDouble(T.JSON_PARAM_DELIVERY_LON);
    obj.delivery_address=json_object.getString(T.JSON_PARAM_DELIVERY_ADDRESS);
    obj.delivery_type_id=json_object.isNull(T.JSON_PARAM_DELIVERY_TYPE_ID)?-1:json_object.getLong(T.JSON_PARAM_DELIVERY_TYPE_ID);
    obj.delivery_type_name=json_object.getString(T.JSON_PARAM_DELIVERY_TYPE_ID);
    obj.reserved_date=json_object.getString(T.JSON_PARAM_RESERVED_DATE);
    obj.reserved_hours=json_object.isNull(T.JSON_PARAM_RESERVED_HOURS)?0:json_object.getInt(T.JSON_PARAM_RESERVED_HOURS);
    obj.distance=json_object.has(T.JSON_PARAM_DISTANCE)?json_object.getInt(T.JSON_PARAM_DISTANCE):-1;
    obj.distance_measure=json_object.has(T.JSON_PARAM_DISTANCE_MEASURE)?json_object.getString(T.JSON_PARAM_DISTANCE_MEASURE):null;
    obj.create_date=json_object.getString(T.JSON_PARAM_CREATE_DATE);
    obj.last_update=json_object.getString(T.JSON_PARAM_LAST_UPDATE);

    //getUser
    JSONObject json_obj;
    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_USER);
    if(json_array.length()>0){
      json_obj=json_array.getJSONObject(0);//one of
      if(json_obj.length()>0){//not empty
        obj.user=new User();
        saveUser(obj.user,json_obj);
      }
    }
    //getTransport
    json_array=json_object.getJSONArray(T.JSON_PARAM_TRANSPORT);
    if(json_array.length()>0){
      json_obj=json_array.getJSONObject(0);//one of
      if(json_obj.length()>0){//not empty
        obj.transport=new Transport();
        saveTransport(obj.transport,json_obj);
      }
    }
    //getPurchase
    json_array=json_object.getJSONArray(T.JSON_PARAM_PURCHASE);
    if(json_array.length()>0){
      obj.purchaseList=new ArrayList();
      Purchase purchase=null;
      //not only one can be
      for(int i=0;i<json_array.length();i++){
        json_obj=json_array.getJSONObject(i);
        purchase=new Purchase();
        savePurchase(purchase,json_obj);
        obj.purchaseList.add(purchase);
      }
      if(purchase!=null)obj.purchase=purchase;//last purchase
    }
  }
  private void saveTransport(Transport obj,JSONObject json_object)throws JSONException{
    obj.id=json_object.isNull(T.JSON_PARAM_TRANSPORT_ID)?-1:json_object.getLong(T.JSON_PARAM_TRANSPORT_ID);
    obj.user_id=json_object.isNull(T.JSON_PARAM_USER_ID)?-1:json_object.getLong(T.JSON_PARAM_USER_ID);
    obj.sensor_id=json_object.isNull(T.JSON_PARAM_SENSOR_ID)?-1:json_object.getLong(T.JSON_PARAM_SENSOR_ID);
    obj.active=json_object.getBoolean(T.JSON_PARAM_SENSOR_ACTIVE);
    obj.name=json_object.getString(T.JSON_PARAM_TRANSPORT_NAME);
    obj.color=json_object.getString(T.JSON_PARAM_TRANSPORT_COLOR);
    obj.license_plate=json_object.getString(T.JSON_PARAM_LICENSE_PLATE);
    //now rate is number always
    obj.rate=json_object.isNull(T.JSON_PARAM_TRANSPORT_RATE)?0:json_object.getDouble(T.JSON_PARAM_TRANSPORT_RATE);
    if(json_object.has(T.JSON_PARAM_LAST_UPDATE))obj.last_update=json_object.getString(T.JSON_PARAM_LAST_UPDATE);

    //getSensor
    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_SENSOR);
    if(json_array.length()>0){
      JSONObject json_obj=json_array.getJSONObject(0);//one of
      if(json_obj.length()>0){//not empty
        obj.sensor=new Sensor();
        saveSensor(obj.sensor,json_obj);
      }
    }
    //getTransportTypePart
    json_array=json_object.getJSONArray(T.JSON_PARAM_TYPE_PART);
    if(json_array.length()>0){
      obj.typeList=new ArrayList();
      saveTypeList(obj.typeList,json_array);
    }
  }
  private void saveTransportReview(TransportReview obj,JSONObject json_object)throws JSONException{
    obj.order_id=json_object.isNull(T.JSON_PARAM_ORDER_ID)?-1:json_object.getLong(T.JSON_PARAM_ORDER_ID);
    obj.user_id=json_object.isNull(T.JSON_PARAM_USER_ID)?-1:json_object.getLong(T.JSON_PARAM_USER_ID);
    obj.transport_id=json_object.isNull(T.JSON_PARAM_TRANSPORT_ID)?-1:json_object.getLong(T.JSON_PARAM_TRANSPORT_ID);
    obj.description=json_object.getString(T.JSON_PARAM_DESCRIPTION);
    obj.review_value=(byte)json_object.getInt(T.JSON_PARAM_REVIEW_VALUE);
    obj.last_update=json_object.getString(T.JSON_PARAM_LAST_UPDATE);

    JSONObject json_obj;
    //getUser
    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_USER);
    if(json_array.length()>0){
      json_obj=json_array.getJSONObject(0);//one of
      if(json_obj.length()>0){//not empty
        obj.user=new User();
        saveUser(obj.user,json_obj);
      }
    }
    //getTransport
    json_array=json_object.getJSONArray(T.JSON_PARAM_TRANSPORT);
    if(json_array.length()>0){
      json_obj=json_array.getJSONObject(0);//one of
      if(json_obj.length()>0){//not empty
        obj.transport=new Transport();
        saveTransport(obj.transport,json_obj);
      }
    }
  }
  private void savePurchase(Purchase obj,JSONObject json_object)throws JSONException{
    obj.id=json_object.isNull(T.JSON_PARAM_PURCHASE_ID)?-1:json_object.getLong(T.JSON_PARAM_PURCHASE_ID);
    obj.user_id=json_object.isNull(T.JSON_PARAM_USER_ID)?-1:json_object.getLong(T.JSON_PARAM_USER_ID);
    obj.invoice_code=json_object.getString(T.JSON_PARAM_INVOICE_CODE);
    obj.invoice_date=json_object.getString(T.JSON_PARAM_INVOICE_DATE);
    obj.total_price=json_object.isNull(T.JSON_PARAM_TOTAL_PRICE)?0.00:json_object.getDouble(T.JSON_PARAM_TOTAL_PRICE);
    obj.total_tax=json_object.isNull(T.JSON_PARAM_TOTAL_TAX)?0.00:json_object.getDouble(T.JSON_PARAM_TOTAL_TAX);
    obj.order_id=json_object.isNull(T.JSON_PARAM_ORDER_ID)?-1:json_object.getLong(T.JSON_PARAM_ORDER_ID);
    obj.order_date=json_object.getString(T.JSON_PARAM_ORDER_DATE);
    obj.order_info=json_object.getString(T.JSON_PARAM_ORDER_INFO);
    obj.delivery_id=json_object.isNull(T.JSON_PARAM_DELIVERY_ID)?-1:json_object.getLong(T.JSON_PARAM_DELIVERY_ID);
    obj.delivery_code=json_object.getString(T.JSON_PARAM_DELIVERY_CODE);
    obj.delivery_type_id=json_object.isNull(T.JSON_PARAM_DELIVERY_TYPE_ID)?-1:json_object.getLong(T.JSON_PARAM_DELIVERY_TYPE_ID);
    obj.delivery_type_name=json_object.getString(T.JSON_PARAM_DELIVERY_TYPE_NAME);
    obj.delivery_date=json_object.getString(T.JSON_PARAM_DELIVERY_DATE);
    obj.delivery_price=json_object.isNull(T.JSON_PARAM_DELIVERY_PRICE)?0.00:json_object.getDouble(T.JSON_PARAM_DELIVERY_PRICE);
    obj.payment_date=json_object.getString(T.JSON_PARAM_PAYMENT_DATE);
    obj.payment_info=json_object.getString(T.JSON_PARAM_PAYMENT_INFO);
    obj.payment_amount=json_object.isNull(T.JSON_PARAM_PAYMENT_AMOUNT)?0.00:json_object.getDouble(T.JSON_PARAM_PAYMENT_AMOUNT);
    if(json_object.has(T.JSON_PARAM_CREATE_DATE))obj.create_date=json_object.getString(T.JSON_PARAM_CREATE_DATE);
    if(json_object.has(T.JSON_PARAM_LAST_UPDATE))obj.last_update=json_object.getString(T.JSON_PARAM_LAST_UPDATE);

    //getDeliveryTypeAttr
    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_DELIVERY_TYPE_ATTR);
    if(json_array.length()>0){
      obj.deliveryTypeAttrList=new ArrayList();
      saveAttrList(obj.deliveryTypeAttrList,json_array);
    }
  }

  private void saveSensor(Sensor obj,JSONObject json_object)throws JSONException{
    obj.id=json_object.isNull(T.JSON_PARAM_SENSOR_ID)?-1:json_object.getLong(T.JSON_PARAM_SENSOR_ID);
    obj.user_id=json_object.isNull(T.JSON_PARAM_USER_ID)?-1:json_object.getLong(T.JSON_PARAM_USER_ID);
    obj.name=json_object.getString(T.JSON_PARAM_SENSOR_NAME);
    obj.serial_number=json_object.getString(T.JSON_PARAM_SERIAL_NUMBER);
    obj.device_name=json_object.getString(T.JSON_PARAM_DEVICE_NAME);
    obj.phone=json_object.getString(T.JSON_PARAM_PHONE);
    obj.active=json_object.getBoolean(T.JSON_PARAM_ACTIVE);
    if(json_object.has(T.JSON_PARAM_CREATE_DATE))obj.create_date=json_object.getString(T.JSON_PARAM_CREATE_DATE);
    if(json_object.has(T.JSON_PARAM_LAST_UPDATE))obj.last_update=json_object.getString(T.JSON_PARAM_LAST_UPDATE);

    //getUser
    JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_USER);
    if(json_array.length()>0){
      JSONObject json_obj=json_array.getJSONObject(0);//one of
      if(json_obj.length()>0){//not empty
        obj.user=new User();
        saveUser(obj.user,json_obj);
      }
    }
    //getSensorCircle (now not check circles for using)
    /*JSONArray json_array=json_object.getJSONArray(T.JSON_PARAM_SENSOR_CIRCLE);
    if(json_array.length()>0){
      obj.sensorCircleList=new ArrayList();
      saveSensorCircleList(obj.sensorCircleList,json_array);
    }*/
  }
  private void saveTrack(Track obj,JSONObject json_object)throws JSONException{
    //json_object.isNull()?-1:json_object.getLong();
    obj.id=json_object.isNull(T.JSON_PARAM_TRACK_ID)?-1:json_object.getLong(T.JSON_PARAM_TRACK_ID);
    obj.user_id=json_object.isNull(T.JSON_PARAM_USER_ID)?-1:json_object.getLong(T.JSON_PARAM_USER_ID);
    obj.sensor_id=json_object.isNull(T.JSON_PARAM_SENSOR_ID)?-1:json_object.getLong(T.JSON_PARAM_SENSOR_ID);
    obj.transport_id=json_object.isNull(T.JSON_PARAM_TRANSPORT_ID)?-1:json_object.getLong(T.JSON_PARAM_TRANSPORT_ID);
    obj.type_id=json_object.isNull(T.JSON_PARAM_TYPE_ID)?-1:json_object.getLong(T.JSON_PARAM_TYPE_ID);
    obj.type_name=json_object.getString(T.JSON_PARAM_TYPE_NAME);
    obj.latitude=json_object.isNull(T.JSON_PARAM_LATITUDE)?0:json_object.getDouble(T.JSON_PARAM_LATITUDE);
    obj.longitude=json_object.isNull(T.JSON_PARAM_LONGITUDE)?0:json_object.getDouble(T.JSON_PARAM_LONGITUDE);
    obj.track_time=json_object.isNull(T.JSON_PARAM_TRACK_TIME)?0:json_object.getLong(T.JSON_PARAM_TRACK_TIME);
    obj.altitude=json_object.isNull(T.JSON_PARAM_ALTITUDE)?0:json_object.getDouble(T.JSON_PARAM_ALTITUDE);
    obj.accuracy=(float)(json_object.isNull(T.JSON_PARAM_ACCURACY)?0:json_object.getDouble(T.JSON_PARAM_ACCURACY));
    obj.bearing=(float)(json_object.isNull(T.JSON_PARAM_BEARING)?0:json_object.getDouble(T.JSON_PARAM_BEARING));
    obj.speed=json_object.isNull(T.JSON_PARAM_SPEED)?0:json_object.getDouble(T.JSON_PARAM_SPEED);
    obj.satellites=json_object.isNull(T.JSON_PARAM_SATELLITES)?0:json_object.getInt(T.JSON_PARAM_SATELLITES);
    obj.battery=json_object.isNull(T.JSON_PARAM_BATTERY)?0:json_object.getInt(T.JSON_PARAM_BATTERY);
    obj.timezone_offset=json_object.isNull(T.JSON_PARAM_TIMEZONE_OFFSET)?0:json_object.getInt(T.JSON_PARAM_TIMEZONE_OFFSET);
    obj.create_date=json_object.getString(T.JSON_PARAM_CREATE_DATE);

    //getSensor (saveSensor)
    //getTransport (saveTransport)
    //getTrackPart
  }
  private void saveStat(Stat obj,JSONObject json_object)throws JSONException{
    obj.audit=json_object.getBoolean(T.JSON_PARAM_AUDIT);
    obj.driver_count=json_object.getLong(T.JSON_PARAM_DRIVER_COUNT);
    obj.client_count=json_object.getLong(T.JSON_PARAM_CLIENT_COUNT);
    obj.driver_active_sensor_count=json_object.getLong(T.JSON_PARAM_DRIVER_ACTIVE_SENSOR_COUNT);
    obj.client_active_sensor_count=json_object.getLong(T.JSON_PARAM_CLIENT_ACTIVE_SENSOR_COUNT);
    obj.transport_count=json_object.getLong(T.JSON_PARAM_TRANSPORT_COUNT);
    obj.manufacture_count=json_object.getLong(T.JSON_PARAM_MANUFACTURE_COUNT);
    obj.product_count=json_object.getLong(T.JSON_PARAM_PRODUCT_COUNT);
    obj.store_count=json_object.getLong(T.JSON_PARAM_STORE_COUNT);
    obj.order_count=json_object.getLong(T.JSON_PARAM_ORDER_COUNT);
    obj.order_completed_count=json_object.getLong(T.JSON_PARAM_ORDER_COMPLETED_COUNT);
    obj.order_cancelled_count=json_object.getLong(T.JSON_PARAM_ORDER_CANCELLED_COUNT);
    obj.order_pending_count=json_object.getLong(T.JSON_PARAM_ORDER_PENDING_COUNT);
    obj.order_today_count=json_object.getLong(T.JSON_PARAM_ORDER_TODAY_COUNT);
    obj.order_yesterday_count=json_object.getLong(T.JSON_PARAM_ORDER_YESTERDAY_COUNT);
    obj.order_week_count=json_object.getLong(T.JSON_PARAM_ORDER_WEEK_COUNT);
    obj.order_month_count=json_object.getLong(T.JSON_PARAM_ORDER_MONTH_COUNT);
    obj.order_today_completed_count=json_object.getLong(T.JSON_PARAM_ORDER_TODAY_COMPLETED_COUNT);
    obj.order_today_cancelled_count=json_object.getLong(T.JSON_PARAM_ORDER_TODAY_CANCELLED_COUNT);
    obj.order_today_pending_count=json_object.getLong(T.JSON_PARAM_ORDER_TODAY_PENDING_COUNT);
    obj.order_yesterday_completed_count=json_object.getLong(T.JSON_PARAM_ORDER_YESTERDAY_COMPLETED_COUNT);
    obj.order_yesterday_cancelled_count=json_object.getLong(T.JSON_PARAM_ORDER_YESTERDAY_CANCELLED_COUNT);
    obj.order_yesterday_pending_count=json_object.getLong(T.JSON_PARAM_ORDER_YESTERDAY_PENDING_COUNT);
    obj.order_total_price=json_object.getDouble(T.JSON_PARAM_ORDE_TOTAL_PRICE);
    obj.purchase_payment_amount=json_object.getDouble(T.JSON_PARAM_PURCHASE_PAYMENT_AMOUNT);
    obj.payment_amount=json_object.getDouble(T.JSON_PARAM_PAYMENT_AMOUNT);
    obj.driver_order_count=json_object.getLong(T.JSON_PARAM_DRIVER_ORDER_COUNT);
    obj.driver_order_completed_count=json_object.getLong(T.JSON_PARAM_DRIVER_ORDER_COMPLETED_COUNT);
    obj.driver_order_pending_count=json_object.getLong(T.JSON_PARAM_DRIVER_ORDER_PENDING_COUNT);
    obj.driver_order_total_price=json_object.getDouble(T.JSON_PARAM_DRIVER_ORDER_TOTAL_PRICE);
    obj.driver_purchase_payment_amount=json_object.getDouble(T.JSON_PARAM_DRIVER_PURCHASE_PAYMENT_AMOUNT);
    obj.input_messages_count=json_object.getInt(T.JSON_PARAM_INPUT_MESSAGES_COUNT);
    obj.output_messages_count=json_object.getInt(T.JSON_PARAM_OUTPUT_MESSAGES_COUNT);
    obj.audit_count=json_object.getInt(T.JSON_PARAM_AUDIT_COUNT);
  }
}