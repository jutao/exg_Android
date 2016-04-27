package com.example.jutao.exg.volleydemo;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import android.content.Context;

public abstract class VolleyInterface {
	
	public Context context;
	public static Listener<String> listener;
	public static ErrorListener errorListener;
	public VolleyInterface(Context context,Listener<String> listener,
			ErrorListener errorListener){
		this.context=context;
		this.listener=listener;
		this.errorListener=errorListener;
	}
	public Listener<String> loadingListener(){
		
		listener=new Listener<String>() {

			@Override
			public void onResponse(String response) {
				onMySuccess(response);
			}
		};
		
		return listener;
		
	}
	
	public ErrorListener errorListener(){
		errorListener=new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				onMyErroe(error);
				
			}
		};
		return errorListener;
	}
	public abstract void onMySuccess(String result);
	public abstract void onMyErroe(VolleyError error);
}
