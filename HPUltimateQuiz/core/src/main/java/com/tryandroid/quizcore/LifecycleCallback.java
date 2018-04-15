package com.tryandroid.quizcore;

import android.os.Bundle;

public interface LifecycleCallback {

    void save(final Bundle state);

    void restore(final Bundle state);
}
