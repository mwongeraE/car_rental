package autozvit.com.fireart.tools;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
//import android.support.v7.app.AlertDialog;//accent colors

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import autozvit.com.fireart.R;
import autozvit.com.fireart.objects.User;

public class T
{
  //const
  public static final String EMPTY="";
  public static final String SPACE=" ";
  public static final String POINT=".";
  public static final String COMA=",";
  public static final String POINT_COMA=";";
  public static final String OPEN_DOOR="[";
  public static final String CLOSE_DOOR="]";
  public static final String DOG="@";
  public static final String AND="&";
  public static final String QUEST="?";
  public static final String SLASH="/";
  public static final String MINUS="-";
  public static final String PLUS="+";
  public static final String EQUAL="=";
  public static final String UNDERSCORE="_";
  public static final String DOUBLE_POINT=":";
  public static final String NEXT_LINE="\r\n";
  public static final String UPPER="\'";
  public static final String DOUBLE_UPPER="\"";
  public static final String PERCENT="%";
  //local
  public static String LOCAL_DELIM=System.getProperty("file.separator");
  public static String FILE_EXT_JPG=".jpg";
  public static String FILE_TYPE_IMAGE="image/*";
  public static String LOCAL_PICTURE="picture";
  public static int SIZE_BUFFER_READ=32768;//32 Kb

  //usertype
  public static final int USERTYPE_ADMIN=User.USER_TYPE_ADMIN;
  public static final int USERTYPE_DRIVER=User.USER_TYPE_WORKER;
  public static final int USERTYPE_CLIENT=User.USER_TYPE_CUSTOMER;
  public static final String USERTYPE_ADMIN_TITLE="admin";
  public static final String USERTYPE_DRIVER_TITLE="driver";
  public static final String USERTYPE_CLIENT_TITLE="client";
  //image object
  public static final String IMAGE_USER="user";
  public static final String IMAGE_PRODUCT="product";
  public static final String IMAGE_PRODUCT_TYPE="product_type";
  public static final String IMAGE_PRODUCT_PARAM="product_param";
  public static final String IMAGE_TRANSPORT="transport";
  public static final String IMAGE_TRACK_PART="track_part";

  //filename
  public static String FILENAME_LOG="fireArt.log";
  public static String FILENAME_PREFERENCES="preferences";//for settings
  public static String FILENAME_MESSAGES="messages";//for chat
  public static String FILENAME_CART_LIST="cart_list";//list of product
  public static String FILENAME_CURRENT_USER="current_user";
  public static String FILENAME_DRIVER_TRANSPORT="driver_transport";
  public static String FILENAME_ACTIVE_CURRENCY="active_currency";
  public static String FILENAME_CURRENT_ORDER_AB="current_order_AB";
  public static String FILENAME_CURRENT_TRACK="current_track";

  //settings
  public static String SETTINGS_COUNT_LOADING="CountLoading";
  public static String SETTINGS_LANGUAGE="Language";
  public static String SETTINGS_MAP_PROVIDER="MapProvider";
  public static String SETTINGS_PAYMENT_PROVIDER="PaymentProvider";
  public static String SETTINGS_SERVER_NAME="ServerName";
  public static String SETTINGS_REGISTERED="Registered";
  public static String SETTINGS_SHOW_LOGO="ShowLogo";
  public static String SETTINGS_TRIP_MODE="TripMode";
  public static String SETTINGS_TOKEN="Token";

  public static String SETTINGS_USERNAME="Username";
  public static String SETTINGS_USERTYPE="Usertype";
  public static String SETTINGS_CALLNAME="Callname";
  public static String SETTINGS_FIRSTNAME="Firstname";
  public static String SETTINGS_LASTNAME="Lastname";
  public static String SETTINGS_EMAIL="Email";
  public static String SETTINGS_PHONE="Phone";
  public static String SETTINGS_PASSWORD="Password";

  public static String SETTINGS_USER_ID="UserId";
  public static String SETTINGS_SENSOR_ID="SensorId";
  public static String SETTINGS_CIRCLE_SENSOR_ID="CircleSensorId";
  public static String SETTINGS_CIRCLE_USER_ID="CircleUserId";
  public static String SETTINGS_TRACK_ID="TrackId";
  public static String SETTINGS_ORDER_ID="OrderId";
  public static String SETTINGS_ORDER_STATUS_ID="OrderStatusId";
  public static String SETTINGS_ORDER_STATUS_NAME="OrderStatusName";
  public static String SETTINGS_ORDER_STATUS_VALUE="OrderStatusValue";
  public static String SETTINGS_ORDER_TIME="OrderTime";
  public static String SETTINGS_TRIP_ORDER_ID="TripOrderId";
  public static String SETTINGS_TRIP_ORDER_USER_ID="TripOrderUserId";
  public static String SETTINGS_TRIP_ORDER_STATUS_ID="TripOrderStatusId";
  public static String SETTINGS_TRIP_ORDER_STATUS_NAME="TripOrderStatusName";
  public static String SETTINGS_TRIP_ORDER_STATUS_VALUE="TripOrderStatusValue";
  public static String SETTINGS_TRIP_ORDER_TIME="TripOrderTime";
  public static String SETTINGS_LANDING_DURATION="LandingDuration";
  public static String SETTINGS_TRIP_DURATION="TripDuration";
  public static String SETTINGS_LANDING_TIME="LandingTime";
  public static String SETTINGS_PICKUP_TIME="PickupTime";
  public static String SETTINGS_TRIP_DISTANCE="TripDistance";
  public static String SETTINGS_DO_NOT_SHOW="DoNotShow";//for Tip
  public static String SETTINGS_LATITUDE="Latitude";//localLocation
  public static String SETTINGS_LONGITUDE="Longitude";//localLocation
  public static String SETTINGS_CLIENT_LATITUDE="ClientLatitude";//clientLat
  public static String SETTINGS_CLIENT_LONGITUDE="ClientLongitude";//clientLng
  public static String SETTINGS_DRIVER_LATITUDE="DriverLatitude";//driverLat
  public static String SETTINGS_DRIVER_LONGITUDE="DriverLongitude";//driverLng

