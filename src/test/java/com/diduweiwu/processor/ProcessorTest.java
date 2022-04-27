package com.diduweiwu.processor;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.diduweiwu.annotation.ContentType;
import com.diduweiwu.annotation.Get;
import com.diduweiwu.annotation.Host;
import com.diduweiwu.annotation.Post;
import com.diduweiwu.annotation.header.Header;
import com.diduweiwu.annotation.header.Headers;
import com.diduweiwu.annotation.param.Body;
import com.diduweiwu.annotation.param.BodyFile;
import com.diduweiwu.annotation.param.Query;
import com.diduweiwu.processor.contract.IPostCheck;
import com.diduweiwu.processor.contract.ISetUp;
import com.diduweiwu.processor.custom.My;
import com.diduweiwu.scene.RequestScene;
import com.diduweiwu.type.Api;
import com.diduweiwu.util.RequestUtil;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
public class ProcessorTest {

    @Post
    @ContentType("application/json")
    class User {

        @Host("http://0.0.0.0:9527")
        private String host;

        @Header(key = "Accept", value = "application/json")
        private String accept;

        @Query(key = "keyWord")
        private String q = "Are you ok";

        @Header(key = "token")
        private String token = "We will we will rock you";

        @Headers
        private Map headers = MapUtil.of("age", 100);

        @Headers
        private Map methodHeaders() {
            return MapUtil.of("Expire-Time", "1000");
        }

        @Body
        private String body = "{'name':'welcome'}";

        @BodyFile
        private File bodyFile = FileUtil.file("/Users/nier/Downloads/20210528153518.png");

        @My(value = "abandon")
        private String search = "test";

//        @File
//        private String uploadFile = "/Users/nier/Downloads/20210528153518.png";
    }

    class SupplierTest implements Supplier<Response> {

        @Override
        public Response get() {
            return null;
        }
    }


    @Test
    public void testSingle(){
        Response response = RequestUtil.send(new User());
        System.out.println(response.asString());
    }

    @Test
    public void testScene() {
        Response response = RequestScene
                .of(RequestUtil.send(new User()))
                .complete();
        System.out.println(response.asString());
    }

    @Test
    public void testCommon() {
        List<ISetUp> setUps = ListUtil.of(r -> r.header("Test", "Yes"));
        List<IPostCheck> postChecks = ListUtil.of(
                rs -> Assert.notContain(rs.body().asString(), "name", "返回值不能包含关键字")
        );
        Response response = RequestUtil.send(new User(), setUps, postChecks);
        log.info(response.asString());
    }

    @Get
    @Host("https://api.uomg.com/api/rand.qinghua")
    @ContentType(ContentType.Type.APPLICATION_JSON)
    static class ApiCall implements Api {
        /**
         * key与value,都以注解配置为第一优先级
         * 若key不存在,则取字段名称/方法名称
         * 若value不存在,则取字段的值/方法返回值
         */
        @Query
        public final String format() {
            return "json";
        }
    }

    @Test
    public void testApi() {
        Response response = RequestScene.of(new ApiCall()).complete();
        log.info(response.getBody().asString());
    }
}
