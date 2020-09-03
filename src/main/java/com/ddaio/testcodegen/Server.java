package com.ddaio.testcodegen;

import com.ddaio.testcodegen.generator.testcode.TestCodeGenerationResult;
import com.ddaio.testcodegen.generator.testcode.TestCodeGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class Server extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private HttpServer httpServer;

    @Override
    public void start(Promise<Void> startPromise) {

        final var router = Router.router(vertx);

        router.get("/hello").handler(rc -> rc.response().end("Hello, World!"));
        router.get("/*").handler(StaticHandler.create("static"));

        router.get("/api/generate/*").handler(logRequestInformation());
        router.get("/api/generate/csv").handler(handleGenerateCSVRequest());
        router.get("/api/generate/raw").handler(handleGenerateRawRequest());

        httpServer = vertx.createHttpServer();
        httpServer
                .requestHandler(router)
                .listen(8080, "0.0.0.0", result -> {
                    logger.info("HTTP Server Started ...");
                    startPromise.complete();
                });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> vertx.close()));
    }

    private Handler<RoutingContext> logRequestInformation() {
        return rc -> {
            MultiMap queryParams = rc.queryParams();
            logger.info("Query param: \"base\" == {}", queryParams.get("base"));
            logger.info("Query param: \"startingNumber\" == {}", queryParams.get("startingNumber"));
            logger.info("Query param: \"passwordLength\" == {}", queryParams.get("passwordLength"));
            logger.info("Query param: \"quantity\" == {}", queryParams.get("quantity"));
            rc.next();
        };
    }

    private Handler<RoutingContext> handleGenerateRawRequest() {
        return rc -> {

            TestCodeGenerationResult generationResult = generateTestCodes(rc.queryParams());

            try {
                String resultJson = new ObjectMapper().writeValueAsString(generationResult);
                rc.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .end(resultJson);

            } catch (Exception e) {
                rc.response().end("Error: " + e.getMessage());
            }
        };
    }

    private Handler<RoutingContext> handleGenerateCSVRequest() {
        return rc -> {
            MultiMap queryParams = rc.queryParams();

            TestCodeGenerationResult generationResult = generateTestCodes(queryParams);

            String content = "username,password\n";
            content += generationResult.getTestCodes()
                    .stream()
                    .map(testCode -> testCode.getUsername() + "," + testCode.getPassword())
                    .collect(Collectors.joining("\n"));
            try {
                rc.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, "text/csv")
                        .putHeader(HttpHeaders.CONTENT_DISPOSITION, "filename=test_codes.csv")
                        .end(Buffer.buffer(content));

            } catch (Exception e) {
                rc.response().end("Error: " + e.getMessage());
            }

        };
    }

    private TestCodeGenerationResult generateTestCodes(MultiMap queryParams) {
        return new TestCodeGenerator(
                queryParams.get("base"),
                Integer.parseInt(queryParams.get("startingNumber")),
                Integer.parseInt(queryParams.get("passwordLength")),
                RandomStringUtils::randomAlphanumeric
        ).generate(Integer.parseInt(queryParams.get("quantity")));
    }

    @Override
    public void stop() {
        logger.info("Shutting Down...");
        httpServer.close(ar -> logger.info("Shutting Down HTTP Server"));
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new Server());
    }

}
