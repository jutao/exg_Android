package com.example.jutao.exg.util;

import android.content.Context;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.jutao.exg.bean.Advertisements;
import com.example.jutao.exg.service.AdvertisemtsListener;
import com.example.jutao.exg.volleydemo.MyApplication;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.json.JSONArray;

public class GetAdvertisements {
  private Context context;
  private AdvertisemtsListener listener;
  public GetAdvertisements(Context context,AdvertisemtsListener listener){
    this.context=context;
    this.listener=listener;
    getAdvertisements();
  }

  public  void getAdvertisements() {
    String url = Config.ADRESS+"/service/advertisements";
    // 第三个参数是请求成功的回调，第四个参数是请求失败的回调
    JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
      @Override public void onResponse(JSONArray jsonArray) {
        List<Advertisements> list= null;
        try {
          list = JSON.parseArray(new String(jsonArray.toString().getBytes("iso-8859-1"), "utf-8"),
              Advertisements.class);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        if(list!=null&&listener!=null){
            listener.getAdvertisemts(list);
        }
      }
    }, new Response.ErrorListener() {
      @Override public void onErrorResponse(VolleyError volleyError) {

      }
    });
    // 设置标签方便寻找
    request.setTag("abcRequest");
    // 将请求加入到队列里
    MyApplication.getHttpQueues().add(request);
  }


}
