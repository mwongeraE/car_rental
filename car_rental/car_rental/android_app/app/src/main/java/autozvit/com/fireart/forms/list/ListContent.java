package autozvit.com.fireart.forms.list;

import java.util.ArrayList;
import java.util.List;

public class ListContent{
  private List<Item> itemList=new ArrayList<Item>();
  public List<Item> getItemList(){return itemList;}
  public void clearItemList(){itemList.clear();}
  public void setItemList(ArrayList list){itemList=list;}
  public void addItem(Item item){itemList.add(item);}
  public static class Item{
    public long id;
    public byte[] picture=null;
    public Item(){}
    @Override
    public String toString(){return String.valueOf(id);}
  }
}