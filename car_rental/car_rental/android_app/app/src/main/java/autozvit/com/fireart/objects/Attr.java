package autozvit.com.fireart.objects;

import java.io.Serializable;

public class Attr implements Serializable,Cloneable{
  public long id;
  public String name;
  public String value;
  public String language;
  public String last_update;

  /*clone*/
  public Attr clone() throws CloneNotSupportedException{
    return (Attr)super.clone();
  }
}
