package de.jochen_schweizer.stackedStickyHeaders;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class ContainerFrameLayout extends FrameLayout {

    public ContainerFrameLayout(Context context) {
        super(context);
        init();
    }

    public ContainerFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContainerFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ContainerFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        //TODO: implement me
    }
}
