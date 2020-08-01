
package com.madteam.beaksupang;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.madteam.beaksupang.InputScoreDialog.OnSendScoreCompleteListener;
import com.madteam.beaksupang.common.Preferences;
import com.madteam.beaksupang.data.Blocks;
import com.madteam.beaksupang.view.GameView;
import com.madteam.beaksupang.view.GameView.OnGameListener;

public class MainActivity extends Activity {

    private Blocks blocks;
    private GameView view;
    Preferences preference;
    private InputScoreDialog inputScoreDialog;
    int flag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preference = Preferences.getInstance(this);
        startGame();

        inputScoreDialog.setOnSendScoreCompleteListener(new OnSendScoreCompleteListener() {
            @Override
            public void onSendScoreComplete() {
                showScoreDialog();
            }
        });
    }

    public void showInputDialog() {
        handler.sendEmptyMessage(0);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            inputScoreDialog.show();
        }
    };

    public void showScoreDialog() {
        ScoreDialog scoreDialog = new ScoreDialog(this);
        scoreDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        scoreDialog.show();
    }

    private void startGame() {
        blocks = new Blocks();
        view = new GameView(this, blocks);
        inputScoreDialog = new InputScoreDialog(this);
        view.setOnGameListener(new OnGameListener() {
            @Override
            public void onReShuffle() {

            }

            @Override
            public void onLevelUp() {

            }

            @Override
            public void onFinish() {
                Log.i("evey", "startGame onFinish()-----");
                showInputDialog();
                // showScoreDialog();
            }
        });
        setContentView(view);
    }

    @Override
    protected void onDestroy() {
        GameView.setScore(0);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        showQuitDialog();
    }

    private void showQuitDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("게임 종료");
        alert.setMessage("종료하시겠습니까?");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

}
