package com.threembed.utilities;


import android.os.AsyncTask;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;


public class OkHttpRequest
{
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * To make JSON request
     *
     * @param request_Url :Provide Service URl to make request.
     * @param requestParameters :Provide JSONObject as request parameter.
     * @param callbacks :JsonRequestCallback defined in Utility for success or error callbacks.
     */

    public static void doJsonRequest(String request_Url,RequestBody requestParameters,JsonRequestCallback callbacks)
    {
       /* OKHttpPostRequestData data = new OKHttpPostRequestData();
        data.request_Url = request_Url;
        data.requestParameter = requestParameters;
        data.callbacks = callbacks;

        new JsonOkHttpRequest().execute(data);*/

        OKHttpPostRequestData data = new OKHttpPostRequestData();
        data.request_Url = request_Url;
        data.requestParameter = requestParameters;
        data.callbacks = callbacks;
        /*MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        data.body = RequestBody.create(JSON, requestParameters.toString());*/
        //return httpPostRequest(data);

        new JsonOkHttpRequest().execute(data);
    }

    private static  class OKHttpPostRequestData
    {
        String request_Url;
        RequestBody requestParameter;
        JsonRequestCallback callbacks;
        //RequestBody body;
    }
    /*****************************************************/

    private  static class JsonOkHttpRequest extends AsyncTask<OKHttpPostRequestData, Void, String>
    {
        JsonRequestCallback callbacks;
        boolean error =false;

        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(OKHttpPostRequestData... params)
        {
            // TODO Auto-generated method stub
            callbacks = params[0].callbacks;
            String result="";

            try
            {
                OkHttpClient client = new OkHttpClient();
                client.setConnectTimeout(50, TimeUnit.SECONDS);
                client.setReadTimeout(50, TimeUnit.SECONDS);
                client.setWriteTimeout(50,TimeUnit.SECONDS);


                Request request = new Request.Builder()
                        .url(params[0].request_Url)
                        .post(params[0].requestParameter)
                        .build();
                Response response = client.newCall(request).execute();
                result= response.body().string();
                response.body().close();
            }
            catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                error= true;
                result = e.toString();
                e.printStackTrace();
            }

            catch (ProtocolException e)
            {
                // TODO Auto-generated catch block
                error= true;
                result = e.toString();
                e.printStackTrace();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                error= true;
                result = e.toString();
                e.printStackTrace();
            }
            catch (Exception e)
            {
                // TODO: handle exception
                error= true;
                result = e.toString();
                e.printStackTrace();
            }
            return result;
        }
        /*****************************************************/

        @Override
        protected void onPostExecute(String result)
        {
            // TODO Auto-generated method stub

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
    /*****************************************************/

    public static interface JsonRequestCallback
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
        public void onError(String error);

    }
    /*****************************************************/



}
