package com.tryandroid.quizcore.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "adventure_texts")
public class AdventureText {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ForeignKey(entity = Adventure.class,
                childColumns = "adventure_id",
                parentColumns = "id")
    public int adventureId;

    public int languageId;
}
