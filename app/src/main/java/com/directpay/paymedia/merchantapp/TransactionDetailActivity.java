package com.directpay.paymedia.merchantapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.directpay.paymedia.merchantapp.Component.VolleyCallback;
import com.directpay.paymedia.merchantapp.Model.UserTransactionModel;
import com.directpay.paymedia.merchantapp.Services.Api;
import com.directpay.paymedia.merchantapp.Services.Parameter;
import com.directpay.paymedia.merchantapp.Services.SecurityHandler;
import com.directpay.paymedia.merchantapp.Services.VolleyRequestHandlerApi;

import org.json.JSONException;
import org.json.JSONObject;


public class TransactionDetailActivity extends AppCompatActivity {

    private UserTransactionModel model;
    private String recieptNumber;
    private String name;
    private String amount;
    private String recievedAmount;
    private String commission;
    private String date;
    private String type;
    private String status;
    private String merchantId;


    private View mProgressView;

    Button button_void;

    int width_dp;
    int height_dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_transaction_detail);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //populateScanList();

        }

        final Intent intent = getIntent();

        mProgressView = findViewById(R.id.login_progress);;
        recieptNumber = intent.getStringExtra("receipt");
        name = intent.getStringExtra("name");
        amount = intent.getStringExtra("amount");
        date = intent.getStringExtra("date");
        type = intent.getStringExtra("type");
        status = intent.getStringExtra("status");
        merchantId = intent.getStringExtra("merchantId");
        recievedAmount = intent.getStringExtra("receivingAmount");
        commission = intent.getStringExtra("commission");

        Log.d("recieptNumber",recieptNumber);
        Log.d("name",name);
        Log.d("amount",amount);
        Log.d("date",date);
        Log.d("type",type);
        Log.d("status",status);
        Log.d("merchantId",merchantId);

        TextView recieptView = (TextView) findViewById(R.id.receipt_number);
        TextView nameView = (TextView) findViewById(R.id.usr_name);
        TextView amountView = (TextView) findViewById(R.id.amount);
        TextView dateView = (TextView) findViewById(R.id.date);
        TextView typeView = (TextView) findViewById(R.id.type);
        TextView statusView = (TextView) findViewById(R.id.status);
        TextView receivedAmountView = (TextView) findViewById(R.id.receivedAmount);
        TextView commissionView = (TextView) findViewById(R.id.commission);

        recieptView.setText(recieptNumber);
        nameView.setText(name);
        amountView.setText(amount+" LKR");
        dateView.setText(date );
        typeView.setText(type);
        statusView.setText(status);
        receivedAmountView.setText(recievedAmount +" LKR");
        commissionView.setText(commission +" LKR");

        button_void = (Button)findViewById(R.id.button_void);
        if(type.equals("refund") || status.equals("void")){
            button_void.setVisibility(View.GONE);
        }
        else{
            button_void.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button_void.setEnabled(false);
                    showProgress(true);
                    refundTransaction(merchantId,recieptNumber);


                }
            });
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width_px = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height_px = Resources.getSystem().getDisplayMetrics().heightPixels;

        int pixeldpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        float pixeldp = Resources.getSystem().getDisplayMetrics().density;

        width_dp = (width_px/pixeldpi)*160;
        height_dp = (height_px/pixeldpi)*160;
    }
    public void refundTransaction(String mechantId,String transactionId){

        JSONObject payload = new JSONObject();
        try {
            payload.put("merchantId",""+mechantId);
            payload.put("transactionId",""+transactionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyRequestHandlerApi.api(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("transactionAdapt",result.toString());
                showProgress(false);

                moveMerchantTransactionReport();

            }

            @Override
            public void login() {
                moveLogin();
            }

            @Override
            public void enableButton() {

            }
        }, Parameter.urlVoidTransaction, Api.getAccessToken(getApplicationContext()),payload,getApplicationContext());
    }
    public void moveLogin(){

        Intent myIntent = new Intent(this, LoginActivity.class);
        startActivity(myIntent);
        finish();
    }

    @Override
    public void onBackPressed() {

        moveMerchantTransactionReport();
        finish();

    }
    @Override
    public void onPause() {
        super.onPause();
        finish();

    }
    private void moveMerchantTransactionReport() {
        Intent myIntent = new Intent(this, ReportActivity.class);
        this.startActivity(myIntent);
        finish();
    }

    private void moveDashBoard() {
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
        finish();
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
