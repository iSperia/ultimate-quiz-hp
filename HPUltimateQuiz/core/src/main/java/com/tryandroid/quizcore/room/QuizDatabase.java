package com.tryandroid.quizcore.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.tryandroid.quizcore.room.dao.QuizDao;
import com.tryandroid.quizcore.room.entities.Adventure;
import com.tryandroid.quizcore.room.entities.AdventureText;
import com.tryandroid.quizcore.room.entities.Question;
import com.tryandroid.quizcore.room.entities.QuestionText;

@Database(version = 1, entities = {
        Question.class,
        Adventure.class,
        QuestionText.class,
        AdventureText.class},
        exportSchema = false)
public abstract class QuizDatabase extends RoomDatabase {

    public abstract QuizDao quizDao();
}
