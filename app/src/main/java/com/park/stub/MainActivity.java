package com.park.stub;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.park.plugin.dynamic.PluginLoadHelper;
import com.park.plugin.dynamic.PluginManager;
import com.park.plugin.dynamic.StubActivity;
import com.park.plugin.dynamic.util.FileUtil;

public class MainActivity extends Activity {

  private static final String TAG = "MainActivity";

  private TextView mTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mTextView = findViewById(R.id.tv);
    ((TextView) findViewById(R.id.tv)).setText("main 1");
    findViewById(R.id.btn3).setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.setComponent(new ComponentName(getPackageName(), "com.park.plugin.MainActivity"));
      startActivity(intent);
    });

    findViewById(R.id.loadAssetBtn).setOnClickListener(v -> PluginLoadHelper
        .loadPluginInAsset(MainActivity.this, "app-debug.apk"));

    findViewById(R.id.dwnBtn).setOnClickListener(v -> new Thread() {
      @Override
      public void run() {
        File outputFile = FileUtil.createNewFileInCache(MainActivity.this, "serverPlugin.apk");
        try {
          Log.d(TAG, "start connect...");
          NetHelper.connect();
          Log.d(TAG, "start downlaod...");
          NetHelper.downloadByServer(new FileOutputStream(outputFile));
          Log.d(TAG, "download complete");
        } catch (Throwable tr) {
          tr.printStackTrace();
        }
        PluginManager.getInstance().loadPath(MainActivity.this, outputFile.getAbsolutePath());
      }
    }.start());
    startCheckByHttps();
  }

  private void setMsg(String msg) {
    runOnUiThread(() -> mTextView.setText(
        mTextView.getText().toString() + "\n" + msg));
  }

  public void startCheckByHttps() {
    new Thread() {
      @Override
      public void run() {
        setMsg("start check version.");
        File versionFile = new File(getCacheDir(), "version.prop");
        String nativeVersion = getNativeVersion(versionFile);
        String serverVersion = getServerVersion();
        setMsg("native:" + nativeVersion + ", server:" + serverVersion);

        if (nativeVersion == null ||
            (!nativeVersion.equalsIgnoreCase(serverVersion) && !TextUtils.isEmpty(serverVersion))) {
          PluginManager.getInstance().clearOat(MainActivity.this);
          File outputFile = FileUtil.createNewFileInCache(MainActivity.this, "serverPlugin.apk");
          try {
            setMsg("start download by https.");
            FileUtil.clearAndWriteFile(versionFile, serverVersion);
            NetHelper.downloadByHttps(new FileOutputStream(outputFile));
            setMsg("download success.");
          } catch (Throwable tr) {
            setMsg("download fail:" + tr.getMessage());
            tr.printStackTrace();
          }
        } else {
          setMsg("no need to download.");
        }
        PluginManager.getInstance().loadPath(MainActivity.this,
            new File(getCacheDir(), "serverPlugin.apk").getAbsolutePath());
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(getPackageName(), "com.park.plugin.MainActivity"));
        startActivity(intent);
      }

      private String getServerVersion() {
        String serverVersion = "";
        try {
          serverVersion = NetHelper.downloadVersionProp();
        } catch (Throwable throwable) {
          throwable.printStackTrace();
        }
        return serverVersion;
      }

      private String getNativeVersion(File versionFile) {
        String nativeVersion = "";
        try {
          if (!versionFile.exists()) {
            versionFile.createNewFile();
          } else {
            nativeVersion = FileUtil.getFileFirstLine(versionFile);
          }
        } catch (Throwable throwable) {
          throwable.printStackTrace();
        }
        return nativeVersion;
      }
    }.start();
  }

  public void test(View view) {
    startActivity(new Intent(MainActivity.this, Main2Activity.class));
  }

  public void test1(View view) {
    startActivity(new Intent(MainActivity.this, StubActivity.class));
  }
}