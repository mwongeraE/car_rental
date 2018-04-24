package autozvit.com.fireart.forms;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;

import autozvit.com.fireart.R;
import autozvit.com.fireart.objects.Currency;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.tools.PayPalPayment;
import autozvit.com.fireart.tools.T;
import autozvit.com.fireart.tools.TypedCallback;

public class PaymentFragment extends Fragment{
  private static final String ARG_PAYMENT_PROVIDER="payment_provider";
  private static final String ARG_ORDER_ID="order_id";
  private static final String ARG_TOTAL_PRICE="total_price";
  private static final String ARG_CURRENCY="currency";

  private int paymentProvider;
  private long orderId;
  private double amount;
  private String currency;

  private Context context;

  private static CardInputWidget cardInputWidget=null;
  private static PayPalPayment payPalPayment=null;

  private TextView textViewOrderId;
  private TextView textViewPrice;
  private TextView textViewCurrency;

  public TypedCallback addPaymentByStripeProviderCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      final String token_id=((StartActivity)context).getManager().getStripeProviderTokenId();
      Currency active_currency=((StartActivity)context).getManager().getActiveCurrency();
      String currency=active_currency!=null?active_currency.name:T.EMPTY;
      ((StartActivity)context).getManager().addPaymentByStripeProviderRequest(token_id,orderId,(long)(amount*100)/*in cents*/,currency);
    }
  };

  public static PaymentFragment newInstance(int payment_provider,long order_id,double total_price,String currency){
    PaymentFragment fragment=new PaymentFragment();
    Bundle args=new Bundle();
    args.putInt(ARG_PAYMENT_PROVIDER,payment_provider);
    args.putLong(ARG_ORDER_ID,order_id);
    args.putDouble(ARG_TOTAL_PRICE,total_price);
    args.putString(ARG_CURRENCY,currency);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      paymentProvider=getArguments().getInt(ARG_PAYMENT_PROVIDER);
      orderId=getArguments().getLong(ARG_ORDER_ID);
      amount=getArguments().getDouble(ARG_TOTAL_PRICE);
      currency=getArguments().getString(ARG_CURRENCY);
    }
  }

  @Override
  public void onDestroy(){
    super.onDestroy();
    if(payPalPayment!=null)payPalPayment.closeService();
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    if(paymentProvider==Manager.PAYMENT_PROVIDER_STRIPE)
      return inflater.inflate(R.layout.fragment_stripe_payment,container,false);
    else if(paymentProvider==Manager.PAYMENT_PROVIDER_PAYPAL)
      return inflater.inflate(R.layout.fragment_paypal_payment,container,false);
    return null;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    context=getActivity();
    View view=getView();
    if(view!=null){

      TextView text_view_payment_info=(TextView)view.findViewById(R.id.text_view_payment_info);
      if(text_view_payment_info!=null)text_view_payment_info.setText(getString(R.string.payment_info));

      if(paymentProvider==Manager.PAYMENT_PROVIDER_STRIPE&&Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
        cardInputWidget=(CardInputWidget)view.findViewById(R.id.card_input_widget);
      else if(paymentProvider==Manager.PAYMENT_PROVIDER_PAYPAL&&Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
        payPalPayment=new PayPalPayment(context);

      textViewOrderId=(TextView)view.findViewById(R.id.text_view_order_id);
      if(textViewOrderId!=null&&amount!=-1)textViewOrderId.setText(String.valueOf(orderId));
      textViewPrice=(TextView)view.findViewById(R.id.text_view_price);
      if(textViewPrice!=null&&amount!=-1)textViewPrice.setText(((StartActivity)context).getManager().getPrice(amount));
      textViewCurrency=(TextView)view.findViewById(R.id.text_view_currency);
      if(textViewCurrency!=null&&currency!=null)textViewCurrency.setText(currency);

      Button button_payment=(Button)view.findViewById(R.id.button_payment);
      if(button_payment!=null){
        button_payment.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v){
            if(paymentProvider==Manager.PAYMENT_PROVIDER_STRIPE&&cardInputWidget!=null){
              Card card=cardInputWidget.getCard();
              /* source for any payment method (not only cards)
              ((StartActivity)context).getManager().createSourceByStripeProvider(card,amount,currency,null);
              String source_id=((StartActivity)context).getManager().getStripProviderSourceId();
              send to server payment info with source_id
              */
              ((StartActivity)context).getManager().createTokenByStripeProvider(card,addPaymentByStripeProviderCallbackHandler);
              //close keyboard
              InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
              imm.hideSoftInputFromWindow(cardInputWidget.getWindowToken(),0);
            }else if(paymentProvider==Manager.PAYMENT_PROVIDER_PAYPAL&&payPalPayment!=null){
              Currency active_currency=((StartActivity)context).getManager().getActiveCurrency();
              String currency=active_currency!=null?active_currency.name:T.EMPTY;
              String sku=getString(R.string.pay_order)+" "+String.valueOf(orderId);
              String string_amount=(amount>0?((StartActivity)context).getManager().getPrice(amount).replace(',','.'):null);
              long user_id=((StartActivity)context).getManager().getUserId();
              String custom=String.format(PayPalPayment.CUSTOM_FORMAT,user_id,orderId);
              try{
                //in real work preset invoice_number (as order_id)
                if(string_amount!=null)payPalPayment.payForItem(sku,string_amount,currency,T.EMPTY,custom);
              }catch(Exception e){
                T.messageBox(context,context.getString(R.string.message_error_title),e.getLocalizedMessage(),context.getString(R.string.button_cancel));
              }
            }
          }
        });
      }
    }
  }
}