package com.femelo.femelo_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CartActivity extends BaseActivity {
    private static final String TAG = "CartActivity";
    private Order.OrderItem removedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activateToolbar(true);
        showCartDetail();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
    }

    void showCartDetail(){
        Log.d(TAG, "showCartDetail: starts");
        LayoutInflater inflater = getLayoutInflater();
        if (mOrderItemList != null && mOrderItemList.size() != 0) {
            LinearLayout linearLayout = findViewById(R.id.my_linear_layout);

            int price = 0;
            int shipping = 7000;
            View view;
            for (int i = 0; i < mOrderItemList.size(); i++) {

                Order.OrderItem item = mOrderItemList.get(i);
                view = inflater.inflate(R.layout.cart_item, linearLayout, false);
                TextView title = view.findViewById(R.id.item_title);
                Button deleteButton = view.findViewById(R.id.delet_btn);
                deleteButton.setId(i);
                Button minusButton = view.findViewById(R.id.minus_btn);
                minusButton.setId(i);
                Button plusButton = view.findViewById(R.id.plus_btn);
                plusButton.setId(i);
                title.setText(item.getName());
                TextView qtyTV = view.findViewById(R.id.qty);
                qtyTV.setText(String.valueOf(item.getQuantity()));
                TextView priceTV = view.findViewById(R.id.item_price);
                priceTV.setText(item.getQuantity() + " x " + item.getTotal());
                ImageView image = view.findViewById(R.id.thumbnail);
                image.setId(item.getProductPos());
                Picasso.get().load(item.getImage())
                        .error(R.drawable.place_holder)
                        .placeholder(R.drawable.place_holder)
                        .into(image);
                if (i==0){
                    view.findViewById(R.id.cart_header).setVisibility(View.VISIBLE);
                }
                linearLayout.addView(view);
                price += Integer.valueOf(item.getTotal()) * item.getQuantity();
            }
            view = inflater.inflate(R.layout.cart_summary, linearLayout, false);
            TextView totalPriceTV = view.findViewById(R.id.s_total_price);
            TextView shippingTV = view.findViewById(R.id.s_shipping);
            TextView paymentTV = view.findViewById(R.id.s_payment);
            totalPriceTV.setText(String.valueOf(price));
            shippingTV.setText(String.valueOf(shipping));
            paymentTV.setText(getString(R.string.currency, String.valueOf(price + shipping)));

            linearLayout.addView(view);
            findViewById(R.id.empty_cart).setVisibility(View.GONE);
        }else {
            findViewById(R.id.empty_cart).setVisibility(View.VISIBLE);
        }

        mOrder.setLine_items(mOrderItemList);

        Log.d(TAG, "showCartDetail: ends");

    }

    public void deleteItem(View view) {
        Log.d(TAG, "deleteItem: " + view.getId());
        int position = view.getId();
        removedItem = mOrderItemList.remove(position);
        LinearLayout linearLayout = findViewById(R.id.my_linear_layout);
        linearLayout.removeAllViews();
        Snackbar.make(linearLayout, getString(R.string.removed_from_cart,removedItem.getName()), 8000).setAction("بازگرداندن", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOrderItemList.add(removedItem);
                LinearLayout linearLayout = findViewById(R.id.my_linear_layout);
                linearLayout.removeAllViews();
                showCartDetail();
            }
        }).show();
        showCartDetail();

    }

    public void minusItem(View view) {
        int position = view.getId();
        mOrderItemList.get(position).minusQuantity();
        LinearLayout linearLayout = findViewById(R.id.my_linear_layout);
        linearLayout.removeAllViews();
        showCartDetail();

    }

    public void plusItem(View view) {
        int position = view.getId();
        mOrderItemList.get(position).plusQuantity();
        LinearLayout linearLayout = findViewById(R.id.my_linear_layout);
        linearLayout.removeAllViews();
        showCartDetail();
    }

    public void goToDetail(View view) {
        Log.d(TAG, "goToDetail: " + mProducts.size());

        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(PRODUCT_POSITION, view.getId());
        intent.putExtra(PRODUCT_TRANSFER, mProducts.get(view.getId()));
        startActivity(intent);
    }

    public void goToLogin(View view) {
        mOrder.setStatus("pending");
        if(isLogedIn) {
            //mOrder.setCustomer_Id(userId);
            startActivity(new Intent(this, AddressActivity.class));


        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(NEXT_STEP, "ORDERING");
            startActivity(intent);
        }

    }
}
