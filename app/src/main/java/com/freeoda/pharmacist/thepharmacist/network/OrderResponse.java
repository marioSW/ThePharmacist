package com.freeoda.pharmacist.thepharmacist.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.freeoda.pharmacist.thepharmacist.exceptions.CustomException;
import com.freeoda.pharmacist.thepharmacist.exceptions.HttpExceptionHandler;
import com.freeoda.pharmacist.thepharmacist.models.Order;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lakna on 5/9/2016.
 */
public class OrderResponse extends PharmacistEndpoints{

    static RequestQueue queue;
    CustomException exception;

    public OrderResponse(Context applicationContext) {
        queue = Volley.newRequestQueue(applicationContext);
    }

    public void getConfirmedOrders(final String username,final NetworkCallbackWithArray callback) {

        StringRequest request = new StringRequest(Request.Method.POST, this.GET_CONFIRMED_ORDERS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "Inside onSeccess");
                        Log.d("TAG", "response: " + response.toString());
                        Gson gson = new Gson();
                        Order[] orders = gson.fromJson(response.toString(), Order[].class);
                        callback.onSuccess(orders);
//
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "inside onError");
                Log.d("TAG", "error: ");
                Log.d("TAG", "Error response");
                CustomException exception = new HttpExceptionHandler().networkException(error);
                callback.onError(exception);

            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("username", username);
                return params;
            }
        };
        queue.add(request);
    }


    public void userConfirmOrder(final String pharId,final String orderId, final NetworkCallback callback){

        final String TAG = "TAG";

        StringRequest request = new StringRequest(Request.Method.POST,this.USER_CONFIRM,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response.toString());
                        Gson gson = new Gson();
//                        String result = gson.fromJson(response.toString(),);
                        com.freeoda.pharmacist.thepharmacist.models.Response response1 = new com.freeoda.pharmacist.thepharmacist.models.Response();
                        callback.onSuccess(response1);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                HttpExceptionHandler.networkException(error);
                CustomException exception = new HttpExceptionHandler().networkException(error);
                callback.onError(exception);
            }
        }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("pharId",pharId);
                params.put("orderId",orderId);
                return params;
            }
        };

        queue.add(request);
    }



    public void userDeclineOrder(final String pharId,final String orderId, final NetworkCallback callback){

        final String TAG = "TAG";

        StringRequest request = new StringRequest(Request.Method.POST,this.USER_DECLINE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response.toString());
                        Gson gson = new Gson();
//                        String result = gson.fromJson(response.toString(),);
                        com.freeoda.pharmacist.thepharmacist.models.Response response1 = new com.freeoda.pharmacist.thepharmacist.models.Response();
                        callback.onSuccess(response1);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                HttpExceptionHandler.networkException(error);
                CustomException exception = new HttpExceptionHandler().networkException(error);
                callback.onError(exception);
            }
        }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("pharId",pharId);
                params.put("orderId",orderId);
                return params;
            }
        };

        queue.add(request);
    }

}
