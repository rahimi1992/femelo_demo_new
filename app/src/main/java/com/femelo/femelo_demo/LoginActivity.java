package com.femelo.femelo_demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

public class LoginActivity extends BaseActivity implements GetFemeloJsonData.OnDataAvailable {
    private static final String TAG = "LoginActivity";

    EditText usernameET;
    EditText passwordET;
    ProgressBar loginProgress;
    TextView statusTV;
    String nextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        if(isLogedIn){
            startActivity(new Intent(this, AddressActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        nextStep = getIntent().getStringExtra(NEXT_STEP);

        usernameET = findViewById(R.id.et_username);
        passwordET = findViewById(R.id.et_password);
        loginProgress = findViewById(R.id.login_progressbar);
        statusTV = findViewById(R.id.statusTV);

    }

    public void onLogin(View view) {

        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        statusTV.setText("");
        String type = "Login";
        View keyView = this.getCurrentFocus();
        if (keyView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        BackgroundWorker backgroundWorker = new BackgroundWorker(this, new BackgroundWorker.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.d(TAG, output);
                loginProgress.setVisibility(View.INVISIBLE);
                statusTV.setText("ورود با موفقیت انجام شد");
                statusTV.setVisibility(View.VISIBLE);

                String status = output.substring(0, 10);
                if (status.equals("SUCCESSFUL")) {
                    Log.d(TAG, "processFinish: " + status);
                    isLogedIn = true;
                    userId = Integer.valueOf(output.substring(10));
                    mOrder.setCustomer_Id(userId);
                    loadCustomerData(output.substring(10));

                }

            }
        });
        if (username.equals("")){
            Snackbar.make(view, "نام کاربری را وارد کنید", 3000).show();
            usernameET.requestFocus();
        } else if (password.equals("")){
            Snackbar.make(view, "رمز عبور را وارد کنید", 3000).show();
            passwordET.requestFocus();

        }else {
            loginProgress.setVisibility(View.VISIBLE);
            backgroundWorker.execute(type, username, password);

        }

    }

    private void loadCustomerData(String s) {
        Log.d(TAG, "loadCustomerData: " + s);
        GetFemeloJsonData getFemeloJsonData = new GetFemeloJsonData(GET_USER, s, this);
        getFemeloJsonData.execute();
    }

    @Override
    public void onDataAvailable(List<Product> data, DownloadStatus status) {

    }

    @Override
    public void onDataAvailable(Customer customer, JSONObject jsonUser, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        mCustomer = customer;
        mJsonUser = jsonUser;
        startActivity(new Intent(this, AddressActivity.class));
        finish();

    }

    @Override
    public void onDataAvailable(List<Category> data, DownloadStatus status, String s) {

    }
}
