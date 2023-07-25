package com.launcher.inflaunch.enum_status;

public enum VideoStatus {
    ACTIVE("정상"),
    INACTIVE("임시 비노출 처리된 강의영상"),
    DELETED("삭제(처리)된 강의영상");

    private final String status;

    VideoStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
