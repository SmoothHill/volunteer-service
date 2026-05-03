package com.yueping.volunteer.dto;

public class SendLoginCodeResponse {

    private boolean mock;
    private String debugCode;

    public SendLoginCodeResponse() {
    }

    public SendLoginCodeResponse(boolean mock, String debugCode) {
        this.mock = mock;
        this.debugCode = debugCode;
    }

    public boolean isMock() {
        return mock;
    }

    public void setMock(boolean mock) {
        this.mock = mock;
    }

    public String getDebugCode() {
        return debugCode;
    }

    public void setDebugCode(String debugCode) {
        this.debugCode = debugCode;
    }
}
