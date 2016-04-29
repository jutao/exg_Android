package com.example.jutao.exg.util;

public class Config {
  //public final String ADRESS="http://192.168.0.110:8080/exg";
  //public static final String ADRESS="http://172.18.13.3:8080/exg";
  public static final String ADRESS="http://10.0.3.2:8080/exg";


  /**
   * 判断字符串是否为空
   */
  public static boolean StringNoEmpty(String str) {
    if (str != null && !str.equals("")) return true;
    return false;
  }
}
