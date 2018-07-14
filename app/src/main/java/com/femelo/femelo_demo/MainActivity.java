package com.femelo.femelo_demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements GetFemeloJsonData.OnDataAvailable,
        RecyclerItemClickListener.OnRecyclerClickListener, NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ConstraintLayout mConstraintLayout;
    private ConstraintLayout homeCL;
    private ConstraintLayout searchCL;
    private Context mContext;
    private String lastData;
    private String searchText = "";
    private EditText searchET;
    private String page;
    //private LinearLayout mLinearLayout;
    private boolean grid = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mDrawerLayout.removeView(mConstraintLayout);
                    mConstraintLayout = homeCL;
                    page = ALL_PRODUCT;
                    mDrawerLayout.addView(mConstraintLayout);

                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(mContext, CategoryActivity.class));
                    //mFemeloRecyclerViewAdapter.loadNewData(null);

                    return true;
                case R.id.navigation_notifications:

                    //mFemeloRecyclerViewAdapter.loadNewData(null);

                    return true;

                case R.id.navigation_logout:
                    return true;
                case R.id.navigation_search:
                    mDrawerLayout.removeView(mConstraintLayout);
                    mConstraintLayout = searchCL;
                    mDrawerLayout.addView(mConstraintLayout);
                    page = SEARCH;
                    return true;

            }
            return false;
        }
    };


    private FemeloRecyclerViewAdapter mFemeloRecyclerViewAdapter;
    private FemeloRecyclerViewAdapter mGridRecyclerViewAdapter;
    private FemeloRecyclerViewAdapter searchRecyclerViewAdapter;
    private GridLayoutManager mGridManager;
    private RecyclerView recyclerView;
