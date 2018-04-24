package autozvit.com.fireart.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import autozvit.com.fireart.R;
import autozvit.com.fireart.forms.StartActivity;

/*

  PayPal payment

  https://github.com/paypal/PayPal-Android-SDK

  The PayPal Android SDK is now available at 'com.paypal.sdk:paypal-android-sdk:2.15.3'

*/
public class PayPalPayment{
  public static final int REQUEST_CODE_PAYMENT=4321;//onActivityResult search code (call this.onActivityResult)
  public static final int RESULT_EXTRAS_INVALID=PaymentActivity.RESULT_EXTRAS_INVALID;
  public static final String CUSTOM_FORMAT="{user_id:%d,order_id=:%d}";
  private Context context;
  private static PayPalConfiguration config;
  /*private static PayPalConfiguration config=new PayPalConfiguration()
  .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
  .clientId("<YOUR_CLIENT_ID>");*/

  public PayPalPayment(Context context){
    this.context=context;

    String paypal_key=((StartActivity)context).getManager().getPaypalKey();
    String key=(paypal_key!=null&&paypal_key.length()>0&&!paypal_key.equals(Manager.DEFAULT_PAYPAL_KEY)?
                paypal_key:context.getString(R.string.paypal_test_key));

    config=new PayPalConfiguration()
    // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
    // or live (ENVIRONMENT_PRODUCTION)
    //.environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
    .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
    //.clientId("<YOUR_CLIENT_ID>");
    .clientId(key);

    Intent intent=new Intent(context,PayPalService.class);
    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
    context.startService(intent);
  }
  public void closeService(){
    context.stopService(new Intent(context,PayPalService.class));
  }
  //need to custom field preset as "{'user_id':<user_id>,'order_id':'<order_id>}" by CUSTOM_FORMAT
  public void payForItem(String sku,String amount,String currency,String invoice_number,String custom)throws Exception{
    com.paypal.android.sdk.payments.PayPalPayment payment=new com.paypal.android.sdk.payments.PayPalPayment(new BigDecimal(amount),currency,sku,
    com.paypal.android.sdk.payments.PayPalPayment.PAYMENT_INTENT_SALE);
    payment.invoiceNumber(invoice_number);
    payment.custom(custom);
    Intent intent=new Intent(context,PaymentActivity.class);
    // send the same configuration for restart resiliency
    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
    intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
    ((Activity)context).startActivityForResult(intent,REQUEST_CODE_PAYMENT);
  }
  public String onActivityResult(Intent data)throws JSONException{
    String ret_val=null;
    PaymentConfirmation confirm=data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
    if(confirm!=null){
      ret_val=confirm.toJSONObject().toString(4);
      // TODO: send 'confirm' to your server for verification.
      // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
      // for more details.
    }
    return ret_val;
  }
}