  public static String SETTINGS_ORDER_LATITUDE="OrderLatitude";//markerA
  public static String SETTINGS_ORDER_LONGITUDE="OrderLongitude";//markerA
  public static String SETTINGS_FIND_ADDRESS="FindAddress";//find address for delivery
  public static String SETTINGS_DELIVERY_ADDRESS="DeliveryAddress";//for delivery bookingStyle
  public static String SETTINGS_DELIVERY_LATITUDE="DeliveryLatitude";//markerB
  public static String SETTINGS_DELIVERY_LONGITUDE="DeliveryLongitude";//markerB
  public static String SETTINGS_PICKUP_ADDRESS="PickupAddress";//geocode address by location
  public static String SETTINGS_PICKUP_NAME="PickupName";//short name of address
  public static String SETTINGS_DROPOFF_ADDRESS="DropoffAddress";//geocode address by location
  public static String SETTINGS_DROPOFF_NAME="DropoffName";//short name of address
  public static String SETTINGS_ORIGIN_LATITUDE="OriginLatitude";//googleFrom
  public static String SETTINGS_ORIGIN_LONGITUDE="OriginLongitude";//googleFrom
  public static String SETTINGS_DESTINATION_LATITUDE="DestinationLatitude";//googleTo
  public static String SETTINGS_DESTINATION_LONGITUDE="DestinationLongitude";//googleTo
  public static String SETTINGS_ORIGIN="Origin";//originFrom (google)
  public static String SETTINGS_DESTINATION="Destination";//destTo (google)

  public static String SETTINGS_TRANSPORT_ID="TransportId";
  public static String SETTINGS_TRANSPORT_NAME="TransportName";
  public static String SETTINGS_TRANSPORT_COLOR="TransportColor";
  public static String SETTINGS_TRANSPORT_LICENSE_PLATE="TransportLicensePlate";
  public static String SETTINGS_TRANSPORT_RATE="TransportRate";
  public static String SETTINGS_TRANSPORT_SENSOR_ID="TransportSensorId";
  public static String SETTINGS_TRANSPORT_SENSOR_NAME="TransportSensorName";
  public static String SETTINGS_TRANSPORT_SENSOR_PHONE="TransportSensorPhone";
  public static String SETTINGS_TRANSPORT_SENSOR_USER_ID="TransportSensorUserId";
  public static String SETTINGS_TRANSPORT_SENSOR_USER_CALLNAME="TransportSensorUserCallname";
  public static String SETTINGS_TRANSPORT_SENSOR_USER_PHONE="TransportSensorUserPhone";

  public static String SETTINGS_DRIVER_TRANSPORT_ID="DriverTransportId";
  public static String SETTINGS_DRIVER_TRANSPORT_NAME="DriverTransportName";
  public static String SETTINGS_DRIVER_TRANSPORT_COLOR="DriverTransportColor";
  public static String SETTINGS_DRIVER_TRANSPORT_LICENSE_PLATE="DriverTransportLicensePlate";
  public static String SETTINGS_DRIVER_TRANSPORT_RATE="DriverTransportRate";

  public static String SETTINGS_BOOKING_X="BookingX";
  public static String SETTINGS_BOOKING_Y="BookingY";
  public static String SETTINGS_DRIVER_X="DriverX";
  public static String SETTINGS_DRIVER_Y="DriverY";
  public static String SETTINGS_CART_X="CartX";
  public static String SETTINGS_CART_Y="CartY";
  public static String SETTINGS_MENU_X="MenuX";
  public static String SETTINGS_MENU_Y="MenuY";

  //language
  public static final int SETTINGS_LANGUAGE_UNKNOWN=0;
  public static final int SETTINGS_LANGUAGE_ENGLISH=1;
  public static final int SETTINGS_LANGUAGE_RUSSIAN=2;
  public static final int SETTINGS_LANGUAGE_UKRAINIAN=3;
  public static final int DEFAULT_LANGUAGE=SETTINGS_LANGUAGE_ENGLISH;

  public static final String SETTINGS_LANGUAGE_NAME_ENGLISH="ENGLISH";
  public static final String SETTINGS_LANGUAGE_NAME_RUSSIAN="RUSSIAN";
  public static final String SETTINGS_LANGUAGE_NAME_UKRAINIAN="UKRAINIAN";
  public static final String DEFAULT_NAME_LANGUAGE=SETTINGS_LANGUAGE_NAME_ENGLISH;

  //gps
  public static final int GPS_PROVIDER_TIMEOUT_DEFAULT_VALUE=30;//seconds
  public static final int GPS_PROVIDER_METERS_DEFAULT_VALUE=50;//meters

  //map
  public static final int MAP_DEFAULT_ZOOM=15;
  public static final int MAP_DEFAULT_ZOOM_FOR_OSM=17;
  public static final double MAP_DEFAULT_LAT_DELIM=0.0005;
  public static final double MAP_DEFAULT_LNG_DELIM=0.001;
  public static final double MAP_MECCA_LAT=21.422510;//Mecca
  public static final double MAP_MECCA_LNG=39.826168;
  public static final double MAP_KIEV_LAT=50.42416949;//Kiev
  public static final double MAP_KIEV_LNG=30.47355571;
  public static final double MAP_ODESSA_LAT=46.4854787;//Odessa
  public static final double MAP_ODESSA_LNG=30.7389907;
  public static final double MAP_LONDON_LAT=51.537182;//London
  public static final double MAP_LONDON_LNG=-0.13112375;
  public static final double MAP_DUBAI_LAT=25.065688;//Dubai
  public static final double MAP_DUBAI_LNG=55.245635;

