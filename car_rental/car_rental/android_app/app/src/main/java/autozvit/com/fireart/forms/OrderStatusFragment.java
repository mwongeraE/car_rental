package autozvit.com.fireart.forms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import autozvit.com.fireart.R;

public class OrderStatusFragment extends Fragment{
  private static final String ARG_ORDER_STATUS_MESSAGE="order_status_message";
  private static final String ARG_PICTURE_ID="picture_id";
  private static final String ARG_PROGRESS_BAR="progress_bar";
  private static final String ARG_SHOW_LOGO="show_logo";

  private String orderStatusMessage;//order status message
  private int pictureId;//picture id(logo)
  private boolean progressBarVisibility;//progress_bar visible, image_view unvisible
  private boolean showLogo;//show logo

  private TextView textViewOrderStatus;
  private ImageView imageView;
  private ProgressBar progressBar;

  //private BroadcastReceiver broadcastReceiver=null;

  public static OrderStatusFragment newInstance(String order_status_message,int picture_id,boolean progress_bar,boolean show_logo){
    OrderStatusFragment fragment=new OrderStatusFragment();
    Bundle args=new Bundle();
    args.putString(ARG_ORDER_STATUS_MESSAGE,order_status_message);
    args.putInt(ARG_PICTURE_ID,picture_id);
    args.putBoolean(ARG_PROGRESS_BAR,progress_bar);
    args.putBoolean(ARG_SHOW_LOGO,show_logo);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      orderStatusMessage=getArguments().getString(ARG_ORDER_STATUS_MESSAGE);
      pictureId=getArguments().getInt(ARG_PICTURE_ID);
      progressBarVisibility=getArguments().getBoolean(ARG_PROGRESS_BAR);
      showLogo=getArguments().getBoolean(ARG_SHOW_LOGO);
    }
  }

  @Override
  public void onDestroy(){
    super.onDestroy();
    //if(broadcastReceiver!=null)((StartActivity)getActivity()).getManager().stopOrderStatusBroadcastReceiver();
  }

  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_order_status,container,false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    View view=getView();
    if(view!=null){

      textViewOrderStatus=(TextView)view.findViewById(R.id.text_view_order_status);
      if(textViewOrderStatus!=null&&orderStatusMessage!=null)textViewOrderStatus.setText(orderStatusMessage);
      imageView=(ImageView)view.findViewById(R.id.image_view_picture);
      if(imageView!=null){
        if(pictureId!=-1)imageView.setImageResource(pictureId);
        if(!progressBarVisibility&&showLogo)imageView.setVisibility(View.VISIBLE);
        else imageView.setVisibility(View.INVISIBLE);
      }
      progressBar=(ProgressBar)view.findViewById(R.id.progress_bar);
      if(progressBar!=null){
        if(progressBarVisibility)progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.INVISIBLE);
      }
    }

    //orderStatus receiver
    //broadcastReceiver=((StartActivity)getActivity()).getManager().startOrderStatusBroadcastReceiver();
  }
  public void updateOrderStatus(String status){
    View view=getView();
    if(view!=null){
      textViewOrderStatus=(TextView)view.findViewById(R.id.text_view_order_status);
      if(textViewOrderStatus!=null)textViewOrderStatus.setText(status);
    }
  }
  public void setProgressBarVisibility(boolean visibility){
    View view=getView();
    if(view!=null){
      progressBar=(ProgressBar)view.findViewById(R.id.progress_bar);
      if(progressBar!=null){
        if(visibility)progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.INVISIBLE);
      }
    }
  }
  public void setImageViewVisibility(boolean visibility){
    View view=getView();
    if(view!=null){
      imageView=(ImageView)view.findViewById(R.id.image_view_picture);
      if(imageView!=null){
        if(visibility)imageView.setVisibility(View.VISIBLE);
        else imageView.setVisibility(View.INVISIBLE);
      }
    }
  }
}