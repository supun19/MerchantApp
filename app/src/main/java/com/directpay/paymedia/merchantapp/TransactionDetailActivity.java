package com.directpay.paymedia.merchantapp;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.directpay.paymedia.merchantapp.Component.VolleyCallback;
import com.directpay.paymedia.merchantapp.Model.UserTransactionModel;
import com.directpay.paymedia.merchantapp.Services.Api;
import com.directpay.paymedia.merchantapp.Services.Parameter;
import com.directpay.paymedia.merchantapp.Services.VolleyRequestHandlerApi;

import org.json.JSONException;
import org.json.JSONObject;


public class TransactionDetailActivity extends AppCompatActivity {

    private UserTransactionModel model;
    private String recieptNumber;
    private String name;
    private String amount;
    private String date;
    private String type;
    private String status;
    private String merchantId;

    int width_dp;
    int height_dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        final Intent intent = getIntent();
        recieptNumber = intent.getStringExtra("receipt");
        name = intent.getStringExtra("name");
        amount = intent.getStringExtra("amount");
        date = intent.getStringExtra("date");
        type = intent.getStringExtra("type");
        status = intent.getStringExtra("status");
        merchantId = intent.getStringExtra("merchantId");

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

        recieptView.setText(recieptNumber);
        nameView.setText(name);
        amountView.setText(amount);
        dateView.setText(date);
        typeView.setText(type);
        statusView.setText(status);

        Button button_void = (Button)findViewById(R.id.button_void);
        if(type.equals("refund") || status.equals("void")){
            button_void.setVisibility(View.GONE);
        }
        else{
            button_void.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refundTransaction(merchantId,recieptNumber);
                    Intent intent = new Intent(TransactionDetailActivity.this,ReportActivity.class);
                    startActivity(intent);
                    finish();

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
        //moveLogin();
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
}
