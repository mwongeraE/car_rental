package autozvit.com.fireart.forms;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import autozvit.com.fireart.R;
import autozvit.com.fireart.objects.OrderAB;
import autozvit.com.fireart.objects.OrderABStatus;
import autozvit.com.fireart.tools.Manager;

public class TaximeterFragment extends Fragment{
  private static final String ARG_LAYOUT="layout";
  private static final String ARG_DROPOFF_PICTURE="dropoff_picture";
  private static final String ARG_DROPOFF_NAME="dropoff_name";
  private static final String ARG_DROPOFF_ADDRESS="dropoff_address";
  private static final String ARG_USER_ID="user_id";//userId
  private static final String ARG_STATUS_ID="status_id";//statusId
  private static final String ARG_LANDING_DURATION="landing_duration";
  private static final String ARG_TRIP_DURATION="trip_duration";
  private static final String ARG_LANDING_TIME="landing_time";//landing
  private static final String ARG_PICKUP_TIME="pickup_time";//pickup
  private static final String ARG_PICTURE_URL="picture_url";//picture url
  private static final String ARG_ORDER_ID="order_id";//order_id
  private static final String ARG_DISTANCE="distance";//distance
  private static final String ARG_PRICE="price";//price
  private static final String ARG_STATUS="status";//status
  private static final String ARG_CURRENCY="currency";//currency
  private static final String ARG_ADDRESS="address";//address_B (orderAB.delivery_address) (not used in current version)
  private static final String ARG_CHRONOMETERS_FORMAT="chronometers_format";//chronometers format
  private static final String ARG_TRIP_DISTANCE="trip_distance";//tripDistance
  private static final String ARG_USER_RATE="user_rate";//userRate
  private static final String ARG_USER_NAME="user_name";//userName

  private int layout;
  private int dropoffPicture;
  private String dropoffName;
  private String dropoffAddress;
  private long userId,statusId,landingDuration,tripDuration,landingTime,pickupTime;
  private String order_id;
  private String distance;
  private String price;
  private String status;
  private String currency;
  private String address;
  private String chronometersFormat;
  private String tripDistance;
  private String pictureUrl;
  private float userRate;
  private String userName;

  private Context context;
  private Manager manager;

  private ImageView imageViewDropoffPicture;
  private TextView textViewDropoffName;
  private TextView textViewDropoffAddress;
  private ImageView imageViewUserPicture;
  private TextView textViewOrderId;
  private TextView textViewDistance;
  private TextView textViewPrice;
  private TextView textViewOrderStatus;
  private TextView textViewCurrency;
  private Chronometer chronometerLandingDuration,chronometerTripDuration;
  private TextView textViewTripDistance;
  private RatingBar ratingBarUserRate;
  private TextView textViewUserName;

