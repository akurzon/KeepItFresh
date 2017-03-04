package com.csci448.freshapps.keepitfresh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Nate on 3/3/2017.
 */

public class AboutFragment extends Fragment {

    //private TextView mTitle;
    //private TextView mDescription;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        //mTitle = (TextView) view.findViewById(R.id.about_title);
        //mTitle = (TextView) view.findViewById(R.id.about_info);

        return view;
    }
}
