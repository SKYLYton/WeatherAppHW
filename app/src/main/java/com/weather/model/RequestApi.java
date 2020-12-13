package com.weather.model;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.weather.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class RequestApi {
    public enum Type {
        GET, POST, PUT, DELETE
    }

    private static final String TAG = RequestApi.class.getSimpleName();

    private final String typeRequest;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    private static final int TIMEOUT = 10000;
    private final String URL;
    private final StringBuilder param;
    private final OnRequestApiListener onRequestApiListener;
    public interface OnRequestApiListener {
        void onSuccess(@Nullable String result, int responseCode);

        void onError();
    }

    public RequestApi(Builder builder) {
        this.URL = builder.URL;
        this.typeRequest = builder.typeRequest;
        this.param = builder.param;
        this.onRequestApiListener = builder.onRequestApiListener;
        request();
    }

    public static class Builder {

        private String typeRequest = GET;
        private StringBuilder param = new StringBuilder();
        private final String URL;
        private OnRequestApiListener onRequestApiListener;

        public Builder(String url) {
            this.URL = url;
        }

        public Builder requestType(Type requestType) {
            switch (requestType) {
                case PUT:
                    typeRequest = PUT;
                    break;
                case POST:
                    typeRequest = POST;
                    break;
                case DELETE:
                    typeRequest = DELETE;
                    break;
                default:
                    typeRequest = GET;
            }
            return this;
        }

        public Builder setOnRequestApiListener(OnRequestApiListener onRequestApiListener) {
            this.onRequestApiListener = onRequestApiListener;
            return this;
        }

        public Builder addPar(String key, String value) {
            if (param.length() > 0) {
                param.append("&");
            } else {
                param.append("?");
            }
            param.append(key + "=").append(value);
            return this;
        }

        public Builder addPar(String key, int value) {
            if (param.length() > 0) {
                param.append("&");
            } else {
                param.append("?");
            }
            param.append(key).append("=").append(value);
            return this;
        }

        public RequestApi build() {
            return new RequestApi(this);
        }

    }

    public void requestAgain() {
        request();
    }

    private void request() {
        final Handler handler = new Handler();
        try {
            final URL uri = new URL(URL + param.toString());
            new Thread(() -> {
                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpsURLConnection) uri.openConnection();
                    urlConnection.setRequestMethod(typeRequest);
                    urlConnection.setReadTimeout(TIMEOUT);
                    urlConnection.connect();

                    InputStream inputStream;

                    int code = urlConnection.getResponseCode();
                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        inputStream = urlConnection.getInputStream();
                    } else {
                        inputStream = urlConnection.getErrorStream();
                    }

                    String result = getLines(inputStream);

                    if(Constants.DEBUG) {
                        Log.d(TAG, result);
                    }

                    handler.post(() -> onRequestApiListener.onSuccess(result, code));

                } catch (Exception e) {
                    Log.e(TAG, "Fail connection", e);
                    e.printStackTrace();
                    handler.post(onRequestApiListener::onError);
                } finally {
                    if (null != urlConnection) {
                        urlConnection.disconnect();
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
            handler.post(onRequestApiListener::onError);
        }
    }

    private String getLines(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                is, StandardCharsets.UTF_8), 8);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        String response = sb.toString();

        return response;
    }
}
