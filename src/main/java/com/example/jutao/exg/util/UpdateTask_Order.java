package com.example.jutao.exg.util;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jutao.exg.bean.Order_masters;
import com.example.jutao.exg.bean.V_task;
import com.example.jutao.exg.service.CallBack;
import com.example.jutao.exg.volleydemo.MyApplication;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateTask_Order {
  private Context context;
  private V_task v_task;
  private CallBack callBack;

  public UpdateTask_Order(Context context, V_task v_task, CallBack callBack) {
    this.context = context;
    this.v_task = v_task;
    this.callBack = callBack;
    updateTask();
  }

  public void updateTask() {

    try {
      v_task.setStatus("30");
      String jsonString = JSON.toJSONString(v_task);
      JSONObject jsonObject = new JSONObject(jsonString);

      String url = Config.ADRESS + "/service/updateTask";
      JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
          new Response.Listener<JSONObject>() {
            @Override public void onResponse(JSONObject response) {
              try {
                String message=response.getString("message").toString();
                message=new String(message.getBytes("iso-8859-1"), "utf-8");
                Boolean result=response.getBoolean("result");
                int code=response.getInt("code");
                if(callBack!=null){
                  callBack.successed(code,message,result);
                }
              } catch (JSONException e) {
                e.printStackTrace();
              } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
              }
            }
          }, new Response.ErrorListener() {
        @Override public void onErrorResponse(VolleyError error) {
          callBack.Failed();
        }
      });
      request.setTag("abcRequest");
      MyApplication.getHttpQueues().add(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
