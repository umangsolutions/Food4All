package com.example.food4all.activities.general;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.food4all.R;
import com.example.food4all.SthreeRaksha;
import com.example.food4all.activities.donor.DonorCelebrateActivity;
import com.example.food4all.activities.donor.FoodDetailsActivity;
import com.example.food4all.activities.recipients.RecipientLoginActivity;
import com.example.food4all.activities.volunteer.VolunteerActivity;
import com.example.food4all.activities.volunteer.VolunteerLoginActivity;
import com.example.food4all.adapter.BannerAdapter;
import com.example.food4all.modals.Banners;
import com.example.food4all.recyclerView.RecyclerViewGallery;
import com.example.food4all.utilities.ConstantValues;
import com.example.food4all.utilities.MyAppPrefsManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public Button b1, b2;
    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;
    public TextView t, textView;
    MyAppPrefsManager myAppPrefsManager;

    String TAG = "MAIN_ACTIVITY";
    boolean doubleBackToExitPressedOnce = false;

    SliderView sliderImage;


    List<Banners> modelList = new ArrayList<>();

    private AdView mAdView;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Food 4 All");


        drawer = findViewById(R.id.drawer_layout);

        sliderImage = findViewById(R.id.slider_image);



        myAppPrefsManager = new MyAppPrefsManager(this);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        firebaseAuth = FirebaseAuth.getInstance();
        listAllFiles();


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-7341014042556519/2689368944");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        FirebaseMessaging.getInstance().subscribeToTopic("/topics/donateFood")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "getString(R.string.msg_subscribed)";
                        if (!task.isSuccessful()) {
                            msg = "getString(R.string.msg_subscribe_failed)";
                        }
                        Log.d(TAG, msg);
                    }
                });

        findViewById(R.id.volun).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn()) {
                        Intent intent = new Intent(getBaseContext(), VolunteerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(getBaseContext(), VolunteerLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.rest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, FoodDetailsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }


    public void listAllFiles() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("bannerImages");
        StorageReference storageRef1 = storage.getReference();


        storageRef.listAll()
                .addOnSuccessListener(listResult -> {


                    modelList.clear();
                    for (StorageReference item : listResult.getItems()) {
                        // All the items under listRef.
                        Log.d(TAG, "onSuccess1: " + item.getPath());

                        // [START download_via_url]
                        storageRef1.child(item.getPath()).getDownloadUrl().addOnSuccessListener(uri -> {


                            Log.d(TAG, "listAllFiles: " + item.getName() + uri.toString());
                            modelList.add(new Banners(item.getName(), uri.toString()));
                            BannerAdapter adapter = new BannerAdapter(MainActivity.this, modelList);
                            sliderImage.setSliderAdapter(adapter);
                            sliderImage.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            sliderImage.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
                            sliderImage.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                            sliderImage.setIndicatorRadius(5);
                            sliderImage.setIndicatorSelectedColor(Color.WHITE);
                            sliderImage.setIndicatorUnselectedColor(Color.GRAY);
                            sliderImage.startAutoCycle();
                            sliderImage.setOnIndicatorClickListener(position ->
                                    sliderImage.setCurrentPagePosition(position));

                        }).addOnFailureListener(exception -> {
                            // Handle any errors

                        });


                    }


                })
                .addOnFailureListener(e -> {
                    // Uh-oh, an error occurred!

                });


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_about:
                Intent b = new Intent(MainActivity.this, IssuesActivity.class);
                b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(b);
                break;
            case R.id.nav_info:
                Intent a = new Intent(MainActivity.this, AboutActivity.class);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
                break;
            case R.id.nav_message:
                Intent i = new Intent(MainActivity.this, HungerStatisticsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.nav_gallery:
                Intent g = new Intent(MainActivity.this, RecyclerViewGallery.class);
                g.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(g);
                break;
            case R.id.nav_org:
                Intent intent = new Intent(MainActivity.this, RecipientLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_happy:
                Intent intent1 = new Intent(MainActivity.this, DonorCelebrateActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                break;
            case R.id.nav_sthree:
                Intent intent2 = new Intent(MainActivity.this, SthreeRaksha.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                doubleBackToExitPressedOnce = false;


            }
        }, 2000);
    }
}
