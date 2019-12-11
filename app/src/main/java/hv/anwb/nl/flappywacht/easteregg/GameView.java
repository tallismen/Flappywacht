package hv.anwb.nl.flappywacht.easteregg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import hv.anwb.nl.flappywacht.R;

public class GameView extends SurfaceView implements Runnable {

    private static final int ENEMYCOUNT = 4;

    volatile boolean playing;
    private MediaPlayer backgroundMusic;
    private MediaPlayer hitSound;

    private Thread gameThread = null;

    private Player player;
    private Enemy[] enemies;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Bitmap background;

    private int screenSizeX;
    private int screenSizeY;
    private int score;
    private int highscore;

    public GameView(Context context, int screenSizeX, int screenSizeY) {
        super(context);

        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;

        score = 0;
        highscore = 0;

        backgroundMusic = MediaPlayer.create(context, R.raw.flappywwsound);
        hitSound = MediaPlayer.create(context, R.raw.hit);

        background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                                                                            R.drawable.flappywacht_bg), screenSizeX, screenSizeY, false);

        player = new Player(context, screenSizeX, screenSizeY);
        surfaceHolder = getHolder();
        paint = new Paint();

        enemies = new Enemy[ENEMYCOUNT];
        for (int i = 0; i < ENEMYCOUNT; i++) {
            enemies[i] = new Enemy(context, screenSizeX, screenSizeY);
        }
        backgroundMusic.start();
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_UP:
            player.stopBoosting();
            break;
        case MotionEvent.ACTION_DOWN:
            player.setBoosting();
            score += 5;
            break;
        }
        return true;
    }

    private void update() {
        score++;
        player.update();
        for (Enemy enemy : enemies) {
            enemy.update(score);
            if (Rect.intersects(player.getDetectCollision(), enemy.detectCollision)) {
                hitSound.stop();
                backgroundMusic.stop();
                try {
                    hitSound.prepare();
                    backgroundMusic.prepare();
                }
                catch (Exception e) {
                }
                enemy.x = -200;

                if (score > highscore) {
                    highscore = score;
                }
                score = 0;
                hitSound.start();
                backgroundMusic.start();
                player.setY(70);
            }
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);

            //Achtergrond
            canvas.drawBitmap(
                    background,
                    0,
                    0,
                    paint);

            //Score tekst bovenaan
            Paint paint2 = new Paint();
            paint2.setColor(Color.WHITE);
            paint2.setTextSize(120);
            canvas.drawText(getScoreString(),
                            10,
                            120,
                            paint2);

            //Highscore tekst onderaan
            paint2.setTextSize(50);
            canvas.drawText(getHighScoreString(),
                            10,
                            screenSizeY - 100,
                            paint2);

            //Player
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);

            for (Enemy enemy : enemies) {
                canvas.drawBitmap(
                        enemy.bitmap,
                        enemy.x,
                        enemy.getY(),
                        paint
                );
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private String getScoreString() {
        return "Score: " + score;
    }

    private String getHighScoreString() {
        return "HighScore: " + highscore;
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
        backgroundMusic.stop();
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
        backgroundMusic.start();
    }
}
