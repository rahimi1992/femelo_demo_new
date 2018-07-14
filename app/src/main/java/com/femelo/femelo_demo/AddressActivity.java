package com.femelo.femelo_demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.json.JSONObject;
import org.json.JSONStringer;

public class AddressActivity extends BaseActivity implements PutData.OnDataAvailable{
    private static final String TAG = "AddressActivity";
    EditText[] mEditTexts = new EditText[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        mEditTexts[0]=findViewById(R.id.first_name_ET);
        mEditTexts[1]=findViewById(R.id.last_name_ET);
        mEditTexts[2]=findViewById(R.id.state_ET);
        mEditTexts[3]=findViewById(R.id.city_ET);
        mEditTexts[4]=findViewById(R.id.address_ET);
        mEditTexts[5]=findViewById(R.id.post_code_ET);
        mEditTexts[6]=findViewById(R.id.phone_ET);
        Customer.Billing billingAddress = mCustomer.getBilling();
        mEditTexts[0].setText(billingAddress.getFirst_name());
        mEditTexts[1].setText(billingAddress.getLast_name());
        mEditTexts[2].setText(billingAddress.getState());
        mEditTexts[3].setText(billingAddress.getCity());
        mEditTexts[4].setText(billingAddress.getAddress_1());
        mEditTexts[5].setText(billingAddress.getPostcode());
        mEditTexts[6].setText(billingAddress.getPhone());
        Log.d(TAG, "onCreate: ends");
    }

    public void addressOK(View view) {
        Log.d(TAG, "addressOK: clicked");
        ObjectWriter ow = new ObjectMapper().writer();
        //JSONObject jsonObject = new JSONObject(mCustomer);
        String myObj = "";
        String siteObj = "";
        mCustomer.getBilling().setFirst_name(mEditTexts[0].getText().toString());
        mCustomer.getBilling().setLast_name(mEditTexts[1].getText().toString());
        mCustomer.getBilling().setState(mEditTexts[2].getText().toString());
        mCustomer.getBilling().setCity(mEditTexts[3].getText().toString());
        mCustomer.getBilling().setAddress_1(mEditTexts[4].getText().toString());
        mCustomer.getBilling().setPostcode(mEditTexts[5].getText().toString());
        mCustomer.getBilling().setPhone(mEditTexts[6].getText().toString());

        try {
            myObj = ow.writeValueAsString(mCustomer);
            //siteObj = ow.writeValueAsString(mJsonUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        PutData putData = new PutData(this);
        putData.execute("https://femelo.com/wp-json/wc/v2/customers/"+mCustomer.getId() , myObj);
        mOrder.setBilling(mCustomer.getBilling());
        startActivity(new Intent(this, PaymentActivity.class));

        //Log.d(TAG, "addressOK: myObj ---> " + myObj);
        //Log.d(TAG, "addressOK: siteObj -> " + siteObj);

    }

    @Override
    public void onDataAvailable(int orderId, UploadStatus uploadStatus) {

    }
}
