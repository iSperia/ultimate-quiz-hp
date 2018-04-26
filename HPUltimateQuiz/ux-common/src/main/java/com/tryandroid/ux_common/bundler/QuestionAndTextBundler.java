package com.tryandroid.ux_common.bundler;

import android.os.Bundle;

import com.google.gson.Gson;
import com.tryandroid.quizcore.room.dao.QuestionAndText;

import icepick.Bundler;

public class QuestionAndTextBundler implements Bundler<QuestionAndText> {

    @Override
    public void put(String s, QuestionAndText questionAndText, Bundle bundle) {
        bundle.putString(s, new Gson().toJson(questionAndText));
    }

    @Override
    public QuestionAndText get(String s, Bundle bundle) {
        return new Gson().fromJson(bundle.getString(s), QuestionAndText.class);
    }
}
