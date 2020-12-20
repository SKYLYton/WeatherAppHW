package com.weather.model.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.weather.Constants;
import com.weather.R;
import com.weather.bottomsheet.BottomSheetCreator;
import com.weather.model.RequestApi;
import com.weather.model.weather.WeatherParser;
import com.weather.model.weather.WeatherRequest;

import java.net.HttpURLConnection;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RequestIntentService extends IntentService {

    public RequestIntentService() {
        super("RequestIntentService");
    }


    public static void startIntentService(Context context, int cityId) {
        Intent intent = new Intent(context, RequestIntentService.class);
        intent.putExtra(Constants.SERVICE_CITY_ID, cityId);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            int cityId = bundle.getInt(Constants.SERVICE_CITY_ID);
            RequestApiForService requestApiForService = new RequestApiForService.Builder(Constants.URL_CONNECTION)
                    .requestType(RequestApiForService.Type.GET)
                    .addPar(Constants.API_UNITS_NAME, Constants.API_UNITS_METRIC)
                    .addPar(Constants.API_LANGUAGE_NAME, Locale.getDefault().getCountry())
                    .addPar(Constants.API_CITY_ID_NAME, cityId)
                    .addPar(Constants.API_KEY_NAME, Constants.API_KEY)
                    .build();

            AnswerItem answerItem = requestApiForService.getCurrentAnswerItem();

            if(answerItem.getCode() == HttpURLConnection.HTTP_OK) {
                WeatherRequest weatherRequest = new WeatherParser(answerItem.getResult()).getWeatherRequest();

                sendBroadcast(answerItem.getCode(), weatherRequest.getName(), weatherRequest.getWeather()[0].getDescription(),
                        weatherRequest.getMain().getTemp(), weatherRequest.getMain().getPressure(), weatherRequest.getWind().getSpeed());
            } else {
                sendBroadcast(answerItem.getCode());
            }
        }
    }

    private void sendBroadcast(int code, String name, String type, float temp, int pressure, float wind) {
        Intent broadcastIntent = new Intent(Constants.BROADCAST_ACTION_REQUEST);
        broadcastIntent.putExtra(Constants.BROADCAST_EXTRA_CITY_NAME, name);
        broadcastIntent.putExtra(Constants.BROADCAST_EXTRA_CITY_PRESSURE, pressure);
        broadcastIntent.putExtra(Constants.BROADCAST_EXTRA_CITY_TEMP, temp);
        broadcastIntent.putExtra(Constants.BROADCAST_EXTRA_CITY_TYPE, type);
        broadcastIntent.putExtra(Constants.BROADCAST_EXTRA_CITY_WIND, wind);
        broadcastIntent.putExtra(Constants.SERVICE_REQUEST_CODE, code);

        sendBroadcast(broadcastIntent);
    }

    private void sendBroadcast(int code) {
        Intent broadcastIntent = new Intent(Constants.BROADCAST_ACTION_REQUEST);
        broadcastIntent.putExtra(Constants.SERVICE_REQUEST_CODE, code);
        sendBroadcast(broadcastIntent);
    }


}