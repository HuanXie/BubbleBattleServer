package com.example.huan.bubblebattle.AnimationListener;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huan.bubblebattle.GamingActivity;
import com.example.huan.bubblebattle.Networking.WebSocketUtility;
import com.example.huan.bubblebattle.R;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

/**
 * Created by huan on 2016/1/2.
 */
public class BubbleAnimationListener implements Animator.AnimatorListener {
    private Activity activity;
    private ImageView bubble;
    private Set<ImageView> allBubbles;
    private boolean my;
    private int id;
    public BubbleAnimationListener(Activity activity, ImageView bubble, Set<ImageView> allBubbles, boolean my, int id) {
        this.activity = activity;
        this.bubble = bubble;
        this.allBubbles = allBubbles;
        this.my = my;
        this.id = id;
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        Log.d("animation", "onAnimationEnd");
        final RelativeLayout layout = (RelativeLayout) activity.findViewById(R.id.activity_gaming);
        layout.post(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (bubble == null) {
                            Log.d("bubble", "bubble already gone");
                        } else {
                            layout.removeView(bubble);
                            if(my)
                            {
                                //sendwinMessagetoServer();    //debug mode comment
                            }
                            allBubbles.remove(bubble); //remove it from the set so that we don't check the its collision anymore
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        Log.d("animation", "onAnimationCancel");
        final RelativeLayout layout = (RelativeLayout) activity.findViewById(R.id.activity_gaming);
        layout.post(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (bubble == null) {
                            Log.d("bubble", "bubble already gone");
                        } else {
                            layout.removeView(bubble);
                            allBubbles.remove(bubble); //remove it from the set so that we don't check the its collision anymore
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    private void sendwinMessagetoServer() {
        WebSocketClient client = WebSocketUtility.getClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "win");
            jsonObject.put("id", "" + id);
            client.send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
