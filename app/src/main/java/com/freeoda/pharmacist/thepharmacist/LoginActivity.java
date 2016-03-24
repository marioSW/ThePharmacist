package com.freeoda.pharmacist.thepharmacist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.freeoda.pharmacist.thepharmacist.exceptions.CustomException;
import com.freeoda.pharmacist.thepharmacist.models.ModelApi;
import com.freeoda.pharmacist.thepharmacist.network.NetworkCallback;
import com.freeoda.pharmacist.thepharmacist.network.NetworkFacade;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Lakna on 3/16/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    Button signinGoogleBtn;
    Button loginBtn;
    private LoginButton signinFbBtn;
    EditText username;
    EditText password;
    CallbackManager callbackManager;
    private ProgressDialog pDialog;
    String user;
    String pass;
    private static final String LOGIN_URL = "http://pharmacist.freeoda.com/customerLogin.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static final int RC_SIGN_IN = 9001;// Logcat tag
    private static final String TAG = "LoginActivity";

    // Google client to interact with Google API
    private static GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.login_loginBtn);
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        signinGoogleBtn = (Button) findViewById(R.id.googleloginBtn);
        signinFbBtn = (LoginButton) findViewById(R.id.fbloginBtn);
        signinFbBtn.setBackgroundResource(R.drawable.fb_button_click_effect);
        signinFbBtn.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();


        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        signinFbBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (isOnline()) {
                    // App code
                    Log.i("TAG", "Before graph request");
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    // Application code
                                    try {
                                        JSONObject jobject = new JSONObject(response.getRawResponse());
                                        String googleFbName = jobject.getString("first_name") + " " + jobject.getString("last_name");
                                        Log.i("TAG", googleFbName);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Intent loginIntent = new Intent(LoginActivity.this,Home.class);
                                    startActivity(loginIntent);


                                }

                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,first_name,last_name,location,email,gender, birthday,verified");
                    request.setParameters(parameters);
                    request.executeAsync();
                } else {
                    Toast.makeText(getApplicationContext(), "Connection unavailble", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancel() {
                // App code
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                if (isOnline()) {
                    Log.v("LoginActivity", exception.getCause().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Connection unavaia" +
                            "lble", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signinGoogleBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

        Button login_create_account = (Button) findViewById(R.id.login_create_account);
        login_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,EnterNameActivity.class));
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.googleloginBtn: {
                if (isOnline()) {
                    signIn();
                } else {
                    Toast.makeText(getApplicationContext(), "Connection unavailble", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.login_loginBtn: {
                user = username.getText().toString();
                pass = password.getText().toString();

                NetworkFacade.loginUser(user, pass, getApplicationContext(), new NetworkCallback() {
                    @Override
                    public void onSuccess(ModelApi result) {
                        startActivity(new Intent(LoginActivity.this, Home.class));
                    }

                    @Override
                    public void onError(CustomException exception) {
                        Toast.makeText(getApplicationContext(), "login failed", Toast.LENGTH_LONG).show();

                    }
                });

            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.i("TAG", "Google API connected");
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            Log.i("TAG", "Google API disconnected");
        }
    }


    /**
     * Method to resolve any signin errors
     */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String name = acct.getDisplayName();
            //googleFbName = acct.getDisplayName();
            //getProfileInformation();
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this, Home.class);
            startActivity(i);
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            Log.i("TAG", "Google sign in result false");
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setMessage("Loading");
            mProgressDialog.hide();
        }
    }

    public static void googlePlusLogout() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            Log.i("TAG", "Google sign out");
        }
    }


}
