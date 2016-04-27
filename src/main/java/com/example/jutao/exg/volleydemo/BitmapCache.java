package com.example.jutao.exg.volleydemo;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
/**
 * 缓存
 * @author JUTAO
 *
 */
public class BitmapCache implements ImageCache {
	public LruCache<String, Bitmap> cache;
	public int max=10*1024*1024;
	
	public BitmapCache(){
		cache=new LruCache<String, Bitmap>(max){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				
				return value.getRowBytes()*value.getHeight();
				
			}
		};
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		
		return cache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		cache.put(url, bitmap);
	}

}
