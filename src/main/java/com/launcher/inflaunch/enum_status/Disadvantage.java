package com.launcher.inflaunch.enum_status;

public enum Disadvantage {
    DIFFICULT_TO_UNDERSTAND("이해하기 어려워요"),
    POORLY_ORGANIZED("강의 전달력이 떨어져요"),
    LACK_OF_INTERACTION("수강생과의 소통이 부족해요"),
    TOO_FAST_PACE("강의 진도가 너무 빨라요"),
    NOTHING_BAD("단점이 없는 강의에요");

    private final String dis;

    Disadvantage(String dis) {
        this.dis = dis;
    }

    public String getDis() {
        return dis;
    }
}
