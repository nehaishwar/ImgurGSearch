package com.example.test.imgurgsearch;

import android.util.Log;

import com.example.test.imgurgsearch.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.example.test.imgurgsearch.MainActivity;
import com.example.test.imgurgsearch.view.PhtoVHAdapter;

public class MyConnection {

    public static class MyConnectRequest {
        private static String TAG= "ConnReq";
        private MyConnectRequest() {

        }
        static OkHttpClient httpClient;
        static Request request;
        static ArrayList<Photo> photoArrayList;

        //Build HtttpClient object
        public static OkHttpClient getHttpClient() {
            httpClient = new OkHttpClient.Builder().build();
            return httpClient;
        }


       //Build Imgur gallery search URL request along with client ID key
        public static Request buildRequest(String searchTag) {

            request = new Request.Builder()
                    .url("https://api.imgur.com/3/gallery/top/week/search?q="+searchTag)
                    .header("Authorization", "Client-ID 20f7e3e4c587881")
                    .header("User-Agent", "My Little App")
                    .build();
            return request;
        }

        public static void getHttpResponse(Request req, final PhtoVHAdapter adapter) {
            photoArrayList = new ArrayList<Photo>();
             Log.d(TAG,"getHttpResponse");
        }
    }
}