package autozvit.com.fireart.objects;

import java.io.Serializable;

public class User implements Serializable{
  public static final int USER_TYPE_ADMIN=1;
  public static final int USER_TYPE_WORKER=2;
  public static final int USER_TYPE_CUSTOMER=3;

  public long id;
  public int type;
  public String discount_code;
  public Discount discount=null;
  public String first_name;
  public String last_name;
  public String call_name;
  public String email;
  public String phone;
  public String username;//database login
  public String password;//database password
  public Object picture=null;
  public double prepaid_amount=0.00;
  public double rate=0.00;
  public boolean active;
  public String create_date;
  public String last_update;
}