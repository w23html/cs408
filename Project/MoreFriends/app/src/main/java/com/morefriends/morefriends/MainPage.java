package com.morefriends.morefriends;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainPage extends Activity {

    ImageButton ib;
    private CircleImageView iv;
    private String nn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            nn = b.getString("name");
        } else {
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            finish();
        }
        iv = (CircleImageView) findViewById(R.id.anon);
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.start_chat_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainPage.this, ChatSender.class);
                startActivity(i);
            }
        });
        findViewById(R.id.add_friend_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Suppose to add the friend into the friend list
                ParseObject po = new ParseObject("Friend");
                po.put("me", ParseUser.getCurrentUser().getObjectId());
                po.put("friend", nn);
                po.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(MainPage.this, "User added as friend.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainPage.this, "Connection failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
