package autozvit.com.fireart.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class TrackArea implements Serializable{
  public String color;
  public String status=null;

  public ArrayList<Track> trackList=null;
  public Object track_list=trackList;

}
