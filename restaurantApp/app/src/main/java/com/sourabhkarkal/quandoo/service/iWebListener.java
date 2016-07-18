package com.sourabhkarkal.quandoo.service;

/**
 * Created by sourabhkarkal on 12/07/16.
 */
public interface iWebListener {

    public void onTaskComplete(int taskId, Object object, boolean isError);
}
