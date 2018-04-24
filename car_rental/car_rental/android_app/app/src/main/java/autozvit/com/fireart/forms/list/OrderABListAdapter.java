package autozvit.com.fireart.forms.list;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import autozvit.com.fireart.R;
import autozvit.com.fireart.forms.ListFragment.OnListFragmentInteractionListener;
import autozvit.com.fireart.forms.StartActivity;
import autozvit.com.fireart.forms.list.OrderABListContent.Item;
import autozvit.com.fireart.objects.Currency;
import autozvit.com.fireart.objects.OrderAB;
import autozvit.com.fireart.objects.OrderABStatus;
import autozvit.com.fireart.objects.Transport;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.tools.T;

public class OrderABListAdapter extends RecyclerView.Adapter<OrderABListAdapter.ViewHolder>{
  private Context context;
  public void setContext(Context context){this.context=context;}
  public Context getContext(){return context;}
  private List<Item> itemList;
  public List<Item> getItemList(){return itemList;}
  private OnListFragmentInteractionListener listener;
  private int layout;
  public void setLayout(int layout){this.layout=layout;}
  private String pictureUrl;
  public void setPictureUrl(String url){pictureUrl=url;}
  public String getPictureUrl(){return pictureUrl;}
  private boolean checking=false;//driver can select order(for trip): cancel button not enabled, visible switch
  public void setChecking(boolean checking){this.checking=checking;}
  public boolean isChecking(){return checking;}
  private Handler handler;
  public OrderABListAdapter(List<Item> items,OnListFragmentInteractionListener listener,Context context,int layout,String picture_url,boolean checking){
    itemList=items;
    this.listener=listener;
    this.context=context;
    this.layout=layout;
    pictureUrl=picture_url;
    this.checking=checking;
    handler=new Handler();
  }
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
    View view=LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
    return new ViewHolder(view);
  }
  @Override
  public void onBindViewHolder(final ViewHolder holder,int position){
    holder.item=itemList.get(position);
    holder.view.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v){
        if(listener!=null){
          // Notify the active callbacks interface (the activity, if the
          // fragment is attached to one) that an item has been selected.
          listener.onListFragmentInteraction(holder.item);
        }
      }
    });
    //here init fields
    String str,currency_str;
    final Manager manager=((StartActivity)context).getManager();
    final OrderAB order=holder.item.getOrderAB();
    Currency currency=holder.item.getCurrency();
    Transport transport=order.transport;
    /*str=order.order_address;
    holder.editTextA.setText(str!=null&&str.length()>0?str:context.getString(R.string.no_address));*/
    str=order.delivery_address;
    holder.editTextB.setText(str!=null&&str.length()>0?str:null/*context.getString(R.string.no_address)*/);

    str=manager.getPrice(order.total_price);
    if(str!=null)holder.textViewPrice.setText(str);
    currency_str=manager.getActiveCurrencyTitle()!=null?manager.getActiveCurrencyTitle():currency.name;
    holder.textViewCurrency.setText(currency_str);

    if(order.route_distance>0){
      str=manager.getDistance(order);
      holder.textViewDistance.setText(str);
    }
    else{
      holder.textViewDistance.setText(null);
      holder.textViewDistance.setVisibility(View.INVISIBLE);
      holder.textViewDistance.invalidate();
    }

    //switch
    if(checking/*manager.getMapMode()==Manager.MAP_DRIVER*/){

      if(manager.getDriverTransportId()==order.transport_id)holder.switchTake.setChecked(true);
      else if(order.transport_id!=-1){holder.switchTake.setChecked(true);holder.switchTake.setEnabled(false);}

      holder.switchTake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
          if(order.transport_id==-1&&isChecked)manager.takeOrderDialog(order.id,manager.getDriverTransportId(),order.user_id,holder.switchTake);
          else if(order.transport_id!=-1&&!isChecked)manager.cancelOrderDialog(order.id,holder.switchTake);
        }
      });
      /*holder.switchTake.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view){
          if(((SwitchCompat)view).isChecked())manager.takeOrderDialog(order.id,manager.getDriverTransportId(),holder.switchTake);
          else manager.cancelOrderDialog(order.id,holder.switchTake);
        }
      });*/
    }
    else holder.switchTake.setVisibility(View.INVISIBLE);

    if(transport!=null){
      str=transport.name;
      holder.textViewTransportName.setText(str!=null&&str.length()>0?str:null/*context.getString(R.string.no_transport)*/);
      str=transport.license_plate;
      holder.textViewLicensePlate.setText(str!=null&&str.length()>0?str:null/*context.getString(R.string.no_license_plate)*/);
    }
    else{
      holder.textViewTransportName.setText(null/*context.getString(R.string.no_transport)*/);
      holder.textViewLicensePlate.setText(null/*context.getString(R.string.no_license_plate)*/);
    }

    holder.textViewCreateDate.setText(manager.getTalkingDatetimeMessage(order.create_date));
    holder.textViewId.setText(String.valueOf(holder.item.id));//order.id

    str=manager.getStatusName(order);
    holder.textViewOrderStatus.setText(str.toUpperCase());

    if(order.status_id<0){
      holder.textViewOrderStatus.setBackgroundColor(context.getResources().getColor(R.color.label_canceled));
      holder.buttonCancelTrip.setTextColor(context.getResources().getColor(R.color.DarkGray));
      holder.buttonCancelTrip.setEnabled(false);
    }
    else if(order.status_id>1&&order.status_id<OrderABStatus.ORDER_STATUS_DELIVERING){
      holder.textViewOrderStatus.setBackgroundColor(context.getResources().getColor(R.color.label_processed));
    }
    else if(order.status_id>=OrderABStatus.ORDER_STATUS_DELIVERING){
      holder.textViewOrderStatus.setBackgroundColor(context.getResources().getColor(R.color.label_completed));
      holder.buttonCancelTrip.setTextColor(context.getResources().getColor(R.color.DarkGray));
      holder.buttonCancelTrip.setEnabled(false);
    }
    else holder.textViewOrderStatus.setBackgroundColor(context.getResources().getColor(R.color.label_accepted));

    if(checking){//not cancel
      holder.buttonCancelTrip.setTextColor(context.getResources().getColor(R.color.DarkGray));
      holder.buttonCancelTrip.setEnabled(false);
    }

    //button_order_service listener
    holder.buttonOrderService.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view){
        if(manager.isOpenFragment(T.FRAGMENT_NAME_PRODUCT_LIST))return;
        manager.setSelectedProduct(null);
        manager.setTempOrderAB(order);
        if(order.orderABPartList!=null)manager.showProductListCallbackHandler.execute(order.orderABPartList);
        else manager.getOrderABPartRequest(manager.showProductListCallbackHandler,order);
      }
    });

    //button_cancel_trip listener
    if(order.status_id>0&&order.status_id<5){
      holder.buttonCancelTrip.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view){
          manager.cancelOrderDialog(order.id,null);
        }
      });
    }

    // picture loading ...
    if(pictureUrl!=null&&holder.imageViewTransportPicture!=null&&transport!=null){
      if(transport.picture!=null&&transport.picture instanceof Drawable)
        holder.imageViewTransportPicture.setImageDrawable((Drawable)transport.picture);
      else manager.loadImage(pictureUrl+transport.id,holder.imageViewTransportPicture,transport);
    }

    //purchase calculation
    double amount=manager.calcPurchase(order.purchaseList);
    if(amount>0) {
      holder.textViewPaymentAmont.setText(String.format(Manager.PAYMENT_AMOUNT, context.getString(R.string.payment_amount), manager.getPrice(amount), currency_str));
      holder.textViewPaymentAmont.setVisibility(View.VISIBLE);
      holder.textViewPaymentAmont.invalidate();
    }
  }
  @Override
  public int getItemCount(){return itemList.size();}

  public class ViewHolder extends RecyclerView.ViewHolder{
    public View view;
    public Item item;

    public TextView textViewId=null;
    public TextView textViewOrderStatus=null;
    //public EditText editTextA=null;
    public EditText editTextB=null;

    public SwitchCompat switchTake=null;

    public ImageView imageViewTransportPicture=null;
    public TextView textViewPrice=null;
    public TextView textViewCurrency=null;
    public TextView textViewDistance=null;
    public TextView textViewTransportName=null;
    public TextView textViewLicensePlate=null;
    public TextView textViewCreateDate=null;
    public TextView textViewPaymentAmont=null;

    public Button buttonOrderService=null;
    public Button buttonCancelTrip=null;

    public ViewHolder(View view){
      super(view);
      this.view=view;
      if(view!=null){
        textViewId=(TextView)view.findViewById(R.id.text_view_id);
        textViewOrderStatus=(TextView)view.findViewById(R.id.text_view_order_status);
        //editTextA=(EditText)view.findViewById(R.id.edit_text_A);
        editTextB=(EditText)view.findViewById(R.id.edit_text_B);
        switchTake=(SwitchCompat)view.findViewById(R.id.switch_take);
        imageViewTransportPicture=(ImageView)view.findViewById(R.id.image_view_transport_picture);
        textViewPrice=(TextView)view.findViewById(R.id.text_view_price);
        textViewCurrency=(TextView)view.findViewById(R.id.text_view_currency);
        textViewDistance=(TextView)view.findViewById(R.id.text_view_distance);
        textViewTransportName=(TextView)view.findViewById(R.id.text_view_transport_name);
        textViewLicensePlate=(TextView)view.findViewById(R.id.text_view_license_plate);
        textViewCreateDate=(TextView)view.findViewById(R.id.text_view_create_date);
        textViewPaymentAmont=(TextView)view.findViewById(R.id.text_view_payment_amount);
        buttonOrderService=(Button)view.findViewById(R.id.button_order_service);
        buttonCancelTrip=(Button)view.findViewById(R.id.button_cancel_trip);
      }
    }

    @Override
    public String toString(){return super.toString();}
  }

  private void toast(final String message){
    handler.post(new Runnable(){
      @Override
      public void run(){
        Toast.makeText(context.getApplicationContext(),message,Toast.LENGTH_LONG).show();
      }});
  }
}