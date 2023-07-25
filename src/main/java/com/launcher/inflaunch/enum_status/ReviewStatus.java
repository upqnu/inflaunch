package com.launcher.inflaunch.enum_status;

public enum ReviewStatus {
    ACTIVE("정상"),
    REPORTED("신고된 수강평입니다"),
    INACTIVE("비노출 처리된 수강평"),
    DELETED("삭제(처리)된 수강평");


    private final String status;

    ReviewStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
