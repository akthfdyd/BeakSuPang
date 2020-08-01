
package com.madteam.beaksupang.data;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;

import com.madteam.beaksupang.common.Const;

public class Blocks {

    private ArrayList<ArrayList<BlockItem>> blocks;
    private final int BLOCK_NUM = Const.BLOCK_NUM;

    public Blocks() { // 이차원 배열 생성
        blocks = new ArrayList<ArrayList<BlockItem>>();
        for (int i = 0; i < BLOCK_NUM; i++) {
            ArrayList<BlockItem> row = new ArrayList<BlockItem>(BLOCK_NUM);
            blocks.add(row);
        }
    }

    public ArrayList<ArrayList<BlockItem>> getBlocks() { // 이차원 배열 전체 리턴
        return blocks;
    }

    public int size() {
        return blocks.size();
    }

    public ArrayList<BlockItem> get(int position) { // 하나의 행 리턴
        return blocks.get(position);
    }

    public void resetYPosition() { // 이차원배열의 Y값 초기화
        for (int i = 0; i < BLOCK_NUM; i++) {
            for (int j = 0; j < BLOCK_NUM; j++) {
                blocks.get(i).get(j).setYPosition(j);
            }
        }
    }

    public boolean isAllStoped() { // 전부멈춰있는지
        for (int i = 0; i < BLOCK_NUM; i++) {
            for (int j = 0; j < BLOCK_NUM; j++) {
                if (blocks.get(i).get(j).isMoving()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void dropBlocks() { // 블럭 떨어뜨리기
        Random rnd = new Random();
        for (int i = 0; i < BLOCK_NUM; i++) {
            if (isFull()) { // 한 행이 가득차있으면
                break;
            }
            for (int j = 0; j < BLOCK_NUM; j++) {
                if (blocks.get(i).size() == BLOCK_NUM) { // 한줄 사이즈가 같으면 패스
                    continue;
                }
                int rndValue = rnd.nextInt(BLOCK_NUM); // 랜덤값생성
                BlockItem item = new BlockItem(); // 새 블럭 만들고 위치값과 랜덤값(색상)을 넣어줌
                item.setXPosition(i);
                item.setYPosition(j);
                item.setValue(rndValue);
                blocks.get(i).add(item);
            }
        }
    }

    public boolean isFull() { // 전부 가득 차 있는가
        for (int i = 0; i < BLOCK_NUM; i++) {
            if (blocks.get(i).size() < BLOCK_NUM) {
                return false;
            }
        }
        return true;
    }

    private int swapPosX1;
    private int swapPosY1;
    private int swapPosX2;
    private int swapPosY2;

    public void reswap() { // 멈춰있지 않으면 다시 블럭아이템을 바꾸기
        while (!isAllStoped()) {
        }
        swap(swapPosX2, swapPosY2, swapPosX1, swapPosY1);
    }

    public void swap(int posX1, int posY1, int posX2, int posY2) {
        // 두개의 블럭아이템 바꾸기
        swapPosX1 = posX1;
        swapPosX2 = posX2;
        swapPosY1 = posY1;
        swapPosY2 = posY2;
        BlockItem item1 = blocks.get(posX1).get(posY1);
        BlockItem item2 = blocks.get(posX2).get(posY2);
        BlockItem temp = new BlockItem();
        temp.setPosTargetXY(item1.getPosTargetX(), item1.getPosTargetY());
        item1.setPosTargetXY(item2.getPosTargetX(), item2.getPosTargetY());
        item2.setPosTargetXY(temp.getPosTargetX(), temp.getPosTargetY());
        blocks.get(posX1).remove(posY1);
        blocks.get(posX1).add(posY1, item2);
        blocks.get(posX2).remove(posY2);
        blocks.get(posX2).add(posY2, item1);
    }

    public void removeBlocks(ArrayList<ArrayList<Boolean>> removableArray) {
        // 블럭아이템 제거
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = BLOCK_NUM - 1; i >= 0; i--) {
            for (int j = BLOCK_NUM - 1; j >= 0; j--) {
                if (removableArray.get(i).get(j)) {
                    blocks.get(i).remove(j);
                }
            }
        }
        dropBlocks();
        checkContinuable();
    }

    private void checkContinuable() {
        // 플레이어가 할 게 없어진건 아닌지 검사
        int check = 0;
        try {
            for (int i = BLOCK_NUM - 2; i >= 1; i--) {
                for (int j = BLOCK_NUM - 2; j >= 1; j--) {
                    if (checkChangable(i, j, 0, -1, -1, -1)
                            || checkChangable(i, j, 0, -1, 1, 1)
                            || checkChangable(i, j, 1, -1, -1, 0)
                            || checkChangable(i, j, 1, 1, -1, 0)
                            || checkChangable(i, j, -1, -1, 0, 1)
                            || checkChangable(i, j, 1, -1, 0, 1)
                            || checkChangable(i, j, -1, -1, 1, 0)
                            || checkChangable(i, j, -1, 1, 1, 0)
                            || checkChangable(i, j, -1, -1, 1, -1)
                            || checkChangable(i, j, 1, -1, 1, 1)
                            || checkChangable(i, j, -1, 1, 1, 1)
                            || checkChangable(i, j, -1, -1, -1, 1)
                            || checkChangable(i, j, -1, 0, 2, 0)
                            || checkChangable(i, j, -2, 0, 1, 0)
                            || checkChangable(i, j, 0, -1, 0, 2)
                            || checkChangable(i, j, 0, -2, 0, 1)) {
                        check++;
                    }
                }
            }
        } catch (Exception e) {
        }
        Log.v("evey", "남은 할일 : " + check);
        if (check == 0) {
            Log.v("evey", "할거 없음");
            removeAllBlocks();
            dropBlocks();
        }
    }

    private boolean checkChangable(int i, int j, int x1, int y1, int x2, int y2) {
        try {
            if ((blocks.get(i).get(j).getValue() == blocks.get(i + x1).get(j + y1).getValue())
                    && ((blocks.get(i).get(j).getValue() == blocks.get(i + x2).get(j + y2)
                            .getValue()))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void removeAllBlocks() { // 블럭 전부 제거
        for (int i = 0; i < BLOCK_NUM; i++) {
            blocks.get(i).clear();
        }
    }

    private ArrayList<ArrayList<Boolean>> removableArray;

    public ArrayList<ArrayList<Boolean>> getRemovedPos() {
        return removableArray;
    }

    public int checkAndRemove() { // 3개이상이 붙어있는지 체크하고 제거
        while (!isAllStoped()) {
        }
        removableArray = new ArrayList<ArrayList<Boolean>>();
        // boolean 이차원 배열 생성 및 false로 초기화
        for (int i = 0; i < BLOCK_NUM; i++) {
            ArrayList<Boolean> removable = new ArrayList<Boolean>();
            for (int j = 0; j < BLOCK_NUM; j++) {
                removable.add(false);
            }
            removableArray.add(removable);
        }
        int count = 0;
        // 인접한것들이 같으면 true로 바꾸고 카운트세기
        for (int i = 0; i < BLOCK_NUM; i++) {
            ArrayList<BlockItem> rowItem = blocks.get(i);
            for (int j = 0; j < BLOCK_NUM - 2; j++) {
                if ((rowItem.get(j).getValue() == rowItem.get(j + 1).getValue())
                        && (rowItem.get(j).getValue() == rowItem.get(j + 2).getValue())) {
                    removableArray.get(i).set(j, true);
                    removableArray.get(i).set(j + 1, true);
                    removableArray.get(i).set(j + 2, true);
                    count++;
                }
            }
        }
        for (int i = 0; i < BLOCK_NUM - 2; i++) {
            for (int j = 0; j < BLOCK_NUM; j++) {
                if ((blocks.get(i).get(j).getValue() == blocks.get(i + 1).get(j).getValue())
                        && (blocks.get(i).get(j).getValue() == blocks.get(i + 2).get(j).getValue())) {
                    removableArray.get(i).set(j, true);
                    removableArray.get(i + 1).set(j, true);
                    removableArray.get(i + 2).set(j, true);
                    count++;
                }
            }
        }
        removeBlocks(removableArray);
        return count;
    }

}
