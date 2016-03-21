package com.freeoda.pharmacist.thepharmacist.network;

/**
 * Created by Lakna on 3/20/2016.
 */
public abstract class PharmacistEndpoints implements NetworkApi {

    protected final String FIND_USER = this.PHARMACIST_URL+"pharLogin.php";

    protected final String REGISTER_USER = this.PHARMACIST_URL+"registerUser.php";
}
