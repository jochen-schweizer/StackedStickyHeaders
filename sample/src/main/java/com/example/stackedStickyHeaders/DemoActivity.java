package com.example.stackedStickyHeaders;

import android.app.Activity;
import android.os.Bundle;

import de.jochen_schweizer.stackedStickyHeaders.StackedStickyHeadersView;

public class DemoActivity extends Activity {

    private StackedStickyHeadersView stackedStickyHeadersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        stackedStickyHeadersView = (StackedStickyHeadersView) findViewById(R.id.stackedStickyHeadersView);
        stackedStickyHeadersView.setAdapter(new ThreeHeadersAdapter());
    }
}
