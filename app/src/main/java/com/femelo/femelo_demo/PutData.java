package com.femelo.femelo_demo;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.fasterxml.jackson.databind.deser.impl.MethodProperty;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

import javax.net.ssl.HttpsURLConnection;

enum UploadStatus {IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK}

class PutData extends AsyncTask<String, Void, String> {

    private static final String TAG = "PutData";
    private UploadStatus mUploadStatus;
    private OnDataAvailable mCallback;

    public PutData(OnDataAvailable callback) {
        mUploadStatus = UploadStatus.IDLE;
        mCallback = callback;
    }

    interface OnDataAvailable{
        void onDataAvailable(int orderId, UploadStatus uploadStatus);
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: starts " + s + " " + mUploadStatus);
        super.onPostExecute(s);
        if (mUploadStatus == UploadStatus.OK){
        try {
            JSONObject jsonOrder = new JSONObject(s);
            int orderId = Integer.valueOf(jsonOrder.getString("id"));
            mCallback.onDataAvailable(orderId, mUploadStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        }

    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground starts");
        BufferedReader reader = null;
        Process process = null;
        HttpsURLConnection connection = null;
        if (strings == null) {
            mUploadStatus = UploadStatus.NOT_INITIALIZED;
            return null;
        }
        try {
            mUploadStatus = UploadStatus.PROCESSING;
            //String page = strings[0];
            String baseUrl = strings[0]; //&search=قهوه"
            String data = strings[1];
            //String data = "{\"billing\":{\"address_1\":\"جمهوري اسلامي ايرا\",\"city\":\"تهران\",\"email\":\"f.rahimi1992@gmail.com\",\"first_name\":\"فريدyyy\",\"last_name\":\"رحيمي\",\"phone\":\"09364813829\",\"postcode\":\"\",\"state\":\"THR\"},\"shipping\":{\"address_1\":\"جمهوري اسلامي ايرا\",\"city\":\"تهران\",\"email\":\"f.rahimi1992@gmail.com\",\"first_name\":\"فريدyyy\",\"last_name\":\"رحيمي\",\"phone\":\"09364813829\",\"postcode\":\"\",\"state\":\"THR\"},\"customer_id\":3" +
            //        ",\"line_items\":[{\"product_id\":1672,\"quantity\":1}],\"payment_method\":\"bacs\",\"payment_method_title\":\"Direct Bank Transfer\",\"shipping_lines\":[{\"method_id\":\"flat_rate\",\"method_title\":\"shipping title\",\"total\":\"200\"}],\"set_paid\":true}";
            Log.d(TAG, "doInBackground: baseUrl: " + baseUrl);
            Log.d(TAG, "doInBackground: data: " + data);
            URL url = new URL(baseUrl);
            connection = (HttpsURLConnection) url.openConnection();
            //connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("X-Requested-With", "Curl");
            String userpass = "ck_a69bd88d175c4c64e7d5062e3ce82951b83797a0" + ":" + "cs_b89c50f09b47688b1e9a46a4113b0bcf469fefd8";

            String enc_userpass = new String(android.util.Base64.encode(userpass.getBytes(),android.util.Base64.NO_WRAP));
            String basicAuth = "Basic " + enc_userpass;

            connection.setRequestProperty ("Authorization", basicAuth);

            connection.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(data);
            outputStreamWriter.flush();
            Log.d(TAG, "doInBackground: flushed");
            connection.connect();
            Log.d(TAG, "doInBackground: response:" + connection.getResponseCode());
            Log.d(TAG, "doInBackground: res message: "+connection.getResponseMessage());

            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line, dLine;
            while ((line = reader.readLine()) != null) {
                dLine = URLDecoder.decode(line, "utf8");
                result.append(dLine).append("\n");
            }
            outputStreamWriter.close();


            mUploadStatus = UploadStatus.OK;
            Log.d(TAG, "doInBackground: " + result.toString());
            return result.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid url " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IO Exception reading data " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Security Exception, Need Permission? " + e.getMessage());

        } finally {

            if (connection != null) {
                connection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream: " + e.getMessage());
                }
            }
        }
        mUploadStatus = UploadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
