package com.tryandroid.hpquiz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tryandroid.ux_common.BackKeyInterceptor;
import com.tryandroid.hpquiz.navigation.Router;
import com.tryandroid.hpquiz.navigation.RouterImpl;
import com.tryandroid.hpquiz.presenter.MenuViewModelImpl;
import com.tryandroid.hpquiz.presenter.SovPresenter;
import com.tryandroid.ux_common.menu.MainMenuFragment;
import com.tryandroid.ux_common.quiz.QuizPresenter;
import com.tryandroid.quizcore.room.dao.QuizDao;
import com.tryandroid.ux_common.quiz.QuizFragment;

public class Activity extends AppCompatActivity implements QuizFragment.Providers, Router {

    private static final String TAG_CONTENT = "content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            final FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.content_root, MainMenuFragment.instantiate(MenuViewModelImpl.class),
                            TAG_CONTENT)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((RouterImpl) QuizApplication.$().router()).setMainActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public QuizPresenter provideQuizPresenter() {
        final QuizDao dao = ((ApplicationComponents) getApplication())
                .repository()
                .database()
                .blockingFirst()
                .quizDao();
        return new SovPresenter(dao);
    }

    @Override
    public void startSovExams() {
        final FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content_root, new QuizFragment(), TAG_CONTENT)
                .addToBackStack(TAG_CONTENT).commit();
    }

    @Override
    public void onBackPressed() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CONTENT);
        boolean handled = false;
        if (fragment instanceof BackKeyInterceptor) {
            handled = ((BackKeyInterceptor) fragment).shouldInterceptBackKey();
        }
        if (!handled) {
            super.onBackPressed();
        }
    }
}
