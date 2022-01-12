package com.park.plugin.dynamic.hooked;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

public class PluginResources extends Resources {

  private static final String TAG = "PluginResources";

  private final Resources originRes;

  public PluginResources(AssetManager assets, DisplayMetrics metrics,
      Configuration config, Resources resources) {
    super(assets, metrics, config);
    this.originRes = resources;
  }

  @Override
  public Drawable getDrawableForDensity(int id, int density, Theme theme) {
    try{
      Drawable drawable = super.getDrawableForDensity(id, density, theme);
      Log.d(TAG, "plugin res:" + id);
      return drawable;
    }catch (Throwable tr){
//      tr.printStackTrace();
    }
    Drawable drawable = originRes.getDrawableForDensity(id, density, theme);
    Log.d(TAG, "main res:" + id);
    return drawable;
  }

}
