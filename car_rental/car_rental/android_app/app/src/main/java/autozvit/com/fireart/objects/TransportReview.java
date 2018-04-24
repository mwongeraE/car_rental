package autozvit.com.fireart.objects;

import java.io.Serializable;

public class TransportReview implements Serializable{
  public long order_id;
  public long user_id;
  public long transport_id;
  public String description;
  public byte review_value;
  public String last_update;

  public User user=null;
  public Transport transport=null;

  public String transport_name;//temp value (for rating dialog)
}
