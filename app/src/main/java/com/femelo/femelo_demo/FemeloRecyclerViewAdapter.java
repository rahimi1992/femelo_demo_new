package com.femelo.femelo_demo;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FemeloRecyclerViewAdapter extends RecyclerView.Adapter<FemeloRecyclerViewAdapter.FemeloImageViewHolder> {
    private static final String TAG = "FemeloRecyclerViewAdapt";
    private List<Product> mProducts;
    private Context mContext;
    private boolean loadingDone = false;
    private OnBottomReachedListener onBottomReachedListener;
    private Resources res;
    private boolean grid;



    public interface OnBottomReachedListener {
        void onBottomReached(int position, boolean loadingDone);
    }

    public FemeloRecyclerViewAdapter(List<Product> products, Context context, boolean mGrid) {
        mProducts = products;
        mContext = context;
        grid = mGrid;
        res = context.getResources();
    }
    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public FemeloImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d(TAG, "onCreateViewHolder: new view requested");
        FemeloImageViewHolder mHolder;

        if (grid) {
            mHolder = new FemeloImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_grid, parent, false));
        }
        else {
            mHolder = new FemeloImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.loading, parent, false));
        }
        return mHolder;
    }

    @Override
    public void onBindViewHolder(FemeloImageViewHolder holder, int position) {

        Product productItem = mProducts.get(position);
        //Log.d(TAG, "onBindViewHolder: " + productItem.getName() + " --> " + position);
        Picasso.get().load(productItem.getImage())
                .error(R.drawable.place_holder)
                .placeholder(R.drawable.place_holder)
                .into(holder.thumbnail);

        holder.title.setText(productItem.getName());

        holder.price.setText(res.getString(R.string.currency, productItem.getPrice()));
        holder.progressBar.setVisibility(View.GONE);

        if (position == mProducts.size() - 1){
            onBottomReachedListener.onBottomReached(position, loadingDone);
            if(!loadingDone) {
                holder.progressBar.setVisibility(View.VISIBLE);
            }
        }

//        if(grid){
//            holder.mLinearLayout.getLayoutParams().height = 200;
//        }else{
//            holder.mLinearLayout.getLayoutParams().height = 100;
//        }


    }

    @Override
    public int getItemCount() {
        return ((mProducts != null) && (mProducts.size() != 0) ? mProducts.size() : 0);

    }


    void loadNewData(List<Product> newProduct){
        //mProducts = (newProduct);
        if (newProduct !=null && newProduct.size()!=0){
            mProducts.addAll(newProduct);
        }else {
            //loadingDone = true;
        }

        notifyDataSetChanged();
    }

    public void clear() {
        mProducts.clear();
        notifyDataSetChanged();
    }

    public Product getProduct(int position){
        return ((mProducts != null) && (mProducts.size() != 0) ? mProducts.get(position) : null);
    }
    static class FemeloImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FemeloImaleViewHolder";
        ImageView thumbnail = null;
        TextView title = null;
        TextView price = null;
        ProgressBar progressBar = null;
        LinearLayout mLinearLayout;

        FemeloImageViewHolder(View itemView) {
            super(itemView);
            //Log.d(TAG, "FemeloImaleViewHolder: starts");
                this.thumbnail = itemView.findViewById(R.id.thumbnail);
                this.title = itemView.findViewById(R.id.title);
                this.price = itemView.findViewById(R.id.price);
                this.progressBar = itemView.findViewById(R.id.progressBar);
                this.mLinearLayout = itemView.findViewById(R.id.item_linear_layout);

        }
    }
}
