package com.example.jutao.exg.volleydemo;

import java.util.Map;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;

public class VolleyRequest {
	
	public static StringRequest stringRequest;
	public static Context context;
	public static void RequestGet(Context context,String url,String tag,VolleyInterface vif){
		MyApplication.getHttpQueues().cancelAll(tag);
		stringRequest=new StringRequest(Method.GET, url, 
				vif.loadingListener(), vif.errorListener);
		stringRequest.setTag(tag);
		MyApplication.getHttpQueues().add(stringRequest);
		MyApplication.getHttpQueues().start();
	}
	public static void RequestPost(Context context,String url,String tag,VolleyInterface vif,final Map<String,String> hasMap){
		
		MyApplication.getHttpQueues().cancelAll(tag);
		stringRequest=new StringRequest(Method.POST ,url,
				vif.loadingListener(), vif.errorListener)
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return hasMap;
			}
		};
		stringRequest.setTag(tag);
		MyApplication.getHttpQueues().add(stringRequest);
		MyApplication.getHttpQueues().start();
	}
}
