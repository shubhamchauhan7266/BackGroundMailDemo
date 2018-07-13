package com.ownwork.backgroundmaildemo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootView = findViewById(R.id.root_view);

//        for(int i=0;i<100;i++){
//            new SendMail().execute();
//        }

        findViewById(R.id.bt_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendMail().execute();
            }
        });
    }

    /*
     * This class is used for sending mail in background by AsyncTask.
     */
    @SuppressLint("StaticFieldLeak")
    private class SendMail extends AsyncTask<String, Void, Integer> {
        ProgressDialog pd = null;
        String error = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle(AppConstant.SENDING_MALE);
            pd.setMessage(AppConstant.PLEASE_WAIT);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            // TODO Auto-generated method stub

            MailSender sender = new MailSender(AppConstant.ADMIN_EMAIL_ID, AppConstant.ADMIN_PASSWORD);

            sender.setTo(new String[]{AppConstant.USER_EMAIL_ID});
            sender.setFrom(AppConstant.ADMIN_EMAIL_ID);
            sender.setSubject(AppConstant.SUBJECT_DETAILS);
            sender.setBody(getData());
            try {
                if (sender.send()) {
                    System.out.println(AppConstant.MESSAGE_SENT);
                    return 1;
                } else {
                    return 2;
                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e(AppConstant.SEND_MAIL, e.getMessage(), e);
            }

            return 3;
        }

        protected void onPostExecute(Integer result) {
            pd.dismiss();
            if (result == 1) {
                Snackbar.make(mRootView, "Email Sent", Snackbar.LENGTH_LONG).show();
            } else if (result == 2) {
                Snackbar.make(mRootView, AppConstant.EMALE_NOT_SENT, Snackbar.LENGTH_LONG).show();
            } else if (result == 3) {
                Snackbar.make(mRootView, AppConstant.SENDING_MAlE_ERROR, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /*
     * Method is used for getting all details which is fiil up by user.
     * All this details will send to the body of the mail
     */
    public String getData() {
        return "Hello  /n/n/" +
                "Party!!!!";
    }

}
