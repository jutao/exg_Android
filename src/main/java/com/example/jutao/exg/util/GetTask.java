package com.example.jutao.exg.util;

import android.content.Context;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jutao.exg.bean.Advertisements;
import com.example.jutao.exg.bean.Task;
import com.example.jutao.exg.bean.Tasks;
import com.example.jutao.exg.service.TaskListener;
import com.example.jutao.exg.volleydemo.MyApplication;
import com.isseiaoki.simplecropview.util.Logger;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetTask {
  private Context context;
  private TaskListener listener;
  private Task task;

  public GetTask(Context context,Task task,TaskListener listener){
    this.context=context;
    this.listener=listener;
    this.task=task;
    getTasks();
  }

  public  void getTasks() {
    String url = Config.ADRESS+"/service/gettask";
    String jsonString = JSON.toJSONString(task);

    try {
      JSONObject jsonObject = new JSONObject(jsonString);
      JsonArrayRequest request=new JsonArrayRequest(url,jsonString,new Response.Listener<JSONArray>() {
        @Override public void onResponse(JSONArray jsonArray) {
          List<Tasks> list= null;
          try {
            list = JSON.parseArray(new String(jsonArray.toString().getBytes("iso-8859-1"), "utf-8"),
                Tasks.class);
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          }
          if(list!=null&&listener!=null){
            listener.getTasks(list);
          }
        }
      }, new Response.ErrorListener() {
        @Override public void onErrorResponse(VolleyError volleyError) {
          Log.d("Tag",volleyError.toString());
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
