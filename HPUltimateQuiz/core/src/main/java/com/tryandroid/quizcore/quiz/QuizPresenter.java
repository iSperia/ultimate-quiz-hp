package com.tryandroid.quizcore.quiz;

import com.tryandroid.quizcore.LifecycleCallback;

/**
 * Created by iSperia on 07.04.2018.
 */

public interface QuizPresenter extends LifecycleCallback {

    void onAnswerSelected(int index);

    void moveToNextQuestion();

    void start();

}
