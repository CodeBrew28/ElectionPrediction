package com.example.semalkan.electionprediction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by semalkan on 10/31/16.
 */

public class StateArrayAdapter extends ArrayAdapter<JSONObject> {

    HashMap<String, String> states = new HashMap<String, String>();

    Context AppContext;

    private static class ViewHolder {
        private TextView stateName;
        private TextView candidateName;
        private TextView winChance;

    }

    public StateArrayAdapter(Context context, int resource, List<JSONObject> objects) {
        super(context, resource, objects);
        AppContext = context;
        addingStates();
    }


    public void addingStates(){
        states.put("AL", "Alabama");
        states.put("AK", "Alaska");
        states.put("AR", "Arizona");
        states.put("AZ", "Arkansaw");
        states.put("CA", "California");
        states.put("CO", "Colordao");
        states.put("CT", "Conneticut");
        states.put("DE", "Delware");
        states.put("FL", "Florida");
        states.put("GA", "Geogria");
        states.put("HI", "Hawaii");
        states.put("ID", "Idaho");
        states.put("IL", "Illinois");
        states.put("IN", "Indiana");
        states.put("IA", "Iowa");
        states.put("KS", "Kansas");
        states.put("KY", "Kentucky");
        states.put("LA", "Louisiana");
        states.put("ME1", "Maine");
        states.put("ME2", "Maine");
        states.put("MD", "Maryland");
        states.put("MI", "Michigan");

        states.put("MN", "Minnesota");
        states.put("MS", "Mississippi");
        states.put("MO", "Missouri");
        states.put("MT", "Montana");
        states.put("NE1", "Nebraska");
        states.put("NE2", "Nebraska");
        states.put("NE3", "Nebraska");
        states.put("NV", "Nevada");
        states.put("NH", "New Hampshire");

        states.put("NJ", "New Jersey");
        states.put("NM", "New Mexico");
        states.put("NY", "New York");
        states.put("NC", "North Carolina");
        states.put("ND", "North Dakota");

        states.put("OH", "Ohio");
        states.put("OK", "Oklahoma");
        states.put("OR", "Oregon");
        states.put("PA", "Pennyslvania");
        states.put("RI", "Rhode Island");

        states.put("SC", "South Carolina");
        states.put("SD", "South Dakota");
        states.put("TN", "Tennessee");
        states.put("TX", "Texas");
        states.put("UT", "Utah");

        states.put("VT", "Vermont");
        states.put("VA", "Virginia");
        states.put("WA", "Washington");
        states.put("WV", "West Virginia");
        states.put("WI", "Wisconsin");
        states.put("WY", "Wyoming");

        states.put("DC", "Dist. Columbia");




    }

    public View getView(int pos, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

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
                if (states.get(item.getString("state")) != null){
                    viewHolder.stateName.setText(states.get(item.getString("state")));
                } else {
                    viewHolder.stateName.setText(item.getString("state"));
                }
                JSONObject latest = item.getJSONObject("latest");
                Iterator<String> candidates = latest.keys();

                //compares the win probabilities and picks teh highest one
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

                // Sets the color to red blue or white based off the party
                if (candidateParty.equals("R")) {
                    viewHolder.stateName.setBackgroundColor(0x22FF0000);
                    viewHolder.candidateName.setBackgroundColor(0x22FF0000);
                    viewHolder.winChance.setBackgroundColor(0x22FF0000);
                } else if (candidateParty.equals("D")) {
                    viewHolder.stateName.setBackgroundColor(0x220000FF);
                    viewHolder.candidateName.setBackgroundColor(0x220000FF);
                    viewHolder.winChance.setBackgroundColor(0x220000FF);
                }



                viewHolder.candidateName.setText(candidateParty + " - " + candidateName);
                viewHolder.winChance.setText("" +   ( (double) Math.round(highestWinProbability * 100) / 100) + "%");


                View.OnClickListener onClick = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent detailActivity = new Intent(AppContext, StateInformation.class);
                        Bundle stateData = new Bundle();
                        stateData.putString("candidate", viewHolder.candidateName.getText().toString());
                        stateData.putString("state", viewHolder.stateName.getText().toString());
                        stateData.putString("winChance", viewHolder.winChance.getText().toString());
                        detailActivity.putExtra("data", stateData);
                        AppContext.startActivity(detailActivity);

                    }
                };

                viewHolder.stateName.setOnClickListener(onClick);
                viewHolder.candidateName.setOnClickListener(onClick);
                viewHolder.winChance.setOnClickListener(onClick);

            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }
}
