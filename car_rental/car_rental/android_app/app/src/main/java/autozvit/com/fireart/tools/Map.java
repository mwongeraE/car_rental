package autozvit.com.fireart.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapController;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.mylocation.SimpleLocationOverlay;

/*import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;*/


/*
icon with text -> IconGenerator
http://googlemaps.github.io/android-maps-utils/
dependencies {
    compile 'com.google.maps.android:android-maps-utils:0.4+'
}
*/

import java.util.ArrayList;
import java.util.List;

import autozvit.com.fireart.objects.Direction;

public class Map{
  public static final int MAP_PROVIDER_GOOGLE=1,
                          MAP_PROVIDER_YANDEX=2,//Bad support provider
                          MAP_PROVIDER_OSM=3,
                          MAP_PROVIDER_MAPBOX=4;//Required min Android SDK version 15

  private Context context;
  private View mapView;
  private int mapProvider;
  private boolean myLocation;
  private int zoom;
  private int duration;
  private int padding;

  //google
  private com.google.android.gms.maps.MapView googleMapView=null;
  public com.google.android.gms.maps.MapView getGoogleMapView(){return googleMapView;}
  private GoogleMap googleMap=null;
  public GoogleMap getGoogleMap(){return googleMap;}

  //osmdroid
  private org.osmdroid.views.MapView osmMapView=null;
  public org.osmdroid.views.MapView getOsmMapView(){return osmMapView;}
  private MapController osmMapController=null;
  private SimpleLocationOverlay osmLocationOverlay=null;
  private MyLocationNewOverlay osmMyLocationOverlay=null;
  private ScaleBarOverlay osmScaleBarOverlay=null;

  //mapbox
  /*private String mapboxAccessToken=null;
  public void setMapboxAccessToken(String access_token){mapboxAccessToken=access_token;}
  private Mapbox mapbox=null;
  public Mapbox getMapbox(){return mapbox;}*/

  public class Location{
    public double latitude,longitude;
  }
  public Location newLocationInstance(double lat,double lng){
    Location location=new Location();
    location.latitude=lat;location.longitude=lng;
    return location;
  }
  public interface LocationCallback<Location>{
    void execute(Location location);
  }
  public interface BooleanCallback<Object>{
    boolean execute(Object object);
  }
  public interface ViewCallback<Object>{
    View execute(Object object);
  }

  LocationCallback<Location> onMapClickCallback=null;
  BooleanCallback<Object> onMarkerClickCallback=null;

  /*
    for private constr Map Location class using :
    double lat=0,lng=0;
    Map map=new Map();
    Map.Location location=map.newLocationInstance(lat,lng);
  */
  public Map(){}
  public Map(Context context,View map_view,int map_provider,boolean my_location,int zoom,int duration,int padding){
    this.context=context;
    mapView=map_view;
    mapProvider=map_provider;
    myLocation=my_location;
    this.zoom=zoom;
    this.duration=duration;
    this.padding=padding;
  }
  public void init(final TypedCallback callback)throws Exception{
    if(mapView==null||(mapProvider!=MAP_PROVIDER_GOOGLE&&mapProvider!=MAP_PROVIDER_OSM))
      return;

    if(mapProvider==MAP_PROVIDER_GOOGLE){
      googleMapView=(com.google.android.gms.maps.MapView)mapView;
      googleMapView.onCreate(null);//savedInstanceState
      //googleMapView.onResume();//needed to get the map to display immediately?
      googleMapView.getMapAsync(new OnMapReadyCallback(){
        @Override
        public void onMapReady(GoogleMap google_map){
          try{
            googleMap=google_map;
            googleMap.clear();
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setIndoorEnabled(true);
            googleMap.setBuildingsEnabled(true);
            try{googleMap.setMyLocationEnabled(myLocation);}catch(SecurityException se){}
            if(callback!=null)callback.execute(googleMap);
          }catch(Exception e){}
        }
      });
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      osmMapView=(org.osmdroid.views.MapView)mapView;
      osmMapView.setTileSource(TileSourceFactory.MAPNIK);
      osmMapView.setBuiltInZoomControls(true);
      osmMapView.setMultiTouchControls(true);
      osmMapController=(MapController)osmMapView.getController();
      osmMapController.setZoom(zoom);
      osmScaleBarOverlay=new ScaleBarOverlay(osmMapView);
      osmMapView.getOverlays().add(osmScaleBarOverlay);
      if(myLocation){
        //osmLocationOverlay=new SimpleLocationOverlay(context);
        //osmMapView.getOverlays().add(osmLocationOverlay);
        osmMyLocationOverlay=new MyLocationNewOverlay(new GpsMyLocationProvider(context),osmMapView);
        osmMyLocationOverlay.enableMyLocation();
        osmMyLocationOverlay.enableFollowLocation();
        osmMapView.getOverlays().add(osmMyLocationOverlay);
      }
      osmMapView.invalidate();
      if(callback!=null)callback.execute(null);
    }
    /*else if(mapProvider==MAP_PROVIDER_MAPBOX&&mapboxAccessToken!=null){
      mapbox=Mapbox.getInstance(context,mapboxAccessToken);
      //todo here
    }*/
  }

