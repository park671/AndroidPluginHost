package com.park.stub;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import com.park.plugin.dynamic.util.FileUtil;

public class NetHelper {

  private static final String TAG = "NetHelper";

  static Socket mSocket;
  static InputStream mInputStream;
  static OutputStream mOutputStream;

  public static void connect() throws Throwable {
    mSocket = new Socket(Config.Ip, Config.Port);
    mOutputStream = mSocket.getOutputStream();
    mInputStream = mSocket.getInputStream();
  }

  public static void downloadByServer(FileOutputStream fileOutputStream) throws Throwable {
    FileUtil.copy(mInputStream, fileOutputStream);
    fileOutputStream.flush();
    fileOutputStream.close();
  }

  public static String downloadVersionProp() throws Throwable {
    Throwable tr = new RuntimeException("unknown");
    for (int i = 0; i <= Config.getAllUrlMode(); i++) {
      try {
        String verProp = downloadVersionProp(i);
        Log.d(TAG, "use mode = " + i);
        return verProp;
      } catch (Throwable throwable) {
        tr = throwable;
      }
    }
    throw tr;
  }

  public static String downloadVersionProp(int mode) throws Throwable {
    URL url = new URL(Config.getVersionPropUrl(mode));
    URLConnection urlConnection = url.openConnection();
    InputStream in = urlConnection.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    return reader.readLine();
  }

  public static void downloadByHttps(FileOutputStream fileOutputStream) throws Throwable {
    Throwable tr = new RuntimeException("unknown");
    for (int i = 0; i <= Config.getAllUrlMode(); i++) {
      try {
        downloadByHttps(fileOutputStream, i);
        Log.d(TAG, "use mode = " + i);
        return;
      } catch (Throwable throwable) {
        tr = throwable;
      }
    }
    throw tr;
  }

  public static void downloadByHttps(FileOutputStream fileOutputStream, int mode) throws Throwable {
    URL url = new URL(Config.getPluginUrl(mode));
    URLConnection urlConnection = url.openConnection();
    InputStream in = urlConnection.getInputStream();
    FileUtil.copy(in, fileOutputStream);
    fileOutputStream.flush();
    fileOutputStream.close();
  }

}
