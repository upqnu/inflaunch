package com.launcher.inflaunch.enum_status;

public enum Advantage {
    EASY_TO_UNDERSTAND("이해하기 쉬워요"),
    WELL_ORGANIZED("강의 전달력이 좋아요"),
    TRENDY("최신 트렌드를 반영해요"),
    USER_FRIENDLY("수강생과의 소통이 잘 돼요"),
    NOTHING_GOOD("딱히 좋은 점이 없어요");

    private final String adv;

    Advantage(String adv) {
        this.adv = adv;
    }

    public String getAdv() {
        return adv;
    }
}
