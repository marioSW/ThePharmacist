package com.freeoda.pharmacist.thepharmacist.network;

import com.freeoda.pharmacist.thepharmacist.exceptions.CustomException;
import com.freeoda.pharmacist.thepharmacist.models.ModelApi;

/**
 * Created by Lakna on 3/20/2016.
 */
public interface NetworkCallback {
    void onSuccess(ModelApi result);
    void onError(CustomException exception);
}
