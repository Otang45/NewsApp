package otang.app.news.util

import android.content.Context
import android.content.SharedPreferences

class PrefUtils(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun saveAs(tag: String?, value: Boolean) {
        editor.putBoolean(tag, value).commit()
    }

    fun getBoolean(tag: String?): Boolean {
        return preferences.getBoolean(tag, false)
    }

    fun saveAs(tag: String?, value: Float) {
        editor.putFloat(tag, value).commit()
    }

    fun getFloat(tag: String?): Float {
        return preferences.getFloat(tag, 0f)
    }

    fun saveAs(tag: String?, value: Int) {
        editor.putInt(tag, value).commit()
    }

    fun getInteger(tag: String?): Int {
        return preferences.getInt(tag, 0)
    }

    fun saveAs(tag: String?, value: Long) {
        editor.putLong(tag, value).commit()
    }

    fun getLong(tag: String?): Long {
        return preferences.getLong(tag, 0)
    }

    fun saveAs(tag: String?, value: String?) {
        editor.putString(tag, value).commit()
    }

    fun getString(tag: String?): String? {
        return preferences.getString(tag, "")
    }

    companion object {
        private const val PREFS = "otang.news_preferences"
    }
}