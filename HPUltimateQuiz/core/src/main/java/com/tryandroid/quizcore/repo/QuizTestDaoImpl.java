package com.tryandroid.quizcore.repo;

import com.tryandroid.quizcore.room.dao.QuestionAndText;
import com.tryandroid.quizcore.room.dao.QuizDao;
import com.tryandroid.quizcore.room.entities.Adventure;
import com.tryandroid.quizcore.room.entities.AdventureText;
import com.tryandroid.quizcore.room.entities.Question;
import com.tryandroid.quizcore.room.entities.QuestionText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

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
