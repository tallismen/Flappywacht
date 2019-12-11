package hv.anwb.nl.flappywacht.easteregg

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import hv.anwb.nl.flappywacht.R

class Player(context: Context, screenX: Int, screenY: Int) {
    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.wegenwacht)
    val x = 75
    var y = 50
    private var speed = 1
    private var boosting: Boolean
    private val maxY: Int
    private val minY: Int
    val detectCollision: Rect


    fun setBoosting() {
        boosting = true
    }

    fun stopBoosting() {
        boosting = false
    }

    fun update() {
        if (boosting) speed += 8 else speed -= 10
        if (speed > MAX_SPEED) speed = MAX_SPEED
        if (speed < MIN_SPEED) speed = MIN_SPEED
        y -= speed + GRAVITY

        if (y < minY) y = minY //Ondergrens
        if (y > maxY) y = maxY //Bovengrens

        detectCollision.apply {
            left = x
            top = y
            right = x + bitmap.width
            bottom = y + bitmap.height
        }
    }

    init {
        maxY = screenY - bitmap.height - 200
        minY = 200
        boosting = false
        detectCollision = Rect(x, y, bitmap.width, bitmap.height)
    }

    companion object {
        private const val GRAVITY = -15
        private const val MIN_SPEED = 1
        private const val MAX_SPEED = 40
    }
}