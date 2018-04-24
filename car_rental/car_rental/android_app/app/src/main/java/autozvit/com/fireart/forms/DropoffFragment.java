package autozvit.com.fireart.forms;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calendarbuilderexample.CalendarDialogBuilder;

import java.util.Date;

import autozvit.com.fireart.R;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.tools.T;

public class DropoffFragment extends Fragment implements CalendarDialogBuilder.OnDateSetListener{
  private static final String ARG_NAME="name";
  private static final String ARG_PICKUP_PICTURE="pickup_picture";
  private static final String ARG_PICKUP_NAME="pickup_name";
  private static final String ARG_PICKUP_ADDRESS="pickup_address";
  private static final String ARG_DROPOFF_PICTURE="dropoff_picture";
  private static final String ARG_DROPOFF_NAME="dropoff_name";
  private static final String ARG_DROPOFF_ADDRESS="dropoff_address";
  private static final String ARG_DURATION="duration";
  private static final String ARG_DISTANCE="distance";
  private static final String ARG_PRICE="price";
  private static final String ARG_DISCOUNT="discount";
  private static final String ARG_CURRENCY="currency";
  private static final String ARG_ORDER_TIME="order_time";

  private String name;
  private int pickupPicture;
  private String pickupName;
  private String pickupAddress;
  private int dropoffPicture;
  private String dropoffName;
  private String dropoffAddress;
  private String duration;
  private String distance;
  private String price;
  private String discount;
  private String currency;
  private String orderTime;

  Manager manager;
  private Context context;
  private View view;

  private ImageView imageViewPickupPicture;
  private TextView textViewPickupName;
  private TextView textViewPickupAddress;
  private ImageView imageViewDropoffPicture;
  private TextView textViewDropoffName;
  private TextView textViewDropoffAddress;
  private TextView textViewDurationValue;
  private TextView textViewDistanceValue;
  private TextView textViewPrice;
  private TextView textViewDiscount;
  private TextView textViewCurrency;
  private EditText editTextOrderTime;
  private TextView textViewOrderTime;

  public ImageButton buttonMinus=null;
  public ImageButton buttonPlus=null;
  public EditText editTextCount=null;

