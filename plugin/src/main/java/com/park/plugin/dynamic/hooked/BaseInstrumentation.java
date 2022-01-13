package com.park.plugin.dynamic.hooked;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.park.plugin.dynamic.PluginManager;
import com.park.plugin.dynamic.StubActivity;

public class BaseInstrumentation extends Instrumentation {

  private static final String TAG = "BaseInstrumentation";

  private Instrumentation originInst;

  @SuppressLint("all")
  public BaseInstrumentation(Instrumentation originInst, Object activityThread) throws Throwable {
    this.originInst = originInst;
    Field field = Instrumentation.class.getDeclaredField("mThread");
    field.setAccessible(true);
    field.set(this, activityThread);
  }

  boolean needHook = false;
  private String realActivity;

  /**
   * 覆盖掉原始Instrumentation类的对应方法,用于插件内部跳转Activity时适配
   *
   * @Override
   */
  @SuppressLint("all")
  public ActivityResult execStartActivity(
      Context who, IBinder contextThread, IBinder token, Activity target,
      Intent intent, int requestCode, Bundle options) {
    Log.d(TAG, "exec:" + target.getClass().getName() + ", intent = " + intent.getComponent());
    Method execStartActivity;
    try {
      execStartActivity = Instrumentation.class.getDeclaredMethod(
          "execStartActivity", Context.class, IBinder.class, IBinder.class,
          Activity.class, Intent.class, int.class, Bundle.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
      throw new RuntimeException("can not hook");
    }
    execStartActivity.setAccessible(true);
    try {
      return (ActivityResult) execStartActivity.invoke(originInst, who,
          contextThread, token, target, intent, requestCode, options);
    } catch (Exception e) {
      Log.d(TAG, "AMS can't load activity:" + intent.getComponent().getClassName() +
          ", try to load by stub.");
      needHook = true;
      realActivity = intent.getComponent().getClassName();
      try {
        intent.setComponent(
            new ComponentName("com.park.stub", "com.park.plugin.dynamic.StubActivity"));
        return (ActivityResult) execStartActivity.invoke(originInst, who,
            contextThread, token, target, intent, requestCode, options);
      } catch (Throwable tr) {
        tr.printStackTrace();
        throw new RuntimeException("err");
      }
    }
  }

  @Override
  public Activity newActivity(ClassLoader cl, String className, Intent intent)
      throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    Log.d(TAG, "new:" + className + ", intent = " + intent.getComponent());
    if (className.equalsIgnoreCase("com.park.plugin.dynamic.StubActivity") && needHook) {
      Log.d(TAG, "stub was found! try to hook:" + realActivity);
      if (needHook) {
        Class<?> activityClazz = null;
        try {
          activityClazz = Class.forName(realActivity);
          Log.e(TAG, realActivity + " is normal activity! Do you forget to add it to AndroidManifest?");
          return (Activity) activityClazz.newInstance();
        }catch (Throwable ignore){}
        try{
          activityClazz = PluginManager.getInstance().getDexClassLoader().loadClass(realActivity);
          Log.d(TAG, realActivity + " is plugin activity.");
        }catch (Throwable ignore){}
        needHook = false;
        if(activityClazz == null){
          activityClazz = StubActivity.class;
        }
        return (Activity) activityClazz.newInstance();
      }
    }
    return super.newActivity(cl, className, intent);
  }

}
