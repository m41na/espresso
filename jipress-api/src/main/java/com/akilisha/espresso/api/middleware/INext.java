package com.akilisha.espresso.api.middleware;

import java.util.function.BiConsumer;

public interface INext extends BiConsumer<String, Exception> {

    default void ok() {
        this.accept(null, null);
    }

    default void error(Exception err) {
        this.accept(null, err);
    }

    default void error(String code, Exception err) {
        this.accept(code, err);
    }

    Boolean hasError();

    String errorCode();

    void setHandled(boolean handled);
}
