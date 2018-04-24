package autozvit.com.fireart.tools;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GooglePlayPayment{
  //const
  public static final String BILLING_FILENAME_PREFERENCES="billing.pref";
  public static final String BILLING_SERVICE_NAME="[Google InAppBillingService]";
  public static final int BILLING_BUY_REQUEST_CODE=1234;//onActivityResult search code (call this.onActivityResult)
  public static final int BILLING_RESPONSE_RESULT_OK=0;//Success
  public static final int BILLING_RESPONSE_RESULT_USER_CANCELED=1;//User pressed back or canceled a dialog
  public static final int BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE=2;//Network connection is down
  public static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE=3;//Billing API version is not supported for the type requested
  public static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE=4;//Requested product is not available for purchase
  public static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR=5;//Invalid arguments provided to the API. This error can also indicate that the application was not correctly signed or properly set up for In-app Billing in Google Play, or does not have the necessary permissions in its manifest
  public static final int BILLING_RESPONSE_RESULT_ERROR=6;//Fatal error during the API action
  public static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED=7;//Failure to purchase since item is already owned
  public static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED=8;//Failure to consume since item is not owned

  //billing names
  public static final String BILLING_PACKAGE="com.android.vending";
  public static final String BILLING_SERVICE_BIND="com.android.vending.billing.InAppBillingService.BIND";
  public static final String BILLING_INAPP="inapp";
  public static final String BILLING_ITEM_ID_LIST="ITEM_ID_LIST";
  public static final String BILLING_RESPONSE_CODE="RESPONSE_CODE";
  public static final String BILLING_DETAILS_LIST="DETAILS_LIST";
  public static final String BILLING_BUY_INTENT="BUY_INTENT";
  public static final String BILLING_INAPP_PURCHASE_DATA="INAPP_PURCHASE_DATA";//onActivityResult(int requestCode,int resultCode,Intent data) -> data.getStringExtra(BILLING_INAPP_PURCHASE_DATA);
  public static final String BILLING_INAPP_DATA_SIGNATURE="INAPP_DATA_SIGNATURE";//onActivityResult(int requestCode,int resultCode,Intent data) -> data.getStringExtra(BILLING_INAPP_DATA_SIGNATURE);

  //onActivityResult values
  public static final String BILLING_DEVELOPER_PAYLOAD="developerPayload";
  public static final String BILLING_PRODUCT_ID="productId";
  public static final String BILLING_ORDER_ID="orderId";//The time the product was purchased, in milliseconds since the epoch (Jan 1, 1970).
  public static final String BILLING_PURCHASE_TIME="purchaseTime";//The purchase state of the order. Possible values are 0 (purchased), 1 (canceled), or 2 (refunded).
  public static final String BILLING_PURCHASE_STATE="purchaseState";
  public static final String BILLING_PURCHASE_TOKEN="purchaseToken";

  //pref settings
  private static final String SETTINGS_BILLING_PURCHASE_DATA="BillingPurchaseData";
  private static final String SETTINGS_BILLING_DATA_SIGNATURE="BillingDataSignature";
  private static final String SETTINGS_BILLING_PAYLOAD="BillingPayload";
  private static final String SETTINGS_BILLING_PRODUCT_ID="BillingProductId";
  private static final String SETTINGS_BILLING_ORDER_ID="BillingOrderId";
  private static final String SETTINGS_BILLING_PURCHASE_TIME="BillingPurchaseTime";
  private static final String SETTINGS_BILLING_PURCHASE_STATE="BillingPurchaseState";
  private static final String SETTINGS_BILLING_PURCHASE_TOKEN="BillingPurchaseToken";

  //billing_item_list
  private static final String BILLING_DONATE_ONE_USD="donate_one_usd";
  private static final String BILLING_CONTRACT_FOR_DEVELOPING="contract_for_developing";
  //json item name
  private static final String ITEM_PRODUCT_ID="productId";
  private static final String ITEM_PRICE="price";
  private static final String ITEM_TITLE="title";
  private static final String ITEM_DESCRIPTION="description";

  private Context context;

  private IInAppBillingService billingService=null;
  private ServiceConnection billingConnection=null;
  private Intent billingIntent=null;

  //need to list re-definition before using class
  private String[] billingItemsList={BILLING_DONATE_ONE_USD,BILLING_CONTRACT_FOR_DEVELOPING};

  private String[] paymentItemsList=null;
  public String[] getPaymentItemsList(){return paymentItemsList;}
  private String[] paymentItemsSku=null;
  public String[] getPaymentItemsSku(){return paymentItemsSku;}
  private String[] paymentItemsPrice=null;
  public String[] getPaymentItemsPrice(){return paymentItemsPrice;}
  private String[] paymentItemsDescription=null;
  public String[] getPaymentItemsDescription(){return paymentItemsDescription;}

  //billing onActivityResult
  private String billingPurchaseData=null;
  public String getBillingPurchaseData(){return billingPurchaseData;}
  private String billingDataSignature=null;
  public String getBillingDataSignature(){return billingDataSignature;}
  private String billingPayload=null;
  public String getBillingPayload(){return billingPayload;}
  private String billingProductId=null;
  public String getBillingProductId(){return billingProductId;}
  private String billingOrderId=null;//payment by order
  public String getBillingOrderId(){return billingOrderId;}
  private long billingPurchaseTime=0;//where billing do(if 0 then no billing or info saved for server)
  public long getBillingPurchaseTime(){return billingPurchaseTime;}
  private int billingPurchaseState=1;
  public int getBillingPurchaseState(){return billingPurchaseState;}
  private String billingPurchaseToken=null;
  public String getBillingPurchaseToken(){return billingPurchaseToken;}

  public GooglePlayPayment(Context context,final TypedCallback callback){
    this.context=context;
    final Object ptr=this;//for callback
    billingConnection=new ServiceConnection(){
      @Override
      public void onServiceDisconnected(ComponentName name){
        billingService=null;
      }
      @Override
      public void onServiceConnected(ComponentName name,IBinder service){
        billingService=IInAppBillingService.Stub.asInterface(service);
        if(callback!=null)callback.execute(ptr);
      }
    };
    billingIntent=new Intent(BILLING_SERVICE_BIND);
    billingIntent.setPackage(BILLING_PACKAGE);
    context.bindService(billingIntent,billingConnection,Context.BIND_AUTO_CREATE);
  }
  public void closeService(){
    if(billingService!=null&&billingConnection!=null)context.unbindService(billingConnection);
  }
  public String getErrorMessage(int response_code){
    String ret_val;
    switch(response_code){
      case -1:ret_val="Billing service unavailable";break;
      case BILLING_RESPONSE_RESULT_USER_CANCELED:ret_val="User pressed back or canceled a dialog";break;
      case BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE:ret_val="Network connection is down";break;
      case BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE:ret_val="Billing API version is not supported for the type requested";break;
      case BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE:ret_val="Requested product is not available for purchase";break;
      case BILLING_RESPONSE_RESULT_DEVELOPER_ERROR:ret_val="Invalid arguments provided to the API. This error can also indicate that the application was not correctly signed or properly set up for In-app Billing in Google Play, or does not have the necessary permissions in its manifest";break;
      case BILLING_RESPONSE_RESULT_ERROR:ret_val="Fatal error during the API action";break;
      case BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED:ret_val="Failure to purchase since item is already owned";break;
      case BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED:ret_val="Failure to consume since item is not owned";break;
      default:ret_val=T.EMPTY;
    }
    return BILLING_SERVICE_NAME+" "+ret_val;
  }
  public void readBillingInfo(){
    SharedPreferences shared_preferences=context.getSharedPreferences(BILLING_FILENAME_PREFERENCES,Context.MODE_PRIVATE);
    billingPurchaseData=shared_preferences.getString(SETTINGS_BILLING_PURCHASE_DATA,null);
    billingDataSignature=shared_preferences.getString(SETTINGS_BILLING_DATA_SIGNATURE,null);
    billingPayload=shared_preferences.getString(SETTINGS_BILLING_PAYLOAD,null);
    billingProductId=shared_preferences.getString(SETTINGS_BILLING_PRODUCT_ID,null);
    billingOrderId=shared_preferences.getString(SETTINGS_BILLING_ORDER_ID,null);
    billingPurchaseTime=shared_preferences.getLong(SETTINGS_BILLING_PURCHASE_TIME,0);
    billingPurchaseState=shared_preferences.getInt(SETTINGS_BILLING_PURCHASE_STATE,-1);
    billingPurchaseToken=shared_preferences.getString(SETTINGS_BILLING_PURCHASE_TOKEN,null);
  }
  public void writeBillingInfo(){
    SharedPreferences shared_preferences=context.getSharedPreferences(BILLING_FILENAME_PREFERENCES,Context.MODE_PRIVATE);
    SharedPreferences.Editor editor=shared_preferences.edit();
    editor.putString(SETTINGS_BILLING_PURCHASE_DATA,billingPurchaseData);
    editor.putString(SETTINGS_BILLING_DATA_SIGNATURE,billingDataSignature);
    editor.putString(SETTINGS_BILLING_PAYLOAD,billingPayload);
    editor.putString(SETTINGS_BILLING_PRODUCT_ID,billingProductId);
    editor.putString(SETTINGS_BILLING_ORDER_ID,billingOrderId);
    editor.putLong(SETTINGS_BILLING_PURCHASE_TIME,billingPurchaseTime);
    editor.putInt(SETTINGS_BILLING_PURCHASE_STATE,billingPurchaseState);
    editor.putString(SETTINGS_BILLING_PURCHASE_TOKEN,billingPurchaseToken);
    editor.commit();
  }
  public int getPaymentItems()throws Exception{
    int ret_val=-1;
    if(billingService==null)return ret_val;//wait for billing connecting
    ArrayList<String> purchase_items=new ArrayList();
    for(String item: billingItemsList){//copy all billing items
      purchase_items.add(item);
    }
    Bundle bundle_request=new Bundle();
    bundle_request.putStringArrayList(BILLING_ITEM_ID_LIST,purchase_items);
    Bundle bundle_items=billingService.getSkuDetails(3,context.getPackageName(),BILLING_INAPP,bundle_request);
    ret_val=bundle_items.getInt(BILLING_RESPONSE_CODE);
    if(ret_val==BILLING_RESPONSE_RESULT_OK){
      ArrayList<String> response_list=bundle_items.getStringArrayList(BILLING_DETAILS_LIST);
      int size=response_list.size(),index=-1;
      paymentItemsList=new String[size];
      paymentItemsSku=new String[size];
      paymentItemsPrice=new String[size];
      paymentItemsDescription=new String[size];
      for(String response:response_list){
        JSONObject object=new JSONObject(response);
        String sku=object.getString(ITEM_PRODUCT_ID);
        String title=object.getString(ITEM_TITLE);
        String price=object.getString(ITEM_PRICE);
        String description=object.getString(ITEM_DESCRIPTION);
        index++;
        paymentItemsList[index]=title+" "+price;
        paymentItemsSku[index]=sku;
        paymentItemsPrice[index]=price;
        paymentItemsDescription[index]=description;
      }
    }
    return ret_val;
  }
  //payload string format: <user_id/order_id/amount>
  //payload = String.valueOf(user_id)+"/"+String.valueOf(order_id)+"/"+String.valueOf(amount)
  public int payForItem(String sku,String payload)throws Exception{
    int ret_val=-1;
    if(billingService!=null){//wait for billing connecting
      Bundle bundle_buy=billingService.getBuyIntent(3,context.getPackageName(),sku,BILLING_INAPP,payload);
      ret_val=bundle_buy.getInt(BILLING_RESPONSE_CODE);
      if(ret_val==BILLING_RESPONSE_RESULT_OK){
        PendingIntent pending_buy=bundle_buy.getParcelable(BILLING_BUY_INTENT);
        ((Activity)context).startIntentSenderForResult(pending_buy.getIntentSender(),BILLING_BUY_REQUEST_CODE,new Intent(),Integer.valueOf(0),Integer.valueOf(0),Integer.valueOf(0));
      }
    }
    return ret_val;
  }
  //call activities onActivityResult
  public int onActivityResult(Intent data)throws JSONException{
    int response=data.getIntExtra(BILLING_RESPONSE_CODE,0);
    billingPurchaseData=data.getStringExtra(BILLING_INAPP_PURCHASE_DATA);
    billingDataSignature=data.getStringExtra(BILLING_INAPP_DATA_SIGNATURE);
    if(response==BILLING_RESPONSE_RESULT_OK&billingPurchaseData!=null){
      JSONObject obj=new JSONObject(billingPurchaseData);
      billingProductId=obj.getString(BILLING_PRODUCT_ID);
      billingPayload=obj.getString(BILLING_DEVELOPER_PAYLOAD);
      billingOrderId=obj.getString(BILLING_ORDER_ID);
      billingPurchaseTime=obj.getLong(BILLING_PURCHASE_TIME);
      billingPurchaseState=obj.getInt(BILLING_PURCHASE_STATE);
      billingPurchaseToken=obj.getString(BILLING_PURCHASE_TOKEN);
    }
    return response;
  }
}