package com.tryandroid.hpquiz.presenter.sov;

import android.os.Bundle;

import com.google.gson.Gson;

import java.util.Random;

/**
 * Created by iSperia on 30.04.2018.
 */

public class CloakTipState {

    public boolean isCloakUsed;

    public int idx1;

    public int idx2;

    public void hide(final int idxCorrect) {
        final Random random = new Random();
        int idxNotToHide = random.nextInt(3);
        if (idxNotToHide >= idxCorrect) idxNotToHide++;
        idx1 = 0;
        while (idx1 == idxCorrect || idx1 == idxNotToHide) idx1++;
        idx2 = 6 - idx1 - idxCorrect - idxNotToHide;
        isCloakUsed = true;
    }

    public void reset() {
        isCloakUsed = false;
    }

    public static class Bundler implements icepick.Bundler<CloakTipState> {

        @Override
        public void put(String s, CloakTipState cloakTipState, Bundle bundle) {
            bundle.putString(s, new Gson().toJson(cloakTipState));
        }

        @Override
        public CloakTipState get(String s, Bundle bundle) {
            return new Gson().fromJson(bundle.getString(s), CloakTipState.class);
        }
    }
}
