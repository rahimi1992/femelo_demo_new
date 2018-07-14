package com.femelo.femelo_demo;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;


import javax.net.ssl.HttpsURLConnection;

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK}

class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";
    private DownloadStatus mDownloadStatus;
    private final OnDownloadComplete mCallback;

    interface OnDownloadComplete{
        void onDownloadComplete(String data, DownloadStatus status);
    }

    public GetRawData(OnDownloadComplete mCallback) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mCallback = mCallback;
    }

    void runInSameThread(String s){
        Log.d(TAG, "runInSameThread starts");
//        onPostExecute(doInBackground(s));
        if (mCallback != null){
            mCallback.onDownloadComplete(doInBackground(s), mDownloadStatus);
        }
    }

    @Override
    protected void onPostExecute(String s) {
//        Log.d(TAG, "onPostExecute: parameters = " + s);
        if(mCallback !=null){
            Log.d(TAG, "onPostExecute: onDownload method called");
            mCallback.onDownloadComplete(s, mDownloadStatus);
        }
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground starts");
        BufferedReader reader = null;
        Process process = null;
        URLConnection connection = null;
        if (strings == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        }
        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            //String page = strings[0];
            String baseUrl = strings[0]; //&search=قهوه"
            Log.d(TAG, "doInBackground: " + baseUrl);
            URL url = new URL(baseUrl);
            connection = url.openConnection();
            connection.setRequestProperty("X-Requested-With", "Curl");
            String userpass = "ck_a69bd88d175c4c64e7d5062e3ce82951b83797a0" + ":" + "cs_b89c50f09b47688b1e9a46a4113b0bcf469fefd8";
            //android.util.Base64.encode(userpass.getBytes(),0);
            //System.out.println(new String(Base64.(userpass.getBytes());
            //System.out.println("anddd "+new String(android.util.Base64.encode(userpass.getBytes(),android.util.Base64.DEFAULT)));
            //System.out.println("bbbbb64 "+new String(Base64.getEncoder().encode(userpass.getBytes())));
            String enc_userpass = new String(android.util.Base64.encode(userpass.getBytes(),android.util.Base64.NO_WRAP));
            String basicAuth = "Basic " + enc_userpass;

            connection.setRequestProperty ("Authorization", basicAuth);
            connection.connect();
//            Runtime runtime = Runtime.getRuntime();
//            process = runtime.exec(curl);
//            int response = process.waitFor();
//            Log.d(TAG, "doInBackground: the response code: " + response);
            StringBuilder result = new StringBuilder();
//            reader = new BufferedReader(new InputStreamReader(process.getInputStream())); //, "ISO-8859-1"
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line, dLine;
            while ((line = reader.readLine()) != null) {
                dLine = URLDecoder.decode(line, "utf8");
                result.append(dLine).append("\n");
                // result.append(line).append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            return result.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid url " + e.getMessage());
            mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IO Exception reading data " + e.getMessage());
            mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Security Exception, Need Permission? " + e.getMessage());
            mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream: " + e.getMessage());
                }
            }

        }

        return null;
    }


}
