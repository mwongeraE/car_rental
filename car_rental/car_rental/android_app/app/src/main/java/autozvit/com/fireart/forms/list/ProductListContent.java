package autozvit.com.fireart.forms.list;

import autozvit.com.fireart.objects.Currency;
import autozvit.com.fireart.objects.Product;

public class ProductListContent extends ListContent{

  public static class Item extends ListContent.Item{
    private Product product;
    public final Product getProduct(){return product;}
    private Currency currency;
    public final Currency getCurrency(){return currency;}
    public Item(Product product,Currency currency){
      this.product=product;
      this.currency=currency;
    }
    @Override
    public String toString(){
      return product.name+" "+product.description;
    }
  }
}