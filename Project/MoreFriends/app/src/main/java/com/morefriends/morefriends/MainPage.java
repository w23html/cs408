package com.morefriends.morefriends;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainPage extends Activity {

    ImageButton ib;
    private CircleImageView iv;
    private String nn;
    private TextView friendname;
    private TextView email;
    private TextView msg;

    private String fname;

    public void loadUserProfile(String nn) {
        final ProgressDialog dialog = new ProgressDialog(MainPage.this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading user profile");
        dialog.show();
        ParseQuery<ParseUser> query = new ParseUser().getQuery();
        query.whereEqualTo("objectId", nn);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    final ParseUser pu = objects.get(0);
                    if (pu.getBytes("image") != null) {
                        ParseFile pf = pu.getParseFile("full_image");
                        pf.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e != null) {
                                    Bitmap b = BitmapFactory.decodeByteArray(pu.getBytes("image"), 0, pu.getBytes("image").length);
                                    iv.setImageBitmap(b);
                                } else {
                                    Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    iv.setImageBitmap(b);
                                }
                            }
                        });
                    } else {
                        iv.setImageResource(R.drawable.anon_54);
                    }
                    if (pu.getString("nickname") != null) {
                        fname = pu.getString("nickname");
                        friendname.setText(pu.getString(("nickname")));
                    } else {
                        fname = "Invalid User";
                        friendname.setText("");
                    }
                    if (pu.getEmail() != null) {
                        email.setText(pu.getEmail());
                    } else {
                        email.setText("");
                    }
                    if (pu.getString("message") != null) {
                        msg.setText(pu.getString("message"));
                    } else {
                        msg.setText("");
                    }
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainPage.this, "User does not exist", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                }
            }
        });
    }

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
        friendname = (TextView) findViewById(R.id.friend_name);
        email = (TextView) findViewById(R.id.email_friend);
        msg = (TextView) findViewById(R.id.intro_friend);
        iv = (CircleImageView) findViewById(R.id.anon);
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.start_chat_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainPage.this, ChatRoom.class);
                i.putExtra("id", nn);
                i.putExtra("fname", fname);
                startActivity(i);
            }
        });
        loadUserProfile(nn);
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
