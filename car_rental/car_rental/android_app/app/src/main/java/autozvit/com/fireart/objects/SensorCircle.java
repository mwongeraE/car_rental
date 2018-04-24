package autozvit.com.fireart.objects;

import java.io.Serializable;

public class SensorCircle implements Serializable{
  public long sensorA_id;//sensor_id
  public long sensorB_id;//sensor_id
  public Sensor sensorA=null;
  public Sensor sensorB=null;
  public String create_date;
}
