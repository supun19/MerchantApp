package com.directpay.paymedia.merchantapp.Model;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.directpay.paymedia.merchantapp.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by supun on 29/05/17.
 */

public class UserTransactionModel extends Application implements Serializable {
    private String fromAccountNumber;
    private String toAccountNumber;
    private String amount;
    private String date;
    private Merchant merchant;
    private String recieptNumber;
    private String type;
    private String status;

    private boolean appUserAccount_isFromAccountNuber;

    public UserTransactionModel(String fromAccountNumber, String amount, String date, Merchant merchant, String recieptNumber, String toAccountNumber) {

        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.date = date;
        this.merchant = merchant;
        this.recieptNumber = recieptNumber;

    }

    public UserTransactionModel(JSONObject object, String appUserId) {

        Log.d("transactionmodel",object.toString());
        try {


            this.amount = object.getString("originalAmount");
            JSONObject date = object.getJSONObject("dateTime");
            this.date = dateTimeFilter(date.getString("date"));
            this.recieptNumber = object.getString("id");
            this.type =object.getString("type");
            this.status = object.getString("status");

            this.toAccountNumber = getUserAccountNumber(object.getJSONObject("payeeDetail")) ;
            //this.toAccountRole = getRole(object.getJSONObject("payeeDetail"));
            this.fromAccountNumber = getUserAccountNumber(object.getJSONObject("payerDetail")) ;
            this.appUserAccount_isFromAccountNuber = checkAppAccountIsFromAccountNumber(object.getJSONObject("payerDetail"),appUserId);
            //this.fromAccountRole = getRole(object.getJSONObject("payerDetail"));


            if(this.appUserAccount_isFromAccountNuber){
                JSONObject payeeDetaildata = object.getJSONObject("payeeDetail");
                JSONArray data = payeeDetaildata.getJSONArray("data");
                JSONObject payeeDetail = data.getJSONObject(0);

                Merchant merchantTemp = new Merchant(payeeDetail.getString("id"));

                if(payeeDetail.getJSONArray("roles").get(0).equals("merchant")){
                    merchantTemp.setMerchantName(payeeDetail.getString("merchantName"));
                }
                else{
                    merchantTemp.setMerchantName(payeeDetail.getString("username"));
                }

                this.merchant = merchantTemp;
            }
            else{
                JSONObject payeeDetaildata = object.getJSONObject("payerDetail");
                JSONArray data = payeeDetaildata.getJSONArray("data");
                JSONObject payeeDetail = data.getJSONObject(0);

                Merchant merchantTemp = new Merchant(payeeDetail.getString("id"));
                if(payeeDetail.getJSONArray("roles").get(0).equals("merchant")){
                    merchantTemp.setMerchantName(payeeDetail.getString("merchantName"));
                }
                else{
                    merchantTemp.setMerchantName(payeeDetail.getString("username"));
                }
                this.merchant = merchantTemp;
            }

//            if(this.type.equals("Merchant_Pay")){
//                this.fromAccountNumber = getUserAccountNumber(object.getJSONObject("payerDetail")) ;
//                this.toAccountNumber =getPayeeName(object.getJSONObject("payeeDetail")) ;
//                JSONObject payeeDetaildata = object.getJSONObject("payeeDetail");
//                JSONArray data = payeeDetaildata.getJSONArray("data");
//                JSONObject payeeDetail = data.getJSONObject(0);
//                Merchant merchantTemp = new Merchant(payeeDetail.getString("merchantId"));
//                merchantTemp.setMerchantName(payeeDetail.getString("username"));
//                this.merchant = merchantTemp;
//
//            }
//            else if(this.type.equals("refund")){
//                this.fromAccountNumber = getUserAccountNumber(object.getJSONObject("payeeDetail")) ;
//                this.toAccountNumber =getPayeeName(object.getJSONObject("payerDetail")) ;
//                JSONObject payerDetaildata = object.getJSONObject("payerDetail");
//                JSONArray data = payerDetaildata.getJSONArray("data");
//                JSONObject payerDetail = data.getJSONObject(0);
//                Merchant merchantTemp = new Merchant(payerDetail.getString("merchantId"));
//                merchantTemp.setMerchantName(payerDetail.getString("username"));
//                this.merchant = merchantTemp;
//            }

//                this.fromAccountNumber = getUserAccountNumber(object.getJSONObject("payerDetail")) ;
//                this.toAccountNumber =getPayeeName(object.getJSONObject("payeeDetail")) ;
//                JSONObject payeeDetaildata = object.getJSONObject("payeeDetail");
//                JSONArray data = payeeDetaildata.getJSONArray("data");
//                JSONObject payeeDetail = data.getJSONObject(0);
//                Merchant merchantTemp = new Merchant(payeeDetail.getString("id"));
//                merchantTemp.setMerchantName(payeeDetail.getString("username"));
//                this.merchant = merchantTemp;


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getRecieptNumber() {
        return recieptNumber;
    }

    public void setRecieptNumber(String recieptNumber) {
        this.recieptNumber = recieptNumber;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static ArrayList<UserTransactionModel> getTransaction() {
        ArrayList<UserTransactionModel> transactions = new ArrayList<UserTransactionModel>();

        return transactions;
    }

    public static ArrayList<UserTransactionModel> getTransaction(JSONArray transaction, String appUserId) {
        ArrayList<UserTransactionModel> transactionModels = new ArrayList<UserTransactionModel>();
        for (int i = 0; i < transaction.length(); i++) {
            try {
                transactionModels.add(new UserTransactionModel(transaction.getJSONObject(i),appUserId));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return transactionModels;
    }
    public String dateTimeFilter(String date){

        String dateout="";
        try {
            Date datein = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse(date);
           dateout = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datein);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateout;
    }

    public void moveLogin(){

        Intent myIntent = new Intent(this, LoginActivity.class);
        this.startActivity(myIntent);
    }


    public String getPayeeName(JSONObject payeeDetail){
        if(payeeDetail.has("data")){
            JSONArray array= (JSONArray) payeeDetail.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                Log.d("payeename",jsonObject.toString());
                return jsonObject.opt("username").toString();



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return "";
    }
    public String getUserAccountNumber(JSONObject payerDetail){
        if(payerDetail.has("data")){
            JSONArray array= (JSONArray) payerDetail.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                Log.d("payerDetail",payerDetail.toString());
                return jsonObject.opt("accountNumber").toString();



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return "";
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }
    public boolean checkAppAccountIsFromAccountNumber(JSONObject payerDetail, String id){

        if(payerDetail.has("data")){
            JSONArray array= (JSONArray) payerDetail.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                Log.d("payerDetail",payerDetail.toString());
                if(jsonObject.opt("id").toString().equals(id)){
                    return true;

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return false;
    }

    public boolean isAppUserAccount_isFromAccountNuber() {
        return appUserAccount_isFromAccountNuber;
    }

    public void setAppUserAccount_isFromAccountNuber(boolean appUserAccount_isFromAccountNuber) {
        this.appUserAccount_isFromAccountNuber = appUserAccount_isFromAccountNuber;
    }
}
