package com.example.jutao.exg.util;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jutao.exg.service.JudgeListener;
import com.example.jutao.exg.volleydemo.MyApplication;
import org.json.JSONObject;

public class UpdateUser {
  private Context context;
  private JudgeListener judgeListener;

  public UpdateUser(Context context,JudgeListener judgeListener){
    this.context=context;
    this.judgeListener=judgeListener;
    VerifyAccount();
  }

  public  void VerifyAccount() {

    try {
      String jsonString = JSON.toJSONString(MyApplication.user);
      final JSONObject jsonObject = new JSONObject(jsonString);

      String url = Config.ADRESS+"/service/updateuser";
      JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
          new Response.Listener<JSONObject>() {

            @Override public void onResponse(JSONObject response) {
              try {
                //产生回调，使用钩子
                if(judgeListener!=null){
                  judgeListener.getJudge(Boolean.valueOf(response.get("result").toString()));
                }


              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }, new Response.ErrorListener() {
        @Override public void onErrorResponse(VolleyError error) {
          //Log.d("Tag", "error");
          if(judgeListener!=null)
            judgeListener.Failed();

        }
      });
      request.setTag("cbaRequest");
      MyApplication.getHttpQueues().add(request);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }




}
