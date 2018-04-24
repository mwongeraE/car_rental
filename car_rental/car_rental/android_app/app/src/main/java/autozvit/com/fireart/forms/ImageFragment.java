package autozvit.com.fireart.forms;
import autozvit.com.fireart.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends Fragment{
  private static final String ARG_PARAM="param";
  private byte[] param;
  private ImageView imageView;
  public static ImageFragment newInstance(byte[] param){
    ImageFragment fragment=new ImageFragment();
    Bundle args=new Bundle();
    args.putByteArray(ARG_PARAM,param);
    fragment.setArguments(args);
    return fragment;
  }
  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    if(getArguments()!=null){
      param=getArguments().getByteArray(ARG_PARAM);
    }
  }
  @Override
  public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
    return inflater.inflate(R.layout.fragment_image,container,false);
  }
  @Override
  public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
    View view=getView();
    if(view!=null){
      imageView=((ImageView)view.findViewById(R.id.image_view_picture));
      Bitmap image=BitmapFactory.decodeByteArray(param,0,param.length);
      if(imageView!=null){
        imageView.setImageBitmap(image);
        imageView.invalidate();
        /*BitmapDrawable bitmapDrawable=new BitmapDrawable(image);
         imageView.setImageDrawable(bitmapDrawable);*/
      }
    }
  }
}
