package com.directpay.paymedia.merchantapp.Model;

import android.renderscript.Sampler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by supun on 09/07/17.
 */

public class MerchantTransactionModel {
    private String fromAccountNumber;
    private String toAccountNumber;
    private String amount;
    private String recievedAmount;
    private String commission;
    private String date;
    private Merchant merchant;
    private String recieptNumber;
    private String type;
    private String status;
    private User user;
    private boolean isOtherAccountOwnerRoleIsMerchant;
    public boolean merchantTo;

    private boolean appUserAccount_isFromAccountNuber;


    public MerchantTransactionModel(String fromAccountNumber, String amount, String date, Merchant merchant, String recieptNumber, String toAccountNumber) {

        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.date = date;
        this.merchant = merchant;
        this.recieptNumber = recieptNumber;
    }

    public MerchantTransactionModel(JSONObject object, String appUserId) {

        Log.d("transactionmodel",object.toString());
        try {

            this.amount = object.getString("originalAmount");
            this.recievedAmount = object.getString("receivingAmount");
            this.commission = String.valueOf((Double.parseDouble(this.amount)-Double.parseDouble(this.recievedAmount)));
            JSONObject date = object.getJSONObject("dateTime");
            this.recieptNumber = object.getString("id");
            this.status = object.getString("status");
            this.date = dateTimeFilter(date.getString("date"));
            this.type =object.getString("type");

                this.toAccountNumber = getPayerAccountNumber(object.getJSONObject("payeeDetail")) ;
                //this.toAccountRole = getRole(object.getJSONObject("payeeDetail"));
                this.fromAccountNumber = getPayerAccountNumber(object.getJSONObject("payerDetail")) ;
                this.appUserAccount_isFromAccountNuber = checkAppAccountIsFromAccountNumber(object.getJSONObject("payerDetail"),appUserId);
                //this.fromAccountRole = getRole(object.getJSONObject("payerDetail"));
                if(this.appUserAccount_isFromAccountNuber){
                    JSONObject payeeDetaildata = object.getJSONObject("payeeDetail");
                    JSONArray payeeData = payeeDetaildata.getJSONArray("data");
                    JSONObject payeeDetail = payeeData.getJSONObject(0);
                    JSONObject payerDetaildata = object.getJSONObject("payerDetail");
                    JSONArray payerData = payerDetaildata.getJSONArray("data");
                    JSONObject payerDetail = payerData.getJSONObject(0);

                    this.merchant = new Merchant(payerDetail.getString("id"));
                    this.merchant.setMerchantName(payerDetail.getString("merchantName"));
                    this.user = new User();
                    this.user.setFirstname(payeeDetaildata.getString("firstName"));
                    this.user.setLastName(payeeDetaildata.getString("lastName"));




                }
                else{
                    JSONObject payerDetaildata = object.getJSONObject("payerDetail");
                    JSONArray payerData = payerDetaildata.getJSONArray("data");
                    JSONObject payerDetail = payerData.getJSONObject(0);

                    JSONObject payeeDetaildata = object.getJSONObject("payeeDetail");
                    JSONArray payeeData = payeeDetaildata.getJSONArray("data");
                    JSONObject payeeDetail = payeeData.getJSONObject(0);


                    this.merchant = new Merchant(payeeDetail.getString("id"));
                    this.merchant.setMerchantName(payeeDetail.getString("merchantName"));
                    this.user = new User();
                    this.user.setFirstname(payerDetaildata.getString("firstName"));
                    this.user.setLastName(payerDetaildata.getString("lastName"));


                }

//
//            else if(this.type.equals("refund")){
//
//                Log.d("psyerDetsil",object.getJSONObject("payerDetail").toString());
//                this.toAccountNumber = getMerchantName(object.getJSONObject("payerDetail")) ;
//                //this.toAccountRole = getRole(object.getJSONObject("payerDetail"));
//                this.fromAccountNumber = getPayerAccountNumber(object.getJSONObject("payeeDetail")) ;
//                this.appUserAccount_isFromAccountNuber = checkAppAccountIsFromAccountNumber(object.getJSONObject("payeeDetail"),appUserId);
//
//                //this.fromAccountRole = getRole(object.getJSONObject("payeeDetail"));
//                JSONObject payerDetaildata = object.getJSONObject("payerDetail");
//                JSONArray data = payerDetaildata.getJSONArray("data");
//                JSONObject payerDetail = data.getJSONObject(0);
//                Merchant merchantTemp = new Merchant(payerDetail.getString("merchantId"));
//                merchantTemp.setMerchantName(payerDetail.getString("username"));
//                this.merchant = merchantTemp;
//            }
//            else if(this.type.equals("Fund_Transfer")){
//
//                this.toAccountNumber = getMerchantName(object.getJSONObject("payeeDetail")) ;
//                //this.toAccountRole = getRole(object.getJSONObject("payeeDetail"));
//                this.fromAccountNumber = getPayerAccountNumber(object.getJSONObject("payerDetail")) ;
//                //this.fromAccountRole = getRole(object.getJSONObject("payerDetail"));
//                JSONObject payeeDetaildata = object.getJSONObject("payeeDetail");
//                JSONArray data = payeeDetaildata.getJSONArray("data");
//                JSONObject payeeDetail = data.getJSONObject(0);
//
//                Merchant merchantTemp = new Merchant(payeeDetail.getString("id"));
//                merchantTemp.setMerchantName(payeeDetail.getString("username"));
//                this.merchant = merchantTemp;
//            }

//            this.fromAccountNumber = getPayerAccountNumber(object.getJSONObject("payerDetail")) ;
//            this.toAccountNumber =getMerchantName(object.getJSONObject("payeeDetail")) ;
//            JSONObject payeeDetaildata = object.getJSONObject("payeeDetail");
//            JSONArray data = payeeDetaildata.getJSONArray("data");
//            JSONObject payeeDetail = data.getJSONObject(0);
//            Merchant merchantTemp = new Merchant(payeeDetail.getString("id"));
//            merchantTemp.setMerchantName(payeeDetail.getString("username"));
//            this.merchant = merchantTemp;





        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getRecievedAmount() {
        return recievedAmount;
    }

    public void setRecievedAmount(String recievedAmount) {
        this.recievedAmount = recievedAmount;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isOtherAccountOwnerRoleIsMerchant() {
        return isOtherAccountOwnerRoleIsMerchant;
    }

    public void setOtherAccountOwnerRoleIsMerchant(boolean otherAccountOwnerRoleIsMerchant) {
        isOtherAccountOwnerRoleIsMerchant = otherAccountOwnerRoleIsMerchant;
    }

    public boolean isMerchantTo() {
        return merchantTo;
    }

    public void setMerchantTo(boolean merchantTo) {
        this.merchantTo = merchantTo;
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

    public static ArrayList<MerchantTransactionModel> getTransaction() {
        ArrayList<MerchantTransactionModel> transactions = new ArrayList<MerchantTransactionModel>();

        return transactions;
    }

    public static ArrayList<MerchantTransactionModel> getTransaction(JSONArray transaction, String appUserId) {
        ArrayList<MerchantTransactionModel> transactionModels = new ArrayList<MerchantTransactionModel>();
        for (int i = 0; i < transaction.length(); i++) {
            try {
                transactionModels.add(new MerchantTransactionModel(transaction.getJSONObject(i),appUserId));
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

//        Intent myIntent = new Intent(this,loginActivity.class);
////        this.startActivity(myIntent);
    }


    public String getMerchantName(JSONObject payeeDetail){
        if(payeeDetail.has("data")){
            JSONArray array= (JSONArray) payeeDetail.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                return jsonObject.opt("username").toString();



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return "";
    }
    public String getRole(JSONObject detail){
        if(detail.has("data")){
            JSONArray array= (JSONArray) detail.opt("data");
            try {
                JSONObject jsonObject = array.getJSONObject(0);
                JSONArray roles = jsonObject.getJSONArray("roles");
                return  roles.get(0).toString();



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return "";

    }
    public String getPayerAccountNumber(JSONObject payerDetail){
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
