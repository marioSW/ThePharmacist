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
import com.freeoda.pharmacist.thepharmacist.models.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lakna on 3/20/2016.
 */
public class UserResponse extends PharmacistEndpoints {

    static RequestQueue queue;
    CustomException exception;

    public UserResponse(Context applicationContext) {
        queue = Volley.newRequestQueue(applicationContext);
    }

    public void loginRequest(final String email, final String password,final NetworkCallback callback){

        final String TAG = "TAG";

        StringRequest request = new StringRequest(Request.Method.POST,this.FIND_USER,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response.toString());
                        Gson gson = new Gson();
                        User user = gson.fromJson(response.toString(), User.class);
                        callback.onSuccess(user);

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
                params.put("password",password);
                params.put("email",email);
                return params;
            }
        };

        queue.add(request);
    }


    public void registerRequest(final User user,final NetworkCallback callback) {
        final String TAG = "TAG";

        StringRequest request = new StringRequest(Request.Method.POST,this.REGISTER_USER,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, "response: ");
                        Log.d(TAG, "response: " + response.toString());
                        com.freeoda.pharmacist.thepharmacist.models.Response response1 = new com.freeoda.pharmacist.thepharmacist.models.Response();
                        callback.onSuccess(response1);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "error: ");
                CustomException exception = new HttpExceptionHandler().networkException(error);
                callback.onError(exception);

            }

        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params  = new HashMap<>();
                Log.d(TAG, user.getFirstName());
                params.put("firstName", user.getFirstName());

                Log.d(TAG, user.getLastName());
                params.put("lastName", user.getLastName());

                Log.d(TAG, user.getEmail());
                params.put("email", user.getEmail());

                Log.d(TAG, user.getMobileNo());
                params.put("mobile", user.getMobileNo());

                Log.d(TAG, user.getBirthDate());
                params.put("birthday", user.getBirthDate());

                Log.d(TAG, user.getPassword());
                params.put("password", user.getPassword());

                return params;
            }

        };

        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
//        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }
}
