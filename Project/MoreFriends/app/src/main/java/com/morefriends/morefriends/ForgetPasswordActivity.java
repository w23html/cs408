package com.morefriends.morefriends;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
                // TODO
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
