package com.directpay.paymedia.merchantapp.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.directpay.paymedia.merchantapp.R;


/**
 * Created by supun on 07/05/17.
 */

public class Api {


    public static String getAccessToken(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.access_token), Context.MODE_PRIVATE);
        String token =sharedPref.getString("access_token",null);

        return token;
    }
    public static String getNic(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String nic =sharedPref.getString("nic",null);

        return nic;
    }
    public static String getPhoneNumber(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String nic =sharedPref.getString("phoneNumber",null);

        return nic;
    }
    public static boolean setAccessToken(Context context, String token){
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.access_token), context.MODE_PRIVATE).edit();
        editor.putString("access_token", token);
        editor.apply();
        return true;
    }
    public static boolean setRegisterId(Context context, String id){
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();

        editor.putString("register_id", id);
        editor.putBoolean("register", true);
        editor.apply();

        return true;
    }

    public static boolean setRegisterVerify(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();

        editor.putBoolean("register_verify", true);
        editor.apply();
        return true;
    }
    public static void setNic(Context context,String nic){
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();
        editor.putString("nic", nic);
        editor.apply();
    }
    public static void setMerchantActivate(Context context,boolean active){
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();
        editor.putBoolean("activate", active);
        editor.apply();
    }
    public static boolean isMerchantActivate(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        boolean token =sharedPref.getBoolean("activate",true);
        return token;
    }
    public static boolean reSetPin(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();

        editor.putBoolean("register_verify", false);
        editor.apply();
        return true;
    }

    public static String getId(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String token = sharedPref.getString("id", null);

        return token;
    }
    public static String getRegisterId(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String token =sharedPref.getString("register_id",null);

        return token;
    }
    public static boolean isRegisterVerify(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        boolean token =sharedPref.getBoolean("register_verify",false);
        return token;
    }
    public static boolean isFirstTimeLogin(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        boolean token =sharedPref.getBoolean("first_login",true);
        return token;
    }
    public static void setFirstTimeLogin(Context context, String nic){
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.register), context.MODE_PRIVATE).edit();

        editor.putString("nic", nic);
        editor.putBoolean("first_login", false);
        editor.apply();

    }
    public static boolean isMerchant(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        String token =sharedPref.getString("role",null);
        if(token!=null && token.equals("merchant")){
            return true;
        }
        return false;
    }
    public static boolean isRegister(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(String.valueOf(R.string.register), Context.MODE_PRIVATE);
        boolean token =sharedPref.getBoolean("register",false);
       return  token;
    }



    public static void sendSms(String phoneNumber, String message, Context context) {
        try{

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, null, null);
            //Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Log.d("exception:sms","Sms");
            Toast.makeText(context,
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Log.d("exception:sms","Sms");
        }
        catch (Error error){

        }




    }
}
