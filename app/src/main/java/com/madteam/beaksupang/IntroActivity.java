
package com.madteam.beaksupang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        delayThread();
    }

    private void delayThread() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1500);
                    startActivity(new Intent(IntroActivity.this, MenuActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
