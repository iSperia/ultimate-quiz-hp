package com.tryandroid.ux_common.quiz;

import com.tryandroid.ux_common.BaseViewModel;

/**
 * Created by iSperia on 07.04.2018.
 */

public abstract class QuizPresenter extends BaseViewModel {

    public abstract void onAnswerSelected(int index);

    public abstract void moveToNextQuestion();
}
