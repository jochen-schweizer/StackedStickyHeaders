package de.jochen_schweizer.stackedStickyHeaders;

import android.content.res.Resources;
import android.util.TypedValue;

public class DisplayMetricsUtils {
    private final Resources resources;

    public DisplayMetricsUtils(Resources resources) {
        this.resources = resources;
    }

    public float getFloatPixelsFromDps(int dps) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dps,
                resources.getDisplayMetrics()
        );
    }
}