  public static DropoffFragment newInstance(String name,int pickup_picture,String pickup_name,String pickup_address,int dropoff_picture,String dropoff_name,String dropoff_address,String duration,String distance,String price,String discount,String currency,String order_time){
    DropoffFragment fragment=new DropoffFragment();
    Bundle args=new Bundle();
    args.putString(ARG_NAME,name);
    args.putInt(ARG_PICKUP_PICTURE,pickup_picture);
    args.putString(ARG_PICKUP_ADDRESS,pickup_address);
    args.putString(ARG_PICKUP_NAME,pickup_name);
    args.putInt(ARG_DROPOFF_PICTURE,dropoff_picture);
    args.putString(ARG_DROPOFF_NAME,dropoff_name);
    args.putString(ARG_DROPOFF_ADDRESS,dropoff_address);
    args.putString(ARG_DURATION,duration);
    args.putString(ARG_DISTANCE,distance);
    args.putString(ARG_PRICE,price);
    args.putString(ARG_DISCOUNT,discount);
    args.putString(ARG_CURRENCY,currency);
    args.putString(ARG_ORDER_TIME,order_time);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      name=getArguments().getString(ARG_NAME);
      pickupPicture=getArguments().getInt(ARG_PICKUP_PICTURE);
      pickupName=getArguments().getString(ARG_PICKUP_NAME);
      pickupAddress=getArguments().getString(ARG_PICKUP_ADDRESS);
      dropoffPicture=getArguments().getInt(ARG_DROPOFF_PICTURE);
      dropoffName=getArguments().getString(ARG_DROPOFF_NAME);
      dropoffAddress=getArguments().getString(ARG_DROPOFF_ADDRESS);
      duration=getArguments().getString(ARG_DURATION);
      distance=getArguments().getString(ARG_DISTANCE);
      price=getArguments().getString(ARG_PRICE);
      discount=getArguments().getString(ARG_DISCOUNT);
      currency=getArguments().getString(ARG_CURRENCY);
      orderTime=getArguments().getString(ARG_ORDER_TIME);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(Manager.CAR_RENTAL?R.layout.fragment_dropoff_rental:R.layout.fragment_dropoff,container,false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    context=getActivity();
    view=getView();//save view for many tools functions
    if(view!=null){

      manager=((StartActivity)context).getManager();

      imageViewPickupPicture=(ImageView)view.findViewById(R.id.image_view_pickup_picture);
      if(imageViewPickupPicture!=null&&pickupPicture!=-1)imageViewPickupPicture.setImageResource(pickupPicture);
      textViewPickupName=(TextView)view.findViewById(R.id.text_view_pickup_name);
      if(textViewPickupName!=null/*&&pickupName!=null*/)textViewPickupName.setText(pickupName);
      textViewPickupAddress=(TextView)view.findViewById(R.id.text_view_pickup_address);
      if(textViewPickupAddress!=null/*&&pickupAddress!=null*/)textViewPickupAddress.setText(pickupAddress);
      imageViewDropoffPicture=(ImageView)view.findViewById(R.id.image_view_dropoff_picture);
      if(imageViewDropoffPicture!=null&&dropoffPicture!=-1)imageViewDropoffPicture.setImageResource(dropoffPicture);
      textViewDropoffName=(TextView)view.findViewById(R.id.text_view_dropoff_name);
      if(textViewDropoffName!=null/*&&dropoffName!=null*/)textViewDropoffName.setText(dropoffName);
      textViewDropoffAddress=(TextView)view.findViewById(R.id.text_view_dropoff_address);
      if(textViewDropoffAddress!=null/*&&dropoffAddress!=null*/)textViewDropoffAddress.setText(dropoffAddress);

      textViewDurationValue=(TextView)view.findViewById(R.id.text_view_duration_value);
      if(textViewDurationValue!=null&&duration!=null)textViewDurationValue.setText(duration);
      textViewDistanceValue=(TextView)view.findViewById(R.id.text_view_distance_value);
      if(textViewDistanceValue!=null&&distance!=null)textViewDistanceValue.setText(distance);
      textViewPrice=(TextView)view.findViewById(R.id.text_view_price);
      if(textViewPrice!=null&&price!=null)textViewPrice.setText(price);
      textViewDiscount=(TextView)view.findViewById(R.id.text_view_discount);
      if(textViewDiscount!=null&&discount!=null)textViewDiscount.setText(discount);
      textViewCurrency=(TextView)view.findViewById(R.id.text_view_currency);
      if(textViewCurrency!=null&&currency!=null)textViewCurrency.setText(currency);

      editTextOrderTime=(EditText)view.findViewById(R.id.edit_text_order_time);
      if(editTextOrderTime!=null){
        if(orderTime!=null)editTextOrderTime.setText(orderTime);
        else{
          Date curr_date=new Date();
          editTextOrderTime.setText(String.format(Manager.HOURS_MINUTES,curr_date.getHours()+2,0));
        }
      }

      textViewOrderTime=(TextView)view.findViewById(R.id.text_view_order_time);

      boolean is_ride_later=((StartActivity)context).getManager().isRideLater();

      if(Manager.CAR_RENTAL){

        if(textViewOrderTime!=null){
          Date curr_date=new Date();
          String talk_date=manager.getTalkingDateMessage(manager.getMySQLDatetime(curr_date.getTime()));
          textViewOrderTime.setText(talk_date);
          manager.setRentalReserved(curr_date.getTime());
        }

        buttonMinus=(ImageButton)view.findViewById(R.id.image_button_minus);
        buttonPlus=(ImageButton)view.findViewById(R.id.image_button_plus);
        editTextCount=(EditText)view.findViewById(R.id.edit_text_count);

        if(manager.getRentalTimeDuration()==0)manager.setRentalTimeDuration(1);//always set to one (for nice looks like)

        if(editTextCount!=null)
          editTextCount.setText(String.valueOf(manager.getRentalTimeDuration()));

        if(buttonMinus!=null){
          buttonMinus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
              if(editTextCount!=null){
                int value=1;
                try{value=Integer.valueOf(editTextCount.getText().toString());}catch(Exception e){}
                if(value>1)value--;
                editTextCount.setText(String.valueOf(value));
                manager.setRentalTimeDuration(value);
                if(manager.getSelectedProduct()!=null/*||manager.getDefaultProduct()!=null*/)
                  manager.updateDropoffFragmentForRentalCallbackHandler.execute(null);
              }
            }
          });
        }

        if(buttonPlus!=null){
          buttonPlus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
              if(editTextCount!=null){
                int value=0;
                try{value=Integer.valueOf(editTextCount.getText().toString());}catch(Exception e){}
                if(value<10)value++;
                editTextCount.setText(String.valueOf(value));
                manager.setRentalTimeDuration(value);
                if(manager.getSelectedProduct()!=null/*||manager.getDefaultProduct()!=null*/)
                  manager.updateDropoffFragmentForRentalCallbackHandler.execute(null);
              }
            }
          });
        }

        if(is_ride_later&&textViewOrderTime!=null){
          textViewOrderTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              CalendarDialogBuilder calendar;
              calendar=new CalendarDialogBuilder(context,DropoffFragment.this/*,Date.getTime()*/);
              Date start_date=new Date();
              Date end_date=new Date();
              end_date.setMonth(end_date.getMonth()+1);
              calendar.setStartDate(start_date.getTime());
              calendar.setEndDate(end_date.getTime());
              calendar.showCalendar();
            }
          });
        }

        if(manager.getSelectedProduct()!=null)
          manager.updateDropoffFragmentForRentalCallbackHandler.execute(null);

      }
      else{
        if(!is_ride_later&&editTextOrderTime!=null&&textViewOrderTime!=null){
          editTextOrderTime.setVisibility(View.INVISIBLE);
          textViewOrderTime.setVisibility(View.INVISIBLE);
        }
      }

      /*Button button_cancel=(Button)view.findViewById(R.id.button_cancel);//cancel not found
      if(button_cancel!=null){
        button_cancel.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v){
          }
        });
      }*/
    }

  }

  //tools methods
  public void updatePickupNameAndAddress(String name,String address){
    pickupName=name;pickupAddress=address;
    textViewPickupName=(TextView)view.findViewById(R.id.text_view_pickup_name);
    if(textViewPickupName!=null){
      if(pickupName!=null)textViewPickupName.setText(pickupName);
      else textViewPickupName.setText(context.getString(R.string.gps_location));
    }
    textViewPickupAddress=(TextView)view.findViewById(R.id.text_view_pickup_address);
    if(textViewPickupAddress!=null){
      if(pickupAddress!=null)textViewPickupAddress.setText(pickupAddress);
      else textViewPickupAddress.setText(T.EMPTY);
    }
  }
  public void updateDropoffNameAndAddress(String name,String address){
    dropoffName=name;dropoffAddress=address;
    textViewDropoffName=(TextView)view.findViewById(R.id.text_view_dropoff_name);
    if(textViewDropoffName!=null){
      if(dropoffName!=null)textViewDropoffName.setText(dropoffName);
      else textViewDropoffName.setText(context.getString(R.string.destination));
    }
    textViewDropoffAddress=(TextView)view.findViewById(R.id.text_view_dropoff_address);
    if(textViewDropoffAddress!=null){
      if(dropoffAddress!=null)textViewDropoffAddress.setText(dropoffAddress);
      else textViewDropoffAddress.setText(T.EMPTY);
    }
  }
  public void updateDuration(String duration){
    this.duration=duration;
    textViewDurationValue=(TextView)view.findViewById(R.id.text_view_duration_value);
    if(textViewDurationValue!=null&&duration!=null)textViewDurationValue.setText(duration);
  }
  public void updateDistance(String distance){
    this.distance=distance;
    textViewDistanceValue=(TextView)view.findViewById(R.id.text_view_distance_value);
    if(textViewDistanceValue!=null&&distance!=null)textViewDistanceValue.setText(distance);
  }
  public void updatePrice(String price){
    this.price=price;
    textViewPrice=(TextView)view.findViewById(R.id.text_view_price);
    if(textViewPrice!=null&&price!=null)textViewPrice.setText(price);
  }
  public void enabledBookNowButton(boolean enabled){
    Button button_book_now=(Button)view.findViewById(R.id.button_book_now);
    if(button_book_now!=null){
      button_book_now.setEnabled(enabled);
    }
  }
  public void enabledPaymentButton(boolean enabled){
    Button button_payment=(Button)view.findViewById(R.id.button_payment);
    if(button_payment!=null){
      button_payment.setEnabled(enabled);
    }
  }
  public void enabledEditTextOrderTime(boolean enabled){
    editTextOrderTime=(EditText)view.findViewById(R.id.edit_text_order_time);
    if(editTextOrderTime!=null)editTextOrderTime.setEnabled(enabled);
  }
  public String getOrderTime(){
    orderTime=null;
    editTextOrderTime=(EditText)view.findViewById(R.id.edit_text_order_time);
    if(editTextOrderTime!=null)orderTime=editTextOrderTime.getText().toString();
    return orderTime;
  }

  // That is the method of OnDateSetListener interface of CalendarDialogBuilder class
  @Override
  public void onDateSet(int year,int month,int day){
    if(year!=0){
      Date new_date=new Date(year-1900,month,day);
      String talk_date=manager.getTalkingDateMessage(manager.getMySQLDatetime(new_date.getTime()));
      textViewOrderTime.setText(talk_date);
      //reserved time
      manager.setRentalReserved(new_date.getTime());
    }
  }
}