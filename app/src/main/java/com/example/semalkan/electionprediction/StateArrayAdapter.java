package com.example.semalkan.electionprediction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by semalkan on 10/31/16.
 */

public class StateArrayAdapter extends ArrayAdapter<JSONObject> {




    private static class ViewHolder {
        private TextView stateName;
        private TextView candidateName;
        private TextView winChance;


    }

    public StateArrayAdapter(Context context, int resource, List<JSONObject> objects) {
        super(context, resource, objects);
    }

    public View getView(int pos, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.item_state, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.stateName = (TextView) convertView.findViewById(R.id.stateName);
            viewHolder.candidateName = (TextView) convertView.findViewById(R.id.candidateName);
            viewHolder.winChance = (TextView) convertView.findViewById(R.id.winChance);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JSONObject item = getItem(pos);
        String candidateParty = "No party";
        String candidateName = "No name";
        double highestWinProbability = -1;
        if (item != null) {
            try{
                viewHolder.stateName.setText(item.getString("state"));
                JSONObject latest = item.getJSONObject("latest");
                Iterator<String> candidates = latest.keys();
                while(candidates.hasNext()) {
                    String partyName = candidates.next();
                    JSONObject partyInfo = latest.getJSONObject(partyName);
                    double winProb = ((Number) partyInfo.getJSONObject("models").getJSONObject("plus").get("winprob")).doubleValue();
                    if(winProb > highestWinProbability) {
                        highestWinProbability = winProb;
                        candidateName = partyInfo.getString("candidate");
                        candidateParty = partyInfo.getString("party");
                    }
                }

                viewHolder.candidateName.setText(candidateParty + " - " + candidateName);
                viewHolder.winChance.setText("" + highestWinProbability + "%");
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }
}
