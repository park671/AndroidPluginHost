package com.park.plugin.dynamic;

import android.content.Context;

import com.park.plugin.dynamic.util.FileUtil;

public class PluginLoadHelper {

  public static void loadPluginInAsset(Context context, String name) {
    new Thread() {
      @Override
      public void run() {
        FileUtil.copyAssetToCache(context, name);
        PluginManager.getInstance()
            .loadPath(context, context.getCacheDir().getAbsolutePath() + "/" + name);
      }
    }.start();

  }


}
