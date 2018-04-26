package com.tryandroid.ux_common.menu;

import android.arch.lifecycle.LiveData;

import com.tryandroid.ux_common.BackKeyInterceptor;
import com.tryandroid.ux_common.BaseViewModel;

import java.util.List;

public abstract class MenuViewModel extends BaseViewModel implements BackKeyInterceptor {

    public abstract LiveData<List<String>> getItemsData();

    public abstract void handleMenuItemClick(int menuItemPosition);
}
