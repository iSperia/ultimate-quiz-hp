package com.tryandroid.quizcore.repo;

import com.tryandroid.quizcore.room.dao.QuizDao;

public interface QuizDatabasePopulator {

    void populate(QuizDao dao);
}
