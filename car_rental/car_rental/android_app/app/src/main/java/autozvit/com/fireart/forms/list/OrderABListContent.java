package autozvit.com.fireart.forms.list;

import autozvit.com.fireart.objects.Currency;
import autozvit.com.fireart.objects.OrderAB;

public class OrderABListContent extends ListContent{

  public static class Item extends ListContent.Item{
    private OrderAB orderAB;
    public final OrderAB getOrderAB(){return orderAB;}
    private Currency currency;
    public final Currency getCurrency(){return currency;}
    public Item(OrderAB order,Currency currency){
      orderAB=order;
      this.currency=currency;
    }
    @Override
    public String toString(){
      return orderAB.status_name;
    }
  }
}