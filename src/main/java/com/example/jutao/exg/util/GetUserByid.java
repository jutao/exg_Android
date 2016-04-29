package com.example.jutao.exg.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jutao.exg.bean.User;
import com.example.jutao.exg.service.UserListener;
import com.example.jutao.exg.volleydemo.MyApplication;
import org.json.JSONObject;

/**
 * Created by TIAN on 2016/4/29.
 */
public class GetUserByid {
  private Context context;
  private String userName;
  private UserListener userListener;

  public GetUserByid(Context context, String userName, UserListener userListener){
    this.context=context;
    this.userName=userName;
    this.userListener=userListener;
    getUser();
  }

  public  void getUser() {
    String url = Config.ADRESS+"/service/user/userid";
    User user = new User();
    user.setUserid(userName);

    try {
      String jsonString = JSON.toJSONString(user);
      JSONObject jsonObject = new JSONObject(jsonString);

      JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
          new Response.Listener<JSONObject>() {

            @Override public void onResponse(JSONObject jsonObject) {
              Log.d("Tag",jsonObject.toString());
            }
          }, new Response.ErrorListener() {
        @Override public void onErrorResponse(VolleyError error) {
          Log.d("Tag", "error");
          Toast.makeText(context,"网络异常",Toast.LENGTH_LONG).show();

        }
      });
      request.setTag("cbaRequest");
      MyApplication.getHttpQueues().add(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }




}
