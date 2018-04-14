package com.tryandroid.quizcore.repo;

import io.reactivex.subjects.ReplaySubject;
import com.tryandroid.quizcore.room.QuizDatabase;

public interface QuizRepository {

    ReplaySubject<QuizDatabase> database();

}
