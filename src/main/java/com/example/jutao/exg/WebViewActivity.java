package com.example.jutao.exg;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity  extends Activity {
  WebView mWebView;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_webview);
    Intent intent =getIntent();
    String uri=intent.getStringExtra("uri");
    mWebView= (WebView) findViewById(R.id.wv_webview);
    mWebView.getSettings().setSupportZoom(false);
    //      this.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    this.mWebView.getSettings().setJavaScriptEnabled(true);
    this.mWebView.getSettings().setDomStorageEnabled(true);
    //      this.webView.loadUrl("http://weibo.cn/");

    mWebView.setWebViewClient(new WebViewClient() {
      public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //handler.cancel(); 默认的处理方式，WebView变成空白页
        //                        //接受证书
        handler.proceed();
        //handleMessage(Message msg); 其他处理
      }
    });

    mWebView.loadUrl(uri);
  }
}