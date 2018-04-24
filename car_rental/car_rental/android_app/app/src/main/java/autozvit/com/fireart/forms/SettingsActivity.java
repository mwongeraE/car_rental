package autozvit.com.fireart.forms;

import autozvit.com.fireart.R;
import autozvit.com.fireart.forms.object.AppCompatPreferenceActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.support.design.widget.AppBarLayout;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class SettingsActivity extends AppCompatPreferenceActivity{
  private static final boolean ALWAYS_SIMPLE_PREFS=false;//for two-pane view on tablets
  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    //for other thread(service)
    getPreferenceManager().setSharedPreferencesMode(Context.MODE_MULTI_PROCESS);
    setupActionBar();
  }
  private void setupActionBar(){
    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.GINGERBREAD_MR1){
      Toolbar toolbar=new Toolbar(this);

      AppBarLayout appBarLayout=new AppBarLayout(this);
      appBarLayout.addView(toolbar);

      final ViewGroup root=(ViewGroup)findViewById(android.R.id.content);
      final ViewGroup window=(ViewGroup)root.getChildAt(0);
      window.addView(appBarLayout,0);

      setSupportActionBar(toolbar);
      toolbar.setTitleTextColor(getResources().getColor(R.color.app_accent));

      //Show the Up button in the action bar.
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setSubtitle(getString(R.string.settings_subtitle));

      toolbar.setNavigationOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
          onBackPressed();
        }
      });
    }
  }
  @Override
  protected void onPostCreate(Bundle savedInstanceState){
    super.onPostCreate(savedInstanceState);
    setupSimplePreferencesScreen();//one pane view
  }
  private void setupSimplePreferencesScreen(){
    if(!isSimplePreferences(this))return;
    //general pref
    addPreferencesFromResource(R.xml.pref_general);
    //notifications pref
    PreferenceCategory fakeHeader=new PreferenceCategory(this);
    fakeHeader.setTitle(R.string.pref_header_notifications);
    getPreferenceScreen().addPreference(fakeHeader);
    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.GINGERBREAD_MR1){
      addPreferencesFromResource(R.xml.pref_notification);
    }
    //data and sync pref
    fakeHeader=new PreferenceCategory(this);
    fakeHeader.setTitle(R.string.pref_header_data_sync);
    getPreferenceScreen().addPreference(fakeHeader);
    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.GINGERBREAD_MR1){
      addPreferencesFromResource(R.xml.pref_data_sync);
    }
    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.GINGERBREAD_MR1){
      //Bind the summaries
      bindPreferenceSummaryToValue(findPreference(getString(R.string.key_hostname)));
      bindPreferenceSummaryToValue(findPreference(getString(R.string.key_map_provider)));
      bindPreferenceSummaryToValue(findPreference(getString(R.string.key_payment_provider)));
      bindPreferenceSummaryToValue(findPreference(getString(R.string.key_booking_style)));
      bindPreferenceSummaryToValue(findPreference(getString(R.string.key_notifications_new_message_ringtone)));
      bindPreferenceSummaryToValue(findPreference(getString(R.string.key_distance)));
    }
    //change listeners
    /*android.preference.Preference pref=findPreference(getString(R.string.key_notifications_show_tips));
    pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
      @Override
      public boolean onPreferenceChange(Preference preference,Object newValue){
        String str=(String)newValue;
        return true;
      }
    });*/

  }
  @Override
  public boolean onIsMultiPane(){
    return isXLargeTablet(this)&&!isSimplePreferences(this);
  }
  //determine if the device has an extra-large screen
  private static boolean isXLargeTablet(Context context){
    return (context.getResources().getConfiguration().screenLayout&Configuration.SCREENLAYOUT_SIZE_MASK)>=Configuration.SCREENLAYOUT_SIZE_XLARGE;
  }
  //Determines whether the simplified settings UI should be shown
  private static boolean isSimplePreferences(Context context){
    return ALWAYS_SIMPLE_PREFS||Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB||!isXLargeTablet(context);
  }
  @Override
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public void onBuildHeaders(List<Header> target){
    if(!isSimplePreferences(this)){
      loadHeadersFromResource(R.xml.pref_headers,target);
    }
  }
  //A preference value change listener that updates the preference's summary
  private static Preference.OnPreferenceChangeListener bindPreferenceSummaryToValueListener=new Preference.OnPreferenceChangeListener(){
    @Override
    public boolean onPreferenceChange(Preference preference,Object value){
      String stringValue=value.toString();
      if(preference instanceof ListPreference){
        ListPreference listPreference=(ListPreference)preference;
        int index=listPreference.findIndexOfValue(stringValue);
        preference.setSummary(index>=0?listPreference.getEntries()[index]:null);
      }
      else if(preference instanceof RingtonePreference){
        if(TextUtils.isEmpty(stringValue)){
          preference.setSummary(R.string.pref_ringtone_silent);
        }
        else{
          Ringtone ringtone=RingtoneManager.getRingtone(preference.getContext(),Uri.parse(stringValue));
          if(ringtone==null){preference.setSummary(null);}
          else{
            String name=ringtone.getTitle(preference.getContext());
            preference.setSummary(name);
          }
        }
      }
      else{preference.setSummary(stringValue);}
      return true;
    }
  };
  private static void bindPreferenceSummaryToValue(Preference preference){
    preference.setOnPreferenceChangeListener(bindPreferenceSummaryToValueListener);
    bindPreferenceSummaryToValueListener.onPreferenceChange(preference,PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(),""));
  }
  //Make sure to deny any unknown fragments here.
  protected boolean isValidFragment(String fragmentName){
    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.GINGERBREAD_MR1)
      return PreferenceFragment.class.getName().equals(fragmentName)||GeneralPreferenceFragment.class.getName().equals(fragmentName)||DataSyncPreferenceFragment.class.getName().equals(fragmentName)||NotificationPreferenceFragment.class.getName().equals(fragmentName);
    else return false;
  }

  //This fragment shows general preferences
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static class GeneralPreferenceFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState){
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_general);
      setHasOptionsMenu(true);
      bindPreferenceSummaryToValue(findPreference(getString(R.string.key_hostname)));
      bindPreferenceSummaryToValue(findPreference(getString(R.string.key_map_provider)));
      bindPreferenceSummaryToValue(findPreference(getString(R.string.key_payment_provider)));
      bindPreferenceSummaryToValue(findPreference(getString(R.string.key_booking_style)));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
      int id=item.getItemId();
      if(id==android.R.id.home){
        startActivity(new Intent(getActivity(),SettingsActivity.class));
        return true;
      }
      return super.onOptionsItemSelected(item);
    }
  }

  //This fragment shows notification preferences
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static class NotificationPreferenceFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState){
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_notification);
      setHasOptionsMenu(true);
      bindPreferenceSummaryToValue(findPreference(getString(R.string.key_notifications_new_message_ringtone)));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
      int id=item.getItemId();
      if(id==android.R.id.home){
        startActivity(new Intent(getActivity(),SettingsActivity.class));
        return true;
      }
      return super.onOptionsItemSelected(item);
    }
  }

  //This fragment shows data and sync preferences
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static class DataSyncPreferenceFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState){
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.pref_data_sync);
      setHasOptionsMenu(true);
      bindPreferenceSummaryToValue(findPreference(getString(R.string.key_distance)));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
      int id=item.getItemId();
      if(id==android.R.id.home){
        startActivity(new Intent(getActivity(),SettingsActivity.class));
        return true;
      }
      return super.onOptionsItemSelected(item);
    }
  }
}