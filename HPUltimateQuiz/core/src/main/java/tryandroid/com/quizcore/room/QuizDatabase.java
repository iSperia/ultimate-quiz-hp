package tryandroid.com.quizcore.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import tryandroid.com.quizcore.room.dao.QuizDao;
import tryandroid.com.quizcore.room.entities.Adventure;
import tryandroid.com.quizcore.room.entities.AdventureText;
import tryandroid.com.quizcore.room.entities.Question;
import tryandroid.com.quizcore.room.entities.QuestionText;

@Database(version = 1, entities = {
        Question.class,
        Adventure.class,
        QuestionText.class,
        AdventureText.class},
        exportSchema = false)
public abstract class QuizDatabase extends RoomDatabase {

    public abstract QuizDao quizDao();
}
