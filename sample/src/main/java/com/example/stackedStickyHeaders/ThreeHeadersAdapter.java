package com.example.stackedStickyHeaders;

import android.graphics.Color;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.jochen_schweizer.stackedStickyHeaders.StackedStickyHeadersAdapter;

/**
 * Created by ganevlyu on 20/07/16.
 */

public class ThreeHeadersAdapter extends StackedStickyHeadersAdapter {
    private static final int VIEW_TYPE_YEAR = 0;
    private static final int VIEW_TYPE_MONTH = 1;
    private static final int VIEW_TYPE_DAY = 2;
    private static final int VIEW_TYPE_TIME_OF_DAY = 3;

    private final static int ITEM_HEIGHT_DP = 32;
    private final static int ITEM_HORIZONTAL_PADDING_DP = 16;

    private final String[] years = new String[]{"2011", "2012", "2013"};
    private final String[] months = new String[]{"January", "February", "March", "April"};
    private final String[] days = new String[]{"01", "02", "03", "04", "05"};
    private final String[] timeOfDays = new String[]{"morning", "afternoon", "evening", "night"};

    private final List<Pair<Integer, String>> data;

    public ThreeHeadersAdapter() {
        data = new ArrayList<>();
        initData(data);
    }

    private void initData(List<Pair<Integer, String>> data) {
        for (String year : years) {
            data.add(new Pair<>(VIEW_TYPE_YEAR, year));
            initMonths(data);
        }
    }

    private void initMonths(List<Pair<Integer, String>> data) {
        for (String month : months) {
            data.add(new Pair<>(VIEW_TYPE_MONTH, month));
            initDays(data);
        }
    }

    private void initDays(List<Pair<Integer, String>> data) {
        for (String day : days) {
            data.add(new Pair<>(VIEW_TYPE_DAY, day));
            initTimeOfDay(data);
        }
    }

    private void initTimeOfDay(List<Pair<Integer, String>> data) {
        for (String timeOfDay : timeOfDays) {
            data.add(new Pair<>(VIEW_TYPE_TIME_OF_DAY, timeOfDay));
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).second;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            TextView textView = new TextView(parent.getContext());
            textView.setTextColor(Color.DKGRAY);
            final int itemHeightPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    ITEM_HEIGHT_DP,
                    parent.getResources().getDisplayMetrics());
            final int itemHorizontalPaddingPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    ITEM_HORIZONTAL_PADDING_DP,
                    parent.getResources().getDisplayMetrics());
            textView.setPadding(itemHorizontalPaddingPixels, 0, itemHorizontalPaddingPixels, 0);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeightPixels));
            convertView = textView;
        }
        switch (getItemViewType(position)) {
            case VIEW_TYPE_YEAR:
                convertView.setBackgroundResource(R.drawable.year_background);
                break;
            case VIEW_TYPE_MONTH:
                convertView.setBackgroundResource(R.drawable.month_background);
                break;
            case VIEW_TYPE_DAY:
                convertView.setBackgroundResource(R.drawable.day_background);
                break;
            case VIEW_TYPE_TIME_OF_DAY:
                convertView.setBackgroundResource(R.drawable.time_of_day_background);
                break;
        }
        ((TextView) convertView).setText(data.get(position).second);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        // only days are non-sticky and clickable
        return data.get(position).first == VIEW_TYPE_TIME_OF_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).first;
    }

    @Override
    public int[] getHeadersViewTypeIds() {
        return new int[]{ThreeHeadersAdapter.VIEW_TYPE_YEAR, ThreeHeadersAdapter.VIEW_TYPE_MONTH, ThreeHeadersAdapter.VIEW_TYPE_DAY};
    }

    @Override
    public int[] getHeadersViewsHeights() {
        return new int[]{ThreeHeadersAdapter.ITEM_HEIGHT_DP, ThreeHeadersAdapter.ITEM_HEIGHT_DP, ThreeHeadersAdapter.ITEM_HEIGHT_DP};
    }
}
