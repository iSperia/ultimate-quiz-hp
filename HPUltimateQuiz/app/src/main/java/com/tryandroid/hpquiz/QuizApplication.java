package com.tryandroid.hpquiz;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import com.tryandroid.hpquiz.navigation.Router;
import com.tryandroid.hpquiz.navigation.RouterImpl;
import com.tryandroid.hpquiz.preferences.ApplicationPreferences;
import com.tryandroid.hpquiz.preferences.DatabaseVersion;
import com.tryandroid.hpquiz.presenter.MenuViewModelImpl;
import com.tryandroid.hpquiz.userdata.UserData;
import com.tryandroid.hpquiz.userdata.UserDataImpl;
import com.tryandroid.quizcore.repo.QuizRepository;
import com.tryandroid.quizcore.repo.QuizRepositoryImpl;
import com.tryandroid.quizcore.room.QuizDatabase;
import com.tryandroid.quizcore.room.dao.QuestionAndText;
import com.tryandroid.ux_common.menu.MenuViewModel;

import io.reactivex.schedulers.Schedulers;

public class QuizApplication extends Application implements ApplicationComponents {

    private static final String TAG = "QuizApplication";

    private static QuizApplication instance;

    private QuizRepository quizRepository;

    private ApplicationPreferences applicationPreferences;

    private UserData userData;

    private Router router;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        applicationPreferences = new ApplicationPreferences(this);
        quizRepository = new QuizRepositoryImpl(this);
        router = new RouterImpl();
        userData = new UserDataImpl(applicationPreferences);

        quizRepository.database().observeOn(Schedulers.io()).subscribe(this::onDatabaseAccessible, this::onDatabaseError);

    }

    private void onDatabaseAccessible(final QuizDatabase database) {
        final DatabaseVersion currentDatabaseVersion = applicationPreferences.getDatabaseVersion();
        if (currentDatabaseVersion == DatabaseVersion.NoDatabase) {
            new DatabasePopulator(this).populate(database.quizDao());
            applicationPreferences.setDatabaseVersion(DatabaseVersion.V1);
        }

        final QuestionAndText qt = database.quizDao().fetchQuestion(0, 0);
        Log.d("QuizApplication", "qt = " + qt.questionText);
    }


    private void onDatabaseError(Throwable throwable) {
        Log.e(TAG, "Database access error", throwable);
    }

    @Override
    public ApplicationPreferences preferences() {
        return applicationPreferences;
    }

    @Override
    public QuizRepository repository() {
        return quizRepository;
    }

    @Override
    public Class<? extends MenuViewModel> mainMenuViewModel() {
        return MenuViewModelImpl.class;
    }

    @Override
    public Resources resources() {
        return getResources();
    }

    @Override
    public Router router() {
        return router;
    }

    @Override
    public UserData userData() {
        return userData;
    }

    public static ApplicationComponents $() {
        return instance;
    }
}