  public static final double MAP_DEFAULT_LAT=MAP_DUBAI_LAT;
  public static final double MAP_DEFAULT_LNG=MAP_DUBAI_LNG;
  public static final int MAP_DEFAULT_ANIMATE_CAMERA_DURATION=1000;
  public static final int MAP_DEFAULT_CIRCLE_RADIUS=200;
  public static final int MAP_DEFAULT_CIRCLE_STROKE=2;
  public static final int MAP_DEFAULT_POLYGON_STROKE=2;
  public static final int MAP_DEFAULT_CIRCLE_COLOR=0x409E9E9E;//grey_500
  public static final int MAP_DEFAULT_CIRCLE_STROKE_COLOR=0x609E9E9E;//grey_500
  public static final int MAP_ACCEPTED_CIRCLE_COLOR=0x40616161;//grey_700
  public static final int MAP_PROCESSING_CIRCLE_COLOR=0x404DB6AC;//green_300
  public static final int MAP_LANDING_CIRCLE_COLOR=0x40388E3C;//green_700
  public static final int MAP_TRIP_CIRCLE_COLOR=0x404DB6AC;//green_300
  public static final int MAP_CANCEL_CIRCLE_COLOR=0x40C2185B;//pink_700

  public static final int MAP_DEFAULT_POLYGON_COLOR=0x409E9E9E;//grey_500
  public static final int MAP_DEFAULT_POLYGON_COLOR_2=0x809E9E9E;//grey_500
  public static final int MAP_DEFAULT_POLYGON_STROKE_COLOR=0x209E9E9E;//grey_500
  public static final int MAP_DEFAULT_POLYLINE_STROKE=10;
  public static final int MAP_DEFAULT_POLYLINE_STROKE_COLOR=0x809E9E9E;//grey_500
  public static final int MAP_DEFAULT_PADDING=200;
  public static final int MAP_CLIENT_PADDING=500;

  public static final String MAP_DISTANCE_MILE="mile";
  public static final String MAP_DISTANCE_KM="km";

  //activities
  public static final int ACTIVITY_START=1;
  public static final int ACTIVITY_SPLASH=2;
  public static final int ACTIVITY_SETTINGS=3;
  public static final int ACTIVITY_MESSAGES=4;
  public static final int ACTIVITY_DRIVER=5;//profile client&driver
  public static final int ACTIVITY_CORE_PHOTO=11;
  public static final int ACTIVITY_PHOTO_ALBUM=12;

  //fragments
  public static final int FRAGMENT_UNKNOWN=0;
  public static final int FRAGMENT_ERROR=-1;
  public static final int FRAGMENT_SUCCESS=-2;
  public static final int FRAGMENT_INFO=-3;
  public static final int FRAGMENT_IMAGE=1;
  //public static final int FRAGMENT_GPS_WARNING=2;
  public static final int FRAGMENT_LIST=10;
  public static final int FRAGMENT_ORDER_LIST=11;
  public static final int FRAGMENT_PRODUCT_LIST=12;
  public static final int FRAGMENT_CART_LIST=13;
  public static final int FRAGMENT_MESSAGE_LIST=14;

  public static final int FRAGMENT_MAP=10000;//google map(be or not to be? What the question...)
  public static final int FRAGMENT_STAT=99;
  public static final int FRAGMENT_START=100;
  public static final int FRAGMENT_REGISTER=101;
  public static final int FRAGMENT_SIGNIN=102;
  public static final int FRAGMENT_SIGNUP=103;
  public static final int FRAGMENT_TIP=201;
  public static final int FRAGMENT_TRIP=202;
  public static final int FRAGMENT_FIND=203;
  public static final int FRAGMENT_ORDER_STATUS=204;
  public static final int FRAGMENT_TAXIMETER=205;
  public static final int FRAGMENT_CART=206;
  public static final int FRAGMENT_PAYMENT=207;
  public static final int FRAGMENT_PICKUP=208;
  public static final int FRAGMENT_DROPOFF=209;
  public static final int FRAGMENT_RIDE=210;
  public static final int FRAGMENT_INVOICE=211;
  public static final int FRAGMENT_INQUIRY=212;
  public static final int FRAGMENT_PRODUCT=213;

  //fragments_name
  public static String FRAGMENT_NAME_UNKNOWN="unknown";
  public static String FRAGMENT_NAME_ERROR="error";
  public static String FRAGMENT_NAME_SUCCESS="success";
  public static String FRAGMENT_NAME_INFO="info";
  public static String FRAGMENT_NAME_IMAGE="image";
  //public static String FRAGMENT_NAME_GPS_WARNING="gps_warning";
  public static String FRAGMENT_NAME_LIST="list";
  public static String FRAGMENT_NAME_ORDER_LIST="order_list";
  public static String FRAGMENT_NAME_PRODUCT_LIST="product_list";
  public static String FRAGMENT_NAME_CART_LIST="cart_list";
  public static String FRAGMENT_NAME_MESSAGE_LIST="message_list";