//    private final String LOADED_DATA = "loaded_data";
//    private List<Product> loadedData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        pageN = 1;
        page = ALL_PRODUCT;



        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.activity_main);
        activateToolbar(false);

        mGridManager = new GridLayoutManager(MainActivity.this,1);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mContext = this;
        homeCL = (ConstraintLayout) View.inflate(mContext,R.layout.content_main, null);
        searchCL = (ConstraintLayout) View.inflate(mContext,R.layout.search_layout, null);
        CardView cardView = (CardView) View.inflate(mContext,R.layout.loading_grid, null);
        //mLinearLayout = cardView.findViewById(R.id.item_linear_layout);

        searchET = searchCL.findViewById(R.id.searchET);
        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchBtnListener(null);

                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });


        mConstraintLayout = homeCL;
        Log.d(TAG, "onCreate: " + (mDrawerLayout==null));

        mDrawerLayout.addView(mConstraintLayout);


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ///////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ///////////
        recyclerView = mConstraintLayout.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        //recyclerView
        recyclerView.setLayoutManager(mGridManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        if (mFemeloRecyclerViewAdapter == null) {
            mFemeloRecyclerViewAdapter = new FemeloRecyclerViewAdapter(new ArrayList<Product>(), this,false);
            mFemeloRecyclerViewAdapter.setOnBottomReachedListener(new FemeloRecyclerViewAdapter.OnBottomReachedListener() {
                @Override
                public void onBottomReached(int position, boolean loadingDone) {
                    Log.d(TAG, "onBottomReached: starts");
                    if (!loadingDone) {
                        lastData = ALL_PRODUCT;
                        onNeedNewData(ALL_PRODUCT);
                    } else {
                        isLoaded = true;
                    }
                }
            });

            recyclerView.setAdapter(mFemeloRecyclerViewAdapter);
        }
        if (mGridRecyclerViewAdapter == null) {
            mGridRecyclerViewAdapter = new FemeloRecyclerViewAdapter(new ArrayList<Product>(), this,true);
            mGridRecyclerViewAdapter.setOnBottomReachedListener(new FemeloRecyclerViewAdapter.OnBottomReachedListener() {
                @Override
                public void onBottomReached(int position, boolean loadingDone) {
                    Log.d(TAG, "onBottomReached: starts");
                    if (!loadingDone) {
                        lastData = ALL_PRODUCT;
                        onNeedNewData(ALL_PRODUCT);
                    } else {
                        isLoaded = true;
                    }
                }
            });

            //recyclerView.setAdapter(mGridRecyclerViewAdapter);
        }


//        GetRawData getRawData = new GetRawData(this);
//        getRawData.execute("curl https://femelo.com/wp-json/wc/v2/products/1697 -u ck_a69bd88d175c4c64e7d5062e3ce82951b83797a0:cs_b89c50f09b47688b1e9a46a4113b0bcf469fefd8");

        Log.d(TAG, "onCreate: ends");
    }


    @Override
    protected void onResume() {

        Log.d(TAG, "onResume starts");
        super.onResume();
        String cUrl = "curl https://femelo.com/wp-json/wc/v2/products?per_page=3 -u ck_a69bd88d175c4c64e7d5062e3ce82951b83797a0:cs_b89c50f09b47688b1e9a46a4113b0bcf469fefd8";
        //if (!isLoaded) {
        lastData = ALL_PRODUCT;
            onNeedNewData(ALL_PRODUCT);
        //}
        Log.d(TAG, "onResume ends");

    }
    private void onNeedNewData(String s){
        Log.d(TAG, "onNeedNewData: starts " +s);
        GetFemeloJsonData getFemeloJsonData = null;
        if (s.equals(SEARCH)) {
            getFemeloJsonData = new GetFemeloJsonData(GET_PRODUCT, "&search="+searchText+"&page=" + pageNs , this);
        } else if (s.equals(ALL_PRODUCT)){
            getFemeloJsonData = new GetFemeloJsonData(GET_PRODUCT, "&page=" + pageN, this);
        }
        getFemeloJsonData.execute();
        Log.d(TAG, "onNeedNewData: ends");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        Log.d(TAG, "onOptionsItemSelected() returned: returned");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(List<Product> data, DownloadStatus status){
        Log.d(TAG, "onDataAvailable: starts");
        if (status == DownloadStatus.OK){
            if (lastData.equals(ALL_PRODUCT)) {
                pageN++;
                Log.d(TAG, "onDataAvailable: mProducts >> " + (mProducts==null));
                Log.d(TAG, "onDataAvailable: data >> " + (data==null));
                mProducts.addAll(data);
                mFemeloRecyclerViewAdapter.loadNewData(data);
                mGridRecyclerViewAdapter.loadNewData(data);
            }else if (lastData.equals(SEARCH)){
                pageNs++;
                mSearchedProducts.addAll(data);
                searchRecyclerViewAdapter.loadNewData(data);
            }
        }else{
            Log.e(TAG, "onDataAvailable failed with status " + status);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

    @Override
    public void onDataAvailable(Customer customer, JSONObject jo, DownloadStatus status) {

    }

    @Override
    public void onDataAvailable(List<Category> data, DownloadStatus status, String s) {

    }


    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        //Toast.makeText(MainActivity.this, "normal tap at pos: "+position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(PRODUCT_POSITION, position);
        if (page.equals(ALL_PRODUCT))
            intent.putExtra(PRODUCT_TRANSFER, mFemeloRecyclerViewAdapter.getProduct(position));
        else if (page.equals(SEARCH))
            intent.putExtra(PRODUCT_TRANSFER, searchRecyclerViewAdapter.getProduct(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
        Toast.makeText(MainActivity.this, "long tap at pos: "+position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_my_orders) {

        } else if (id == R.id.nav_categories) {
            startActivity(new Intent(this, Test2Activity.class));

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void searchBtnListener(View view) {
        pageNs = 1;

         RecyclerView recyclerView = searchCL.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));
        searchText = searchET.getText().toString();
        //mSearchedProducts.removeAll();
            searchRecyclerViewAdapter = new FemeloRecyclerViewAdapter(new ArrayList<Product>(), this,false);
            searchRecyclerViewAdapter.setOnBottomReachedListener(new FemeloRecyclerViewAdapter.OnBottomReachedListener() {
                @Override
                public void onBottomReached(int position, boolean loadingDone) {
                    Log.d(TAG, "onBottomReached: starts");
                    if (!loadingDone) {
                        lastData = SEARCH;
                        onNeedNewData(SEARCH);
                    } else {
                        isLoaded = true;
                    }
                }
            });
            lastData = SEARCH;
            onNeedNewData(SEARCH);

            recyclerView.setAdapter(searchRecyclerViewAdapter);


    }

    public void clearSearch(View view) {
        searchET.setText("");
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null){
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
//        }
    }

    public void toggleGrid(MenuItem item) {
        if (grid){
            grid = false;
            item.setIcon(R.drawable.ic_grid_view);
            recyclerView.setAdapter(mFemeloRecyclerViewAdapter);
            mGridManager.setSpanCount(1);
        }else{
            grid = true;
            item.setIcon(R.drawable.ic_list_view);
            recyclerView.setAdapter(mGridRecyclerViewAdapter);
            mGridManager.setSpanCount(2);

        }
    }


//    public void saveData(String data){
//        //this.loadedData = data;
//    }
}
