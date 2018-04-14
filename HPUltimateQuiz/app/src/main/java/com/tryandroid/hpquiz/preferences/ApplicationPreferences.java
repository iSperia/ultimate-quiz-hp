package com.tryandroid.hpquiz.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationPreferences {

    private static final String PREFERENCES_SCOPE = "com.tryandroid.hpquiz.preferences";

    private static final String APPLICATION_DATABASE_VERSION = "database.version";

    private final SharedPreferences sharedPreferences;

    public ApplicationPreferences(final Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_SCOPE, 0);
    }

    public DatabaseVersion getDatabaseVersion() {
        return DatabaseVersion.valueOf(sharedPreferences.getString(APPLICATION_DATABASE_VERSION,
                DatabaseVersion.NoDatabase.toString()));
    }

    public void setDatabaseVersion(final DatabaseVersion databaseVersion) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APPLICATION_DATABASE_VERSION, databaseVersion.toString());
        editor.commit();
    }

}
