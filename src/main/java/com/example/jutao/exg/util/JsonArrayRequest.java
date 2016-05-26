package com.example.jutao.exg.util;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import java.io.UnsupportedEncodingException;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * 自己写的一个专门请求Volley  JsonArray的类
 */
public class JsonArrayRequest extends JsonRequest<JSONArray> {
  public JsonArrayRequest(String url, String jsonString, Response.Listener<JSONArray> listener,
      Response.ErrorListener errorListener) {
    super(1, url, jsonString, listener, errorListener);
  }

  protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
    try {
      String je = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
      return Response.success(new JSONArray(je), HttpHeaderParser.parseCacheHeaders(response));
    } catch (UnsupportedEncodingException var3) {
      return Response.error(new ParseError(var3));
    } catch (JSONException var4) {
      return Response.error(new ParseError(var4));
    }
  }
}

