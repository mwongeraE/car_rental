package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Transport implements Serializable{
  public long id;//transport_id
  public long user_id;
  public long sensor_id;
  public Object picture=null;
  public boolean active;
  public String name;
  public String color;
  public String license_plate;
  public double rate;
  public String last_update;

  public Sensor sensor=null;
  public ArrayList<Type> typeList=null;
  public Object type_list=typeList;
}
