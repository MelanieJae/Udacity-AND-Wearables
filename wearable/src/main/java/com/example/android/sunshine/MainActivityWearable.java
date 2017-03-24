package com.example.android.sunshine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;

public class MainActivityWearable extends Activity
        implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private TextView mDateView;
    private GoogleApiClient mGoogleApiClient;
    private static final String LOG_TAG = MainActivityWearable.class.getSimpleName();
    public static final String IMAGE_KEY = "weather icon";
    private String dateString;
    private String highTempString;
    private String lowTempString;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mDateView = (TextView) stub.findViewById(R.id.date);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        Log.v(LOG_TAG, "date String= " + dateString);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        if ((mGoogleApiClient != null) && mGoogleApiClient.isConnected()) {
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(LOG_TAG, "onConnected(): Successfully connected to Google API client");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOG_TAG, "onConnectionSuspended(): Connection to Google API client was suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "onConnectionFailed(): Connection to Google API client was suspended");
    }

    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(LOG_TAG, "onDataChanged(): " + dataEvents);

        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (SunshineWearableListenerService.UPDATE_PATH.equals(path)) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    Asset photoAsset = dataMapItem.getDataMap()
                            .getAsset(IMAGE_KEY);
                    dateString = dataMapItem.getDataMap().getString("date");
                    highTempString = dataMapItem.getDataMap().getString("high temp");
                    lowTempString = dataMapItem.getDataMap().getString("low temp");
                    description = dataMapItem.getDataMap().getString("description");

                     //Loads image on background thread.
                    new LoadBitmapAsyncTask().execute(photoAsset);

                    ImageView iconView = (ImageView)findViewById(R.id.weather_icon);
                    TextView highTempView = (TextView)findViewById(R.id.high_temperature);
                    TextView lowTempView = (TextView)findViewById(R.id.low_temperature);
                    TextView descriptionView = (TextView)findViewById(R.id.weather_description);
//
                    mDateView.setText(dateString);
                    Log.v(LOG_TAG, "date String= " + dateString);
                    highTempView.setText(highTempString);
                    lowTempView.setText(lowTempString);
                    descriptionView.setText(description);
                }

            }
        }

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
                    Log.w(LOG_TAG, "Requested an unknown Asset.");
                    return null;
                }
                return BitmapFactory.decodeStream(assetInputStream);

            } else {
                Log.e(LOG_TAG, "Asset must be non-null");
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null) {
                ImageView iconView = (ImageView)findViewById(R.id.weather_icon);
                TextView dateView = (TextView)findViewById(R.id.date);
                TextView highTempView = (TextView)findViewById(R.id.high_temperature);
                TextView lowTempView = (TextView)findViewById(R.id.low_temperature);
                TextView descriptionView = (TextView)findViewById(R.id.weather_description);

                dateView.setText(dateString);
                highTempView.setText(highTempString);
                lowTempView.setText(lowTempString);
                descriptionView.setText(dateString);

                Drawable weatherIcon = new BitmapDrawable(getResources(), bitmap);
                iconView.setBackground(weatherIcon);
            }

        }
    }


}
