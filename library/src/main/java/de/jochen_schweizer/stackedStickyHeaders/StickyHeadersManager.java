package de.jochen_schweizer.stackedStickyHeaders;

import android.util.Pair;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StickyHeadersManager {
    private final StackedStickyHeadersView stackedStickyHeadersView;
    private final DisplayMetricsUtils displayMetricsUtils;
    private HashMap<Integer, View> headerTypeToHeaderView;
    private HashMap<Integer, Float> headerTypeToHeaderTopMargin;
    private HashMap<Integer, Float> headerTypeToHeaderHeight;
    private HashMap<Integer, Integer> headerTypeToHeaderPosition;
    private HashMap<Integer, Integer> headerTypeToPreviousHeaderAdapterPosition;
    private int[] headerTypes;

    public StickyHeadersManager(StackedStickyHeadersView stackedStickyHeadersView) {
        this.stackedStickyHeadersView = stackedStickyHeadersView;
        resetMappings();
        displayMetricsUtils = new DisplayMetricsUtils(stackedStickyHeadersView.getResources());
    }

    private void resetMappings() {
        this.headerTypeToHeaderView = new HashMap<>();
        this.headerTypeToHeaderTopMargin = new HashMap<>();
        this.headerTypeToHeaderHeight = new HashMap<>();
        this.headerTypeToHeaderPosition = new HashMap<>();
        this.headerTypeToPreviousHeaderAdapterPosition = new HashMap<>();
        this.headerTypes = new int[0];
    }

    public void initHeaderViews(int[] headersAdapterViewTypes, int[] headerViewsHeights) {
        // remove all previous sticky views
        stackedStickyHeadersView.removeHeaderViews();

        resetMappings();

        // init maps with new view types
        this.headerTypes = headersAdapterViewTypes;

        for (int i = 0; i < headersAdapterViewTypes.length; i++) {
            this.headerTypeToHeaderHeight.put(headersAdapterViewTypes[i], displayMetricsUtils.getFloatPixelsFromDps(headerViewsHeights[i]));
        }

        final ListAdapter listAdapter = stackedStickyHeadersView.getListAdapter();

        // Attach only if we have an non-empty adapter
        if (listAdapter.getCount() < headerTypes.length) {
            return;
        }

        // Init header views and top margins

        float topMargin = 0;
        for (int i = 0; i < headerTypes.length; i++) {
            final int headerType = headerTypes[i];

            this.headerTypeToHeaderPosition.put(headerType, i);
            this.headerTypeToHeaderTopMargin.put(headerType, topMargin);
            topMargin += headerTypeToHeaderHeight.get(headerType);

            // Get a View from the adapter for this header view
            for (int j = 0; j < listAdapter.getCount(); j++) {
                if (listAdapter.getItemViewType(j) == headerType) {
                    View v = listAdapter.getView(j, null, stackedStickyHeadersView.getListView());
                    this.headerTypeToHeaderView.put(headerType, v);
                    this.headerTypeToPreviousHeaderAdapterPosition.put(headerType, j);
                    break;
                }
            }
        }

        // Attach headers to layout
        // Attaching in reverse order because of Z order

        for (int i = headerTypes.length - 1; i >= 0; i--) {
            View headerView = headerTypeToHeaderView.get(headerTypes[i]);
            headerView.setClickable(true);
            stackedStickyHeadersView.addHeaderView(headerView, headerTypeToHeaderHeight.get(headerTypes[i]).intValue());
        }

        // Posts the reset of initial positions (posting makes sure it's done after layout has passed)
        stackedStickyHeadersView.post(new Runnable() {
            @Override
            public void run() {
                resetHeadersToStickyPositions();
            }
        });
    }

    public void onScroll(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        final ListAdapter listAdapter = stackedStickyHeadersView.getListAdapter();
        final ListView listView = stackedStickyHeadersView.getListView();

        // Currently there have to be at least as many views as the number of different headers
        if (totalItemCount <= headerTypes.length
                || visibleItemCount <= headerTypes.length
                || listAdapter == null
                || listAdapter.getCount() <= headerTypes.length) {
            return;
        }

        // Bind the header views, i.e. update their data from the adapter

        List<Pair<Integer, Integer>> viewsToBeBound = new ArrayList<>();
        for (int cellIndex = firstVisibleItem; cellIndex < firstVisibleItem + visibleItemCount; cellIndex++) {
            final View childView = listView.getChildAt(cellIndex - firstVisibleItem);
            final int viewType = listAdapter.getItemViewType(cellIndex);

            // We can stop when we reach views which are below where the header views stick
            if (childView.getY() > getLastStickyHeaderDefaultBottom()) {
                break;
            }

            // We can skip views which are not header views
            if (!headerTypeToHeaderPosition.containsKey(viewType)) {
                continue;
            }

            // We want to exchange the content, only if the scrolling view is completely covered by the
            // sticky header view
            if (childView.getY() - headerTypeToHeaderTopMargin.get(viewType) <= 0) {
                viewsToBeBound.add(new Pair<>(viewType, cellIndex));
            }
        }

        if (viewsToBeBound.isEmpty()) {
            // This happens when we scroll down. We need to just take the already invisible headers
            // content which are to be found before the currently first visible view
            for (int headerType : headerTypes) {
                bindHeaderView(firstVisibleItem, headerType);
            }
        } else {
            // We bind only views which need to be bound
            for (Pair<Integer, Integer> headerInNeed : viewsToBeBound) {
                bindHeaderView(headerInNeed.second, headerInNeed.first);
            }
        }

        boolean shouldResetHeadersPositions = true;

        // Adjust the sticky headers positions to like like the new sticky headers are pushing them up
        loopLabel:
        for (int cellIndex = firstVisibleItem; cellIndex < firstVisibleItem + visibleItemCount; cellIndex++) {
            View childView = listView.getChildAt(cellIndex - firstVisibleItem);

            int itemViewType = listAdapter.getItemViewType(cellIndex);
            final float totalHeight = getLastStickyHeaderDefaultBottom();


            if (headerTypeToHeaderPosition.containsKey(itemViewType)) {

                int pos = headerTypeToHeaderPosition.get(itemViewType);

                if (childView.getY() >= headerTypeToHeaderTopMargin.get(itemViewType) && childView.getY() - totalHeight <= 0) {
                    // If the new header is entering the area of the bottommost header

                    // We adjust the current header and all sticky below it
                    for (int i = pos, j = headerTypes.length - 1 - pos; i < headerTypes.length; i++, j--) {
                        headerTypeToHeaderView.get(headerTypes[i]).setY(childView.getY() - getStickyHeaderDefaultBottom(headerTypes[j]));
                        shouldResetHeadersPositions = false;
                    }
                    break loopLabel;
                }
            }
        }

        if (shouldResetHeadersPositions) {
            // Reset the headers positions to the default margins
            resetHeadersToStickyPositions();
        } else {
        }
    }

    // Helper methods

    private void resetHeadersToStickyPositions() {
        for (int headerType : headerTypes) {
            headerTypeToHeaderView.get(headerType).setY(headerTypeToHeaderTopMargin.get(headerType));
        }
    }

    private float getLastStickyHeaderDefaultBottom() {
        return getStickyHeaderDefaultBottom(headerTypes[headerTypes.length - 1]);
    }

    private float getStickyHeaderDefaultBottom(int headerType) {
        return headerTypeToHeaderTopMargin.get(headerType) + headerTypeToHeaderView.get(headerType).getHeight();
    }

    private void bindHeaderView(int adapterIndex, int headerType) {
        int previousAdapterIndex = -1;

        // Look for a previous header
        for (int i = adapterIndex; i >= 0; i--) {
            if (stackedStickyHeadersView.getListAdapter().getItemViewType(i) == headerType) {
                previousAdapterIndex = i;
                break;
            }
        }

        // If a previous header is found and we actually need to rebind, bind with its data
        if (previousAdapterIndex >= 0 && previousAdapterIndex != headerTypeToPreviousHeaderAdapterPosition.get(headerType)) {
            headerTypeToPreviousHeaderAdapterPosition.put(headerType, previousAdapterIndex);
            stackedStickyHeadersView.getListAdapter().getView(previousAdapterIndex, headerTypeToHeaderView.get(headerType), stackedStickyHeadersView.getListView());
        }
    }
}
