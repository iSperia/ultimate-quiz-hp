package com.tryandroid.hpquiz.presenter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.res.Resources;
import android.support.annotation.IntDef;

import com.tryandroid.hpquiz.QuizApplication;
import com.tryandroid.hpquiz.R;
import com.tryandroid.ux_common.BackKeyInterceptor;
import com.tryandroid.hpquiz.navigation.Router;
import com.tryandroid.ux_common.menu.MenuViewModel;

import java.util.ArrayList;
import java.util.List;

import icepick.State;

public class MenuViewModelImpl extends MenuViewModel {

    @IntDef({Mode.ShowRoot, Mode.ShowExam})
    public @interface Mode {
        int ShowRoot = 0;
        int ShowExam = 1;
    }

    private final Router router;

    private final int idxMenuExam;
    private final int idxMenuExamSov;

    private final List<String> menuItems = new ArrayList<>();
    private final List<String> menuExams = new ArrayList<>();

    @State
    @Mode
    int mode = Mode.ShowRoot;

    private MutableLiveData<List<String>> itemsLiveData = new MutableLiveData<>();

    public MenuViewModelImpl() {
        final Resources resources = QuizApplication.$().resources();
        router = QuizApplication.$().router();

        menuItems.add(resources.getString(R.string.menu_exams));
        idxMenuExam = 0;

        menuExams.add(resources.getString(R.string.menu_exams_sov));
        idxMenuExamSov = 0;

        emitItems();
    }

    @Override
    public void handleMenuItemClick(int menuItemPosition) {
        switch (mode) {
            case Mode.ShowRoot:
                handleRoot(menuItemPosition);
                break;
            case Mode.ShowExam:
                handleExam(menuItemPosition);
                break;
        }
    }

    @Override
    public boolean shouldInterceptBackKey() {
        switch (mode) {
            case Mode.ShowExam:
                setMode(Mode.ShowRoot);
                return true;
        }
        return false;
    }

    @Override
    public LiveData<List<String>> getItemsData() {
        return itemsLiveData;
    }

    private void emitItems() {
        switch (mode) {
            case Mode.ShowRoot:
                itemsLiveData.postValue(menuItems);
                break;
            case Mode.ShowExam:
                itemsLiveData.postValue(menuExams);
                break;
        }
    }

    private void handleExam(int index) {
        if (index == idxMenuExamSov) {
            router.startSovExams();
        }
    }

    private void handleRoot(int index) {
        if (index == idxMenuExam) {
            setMode(Mode.ShowExam);
        }
    }

    private void setMode(@Mode final int mode) {
        this.mode = mode;
        emitItems();
    }
}
