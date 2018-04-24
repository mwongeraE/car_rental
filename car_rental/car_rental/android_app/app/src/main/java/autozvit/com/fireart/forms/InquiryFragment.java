package autozvit.com.fireart.forms;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import autozvit.com.fireart.R;
import autozvit.com.fireart.tools.Manager;

public class InquiryFragment extends Fragment{
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
  private static final String ARG_PICTURE_URL="picture_url";
  private static final String ARG_USER_ID="user_id";//userId
  private static final String ARG_USER_RATE="user_rate";
  private static final String ARG_USER_NAME="user_name";

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
  private String pictureUrl;
  private long userId;
  private float userRate;
  private String userName;

  private Context context;
  private Manager manager;

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
  private TextView editTextOrderTime;
  private ImageView imageViewUserPicture;

  private RatingBar ratingBarUserRate;
  private TextView textViewUserName;

  public static InquiryFragment newInstance(String name,int pickup_picture,String pickup_name,String pickup_address,int dropoff_picture,String dropoff_name,String dropoff_address,String duration,String distance,String price,String discount,String currency,String order_time,String picture_url,long user_id,float user_rate,String user_name){
    InquiryFragment fragment=new InquiryFragment();
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
    args.putLong(ARG_USER_ID,user_id);
    args.putString(ARG_PICTURE_URL,picture_url);
    args.putFloat(ARG_USER_RATE,user_rate);
    args.putString(ARG_USER_NAME,user_name);
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
      pictureUrl=getArguments().getString(ARG_PICTURE_URL);
      userId=getArguments().getLong(ARG_USER_ID);
      userRate=getArguments().getFloat(ARG_USER_RATE);
      userName=getArguments().getString(ARG_USER_NAME);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_inquiry,container,false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    context=getActivity();
    manager=((StartActivity)context).getManager();
    View view=getView();
    if(view!=null){

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
      if(editTextOrderTime!=null/*&&orderTime!=null*/)editTextOrderTime.setText(orderTime);

      textViewUserName=(TextView)view.findViewById(R.id.text_view_user_name);
      if(textViewUserName!=null/*&&userName!=null*/) textViewUserName.setText(userName);
      ratingBarUserRate=(RatingBar)view.findViewById(R.id.rating_bar_user_rate);
      if(ratingBarUserRate!=null&&userRate>0)ratingBarUserRate.setRating(userRate);

      //picasso picture loading ...
      imageViewUserPicture=(ImageView)view.findViewById(R.id.image_view_user_picture);
      if(userId!=-1&&pictureUrl!=null&&imageViewUserPicture!=null){
        String url=pictureUrl+userId;
        manager.loadImage(url,imageViewUserPicture,null);
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
}