package autozvit.com.fireart.other_package.forms;

import android.os.Bundle;

import autozvit.com.fireart.forms.StartActivity;
import autozvit.com.fireart.tools.Manager;

/*

To start app as other package application need next steps completed:

1. rename package name from <package_name> to <package_name.other_package_name>
   in File -> Project Structure -> Flavors -> Application Id = <package_name>.<other_package_name>
   not rename in manifest manifest package="<package_name>"

2. change app_name string parameter in all string.xml files, such as : "Other app"

3. change app icon in manifest
   application android:icon="@drawable/<other_app_icon>"

4. preset true Admin credentials of Your database backend for Admin user in RemoteStorage, such as
   public static final String ADMIN_USERNAME="admin";
   public static final String ADMIN_PASSWORD="admin";

*/

public class StartClientActivity extends StartActivity{
  @Override
  protected void onCreate(Bundle savedInstanceState){
    Manager.TYPE_OF_APP=Manager.CLIENT_APP;
    Manager.VERIFY_PHONE_SIMULATE=true;
    //Manager.TYPE_OF_ORDER_LIST=Manager.LIST_STYLE_ORDER_AB;
    //Manager.TYPE_OF_PRODUCT_LIST=Manager.LIST_STYLE_PRODUCT_DELIVERY;
    super.onCreate(savedInstanceState);
  }
}
