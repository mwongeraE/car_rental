package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

/*for GoogleAPI object var style*/
/*similar style of orderRoute object in jfa_lib.js*/
public class Direction implements Serializable{
  public int duration;
  public String durationText;
  public int distance;
  public String distanceText;
  public double startLocationLat;
  public double startLocationLon;
  public double finishLocationLat;
  public double finishLocationLon;
  public String startAddress;
  public String finishAddress;

  public ArrayList<Direction> steps=null;
  public String instructions;

  public String data;//not use overview_polyline(use base64 encoding)
}