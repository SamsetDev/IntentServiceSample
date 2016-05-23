package com.samset.intentservicesample;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by weesync on 23/05/16.
 */
public class MyIntentService extends IntentService {

    public static final String REQUEST_STRING = "myRequest";
    public static final String RESPONSE_STRING = "myResponse";
    public static final String RESPONSE_MESSAGE = "myResponseMessage";

    private String URL = null;
    private static final int REGISTRATION_TIMEOUT = 3 * 1000;
    private static final int WAIT_TIMEOUT = 30 * 1000;
    public static final String PROCESS_RESPONSE = "com.samset.intentservicesample.intent.action.PROCESS_RESPONSE";

    private static OkHttpClient okHttpClient;
    private static String strdata;

    public MyIntentService() {
        super("MyService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String requestString = intent.getStringExtra(REQUEST_STRING);
        String responseString = requestString + " " + DateFormat.format("MM/dd/yy h:mmaa", System.currentTimeMillis());

        SystemClock.sleep(10000); // Wait 10 seconds
        Log.v("MyWebRequestService:", responseString);
        String output = getDataFromserver(requestString);
        broadcastData(responseString, output);


    }

    private void broadcastData(String data, String msg) {

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESPONSE_STRING, data);
        broadcastIntent.putExtra(RESPONSE_MESSAGE, msg);
        sendBroadcast(broadcastIntent);
    }

    public static String getDataFromserver(String url) {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        Response response = null;

        try {
            response = okHttpClient.newCall(request).execute();
            strdata = response.body().string();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return strdata;
    }

}
