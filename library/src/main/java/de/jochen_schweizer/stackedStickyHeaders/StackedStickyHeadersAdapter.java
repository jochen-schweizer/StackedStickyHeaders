package de.jochen_schweizer.stackedStickyHeaders;

import android.widget.BaseAdapter;

/**
 * Created by ganevlyu on 20/07/16.
 */

public abstract class StackedStickyHeadersAdapter extends BaseAdapter {

    public abstract int[] getHeadersViewTypeIds();

    public abstract int[] getHeadersViewsHeights();
}
