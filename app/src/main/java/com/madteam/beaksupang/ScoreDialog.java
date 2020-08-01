
package com.madteam.beaksupang;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.madteam.beaksupang.adapter.ScoreListAdapter;
import com.madteam.beaksupang.common.Preferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScoreDialog {

    private ListView scoreListView;
    private Context context;
    private ScoreListAdapter scoreListAdapter;
    private TextView scoreTextView;

    private OnCancelListener cancelListener;

    public void setOnCancelListener(OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    public ScoreDialog(Context context) {
        this.context = context;
    }

    private Handler testHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (dialog == null || !dialog.isShowing()) {
                    getDialog(context).show();
                }
            } catch (Exception e) {
            }
        }

    };

    public void show() {
        testHandler.sendEmptyMessage(0);
        Log.i("evey", "ScoreDialog show()");
    }

    private Dialog dialog;

    private Dialog getDialog(final Context context) {

        dialog = new Dialog(context, R.style.full_screen_dialog) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                setContentView(R.layout.dialog_score);
                getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                scoreTextView = (TextView) findViewById(R.id.score_text_view);
                scoreListView = (ListView) findViewById(R.id.score_list_view);
                getBestScore();
            }

            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                }
                return super.onKeyDown(keyCode, event);
            }
        };
        dialog.setCancelable(true);
        if (cancelListener != null) {
            dialog.setOnCancelListener(cancelListener);
        }
        return dialog;
    }

    private void getBestScore() {
        String scoreData = Preferences.getInstance(context).getScoreData();
        Log.i("evey", "" + scoreData);
        if (scoreData != "") {
            try {
                JSONObject jsonObject = new JSONObject(scoreData);
                JSONArray dataArray = jsonObject.getJSONArray("data");
                ArrayList<JSONObject> dataArrayList = new ArrayList<JSONObject>();

                for (int i = 0; i < dataArray.length(); i++) {
                    Log.i("evey", "" + dataArray.getJSONObject(i).getString("username") + " " + dataArray.getJSONObject(i).getInt("score"));
                    dataArrayList.add(dataArray.getJSONObject(i));
                }

                scoreListAdapter = new ScoreListAdapter(context, dataArrayList);
                scoreListView.setAdapter(scoreListAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
