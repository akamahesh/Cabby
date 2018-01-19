package com.threembed.utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * @author Akbar
 */

public class OkHttpRequestObject
{
    /**
     * To make JSON request
     *
     * @param url :Provide Service URl to make request.
     * @param requestParam :Provide JSONObject as request parameter.
     * @param callbacks :JsonRequestCallback defined in Utility for success or error callbacks.
     */
    public static void postRequest(String url, JSONObject requestParam, OkHttpRequestObject.JsonRequestCallback callbacks)
    {
        OkHttpRequestObject.JsonHttpRequestData data = new OkHttpRequestObject.JsonHttpRequestData();
        data.request_Url = url;
        data.requestParameter = requestParam;
        data.callbacks = callbacks;

        new OkHttpRequestObject.HttpRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
    }
    private static class HttpRequest extends AsyncTask<JsonHttpRequestData, Void, String>
    {
         private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpRequestObject.JsonRequestCallback callbacks;
        boolean error =false;

        @Override
        protected String doInBackground(OkHttpRequestObject.JsonHttpRequestData... params)
        {
            callbacks = params[0].callbacks;
            String result = "";
            OkHttpClient httpClient = new OkHttpClient();
            httpClient.setConnectTimeout(50, TimeUnit.SECONDS);
            httpClient.setReadTimeout(50, TimeUnit.SECONDS);
            httpClient.setWriteTimeout(50,TimeUnit.SECONDS);

            httpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                     //return true;
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify("pqadmin.com", session);
                }
            });

            RequestBody body = RequestBody.create(JSON, params[0].requestParameter.toString());
            Request request = new Request.Builder()
                    .url(params[0].request_Url)
                    .header("Content-Type", "application/json; Charset=UTF-8")
                    .post(body)
                    .build();

            Response response = null;
            try
            {
                response = httpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {

                result = response.body().string();
                Log.i("TAG","response code "+response.code());


            } catch (Exception e1)
            {
                error= true;
                result = e1.toString();
                e1.printStackTrace();
            }

            return result;
        }
        @Override
        protected void onPostExecute(String result)
        {


            super.onPostExecute(result);
            if(!error)
            {
                callbacks.onSuccess(result);
            }
            else
            {
                callbacks.onError(result);
            }
        }
    }

    private static  class JsonHttpRequestData
    {
        String request_Url;
        JSONObject requestParameter;
        OkHttpRequestObject.JsonRequestCallback callbacks;
    }

    public interface JsonRequestCallback
    {
        /**
         * Called When Success result of JSON request
         *
         * @param result
         */
        public void onSuccess(String result);


        /**
         * Called When Error result of JSON request
         *
         * @param error
         */
        void onError(String error);

    }
    /****************************************************************************************************/
}
