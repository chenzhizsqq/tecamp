package com.example.tecamp.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tecamp.Frag02;
import com.example.tecamp.Frag01;
import com.example.tecamp.Frag03;
import com.example.tecamp.Frag04;
import com.example.tecamp.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.tab_text_4
            ,R.string.tab_text_1
            , R.string.tab_text_2
            ,R.string.tab_text_3
    };

    private static final String TAG = "SectionsPagerAdapter";
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;


    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1);

        //Log.e(TAG, "getItem: begin");

        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new Frag04();
                break;
            case 1:
                fragment = new Frag01();
                break;
            case 2:
                fragment = new Frag02();
                break;
            case 3:
                fragment = new Frag03();
                break;

        }
        return fragment;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}