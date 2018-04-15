package com.tryandroid.ux_common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.tryandroid.quizcore.quiz.QuizPresenter;
import com.tryandroid.quizcore.quiz.QuizView;

/**
 * Created by iSperia on 07.04.2018.
 */

public class QuizFragment extends Fragment implements QuizView {

    private final int [] viewIdToIndexMap = {
            R2.id.btn_answer_1,
            R2.id.btn_answer_2,
            R2.id.btn_answer_3,
            R2.id.btn_answer_4
    };

    @BindView(R2.id.btn_answer_1)
    Button buttonAnswer1;
    @BindView(R2.id.btn_answer_2)
    Button buttonAnswer2;
    @BindView(R2.id.btn_answer_3)
    Button buttonAnswer3;
    @BindView(R2.id.btn_answer_4)
    Button buttonAnswer4;

    private QuizPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_question, container);
        ButterKnife.bind(this, result);
        return result;
    }

    @Override
    public void showQuestion(String questionText, String answer1, String answer2, String answer3, String answer4) {
        buttonAnswer1.setText(answer1);
        buttonAnswer2.setText(answer2);
        buttonAnswer3.setText(answer3);
        buttonAnswer4.setText(answer4);
    }

    @OnClick({R2.id.btn_answer_1, R2.id.btn_answer_2, R2.id.btn_answer_3, R2.id.btn_answer_4})
    public void onAnswerClicked(final View view) {
        final int index = viewIdToIndex(view.getId());
        presenter.onAnswerSelected(index);
    }

    private int viewIdToIndex(final int viewId) {
        for (int i = 0; i < viewIdToIndexMap.length; i++) {
            if (viewIdToIndexMap[i] == viewId) return i;
        }
        throw new IllegalArgumentException("View id is wrong: " + viewId);
    }
}
