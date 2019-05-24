package hv.anwb.nl.flappywacht

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hv.anwb.nl.flappywacht.easteregg.GameView

class MainActivity : AppCompatActivity() {

    private var gameView: GameView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Point().let {
            windowManager.defaultDisplay.getSize(it)
            gameView = GameView(this, it.x, it.y)
            setContentView(gameView)
        }
    }

    override fun onPause() {
        super.onPause()
        gameView?.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView?.resume()
    }
}