  public static String FRAGMENT_NAME_MAP="google_map";//google map
  public static String FRAGMENT_NAME_STAT="stat";
  public static String FRAGMENT_NAME_START="start";
  public static String FRAGMENT_NAME_REGISTER="register";
  public static String FRAGMENT_NAME_SIGNIN="signin";
  public static String FRAGMENT_NAME_SIGNUP="signup";
  public static String FRAGMENT_NAME_TIP="tip";
  public static String FRAGMENT_NAME_TRIP="trip";
  public static String FRAGMENT_NAME_FIND="find";
  public static String FRAGMENT_NAME_ORDER_STATUS="order_status";
  public static String FRAGMENT_NAME_TAXIMETER="taximeter";
  public static String FRAGMENT_NAME_CART="cart";
  public static String FRAGMENT_NAME_PAYMENT="payment";
  public static String FRAGMENT_NAME_PICKUP="pickup";
  public static String FRAGMENT_NAME_DROPOFF="dropoff";
  public static String FRAGMENT_NAME_RIDE="ride";
  public static String FRAGMENT_NAME_INVOICE="invoice";
  public static String FRAGMENT_NAME_INQUIRY="inquiry";
  public static String FRAGMENT_NAME_PRODUCT="product";
  //sleep timeout
  public static final int TASK_SLEEP_TIMEOUT=1000;
  public static final int TASK_SHORT_SLEEP_TIMEOUT=100;
  public static final int TASK_SHOW_1_COUNT=1;
  public static final int TASK_SHOW_3_COUNT=3;
  public static final int TASK_SHOW_15_COUNT=15;
  public static final int TASK_VIBRATE_TIMEOUT=100;
  public static final int TASK_VIBRATE_SHORT_TIMEOUT=10;
  public static final int TASK_VIBRATE_1_COUNT=1;
  public static final int TASK_VIBRATE_3_COUNT=3;
  public static final int TASK_VIBRATE_15_COUNT=15;
  public static final int TASK_SLEEP_COUNT=3;
  public static final int TASK_DANCING_SLEEP_COUNT=5;
  public static final int TASK_SERVICE_SLEEP_COUNT=30;//0,5 min
  public static final int TASK_UNLIMITED_COUNT=1000000;
  public static final int TASK_RELOAD_TIMEOUT=1;//1 milisec

  public static final long MILISEC_IN_DAY=24*3600*1000;
  //json
  public static String JSON_VALUE_SUCCESS="SUCCESS";
  public static String JSON_VALUE_ERROR="ERROR";
  public static String JSON_PARAM_RESULTS="results";
  public static String JSON_PARAM_STATUS="status";
  public static String JSON_PARAM_SESSION_ID="session_id";
  public static String JSON_PARAM_MESSAGE="message";
  public static String JSON_PARAM_DATABASE_MESSAGE="database_message";
  public static String JSON_PARAM_MESSAGE_CODE="message_code";
  public static String JSON_PARAM_VALUE="value";
  public static String JSON_PARAM_NAME="name";
  public static String JSON_PARAM_OFFSET="offset";
  public static String JSON_PARAM_ROWS="rows";
  public static String JSON_PARAM_TOKEN="token";
  public static String JSON_PARAM_APIKEY="api_key";

  public static String JSON_PARAM_LANGUAGE="language";
  public static String JSON_PARAM_ACTIVITY="activity";
  public static String JSON_PARAM_START_DATE="start_date";
  public static String JSON_PARAM_FINISH_DATE="finish_date";
  public static String JSON_PARAM_CREATE_DATE="create_date";
  public static String JSON_PARAM_LAST_UPDATE="last_update";
  public static String JSON_PARAM_DISTANCE="distance";
  public static String JSON_PARAM_DISTANCE_MEASURE="distance_measure";
  public static String JSON_PARAM_REVIEW_VALUE="review_value";

  public static String JSON_PARAM_ATTR_PART_ID="attr_part_id";
  public static String JSON_PARAM_ATTR_NAME="attr_name";
  public static String JSON_PARAM_ATTR_VALUE="attr_value";

  public static String JSON_PARAM_ATTR_NAME_VALUE_TITLE="title";
  public static String JSON_PARAM_ATTR_NAME_VALUE_ABOUT="about";

  public static String JSON_PARAM_TYPE_ID="type_id";
  public static String JSON_PARAM_TYPE_PARENT_ID="type_parent_id";
  public static String JSON_PARAM_TYPE_NAME="type_name";
  public static String JSON_PARAM_TYPE_ATTR="type_attr";

  public static String JSON_PARAM_TOKEN_ID="token_id";
  public static String JSON_PARAM_AMOUNT="amount";
  public static String JSON_PARAM_PARAM_PARENT_ID="param_parent_id";

  public static String JSON_PARAM_TAX_ID="tax_id";
  public static String JSON_PARAM_TAX_CODE="tax_code";
  public static String JSON_PARAM_TAX_NAME="tax_name";
  public static String JSON_PARAM_TAX_VALUE="tax_value";
  public static String JSON_PARAM_TAX_ATTR="tax_attr";

  public static String JSON_PARAM_PICTURE="picture";
  public static String JSON_PARAM_PICTURE_ID="picture_id";
  public static String JSON_PARAM_OBJECT_NAME="object_name";
  public static String JSON_PARAM_USERNAME="user";
  public static String JSON_PARAM_PASSWORD="pass";
  public static String JSON_PARAM_NEW_USERNAME="new_user";
  public static String JSON_PARAM_NEW_PASSWORD="new_pass";
  public static String JSON_PARAM_USERTYPE="user_type";
  public static String JSON_PARAM_FIRSTNAME="first_name";
  public static String JSON_PARAM_LASTNAME="last_name";
  public static String JSON_PARAM_CALLNAME="call_name";
  public static String JSON_PARAM_PHONE="phone";
  public static String JSON_PARAM_EMAIL="email";
  public static String JSON_PARAM_ACTIVE="active";
  public static String JSON_PARAM_PARAM_ATTR="param_attr";
  public static String JSON_PARAM_PARAM_PART="param_part";
  public static String JSON_PARAM_TYPE_PART="type_part";

