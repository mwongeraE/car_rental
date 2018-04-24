package autozvit.com.fireart.storage;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/*class for http|json queue*/
public class Singleton{
  public static final String TAG="singleton";
  private static Singleton singleton;
  private RequestQueue requestQueue;
  private ImageLoader imageLoader;
  private static Context context;
  private Singleton(Context context){
    this.context=context;
    requestQueue=getRequestQueue();
    imageLoader=new ImageLoader(
      requestQueue,new ImageLoader.ImageCache(){
        private final LruCache<String,Bitmap>cache=new LruCache<String,Bitmap>(20);
        @Override
        public Bitmap getBitmap(String url){return cache.get(url);}
        @Override
        public void putBitmap(String url,Bitmap bitmap){cache.put(url,bitmap);}
      }
    );
  }
  public static synchronized Singleton getInstance(Context context){
    if(singleton==null){singleton=new Singleton(context);}
    return singleton;
  }
  public RequestQueue getRequestQueue(){
    if(requestQueue==null){
      requestQueue=Volley.newRequestQueue(context.getApplicationContext());
    }
    return requestQueue;
  }
  public<T> void addToRequestQueue(Request<T> request){getRequestQueue().add(request);}
  public<T> void addToRequestQueue(Request<T> request,String tag){
    request.setTag(TextUtils.isEmpty(tag)?TAG:tag);
    getRequestQueue().add(request);
  }
  public void cancelPendingRequests(Object tag){
    if(requestQueue!=null){requestQueue.cancelAll(tag);}
  }
  public ImageLoader getImageLoader(){return imageLoader;}
}