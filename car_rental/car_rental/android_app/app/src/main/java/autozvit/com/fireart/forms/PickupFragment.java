package autozvit.com.fireart.forms;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import autozvit.com.fireart.R;
import autozvit.com.fireart.forms.list.EasyProductListAdapter;
import autozvit.com.fireart.forms.list.ListContent;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.tools.T;

public class PickupFragment extends Fragment{
  private static final String ARG_NAME="name";
  private static final String ARG_COL_COUNT="col_count";
  private static final String ARG_LIST="list";
  private static final String ARG_CLASSNAME="classname";
  private static final String ARG_LAYOUT="layout";
  private static final String ARG_PICTURE_URL="picture_url";
  private static final String ARG_PICKUP_PICTURE="pickup_picture";
  private static final String ARG_PICKUP_NAME="pickup_name";
  private static final String ARG_PICKUP_ADDRESS="pickup_address";

  private String name;
  private int colCount;
  private ArrayList itemList=null;
  public ArrayList getItemList(){return itemList;}
  private String classname;
  private int layout;
  private String pictureUrl;
  private int pickupPicture;
  private String pickupName;
  private String pickupAddress;
  private Object listAdapterObject=null;
  public Object getListAdapterObject(){return listAdapterObject;}

  private Context context;
  private View view;

  private ListFragment.OnListFragmentInteractionListener listener;

  private ImageView imageViewPickupPicture;
  private TextView textViewPickupName;
  private TextView textViewPickupAddress;

  public static PickupFragment newInstance(String name,int col_count,List list,String classname,int layout,String picture_url,int pickup_picture,String pickup_name,String pickup_address){
    PickupFragment fragment=new PickupFragment();
    Bundle args=new Bundle();
    args.putString(ARG_NAME,name);
    args.putInt(ARG_COL_COUNT,col_count);
    args.putParcelableArrayList(ARG_LIST,(ArrayList)list);
    args.putString(ARG_CLASSNAME,classname);
    args.putInt(ARG_LAYOUT,layout);
    args.putString(ARG_PICTURE_URL,picture_url);
    args.putInt(ARG_PICKUP_PICTURE,pickup_picture);
    args.putString(ARG_PICKUP_NAME,pickup_name);
    args.putString(ARG_PICKUP_ADDRESS,pickup_address);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      name=getArguments().getString(ARG_NAME);
      colCount=getArguments().getInt(ARG_COL_COUNT);
      itemList=getArguments().getParcelableArrayList(ARG_LIST);
      classname=getArguments().getString(ARG_CLASSNAME);
      layout=getArguments().getInt(ARG_LAYOUT);
      pictureUrl=getArguments().getString(ARG_PICTURE_URL);
      pickupPicture=getArguments().getInt(ARG_PICKUP_PICTURE);
      pickupName=getArguments().getString(ARG_PICKUP_NAME);
      pickupAddress=getArguments().getString(ARG_PICKUP_ADDRESS);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_pickup,container,false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    context=getActivity();
    view=getView();
    if(view!=null){

      imageViewPickupPicture=(ImageView)view.findViewById(R.id.image_view_pickup_picture);
      if(imageViewPickupPicture!=null&&pickupPicture!=-1)imageViewPickupPicture.setImageResource(pickupPicture);
      textViewPickupName=(TextView)view.findViewById(R.id.text_view_pickup_name);
      if(textViewPickupName!=null/*&&pickupName!=null*/)textViewPickupName.setText(pickupName);
      textViewPickupAddress=(TextView)view.findViewById(R.id.text_view_pickup_address);
      if(textViewPickupAddress!=null/*&&pickupAddress!=null*/)textViewPickupAddress.setText(pickupAddress);

      /*Button button_cancel=(Button)view.findViewById(R.id.button_cancel);//cancel not found
      if(button_cancel!=null){
        button_cancel.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v){
          }
        });
      }*/

      Manager manager=((StartActivity)context).getManager();
      if(itemList!=null/*manager.getProductList()!=null*/){
        setAdapter(view);
        //similar to setAdapter
        //manager.showEasyProductListCallbackHandler.execute(manager.getProductList());
      }
      else{
        manager.getProductRequest(manager.showEasyProductListCallbackHandler);
      }
    }
  }

  @Override
  public void onDestroy(){
    super.onDestroy();
  }

  @Override
  public void onAttach(Context context){
    super.onAttach(context);
    if(context instanceof ListFragment.OnListFragmentInteractionListener){
      listener=(ListFragment.OnListFragmentInteractionListener)context;
    }
    else{throw new RuntimeException(context.toString()+" must implement OnListFragmentInteractionListener");}
  }

  @Override
  public void onDetach(){
    super.onDetach();
    listener=null;
  }

  public interface OnListFragmentInteractionListener{
    void onListFragmentInteraction(ListContent.Item item);
  }

  //tools methods
  public void updateItemList(List list){
    itemList=(ArrayList)list;
    setAdapter(view);
  }
  public void updatePickupNameAndAddress(String name,String address){
    pickupName=name;pickupAddress=address;
    textViewPickupName=(TextView)view.findViewById(R.id.text_view_pickup_name);
    if(textViewPickupName!=null){
      if(pickupName!=null)textViewPickupName.setText(pickupName);
      else textViewPickupName.setText(T.EMPTY);
    }
    textViewPickupAddress=(TextView)view.findViewById(R.id.text_view_pickup_address);
    if(textViewPickupAddress!=null){
      if(pickupAddress!=null)textViewPickupAddress.setText(pickupAddress);
      else textViewPickupAddress.setText(T.EMPTY);
    }
  }
  private void setAdapter(View v){
    RecyclerView recycler_view=(v!=null)?(RecyclerView)v.findViewById(R.id.list):null;
    //set the adapter
    if(recycler_view!=null&&itemList!=null){
      Context context=v.getContext();
      if(colCount<=1)recycler_view.setLayoutManager(new LinearLayoutManager(context));
      else recycler_view.setLayoutManager(new GridLayoutManager(context,colCount));
      try{
        Class<?> c=Class.forName(classname);
        Constructor<?> cons=c.getConstructor(List.class,ListFragment.OnListFragmentInteractionListener.class,Context.class,int.class,String.class);
        Object object=cons.newInstance(itemList,listener,getActivity(),layout,pictureUrl);
        ((EasyProductListAdapter)object).setOptions(true);//options dialog

        LinearLayoutManager linear_layout_manager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recycler_view.setLayoutManager(linear_layout_manager);

        recycler_view.setAdapter((RecyclerView.Adapter)object);
        listAdapterObject=object;
      }catch(Exception e){}
    }
  }
}