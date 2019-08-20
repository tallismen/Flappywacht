package hv.anwb.nl.flappywacht

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hv.anwb.nl.flappywacht.easteregg.GameView

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        gameView = GameView(this, size.x, size.y)
        setContentView(gameView)
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }
}