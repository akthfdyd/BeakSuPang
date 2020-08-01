
package com.madteam.beaksupang.view;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.madteam.beaksupang.InputScoreDialog;
import com.madteam.beaksupang.R;
import com.madteam.beaksupang.common.Const;
import com.madteam.beaksupang.data.Blocks;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public interface OnGameListener {

        void onFinish();

        void onLevelUp();

        void onReShuffle();

    }

    private OnGameListener onGameListener;

    public void setOnGameListener(OnGameListener onGameListener) {

        this.onGameListener = onGameListener;
    }

    private static final int BLOCK_COLOR_1 = Color.argb(0xff, 0xfc, 0xb2, 0x00); // yellow
    private static final int BLOCK_COLOR_2 = Color.argb(0xff, 0x00, 0xad, 0xdc); // purple
    private static final int BLOCK_COLOR_3 = Color.argb(0xff, 0x00, 0xac, 0x4e); // green
    private static final int BLOCK_COLOR_4 = Color.argb(0xff, 0xea, 0x15, 0x7a); // pink
    private static final int BLOCK_COLOR_5 = Color.argb(0xff, 0x73, 0x8a, 0xc8); // lightblue
    private static final int BLOCK_COLOR_6 = Color.argb(0xff, 0xff, 0x00, 0x00); // red
    private static final int BLOCK_COLOR_7 = Color.argb(0xff, 0x00, 0x00, 0xff); // blue
    private static final int BLOCK_COLOR_8 = Color.argb(0xff, 0x88, 0x88, 0x88); // grey
    public static final int[] BLOCK_COLORS = {
            BLOCK_COLOR_1, BLOCK_COLOR_2, BLOCK_COLOR_3, BLOCK_COLOR_4,
            BLOCK_COLOR_5, BLOCK_COLOR_6, BLOCK_COLOR_7, BLOCK_COLOR_8
    };

    public static int[] stageScore = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    private long mCurrTimeInMillis = 0;

    private SevenThread thread;
    private Blocks blocks;
    private float width;
    private float height;
    public static float gameSpeedByResolution;
    private float padding;
    private float blockSize;
    private float touchMoveSize;
    private float startYPos;
    private final int BLOCK_NUM = Const.BLOCK_NUM;
    private static int score = 0;
    private static int totalScore = 0;
    private int stage = 1;
    private int timeout;

    public GameView(Context context, Blocks blocks) {
        super(context);
        this.blocks = blocks;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new SevenThread(getHolder());
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.width = width;
        this.height = height;
        gameSpeedByResolution = height / 1280;
        padding = width * 0.02f;
        blockSize = (width - padding * 2) / BLOCK_NUM;
        touchMoveSize = blockSize * 0.6f;
        startYPos = height / 2 - (width / 2);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.setLoop(false);
        thread.setStageLoop(false);
        try {
            thread.join();
        } catch (Exception e) {
        }
    }

    class SevenThread extends Thread {

        private SurfaceHolder holder;
        private boolean isLoop;
        private boolean isStageLoop;
        private Canvas canvas;
        private Paint paint;

        private BitmapFactory.Options getBitmapSize() {
            BitmapFactory.Options options = new BitmapFactory.Options();
            return options;
        }

        Bitmap bitmapBlock1 = BitmapFactory
                .decodeResource(getResources(), R.drawable.traning_black, getBitmapSize());
        Bitmap bitmapBlock2 = BitmapFactory.decodeResource(getResources(), R.drawable.traning_blue,
                getBitmapSize());
        Bitmap bitmapBlock3 = BitmapFactory
                .decodeResource(getResources(), R.drawable.traning_purple, getBitmapSize());
        Bitmap bitmapBlock4 = BitmapFactory.decodeResource(getResources(),
                R.drawable.traning_green, getBitmapSize());
        Bitmap bitmapBlock5 = BitmapFactory.decodeResource(getResources(),
                R.drawable.traning_red, getBitmapSize());
        Bitmap bitmapBlock6 = BitmapFactory.decodeResource(getResources(),
                R.drawable.traning_white, getBitmapSize());
        Bitmap bitmapBlock7 = BitmapFactory
                .decodeResource(getResources(), R.drawable.traning_yellow, getBitmapSize());
        Bitmap bitmapBlock8 = BitmapFactory.decodeResource(getResources(),
                R.drawable.traning_grey, getBitmapSize());

        Bitmap[] bitmapBlockArray = {
                bitmapBlock1, bitmapBlock2, bitmapBlock3, bitmapBlock4, bitmapBlock5,
                bitmapBlock6, bitmapBlock7, bitmapBlock8
        };

        public SevenThread(SurfaceHolder _holder) {
            this.holder = _holder;
            this.isLoop = true;
            this.isStageLoop = true;
        }

        public void setLoop(boolean _isLoop) {
            this.isLoop = _isLoop;
        }

        public void setStageLoop(boolean _isStageLoop) {
            this.isStageLoop = _isStageLoop;
        }

        @Override
        public void run() {
            score = 0;
            totalScore = 0;
            stage = 1;
            paint = new Paint();
            paint.setAntiAlias(true);
            while (isLoop) {
                initStage();
                while (isStageLoop) {
                    onStage();
                }
                stageScore[stage - 1] = score;
                if (stage < 10) {
                    if (isStageClear()) {
                        goNextStage();
                    } else {
                        gameOver();
                    }
                } else {
                    gameOver();
                    // 엔딩 및 스코어 페이지 노출
                    // if (onGameListener != null) {
                    // Log.i("evey","GameView  onGameListener!= null");
                    // onGameListener.onFinish();
                    // }
                    // Log.i("evey","GameView  onGameListener ========= null");
                }
            }
        }

        private void initStage() {
            blocks.dropBlocks();
            do {
                blocks.dropBlocks();
                blocks.resetYPosition();
            } while (blocks.checkAndRemove() > 0);
            mCurrTimeInMillis = Calendar.getInstance().getTimeInMillis();
            timeout = Const.INITIAL_TIMEOUT - ((stage - 1) * Const.TIMEOUT_DECREASE);
        }

        private void onStage() {
            try {
                canvas = holder.lockCanvas(null);
                synchronized (holder) {
                    drawOutline();
                    // drawGrid();
                    drawTimeBar();
                    drawStage();
                    drawBlocks();
                    drawScore();
                }
            } catch (Exception e) {
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            if (calcTimeBar() <= (padding * 3) || (score >= Const.MAX_SCORE_PER_STAGE)) {
                isStageLoop = false;
            }
        }

        private boolean isStageClear() {
            return (score >= (((stage - 1) * Const.NEED_SCORE_INCREASE) + Const.SAFE_SCORE_PER_STAGE));
        }

        private void goNextStage() {
            Log.i("evey", "Stage " + stage + " Score : " + stageScore[stage - 1]);
            score = 0;
            stage++;
            blocks.removeAllBlocks();
            isStageLoop = true;
        }

        private void gameOver() {
            for (int i = 0; i < 10; i++) {
                totalScore = (int) (totalScore + stageScore[i]
                        * (1 + (i * Const.SCORE_RATE_PER_STAGE)));
                Log.i("evey", "Stage " + (i + 1) + " Score : " + stageScore[i]);
            }
            totalScore = totalScore * Const.CALC_SCORE;
            Log.i("evey", "gameOver totalScore : " + totalScore);
            isStageLoop = false;
            isLoop = false;
            if (onGameListener != null) {
                onGameListener.onFinish();
            }
        }

        private void drawOutline() {
            paint.setColor(Color.argb(255, 0x88, 0x00, 0x00)); // 배경색
            canvas.drawRect(0, 0, width, height, paint);
            paint.setColor(Color.argb(255, 0xad, 0x54, 0x1d)); // 패딩 내 배경색
            canvas.drawRect(padding, padding, width - padding, height - padding, paint);
            paint.setColor(Color.argb(255, 0xad, 0x54, 0x1d)); // 게임공간
            canvas.drawRect(padding, startYPos + padding, width - padding, width - 2
                    * padding + startYPos + padding, paint);
            paint.setColor(Color.argb(255, 0x65, 0x30, 0x0f)); // 게임 위쪽 공간
            canvas.drawRect(padding, padding * 2, width - (padding),
                    ((height - width) / 2), paint);
            paint.setColor(Color.argb(255, 0x65, 0x30, 0x0f)); // 게임 아래쪽 공간
            canvas.drawRect(padding, ((height - width) / 2) + width, width
                    - (padding), height - (padding * 2), paint);
        }

        private void drawGrid() {
            paint.setColor(Color.BLACK);
            for (int i = 1; i < BLOCK_NUM; i++) {
                canvas.drawLine(padding, startYPos + padding + blockSize * i, width
                        - padding, startYPos
                        + padding + blockSize * i, paint);
                canvas.drawLine(padding + blockSize * i, startYPos + padding, padding
                                + blockSize * i,
                        startYPos + width - padding, paint);
            }
        }

        private void drawTimeBar() {
            paint.setColor(Color.argb(255, 0xad, 0x54, 0x1d)); // 남은시간배경
            canvas.drawRect(padding * 2, padding * 3, width - (padding * 2),
                    ((height - width) / 2) - padding, paint);
            paint.setColor(Color.argb(255, 0x88, 0x00, 0x00)); // 남은시간
            canvas.drawRect(padding * 3, padding * 4, calcTimeBar(),
                    ((height - width) / 2) - (padding * 2), paint);
        }

        private int calcTimeBar() {
            float fullLength = width - (padding * 4);
            int timeoutMillis = timeout * Const.MILLIS_IN_SEC;
            float increase = Calendar.getInstance().getTimeInMillis() - mCurrTimeInMillis;
            int result = (int) (fullLength - ((fullLength) * (increase / timeoutMillis)));
            if (result <= (padding * 3)) {
                return (int) (padding * 3);
            }
            return result;
        }

        private void drawStage() {
//            paint.setTextSize(((height - width) / 2) - (padding * 11));
            paint.setTextSize(width / 10);

            paint.setColor(Color.argb(255, 0xaa, 0xaa, 0xaa));
            canvas.drawText("취업 시즌 " + stage, padding * 4, (padding * 4)
                    + (((height - width) / 2) - (padding * 9)), paint);
        }

        private void drawBlocks() {
            for (int i = 0; i < blocks.size(); i++) {
                for (int j = 0; j < blocks.get(i).size(); j++) {
                    float targetX = padding + blockSize / 2 + blockSize * i;
                    float targetY = startYPos + width - padding - blockSize / 2 - blockSize * j;
                    blocks.get(i).get(j).initPosTargetXY(targetX, targetY);
                    float currentY = blocks.get(i).get(j).getPosCurrentY();
                    float currentX = blocks.get(i).get(j).getPosCurrentX();
                    paint.setColor(BLOCK_COLORS[blocks.get(i).get(j).getValue()]);
                    RectF rectf = new RectF(currentX
                            - ((width - (padding * 2)) / (Const.BLOCK_NUM * 2)), currentY
                            - ((width - (padding * 2)) / (Const.BLOCK_NUM * 2)), currentX
                            - ((width - (padding * 2)) / (Const.BLOCK_NUM * 2)) + blockSize - 1,
                            currentY - ((width - (padding * 2)) / (Const.BLOCK_NUM * 2))
                                    + blockSize - 1);
                    canvas.drawRoundRect(rectf, width / 24, width / 24, paint);
                    canvas.drawBitmap(bitmapBlockArray[blocks.get(i).get(j).getValue()],
                            getBitmapPadding(currentX), getBitmapPadding(currentY), paint);
                }
            }
        }

        private void drawScore() {
//            paint.setTextSize(((height - width) / 2) - (padding * 11));
            paint.setTextSize(width / 10);

            paint.setColor(Color.argb(255, 0xaa, 0xaa, 0xaa));
            canvas.drawText("자소서 수 : " + getScore(), padding * 4, ((height - width) / 2) + width
                    + (padding * 2) + (((height - width) / 2) - (padding * 9)), paint);
        }

        private float getBitmapPadding(float position) {
            return position
                    - ((width - (padding * 2)) / (Const.BLOCK_NUM * 2))
                    + ((((width - (padding * 2)) / (Const.BLOCK_NUM * 2)) - (bitmapBlock1
                    .getHeight()) / 2));
        }
    }

    class Pos {
        private int x;
        private int y;
    }

    public Pos getPosition(float x, float y) {
        Pos pos = new Pos();
        pos.x = (int) ((x - padding) / blockSize);
        pos.y = BLOCK_NUM - (int) ((y - startYPos - padding) / blockSize) - 1;
        return pos;
    }

    Pos pos;
    private float startXpos;
    private float startYpos;
    private boolean selectedBlock;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    selectedBlock = true;
                    startXpos = event.getX();
                    startYpos = event.getY();
                    Pos pos = getPosition(startXpos, startYpos);
                    this.pos = pos;
                } catch (Exception e) {
                }
                return true;
            case MotionEvent.ACTION_UP:
                selectedBlock = false;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (selectedBlock) {
                    try {
                        if (startXpos - event.getX() < touchMoveSize * -1) {
                            blocks.swap(pos.x, pos.y, pos.x + 1, pos.y);
                            checkMatching();
                        } else if (startXpos - event.getX() > touchMoveSize) {
                            blocks.swap(pos.x, pos.y, pos.x - 1, pos.y);
                            checkMatching();
                        } else if (startYpos - event.getY() < touchMoveSize * -1) {
                            blocks.swap(pos.x, pos.y, pos.x, pos.y - 1);
                            checkMatching();
                        } else if (startYpos - event.getY() > touchMoveSize) {
                            blocks.swap(pos.x, pos.y, pos.x, pos.y + 1);
                            checkMatching();
                        }
                    } catch (Exception e) {
                    }
                }
                return true;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void checkMatching() {
        selectedBlock = false;
        if (blocks.checkAndRemove() == 0) {
            blocks.reswap();
            return;
        }
        do {
            setScore(getScore() + 1);
            mCurrTimeInMillis = (long) (mCurrTimeInMillis + (timeout
                    * Const.MILLIS_IN_SEC * Const.INCREASE_PERCENTAGE));
            blocks.dropBlocks();
        } while (blocks.checkAndRemove() > 0);
        selectedBlock = false;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        GameView.score = score;
    }

    public static int getTotalScore() {
        return totalScore;
    }

    public static void setTotalScore(int score) {
        GameView.totalScore = score;
    }

}
