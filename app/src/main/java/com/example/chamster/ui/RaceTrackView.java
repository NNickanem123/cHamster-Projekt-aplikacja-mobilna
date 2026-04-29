package com.example.chamster.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class RaceTrackView extends View {

    private Paint lanePaint;
    private List<RaceParticipant> participants;
    private float trackWidth;
    private float trackHeight;
    private float laneHeight;
    private float startX;
    private float finishLineX;
    private ValueAnimator animator;
    private boolean isRacing = false;
    private Runnable onRaceFinishCallback;
    private int winnerIndex = -1;
    private boolean useMyHamster = false;
    private String trackType = "straight";

    public RaceTrackView(Context context) {
        super(context);
        init();
    }

    public RaceTrackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        lanePaint = new Paint();
        lanePaint.setColor(Color.WHITE);
        lanePaint.setStrokeWidth(3);
        lanePaint.setAntiAlias(true);
    }

    public void setParticipants(List<RaceParticipant> participants, boolean useMyHamster) {
        this.participants = participants;
        this.useMyHamster = useMyHamster;
        invalidate();
    }

    public void setTrackType(String type) {
        this.trackType = type;
        invalidate();
    }

    public void startRace(Runnable onFinish) {
        if (isRacing || participants == null) return;

        this.onRaceFinishCallback = onFinish;
        isRacing = true;
        winnerIndex = -1;

        final float maxDistance;
        final boolean isCurved = "curved".equals(trackType);

        if (isCurved) {
            maxDistance = 360f;
        } else {
            maxDistance = finishLineX - startX;
        }

        for (RaceParticipant p : participants) {
            p.setX(0);
        }

        animator = ValueAnimator.ofFloat(0, maxDistance);
        animator.setDuration(5000);
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();

            for (int i = 0; i < participants.size(); i++) {
                RaceParticipant p = participants.get(i);
                float progress = value / maxDistance;
                float pDistance = p.getSpeed() * progress * maxDistance;
                if (pDistance > maxDistance) {
                    pDistance = maxDistance;
                }
                p.setX(pDistance);
            }

            invalidate();

            for (int i = 0; i < participants.size(); i++) {
                if (participants.get(i).getX() >= maxDistance && winnerIndex == -1) {
                    winnerIndex = i;
                }
            }

            if (winnerIndex != -1 && animator.isRunning()) {
                animator.cancel();
                isRacing = false;
                if (onRaceFinishCallback != null) {
                    onRaceFinishCallback.run();
                }
            }
        });
        animator.start();
    }

    public int getWinnerIndex() {
        return winnerIndex;
    }

    public boolean isRacing() {
        return isRacing;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        trackWidth = getWidth();
        trackHeight = getHeight();

        if (trackWidth <= 0 || trackHeight <= 0) return;

        int numLanes = useMyHamster ? 5 : 4;
        laneHeight = trackHeight / numLanes;
        startX = 70;
        finishLineX = trackWidth - 70;

        canvas.drawColor(Color.parseColor("#2d5016"));

        if ("straight".equals(trackType)) {
            drawStraightTrack(canvas, numLanes);
            canvas.drawLine(startX, 0, startX, trackHeight, lanePaint);
            canvas.drawLine(finishLineX, 0, finishLineX, trackHeight, lanePaint);
        } else {
            drawCurvedTrack(canvas, numLanes);
        }

        if (participants != null) {
            for (int i = 0; i < participants.size(); i++) {
                RaceParticipant p = participants.get(i);
                float x, y;
                float size = 67;

                if ("curved".equals(trackType)) {
                    float centerX = trackWidth / 2;
                    float centerY = trackHeight / 2;
                    float maxRadius = Math.min(trackWidth, trackHeight) / 2 - 50;
                    float radius = maxRadius - i * 30;
                    if (radius < 20) radius = 20;
                    float angle = (float) (p.getX() * Math.PI * 2 / 360);
                    x = centerX + (float) Math.cos(angle - Math.PI / 2) * radius;
                    y = centerY + (float) Math.sin(angle - Math.PI / 2) * radius;
                } else {
                    float yPos = i * laneHeight + laneHeight / 2;
                    x = startX + p.getX();
                    y = yPos;
                }

                Bitmap bitmapToDraw = p.getCombinedBitmap() != null ? p.getCombinedBitmap() : p.getBitmap();

                if (bitmapToDraw != null && !bitmapToDraw.isRecycled()) {
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmapToDraw, (int) size, (int) size, true);
                    canvas.drawBitmap(scaled, x - size / 2, y - size / 2, null);
                }
            }
        }
    }

    private void drawStraightTrack(Canvas canvas, int numLanes) {
        for (int i = 0; i <= numLanes; i++) {
            float y = i * laneHeight;
            canvas.drawLine(0, y, trackWidth, y, lanePaint);
        }
    }

    private void drawCurvedTrack(Canvas canvas, int numLanes) {
        float centerX = trackWidth / 2;
        float centerY = trackHeight / 2;
        float maxRadius = Math.min(trackWidth, trackHeight) / 2 - 50;
        float minRadius = maxRadius - (numLanes - 1) * 30;
        if (minRadius < 20) minRadius = 20;

        for (int i = 0; i < numLanes; i++) {
            float radius = maxRadius - i * 30;
            if (radius >= minRadius) {
                canvas.drawCircle(centerX, centerY, radius, lanePaint);
            }
        }

        for (int i = 0; i < 8; i++) {
            float angle = (float) (i * Math.PI * 2 / 8);
            float x2 = centerX + (float) Math.cos(angle) * maxRadius;
            float y2 = centerY + (float) Math.sin(angle) * maxRadius;
            canvas.drawLine(centerX, centerY, x2, y2, lanePaint);
        }

        canvas.drawCircle(centerX, centerY, 15, lanePaint);
    }

    public static class RaceParticipant {
        private String name;
        private Bitmap bitmap;
        private Bitmap combinedBitmap;
        private float speed;
        private float x;

        public RaceParticipant(String name, Bitmap bitmap, Bitmap combinedBitmap, float speed) {
            this.name = name;
            this.bitmap = bitmap;
            this.combinedBitmap = combinedBitmap;
            this.speed = speed;
            this.x = 0;
        }

        public String getName() { return name; }
        public Bitmap getBitmap() { return bitmap; }
        public Bitmap getCombinedBitmap() { return combinedBitmap; }
        public float getSpeed() { return speed; }
        public float getX() { return x; }
        public void setX(float x) { this.x = x; }
    }
}