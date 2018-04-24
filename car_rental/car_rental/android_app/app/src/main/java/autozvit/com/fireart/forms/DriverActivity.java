package autozvit.com.fireart.forms;
import autozvit.com.fireart.R;
import autozvit.com.fireart.objects.Color;
import autozvit.com.fireart.tools.Manager;
import autozvit.com.fireart.tools.T;
import autozvit.com.fireart.tools.TypedCallback;
import de.hdodenhof.circleimageview.CircleImageView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class DriverActivity extends AppCompatActivity implements OnClickListener{
  private final static int version=android.os.Build.VERSION.SDK_INT;

  private Context context;
  private Manager manager;

  private EditText editTextCallname;
  private EditText editTextFirstname;
  private EditText editTextLastname;
  private EditText editTextEmail;
  private EditText editTextPhone;

  private EditText editTextTransportName;
  private EditText editTextLicensePlate;

  private Spinner spinner;
  private ArrayList<Color> colorList=null;
  public ArrayList<Color> getColorList(){return colorList;}
  private int colorPosition=0;
  public int getColorPosition(){return colorPosition;}

  private ImageView imageViewUserPicture;
  private CircleImageView imageViewTransportPicture;

  private final static int PICTURE_TYPE_USER=1,PICTURE_TYPE_TRANSPORT=2;
  private int pictureType=0;//1-user 2-transport

  //callback
  public TypedCallback showColorCallbackHandler=new TypedCallback<Object>(){
    @Override
    public void execute(Object obj){
      /*ArrayList<Color> color_list=(ArrayList)obj;
      Color color;
      if(color_list!=null&&color_list.size()>0)colorList=new ArrayList();
      Iterator iter=color_list.iterator();
      while(iter.hasNext()){
        color=(Color)iter.next();
        if(color!=null)colorList.add(color.name);
      }*/

      colorList=(ArrayList)obj;
      spinner=(Spinner)findViewById(R.id.spinner);
      if(spinner!=null&&colorList!=null&&colorList.size()>0){
        ColorAdapter<Color> adapter=new ColorAdapter(context,android.R.layout.simple_spinner_item,colorList);
        //ColorAdapter<String> adapter=new ColorAdapter(context,android.R.layout_calendar_dialog.simple_spinner_item,colorList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        colorPosition=findColorPosition(manager.getDriverTransportColor());
        spinner.setSelection(colorPosition);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
          @Override
          public void onItemSelected(AdapterView<?> parent,View v,int pos,long id){
            colorPosition=pos;
          }
          @Override
          public void onNothingSelected(AdapterView<?> arg0){
          }
        });
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_driver);

    context=this;
    manager=new Manager(context);
    manager.getPrefData();
    manager.setRemoteStorageErrorFragment(false);
    manager.getColorRequest(showColorCallbackHandler,manager.newInstanceColorList());

    initToolbar();
    initUserdata();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item){
    switch (item.getItemId()){
      case android.R.id.home:
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void initToolbar(){
    final Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
    //toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_48dp);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setSubtitle(getString(R.string.profile_subtitle));
    //getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);
    CollapsingToolbarLayout collapsingToolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);
    collapsingToolbar.setTitle(getString(R.string.profile));
    collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.app_accent));
  }

  private void initUserdata(){
    String str;
    editTextCallname=(EditText)findViewById(R.id.edit_text_callname);
    if(editTextCallname!=null){
      str=manager.getCallname();
      editTextCallname.setText(str!=null?str:T.EMPTY);
      editTextCallname.setHint(R.string.callname);
    }
    editTextFirstname=(EditText)findViewById(R.id.edit_text_firstname);
    if(editTextFirstname!=null){
      str=manager.getFirstname();
      editTextFirstname.setText(str!=null?str:T.EMPTY);
      editTextFirstname.setHint(R.string.firstname);
    }
    editTextLastname=(EditText)findViewById(R.id.edit_text_lastname);
    if(editTextLastname!=null){
      str=manager.getLastname();
      editTextLastname.setText(str!=null?str:T.EMPTY);
      editTextLastname.setHint(R.string.lastname);
    }
    editTextEmail=(EditText)findViewById(R.id.edit_text_email);
    if(editTextEmail!=null){
      str=manager.getEmail();
      editTextEmail.setText(str!=null?str:T.EMPTY);
      editTextEmail.setHint(R.string.email);
    }
    editTextPhone=(EditText)findViewById(R.id.edit_text_phone);
    if(editTextPhone!=null){
      str=manager.getPhone();
      editTextPhone.setText(str!=null?str:T.EMPTY);
      editTextPhone.setHint(R.string.phone);
    }

    editTextTransportName=(EditText)findViewById(R.id.edit_text_transport_name);
    if(editTextTransportName!=null){
      str=manager.getDriverTransportName();
      editTextTransportName.setText(str!=null?str:T.EMPTY);
      editTextTransportName.setHint(R.string.transport_name);
    }
    editTextLicensePlate=(EditText)findViewById(R.id.edit_text_license_plate);
    if(editTextLicensePlate!=null){
      str=manager.getDriverTransportLicensePlate();
      editTextLicensePlate.setText(str!=null?str:T.EMPTY);
      editTextLicensePlate.setHint(R.string.license_plate);
    }

    imageViewUserPicture=(ImageView)findViewById(R.id.image_view_user_picture);
    imageViewTransportPicture=(CircleImageView)findViewById(R.id.image_view_transport_picture);

    //picture loading ...
    if(manager.getUserId()!=-1&&imageViewUserPicture!=null){
      String url=manager.getPictureUrl(T.IMAGE_USER);
      manager.loadImage(url+manager.getUserId(),imageViewUserPicture,null);//no need to save image
    }
    if(manager.getDriverTransportId()!=-1&&imageViewTransportPicture!=null){
      String url=manager.getPictureUrl(T.IMAGE_TRANSPORT);
      manager.loadImage(url+manager.getDriverTransportId(),imageViewTransportPicture,null);//no need to save image
    }
  }

  @Override
  public void onClick(View v){
    try{
      switch(v.getId()){
        case R.id.floating_action_button_photo:
          pictureType=PICTURE_TYPE_USER;
          //Open camera
          manager.openCamera(this);
          break;
        case R.id.image_view_user_picture:
          pictureType=PICTURE_TYPE_USER;
          //Open built-in album
          manager.openPhotoAlbum(this);
          break;
        case R.id.image_view_transport_picture:
          pictureType=PICTURE_TYPE_TRANSPORT;
          //Open built-in album
          manager.openPhotoAlbum(this);
          break;
        case R.id.button_save_user:
          manager.updateUserRequest(this);
          //closing in manager
          //setResult(RESULT_OK,null);finish();
          break;
        case R.id.button_save_transport:
          if(manager.getUsertype()==T.USERTYPE_ADMIN||manager.getUsertype()==T.USERTYPE_DRIVER){
            manager.addOrUpdateDriverTransportRequest(this);
            //closing in manager
            //setResult(RESULT_OK,null);finish();
          }
          else{
            T.messageBox(context,getString(R.string.message_error_title),
                                 getString(R.string.transport_error),
                                 getString(R.string.button_agree));
          }
          break;
      }
    }catch(Exception e){}
  }
  @Override
  protected void onActivityResult(int requestCode,int resultCode,Intent data){
    super.onActivityResult(requestCode,resultCode,data);
    if(requestCode==T.ACTIVITY_CORE_PHOTO||requestCode==T.ACTIVITY_PHOTO_ALBUM){
      if(resultCode==RESULT_OK){
        Uri image_uri=null;
        String path=null,datatype=null;
        if(requestCode==T.ACTIVITY_CORE_PHOTO){
          image_uri=manager.getPhotoFile();
          datatype=MediaStore.Images.ImageColumns.DATA;
        }else if(requestCode==T.ACTIVITY_PHOTO_ALBUM){
          image_uri=data.getData();
          datatype=MediaStore.Images.Media.DATA;
        }
        if(image_uri!=null){
          path=manager.getPathFromUri(image_uri,datatype);
          ImageView image_view=null;
          if(pictureType==PICTURE_TYPE_USER)image_view=(ImageView)findViewById(R.id.image_view_user_picture);
          else if(pictureType==PICTURE_TYPE_TRANSPORT)image_view=(CircleImageView)findViewById(R.id.image_view_transport_picture);
          if(image_view!=null){
            manager.loadImage(image_uri,image_view,null);
          }
        }

        FileInputStream fis=null;
        if(path!=null){
          try{fis=new FileInputStream(path);}catch(FileNotFoundException fnf_e){}
        }
        if(fis!=null){
          ByteArrayOutputStream dos=new ByteArrayOutputStream();
          try{manager.bufferedWriter(fis,dos);}catch(IOException io_e){}
          if(dos.size()>0){
            if(pictureType==PICTURE_TYPE_USER)manager.updateUserPictureRequest(dos.toByteArray());
            else if(pictureType==PICTURE_TYPE_TRANSPORT&&manager.getDriverTransportId()!=-1)manager.updatePictureRequest(dos.toByteArray(),manager.getDriverTransportId(),T.IMAGE_TRANSPORT);
          }
        }
      }
    }
  }
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  /*@SuppressWarnings("deprecation")*/
  private void forceBackground(Context context,View v){
    if(version<Build.VERSION_CODES.JELLY_BEAN){
      v.setBackgroundDrawable(T.getDrawable(context,R.xml.rectangle_white));
    }
    else {
      v.setBackground(T.getDrawable(context,R.xml.rectangle_white));
    }
  }

  //adapter
  private class ColorAdapter<Color> extends ArrayAdapter{
    private LayoutInflater inflater;
    private ArrayList<Color> items;
    public ColorAdapter(Context context,int textViewResourceId,ArrayList<Color> items){
      super(context,textViewResourceId,items);
      inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      this.items=items;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
      String color=((autozvit.com.fireart.objects.Color)items.get(position)).name;
      //String color=items.get(position);
      TextView v=(TextView)super.getView(position, convertView, parent);
      String s=color.toString();
      int c=T.getColor(s);
      if(c!=0){
        v.setText(T.EMPTY);
        //v.setBackgroundDrawable(T.getDrawable(context,R.xml.rectangle_white));
        forceBackground(context,v);
        v.getBackground().setColorFilter(c,PorterDuff.Mode.MULTIPLY);
      }
      else v.setText(s);
      return v;
    }
    @Override
    public View getDropDownView(int position,View convertView,ViewGroup parent){
      String color=((autozvit.com.fireart.objects.Color)items.get(position)).name;
      //String color=items.get(position);
      TextView v=(TextView)super.getDropDownView(position,convertView,parent);
      v.setText(color.toString());
      int c=T.getColor(color.toString());
      if(c!=0){
        v.setTextColor(c);v.setBackgroundColor(c);
      }
      return v;
    }
  }

  private int findColorPosition(String color_name){
    int ret_val=0,index=-1;
    Iterator iter=colorList.iterator();
    Color color;
    while(iter.hasNext()){
      color=(Color)iter.next();
      index++;
      if(color!=null&&color.name.equalsIgnoreCase(color_name)){
        ret_val=index;break;
      }
    }
    return ret_val;
  }
}