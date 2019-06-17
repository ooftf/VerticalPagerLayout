package com.ooftf.vertical.nested;

import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/6/17 0017
 */
public class NestedScrollingParentHelperPro extends NestedScrollingParentHelper {
    /**
     * Construct a new helper for a given ViewGroup
     *
     * @param viewGroup
     */
    public NestedScrollingParentHelperPro(@NonNull ViewGroup viewGroup) {
        super(viewGroup);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        speedY = 0;
        lastTime = 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        super.onNestedScrollAccepted(child, target, axes, type);
        if(type == 0){
            speedY = 0;
            lastTime = 0;
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return super.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        super.onStopNestedScroll(target);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        super.onStopNestedScroll(target, type);
    }

    double speedY;
    long lastTime;

    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onStopNestedScroll(target, type);

    }


    public void onNestedPreScroll(@NonNull View target, int dx, int dy, int[] consumed, int type) {
        if(type ==0){
            long temp = System.currentTimeMillis();
            if (lastTime != 0) {
                speedY = (double) dy  / (temp - lastTime);
            }
            lastTime = temp;
        }

    }

    public double getSpeedY() {
        return speedY;
    }
}
