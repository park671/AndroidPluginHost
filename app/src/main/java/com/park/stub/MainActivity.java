package com.park.stub;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.park.plugin.dynamic.PluginLoadHelper;
import com.park.plugin.dynamic.util.FileUtil;
import com.park.plugin.dynamic.PluginManager;
import com.park.plugin.dynamic.StubActivity;

public class MainActivity extends Activity {

  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ((TextView) findViewById(R.id.tv)).setText("main 1");
    findViewById(R.id.btn3).setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.setComponent(new ComponentName(getPackageName(), "com.park.plugin.MainActivity"));
      startActivity(intent);
    });

    findViewById(R.id.loadAssetBtn).setOnClickListener(v-> PluginLoadHelper
        .loadPluginInAsset(MainActivity.this, "app-debug.apk"));

    findViewById(R.id.dwnBtn).setOnClickListener(v -> new Thread() {
      @Override
      public void run() {
        File outputFile = FileUtil.createNewFileInCache(MainActivity.this, "serverPlugin.apk");
        NetHelper netHelper = new NetHelper(MainActivity.this);
        try {
          Log.d(TAG, "start connect...");
          netHelper.connect();
          Log.d(TAG, "start downlaod...");
          netHelper.download(new FileOutputStream(outputFile));
          Log.d(TAG, "download complete");
        } catch (Throwable tr) {
          tr.printStackTrace();
        }
        PluginManager.getInstance().loadPath(MainActivity.this, outputFile.getAbsolutePath());
      }
    }.start());
  }

  public void test(View view) {
    startActivity(new Intent(MainActivity.this, Main2Activity.class));
  }

  public void test1(View view) {
    startActivity(new Intent(MainActivity.this, StubActivity.class));
  }
}