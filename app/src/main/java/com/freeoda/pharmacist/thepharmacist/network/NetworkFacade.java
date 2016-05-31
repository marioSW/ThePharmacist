package com.freeoda.pharmacist.thepharmacist.network;

import android.content.Context;

import com.freeoda.pharmacist.thepharmacist.exceptions.CustomException;
import com.freeoda.pharmacist.thepharmacist.models.ModelApi;
import com.freeoda.pharmacist.thepharmacist.models.User;

/**
 * Created by Lakna on 3/20/2016.
 */
public abstract class NetworkFacade {

    public static void loginUser(final String email, String password,final String regId,Context context,final NetworkCallback callback){

        final UserResponse userResponse = new UserResponse(context);

        userResponse.loginRequest(email, password, regId, callback);

    }

    public static void registerUser(final User user,Context context,final NetworkCallback callback){

        final UserResponse userResponse = new UserResponse(context);

        userResponse.registerRequest(user, callback);

    }

    public static void sendEmailPwdReset(final String email,Context context,final NetworkCallback callback){

        final UserResponse userResponse = new UserResponse(context);

        userResponse.resetPwdEmailRequest(email, callback);

    }

    public static void sendCodePwdReset(final String code,Context context,final NetworkCallback callback){

        final UserResponse userResponse = new UserResponse(context);

        userResponse.resetPwdCodeRequest(code, callback);

    }

    public static void resetPassword(final String email,final String pwd,Context context,final NetworkCallback callback){

        final UserResponse userResponse = new UserResponse(context);

        userResponse.resetPasswordRequest(email, pwd, callback);

    }

    public static void getOrders(final String username,Context context,final  NetworkCallbackWithArray callback){

        final OrderResponse orderResponse = new OrderResponse(context);
        orderResponse.getConfirmedOrders(username,callback);
    }
}
