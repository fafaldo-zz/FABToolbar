package com.github.fafaldo.fabtoolbar.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.fafaldo.fabtoolbar.R;
import com.github.fafaldo.fabtoolbar.util.ExpandAnimationUtils;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by rtulaza on 2015-07-30.
 */
public class FABToolbarLayout extends RelativeLayout {

    // anim timing
    //
    // 0              1/4   1/3        1/2        2/3   3/4              1
    // |---------------|-----|----------|----------|-----|---------------|
    // |<------------------------------>|
    //            xAnim, yAnim
    // |<------------------->|
    //        drawable
    //                 |<------------------------------->|
    //                               sizeAnim
    //                                             |<------------------->|
    //                                                      expand
    //

    private int SHOW_ANIM_DURATION = 600;
    private int HIDE_ANIM_DURATION = 600;
    private int RIGHT_MARGIN = 100;
    private int BOTTOM_MARGIN = 100;

    private int pivotX = -1;
    private int pivotY = -1;
    private float fraction = 0.2f;

    private int fabId = -1;
    private int containerId = -1;
    private int toolbarId = -1;

    private View toolbarLayout;
    private ImageView fab;
    private TransitionDrawable fabDrawable;
    private Drawable fabNormalDrawable;
    private RelativeLayout fabContainer;

    private Point toolbarPos = new Point();
    private Point toolbarSize = new Point();
    private Point fabPos = new Point();
    private Point fabSize = new Point();

    private boolean isFab = true;
    private boolean isToolbar = false;
    private boolean isInit = true;

    private boolean fabDrawableAnimationEnabled = true;

    private AnimatorSet hideAnimSet;

    public FABToolbarLayout(Context context) {
        super(context);
    }

