package tryandroid.com.quizcore.repo;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteTransactionListener;
import android.os.CancellationSignal;
import android.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.subjects.ReplaySubject;
import tryandroid.com.quizcore.room.QuizDatabase;
import tryandroid.com.quizcore.room.dao.QuizDao;

/**
 * Created by iSperia on 07.04.2018.
 */

public class QuizRepositoryTestImpl implements QuizRepository {

    private final ReplaySubject<QuizDatabase> mockDatabase = ReplaySubject.createWithSize(1);

    private final QuizTestDaoImpl quizTestDao = new QuizTestDaoImpl();

    private final RoomDatabase.Callback callback;

    public QuizRepositoryTestImpl(final RoomDatabase.Callback callback) {
        this.callback = callback;
        mockDatabase.onNext(new QuizDatabase() {
            @Override
            public QuizDao quizDao() {
                return quizTestDao;
            }

            @Override
            protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
                return null;
            }

            @Override
            protected InvalidationTracker createInvalidationTracker() {
                return null;
            }
        });
        callback.onCreate(new SupportSQLiteDatabase() {
            @Override
            public SupportSQLiteStatement compileStatement(String sql) {
                return null;
            }

            @Override
            public void beginTransaction() {

            }

            @Override
            public void beginTransactionNonExclusive() {

            }

            @Override
            public void beginTransactionWithListener(SQLiteTransactionListener transactionListener) {

            }

            @Override
            public void beginTransactionWithListenerNonExclusive(SQLiteTransactionListener transactionListener) {

            }

            @Override
            public void endTransaction() {

            }

            @Override
            public void setTransactionSuccessful() {

            }

            @Override
            public boolean inTransaction() {
                return false;
            }

            @Override
            public boolean isDbLockedByCurrentThread() {
                return false;
            }

            @Override
            public boolean yieldIfContendedSafely() {
                return false;
            }

            @Override
            public boolean yieldIfContendedSafely(long sleepAfterYieldDelay) {
                return false;
            }

            @Override
            public int getVersion() {
                return 0;
            }

            @Override
            public void setVersion(int version) {

            }

            @Override
            public long getMaximumSize() {
                return 0;
            }

            @Override
            public long setMaximumSize(long numBytes) {
                return 0;
            }

            @Override
            public long getPageSize() {
                return 0;
            }

            @Override
            public void setPageSize(long numBytes) {

            }

            @Override
            public Cursor query(String query) {
                return null;
            }

            @Override
            public Cursor query(String query, Object[] bindArgs) {
                return null;
            }

            @Override
            public Cursor query(SupportSQLiteQuery query) {
                return null;
            }

            @Override
            public Cursor query(SupportSQLiteQuery query, CancellationSignal cancellationSignal) {
                return null;
            }

            @Override
            public long insert(String table, int conflictAlgorithm, ContentValues values) throws SQLException {
                return 0;
            }

            @Override
            public int delete(String table, String whereClause, Object[] whereArgs) {
                return 0;
            }

            @Override
            public int update(String table, int conflictAlgorithm, ContentValues values, String whereClause, Object[] whereArgs) {
                return 0;
            }

            @Override
            public void execSQL(String sql) throws SQLException {

            }

            @Override
            public void execSQL(String sql, Object[] bindArgs) throws SQLException {

            }

            @Override
            public boolean isReadOnly() {
                return false;
            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public boolean needUpgrade(int newVersion) {
                return false;
            }

            @Override
            public String getPath() {
                return null;
            }

            @Override
            public void setLocale(Locale locale) {

            }

            @Override
            public void setMaxSqlCacheSize(int cacheSize) {

            }

            @Override
            public void setForeignKeyConstraintsEnabled(boolean enable) {

            }

            @Override
            public boolean enableWriteAheadLogging() {
                return false;
            }

            @Override
            public void disableWriteAheadLogging() {

            }

            @Override
            public boolean isWriteAheadLoggingEnabled() {
                return false;
            }

            @Override
            public List<Pair<String, String>> getAttachedDbs() {
                return null;
            }

            @Override
            public boolean isDatabaseIntegrityOk() {
                return false;
            }

            @Override
            public void close() throws IOException {

            }
        });
    }

    @Override
    public ReplaySubject<QuizDatabase> database() {
        return mockDatabase;
    }
}
