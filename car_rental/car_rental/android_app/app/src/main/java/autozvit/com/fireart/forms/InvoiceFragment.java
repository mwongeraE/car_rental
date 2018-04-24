package autozvit.com.fireart.forms;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import autozvit.com.fireart.R;
import autozvit.com.fireart.objects.User;
import autozvit.com.fireart.tools.Manager;

public class InvoiceFragment extends Fragment{
  private static final String ARG_NAME="name";
  private static final String ARG_PICKUP_PICTURE="pickup_picture";
  private static final String ARG_PICKUP_NAME="pickup_name";
  private static final String ARG_PICKUP_ADDRESS="pickup_address";
  private static final String ARG_DROPOFF_PICTURE="dropoff_picture";
  private static final String ARG_DROPOFF_NAME="dropoff_name";
  private static final String ARG_DROPOFF_ADDRESS="dropoff_address";
  private static final String ARG_PRICE="price";
  private static final String ARG_CURRENCY="currency";
  private static final String ARG_PICTURE_URL="picture_url";
  private static final String ARG_USER_NAME="user_name";
  private static final String ARG_REVIEW_DRIVER="review_driver";

  private String name;
  private int pickupPicture;
  private String pickupName;
  private String pickupAddress;
  private int dropoffPicture;
  private String dropoffName;
  private String dropoffAddress;
  private String price;
  private String currency;
  private String pictureUrl;
  private String userName;
  private boolean review_driver;

  private long userId;
  public long getUserId(){return userId;}

  private Context context;
  private View view;

  private ImageView imageViewPickupPicture;
  private TextView textViewPickupName;
  private TextView textViewPickupAddress;
  private ImageView imageViewDropoffPicture;
  private TextView textViewDropoffName;
  private TextView textViewDropoffAddress;
  private TextView textViewPrice;
  private TextView textViewCurrency;
  private ImageView imageViewUserPicture;
  private TextView textViewUserName;

  public static InvoiceFragment newInstance(String name,int pickup_picture,String pickup_name,String pickup_address,int dropoff_picture,String dropoff_name,String dropoff_address,String price,String currency,String picture_url,String user_name,boolean review_driver){
    InvoiceFragment fragment=new InvoiceFragment();
    Bundle args=new Bundle();
    args.putString(ARG_NAME,name);
    args.putInt(ARG_PICKUP_PICTURE,pickup_picture);
    args.putString(ARG_PICKUP_ADDRESS,pickup_address);
    args.putString(ARG_PICKUP_NAME,pickup_name);
    args.putInt(ARG_DROPOFF_PICTURE,dropoff_picture);
    args.putString(ARG_DROPOFF_NAME,dropoff_name);
    args.putString(ARG_DROPOFF_ADDRESS,dropoff_address);
    args.putString(ARG_PRICE,price);
    args.putString(ARG_CURRENCY,currency);
    args.putString(ARG_USER_NAME,user_name);
    args.putString(ARG_PICTURE_URL,picture_url);
    args.putBoolean(ARG_REVIEW_DRIVER,review_driver);
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
      price=getArguments().getString(ARG_PRICE);
      currency=getArguments().getString(ARG_CURRENCY);
      pictureUrl=getArguments().getString(ARG_PICTURE_URL);
      userName=getArguments().getString(ARG_USER_NAME);
      review_driver=getArguments().getBoolean(ARG_REVIEW_DRIVER);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_invoice,container,false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    context=getActivity();
    view=getView();
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

      textViewPrice=(TextView)view.findViewById(R.id.text_view_price);
      if(textViewPrice!=null&&price!=null)textViewPrice.setText(price);
      textViewCurrency=(TextView)view.findViewById(R.id.text_view_currency);
      if(textViewCurrency!=null&&currency!=null)textViewCurrency.setText(currency);

      imageViewUserPicture=(ImageView)view.findViewById(R.id.image_view_user_picture);
      // picture loading ...
      if(pictureUrl!=null&&imageViewUserPicture!=null){
        Manager manager=((StartActivity)context).getManager();
        User user=null;
        if(review_driver&&manager.getCurrentOrderAB()!=null){
          if(manager.getCurrentOrderAB().transport!=null&&manager.getCurrentOrderAB().transport.sensor!=null){
            user=manager.getCurrentOrderAB().transport.sensor.user;
          }
        }
        else{
          user=manager.getCurrentOrderAB().user;
        }

        if(user!=null){
          userId=user.id;
          if(user.picture!=null&&user.picture instanceof Drawable)imageViewUserPicture.setImageDrawable((Drawable)user.picture);
          else manager.loadImage(pictureUrl+user.id,imageViewUserPicture,user);
        }
      }
      textViewUserName=(TextView)view.findViewById(R.id.text_view_user_name);
      if(textViewUserName!=null/*&&userName!=null*/)textViewUserName.setText(userName);

      if(!review_driver){
        Button button=(Button)view.findViewById(R.id.button_transport_review);
        if(button!=null){
          button.setEnabled(false);button.setTextColor(context.getResources().getColor(R.color.DarkGray));
        }
      }
    }
  }

  //tools
  public byte getUserReviewValue(){
    byte new_review_value=0;
    RatingBar rating_bar=(RatingBar)view.findViewById(R.id.rating_bar_user_rate);
    if(rating_bar!=null)new_review_value=(byte)rating_bar.getRating();
    return new_review_value;
  }
}