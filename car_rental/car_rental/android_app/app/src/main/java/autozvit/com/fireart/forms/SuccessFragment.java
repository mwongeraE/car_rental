package autozvit.com.fireart.forms;
import autozvit.com.fireart.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SuccessFragment extends Fragment{
  private static final String ARG_PARAM1="param1";
  private static final String ARG_PARAM2="param2";
  private String param1;
  private String param2;
  private TextView textViewLarge;
  private TextView textViewSmall;
  public static SuccessFragment newInstance(String param1,String param2){
    SuccessFragment fragment=new SuccessFragment();
    Bundle args=new Bundle();
    args.putString(ARG_PARAM1,param1);
    args.putString(ARG_PARAM2,param2);
    fragment.setArguments(args);
    return fragment;
  }
  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      param1=getArguments().getString(ARG_PARAM1);
      param2=getArguments().getString(ARG_PARAM2);
    }
  }
  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_success,container,false);
  }
  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    View view=getView();
    if(view!=null){
      textViewLarge=((TextView)view.findViewById(R.id.text_view_large));
      if(textViewLarge!=null)textViewLarge.setText(param1);
      textViewSmall=((TextView)view.findViewById(R.id.text_view_small));
      if(textViewSmall!=null)textViewSmall.setText(param2);
    }
  }
}