  public static String JSON_PARAM_USER_ID="user_id";
  public static String JSON_PARAM_USER_TYPE="user_type";
  public static String JSON_PARAM_USER_RATE="user_rate";
  public static String JSON_PARAM_USER="user";
  public static String JSON_PARAM_REVIEW_USER_ID="review_user_id";
  public static String JSON_PARAM_USER_USERNAME="username";
  public static String JSON_PARAM_CURRENCY_ID="currency_id";
  public static String JSON_PARAM_PRODUCT_ID="product_id";
  public static String JSON_PARAM_PRODUCT="product";
  public static String JSON_PARAM_MANUFACTURE_ID="manufacture_id";
  public static String JSON_PARAM_CURRENCY="currency";
  public static String JSON_PARAM_CURRENCY_NAME="currency_name";
  public static String JSON_PARAM_CURRENCY_VALUE="currency_value";
  public static String JSON_PARAM_CURRENCY_ATTR="currency_attr";
  public static String JSON_PARAM_PRODUCT_NAME="product_name";
  public static String JSON_PARAM_DESCRIPTION="description";
  public static String JSON_PARAM_PRODUCT_CODE="product_code";
  public static String JSON_PARAM_PRODUCT_PRICE="product_price";
  public static String JSON_PARAM_PRODUCT_RATE="product_rate";
  public static String JSON_PARAM_PRODUCT_ATTR="product_attr";
  public static String JSON_PARAM_STOCK_COUNT="stock_count";

  public static String JSON_PARAM_PRODUCT_PARAM_ID="param_id";
  public static String JSON_PARAM_PRODUCT_PARAM_NAME="param_name";
  public static String JSON_PARAM_PRODUCT_PARAM_VALUE="param_value";
  public static String JSON_PARAM_PRODUCT_PARAM_PART_PRICE="param_part_price";
  public static String JSON_PARAM_PRODUCT_PARAM_PART_COUNT="param_part_count";
  public static String JSON_PARAM_PRODUCT_PARAM_PART_VALUE="param_part_value";
  public static String JSON_PARAM_PRODUCT_COUNT="product_count";

  public static String JSON_PARAM_DISCOUNT="discount";
  public static String JSON_PARAM_DISCOUNT_ID="discount_id";
  public static String JSON_PARAM_PRODUCT_TYPE_ID="product_type_id";
  public static String JSON_PARAM_DISCOUNT_TYPE="discount_type";
  public static String JSON_PARAM_DISCOUNT_CODE="discount_code";
  public static String JSON_PARAM_DISCOUNT_NAME="discount_name";
  public static String JSON_PARAM_DISCOUNT_VALUE="discount_value";
  public static String JSON_PARAM_DISCOUNT_ATTR="discount_attr";

  public static String JSON_PARAM_TRANSPORT_ID="transport_id";
  public static String JSON_PARAM_ORDER_STATUS_ID="order_status_id";
  public static String JSON_PARAM_TOTAL_PRICE="total_price";
  public static String JSON_PARAM_TOTAL_TAX="total_tax";
  public static String JSON_PARAM_ROUTE_DISTANCE="route_distance";
  public static String JSON_PARAM_ROUTE_DURATION="route_duration";
  public static String JSON_PARAM_ROUTE_DATA="route_data";
  public static String JSON_PARAM_ORDER_LAT="order_lat";
  public static String JSON_PARAM_ORDER_LON="order_lon";
  public static String JSON_PARAM_ORDER_ADDRESS="order_address";
  public static String JSON_PARAM_DELIVERY_LAT="delivery_lat";
  public static String JSON_PARAM_DELIVERY_LON="delivery_lon";
  public static String JSON_PARAM_DELIVERY_ADDRESS="delivery_address";
  public static String JSON_PARAM_DELIVERY_TYPE_ID="delivery_type_id";
  public static String JSON_PARAM_RESERVED_DATE="reserved_date";
  public static String JSON_PARAM_RESERVED_HOURS="reserved_hours";

  public static String JSON_PARAM_SENSOR_ID="sensor_id";
  public static String JSON_PARAM_SENSOR_A_ID="sensorA_id";
  public static String JSON_PARAM_SENSOR_B_ID="sensorB_id";
  public static String JSON_PARAM_SENSOR_NAME="sensor_name";
  public static String JSON_PARAM_SERIAL_NUMBER="serial_number";
  public static String JSON_PARAM_DEVICE_NAME="device_name";
  public static String JSON_PARAM_SENSOR_ACTIVE="sensor_active";
  public static String JSON_PARAM_SENSOR="sensor";
  public static String JSON_PARAM_SENSOR_CIRCLE="sensor_circle";

  public static String JSON_PARAM_TRACK_ID="track_id";

  public static String JSON_PARAM_MESSAGE_ID="message_id";
  public static String JSON_PARAM_FROM_MESSAGE_ID="from_message_id";
  public static String JSON_PARAM_MESSAGE_TYPE="message_type";
  public static String JSON_PARAM_USERA_ID="userA_id";
  public static String JSON_PARAM_USERA="userA";
  public static String JSON_PARAM_USERB_ID="userB_id";
  public static String JSON_PARAM_USERB="userB";
  public static String JSON_PARAM_RECIPIENT="recipient";

  public static String JSON_PARAM_LATITUDE="latitude";
  public static String JSON_PARAM_LONGITUDE="longitude";
  public static String JSON_PARAM_TRACK_TIME="track_time";
  public static String JSON_PARAM_ALTITUDE="altitude";
  public static String JSON_PARAM_ACCURACY="accuracy";
  public static String JSON_PARAM_BEARING="bearing";
  public static String JSON_PARAM_SPEED="speed";
  public static String JSON_PARAM_SATELLITES="satellites";
  public static String JSON_PARAM_BATTERY="battery";
  public static String JSON_PARAM_TIMEZONE_OFFSET="timezone_offset";
  public static String JSON_PARAM_GEOCODE_ADDRESS="geocode_address";
  public static String JSON_PARAM_FORMATTED_ADDRESS="formatted_address";

  public static String JSON_PARAM_ORDER_ID="order_id";
  public static String JSON_PARAM_STATUS_ID="status_id";
  public static String JSON_PARAM_STATUS_NAME="status_name";
  public static String JSON_PARAM_STATUS_ATTR="status_attr";

