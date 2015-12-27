package com.example.huan.bubblebattle.Gesture;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.huan.bubblebattle.R;

/**
 * Created by huan on 2016/1/2.
 */
public class BubbleBattleGestureDetector extends android.view.GestureDetector.SimpleOnGestureListener {
    private Activity activity;
    private View view; //the view that is associated with the gesture, e.g a button
    private final static float xVelocityThreshold = 0;

    public BubbleBattleGestureDetector(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
    }

    //if offset > 0, move to the right, otherwise left
    private void moveShooter(float end, float start) {
        float offset = end - start;
        if (offset > 0) {
            Log.d("onFling", "left to right " + offset);
        } else {
            Log.d("onFling", "right to left " + offset);
        }

        RelativeLayout layout = (RelativeLayout) activity.findViewById(R.id.activity_gaming);
        //Object animatedValue = animation.getAnimatedValue();
        //int newShooterX = (int)(float) animatedValue;

        int shooterX = (int) view.getX();
        int newShooterX = shooterX + (int)offset;

        //Limit the right boundary
        int max = layout.getWidth() - layout.getPaddingRight();
        if (newShooterX > max) {
            newShooterX = max;
        }

        //Limit the left boundary
        if (newShooterX < 0) {
            newShooterX = 0;
        }

        //make a copy of the layout params
        final RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(rlParams);
        Log.d("original layout", "leftMargin="+ newParams.leftMargin + ", top margin=" +newParams.topMargin);

        //Change its left margin so that it appears that it moves
        newParams.leftMargin = newShooterX;
        Log.d("new layout", "leftMargin="+ newParams.leftMargin + ", top margin=" +newParams.topMargin);
//                params.topMargin = ((RelativeLayout.LayoutParams)(shooterButton.getLayoutParams())).topMargin;
        view.setLayoutParams(newParams);
            /*ValueAnimator animation = ValueAnimator.ofFloat(start, end);
            animation.addUpdateListener(this);
            animation.setDuration(100);
            animation.start();*/
        //calculate the new position as a result of the swiping gesture

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityX) >= xVelocityThreshold) {
            //int offset = (int) (e2.getX() - e1.getX());
            moveShooter(e2.getX(), e1.getX());
            return true;
        }
        return false;
    }
}