  //add
  public Object addMarker(Location location,int icon)throws Exception{
    return addMarker(location.latitude,location.longitude,icon);
  }
  public Object addMarker(Location location,int icon,String text)throws Exception{
    return addMarker(location.latitude,location.longitude,icon,text);
  }
  public Object addMarker(double lat,double lng,int icon)throws Exception{
    Object ret_val=null;
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      ret_val=addMarker(lat,lng,BitmapDescriptorFactory.fromResource(icon));
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      ret_val=addMarker(lat,lng,ContextCompat.getDrawable(context,icon));
    }
    return ret_val;
  }
  public Object addMarker(double lat,double lng,int icon,String text)throws Exception{
    Object ret_val=null;
    IconGenerator icon_factory=new IconGenerator(context);
    icon_factory.setBackground(ContextCompat.getDrawable(context,icon));
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      BitmapDescriptor bitmap_descriptior=BitmapDescriptorFactory.fromBitmap(icon_factory.makeIcon(text));
      ret_val=addMarker(lat,lng,bitmap_descriptior);
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      Drawable drawable=new BitmapDrawable(context.getResources(),icon_factory.makeIcon(text));
      ret_val=addMarker(lat,lng,drawable);
    }
    return ret_val;
  }
  private Object addMarker(double lat,double lng,Object icon)throws Exception{
    Object ret_val=null;
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      MarkerOptions marker_options=new MarkerOptions();
      marker_options.position(new LatLng(lat,lng));
      marker_options.icon((BitmapDescriptor)icon);
      com.google.android.gms.maps.model.Marker marker=googleMap.addMarker(marker_options);
      ret_val=marker;
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      org.osmdroid.views.overlay.Marker marker=new org.osmdroid.views.overlay.Marker(osmMapView);
      marker.setIcon((Drawable)icon);
      marker.setPosition(new GeoPoint(lat,lng));

      marker.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER,
      org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM);

      osmMapView.getOverlays().add(marker);
      //set to all new markers
      if(onMarkerClickCallback!=null)osmOnMarkerClickListener(marker,onMarkerClickCallback);
      osmMapView.invalidate();
      ret_val=marker;
    }
    return ret_val;
  }

  public Object addCircle(Location location,int color,int stroke_color,float stroke_width,double radius)throws Exception{
    return addCircle(location.latitude,location.longitude,color,stroke_color,stroke_width,radius);
  }
  public Object addCircle(double lat,double lng,int color,int stroke_color,float stroke_width,double radius)throws Exception{
    Object ret_val=null;
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      CircleOptions circle_options=new CircleOptions();
      circle_options.center(new LatLng(lat,lng)).fillColor(color).strokeColor(stroke_color).strokeWidth(stroke_width).radius(radius);
      ret_val=googleMap.addCircle(circle_options);
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      org.osmdroid.views.overlay.Polygon circle=new org.osmdroid.views.overlay.Polygon(context);
      circle.setPoints(org.osmdroid.views.overlay.Polygon.pointsAsCircle(new GeoPoint(lat,lng),radius));
      circle.setFillColor(color);
      circle.setStrokeColor(stroke_color);
      circle.setStrokeWidth(stroke_width);
      osmMapView.getOverlays().add(circle);
      osmMapView.invalidate();
      ret_val=circle;
    }
    return ret_val;
  }
  public Object addPolyline(ArrayList<Map.Location> list,int color,float width)throws Exception{
    Object ret_val=null;
    if(list==null)return ret_val;
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      PolylineOptions polyline_options=new PolylineOptions();
      polyline_options.color(color).width(width);
      for(Location item:list){
        polyline_options.add(new LatLng(item.latitude,item.longitude));
      }
      ret_val=googleMap.addPolyline(polyline_options);
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      org.osmdroid.views.overlay.Polyline polyline=new org.osmdroid.views.overlay.Polyline(context);
      polyline.setColor(color);
      polyline.setWidth(width);
      List<GeoPoint> points=new ArrayList();
      for(Location item:list){
        points.add(new GeoPoint(item.latitude,item.longitude));
      }
      polyline.setPoints(points);
      osmMapView.getOverlays().add(polyline);
      osmMapView.invalidate();
      ret_val=polyline;
    }
    return ret_val;
  }
  public Object addPolyline(Direction direction,int color,float width)throws Exception{
    Object ret_val=null;
    if(direction==null||direction.steps==null)return ret_val;
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      PolylineOptions polyline_options=new PolylineOptions();
      polyline_options.color(color).width(width);
      Direction last_item=null;
      for(Direction item:direction.steps){
        polyline_options.add(new LatLng(item.startLocationLat,item.startLocationLon));
        last_item=item;
      }
      if(last_item!=null)polyline_options.add(new LatLng(last_item.finishLocationLat,last_item.finishLocationLon));
      ret_val=googleMap.addPolyline(polyline_options);
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      org.osmdroid.views.overlay.Polyline polyline=new org.osmdroid.views.overlay.Polyline(context);
      polyline.setColor(color);
      polyline.setWidth(width);
      List<GeoPoint> points=new ArrayList();
      Direction last_item=null;
      for(Direction item:direction.steps){
        points.add(new GeoPoint(item.startLocationLat,item.startLocationLon));
        last_item=item;
      }
      if(last_item!=null)points.add(new GeoPoint(last_item.finishLocationLat,last_item.finishLocationLon));
      polyline.setPoints(points);
      osmMapView.getOverlays().add(polyline);
      osmMapView.invalidate();
      ret_val=polyline;
    }
    return ret_val;
  }

  //set-get-remove
  public void setMarkerObject(Object marker,Object object){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      ((com.google.android.gms.maps.model.Marker)marker).setTag(object);
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      ((org.osmdroid.views.overlay.Marker)marker).setRelatedObject(object);
    }
  }
  public Object getMarkerObject(Object marker){
    Object object=null;
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      object=((com.google.android.gms.maps.model.Marker)marker).getTag();
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      object=((org.osmdroid.views.overlay.Marker)marker).getRelatedObject();
    }
    return object;
  }
  public Location getMarkerLocation(Object marker){
    return getMarkerPosition(marker);
  }
  public void setPolylineObject(Object polyline,Object object){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      //not for google
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      ((org.osmdroid.views.overlay.Polyline)polyline).setRelatedObject(object);
    }
  }
  public Object getPolylineObject(Object polyline){
    Object object=null;
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      //not for google
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      object=((org.osmdroid.views.overlay.Polyline)polyline).getRelatedObject();
    }
    return object;
  }
  public void setMarkerPosition(Object marker,Location location){
    setMarkerPosition(marker,location.latitude,location.longitude);
  }
  public void setMarkerPosition(Object marker,double lat,double lng){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      ((com.google.android.gms.maps.model.Marker)marker).setPosition(new LatLng(lat,lng));
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      ((org.osmdroid.views.overlay.Marker)marker).setPosition(new GeoPoint(lat,lng));
      osmMapView.invalidate();
    }
  }
  public Location getMarkerPosition(Object marker){//identical to getMarkerLocation
    Location location=null;
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      LatLng position=((com.google.android.gms.maps.model.Marker)marker).getPosition();
      location=newLocationInstance(position.latitude,position.longitude);
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      GeoPoint position=((org.osmdroid.views.overlay.Marker)marker).getPosition();
      location=newLocationInstance(position.getLatitude(),position.getLongitude());
    }
    return location;
  }
  public void setMarkerIcon(Object marker,int icon){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      ((com.google.android.gms.maps.model.Marker)marker).setIcon(BitmapDescriptorFactory.fromResource(icon));
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      ((org.osmdroid.views.overlay.Marker)marker).setIcon(ContextCompat.getDrawable(context,icon));
    }
  }
  public void setMarkerIcon(Object marker,int icon,String text){
    IconGenerator icon_factory=new IconGenerator(context);
    icon_factory.setBackground(ContextCompat.getDrawable(context,icon));
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      BitmapDescriptor bitmap_descriptior=BitmapDescriptorFactory.fromBitmap(icon_factory.makeIcon(text));
      ((com.google.android.gms.maps.model.Marker)marker).setIcon(bitmap_descriptior);
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      Drawable drawable=new BitmapDrawable(context.getResources(),icon_factory.makeIcon(text));
      ((org.osmdroid.views.overlay.Marker)marker).setIcon(drawable);
    }
  }
  public void setCirclePosition(Object circle,double radius,Location location){
    setCirclePosition(circle,location.latitude,radius,location.longitude);
  }
  public void setCirclePosition(Object circle,double radius,double lat,double lng){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      ((com.google.android.gms.maps.model.Circle)circle).setCenter(new LatLng(lat,lng));
      ((com.google.android.gms.maps.model.Circle)circle).setRadius(radius);
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      ((org.osmdroid.views.overlay.Polygon)circle).setPoints(org.osmdroid.views.overlay.Polygon.pointsAsCircle(new GeoPoint(lat,lng),radius));
      osmMapView.invalidate();
    }
  }
  public void setCircleColor(Object circle,int color){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      ((com.google.android.gms.maps.model.Circle)circle).setFillColor(color);
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      ((org.osmdroid.views.overlay.Polygon)circle).setFillColor(color);
    }
  }
  public int getCircleColor(Object circle){
    int color=0;
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      color=((com.google.android.gms.maps.model.Circle)circle).getFillColor();
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      color=((org.osmdroid.views.overlay.Polygon)circle).getFillColor();
    }
    return color;
  }
  public void removeMarker(Object marker){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      ((com.google.android.gms.maps.model.Marker)marker).remove();
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      ((org.osmdroid.views.overlay.Marker)marker).remove(osmMapView);
    }
  }
  public void removeCircle(Object circle){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      ((com.google.android.gms.maps.model.Circle)circle).remove();
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      osmMapView.getOverlays().remove(circle);
    }
  }
  public void removePolyline(Object polyline){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      ((com.google.android.gms.maps.model.Polyline)polyline).remove();
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      osmMapView.getOverlays().remove(polyline);
    }
  }

  //goto location
  public void toLocation(Location location){
    toLocation(location.latitude,location.longitude);
  }
  public void toLocation(double lat,double lng){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      googleMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
      googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)),duration,null);
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      //osmMapController.setCenter(new GeoPoint(lat,lng));
      osmMapController.animateTo(new GeoPoint(lat,lng));
      osmMapController.setZoom(zoom);
      osmMapView.invalidate();
    }
  }
  public void toLocation(Location location1,Location location2){
    toLocation(location1.latitude,location1.longitude,location2.latitude,location2.longitude);
  }
  public void toLocation(double lat1,double lng1,double lat2,double lng2){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      LatLngBounds.Builder builder=new LatLngBounds.Builder();
      builder.include(new LatLng(lat1,lng1));
      builder.include(new LatLng(lat2,lng2));
      googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),padding),duration,null);
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      BoundingBox bounding_box=new BoundingBox(lat1,lng1,lat2,lng2);
      //osmMapView.zoomToBoundingBox(bounding_box,true);//some problems with
      GeoPoint min=new GeoPoint(bounding_box.getLatSouth(),bounding_box.getLonWest());
      GeoPoint max=new GeoPoint(bounding_box.getLatNorth(),bounding_box.getLonEast());
      osmToLocation(min,max);
      osmMapView.invalidate();
    }
  }
  public Location getMapCenter(){
    Location location=null;
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      CameraPosition camera=googleMap.getCameraPosition();
      if(camera!=null&&camera.target!=null)
        location=newLocationInstance(camera.target.latitude,camera.target.longitude);
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      IGeoPoint point=osmMapView.getMapCenter();
      if(point!=null)
        location=newLocationInstance(point.getLatitude(),point.getLongitude());
    }
    return location;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  /*
    OSMDroid5 : can't get zoomToBoundingBox() working correctly
    http://stackoverflow.com/questions/34577385/osmdroid5-cant-get-zoomtoboundingbox-working-correctly
  */
  private void osmToLocation(GeoPoint min,GeoPoint max){
    GeoPoint center=new GeoPoint((max.getLatitudeE6()+min.getLatitudeE6())/2,
                                 (max.getLongitudeE6()+min.getLongitudeE6())/2);
    //diagonale in pixels
    double pixels=Math.sqrt((mapView.getWidth()*mapView.getWidth())+(mapView.getHeight()*mapView.getHeight()));
    final double requiredMinimalGroundResolutionInMetersPerPixel=((double) new GeoPoint(min.getLatitudeE6(),min.getLongitudeE6()).distanceTo(max))/pixels;
    int zoom=calculateZoom(center.getLatitude(),requiredMinimalGroundResolutionInMetersPerPixel,osmMapView.getTileProvider().getMaximumZoomLevel(),osmMapView.getTileProvider().getMinimumZoomLevel());
    osmMapController.setZoom(zoom);
    osmMapController.setCenter(center);
  }
  private int calculateZoom(double latitude,double requiredMinimalGroundResolutionInMetersPerPixel,int maximumZoomLevel,int minimumZoomLevel) {
    for(int zoom=maximumZoomLevel;zoom>=minimumZoomLevel;zoom--){
      if(TileSystem.GroundResolution(latitude,zoom)>requiredMinimalGroundResolutionInMetersPerPixel)
        return zoom;
    }
    return zoom;
  }
  //////////////////////////////////////////////////////////////////////////////////////////////////

  //set on
  public void setOnMapClickListener(final LocationCallback callback){
    onMapClickCallback=callback;
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
        @Override
        public void onMapClick(LatLng latlng){
          Location location=newLocationInstance(latlng.latitude,latlng.longitude);
          if(onMapClickCallback!=null)onMapClickCallback.execute(location);
        }
      });
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      Overlay overlay=new Overlay(context){
        @Override
        public void draw(Canvas c,org.osmdroid.views.MapView osm_view,boolean shadow){
        }
        @Override
        public boolean onSingleTapUp/*Confirmed*/(MotionEvent e,org.osmdroid.views.MapView map_view) {
          Projection projection=map_view.getProjection();
          GeoPoint geoPoint=(GeoPoint)projection.fromPixels((int)e.getX(),(int)e.getY());
          Location location=newLocationInstance((double)geoPoint.getLatitudeE6()/1000000,
                                                (double)geoPoint.getLongitudeE6()/1000000);
          if(onMapClickCallback!=null)onMapClickCallback.execute(location);
          //If you returned true none of the following Overlays or the underlying MapView has the chance to handle this event.
          return true;
        }
      };
      osmMapView.getOverlays().add(overlay);
    }
  }
  public void setOnMarkerClickListener(final BooleanCallback callback){
    onMarkerClickCallback=callback;
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
        @Override
        public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker){
          boolean ret_val=false;
          if(onMarkerClickCallback!=null)ret_val=onMarkerClickCallback.execute(marker);
          return ret_val;
        }
      });
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      for(Overlay item:osmMapView.getOverlays()){
        if(item instanceof org.osmdroid.views.overlay.Marker){
          osmOnMarkerClickListener((org.osmdroid.views.overlay.Marker)item,onMarkerClickCallback);
        }
      }
    }
  }
  private void osmOnMarkerClickListener(org.osmdroid.views.overlay.Marker marker,final BooleanCallback callback){
    (marker).setOnMarkerClickListener(new org.osmdroid.views.overlay.Marker.OnMarkerClickListener(){
      @Override
      public boolean onMarkerClick(org.osmdroid.views.overlay.Marker marker,org.osmdroid.views.MapView map_view){
        boolean ret_val=false;
        if(callback!=null)ret_val=callback.execute(marker);
        return ret_val;
      }
    });
  }

  public void setInfoWindowAdapter(int layoutResId,final ViewCallback callback1,final ViewCallback callback2){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){
        @Override
        public View getInfoWindow(com.google.android.gms.maps.model.Marker marker){
          View ret_val=null;
          if(callback1!=null)ret_val=callback1.execute(marker);
          return ret_val;
        }
        @Override
        public View getInfoContents(com.google.android.gms.maps.model.Marker marker){
          View ret_val=null;
          if(callback2!=null)ret_val=callback2.execute(marker);
          return ret_val;
        }
      });
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      for(Overlay item:osmMapView.getOverlays()){
        if(item instanceof org.osmdroid.views.overlay.Marker){
          ((org.osmdroid.views.overlay.Marker)item).setInfoWindow(new BasicInfoWindow(layoutResId,osmMapView){
            @Override public void onOpen(Object item){
              super.onOpen(item);
              if(callback1!=null)callback1.execute(item);
            }
            @Override public void onClose() {
              super.onClose();
              if(callback2!=null)callback2.execute(null);
            }
          });
        }
      }
    }
  }
  public void setMapStyle(int resource_id){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,resource_id));
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
    }
  }

  //clear and on
  public void clear(){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      if(googleMap!=null)googleMap.clear();
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      if(osmMapView!=null)osmMapView.getOverlays().clear();
    }
  }
  public void onResume(){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      if(googleMapView!=null)googleMapView.onResume();
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
    }
  }
  public void onPause(){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      if(googleMapView!=null)googleMapView.onPause();
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
    }
  }
  public void onDestroy(){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      if(googleMapView!=null)googleMapView.onDestroy();
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
      //if(osmMapView!=null)osmMapView.onDetach();
    }
  }
  public void onLowMemory(){
    if(mapProvider==MAP_PROVIDER_GOOGLE){
      if(googleMapView!=null)googleMapView.onLowMemory();
    }
    else if(mapProvider==MAP_PROVIDER_OSM){
    }
  }
}
/*public void func(){
  if(mapProvider==MAP_PROVIDER_GOOGLE){}
  else if(mapProvider==MAP_PROVIDER_OSM){}
}*/