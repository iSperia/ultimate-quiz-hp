package tryandroid.com.quizcore.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Single;
import tryandroid.com.quizcore.room.entities.Adventure;
import tryandroid.com.quizcore.room.entities.AdventureText;
import tryandroid.com.quizcore.room.entities.Question;
import tryandroid.com.quizcore.room.entities.QuestionText;

@Dao
public interface QuizDao {

    @Insert
    void insertAdventure(Adventure adventures, List<AdventureText> adventureTexts);

    @Insert
    void insertQuestion(Question question, List<QuestionText> questionTexts);

    @Update
    void updateQuestion(Question question);

    @Query("SELECT * FROM questions" +
            " INNER JOIN question_texts ON question_texts.questionId = questions.id" +
            " WHERE question_texts.languageId = :languageId" +
            " AND questions.adventureId = :adventureId" +
            " ORDER BY questions.`order`" +
            " LIMIT 1")
    Single<QuestionAndText> fetchQuestion(int languageId, int adventureId);

}
