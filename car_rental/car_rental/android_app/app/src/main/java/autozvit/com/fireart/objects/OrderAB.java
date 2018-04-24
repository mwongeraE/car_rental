package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderAB implements Serializable{
  public long id;//order_id
  public long user_id;
  public long status_id;
  public String status_name;
  public long transport_id;
  public double total_price;
  public int route_distance;
  public int route_duration;
  public String route_data;
  public double order_lat;
  public double order_lon;
  public String order_address;
  public double delivery_lat;
  public double delivery_lon;
  public String delivery_address;
  public long delivery_type_id;
  public String delivery_type_name;
  public String reserved_date;
  public int reserved_hours;
  public double distance;
  public String distance_measure;
  public String create_date;
  public String last_update;

  public User user=null;
  public Transport transport=null;
  public Purchase purchase=null;//last purchase(can be more then one)
  public ArrayList<Purchase> purchaseList=null;
  public Object purchase_list=purchaseList;
  public ArrayList<Attr> statusAttrList=null;
  public Object status_attr_list=statusAttrList;
  public ArrayList<Attr> deliveryTypeAttrList=null;
  public Object delivery_type_attr_list=deliveryTypeAttrList;
  public ArrayList<OrderABPart> orderABPartList=null;
  public Object order_part_list=orderABPartList;
}