package com.sourabhkarkal.quandoo.service;

import android.app.Service;
import android.content.Context;


/**
 * Created by sourabhkarkal on 12/07/16.
 */
public class TagRequest {

    private int taskId;
    private iWebListener iWebListener;
    private String url;
    private Context context;
    private Object data;
    private Service service;


    public TagRequest() {

    }

    public TagRequest(int taskId, String url, Context context, iWebListener iWebListener) {
        this.taskId = taskId;
        this.url = url;
        this.context=context;
        this.iWebListener = iWebListener;
    }


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public iWebListener getiWebListener() {
        return iWebListener;
    }

    public void setiWebListener(iWebListener iWebListener) {
        this.iWebListener = iWebListener;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

}
