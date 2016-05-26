package com.example.jutao.exg.util;

import android.content.Context;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jutao.exg.bean.Order_master;
import com.example.jutao.exg.bean.Order_masters;
import com.example.jutao.exg.bean.Task;
import com.example.jutao.exg.bean.V_task;
import com.example.jutao.exg.service.Order_mastersListener;
import com.example.jutao.exg.service.TaskListListener;
import com.example.jutao.exg.volleydemo.MyApplication;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetTaskLists {
  private Context context;
  private TaskListListener listener;
  private Task task;
  String url;
  public GetTaskLists(Context context, Task task, String url,
      TaskListListener listener){
    this.context=context;
    this.listener=listener;
    this.task=task;
    this.url=url;
    getTasks();
  }

  public  void getTasks() {
    String jsonString = JSON.toJSONString(task);

    try {
      JSONObject jsonObject = new JSONObject(jsonString);
      JsonArrayRequest request=new JsonArrayRequest(url,jsonString,new Response.Listener<JSONArray>() {
        @Override public void onResponse(JSONArray jsonArray) {
          List<V_task> list= null;
          try {
            list = JSON.parseArray(new String(jsonArray.toString().getBytes("iso-8859-1"), "utf-8"),
                V_task.class);
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          }
          if(list!=null&&listener!=null){
            listener.getTasks(list);
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
