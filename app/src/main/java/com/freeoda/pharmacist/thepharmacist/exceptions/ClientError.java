package com.freeoda.pharmacist.thepharmacist.exceptions;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * Created by Lakna on 3/20/2016.
 */
public class ClientError extends VolleyError {
    public ClientError(NetworkResponse networkResponse) {
        super(networkResponse);
    }

    public ClientError() {
    }
}