package com.tryandroid.hpquiz.userdata;

import com.tryandroid.hpquiz.preferences.ApplicationPreferences;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Implementation for user data that should be extended with FIREBASE downSync in later
 */
public class UserDataImpl implements UserData {

    private static final String KEY_FLASH = "flash";
    private static final String KEY_FLASH_DELTA = "flash.delta";

    private static final String IS_START_TIPS_GIVEN = "tips.given";

    private ApplicationPreferences prefs;

    public UserDataImpl(final ApplicationPreferences prefs) {
        this.prefs = prefs;
        if (prefs.getCachedInt(IS_START_TIPS_GIVEN, 0) == 0) {
            addTip(TipType.Wand, 5);
            addTip(TipType.Cloak, 5);
            addTip(TipType.Stone, 5);
            prefs.setCache(IS_START_TIPS_GIVEN, 1);
        }
    }

    @Override
    public int getFlashCount() {
        return prefs.getCachedInt(KEY_FLASH, 0)
                + prefs.getCachedInt(KEY_FLASH_DELTA, 0);
    }

    @Override
    public void addFlash(int count) {
        prefs.setCache(KEY_FLASH_DELTA,
                prefs.getCachedInt(KEY_FLASH_DELTA, 0) + count);
        applyOperationsAfterSync();
    }

    @Override
    public boolean spendFlash(int count) {
        final int nowDelta = prefs.getCachedInt(KEY_FLASH_DELTA, 0);
        final int nowValue = nowDelta + prefs.getCachedInt(KEY_FLASH, 0);
        if (nowValue < count) {
            return false;
        }
        prefs.setCache(KEY_FLASH_DELTA, nowDelta - count);
        applyOperationsAfterSync();
        return true;
    }

    @Override
    public int getTipCount(TipType tipType) {
        return prefs.getCachedInt(tipType.toString(), 0)
                + prefs.getCachedInt(tipType.toString().concat(".delta"), 0);
    }

    @Override
    public void addTip(TipType tipType, int count) {
        prefs.setCache(tipType.toString().concat(".delta"), count +
                        prefs.getCachedInt(tipType.toString().concat(".delta"), 0));
        applyOperationsAfterSync();
    }

    @Override
    public boolean spendTip(TipType tipType) {
        final int nowDelta = prefs.getCachedInt(tipType.toString().concat(".delta"), 0);
        final int nowValue = nowDelta + prefs.getCachedInt(tipType.toString(), 0);
        if (nowValue <= 0) {
            return false;
        }
        prefs.setCache(tipType.toString().concat(".delta"), nowDelta - 1);
        applyOperationsAfterSync();
        return true;
    }

    private void applyOperationsAfterSync() {
        downSync().subscribeOn(Schedulers.io()).andThen(upSync()).subscribe();
    }

    private Completable downSync() {
        return Completable.complete();
    }

    private Completable upSync() {
        return Completable.fromAction(() -> {
            final int nowFlashes = prefs.getCachedInt(KEY_FLASH, 0);
            final int nowFlashesDelta = prefs.getCachedInt(KEY_FLASH_DELTA, 0);
            prefs.setCache(KEY_FLASH, nowFlashes + nowFlashesDelta);
            prefs.setCache(KEY_FLASH_DELTA, 0);
        }).andThen(
                Observable.fromArray(TipType.values())
                        .doOnNext(tipType -> {
                            final String keyDelta = tipType.toString().concat(".delta");
                            final int nowTips = prefs.getCachedInt(tipType.toString(), 0);
                            final int deltaTips = prefs.getCachedInt(keyDelta, 0);
                            prefs.setCache(tipType.toString(), deltaTips + nowTips);
                            prefs.setCache(tipType.toString().concat(".delta"), 0);
                        }).ignoreElements());
    }
}
