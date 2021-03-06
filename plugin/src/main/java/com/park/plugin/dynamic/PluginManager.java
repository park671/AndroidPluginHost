package com.park.plugin.dynamic;

import java.io.File;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.park.plugin.dynamic.hooked.PluginResources;
import com.park.plugin.dynamic.util.FileUtil;

import dalvik.system.DexClassLoader;

public class PluginManager {

  private static final PluginManager mInstance = new PluginManager();
  private DexClassLoader dexClassLoader;
  private Resources resources;
  private PackageInfo packageInfo;

  public static PluginManager getInstance() {
    return mInstance;
  }

  public PluginManager() {
  }

  public void clearOat(Context context) {
    File file = new File(context.getCacheDir(), "oat");
    FileUtil.deleteDir(file);
  }

  public void loadPath(Context context, String filePath) {
    String path = new File(filePath).getAbsolutePath();
    PackageManager packageManager = context.getPackageManager();
    packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);

    //activity
    ///data/data/com.hongx.plugin/app_dex/plugin.dex
    File dex = context.getDir("dex", Context.MODE_PRIVATE);
    dexClassLoader =
        new DexClassLoader(path, dex.getAbsolutePath(), null, context.getClassLoader());

    //resource
    try {
      AssetManager manager = AssetManager.class.newInstance();
      Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
      //path = /data/data/com.hongx.plugin/app_plugin/plugin.apk
      addAssetPath.invoke(manager, path);
      Resources originRes = context.getResources();
      resources = new PluginResources(manager,
          context.getResources().getDisplayMetrics(),
          context.getResources().getConfiguration(),
          originRes);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Resources getResources() {
    return resources;
  }

  public DexClassLoader getDexClassLoader() {
    return dexClassLoader;
  }

  public PackageInfo getPackageInfo() {
    return packageInfo;
  }
}



