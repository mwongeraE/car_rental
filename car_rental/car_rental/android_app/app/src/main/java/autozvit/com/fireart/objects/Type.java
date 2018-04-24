package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Type implements Serializable{
  public long id;
  public long parent_id;
  public String name;
  public String description;
  public String language;
  public String last_update;

  public ArrayList<Attr> attrList=null;
  public Object attr_list=attrList;
}
