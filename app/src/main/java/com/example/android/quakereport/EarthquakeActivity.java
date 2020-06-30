/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";
    private SpecificationAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
       // ArrayList<Specification> earthquakes = new ArrayList<>();
        /*earthquakes.add(new Specification("7.2","San Francisco","Jun 25,2020"));
        earthquakes.add(new Specification("6.1","London","July 20,2015"));
        earthquakes.add(new Specification("3.9","Tokyo","Nov 10,2014"));
        earthquakes.add(new Specification("5.4","Mexico City","May 3,2014"));
        earthquakes.add(new Specification("2.8","Moscow","Jan 31,2013"));
        earthquakes.add(new Specification("4.9","Rio de janerio","Aug 19,2012"));
        earthquakes.add(new Specification("1.6","Paris","Oct 30,2011"));*/
        mAdapter=new SpecificationAdapter(this,new ArrayList<Specification>());
        ListView earthquakeListView= (ListView) findViewById(R.id.list);



        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Specification quake=mAdapter.getItem(i);

                Uri earthquakeUri  =Uri.parse(quake.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW,earthquakeUri);
                startActivity(intent);
            }
        });
        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);


    }
    private class EarthquakeAsyncTask extends AsyncTask<String,Void,List<Specification>>{


        @Override
        protected List<Specification> doInBackground(String... strings) {
            if(strings.length<1 || strings[0] == null)
                return null;
            List<Specification> result = QueryUtils.fetchEarthquakeData(strings[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Specification> specifications) {
            mAdapter.clear();

            if(specifications != null && !specifications.isEmpty())
                mAdapter.addAll(specifications);
        }
    }
}
