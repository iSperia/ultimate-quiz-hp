package com.tryandroid.hpquiz;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tryandroid.hpquiz.presenter.SovPresenter;
import com.tryandroid.quizcore.quiz.QuizPresenter;
import com.tryandroid.quizcore.quiz.QuizView;
import com.tryandroid.quizcore.room.dao.QuizDao;
import com.tryandroid.ux_common.QuizFragment;

public class MainActivity extends AppCompatActivity implements QuizFragment.Providers {

    private static final String TAG_QUIZ = "quiz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            final FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().add(R.id.content_root, new QuizFragment(), TAG_QUIZ).commit();
        }
    }

    @Override
    public QuizPresenter provideQuizPresenter(final QuizView view) {
        final QuizDao dao = ((AppContext) getApplication())
                .repository()
                .database()
                .blockingFirst()
                .quizDao();
        return new SovPresenter(view, dao);
    }
}
