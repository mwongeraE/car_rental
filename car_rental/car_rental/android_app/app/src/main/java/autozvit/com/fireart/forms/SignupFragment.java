package autozvit.com.fireart.forms;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import autozvit.com.fireart.R;

public class SignupFragment extends Fragment{
  private static final String ARG_PHONE="phone";
  private static final String ARG_PASSWORD="password";
  private String phone;//phone
  private String password;//password
  public static SignupFragment newInstance(String phone,String password){
    SignupFragment fragment=new SignupFragment();
    Bundle args=new Bundle();
    args.putString(ARG_PHONE,phone);
    args.putString(ARG_PASSWORD,password);
    fragment.setArguments(args);
    return fragment;
  }
  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      phone=getArguments().getString(ARG_PHONE);
      password=getArguments().getString(ARG_PASSWORD);
    }
  }
  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_signup,container,false);
  }
  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    View view=getView();
    if(view!=null){
      EditText edit_text_phone=((EditText)view.findViewById(R.id.edit_text_phone));
      if(edit_text_phone!=null&&phone!=null)edit_text_phone.setText(phone);
      EditText edit_text_password=((EditText)view.findViewById(R.id.edit_text_password));
      if(edit_text_password!=null)edit_text_password.setText(password);
    }
  }
}
