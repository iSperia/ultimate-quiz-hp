package com.tryandroid.ux_common.menu;

import com.tryandroid.ux_common.LifecycleCallback;

public interface MenuPresenter extends LifecycleCallback {

    void onMenuItemClicked(final int index);
}
