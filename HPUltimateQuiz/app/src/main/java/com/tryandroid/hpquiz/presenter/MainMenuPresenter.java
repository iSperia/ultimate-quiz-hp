package com.tryandroid.hpquiz.presenter;

import android.content.res.Resources;
import android.os.Bundle;

import com.tryandroid.hpquiz.R;
import com.tryandroid.hpquiz.navigation.BackKeyInterceptor;
import com.tryandroid.hpquiz.navigation.NavigationContext;
import com.tryandroid.ux_common.menu.MenuPresenter;
import com.tryandroid.ux_common.menu.MenuView;

import java.util.ArrayList;
import java.util.List;

public class MainMenuPresenter implements MenuPresenter, BackKeyInterceptor {

    private static final String KEY_MODE = "mode";

    private enum Mode {
        ShowRoot,
        ShowExam
    }

    private final NavigationContext navigationContext;
    private final MenuView view;

    private final int idxMenuExam;
    private final int idxMenuExamSov;

    private final List<String> menuItems = new ArrayList<>();
    private final List<String> menuExams = new ArrayList<>();

    private Mode mode = Mode.ShowRoot;

    public MainMenuPresenter(final Resources resources, final MenuView view,
                             final NavigationContext navigationContext) {
        this.view = view;
        this.navigationContext = navigationContext;

        menuItems.add(resources.getString(R.string.menu_exams));
        idxMenuExam = 0;

        menuExams.add(resources.getString(R.string.menu_exams_sov));
        idxMenuExamSov = 0;
    }

    @Override
    public void onMenuItemClicked(int index) {
        switch (mode) {
            case ShowRoot:
                handleRoot(index);
                break;
            case ShowExam:
                handleExam(index);
                break;
        }
    }

    @Override
    public void save(Bundle state) {
        state.putString(KEY_MODE, mode.toString());
    }

    @Override
    public void hibernate() {
        navigationContext.unregisterBackKeyInterceptor(this);
    }

    @Override
    public void restore(Bundle state) {
        mode = Mode.valueOf(state.getString(KEY_MODE, Mode.ShowRoot.toString()));
        navigationContext.registerBackKeyInterceptor(this);
        showMenu();
    }

    @Override
    public void start() {
        navigationContext.registerBackKeyInterceptor(this);
        showMenu();
    }

    @Override
    public boolean shouldInterceptBackKey() {
        switch (mode) {
            case ShowExam:
                mode = Mode.ShowRoot;
                showMenu();
                return true;
        }
        return false;
    }

    private void showMenu() {
        switch (mode) {
            case ShowRoot:
                view.showMenu(menuItems);
                break;
            case ShowExam:
                view.showMenu(menuExams);
                break;
        }
    }

    private void handleExam(int index) {
        if (index == idxMenuExamSov) {
            navigationContext.startAction(NavigationContext.NavigationAction.StartExamSov);
        }

    }

    private void handleRoot(int index) {
        if (index == idxMenuExam) {
            mode = Mode.ShowExam;
            showMenu();
        }
    }

}
