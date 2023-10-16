package woowacourse.paint.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint.Style.FILL
import android.graphics.Paint.Style.STROKE
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import woowacourse.paint.R
import woowacourse.paint.model.History
import woowacourse.paint.model.Painting
import woowacourse.paint.model.Tools
import woowacourse.paint.model.Tools.CIRCLE
import woowacourse.paint.model.Tools.ERASER
import woowacourse.paint.model.Tools.LINE
import woowacourse.paint.model.Tools.RECTANGLE
import woowacourse.paint.shape.Circle
import woowacourse.paint.shape.Line
import woowacourse.paint.shape.Rectangle

class PaintBoard(context: Context, attrs: AttributeSet) : View(context, attrs) {

    val history: History = History()
    private var _painting: Painting = Painting.newInstance(context)
    val painting
        get() = _painting

    init {
        setupPaintSetting()
    }

    private fun setupPaintSetting() {
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            drawHistory()
            painting.drawPath(this)
        }
    }

    private fun Canvas.drawHistory() {
        history.paintings.forEach { painting ->
            painting.drawPath(this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            ACTION_DOWN -> initPath(event.x, event.y)
            ACTION_MOVE -> drawPath(event.x, event.y)
            ACTION_UP -> savePath()
            else -> super.onTouchEvent(event)
        }
        invalidate()
        return true
    }

    private fun initPath(pointX: Float, pointY: Float) {
        painting.startDraw(pointX, pointY)
    }

    private fun drawPath(pointX: Float, pointY: Float) {
        painting.onDraw(pointX, pointY)
    }

    private fun savePath() {
        history.add(painting)
        _painting = _painting.copy()
    }

    fun changeSize(value: Float) {
        painting.changeSize(value)
    }

    fun changeTool(value: Tools) {
        _painting = when (value) {
            LINE -> painting.changeShape(Line(), STROKE)
            CIRCLE -> painting.changeShape(Circle(), FILL)
            RECTANGLE -> painting.changeShape(Rectangle(), FILL)
            ERASER -> painting.changeShape(Line(), STROKE, true)
        }
    }

    fun changeColor(@ColorRes value: Int) {
        painting.changeColor(context.getColor(value))
    }

    fun clear() {
        history.clear()
        invalidate()
    }

    fun undo() {
        history.undo()
        invalidate()
    }

    fun redo() {
        history.redo()
        invalidate()
    }

    fun restoreHistory(latestHistory: History) {
        history.restoreHistory(latestHistory)
    }

    fun restorePainting(latestPainting: Painting) {
        _painting = latestPainting
    }

    companion object {
        const val DEFAULT_SIZE = 20F

        @ArrayRes
        val COLORS =
            intArrayOf(R.color.red, R.color.orange, R.color.yellow, R.color.green, R.color.blue)
    }
}
