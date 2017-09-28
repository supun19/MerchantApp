package com.directpay.paymedia.merchantapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.directpay.paymedia.merchantapp.Component.VolleyCallback;
import com.directpay.paymedia.merchantapp.Services.Api;
import com.directpay.paymedia.merchantapp.Services.Parameter;
import com.directpay.paymedia.merchantapp.Services.VolleyRequestHandlerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class forgotPasswordActivity extends AppCompatActivity {

    private EditText passField;
    private EditText rePassField;
    private TextInputLayout rePassLayout;
    private Button passwordChange;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        passField = (EditText) findViewById(R.id.password);
        final TextInputLayout passLayout = (TextInputLayout) findViewById(R.id.password_error);
        rePassField = (EditText) findViewById(R.id.re_enter_password);
        rePassLayout = (TextInputLayout) findViewById(R.id.re_enter_password_error);
        mProgressView = findViewById(R.id.forget_progress);;

        passwordChange = (Button) findViewById(R.id.button_change_password);

        passField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                passLayout.setError(null);
                return false;
            }
        });

        rePassField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rePassLayout.setError(null);
                return false;
            }
        });

        passwordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                passwordChange.setEnabled(false);
                changePassword();
            }
        });

    }

    private void changePassword() {

        String password = passField.getText().toString();
        String rePassword = rePassField.getText().toString();

        if (password.equals(rePassword)){

            JSONObject payload = new JSONObject();
            try {
                payload.put("username",Api.getNic(getApplicationContext()));
//                Log.d("username",Api.getNic(getApplicationContext()));
                payload.put("password",password);
                payload.put("id", Api.getId(getApplicationContext()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            VolleyRequestHandlerApi.api(new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {

                }

                @Override
                public void login() {

                }

                @Override
                public void enableButton() {

                }
            }, Parameter.urlMerchantForgotPassword,"",payload,getApplicationContext());
        }
        else {
            showProgress(false);
            passwordChange.setEnabled(true);
            rePassLayout.setError("password not match");
        }
    }
    private void responseProcess(JSONObject result){

        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();

                // Api.reSetPin(getApplicationContext());
                //Api.sendSms(Api.getPhoneNumber(getApplication()),jsonObject.opt("verificationCode").toString(),getApplicationContext());
                moveToLogin();
                //showmessgebox();


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else if(result.has("errors")){
            JSONArray array= (JSONArray) result.opt("errors");
            try {
                JSONObject jsonObject = array.getJSONObject(0);

                if(jsonObject.opt("status").toString().equals("409")){
                    Toast.makeText(getApplicationContext(),"User Already Exist",Toast.LENGTH_LONG).show();

                    passwordChange.setEnabled(true);
                }
                else if(jsonObject.opt("status").toString().equals("422")){
                    Toast.makeText(getApplicationContext(),"Account Does Not Exist",Toast.LENGTH_LONG).show();
                    passwordChange.setEnabled(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        showProgress(false);
        passwordChange.setEnabled(false);
    }

    private void moveToLogin(){

        Intent intent  = new Intent(this,LoginActivity.class);
        startActivity(intent);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }

    }

}
