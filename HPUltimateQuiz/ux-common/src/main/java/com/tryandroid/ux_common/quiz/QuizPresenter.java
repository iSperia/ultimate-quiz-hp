package com.tryandroid.ux_common.quiz;

import com.tryandroid.ux_common.LifecycleCallback;

/**
 * Created by iSperia on 07.04.2018.
 */

public interface QuizPresenter extends LifecycleCallback {

    void onAnswerSelected(int index);

    void moveToNextQuestion();
}
