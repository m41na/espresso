package com.akilisha.espresso.jett.routable;

import com.akilisha.espresso.api.middleware.INext;
import lombok.Getter;

@Getter
public class HandleNext implements INext {

    private Exception error;
    private String errorCode;
    private Boolean handled = Boolean.FALSE;

    @Override
    public void accept(String code, Exception err) {
        this.errorCode = code;
        this.error = err;
    }

    @Override
    public Boolean hasError() {
        return this.error != null;
    }

    @Override
    public String errorCode() {
        return this.errorCode;
    }

    @Override
    public void setHandled(boolean handled) {
        this.handled = handled;
    }
}
