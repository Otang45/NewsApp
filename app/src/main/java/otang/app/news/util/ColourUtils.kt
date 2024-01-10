package otang.app.news.util

import android.content.Context
import android.util.TypedValue
import androidx.annotation.ColorInt

object ColourUtils {
    @ColorInt
    fun getColorAttr(context: Context, colorRes: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(colorRes, typedValue, true)
        return typedValue.data
    }
}