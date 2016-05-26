package com.example.jutao.exg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import com.example.jutao.exg.view.ZoomImageView;

public class QualificatActivity extends Activity {
  ZoomImageView msivQualificat;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_qualificat);
    msivQualificat= (ZoomImageView) findViewById(R.id.msiv_qualificat);
    Intent intent=getIntent();
    if(intent.getStringExtra("qualificat")!=null){
      msivQualificat.setImageUrl(intent.getStringExtra("qualificat"));
    }
  }
}
