package autozvit.com.fireart.objects;

import java.io.Serializable;

public class OrderABPart implements Serializable{
  public long order_id;
  public long product_id;
  public Product product=null;
  public long product_count;
  public double total_price;
  public String last_update;
}