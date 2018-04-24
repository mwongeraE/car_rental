package autozvit.com.fireart.forms.list;

import autozvit.com.fireart.objects.Message;

public class MessageListContent extends ListContent{

  public static class Item extends ListContent.Item{
    private Message message;
    public final Message getMessage(){return message;}
    public Item(Message message){
      this.message=message;
    }
    @Override
    public String toString(){
      return message.message;
    }
  }
}