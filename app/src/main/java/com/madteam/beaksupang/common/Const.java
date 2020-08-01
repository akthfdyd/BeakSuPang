
package com.madteam.beaksupang.common;

public class Const {
    public static final int BLOCK_NUM = 7;
    public static final int MILLIS_IN_SEC = 1000;

    // 하나 맞출때마다 시간 증가 퍼센테이지
    public static final double INCREASE_PERCENTAGE = 0.02;

    // 기본 스테이지 제한시간(초)
    public static int INITIAL_TIMEOUT = 50;

    // 스테이지마다 줄어드는 제한시간(초)
    public static int TIMEOUT_DECREASE = 3;

    // 각 스테이지 클리어 기본 필요점수
    public static int SAFE_SCORE_PER_STAGE = 20;

    // 각 스테이지별 최대점수
    public static int MAX_SCORE_PER_STAGE = 50;

    // 매 스테이지마다 늘어나는 필요점수
    public static final int NEED_SCORE_INCREASE = 2;

    // 각 스테이지 올라갈수록 상승하는 토탈스코어 비중
    public static final double SCORE_RATE_PER_STAGE = 0.1;

    // 토탈스코어 계산식에서 총 점수에 곱할 값
    public static final int CALC_SCORE = 20;
}
