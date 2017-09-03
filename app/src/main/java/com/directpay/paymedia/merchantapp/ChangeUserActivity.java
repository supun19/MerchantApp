package com.directpay.paymedia.merchantapp;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.directpay.paymedia.merchantapp.Component.VolleyCallback;
import com.directpay.paymedia.merchantapp.Services.Api;
import com.directpay.paymedia.merchantapp.Services.Parameter;
import com.directpay.paymedia.merchantapp.Services.SecurityHandler;
import com.directpay.paymedia.merchantapp.Services.VolleyRequestHandlerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangeUserActivity extends AppCompatActivity {


    private EditText password;
    private EditText usename;
    private Button btn_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_change_user);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //populateScanList();

        }


        password = (EditText) findViewById(R.id.password);
        usename = (EditText) findViewById(R.id.username);
        btn_change = (Button) findViewById(R.id.btn_change);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                changePassword();


            }
        });


    }
    private void changePassword() {

        JSONObject params=new JSONObject();
        try {
            params.put("id",""+ Api.getRegisterId(getApplicationContext()));
            params.put("username",""+ Api.getNic(getApplicationContext()));
            params.put("password",password.getText().toString());
            params.put("new_username",usename.getText().toString());

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

                btn_change.setEnabled(true);
                //v.setVisibility(View.VISIBLE);
            }
        }, Parameter.urlMerchantChangeUsername,Api.getAccessToken(getApplicationContext()),params,getApplicationContext());

    }
    private void responseProcess(JSONObject result) {
        if(result.has("data")) {
            JSONArray array = (JSONArray) result.opt("data");
            try {
                if (array.length() != 0) {
                    JSONObject jsonObject = array.getJSONObject(0);
                    Log.d("Changeusername:",""+jsonObject.toString());
                    if (jsonObject.getBoolean("success")){

                        Log.d("username",jsonObject.getString("username"));
                        Api.setNic(getApplicationContext(),jsonObject.getString("username"));
                        Intent intent = new Intent(this,LoginActivity.class);
                        startActivity(intent);
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
                moveSetting();
                break;
        }
        return true;
    }

    private void moveSetting() {
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        moveSetting();
    }
}
