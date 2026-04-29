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
    private Paint textPaint;
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

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(12);
    }

    public void setParticipants(List<RaceParticipant> participants, boolean useMyHamster) {
        this.participants = participants;
        this.useMyHamster = useMyHamster;
        invalidate();
    }

    public void startRace(Runnable onFinish) {
        if (isRacing || participants == null) return;

        this.onRaceFinishCallback = onFinish;
        isRacing = true;
        winnerIndex = -1;

        float raceDistance = finishLineX - startX;

        for (RaceParticipant p : participants) {
            p.setX(0);
        }

        animator = ValueAnimator.ofFloat(0, raceDistance);
        animator.setDuration(5000);
        animator.addUpdateListener(animation -> {
            float distance = (float) animation.getAnimatedValue();

            for (int i = 0; i < participants.size(); i++) {
                RaceParticipant p = participants.get(i);
                float progress = distance / raceDistance;
                float pDistance = p.getSpeed() * progress * raceDistance;
                if (pDistance > raceDistance) {
                    pDistance = raceDistance;
                }
                p.setX(pDistance);
            }

            invalidate();

            for (int i = 0; i < participants.size(); i++) {
                if (participants.get(i).getX() >= raceDistance && winnerIndex == -1) {
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

        for (int i = 0; i <= numLanes; i++) {
            float y = i * laneHeight;
            canvas.drawLine(0, y, trackWidth, y, lanePaint);
        }

        canvas.drawLine(startX, 0, startX, trackHeight, lanePaint);
        canvas.drawLine(finishLineX, 0, finishLineX, trackHeight, lanePaint);

        if (participants != null) {
            for (int i = 0; i < participants.size(); i++) {
                RaceParticipant p = participants.get(i);
                float y = i * laneHeight + laneHeight / 2;
                float x = startX + p.getX();

                float size = 67;

                if (p.getCombinedBitmap() != null && !p.getCombinedBitmap().isRecycled()) {
                    Bitmap scaled = Bitmap.createScaledBitmap(p.getCombinedBitmap(), (int)size, (int)size, true);
                    canvas.drawBitmap(scaled, x - size/2, y - size/2, null);
                } else if (p.getBitmap() != null && !p.getBitmap().isRecycled()) {
                    Bitmap scaled = Bitmap.createScaledBitmap(p.getBitmap(), (int)size, (int)size, true);
                    canvas.drawBitmap(scaled, x - size/2, y - size/2, null);
                }
            }
        }
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