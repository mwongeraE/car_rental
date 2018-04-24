package autozvit.com.fireart.forms;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import autozvit.com.fireart.R;

public class RegisterFragment extends Fragment{
  private static final String ARG_FIRSTNAME="firstname";
  private static final String ARG_LASTNAME="lastname";
  private static final String ARG_CALLNAME="callname";
  private static final String ARG_EMAIL="email";
  private static final String ARG_PHONE="phone";
  private static final String ARG_PASSWORD="password";
  private String firstname;
  private String lastname;
  private String callname;
  private String email;
  private String phone;
  private String password;
  public static RegisterFragment newInstance(String firstname,String lastname,String callname,String email,String phone,String password){
    RegisterFragment fragment=new RegisterFragment();
    Bundle args=new Bundle();
    args.putString(ARG_FIRSTNAME,firstname);
    args.putString(ARG_LASTNAME,lastname);
    args.putString(ARG_CALLNAME,callname);
    args.putString(ARG_EMAIL,email);
    args.putString(ARG_PHONE,phone);
    args.putString(ARG_PASSWORD,password);
    fragment.setArguments(args);
    return fragment;
  }
  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      firstname=getArguments().getString(ARG_FIRSTNAME);
      lastname=getArguments().getString(ARG_LASTNAME);
      callname=getArguments().getString(ARG_CALLNAME);
      email=getArguments().getString(ARG_EMAIL);
      phone=getArguments().getString(ARG_PHONE);
      password=getArguments().getString(ARG_PASSWORD);
    }
  }
  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_register,container,false);
  }
  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    View view=getView();
    if(view!=null){
      EditText edit_text_firstname=((EditText)view.findViewById(R.id.edit_text_firstname));
      if(edit_text_firstname!=null)edit_text_firstname.setText(firstname);
      EditText edit_text_lastname=((EditText)view.findViewById(R.id.edit_text_lastname));
      if(edit_text_lastname!=null)edit_text_lastname.setText(lastname);
      EditText edit_text_callname=((EditText)view.findViewById(R.id.edit_text_callname));
      if(edit_text_callname!=null)edit_text_callname.setText(callname);
      EditText edit_text_email=((EditText)view.findViewById(R.id.edit_text_email));
      if(edit_text_email!=null)edit_text_email.setText(email);
      EditText edit_text_phone=((EditText)view.findViewById(R.id.edit_text_phone));
      if(edit_text_phone!=null&&phone!=null)edit_text_phone.setText(phone);
      EditText edit_text_password=((EditText)view.findViewById(R.id.edit_text_password));
      if(edit_text_password!=null)edit_text_password.setText(password);
    }
  }

  //tools
  public String getEmail(){
    View view=getView();
    if(view!=null){
      EditText editText=(EditText)view.findViewById(R.id.edit_text_email);
      if(editText!=null)return editText.getText().toString();
    }
    return null;
  }
  public String getPhone(){
    View view=getView();
    if(view!=null){
      EditText editText=(EditText)view.findViewById(R.id.edit_text_phone);
      if(editText!=null)return editText.getText().toString();
    }
    return null;
  }
  public String getPassword(){
    View view=getView();
    if(view!=null){
      EditText editText=(EditText)view.findViewById(R.id.edit_text_password);
      if(editText!=null)return editText.getText().toString();
    }
    return null;
  }
}