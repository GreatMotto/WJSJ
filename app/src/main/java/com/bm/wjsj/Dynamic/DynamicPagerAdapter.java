package com.bm.wjsj.Dynamic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/19.
 */
public class DynamicPagerAdapter extends FragmentPagerAdapter{
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    public DynamicPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public DynamicPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
