
package com.madteam.beaksupang.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.madteam.beaksupang.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class ScoreListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int layoutId;

    private JSONObject entity;

    private ArrayList<JSONObject> entities;

    public ScoreListAdapter(Context context, ArrayList<JSONObject> entities) {
        this.entities = entities;
        this.layoutId = R.layout.score_list_item;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return entities.size() < 10 ? entities.size() : 10;
    }

    @Override
    public JSONObject getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, null, true);
            convertView.setTag(getHolder(convertView));
        }
        setContent(convertView, position);
        return convertView;
    }

    private void setContent(View view, int position) {
        ViewHolder holder = (ViewHolder) view.getTag();

        entity = entities.get(position);

        try {
            Log.i("evey", entity.get("username").toString());
            holder.number.setText(position + 1 + ".");
            holder.name.setText(entity.getString("username"));
            holder.score.setText("" + entity.getInt("score") + "만원");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ViewHolder getHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.name = (TextView) view.findViewById(R.id.name);
        holder.score = (TextView) view.findViewById(R.id.score);
        holder.number = (TextView) view.findViewById(R.id.rank_number);
        return holder;
    }

    class ViewHolder {
        TextView number;
        TextView name;
        TextView score;
    }
}
