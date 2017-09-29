package com.directpay.paymedia.merchantapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;

public class ReportActivity extends AppCompatActivity {

    boolean flag;
    private int page=1;
    private int headersize;
    private Button btn_load;
    private TextView text_from_date;
    private TextView text_to_date;
    private int mYear, mMonth, mDay;
    private int fYear, fMonth, fDay;
    private int tYear, tMonth, tDay;

    private boolean selectDate;
    private boolean selectAccount;

    private EditText searchView_account;
    private MerchantTransactionAdapter adapter;
    private ListView listView;
    private boolean loadDate;
    private RelativeLayout loadingbar;
    private AbsListView.OnScrollListener listScrollListner;
    private boolean scrollbyfinger;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecurityHandler.handleSSLHandshake();
        setContentView(R.layout.activity_report);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //populateScanList();

        }

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
        listView = (ListView) findViewById(R.id.merchantTransactionList);

        searchView_account = (EditText) findViewById(R.id.search_box);


        text_from_date = (TextView) findViewById(R.id.txt_from);
        text_to_date = (TextView) findViewById(R.id.txt_todate);

        btn_load = (Button) findViewById(R.id.btn_load);
        loadingbar = (RelativeLayout) findViewById(R.id.loadingPanel);
        loadingbar.setVisibility(View.GONE);

        date_header.setWidth(headersize);
        detail_header.setWidth(headersize);
        amount_header.setWidth(headersize);

        text_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFromDate();
            }
        });
        text_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToDate(
                );
            }
        });
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.setSave(getApplicationContext(),null);
                page=1;
                listView.setAdapter(null);
                loadingbar.setVisibility(View.VISIBLE);
                getMerchantTransaction();


            }
        });
        Search();
        currentDate();

        if(Api.isSave(getApplicationContext())) {
            loadDate=true;
            JSONArray array= Api.getTransactionHistory(getApplicationContext());
            populateTransactionList(array);
        }

        listScrollListner = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if(scrollState == SCROLL_STATE_FLING){
                    scrollbyfinger = true;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                if (!flag && scrollbyfinger) {
                    flag = true;
                    scrollbyfinger = false;
                    page++;
                    getMerchantTransaction();
                }
                Log.d("page", page + "");

            }
        };


    }
    public void getMerchantTransaction() {

        JSONObject parameter = new JSONObject();
        try {
            parameter.put("page",page);
            parameter.put("offset",Parameter.offset);
            parameter.put("userId",""+ Api.getRegisterId(getApplicationContext()));
            JSONObject fromdate = new JSONObject();
            fromdate.put("year",fYear);
            fromdate.put("month",fMonth);
            fromdate.put("day",fDay);
            JSONObject todate = new JSONObject();
            if(fYear == tYear && fMonth==tMonth && fDay == tDay){
                getnextDay(fYear,fMonth,fDay);
            }

            todate.put("year",tYear);
            todate.put("month",tMonth);
            todate.put("day",tDay);


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

            if (Api.isSave(getApplicationContext())){
                JSONArray temp=Api.getTransactionHistory(getApplicationContext());
                try {
                    array=concatArray(array,temp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            try {
                if(array.length()!=0){
                    JSONObject jsonObject = array.getJSONObject(0);
                    Log.d("Transaction:Transaction",jsonObject.toString());

                    //save transaction data
                    Api.setSave(getApplicationContext(),array);
                    listView.setOnScrollListener(null);

                    populateTransactionList(array);
                    if(Api.isSave(getApplicationContext())){
                        listView.setOnScrollListener(listScrollListner);
                    }
                    loadDate=true;
                    flag=false;
                    loadingbar.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(getApplicationContext(),"No Any Transaction",Toast.LENGTH_LONG).show();
                }

                loadingbar.setVisibility(View.GONE);

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

    public JSONArray concatArray(JSONArray array,JSONArray temp) throws JSONException{
        JSONArray result=new JSONArray();
        for(int i=0; i<array.length();i++){
            result.put(array.get(i));
        }
        for(int i=0; i<temp.length();i++){
            result.put(temp.get(i));
        }
        return result;
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
        adapter = new MerchantTransactionAdapter(this, arrayOfTrnsactions,headersize);
        // Attach the adapter to a ListView
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
                    intent.putExtra("name",model.getUser().getLastName());
                }
                else{
                    intent.putExtra("name",model.getUser().getLastName());

                }

                intent.putExtra("amount",model.getAmount());
                intent.putExtra("date",model.getDate());
                intent.putExtra("type",model.getType());
                intent.putExtra("status",model.getStatus());
                intent.putExtra("merchantId",model.getMerchant().getId());
                intent.putExtra("receivingAmount",model.getRecievedAmount());
                intent.putExtra("commission",model.getCommission());
                intent.putExtra("userAccount", model.getToAccountNumber());
                intent.putExtra("merchantAccount",model.getFromAccountNumber());
                intent.putExtra("userPhone",model.getUser().getPhoneNumber());
                intent.putExtra("merchantPhone",model.getMerchant().getPhoneNumber());
                startActivity(intent);

            }
        });


    }


    public void setToDate() {



            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            tYear = year;
                            tMonth = monthOfYear+1;
                            tDay = dayOfMonth;

                            text_to_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();


    }



    public void setFromDate() {



        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        fYear = year;
                        fMonth = monthOfYear+1;
                        fDay = dayOfMonth;

                        text_from_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }


    private void Search() {
        // Capture Text in EditText
        searchView_account.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if(loadDate){
                    ReportActivity.this.adapter.getFilter().filter(cs);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Load Transaction First",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

    }
    public void currentDate(){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        fYear =  c.get(Calendar.YEAR);
        fMonth = c.get(Calendar.MONTH) + 1;
        fDay = c.get(Calendar.DAY_OF_MONTH);

        tYear =  c.get(Calendar.YEAR);
        tMonth = c.get(Calendar.MONTH) + 1;
        tDay = c.get(Calendar.DAY_OF_MONTH);

        text_to_date.setText(fDay + "-" + fMonth + "-" + fYear);
        text_from_date.setText(tDay + "-" + tMonth + "-" + tYear);
    }
    public void getnextDay(int year,int month,int day){

        final Calendar c = Calendar.getInstance();
        c.set(year,month,day);
        c.add(Calendar.DATE,+1);
        tYear = c.get(Calendar.YEAR);
        tMonth = c.get(Calendar.MONTH)+1;
        tDay = c.get(Calendar.DAY_OF_MONTH);
    }
    private void moveDashBoard() {
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
        finish();
        Api.setSave(getApplicationContext(),null);
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