    public FABToolbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(attrs);
    }

    public FABToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(attrs);
    }

    private void parseAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FABToolbarLayout);

        SHOW_ANIM_DURATION = a.getInt(R.styleable.FABToolbarLayout_showDuration, SHOW_ANIM_DURATION);
        HIDE_ANIM_DURATION = a.getInt(R.styleable.FABToolbarLayout_hideDuration, HIDE_ANIM_DURATION);
        BOTTOM_MARGIN = a.getDimensionPixelSize(R.styleable.FABToolbarLayout_bottomMargin, BOTTOM_MARGIN);
        RIGHT_MARGIN = a.getDimensionPixelSize(R.styleable.FABToolbarLayout_rightMargin, RIGHT_MARGIN);
        pivotX = a.getDimensionPixelSize(R.styleable.FABToolbarLayout_fadeInPivotX, -1);
        pivotY = a.getDimensionPixelSize(R.styleable.FABToolbarLayout_fadeInPivotY, -1);
        fraction = a.getFloat(R.styleable.FABToolbarLayout_fadeInFraction, fraction);
        fabId = a.getResourceId(R.styleable.FABToolbarLayout_fabId, -1);
        containerId = a.getResourceId(R.styleable.FABToolbarLayout_containerId, -1);
        toolbarId = a.getResourceId(R.styleable.FABToolbarLayout_fabToolbarId, -1);
        fabDrawableAnimationEnabled = a.getBoolean(R.styleable.FABToolbarLayout_fabDrawableAnimationEnabled, true);

        a.recycle();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(!isInit) {
            return;
        }

        toolbarLayout = findViewById(toolbarId);
        if (toolbarLayout == null) {
            throw new IllegalStateException("You have to place a view with id = R.id.fabtoolbar_toolbar inside FABToolbarLayout");
        }

        fabContainer = (RelativeLayout) findViewById(containerId);
        if (fabContainer == null) {
            throw new IllegalStateException("You have to place a FABContainer view with id = R.id.fabtoolbar_container inside FABToolbarLayout");
        }

        fab = (ImageView) fabContainer.findViewById(fabId);
        if (fab == null) {
            throw new IllegalStateException("You have to place a FAB view with id = R.id.fabtoolbar_fab inside FABContainer");
        }

        fab.setVisibility(INVISIBLE);

        Drawable tempDrawable = fab.getDrawable();
        fabNormalDrawable = tempDrawable;
        if(fabDrawableAnimationEnabled) {
            TransitionDrawable transitionDrawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                transitionDrawable = new TransitionDrawable(new Drawable[]{tempDrawable, getResources().getDrawable(R.drawable.empty_drawable, getContext().getTheme())});
            } else {
                transitionDrawable = new TransitionDrawable(new Drawable[]{tempDrawable, getResources().getDrawable(R.drawable.empty_drawable)});
            }
            transitionDrawable.setCrossFadeEnabled(fabDrawableAnimationEnabled);
            fabDrawable = transitionDrawable;
            fab.setImageDrawable(transitionDrawable);
        }

        toolbarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (toolbarLayout.getWidth() != 0 || toolbarLayout.getHeight() != 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        toolbarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        toolbarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    int[] pos = new int[2];

                    toolbarSize.set(toolbarLayout.getWidth(), toolbarLayout.getHeight());
                    toolbarLayout.getLocationOnScreen(pos);
                    toolbarPos.set(pos[0], pos[1]);

                    int[] fabContainerPos = new int[2];
                    fabContainer.getLocationOnScreen(fabContainerPos);

                    RelativeLayout.LayoutParams fabParams = (RelativeLayout.LayoutParams) fab.getLayoutParams();

                    int distanceFromBottom = (toolbarSize.y - fab.getHeight())/2;
                    int marginToSet = BOTTOM_MARGIN - distanceFromBottom;

                    fabParams.rightMargin = RIGHT_MARGIN;
                    fab.setLayoutParams(fabParams);

                    fabContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            fabContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                            fab.setVisibility(VISIBLE);
                        }
                    });

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fabContainer.getLayoutParams();
                    layoutParams.height = toolbarSize.y;
                    layoutParams.bottomMargin = marginToSet;
                    fabContainer.setLayoutParams(layoutParams);

                    toolbarLayout.setVisibility(INVISIBLE);
                    toolbarLayout.setAlpha(0f);
                }
            }
        });

        fab.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (fab.getWidth() != 0 || fab.getHeight() != 0) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        fab.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        fab.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    fabSize.set(fab.getWidth(), fab.getHeight());

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fab.getLayoutParams();
                    layoutParams.addRule(ALIGN_PARENT_RIGHT);
                    layoutParams.addRule(CENTER_VERTICAL);
                    fab.setLayoutParams(layoutParams);
                }
            }
        });

        fab.setLayerType(LAYER_TYPE_SOFTWARE, null);

        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO better handling of fast clicks
                if(!isFab) {
                    return;
                }
                isFab = false;

                int[] fabP = new int[2];
                fab.getLocationOnScreen(fabP);
                fabPos.set(fabP[0], fabP[1]);

                List<Animator> animators = new ArrayList<>();
                List<Animator> reverseAnimators = new ArrayList<>();


                // TRANSLATION ANIM
                int xDest = toolbarPos.x + (toolbarSize.x - fabSize.x) / 2;
                int yDest = toolbarPos.y + (toolbarSize.y - fabSize.y) / 2;


                int[] fabConPos = new int[2];
                fabContainer.getLocationOnScreen(fabConPos);

                int xDelta = xDest - fabPos.x;
                int yDelta = toolbarPos.y - fabConPos[1];

                ObjectAnimator xAnim = ObjectAnimator.ofFloat(fab, "translationX", 0, xDelta);
                ObjectAnimator yAnim = ObjectAnimator.ofFloat(fabContainer, "translationY", 0, yDelta);

                xAnim.setInterpolator(new AccelerateInterpolator());
                yAnim.setInterpolator(new DecelerateInterpolator(3f));

                xAnim.setDuration(SHOW_ANIM_DURATION / 2);
                yAnim.setDuration(SHOW_ANIM_DURATION / 2);

                animators.add(xAnim);
                animators.add(yAnim);


                // REVERSE TRANSLATION ANIM
                ObjectAnimator xAnimR = ObjectAnimator.ofFloat(fab, "translationX", xDelta, 0);
                ObjectAnimator yAnimR = ObjectAnimator.ofFloat(fabContainer, "translationY", yDelta, 0);

                xAnimR.setInterpolator(new DecelerateInterpolator());
                yAnimR.setInterpolator(new AccelerateInterpolator());

                xAnimR.setDuration(HIDE_ANIM_DURATION / 2);
                yAnimR.setDuration(HIDE_ANIM_DURATION / 2);

                xAnimR.setStartDelay(HIDE_ANIM_DURATION / 2);
                yAnimR.setStartDelay(HIDE_ANIM_DURATION / 2);

                reverseAnimators.add(xAnimR);
                reverseAnimators.add(yAnimR);

                // DRAWABLE ANIM
                if (fabDrawable != null && fabDrawableAnimationEnabled) {
                    fabDrawable.startTransition(SHOW_ANIM_DURATION / 3);
                }
                if(!fabDrawableAnimationEnabled) {
                    fab.setImageDrawable(null);
                }


                // REVERSE DRAWABLE ANIM
                ValueAnimator drawableAnimR = ValueAnimator.ofFloat(0, 0);

                drawableAnimR.setDuration(2* HIDE_ANIM_DURATION /3);
                drawableAnimR.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(fabDrawableAnimationEnabled) {
                            fabDrawable.reverseTransition(HIDE_ANIM_DURATION / 3);
                        } else {
                            fab.setImageDrawable(fabNormalDrawable);
                        }
                    }
                });

                reverseAnimators.add(drawableAnimR);


                // SIZE ANIM
                // real size is 55x55 instead of 84x98
                final int startRadius = fabSize.x / 2;
                int finalRadius = (int)(Math.sqrt(Math.pow(toolbarSize.x, 2) + Math.pow(toolbarSize.y, 2))/2);
                int realRadius = (int)(98f * finalRadius / 55f);
                final ValueAnimator sizeAnim = ValueAnimator.ofFloat(startRadius, realRadius);
                sizeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float valFloat = (Float) valueAnimator.getAnimatedValue();

                        fab.setScaleX(valFloat / startRadius);
                        fab.setScaleY(valFloat / startRadius);
                    }
                });
                sizeAnim.setDuration(SHOW_ANIM_DURATION / 2);
                sizeAnim.setStartDelay(SHOW_ANIM_DURATION / 4);

                animators.add(sizeAnim);


                // REVERSE SIZE ANIM
                final ValueAnimator sizeAnimR = ValueAnimator.ofFloat(finalRadius, startRadius);
                sizeAnimR.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float valFloat = (Float) valueAnimator.getAnimatedValue();

                        fab.setScaleX(valFloat / startRadius);
                        fab.setScaleY(valFloat / startRadius);
                    }
                });
                sizeAnimR.setDuration(HIDE_ANIM_DURATION / 2);
                sizeAnimR.setStartDelay(HIDE_ANIM_DURATION / 4);

                reverseAnimators.add(sizeAnimR);


                // EXPAND AND SHOW MENU ANIM
                ViewGroup toolbarLayoutViewGroup = (ViewGroup) toolbarLayout;
                List<Animator> expandAnim = ExpandAnimationUtils.build(toolbarLayoutViewGroup, pivotX != -1 ? pivotX : toolbarLayout.getWidth() / 2, pivotY != -1 ? pivotY : toolbarLayout.getHeight() / 2, fraction, SHOW_ANIM_DURATION / 3, 2 * SHOW_ANIM_DURATION / 3);

                animators.addAll(expandAnim);


                // REVERSE EXPAND AND SHOW MENU ANIM
                List<Animator> expandAnimR = ExpandAnimationUtils.buildReversed(toolbarLayoutViewGroup, pivotX != -1 ? pivotX : toolbarLayout.getWidth()/2, pivotY != -1 ? pivotY : toolbarLayout.getHeight()/2, fraction, HIDE_ANIM_DURATION /3, 0);

                reverseAnimators.addAll(expandAnimR);


                // PLAY SHOW ANIMATION
                final AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animators);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isToolbar = true;
                    }
                });

                hideAnimSet = new AnimatorSet();
                hideAnimSet.playTogether(reverseAnimators);

                animatorSet.start();
            }
        });

        isInit = false;
    }

    public void show() {
        fab.callOnClick();
    }

    public void hide() {
        if(hideAnimSet != null && isToolbar) {
            hideAnimSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    isFab = true;
                }
            });
            hideAnimSet.start();
            isToolbar = false;
        }
    }

    public boolean isFab() {
        return isFab;
    }

    public boolean isToolbar() {
        return isToolbar;
    }
}
