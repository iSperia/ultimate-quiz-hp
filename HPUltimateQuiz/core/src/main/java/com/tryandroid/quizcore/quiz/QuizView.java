package com.tryandroid.quizcore.quiz;

/**
 * Created by iSperia on 07.04.2018.
 */

public interface QuizView {

    void showQuestion(String questionText,
                      String answer1,
                      String answer2,
                      String answer3,
                      String answer4);

    void showAnswerCorectness(boolean isCorrect);

}
