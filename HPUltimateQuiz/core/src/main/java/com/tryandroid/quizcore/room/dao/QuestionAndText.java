package com.tryandroid.quizcore.room.dao;

import android.arch.persistence.room.ColumnInfo;

public class QuestionAndText {

    public int id;

    public int complexity;

    public int order;

    public int adventureId;

    @ColumnInfo(name = "question_id")
    public int questionId;

    public int languageId;

    public String questionText;

    public String answer1;

    public String answer2;

    public String answer3;

    public String answer4;
}
