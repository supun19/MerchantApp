package com.directpay.paymedia.merchantapp;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.directpay.paymedia.merchantapp.Services.SecurityHandler;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_dashboard);

        Button button_my_qr = (Button) findViewById(R.id.btn_my_qr);
        Button button_transaction_report = (Button) findViewById(R.id.btn_transaction_report);
        Button button_setting = (Button) findViewById(R.id.btn_setting);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width_px = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height_px = Resources.getSystem().getDisplayMetrics().heightPixels;

        int pixeldpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        float pixeldp = Resources.getSystem().getDisplayMetrics().density;

        int width_dp = (width_px/pixeldpi)*160;
        int height_dp = (height_px/pixeldpi)*160;

        int button_width = (width_dp-48)/2;

        int btn_size = (int) (button_width*pixeldp);
        button_my_qr.setLayoutParams (new LinearLayout.LayoutParams(btn_size, btn_size));
        button_transaction_report.setLayoutParams (new LinearLayout.LayoutParams(btn_size, btn_size));
        button_setting.setLayoutParams (new LinearLayout.LayoutParams(btn_size, btn_size));
        button_transaction_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DashboardActivity.this,ReportActivity.class);
                startActivity(intent);
                finish();
            }
        });
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });


        button_my_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,MyQrActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
