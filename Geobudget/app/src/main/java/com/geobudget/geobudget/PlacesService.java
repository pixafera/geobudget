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

import java.util.Timer;
import java.util.TimerTask;

import static com.google.android.gms.location.places.Place.TYPE_AIRPORT;
import static com.google.android.gms.location.places.Place.TYPE_AMUSEMENT_PARK;
import static com.google.android.gms.location.places.Place.TYPE_AQUARIUM;
import static com.google.android.gms.location.places.Place.TYPE_BAKERY;
import static com.google.android.gms.location.places.Place.TYPE_BAR;
import static com.google.android.gms.location.places.Place.TYPE_BEAUTY_SALON;
import static com.google.android.gms.location.places.Place.TYPE_BICYCLE_STORE;
import static com.google.android.gms.location.places.Place.TYPE_BOOK_STORE;
import static com.google.android.gms.location.places.Place.TYPE_BOWLING_ALLEY;
import static com.google.android.gms.location.places.Place.TYPE_BUS_STATION;
import static com.google.android.gms.location.places.Place.TYPE_CAFE;
import static com.google.android.gms.location.places.Place.TYPE_CAR_DEALER;
import static com.google.android.gms.location.places.Place.TYPE_CAR_RENTAL;
import static com.google.android.gms.location.places.Place.TYPE_CAR_REPAIR;
import static com.google.android.gms.location.places.Place.TYPE_CAR_WASH;
import static com.google.android.gms.location.places.Place.TYPE_CLOTHING_STORE;
import static com.google.android.gms.location.places.Place.TYPE_CONVENIENCE_STORE;
import static com.google.android.gms.location.places.Place.TYPE_DEPARTMENT_STORE;
import static com.google.android.gms.location.places.Place.TYPE_DOCTOR;
import static com.google.android.gms.location.places.Place.TYPE_ELECTRONICS_STORE;
import static com.google.android.gms.location.places.Place.TYPE_FURNITURE_STORE;
import static com.google.android.gms.location.places.Place.TYPE_GAS_STATION;
import static com.google.android.gms.location.places.Place.TYPE_GROCERY_OR_SUPERMARKET;
import static com.google.android.gms.location.places.Place.TYPE_GYM;
import static com.google.android.gms.location.places.Place.TYPE_HAIR_CARE;
import static com.google.android.gms.location.places.Place.TYPE_HARDWARE_STORE;
import static com.google.android.gms.location.places.Place.TYPE_HEALTH;
import static com.google.android.gms.location.places.Place.TYPE_HOME_GOODS_STORE;
import static com.google.android.gms.location.places.Place.TYPE_HOSPITAL;
import static com.google.android.gms.location.places.Place.TYPE_JEWELRY_STORE;
import static com.google.android.gms.location.places.Place.TYPE_LAUNDRY;
import static com.google.android.gms.location.places.Place.TYPE_MEAL_DELIVERY;
import static com.google.android.gms.location.places.Place.TYPE_MEAL_TAKEAWAY;
import static com.google.android.gms.location.places.Place.TYPE_MOVIE_RENTAL;
import static com.google.android.gms.location.places.Place.TYPE_MOVIE_THEATER;
import static com.google.android.gms.location.places.Place.TYPE_NIGHT_CLUB;
import static com.google.android.gms.location.places.Place.TYPE_PARKING;
import static com.google.android.gms.location.places.Place.TYPE_PET_STORE;
import static com.google.android.gms.location.places.Place.TYPE_PHARMACY;
import static com.google.android.gms.location.places.Place.TYPE_PHYSIOTHERAPIST;
import static com.google.android.gms.location.places.Place.TYPE_PLUMBER;
import static com.google.android.gms.location.places.Place.TYPE_RESTAURANT;
import static com.google.android.gms.location.places.Place.TYPE_ROOFING_CONTRACTOR;
import static com.google.android.gms.location.places.Place.TYPE_SHOE_STORE;
import static com.google.android.gms.location.places.Place.TYPE_SPA;
import static com.google.android.gms.location.places.Place.TYPE_SUBWAY_STATION;
import static com.google.android.gms.location.places.Place.TYPE_TAXI_STAND;
import static com.google.android.gms.location.places.Place.TYPE_TRAIN_STATION;
import static com.google.android.gms.location.places.Place.TYPE_TRANSIT_STATION;
import static com.google.android.gms.location.places.Place.TYPE_TRAVEL_AGENCY;
import static com.google.android.gms.location.places.Place.TYPE_VETERINARY_CARE;
import static com.google.android.gms.location.places.Place.TYPE_ZOO;


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

    int runctr = 0;

    private ArrayMap<LatLng, String> mLocationMap;

    private ArrayMap<Integer, String> mPlacesCategoryMap;

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
        mPlacesCategoryMap = new ArrayMap<Integer, String>();
        mapCats();


        startService();
    }

    private void startService() {
        // Temporarily on a five minute timer

        timer.scheduleAtFixedRate(new mainTask(), 0, 30000);
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

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override

                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {


                    if (runctr != 1)
                    {
                        runctr++;
                        return;
                    }
                    else
                    {
                        runctr++;
                    }


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

                        ArrayMap<String, String> categoryShopsMap = new ArrayMap<String, String>();

                        for (LatLng loc : mLocationMap.keySet())
                        {
                            String nameType = mLocationMap.get(loc);
                            String[] components = nameType.split(":");
                            if (components.length > 1) {

                                String[] categories = components[1].substring(1, components[1].length() - 1).replace(" ", "").split(",");

                                if (categories.length > 0) {

                                    Integer placeCategory = Integer.parseInt(categories[0]);

                                    if (mPlacesCategoryMap.containsKey(placeCategory)) {
                                        String catname = mPlacesCategoryMap.get(placeCategory);
                                        String shopname = components[0];

                                        StringBuffer existingShops = new StringBuffer("");

                                        if (categoryShopsMap.containsKey(catname))
                                        {
                                            existingShops.append(categoryShopsMap.get(catname));
                                            existingShops.append(", ");
                                        }

                                        existingShops.append(shopname);
                                        categoryShopsMap.put(catname, existingShops.toString());
                                    }

                                    Log.d(TAG, "onComplete: Place category: " + placeCategory);
                                }
                            }



                        }

                        for (String catname : categoryShopsMap.keySet())
                        {

                            mBudgetNotificationManager.showNotificationForCategory(catname, categoryShopsMap.get(catname));
                        }



                        Log.d(TAG, "Notify");

                        mBudgetNotificationManager.showNotificationForCategory("Living Costs", "Foo");

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

    private void mapCats()
    {
        //mPlacesCategoryMap.put(TYPE_ATM,                    "Cash");
        //mPlacesCategoryMap.put(TYPE_BANK,                   "Cash");
        mPlacesCategoryMap.put(TYPE_PET_STORE,              "Family and Pets");
        mPlacesCategoryMap.put(TYPE_VETERINARY_CARE,        "Family and Pets");
        mPlacesCategoryMap.put(TYPE_CAR_DEALER,	            "Future needs");
        mPlacesCategoryMap.put(TYPE_TRAVEL_AGENCY,	        "Future needs");
        mPlacesCategoryMap.put(TYPE_AIRPORT,	            "Future needs");
        mPlacesCategoryMap.put(TYPE_JEWELRY_STORE,	        "Future needs");
        //mPlacesCategoryMap.put(TYPE_CASINO,	                "Gambeling warning");
        mPlacesCategoryMap.put(TYPE_ELECTRONICS_STORE,	    "Home");
        mPlacesCategoryMap.put(TYPE_FURNITURE_STORE,	    "Home");
        mPlacesCategoryMap.put(TYPE_HARDWARE_STORE,	        "Home");
        mPlacesCategoryMap.put(TYPE_HOME_GOODS_STORE,	    "Home");
        mPlacesCategoryMap.put(TYPE_PLUMBER,	            "Home");
        mPlacesCategoryMap.put(TYPE_ROOFING_CONTRACTOR,	    "Home");
        mPlacesCategoryMap.put(TYPE_AMUSEMENT_PARK,	        "Leisure");
        mPlacesCategoryMap.put(TYPE_AQUARIUM,	            "Leisure");
        mPlacesCategoryMap.put(TYPE_BAR,	                "Leisure");
        mPlacesCategoryMap.put(TYPE_BEAUTY_SALON,   	    "Leisure");
        mPlacesCategoryMap.put(TYPE_BOOK_STORE,     	    "Leisure");
        mPlacesCategoryMap.put(TYPE_BOWLING_ALLEY,	        "Leisure");
        mPlacesCategoryMap.put(TYPE_CAFE,	                "Leisure");
        mPlacesCategoryMap.put(TYPE_GYM,	                "Leisure");
        mPlacesCategoryMap.put(TYPE_HAIR_CARE,	            "Leisure");
        mPlacesCategoryMap.put(TYPE_MEAL_DELIVERY,	        "Leisure");
        mPlacesCategoryMap.put(TYPE_MEAL_TAKEAWAY,	        "Leisure");
        mPlacesCategoryMap.put(TYPE_MOVIE_RENTAL,	        "Leisure");
        mPlacesCategoryMap.put(TYPE_MOVIE_THEATER,	        "Leisure");
        mPlacesCategoryMap.put(TYPE_NIGHT_CLUB,	            "Leisure");
        mPlacesCategoryMap.put(TYPE_RESTAURANT,	            "Leisure");
        mPlacesCategoryMap.put(TYPE_SPA,	                "Leisure");
        mPlacesCategoryMap.put(TYPE_ZOO,	                "Leisure");
        mPlacesCategoryMap.put(TYPE_CLOTHING_STORE,	        "Living Costs");
        mPlacesCategoryMap.put(TYPE_SHOE_STORE,	            "Living Costs");
        mPlacesCategoryMap.put(TYPE_BAKERY,	                "Living Costs");
        mPlacesCategoryMap.put(TYPE_CONVENIENCE_STORE,	    "Living Costs");
        mPlacesCategoryMap.put(TYPE_GROCERY_OR_SUPERMARKET,	"Living Costs");
        mPlacesCategoryMap.put(TYPE_DEPARTMENT_STORE,	    "Living Costs");
        mPlacesCategoryMap.put(TYPE_DOCTOR,	                "Living Costs");
        mPlacesCategoryMap.put(TYPE_HEALTH,	                "Living Costs");
        mPlacesCategoryMap.put(TYPE_HOSPITAL,	            "Living Costs");
        mPlacesCategoryMap.put(TYPE_PHARMACY,	            "Living Costs");
        mPlacesCategoryMap.put(TYPE_PHYSIOTHERAPIST,	    "Living Costs");
        mPlacesCategoryMap.put(TYPE_LAUNDRY,	            "Living Costs");
        mPlacesCategoryMap.put(TYPE_PARKING,	            "Travel");
        mPlacesCategoryMap.put(TYPE_SUBWAY_STATION,     	"Travel");
        mPlacesCategoryMap.put(TYPE_TAXI_STAND,	            "Travel");
        mPlacesCategoryMap.put(TYPE_TRAIN_STATION,	        "Travel");
        mPlacesCategoryMap.put(TYPE_TRANSIT_STATION,      	"Travel");
        mPlacesCategoryMap.put(TYPE_BICYCLE_STORE,      	"Travel");
        mPlacesCategoryMap.put(TYPE_BUS_STATION,	        "Travel");
        mPlacesCategoryMap.put(TYPE_CAR_RENTAL,         	"Travel");
        mPlacesCategoryMap.put(TYPE_CAR_REPAIR,	            "Travel");
        mPlacesCategoryMap.put(TYPE_CAR_WASH,	            "Travel");
        mPlacesCategoryMap.put(TYPE_GAS_STATION,	        "Travel");
        //mPlacesCategoryMap.put(TYPE_LIQUOR_STORE,	"Warning_alcoholims");
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

