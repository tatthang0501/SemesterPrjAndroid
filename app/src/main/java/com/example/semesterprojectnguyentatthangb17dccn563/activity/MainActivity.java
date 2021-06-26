package com.example.semesterprojectnguyentatthangb17dccn563.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.semesterprojectnguyentatthangb17dccn563.R;
import com.example.semesterprojectnguyentatthangb17dccn563.adapter.FragmentNavigationAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView bnv;
    private FragmentNavigationAdapter fragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, StaticReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 45);
        cal.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        viewPager = findViewById(R.id.mainViewPager);
        bnv = findViewById(R.id.mainNavigation);
        fragmentAdapter = new FragmentNavigationAdapter(getSupportFragmentManager(), FragmentNavigationAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(fragmentAdapter);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.naviMoney:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.naviStatic:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.newsNavi:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.moreNavi:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bnv.getMenu().findItem(R.id.naviMoney).setChecked(true);
                        break;
                    case 1:
                        bnv.getMenu().findItem(R.id.naviStatic).setChecked(true);
                        break;
                    case 2:
                        bnv.getMenu().findItem(R.id.newsNavi).setChecked(true);
                        break;
                    case 3:
                        bnv.getMenu().findItem(R.id.moreNavi).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}