package com.tryandroid.hpquiz;

import com.tryandroid.hpquiz.preferences.ApplicationPreferences;
import com.tryandroid.quizcore.repo.QuizRepository;

public interface AppContext {

    ApplicationPreferences preferences();

    QuizRepository repository();
}
