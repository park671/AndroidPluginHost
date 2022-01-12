package com.park.plugin.dynamic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Instrumentation;

import com.park.plugin.dynamic.hooked.BaseInstrumentation;
import com.park.plugin.dynamic.util.UnsealUtil;

public class HookManager {

  @SuppressLint({"PrivateApi", "DiscouragedPrivateApi"})
  public static void hookInstrumentation() {
    UnsealUtil.exemptAll();
    Class<?> activityThread;
    try {
      activityThread = Class.forName("android.app.ActivityThread");
      Method sCurrentActivityThread = activityThread.getDeclaredMethod("currentActivityThread");
      sCurrentActivityThread.setAccessible(true);
      //获取ActivityThread 对象
      Object activityThreadObject = sCurrentActivityThread.invoke(activityThread);

      //获取 Instrumentation 对象
      Field mInstrumentation = activityThread.getDeclaredField("mInstrumentation");
      mInstrumentation.setAccessible(true);
      Instrumentation instrumentation =
          (Instrumentation) mInstrumentation.get(activityThreadObject);
      BaseInstrumentation customInstrumentation = null;
      try {
        customInstrumentation = new BaseInstrumentation(instrumentation, activityThreadObject);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
      //将我们的 customInstrumentation 设置进去
      mInstrumentation.set(activityThreadObject, customInstrumentation);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
