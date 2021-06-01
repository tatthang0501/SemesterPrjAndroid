package com.example.semesterprojectnguyentatthangb17dccn563.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.semesterprojectnguyentatthangb17dccn563.fragment.MoneyFragment;
import com.example.semesterprojectnguyentatthangb17dccn563.fragment.MoreFragment;
import com.example.semesterprojectnguyentatthangb17dccn563.fragment.NewFragment;

public class FragmentNavigationAdapter extends FragmentStatePagerAdapter {
    private int pageNum = 3;
    public FragmentNavigationAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new MoneyFragment();
            case 1: return new NewFragment();
            case 2: return new MoreFragment();
            default: return new MoneyFragment();
        }
    }

    @Override
    public int getCount() {
        return pageNum;
    }
}
