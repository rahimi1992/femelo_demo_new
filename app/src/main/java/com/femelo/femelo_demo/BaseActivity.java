package com.femelo.femelo_demo;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    static final String FEMELO_QUERY = "FEMELO_QUERY";
    static final String PRODUCT_TRANSFER = "PRODUCT_TRANSFER";
    static final String USER_TRANSFER = "USER_TRANSFER";
    static final String PRODUCT_POSITION = "PRODUCT_POSITION";
    static final String NEXT_STEP = "NEXT_STEP";
    static final String GET_PRODUCT = "GET_P";
    static final String GET_USER = "GET_U";
    static final String GET_CATEGORY = "GET_C";
    static final String SEARCH = "SEARCH";
    static final String ALL_PRODUCT = "ALL_PRODUCT";
    static final String CATEGORY = "CATEGORY";
    static public Customer mCustomer;
    static public JSONObject mJsonUser;
    static public boolean isLogedIn = false;
    static public int userId = -1;
    static public List<Product> mProducts = new ArrayList<>();
    static public List<Product> mSearchedProducts = new ArrayList<>();
    static public List<Category> mCategories=null;
    static public List<Order.OrderItem> mOrderItemList = new ArrayList<>();
    static public Order mOrder = new Order();
    static public boolean isLoaded = false;
    public static int pageN =1;
    public static int pageNs =1;

    void activateToolbar(boolean enableHome) {
        Log.d(TAG, "activareToolbar: starts");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
            //actionBar.setDisplayShowHomeEnabled(enableHome);
        }
    }
    public void goToCart(MenuItem item) {
        startActivity(new Intent(this, CartActivity.class));
        //finish();
    }
}
