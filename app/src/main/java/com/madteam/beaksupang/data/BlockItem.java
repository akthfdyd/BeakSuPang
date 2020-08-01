
package com.madteam.beaksupang.data;

import com.madteam.beaksupang.view.GameView;

public class BlockItem {
    private int value; // 블럭의 종류
    private int xPosition; // 현재 좌표
    private int yPosition;
    private float posTargetX; // 목적지 좌표
    private float posTargetY;
    private float posCurrentX; // 현재 이동중인 좌표
    private float posCurrentY;
    private boolean isInit = true;
    private boolean isPress;
    private boolean isMoving;

    enum Direction {
        LEFT, RIGHT, UP, DOWN, NONE
    }

    public int getValue() {
        return value;
    }

    public float getPosTargetX() {
        return posTargetX;
    }

    public float getPosTargetY() {
        return posTargetY;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public void setYPosition(int position) {
        this.yPosition = position;
    }

    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public boolean isPress() {
        return isPress;
    }

    public void setPress(boolean isPress) {
        this.isPress = isPress;
    }

    public void setInit() {
        isInit = true;
    }

    public void initPosTargetXY(float posX, float posY) {
        this.posTargetX = posX;
        this.posTargetY = posY;
        if (isInit) {
            posCurrentX = posX;
            posCurrentY = -50 * (yPosition * 2) - 100 - xPosition * 20;
            isInit = false;
        }
    }

    public void setPosTargetXY(float posX, float posY) {
        posCurrentX = posTargetX;
        posTargetX = posX;
        posCurrentY = posTargetY;
        posTargetY = posY;
    }

    public float getPosCurrentX() {
        if (posTargetX == posCurrentX) {
            isMoving = false;
            return posTargetX;
        }
        float direction = posTargetX - posCurrentX > 0 ? GameView.gameSpeedByResolution : -1 * GameView.gameSpeedByResolution;
        posCurrentX += 15 * direction;
        if (Math.abs(posTargetX - posCurrentX) < 30) {
            posCurrentX = posTargetX;
        }
        isMoving = true;
        return posCurrentX;
    }

    public float getPosCurrentY() {
        if (posTargetY == posCurrentY) {
            isMoving = false;
            return posTargetY;
        }
        float direction = posTargetY - posCurrentY > 0 ? GameView.gameSpeedByResolution : -1 * GameView.gameSpeedByResolution;
        posCurrentY += (15 + posCurrentY / (70 * GameView.gameSpeedByResolution)) * direction;
        if (Math.abs(posTargetY - posCurrentY) < 30) {
            posCurrentY = posTargetY;
        }
        isMoving = true;
        return posCurrentY;
    }

}
