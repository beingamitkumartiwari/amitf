package com.mtechno.freerechargeswipe.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.extras.Constants;
import com.mtechno.freerechargeswipe.extras.Frs_Constants;
import com.mtechno.freerechargeswipe.utils.CustomProgressDialog;
import com.mtechno.freerechargeswipe.utils.SharedPrefs;
import com.mtechno.freerechargeswipe.volley_works.AppController;
import com.mtechno.freerechargeswipe.volley_works.ConnectionCheck;
import com.mtechno.freerechargeswipe.volley_works.CustomJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DEVEN SINGH on 6/11/2015.
 */
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "MainActivity";
    private static final int PROFILE_PIC_SIZE = 100;
    private GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private SignInButton btnSignIn;
    private Button btnSignInNext, btnRevokeAccessExit;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private LinearLayout llProfileLayout;
    private ProgressBar progressBar;
    SharedPrefs sharedPrefs;
    private EditText mobNum;
    CustomProgressDialog customProgressDialog;
    String TAG_JSON = "login_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        initGoogleApiClient();
    }

    private void initViews() {
        customProgressDialog = new CustomProgressDialog(this, R.mipmap.loading);
        sharedPrefs = new SharedPrefs(RegistrationActivity.this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignInNext = (Button) findViewById(R.id.btn_sign_next);
        btnRevokeAccessExit = (Button) findViewById(R.id.btn_revoke_access_exit);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.userGmailName);
        txtEmail = (TextView) findViewById(R.id.userGmailEmail);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        mobNum = (EditText) findViewById(R.id.mobNum);
// Button click listeners
        btnSignIn.setOnClickListener(this);
        btnSignInNext.setOnClickListener(this);
        btnRevokeAccessExit.setOnClickListener(this);
    }

    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }


    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
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

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        progressBar.setVisibility(View.VISIBLE);
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        getProfileInformation();
        updateUI(true);
    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                int gender = currentPerson.getGender();
                String birthday = currentPerson.getBirthday();

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email + ", gender: " + gender + ", birthday: " + birthday
                        + ", Image: " + personPhotoUrl);
                sharedPrefs.setUserName(personName);
                sharedPrefs.setUserEmail(email);
                if (birthday != null)
                    sharedPrefs.setUserDob(birthday);
                else
                    sharedPrefs.setUserDob("");
                switch (gender) {
                    case 0:
                        sharedPrefs.setUserGender("Male");
                        break;
                    case 1:
                        sharedPrefs.setUserGender("Female");
                        break;
                    default:
                        sharedPrefs.setUserGender("Other");
                        break;
                }
                txtName.setText(personName);
                txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);
    }


    /**
     * Button on click listener
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                progressBar.setVisibility(View.VISIBLE);
                signInWithGplus();
                break;
            case R.id.btn_sign_next:
//                signOutFromGplus();
                registerUser();
                break;
            case R.id.btn_revoke_access_exit:
                revokeGplusAccess();
                break;
        }
    }

    /**
     * Sign-in into google
     */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

//    /**
//     * Sign-out from google
//     */
//    private void signOutFromGplus() {
//        if (mGoogleApiClient.isConnected()) {
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            mGoogleApiClient.disconnect();
//            mGoogleApiClient.connect();
//            updateUI(false);
//        }
//    }

    /**
     * Revoking access from google
     */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(false);
                            finish();
                        }
                    });
        }

    }

    /**
     * Background Async task to load user profile picture from url
     */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                convertImageInString(mIcon11);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void convertImageInString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        sharedPrefs.setUserAvatar(encodedImage);
    }

    /**
     * request for register user on server
     */

    private void registerUser() {
        if (TextUtils.isEmpty(mobNum.getText().toString().trim()) || mobNum.getText().toString().trim().length() < 10) {
            mobNum.requestFocus();
            mobNum.setError("Fill the details correctly");
        } else {
            if (new ConnectionCheck(this).isConnectionAvailable()) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceid", Constants.getImeiNum(getApplicationContext()));
                params.put("email", sharedPrefs.getUserEmail());
                params.put("name", sharedPrefs.getUserName());
                params.put("gender", sharedPrefs.getUserGender());
                params.put("dob", sharedPrefs.getUserDob());
                params.put("mobile", mobNum.getText().toString());
                customProgressDialog.show();
                AppController.getInstance().addToRequestQueue(requestCustomRegistration(Frs_Constants.LOGIN, params, mobNum.getText().toString()), TAG_JSON);
            } else {
                alertNetwork();
            }
        }
    }

    private CustomJsonObjectRequest requestCustomRegistration(String url, Map<String, String> params, final String phNum) {
        return new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.get("Successfull").toString().equals("0")) {
                        if (customProgressDialog != null)
                            customProgressDialog.dismiss();
                        sharedPrefs.setUserId(response.get("Successfull").toString());
                        sharedPrefs.setMobileNumber(phNum);
                        sharedPrefs.setUserKey(new Constants().getUserKey(getApplicationContext()));
                        startActivity(new Intent(getApplicationContext(), ProfileCompletionActivity.class));
                        finish();
                    } else {
                        if (customProgressDialog != null)
                            customProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistrationActivity.this, "Server Error! registration failed, please try again later.", Toast.LENGTH_SHORT).show();
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
            }
        });
    }


    private void alertNetwork() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.DevenDialogTheme);
        alertDialogBuilder.setTitle("Internet unavailable");
        alertDialogBuilder
                .setMessage("Check your Internet connection and try again.")
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Window window = alertDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(
                    android.graphics.Color.TRANSPARENT));
        }
        alertDialog.show();
    }

}

