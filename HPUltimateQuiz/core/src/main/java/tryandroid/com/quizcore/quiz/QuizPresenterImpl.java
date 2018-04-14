package tryandroid.com.quizcore.quiz;

import android.util.Log;

import java.util.Random;

import tryandroid.com.quizcore.room.dao.QuestionAndText;
import tryandroid.com.quizcore.room.dao.QuizDao;

/**
 * Created by iSperia on 07.04.2018.
 */

public class QuizPresenterImpl implements QuizPresenter {

    private final String TAG = "QuizPresenterImpl";

    private QuizView quizView;

    private QuizDao dao;

    private QuestionAndText currentQuestion;

    private final Random random = new Random();

    private final int [] indicies = new int[4];

    public QuizPresenterImpl(final QuizView quizView,
                             final QuizDao dao) {
        this.quizView = quizView;
        this.dao = dao;
    }

    @Override
    public void onAnswerSelected(final int index) {
        final boolean isAnswerCorrect = indicies[index] == 0;
        Log.d(TAG, "answer provided. correctness = " + isAnswerCorrect);
        nextQuestion();
    }

    @Override
    public void onCreate() {
        nextQuestion();
    }

    @Override
    public void onRestore() {
    }

    private void nextQuestion() {
        currentQuestion = dao.fetchQuestion(0, 0).blockingGet();

        resetIndicies();
        for (int i = 0; i < 5; i++) {
            int index1 = 0, index2 = 0;
            while (index1 == index2) {
                index1 = random.nextInt(4);
                index2 = random.nextInt(4);
            }
            int valueLeft = indicies[index1];
            indicies[index1] = indicies[index2];
            indicies[index2] = valueLeft;
        }

        quizView.showQuestion(currentQuestion.questionText,
                extractAnswerByIndex(indicies[0]),
                extractAnswerByIndex(indicies[1]),
                extractAnswerByIndex(indicies[2]),
                extractAnswerByIndex(indicies[3]));
    }

    private void resetIndicies() {
        for (int i = 0; i < indicies.length; i++) {
            indicies[i] = i;
        }
    }

    private String extractAnswerByIndex(final int index) {
        switch (index) {
            case 0:
                return currentQuestion.answer1;
            case 1:
                return currentQuestion.answer2;
            case 2:
                return currentQuestion.answer3;
            case 3:
                return currentQuestion.answer4;
            default:
                throw new IllegalStateException("Requested wrong index");
        }
    }
}
