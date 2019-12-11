package hv.anwb.nl.flappywacht;

import android.graphics.Point;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

import hv.anwb.nl.flappywacht.easteregg.GameView;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        gameView = new GameView(this, size.x, size.y);
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
