package com.example.jutao.exg.talk.test;


import android.test.AndroidTestCase;
import android.util.Log;
import com.example.jutao.exg.talk.utils.HttpUtils;

public class TestHttpUtils extends AndroidTestCase
{
	public void testSendInfo()
	{
		String res = HttpUtils.doGet("怎么修灯泡");
		Log.e("TAG", res);
		res = HttpUtils.doGet("你好");
		Log.e("TAG", res);
		res = HttpUtils.doGet("呵呵");
		Log.e("TAG", res);
		res = HttpUtils.doGet("你很厉害");
		Log.e("TAG", res);
	}
}