  public static TaximeterFragment newInstance(int layout,int dropoff_picture,String dropoff_name,String dropoff_address,long user_id,long status_id,long landing_duration,long trip_duration,long landing_time,long pickup_time,String picture_url,String order_id,String distance,String price,String status,String currency,String address,String chronometers_format,String trip_distance,float user_rate,String user_name){
    TaximeterFragment fragment=new TaximeterFragment();
    Bundle args=new Bundle();
    args.putInt(ARG_LAYOUT,layout);
    args.putInt(ARG_DROPOFF_PICTURE,dropoff_picture);
    args.putString(ARG_DROPOFF_NAME,dropoff_name);
    args.putString(ARG_DROPOFF_ADDRESS,dropoff_address);
    args.putLong(ARG_USER_ID,user_id);
    args.putLong(ARG_STATUS_ID,status_id);
    args.putLong(ARG_LANDING_DURATION,landing_duration);
    args.putLong(ARG_TRIP_DURATION,trip_duration);
    args.putLong(ARG_LANDING_TIME,landing_time);
    args.putLong(ARG_PICKUP_TIME,pickup_time);
    args.putString(ARG_PICTURE_URL,picture_url);
    args.putString(ARG_ORDER_ID,order_id);
    args.putString(ARG_DISTANCE,distance);
    args.putString(ARG_PRICE,price);
    args.putString(ARG_STATUS,status);
    args.putString(ARG_CURRENCY,currency);
    args.putString(ARG_ADDRESS,address);
    args.putString(ARG_CHRONOMETERS_FORMAT,chronometers_format);
    args.putString(ARG_TRIP_DISTANCE,trip_distance);
    args.putFloat(ARG_USER_RATE,user_rate);
    args.putString(ARG_USER_NAME,user_name);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      layout=getArguments().getInt(ARG_LAYOUT);
      dropoffPicture=getArguments().getInt(ARG_DROPOFF_PICTURE);
      dropoffName=getArguments().getString(ARG_DROPOFF_NAME);
      dropoffAddress=getArguments().getString(ARG_DROPOFF_ADDRESS);
      userId=getArguments().getLong(ARG_USER_ID);
      statusId=getArguments().getLong(ARG_STATUS_ID);
      landingDuration=getArguments().getLong(ARG_LANDING_DURATION);
      tripDuration=getArguments().getLong(ARG_TRIP_DURATION);
      landingTime=getArguments().getLong(ARG_LANDING_TIME);
      pickupTime=getArguments().getLong(ARG_PICKUP_TIME);
      pictureUrl=getArguments().getString(ARG_PICTURE_URL);
      order_id=getArguments().getString(ARG_ORDER_ID);
      distance=getArguments().getString(ARG_DISTANCE);
      price=getArguments().getString(ARG_PRICE);
      status=getArguments().getString(ARG_STATUS);
      currency=getArguments().getString(ARG_CURRENCY);
      address=getArguments().getString(ARG_ADDRESS);
      chronometersFormat=getArguments().getString(ARG_CHRONOMETERS_FORMAT);
      tripDistance=getArguments().getString(ARG_TRIP_DISTANCE);
      userRate=getArguments().getFloat(ARG_USER_RATE);
      userName=getArguments().getString(ARG_USER_NAME);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(layout,container,false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    context=getActivity();
    manager=((StartActivity)context).getManager();
    View view=getView();
    if(view!=null){
      final Fragment fragment=this;

      imageViewDropoffPicture=(ImageView)view.findViewById(R.id.image_view_dropoff_picture);
      if(imageViewDropoffPicture!=null&&dropoffPicture!=-1)imageViewDropoffPicture.setImageResource(dropoffPicture);
      textViewDropoffName=(TextView)view.findViewById(R.id.text_view_dropoff_name);
      if(textViewDropoffName!=null/*&&dropoffName!=null*/)textViewDropoffName.setText(dropoffName);
      textViewDropoffAddress=(TextView)view.findViewById(R.id.text_view_dropoff_address);
      if(textViewDropoffAddress!=null/*&&dropoffAddress!=null*/)textViewDropoffAddress.setText(dropoffAddress);

      textViewOrderId=(TextView)view.findViewById(R.id.text_view_order_id);
      if(textViewOrderId!=null&&order_id!=null)textViewOrderId.setText(order_id);
      textViewDistance=(TextView)view.findViewById(R.id.text_view_distance);
      if(textViewDistance!=null&&distance!=null)textViewDistance.setText(distance);
      textViewPrice=(TextView)view.findViewById(R.id.text_view_price);
      if(textViewPrice!=null&&price!=null)textViewPrice.setText(price);
      textViewOrderStatus=(TextView)view.findViewById(R.id.text_view_order_status);
      if(textViewOrderStatus!=null&&status!=null) textViewOrderStatus.setText(status.toUpperCase());
      updateStatusColor();
      textViewCurrency=(TextView)view.findViewById(R.id.text_view_currency);
      if(textViewCurrency!=null&&currency!=null)textViewCurrency.setText(currency);
      textViewUserName=(TextView)view.findViewById(R.id.text_view_user_name);
      if(textViewUserName!=null/*&&userName!=null*/) textViewUserName.setText(userName);
      ratingBarUserRate=(RatingBar)view.findViewById(R.id.rating_bar_user_rate);
      if(ratingBarUserRate!=null&&userRate>0)ratingBarUserRate.setRating(userRate);

      chronometerLandingDuration=(Chronometer)view.findViewById(R.id.chronometer_landing_duration);
      if(chronometerLandingDuration!=null){
        chronometerLandingDuration.setBase(SystemClock.elapsedRealtime());
        if(chronometersFormat!=null)chronometerLandingDuration.setFormat(chronometersFormat);
        if(landingTime!=0){
          long elapsed_time=SystemClock.elapsedRealtime(),landing_elapsed_time=System.currentTimeMillis()-landingTime;
          if(statusId==OrderABStatus.ORDER_STATUS_LANDING)chronometerLandingDuration.setBase(elapsed_time-landing_elapsed_time);
          else chronometerLandingDuration.setBase(elapsed_time-landingDuration);
        }
        else chronometerLandingDuration.setBase(SystemClock.elapsedRealtime());
        //start chronometer
        if(statusId==OrderABStatus.ORDER_STATUS_LANDING)chronometerLandingDuration.start();
      }

      chronometerTripDuration=(Chronometer)view.findViewById(R.id.chronometer_trip_duration);
      if(chronometerTripDuration!=null){
        if(chronometersFormat!=null)chronometerTripDuration.setFormat(chronometersFormat);
        if(pickupTime!=0){
          long elapsed_time=SystemClock.elapsedRealtime(),pickup_elapsed_time=System.currentTimeMillis()-pickupTime;
          if(statusId==OrderABStatus.ORDER_STATUS_DELIVERING)chronometerTripDuration.setBase(elapsed_time-pickup_elapsed_time);
          else chronometerTripDuration.setBase(elapsed_time-tripDuration);
        }
        else chronometerTripDuration.setBase(SystemClock.elapsedRealtime());
        //start chronometer
        if(statusId>OrderABStatus.ORDER_STATUS_LANDING&&statusId<OrderABStatus.ORDER_STATUS_COMPLETED)
          chronometerTripDuration.start();
      }

      textViewTripDistance=(TextView)view.findViewById(R.id.text_view_trip_distance);
      if(textViewTripDistance!=null&&tripDistance!=null)textViewTripDistance.setText(tripDistance);

      /* all buttons not using onClick */
      Button button_landing=(Button)view.findViewById(R.id.button_landing);//landing
      if(button_landing!=null){
        button_landing.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v){
            if(manager.getCurrentOrderAB()!=null)
              manager.updateOrderStatusDialog(manager.getCurrentOrderAB().id,OrderABStatus.ORDER_STATUS_LANDING,fragment);
          }
        });
      }
      Button button_delivering=(Button)view.findViewById(R.id.button_delivering);//delivering
      if(button_delivering!=null){
        button_delivering.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v){
            if(manager.getCurrentOrderAB()!=null)
              manager.updateOrderStatusDialog(manager.getCurrentOrderAB().id,OrderABStatus.ORDER_STATUS_DELIVERING,fragment);
          }
        });
      }
      Button button_completed=(Button)view.findViewById(R.id.button_completed);//completed
      if(button_completed!=null){
        button_completed.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v){
            if(manager.getCurrentOrderAB()!=null)
              manager.updateOrderStatusDialog(manager.getCurrentOrderAB().id,OrderABStatus.ORDER_STATUS_COMPLETED,fragment);
          }
        });
      }
      Button button_payment=(Button)view.findViewById(R.id.button_payment);//payment
      if(button_payment!=null){
        button_payment.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v){
            if(manager.getCurrentOrderAB()!=null&&manager.getCurrentOrderAB().status_id==OrderABStatus.ORDER_STATUS_COMPLETED)
              manager.makeTripPaymentDialog();
          }
        });
      }
      //job fragment
      Button button_order_status=(Button)view.findViewById(R.id.button_order_status);//order_status
      if(button_order_status!=null){
        if(manager.isTripMode()){
          OrderAB order=manager.getCurrentOrderAB();
          String button_name=null;
          if(order.status_id==OrderABStatus.ORDER_STATUS_PROCESSED){
            button_name=context.getString(R.string.button_landing);
          }
          else if(order.status_id==OrderABStatus.ORDER_STATUS_LANDING){
            button_name=context.getString(R.string.button_delivering);
          }
          else if(order.status_id==OrderABStatus.ORDER_STATUS_DELIVERING){
            button_name=context.getString(R.string.button_completed);
          }
          if(button_name!=null)button_order_status.setText(button_name);
          else button_order_status.setVisibility(View.INVISIBLE);
          button_order_status.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              if(manager.getCurrentOrderAB()!=null&&manager.getCurrentOrderAB().status_id<OrderABStatus.ORDER_STATUS_COMPLETED&&
                                                    manager.getCurrentOrderAB().status_id>0)
                manager.updateOrderStatusDialog(manager.getCurrentOrderAB().id,manager.getCurrentOrderAB().status_id+1,fragment);
            }
          });
        }
      }

      //picasso picture loading ...
      imageViewUserPicture=(ImageView)view.findViewById(R.id.image_view_user_picture);
      if(userId!=-1&&pictureUrl!=null&&imageViewUserPicture!=null){
        String url=pictureUrl+userId;
        manager.loadImage(url,imageViewUserPicture,null);
      }
    }
  }

  public void updateLandingDurationFormat(String duration_format){
    View view=getView();
    if(view!=null){
      chronometerLandingDuration=(Chronometer)view.findViewById(R.id.chronometer_landing_duration);
      if(chronometerLandingDuration!=null)chronometerLandingDuration.setFormat(duration_format);
    }
  }
  public void restartLandingDuration(){
    View view=getView();
    if(view!=null){
      chronometerLandingDuration=(Chronometer)view.findViewById(R.id.chronometer_landing_duration);
      if(chronometerLandingDuration!=null){
        chronometerLandingDuration.setBase(SystemClock.elapsedRealtime());
        chronometerLandingDuration.start();
      }
    }
  }
  public void stopLandingDuration(){
    View view=getView();
    if(view!=null){
      chronometerLandingDuration=(Chronometer)view.findViewById(R.id.chronometer_landing_duration);
      if(chronometerLandingDuration!=null)chronometerLandingDuration.stop();
    }
  }
  public void updateTripDurationFormat(String duration_format){
    View view=getView();
    if(view!=null){
      chronometerTripDuration=(Chronometer)view.findViewById(R.id.chronometer_trip_duration);
      if(chronometerTripDuration!=null)chronometerTripDuration.setFormat(duration_format);
    }
  }
  public void restartTripDuration(){
    View view=getView();
    if(view!=null){
      chronometerTripDuration=(Chronometer)view.findViewById(R.id.chronometer_trip_duration);
      if(chronometerTripDuration!=null){
        chronometerTripDuration.setBase(SystemClock.elapsedRealtime());
        chronometerTripDuration.start();
      }
    }
  }
  public void stopTripDuration(){
    View view=getView();
    if(view!=null){
      chronometerTripDuration=(Chronometer)view.findViewById(R.id.chronometer_trip_duration);
      if(chronometerTripDuration!=null)chronometerTripDuration.stop();
    }
  }
  public void updateTripDistance(String distance){
    View view=getView();
    if(view!=null){
      textViewTripDistance=(TextView)view.findViewById(R.id.text_view_trip_distance);
      if(textViewTripDistance!=null)textViewTripDistance.setText(distance);
    }
  }
  public void updateOrderStatus(long status_id,String status){
    View view=getView();
    if(view!=null){
      textViewOrderStatus=(TextView)view.findViewById(R.id.text_view_order_status);
      if(textViewOrderStatus!=null)textViewOrderStatus.setText(status.toUpperCase());
      this.statusId=status_id;
      updateStatusColor();
    }
  }
  public void updateTripPrice(String price){
    View view=getView();
    if(view!=null){
      this.price=price;
      textViewPrice=(TextView)view.findViewById(R.id.text_view_price);
      if(textViewPrice!=null&&this.price!=null)textViewPrice.setText(this.price);
    }
  }
  public void updateOrderStatusButtonName(String name){
    View view=getView();
    if(view!=null){
      Button button_order_status=(Button)view.findViewById(R.id.button_order_status);//order_status
      if(button_order_status!=null)button_order_status.setText(name);
    }
  }
  public void updateOrderStatusButtonEnabled(boolean enabled){
    View view=getView();
    if(view!=null){
      Button button_order_status=(Button)view.findViewById(R.id.button_order_status);//order_status
      if(button_order_status!=null)button_order_status.setEnabled(enabled);
    }
  }

  private void updateStatusColor(){
    View view=getView();
    if(view!=null){
      textViewOrderStatus=(TextView)view.findViewById(R.id.text_view_order_status);
      if(textViewOrderStatus!=null){
        if(statusId<0){
          textViewOrderStatus.setBackgroundColor(context.getResources().getColor(R.color.label_canceled));
        }
        else if(statusId>1&&statusId<OrderABStatus.ORDER_STATUS_DELIVERING){
          textViewOrderStatus.setBackgroundColor(context.getResources().getColor(R.color.label_processed));
        }
        else if(statusId>=OrderABStatus.ORDER_STATUS_DELIVERING){
          textViewOrderStatus.setBackgroundColor(context.getResources().getColor(R.color.label_completed));
        }
        else textViewOrderStatus.setBackgroundColor(context.getResources().getColor(R.color.label_accepted));
      }
    }
  }
}