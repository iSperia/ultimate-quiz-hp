package com.tryandroid.ux_common;

import android.os.Bundle;

public interface LifecycleCallback {

    void hibernate();

    void save(final Bundle state);

    void restore(final Bundle state);

    void start();
}
