package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Currency implements Serializable{
  public long id;
  public String name;
  public String description;
  public double value;
  public boolean active;
  public String language;
  public String last_update;

  public ArrayList<Attr> attrList=null;
  public Object attr_list=attrList;

}
