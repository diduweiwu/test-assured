package com.diduweiwu.scene;

import cn.hutool.core.lang.Assert;
import com.diduweiwu.type.Api;
import com.diduweiwu.util.RequestUtil;
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
    private Supplier<Response> initResponseSupplier;

    /**
     * 初始化场景对象
     *
     * @param response
     * @return
     */
    public static RequestScene of(Response response) {
        RequestScene scene = new RequestScene();
        // 以第一个结果为准初始化 CompleteableFuture 场景对象
        scene.initResponseSupplier = () -> response;
        return scene;
    }

    /**
     * 初始化场景对象
     *
     * @param api
     * @return
     */
    public static RequestScene of(Api api) {
        RequestScene scene = new RequestScene();
        // 以第一个结果为准初始化 CompleteableFuture 场景对象
        scene.initResponseSupplier = () -> RequestUtil.send(api);
        return scene;
    }

    /**
     * 初始化场景对象
     *
     * @param supplier
     * @return
     */
    public static RequestScene of(Supplier<Response> supplier) {
        RequestScene scene = new RequestScene();
        scene.initResponseSupplier = supplier;
        return scene;
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
     * then链式调用下一个请求
     *
     * @param api
     * @return
     */
    public RequestScene then(Api api) {
        Assert.notNull(api, "接口响应回调不能为空");
        this.currentCall = this.currentCall.andThen(r -> RequestUtil.send(api));

        return this;
    }

    /**
     * then链式调用下一个请求
     *
     * @param apis
     * @return
     */
    public RequestScene then(Api... apis) {
        Assert.notEmpty(apis, "接口响应回调不能为空");

        for (Api api : apis) {
            this.currentCall = this.currentCall.andThen(r -> RequestUtil.send(api));
        }

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
        Assert.notNull(this.initResponseSupplier, "接口返回值不能为空");
        return this.currentCall.apply(this.initResponseSupplier.get());
    }

    /**
     * complete 函数的别名
     *
     * @return
     */
    public Response done() {
        return this.complete();
    }
}
