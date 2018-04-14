package com.tryandroid.hpquiz;

import android.app.Application;
import android.util.Log;

import com.tryandroid.hpquiz.preferences.ApplicationPreferences;
import com.tryandroid.hpquiz.preferences.DatabaseVersion;
import com.tryandroid.quizcore.repo.QuizRepository;
import com.tryandroid.quizcore.repo.QuizRepositoryImpl;
import com.tryandroid.quizcore.room.QuizDatabase;
import com.tryandroid.quizcore.room.dao.QuestionAndText;

public class QuizApplication extends Application implements AppContext {

    private static final String TAG = "QuizApplication";

    private QuizRepository quizRepository;

    private ApplicationPreferences applicationPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationPreferences = new ApplicationPreferences(this);
        quizRepository = new QuizRepositoryImpl(this);

        quizRepository.database().subscribe(this::onDatabaseAccessible, this::onDatabaseError);

    }

    private void onDatabaseAccessible(final QuizDatabase database) {
        final DatabaseVersion currentDatabaseVersion = applicationPreferences.getDatabaseVersion();
        if (currentDatabaseVersion == DatabaseVersion.NoDatabase) {
            new DatabasePopulator(this).populate(database.quizDao());
            applicationPreferences.setDatabaseVersion(DatabaseVersion.V1);
        }

        final QuestionAndText qt = database.quizDao().fetchQuestion(0, 0).blockingGet();
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
}
