package com.park.plugin.dynamic.hooked;

import android.app.Application;
import android.content.Context;
import androidx.annotation.CallSuper;

import com.park.plugin.dynamic.HookManager;

public class BaseApplication extends Application {

  @CallSuper
  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    HookManager.hookInstrumentation();
  }

}
