package com.ddaio.testcodegen;

import com.ddaio.testcodegen.generator.testcode.TestCodeGenerationResult;
import com.ddaio.testcodegen.generator.testcode.TestCodeGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private HttpServer httpServer;

    @Override
    public void start(Promise<Void> startPromise) {

        final var router = Router.router(vertx);

        router.get("/").handler(rc -> rc.response().end("Hello, World!"));

        router.get("/api/generate/raw").handler(rc -> {

            MultiMap queryParams = rc.queryParams();
            logger.info("Query param: \"base\" == {}", queryParams.get("base"));
            logger.info("Query param: \"startingNumber\" == {}", queryParams.get("startingNumber"));
            logger.info("Query param: \"passwordLength\" == {}", queryParams.get("passwordLength"));
            logger.info("Query param: \"quantity\" == {}", queryParams.get("quantity"));

            TestCodeGenerationResult generationResult = new TestCodeGenerator(
                    queryParams.get("base"),
                    Integer.parseInt(queryParams.get("startingNumber")),
                    Integer.parseInt(queryParams.get("passwordLength")),
                    RandomStringUtils::randomAlphanumeric
            ).generate(Integer.parseInt(queryParams.get("quantity")));

            try {
                String resultJson = new ObjectMapper().writeValueAsString(generationResult);
                rc.response().end(resultJson);
            } catch (Exception e) {
                rc.response().end("Error: " + e.getMessage());
            }
        });

        httpServer = vertx.createHttpServer();
        httpServer
                .requestHandler(router)
                .listen(8080, "0.0.0.0", result -> {
                    logger.info("HTTP Server Started ...");
                    startPromise.complete();
                });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> vertx.close()));
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
