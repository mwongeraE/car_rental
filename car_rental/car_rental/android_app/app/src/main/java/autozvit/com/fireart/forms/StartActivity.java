package autozvit.com.fireart.forms;

import autozvit.com.fireart.R;
import autozvit.com.fireart.forms.list.OrderABListContent;
import autozvit.com.fireart.forms.list.ProductListContent;
import autozvit.com.fireart.objects.OrderAB;
import autozvit.com.fireart.objects.OrderABStatus;
import autozvit.com.fireart.objects.Product;
import autozvit.com.fireart.objects.ProductParam;
import autozvit.com.fireart.objects.User;
import autozvit.com.fireart.tools.GooglePlayPayment;
import autozvit.com.fireart.tools.Map;
import autozvit.com.fireart.tools.PayPalPayment;
import autozvit.com.fireart.tools.T;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.forms.list.ListContent;
import autozvit.com.fireart.tools.TypedCallback;

import com.matesnetwork.Cognalys.VerifyMobile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class StartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                                OnClickListener,
                                                                ListFragment.OnListFragmentInteractionListener{

  public Manager manager=null;
  public Manager getManager(){return manager;}
  private Context context;
  private Object object=null;

  private static GooglePlayPayment googlePlayPayment=null;

  private DrawerLayout drawerLayout;//always here
  private NavigationView navigationView;//always here
  private FloatingActionButton bookingFloatingActionButton;
  public FloatingActionButton getBookingFloatingActionButton(){return bookingFloatingActionButton;}
  private FloatingActionButton driverFloatingActionButton;
  public FloatingActionButton getDriverFloatingActionButton(){return driverFloatingActionButton;}
  private FloatingActionButton cartFloatingActionButton;
  public FloatingActionButton getCartFloatingActionButton(){return cartFloatingActionButton;}
  private FloatingActionButton menuFloatingActionButton;
  public FloatingActionButton getMenuFloatingActionButton(){return menuFloatingActionButton;}
  private ProgressBar progressBar;

  //exit
  private void doOnExit(){
    manager.setNeedToExit(true);
    Map2Fragment frag=(Map2Fragment)manager.getFragment(T.FRAGMENT_NAME_MAP);
    if(frag!=null)frag.stopMoving();
  }
  private void doHistory(){
    if(manager.isRegistered()){
      //manager.showOrderABListCallbackHandler.execute(new OrderABListContent().getItemList());//create as empty list
      if(!manager.isOpenFragment(T.FRAGMENT_NAME_ORDER_LIST)){
        if(manager.isOpenFragment(T.FRAGMENT_NAME_PRODUCT_LIST))manager.removeFragment(T.FRAGMENT_NAME_PRODUCT_LIST);
        //goto history fragment
        if(manager.getMapMode()==Manager.MAP_CLIENT){
          manager.getOrderABCallbackHandler.execute(new Integer(0));//page_num
          //similar to
          //manager.getOrderABRequest(manager.showOrderABListCallbackHandler,T.PERCENT,0,manager.LIST_COUNT);
        }
        else if(manager.getMapMode()==Manager.MAP_DRIVER){
          manager.getOrderABTransportCallbackHandler.execute(new Integer(0));//page_num
          //similar to
          //manager.getOrderABTransportRequest(manager.showOrderABListCallbackHandler,manager.getDriverTransportId(),0,manager.LIST_COUNT);
        }
      }
    }
  }
/////////////////////////////////////////////////////////////////////////////////////////
  @Override
  protected void onCreate(Bundle savedInstanceState){
    try{
      super.onCreate(savedInstanceState);
      context=this;

      if(manager.DEBUG)Toast.makeText(getApplicationContext(),"Start app",Toast.LENGTH_SHORT).show();

      //T.updateLanguage(language,context);
      setContentView(R.layout.activity_start);

      Object obj=getLastCustomNonConfigurationInstance();
      if(obj!=null){//on rotate screen
        //if manager object in the memory
        //need to restore the backstack(all fragments in list)
        //manager=((Manager)obj).clone();
      }
      if(manager==null){
        //manager
        manager=new Manager(context);
        manager.setOnAddFragmentCallbackHandler(onAddFragmentCallbackHandler);
        manager.setOnNullFragmentCallbackHandler(onNullFragmentCallbackHandler);
        if(manager.DEBUG)Toast.makeText(getApplicationContext(),"Get data",Toast.LENGTH_SHORT).show();
        //preferences
        manager.getPrefData();

        if(manager.DEBUG)Toast.makeText(getApplicationContext(),"Get premissions",Toast.LENGTH_SHORT).show();
        //permissions
        //if(Build.VERSION.SDK_INT>=23)
        if(manager.getCountLoading()==0)manager.checkPermissions();

        //cart
        if(manager.DEBUG)Toast.makeText(getApplicationContext(),"Read cart",Toast.LENGTH_SHORT).show();
        try{manager.readCartList();}catch(IOException io_e){}

        if(manager.DEBUG)Toast.makeText(getApplicationContext(),"Set options",Toast.LENGTH_SHORT).show();
        //booking style
        String style=manager.getStyle();
        //T.messageBox(context,"bookingStyle","value="+style,"Cancel");
        manager.setBookingStyle(Integer.parseInt(style));

        //map provider
        String map_provider=manager.readMapProvider();
        //T.messageBox(context,"mapProvider","value="+map_provider,"Cancel");
        manager.setMapProvider(Integer.parseInt(map_provider));

        //payment provider
        //manager.setPaymentProvider(Manager.PAYMENT_PROVIDER_STRIPE);
        //manager.setPaymentProvider(Manager.PAYMENT_PROVIDER_PAYPAL);
        String payment_provider=manager.readPaymentProvider();
        manager.setPaymentProvider(Integer.parseInt(payment_provider));

        //first loading, congratulation message
        if(manager.getCountLoading()==0){
          //default location
          manager.putLatitude(String.valueOf(T.MAP_DEFAULT_LAT));
          manager.putLongitude(String.valueOf(T.MAP_DEFAULT_LNG));
          manager.getEditor().commit();
        }
        manager.putCountLoading(manager.getCountLoading()+1);
        manager.getEditor().commit();
        //internet
        if(!manager.isInternetOn()){
          throw new Exception(getString(R.string.internet_error));
        }
      }
      //else manager.restoreBackstack(manager.getFragmentTypeList(),manager.getFragmentList());
      //register
      if(manager.DEBUG)Toast.makeText(getApplicationContext(),"App registration",Toast.LENGTH_SHORT).show();
      if(!manager.isRegistered()){
        /*
          easyStyle:
            -> startFragment - registerFragment
                |               |
               signinFragment   |
                |               |
               splashUntilDoRequest
          otherStyle:
            -> signupFragment
                |
               splashUntilDoRequest
        */
        if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY||manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB)manager.showStartFragment();
        else manager.showSignupFragment(manager.getPhoneNumber());
      }
      else{//other
        manager.showSplashActivityUntilDoRequestCallbackHandler.execute(null);
        //check empty transport for driver
        if(manager.getUsertype()==T.USERTYPE_DRIVER&&
           manager.getSensorId()!=-1&&manager.getDriverTransportId()==-1)manager.getTransportRequest(manager.getSensorId());
      }

      if(manager.DEBUG)Toast.makeText(getApplicationContext(),"App buttons and toolbar",Toast.LENGTH_SHORT).show();
      initFloatButton();

      //fabs always hide
      bookingFloatingActionButton.hide();
      driverFloatingActionButton.hide();
      cartFloatingActionButton.hide();
      menuFloatingActionButton.hide();

      if(manager.getBookingStyle()==Manager.BOOKING_STYLE_DELIVERY)updateButtonProductVisibility(true);
      else updateButtonProductVisibility(false);

      initViews();
      if(!manager.isToolbar())initNoToolbar();else initToolbar();
      manager.setProgressBar(progressBar);
      object=manager;
    }catch(Exception e){if(manager!=null)manager.showFatalError(e.toString());}
  }
  @Override
  protected void onDestroy(){
    super.onDestroy();
    if(manager.isNeedDisableMobile())manager.internetOff();
    if(manager.getCurrentOrderAB()!=null){
      OrderAB orderAB=manager.getCurrentOrderAB();
      if(orderAB.status_id<0||orderAB.status_id==OrderABStatus.ORDER_STATUS_COMPLETED){
        manager.clearOrder();
        manager.clearTransport();
        manager.clearTransportSensor();
      }
    }
    if(manager.isNeedToClearSensorAndDriverTransport()){
      manager.clearSensor();
      manager.clearDriverTransport();
    }

    //cart
    if(manager.getCartList()!=null&&manager.getCartList().size()>0){
      manager.clearCartPictures();
      try{manager.writeCartList();}catch(IOException io_e){}
    }
    else{
      //File file=new File(context.getFilesDir().getAbsolutePath()+T.LOCAL_DELIM+T.FILENAME_CART_LIST);
      File file=new File(context.getFilesDir(),T.FILENAME_CART_LIST);
      if(file.exists()){
        //Toast.makeText(context.getApplicationContext(),"remove file="+T.FILENAME_CART_LIST,Toast.LENGTH_SHORT).show();
        file.delete();//remove file
      }
    }

    //google play payment
    if(googlePlayPayment!=null)googlePlayPayment.closeService();

    //stop service(send broadcast request?)
    //if(manager.isServiceStarted(DispatcherService.BROADCAST_CLASS_DISPATCHER_SERVICE))
    manager.stopDispatcherService();
  }
  @Override
  protected void onStart(){
    super.onStart();
    manager.showAllFragment();
  }
  @Override
  protected void onStop(){super.onStop();}
  @Override
  protected void onPause(){
    super.onPause();
    //close list fragments (uncloses list fragments shutdown app)
    manager.removeListFragment();
  }
  @Override
  protected void onResume(){super.onResume();}
  @Override
  public Object onRetainCustomNonConfigurationInstance(){
    return object;
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu){
    Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
    toolbar.inflateMenu(R.menu.menu_toolbar/*R.menu.menu_start*/);
    return super.onCreateOptionsMenu(menu);
  }
  @Override
  public boolean onNavigationItemSelected(MenuItem menu_item){
    //menu_item.setChecked(true);
    switch(menu_item.getItemId()){
      case R.id.navi_item_9://go away
        drawerLayout.closeDrawer(GravityCompat.START);
        doOnExit();finish();
        break;
      case R.id.navi_item_8://history
        drawerLayout.closeDrawer(GravityCompat.START);
        doHistory();
        break;
      case R.id.navi_item_7:
        drawerLayout.closeDrawer(GravityCompat.START);
        manager.callToServiceDialog();
        break;
      case R.id.navi_item_6://driver candidate
        drawerLayout.closeDrawer(GravityCompat.START);
        manager.driverCandidateDialog();
        break;
      case R.id.navi_item_5:
        drawerLayout.closeDrawer(GravityCompat.START);
        manager.socialNetworkDialog();
        break;
      case R.id.navi_item_4://prepaid
        drawerLayout.closeDrawer(GravityCompat.START);
        manager.prepaidAccountDialog();
        break;
      case R.id.navi_item_3:
        drawerLayout.closeDrawer(GravityCompat.START);
        if(manager.isRegistered()){//ooops! message here found now
          if(manager.isOpenFragment(T.FRAGMENT_NAME_MESSAGE_LIST))manager.removeFragment(T.FRAGMENT_NAME_MESSAGE_LIST);
          manager.getMessageCallbackHandler.execute(new Integer(0));//page_num
        }
        break;
      case R.id.navi_item_2://settings
        drawerLayout.closeDrawer(GravityCompat.START);
        //easy product list must close before open Settings Activity
        //manager.removeAllWithoutMapFragment();
        manager.removeFragment(T.FRAGMENT_NAME_PRODUCT);
        manager.removeFragment(T.FRAGMENT_NAME_PICKUP);

        manager.showSettingsActivity();
        break;
      case R.id.navi_item_1://signup
        drawerLayout.closeDrawer(GravityCompat.START);
        if(manager.getFragmentType()!=T.FRAGMENT_SIGNUP)manager.showSignupFragment(null);
        break;
      case R.id.navi_item_0://client & driver profile
        drawerLayout.closeDrawer(GravityCompat.START);
        if(manager.isRegistered()){
          //easy product list must close before open Settings Activity
          //manager.removeAllWithoutMapFragment();
          manager.removeFragment(T.FRAGMENT_NAME_PRODUCT);
          manager.removeFragment(T.FRAGMENT_NAME_PICKUP);

          manager.showDriverActivity();
        }
        break;
      case R.id.navi_item_99://stat
        drawerLayout.closeDrawer(GravityCompat.START);
        if(manager.getFragmentType()!=T.FRAGMENT_STAT)manager.getStatRequest();
        break;
    }
    return false;
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item){
    switch(item.getItemId()){
      case android.R.id.home:
        drawerLayout.openDrawer(GravityCompat.START);
        break;
      case R.id.action_exit:
        doOnExit();finish();
        break;
      case R.id.action_history:
        doHistory();
        break;
    }
    //return true;
    return super.onOptionsItemSelected(item);
  }
  @Override
  public void onClick(View v){
    try{
      InputMethodManager imm;
      OrderAB order;
      String email,phone,password;
      Fragment frag;
      switch(v.getId()){
        case R.id.button_tool_window_cancel:
          //only service fragment have a cancel or ok button
          //T.FRAGMENT_SUCCESS, T.FRAGMENT_ERROR, T.FRAGMENT_INFO, T.FRAGMENT_IMAGE
          manager.removeServiceFragment();
          if(manager.getFragment()==null){//Kill activity
            doOnExit();
            finish();
          }
          break;
        case R.id.button_back:
          manager.removeCurrFragment();
          //manager.fabAction(-1);
          break;
        case R.id.button_start_register:
          manager.showRegisterFragment(null,null,null,null,null,null);
          break;
        case R.id.button_start_signin:
          manager.showSigninFragment(null);
          break;
        case R.id.button_start_signup:
          manager.showSignupFragment(manager.getPhoneNumber());
          break;
        case R.id.button_register:
          frag=manager.getFragment(T.FRAGMENT_NAME_REGISTER);
          if(frag!=null){
            if(Manager.VERIFY_PHONE_NUMBER){//verify by cognalys api
              email=((RegisterFragment)frag).getEmail();
              phone=((RegisterFragment)frag).getPhone();
              password=((RegisterFragment)frag).getPassword();
              if(email!=null&&email.length()>0&&phone!=null&&phone.length()>0&&password!=null&&password.length()>0){
                manager.startVerifyPhoneNumber(StartActivity.this,phone);
              }
            }else{//goto signup
              manager.signupRequest(frag,false);
            }
          }
          break;
        case R.id.button_policy:
          manager.openUrl(context.getString(R.string.app_policy_url));
          break;
        case R.id.button_signin:
          frag=manager.getFragment(T.FRAGMENT_NAME_SIGNIN);
          if(frag!=null){
            phone=((SigninFragment)frag).getPhone();
            password=((SigninFragment)frag).getPassword();
            if(phone!=null&&phone.length()>0&&password!=null&&password.length()>0){//login data is already registered
              manager.putUsername(phone);
              manager.putPassword(password);
              manager.getEditor().commit();

              //get type for check signed user (if user registered, he has type and add sensor, get data after)
              manager.getUsertypeRequest();
            }
          }
          break;
        case R.id.button_forgot:
          //call to the office (office send email or sms with password)
          if(manager.isPasswordRecovery()){
            manager.callToServiceDialog();
          }else{
            frag=manager.getFragment(T.FRAGMENT_NAME_SIGNIN);
            if(frag!=null){
              phone=((SigninFragment)frag).getPhone();
              if(phone!=null&&phone.length()>0){
                manager.passwordRecoveryRequest(phone);
                manager.setPasswordRecovery(true);
              }else manager.phoneNumberMessage();
            }
          }
          break;
        case R.id.button_signup:
          manager.signupRequest(manager.getFragment(),false);
          break;
        case R.id.button_demo:
          manager.signupRequest(manager.getFragment(),true);
          break;
        case R.id.button_easy_register:
          manager.showSignupFragment(manager.getPhoneNumber());
          break;
        case R.id.button_tip_agree:
          manager.removeFragment(T.FRAGMENT_NAME_TIP);
          break;
        case R.id.button_do_not_show:
          manager.cancelTipDialog();
          break;
        case R.id.button_service:
          if(manager.getSelectedProduct()==null)manager.setSelectedProduct(manager.getDefaultProduct());
          manager.showProductListCallbackHandler.execute(manager.newInstanceListWithOptions(manager.getProductList(),true));
          //manager.showProductListCallbackHandler.execute(manager.getProductList());//show not checking list
          //how to get list adapter object: ((ProductListAdapter)((ListFragment)frag).getListAdapterObject()).setChecking(true);
          break;
        case R.id.button_cart:
          manager.showCartListCallbackHandler.execute(manager.getCartList());
          break;
        case R.id.button_product:
          if(manager.getProductList()!=null&&manager.getProductList().size()>0)
            manager.showProductListCallbackHandler.execute(manager.newInstanceListWithOptions(manager.getProductList(),true));
          else manager.getProductCallbackHandler.execute(new Integer(0));//page_num
          break;
        case R.id.button_book_trip:
          if(manager.getCurrentDirection()!=null){
            manager.addOrderABRequest();
            bookingFloatingActionButton.setImageResource(R.drawable.ic_clear_white_24dp);
          }
          break;
        case R.id.button_book_delivery:
          if(!manager.isOpenFragment(T.FRAGMENT_NAME_FIND)){
            boolean is_preset=(manager.getFindAddress()!=null&&manager.getFindAddress().length()>0);
            String delivery=(is_preset?manager.getFindAddress():getString(R.string.app_place));
            String currency=manager.getActiveCurrencyTitle()!=null?manager.getActiveCurrencyTitle():manager.getActiveCurrency().name;
            manager.showFindFragment(R.layout.fragment_delivery,getString(R.string.order_delivery_address),R.drawable.marker_b,null,delivery,null);
          }
          if((manager.getFindAddress()!=null&&manager.getFindAddress().length()>0)||(manager.getDeliveryLatitude()!=null&&manager.getDeliveryLongitude()!=null)){
            manager.sendOrderDialog();
            //bookingFloatingActionButton.setImageResource(R.drawable.ic_clear_white_24dp);
          }
          break;
        case R.id.button_find:
        case R.id.image_view_picture://find picture
          //close keyboard
          imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(v.getWindowToken(),0);
          if(manager.getBookingStyle()==Manager.BOOKING_STYLE_DELIVERY){
            frag=manager.getFragment(T.FRAGMENT_NAME_FIND);
            if(frag!=null){
              String address=((FindFragment)frag).getDestinationAddress().trim();
              String find_address=(manager.getFindAddress()!=null?manager.getFindAddress():T.EMPTY);
              if(address.length()>0&&!address.equalsIgnoreCase(find_address)){
                if(manager.getBookingStyle()==Manager.BOOKING_STYLE_DELIVERY)manager.googleGeocode(address,manager.showGeocodeCallbackHandler);
              }
              else{
                //T.messageBox(context,"latitude/longitude",manager.getDeliveryLatitude()+"/"+manager.getDeliveryLongitude(),"Cancel");
                if(manager.getBookingStyle()==Manager.BOOKING_STYLE_DELIVERY){
                  double lat=Double.parseDouble(manager.getDeliveryLatitude()),lng=Double.parseDouble(manager.getDeliveryLongitude());
                  Map map=new Map();
                  Map.Location location=map.newLocationInstance(lat,lng);
                  manager.showMapCallbackHandler.execute(location);
                }
              }
            }
          }
          else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_TRIP){
            frag=manager.getFragment(T.FRAGMENT_NAME_FIND);
            if(frag!=null)manager.addressFindRequest(frag);//find by fragment edit text fields (pickup and dest)
          }
          break;
        case R.id.image_button_order:
          if(!manager.isOpenFragment(T.FRAGMENT_NAME_ORDER_LIST)&&manager.getCurrentOrderAB()!=null){
            ArrayList<OrderAB> order_list=new ArrayList();
            order_list.add(manager.getCurrentOrderAB());
            manager.showOrderABListCallbackHandler.execute(order_list);
          }
          break;
        case R.id.button_purchase:
          drawerLayout.closeDrawer(GravityCompat.START);//botton on drawer (on layout_navigation)
          googlePlayPayment=manager.googlePlayPaymentNewInstance(manager.afterGooglePlayPaymentCallbackHandler);//start google payment service
          break;
        case R.id.button_social_network:
          manager.socialNetworkDialog();
          break;
        case R.id.button_google:
          manager.openUrl(context.getString(R.string.google_url));
          break;
        case R.id.button_facebook:
          manager.openUrl(context.getString(R.string.facebook_url));
          break;
        case R.id.button_twitter:
          manager.openUrl(context.getString(R.string.twitter_url));
          break;
        case R.id.button_instagram:
          manager.openUrl(context.getString(R.string.instagram_url));
          break;
        case R.id.floating_action_button_call:
          if(manager.getCurrentOrderAB()!=null){
            User user=manager.getCurrentOrderAB().user;
            if(manager.DEBUG)Toast.makeText(getApplicationContext(),"CALL:"+(user!=null?user.phone:T.EMPTY),Toast.LENGTH_SHORT).show();
            try{
              if(user.phone!=null&&user.phone.length()>0)startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+user.phone)));
            }catch(SecurityException se){}
          }
          break;
        case R.id.button_call:
          if(manager.getCurrentOrderAB()!=null&&manager.getCurrentOrderAB().transport!=null&&manager.getCurrentOrderAB().transport.sensor!=null){
            phone=manager.getCurrentOrderAB().transport.sensor.phone;
            if(manager.DEBUG)Toast.makeText(getApplicationContext(),"CALL:"+(phone!=null?phone:T.EMPTY),Toast.LENGTH_SHORT).show();
            try{
              if(phone!=null&&phone.length()>0)startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone)));
            }catch(SecurityException se){}
          }
          break;
        case R.id.floating_action_button_clear_cart:
          if(manager.getCartList()!=null&&manager.getCartList().size()>0){
            manager.clearCartDialog();
          }
          break;
        case R.id.floating_action_button_product:
          if(!manager.isOpenFragment(T.FRAGMENT_NAME_PRODUCT)){
            manager.showProductFragmentCallbackHandler.execute(null);
          }
          else manager.removeFragment(T.FRAGMENT_NAME_PRODUCT);
          break;
        case R.id.button_ride_now:
          manager.setRideLater(false);
          manager.removeFragment(T.FRAGMENT_NAME_PICKUP);
          manager.showDropoffFragmentCallbackHandler.execute(null);
          break;
        case R.id.button_ride_later:
          manager.setRideLater(true);
          manager.removeFragment(T.FRAGMENT_NAME_PICKUP);
          manager.showDropoffFragmentCallbackHandler.execute(null);
          break;
        case R.id.button_book_now:
          if(!Manager.CAR_RENTAL){
            if(manager.isRideLater()){
              String order_time=null;
              frag=manager.getFragment(T.FRAGMENT_NAME_DROPOFF);
              if(frag!=null)order_time=((DropoffFragment)frag).getOrderTime();
              if(order_time!=null&&order_time.length()>0){
                SimpleDateFormat format=new SimpleDateFormat(Manager.DATETIME_FLOAT_FORMAT);
                Date curr_date=new Date();
                Date date=format.parse((curr_date.getYear()+1900)+T.MINUS+(curr_date.getMonth()+1)+T.MINUS+curr_date.getDate()+T.SPACE+order_time);
                manager.setReservedTime(date.getTime());
              }else break;
            }
            //add_order (There are limiting call driver? add-on form open on book now)
            if(manager.getCurrentDirection()!=null)manager.addOrderABRequest();//Client book Trip
            else{//CALL DRIVER
              //default product
              if(manager.getSelectedProduct()==null&&manager.getDefaultProduct()==null){
                manager.findDefaultProductCallbackHandler.execute(null);
              }
              manager.addOrderABRequest(true);//Client call Driver
            }
          }
          else{//CAR RENTAL
            String order_time=null;
            frag=manager.getFragment(T.FRAGMENT_NAME_DROPOFF);
            if(frag!=null)order_time=((DropoffFragment)frag).getOrderTime();
            if(order_time!=null&&order_time.length()>0){
              SimpleDateFormat format=new SimpleDateFormat(Manager.DATETIME_FLOAT_FORMAT);
              Date res_date=new Date(manager.getRentalReserved());
              Date date=format.parse((res_date.getYear()+1900)+T.MINUS+(res_date.getMonth()+1)+T.MINUS+res_date.getDate()+T.SPACE+order_time);
              manager.setReservedTime(date.getTime());
            }
            manager.addOrderABRequest();
          }

          break;
        case R.id.button_payment:
          //payment Dialog
          if(manager.getCurrentOrderAB()!=null)manager.payOrderDialog(manager.getCurrentOrderAB().id,manager.getCurrentOrderAB().total_price);
          break;
        case R.id.button_cancel_trip:
          if(manager.getCurrentOrderAB()!=null){
            manager.cancelOrderDialog(manager.getCurrentOrderAB().id,null);
          }
          break;
        //case R.id.button_prepaid_code:break;
        //case R.id.button_prepaid_payment:break;
        case R.id.button_transport_review:
          if(manager.getCurrentOrderAB()!=null){
            order=manager.getCurrentOrderAB();
            String transport=(order.transport!=null?order.transport.name+T.SPACE+order.transport.license_plate:T.EMPTY);
            manager.makeTripRatingDialog(order.id,order.user_id,order.transport_id,(byte)-1,null,null,transport);
          }
          break;
        case R.id.button_user_review:
          frag=manager.getFragment(T.FRAGMENT_NAME_INVOICE);
          if(frag!=null){
            long user_id=((InvoiceFragment)frag).getUserId();
            byte new_review_value=((InvoiceFragment)frag).getUserReviewValue();
            if(new_review_value==0)break;//need a real value!
            //review rating(and start again)
            order=manager.getCurrentOrderAB();//order for review describe
            manager.addUserReviewRequest(order!=null?order.id:-1,manager.getUserId(),user_id,new_review_value);
            manager.removeFragment(T.FRAGMENT_NAME_INVOICE);
            if(manager.isTripMode())manager.closeTripMode();//driver on the way
            else if(manager.getMapMode()==Manager.MAP_CLIENT){//client works
              manager.removeAllWithoutMapFragment();
              frag=manager.getFragment(T.FRAGMENT_NAME_MAP);
              if(frag!=null){
                ((Map2Fragment)frag).startClient();
              }
              //ride is finished, set dropoff empty
              manager.clearOrderCalcValues();
              manager.putDropoffName(null);
              manager.putDropoffAddress(null);
              manager.getEditor().commit();
            }
          }
          break;
        case R.id.button_invoice:
          if(manager.getCurrentOrderAB()!=null){
            order=manager.getCurrentOrderAB();
            //invoice fragment here
            if(order.status_id==OrderABStatus.ORDER_STATUS_COMPLETED)
              manager.showInvoiceFragmentCallbackHandler.execute(new Boolean(false));//driver review client
            break;
          }
        case R.id.button_take_order:
          order=manager.getSelectedOrderAB();
          if(order!=null)manager.takeOrderDialog(order.id,manager.getDriverTransportId(),order.user_id,null);
          break;
        case R.id.image_button_message_send:
          frag=manager.getFragment(T.FRAGMENT_NAME_MESSAGE_LIST);
          if(frag!=null){
            View view=frag.getView();
            EditText edit_text_new_message=(EditText)view.findViewById(R.id.edit_text_new_message);
            EditText edit_text_call_name=(EditText)view.findViewById(R.id.edit_text_call_name);
            if(edit_text_call_name!=null&&edit_text_new_message!=null){
              String call_name=edit_text_call_name.getText().toString();
              String new_message=edit_text_new_message.getText().toString();
              if(call_name.length()>0&&new_message.length()>0){
                //close keyboard
                imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_text_new_message.getWindowToken(),0);

                manager.addMessageRequest(call_name,new_message);

                //clear new message
                edit_text_new_message.setText(T.EMPTY);
              }
            }
          }
          break;

          //todo code here
      }
    }catch(Exception e){manager.showErrorFragment(e.getLocalizedMessage());}
  }
  @Override
  protected void onActivityResult(int requestCode,int resultCode,Intent data){
    try{
      super.onActivityResult(requestCode,resultCode,data);
      if(requestCode==T.ACTIVITY_DRIVER){//driver settings
        if(resultCode==RESULT_OK){
          if(data!=null){
            String message=data.getStringExtra(Manager.INTENT_PARAM_MESSAGE);
            manager.showSuccessFragment(message);
            //restart for driver
            manager.reloadDialog();
          }
        }
        else if(resultCode==RESULT_CANCELED){
        }
      }
      else if(requestCode==T.ACTIVITY_SETTINGS){//local settings
        //sync
        manager.prefSync();
      }
      else if(requestCode==GooglePlayPayment.BILLING_BUY_REQUEST_CODE){//buy google play
        if(googlePlayPayment!=null){
          googlePlayPayment.onActivityResult(data);
          googlePlayPayment.writeBillingInfo();
          //manager.addGooglePlayPaymentCallbackHandler(googlePlayPayment);//save on backend (not used)
          manager.showSuccessFragment(context.getString(R.string.message_success_payment));
        }
      }
      //paypal
      else if(requestCode==PayPalPayment.REQUEST_CODE_PAYMENT){
        if(resultCode==Activity.RESULT_OK){
          ((StartActivity)context).getManager().showSuccessFragment(context.getString(R.string.message_success_payment));
          /*if(payPalPayment!=null){ if need to client send confirmation to backend server (paypal webhook using for that)
            try{payPalPayment.onActivityResult(data);}
            catch(Exception e){
              T.messageBox(context,context.getString(R.string.message_error_title),e.getLocalizedMessage(),context.getString(R.string.button_cancel));
            }
          }*/
        }
        else if(resultCode==PayPalPayment.RESULT_EXTRAS_INVALID){
          ((StartActivity)context).getManager().showErrorFragment("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
      }
      //cognalys
      else if(requestCode==VerifyMobile.REQUEST_CODE){
        String message=data.getStringExtra(Manager.INTENT_PARAM_MESSAGE);
        int result=data.getIntExtra(Manager.INTENT_PARAM_RESULT,0);
        if(Manager.DEBUG){
          Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
          Toast.makeText(getApplicationContext(),"phone verify result="+result,Toast.LENGTH_SHORT).show();
        }
        if(result==T.COGNALYS_RESULT_CODE_VERIFICATION_SUCCESS){
          Fragment frag=manager.getFragment(T.FRAGMENT_NAME_REGISTER);
          if(frag!=null){
            manager.signupRequest(frag,false);
          }
        }

      }
    }catch(Exception e){/*error fragment*/}
  }
  @Override
  public boolean onKeyDown(int keyCode,KeyEvent event){
    if(keyCode==KeyEvent.KEYCODE_BACK){
      if(drawerLayout.isDrawerOpen(GravityCompat.START)){
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
      }
      else if(manager.getFragment()!=null){
        //if(manager.isServiceFragmentBeforeMapFragment());
        int list_size=manager.getFragmentTypeList().size(),
            type=manager.getFragmentTypeList().get(list_size-1);//last action hero

        manager.removeCurrFragment();

        if((manager.getFragment()==null&&manager.getBookingStyle()!=Manager.BOOKING_STYLE_DELIVERY)||
           (manager.getFragment()==null&&!manager.isRegistered())){//Kill activity
          doOnExit();finish();
        }

        if(type==T.FRAGMENT_MAP){
          manager.setMapStarted(false);
        }
        if(type==T.FRAGMENT_CART){
          View view=findViewById(android.R.id.content);
          if(view!=null)showSnackBar(view,getString(R.string.back_to_prev),R.string.button_back,manager.showCartFragmentCallbackHandler,manager.getCartList());
        }
        else if(type==T.FRAGMENT_TRIP){
          View view=findViewById(android.R.id.content);
          if(view!=null)showSnackBar(view,getString(R.string.back_to_prev),R.string.button_back,manager.showTripFragmentAndRouteCallbackHandler,manager.getCurrentDirection());
        }
        else if(type==T.FRAGMENT_PICKUP){
          View view=findViewById(android.R.id.content);
          if(view!=null)showSnackBar(view,getString(R.string.back_to_prev),R.string.button_back,manager.showPickupFragmentCallbackHandler,null);
        }
        else if(type==T.FRAGMENT_DROPOFF&&manager.getOrderId()==-1){
          //start pickup fragment
          manager.showPickupFragmentCallbackHandler.execute(null);
        }
        else if(type==T.FRAGMENT_INVOICE){
          View view=findViewById(android.R.id.content);
          boolean is_driver_review=manager.isTripMode()?new Boolean(false):new Boolean(true);
          if(view!=null)showSnackBar(view,getString(R.string.back_to_prev),R.string.button_back,manager.showInvoiceFragmentCallbackHandler,is_driver_review);
        }
        else if(type==T.FRAGMENT_TAXIMETER){
          View view=findViewById(android.R.id.content);
          if(view!=null)showSnackBar(view,getString(R.string.back_to_prev),R.string.button_back,manager.showTaximeterFragmentCallbackHandler,manager.getCurrentOrderAB());
        }

        //manager.fabAction(-1);
        return true;
      }
    }
    return super.onKeyDown(keyCode,event);
  }
  @Override
  public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults){
    switch(requestCode){
      case Manager.SET_PERMISSIONS_REQUEST_CODE:
        /*
        if(Manager.TYPE_OF_APP==Manager.DRIVER_APP)manager.reloadApp(StartDriverActivity.class,context.getPackageName());
        else if(Manager.TYPE_OF_APP==Manager.CLIENT_APP)manager.reloadApp(StartClientActivity.class,context.getPackageName());
        else manager.reloadApp(StartActivity.class,manager.getPackageName());
        ((StartActivity)context).finish();
        */
        break;
    }
  }
  private void initToolbar(){
    Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);
    if(manager.getBookingStyle()==Manager.BOOKING_STYLE_DELIVERY)
      toolbar.setTitle(R.string.app_title_delivery);
    else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_TRIP)
      toolbar.setTitle(R.string.app_title_trip);
    toolbar.setSubtitle(R.string.app_info);
    //color
    toolbar.setTitleTextColor(getResources().getColor(R.color.label_text_large));
    toolbar.setSubtitleTextColor(getResources().getColor(R.color.fragment_background));

    //specific icon
    //getSupportActionBar().setIcon(icon);
    progressBar=(ProgressBar)findViewById(R.id.progress_bar);
    if(progressBar!=null){
      progressBar.setProgress(0);
      progressBar.setVisibility(View.INVISIBLE);
    }

    ProgressBar progress_bar=(ProgressBar)findViewById(R.id.progress_bar_large);
    if(progress_bar!=null)progress_bar.setVisibility(View.GONE);

    /*FloatingActionButton action_button=(FloatingActionButton)findViewById(R.id.floating_action_button_menu);
    if(action_button!=null)action_button.setVisibility(View.GONE);*/
  }
  private void initNoToolbar(){
    RelativeLayout toolbar_layout=(RelativeLayout)findViewById(R.id.toolbar_layout);
    if(toolbar_layout!=null)toolbar_layout.setVisibility(View.GONE);

    progressBar=(ProgressBar)findViewById(R.id.progress_bar_large);
    if(progressBar!=null){
      progressBar.setProgress(0);
      progressBar.setVisibility(View.INVISIBLE);
    }

    FloatingActionButton action_button=(FloatingActionButton)findViewById(R.id.floating_action_button_menu);
    if(action_button!=null)action_button.setVisibility(View.VISIBLE);
  }
  private void initViews(){
    drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
    navigationView=(NavigationView)findViewById(R.id.navigation_view);
    navigationView.setNavigationItemSelectedListener(this);

    drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener(){
      @Override
      public void onDrawerSlide(View drawerView,float slideOffset){
        drawerLayout.bringChildToFront(drawerView);
        drawerLayout.requestLayout();
        drawerLayout.setScrimColor(Color.TRANSPARENT);
      }
      @Override
      public void onDrawerOpened(View drawerView){
        TextView text_view_prepaid_amount=(TextView)drawerView/*drawerLayout*/.findViewById(R.id.text_view_prepaid_amount);
        if(text_view_prepaid_amount!=null&&manager.getCurrentUser()!=null){
          text_view_prepaid_amount.setText(manager.getPrice(manager.getCurrentUser().prepaid_amount));
        }
        TextView text_view_currency=(TextView)drawerView/*drawerLayout*/.findViewById(R.id.text_view_currency);
        if(text_view_currency!=null&&manager.getActiveCurrency()!=null){
          String currency=manager.getActiveCurrencyTitle()!=null?manager.getActiveCurrencyTitle():manager.getActiveCurrency().name;
          text_view_currency.setText(currency);
        }

        //read additional drawer options
        initDrawerTab();
      }
      @Override
      public void onDrawerClosed(View drawerView){
        //write additional drawer options
        closeDrawerTab();
      }
      @Override
      public void onDrawerStateChanged(int newState){}
    });
  }
  private void initDrawerTab(){
    long selected_product_id=(manager.getSelectedProduct()!=null?manager.getSelectedProduct().id:-1);
    final TabLayout tab_layout=(TabLayout)findViewById(R.id.tab_layout);
    LinearLayout page_layout=(LinearLayout)findViewById(R.id.page_layout);
    if(!manager.isRegistered()||manager.getProductList()==null){
      if(tab_layout!=null)tab_layout.setVisibility(View.GONE);
      if(page_layout!=null)page_layout.setVisibility(View.GONE);
      return;
    }
    else if(manager.getProductList()!=null&&tab_layout!=null&&tab_layout.getTabCount()==1&&page_layout!=null){
      tab_layout.setVisibility(View.VISIBLE);
      page_layout.setVisibility(View.VISIBLE);
      Product product;
      for(int i=0;i<manager.getProductList().size();i++){
        product=manager.getProductList().get(i);
        if(product!=null){
          final TabLayout.Tab tab=tab_layout.newTab();
          tab.setText(manager.getProductName(product));
          tab.setTag(product);

          //picasso target
          Target target=new Target(){
            @Override
            public void onPrepareLoad(Drawable drawable){}
            @Override
            public void onBitmapLoaded(Bitmap bitmap,Picasso.LoadedFrom from){
              tab.setIcon(new BitmapDrawable(bitmap));
            }
            @Override
            public void onBitmapFailed(Drawable drawable){}
          };

          tab_layout.addTab(tab);
          if(product.picture!=null&&product.picture instanceof Drawable)tab.setIcon((Drawable)product.picture);
          else manager.loadImage(manager.getPictureUrl(T.IMAGE_PRODUCT)+product.id,target);
        }
      }

      //remove lefts
      tab_layout.removeTabAt(0);//extra for hide first item(default at the layout_calendar_dialog)

      //tab selecting
      tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
        @Override
        public void onTabSelected(TabLayout.Tab tab){
          View v=LayoutInflater.from(context).inflate(R.layout.layout_multiple_choice,null);
          ListView list_view=(ListView)v.findViewById(R.id.list_view);
          Product product=(Product)tab.getTag();
          if(product!=null){//need init list view
            String attr;
            ArrayList list=new ArrayList<String>();
            if(product.paramList!=null){
              for(int i=0;i<product.paramList.size();i++){
                attr=manager.getAttrByName(product.paramList.get(i).attrList,T.JSON_PARAM_ATTR_NAME_VALUE_TITLE);
                if(attr!=null)list.add(attr);else list.add(product.paramList.get(i).name);
              }
            }

            ArrayAdapter adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_multiple_choice,list);
            list_view.setAdapter(adapter);

            //param selected
            if(product.paramList!=null){
              for(int i=0;i<product.paramList.size();i++){
                if(Manager.CAR_RENTAL)product.paramList.get(i).checked=true;
                if(product.paramList.get(i).checked)list_view.setItemChecked(i, true);
              }
            }

            //can't change if order calculated
            if(manager.getOrderPrice()!=0)list_view.setEnabled(false);
            else list_view.setEnabled(true);

            //select product if not calculated order
            if(manager.getOrderPrice()==0)manager.setSelectedProduct(product);
          }
          LinearLayout page_layout=(LinearLayout)findViewById(R.id.page_layout);
          if(page_layout!=null)page_layout.addView(v);
        }
        @Override
        public void onTabUnselected(TabLayout.Tab tab){
          LinearLayout page_layout=(LinearLayout)findViewById(R.id.page_layout);
          if(page_layout!=null)page_layout.removeAllViews();
        }
        @Override
        public void onTabReselected(TabLayout.Tab tab){
          //empty tab, reselect
          if(tab.getCustomView()==null)this.onTabSelected(tab);
        }
      });
    }

    //selected product
    TabLayout.Tab tab;
    if(tab_layout==null)return;

    manager.removeListFragment();//if selected product changes, need to show in product list

    if(selected_product_id!=-1){
      for(int i=0;i<tab_layout.getTabCount();i++){
        tab=tab_layout.getTabAt(i);
        if(tab!=null&&tab.getTag()!=null){
          Product product=(Product)tab.getTag();
          if(product.id==selected_product_id){
            //selected tab
            if(tab_layout.getSelectedTabPosition()==i){
              //reset param
              ListView list_view=(ListView)findViewById(R.id.list_view);
              if(list_view!=null){
                checkParamSelected(list_view,product);//param selected
                //can't change if order calculated
                if(manager.getOrderPrice()!=0)list_view.setEnabled(false);
                else list_view.setEnabled(true);
              }
              else tab.select();
            }
            //not selected tab
            else tab.select();
            break;
          }
        }
      }
    }
    else{
      tab=tab_layout.getTabAt(0);
      if(tab!=null&&tab.getTag()!=null){
        tab.select();
      }
    }
  }
  private void closeDrawerTab(){
    ListView list_view=(ListView)findViewById(R.id.list_view);
    TabLayout tab_layout=(TabLayout)findViewById(R.id.tab_layout);
    if(list_view!=null&&tab_layout!=null){
      int tab_position=tab_layout.getSelectedTabPosition();
      TabLayout.Tab tab=tab_layout.getTabAt(tab_position);
      if(tab!=null&&tab.getTag()!=null){
        Product product=(Product)tab.getTag();
        saveParamSelected(list_view,product);
      }
    }
  }
  private void checkParamSelected(ListView list_view,Product product){
    if(list_view!=null&&product!=null&&product.paramList!=null){
      for(int j=0;j<product.paramList.size();j++){
        if(product.paramList.get(j).checked)list_view.setItemChecked(j,true);
        else list_view.setItemChecked(j,false);
      }
    }
  }
  private void saveParamSelected(ListView list_view,Product product){
    if(list_view!=null&&product!=null&&product.paramList!=null){
      for(int j=0;j<product.paramList.size();j++){
        if(list_view.isItemChecked(j))product.paramList.get(j).checked=true;
        else product.paramList.get(j).checked=false;
      }
    }
  }
  private void initFloatButton(){
    bookingFloatingActionButton=(FloatingActionButton)findViewById(R.id.floating_action_button_booking);
    if(bookingFloatingActionButton!=null){
      bookingFloatingActionButton.hide();
      /*bookingFloatingActionButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
        }
      });*/
      if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
        if(manager.getBookingX()!=0&&manager.getBookingY()!=0){
          bookingFloatingActionButton.setX(bookingFloatingActionButton.getX()+manager.getBookingX());
          bookingFloatingActionButton.setY(bookingFloatingActionButton.getY()+manager.getBookingY());
        }
      }
      forceMovingToFab(bookingFloatingActionButton,bookingCallbackHandler);
    }
    driverFloatingActionButton=(FloatingActionButton)findViewById(R.id.floating_action_button_driver);
    if(driverFloatingActionButton!=null){
      driverFloatingActionButton.hide();
      /*driverFloatingActionButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
        }
      });*/
      if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
        if(manager.getDriverX()!=0&&manager.getDriverY()!=0){
          driverFloatingActionButton.setX(driverFloatingActionButton.getX()+manager.getDriverX());
          driverFloatingActionButton.setY(driverFloatingActionButton.getY()+manager.getDriverY());
        }
      }
      forceMovingToFab(driverFloatingActionButton,driverCallbackHandler);
    }
    cartFloatingActionButton=(FloatingActionButton)findViewById(R.id.floating_action_button_cart);
    if(cartFloatingActionButton!=null){
      cartFloatingActionButton.hide();
      /*cartFloatingActionButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
        }
      });*/
      if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
        if(manager.getCartX()!=0&&manager.getCartY()!=0){
          cartFloatingActionButton.setX(cartFloatingActionButton.getX()+manager.getCartX());
          cartFloatingActionButton.setY(cartFloatingActionButton.getY()+manager.getCartY());
        }
      }
      forceMovingToFab(cartFloatingActionButton,cartCallbackHandler);
    }
    menuFloatingActionButton=(FloatingActionButton)findViewById(R.id.floating_action_button_menu);
    if(menuFloatingActionButton!=null){
      menuFloatingActionButton.hide();
      if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
        if(manager.getMenuX()!=0&&manager.getMenuY()!=0){
          menuFloatingActionButton.setX(menuFloatingActionButton.getX()+manager.getMenuX());
          menuFloatingActionButton.setY(menuFloatingActionButton.getY()+manager.getMenuY());
        }
      }
      forceMovingToFab(menuFloatingActionButton,menuCallbackHandler);
    }
  }
  //force moving feature to fab button (callback for click)
  private void forceMovingToFab(final FloatingActionButton fab,final TypedCallback callback){
    fab.setOnTouchListener(new View.OnTouchListener(){
      private float start_x,start_y;
      private float start_raw_x,start_raw_y;
      private float distance_x,distance_y;
      @Override
      public boolean onTouch(View view,MotionEvent event){
        switch(event.getActionMasked()){
          case MotionEvent.ACTION_DOWN:
            start_raw_x=event.getRawX();
            start_raw_y=event.getRawY();
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
              start_x=view.getX()-start_raw_x;
              start_y=view.getY()-start_raw_y;
            }
            break;
          case MotionEvent.ACTION_MOVE:
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
              view.setX(event.getRawX()+start_x);
              view.setY(event.getRawY()+start_y);
            }
            break;
          case MotionEvent.ACTION_UP:
            distance_x=event.getRawX()-start_raw_x;
            distance_y=event.getRawY()-start_raw_y;
            if(Math.abs(distance_x)<10&&Math.abs(distance_y)<10){
              if(callback!=null)callback.execute(null);
            }
            else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
              //save locations
              if(fab==bookingFloatingActionButton){
                manager.putBookingX(manager.getBookingX()+distance_x);manager.putBookingY(manager.getBookingY()+distance_y);
              }
              else if(fab==driverFloatingActionButton){
                manager.putDriverX(manager.getDriverX()+distance_x);manager.putDriverY(manager.getDriverY()+distance_y);
              }
              else if(fab==cartFloatingActionButton){
                manager.putCartX(manager.getCartX()+distance_x);manager.putCartY(manager.getCartY()+distance_y);
              }
              else if(fab==menuFloatingActionButton){
                manager.putMenuX(manager.getMenuX()+distance_x);manager.putMenuY(manager.getMenuY()+distance_y);
              }
            }
            break;
          case MotionEvent.ACTION_BUTTON_PRESS:
            break;
          default:
            return false;
        }
        return true;
      }
    });
  }
  //fab callback
  private TypedCallback bookingCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(manager!=null){
        if(manager.isTripMode()){//trip mode
          if(manager.getTripOrderId()!=-1&&manager.getTripOrderStatusId()>0)manager.cancelOrderDialog(manager.getTripOrderId(),null);
        }
        else{//booking mode
          if(manager.getOrderId()!=-1&&manager.getOrderStatusId()>0)manager.cancelOrderDialog(manager.getOrderId(),null);
          else{
            String currency=manager.getActiveCurrencyTitle()!=null?manager.getActiveCurrencyTitle():manager.getActiveCurrency().name;
            //delivery
            if(manager.getBookingStyle()==Manager.BOOKING_STYLE_DELIVERY){
              boolean is_preset=(manager.getFindAddress()!=null&&manager.getFindAddress().length()>0);
              String delivery=(is_preset?manager.getFindAddress():getString(R.string.app_place));
              manager.showFindFragment(R.layout.fragment_delivery,getString(R.string.order_delivery_address),R.drawable.marker_b,null,delivery,currency);
            }
            //trip
            else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_TRIP)
              manager.showFindFragment(R.layout.fragment_find,getString(R.string.order_find_address),R.drawable.marker_b,null,null,currency);
            //easyAB
            else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB)
              manager.showFindFragment(R.layout.fragment_places,null,-1,manager.getPickupAddress(),manager.getDropoffAddress(),currency);
          }
        }
      }
    }
  };
  private TypedCallback driverCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(manager!=null){
        if(manager.getDriverTransportId()!=-1){
          if(manager.getMapMode()==Manager.MAP_CLIENT)manager.startDriverDialog();
          else if(manager.getMapMode()==Manager.MAP_DRIVER)manager.stopDriverDialog();
        }
      }
    }
  };
  private TypedCallback cartCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(manager!=null){
        if(manager.getCartList()!=null)manager.showCartFragmentCallbackHandler.execute(manager.getCartList());
        else{
          if(manager.getProductList()!=null&&manager.getProductList().size()>0){
            if(manager.getFragment(T.FRAGMENT_NAME_PRODUCT_LIST)==null)manager.showProductListCallbackHandler.execute(manager.newInstanceListWithOptions(manager.getProductList(),true));
          }else manager.getProductCallbackHandler.execute(new Integer(0));//page_num
        }
      }
    }
  };
  private TypedCallback menuCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      drawerLayout.openDrawer(GravityCompat.START);
    }
  };
  //button in center
  private void updateButtonProductVisibility(boolean visibility){
    Button button=(Button)findViewById(R.id.button_product);
    if(button!=null)button.setVisibility(visibility?View.VISIBLE:View.INVISIBLE);
  }
  //showSnackBar(v,"message to snack",R.string.button_upper_cancel,callback,callback_obj);
  private void showSnackBar(View view,final String msg,int resId,final TypedCallback callback,final Object callback_obj){
    Snackbar.make(view,msg,Snackbar.LENGTH_LONG).setAction(resId,new View.OnClickListener(){
      @Override
      public void onClick(View v){
        if(callback!=null)callback.execute(callback_obj);
      }
    }).show();
  }
  public void onListFragmentInteraction(ListContent.Item item){
    if(manager.DEBUG)Toast.makeText(getApplicationContext(),item.toString(),Toast.LENGTH_SHORT).show();
    if(item instanceof ProductListContent.Item){
      if(!manager.isOpenFragment(T.FRAGMENT_NAME_ORDER_LIST)&&
         !manager.isOpenFragment(T.FRAGMENT_NAME_CART_LIST)){//No check for order&cart
        Product product=((ProductListContent.Item)item).getProduct();
        if(manager.getBookingStyle()==Manager.BOOKING_STYLE_TRIP||manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB){
          if(product.checked){//select one
            manager.updateProductForOrder(item.id);
            if(manager.getBookingStyle()==Manager.BOOKING_STYLE_TRIP)manager.removeFragment(T.FRAGMENT_NAME_PRODUCT_LIST);
            if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY_AB&&manager.getCurrentDirection()!=null)
              manager.updateFindFragmentCallbackHandler.execute(manager.getCurrentDirection());
          }
        }
        else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_DELIVERY){
          if(product.checked){//add one
            //saving in cart
            if(manager.getCartList()==null)manager.newInstanceCartList();

            if(product.cart_count==0)product.cart_count++;

            Product product_clone=null;
            try{
              product_clone=product.clone();
            }catch(CloneNotSupportedException cns_e){}

            if(product_clone!=null){
              if(product_clone.paramList!=null){
                ProductParam product_param;
                ArrayList<ProductParam> remove_list=new ArrayList();
                Iterator iter=product_clone.paramList.iterator();
                while(iter.hasNext()){
                  product_param=(ProductParam)iter.next();
                  if(product_param!=null&&!product_param.checked)remove_list.add(product_param);
                }
                product_clone.paramList.removeAll(remove_list);
              }
              manager.getCartList().add(product_clone);
            }
          }
          else{//remove all
            product.cart_count=0;
            if(manager.getCartList()!=null){
              Product find_product;
              ArrayList<Product> product_list=manager.getCartList();
              ArrayList<Product> remove_list=new ArrayList();
              Iterator iter=product_list.iterator();
              while(iter.hasNext()){
                find_product=(Product)iter.next();
                if(find_product.id==product.id)remove_list.add(find_product);
              }
              product_list.removeAll(remove_list);
            }
          }
          //preview cart fragment
          manager.showCartFragmentCallbackHandler.execute(manager.getCartList());
        }
        else if(manager.getBookingStyle()==Manager.BOOKING_STYLE_EASY){
          manager.setSelectedProduct(product);
          if(manager.getCurrentDirection()!=null){//order & total price calc
            manager.calcOrderPrice(manager.getCurrentDirection(),manager.getSelectedProduct());
          }
        }
      }
    }
    else if(item instanceof OrderABListContent.Item){
      OrderAB order=((OrderABListContent.Item)item).getOrderAB();
      if(manager.getMapMode()==manager.MAP_DRIVER&&(order!=null&&order.transport_id==-1/*no_driver*/)){
        manager.takeOrderDialog(order.id,manager.getDriverTransportId(),order.user_id,null);
      }
      else if(order!=null&&!manager.isOpenFragment(T.FRAGMENT_NAME_PRODUCT_LIST)){//No productList mode
        manager.setSelectedProduct(null);
        if(order.status_id==OrderABStatus.ORDER_STATUS_COMPLETED){
          String transport=(order.transport!=null?order.transport.name+T.SPACE+order.transport.license_plate:T.EMPTY);
          manager.getTransportReviewRequest(order.id,order.user_id,order.transport_id,transport);//client review driver's trip(transport)
        }
        else{
          if(order.purchase==null){//last payment is not found
            //payment Dialog
            manager.payOrderDialog(order.id,order.total_price);
          }
          else{
            if(order.orderABPartList!=null)manager.showProductListCallbackHandler.execute(order.orderABPartList);
            else manager.getOrderABPartRequest(manager.showProductListCallbackHandler,order);
          }
        }
      }
      //manager.removeFragment(T.FRAGMENT_NAME_ORDER_LIST);
    }
  }
  public TypedCallback onAddFragmentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(manager.getBookingStyle()==Manager.BOOKING_STYLE_DELIVERY)updateButtonProductVisibility(false);
    }
  };
  public TypedCallback onNullFragmentCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      if(manager.getBookingStyle()==Manager.BOOKING_STYLE_DELIVERY)updateButtonProductVisibility(true);
    }
  };
}