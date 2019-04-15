package com.pointer.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

fun View.visible() { visibility = View.VISIBLE }

fun View.invisible() { visibility = View.INVISIBLE }

fun View.gone() { visibility = View.GONE }

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)

inline fun <reified T : AppCompatActivity> Context.launchActivity(
    options: Bundle? = null,
    noinline initIntent: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    intent.initIntent()
    startActivity(intent, options)
}

inline fun <reified T : AppCompatActivity> Activity.launchActivityWithResult(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline initIntent: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    intent.initIntent()
    startActivityForResult(intent, requestCode, options)
}