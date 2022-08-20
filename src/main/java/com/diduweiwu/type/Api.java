package com.diduweiwu.type;

import com.diduweiwu.util.RequestUtil;
import io.restassured.response.Response;

/**
 * 空接口,用于标识接口请求
 */
public interface Api {
    default Response send(){
        return RequestUtil.send(this);
    }
}
