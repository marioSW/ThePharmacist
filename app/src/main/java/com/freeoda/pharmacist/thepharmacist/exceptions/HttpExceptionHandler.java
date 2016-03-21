package com.freeoda.pharmacist.thepharmacist.exceptions;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by Lakna on 3/20/2016.
 */
public class HttpExceptionHandler extends Throwable implements CustomException {

    private String message;
    private static final String TAG = "TAG";

    public CustomException networkException(VolleyError error) {
        try {
//        Log.e("TAG", "error networkresponse : "+error.networkResponse);
// Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
            // For AuthFailure, you can re login with user credentials.
            // For ClientError, 400 & 401, Errors happening on client side when sending api request.
            // In this case you can check how client is forming the api and debug accordingly.
            // For ServerError 5xx, you can do retry or handle accordingly.
            if (error instanceof NetworkError) {
                Log.d(TAG, "NetworkError: ");
                this.message = "NetworkError";
            } else if (error instanceof ClientError) {
                Log.d(TAG, "ClientError: ");
                this.message = "ClientError";
            } else if (error instanceof ServerError) {
                Log.d(TAG, "ServerError: ");
                this.message = "ServerError";
            } else if (error instanceof AuthFailureError) {
                Log.d(TAG, "AuthFailureError: ");
                this.message = "AuthFailureError";
            } else if (error instanceof ParseError) {
                Log.d(TAG, "ParseError: ");
                this.message = "ParseError";
            } else if (error instanceof NoConnectionError) {
                Log.d(TAG, "NoConnectionError: ");
                this.message = "NoConnectionError";
            } else if (error instanceof TimeoutError) {
                Log.d(TAG, "TimeoutError: ");
                this.message = "TimeoutError";
            } else {
                Log.d(TAG, "unknown error: ");
                this.message = "Unknown Error";
            }

        } catch (Exception e) {
            error.printStackTrace();
        } finally {
            return this;
        }
    }
}
