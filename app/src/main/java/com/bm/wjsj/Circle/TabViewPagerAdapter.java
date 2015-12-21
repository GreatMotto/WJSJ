package com.bm.wjsj.Circle;

import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yangk on 2015/7/9.
 */
public class TabViewPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentsList;


    public TabViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentsList) {
        super(fm);
        this.fragmentsList = fragmentsList;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if(observer != null){
            super.unregisterDataSetObserver(observer);
        }
    }

    @Override
    public Fragment getItem(int position) {
        // return ArrFragment.newInstance(position);
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup arg0, int arg1) {
        return super.instantiateItem(arg0, arg1);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

}
