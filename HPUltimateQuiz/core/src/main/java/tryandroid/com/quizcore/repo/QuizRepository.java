package tryandroid.com.quizcore.repo;

import io.reactivex.subjects.ReplaySubject;
import tryandroid.com.quizcore.room.QuizDatabase;

public interface QuizRepository {

    ReplaySubject<QuizDatabase> database();

}
