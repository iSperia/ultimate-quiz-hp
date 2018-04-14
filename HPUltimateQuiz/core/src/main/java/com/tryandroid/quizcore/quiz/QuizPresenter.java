package com.tryandroid.quizcore.quiz;

/**
 * Created by iSperia on 07.04.2018.
 */

public interface QuizPresenter {

    void onAnswerSelected(int index);

    void onCreate();

    void onRestore();

}
