package com.samset.intentservicesample;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MyActivity extends Activity {

    MyBroadcastReciver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(MyIntentService.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyBroadcastReciver();
        registerReceiver(receiver, filter);

        Button addButton = (Button) findViewById(R.id.sendRequest);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent msgIntent = new Intent(MyActivity.this, MyIntentService.class);

                msgIntent.putExtra(MyIntentService.REQUEST_STRING, "http://www.amazon.com");
                startService(msgIntent);

                msgIntent.putExtra(MyIntentService.REQUEST_STRING, "https://github.com/SamsetDev?tab=repositories");
                startService(msgIntent);

                msgIntent.putExtra(MyIntentService.REQUEST_STRING, "http://samsetdev.blogspot.in/");
                startService(msgIntent);
            }
        });
    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }
    class MyBroadcastReciver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {

            String responseString = intent.getStringExtra(MyIntentService.RESPONSE_STRING);
            String reponseMessage = intent.getStringExtra(MyIntentService.RESPONSE_MESSAGE);

            TextView myTextView = (TextView) findViewById(R.id.response);
            myTextView.setText(responseString);

            WebView myWebView = (WebView) findViewById(R.id.myWebView);
            myWebView.getSettings().setJavaScriptEnabled(true);
            try {
                myWebView.loadData(URLEncoder.encode(reponseMessage,"utf-8").replaceAll("\\+"," "), "text/html", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }
}