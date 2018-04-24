package autozvit.com.fireart.forms;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import autozvit.com.fireart.R;

public class TipFragment extends Fragment{
  private static final String ARG_TIP_MESSAGE="tip_message";//tip message
  private static final String ARG_PICTURE_ID="picture_id";//picture id

  private String tipMessage;
  private int pictureId;

  private Context context;

  private TextView textViewTip;
  private ImageView imageView;

  public static TipFragment newInstance(String tip_message,int picture_id){
    TipFragment fragment=new TipFragment();
    Bundle args=new Bundle();
    args.putString(ARG_TIP_MESSAGE,tip_message);
    args.putInt(ARG_PICTURE_ID,picture_id);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      tipMessage=getArguments().getString(ARG_TIP_MESSAGE);
      pictureId=getArguments().getInt(ARG_PICTURE_ID);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_tip,container,false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    context=getActivity();
    View view=getView();
    if(view!=null){

      textViewTip=(TextView)view.findViewById(R.id.text_view_tip);
      if(textViewTip!=null&&tipMessage!=null)textViewTip.setText(tipMessage);
      imageView=(ImageView)view.findViewById(R.id.image_view_picture);
      if(imageView!=null){
        if(pictureId!=-1)imageView.setImageResource(pictureId);
        else imageView.setVisibility(View.INVISIBLE);
      }

      /*Button button_cancel=(Button)view.findViewById(R.id.button_cancel);//cancel not found
      if(button_cancel!=null){
        button_cancel.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v){
            ((StartActivity)context).getManager().removeFragment(T.FRAGMENT_NAME_TIP);
          }
        });
      }*/
    }
  }

  public void updateTipMessage(String tip_message){
    View view=getView();
    if(view!=null){
      textViewTip=(TextView)view.findViewById(R.id.text_view_tip);
      if(textViewTip!=null)textViewTip.setText(tip_message);
    }
  }
}
