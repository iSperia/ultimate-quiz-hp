package com.tryandroid.hpquiz.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.tryandroid.ux_common.quiz.QuizPresenter;
import com.tryandroid.ux_common.quiz.QuizView;
import com.tryandroid.quizcore.room.dao.QuestionAndText;
import com.tryandroid.quizcore.room.dao.QuizDao;
import com.tryandroid.quizcore.room.entities.Question;

import java.util.Random;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by iSperia on 07.04.2018.
 */

public class SovPresenter implements QuizPresenter {

    private final String TAG = "SovPresenter";

    private static final String STATE_CURRENT_QUESTION = "currentQuestion";
    private static final String STATE_INDICIES = "indicies";
    private static final String STATE_SCORE = "score";
    private static final String STATE_QUESTION_INDEX = "question_index";

    private QuizView quizView;

    private QuizDao dao;

    private QuestionAndText currentQuestion;

    private final Random random = new Random();

    private int [] indicies = new int[4];

    private State state = State.Default;

    private int score = 0;

    private int questionIndex = 0;

    private enum State {
        Default,
        InvalidAnswerProvided
    }

    public SovPresenter(final QuizView quizView,
                        final QuizDao dao) {
        this.quizView = quizView;
        this.dao = dao;
    }

    @Override
    public void onAnswerSelected(final int index) {
        final boolean isAnswerCorrect = indicies[index] == 0;
        Log.d(TAG, "answer provided. correctness = " + isAnswerCorrect);
        quizView.showAnswerCorectness(isAnswerCorrect);
        if (!isAnswerCorrect) {
            state = State.InvalidAnswerProvided;
        } else {
            final int multipler = Math.min(questionIndex / 4, 4) + 1;
            final int scoreToAdd = currentQuestion.complexity;
            score += scoreToAdd * multipler;
            quizView.addScore(scoreToAdd, scoreToAdd + " X " + multipler, score);
        }
    }

    @Override
    public void hibernate() {
    }

    @Override
    public void save(Bundle state) {
        state.putString(STATE_CURRENT_QUESTION, new Gson().toJson(currentQuestion));
        state.putIntArray(STATE_INDICIES, indicies);
        state.putInt(STATE_SCORE, score);
        state.putInt(STATE_QUESTION_INDEX, questionIndex);
    }

    @Override
    public void restore(Bundle state) {
        currentQuestion = new Gson().fromJson(state.getString(STATE_CURRENT_QUESTION), QuestionAndText.class);
        indicies = state.getIntArray(STATE_INDICIES);
        score = state.getInt(STATE_SCORE, 0);
        questionIndex = state.getInt(STATE_QUESTION_INDEX, 0);
        quizView.showScore(score);
        showQuestion(currentQuestion);
    }

    @Override
    public void start() {
        nextQuestion();
        quizView.showScore(score);
    }

    @Override
    public void moveToNextQuestion() {
        if (state == State.Default) {
            nextQuestion();
        }
    }

    private void nextQuestion() {
        Single.just(dao)
                .observeOn(Schedulers.io())
                .map(dao -> dao.fetchQuestion(0, 0))
                .doOnSuccess(this::onQuestionQueryReady)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showQuestion);
    }

    private void onQuestionQueryReady(final QuestionAndText qt) {
        questionIndex++;
        currentQuestion = qt;
        resetIndicies();
        for (int i = 0; i < 5; i++) {
            int index1 = 0, index2 = 0;
            while (index1 == index2) {
                index1 = random.nextInt(4);
                index2 = random.nextInt(4);
            }
            int valueLeft = indicies[index1];
            indicies[index1] = indicies[index2];
            indicies[index2] = valueLeft;
        }

        final Question question = dao.findQuestionById(currentQuestion.questionId);
        final int questionGeneration = question.order / 1000;
        question.order = 1000 * (questionGeneration + 1) + random.nextInt(1000);//TODO: generation
        dao.updateQuestion(question);
    }

    private void showQuestion(final QuestionAndText qt) {
        quizView.showQuestion(currentQuestion.questionText,
                extractAnswerByIndex(indicies[0]),
                extractAnswerByIndex(indicies[1]),
                extractAnswerByIndex(indicies[2]),
                extractAnswerByIndex(indicies[3]));
    }

    private void resetIndicies() {
        for (int i = 0; i < indicies.length; i++) {
            indicies[i] = i;
        }
    }

    private String extractAnswerByIndex(final int index) {
        switch (index) {
            case 0:
                return currentQuestion.answer1;
            case 1:
                return currentQuestion.answer2;
            case 2:
                return currentQuestion.answer3;
            case 3:
                return currentQuestion.answer4;
            default:
                throw new IllegalStateException("Requested wrong index");
        }
    }
}
