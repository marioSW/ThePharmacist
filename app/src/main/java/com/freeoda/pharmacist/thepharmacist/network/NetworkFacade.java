package com.freeoda.pharmacist.thepharmacist.network;

import android.content.Context;

import com.freeoda.pharmacist.thepharmacist.exceptions.CustomException;
import com.freeoda.pharmacist.thepharmacist.models.ModelApi;
import com.freeoda.pharmacist.thepharmacist.models.User;

/**
 * Created by Lakna on 3/20/2016.
 */
public abstract class NetworkFacade {

    public static void loginUser(final String email, String password,Context context,final NetworkCallback callback){

        final UserResponse userResponse = new UserResponse(context);

        userResponse.loginRequest(email, password,callback);

    }

    public static void registerUser(final User user,Context context,final NetworkCallback callback){

        final UserResponse userResponse = new UserResponse(context);

        userResponse.registerRequest(user,callback);

    }
}
