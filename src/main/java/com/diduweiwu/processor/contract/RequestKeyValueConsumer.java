package com.diduweiwu.processor.contract;

import io.restassured.specification.RequestSpecification;

public interface RequestKeyValueConsumer {
    void execute(RequestSpecification request, String key, Object value);
}
