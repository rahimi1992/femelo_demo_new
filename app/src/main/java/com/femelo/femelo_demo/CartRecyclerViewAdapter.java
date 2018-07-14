package com.femelo.femelo_demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.CartItemHolder> {
    private static final String TAG = "CartRecyclerViewAdapter";
    private List<Order.OrderItem> mProducts;
    private Context mContext;
    private boolean loadingDone = true;
    private FemeloRecyclerViewAdapter.OnBottomReachedListener onBottomReachedListener;

    public interface OnBottomReachedListener {
        void onBottomReached(int position, boolean loadingDone);
    }

    public CartRecyclerViewAdapter(List<Order.OrderItem> products, Context context) {
        mProducts = products;
        mContext = context;
    }
    public void setOnBottomReachedListener(FemeloRecyclerViewAdapter.OnBottomReachedListener onBottomReachedListener){
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public CartRecyclerViewAdapter.CartItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");

        return new CartRecyclerViewAdapter.CartItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CartItemHolder holder, int position) {
        Order.OrderItem productItem = mProducts.get(position);
        Log.d(TAG, "onBindViewHolder: " + productItem.getName() + " --> " + position);
        Picasso.get().load(productItem.getImage())
                .error(R.drawable.place_holder)
                .placeholder(R.drawable.place_holder)
                .into(holder.thumbnail);
        holder.title.setText(productItem.getName());
        holder.price.setText(String.valueOf(productItem.getQuantity()));
        if (position == mProducts.size() - 1){
            //onBottomReachedListener.onBottomReached(position, loadingDone);
            if(!loadingDone) {
            }
        }


    }


    @Override
    public int getItemCount() {
        return ((mProducts != null) && (mProducts.size() != 0) ? mProducts.size() : 0);

    }


    void loadNewData(List<Order.OrderItem> newProduct){
        //mProducts = (newProduct);
        if (newProduct.size()!=0){
            mProducts.addAll(newProduct);
        }else {
            loadingDone = true;
        }

        notifyDataSetChanged();
    }

    public Order.OrderItem getProduct(int position){
        return ((mProducts != null) && (mProducts.size() != 0) ? mProducts.get(position) : null);
    }
    static class CartItemHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "CartItemHolder";
        ImageView thumbnail = null;
        TextView title = null;
        TextView price = null;
        ProgressBar progressBar = null;

        CartItemHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "CartItemHolder: starts");
            this.thumbnail = itemView.findViewById(R.id.thumbnail);
            this.title = itemView.findViewById(R.id.title);
            this.price = itemView.findViewById(R.id.price);

        }
    }
}
