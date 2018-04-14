package com.tryandroid.quizcore.repo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;
import com.tryandroid.quizcore.room.QuizDatabase;

public class QuizRepositoryImpl implements QuizRepository {

    private static final String TAG = "QuizRepoImpl";
    private static final String DATABASE_FILENAME = "quiz.db";

    private final Context context;

    @Nullable
    private QuizDatabase database;

    private final ReplaySubject<QuizDatabase> databaseReplaySubject;

    public QuizRepositoryImpl(final Context context) {
        this.context = context;
        databaseReplaySubject = ReplaySubject.createWithSize(1);

        Single.fromCallable(this::initDatabase)
                .subscribeOn(Schedulers.io())
                .subscribe(this::onDatabaseReady, this::onError);
    }

    @Override
    public synchronized ReplaySubject<QuizDatabase> database() {
        return databaseReplaySubject;
    }

    private QuizDatabase initDatabase() {
        return Room.databaseBuilder(context, QuizDatabase.class, DATABASE_FILENAME)
                .build();
    }

    private void onDatabaseReady(final QuizDatabase database) {
        this.database = database;
        databaseReplaySubject.onNext(database);
    }

    private void onError(final Throwable throwable) {
        Log.e(TAG, "Failed to access database", throwable);
    }
}
