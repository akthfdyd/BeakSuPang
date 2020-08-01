/**
 * 
 */

package com.madteam.beaksupang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author user 2013. 12. 12.
 */
public class MenuActivity extends Activity {

    private Button startButton;
    private Button rankButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initLayout();
        initListener();
    }

    private void initLayout() {
        startButton = (Button) findViewById(R.id.btn_activity_menu_start);
        rankButton = (Button) findViewById(R.id.btn_activity_menu_rank);
    }

    private void initListener() {
        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        rankButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showScoreDialog();
            }
        });
    }

    private void showScoreDialog() {
        ScoreDialog scoreDialog = new ScoreDialog(this);
        scoreDialog.show();
    }

}
