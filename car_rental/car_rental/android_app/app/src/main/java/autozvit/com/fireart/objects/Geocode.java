package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

/*for GoogleAPI object var style*/
public class Geocode implements Serializable{
  public String geocodeAddress;
  public String formattedAddress;
  public double locationLat;
  public double locationLon;
  public ArrayList<String> addressComponent;

  /*bounds*/
  public double northeastLat,northeastLon;
  public double southwestLat,southwestLon;
}