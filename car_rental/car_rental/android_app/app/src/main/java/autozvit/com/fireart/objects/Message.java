package autozvit.com.fireart.objects;

import java.io.Serializable;

public class Message implements Serializable{
  public static final int MESSAGE_TYPE_INPUT=1;
  public static final int MESSAGE_TYPE_OUTPUT=2;

  public long id;//message_id
  public int type;
  public long userA_id;//message from userId
  public long userB_id;//message to userId
  public String message;
  public String create_date;

  public User userA;
  public User userB;
  //public String file_ext;
  //public byte[] file_buffer;
}