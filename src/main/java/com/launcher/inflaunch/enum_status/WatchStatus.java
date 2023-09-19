package com.launcher.inflaunch.enum_status;

public enum WatchStatus {
    NOT_WATCHED("시청 중"),
    WATCHED("시청 완료");

    private final String status;

    WatchStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
