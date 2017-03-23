package com.example.android.sunshine;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends Activity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private TextView mDateView;
    private GoogleApiClient mGoogleApiClient;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

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

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
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

//    private static Asset createAssetFromBitmap(Bitmap bitmap) {
//        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
//        return Asset.createFromBytes(byteStream.toByteArray());
//    }
//
//    // create data items to send to wearable
//    public void sendWeatherData(String date, String highTemp, String lowTemp) {
//        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/weatherupdate");
//        putDataMapRequest.getDataMap().putString("date", date);
//        putDataMapRequest.getDataMap().putString("high temp", highTemp);
//        putDataMapRequest.getDataMap().putString("low temp", lowTemp);
//
//        // fetch weather graphic to display on wearable by converting to an asset to place
//        // in data map
//        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.art_clear);
//        Asset iconAsset = createAssetFromBitmap(icon);
//        // place asset in data map
//        putDataMapRequest.getDataMap().putAsset("weather icon", iconAsset);
//
//        PutDataRequest request = putDataMapRequest.asPutDataRequest();
//        Wearable.DataApi.putDataItem(mGoogleApiClient, request)
//                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
//                    @Override
//                    public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
//                        if (!dataItemResult.getStatus().isSuccess()) {
//                            Log.e(LOG_TAG, "Failed to send weather data item");
//                        } else {
//                            Log.d(LOG_TAG, "Successfully sent weather data item");
//                        }
//
//                    }
//                });
//    }

}
