package com.tryandroid.ux_common.quiz;

import android.arch.lifecycle.LiveData;
import android.support.annotation.IdRes;

import com.tryandroid.ux_common.BaseViewModel;

import java.util.List;

/**
 * Created by iSperia on 07.04.2018.
 */

public abstract class QuizPresenter extends BaseViewModel {

    public abstract void onAnswerSelected(int index);

    public abstract void moveToNextQuestion();

    public abstract void useTip(final TipInfo tipInfo);

    public abstract LiveData<QuestionViewModel> observeQuestion();

    public abstract LiveData<Boolean> observeCorectness();

    public abstract LiveData<Integer> observeScore();

    public abstract LiveData<ScoreAddViewModel> observeScoreAdditions();

    public abstract LiveData<List<TipInfo>> observeTipInfo();

    public abstract LiveData<HiddenAnswerInfo> observeHiddenAnswers();

    public abstract LiveData<GlobalEffectInfo> observeGlobalEffects();

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

    public static class TipInfo {

        private final String name;

        @IdRes
        private final int icon;

        private final int count;

        private final Object token;

        public TipInfo(final String name, @IdRes final int icon, final int count, final Object token) {
            this.name = name;
            this.icon = icon;
            this.count = count;
            this.token = token;
        }

        public String getName() {
            return name;
        }

        public int getIcon() {
            return icon;
        }

        public int getCount() {
            return count;
        }

        public Object getToken() {
            return token;
        }
    }

    public static class HiddenAnswerInfo {

        private final int idx1;

        private final int idx2;

        public HiddenAnswerInfo(final int idx1, final int idx2) {
            this.idx1 = idx1;
            this.idx2 = idx2;
        }

        public HiddenAnswerInfo() {
            this.idx1 = this.idx2 = -1;
        }

        public int getIdx1() {
            return idx1;
        }

        public int getIdx2() {
            return idx2;
        }

        public boolean isHidden(final int idx) {
            return idx1 == idx || idx2 == idx;
        }
    }

    public static class GlobalEffectInfo {

        private final String effectName;

        public GlobalEffectInfo(final String effectName) {
            this.effectName = effectName;
        }

        public String getEffectName() {
            return effectName;
        }
    }
}
