package com.tryandroid.quizcore.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "adventures")
public class Adventure {

    public static final int FAKE_ADVENTURE = 0;

    @PrimaryKey(autoGenerate = true)
    public int id;

    public Adventure(int id) {
        this.id = id;
    }
}
