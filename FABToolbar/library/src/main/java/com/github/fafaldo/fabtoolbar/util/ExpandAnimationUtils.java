package com.github.fafaldo.fabtoolbar.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fafik on 2015-07-31.
 */
public class ExpandAnimationUtils {

    public static List<Animator> build(final ViewGroup view, int pivotX, int pivotY, float fraction, int duration, int delay) {
        List<Animator> animatorList = new ArrayList<>();

        for(int i = 0; i < view.getChildCount(); i++) {
            View childView = view.getChildAt(i);

            int deltaX = pivotX - (childView.getLeft() + childView.getWidth()/2);
            int deltaY = pivotY - (childView.getTop() + childView.getHeight()/2);

            ObjectAnimator xAnim = ObjectAnimator.ofFloat(childView, "translationX", fraction * deltaX, 0);
            ObjectAnimator yAnim = ObjectAnimator.ofFloat(childView, "translationY", fraction * deltaY, 0);

            xAnim.setDuration(duration);
            xAnim.setStartDelay(delay);
            yAnim.setDuration(duration);
            yAnim.setStartDelay(delay);

            animatorList.add(xAnim);
            animatorList.add(yAnim);
        }

        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        alphaAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }
        });

        alphaAnim.setDuration(duration);
        alphaAnim.setStartDelay(delay);

        animatorList.add(alphaAnim);

        return animatorList;
    }

    public static List<Animator> buildReversed(final ViewGroup view, int pivotX, int pivotY, float fraction, int duration, int delay) {
        List<Animator> animatorList = new ArrayList<>();

        for(int i = 0; i < view.getChildCount(); i++) {
            View childView = view.getChildAt(i);

            int deltaX = pivotX - (childView.getLeft() + childView.getWidth()/2);
            int deltaY = pivotY - (childView.getTop() + childView.getHeight()/2);

            ObjectAnimator xAnim = ObjectAnimator.ofFloat(childView, "translationX", 0, fraction * deltaX);
            ObjectAnimator yAnim = ObjectAnimator.ofFloat(childView, "translationY", 0, fraction * deltaY);

            xAnim.setDuration(duration);
            xAnim.setStartDelay(delay);
            yAnim.setDuration(duration);
            yAnim.setStartDelay(delay);

            animatorList.add(xAnim);
            animatorList.add(yAnim);
        }

        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        alphaAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
            }
        });

        alphaAnim.setDuration(duration);
        alphaAnim.setStartDelay(delay);

        animatorList.add(alphaAnim);

        return animatorList;
    }
}
