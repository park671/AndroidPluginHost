package com.park.plugin;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.park.plugin.dynamic.PluginManager;

public class BaseActivity extends Activity {

  private static final String TAG = "BaseActivity";

  @SuppressLint("all")
  public BaseActivity() {
    try {
      Field field = ContextThemeWrapper.class.getDeclaredField("mResources");
      field.setAccessible(true);
      field.set(this, PluginManager.getInstance().getResources());
    } catch (Throwable e) {
      e.printStackTrace();
    }
    Log.d(TAG, "res was change to PluginResources");
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    for (ActivityInfo activityInfo : PluginManager.getInstance().getPackageInfo().activities) {
      if (activityInfo.name.equalsIgnoreCase(this.getClass().getName())) {
        String label = activityInfo.loadLabel(getPackageManager()).toString();
        setTitle(label);
      }
    }
  }
}
