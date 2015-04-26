package com.oleksiykovtun.android.cooltools;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oleksiykovtun.iwmy.speeddating.Base64Converter;

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
    private String clientVersion;
    protected List parsedResponse = new ArrayList();
    protected CoolWebAsyncResponse delegate = null;
    private Object[] uploadData;
    private Class responseClass;
    private int httpResponseStatusCode = NOT_CONNECTED;

    private static final int NOT_CONNECTED = -1;
    private static final int HTTP_OK = 200;
    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_FORBIDDEN = 403;
    private static final int HTTP_GONE = 410;

    private static int timeoutMillis = 15000;

    public CoolWebAsyncTask(String tag, String authorizationId, String password,
                            String clientVersion, CoolWebAsyncResponse delegate,
                            Class responseClass, Object... uploadData) {
        this.tag = tag;
        this.authorizationId = authorizationId;
        this.clientVersion = clientVersion;
        this.password = password;
        this.delegate = delegate;
        this.responseClass = responseClass;
        this.uploadData = (uploadData != null) ? uploadData : new Object[]{};
    }

    public interface CoolWebAsyncResponse {
        void onPostReceive(String tag, List responseObjectList); // success (HTTP 200)

        void onPostConnectionError(); // unable to reach the server

        void onPostAuthorizationError(); // client unauthorized (HTTP 401)

        void onPostAccessError(); // client authorized, access forbidden (HTTP 403)

        void onPostVersionError(); // client needs to update the app (HTTP 410)

        void onPostError(); // server cannot process this request now (HTTP other code)
    }

    public boolean isRunningNow() {
        return (getStatus() == Status.RUNNING) && (!isCancelled());
    }

    @Override
    protected Void doInBackground(String... urls) {
        final String encoding = "UTF-8";
        try {
            HttpPost httpPost = new HttpPost(new URI(urls[0]));
            httpPost.setHeader("Authorization", getAuthorizationHeader(authorizationId, password));
            httpPost.setHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setHeader("Content-Version", clientVersion);
            String jsonStringUpload = getJsonString(uploadData);
            Log.d("IWMY", "Sending\n" + uploadData.length + " items TO " + urls[0]
                    + "\n" + jsonStringUpload + "\n");
            httpPost.setEntity(new StringEntity(jsonStringUpload, encoding));

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutMillis);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutMillis);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse response = httpClient.execute(httpPost);
            httpResponseStatusCode = response.getStatusLine().getStatusCode();
            if (httpResponseStatusCode != HTTP_OK) {
                throw new Exception("Request failed. HTTP status code " + httpResponseStatusCode);
            }
            String jsonString = EntityUtils.toString(response.getEntity());
            if (responseClass != null) {
                parsedResponse = getObjectList(jsonString, responseClass);
            }
            Log.d("IWMY", "Received\n" + parsedResponse.size() + " items FROM " + urls[0] + "\n"
                    + jsonString + "\n");
        } catch (Throwable e) {
            Log.e("IWMY", "Post request error:\n", e);
            cancel(true);
        }
        return null;
    }

    public void cancel() {
        delegate = null;
        cancel(true);
    }

    @Override
    protected void onPostExecute(Void result) {
        if (delegate != null) {
            delegate.onPostReceive(tag, parsedResponse);
        }
    }

    @Override
    protected void onCancelled(Void result) {
        if (delegate != null) {
            switch (httpResponseStatusCode) {
                case NOT_CONNECTED:
                    delegate.onPostConnectionError();
                    break;
                case HTTP_UNAUTHORIZED:
                    delegate.onPostAuthorizationError();
                    break;
                case HTTP_FORBIDDEN:
                    delegate.onPostAccessError();
                    break;
                case HTTP_GONE:
                    delegate.onPostVersionError();
                    break;
                default:
                    delegate.onPostError();
                    break;
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
        return "Basic "
                + Base64Converter.getBase64StringFromString(authorizationId + ":" + password);
    }

}
