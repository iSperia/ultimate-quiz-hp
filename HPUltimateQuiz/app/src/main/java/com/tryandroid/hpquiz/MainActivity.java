package com.tryandroid.hpquiz;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tryandroid.hpquiz.navigation.BackKeyInterceptor;
import com.tryandroid.hpquiz.navigation.NavigationContext;
import com.tryandroid.hpquiz.presenter.MainMenuPresenter;
import com.tryandroid.hpquiz.presenter.SovPresenter;
import com.tryandroid.ux_common.menu.MainMenuFragment;
import com.tryandroid.ux_common.menu.MenuPresenter;
import com.tryandroid.ux_common.menu.MenuView;
import com.tryandroid.ux_common.quiz.QuizPresenter;
import com.tryandroid.ux_common.quiz.QuizView;
import com.tryandroid.quizcore.room.dao.QuizDao;
import com.tryandroid.ux_common.quiz.QuizFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements QuizFragment.Providers,
        MainMenuFragment.Providers, NavigationContext {

    private static final String TAG_QUIZ = "quiz";
    private static final String TAG_MENU = "menu";

    private final List<BackKeyInterceptor> interceptorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            final FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().add(R.id.content_root, new MainMenuFragment(), TAG_MENU).commit();
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

    @Override
    public MenuPresenter provideMenuPresenter(final MenuView view) {
        return new MainMenuPresenter(getResources(), view, this);
    }

    @Override
    public void registerBackKeyInterceptor(BackKeyInterceptor interceptor) {
        interceptorList.add(interceptor);
    }

    @Override
    public void unregisterBackKeyInterceptor(BackKeyInterceptor interceptor) {
        interceptorList.remove(interceptor);
    }

    @Override
    public void startAction(NavigationAction action) {
        switch (action) {
            case StartExamSov:
                final FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.content_root, new QuizFragment(), TAG_QUIZ)
                        .addToBackStack(TAG_QUIZ).commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        for (final BackKeyInterceptor interceptor : interceptorList) {
            if (interceptor.shouldInterceptBackKey()) {
                return;
            }
        }
        super.onBackPressed();
    }
}
