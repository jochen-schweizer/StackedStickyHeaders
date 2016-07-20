[![build status](https://travis-ci.org/jochen-schweizer/StackedStickyHeaders.png)](https://travis-ci.org/jochen-schweizer/StackedStickyHeaders) [![license](https://img.shields.io/github/license/mashape/apistatus.svg?maxAge=2592000)](https://www.tldrlegal.com/l/mit)
StackedStickyHeaders
====================

An android library for multiple hierarchically related section headers that stick to the top of a ListView.

![Logo](sample_preview.gif)

### Usage

1. Add the StackedStickyHeadersView either to an XML layout file, or programmatically.
2. Extend a StackedStickyHeadersAdapter and set it as an adapter of the view by calling StackedStickyHeadersView.setAdapter(StackedStickyHeadersAdapter listAdapter)
3. Make sure to implement the getHeadersViewTypeIds() and getHeadersViewsHeights() methods of the StackedStickyHeadersAdapter
4. Use StackedStickyHeadersView.getListView() to access the internal ListView for setting click listeners for items

### Possible future improvements
- remove the explicit height of the sticky headers and calculate it dynamically on the go
- support for RecyclerView
- click listeners for the sticky headers
