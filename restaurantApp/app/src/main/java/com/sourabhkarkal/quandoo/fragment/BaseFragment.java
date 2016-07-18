package com.sourabhkarkal.quandoo.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.View;

import com.sourabhkarkal.quandoo.service.iWebListener;

/**
 * Created by sourabhkarkal on 13/07/16.
 */
public abstract class BaseFragment  extends Fragment implements View.OnClickListener,iWebListener {

    String customerUrl = "https://s3-eu-west-1.amazonaws.com/quandoo-assessment/customer-list.json";
    String tableUrl = "https://s3-eu-west-1.amazonaws.com/quandoo-assessment/table-map.json";

    @Override
    public void onTaskComplete(int taskId, Object object, boolean isError) {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * Use this method to show dialog box where user able to write event on both button positive or negative
     *
     * @param title     Show dialog box title
     * @param msg       dialog box message content
     * @param btn1      Button Object that indicate positiveButton click
     * @param btn2      Button Object that indicate Negative button click
     * @param listener1 listener1 listen btn1 click
     * @param listener2 listener2 listen btn2 click
     * @return AlertDialog Object , we need to show "show()" to show dialogbox
     */
    public static void showDialog(Context context,String title, String msg,
                                  String btn1, String btn2,
                                  DialogInterface.OnClickListener listener1,
                                  DialogInterface.OnClickListener listener2) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg).setCancelable(true)
                .setPositiveButton(btn1, listener1);
        if (btn2 != null)
            builder.setNegativeButton(btn2, listener2);

        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }

}
