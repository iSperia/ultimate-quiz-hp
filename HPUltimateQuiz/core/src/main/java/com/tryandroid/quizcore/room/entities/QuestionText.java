package com.tryandroid.quizcore.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "question_texts", foreignKeys = {
        @ForeignKey(entity = Question.class,
        parentColumns = "id",
        childColumns = "question_id")},
indices = {@Index(value="question_id", name="question_id_index")})
public class QuestionText {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "question_id")
    public long questionId;

    public int languageId;

    public String questionText;

    public String answer1;

    public String answer2;

    public String answer3;

    public String answer4;
}
