package de.jochen_schweizer.stackedStickyHeaders;

import android.content.res.Resources;
import android.util.TypedValue;

class DisplayMetricsUtils {
    private final Resources resources;

    DisplayMetricsUtils(Resources resources) {
        this.resources = resources;
    }

    float getFloatPixelsFromDps(int dps) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dps,
                resources.getDisplayMetrics()
        );
    }
}
