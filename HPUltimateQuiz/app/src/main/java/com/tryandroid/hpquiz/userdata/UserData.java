package com.tryandroid.hpquiz.userdata;

/**
 * Flashes is in-game currency
 */
public interface UserData {

    int getFlashCount();

    void addFlash(int count);

    boolean spendFlash(int count);

    int getTipCount(TipType tipType);

    void addTip(TipType tipType, int count);

    boolean spendTip(TipType tipType);

}
