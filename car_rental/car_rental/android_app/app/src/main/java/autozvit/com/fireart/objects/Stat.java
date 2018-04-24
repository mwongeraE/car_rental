package autozvit.com.fireart.objects;

import java.io.Serializable;

/*
{"audit":true,"driver_count":376,"client_count":265,"driver_active_sensor_count":52,"client_active_sensor_count":0,"transport_count":58,"manufacture_count":1,"product_count":5,"store_count":2,"order_count":173,"order_completed_count":10,"order_cancelled_count":141,"order_pending_count":22,"order_today_count":2,"order_yesterday_count":1,"order_week_count":7,"order_month_count":7,"order_today_completed_count":0,"order_today_cancelled_count":2,"order_today_pending_count":0,"order_yesterday_completed_count":0,"order_yesterday_cancelled_count":1,"order_yesterday_pending_count":0,"order_total_price":671.73,"purchase_payment_amount":20693.02,"payment_amount":23790.75,"driver_order_count":22,"driver_order_completed_count":13,"driver_order_pending_count":5,"driver_order_total_price":2134.46,"driver_purchase_payment_amount":1087.07,"input_messages_count":5,"output_messages_count":7,"audit_count":"393"}
*/
public class Stat implements Serializable{
  public boolean audit;
  public long driver_count;
  public long client_count;
  public long driver_active_sensor_count;
  public long client_active_sensor_count;
  public long transport_count;
  public long manufacture_count;
  public long product_count;
  public long store_count;
  public long order_count;
  public long order_completed_count;
  public long order_cancelled_count;
  public long order_pending_count;
  public long order_today_count;
  public long order_yesterday_count;
  public long order_week_count;
  public long order_month_count;
  public long order_today_completed_count;
  public long order_today_cancelled_count;
  public long order_today_pending_count;
  public long order_yesterday_completed_count;
  public long order_yesterday_cancelled_count;
  public long order_yesterday_pending_count;
  public double order_total_price;
  public double purchase_payment_amount;
  public double payment_amount;
  public long driver_order_count;
  public long driver_order_completed_count;
  public long driver_order_pending_count;
  public double driver_order_total_price;
  public double driver_purchase_payment_amount;
  public int input_messages_count;
  public int output_messages_count;
  public int audit_count;
}