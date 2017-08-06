package com.directpay.paymedia.merchantapp;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.directpay.paymedia.merchantapp.Component.MerchantTransactionAdapter;
import com.directpay.paymedia.merchantapp.Component.VolleyCallback;
import com.directpay.paymedia.merchantapp.Model.MerchantTransactionModel;
import com.directpay.paymedia.merchantapp.Services.Api;
import com.directpay.paymedia.merchantapp.Services.Parameter;
import com.directpay.paymedia.merchantapp.Services.SecurityHandler;
import com.directpay.paymedia.merchantapp.Services.VolleyRequestHandlerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    private int headersize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_report);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width_px = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height_px = Resources.getSystem().getDisplayMetrics().heightPixels;

        int pixeldpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        float pixeldp = Resources.getSystem().getDisplayMetrics().density;

        int width_dp = (width_px/pixeldpi)*160;
        int height_dp = (height_px/pixeldpi)*160;

        int button_width = (width_dp-32);

        int screen_rest_width = (int) (button_width*pixeldp);
        headersize = screen_rest_width/3;

        TextView date_header = (TextView) findViewById(R.id.date_header);
        TextView detail_header = (TextView)findViewById(R.id.detail_header);
        TextView amount_header = (TextView)findViewById(R.id.amount_header);
        date_header.setWidth(headersize);
        detail_header.setWidth(headersize);
        amount_header.setWidth(headersize);

        getMerchantTransaction();

    }
    public void getMerchantTransaction() {
        JSONObject parameter = new JSONObject();
        try {
            parameter.put("userId",""+ Api.getRegisterId(getApplicationContext()));
            JSONObject fromdate = new JSONObject();
            fromdate.put("year",2017);
            fromdate.put("month",1);
            fromdate.put("day",1);

            JSONObject todate = new JSONObject();
            todate.put("year",2018);
            todate.put("month",6);
            todate.put("day",22);

            parameter.put("fromDate",fromdate);
            parameter.put("toDate",todate);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        VolleyRequestHandlerApi.api(new VolleyCallback(){


            @Override
            public void onSuccess(JSONObject result) {
                responseProcess(result);
            }

            @Override
            public void login() {
                moveLogin();
            }

            @Override
            public void enableButton() {

            }
        }, Parameter.urlTransactionDetail,Api.getAccessToken(getApplicationContext()),parameter,getApplicationContext());

    }
    private void responseProcess(JSONObject result){

        if(result.has("data")){
            JSONArray array= (JSONArray) result.opt("data");
            try {
                if(array.length()!=0){
                    JSONObject jsonObject = array.getJSONObject(0);
                    Log.d("Transaction:Transaction",jsonObject.toString());
                    populateTransactionList(array);
                }
                else {
                    Toast.makeText(getApplicationContext(),"No Any Transaction",Toast.LENGTH_LONG).show();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else if(result.has("errors")){
            JSONArray array= (JSONArray) result.opt("errors");
            try {
                JSONObject jsonObject = array.getJSONObject(0);

                if(jsonObject.opt("status").toString().equals("422")){
                    Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveLogin(){

        Intent myIntent = new Intent(this, LoginActivity.class);
        startActivity(myIntent);
        finish();
    }
    private void populateTransactionList(JSONArray transactions) {
        Log.d("Transaction activity",transactions.toString());
        // Construct the data source
        final ArrayList<MerchantTransactionModel> arrayOfTrnsactions = MerchantTransactionModel.getTransaction(transactions,Api.getRegisterId(getApplicationContext()));
        // Create the adapter to convert the array to views
        MerchantTransactionAdapter adapter = new MerchantTransactionAdapter(this, arrayOfTrnsactions,headersize);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.merchantTransactionList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("Merchant","click");

                Intent intent = new Intent(ReportActivity.this,TransactionDetailActivity.class);
                MerchantTransactionModel model = arrayOfTrnsactions.get(position);
//                Log.d("Merchant",""+model.getRecieptNumber()+"");
//                Log.d("Merchant",""+model.getFromAccountNumber()+"");
//                Log.d("Merchant",""+model.getAmount()+"");
//                Log.d("Merchant",""+model.getDate()+"");
//                Log.d("Merchant",""+model.getType()+"");
//                Log.d("Merchant",""+model.getStatus()+"");
//                Log.d("Merchant",""+model.getMerchant().getId()+"");
                intent.putExtra("receipt",model.getRecieptNumber());
                if(model.isAppUserAccount_isFromAccountNuber()){
                    intent.putExtra("name",model.getToAccountNumber());
                }
                else{
                    intent.putExtra("name",model.getFromAccountNumber());

                }

                intent.putExtra("amount",model.getAmount());
                intent.putExtra("date",model.getDate());
                intent.putExtra("type",model.getType());
                intent.putExtra("status",model.getStatus());
                intent.putExtra("merchantId",model.getMerchant().getId());
                startActivity(intent);

            }
        });


    }
}
