package com.example.semalkan.electionprediction;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context AppContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppContext = this;

        JSONParse p = new JSONParse();
        p.execute();
    }

    private class JSONParse extends AsyncTask<String, String, List<JSONObject>> {

        private TextView textView;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected List<JSONObject> doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            List<JSONObject> json = jParser.getJSONFromUrl("http://projects.fivethirtyeight.com/2016-election-forecast/summary.json");
            Log.i("MainActivity", json.toString());
            return json;
        }

        @Override
        protected void onPostExecute(List<JSONObject> json) {

            StateArrayAdapter adapter = new StateArrayAdapter(AppContext, R.layout.activity_main, json);

            ListView listView = (ListView) findViewById(R.id.states);
            listView.setAdapter(adapter);
        }


        }
    }


