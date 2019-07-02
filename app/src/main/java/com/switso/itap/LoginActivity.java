package com.switso.itap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.switso.itap.Form.FormList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {

    View parentLayout;
    RelativeLayout relLogin, relSplashScreen, RelMain;
    String dataAddress, user_fname,user_email,user_image;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton btnGoogle;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    ProgressDialog googleProgressDialog;
    GlobalFunction globalFunction;
    Login request;

    ProgressDialog pd;

    @Override
    public void onStart() {
        super.onStart();
        globalFunction = new GlobalFunction(LoginActivity.this);
        boolean inConnected = globalFunction.isNetworkAvailable();
        googleProgressDialog = new ProgressDialog(LoginActivity.this);

        if (!inConnected){
            globalFunction.ShowErrorSnackbar(LoginActivity.this, getResources().getString(R.string.nonet));
        }else {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                updateUI(currentUser);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pd = new ProgressDialog(LoginActivity.this);

        globalFunction = new GlobalFunction(LoginActivity.this);
        parentLayout = findViewById(android.R.id.content);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        btnGoogle = (SignInButton) findViewById(R.id.sign_in_button);
        TextView textView = (TextView) btnGoogle.getChildAt(0);
        textView.setText("Sign in Prople Google Account");

        RelMain = (RelativeLayout) findViewById(R.id.RelMain);



        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean inConnected = globalFunction.isNetworkAvailable();
                if (!inConnected){
                    globalFunction.ShowErrorSnackbar(LoginActivity.this, getResources().getString(R.string.nonet));
                }else {

                    signIn();

                }

            }
        });




    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("getid", "firebaseAuthWithGoogle:" + acct.getId());
        googleProgressDialog.setMessage(getString(R.string.pdGoogleSignin));
        googleProgressDialog.setCancelable(false);
        googleProgressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("successful", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            googleProgressDialog.dismiss();
                            updateUI(user);

                        } else {
                            googleProgressDialog.dismiss();
                            updateUI(null);
                            signOut();
                            globalFunction.ShowErrorDialog(task.getException().getMessage());
                        }

                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            user_fname =  user.getDisplayName();
            user_email = user.getEmail();
            user_image = user.getPhotoUrl().toString();

            HashMap<String, String> params = new HashMap<>();
            params.put("google_signin" , "1");
            params.put("email_address", user_email);
            params.put("subdomain" , Api.SUBDOMAIN);

            request = new Login(Api.URL_LOGIN, params, CODE_POST_REQUEST);
            request.execute();


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(), getString(R.string.signinfail), Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        }else{
            onActivityResult(requestCode, resultCode, data);
            Log.d("test","test");
        }

    }

    private class Login extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        Login(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage(getString(R.string.pdLogin));
            pd.setCancelable(false);
            pd.show();



        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int count = globalFunction.getErrorCount();
            if(s != null){
                try {
                    JSONObject json_obj= new JSONObject(s);
                    if (json_obj.getBoolean("flag") == true){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("employee_id", json_obj.getString("employee_id"));
                        intent.putExtra("user_id", json_obj.getString("user_id"));
                        intent.putExtra("employee_fname", json_obj.getString("firstname") + " " + json_obj.getString("lastname"));
                        intent.putExtra("employee_email", json_obj.getString("email"));
                        intent.putExtra("employee_image",user_image);
                        intent.putExtra("employee_department", json_obj.getString("department"));
                        intent.putExtra("employee_role", json_obj.getString("position"));
                        startActivity(intent);
                    }else {
                        globalFunction.ShowErrorSnackbar(LoginActivity.this, json_obj.getString("message"));
                        signOut();
                    }
                    globalFunction.setErrorCount(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                    globalFunction.ShowErrorDialog(getResources().getString(R.string.errorOccuredRetryAgain));
                    signOut();
                }
            }else {
                globalFunction.ShowErrorDialog(getResources().getString(R.string.errorOccuredCannotConnect));
                signOut();
            }

            pd.dismiss();
        }

        @Override
        protected String doInBackground(Void... voids) {
            GetPostRequest requestHandler = new GetPostRequest();
            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    @Override
    public void onDestroy() {
        pd.dismiss();
        super.onDestroy();
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

    }




}
