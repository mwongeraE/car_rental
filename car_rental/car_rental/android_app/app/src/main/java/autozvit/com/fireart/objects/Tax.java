package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Tax implements Serializable{
  public long id;
  public String code;
  public String name;
  public double value;
  public String language;
  public String last_update;
  public ArrayList<Attr> attrList=null;
  public Object attr_list=attrList;
}
