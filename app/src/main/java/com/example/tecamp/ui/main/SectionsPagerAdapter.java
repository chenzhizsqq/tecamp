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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentTitle = new ArrayList<>();


    public void add(Fragment fragment,String title)
    {
        fragments.add(fragment);
        fragmentTitle.add(title);
    }

    private static final String TAG = "SectionsPagerAdapter";
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;


    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1);

        //Log.e(TAG, "getItem: begin");


        return fragments.get(position);

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }

    @Override
    public int getCount() {

        return fragments.size();
    }
}