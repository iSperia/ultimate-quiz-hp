package com.tryandroid.hpquiz.navigation;

import io.reactivex.annotations.Nullable;

public class RouterImpl implements Router {

    @Nullable
    private Router mainActivity;

    @Override
    public void startSovExams() {
        if (mainActivity != null) {
            mainActivity.startSovExams();
        }
    }

    public void setMainActivity(@Nullable final Router mainActivity) {
        this.mainActivity = mainActivity;
    }
}
