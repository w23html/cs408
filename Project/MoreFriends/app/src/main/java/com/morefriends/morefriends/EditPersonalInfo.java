package com.morefriends.morefriends;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditPersonalInfo extends ActionBarActivity {

    private final static int REQUEST_CAMERA = 3333;
    private final static int SELECT_FILE = 4444;
    private final static int CROP_IMAGE = 5555;
    private final static int REMOVE_FILE = 6666;
    private TextView email;
    private TextView user;
    private TextView birthday;
    private TextView intro;
    private TextView age;
    private TextView gender;
    private TextView country;
    private android.support.design.widget.FloatingActionButton fab;
    private Date bdate = null;
    private String init_email = null;
    private String init_intro = null;
    private String init_name = null;
    private String init_gender = null;
    private String init_country = null;
    private File file;
    private String filePath;
    private Uri uri;
    private byte[] fullScreenImage;
    private ImageView image;
    private byte[] bm50;
    private int changed = 0;
    private int from = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
        File root = new File(appFolderCheckandCreate());
        File[] files = root.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
        email = (TextView) findViewById(R.id.email_pi);
        user = (TextView) findViewById(R.id.name_pi);
        birthday = (TextView) findViewById(R.id.bday_pi);
        intro = (TextView) findViewById(R.id.intro_pi);
        age = (TextView) findViewById(R.id.age_pi);
        gender = (TextView) findViewById(R.id.gender_pi);
        country = (TextView) findViewById(R.id.country_pi);
        image = (ImageView) findViewById(R.id.image_pi);
        fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);
        ParseUser u = ParseUser.getCurrentUser();
        //if (u.getEmail() == null) {
        if (u.getString("email") != null) {
            email.setText(u.getString("email"));
        } else {
            email.setText(getString(R.string.unspecified));
        }
        //} else {
        //   email.setText(u.getEmail());
        // }
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
            } else if (g.compareTo("Female") == 0) {
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
            bdate = d;
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
        if (u.getBytes("image") != null) {
            Bitmap b = BitmapFactory.decodeByteArray(u.getBytes("image"), 0, u.getBytes("image").length);
            Bitmap bm = Bitmap.createScaledBitmap(b, (int)Util.convertDpToPixel(54, this), (int)Util.convertDpToPixel(54, this), true);
            image.setImageBitmap(bm);
            if (u.getParseFile("full_image") != null) {
                u.getParseFile("full_image").getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        if (e == null) {
                            fullScreenImage = bytes;
                            image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (fullScreenImage != null) {
                                        Intent i = new Intent(EditPersonalInfo.this, FullScreenImage.class);
                                        i.putExtra("picture", fullScreenImage);
                                        startActivityForResult(i, REMOVE_FILE);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    public void backButton(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.left_to_right);
    }

    public void upload(View v) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.saving));
        dialog.setCancelable(false);
        dialog.show();
        final ParseUser user = ParseUser.getCurrentUser();
        if (changed == 1) {
            if (bm50 == null || fullScreenImage == null) {
                if (bdate != null) {
                    user.put("bday", bdate);
                }
                if (init_name != null) {
                    user.put("name", init_name);
                }
                if (init_country != null) {
                    user.put("country", init_country);
                }
                if (init_intro != null) {
                    user.put("message", init_intro);
                }
                if (init_gender != null) {
                    user.put("gender", init_gender);
                }
                if (init_email != null) {
                    user.put("email_str_p", init_email);
                }
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            setResult(RESULT_OK);
                            dialog.dismiss();
                            finish();
                            overridePendingTransition(0, R.anim.left_to_right);
                        } else {
                            Toast.makeText(EditPersonalInfo.this, getString(R.string.connection_err), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
            } else {
                final ParseFile f = new ParseFile("img.jpg", fullScreenImage);
                f.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            user.put("image", bm50);
                            if (bdate != null) {
                                user.put("bday", bdate);
                            }
                            if (init_name != null) {
                                user.put("name", init_name);
                            }
                            if (init_country != null) {
                                user.put("country", init_country);
                            }
                            if (init_intro != null) {
                                user.put("message", init_intro);
                            }
                            if (init_gender != null) {
                                user.put("gender", init_gender);
                            }
                            if (init_email != null) {
                                user.put("email_str_p", init_email);
                            }
                            user.put("full_image", f);
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        setResult(RESULT_OK);
                                        dialog.dismiss();
                                        finish();
                                        overridePendingTransition(0, R.anim.left_to_right);
                                    } else {
                                        Toast.makeText(EditPersonalInfo.this, getString(R.string.connection_err), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(EditPersonalInfo.this, getString(R.string.connection_err), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        } else {
            if (bdate != null) {
                user.put("bday", bdate);
            }
            if (init_name != null) {
                user.put("name", init_name);
            }
            if (init_country != null) {
                user.put("country", init_country);
            }
            if (init_intro != null) {
                user.put("message", init_intro);
            }
            if (init_gender != null) {
                user.put("gender", init_gender);
            }
            if (init_email != null) {
                user.put("email_str_p", init_email);
            }
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        dialog.dismiss();
                        setResult(RESULT_OK);
                        finish();
                        overridePendingTransition(0, R.anim.left_to_right);
                    } else {
                        Toast.makeText(EditPersonalInfo.this, getString(R.string.connection_err), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
        }

    }

    public void edit_name(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.input_name));
        final EditText et = (EditText) View.inflate(this, R.layout.edittext_material2, null);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setText(user.getText().toString());
        et.setSelection(user.getText().toString().length());
        int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15,
                getResources().getDisplayMetrics());
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = et.getText().toString().trim();
                if (text.length() < 2) {
                    Toast.makeText(EditPersonalInfo.this, getString(R.string.fl_name), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    init_name = text;
                    user.setText(text);
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        ad.setView(et, pad, pad, pad, pad);
        ad.show();
    }

    public void edit_bday(View v) {
        final Calendar c = Calendar.getInstance();
        final int c_year = c.get(Calendar.YEAR);
        int year = 0;
        int month = 0;
        int day = 0;
        if (bdate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(bdate);
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            year = c_year;
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year2, int monthOfYear, int dayOfMonth) {
                if (c_year < year2) {
                    Toast.makeText(EditPersonalInfo.this, getString(R.string.invalid_date), Toast.LENGTH_SHORT).show();
                } else {
                    int diff = c_year - year2;
                    age.setText(Integer.toString(diff));
                    DateFormat df = new SimpleDateFormat("M-dd-yyyy");
                    Calendar c = Calendar.getInstance();
                    c.set(year2, monthOfYear, dayOfMonth);
                    bdate = c.getTime();
                    birthday.setText(df.format(c.getTime()));
                }
            }
        }, year, month, day);
        dpd.show();
    }

    public void edit_intro(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.edit_intro));
        final EditText et = (EditText) View.inflate(this, R.layout.edittext_material2, null);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        if (ParseUser.getCurrentUser().getString("message") != null) {
            et.setText(intro.getText().toString());
            et.setSelection(intro.getText().toString().length());
        }
        int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15,
                getResources().getDisplayMetrics());
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = et.getText().toString().trim();
                intro.setText(text);
                init_intro = text;
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        ad.setView(et, pad, pad, pad, pad);
        ad.show();
    }

    public void edit_gender(View v) {
        AlertDialog dialog;
        final CharSequence[] items = {getString(R.string.male), getString(R.string.female), getString(R.string.other)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int choice = -1;
        for (int i = 0; i < items.length; i++) {
            if (gender.getText().toString().trim().compareTo(items[i].toString()) == 0) {
                choice = i;
                break;
            }
        }
        builder.setSingleChoiceItems(items, choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        gender.setText(items[0]);
                        break;
                    case 1:
                        gender.setText(items[1]);
                        break;
                    case 2:
                        gender.setText(items[2]);
                        break;
                }
                init_gender = items[which].toString();
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public void edit_country(View v) {
        AlertDialog dialog;
        final CharSequence[] items = {"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",

                "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria",

                "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",

                "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana",

                "Brazil", "British Indian Ocean Territory", "British Virgin Islands", "Brunei", "Bulgaria",

                "Burkina Faso", "Burma (Myanmar)", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde",

                "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island",

                "Cocos (Keeling) Islands", "Colombia", "Comoros", "Cook Islands", "Costa Rica",

                "Croatia", "Cuba", "Cyprus", "Czech Republic", "Democratic Republic of the Congo",

                "Denmark", "Djibouti", "Dominica", "Dominican Republic",

                "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",

                "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Polynesia",

                "Gabon", "Gambia", "Gaza Strip", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece",

                "Greenland", "Grenada", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana",

                "Haiti", "Holy See (Vatican City)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India",

                "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man", "Israel", "Italy", "Ivory Coast", "Jamaica",

                "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kosovo", "Kuwait",

                "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein",

                "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia",

                "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mayotte", "Mexico",

                "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco",

                "Mozambique", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia",

                "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "North Korea",

                "Northern Mariana Islands", "Norway", "Oman", "Other", "Pakistan", "Palau", "Panama",

                "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn Islands", "Poland",

                "Portugal", "Puerto Rico", "Qatar", "Republic of the Congo", "Romania", "Russia", "Rwanda",

                "Saint Barthelemy", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia", "Saint Martin",

                "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines", "Samoa", "San Marino",

                "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone",

                "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Korea",

                "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland",

                "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tokelau",

                "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands",

                "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "US Virgin Islands", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam",

                "Wallis and Futuna", "West Bank", "Yemen", "Zambia", "Zimbabwe"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int choice = -1;
        for (int i = 0; i < items.length; i++) {
            if (country.getText().toString().trim().compareTo(items[i].toString()) == 0) {
                choice = i;
                break;
            }
        }
        builder.setSingleChoiceItems(items, choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                country.setText(items[which]);
                init_country = items[which].toString();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public void editEmail(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.set_email));
        final EditText et = (EditText) View.inflate(this, R.layout.edittext_material2, null);
        et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        if (ParseUser.getCurrentUser().getString("email_str_p") != null) {
            et.setText(email.getText().toString());
            et.setSelection(email.getText().toString().length());
        }
        int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15,
                getResources().getDisplayMetrics());
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = et.getText().toString().trim();
                if (!Util.isEmailValid(text)) {
                    Toast.makeText(EditPersonalInfo.this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    email.setText(text);
                    init_email = text;
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        ad.setView(et, pad, pad, pad, pad);
        ad.show();
    }


    public void takeImage(View v) {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.gallery), getString(R.string.cancel)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_image));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    file = new File(appFolderCheckandCreate(), "." + System.currentTimeMillis() + ".jpg");
                    filePath = file.getAbsolutePath();
                    uri = Uri.fromFile(file);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (which == 1) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private String appFolderCheckandCreate(){

        String appFolderPath="";
        File externalStorage = Environment.getExternalStorageDirectory();

        if (externalStorage.canWrite())
        {
            appFolderPath = externalStorage.getAbsolutePath() + "/MoreFriends/.imgfldr";
            File dir = new File(appFolderPath);

            if (!dir.exists())
            {
                dir.mkdirs();
            }

        }

        return appFolderPath;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                if (!file.exists()) {
                    Toast.makeText(this, getString(R.string.image_load_err), Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri uri = this.uri;
                Intent i = new Intent(this, CropImageActivity.class);
                i.putExtra("uri", uri);
                i.putExtra("filepath", file.getAbsolutePath());
                i.putExtra("edit_profile", 1);
                from = 1;
                startActivityForResult(i, CROP_IMAGE);
                overridePendingTransition(0, 0);
            } else if (requestCode == SELECT_FILE) {
                Intent i = new Intent(this, CropImageActivity.class);
                uri = data.getData();
                i.putExtra("uri", data.getData());
                i.putExtra("edit_profile", 1);
                from = 2;
                startActivityForResult(i, CROP_IMAGE);
            } else if (requestCode == CROP_IMAGE) {
                bm50 = (byte[]) data.getExtras().getByteArray("data_50");
                Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(bm50, 0, bm50.length), (int)Util.convertDpToPixel(54, this), (int)Util.convertDpToPixel(54, this), true);
                image.setImageBitmap(bm);
                Bitmap bm_full;
                if (from == 1) {
                    bm_full = Util.decodeFile(file.getAbsolutePath());
                } else if (from == 2) {
                    String[] projection = {MediaStore.MediaColumns.DATA};
                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    int col = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    cursor.moveToFirst();
                    bm_full = Util.decodeFile(cursor.getString(col));
                } else {
                    bm_full = null;
                }
                if (bm_full != null) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm_full.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    fullScreenImage = bytes.toByteArray();
                    changed = 1;
                }
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fullScreenImage != null) {
                            Intent i = new Intent(EditPersonalInfo.this, FullScreenImage.class);
                            i.putExtra("picture", fullScreenImage);
                            startActivityForResult(i, REMOVE_FILE);
                        }
                    }
                });
                Toast.makeText(this, getString(R.string.click_button_view_image), Toast.LENGTH_SHORT).show();
            } else if (requestCode == REMOVE_FILE) {
                changed = 1;
                image.setImageResource(R.drawable.anon_54);
                ParseUser.getCurrentUser().remove("image");
                fullScreenImage = null;
                bm50 = null;
            }
        }
    }


}
