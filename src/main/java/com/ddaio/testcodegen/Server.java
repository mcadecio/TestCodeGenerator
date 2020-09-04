package com.ddaio.testcodegen;

import com.ddaio.testcodegen.generator.testcode.SimpleTestCodeGenerator;
import com.ddaio.testcodegen.generator.testcode.TestCode;
import com.ddaio.testcodegen.generator.testcode.TestCodeGenerationRequest;
import com.ddaio.testcodegen.generator.testcode.TestCodeGenerationResult;
import com.ddaio.testcodegen.module.InjModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.*;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class Server extends AbstractVerticle {

    private static final String REQUEST = "request";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Injector injector = Guice.createInjector(new InjModule());

    private HttpServer httpServer;

    @Override
    public void start(Promise<Void> startPromise) {

        final var router = Router.router(vertx);

        router.get("/hello").handler(rc -> rc.response().end("Hello, World!"));
        router.get("/*").handler(StaticHandler.create("static"));

        router.get("/api/generate/*").handler(logRequestInformation());
        router.get("/api/generate/simple/csv").handler(handleGenerateCSVRequest());
        router.get("/api/generate/simple/raw").handler(handleGenerateRawRequest());

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
            TestCodeGenerationRequest request = paramsToTestCodeRequest(queryParams);
            rc.put(REQUEST, request);
            logger.info("Query param: \"loginBase\" == {}", request.getLoginBase());
            logger.info("Query param: \"loginStartingNumber\" == {}", request.getLoginStartingNumber());
            logger.info("Query param: \"passwordLength\" == {}",request.getPasswordLength());
            logger.info("Query param: \"quantity\" == {}", request.getQuantity());
            logger.info("Query param: \"allocatedTo\" == {}", request.getAllocatedTo());
            rc.next();
        };
    }

    private Handler<RoutingContext> handleGenerateRawRequest() {
        return rc -> {

            TestCodeGenerationResult generationResult = generateTestCodes(rc.remove(REQUEST));

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

            TestCodeGenerationResult generationResult = generateTestCodes(rc.remove(REQUEST));

            String headers = "Allocated to,Country,Login,Password,Number of Attempts\n";
            String content = generationResult.getTestCodes()
                    .stream()
                    .map(TestCode::toString)
                    .collect(Collectors.joining("\n"));
            try {
                rc.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, "text/csv")
                        .putHeader(HttpHeaders.CONTENT_DISPOSITION, "filename=test_codes.csv")
                        .end(headers + content);

            } catch (Exception e) {
                rc.response().end("Error: " + e.getMessage());
            }

        };
    }

    private TestCodeGenerationRequest paramsToTestCodeRequest(MultiMap queryParams) {
        return new TestCodeGenerationRequest()
                .setAllocatedTo(queryParams.get("allocatedTo"))
                .setLoginBase(queryParams.get("loginBase"))
                .setLoginStartingNumber(Integer.parseInt(queryParams.get("loginStartingNumber")))
                .setPasswordLength(Integer.parseInt(queryParams.get("passwordLength")))
                .setQuantity(Integer.parseInt(queryParams.get("quantity")));
    }

    private TestCodeGenerationResult generateTestCodes(TestCodeGenerationRequest request) {
        return injector
                .getInstance(SimpleTestCodeGenerator.class)
                .generateTestCodes(request);
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
