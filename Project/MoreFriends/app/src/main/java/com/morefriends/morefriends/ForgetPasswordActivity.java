package com.morefriends.morefriends;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

public class ForgetPasswordActivity extends Activity {
    private EditText email;
    private TextView forgetPass;
    private TextView cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        email = (EditText) findViewById(R.id.reset_email);
        forgetPass = (TextView) findViewById(R.id.reset_password);
        cancel = (TextView) findViewById(R.id.cancel_reset);

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = email.getText().toString();
                Toast.makeText(ForgetPasswordActivity.this, em, Toast.LENGTH_SHORT).show();
                em = em.trim();
                try {
                    ParseUser.requestPasswordReset(em);
                    Toast.makeText(ForgetPasswordActivity.this, "Instructions to reset password has been sent to provided email.", Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    if (e.getCode() == 205) {
                        Toast.makeText(ForgetPasswordActivity.this, "Email address does not exist.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "Connection failed. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                    e.printStackTrace();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
