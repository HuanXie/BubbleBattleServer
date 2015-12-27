package com.example.huan.bubblebattle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.huan.bubblebattle.Networking.WebSocketUtility;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PersonalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MessageHandler {

    public final static String EXTRA_MESSAGE ="extraMessage";
    private boolean debug = true;
    private ImageButton[] gameTables = new ImageButton[16];
    public int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebSocketUtility.setMessageHandler(this); //set the handler so that this activity handleMessage() will be called
        setContentView(R.layout.activity_personal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addListenersForTableImages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        WebSocketUtility.setMessageHandler(this); //set the handler so that this activity handleMessage() will be called
    }

    private void updateTableStatus(JSONObject status) {
        //FIXME:
    }

    @Override
    public void handleMessage(String s) {
        try {
            JSONObject jsonObj = new JSONObject(s);
            switch (jsonObj.getString("action")) {
                case "allGameStatus":
                    updateTableStatus(jsonObj);
                    break;

                case "joinResponse":
                    if (jsonObj.getString("status").equals("success")) {
                        goToGamingActivity();
                    } else {
                        showToast("failed to join the game");
                    }
                    break;
                case "profileResponse":
                    JSONArray arrayOfInfos =jsonObj.getJSONArray("profiles");
                    StringBuilder msg = new StringBuilder();
                    for(int i= 0; i < arrayOfInfos.length(); i++)
                    {
                        JSONObject player = arrayOfInfos.getJSONObject(i);
                        String email = player.getString("email");
                        int totalGames = player.getInt("totalGames");
                        int wins = player.getInt("wins");
                        msg.append(email).append("&").append(totalGames).append("&").append(wins).append("&");
                    }
                    String extramessage = msg.toString();
                    gotoProfileActivity(extramessage);
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String s) {
        int duration = Toast.LENGTH_SHORT;
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, s, duration);
        toast.show();
    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private int id;
        public MyMenuItemClickListener(int id) {
            this.id = id;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            boolean handled = false;

            switch (item.getItemId()) {
                case R.id.table_join_game:
                    handled = true;
                    if(debug)
                    {
                        goToGamingActivity();
                    }
                    else
                    {
                        //send a message to the server
                        WebSocketClient client = WebSocketUtility.getClient();
                        JSONObject joinGame = new JSONObject();
                        try {
                            joinGame.put("action", "join");
                            joinGame.put("id", "" + id);
                            client.send(joinGame.toString());
                            showToast("sending " + joinGame.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.table_watch_game:
                    handled = true;
                    break;
                case R.id.table_show_profile:
                    sendTableInfotoServer(id);
                    break;
                default:
                    break;
            }
            return handled;
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        private int id = 0;
        public MyOnClickListener(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            //Create a popup menu
            PopupMenu popup = new PopupMenu(PersonalActivity.this, v);
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener(id));
            PersonalActivity.this.id = id;
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.table_popup, popup.getMenu());
            popup.show();
        }
    }

    private void addListenersForTableImages() {
        gameTables[0] = (ImageButton) findViewById(R.id.table0);
        gameTables[0].setOnClickListener(new MyOnClickListener(0));
        gameTables[1] = (ImageButton) findViewById(R.id.table1);
        gameTables[1].setOnClickListener(new MyOnClickListener(1));
        gameTables[2] = (ImageButton) findViewById(R.id.table2);
        gameTables[2].setOnClickListener(new MyOnClickListener(2));
        gameTables[3] = (ImageButton) findViewById(R.id.table3);
        gameTables[3].setOnClickListener(new MyOnClickListener(3));
        gameTables[4] = (ImageButton) findViewById(R.id.table4);
        gameTables[4].setOnClickListener(new MyOnClickListener(4));
        gameTables[5] = (ImageButton) findViewById(R.id.table5);
        gameTables[5].setOnClickListener(new MyOnClickListener(5));
        gameTables[6] = (ImageButton) findViewById(R.id.table6);
        gameTables[6].setOnClickListener(new MyOnClickListener(6));
        gameTables[7] = (ImageButton) findViewById(R.id.table7);
        gameTables[7].setOnClickListener(new MyOnClickListener(7));
        gameTables[8] = (ImageButton) findViewById(R.id.table8);
        gameTables[8].setOnClickListener(new MyOnClickListener(8));
        gameTables[9] = (ImageButton) findViewById(R.id.table9);
        gameTables[9].setOnClickListener(new MyOnClickListener(9));
        gameTables[10] = (ImageButton) findViewById(R.id.table10);
        gameTables[10].setOnClickListener(new MyOnClickListener(11));
        gameTables[11] = (ImageButton) findViewById(R.id.table11);
        gameTables[11].setOnClickListener(new MyOnClickListener(11));
        gameTables[12] = (ImageButton) findViewById(R.id.table12);
        gameTables[12].setOnClickListener(new MyOnClickListener(12));
        gameTables[13] = (ImageButton) findViewById(R.id.table13);
        gameTables[13].setOnClickListener(new MyOnClickListener(13));
        gameTables[14] = (ImageButton) findViewById(R.id.table14);
        gameTables[14].setOnClickListener(new MyOnClickListener(14));
        gameTables[15] = (ImageButton) findViewById(R.id.table15);
        gameTables[15].setOnClickListener(new MyOnClickListener(15));
    }

    private void goToGamingActivity() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, GamingActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        Log.d(PersonalActivity.class.toString(), "Jumping to gaming activity");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.personal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mypage) {
            GoToMyPageamingActivity();

        } else if (id == R.id.baggage) {

        } else if (id == R.id.market) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_help) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void GoToMyPageamingActivity() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, MyPageActivity.class);
        startActivity(intent);
        Log.d(PersonalActivity.class.toString(), "Jumping to MyPage activity");
    }

    private void gotoProfileActivity(String extraMessage) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_MESSAGE,extraMessage);
        startActivity(intent);
        Log.d(PersonalActivity.class.toString(), "Jumping to Profile activity");
    }

    private void sendTableInfotoServer(int id)
    {
        WebSocketClient client = WebSocketUtility.getClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "playerprofile");
            jsonObject.put("id", ""+id);
            client.send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
