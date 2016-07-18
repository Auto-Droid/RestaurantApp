package com.sourabhkarkal.quandoo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sourabhkarkal.quandoo.service.ReservationUpdateService;

/**
 * This broadcast is called when the device is rebooted
 *
 * Created by sourabhkarkal on 18/07/16.
 */
public class StartReservationService  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // started reservation service
        context.startService(new Intent(context, ReservationUpdateService.class));
    }

}
