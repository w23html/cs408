package com.morefriends.morefriends;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class SignUpActivity extends ActionBarActivity {

    private EditText nickname;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        nickname = (EditText) findViewById(R.id.nick_name);
        email = (EditText) findViewById(R.id.reset_email);
        password = (EditText) findViewById(R.id.new_password);
    }

}
