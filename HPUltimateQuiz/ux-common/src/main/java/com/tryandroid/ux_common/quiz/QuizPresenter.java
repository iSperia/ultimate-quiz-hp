package com.tryandroid.ux_common.quiz;

import android.arch.lifecycle.LiveData;

import com.tryandroid.quizcore.room.dao.QuestionAndText;
import com.tryandroid.ux_common.BaseViewModel;

/**
 * Created by iSperia on 07.04.2018.
 */

public abstract class QuizPresenter extends BaseViewModel {

    public abstract void onAnswerSelected(int index);

    public abstract void moveToNextQuestion();

    public abstract LiveData<QuestionViewModel> observeQuestion();

    public abstract LiveData<Boolean> observeCorectness();

    public abstract LiveData<Integer> observeScore();

    public abstract LiveData<ScoreAddViewModel> observeScoreAdditions();

    public static class ScoreAddViewModel {

        private final int count;

        private final String comment;

        public ScoreAddViewModel(final int count, final String comment) {
            this.count = count;
            this.comment = comment;
        }

        public int getCount() {
            return count;
        }

        public String getComment() {
            return comment;
        }
    }

    public static class QuestionViewModel {

        private final String question;

        private final String answer1;
        private final String answer2;
        private final String answer3;
        private final String answer4;


        public QuestionViewModel(final String question,
                                 final String answer1,
                                 final String answer2,
                                 final String answer3,
                                 final String answer4) {
            this.question = question;
            this.answer1 = answer1;
            this.answer2 = answer2;
            this.answer3 = answer3;
            this.answer4 = answer4;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer1() {
            return answer1;
        }

        public String getAnswer2() {
            return answer2;
        }

        public String getAnswer3() {
            return answer3;
        }

        public String getAnswer4() {
            return answer4;
        }
    }
}
