package com.sourabhkarkal.quandoo.service;

import android.os.AsyncTask;
import android.util.Log;

import com.sourabhkarkal.quandoo.service.TagRequest;
import com.sourabhkarkal.quandoo.utils.TagResponse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * RestTemplateExecutor will handle server communication request
 * Created by sourabhkarkal on 12/07/16.
 */
public class RestTemplateExecutor {

    public void callServerApi(final TagRequest tagRequest) {

        new AsyncTask<Void, Void, TagResponse>() {
            @Override
            protected TagResponse doInBackground(Void... params) {
                TagResponse tagResponse = new TagResponse();
                HttpURLConnection urlConnection = null;
                try {

                    StringBuilder stringBuilder = new StringBuilder();
                    URL url = new URL(tagRequest.getUrl());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    tagResponse.setError(false);
                    tagResponse.setObject(stringBuilder);
                }catch (Exception e) {
                    Log.e("Exception ", e.getMessage());
                    tagResponse.setError(true);
                    tagResponse.setObject(e.getMessage());
                }
                finally {
                    if(urlConnection!=null)
                        urlConnection.disconnect();
                }

                return tagResponse;
            }

            @Override
            protected void onPostExecute(TagResponse tagResponse) {
                super.onPostExecute(tagResponse);
                tagRequest.getiWebListener().onTaskComplete(tagRequest.getTaskId(), tagResponse.getObject(), tagResponse.isError());
            }
        }.execute();


    }

}
