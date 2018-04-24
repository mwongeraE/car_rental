package autozvit.com.fireart.forms.list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import autozvit.com.fireart.objects.Product;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.tools.T;

//import android.support.v7.app.AlertDialog;//accent colors

public class EasyProductListAdapter extends RecyclerView.Adapter<EasyProductListAdapter.ViewHolder>{
  private Context context;
  public void setContext(Context context){this.context=context;}
  public Context getContext(){return context;}
  private final List<Item> itemList;
  public List<Item> getItemList(){return itemList;}
  private final OnListFragmentInteractionListener listener;
  private int layout;
  public void setLayout(int layout){this.layout=layout;}
  private String pictureUrl;
  public void setPictureUrl(String url){pictureUrl=url;}
  public String getPictureUrl(){return pictureUrl;}
  private boolean options=false;//client can select product options parameters(for order)
  public void setOptions(boolean options){this.options=options;}
  public boolean isOptions(){return options;}
  private Handler handler;
  //private Product selectedProduct,defaultProduct;
  private CardView lastSelectedCardView=null;
  public EasyProductListAdapter(List<Item> items,OnListFragmentInteractionListener listener,Context context,int layout,String picture_url){
    itemList=items;
    this.listener=listener;
    this.context=context;
    this.layout=layout;
    pictureUrl=picture_url;
    handler=new Handler();
    //selectedProduct=((StartActivity)context).getManager().getSelectedProduct();
    //defaultProduct=((StartActivity)context).getManager().getDefaultProduct();
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
    String name;
    Attr attr;
    final Manager manager=((StartActivity)context).getManager();
    final Product product=holder.item.getProduct();
    //Currency currency=holder.item.getCurrency();
    Iterator iter;
    holder.view.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v){
        if(listener!=null){
          // Notify the active callbacks interface (the activity, if the
          // fragment is attached to one) that an item has been selected.
          product.checked=true;//select product
          listener.onListFragmentInteraction(holder.item);
          if(options)optionsDialog(product,holder,manager);
          //selected card color
          CardView card_view=(CardView)holder.view.findViewById(R.id.card_view); // checked not only one!!! (if there are many)
          if(card_view!=null){
            if(lastSelectedCardView!=null){
              //lastSelectedCardView.setCardBackgroundColor(context.getResources().getColor(R.color.easy_style_card_background));
              TextView text_view=(TextView)lastSelectedCardView.findViewById(R.id.text_view_product_name);
              if(text_view!=null)text_view.setBackgroundResource(R.color.easy_style_card_background);
            }
            //card_view.setCardBackgroundColor(context.getResources().getColor(R.color.easy_style_selected_card_background));
            TextView text_view=(TextView)card_view.findViewById(R.id.text_view_product_name);
            if(text_view!=null)text_view.setBackgroundResource(R.color.easy_style_selected_card_background);
            lastSelectedCardView=card_view;
          }
        }
      }
    });
    name=T.EMPTY;
    if(product.attrList!=null&&product.attrList.size()>0){//work with first
      iter=product.attrList.iterator();
      while(iter.hasNext()){
        attr=(Attr)iter.next();
        if(attr.name.equals(T.JSON_PARAM_ATTR_NAME_VALUE_TITLE))name=attr.value;
      }
    }
    else{name=product.name;}
    holder.textViewName.setText(name);

    if(manager.getSelectedProduct()!=null&&manager.getSelectedProduct().id==product.id){
      CardView card_view=(CardView)holder.view.findViewById(R.id.card_view);
      if(card_view!=null){
        if(lastSelectedCardView!=null){
          //lastSelectedCardView.setCardBackgroundColor(context.getResources().getColor(R.color.easy_style_card_background));
          TextView text_view=(TextView)lastSelectedCardView.findViewById(R.id.text_view_product_name);
          if(text_view!=null)text_view.setBackgroundResource(R.color.easy_style_card_background);
        }
        //card_view.setCardBackgroundColor(context.getResources().getColor(R.color.easy_style_selected_card_background));
        TextView text_view=(TextView)card_view.findViewById(R.id.text_view_product_name);
        if(text_view!=null)text_view.setBackgroundResource(R.color.easy_style_selected_card_background);
        lastSelectedCardView=card_view;
      }
    }

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
    public TextView textViewName=null;
    public ViewHolder(View view){
      super(view);
      this.view=view;
      if(view!=null){
        imageViewPicture=(ImageView)view.findViewById(R.id.image_view_product_picture);
        textViewName=(TextView)view.findViewById(R.id.text_view_product_name);
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

  private void optionsDialog(Product product,ViewHolder holder,Manager manager){
    if(product.paramList!=null&&product.paramList.size()>0){
      int size=product.paramList.size();
      CharSequence[] items=new CharSequence[size];
      boolean checked_items[]=new boolean[size];
      Currency currency=holder.item.getCurrency();
      String attr,param_value,price;
      String current_currency=manager.getActiveCurrencyTitle()!=null?manager.getActiveCurrencyTitle():currency.name;
      for(int i=0;i<size;i++){
        attr=manager.getAttrByName(product.paramList.get(i).attrList,T.JSON_PARAM_ATTR_NAME_VALUE_TITLE);
        param_value=manager.getParamValue(product.paramList.get(i).value,product.paramList.get(i).part_value);
        price=manager.getPrice(product.paramList.get(i).part_price);
        items[i]=(attr!=null?attr:product.paramList.get(i).name)+T.SPACE+
                 (param_value.length()>0?param_value+T.SPACE:T.EMPTY)+
                 (product.paramList.get(i).part_price>0?price+T.SPACE+current_currency:T.EMPTY);
        if(Manager.CAR_RENTAL)product.paramList.get(i).checked=true;
        checked_items[i]=product.paramList.get(i).checked;
      }

      AlertDialog dialog=manager.selectOptionsDialog(product.paramList,items,checked_items,
        new DialogInterface.OnMultiChoiceClickListener(){
          @Override
          public void onClick(DialogInterface d,int which,boolean is_checked){
          }
        }
      );
      dialog.getListView().setEnabled(true);
    }
  }
}