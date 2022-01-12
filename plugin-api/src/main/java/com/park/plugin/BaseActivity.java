package com.park.plugin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {

  private static final String TAG = "BaseActivity";

  public BaseActivity() {
    Log.e(TAG, "stub!");
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.e(TAG, "stub!");
  }
}
