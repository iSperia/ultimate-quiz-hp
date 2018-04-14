package tryandroid.com.quizcore.quiz;

import tryandroid.com.quizcore.room.dao.QuestionAndText;

/**
 * Created by iSperia on 07.04.2018.
 */

public interface QuizView {

    void showQuestion(String questionText,
                      String answer1,
                      String answer2,
                      String answer3,
                      String answer4);

}
