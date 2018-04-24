package autozvit.com.fireart.forms;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import autozvit.com.fireart.R;
import autozvit.com.fireart.objects.Stat;
import autozvit.com.fireart.tools.Manager;

public class StatFragment extends Fragment{

  private Context context;
  private Manager manager;
  private Stat stat;

  private TextView textView;

  public static StatFragment newInstance(){
    StatFragment fragment=new StatFragment();
    fragment.setArguments(null);
    return fragment;
  }
  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    //if(getArguments()!=null){}
  }
  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_stat,container,false);
  }
  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    context=getActivity();
    manager=((StartActivity)context).getManager();
    stat=manager.getStat();
    View view=getView();
    if(view!=null&&stat!=null){
      //today
      textView=((TextView)view.findViewById(R.id.text_view_order_today_completed_value));
      if(textView!=null)textView.setText(String.valueOf(stat.order_today_completed_count));
      textView=((TextView)view.findViewById(R.id.text_view_order_today_cancelled_value));
      if(textView!=null)textView.setText(String.valueOf(stat.order_today_cancelled_count));
      textView=((TextView)view.findViewById(R.id.text_view_order_today_pending_value));
      if(textView!=null)textView.setText(String.valueOf(stat.order_today_pending_count));
      //yesterday
      textView=((TextView)view.findViewById(R.id.text_view_order_yesterday_completed_value));
      if(textView!=null)textView.setText(String.valueOf(stat.order_yesterday_completed_count));
      textView=((TextView)view.findViewById(R.id.text_view_order_yesterday_cancelled_value));
      if(textView!=null)textView.setText(String.valueOf(stat.order_yesterday_cancelled_count));
      textView=((TextView)view.findViewById(R.id.text_view_order_yesterday_pending_value));
      if(textView!=null)textView.setText(String.valueOf(stat.order_yesterday_pending_count));
      //alltime
      textView=((TextView)view.findViewById(R.id.text_view_order_alltime_completed_value));
      if(textView!=null)textView.setText(String.valueOf(stat.order_completed_count));
      textView=((TextView)view.findViewById(R.id.text_view_order_alltime_cancelled_value));
      if(textView!=null)textView.setText(String.valueOf(stat.order_cancelled_count));
      textView=((TextView)view.findViewById(R.id.text_view_order_alltime_pending_value));
      if(textView!=null)textView.setText(String.valueOf(stat.order_pending_count));
      //payment
      textView=((TextView)view.findViewById(R.id.text_view_order_total_price_value));
      if(textView!=null)textView.setText(manager.getPrice(stat.order_total_price));
      textView=((TextView)view.findViewById(R.id.text_view_order_purchase_amount_value));
      if(textView!=null)textView.setText(manager.getPrice(stat.purchase_payment_amount));
      textView=((TextView)view.findViewById(R.id.text_view_order_payment_amount_value));
      if(textView!=null)textView.setText(manager.getPrice(stat.payment_amount));
      //count
      textView=((TextView)view.findViewById(R.id.text_view_order_count_today_value));
      if(textView!=null)textView.setText(String.valueOf(stat.order_today_count));
      textView=((TextView)view.findViewById(R.id.text_view_order_count_yesterday_value));
      if(textView!=null)textView.setText(String.valueOf(stat.order_yesterday_count));
      textView=((TextView)view.findViewById(R.id.text_view_order_count_week_value));
      if(textView!=null)textView.setText(String.valueOf(stat.order_week_count));
      textView=((TextView)view.findViewById(R.id.text_view_order_count_month_value));
      if(textView!=null)textView.setText(String.valueOf(stat.order_month_count));
      textView=((TextView)view.findViewById(R.id.text_view_order_count_alltime_value));
      if(textView!=null)textView.setText(String.valueOf(stat.order_count));
      //driver
      textView=((TextView)view.findViewById(R.id.text_view_driver_total_price_value));
      if(textView!=null)textView.setText(manager.getPrice(stat.driver_order_total_price));
      textView=((TextView)view.findViewById(R.id.text_view_driver_purchase_amount_value));
      if(textView!=null)textView.setText(manager.getPrice(stat.driver_purchase_payment_amount));
      textView=((TextView)view.findViewById(R.id.text_view_driver_alltime_completed_value));
      if(textView!=null)textView.setText(String.valueOf(stat.driver_order_completed_count));
      textView=((TextView)view.findViewById(R.id.text_view_driver_alltime_pending_value));
      if(textView!=null)textView.setText(String.valueOf(stat.driver_order_pending_count));
      textView=((TextView)view.findViewById(R.id.text_view_driver_count_alltime_value));
      if(textView!=null)textView.setText(String.valueOf(stat.driver_order_count));
      //service
      textView=((TextView)view.findViewById(R.id.text_view_service_messages_input_value));
      if(textView!=null)textView.setText(String.valueOf(stat.input_messages_count));
      textView=((TextView)view.findViewById(R.id.text_view_service_messages_output_value));
      if(textView!=null)textView.setText(String.valueOf(stat.output_messages_count));
      textView=((TextView)view.findViewById(R.id.text_view_service_messages_audit_value));
      if(textView!=null)textView.setText(String.valueOf(stat.audit_count));
      textView=((TextView)view.findViewById(R.id.text_view_service_client_value));
      if(textView!=null)textView.setText(String.valueOf(stat.client_count));
      textView=((TextView)view.findViewById(R.id.text_view_service_driver_value));
      if(textView!=null)textView.setText(String.valueOf(stat.driver_count));
      textView=((TextView)view.findViewById(R.id.text_view_service_active_client_value));
      if(textView!=null)textView.setText(String.valueOf(stat.client_active_sensor_count));
      textView=((TextView)view.findViewById(R.id.text_view_service_active_driver_value));
      if(textView!=null)textView.setText(String.valueOf(stat.driver_active_sensor_count));
      textView=((TextView)view.findViewById(R.id.text_view_service_transport_value));
      if(textView!=null)textView.setText(String.valueOf(stat.transport_count));
    }
  }
}