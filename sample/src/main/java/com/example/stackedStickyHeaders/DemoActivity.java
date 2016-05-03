package com.example.stackedStickyHeaders;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.jochen_schweizer.stackedStickyHeaders.DisplayMetricsUtils;
import de.jochen_schweizer.stackedStickyHeaders.StackedStickyHeadersView;

public class DemoActivity extends Activity {

    private StackedStickyHeadersView stackedStickyHeadersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        stackedStickyHeadersView = (StackedStickyHeadersView) findViewById(R.id.stackedStickyHeadersView);
        stackedStickyHeadersView.setAdapter(new ThreeHeadersAdapter());
        stackedStickyHeadersView.initStickyHeaders(new int[]{ThreeHeadersAdapter.VIEW_TYPE_YEAR, ThreeHeadersAdapter.VIEW_TYPE_MONTH},
                new int[]{ThreeHeadersAdapter.ITEM_HEIGHT_DP, ThreeHeadersAdapter.ITEM_HEIGHT_DP});
    }

    private class ThreeHeadersAdapter extends BaseAdapter {
        public static final int VIEW_TYPE_YEAR = 1;
        public static final int VIEW_TYPE_MONTH = 2;
        public static final int VIEW_TYPE_DAY = 3;

        public final static int ITEM_HEIGHT_DP = 32;
        public final int itemHeightPixels;

        private final String[] years = new String[]{"2011", "2012", "2013"};
        private final String[] months = new String[]{"January", "February", "March", "April"};
        private final String[] days = new String[]{"01", "02", "03", "04", "05"};

        private final List<Pair<Integer, String>> data;

        public ThreeHeadersAdapter() {
            DisplayMetricsUtils utils = new DisplayMetricsUtils(getResources());
            this.itemHeightPixels = (int) utils.getFloatPixelsFromDps(ThreeHeadersAdapter.ITEM_HEIGHT_DP);
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
                convertView = new TextView(parent.getContext());
                convertView.setBackgroundColor(Color.WHITE);
                ((TextView) convertView).setTextColor(Color.DKGRAY);
                convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeightPixels));
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
            return data.get(position).first == VIEW_TYPE_DAY;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            return data.get(position).first;
        }
    }
}
