package com.park.plugin.dynamic.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;

public class FileUtil {

  public static void copyAssetToCache(Context context, String assetFileName) {
    try {
      File cacheDir = context.getCacheDir();
      if (!cacheDir.exists()) {
        cacheDir.mkdirs();
      }
      File outFile = new File(cacheDir, assetFileName);
      if (!outFile.exists()) {
        outFile.createNewFile();
      }
      InputStream inputStream = context.getAssets().open(assetFileName);
      OutputStream outputStream = new FileOutputStream(outFile);
      byte[] buffer = new byte[1024];
      int len = 0;
      while ((len = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, len);
      }
      outputStream.flush();
      outputStream.close();
      inputStream.close();
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public static File createNewFileInCache(Context context, String fileName){
    try {
      File cacheDir = context.getCacheDir();
      if (!cacheDir.exists()) {
        cacheDir.mkdirs();
      }
      File outFile = new File(cacheDir, fileName);
      if (!outFile.exists()) {
        outFile.createNewFile();
      }
      return outFile;
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return null;
  }
}
