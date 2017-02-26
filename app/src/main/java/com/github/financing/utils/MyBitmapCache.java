package com.github.financing.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/********************************************
 * 作者：Administrator
 * 时间：2017/2/26
 * 描述：
 *******************************************/
public class MyBitmapCache implements ImageLoader.ImageCache{

    private LruCache<String, Bitmap> mLruCache;
    int maxSize = 1 * 1024 * 1024;
    public MyBitmapCache() {
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }
    @Override
    public Bitmap getBitmap(String url) {
        return mLruCache.get(url);
    }
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mLruCache.put(url, bitmap);
    }
}
