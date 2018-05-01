package com.tryandroid.hpquiz.presenter.sov;

import android.os.Bundle;

import com.google.gson.Gson;

/**
 * Created by iSperia on 01.05.2018.
 */

public class StoneTipState {

    public boolean isProtected = false;

    public void startProtection() {
        isProtected = true;
    }

    public void reset() {
        isProtected = false;
    }

    public static class Bundler implements icepick.Bundler<StoneTipState> {

        @Override
        public void put(String s, StoneTipState cloakTipState, Bundle bundle) {
            bundle.putString(s, new Gson().toJson(cloakTipState));
        }

        @Override
        public StoneTipState get(String s, Bundle bundle) {
            return new Gson().fromJson(bundle.getString(s), StoneTipState.class);
        }
    }
}
