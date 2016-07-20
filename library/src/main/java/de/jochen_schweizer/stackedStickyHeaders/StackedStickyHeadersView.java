package de.jochen_schweizer.stackedStickyHeaders;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class StackedStickyHeadersView extends FrameLayout {

    private ListView listView;
    private StickyHeadersManager stickyHeadersManager;

    public StackedStickyHeadersView(Context context) {
        super(context);
        init();
    }

    public StackedStickyHeadersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StackedStickyHeadersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StackedStickyHeadersView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        listView = new ListView(getContext());
        listView.setLayoutParams(params);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        addView(listView);
        listView.setHorizontalScrollBarEnabled(false);
        listView.setVerticalScrollBarEnabled(false);

        stickyHeadersManager = new StickyHeadersManager(this);
        if (!isInEditMode()) {
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    stickyHeadersManager.onScroll(firstVisibleItem, visibleItemCount, totalItemCount);
                }
            });
        }
    }

    void removeHeaderViews() {
        if (getChildCount() > 1) {
            removeViews(1, getChildCount() - 1);
        }
    }

    void addHeaderView(View headerView, int heightInPixels) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, heightInPixels);
        addView(headerView, params);
    }

    ListAdapter getListAdapter() {
        return listView.getAdapter();
    }

    public ListView getListView() {
        return listView;
    }

    public void setAdapter(StackedStickyHeadersAdapter listAdapter) {
        listView.setAdapter(listAdapter);
        stickyHeadersManager.initHeaderViews(listAdapter.getHeadersViewTypeIds(), listAdapter.getHeadersViewsHeights());
    }
}
