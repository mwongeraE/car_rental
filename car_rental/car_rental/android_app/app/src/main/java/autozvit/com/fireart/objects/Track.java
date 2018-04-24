package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Track implements Serializable{
  public long id;//track_id
  public long user_id;
  public long sensor_id;
  public long transport_id;
  public long type_id;
  public String type_name;

  public double latitude;
  public double longitude;
  public long track_time;
  public double speed;
  public float accuracy;
  public double altitude;
  public float bearing;
  //device
  public int battery;//battery save
  public int satellites;//number of satellites

  public int timezone_offset;
  public String create_date;

  public Sensor sensor=null;
  public Transport transport=null;
  public ArrayList<Attr> typeAttrList=null;
  public Object type_attr_list=typeAttrList;

  //public ArrayList<TrackPart> trackPartList=null;
  //public Object track_part_list=trackPartList;

  //marker
  public Object marker;//map marker
}

