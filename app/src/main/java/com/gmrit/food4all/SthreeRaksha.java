package com.gmrit.food4all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gmrit.food4all.location.Constants;
import com.gmrit.food4all.location.FetchAddressIntentService;
import com.gmrit.food4all.modals.Fooddetails;
import com.gmrit.food4all.modals.Volunteer;
import com.gmrit.food4all.utilities.LocationTrack;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SthreeRaksha extends AppCompatActivity {

    DatabaseReference ref;
    LinearLayout emergencylayout, policelayout;
    //LocationTrack locationTrack;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    private static final String LOCATION_ADDRESS_KEY = "location-address";

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    private Location mLastLocation;

    private boolean mAddressRequested=false;

    /**
     * The formatted location address.
     */
    private String mAddressOutput="";
    private String mAddressLatitude="";
    private String mAddressLongitude="";
    private String mAddressPostalCode="";

    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private AddressResultReceiver mResultReceiver;


    String lat, lon;
    String url;
    String police = "09440795852";
    private ArrayList<String> phoneno = new ArrayList<String>();
    private ArrayList<Fooddetails> fooddetailsArrayList = new ArrayList<Fooddetails>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sthree_raksha);
        this.setTitle("Sthree Raksha");

       /* locationTrack = new LocationTrack(this);

        lat = Double.toString(locationTrack.getLatitude());
        lon = Double.toString(locationTrack.getLongitude());*/

        mResultReceiver = new AddressResultReceiver(new Handler());


        // Set defaults, then update using values stored in the Bundle.


        updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (mLastLocation != null) {
            startIntentService();
            return;
        }

        // If we have not yet retrieved the user location, we process the user's request by setting
        // mAddressRequested to true. As far as the user is concerned, pressing the Fetch Address button
        // immediately kicks off the process of getting the address.
        mAddressRequested = true;



        Toast.makeText(SthreeRaksha.this, "Double Click on Emergency button to send Alert Messages", Toast.LENGTH_LONG).show();

        emergencylayout = (LinearLayout) findViewById(R.id.layout_emergency);
        policelayout = (LinearLayout) findViewById(R.id.policelayout);


        policelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", police, null));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });

        ref = (DatabaseReference) FirebaseDatabase.getInstance().getReference("Volunteers");

        emergencylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    //Toast.makeText(SthreeRaksha.this, "Latitude " + lat + "\n Longitude " + lon, Toast.LENGTH_SHORT).show();
                    /*Log.d("Latitude", lat);
                    Log.d("Longitude", lon);*/

                    url = "https://maps.google.com/?q=" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude() + "";

                   ref.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           if(dataSnapshot.exists()) {
                               for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                   String fooddetails = dataSnapshot1.getValue(Fooddetails.class).getPhone();
                                   //fooddetailsArrayList.add(fooddetails);
                                   phoneno.add(fooddetails);
                               }
                           }
                           else {
                               Toast.makeText(SthreeRaksha.this, "No Data Exists !", Toast.LENGTH_SHORT).show();
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {
                           Toast.makeText(SthreeRaksha.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   });


                    /*ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot.exists()) {
                                    String phone = dataSnapshot1.getValue(Volunteer.class).toString();
                                    phoneno.add(phone);
                                    //SmsManager smsManager = SmsManager.getDefault();
                                } else {
                                    Toast.makeText(SthreeRaksha.this, "Emergency Alert failed to Send", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(SthreeRaksha.this, "Hello" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                            Toast.makeText(SthreeRaksha.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });*/

                    String msg = "High Emergency Alert!!!\n";
                    String fina = msg + "I'm in Danger.Please help me.\nLocation: ";
                    String sam = fina + url;

                    String toNumbers = "";

                    for (String s : phoneno) {

                        Log.e("PHONELIST", "" + s);
                        toNumbers = toNumbers + s + ";";
                    }

                    phoneno.clear();
                    if (!toNumbers.isEmpty()) {
                        toNumbers = toNumbers.substring(0, toNumbers.length() - 1);
                        Uri sendSmsTo = Uri.parse("smsto:" + toNumbers);
                        Intent intent = new Intent(
                                Intent.ACTION_SENDTO, sendSmsTo);
                        intent.putExtra("sms_body",sam);
                        startActivity(intent);
                    }

                    //smsManager.sendTextMessage(phone, null, sam, null, null);

                   // Toast.makeText(SthreeRaksha.this, "Emergency Alert Messages Sent Successfully to all the Volunteers !", Toast.LENGTH_SHORT).show();

                }
        });


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }



    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getAddress();
        }
    }

    /**
     * Updates fields based on data stored in the bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                mAddressLatitude = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                mAddressLongitude = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                mAddressPostalCode = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                displayAddressOutput();
            }
        }
    }



    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    private void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.

        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    /**
     * Gets the address for the last known location.
     */
    @SuppressWarnings("MissingPermission")
    private void getAddress() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                           // Log.w(TAG, "onSuccess:null");
                            return;
                        }

                        mLastLocation = location;

                        // Determine whether a Geocoder is available.
                        if (!Geocoder.isPresent()) {
                            showSnackbar(getString(R.string.no_geocoder_available));
                            return;
                        }

                        // If the user pressed the fetch address button before we had the location,
                        // this will be set to true indicating that we should kick off the intent
                        // service after fetching the location.
                        if (mAddressRequested) {
                            startIntentService();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // Log.w(TAG, "getLastLocation:onFailure", e);
                    }
                });
    }

    /**
     * Updates the address in the UI.
     */
    private void displayAddressOutput() {

       /* Log.d(TAG,"Current Location  : "+mAddressOutput);
        Log.d(TAG,"Current Location  : "+mAddressLatitude);
        Log.d(TAG,"Current Location  : "+mAddressLongitude);
        Log.d(TAG,"Current Location  : "+mAddressPostalCode);
*/

       lat = mAddressLatitude;
       lon = mAddressLongitude;
    }




    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save whether the address has been requested.
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);

        // Save the address string.
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressLatitude);
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressLongitude);
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressPostalCode);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in RegistrationActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY_ADDRESS);
            mAddressLatitude = resultData.getString(Constants.RESULT_DATA_KEY_LATITUDE);
            mAddressLongitude = resultData.getString(Constants.RESULT_DATA_KEY_LONGITUDE);
            mAddressPostalCode = resultData.getString(Constants.RESULT_DATA_KEY_POSTAL_CODE);
            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                showToast(getString(R.string.address_found));
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
            updateUIWidgets();
        }
    }

    /**
     * Toggles the visibility of the progress bar. Enables or disables the Fetch Address button.
     */
    private void updateUIWidgets() {
        if (mAddressRequested) {


            //Log.d(TAG,"locationUpadted");
        } else {

           // Log.d(TAG,"locationNotUpadted");
        }
    }

    /**
     * Shows a toast with the given text.
     */
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        //Log.d(TAG,""+text);
    }


    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
           // Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(SthreeRaksha.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });

        } else {
            //Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(SthreeRaksha.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
               // Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getAddress();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}


