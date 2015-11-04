package com.morefriends.morefriends;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PersonalInfo extends ActionBarActivity {

    private TextView name;
    private TextView email;
    private TextView user;
    private TextView birthday;
    private TextView intro;
    private TextView age;
    private TextView gender;
    private TextView country;
    private ImageView iv;
    private byte[] fullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        name =(TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email_pi);
        user = (TextView) findViewById(R.id.name_pi);
        birthday = (TextView) findViewById(R.id.bday_pi);
        intro = (TextView) findViewById(R.id.intro_pi);
        age = (TextView) findViewById(R.id.age_pi);
        gender = (TextView) findViewById(R.id.gender_pi);
        country = (TextView) findViewById(R.id.country_pi);
        iv = (ImageView) findViewById(R.id.image_pi);
        ParseUser u = ParseUser.getCurrentUser();
        //if (u.getEmail() == null) {
        if (u.getString("email") != null) {
            email.setText(u.getString("email"));
        } else {
            email.setText(getString(R.string.unspecified));
        }
        //} else {
        //   email.setText(u.getEmail());
        //}
        user.setText(u.getString("nickname"));
        if (u.getString("message") != null) {
            intro.setText(u.getString("message"));
        } else {
            intro.setText(getString(R.string.unspecified));
        }
        if (u.getString("gender") != null) {
            String g = u.getString("gender");
            if (g.compareTo("Male") == 0) {
                gender.setText(getString(R.string.male));
            } else if ( g.compareTo("Female") == 0) {
                gender.setText(getString(R.string.female));
            } else if (g.compareTo("Other") == 0) {
                gender.setText(getString(R.string.other));
            } else {
                gender.setText(g);
            }
        } else {
            gender.setText(getString(R.string.unspecified));
        }
        if (u.getString("country") != null) {
            country.setText(u.getString("country"));
        } else {
            country.setText(getString(R.string.unspecified));
        }
        if (u.getDate("bday") != null) {
            Date d = u.getDate("bday");
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int age_val = Calendar.getInstance().get(Calendar.YEAR) - c.get(Calendar.YEAR);
            if (age_val < 1) age_val = 0;
            age.setText(Integer.toString(age_val));
            DateFormat df = new SimpleDateFormat("M-dd-yyyy");
            birthday.setText(df.format(d));
        } else {
            birthday.setText(getString(R.string.unspecified));
            age.setText(getString(R.string.unspecified));
        }
        byte[] image = u.getBytes("image");
        if (image != null) {
            Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(image, 0, image.length), (int) Util.convertDpToPixel(54, this),
                    (int) Util.convertDpToPixel(54, this), true);
            iv.setImageBitmap(bm);
            u.getParseFile("full_image").getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null) {
                        fullImage = bytes;
                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (fullImage != null) {
                                    Intent i = new Intent(PersonalInfo.this, FullScreenImageActivity.class);
                                    i.putExtra("image", fullImage);
                                    startActivity(i);
                                    //this.overridePendingTransition(R.anim.fade_in, 0);
                                }
                            }
                        });
                    }
                }
            });
        } else {
            iv.setOnClickListener(null);
            iv.setImageResource(R.drawable.anon_54);
        }

        findViewById(R.id.back_button_profile).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call editProfile
                //findViewById(R.id.edit_profile).setClickable(true);
                //findViewById(R.id.edit_profile).setEnabled(false);
                Intent i = new Intent(PersonalInfo.this, EditPersonalInfo.class);
                startActivityForResult(i, 2222);
                overridePendingTransition(R.anim.right_to_left, 0);
            }
        });

    }
    public void onResume() {
        super.onResume();
        Toast.makeText(PersonalInfo.this, "onResume", Toast.LENGTH_SHORT).show();
        findViewById(R.id.back_button_profile).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonalInfo.this, "Resume", Toast.LENGTH_SHORT).show();
                //call editProfile
                //findViewById(R.id.edit_profile).setClickable(false);
                //findViewById(R.id.edit_profile).setEnabled(false);
                Intent i = new Intent(PersonalInfo.this, EditPersonalInfo.class);
                startActivityForResult(i, 2222);
                overridePendingTransition(R.anim.right_to_left, 0);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 2222) {
                name =(TextView) findViewById(R.id.name);
                email = (TextView) findViewById(R.id.email_pi);
                user = (TextView) findViewById(R.id.name_pi);
                birthday = (TextView) findViewById(R.id.bday_pi);
                intro = (TextView) findViewById(R.id.intro_pi);
                age = (TextView) findViewById(R.id.age_pi);
                gender = (TextView) findViewById(R.id.gender_pi);
                country = (TextView) findViewById(R.id.country_pi);
                iv = (ImageView) findViewById(R.id.image_pi);
                ParseUser u = ParseUser.getCurrentUser();
                //if (u.getEmail() == null) {
                if (u.getString("email") != null) {
                    email.setText(u.getString("email"));
                } else {
                    email.setText(getString(R.string.unspecified));
                }
                //} else {
                //   email.setText(u.getEmail());
                //}
                user.setText(u.getString("nickname"));
                if (u.getString("message") != null) {
                    intro.setText(u.getString("message"));
                } else {
                    intro.setText(getString(R.string.unspecified));
                }
                if (u.getString("gender") != null) {
                    String g = u.getString("gender");
                    if (g.compareTo("Male") == 0) {
                        gender.setText(getString(R.string.male));
                    } else if ( g.compareTo("Female") == 0) {
                        gender.setText(getString(R.string.female));
                    } else if (g.compareTo("Other") == 0) {
                        gender.setText(getString(R.string.other));
                    } else {
                        gender.setText(g);
                    }
                } else {
                    gender.setText(getString(R.string.unspecified));
                }
                if (u.getString("country") != null) {
                    country.setText(u.getString("country"));
                } else {
                    country.setText(getString(R.string.unspecified));
                }
                if (u.getDate("bday") != null) {
                    Date d = u.getDate("bday");
                    Calendar c = Calendar.getInstance();
                    c.setTime(d);
                    int age_val = Calendar.getInstance().get(Calendar.YEAR) - c.get(Calendar.YEAR);
                    if (age_val < 1) age_val = 0;
                    age.setText(Integer.toString(age_val));
                    DateFormat df = new SimpleDateFormat("M-dd-yyyy");
                    birthday.setText(df.format(d));
                } else {
                    birthday.setText(getString(R.string.unspecified));
                    age.setText(getString(R.string.unspecified));
                }
                byte[] image = u.getBytes("image");
                if (image != null) {
                    Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(image, 0, image.length), (int) Util.convertDpToPixel(54, this),
                            (int) Util.convertDpToPixel(54, this), true);
                    iv.setImageBitmap(bm);
                    u.getParseFile("full_image").getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            if (e == null) {
                                fullImage = bytes;
                                iv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (fullImage != null) {
                                            Intent i = new Intent(PersonalInfo.this, FullScreenImageActivity.class);
                                            i.putExtra("image", fullImage);
                                            startActivity(i);
                                            //this.overridePendingTransition(R.anim.fade_in, 0);
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    iv.setOnClickListener(null);
                    iv.setImageResource(R.drawable.anon_54);
                }
            }
        }
    }


}


