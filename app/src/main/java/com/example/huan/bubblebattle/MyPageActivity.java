package com.example.huan.bubblebattle;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;

public class MyPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView listview = (ListView) findViewById(R.id.listView);
        String[] titles = new String[] { "ID", "Totalscore", "Wins" };
        final ArrayList<Info> list = new ArrayList<Info>();
        for (int i = 0; i < titles.length; i++) {
            list.add(new Info(titles[i], "0"));
        }

        /*ArrayList<Info> arrayOfinfos = new ArrayList<Info>();
        InfoAdapter adapter = new InfoAdapter(this, arrayOfinfos);
        JSONArray jsonArray = ...;
        arrayOfinfos = Info.fromJson(jsonArray);
        adapter.addAll(arrayOfinfos);
        listview.setAdapter(adapter);*/

        InfoAdapter infosAd = new InfoAdapter(this,list);
        listview.setAdapter(infosAd);

    }

}
