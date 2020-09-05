package com.ddaio.testcodegen;

import com.ddaio.testcodegen.generator.testcode.random.RandomLoginTestCodeGenerator;
import com.ddaio.testcodegen.generator.testcode.simple.SimpleTestCodeGenerator;
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
        router.get("/api/generate/skills/csv").handler(handleGenerateSkillCSVRequest());
        router.get("/api/generate/randomlogin/csv").handler(handleGenerateRandomLoginCSVRequest());

        httpServer = vertx.createHttpServer();
        httpServer
                .requestHandler(router)
                .listen(getPort(), "0.0.0.0", result -> {
                    logger.info("HTTP Server Started ...");
                    startPromise.complete();
                });
    }

    private Handler<RoutingContext> handleGenerateRandomLoginCSVRequest() {
        return rc -> {
             TestCodeGenerationResult result = injector.getInstance(RandomLoginTestCodeGenerator.class)
                    .generateTestCodes(rc.remove(REQUEST));

            String headers = "Allocated to,Country,Login,Password\n";
            String content = result.getTestCodes()
                    .stream()
                    .map(TestCode::toString)
                    .collect(Collectors.joining("\n"));
            try {
                rc.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, "text/csv")
                        .putHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=test_codes.csv")
                        .end(headers + content);

            } catch (Exception e) {
                rc.response().end("Error: " + e.getMessage());
            }
        };
    }

    private Handler<RoutingContext> handleGenerateSkillCSVRequest() {
        return rc -> {
            TestCodeGenerationResult generationResult = generateTestCodes(rc.remove(REQUEST));

            String headers = "Allocated to,Country,Login,Password\n";
            String content = generationResult.getTestCodes()
                    .stream()
                    .map(TestCode::toString)
                    .collect(Collectors.joining("\n"));
            try {
                rc.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, "text/csv")
                        .putHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=test_codes.csv")
                        .end(headers + content);

            } catch (Exception e) {
                rc.response().end("Error: " + e.getMessage());
            }

        };
    }

    private int getPort() {
        String port = System.getProperty("heroku.port", "80");
        logger.info(port);
        return Integer.parseInt(port);
    }

    private Handler<RoutingContext> logRequestInformation() {
        return rc -> {
            MultiMap queryParams = rc.queryParams();
            TestCodeGenerationRequest request = paramsToTestCodeRequest(queryParams);
            rc.put(REQUEST, request);
            logger.info("Query param: \"loginBase\" == {}", request.getLoginBase());
            logger.info("Query param: \"loginStartingNumber\" == {}", request.getLoginStartingNumber());
            logger.info("Query param: \"passwordLength\" == {}", request.getPasswordLength());
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
                    .map(testCode -> String.format("%s,%s", testCode.toString(), testCode.getNAttempts()))
                    .collect(Collectors.joining("\n"));
            try {
                rc.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, "text/csv")
                        .putHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=test_codes.csv")
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
        vertx.close();
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new Server());
    }

}
