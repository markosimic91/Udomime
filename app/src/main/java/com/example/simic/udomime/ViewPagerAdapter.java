package com.example.simic.udomime;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simic on 20.10.2017..
 */

public class ViewPagerAdapter  extends FragmentPagerAdapter{

    List<Fragment> mFragmentList;
    List<String> mFragmentTitleList;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.mFragmentTitleList = new ArrayList<>();
        this.mFragmentList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return this.mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int positon){
        return this.mFragmentTitleList.get(positon);

    }

    @Override
    public int getCount() {
        return this.mFragmentList.size();
    }
    public void insertFragment(Fragment fragment,String title){
        this.mFragmentList.add(fragment);
        this.mFragmentTitleList.add(title);
    }

}
