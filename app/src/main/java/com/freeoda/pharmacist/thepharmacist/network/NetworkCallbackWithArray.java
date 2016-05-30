package com.freeoda.pharmacist.thepharmacist.network;


import com.freeoda.pharmacist.thepharmacist.exceptions.CustomException;
import com.freeoda.pharmacist.thepharmacist.models.ModelApi;

public interface NetworkCallbackWithArray {
    void onSuccess(ModelApi[] result);
    void onError(CustomException exception);
}
