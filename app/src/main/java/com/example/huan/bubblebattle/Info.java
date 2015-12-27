package com.example.huan.bubblebattle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;

public class Info {
    public String title;
    public String value;
    public Info (String title, String value )
    {
        this.title = title;
        this.value = value;
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    /*public static ArrayList<Info> fromJson(JSONArray jsonObjects) {
        ArrayList<Info> infos = new ArrayList<Info>();
        int max = 3*jsonObjects.length();
        for (int i = 0; i < max; i++) {
            try {
                infos.add(new Info(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return infos;
    }*/
}
