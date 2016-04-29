package com.example.jutao.exg.util;

import android.content.Context;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jutao.exg.bean.User;
import com.example.jutao.exg.service.JudgeListener;
import com.example.jutao.exg.volleydemo.MyApplication;
import org.json.JSONObject;

public class JudgeLogin {

  private Context context;
  private String userName;
  private String passWord;
  private JudgeListener judgeListener;

  public JudgeLogin(Context context, String userName, String passWord,JudgeListener judgeListener){
    this.context=context;
    this.userName=userName;
    this.passWord=passWord;
    this.judgeListener=judgeListener;
    VerifyAccount();
  }

  public  void VerifyAccount() {
    User user = new User();
    user.setUserid(userName);
    user.setLogin_password(passWord);

    try {
      String jsonString = JSON.toJSONString(user);
      final JSONObject jsonObject = new JSONObject(jsonString);

      String url = Config.ADRESS+"/service/usercheck";
      JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
          new Response.Listener<JSONObject>() {

            @Override public void onResponse(JSONObject response) {
              try {
                //产生回调，使用钩子
                if(judgeListener!=null){
                  JSONObject object = new JSONObject(response.get("user").toString());
                  ObjectToBean.Userjson(object, context);
                  judgeListener.getJudge(Boolean.valueOf(response.get("result").toString()));

                }


              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }, new Response.ErrorListener() {
        @Override public void onErrorResponse(VolleyError error) {
          //Log.d("Tag", "error");
          Toast.makeText(context,"网络异常",Toast.LENGTH_LONG).show();
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