  public static String JSON_PARAM_PURCHASE_ID="purchase_id";
  public static String JSON_PARAM_PURCHASE="purchase";

  public static String JSON_PARAM_TRANSPORT_NAME="transport_name";
  public static String JSON_PARAM_TRANSPORT_COLOR="transport_color";
  public static String JSON_PARAM_LICENSE_PLATE="license_plate";
  public static String JSON_PARAM_TRANSPORT_RATE="transport_rate";
  public static String JSON_PARAM_TRANSPORT="transport";

  public static String JSON_PARAM_INVOICE_CODE="invoice_code";
  public static String JSON_PARAM_INVOICE_DATE="invoice_date";
  public static String JSON_PARAM_ORDER_DATE="order_date";
  public static String JSON_PARAM_ORDER_INFO="order_info";

  public static String JSON_PARAM_DELIVERY_ID="delivery_id";
  public static String JSON_PARAM_DELIVERY_CODE="delivery_code";
  public static String JSON_PARAM_DELIVERY_TYPE_NAME="delivery_type_name";
  public static String JSON_PARAM_DELIVERY_TYPE_ATTR="delivery_type_attr";
  public static String JSON_PARAM_DELIVERY_DATE="delivery_date";
  public static String JSON_PARAM_DELIVERY_PRICE="delivery_price";
  public static String JSON_PARAM_PAYMENT_DATE="payment_date";
  public static String JSON_PARAM_PAYMENT_INFO="payment_info";
  public static String JSON_PARAM_PAYMENT_AMOUNT="payment_amount";
  public static String JSON_PARAM_PAYMENT_CURRENCY="payment_currency";
  public static String JSON_PARAM_PAYMENT_STATUS="payment_status";
  public static String JSON_PARAM_TRANSACTION_ID="transaction_id";

  public static String JSON_PARAM_PREPAID_CODE="prepaid_code";
  public static String JSON_PARAM_PREPAID_AMOUNT="prepaid_amount";

  public static String JSON_PARAM_AUDIT="audit";
  public static String JSON_PARAM_DRIVER_COUNT="driver_count";
  public static String JSON_PARAM_CLIENT_COUNT="client_count";
  public static String JSON_PARAM_DRIVER_ACTIVE_SENSOR_COUNT="driver_active_sensor_count";
  public static String JSON_PARAM_CLIENT_ACTIVE_SENSOR_COUNT="client_active_sensor_count";
  public static String JSON_PARAM_TRANSPORT_COUNT="transport_count";
  public static String JSON_PARAM_MANUFACTURE_COUNT="manufacture_count";
  //public static String JSON_PARAM_PRODUCT_COUNT="product_count";
  public static String JSON_PARAM_STORE_COUNT="store_count";
  public static String JSON_PARAM_ORDER_COUNT="order_count";
  public static String JSON_PARAM_ORDER_COMPLETED_COUNT="order_completed_count";
  public static String JSON_PARAM_ORDER_CANCELLED_COUNT="order_cancelled_count";
  public static String JSON_PARAM_ORDER_PENDING_COUNT="order_pending_count";
  public static String JSON_PARAM_ORDER_TODAY_COUNT="order_today_count";
  public static String JSON_PARAM_ORDER_YESTERDAY_COUNT="order_yesterday_count";
  public static String JSON_PARAM_ORDER_WEEK_COUNT="order_week_count";
  public static String JSON_PARAM_ORDER_MONTH_COUNT="order_month_count";
  public static String JSON_PARAM_ORDER_TODAY_COMPLETED_COUNT="order_today_completed_count";
  public static String JSON_PARAM_ORDER_TODAY_CANCELLED_COUNT="order_today_cancelled_count";
  public static String JSON_PARAM_ORDER_TODAY_PENDING_COUNT="order_today_pending_count";
  public static String JSON_PARAM_ORDER_YESTERDAY_COMPLETED_COUNT="order_yesterday_completed_count";
  public static String JSON_PARAM_ORDER_YESTERDAY_CANCELLED_COUNT="order_yesterday_cancelled_count";
  public static String JSON_PARAM_ORDER_YESTERDAY_PENDING_COUNT="order_yesterday_pending_count";
  public static String JSON_PARAM_ORDE_TOTAL_PRICE="order_total_price";
  public static String JSON_PARAM_PURCHASE_PAYMENT_AMOUNT="purchase_payment_amount";
  //public static String JSON_PARAM_PAYMENT_AMOUNT="payment_amount";
  public static String JSON_PARAM_DRIVER_ORDER_COUNT="driver_order_count";
  public static String JSON_PARAM_DRIVER_ORDER_COMPLETED_COUNT="driver_order_completed_count";
  public static String JSON_PARAM_DRIVER_ORDER_PENDING_COUNT="driver_order_pending_count";
  public static String JSON_PARAM_DRIVER_ORDER_TOTAL_PRICE="driver_order_total_price";
  public static String JSON_PARAM_DRIVER_PURCHASE_PAYMENT_AMOUNT="driver_purchase_payment_amount";
  public static String JSON_PARAM_INPUT_MESSAGES_COUNT="input_messages_count";
  public static String JSON_PARAM_OUTPUT_MESSAGES_COUNT="output_messages_count";
  public static String JSON_PARAM_AUDIT_COUNT="audit_count";

  //tokens and api keys
  //response object: {"cognalys_app_id":"put_app_id_number_here","cognalys_access_token":"put_access_token_here","google_maps_key":"put_key_here","paypal_key":"put_key_here"}
  public static String JSON_PARAM_COGNALYS_APP_ID="cognalys_app_id";
  public static String JSON_PARAM_COGNALYS_ACCESS_TOKEN="cognalys_access_token";
  public static String JSON_PARAM_GOOGLE_MAPS_KEY="google_maps_key";
  public static String JSON_PARAM_PAYPAL_KEY="paypal_key";
  public static String JSON_PARAM_STRIPE_KEY="stripe_key";
  /////////////////////////////////////////////////////////////////////////////////////////

