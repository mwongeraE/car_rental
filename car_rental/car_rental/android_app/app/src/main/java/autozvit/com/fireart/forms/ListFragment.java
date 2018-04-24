package autozvit.com.fireart.forms;

import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.ArrayList;

import autozvit.com.fireart.R;
import autozvit.com.fireart.forms.list.ListContent;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.tools.T;
import autozvit.com.fireart.tools.TypedCallback;

public class ListFragment extends Fragment{
  private static final String ARG_NAME="name";
  private static final String ARG_COL_COUNT="col_count";
  private static final String ARG_LIST="list";
  private static final String ARG_CLASSNAME="classname";
  private static final String ARG_LAYOUT="layout";
  private static final String ARG_LAYOUT1="layout1";
  private static final String ARG_LAYOUT2="layout2";
  private static final String ARG_PICTURE_URL="picture_url";
  private static final String ARG_NEXT_ENABLED="next_enabled";
  private static final String ARG_CHECKING="checking";
  private View view=null;
  private String name=null;
  private int colCount=1;
  private ArrayList itemList=null;
  public ArrayList getItemList(){return itemList;}
  private String classname=null;
  private int layout=0,layout1=0,layout2=0;
  private String pictureUrl=null;
  private boolean nextEnabled=false;
  private boolean checking=false;
  private Object listAdapterObject=null;
  public Object getListAdapterObject(){return listAdapterObject;}

  private Context context;
  private Manager manager;

  private OnListFragmentInteractionListener listener;
  private RecyclerView recyclerView=null;

  private TypedCallback callback=null;
  public void setCallback(TypedCallback callback){this.callback=callback;}

  private Button button_next=null/*,button_back=null*/;
  public void setNextEnabled(boolean enabled){
    if(button_next!=null){
      if(!enabled)button_next.setTextColor(getResources().getColor(R.color.DarkGray));
      else button_next.setTextAppearance(getContext(),R.style.Widget_Style_Button);
      button_next.setEnabled(enabled);
    }
  }

  private int pageNumber=0;

  Manager.WaitStarter waitStarter=null;

