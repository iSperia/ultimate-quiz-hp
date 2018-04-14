package com.tryandroid.hpquiz;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.tryandroid.quizcore.repo.QuizDatabasePopulator;
import com.tryandroid.quizcore.room.dao.QuizDao;
import com.tryandroid.quizcore.room.entities.Adventure;
import com.tryandroid.quizcore.room.entities.Question;
import com.tryandroid.quizcore.room.entities.QuestionText;

public class DatabasePopulator implements QuizDatabasePopulator {

    private static final String TAG = "DatabasePopulator";

    public static final String QUESTIONS_CSV_FILENAME = "quest1.csv";
    public static final String COLUMN_SEPARATOR = ";";
    public static final int COLUMN_COMPLEXITY = 10;
    private static final int GENERATION_SIZE = 1000;

    private final Context context;

    public DatabasePopulator(final Context context) {
        this.context = context;
    }

    public void populate(final QuizDao dao) {
        final Random random = new Random();

        /**
         * First of all, create 0 adventure
         */
        dao.insertAdventure(new Adventure(Adventure.FAKE_ADVENTURE), Collections.EMPTY_LIST);

        final List<QuestionText> questionTexts = new ArrayList<>(2);

        try (final InputStream inputStream =
                     context.getResources().openRawResource(R.raw.quest1)) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            while (line != null) {
                final String [] columns = line.split(COLUMN_SEPARATOR);
                if (columns.length != 11) {
                    line = reader.readLine();
                    continue;
                }

                final Question question = new Question();
                question.adventureId = Adventure.FAKE_ADVENTURE;
                question.complexity = Integer.parseInt(columns[COLUMN_COMPLEXITY]);
                question.order = random.nextInt(GENERATION_SIZE);

                questionTexts.clear();

                //Parse russian question text
                final QuestionText questionTextRu = new QuestionText();
                questionTextRu.questionText = columns[0];
                questionTextRu.answer1 = columns[2];
                questionTextRu.answer2 = columns[4];
                questionTextRu.answer3 = columns[6];
                questionTextRu.answer4 = columns[8];
                questionTexts.add(questionTextRu);

                //Parse english questionText text
                final QuestionText questionTextEn = new QuestionText();
                questionTextEn.questionText = columns[1];
                questionTextEn.answer1 = columns[3];
                questionTextEn.answer2 = columns[5];
                questionTextEn.answer3 = columns[7];
                questionTextEn.answer4 = columns[9];
                questionTexts.add(questionTextEn);

                final long questionId = dao.insertQuestion(question);
                for (final QuestionText qt : questionTexts) {
                    qt.questionId = questionId;
                }
                dao.insertTexts(questionTexts);

                line = reader.readLine();
            }
        } catch (IOException e) {
            Log.w(TAG, "Error while populating database", e);
        }
    }
}
