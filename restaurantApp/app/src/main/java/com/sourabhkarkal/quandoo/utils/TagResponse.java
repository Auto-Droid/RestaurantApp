package com.sourabhkarkal.quandoo.utils;

/**
 * Created by sourabhkarkal on 12/07/16.
 */
public class TagResponse {

    Object object;
    boolean isError;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
