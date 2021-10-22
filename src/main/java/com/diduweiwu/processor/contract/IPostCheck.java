package com.diduweiwu.processor.contract;

import io.restassured.response.Response;

/**
 * requst后置检查
 */
public interface IPostCheck {
    void execute(Response response);
}
