package hv.anwb.nl.flappywacht.easteregg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import hv.anwb.nl.flappywacht.R;

public class Wegenwacht {
    private static final int GRAVITY = -20;
    private static final int MIN_SPEED = 1;
    private static final int MAX_SPEED = 50;

    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed;
    private boolean boosting;
    private int maxY;
    private int minY;
    private Rect detectCollision;

    public Wegenwacht(Context context, int screenX, int screenY) {
        x = 75;
        y = 50;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.wegenwacht);

        maxY = screenY - bitmap.getHeight() - 200;
        minY = 200;

        boosting = false;
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void setBoosting() {
        boosting = true;
    }

    public void stopBoosting() {
        boosting = false;
    }

    public void update() {
        if (boosting) {
            speed += 8;
        } else {
            speed -= 10;
        }
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }
        y -= speed + GRAVITY;

        //Ondergrens
        if (y < minY) {
            y = minY;
        }
        //Bovengrens
        if (y > maxY) {
            y = maxY;
        }

        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}
