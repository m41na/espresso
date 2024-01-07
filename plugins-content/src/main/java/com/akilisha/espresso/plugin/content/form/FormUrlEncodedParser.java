package com.akilisha.espresso.plugin.content.form;

import lombok.extern.slf4j.Slf4j;
import com.akilisha.espresso.api.content.IBodyParser;
import com.akilisha.espresso.api.request.IRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static com.akilisha.espresso.api.content.IContentType.FORM_URL_ENCODED;

@Slf4j
public class FormUrlEncodedParser implements IBodyParser {

    @Override
    public void init(Map<String, Object> params) {
        log.info("Initializing {}".getClass().getName());
    }

    @Override
    public String contentType() {
        return FORM_URL_ENCODED;
    }

    @Override
    public Object read(IRequest request) throws IOException {
        byte[] bytes = request.readSync();
        return Arrays.stream(new String(bytes).split("&"))
                .map(arr -> {
                    String[] split = arr.split("=");
                    if (split.length > 2) throw new RuntimeException("malformed url encoded data");
                    return split;
                })
                .collect(Collectors.toMap(data -> data[0], data2 -> Arrays.stream(data2.length < 2 ? new String[]{} : data2[1].split(",")).collect(Collectors.toList()), (existing, similar) -> {
                    existing.addAll(similar);
                    return existing;
                }));
    }
}
