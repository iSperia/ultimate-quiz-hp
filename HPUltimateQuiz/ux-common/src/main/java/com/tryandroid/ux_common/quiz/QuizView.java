package com.tryandroid.ux_common.quiz;

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

    void showScore(int score);

    void addScore(int count, String comment, int newScore);
}
