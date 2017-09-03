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
import android.widget.TextView;

import com.directpay.paymedia.merchantapp.Component.VolleyCallback;
import com.directpay.paymedia.merchantapp.Services.Api;
import com.directpay.paymedia.merchantapp.Services.Parameter;
import com.directpay.paymedia.merchantapp.Services.SecurityHandler;
import com.directpay.paymedia.merchantapp.Services.VolleyRequestHandlerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText current_password;
    private EditText password;
    private EditText re_password;
    private Button btn_change;

    private TextInputLayout current_password_error;
    private TextInputLayout password_error;
    private TextInputLayout re_password_error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_change_password);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //populateScanList();

        }

        current_password = (EditText) findViewById(R.id.current_password);
        password = (EditText) findViewById(R.id.password);
        re_password = (EditText) findViewById(R.id.repassword);
        btn_change = (Button) findViewById(R.id.btn_change);

        current_password_error = (TextInputLayout) findViewById(R.id.currentErr);
        password_error = (TextInputLayout) findViewById(R.id.passwordErr);
        re_password_error = (TextInputLayout) findViewById(R.id.repasswordErr);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkError()){
                    changePassword();
                }

            }
        });

    }

    private void changePassword() {

        JSONObject params=new JSONObject();
        try {
            params.put("id",""+ Api.getRegisterId(getApplicationContext()));
            params.put("username",""+ Api.getNic(getApplicationContext()));
            params.put("password",current_password.getText().toString());
            params.put("new_password",password.getText().toString());

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
        }, Parameter.urlMerchantChangePassword,Api.getAccessToken(getApplicationContext()),params,getApplicationContext());

    }

    private void responseProcess(JSONObject result) {
        if(result.has("data")) {
            JSONArray array = (JSONArray) result.opt("data");
            try {
                if (array.length() != 0) {
                    JSONObject jsonObject = array.getJSONObject(0);
                    Log.d("ChangePassword:",""+jsonObject.toString());
                    if (jsonObject.getBoolean("success")){
                        Intent intent = new Intent(this,LoginActivity.class);
                        startActivity(intent);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private boolean checkError(){
        Log.d("current",current_password.getText().toString());
        Log.d("new pasword",password.getText().toString());
        Log.d("re enter password",re_password.getText().toString());
        if(current_password.getText().toString().equals("")){
            current_password_error.setError("empty password");

            return false;
        }
        else if(password.getText().toString().equals("")){
            password_error.setError("empty password");
            return false;
        }
        else if( re_password.getText().toString().equals("")){
            re_password_error.setError("empty password");
            return false;
        }
        else if(!password.getText().toString().equals(re_password.getText().toString())){
            re_password_error.setError("password not match");
            return false;
        }

        return true;
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
