package com.geobudget.geobudget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PlacesService extends Service {
    private static Timer timer = new Timer();
    private Context ctx;
    private static final String TAG = "PlacesService";
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private BudgetNotificationManager mBudgetNotificationManager;

    private static final int M_MAX_ENTRIES = 100;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    private String[] mLikelyPlaceTypes;


    private Map<LatLng, String> mLocationMap;
    private BudgetDatabase _db;

//    public void SetBudgetNotificationManager(BudgetNotificationManager budgetNotificationManager){
//        mBudgetNotificationManager = budgetNotificationManager;
//    }

    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onCreate()
    {
        super.onCreate();
        ctx = this;

        _db = new BudgetDatabase(this);

        mBudgetNotificationManager = new BudgetNotificationManager(this, _db);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mLocationMap = new ArrayMap<LatLng, String>();



        startService();
    }

    private void startService()
    {
        // Removed for debugging
//        timer.scheduleAtFixedRate(new mainTask(), 0, 5000);
    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
            //toastHandler.sendEmptyMessage(0);

            // Do all of the checking for places, and working out notification logic in here

            //noinspection MissingPermission TODO Check perms
            Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);

            placeResult.addOnCompleteListener (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {

                @Override

                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getCount();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        mLikelyPlaceNames = new String[count];
                        mLikelyPlaceAddresses = new String[count];
                        mLikelyPlaceAttributions = new String[count];
                        mLikelyPlaceLatLngs = new LatLng[count];
                        mLikelyPlaceTypes = new String[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                            // Build a list of likely places to show the user.
                            mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                            mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
                                    .getAddress();
                            mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                    .getAttributions();
                            mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
                            mLikelyPlaceTypes[i] = placeLikelihood.getPlace().getPlaceTypes().toString();



                            //Log.d(TAG, placeLikelihood.getPlace().getPlaceTypes().toString());

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Release the place likelihood buffer, to avoid memory leaks.
                        likelyPlaces.release();

                        if (mLikelyPlaceNames.length > 0) {

                            for (int j = 0; j < mLikelyPlaceNames.length; j++) {
//                                Log.d(TAG, mLikelyPlaceNames[j].concat(mLikelyPlaceTypes[j]));
                                if(!mLocationMap.containsKey(mLikelyPlaceLatLngs[j]));
                                {
                                    mLocationMap.put(mLikelyPlaceLatLngs[j], mLikelyPlaceNames[j].concat(":").concat(mLikelyPlaceTypes[j]));
                                }
                            }
                        }
                        else
                        {
                            Log.e(TAG, "No Places");
                        }

                        Log.d(TAG, "Notify");

                        mBudgetNotificationManager.showNotificationForCategory(1, "Foo");

                        Log.d(TAG, mLocationMap.toString());

                    } else {
                        Log.e(TAG, "Exception: %s ", task.getException());
                    }
                }
            });

        }
    }

    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
    }

//    private final Handler toastHandler = new Handler()
//    {
//        @Override
//        public void handleMessage(Message msg)
//        {
//            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
//        }
//    };
}








//    @Override
//    public void onCreate() {
//        Log.d(TAG, "OnCreate");
//
//        super.onCreate();
//
//
//        //noinspection MissingPermission TODO Fix this missing perms check
//



//        Log.d(TAG, "OnCreated");

//    }

