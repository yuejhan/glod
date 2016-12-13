package com.github.financing.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.github.financing.R;
import com.github.financing.base.BaseActivity;

/********************************************
 * 作者：Administrator
 * 时间：2016/12/9
 * 描述：
 *******************************************/
public class Test extends BaseActivity {
    private ImageView image;
    int imageWidth;
    int imageHeight ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        image = (ImageView) this.findViewById(R.id.test_image);
        image.post(new Runnable(){

            @Override
            public void run() {
                imageWidth = image.getWidth();
                imageHeight = image.getHeight();
            }
        });

        Log.i("test",image.getWidth()+"");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.eee,options);
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;

        int radioW = imageWidth / outWidth;
        int radioH = imageHeight / outHeight;
        if(radioH > radioW){
            //TODO:剪切
        }
        options.inSampleSize = radioW;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eee, options);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, imageHeight, false);
        if(scaledBitmap != bitmap){
            bitmap.recycle();
        }

        image.setImageBitmap(scaledBitmap);
    }
}
