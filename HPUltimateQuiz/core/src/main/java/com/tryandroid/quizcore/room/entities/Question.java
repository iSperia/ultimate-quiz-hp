package com.tryandroid.quizcore.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "questions")
public class Question {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public int complexity;

    public int order;

    @ForeignKey(entity = Adventure.class,
                parentColumns = "id",
                childColumns = "adventure_id")
    public int adventureId;
}
