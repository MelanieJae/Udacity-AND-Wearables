package com.example.android.sunshine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.InputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by melanieh on 3/23/17.
 */

public class SunshineWearableListenerService
        extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

        @Override
        public void onCreate() {
            super.onCreate();
            GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)
                    .build();
            mGoogleApiClient.connect();

        }

        @Override
        public void onDataChanged(DataEventBuffer dataEvents) {
            for (DataEvent dataEvent: dataEvents) {
                if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                    DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem())
                            .getDataMap();
                    String path = dataEvent.getDataItem().getUri().getPath();
                    if (path.equals(STRINGS_PATH)) {
                        String dateString = dataMap.getString("date");
                        String highTempString = dataMap.getString("high temp");
                        String lowTempString = dataMap.getString("low temp");
                        String description = dataMap.getString("description");
                    } else {
                        Asset iconAsset = dataMap.getAsset("weather icon");
                        new LoadBitmapAsyncTask().execute(photoAsset);

                    }
                }
            }
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {

        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

    /*
    * Extracts {@link android.graphics.Bitmap} data from the
    * {@link com.google.android.gms.wearable.Asset}
    */
    private class LoadBitmapAsyncTask extends AsyncTask<Asset, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Asset... params) {

            if (params.length > 0) {

                Asset asset = params[0];

                InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                        mGoogleApiClient, asset).await().getInputStream();

                if (assetInputStream == null) {
                    Log.w(TAG, "Requested an unknown Asset.");
                    return null;
                }
                return BitmapFactory.decodeStream(assetInputStream);

            } else {
                Log.e(TAG, "Asset must be non-null");
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null) {
                LOGD(TAG, "Setting background image on second page..");
                moveToPage(1);
                mAssetFragment.setBackgroundImage(bitmap);
            }
        }
    }

}
