package com.tryandroid.hpquiz;

import android.content.res.Resources;

import com.tryandroid.hpquiz.navigation.Router;
import com.tryandroid.hpquiz.preferences.ApplicationPreferences;
import com.tryandroid.quizcore.repo.QuizRepository;
import com.tryandroid.ux_common.menu.MenuViewModel;

public interface ApplicationComponents {

    ApplicationPreferences preferences();

    QuizRepository repository();

    Class<? extends MenuViewModel> mainMenuViewModel();

    Resources resources();

    Router router();
}
