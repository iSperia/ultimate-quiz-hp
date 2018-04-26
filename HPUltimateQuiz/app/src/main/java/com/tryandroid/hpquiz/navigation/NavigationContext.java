package com.tryandroid.hpquiz.navigation;

public interface NavigationContext {

    enum NavigationAction {
        StartExamSov
    }

    void registerBackKeyInterceptor(BackKeyInterceptor interceptor);

    void unregisterBackKeyInterceptor(BackKeyInterceptor interceptor);

    void startAction(NavigationAction action);
}
