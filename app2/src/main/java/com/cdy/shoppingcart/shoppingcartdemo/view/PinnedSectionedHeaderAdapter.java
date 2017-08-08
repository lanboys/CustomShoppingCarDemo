package com.cdy.shoppingcart.shoppingcartdemo.view;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 蓝兵 on 2017/8/8.
 */

public interface PinnedSectionedHeaderAdapter {

    boolean isSectionHeader(int position);

    int getSectionForPosition(int position);

    View getSectionHeaderView(int section, View convertView, ViewGroup parent);

    int getSectionHeaderViewType(int section);

    int getCount();
}