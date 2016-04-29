package com.example.jutao.exg.util;

import android.content.Context;
import java.io.UnsupportedEncodingException;
import org.json.JSONObject;

public class ObjectToBean {
  public static void Userjson(JSONObject object,Context context) throws UnsupportedEncodingException {

    String objectString=new String(object.toString().getBytes("iso-8859-1"),"utf-8");

    PrefUtils.setString(context, "userInfo", objectString);
  }
}
