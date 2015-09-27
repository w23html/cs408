package com.morefriends.morefriends;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainPage extends Activity {

    ImageButton ib;
    private CircleImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DrawingView dv = new DrawingView(this);
       // setContentView(dv);
        setContentView(R.layout.activity_main_page);
        iv = (CircleImageView) findViewById(R.id.anon);
        //addListenerOnImage();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void addListenerOnImage() {
        ib = (ImageButton) findViewById(R.id.anon);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // To show the large image
            }
        });

    }



    class DrawingView extends View {

        Bitmap bitmap;

        public DrawingView(Context context) {
            super(context);
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.anon);

        }

        @Override
        public void onDraw(Canvas canvas) {
            Paint paint = new Paint();
            // paint.setColor(Color.CYAN);
            canvas.drawBitmap(getclip(), 30, 20, paint);
        }

        public Bitmap getclip() {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            // paint.setColor(color);
            canvas.drawCircle(bitmap.getWidth() / 2,
                    bitmap.getHeight() / 2,
                    bitmap.getWidth() / 2,
                    paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        }
    }
}
