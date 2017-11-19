package com.example.android.reelmovies;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * THIS CLASS IS MEANT TO SETUP AND DISPLAY
 * THE TWO "Now Playing" AND "Upcoming" MOVIES
 * FRAGMENTS IN A SWIPPABLE TABBED VIEWPAGER
 */
public class TabsHost extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;


    public TabsHost(){
        //Required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get reference to inflate the UI layout for this movie fragment
        View rootView = inflater.inflate(R.layout.tabshost, container, false);



        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.container);
        setupViewPager(mViewPager);
        //mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

    private void setupViewPager(ViewPager viewPager){
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getFragmentManager());
        adapter.addFragment(new NowPlayingFragment(), "NOW PLAYING");
        adapter.addFragment(new UPCMoviesFragment(), "UPCOMING");
        viewPager.setAdapter(adapter);


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> myFragmentList = new ArrayList<>();
        private final List<String> myFragmentTitles = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment frag, String title){
            myFragmentList.add(frag);
            myFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {

            return myFragmentList.get(position);
        }

        @Override
        public int getCount() {
            //Show 2 pages
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return myFragmentTitles.get(position);
        }
    }



}
