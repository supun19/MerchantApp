package com.directpay.paymedia.merchantapp.Services;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.directpay.paymedia.merchantapp.Component.VolleyCallback;
import com.directpay.paymedia.merchantapp.Component.VolleyComponent;


import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by supun on 01/06/17.
 */

public class VolleyRequestHandlerApi {


    public static void authenticateUser(final VolleyCallback callback, final Map<String,String> payload, final Context context){

        StringRequest jsObjRequest = new StringRequest
                (Request.Method.POST, Parameter.loginUrl, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        Log.d("response",response.toString());
                        try {
                            callback.onSuccess(new JSONObject(response.toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        // As of f605da3 the following should work
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            Toast.makeText(context,"Unauthorized", Toast.LENGTH_LONG).show();
                            callback.enableButton();
                        }
                        else if(networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST){
                            callback.enableButton();
                            Toast.makeText(context,"password wrong", Toast.LENGTH_LONG).show();

                        }

                        else if(error instanceof NoConnectionError){
                            callback.enableButton();
                            Toast.makeText(context,"Connection Error", Toast.LENGTH_LONG).show();
                        }

                        else if (error instanceof TimeoutError) {
                            Toast.makeText(context,"time out", Toast.LENGTH_LONG).show();
                            callback.enableButton();
                        }  else if (error instanceof ServerError || networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                            //TODO
                            Toast.makeText(context,"Server Error", Toast.LENGTH_LONG).show();
                        }  else if (error instanceof ParseError) {
                            //TODO
                            Toast.makeText(context,"Error in Application(Parse Error)", Toast.LENGTH_LONG).show();
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=payload;
                return params;

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                String key = Parameter.clientkey+":"+Parameter.secretkey;
                params.put("Authorization", "Basic "+ Base64.encodeToString(key.getBytes(), Base64.DEFAULT));
                return params;
            }

        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyComponent.getInstance(context).addToRequestQueue(jsObjRequest);

    }


    public static void api(final VolleyCallback callback, String url, final String token, JSONObject parameters, final Context context){



        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST,url,parameters , new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response",""+response.toString());
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        //Log.d("err",error.getMessage());
                        error.printStackTrace();
                        //callback.login();
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            callback.login();
                            Toast.makeText(context,"Unauthorized", Toast.LENGTH_LONG).show();
                        }
                        else if(networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST){
                            Toast.makeText(context,"error", Toast.LENGTH_LONG).show();

                            callback.enableButton();

                        }
                        else if(error instanceof NoConnectionError){
                            callback.login();
                            callback.enableButton();
                            Toast.makeText(context,"Connection Error", Toast.LENGTH_LONG).show();

                        }

                        else if (error instanceof TimeoutError) {
                            callback.enableButton();
                            Toast.makeText(context,"time out", Toast.LENGTH_LONG).show();

                        }  else if (error instanceof ServerError || networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                            //TODO
                            callback.enableButton();
                            Toast.makeText(context,"Server Error", Toast.LENGTH_LONG).show();
                        }  else if (error instanceof ParseError) {
                            //TODO
                            Toast.makeText(context,"Error in Application(Parse Error)", Toast.LENGTH_LONG).show();
                        }

                    }
                }){
            /* @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String,String> params=new HashMap<String,String>();
                 params.put("merchantId","12345");
                 params.put("amount","1000");
                 params.put("accessToken",""+token);

                 return params;

             }*/
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+token);
                return params;
            }

        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyComponent.getInstance(context).addToRequestQueue(jsObjRequest);
    }



}
