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

import com.park.plugin.dynamic.util.FileUtil;

public class NetHelper {

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

  public static String downloadVersionProp() throws Throwable{
    URL url = new URL(Config.VersionPropUrl);
    URLConnection urlConnection = url.openConnection();
    InputStream in = urlConnection.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    return reader.readLine();
  }

  public static void downloadByHttps(FileOutputStream fileOutputStream) throws Throwable {
    URL url = new URL(Config.PluginUrl);
    URLConnection urlConnection = url.openConnection();
    InputStream in = urlConnection.getInputStream();
    FileUtil.copy(in, fileOutputStream);
    fileOutputStream.flush();
    fileOutputStream.close();
  }

}
