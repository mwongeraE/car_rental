package autozvit.com.fireart.forms;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import autozvit.com.fireart.R;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.tools.Map;
import autozvit.com.fireart.tools.T;

public class FindFragment extends Fragment{
  private static final String ARG_LAYOUT="layout";
  private static final String ARG_TIP_MESSAGE="tip_message";
  private static final String ARG_PICTURE_ID="picture_id";
  private static final String ARG_PICKUP_ADDRESS="pickup_address";
  private static final String ARG_DESTINATION_ADDRESS="destination_address";
  private static final String ARG_CURRENCY="currency";

  private int layout=0;
  private String tipMessage;//tip message
  private int pictureId;//picture id
  private String pickupAddress;//pickup address
  private String destinationAddress;//destination address
  private String currency;

  private String pickupName,destinationName;

  private View view;
  private Context context;
  private Manager manager;

  private TextView textViewTip;
  private ImageView imageView;
  private EditText editTextPickupAddress;
  private EditText editTextDestinationAddress;

  public static FindFragment newInstance(int layout,String tip_message,int picture_id,String pickup_address,String destination_address,String currency){
    FindFragment fragment=new FindFragment();
    Bundle args=new Bundle();
    args.putInt(ARG_LAYOUT,layout);
    args.putString(ARG_TIP_MESSAGE,tip_message);
    args.putInt(ARG_PICTURE_ID,picture_id);
    args.putString(ARG_PICKUP_ADDRESS,pickup_address);
    args.putString(ARG_DESTINATION_ADDRESS,destination_address);
    args.putString(ARG_CURRENCY,currency);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      layout=getArguments().getInt(ARG_LAYOUT);
      tipMessage=getArguments().getString(ARG_TIP_MESSAGE);
      pictureId=getArguments().getInt(ARG_PICTURE_ID);
      pickupAddress=getArguments().getString(ARG_PICKUP_ADDRESS);
      destinationAddress=getArguments().getString(ARG_DESTINATION_ADDRESS);
      currency=getArguments().getString(ARG_CURRENCY);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(layout,container,false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    context=getActivity();
    manager=((StartActivity)context).getManager();
    if(manager.getBookingStyle()!=Manager.BOOKING_STYLE_EASY&&
       manager.getBookingStyle()!=Manager.BOOKING_STYLE_EASY_AB)
      ((StartActivity)context).getBookingFloatingActionButton().hide();//setVisibility(View.INVISIBLE);

    view=getView();
    if(view!=null){
      if(layout==R.layout.fragment_places){//fragment_places
        final TextView text_view_pickup_address=(TextView)view.findViewById(R.id.text_view_pickup_address);
        final TextView text_view_dropoff_address=(TextView)view.findViewById(R.id.text_view_dropoff_address);
        View pickup_view=view.findViewById(R.id.place_autocomplete_fragment_pickup);
        View dropoff_view=view.findViewById(R.id.place_autocomplete_fragment_dropoff);
        if(pickup_view!=null){
          final View clear_button=pickup_view.findViewById(R.id.place_autocomplete_clear_button);
          View search_button=pickup_view.findViewById(R.id.place_autocomplete_search_button);
          if(search_button!=null)search_button.setVisibility(View.GONE);
          final EditText edit_text=(EditText)pickup_view.findViewById(R.id.place_autocomplete_search_input);
          //if write pickup name and address, must set marker
          //if(edit_text!=null)edit_text.setHint(R.string.pickup_address);*/
          //if(text_view_pickup_address!=null&&pickupAddress!=null)text_view_pickup_address.setText(pickupAddress);

          edit_text.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v,MotionEvent me){
              ((StartActivity)context).getManager().removeFragment(T.FRAGMENT_NAME_PRODUCT);
              return false;
            }
          });

          if(edit_text!=null&&manager.getPickupName()!=null&&manager.getPickupName().length()>0){
            edit_text.setText(manager.getPickupName());
            if(clear_button!=null)clear_button.setVisibility(View.VISIBLE);
          }
          if(text_view_pickup_address!=null&&manager.getPickupAddress()!=null&&manager.getPickupAddress().length()>0)
            text_view_pickup_address.setText(manager.getPickupAddress());
          if(manager.getOrderLatitude()!=null&&manager.getOrderLongitude()!=null){
            //map preview
            Fragment frag=manager.getFragment(T.FRAGMENT_NAME_MAP);
            if(frag!=null){
              Map.Location location=new Map().newLocationInstance(Double.parseDouble(manager.getOrderLatitude()),Double.parseDouble(manager.getOrderLongitude()));
              //not show pickup marker (use static marker on map)
              //((Map2Fragment)frag).addPickupMarker(location);
              ((Map2Fragment)frag).toLocation(location);
              ((Map2Fragment)frag).setCenterLocation(location);//for not re-calculate address
            }
          }

          //Filter: Place.TYPE_COUNTRY, PlaceAutocomplete.MODE_FULLSCREEN, AutocompleteFilter.TYPE_FILTER_ADDRESS
          SupportPlaceAutocompleteFragment place_frag=(SupportPlaceAutocompleteFragment)getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_pickup);
          //AutocompleteFilter type_filter=new AutocompleteFilter.Builder().setCountry("UK").build();
          //place_frag.setFilter(type_filter);
          //select by location
          /*
          place_frag.setBoundsBias(new LatLngBounds(
                                   new LatLng(-33.880490, 151.184363),
                                   new LatLng(-33.858754, 151.229596)));
          */
          //filter by country

          AutocompleteFilter typeFilter=new AutocompleteFilter.Builder().setCountry("ARE").build();
          place_frag.setFilter(typeFilter);

          place_frag.setOnPlaceSelectedListener(new PlaceSelectionListener(){
            @Override
            public void onPlaceSelected(Place place){
              if(place!=null){
                if(text_view_pickup_address!=null)text_view_pickup_address.setText(place.getAddress());
                //save to pickup
                manager.putPickupName(place.getName().toString());
                manager.putPickupAddress(place.getAddress().toString());
                manager.putOrderLatitude(String.valueOf(place.getLatLng().latitude));
                manager.putOrderLongitude(String.valueOf(place.getLatLng().longitude));
                manager.getEditor().commit();
                //map preview
                Fragment frag=manager.getFragment(T.FRAGMENT_NAME_MAP);
                if(frag!=null){
                  Map.Location location=new Map().newLocationInstance(place.getLatLng().latitude,place.getLatLng().longitude);
                  //not show pickup marker (use static marker on map)
                  //((Map2Fragment)frag).addPickupMarker(location);
                  ((Map2Fragment)frag).toLocation(location);
                  ((Map2Fragment)frag).setCenterLocation(location);//for not re-calculate address
                }

                //need calculate trip! (if dropoff is present)
                clearPriceAndHide();
                manager.clearOrderCalcValues();
                if(manager.getDeliveryLatitude()!=null&&manager.getDeliveryLongitude()!=null&&
                   manager.getPickupName()!=null&&pickupName.length()>0)routeCalculation();
              }
              if(manager.DEBUG)Toast.makeText(context.getApplicationContext(),place.getAddress(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Status status){}
          });
          clear_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              if(edit_text!=null)edit_text.setText(null);
              if(text_view_pickup_address!=null)text_view_pickup_address.setText(getString(R.string.pickup_address));
              if(clear_button!=null)clear_button.setVisibility(View.GONE);
              //clear pickup
              manager.clearPickup();
              //map preview
              //not show pickup marker (use static marker on map)
              /*Fragment frag=manager.getFragment(T.FRAGMENT_NAME_MAP);
              if(frag!=null){
                ((Map2Fragment)frag).clearTripMarkerA();
              }*/

              //clear price
              clearPriceAndHide();
              manager.clearOrderCalcValues();

              ((StartActivity)context).getManager().removeFragment(T.FRAGMENT_NAME_PRODUCT);
            }
          });
        }
        if(dropoff_view!=null){
          final View clear_button=dropoff_view.findViewById(R.id.place_autocomplete_clear_button);
          View search_button=dropoff_view.findViewById(R.id.place_autocomplete_search_button);
          if(search_button!=null)search_button.setVisibility(View.GONE);
          final EditText edit_text=(EditText)dropoff_view.findViewById(R.id.place_autocomplete_search_input);
          //if write pickup name and address, must set marker
          //if(edit_text!=null)edit_text.setHint(R.string.dropoff_address);*/
          //if(text_view_dropoff_address!=null&&destinationAddress!=null)text_view_dropoff_address.setText(destinationAddress);

          edit_text.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v,MotionEvent me){
              ((StartActivity)context).getManager().removeFragment(T.FRAGMENT_NAME_PRODUCT);
              return false;
            }
          });

          if(edit_text!=null&&manager.getDropoffName()!=null&&manager.getDropoffName().length()>0){
            edit_text.setText(manager.getDropoffName());
            if(clear_button!=null)clear_button.setVisibility(View.VISIBLE);
          }
          if(text_view_dropoff_address!=null&&manager.getDropoffAddress()!=null&&manager.getDropoffAddress().length()>0)
            text_view_dropoff_address.setText(manager.getDropoffAddress());
          if(manager.getDeliveryLatitude()!=null&&manager.getDeliveryLongitude()!=null){
            Fragment frag=manager.getFragment(T.FRAGMENT_NAME_MAP);
            if(frag!=null){
              Map.Location location=new Map().newLocationInstance(Double.parseDouble(manager.getDeliveryLatitude()),Double.parseDouble(manager.getDeliveryLongitude()));
              ((Map2Fragment)frag).addDropoffMarker(location);
            }
          }

          SupportPlaceAutocompleteFragment place_frag=(SupportPlaceAutocompleteFragment)getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_dropoff);
          AutocompleteFilter typeFilter=new AutocompleteFilter.Builder().setCountry("ARE").build();
          place_frag.setFilter(typeFilter);

          place_frag.setOnPlaceSelectedListener(new PlaceSelectionListener(){
            @Override
            public void onPlaceSelected(Place place){
              if(place!=null){
                if(text_view_dropoff_address!=null)text_view_dropoff_address.setText(place.getAddress());
                //save to dropoff
                manager.putDropoffName(place.getName().toString());
                manager.putDropoffAddress(place.getAddress().toString());
                manager.putDeliveryLatitude(String.valueOf(place.getLatLng().latitude));
                manager.putDeliveryLongitude(String.valueOf(place.getLatLng().longitude));
                manager.getEditor().commit();
                //map preview
                Fragment frag=manager.getFragment(T.FRAGMENT_NAME_MAP);
                if(frag!=null){
                  Map.Location location=new Map().newLocationInstance(place.getLatLng().latitude,place.getLatLng().longitude);
                  ((Map2Fragment)frag).addDropoffMarker(location);
                  //not show dropoff location
                  /*if(manager.getOrderLatitude()!=null&&manager.getOrderLongitude()!=null){
                    ((Map2Fragment)frag).toLocation(Double.valueOf(manager.getOrderLatitude()),Double.valueOf(manager.getOrderLongitude()),
                                                    location.latitude,location.longitude);
                  }
                  else ((Map2Fragment)frag).toLocation(location);*/
                }

                //need calculate trip! (if pickup is present)
                clearPriceAndHide();
                manager.clearOrderCalcValues();
                if(manager.getOrderLatitude()!=null&&manager.getOrderLongitude()!=null&&
                   manager.getPickupName()!=null&&pickupName.length()>0)routeCalculation();
              }
              if(manager.DEBUG)Toast.makeText(context.getApplicationContext(),place.getAddress(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Status status){}
          });
          clear_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              if(edit_text!=null)edit_text.setText(null);
              if(text_view_dropoff_address!=null)text_view_dropoff_address.setText(getString(R.string.dropoff_address));
              if(clear_button!=null)clear_button.setVisibility(View.GONE);
              //clear dropoff
              manager.clearDropoff();
              //map preview
              Fragment frag=manager.getFragment(T.FRAGMENT_NAME_MAP);
              if(frag!=null){
                ((Map2Fragment)frag).clearTripMarkerB();
              }

              //clear price
              clearPriceAndHide();
              manager.clearOrderCalcValues();

              ((StartActivity)context).getManager().removeFragment(T.FRAGMENT_NAME_PRODUCT);
            }
          });
        }
        //currency
        TextView text_view_currency=(TextView)view.findViewById(R.id.text_view_currency);
        if(text_view_currency!=null){
          if(currency!=null)text_view_currency.setText(currency);
          else text_view_currency.setText(T.EMPTY);
        }
        //re-calculate price
        if(manager.DEBUG)Toast.makeText(context.getApplicationContext(),
        "Order latLng="+manager.getOrderLatitude()+", "+manager.getOrderLongitude()+
             "Delivery latLng="+manager.getDeliveryLatitude()+", "+manager.getDeliveryLongitude(),Toast.LENGTH_SHORT).show();

        if(manager.getOrderLatitude()!=null&&manager.getOrderLongitude()!=null&&
           manager.getDeliveryLatitude()!=null&&manager.getDeliveryLongitude()!=null){
          routeCalculation();
        }
      }
      else if(layout==R.layout.fragment_dest){//fragment_dest
        final TextView text_view_dropoff_address=(TextView)view.findViewById(R.id.text_view_dropoff_address);
        View dropoff_view=view.findViewById(R.id.place_autocomplete_fragment_dropoff);
        if(dropoff_view!=null){
          final View clear_button=dropoff_view.findViewById(R.id.place_autocomplete_clear_button);
          View search_button=dropoff_view.findViewById(R.id.place_autocomplete_search_button);
          if(search_button!=null)search_button.setVisibility(View.GONE);
          final EditText edit_text=(EditText)dropoff_view.findViewById(R.id.place_autocomplete_search_input);
          //if(edit_text!=null)edit_text.setHint(R.string.dropoff_address);*/
          //if(text_view_dropoff_address!=null&&destinationAddress!=null)text_view_dropoff_address.setText(destinationAddress);
          SupportPlaceAutocompleteFragment place_frag=(SupportPlaceAutocompleteFragment)getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_dropoff);
          AutocompleteFilter typeFilter=new AutocompleteFilter.Builder().setCountry("ARE").build();
          place_frag.setFilter(typeFilter);

          place_frag.setOnPlaceSelectedListener(new PlaceSelectionListener(){
            @Override
            public void onPlaceSelected(Place place){
              if(place!=null){
                if(text_view_dropoff_address!=null)text_view_dropoff_address.setText(place.getAddress());
                manager.googleGeocode(place.getAddress().toString(),manager.showGeocodeWithDropoffRouteCalcCallbackHandler);
              }
            }
            @Override
            public void onError(Status status){}
          });
          clear_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              if(edit_text!=null)edit_text.setText(null);
              if(text_view_dropoff_address!=null)text_view_dropoff_address.setText(getString(R.string.dropoff_address));
              if(clear_button!=null)clear_button.setVisibility(View.GONE);
              //clear dropoff and order calc
              manager.clearDropoff();
              manager.clearOrderCalcValues();
              //map preview
              Fragment frag=manager.getFragment(T.FRAGMENT_NAME_MAP);
              if(frag!=null){
                ((Map2Fragment)frag).clearTripMarkerB();
              }
              //dropoff fragment
              frag=manager.getFragment(T.FRAGMENT_NAME_DROPOFF);
              if(frag!=null){
                ((DropoffFragment)frag).updateDropoffNameAndAddress(null,null);
                ((DropoffFragment)frag).updateDuration(Manager.NO_TIME);
                ((DropoffFragment)frag).updateDistance(Manager.ZERO);
                ((DropoffFragment)frag).updatePrice(Manager.NO_MONEY);
              }
            }
          });
        }
      }
      else{//others layout_calendar_dialog (fragment_find)
        textViewTip=(TextView)view.findViewById(R.id.text_view_tip);
        if(textViewTip!=null&&tipMessage!=null)textViewTip.setText(tipMessage);
        imageView=(ImageView)view.findViewById(R.id.image_view_picture);
        if(imageView!=null&&pictureId!=-1)imageView.setImageResource(pictureId);
        editTextPickupAddress=(EditText)view.findViewById(R.id.edit_text_pickup_address);
        if(editTextPickupAddress!=null&&pickupAddress!=null)editTextPickupAddress.setText(pickupAddress);
        editTextDestinationAddress=(EditText)view.findViewById(R.id.edit_text_destination_address);
        if(editTextDestinationAddress!=null){
          if(destinationAddress!=null)editTextDestinationAddress.setText(destinationAddress);
          editTextDestinationAddress.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            public boolean onEditorAction(TextView v,int action_id,KeyEvent event){
              if(action_id==EditorInfo.IME_ACTION_SEND){
                //start find (click on image_view_picture)
                ((StartActivity)context).onClick(imageView);
              }
              return false;
            }
          });
        }
      }

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

  @Override
  public void onDestroy(){
    super.onDestroy();
    if(((StartActivity)context).getManager().getBookingStyle()!=Manager.BOOKING_STYLE_EASY&&
       ((StartActivity)context).getManager().getBookingStyle()!=Manager.BOOKING_STYLE_EASY_AB)
      ((StartActivity)context).getBookingFloatingActionButton().show();//.setVisibility(View.VISIBLE);
  }

  /*tools*/
  public String getPickupAddress(){
    View view=getView();
    if(view!=null){
      EditText editText=(EditText)view.findViewById(R.id.edit_text_pickup_address);
      if(editText!=null)return editText.getText().toString();
    }
    return null;
  }
  public String getDestinationAddress(){
    View view=getView();
    if(view!=null){
      EditText editText=(EditText)view.findViewById(R.id.edit_text_destination_address);
      if(editText!=null)return editText.getText().toString();
    }
    return null;
  }
  public void updatePickupNameAndAddress(String name,String address){
    pickupName=name;pickupAddress=address;
    TextView text_view_pickup_address=(TextView)view.findViewById(R.id.text_view_pickup_address);
    if(text_view_pickup_address!=null){
      if(pickupAddress!=null&&pickupAddress.length()!=0)text_view_pickup_address.setText(pickupAddress);
      else text_view_pickup_address.setText(getString(R.string.pickup_address));
    }
    SupportPlaceAutocompleteFragment place_frag=(SupportPlaceAutocompleteFragment)getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_pickup);
    if(place_frag!=null){
      if(pickupName!=null&&pickupName.length()!=0)place_frag.setText(pickupName);
      else place_frag.setText(T.EMPTY);
    }
  }
  public void updatePrice(String price){
    TextView text_view_price=(TextView)view.findViewById(R.id.text_view_price);
    if(text_view_price!=null){
      if(price!=null)text_view_price.setText(price);
      else text_view_price.setText(T.EMPTY);
    }
  }
  public void clearPriceAndHide(){
    RelativeLayout relative_layout_addon=(RelativeLayout)view.findViewById(R.id.relative_layout_addon);
    if(relative_layout_addon!=null)relative_layout_addon.setVisibility(View.GONE);
    updatePrice(null);
  }
  public void routeCalculation(){
    RelativeLayout relative_layout_addon=(RelativeLayout)view.findViewById(R.id.relative_layout_addon);
    if(relative_layout_addon!=null)relative_layout_addon.setVisibility(View.VISIBLE);
    manager.googleDirections(Manager.DIRECTIONS_ROUTE_LOCATION,manager.updateFindFragmentCallbackHandler);
  }
}