package com.sourabhkarkal.quandoo.activity;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sourabhkarkal.quandoo.R;
import com.sourabhkarkal.quandoo.fragment.CustomerListFragment;
import com.sourabhkarkal.quandoo.service.ReservationUpdateService;
import com.sourabhkarkal.quandoo.service.iWebListener;

public class MainActivity extends AppCompatActivity {

    private String TAG=MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        callMainFragment();

        startService(new Intent(this, ReservationUpdateService.class));


    }

    private void callMainFragment(){
        Fragment fragment = new CustomerListFragment();
        final android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainfragment, fragment, TAG);
        ft.commit();
    }


}
