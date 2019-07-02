package com.switso.itap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class GlobalFunction {
    Context mContext;
    int errorCount;

    // constructor
    public GlobalFunction(Context context){
        this.mContext = context;
    }

    public void ShowErrorDialog(String message){
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_error);
        dialog.setTitle("Custom Dialog");

        TextView txtMessage = dialog.findViewById(R.id.txtError);
        Button dialogClose = dialog.findViewById(R.id.BtnOkay);
        txtMessage.setText(message);

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void ShowSucessSnackbar(Activity activity, String message){
        Snackbar snackBar = Snackbar.make(activity.findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG);
        View snackBarView = snackBar.getView();
        snackBarView.setBackgroundColor(activity.getColor(R.color.colorAccent));
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(activity.getColor(R.color.colorWhite));
        textView.setTextSize(18);
        snackBar.show();
    }

    public void ShowErrorSnackbar(Activity activity, String message){
        Snackbar snackBar = Snackbar.make(activity.findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG);
        View snackBarView = snackBar.getView();
        snackBarView.setBackgroundColor(activity.getColor(R.color.colorError));
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(activity.getColor(R.color.colorWhite));
        textView.setTextSize(18);
        snackBar.show();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean checkConnection(){
        try {
            URL url = new URL("http://chronosphere.propleinc.com:8080/tk/api/FormsAPI/getShiftCodes/test");
            //URL url = new URL("http://192.168.43.38/Prople");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }


}
