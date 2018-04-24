package autozvit.com.fireart.objects;

import java.io.Serializable;

public class RetVal implements Serializable{
  public String name;
  public long value;

  public String status;
  public String message,database_message;
  public long message_code;
  public long session_id;
  public long rows;


  public Object temp_object;//for temporary objects
  public long temp_id;//for temporary identifier(to after callback)
  public long temp_long_value;//temp long value
  public long temp_long_value_2;//temp long value 2
}
