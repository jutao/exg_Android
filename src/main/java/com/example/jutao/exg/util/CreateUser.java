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
import java.io.UnsupportedEncodingException;
import org.json.JSONObject;

/**
 * Created by TIAN on 2016/4/27.
 */
public class CreateUser {
  private static Context context;
  private String userName;
  private String passWord;
  private JudgeListener judgeListener;

  public CreateUser(Context context, String userName, String passWord,JudgeListener judgeListener) {
    this.context = context;
    this.userName = userName;
    this.passWord = passWord;
    this.judgeListener=judgeListener;
    createUser();
  }

  public void createUser() {
    User user = new User();
    user.setUserid(userName);
    user.setLogin_password(passWord);

    try {
      String jsonString = JSON.toJSONString(user);
      JSONObject jsonObject = new JSONObject(jsonString);

      String url = Config.ADRESS+"/service/createuser";
      JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
          new Response.Listener<JSONObject>() {
            @Override public void onResponse(JSONObject response) {
              try {
                String code = response.get("code").toString();
                if (code.equals("-1")) {
                  Toast.makeText(context, "该号码已经被注册了", Toast.LENGTH_LONG).show();
                } else if (code.equals("0")) {
                  Toast.makeText(context, "注册失败", Toast.LENGTH_LONG).show();
                } else if (code.equals("1")) {

                  PrefUtils.setString(context,"username",userName);
                  PrefUtils.setString(context, "password", passWord);
                  if(judgeListener!=null){
                    JSONObject object = new JSONObject(response.get("user").toString());
                    ObjectToBean.Userjson(object, context);
                    judgeListener.getJudge(true);
                  }

                }
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }, new Response.ErrorListener() {
        @Override public void onErrorResponse(VolleyError error) {
          //Log.d("Tag", "error");
          Toast.makeText(context, "网络异常", Toast.LENGTH_LONG).show();
        }
      });
      request.setTag("abcRequest");
      MyApplication.getHttpQueues().add(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
