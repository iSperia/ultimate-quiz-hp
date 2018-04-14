package tryandroid.com.quizcore.repo;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;
import tryandroid.com.quizcore.room.QuizDatabase;

public class QuizRepositoryImpl implements QuizRepository {

    private static final String DATABASE_FILENAME = "quiz.db";

    private final Context context;

    private final RoomDatabase.Callback dbCallback;

    @Nullable
    private QuizDatabase database;

    private final ReplaySubject<QuizDatabase> databaseReplaySubject;

    public QuizRepositoryImpl(final Context context, final RoomDatabase.Callback callback) {
        this.context = context;
        this.dbCallback = callback;
        databaseReplaySubject = ReplaySubject.createWithSize(1);

        Single.fromCallable(this::initDatabase)
                .subscribeOn(Schedulers.io())
                .subscribe(this::onDatabaseReady);
    }

    @Override
    public synchronized ReplaySubject<QuizDatabase> database() {
        return databaseReplaySubject;
    }

    private QuizDatabase initDatabase() {
        return Room.databaseBuilder(context, QuizDatabase.class, DATABASE_FILENAME)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        dbCallback.onCreate(db);
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                        dbCallback.onOpen(db);
                        databaseReplaySubject.onNext(database);
                    }
                })
                .build();
    }

    private void onDatabaseReady(final QuizDatabase database) {
        this.database = database;
    }
}
