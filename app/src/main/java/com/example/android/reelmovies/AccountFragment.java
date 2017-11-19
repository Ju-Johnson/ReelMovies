package com.example.android.reelmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AccountFragment extends Fragment {


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.accountfragment, container, false);

        //Creating a list of text labels for user account selections
        String[] accountSelections = {"REWARDS", "PURCHASED TICKETS",
                "SAVED SHOWTIMES", "NOTIFICATIONS", "CREDIT CARD", "SETTINGS", "SIGN OUT"};

        //Get reference to list view in the account fragment UI layout
        //Set adapter that will load the user account options onto the simple list view layout
        ListView accountList = (ListView) rootView.findViewById(R.id.accountListView);
        ArrayAdapter myAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,
                accountSelections);
        accountList.setAdapter(myAdapter);

        //TODO: set listview layout onItemClick Listener to allow user to manipulate their account
        //based on their option selection

        // Inflate the layout for this fragment
        return rootView;
    }


}
