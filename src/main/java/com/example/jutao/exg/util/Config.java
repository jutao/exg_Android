package com.example.jutao.exg.util;

public class Config {


  //public static final String ADRESS = "http://192.168.1.102:8080/exg";
  public static final String ADRESS = "http://192.168.21.105:8080/exg";
  //public static final String ADRESS = "http://192.168.0.109:8080/exg";
  //public static final String ADRESS="http://172.18.13.3:8080/exg";
  //public static final String ADRESS="http://10.0.3.2:8080/exg";

  public static final String AccessKey = "ISAnY7RsRru1E_kyQjsC2YgzYnFT_oKJqTJsL_Hg";
  public static final String SecretKey = "SHUgWm0j_5OAyTythxJAJFdaNUI8V7koHYPjWwCT";
  public static final String ZONE="http://7xthjd.com1.z0.glb.clouddn.com/";
  /**
   * 判断字符串是否为空
   */
  public static boolean StringNoEmpty(String str) {
    if (str != null && !str.equals("")) return true;
    return false;
  }

}
