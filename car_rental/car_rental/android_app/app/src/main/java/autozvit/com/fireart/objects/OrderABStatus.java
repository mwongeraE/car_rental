package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderABStatus implements Serializable{
  public static final int ORDER_STATUS_CANCELED=-2;
  public static final int ORDER_STATUS_CANCELLATION=-1;
  public static final int ORDER_STATUS_ACCEPTED=1;
  public static final int ORDER_STATUS_PROCESSED=2;
  public static final int ORDER_STATUS_LANDING=3;
  public static final int ORDER_STATUS_DELIVERING=4;
  public static final int ORDER_STATUS_COMPLETED=5;

  public long order_id;
  public long status_id;
  public String name;

  public ArrayList<Attr> attrList=null;
  public Object attr_list=attrList;
}