package com.akilisha.espresso.api.websocket;

public interface OnBinary {

    void accept(byte[] payload, int offset, int length);
}