  /*google*/
  public static String JSON_GOOGLE_KEY="key";
  public static String JSON_GOOGLE_ORIGIN="origin";
  public static String JSON_GOOGLE_DESTINATION="destination";
  public static String JSON_GOOGLE_REGION="region";
  public static String JSON_GOOGLE_LANGUAGE="language";
  public static String JSON_GOOGLE_ADDRESS="address";
  public static String JSON_GOOGLE_LATLNG="latlng";

  public static String JSON_GOOGLE_PARAM_STATUS="status";
  public static String JSON_GOOGLE_PARAM_RESULTS="results";
  public static String JSON_GOOGLE_PARAM_ROUTES="routes";
  public static String JSON_GOOGLE_PARAM_LEGS="legs";
  public static String JSON_GOOGLE_PARAM_STEPS="steps";
  public static String JSON_GOOGLE_PARAM_DURATION="duration";
  public static String JSON_GOOGLE_PARAM_DISTANCE="distance";
  public static String JSON_GOOGLE_PARAM_LAT="lat";
  public static String JSON_GOOGLE_PARAM_LNG="lng";
  public static String JSON_GOOGLE_PARAM_START_LOCATION="start_location";
  public static String JSON_GOOGLE_PARAM_END_LOCATION="end_location";
  public static String JSON_GOOGLE_PARAM_START_ADDRESS="start_address";
  public static String JSON_GOOGLE_PARAM_END_ADDRESS="end_address";
  public static String JSON_GOOGLE_PARAM_OVERVIEW_POLYLINE="overview_polyline";
  public static String JSON_GOOGLE_PARAM_HTML_INSTRUCTIONS="html_instructions";
  public static String JSON_GOOGLE_PARAM_FORMATTED_ADDRESS="formatted_address";
  public static String JSON_GOOGLE_PARAM_GEOMETRY="geometry";
  public static String JSON_GOOGLE_PARAM_BOUNDS="bounds";
  public static String JSON_GOOGLE_PARAM_NORTHEAST="northeast";
  public static String JSON_GOOGLE_PARAM_SOUTHWEST="southwest";
  public static String JSON_GOOGLE_PARAM_LOCATION="location";
  public static String JSON_GOOGLE_PARAM_ADDRESS_COMPONENTS="address_components";
  public static String JSON_GOOGLE_PARAM_LONG_NAME="long_name";

  public static String JSON_GOOGLE_PARAM_VALUE="value";
  public static String JSON_GOOGLE_PARAM_TEXT="text";

  public static String JSON_GOOGLE_VALUE_OK="OK";
  public static String JSON_GOOGLE_VALUE_ZERO_RESULTS="ZERO_RESULTS";
  public static String JSON_GOOGLE_VALUE_REQUEST_DENIED="REQUEST_DENIED";
  public static String JSON_GOOGLE_VALUE_SUCCESS="success";
  public static String JSON_GOOGLE_VALUE_ERROR="error";

  /*cognalys*/
/*
101 = "MISSING CREDENTIALS";
102 = "MISSING REQUIRED VALUES";
103 = "MISSING PROPER NUMBER";
104 = "VERIFICATION SUCCESS";
105 = "NUMBER IS NOT CORRECT";
106 = "MOBILE NUMBER VERIFICATION CANCELED";
107 = "NETWORK ERROR CANNOT BE VERIFIED";
108 = "MOBILE NUMBER VERIFICATION FAILED, NO INTERNET";
*/
  public static final int COGNALYS_RESULT_CODE_MISSING_CREDENTIALS=101;
  public static final int COGNALYS_RESULT_CODE_MISSING_REQUIRED_VALUES=102;
  public static final int COGNALYS_RESULT_CODE_MISSING_PROPER_NUMBER=103;
  public static final int COGNALYS_RESULT_CODE_VERIFICATION_SUCCESS=104;
  public static final int COGNALYS_RESULT_CODE_NUMBER_IS_NOT_CORRECT=105;
  public static final int COGNALYS_RESULT_CODE_MOBILE_NUMBER_VERIFICATION_CANCELED=106;
  public static final int COGNALYS_RESULT_CODE_NETWORK_ERROR_CANNOT_BE_VERIFIED=107;
  public static final int COGNALYS_RESULT_CODE_MOBILE_NUMBER_VERIFICATION_FAILED_NO_INTERNET=108;

  //fragment
  public static class fragment{
    public String name;
    public Integer type;
    public Fragment fragment;
  }
  public static T.fragment addFragment(int view_id,FragmentTransaction transaction,Fragment fragment,String name,Integer type,ArrayList<Integer> type_list,ArrayList<Fragment> list){
    T.fragment ret_val=new T.fragment();
    if(fragment!=null){
      transaction.add(view_id,fragment,name);
      transaction.commit();
      type_list.add(type);
      list.add(fragment);
      ret_val.fragment=fragment;
      ret_val.name=name;
      ret_val.type=type;
    }
    return ret_val;
  }
  public static T.fragment removeFragment(FragmentTransaction transaction,Fragment fragment,ArrayList<Integer> type_list,ArrayList<Fragment> list){
    T.fragment ret_val=new T.fragment();
    int index=list.lastIndexOf(fragment),ind;
    if(index!=-1){type_list.remove(index);type_list.trimToSize();}
    ind=type_list.size()-1;
    if(ind>=0)ret_val.type=type_list.get(ind);
    else ret_val.type=FRAGMENT_UNKNOWN;
    if(index!=-1){list.remove(index);list.trimToSize();}
    ind=list.size()-1;
    if(ind>=0)ret_val.fragment=list.get(ind);
    else ret_val.fragment=null;
    if(ret_val.fragment!=null)ret_val.name=ret_val.fragment.getTag();
    transaction.remove(fragment);
    transaction.commit();
    return ret_val;
  }

