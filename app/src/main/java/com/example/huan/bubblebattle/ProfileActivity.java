package com.example.huan.bubblebattle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String message = intent.getStringExtra(PersonalActivity.EXTRA_MESSAGE);
        String[] infos = message.split("&");
        final ListView listview1 = (ListView) findViewById(R.id.listView1);
        final ListView listview2 = (ListView) findViewById(R.id.listView2);
        String[] titles = new String[] { "Email", "TotalGames", "Wins" };
        final ArrayList<Info> list1 = new ArrayList<Info>();
        for (int i = 0; i < titles.length; i++) {
            list1.add(new Info(titles[i],infos[i]));
        }
        if(infos.length > 3)
        {
            final ArrayList<Info> list2 = new ArrayList<Info>();
            for (int i = 0; i < titles.length; i++) {
                list1.add(new Info(titles[i],infos[i+3]));
            }
            InfoAdapter infosSecond = new InfoAdapter(this,list2);
            listview2.setAdapter(infosSecond);
        }




        InfoAdapter infosFirst = new InfoAdapter(this,list1);

        listview1.setAdapter(infosFirst);


    }
}
