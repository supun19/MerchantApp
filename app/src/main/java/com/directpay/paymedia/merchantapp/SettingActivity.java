package com.directpay.paymedia.merchantapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        TextView changePassword = (TextView) findViewById(R.id.text_change_username_password);
        TextView changeUsername = (TextView) findViewById(R.id.text_change_username);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,ChangePasswordActivity.class);
                startActivity(intent);

            }
        });
        changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,ChangeUserActivity.class);
                startActivity(intent);
            }
        });
    }
}
