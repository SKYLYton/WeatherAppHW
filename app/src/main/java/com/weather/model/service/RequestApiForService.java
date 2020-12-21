package com.weather.model.service;

import android.util.Log;

import androidx.annotation.Nullable;

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

public class RequestApiForService {
    public enum Type {
        GET, POST, PUT, DELETE
    }

    private static final String TAG = RequestApiForService.class.getSimpleName();

    private final String typeRequest;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    private static final int TIMEOUT = 10000;
    private final String URL;
    private final StringBuilder param;
    private AnswerItem currentAnswerItem;


    public RequestApiForService(Builder builder) {
        this.URL = builder.URL;
        this.typeRequest = builder.typeRequest;
        this.param = builder.param;
        currentAnswerItem = request();
    }

    public AnswerItem getCurrentAnswerItem() {
        return currentAnswerItem;
    }

    public static class Builder {

        private String typeRequest = GET;
        private StringBuilder param = new StringBuilder();
        private final String URL;

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

        public RequestApiForService build() {
            return new RequestApiForService(this);
        }

    }

    public void requestAgain() {
        currentAnswerItem = request();
    }

    private AnswerItem request() {
        try {
            final URL uri = new URL(URL + param.toString());

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

                    return new AnswerItem(result, code);

                } catch (Exception e) {
                    Log.e(TAG, "Fail connection", e);
                    e.printStackTrace();

                } finally {
                    if (null != urlConnection) {
                        urlConnection.disconnect();
                    }
                }

        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
        }
        return new AnswerItem(Constants.EMPTY_STRING, 0);
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
