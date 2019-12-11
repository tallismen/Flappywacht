package hv.anwb.nl.flappywacht.easteregg

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import hv.anwb.nl.flappywacht.R
import java.util.*

class Enemy(context: Context, screenX: Int, screenY: Int) {
    val bitmap: Bitmap
    var x: Int
    var y: Int
        private set
    private var speed: Int
    private val maxX: Int
    private val minX: Int
    private val maxY: Int
    private val minY: Int
    val detectCollision: Rect

    fun update(score: Int) {
        val extrasnelheid = score / 100
        if (extrasnelheid > 0) x -= extrasnelheid
        x -= speed

        if (x < minX - bitmap.width) {
            val generator = Random()
            speed = generator.nextInt(5) + 5
            x = maxX
            y = generator.nextInt(maxY) - bitmap.height
        }
        detectCollision.left = x
        detectCollision.top = y
        detectCollision.right = x + bitmap.width
        detectCollision.bottom = y + bitmap.height
    }

    init {
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.auto)
        maxX = screenX
        maxY = screenY
        minX = 0
        minY = 0
        val generator = Random()
        speed = generator.nextInt(3) + 5
        x = screenX
        y = generator.nextInt(maxY) - bitmap.height
        detectCollision = Rect(x, y, bitmap.width, bitmap.height)
    }
}