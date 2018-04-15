package com.tryandroid.ux_common;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.tryandroid.quizcore.quiz.QuizPresenter;
import com.tryandroid.quizcore.quiz.QuizView;

/**
 * Created by iSperia on 07.04.2018.
 */

public class QuizFragment extends Fragment implements QuizView {

    private static final String STATE_IS_QUESTION_SHOWING = "isQuestionShowing";

    private final int [] viewIdToIndexMap = {
            R.id.btn_answer_1,
            R.id.btn_answer_2,
            R.id.btn_answer_3,
            R.id.btn_answer_4
    };

    @BindView(R2.id.button_answer_layer_top)
    LinearLayout topAnswerLayer;
    @BindView(R2.id.button_answer_layer_bottom)
    LinearLayout bottomAnswerLayer;

    @BindView(R2.id.btn_answer_1)
    Button buttonAnswer1;
    @BindView(R2.id.btn_answer_2)
    Button buttonAnswer2;
    @BindView(R2.id.btn_answer_3)
    Button buttonAnswer3;
    @BindView(R2.id.btn_answer_4)
    Button buttonAnswer4;
    @BindView(R2.id.question_text)
    TextView textQuestionBody;

    private QuizPresenter presenter;

    private boolean isQuestionShowing;

    private Handler handler;

    private String questionText;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;

    private View clickedAnswerButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, result);

        final Providers components = (Providers) getActivity();
        presenter = components.provideQuizPresenter(this);

        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            presenter.start();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.save(outState);
        outState.putBoolean(STATE_IS_QUESTION_SHOWING, isQuestionShowing);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            isQuestionShowing = savedInstanceState.getBoolean(STATE_IS_QUESTION_SHOWING);
            presenter.restore(savedInstanceState);
        }
    }

    @Override
    public void showQuestion(String questionText, String a1, String a2, String a3, String a4) {
        this.questionText = questionText;
        this.answer1 = a1;
        this.answer2 = a2;
        this.answer3 = a3;
        this.answer4 = a4;

        if (isQuestionShowing) {
            hideQuestionAnswers(this::showQuestionAnswers);
        } else {
            showQuestionAnswers();
        }
    }

    private void hideQuestionAnswers(@Nullable final Runnable onAnimationEnd) {
        final Animation topLayerAnimation =
                AnimationUtils.loadAnimation(getContext(), R.anim.translate_down_200);
        final Animation bottomLayerAnimation =
                AnimationUtils.loadAnimation(getContext(), R.anim.translate_down_100);
        final Animation questionTextAnimation =
                AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_zoom);

        topAnswerLayer.startAnimation(topLayerAnimation);
        bottomAnswerLayer.startAnimation(bottomLayerAnimation);
        textQuestionBody.startAnimation(questionTextAnimation);

        topLayerAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                if (onAnimationEnd != null) {
                    onAnimationEnd.run();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        lockButtons(true);
    }

    private void showQuestionAnswers() {
        buttonAnswer1.setText(answer1);
        buttonAnswer2.setText(answer2);
        buttonAnswer3.setText(answer3);
        buttonAnswer4.setText(answer4);
        textQuestionBody.setText(questionText);

        final Animation topLayerAnimation =
                AnimationUtils.loadAnimation(getContext(), R.anim.translate_up_200);
        final Animation bottomLayerAnimation =
                AnimationUtils.loadAnimation(getContext(), R.anim.translate_up_100);
        final Animation questionTextAnimation =
                AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_zoom);

        topAnswerLayer.startAnimation(topLayerAnimation);
        bottomAnswerLayer.startAnimation(bottomLayerAnimation);
        textQuestionBody.startAnimation(questionTextAnimation);

        isQuestionShowing = true;
        lockButtons(false);
    }

    @OnClick({R2.id.btn_answer_1, R2.id.btn_answer_2, R2.id.btn_answer_3, R2.id.btn_answer_4})
    public void onAnswerClicked(final View view) {
        lockButtons(true);
        clickedAnswerButton = view;
        final int index = viewIdToIndex(view.getId());
        final Drawable nowDrawable = view.getBackground();
        final Drawable blinkDrawable = DrawableCompat.wrap(nowDrawable);
        blinkDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.tint_quiz_variant_yellow), PorterDuff.Mode.MULTIPLY);

        view.setBackground(blinkDrawable);
        handler.postDelayed(() -> {
            blinkDrawable.clearColorFilter();
            view.setBackground(nowDrawable);
            provideAnswerToPresenter(index);
        }, 1200L);

        view.setBackground(blinkDrawable);
        handler.postDelayed(() ->  blinkDrawable.clearColorFilter(), 150L);
        handler.postDelayed(() -> blinkDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.tint_quiz_variant_yellow), PorterDuff.Mode.MULTIPLY), 250L);
        handler.postDelayed(() -> blinkDrawable.clearColorFilter(), 350L);
        handler.postDelayed(() -> blinkDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.tint_quiz_variant_yellow), PorterDuff.Mode.MULTIPLY), 450);
    }

    @Override
    public void showAnswerCorectness(boolean isCorrect) {
        final Drawable nowDrawable = clickedAnswerButton.getBackground();
        nowDrawable.setColorFilter(ContextCompat.getColor(getContext(), isCorrect ? R.color.tint_quiz_variant_green : R.color.tint_quiz_variant_red), PorterDuff.Mode.MULTIPLY);

        handler.postDelayed(() -> {
            if (isCorrect) {
                nowDrawable.clearColorFilter();
            }
            clickedAnswerButton.setBackground(nowDrawable);
            presenter.moveToNextQuestion();
            lockButtons(false);
        }, 800L);
    }

    private void provideAnswerToPresenter(final int index) {
        presenter.onAnswerSelected(index);
    }

    private int viewIdToIndex(final int viewId) {
        for (int i = 0; i < viewIdToIndexMap.length; i++) {
            if (viewIdToIndexMap[i] == viewId) return i;
        }
        throw new IllegalArgumentException("View id is wrong: " + viewId);
    }

    private void lockButtons(final boolean isLocked) {
        buttonAnswer1.setClickable(!isLocked);
        buttonAnswer2.setClickable(!isLocked);
        buttonAnswer3.setClickable(!isLocked);
        buttonAnswer4.setClickable(!isLocked);
    }

    public interface Providers {
        QuizPresenter provideQuizPresenter(QuizView view);
    }
}
