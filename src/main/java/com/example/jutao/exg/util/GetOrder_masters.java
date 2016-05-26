package com.example.jutao.exg.util;

import android.content.Context;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jutao.exg.bean.Order_master;
import com.example.jutao.exg.bean.Order_masters;
import com.example.jutao.exg.service.Order_mastersListener;
import com.example.jutao.exg.volleydemo.MyApplication;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetOrder_masters {
  private Context context;
  private Order_mastersListener listener;
  private Order_master order_master;
  String url;
  public GetOrder_masters(Context context, Order_master order_master, String url,
      Order_mastersListener listener){
    this.context=context;
    this.listener=listener;
    this.order_master=order_master;
    this.url=url;
    getOrder_masters();
  }

  public  void getOrder_masters() {
    String jsonString = JSON.toJSONString(order_master);

    try {
      JSONObject jsonObject = new JSONObject(jsonString);
      JsonArrayRequest request=new JsonArrayRequest(url,jsonString,new Response.Listener<JSONArray>() {
        @Override public void onResponse(JSONArray jsonArray) {
          List<Order_masters> list= null;
          try {
            list = JSON.parseArray(new String(jsonArray.toString().getBytes("iso-8859-1"), "utf-8"),
                Order_masters.class);
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          }
          if(list!=null&&listener!=null){
            listener.getOrder_masters(list);
          }
        }
      }, new Response.ErrorListener() {
        @Override public void onErrorResponse(VolleyError volleyError) {
          Log.d("Tag", volleyError.toString());
          if(listener!=null)
            listener.onFailed();
        }
      });

      request.setTag("cbaRequest");
      MyApplication.getHttpQueues().add(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
