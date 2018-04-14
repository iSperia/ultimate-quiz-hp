package com.tryandroid.quizcore.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "question_texts")
public class QuestionText {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ForeignKey(entity = Question.class,
                parentColumns = "id",
                childColumns = "question_id")
    public int questionId;

    public int languageId;

    public String questionText;

    public String answer1;

    public String answer2;

    public String answer3;

    public String answer4;
}
