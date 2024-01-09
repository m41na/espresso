package com.akilisha.espresso.api.content;

import com.akilisha.espresso.api.request.IRequest;
import com.akilisha.espresso.api.response.IResponse;

import java.io.IOException;
import java.util.Map;

public interface IBodyParser {

    void init(Map<String, Object> params); // provide a hook for initializing object variables after construction is already completed

    String contentType();

    Object read(IRequest request) throws IOException;

    default <T> T read(IRequest request, Class<T> type) throws IOException {
        return type.cast(this.read(request));
    }

    default void write(IResponse response, Object data) {
        //implement where needed
    }
}
