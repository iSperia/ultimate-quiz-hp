package com.tryandroid.patternsapplication.singleton;

import android.content.Context;

public class SingletoneModule {

    private volatile static SingletoneModule instance;

    private int a = 10;

    private Context context;

    private SingletoneModule(Context context) {
    }

    public static SingletoneModule getInstance(Context context) {
        SingletoneModule localInstance = instance;
        if (localInstance == null) {
            synchronized (SingletoneModule.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SingletoneModule(context);
                }
            }
        }
        return localInstance;
    }

    public int getA() {
        return a;
    }
}
