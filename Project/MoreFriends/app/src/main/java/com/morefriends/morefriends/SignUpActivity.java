package com.morefriends.morefriends;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpActivity extends ActionBarActivity {

    private EditText nickname;
    private EditText email;
    private EditText password;
    private Button createacc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        nickname = (EditText) findViewById(R.id.nick_name);
        email = (EditText) findViewById(R.id.reset_email);
        password = (EditText) findViewById(R.id.new_password);
        createacc = (Button) findViewById(R.id.create_account);
        createacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nn = nickname.getText().toString();
                String em = email.getText().toString();
                String pw = password.getText().toString();
                if (nn.trim().length() < 3) {
                    Toast.makeText(SignUpActivity.this, "Nickname should be at least 3 characters long.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (em.trim().length() < 3 || !isEmailValid(em)) {
                    Toast.makeText(SignUpActivity.this, "Invalid email address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pw.trim().length() < pw.length()) {
                    Toast.makeText(SignUpActivity.this, "Password should not contain spaces.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pw.trim().length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Password should be at least 6 characters long.", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser user = new ParseUser();
                user.setUsername(nn);
                user.setEmail(em);
                user.setPassword(pw);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "Sign Up Successfully, Please log in", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: Please Resign Up", Toast.LENGTH_LONG).show();
                        }
                    }

                });

                setResult(RESULT_OK);
                finish();
            }
        });
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}
