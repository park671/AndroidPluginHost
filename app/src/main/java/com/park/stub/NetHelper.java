package com.park.stub;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.content.Context;

public class NetHelper {

  Socket mSocket;
  InputStream mInputStream;
  OutputStream mOutputStream;
  Context mContext;

  public NetHelper(Context context) {
    mContext = context;
  }

  public void connect() throws Throwable {
    mSocket = new Socket(Config.Ip, Config.Port);
    mOutputStream = mSocket.getOutputStream();
    mInputStream = mSocket.getInputStream();
  }

  public void download(FileOutputStream fileOutputStream) throws Throwable{
    int len = 0;
    byte[] buffer = new byte[1024];
    while((len = mInputStream.read(buffer)) != -1){
      fileOutputStream.write(buffer, 0, len);
    }
    fileOutputStream.flush();
    fileOutputStream.close();

  }

}
