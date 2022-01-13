package com.park.stub;

public class Config {

  public static final String Ip = "172.26.192.86";

  public static final String[] PrefixUrl = new String[]{
      "https://raw.githubusercontent.com/youngpark671/AndroidPluginBundle",
      "https://github.com/youngpark671/AndroidPluginBundle/raw",
      "https://gitee.com/youngpark/AndroidPluginBundle/raw"
  };

  public static final String PluginUrl = "/main/app/build/intermediates/apk/debug/app-debug.apk";
  public static final String VersionPropUrl = "/main/plugin_version.properties";

  public static int getAllUrlMode(){
    return PrefixUrl.length;
  }

  public static String getPrefixUrl(int urlMode) {
    return PrefixUrl[urlMode];
  }

  public static String getPluginUrl(int urlMode) {
    return getPrefixUrl(urlMode) + PluginUrl;
  }

  public static String getVersionPropUrl(int urlMode) {
    return getPrefixUrl(urlMode) + VersionPropUrl;
  }

  public static final int Port = 6710;

}
