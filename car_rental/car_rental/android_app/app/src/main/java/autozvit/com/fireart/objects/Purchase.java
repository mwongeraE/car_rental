package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Purchase implements Serializable{
  public long id;//purchase_id
  public long user_id;
  public String invoice_code;
  public String invoice_date;
  public double total_price;
  public double total_tax;
  public long order_id;
  public String order_date;
  public String order_info;
  public long delivery_id;
  public String delivery_code;
  public long delivery_type_id;
  public String delivery_type_name;
  public String delivery_date;
  public double delivery_price;
  public String payment_date;
  public String payment_info;
  public double payment_amount;
  public String create_date;
  public String last_update;

  public ArrayList<Attr> deliveryTypeAttrList=null;
  public Object delivery_type_attr_list=deliveryTypeAttrList;
}
