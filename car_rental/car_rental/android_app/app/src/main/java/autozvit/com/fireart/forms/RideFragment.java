package autozvit.com.fireart.forms;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import autozvit.com.fireart.objects.User;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.tools.T;

public class RideFragment extends Fragment{
  private static final String ARG_NAME="name";
  private static final String ARG_DROPOFF_PICTURE="dropoff_picture";
  private static final String ARG_DROPOFF_NAME="dropoff_name";
  private static final String ARG_DROPOFF_ADDRESS="dropoff_address";
  private static final String ARG_PICTURE_URL="picture_url";
  private static final String ARG_DRIVER_NAME="driver_name";
  private static final String ARG_TRANSPORT_REVIEW="transport_review";
  private static final String ARG_TRANSPORT_COLOR="transport_color";
  private static final String ARG_TRANSPORT_NAME="transport_name";
  private static final String ARG_LICENSE_PLATE="license_plate";
  private static final String ARG_USER_RATE="user_rate";

  private String name;
  private int dropoffPicture;
  private String dropoffName;
  private String dropoffAddress;
  private String pictureUrl;
  private String driverName;
  private float transportReview;
  private String transportColor;
  private String transportName;
  private String licensePlate;
  private float userRate;

  private Context context;
  private View view;

  private ImageView imageViewDropoffPicture;
  private TextView textViewDropoffName;
  private TextView textViewDropoffAddress;
  private ImageView imageViewDriverPicture;
  private TextView textViewDriverName;
  private RatingBar ratingBarTransportReview;
  private TextView textViewTransportColor;
  private TextView textViewTransportName;
  //private TextView editTextLicensePlate;
  private EditText editTextLicensePlate;
  private RatingBar ratingBarUserRate;

  public static RideFragment newInstance(String name,int dropoff_picture,String dropoff_name,String dropoff_address,String picture_url,String driver_name,float transport_review,String transport_color,String transport_name,String license_plate,float user_rate){
    RideFragment fragment=new RideFragment();
    Bundle args=new Bundle();
    args.putString(ARG_NAME,name);
    args.putInt(ARG_DROPOFF_PICTURE,dropoff_picture);
    args.putString(ARG_DROPOFF_NAME,dropoff_name);
    args.putString(ARG_DROPOFF_ADDRESS,dropoff_address);
    args.putString(ARG_PICTURE_URL,picture_url);
    args.putString(ARG_DRIVER_NAME,driver_name);
    args.putFloat(ARG_TRANSPORT_REVIEW,transport_review);
    args.putString(ARG_TRANSPORT_COLOR,transport_color);
    args.putString(ARG_TRANSPORT_NAME,transport_name);
    args.putString(ARG_LICENSE_PLATE,license_plate);
    args.putFloat(ARG_USER_RATE,user_rate);

    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      name=getArguments().getString(ARG_NAME);
      dropoffPicture=getArguments().getInt(ARG_DROPOFF_PICTURE);
      dropoffName=getArguments().getString(ARG_DROPOFF_NAME);
      dropoffAddress=getArguments().getString(ARG_DROPOFF_ADDRESS);
      pictureUrl=getArguments().getString(ARG_PICTURE_URL);
      driverName=getArguments().getString(ARG_DRIVER_NAME);
      transportReview=getArguments().getFloat(ARG_TRANSPORT_REVIEW);
      transportColor=getArguments().getString(ARG_TRANSPORT_COLOR);
      transportName=getArguments().getString(ARG_TRANSPORT_NAME);
      licensePlate=getArguments().getString(ARG_LICENSE_PLATE);
      userRate=getArguments().getFloat(ARG_USER_RATE);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_ride,container,false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    context=getActivity();
    view=getView();
    if(view!=null){

      imageViewDropoffPicture=(ImageView)view.findViewById(R.id.image_view_dropoff_picture);
      if(imageViewDropoffPicture!=null&&dropoffPicture!=-1)imageViewDropoffPicture.setImageResource(dropoffPicture);
      textViewDropoffName=(TextView)view.findViewById(R.id.text_view_dropoff_name);
      if(textViewDropoffName!=null/*&&dropoffName!=null*/)textViewDropoffName.setText(dropoffName);
      textViewDropoffAddress=(TextView)view.findViewById(R.id.text_view_dropoff_address);
      if(textViewDropoffAddress!=null/*&&dropoffAddress!=null*/)textViewDropoffAddress.setText(dropoffAddress);

      imageViewDriverPicture=(ImageView)view.findViewById(R.id.image_view_driver_picture);
      // picture loading ...
      if(pictureUrl!=null&&imageViewDriverPicture!=null){
        Manager manager=((StartActivity)context).getManager();
        if(manager.getCurrentOrderAB()!=null&&manager.getCurrentOrderAB().transport!=null&&
           manager.getCurrentOrderAB().transport.sensor!=null&&
           manager.getCurrentOrderAB().transport.sensor.user!=null){
          User user=manager.getCurrentOrderAB().transport.sensor.user;
          if(user.picture!=null&&user.picture instanceof Drawable)imageViewDriverPicture.setImageDrawable((Drawable)user.picture);
          else manager.loadImage(pictureUrl+user.id,imageViewDriverPicture,user);
        }
      }

      textViewDriverName=(TextView)view.findViewById(R.id.text_view_driver_name);
      if(textViewDriverName!=null/*&&driverName!=null*/)textViewDriverName.setText(driverName);
      ratingBarTransportReview=(RatingBar)view.findViewById(R.id.rating_bar_transport_rate);
      if(ratingBarTransportReview!=null&&transportReview>0)ratingBarTransportReview.setRating(transportReview);
      ratingBarUserRate=(RatingBar)view.findViewById(R.id.rating_bar_user_rate);
      if(ratingBarUserRate!=null&&userRate>0)ratingBarUserRate.setRating(userRate);

      textViewTransportColor=(TextView)view.findViewById(R.id.text_view_transport_color);
      if(textViewTransportColor!=null&&transportColor!=null){
        int color=T.getColor(transportColor);
        textViewTransportColor.setText(transportColor);
        if(color!=0){
          textViewTransportColor.setTextColor(color);
          textViewTransportColor.setBackgroundColor(color);
        }
      }
      textViewTransportName=(TextView)view.findViewById(R.id.text_view_transport_name);
      if(textViewTransportName!=null/*&&transportName!=null*/)textViewTransportName.setText(transportName);
      //editTextLicensePlate=(TextView)view.findViewById(R.id.text_view_license_plate);
      //if(editTextLicensePlate!=null/*&&licensePlate!=null*/)editTextLicensePlate.setText(licensePlate);
      editTextLicensePlate=(EditText)view.findViewById(R.id.edit_text_license_plate);
      if(editTextLicensePlate!=null/*&&licensePlate!=null*/) editTextLicensePlate.setText(licensePlate);
    }

  }
}