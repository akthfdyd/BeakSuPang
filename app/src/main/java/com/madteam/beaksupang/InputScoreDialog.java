
package com.madteam.beaksupang;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.madteam.beaksupang.common.Preferences;
import com.madteam.beaksupang.view.GameView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class InputScoreDialog {

    public interface OnSendScoreCompleteListener {
        void onSendScoreComplete();
    }

    private OnSendScoreCompleteListener onSendScoreCompleteListener;

    public void setOnSendScoreCompleteListener(
            OnSendScoreCompleteListener onSendScoreCompleteListener) {

        this.onSendScoreCompleteListener = onSendScoreCompleteListener;
    }

    private Context context;

    TextView title;
    EditText name;
    TextView score;
    Button sendButton;

    GameView gameView;
    int totalScore;

    public InputScoreDialog(Context context) {
        this.context = context;

    }

    private Handler InputScoreHandler = new Handler() {
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
        InputScoreHandler.sendEmptyMessage(0);
    }

    private Dialog dialog;

    Preferences preference;

    private Dialog getDialog(final Context context) {

        dialog = new Dialog(context, R.style.full_screen_dialog) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {

                super.onCreate(savedInstanceState);

                setContentView(R.layout.dialog_input_score);

                getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                title = (TextView) findViewById(R.id.input_dialog_title);
                name = (EditText) findViewById(R.id.input_dialog_name);
                score = (TextView) findViewById(R.id.input_dialog_score);
                sendButton = (Button) findViewById(R.id.input_dialog_send_button);

                score.setText("" + GameView.getTotalScore());

                Log.i("evey", "GameView.getTotalScore : " + GameView.getTotalScore());

                sendButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (name.getText().toString().length() <= 1) {
                            Toast.makeText(getContext(), "랭킹에 기록될 이름을 입력하세요.", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            String scoreData = Preferences.getInstance(context).getScoreData();
                            if (scoreData != "") {
                                try {
                                    JSONObject jsonObject = new JSONObject(scoreData);
                                    JSONArray dataArray = jsonObject.getJSONArray("data");

                                    JSONObject scoreObject = new JSONObject();
                                    scoreObject.put("username", name.getText().toString());
                                    scoreObject.put("score", GameView.getTotalScore());
                                    dataArray.put(scoreObject);

                                    ArrayList<JSONObject> dataArrayList = new ArrayList<JSONObject>();
                                    JSONArray sortedDataArray = new JSONArray();

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        dataArrayList.add(dataArray.getJSONObject(i));
                                    }

                                    Collections.sort(dataArrayList, new Comparator<JSONObject>() {
                                        @Override
                                        public int compare(JSONObject json1, JSONObject json2) {
                                            int val1 = 0;
                                            int val2 = 0;
                                            try {
                                                val1 = json1.getInt("score");
                                                val2 = json2.getInt("score");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            if (val1 > val2) {
                                                return -1;
                                            } else if (val1 < val2) {
                                                return 1;
                                            } else {
                                                return 0;
                                            }
                                        }
                                    });

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        sortedDataArray.put(dataArrayList.get(i));
                                    }

                                    jsonObject = new JSONObject();
                                    jsonObject.put("data", sortedDataArray);
                                    Preferences.getInstance(context).setScoreData(jsonObject.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                JSONObject jsonObject = new JSONObject();
                                JSONArray dataArray = new JSONArray();
                                try {
                                    JSONObject scoreObject = new JSONObject();
                                    scoreObject.put("username", name.getText().toString());
                                    scoreObject.put("score", GameView.getTotalScore());
                                    dataArray.put(scoreObject);
                                    jsonObject.put("data", dataArray);
                                    Preferences.getInstance(context).setScoreData(jsonObject.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            dialog.dismiss();
                        }
                    }
                });
                Log.i("evey", name.getText().toString());
            }

            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                }
                return super.onKeyDown(keyCode, event);
            }
        };
        dialog.setCancelable(true);
        return dialog;
    }
}
