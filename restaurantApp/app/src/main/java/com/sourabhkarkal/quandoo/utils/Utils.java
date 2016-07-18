package com.sourabhkarkal.quandoo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.sourabhkarkal.quandoo.R;

/**
 * Created by sourabhkarkal on 14/07/16.
 */
public class Utils {

    public static void replaceFragment(FragmentActivity context, Fragment fragment) {
        String TAG = fragment.getClass().toString();
        String backStackName = fragment.getClass().getName();
        FragmentManager manager = context.getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped && context.getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final android.support.v4.app.FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            ft.replace(R.id.mainfragment, fragment, TAG);
            ft.addToBackStack(backStackName);
            ft.commit();
        }


    }


    /**
     * Checks and returns whether there is an Internet connectivity or not. This
     * would be useful to check the network connectivity before making a network
     * call.
     *
     * @return "True" -> is Connected , "False" -> if not.
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean isConnected = false;
        final ConnectivityManager connectivityService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityService != null) {
            final NetworkInfo networkInfo = connectivityService
                    .getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                isConnected = true;
            }
        }
        return isConnected;
    }

}
