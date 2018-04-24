package autozvit.com.fireart.objects;

import java.io.Serializable;

public class Taximeter implements Serializable{
  public int wait_time;//wait for client
  public int idle_time;//idle in driving
  public float distance;

  public long start_time;//order started
  public long finish_time;//order finished
}