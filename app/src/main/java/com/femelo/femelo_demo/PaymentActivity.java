package com.femelo.femelo_demo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.zarinpal.ewallets.purchase.OnCallbackRequestPaymentListener;
import com.zarinpal.ewallets.purchase.OnCallbackVerificationPaymentListener;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends BaseActivity implements PutData.OnDataAvailable{
    private static final String TAG = "PaymentActivity";
    EditText amountET;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        activateToolbar(true);

        Button btnPayment = findViewById(R.id.payment_btn);
        amountET = findViewById(R.id.amount_ET);
        status = findViewById(R.id.payment_status);

        ObjectWriter ow = new ObjectMapper().writer();
        String myObj = "";
        mOrder.setSet_paid(false);
        mOrder.setPayment_method("mBank");
        mOrder.setPayment_method_title("mobile bank zarin payment");
        mOrder.setStatus("pending");

        Order.Shipping_line shipping_lines = new Order.Shipping_line("shippingID","ارسال از طریق پست", "5000");
        List<Order.Shipping_line> mShipping_lines = new ArrayList<>();
        mShipping_lines.add(shipping_lines);
        mOrder.setShipping_lines(mShipping_lines);
        try {
            myObj = ow.writeValueAsString(mOrder);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onCreate: " + myObj);
        PutData putData = new PutData(this);
        putData.execute("https://femelo.com/wp-json/wc/v2/orders", myObj);

        Uri data = getIntent().getData();
        ZarinPal.getPurchase(this).verificationPayment(data, new OnCallbackVerificationPaymentListener() {
            @Override
            public void onCallbackResultVerificationPayment(boolean isPaymentSuccess, String refID, PaymentRequest paymentRequest) {

                String message;
                if (isPaymentSuccess) {
                    /* When Payment Request is Success :) */
                    message = "Your Payment is Success :) " + refID;
                    mOrder.setSet_paid(true);
                    mOrder.setStatus("processing");
                    int orderId = mOrder.getId();
                    updateOrder(orderId, "{\"set_paid\": true, \"status\":\"processing\"}");
                    long amount = paymentRequest.getAmount();
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    /* When Payment Request is Failure :) */
                    message = "Your Payment is Failure :(";
                    mOrder.setSet_paid(false);
                    mOrder.setStatus("failed");
                    int orderId = mOrder.getId();
                    updateOrder(orderId, "{\"set_paid\": false, \"status\":\"failed\"}");
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }

                status.setText(message);

            }
        });


        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = Integer.valueOf(amountET.getText().toString());
                requestPayment(amount);
            }
        });
    }

    private void updateOrder(int orderId, String data){
        Log.d(TAG, "updateOrder: starts");
        PutData putData = new PutData(this);
        putData.execute("https://femelo.com/wp-json/wc/v2/orders/" + orderId, data);
    }

    private void requestPayment(int amount) {

        ZarinPal purchase = ZarinPal.getPurchase(this);
        PaymentRequest payment  = ZarinPal.getPaymentRequest();

        payment.setMerchantID("1969f7f6-5df1-11e8-8783-005056a205be");
        payment.setAmount(amount);
        payment.setDescription("تست پرداخت");
        payment.setCallbackURL("femeloapp://app");     /* Your App Scheme */
        payment.setMobile("09364813829");            /* Optional Parameters */
        payment.setEmail("f.rahimi1992@gmail.com");     /* Optional Parameters */


        purchase.startPayment(payment, new OnCallbackRequestPaymentListener() {
            @Override
            public void onCallbackResultPaymentRequest(int status, String authority, Uri paymentGatewayUri, Intent intent) {


                if (status == 100) {
                    /*
                    When Status is 100 Open Zarinpal PG on Browser
                    */
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Your Payment Failure :(", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onDataAvailable(int orderId, UploadStatus uploadStatus) {
        Log.d(TAG, "onDataAvailable: starts");
        if (uploadStatus == UploadStatus.OK){
            mOrder.setId(orderId);
        }
    }
}
