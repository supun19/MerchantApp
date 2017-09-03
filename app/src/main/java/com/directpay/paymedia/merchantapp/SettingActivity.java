package com.directpay.paymedia.merchantapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.directpay.paymedia.merchantapp.Component.VolleyCallback;
import com.directpay.paymedia.merchantapp.Services.Api;
import com.directpay.paymedia.merchantapp.Services.Parameter;
import com.directpay.paymedia.merchantapp.Services.SecurityHandler;
import com.directpay.paymedia.merchantapp.Services.VolleyRequestHandlerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingActivity extends AppCompatActivity {

    private RelativeLayout loadingbar;
    private CheckBox checkBox_activate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_setting);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //populateScanList();

        }

        TextView changePassword = (TextView) findViewById(R.id.text_change_username_password);
        TextView changeUsername = (TextView) findViewById(R.id.text_change_username);

        checkBox_activate = (CheckBox) findViewById(R.id.checkbox_activate_merchant);
        loadingbar = (RelativeLayout) findViewById(R.id.loadingPanel);
        loadingbar.setVisibility(View.GONE);
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
                finish();
            }
        });

        checkBox_activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingbar.setVisibility(View.VISIBLE);
                activateMerchant();

            }
        });
        checkBox_activate.setChecked(Api.isMerchantActivate(getApplicationContext()));


    }

    private void activateMerchant() {

        JSONObject params=new JSONObject();

        try {

            params.put("id",Api.getRegisterId(getApplicationContext()));
            params.put("activate",checkBox_activate.isChecked());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyRequestHandlerApi.api(new VolleyCallback(){
            @Override
            public void onSuccess(JSONObject result){
                responseProcess(result);

            }

            @Override
            public void login() {

            }

            @Override
            public void enableButton() {


                //v.setVisibility(View.VISIBLE);
            }
        }, Parameter.urlMerchantActivate,Api.getAccessToken(getApplicationContext()),params,getApplicationContext());
    }
    private void responseProcess(JSONObject result){


        if(result.has("data")) {
            JSONArray array = (JSONArray) result.opt("data");
            try {
                if (array.length() != 0) {
                    JSONObject jsonObject = array.getJSONObject(0);
                    if(jsonObject.getBoolean("success")){

                        Api.setMerchantActivate(getApplicationContext(),checkBox_activate.isChecked());
                        loadingbar.setVisibility(View.GONE);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

    private void moveDashBoard() {
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        moveDashBoard();
    }
}
