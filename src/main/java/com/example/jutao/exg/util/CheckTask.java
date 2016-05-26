package com.example.jutao.exg.util;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jutao.exg.bean.Task;
import com.example.jutao.exg.service.CallBack;
import com.example.jutao.exg.volleydemo.MyApplication;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckTask {
  private Context context;
  private CallBack callBack;

  public CheckTask(Context context, CallBack callBack) {
    this.context = context;
    this.callBack = callBack;
    checkTask();
  }

  public void checkTask() {

    try {
      Task task=new Task();
      task.setId(PrefUtils.getString(context,"taskid",""));
      String jsonString = JSON.toJSONString(task);
      JSONObject jsonObject = new JSONObject(jsonString);

      String url = Config.ADRESS + "/service/checktask";
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
