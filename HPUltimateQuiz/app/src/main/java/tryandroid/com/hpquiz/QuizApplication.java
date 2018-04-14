package tryandroid.com.hpquiz;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import tryandroid.com.quizcore.repo.QuizRepository;
import tryandroid.com.quizcore.repo.QuizRepositoryImpl;
import tryandroid.com.quizcore.repo.QuizRepositoryTestImpl;
import tryandroid.com.quizcore.room.QuizDatabase;
import tryandroid.com.quizcore.room.dao.QuestionAndText;
import tryandroid.com.quizcore.room.entities.QuestionText;

public class QuizApplication extends Application {

    private QuizRepository quizRepository;

    private boolean isDatabaseJustCreated = false;

    @Override
    public void onCreate() {
        super.onCreate();

        quizRepository = new QuizRepositoryImpl(this, dbCallback);

        quizRepository.database().subscribe(this::onDatabaseAccessible);

    }

    private void onDatabaseAccessible(final QuizDatabase database) {
        if (isDatabaseJustCreated) {
            new DatabasePopulator(this, database.quizDao()).populate();
        }

        final QuestionAndText qt = database.quizDao().fetchQuestion(0, 0).blockingGet();
        Log.d("QuizApplication", "qt = " + qt);
    }

    private final RoomDatabase.Callback dbCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            isDatabaseJustCreated = true;
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}
