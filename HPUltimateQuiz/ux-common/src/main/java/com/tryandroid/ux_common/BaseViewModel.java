package com.tryandroid.ux_common;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

import icepick.Icepick;
import io.reactivex.annotations.Nullable;

public abstract class BaseViewModel extends ViewModel {

    public final void persist(final Bundle state) {
        Icepick.saveInstanceState(this, state);
    }

    public final void restore(@Nullable final Bundle state) {
        if (state != null) {
            Icepick.restoreInstanceState(this, state);
        }
    }

    public boolean shouldInterceptBackKey() {
        return false;
    }

}
