package com.tryandroid.ux_common.quiz;

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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tryandroid.ux_common.R;
import com.tryandroid.ux_common.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

/**
 * Created by iSperia on 07.04.2018.
 */

public class QuizFragment extends Fragment {

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

    @BindView(R2.id.score_comment)
    TextView textScoreComment;
    @BindView(R2.id.score_count_text)
    TextView textScoreCount;

    private QuizPresenter presenter;

    @State
    boolean isQuestionShowing;

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
        presenter = components.provideQuizPresenter();

        presenter.observeCorectness().observe(this, this::showAnswerCorectness);
        presenter.observeQuestion().observe(this, this::showQuestion);
        presenter.observeScore().observe(this, this::showScore);
        presenter.observeScoreAdditions().observe(this, this::addScore);
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

        //TODO: observe
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.persist(outState);
        Icepick.saveInstanceState(this, outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
            presenter.restore(savedInstanceState);
        } else {
            presenter.start();
        }
    }

    public void showQuestion(final QuizPresenter.QuestionViewModel questionViewModel) {
        this.questionText = questionViewModel.getQuestion();
        this.answer1 = questionViewModel.getAnswer1();
        this.answer2 = questionViewModel.getAnswer2();
        this.answer3 = questionViewModel.getAnswer3();
        this.answer4 = questionViewModel.getAnswer4();

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
        handler.postDelayed(() -> blinkDrawable.clearColorFilter(), 150L);
        handler.postDelayed(() -> blinkDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.tint_quiz_variant_yellow), PorterDuff.Mode.MULTIPLY), 250L);
        handler.postDelayed(() -> blinkDrawable.clearColorFilter(), 350L);
        handler.postDelayed(() -> blinkDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.tint_quiz_variant_yellow), PorterDuff.Mode.MULTIPLY), 450);
    }

    public void showAnswerCorectness(final Boolean isCorrect) {
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

    public void showScore(final Integer score) {
        textScoreCount.setText(String.valueOf(score));
    }

    public void addScore(final QuizPresenter.ScoreAddViewModel scoreAddViewModel) {
        textScoreComment.setText(scoreAddViewModel.getCount() + scoreAddViewModel.getComment());
        final AnimationSet textScoreShadowAnimation = new AnimationSet(true);
        textScoreShadowAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textScoreComment.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        textScoreShadowAnimation.addAnimation(new TranslateAnimation(0, 0, 0, -textScoreCount.getHeight()));
        textScoreShadowAnimation.addAnimation(new AlphaAnimation(1f, 0.4f));
        textScoreShadowAnimation.addAnimation(new ScaleAnimation(1.1f, 0.8f, 0.8f, 1f));
        textScoreShadowAnimation.setDuration(300);
        textScoreComment.setVisibility(View.VISIBLE);
        textScoreComment.startAnimation(textScoreShadowAnimation);

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
        QuizPresenter provideQuizPresenter();
    }
}
