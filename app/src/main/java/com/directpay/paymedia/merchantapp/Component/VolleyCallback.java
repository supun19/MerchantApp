package com.directpay.paymedia.merchantapp.Component;

import org.json.JSONObject;

/**
 * Created by supun on 01/06/17.
 */

public interface VolleyCallback {

    void onSuccess(JSONObject result);
    void login();
    void enableButton();
}
