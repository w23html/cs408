package com.morefriends.morefriends;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;


public class FullScreenImageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image2);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(0xff000000);
        }
        ImageView iv = (ImageView) findViewById(R.id.iv);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getByteArray("image") != null) {
            byte[] image = getIntent().getExtras().getByteArray("image");
            Bitmap b = BitmapFactory.decodeByteArray(image, 0, image.length);
            iv.setImageBitmap(b);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            finish();
            overridePendingTransition(0, R.anim.fade_out);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.fade_out);
    }

    public void backButton(View v) {
        onBackPressed();
    }

}
