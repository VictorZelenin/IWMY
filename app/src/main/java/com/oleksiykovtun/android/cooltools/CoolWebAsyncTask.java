package com.oleksiykovtun.android.cooltools;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alx on 2014-11-20.
 */
class CoolWebAsyncTask extends AsyncTask<String, Void, Void> {

    private String tag;
    private String authorizationId;
    private String password;
    protected List parsedResponse = new ArrayList();
    protected CoolWebAsyncResponse delegate = null;
    private Object[] uploadData;
    private Class responseClass;

    private static final int HTTP_OK = 200;
    private static int timeoutMillis = 15000;
    private String errorString = null;

    public CoolWebAsyncTask(String tag, String authorizationId, String password,
                            CoolWebAsyncResponse delegate, Class responseClass, Object... uploadData) {
        this.tag = tag;
        this.authorizationId = authorizationId;
        this.password = password;
        this.delegate = delegate;
        this.responseClass = responseClass;
        this.uploadData = (uploadData != null) ? uploadData : new Object[]{};
    }

    public interface CoolWebAsyncResponse {
        void onReceiveWebData(List webDataString);

        void onReceiveWebData(String tag, List webDataString);

        void onFailReceivingWebData(String webDataErrorString);
    }

    public boolean isRunningNow() {
        return (getStatus() == Status.RUNNING) && (!isCancelled());
    }

    @Override
    protected Void doInBackground(String... urls) {
        int status = -1;
        final String encoding = "UTF-8";
        try {
            HttpPost httpPost = new HttpPost(new URI(urls[0]));
            httpPost.setHeader("Authorization", getAuthorizationHeader(authorizationId, password));
            httpPost.setHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            String jsonStringUpload = getJsonString(uploadData);
            Log.d("IWMY", "Sending\n" + uploadData.length + " items TO " + urls[0]
                    + "\n" + jsonStringUpload + "\n");
            httpPost.setEntity(new StringEntity(jsonStringUpload, encoding));

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutMillis);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutMillis);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse response = httpClient.execute(httpPost);
            status = response.getStatusLine().getStatusCode();
            if (status == HTTP_OK) {
                String jsonString = EntityUtils.toString(response.getEntity());
                if (responseClass != null) {
                    parsedResponse = getObjectList(jsonString, responseClass);
                }
                Log.d("IWMY", "Received\n" + parsedResponse.size() + " items FROM " + urls[0]
                        + "\n" + jsonString + "\n");
            } else {
                throw new Throwable("HTTP status " + status);
            }
        } catch (Throwable e) {
            errorString = "HTTP " + status + "\nException:\n" + e.getMessage() + "\nStack trace:\n";
            for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
                errorString += stackTraceElement + "\n";
            }
            Log.e("IWMY", errorString);
        }
        return null;
    }

    public void cancel() {
        cancel(true);
        delegate = null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (delegate != null && !isCancelled()) {
            if (errorString != null) {
                delegate.onFailReceivingWebData(errorString);
                cancel();
            } else {
                delegate.onReceiveWebData(parsedResponse);
                delegate.onReceiveWebData(tag, parsedResponse);
            }
        }
    }

    private String getJsonString(Object[] objectArray) {
        return new Gson().toJson(objectArray,
                TypeToken.get(objectArray.getClass()).getRawType());
    }

    private List getObjectList(String jsonString, Class objectClass) {
        return Arrays.asList((Object[]) new Gson().fromJson(jsonString, objectClass));
    }

    private String getAuthorizationHeader(String authorizationId, String password) {
        return "Basic " + Base64.encodeToString((authorizationId + ":" + password).getBytes(),
                Base64.URL_SAFE | Base64.NO_WRAP);
    }

}
