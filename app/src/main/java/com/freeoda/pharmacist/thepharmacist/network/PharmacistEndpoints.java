package com.freeoda.pharmacist.thepharmacist.network;

/**
 * Created by Lakna on 3/20/2016.
 */
public abstract class PharmacistEndpoints implements NetworkApi {

    protected final String FIND_USER = this.PHARMACIST_URL+"pharLogin.php";

    protected final String REGISTER_USER = this.PHARMACIST_URL+"registerUser.php";

    protected final String SEND_EMAIL_RESET_PWD = this.PHARMACIST_URL+"resetPwdSendCode.php";

    protected final String SEND_CODE_RESET_PWD = this.PHARMACIST_URL+"compareCode.php";

    protected final String RESET_PWD = this.PHARMACIST_URL+"resetPwd.php";

    protected final String GET_CONFIRMED_ORDERS = this.PHARMACIST_URL+"getConfirmedOrders.php";

    protected final String USER_CONFIRM = this.PHARMACIST_URL+"userConfirmed.php";

    protected final String USER_DECLINE = this.PHARMACIST_URL+"userDeclined.php";


}
