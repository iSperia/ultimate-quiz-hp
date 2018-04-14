package com.tryandroid.hpquiz;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tryandroid.quizcore.repo.QuizRepository;
import com.tryandroid.quizcore.repo.QuizRepositoryImpl;
import com.tryandroid.quizcore.room.QuizDatabase;
import com.tryandroid.quizcore.room.dao.QuestionAndText;

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
