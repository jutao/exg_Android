package com.ycl.chooseavatar.library;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.jutao.exg.R;
import com.isseiaoki.simplecropview.CropImageView;

import java.io.File;



public class CropImageViewActivity extends Activity {
    public static final String TAG="YCL_CHOOSE_PICTURE";

    private static String YCL_FOLDER_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "ycl/";

    private static int MAX_PARAMS = 0;


    private static int TO_SERVER_IMAGE_HEIGHT = 0;
    private static int TO_SERVER_IMAGE_WIDTH = 0;

    private String TEMP_PIC_NAME = "temp_headImg";

    CropImageView cropImageView;
    ImageView iv_cancel;
    ImageView iv_ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_imageview);

        init();
        MAX_PARAMS=this.getIntent().getIntExtra("maxPx",720);
        String path = this.getIntent().getStringExtra("photo_path");
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        bitmap = ImageTools.rotateBitmap(bitmap, path);
        if (bitmap != null) {
            if (bitmap.getHeight() > bitmap.getWidth()) {
                TO_SERVER_IMAGE_HEIGHT = MAX_PARAMS;
                TO_SERVER_IMAGE_WIDTH = (int) (bitmap.getWidth() * ((float) TO_SERVER_IMAGE_HEIGHT / bitmap.getHeight()));
            } else {
                TO_SERVER_IMAGE_WIDTH = MAX_PARAMS;
                TO_SERVER_IMAGE_HEIGHT = (int) (bitmap.getHeight() * ((float) TO_SERVER_IMAGE_WIDTH / bitmap.getWidth()));
            }
            bitmap = ImageTools.zoomBitmap(bitmap, TO_SERVER_IMAGE_WIDTH, TO_SERVER_IMAGE_HEIGHT);
            cropImageView.setImageBitmap(bitmap);
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_3_4);

        }
    }

    private void init() {
        cropImageView= (CropImageView) findViewById(R.id.cropImageView);
        iv_cancel= (ImageView) findViewById(R.id.iv_cancel);
        iv_ok= (ImageView) findViewById(R.id.iv_ok);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( YCLTools.listener==null){
                    Log.e(TAG,"you should use the medthod YCLTools.getInstance.setOnChoosePictureListener() in your activity");
                }else{
                    YCLTools.listener.OnCancel();
                }

                CropImageViewActivity.this.finish();
            }
        });
        iv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap1 = cropImageView.getCroppedBitmap();
                File file = ImageTools.savePhotoToSDCard(bitmap1, YCL_FOLDER_PATH, TEMP_PIC_NAME);
                bitmap1.recycle();
                if( YCLTools.listener==null){
                    Log.e(TAG,"you should use the medthod YCLTools.getInstance.setOnChoosePictureListener() in your activity");
                }else{
                    YCLTools.listener.OnChoose(file.getAbsolutePath());
                }

                Log.e("test",file.getAbsolutePath());
                CropImageViewActivity.this.finish();
            }
        });
    }


}
