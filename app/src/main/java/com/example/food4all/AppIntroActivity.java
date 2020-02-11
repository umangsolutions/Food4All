package com.example.food4all;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class AppIntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_app_intro);

        addSlide(AppIntroFragment.newInstance("Food 4 All", "Donate Food and it reaches the needy",
                R.drawable.flog, ContextCompat.getColor(getApplicationContext(), R.color.orange)));
        addSlide(AppIntroFragment.newInstance("Donate your Excess Food", "Submit your Details, our Volunteer will pick & deliver the Food",
                R.drawable.lovefood, ContextCompat.getColor(getApplicationContext(), R.color.soda)));
        addSlide(AppIntroFragment.newInstance("We need You Volunteers !", "Join and show your Social Responsibility towards the Society",
                R.drawable.vol, ContextCompat.getColor(getApplicationContext(), R.color.hodors_sentence)));
    }


    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

}
