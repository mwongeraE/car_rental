package autozvit.com.fireart.forms;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import autozvit.com.fireart.R;

public class CartFragment extends Fragment{
  private static final String ARG_COUNT="count";//count
  private static final String ARG_PRICE="price";//price
  private static final String ARG_DISCOUNT="discount";//discount
  private static final String ARG_CURRENCY="currency";//currency

  private String count;
  private String price;
  private String discount;
  private String currency;

  private Context context;

  private TextView textViewCountValue;
  private TextView textViewPrice;
  private TextView textViewDiscount;
  private TextView textViewCurrency;

  public static CartFragment newInstance(String count,String price,String discount,String currency){
    CartFragment fragment=new CartFragment();
    Bundle args=new Bundle();
    args.putString(ARG_COUNT,count);
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
      count=getArguments().getString(ARG_COUNT);
      price=getArguments().getString(ARG_PRICE);
      discount=getArguments().getString(ARG_DISCOUNT);
      currency=getArguments().getString(ARG_CURRENCY);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_cart,container,false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    context=getActivity();
    View view=getView();
    if(view!=null){
      textViewCountValue=(TextView)view.findViewById(R.id.text_view_count_value);
      if(textViewCountValue!=null&&count!=null)textViewCountValue.setText(count);
      textViewPrice=(TextView)view.findViewById(R.id.text_view_price);
      if(textViewPrice!=null&&discount!=null)textViewPrice.setText(price);
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
            ((StartActivity)context).getManager().removeFragment(T.FRAGMENT_NAME_CART);
          }
        });
      }*/
    }
  }

  public void updateCount(String count){
    View view=getView();
    if(view!=null){
      textViewCountValue=(TextView)view.findViewById(R.id.text_view_count_value);
      if(textViewCountValue!=null)textViewCountValue.setText(count);
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
