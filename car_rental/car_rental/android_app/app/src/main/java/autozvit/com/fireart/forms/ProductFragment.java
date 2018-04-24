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

public class ProductFragment extends Fragment{
  private static final String ARG_COL_COUNT="col_count";
  private static final String ARG_LIST="list";
  private static final String ARG_CLASSNAME="classname";
  private static final String ARG_LAYOUT="layout";
  private static final String ARG_PICTURE_URL="picture_url";
  private static final String ARG_FIND_PICTURE="find_picture";
  private static final String ARG_FIND_NAME="find_name";

  private int colCount;
  private ArrayList itemList=null;
  public ArrayList getItemList(){return itemList;}
  private String classname;
  private int layout;
  private String pictureUrl;
  private int findPicture;
  private String findName;
  private Object listAdapterObject=null;
  public Object getListAdapterObject(){return listAdapterObject;}

  private Context context;
  private View view;

  private ListFragment.OnListFragmentInteractionListener listener;

  private ImageView imageViewFindPicture;
  private TextView textViewFindName;

  public static ProductFragment newInstance(int col_count,List list,String classname,int layout,String picture_url,int pickup_picture,String pickup_name){
    ProductFragment fragment=new ProductFragment();
    Bundle args=new Bundle();
    args.putInt(ARG_COL_COUNT,col_count);
    args.putParcelableArrayList(ARG_LIST,(ArrayList)list);
    args.putString(ARG_CLASSNAME,classname);
    args.putInt(ARG_LAYOUT,layout);
    args.putString(ARG_PICTURE_URL,picture_url);
    args.putInt(ARG_FIND_PICTURE,pickup_picture);
    args.putString(ARG_FIND_NAME,pickup_name);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      colCount=getArguments().getInt(ARG_COL_COUNT);
      itemList=getArguments().getParcelableArrayList(ARG_LIST);
      classname=getArguments().getString(ARG_CLASSNAME);
      layout=getArguments().getInt(ARG_LAYOUT);
      pictureUrl=getArguments().getString(ARG_PICTURE_URL);
      findPicture=getArguments().getInt(ARG_FIND_PICTURE);
      findName=getArguments().getString(ARG_FIND_NAME);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_product,container,false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    context=getActivity();
    view=getView();
    if(view!=null){

      imageViewFindPicture=(ImageView)view.findViewById(R.id.image_view_find_picture);
      if(imageViewFindPicture!=null&&findPicture!=-1) imageViewFindPicture.setImageResource(findPicture);
      textViewFindName=(TextView)view.findViewById(R.id.text_view_find_name);
      if(textViewFindName!=null/*&&findName!=null*/) textViewFindName.setText(findName);

      /*
      Button button_back=(Button)view.findViewById(R.id.button_back);
      if(button_back!=null){
        button_back.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v){
            manager.removeFragment(T.FRAGMENT_NAME_PRODUCT);
          }
        });
      }
      */

      Manager manager=((StartActivity)context).getManager();
      if(itemList!=null){
        setAdapter(view);
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