package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductParam implements Serializable,Cloneable{
  public long id;
  public long parent_id;
  public String name;
  public String value;
  public Object picture=null;
  public String language;
  public String last_update;

  public String part_value;
  public int part_count;//for order(not used OrderABProductParamPart object)
  public double part_price;

  public ArrayList<Attr> attrList=null;
  public Object attr_list=attrList;

  public boolean checked=false;//user interface(for select productParam)

  /*clone*/
  public static ArrayList<Attr> cloneList(ArrayList<Attr> list){
    ArrayList<Attr> clone=null;
    if(list!=null){
      clone=new ArrayList(list.size());
      try{
        for(Attr item : list)clone.add(item.clone());
      }catch(CloneNotSupportedException cns_e){}
    }
    return clone;
  }
  public ProductParam clone() throws CloneNotSupportedException{
    ProductParam product_param=(ProductParam)super.clone();
    product_param.attrList=cloneList(attrList);
    return product_param;
  }
}