  //color
  public static int getColor(String name){
    int color=0;
    try{
      Field field=Class.forName("android.graphics.Color").getField(name);
      color=(int)field.get(null);
    }catch(Exception e){}
    return color;
  }

  //message
  public static AlertDialog messageBox(Context context,String title,String message,String button1){
    AlertDialog.Builder dialog=new AlertDialog.Builder(context);
    dialog.setTitle(title);
    dialog.setMessage(message);
    dialog.setPositiveButton(button1,null);
    dialog.setCancelable(true);
    return dialog.show();
  }

  //dialog
  public static AlertDialog dialogBox(Context context,String title,String message,String button1,String button2,DialogInterface.OnClickListener listener1,DialogInterface.OnClickListener listener2){
    AlertDialog.Builder dialog=new AlertDialog.Builder(context);
    dialog.setTitle(title);
    dialog.setMessage(message);
    dialog.setPositiveButton(button1,listener1);
    dialog.setNegativeButton(button2,listener2);
    dialog.setCancelable(true);
    return dialog.show();
  }
  public static Dialog dialogContentBox(Context context,int content,int button_content,View.OnClickListener listener){
    Dialog dialog=new Dialog(context);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(true);
    dialog.setContentView(content);
    if(button_content>=0){
      Button button=(Button)dialog.findViewById(button_content);
      if(button!=null)button.setOnClickListener(listener);
    }
    dialog.show();
    return dialog;
  }
  public static AlertDialog dialogListBox(Context context,String title,String button1,String button2,
                                     DialogInterface.OnClickListener listener1,DialogInterface.OnClickListener listener2,
                                     CharSequence[] items,boolean[] checked_items,DialogInterface.OnMultiChoiceClickListener listener){
    AlertDialog.Builder dialog;
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
      dialog=new AlertDialog.Builder(context,R.style.AlertDialogCustom);
    else
      dialog=new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.AlertDialogCustom));
    dialog.setTitle(title);
    dialog.setPositiveButton(button1,listener1);
    dialog.setNegativeButton(button2,listener2);
    dialog.setMultiChoiceItems(items,checked_items,listener);
    dialog.setCancelable(true);
    return dialog.show();
  }

  //drawable
  public static final Drawable getDrawable(Context context,int id){
    final int version=Build.VERSION.SDK_INT;
    if(version>=21){
      return ContextCompat.getDrawable(context,id);}
    else{
      return context.getResources().getDrawable(id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////

  /*Haversine distance formula, see http://en.wikipedia.org/wiki/Haversine_formula*/
  public static Double getDistanceBetweenTwoMarkers(Double latitude1,Double longitude1,Double latitude2,Double longitude2) {
    final int RADIUS_EARTH = 6371;//or 6378137 Earth's mean radius in meters

    double dLat = getRad(latitude2 - latitude1);
    double dLong = getRad(longitude2 - longitude1);

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(getRad(latitude1)) * Math.cos(getRad(latitude2)) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return (RADIUS_EARTH * c) * 1000;
  }

  private static Double getRad(Double x) {
    return x * Math.PI / 180;
  }

  //replace quot
  public static String replace(String str){
    return str.replace("&quot;",DOUBLE_UPPER).replace("&#39;",UPPER);
  }

  /*language*/
  public static int getDefaultLanguageCode(Context context){
    int ret_val=DEFAULT_LANGUAGE;
    String lang=context.getResources().getConfiguration().locale.getLanguage();
    if(lang==null)Locale.getDefault().getLanguage();
    if(lang!=null){
      if(lang.equals(new Locale("ru").getLanguage()))ret_val=SETTINGS_LANGUAGE_RUSSIAN;
      else if(lang.equals(new Locale("uk").getLanguage()))ret_val=SETTINGS_LANGUAGE_UKRAINIAN;
      else if(lang.equals(new Locale("en").getLanguage()))ret_val=SETTINGS_LANGUAGE_ENGLISH;
    }
    return ret_val;
  }
  public static String getDefaultLanguageName(Context context){
    String ret_val=DEFAULT_NAME_LANGUAGE;
    String lang=context.getResources().getConfiguration().locale.getLanguage();
    if(lang==null)Locale.getDefault().getLanguage();
    if(lang!=null){
      if(lang.equals(new Locale("ru").getLanguage()))ret_val=SETTINGS_LANGUAGE_NAME_RUSSIAN;
      else if(lang.equals(new Locale("uk").getLanguage()))ret_val=SETTINGS_LANGUAGE_NAME_UKRAINIAN;
      else if(lang.equals(new Locale("en").getLanguage()))ret_val=SETTINGS_LANGUAGE_NAME_ENGLISH;
    }
    return ret_val;
  }

  public static String timeZone() {// +03:30
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
    String   timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
    return timeZone.substring(0, 3) + ":"+ timeZone.substring(3, 5);
  }
  public static String getCurrentTimezoneOffset() {
    TimeZone tz = TimeZone.getDefault();
    Calendar cal = GregorianCalendar.getInstance(tz);
    int offsetInMillis = tz.getOffset(cal.getTimeInMillis());

    String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
    offset = (offsetInMillis >= 0 ? "+" : "-") + offset;

    return offset;
  }
  public static int getTimezoneOffset(){
    TimeZone tz=TimeZone.getDefault();
    Calendar cal=GregorianCalendar.getInstance(tz);
    int offsetInMillis=tz.getOffset(cal.getTimeInMillis());
    return offsetInMillis/60000;//in minutes
  }
  public static int getDateNumber(String str){//"01"->1 "12"->12
    int ret_val;
    if(str.length()>1&&str.startsWith("0"))ret_val=Integer.parseInt(str.substring(1));
    else ret_val=Integer.parseInt(str);
    return ret_val;
  }
}