package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Discount implements Serializable{
  public static final int DISCOUNT_IN_PERCENT=1;
  public static final int DISCOUNT_IN_VALUE=2;
  public static final int INCREASE_IN_PERCENT=3;
  public static final int INCREASE_IN_VALUE=4;

  public long id;
  public long product_type_id;
  public int type;
  public String code;
  public String name;
  public double value;
  public String language;
  public String start_date;
  public String finish_date;
  public String create_date;
  public String last_update;

  public ArrayList<Attr> attrList=null;
  public Object attr_list=attrList;
}
