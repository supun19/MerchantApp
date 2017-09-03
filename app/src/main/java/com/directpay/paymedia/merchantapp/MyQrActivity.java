package com.directpay.paymedia.merchantapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.ImageView;

import com.directpay.paymedia.merchantapp.Services.Api;
import com.directpay.paymedia.merchantapp.Services.SecurityHandler;

import net.glxn.qrgen.android.QRCode;

public class MyQrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_my_qr);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            String code = ""+ Api.getRegisterId(getApplicationContext())+" $ user";

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width_px = Resources.getSystem().getDisplayMetrics().widthPixels;
            int height_px = Resources.getSystem().getDisplayMetrics().heightPixels;

            int pixeldpi = Resources.getSystem().getDisplayMetrics().densityDpi;
            float pixeldp = Resources.getSystem().getDisplayMetrics().density;

            int width_dp = (width_px/pixeldpi)*160;
            int height_dp = (height_px/pixeldpi)*160;

            Bitmap myBitmap = QRCode.from(""+code).withSize(width_px,width_px).bitmap();
            ImageView myImage = (ImageView) findViewById(R.id.imageView2);
            myImage.setImageBitmap(myBitmap);
        }

    }
    private void moveDashBoard() {
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        moveDashBoard();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                moveDashBoard();
                break;
        }
        return true;
    }
}
