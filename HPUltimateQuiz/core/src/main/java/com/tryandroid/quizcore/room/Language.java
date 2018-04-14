package com.tryandroid.quizcore.room;

import android.support.annotation.Nullable;

/**
 * Language that is used for a question
 */
public enum Language {
    English(1),
    Russian(0);

    private int databaseId;

    Language(final int databaseId) {
        this.databaseId = databaseId;
    }

    @Nullable
    public static Language valueOf(final int databaseId) {
        for (final Language language : Language.values()) {
            if (language.databaseId == databaseId) {
                return language;
            }
        }
        return null;
    }
}
