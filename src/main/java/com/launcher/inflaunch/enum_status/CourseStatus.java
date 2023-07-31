package com.launcher.inflaunch.enum_status;

public enum CourseStatus {

    READY("강의 준비 중"),
    ACTIVE("정상"),
    INACTIVE("임시 비노출 처리된 강의"),
    DELETED("삭제(처리)된 강의");

    private final String status;

    CourseStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
