package com.diduweiwu.scene;

import cn.hutool.core.lang.Assert;
import io.restassured.response.Response;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 场景接口调用编排
 */
public class RequestScene {

    /**
     * 初始化call
     */
    private Function<Response, Response> currentCall = r -> r;

    /**
     * 初始化response
     */
    private Response initResponse;

    /**
     * 初始化场景对象
     *
     * @param response
     * @return
     */
    public static RequestScene of(Response response) {
        RequestScene scece = new RequestScene();
        // 以第一个结果为准初始化completeablefuture任务对象
        scece.initResponse = response;
        return scece;
    }

    /**
     * 初始化场景对象
     *
     * @param supplier
     * @return
     */
    public static RequestScene of(Supplier<Response> supplier) {
        RequestScene scece = new RequestScene();
        scece.initResponse = supplier.get();
        return scece;
    }

    /**
     * then链式调用下一个请求
     *
     * @param requestCall
     * @return
     */
    public RequestScene then(Function<Response, Response> requestCall) {
        Assert.notNull(requestCall, "接口响应回调不能为空");
        this.currentCall = this.currentCall.andThen(requestCall);

        return this;
    }

    /**
     * 串联多个接口
     *
     * @param responses
     * @return
     */
    public RequestScene then(Response... responses) {
        for (Response response : responses) {
            // 新增一个then调用
            this.then(r -> response);
        }

        return this;
    }

    /**
     * 串联多个接口
     *
     * @param functions
     * @return
     */
    public RequestScene then(Function<Response, Response>... functions) {
        for (Function<Response, Response> function : functions) {
            // 新增一个then调用
            this.then(function);
        }

        return this;
    }

    /**
     * 场景编排结尾,执行编排流程并返回最后一个接口的返回值
     *
     * @return
     */
    public Response complete() {
        Assert.notNull(this.initResponse, "接口返回值不能为空");
        return this.currentCall.apply(this.initResponse);
    }
}
