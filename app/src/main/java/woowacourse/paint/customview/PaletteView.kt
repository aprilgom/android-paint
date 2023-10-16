package woowacourse.paint.customview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import woowacourse.paint.Color

class PaletteView constructor(
    context: Context,
    attr: AttributeSet? = null,
) : LinearLayout(context, attr) {

    init {
        gravity = Gravity.CENTER_HORIZONTAL
    }

    var onColorSelected: ((Color) -> Unit)? = null
        set(value) {
            field = value
            value?.let { initPalette(value) }
        }

    private fun initPalette(onColorSelected: (Color) -> Unit) {
        Color.values().forEach { color ->
            val view = ColorView.create(
                context = context,
                size = colorViewSize(),
                color = color,
                onColorSelected = onColorSelected,
            )
            addView(view)
        }
    }

    private fun colorViewSize(): Int {
        val screenWidth = minOf(resources.displayMetrics.widthPixels, 1024)
        val numberOfColors = Color.values().size
        return screenWidth / numberOfColors
    }
}
