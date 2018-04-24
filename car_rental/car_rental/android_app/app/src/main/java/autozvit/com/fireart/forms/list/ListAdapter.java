package autozvit.com.fireart.forms.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import autozvit.com.fireart.forms.ListFragment.OnListFragmentInteractionListener;
import autozvit.com.fireart.forms.list.ListContent.Item;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
  private Context context;
  public void setContext(Context context){this.context=context;}
  public Context getContext(){return context;}
  private List<Item> itemList;
  public List<Item> getItemList(){return itemList;}
  private OnListFragmentInteractionListener listener;
  private String pictureUrl;
  public void setPictureUrl(String url){pictureUrl=url;}
  public String getPictureUrl(){return pictureUrl;}
  private int layout;
  public void setLayout(int layout){this.layout=layout;}

  public ListAdapter(List<Item> items,OnListFragmentInteractionListener listener,Context context,int layout,String picture_url){
    itemList=items;
    this.listener=listener;
    this.context=context;
    this.layout=layout;
    pictureUrl=picture_url;
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
  }

  @Override
  public int getItemCount(){return itemList.size();}

  public class ViewHolder extends RecyclerView.ViewHolder{
    public View view;
    public Item item;

    public ViewHolder(View view){
      super(view);
      this.view=view;
      if(view!=null){
      }
    }

    @Override
    public String toString(){return super.toString();}
  }
}