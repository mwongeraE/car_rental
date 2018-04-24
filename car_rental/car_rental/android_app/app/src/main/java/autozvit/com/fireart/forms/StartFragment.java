package autozvit.com.fireart.forms;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import autozvit.com.fireart.R;

public class StartFragment extends Fragment{
  public static StartFragment newInstance(){
    StartFragment fragment=new StartFragment();
    Bundle args=new Bundle();
    fragment.setArguments(args);
    return fragment;
  }
  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
    }
  }
  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_start,container,false);
  }
  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    View view=getView();
    if(view!=null){
    }
  }
}
