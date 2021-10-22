package com.diduweiwu.processor.contract;

import io.restassured.specification.RequestSpecification;

/**
 * requst前置操作
 */
public interface ISetUp {
    void execute(RequestSpecification request);
}
