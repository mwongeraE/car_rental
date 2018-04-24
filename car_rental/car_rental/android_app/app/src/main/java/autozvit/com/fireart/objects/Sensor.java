package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Sensor implements Serializable{
  public long id;//sensor_id
  public long user_id;
  public String name;
  public String serial_number;
  public String device_name;
  public String phone;
  public boolean active;
  public String create_date;
  public String last_update;

  public User user=null;
  public ArrayList<SensorCircle> sensorCircleList=null;
  public Object sensor_circle_list=sensorCircleList;
}
