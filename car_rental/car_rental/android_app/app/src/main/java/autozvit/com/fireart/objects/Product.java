package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable,Cloneable{
  public long id;
  public long manufacture_id;
  public String discount_code;
  public Discount discount=null;
  public String name;
  public String description;
  public String code;
  public double price;
  public Object picture=null;
  public double rate;
  public long stock_count;
  public String last_update;

  public ArrayList<Attr> attrList=null;
  public Object attr_list=attrList;
  public ArrayList<Type> typeList=null;
  public Object type_list=typeList;
  public ArrayList<ProductParam> paramList=null;
  public Object param_list=paramList;

  public boolean checked=false;//user interface(for select product)
  public int cart_count;//count in user cart(for app only)
  public long reserved_date;//for rental service
  public int reserved_hours;//for rental service

  /*clone*/
  public static ArrayList<ProductParam> cloneList(ArrayList<ProductParam> list){
    ArrayList<ProductParam> clone=null;
    if(list!=null){
      clone=new ArrayList(list.size());
      try{
        for(ProductParam item : list)clone.add(item.clone());
      }catch(CloneNotSupportedException cns_e){}
    }
    return clone;
  }
  public Product clone() throws CloneNotSupportedException{
    Product product=(Product)super.clone();
    product.paramList=cloneList(paramList);
    return product;
  }
}
