package com.sourabhkarkal.quandoo.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.sourabhkarkal.quandoo.ApplicationController;
import com.sourabhkarkal.quandoo.realm.modal.RReservationDTO;
import com.sourabhkarkal.quandoo.utils.Utils;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * As per the business rule that after every 10 min the application should clear all reservation
 * I have called tha api again to check the reservation and update it in database.
 * this service is called in {@link com.sourabhkarkal.quandoo.activity.MainActivity}
 *
 * NOTE : THIS SERVICE CAN ALSO BE WRITTEN USING ANDROID JOB SCHEDULER
 *
 * Created by sourabhkarkal on 16/07/16.
 */
public class ReservationUpdateService extends Service implements iWebListener{

    ArrayList<RReservationDTO> rReservationDTOs;
    Handler handler;
    Runnable runnable;
    String tableUrl = "https://s3-eu-west-1.amazonaws.com/quandoo-assessment/table-map.json";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        rReservationDTOs = new ArrayList<>();
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {

                if(Utils.isNetworkAvailable(getApplicationContext())) {
                    TagRequest tagRequest = new TagRequest(102, tableUrl, ReservationUpdateService.this, ReservationUpdateService.this);
                    new RestTemplateExecutor().callServerApi(tagRequest);
                    Log.d("QUANDOO", "Service Started");
                }
                //Toast.makeText(ReservationUpdateService.this,"Service Started",Toast.LENGTH_SHORT).show();

                handler.postDelayed(runnable,10*60*1000); //10 min
            }
        };

        handler.post(runnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;

    }

    @Override
    public void onTaskComplete(int taskId, Object object, boolean isError) {
        if(taskId==102){
            if(isError){

            }else{
                try {
                    String[] strings = object.toString().replace("[","").replace("]","").split(",");

                    for (int i=0;i<strings.length;i++){
                        RReservationDTO rReservationDTO =  new RReservationDTO();
                        rReservationDTO.setTableNo(i+1);
                        rReservationDTO.setReserved(Boolean.parseBoolean(strings[i]));
                        rReservationDTOs.add(rReservationDTO);
                    }
                    Realm realm = Realm.getInstance(ApplicationController.getInstance().getRealmConfig());
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(rReservationDTOs); //updating values to DB
                    realm.commitTransaction();
                    //Toast.makeText(ReservationUpdateService.this,"Service Ended",Toast.LENGTH_SHORT).show();
                    Log.d("QUANDOO","Service Ended");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
