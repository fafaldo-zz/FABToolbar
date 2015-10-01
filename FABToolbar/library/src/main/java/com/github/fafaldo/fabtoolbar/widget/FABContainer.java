package com.github.fafaldo.fabtoolbar.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by fafik on 2015-08-01.
 */
public class FABContainer extends RelativeLayout {
    public FABContainer(Context context) {
        super(context);
        init();
    }

    public FABContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FABContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }
}
