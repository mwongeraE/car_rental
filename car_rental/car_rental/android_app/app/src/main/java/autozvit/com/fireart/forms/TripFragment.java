package autozvit.com.fireart.forms;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import autozvit.com.fireart.R;

public class TripFragment extends Fragment{
  private static final String ARG_DURATION="duration";//duration
  private static final String ARG_DISTANCE="distance";//distance
  private static final String ARG_PRICE="price";//price
  private static final String ARG_DISCOUNT="discount";//discount
  private static final String ARG_CURRENCY="currency";//currency

  private String duration;
  private String distance;
  private String price;
  private String discount;
  private String currency;

  private Context context;

  private TextView textViewDurationValue;
  private TextView textViewDistanceValue;
  private TextView textViewPrice;
  private TextView textViewDiscount;
  private TextView textViewCurrency;

  public static TripFragment newInstance(String duration,String distance,String price,String discount,String currency){
    TripFragment fragment=new TripFragment();
    Bundle args=new Bundle();
    args.putString(ARG_DURATION,duration);
    args.putString(ARG_DISTANCE,distance);
    args.putString(ARG_PRICE,price);
    args.putString(ARG_DISCOUNT,discount);
    args.putString(ARG_CURRENCY,currency);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      duration=getArguments().getString(ARG_DURATION);
      distance=getArguments().getString(ARG_DISTANCE);
      price=getArguments().getString(ARG_PRICE);
      discount=getArguments().getString(ARG_DISCOUNT);
      currency=getArguments().getString(ARG_CURRENCY);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_trip,container,false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    context=getActivity();
    View view=getView();
    if(view!=null){
      textViewDurationValue=(TextView)view.findViewById(R.id.text_view_duration_value);
      if(textViewDurationValue!=null&&duration!=null)textViewDurationValue.setText(duration);
      textViewDistanceValue=(TextView)view.findViewById(R.id.text_view_distance_value);
      if(textViewDistanceValue!=null&&distance!=null)textViewDistanceValue.setText(distance);
      textViewPrice=(TextView)view.findViewById(R.id.text_view_price);
      if(textViewPrice!=null&&price!=null)textViewPrice.setText(price);
      textViewDiscount=(TextView)view.findViewById(R.id.text_view_discount);
      if(textViewDiscount!=null){
        if(discount!=null&&discount.length()>0)textViewDiscount.setText(discount);
        else textViewDiscount.setVisibility(View.INVISIBLE);
      }
      textViewCurrency=(TextView)view.findViewById(R.id.text_view_currency);
      if(textViewCurrency!=null&&currency!=null)textViewCurrency.setText(currency);

      /*Button button_cancel=(Button)view.findViewById(R.id.button_cancel);//cancel not found
      if(button_cancel!=null){
        button_cancel.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v){
            ((StartActivity)context).getManager().removeFragment(T.FRAGMENT_NAME_TRIP);
          }
        });
      }*/
    }
  }

  public void updateDuration(String duration){
    View view=getView();
    if(view!=null){
      textViewDurationValue=(TextView)view.findViewById(R.id.text_view_duration_value);
      if(textViewDurationValue!=null)textViewDurationValue.setText(duration);
    }
  }
  public void updateDistance(String distance){
    View view=getView();
    if(view!=null){
      textViewDistanceValue=(TextView)view.findViewById(R.id.text_view_distance_value);
      if(textViewDistanceValue!=null)textViewDistanceValue.setText(distance);
    }
  }
  public void updatePrice(String price){
    View view=getView();
    if(view!=null){
      textViewPrice=(TextView)view.findViewById(R.id.text_view_price);
      if(textViewPrice!=null) textViewPrice.setText(price);
    }
  }
}