  @SuppressWarnings("unused")
  public static ListFragment newInstance(String name,int col_count,List list,String classname,int layout,String picture_url,boolean next_enabled,boolean checking){
    ListFragment fragment=new ListFragment();
    Bundle args=new Bundle();
    args.putString(ARG_NAME,name);
    args.putInt(ARG_COL_COUNT,col_count);
    args.putParcelableArrayList(ARG_LIST,(ArrayList)list);
    args.putString(ARG_CLASSNAME,classname);
    args.putInt(ARG_LAYOUT,layout);
    args.putString(ARG_PICTURE_URL,picture_url);
    args.putBoolean(ARG_NEXT_ENABLED,next_enabled);
    args.putBoolean(ARG_CHECKING,checking);
    fragment.setArguments(args);
    return fragment;
  }
  public static ListFragment newInstance(String name,int col_count,List list,String classname,int layout1,int layout2,String picture_url,boolean next_enabled,boolean checking){
    ListFragment fragment=new ListFragment();
    Bundle args=new Bundle();
    args.putString(ARG_NAME,name);
    args.putInt(ARG_COL_COUNT,col_count);
    args.putParcelableArrayList(ARG_LIST,(ArrayList)list);
    args.putString(ARG_CLASSNAME,classname);
    args.putInt(ARG_LAYOUT1,layout1);
    args.putInt(ARG_LAYOUT2,layout2);
    args.putString(ARG_PICTURE_URL,picture_url);
    args.putBoolean(ARG_NEXT_ENABLED,next_enabled);
    args.putBoolean(ARG_CHECKING,checking);
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
      layout=getArguments().getInt(ARG_LAYOUT,0);
      layout1=getArguments().getInt(ARG_LAYOUT1,0);
      layout2=getArguments().getInt(ARG_LAYOUT2,0);
      pictureUrl=getArguments().getString(ARG_PICTURE_URL);
      nextEnabled=getArguments().getBoolean(ARG_NEXT_ENABLED);
      checking=getArguments().getBoolean(ARG_CHECKING);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    if(layout!=0){//one layout_calendar_dialog
      view=inflater.inflate(R.layout.fragment_list,container,false);
    }
    else if(layout1!=0&&layout2!=0){//two layouts (such as: for input,output messages)
      view=inflater.inflate(R.layout.fragment_message_list,container,false);
    }
    TextView text_view_list=null;
    if(view!=null){
      text_view_list=(TextView)view.findViewById(R.id.text_view_list);
      recyclerView=(RecyclerView)view.findViewById(R.id.list);
      button_next=(Button)view.findViewById(R.id.button_next);
      //button_back=(Button)view.findViewById(R.id.button_back);
    }
    //list title
    if(text_view_list!=null){
      text_view_list.setText(name.toUpperCase());
    }
    //set the adapter
    if(recyclerView!=null){
      Context context=view.getContext();
      if(colCount<=1)recyclerView.setLayoutManager(new LinearLayoutManager(context));
      else recyclerView.setLayoutManager(new GridLayoutManager(context,colCount));
      try{
        Class<?> c=Class.forName(classname);
        Constructor<?> cons;
        Object object=null;
        if(layout!=0){//one layout_calendar_dialog
          cons=c.getConstructor(List.class,OnListFragmentInteractionListener.class,Context.class,int.class,String.class,boolean.class);
          object=cons.newInstance(itemList,listener,getActivity(),layout,pictureUrl,checking);
        }
        else if(layout1!=0&&layout2!=0){//two layouts (such as: for input,output messages)
          cons=c.getConstructor(List.class,OnListFragmentInteractionListener.class,Context.class,int.class,int.class,String.class,boolean.class);
          object=cons.newInstance(itemList,listener,getActivity(),layout1,layout2,pictureUrl,checking);
        }
        if(object!=null)recyclerView.setAdapter((RecyclerView.Adapter)object);
        listAdapterObject=object;
      }catch(Exception e){}

      if(button_next!=null){
        if(nextEnabled){
          button_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              if(callback!=null)callback.execute(new Integer(++pageNumber));
            }
          });
        }
        else{
          button_next.setTextColor(context.getResources().getColor(R.color.DarkGray));
          button_next.setEnabled(false);
        }
      }
      /*if(button_back!=null){
        button_back.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v){
          }
        });
      }*/

      //goto end
      if(layout1!=0&&layout2!=0){
        context=getActivity();
        if(context!=null){
          manager=((StartActivity)context).getManager();
          //queries recycler
          waitStarter=manager.waitStarter(manager.doNewMessageRequestCallbackHandler,null,T.TASK_SERVICE_SLEEP_COUNT);
        }
      }

    }
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    //context=getActivity();
    //manager=((StartActivity)context).getManager();
  }

  @Override
  public void onDestroy(){
    super.onDestroy();
    if(waitStarter!=null){waitStarter.setNeedClose(true);waitStarter=null;}
  }

  @Override
  public void onAttach(Context context){
    super.onAttach(context);
    if(context instanceof OnListFragmentInteractionListener){
      listener=(OnListFragmentInteractionListener)context;
    }else{throw new RuntimeException(context.toString()+" must implement OnListFragmentInteractionListener");}
  }

  @Override
  public void onDetach(){
    super.onDetach();
    listener=null;
  }

  public interface OnListFragmentInteractionListener{
    void onListFragmentInteraction(ListContent.Item item);
  }
  public void updateName(String name){
    if(view!=null){
      TextView text_view_list=(TextView)view.findViewById(R.id.text_view_list);
      //list title
      if(text_view_list!=null){
        text_view_list.setText(name.toUpperCase());
      }
    }
  }
  public void smoothScrollToPosition(int position){
    if(recyclerView!=null&&itemList!=null&&itemList.size()>position){
      recyclerView.smoothScrollToPosition(position);
    }
  }
}