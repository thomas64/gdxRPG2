package nl.t64.game.rpg.sfx;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


public class ShakeCamera {

    private static final float SHAKE_RADIUS = 30f;
    private static final float DIMINISH_THRESHOLD = 2f;
    private static final float DIMINISH_FACTOR = 0.9f;

    private final Vector2 originalPosition;
    private final Vector2 currentPosition;
    private final Vector2 offset;
    private final float originalShakeRadius;

    private float shakeRadius;
    private float randomAngle;
    private boolean isShaking = false;

    public ShakeCamera() {
        this(0f, 0f, SHAKE_RADIUS);
    }

    public ShakeCamera(float shakeRadius) {
        this(0f, 0f, shakeRadius);
    }

    public ShakeCamera(float x, float y) {
        this(x, y, SHAKE_RADIUS);
    }

    public ShakeCamera(float x, float y, float shakeRadius) {
        this.originalPosition = new Vector2(x, y);
        this.currentPosition = new Vector2();
        this.offset = new Vector2();

        this.originalShakeRadius = shakeRadius;
        this.shakeRadius = shakeRadius;
        this.resetShake();
    }

    public void setOriginalPosition(float x, float y) {
        originalPosition.set(x, y);
    }

    public boolean isShaking() {
        return isShaking;
    }

    public void startShaking() {
        isShaking = true;
    }

    public Vector2 getNewShakePosition() {
        computeCameraOffset();
        computeCurrentPosition();
        diminishShake();
        return currentPosition;
    }

    private void computeCameraOffset() {
        offset.x = MathUtils.cosDeg(randomAngle) * shakeRadius;
        offset.y = MathUtils.sinDeg(randomAngle) * shakeRadius;
    }

    private void computeCurrentPosition() {
        currentPosition.set(offset.add(originalPosition));
    }

    private void diminishShake() {
        if (shakeRadius > DIMINISH_THRESHOLD) {
            continueShake();
        } else {
            resetShake();
        }
    }

    private void continueShake() {
        isShaking = true;
        shakeRadius = shakeRadius * DIMINISH_FACTOR;
        seedRandomAngle();
    }

    private void resetShake() {
        isShaking = false;
        shakeRadius = originalShakeRadius;
        seedRandomAngle();
        currentPosition.set(originalPosition);
    }

    private void seedRandomAngle() {
        randomAngle = MathUtils.random(1, 360);
    }

}
