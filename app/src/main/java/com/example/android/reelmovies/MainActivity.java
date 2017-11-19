package com.example.android.reelmovies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    //Tag for log messages
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Object used to control fragment UI layout, navigation swapping
    FragmentManager fm = getSupportFragmentManager();

    //Material Design Bottom Navigation declaration
    //Allows users to quickly switch between activities from a bottom panel
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        //Listener method that checks which requested activity was selected
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_movies:
                    //If selected, swaps the current fragment in the Main activity's FrameContainer
                    // with the movie fragment UI layout
                    setTitle("Now Playing");
                    fm.beginTransaction().replace(R.id.frameContainer, new NowPlayingFragment()).commit();
                    return true;

                case R.id.navigation_theaters:
                    //TODO: implement theater fragment activity that lists local theaters;
                    return true;

                case R.id.navigation_account:
                    //If selected, swaps the current fragment in the Main activity's FrameContainer
                    // with the account fragment UI layout
                    setTitle("Account");
                    fm.beginTransaction().replace(R.id.frameContainer, new AccountFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup and initialize first fragment to display from BottomNavigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Fragment initialFrag = new NowPlayingFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, initialFrag);
        fragmentTransaction.commit();


    }

}
