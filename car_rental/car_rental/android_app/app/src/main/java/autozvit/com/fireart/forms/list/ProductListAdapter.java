package autozvit.com.fireart.forms.list;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.app.AlertDialog;
//import android.support.v7.app.AlertDialog;//accent colors
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import autozvit.com.fireart.R;
import autozvit.com.fireart.forms.ListFragment.OnListFragmentInteractionListener;
import autozvit.com.fireart.forms.StartActivity;
import autozvit.com.fireart.forms.list.ProductListContent.Item;
import autozvit.com.fireart.objects.Attr;
import autozvit.com.fireart.objects.Currency;
import autozvit.com.fireart.objects.OrderAB;
import autozvit.com.fireart.objects.OrderABPart;
import autozvit.com.fireart.objects.Product;
import autozvit.com.fireart.objects.Type;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.tools.T;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>{
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
  private boolean checking=false;//client can select product parameters(for order)
  public void setChecking(boolean checking){this.checking=checking;}
  public boolean isChecking(){return checking;}
  private Handler handler;
  private Product selectedProduct/*,defaultProduct*/;
  private OrderAB order=null;
  public ProductListAdapter(List<Item> items,OnListFragmentInteractionListener listener,Context context,int layout,String picture_url,boolean checking){
    itemList=items;
    this.listener=listener;
    this.context=context;
    this.layout=layout;
    pictureUrl=picture_url;
    this.checking=checking;
    handler=new Handler();
    selectedProduct=((StartActivity)context).getManager().getSelectedProduct();
    //defaultProduct=((StartActivity)context).getManager().getDefaultProduct();
    order=((StartActivity)context).getManager().getTempOrderAB()!=null?
          ((StartActivity)context).getManager().getTempOrderAB():((StartActivity)context).getManager().getCurrentOrderAB();
  }
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
    View view=LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
    return new ViewHolder(view);
  }
  @Override
  public void onBindViewHolder(final ViewHolder holder,int position){
    holder.item=itemList.get(position);
    //here init fields
    final String current_currency;
    String str,name,description;
    Attr attr;
    Type type;
    final Manager manager=((StartActivity)context).getManager();
    final Product product=holder.item.getProduct();
    Currency currency=holder.item.getCurrency();
    Iterator iter;
    holder.view.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v){
        if(listener!=null){
          // Notify the active callbacks interface (the activity, if the
          // fragment is attached to one) that an item has been selected.
          if(holder.checkBoxService!=null)holder.checkBoxService.setChecked(true);
          if(holder.buttonAddToCart!=null)holder.buttonAddToCart.setEnabled(false);
          if(holder.buttonBookNow!=null)holder.buttonBookNow.setEnabled(false);
          product.checked=true;//select product
          if(holder.editTextCount!=null)product.cart_count=(int)Integer.valueOf(holder.editTextCount.getText().toString());
          listener.onListFragmentInteraction(holder.item);
        }
      }
    });
    name=T.EMPTY;description=T.EMPTY;
    if(product.attrList!=null&&product.attrList.size()>0){//work with first
      iter=product.attrList.iterator();
      while(iter.hasNext()){
        attr=(Attr)iter.next();
        if(attr.name.equals(T.JSON_PARAM_ATTR_NAME_VALUE_TITLE))name=attr.value;
        else if(attr.name.equals(T.JSON_PARAM_ATTR_NAME_VALUE_ABOUT))description=attr.value;
      }
    }
    else{name=product.name;description=product.description;}
    holder.textViewName.setText(name);
    holder.textViewDescription.setText(T.OPEN_DOOR+product.code+T.CLOSE_DOOR+T.SPACE+description);
    str=T.EMPTY;
    if(product.typeList!=null&&product.typeList.size()>0){//work with first
      iter=product.typeList.iterator();
      while(iter.hasNext()){
        type=(Type)iter.next();
        if(type.attrList!=null&&type.attrList.size()>0){
          String title=T.EMPTY,about=T.EMPTY;
          iter=type.attrList.iterator();
          while(iter.hasNext()){
            attr=(Attr)iter.next();
            if(attr.name.equals(T.JSON_PARAM_ATTR_NAME_VALUE_TITLE))title=attr.value;
            else if(attr.name.equals(T.JSON_PARAM_ATTR_NAME_VALUE_ABOUT))about=attr.value;
          }
          str=title+T.NEXT_LINE+about;
        }
        else{
          str=type.name+T.NEXT_LINE+type.description;
        }
      }
    }
    if(holder.textViewType!=null)holder.textViewType.setText(str);
    str=manager.getPrice(product.price);
    if(str!=null)holder.textViewPrice.setText(str);
    str=manager.getActiveCurrencyTitle()!=null?manager.getActiveCurrencyTitle():currency.name;
    holder.textViewCurrency.setText(str);
    current_currency=str;
    str=product.discount!=null?manager.getDiscountPolicy(product.discount.type,product.discount.value):context.getString(R.string.no_add_fees);
    holder.textViewDiscount.setText(str);

    if(selectedProduct!=null){
      if(product.id==selectedProduct.id){
        if(holder.checkBoxService!=null)holder.checkBoxService.setChecked(true);
        if(holder.buttonAddToCart!=null)holder.buttonAddToCart.setEnabled(false);
        if(holder.buttonBookNow!=null)holder.buttonBookNow.setEnabled(false);
        if(holder.editTextCount!=null)holder.editTextCount.setText(String.valueOf(selectedProduct.cart_count));
      }
    }
    else if(!checking){//no select mode
      if(holder.checkBoxService!=null){
        holder.checkBoxService.setChecked(true);
        holder.checkBoxService.setEnabled(false);
      }
      if(holder.buttonAddToCart!=null)holder.buttonAddToCart.setEnabled(false);
      if(holder.buttonBookNow!=null)holder.buttonBookNow.setEnabled(false);
      if(holder.editTextCount!=null){
        if(product.cart_count==0){
          if(order!=null&&order.orderABPartList!=null){
            for(OrderABPart item : order.orderABPartList){
              if(item.product_id==product.id){
                holder.editTextCount.setText(String.valueOf(item.product_count));
              }
            }
          }
        }
        else holder.editTextCount.setText(String.valueOf(product.cart_count));
        holder.editTextCount.setEnabled(false);
      }
      if(holder.buttonMinus!=null)holder.buttonMinus.setEnabled(false);
      if(holder.buttonPlus!=null)holder.buttonPlus.setEnabled(false);
    }
    else if(product.cart_count>0){
      if(holder.checkBoxService!=null)holder.checkBoxService.setChecked(true);
      if(holder.buttonAddToCart!=null)holder.buttonAddToCart.setEnabled(false);
      if(holder.buttonBookNow!=null)holder.buttonBookNow.setEnabled(false);
      if(holder.editTextCount!=null)holder.editTextCount.setText(String.valueOf(product.cart_count));
    }
    else if(product.cart_count==0){
      if(holder.buttonAddToCart!=null)holder.buttonAddToCart.setEnabled(true);
      if(holder.buttonBookNow!=null)holder.buttonBookNow.setEnabled(true);
      if(holder.editTextCount!=null)holder.editTextCount.setText(String.valueOf(1));
    }

    // Similar to holder.view.setOnClickListener
    if(holder.checkBoxService!=null)
    holder.checkBoxService.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v){
        CheckBox check_box=(CheckBox)v;
        if(listener!=null&&check_box.isEnabled()){
          product.checked=check_box.isChecked();//select|unselect product
          listener.onListFragmentInteraction(holder.item);
        }
      }
    });

    //button_order_service listener
    if(product.paramList!=null&&product.paramList.size()>0)
    holder.buttonOptions.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view){
        /*toast("OPTIONS="+(product!=null?product.name+
        (product.paramList!=null?product.paramList.size():T.EMPTY):T.EMPTY));*/

        int size=product.paramList.size();
        CharSequence[] items=new CharSequence[size];
        boolean checked_items[]=new boolean[size];
        String attr,param_value,price;
        for(int i=0;i<size;i++){
          attr=manager.getAttrByName(product.paramList.get(i).attrList,T.JSON_PARAM_ATTR_NAME_VALUE_TITLE);
          param_value=manager.getParamValue(product.paramList.get(i).value,product.paramList.get(i).part_value);
          price=manager.getPrice(product.paramList.get(i).part_price);
          items[i]=(attr!=null?attr:product.paramList.get(i).name)+T.SPACE+
                   (param_value.length()>0?param_value+T.SPACE:T.EMPTY)+
                   (product.paramList.get(i).part_price>0?price+T.SPACE+current_currency:T.EMPTY);
          if(Manager.CAR_RENTAL)product.paramList.get(i).checked=true;
          checked_items[i]=checking?product.paramList.get(i).checked:true;
        }

        AlertDialog dialog=manager.selectOptionsDialog(product.paramList,items,checked_items,
          new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which,boolean is_checked){
            }
          }
        );
        dialog.getListView().setEnabled(checking);
      }
    });

    if(holder.buttonBookNow!=null)
    holder.buttonBookNow.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view){
        holder.view.performClick();
      }
    });

    if(holder.buttonAddToCart!=null)
    holder.buttonAddToCart.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view){
        holder.view.performClick();
      }
    });

    if(holder.buttonMinus!=null)
    holder.buttonMinus.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view){
        if(holder.editTextCount!=null){
          int value=1;
          try{value=Integer.valueOf(holder.editTextCount.getText().toString());}catch(Exception e){}
          if(value>1)value--;
          holder.editTextCount.setText(String.valueOf(value));
        }
      }
    });

    if(holder.buttonPlus!=null)
    holder.buttonPlus.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view){
        if(holder.editTextCount!=null){
          int value=0;
          try{value=Integer.valueOf(holder.editTextCount.getText().toString());}catch(Exception e){}
          if(value<10)value++;
          holder.editTextCount.setText(String.valueOf(value));
        }
      }
    });

    //picasso picture loading ...
    if(pictureUrl!=null&&holder.imageViewPicture!=null&&product!=null){
      if(product.picture!=null&&product.picture instanceof Drawable)
        holder.imageViewPicture.setImageDrawable((Drawable)product.picture);
      else manager.loadImage(pictureUrl+product.id,holder.imageViewPicture,product);
    }
  }
  @Override
  public int getItemCount(){return itemList.size();}

  public class ViewHolder extends RecyclerView.ViewHolder{
    public View view;
    public Item item;

    public ImageView imageViewPicture=null;
    public CheckBox checkBoxService=null;
    public TextView textViewName=null;
    public TextView textViewDescription=null;
    public TextView textViewType=null;
    public TextView textViewPrice=null;
    public TextView textViewCurrency=null;
    public TextView textViewDiscount=null;

    public Button buttonOptions=null;
    public Button buttonBookNow=null;
    public Button buttonAddToCart=null;
    public ImageButton buttonMinus=null;
    public ImageButton buttonPlus=null;

    public EditText editTextCount=null;

    public ViewHolder(View view){
      super(view);
      this.view=view;
      if(view!=null){
        imageViewPicture=(ImageView)view.findViewById(R.id.image_view_product_picture);
        checkBoxService=(CheckBox)view.findViewById(R.id.check_box_service);
        textViewName=(TextView)view.findViewById(R.id.text_view_name);
        textViewDescription=(TextView)view.findViewById(R.id.text_view_description);
        textViewType=(TextView)view.findViewById(R.id.text_view_type);
        textViewPrice=(TextView)view.findViewById(R.id.text_view_price);
        textViewCurrency=(TextView)view.findViewById(R.id.text_view_currency);
        textViewDiscount=(TextView)view.findViewById(R.id.text_view_discount);
        buttonOptions=(Button)view.findViewById(R.id.button_options);
        buttonBookNow=(Button)view.findViewById(R.id.button_book_now);
        buttonAddToCart=(Button)view.findViewById(R.id.button_add_to_cart);
        buttonMinus=(ImageButton)view.findViewById(R.id.image_button_minus);
        buttonPlus=(ImageButton)view.findViewById(R.id.image_button_plus);
        editTextCount=(EditText)view.findViewById(R.id.edit_text_count);
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