package com.example.android.sunshine;

import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.concurrent.TimeUnit;

/**
 * Created by melanieh on 3/23/17.
 */

public class SunshineWearableListenerService
        extends WearableListenerService {

    private GoogleApiClient mGoogleApiClient;
    public static final String UPDATE_PATH = "/update";
    private static final String LOG_TAG = SunshineWearableListenerService.class.getSimpleName();

    @Override
        public void onCreate() {
            super.onCreate();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)
                    .build();
            mGoogleApiClient.connect();

        }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        ConnectionResult connectionResult = mGoogleApiClient
                .blockingConnect(30, TimeUnit.SECONDS);
        if (mGoogleApiClient.isConnected()) {
            Log.d(LOG_TAG, "Connection to GoogleApiClient successful");
        } else {
            Log.e(LOG_TAG, "Mobile Main Activity failed to connect to GoogleApiClient, "
                    + "error code: " + connectionResult.getErrorCode());
            return;
        }

    }

}
