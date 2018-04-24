package autozvit.com.fireart.forms.list;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import autozvit.com.fireart.R;
import autozvit.com.fireart.forms.ListFragment.OnListFragmentInteractionListener;
import autozvit.com.fireart.forms.StartActivity;
import autozvit.com.fireart.forms.list.MessageListContent.Item;
import autozvit.com.fireart.objects.Message;
import autozvit.com.fireart.objects.User;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.tools.T;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder>{
  private Context context;
  public void setContext(Context context){this.context=context;}
  public Context getContext(){return context;}
  private ArrayList itemList;
  public ArrayList getItemList(){return itemList;}
  public void setItemlist(ArrayList list){itemList=list;}
  private OnListFragmentInteractionListener listener;
  private int layoutInputMessage,layoutOutputMessage;
  public void setLayoutInputMessage(int layout){layoutInputMessage=layout;}
  public void setLayoutOutputMessage(int layout){layoutOutputMessage=layout;}
  private String pictureUrl;
  public void setPictureUrl(String url){pictureUrl=url;}
  public String getPictureUrl(){return pictureUrl;}
  private boolean checking=false;//user can select message
  public void setChecking(boolean checking){this.checking=checking;}
  public boolean isChecking(){return checking;}
  private Handler handler;

  //binding param is for input/output layouts bind to holder
  private int lastBindedPosition=-1;
  public void setLastBindedPosition(int value){lastBindedPosition=value;}
  private int valueBindedPosition=1;
  public void setValueBindedPosition(int value){valueBindedPosition=value;}

  public MessageListAdapter(List<Item> items,OnListFragmentInteractionListener listener,Context context,int layout_input_message,int layout_output_message,String picture_url,boolean checking){
    itemList=(ArrayList)items;
    this.listener=listener;
    this.context=context;
    layoutInputMessage=layout_input_message;
    layoutOutputMessage=layout_output_message;
    pictureUrl=picture_url;
    handler=new Handler();
    this.checking=checking;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
    MessageListContent.Item item=(Item)itemList.get(lastBindedPosition+valueBindedPosition);
    int layout=0;
    if(item!=null){
      Message message=item.getMessage();
      if(message.type==Message.MESSAGE_TYPE_INPUT){
        layout=layoutInputMessage;
      }
      else if(message.type==Message.MESSAGE_TYPE_OUTPUT){
        layout=layoutOutputMessage;
      }
    }
    View view=LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
    return new ViewHolder(view);
  }
  @Override
  public void onBindViewHolder(final ViewHolder holder,int position){
    lastBindedPosition=position;
    holder.item=(Item)itemList.get(position);
    //toast("message="+holder.item.toString());//temp
    //here init fields
    final Manager manager=((StartActivity)context).getManager();

    final Message message=holder.item.getMessage();
    User user=null;

    if(message.type==Message.MESSAGE_TYPE_INPUT){
      user=message.userA;//from user
    }
    else if(message.type==Message.MESSAGE_TYPE_OUTPUT){
      user=message.userB;//to user
    }

    //if(user!=null)toast("message="+user.phone);//temp

    String message_text=message.message,create_date=message.create_date,callname=(user!=null)?user.call_name:T.EMPTY;

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

    holder.textViewMessage.setText(message_text);
    holder.textViewCreateDate.setText(manager.getTalkingDatetimeMessage(create_date));
    holder.textViewCallname.setText(callname);

    //picasso picture loading ...
    if(pictureUrl!=null&&holder.imageViewUserPicture!=null&&user!=null){
      if(user.picture!=null&&user.picture instanceof Drawable)
        holder.imageViewUserPicture.setImageDrawable((Drawable)user.picture);
      else{
        Object picture=findUserPicture(user.id);//find similar user object in list
        if(picture!=null&&picture instanceof Drawable){
          user.picture=picture;
          holder.imageViewUserPicture.setImageDrawable((Drawable)user.picture);
        }
        else manager.loadImage(pictureUrl+user.id,holder.imageViewUserPicture,user);
      }
    }
  }
  @Override
  public int getItemCount(){return itemList.size();}

  private Object findUserPicture(long user_id){
    for(MessageListContent.Item item:(List<Item>)itemList){
      if(item.getMessage().userA_id==user_id&&item.getMessage().userA!=null&&item.getMessage().userA.picture!=null){
        return item.getMessage().userA.picture;
      }
      if(item.getMessage().userB_id==user_id&&item.getMessage().userB!=null&&item.getMessage().userB.picture!=null){
        return item.getMessage().userB.picture;
      }
    }
    return null;
  }

  public class ViewHolder extends RecyclerView.ViewHolder{
    public View view;
    public Item item;

    public ImageView imageViewUserPicture=null;
    public TextView textViewMessage=null;
    public TextView textViewCreateDate=null;
    public TextView textViewCallname=null;

    public ViewHolder(View view){
      super(view);
      this.view=view;
      if(view!=null){
        imageViewUserPicture=(ImageView)view.findViewById(R.id.image_view_user_picture);
        textViewMessage=(TextView)view.findViewById(R.id.text_view_message);
        textViewCreateDate=(TextView)view.findViewById(R.id.text_view_create_date);
        textViewCallname=(TextView)view.findViewById(R.id.text_view_call_name);
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