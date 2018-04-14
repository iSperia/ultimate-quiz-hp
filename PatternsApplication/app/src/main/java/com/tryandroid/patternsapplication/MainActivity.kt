package com.tryandroid.patternsapplication

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.accessibility.AccessibilityManager
import com.tryandroid.patternsapplication.singleton.SingletoneModule

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val a = SingletoneModule.getInstance(applicationContext).a

        val service = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    }
}
