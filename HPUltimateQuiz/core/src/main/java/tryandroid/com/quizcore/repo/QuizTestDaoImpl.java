package tryandroid.com.quizcore.repo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import tryandroid.com.quizcore.room.dao.QuestionAndText;
import tryandroid.com.quizcore.room.dao.QuizDao;
import tryandroid.com.quizcore.room.entities.Adventure;
import tryandroid.com.quizcore.room.entities.AdventureText;
import tryandroid.com.quizcore.room.entities.Question;
import tryandroid.com.quizcore.room.entities.QuestionText;

/**
 * Fake dao
 * Created by iSperia on 07.04.2018.
 */
public class QuizTestDaoImpl implements QuizDao {

    private final List<QuestionAndText> data = new ArrayList<>(20);

    private int currentIndex;

    @Override
    public void insertAdventure(Adventure adventures, List<AdventureText> adventureTexts) {

    }

    @Override
    public void insertQuestion(Question question, List<QuestionText> questionTexts) {
        final QuestionAndText questionAndText = new QuestionAndText();
        questionAndText.questionId = data.size();
        questionAndText.adventureId = 0;
        final QuestionText questionText = questionTexts.get(1);
        questionAndText.answer1 = questionText.answer1;
        questionAndText.answer2 = questionText.answer2;
        questionAndText.answer3 = questionText.answer3;
        questionAndText.answer4 = questionText.answer4;
        questionAndText.questionText = questionText.questionText;

        data.add(questionAndText);
    }

    @Override
    public void updateQuestion(Question question) {

    }

    @Override
    public Single<QuestionAndText> fetchQuestion(int languageId, int adventureId) {
        return Single.just(data.get(currentIndex++));
    }
}
