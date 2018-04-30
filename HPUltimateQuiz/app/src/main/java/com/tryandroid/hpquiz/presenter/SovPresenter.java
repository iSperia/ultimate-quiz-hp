package com.tryandroid.hpquiz.presenter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.util.Log;

import com.google.gson.Gson;
import com.tryandroid.ux_common.bundler.QuestionAndTextBundler;
import com.tryandroid.ux_common.quiz.QuizPresenter;
import com.tryandroid.quizcore.room.dao.QuestionAndText;
import com.tryandroid.quizcore.room.dao.QuizDao;
import com.tryandroid.quizcore.room.entities.Question;

import java.util.Random;

import icepick.State;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by iSperia on 07.04.2018.
 */

public class SovPresenter extends QuizPresenter {

    private final String TAG = "SovPresenter";

    @IntDef({Mode.Default, Mode.InvalidAnswerProvided})
    public @interface Mode {
        int Default = 0;
        int InvalidAnswerProvided = 1;
    }

    private QuizDao dao;

    @State(QuestionAndTextBundler.class)
    QuestionAndText currentQuestion;

    private final Random random = new Random();

    @State
    int [] indicies = new int[4];

    @State
    @Mode
    int presenterState = Mode.Default;

    @State
    int score = 0;

    @State
    int questionIndex = 0;

    public SovPresenter(final QuizDao dao) {
        this.dao = dao;
    }

    private final MutableLiveData<Boolean> answerCorectnessObservable = new MutableLiveData<>();

    private final MutableLiveData<ScoreAddViewModel> scoreAddObservable = new MutableLiveData<>();

    private final MutableLiveData<Integer> scoreObservable = new MutableLiveData<>();

    private final MutableLiveData<QuestionViewModel> questionObservable = new MutableLiveData<>();

    @Override
    public void onAnswerSelected(final int index) {
        final boolean isAnswerCorrect = indicies[index] == 0;
        Log.d(TAG, "answer provided. correctness = " + isAnswerCorrect);
        answerCorectnessObservable.postValue(isAnswerCorrect);
        if (!isAnswerCorrect) {
            presenterState = Mode.InvalidAnswerProvided;
        } else {
            final int multipler = Math.min(questionIndex / 4, 4) + 1;
            final int scoreToAdd = currentQuestion.complexity;
            score += scoreToAdd * multipler;
            scoreAddObservable.postValue(new ScoreAddViewModel(scoreToAdd,
                    " X " + multipler));
            scoreObservable.postValue(score);
        }
    }

    @Override
    public void moveToNextQuestion() {
        if (presenterState == Mode.Default) {
            nextQuestion();
        }
    }

    @Override
    public void start() {
        moveToNextQuestion();
        scoreObservable.postValue(score);
    }

    @Override
    public LiveData<QuestionViewModel> observeQuestion() {
        return questionObservable;
    }

    @Override
    public LiveData<Boolean> observeCorectness() {
        return answerCorectnessObservable;
    }

    @Override
    public LiveData<Integer> observeScore() {
        return scoreObservable;
    }

    @Override
    public LiveData<ScoreAddViewModel> observeScoreAdditions() {
        return scoreAddObservable;
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
        questionObservable.postValue(new QuestionViewModel(
                currentQuestion.questionText,
                extractAnswerByIndex(indicies[0]),
                extractAnswerByIndex(indicies[1]),
                extractAnswerByIndex(indicies[2]),
                extractAnswerByIndex(indicies[3])));
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
