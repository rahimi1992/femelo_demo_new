package com.femelo.femelo_demo;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends BaseActivity {
    private static final String TAG = "ProductDetailActivity";
    private int product_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        activateToolbar(true);

        Intent intent = getIntent();
        final Product product = (Product) intent.getSerializableExtra(PRODUCT_TRANSFER);
        product_position = intent.getIntExtra(PRODUCT_POSITION, -1);
        if (product != null){
            Resources res = getResources();
            TextView pTitle = findViewById(R.id.product_title);
            pTitle.setText(product.getName());
            ImageView pImage = findViewById(R.id.product_image);
            Picasso.get().load(product.getImage())
                    .error(R.drawable.place_holder)
                    .placeholder(R.drawable.place_holder)
                    .into(pImage);
            TextView pPrice = findViewById(R.id.product_price);
            pPrice.setText(res.getString(R.string.currency, product.getPrice()));
            //pPrice.setText( product.getPrice());
            //pPrice.setTextColor(getResources().getColor(R.color.colorAccent));
            pPrice.setTextColor(getResources().getColor(R.color.colorAccent));
            TextView pDescription = findViewById(R.id.product_description);
            String rawText = product.getDescription();
            pDescription.setText(Html.fromHtml(rawText));
            pDescription.setMovementMethod(LinkMovementMethod.getInstance());

        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemsNo = mOrderItemList == null?0:mOrderItemList.size();
                boolean exist = false;
                for (int i=0; i<itemsNo;i++){
                    if(mOrderItemList.get(i).getProduct_id() == Integer.valueOf(product.getId())){
                        exist = true;
                        mOrderItemList.get(i).plusQuantity();
                        break;
                    }
                }
                if (!exist){
                    Order.OrderItem orderItem = new Order.OrderItem( product.getName(), product_position, product.getId(),
                            1, product.getPrice(), product.getImage());
                    mOrderItemList.add(orderItem);
                }

                mOrder.setLine_items(mOrderItemList);
                Snackbar.make(view,getString(R.string.added_to_cart,product.getName()), 5000).setAction(getString(R.string.show_cart), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToCart(null);
                    }
                }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    public void toggleGrid(MenuItem item) {
    }
}
