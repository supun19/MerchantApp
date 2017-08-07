package com.directpay.paymedia.merchantapp;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.directpay.paymedia.merchantapp.Component.VolleyCallback;
import com.directpay.paymedia.merchantapp.Services.Api;
import com.directpay.paymedia.merchantapp.Services.Parameter;
import com.directpay.paymedia.merchantapp.Services.SecurityHandler;
import com.directpay.paymedia.merchantapp.Services.VolleyRequestHandlerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {


    private TextInputLayout passLayout;
    private TextInputLayout nicLayout;
    private EditText passField;
    private EditText nicField;
    private Button btn_login;

    private String password;
    private String nic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_login);

        btn_login = (Button) findViewById(R.id.log_button);
        nicField = (EditText) findViewById(R.id.login_nic);
        passField = (EditText) findViewById(R.id.login_pin);

        passLayout = (TextInputLayout) findViewById(R.id.pinErr);
        nicLayout = (TextInputLayout) findViewById(R.id.nic_error);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();

            }
        });
        ///nicValidation(nicField);
        if(!Api.isFirstTimeLogin(getApplicationContext())){
            nicField.setVisibility(View.GONE);
            nicLayout.setVisibility(View.GONE);
        }

    }

    private void login(){
        //Log.d("login data",""+ Api.getNic(getApplicationContext()));
        if(isEnterdValideLoginData()){
            Log.d("...login data validate.","");
            JSONObject params = getLoginCredential();

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

                    btn_login.setEnabled(true);
                    //v.setVisibility(View.VISIBLE);
                }
            }, Parameter.loginMerchantUrl,"",params,getApplicationContext());

           /*JSONObject detailLogin = getLoginDetail();

           VolleyRequestHandlerApi.api(new VolleyCallback() {
               @Override
               public void onSuccess(JSONObject result) {
                   responseProcess(result);
               }

               @Override
               public void login() {

               }

               @Override
               public void enableButton() {
                   btn.setEnabled(true);
               }
           },Parameter.loginUrl,"",detailLogin,getApplicationContext());*/
        }

        else{
            btn_login.setEnabled(true);
        }



    }
    private boolean isEnterdValideLoginData(){
        //username = accountField.getText().toString();
        password = passField.getText().toString();
        nic = nicField.getText().toString();
        if(password.matches("")){
            passLayout.setError("enter password");
            return false;
        }
        else if(Api.isFirstTimeLogin(getApplicationContext()) && nic.matches("")) {
            nicLayout.setError("enter nic");
            return false;

        }
        return true;
    }
    private JSONObject getLoginCredential(){
        JSONObject params=new JSONObject();

        try {
            params.put("grant_type","password");
            if(Api.isFirstTimeLogin(getApplicationContext())){
                params.put("username",""+ nicField.getText().toString());
                Log.d("username",""+nicField.getText().toString());
            }
            else {
                params.put("username",""+ Api.getNic(getApplicationContext()));
                Log.d("nic", "");
            }

            params.put("password",""+password);
            params.put("scope","openid");
            params.put("firstLogin",Api.isFirstTimeLogin(getApplicationContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }
    private void responseProcess(JSONObject result){


        if(result.has("data")) {
            JSONArray array = (JSONArray) result.opt("data");
            try {
                if (array.length() != 0) {
                    JSONObject jsonObject = array.getJSONObject(0);
                    Api.setAccessToken(getApplicationContext(), jsonObject.getString("accessToken").toString());

                    btn_login.setEnabled(true);
                    passField.setText(null);

                    //TODO success login


                    if (Api.isFirstTimeLogin(getApplicationContext())) {
                        Api.setFirstTimeLogin(getApplicationContext(), nicField.getText().toString());
                        Api.setRegisterId(getApplicationContext(),jsonObject.getString("id").toString());
                    }
                    moveDashboard();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//        if(result.has("access_token")){
//            Api.setAccessToken(getApplicationContext(),result.opt("access_token").toString());
//            Log.d("accesstoken",Api.getAccessToken(getApplicationContext()));
//            //login successs go totransaction
//            Log.d("loginActivity",""+Api.isMerchant(getApplicationContext()));
//            Toast.makeText(getApplicationContext(),"logged in",Toast.LENGTH_LONG).show();
//            btn_login.setEnabled(true);
//            passField.setText(null);
//
//            //TODO success login
//                moveDashboard();
//
//            if(Api.isFirstTimeLogin(getApplicationContext())){
//                Api.setFirstTimeLogin(getApplicationContext(),nicField.getText().toString());
//
//            }
//        }
//        else {
//            Toast.makeText(getApplicationContext(),"No Access Token in Response",Toast.LENGTH_LONG).show();
//        }
        }
    }

    private void moveDashboard() {

        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }

    private void showError(TextInputLayout passField) {
        // Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        //passField.startAnimation(shake);
        passField.setError("empty password");
    }
    private void nicValidation(final EditText nic){

        nic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String regex;
                Pattern p;
                Matcher m;
                nic.removeTextChangedListener(this);
                if (s.length()<=9){
                    Log.d("change","9");
                    regex = "[^\\d]";
                    p = Pattern.compile(regex);
                    m = p.matcher(s.toString());
                    nicField.setInputType(InputType.TYPE_CLASS_NUMBER);
                    if(m.matches()){
                        String cleanString = s.toString().replaceAll(regex, "");
                        nic.setText(cleanString);

                        nic.setSelection(cleanString.length());
                    }
                    if(s.length()==9){
                        nicField.setInputType(InputType.TYPE_CLASS_TEXT);
                    }


                }
                else if(s.length()==10){
                    if(s.charAt(9)=='v' || s.charAt(9)=='V' || s.charAt(9)=='x' || s.charAt(9)=='X' || s.charAt(9)=='B' || s.charAt(9)=='b'){

                    }
                    else {
                        String cleanString = s.toString().replaceAll("[^\\d]", "");
                        nic.setText(cleanString);
                        nic.setSelection(9);
                    }
                }
                nic.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}
