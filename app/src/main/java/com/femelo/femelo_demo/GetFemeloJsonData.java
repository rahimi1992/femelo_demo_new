package com.femelo.femelo_demo;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFemeloJsonData extends AsyncTask<String, Void, List<Product>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFemeloJsonData";
    private List<Product> mProducts = null;
    private List<Category> mCategories = null;
    private String mDataType;
    private String mBaseUrl;
    private Customer mCustomer;
    private JSONObject jsonUser;
    private Customer.Billing mBilling;
    private final OnDataAvailable mCallback;
    private boolean runningOnSameThread = false;

    interface OnDataAvailable {
        void onDataAvailable(List<Product> data, DownloadStatus status);

        void onDataAvailable(Customer customer, JSONObject jsonObject, DownloadStatus status);

        void onDataAvailable(List<Category> data, DownloadStatus status, String s);

    }

    public GetFemeloJsonData(String dataType, String baseUrl, OnDataAvailable callback) {
        Log.d(TAG, "GetFemeloJsonData called");
        mDataType = dataType;
        mBaseUrl = baseUrl;
        mCallback = callback;
    }

    void executeOnSameThread() {
        Log.d(TAG, "executeOnSameThread starts");
        runningOnSameThread = true;
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(mBaseUrl);
        Log.d(TAG, "executeOnSameThread ends");
    }


    @Override
    protected void onPostExecute(List<Product> products) {
        Log.d(TAG, "onPostExecute starts");
        if (mCallback != null) {
            if (mDataType.equals(BaseActivity.GET_PRODUCT)) {
                mCallback.onDataAvailable(mProducts, DownloadStatus.OK);
            } else if (mDataType.equals(BaseActivity.GET_USER)){
                mCallback.onDataAvailable(mCustomer, jsonUser, DownloadStatus.OK);
            } else if (mDataType.equals(BaseActivity.GET_CATEGORY)){
                mCallback.onDataAvailable(mCategories, DownloadStatus.OK, "");
            }
        }
        Log.d(TAG, "onPostExecute ends");
        super.onPostExecute(products);
    }

    @Override
    protected List<Product> doInBackground(String... params) {
        Log.d(TAG, "doInBackground stats");

        //String baseUrl = params[0];
        GetRawData getRawData = new GetRawData(this);
        if (mDataType.equals(BaseActivity.GET_PRODUCT)) {
            getRawData.runInSameThread("https://femelo.com/wp-json/wc/v2/products?per_page=10" + mBaseUrl);
        } else if (mDataType.equals(BaseActivity.GET_USER)) {
            getRawData.runInSameThread("https://femelo.com/wp-json/wc/v2/customers/" + mBaseUrl);
        } else if (mDataType.equals(BaseActivity.GET_CATEGORY)){
            getRawData.runInSameThread("https://femelo.com/wp-json/wc/v2/products/categories?" + mBaseUrl);
        }
        Log.d(TAG, "doInBackground ends");
        return mProducts;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete starts. Status: " + status + data);

        if (status == DownloadStatus.OK) {
            if (mDataType.equals(BaseActivity.GET_PRODUCT)) {
                mProducts = new ArrayList<>();
                //String test = "{\"id\":1697,\"name\":\"\\u0627\\u0633\\u0645 \\u0645\\u062d\\u0635\\u0648\\u0644\",\"slug\":\"url-e-mahsool\",\"permalink\":\"https:\\/\\/femelo.com\\/shop\\/beauty\\/url-e-mahsool\\/\",\"date_created\":\"2018-06-19T23:22:14\",\"date_created_gmt\":\"2018-06-19T18:52:14\",\"date_modified\":\"2018-06-19T23:22:14\",\"date_modified_gmt\":\"2018-06-19T18:52:14\",\"type\":\"simple\",\"status\":\"publish\",\"featured\":false,\"catalog_visibility\":\"visible\",\"description\":\"<h3>Product long description heading<\\/h3>\\n<p>Product long description line 1<\\/p>\\n<p>Product long description line 2<\\/p>\\n<p>\\u062a\\u0648\\u0636\\u06cc\\u062d\\u0627\\u062a \\u0637\\u0648\\u0644\\u0627\\u0646\\u06cc \\u0645\\u062d\\u0635\\u0648\\u0644 \\u062e\\u0637 3<\\/p>\\n<p>\\u062a\\u0648\\u0636\\u06cc\\u062d\\u0627\\u062a \\u0637\\u0648\\u0644\\u0627\\u0646\\u06cc \\u0645\\u062d\\u0635\\u0648\\u0644 \\u062e\\u0637 4<\\/p>\\n\",\"short_description\":\"<p>\\u062e\\u0644\\u0627\\u0635\\u0647 \\u062a\\u0648\\u0636\\u06cc\\u062d \\u0645\\u062d\\u0635\\u0648\\u0644<\\/p>\\n\",\"sku\":\"\",\"price\":\"25000\",\"regular_price\":\"30000\",\"sale_price\":\"25000\",\"date_on_sale_from\":null,\"date_on_sale_from_gmt\":null,\"date_on_sale_to\":null,\"date_on_sale_to_gmt\":null,\"price_html\":\"<del><span class=\\\"woocommerce-Price-amount amount\\\">30,000&nbsp;<span class=\\\"woocommerce-Price-currencySymbol\\\">\\u062a\\u0648\\u0645\\u0627\\u0646<\\/span><\\/span><\\/del> <ins><span class=\\\"woocommerce-Price-amount amount\\\">25,000&nbsp;<span class=\\\"woocommerce-Price-currencySymbol\\\">\\u062a\\u0648\\u0645\\u0627\\u0646<\\/span><\\/span><\\/ins>\",\"on_sale\":true,\"purchasable\":true,\"total_sales\":0,\"virtual\":false,\"downloadable\":false,\"downloads\":[],\"download_limit\":-1,\"download_expiry\":-1,\"external_url\":\"\",\"button_text\":\"\",\"tax_status\":\"taxable\",\"tax_class\":\"\",\"manage_stock\":false,\"stock_quantity\":null,\"in_stock\":true,\"backorders\":\"no\",\"backorders_allowed\":false,\"backordered\":false,\"sold_individually\":false,\"weight\":\"\",\"dimensions\":{\"length\":\"\",\"width\":\"\",\"height\":\"\"},\"shipping_required\":true,\"shipping_taxable\":true,\"shipping_class\":\"\",\"shipping_class_id\":0,\"reviews_allowed\":true,\"average_rating\":\"0.00\",\"rating_count\":0,\"related_ids\":[1683,1669,1651,1645,1638],\"upsell_ids\":[],\"cross_sell_ids\":[],\"parent_id\":0,\"purchase_note\":\"\",\"categories\":[{\"id\":15,\"name\":\"\\u0622\\u0631\\u0627\\u06cc\\u0634\\u06cc \\u0648 \\u0628\\u0647\\u062f\\u0627\\u0634\\u062a\\u06cc\",\"slug\":\"beauty\"},{\"id\":134,\"name\":\"\\u0622\\u0631\\u0627\\u06cc\\u0634 \\u0644\\u0628\",\"slug\":\"lips\"},{\"id\":124,\"name\":\"\\u0644\\u0648\\u0627\\u0632\\u0645 \\u0622\\u0631\\u0627\\u06cc\\u0634\\u06cc\",\"slug\":\"beauty-makeup\"}],\"tags\":[{\"id\":1015,\"name\":\"\\u0628\\u0631\\u0686\\u0633\\u0628\",\"slug\":\"برچسب\"}],\"images\":[{\"id\":1285,\"date_created\":\"2018-04-30T06:56:49\",\"date_created_gmt\":\"2018-04-29T17:26:49\",\"date_modified\":\"2018-04-30T06:57:07\",\"date_modified_gmt\":\"2018-04-29T17:27:07\",\"src\":\"https:\\/\\/femelo.com\\/wp-content\\/uploads\\/2018\\/04\\/marquise-diamond-promise-ring-band-in-14K-rose-gold-FD8372-NL-RG.jpg\",\"name\":\"marquise-diamond-promise-ring-band-in-14K-rose-gold-FD8372-NL-RG\",\"alt\":\"\\u0627\\u0646\\u06af\\u0634\\u062a\\u0631 \\u0632\\u0646\\u0627\\u0646\\u0647\",\"position\":0}],\"attributes\":[{\"id\":1,\"name\":\"\\u0631\\u0646\\u06af\",\"position\":0,\"visible\":true,\"variation\":false,\"options\":[\"\\u0632\\u0631\\u062f\"]}],\"default_attributes\":[],\"variations\":[],\"grouped_products\":[],\"menu_order\":0,\"meta_data\":[{\"id\":12753,\"key\":\"_vc_post_settings\",\"value\":{\"vc_grid_id\":[]}},{\"id\":12790,\"key\":\"slide_template\",\"value\":\"default\"},{\"id\":12791,\"key\":\"_yoast_wpseo_primary_pwb-brand\",\"value\":\"956\"},{\"id\":12792,\"key\":\"_yoast_wpseo_primary_product_cat\",\"value\":\"15\"},{\"id\":12793,\"key\":\"_wpb_vc_js_status\",\"value\":\"false\"},{\"id\":12794,\"key\":\"_yoast_wpseo_focuskw_text_input\",\"value\":\"\\u0627\\u0633\\u0645 \\u0645\\u062d\\u0635\\u0648\\u0644\"},{\"id\":12795,\"key\":\"_yoast_wpseo_focuskw\",\"value\":\"\\u0627\\u0633\\u0645 \\u0645\\u062d\\u0635\\u0648\\u0644\"},{\"id\":12796,\"key\":\"_yoast_wpseo_title\",\"value\":\"\\u0627\\u0633\\u0645 \\u0645\\u062d\\u0635\\u0648\\u0644 \\u062f\\u0631 \\u0639\\u0646\\u0648\\u0627\\u0646 \\u0645\\u062a\\u0627\\u06cc \\u0645\\u062d\\u0635\\u0648\\u0644 \\u062f\\u0631 \\u06af\\u0648\\u06af\\u0644\"},{\"id\":12797,\"key\":\"_yoast_wpseo_metad";
                try {
                    //    System.out.println("test length :" + test.length());
                    System.out.println("data length :" + data.length());
                    //JSONObject jsonData = new JSONObject(data);
                    JSONArray itemsArray = new JSONArray(data);


                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject jsonProduct = itemsArray.getJSONObject(i);
                        //JSONObject jsonProduct = jsonData;
                        int id = Integer.valueOf(jsonProduct.getString("id"));
                        String name = jsonProduct.getString("name");
                        String description = jsonProduct.getString("description");
                        String shortDesc = jsonProduct.getString("short_description");
                        String pStatus = jsonProduct.getString("status");
                        String price = jsonProduct.getString("price");
                        JSONObject jsonImages = jsonProduct.getJSONArray("images").getJSONObject(0);
                        String photoUrl = jsonImages.getString("src");
                        Product product = new Product(id, name, description, shortDesc, pStatus, photoUrl, price);
                        mProducts.add(product);
                       // Log.d(TAG, "onDownloadComplete. " + product.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onDownloadComplete: Error proessing json data " + e.getMessage());
                    status = DownloadStatus.FAILED_OR_EMPTY;
                }
            } else if (mDataType.equals(BaseActivity.GET_USER)){
                Log.d(TAG, "onDownloadComplete: get user data completed ----> " );
                try {
                    jsonUser = new JSONObject(data);


                    int id = jsonUser.getInt("id");
                    String email = jsonUser.getString("email");
                    String firstName = jsonUser.getString("first_name");
                    String lastName = jsonUser.getString("last_name");
                    String role = jsonUser.getString("role");
                    String username = jsonUser.getString("username");
                    JSONObject jsonBilling = jsonUser.getJSONObject("billing");
                    String billFirstName = jsonBilling.getString("first_name");
                    String billLastName  = jsonBilling.getString("last_name");
                    String billAddress = jsonBilling.getString("address_1");
                    String billCity = jsonBilling.getString("city");
                    String billState = jsonBilling.getString("state");
                    String billPostCode = jsonBilling.getString("postcode");
                    String billEmail = jsonBilling.getString("email");
                    String billPhone = jsonBilling.getString("phone");
                    Customer.Billing billingAddress = new Customer.Billing(
                            billFirstName, billLastName, billAddress, billCity,billState,billPostCode,billEmail,billPhone);
                    //int id, String username, String firstname, String lastname, String email, boolean signedIn, BillingAddress billingAddress , String role
                    mCustomer = new Customer(id,username, firstName, lastName, email, billingAddress, role);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onDownloadComplete: Error proessing json data " + e.getMessage());
                    status = DownloadStatus.FAILED_OR_EMPTY;
                }


            } else if (mDataType.equals(BaseActivity.GET_CATEGORY)){
                mCategories = new ArrayList<>();
                try {
                    JSONArray itemsArray = new JSONArray(data);
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject jsonCat = itemsArray.getJSONObject(i);

                        int id = jsonCat.getInt("id");
                        String name = jsonCat.getString("name");
                        String description = jsonCat.getString("description");
                        int count = jsonCat.getInt("id");
                        int parent = jsonCat.getInt("parent");
                        String photoUrl="empty";
                        try {
                            JSONObject jsonImages = jsonCat.getJSONObject("image");
                            photoUrl = jsonImages.getString("src");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        Category category = new Category(id, name, parent, description, count, photoUrl);
                        mCategories.add(category);
                        // Log.d(TAG, "onDownloadComplete. " + product.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    status = DownloadStatus.FAILED_OR_EMPTY;
                }
            }

        }
        if (runningOnSameThread && mCallback != null) {
            mCallback.onDataAvailable(mProducts, status);
        }

        Log.d(TAG, "onDownloadComplete ends");
    }
}













