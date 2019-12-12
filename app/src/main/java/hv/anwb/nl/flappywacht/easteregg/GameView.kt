package hv.anwb.nl.flappywacht.easteregg

import android.content.Context
import android.graphics.*
import android.media.MediaPlayer
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import hv.anwb.nl.flappywacht.R

class GameView(context: Context, private val screenSizeX: Int, private val screenSizeY: Int) : SurfaceView(context), Runnable {
    @Volatile
    var playing = false
    private val backgroundMusic: MediaPlayer = MediaPlayer.create(context, R.raw.flappywwsound)
    private val hitSound: MediaPlayer = MediaPlayer.create(context, R.raw.hit)

    private val player: Player = Player(context, screenSizeX, screenSizeY)
    private val enemies: Array<Enemy?>

    private val paint: Paint = Paint()
    private val surfaceHolder: SurfaceHolder = holder
    private val background: Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.resources,
            R.drawable.flappywacht_bg), screenSizeX, screenSizeY, false)

    private var score = 0
    private var highscore = 0
    private var scoreString = "Score: $score"
    private var highScoreString = "HighScore: $highscore"

    override fun run() {
        while (playing) {
            update()
            draw()
        }
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        when (motionEvent.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_UP -> player.stopBoosting()
            MotionEvent.ACTION_DOWN -> {
                player.setBoosting()
                score += 5
            }
        }
        return true
    }

    private fun update() {
        score++
        player.update()
        for (enemy in enemies) {
            enemy?.update(score)
            if (Rect.intersects(player.detectCollision, enemy.detectCollision)) {
                hitSound.stop()
                backgroundMusic.stop()
                try {
                    hitSound.prepare()
                    backgroundMusic.prepare()
                } catch (e: Exception) {
                }
                enemy.x = -200
                if (score > highscore) {
                    highscore = score
                }
                score = 0
                hitSound.start()
                backgroundMusic.start()
                player.y = 70
            }
        }
    }

    private fun draw() {
        surfaceHolder.apply {
            if (surface.isValid) unlockCanvasAndPost(lockCanvas().apply {
                drawColor(Color.WHITE)

                //Achtergrond
                drawBitmap(background, 0f, 0f, paint)

                //Score tekst bovenaan
                drawText(scoreString, 10f, 120f, Paint().apply {
                    color = Color.WHITE
                    textSize = 120f
                })

                //Highscore tekst onderaan
                drawText(highScoreString, 10f, screenSizeY - 100.toFloat(), Paint().apply {
                    color = Color.WHITE
                    textSize = 50f
                })

                //Player
                drawBitmap(player.bitmap, player.x.toFloat(), player.y.toFloat(), paint)

                enemies.forEach { it?.apply { drawBitmap(bitmap, x.toFloat(), y.toFloat(), paint) } }
            })
        }
    }

    fun pause() {
        playing = false
        backgroundMusic.stop()
    }

    fun resume() {
        playing = true
        backgroundMusic.start()
    }

    init {
        enemies = arrayOfNulls(ENEMYCOUNT)
        for (i in 0 until ENEMYCOUNT) {
            enemies[i] = Enemy(context, screenSizeX, screenSizeY)
        }
        backgroundMusic.start()
    }

    companion object {
        private const val ENEMYCOUNT = 4
    }
}