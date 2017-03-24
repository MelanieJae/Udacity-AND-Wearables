package com.example.android.sunshine;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
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
        Log.d(LOG_TAG, "onDataChanged: " + dataEvents);
        if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting()) {
            ConnectionResult connectionResult = mGoogleApiClient
                    .blockingConnect(30, TimeUnit.SECONDS);
            if (!connectionResult.isSuccess()) {
                Log.e(LOG_TAG, "DataLayerListenerService failed to connect to GoogleApiClient, "
                        + "error code: " + connectionResult.getErrorCode());
                return;
            }
        }
//
//        // Loop through the events and send a message back to the node that created the data item.
        for (DataEvent event : dataEvents) {
            Uri uri = event.getDataItem().getUri();
            String path = uri.getPath();
            if (UPDATE_PATH.equals(path)) {
                // Get the node id of the node that created the data item from the host portion of
                // the uri.
                String nodeId = uri.getHost();
                // Set the data of the message to be the bytes of the Uri.
                byte[] payload = uri.toString().getBytes();
            }
        }
    }

}
