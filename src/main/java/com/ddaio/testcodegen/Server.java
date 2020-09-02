package com.ddaio.testcodegen;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private HttpServer httpServer;

    @Override
    public void start(Promise<Void> startPromise) {

        final var router = Router.router(vertx);

        router.get().handler(rc -> rc.response().end("Hello, World!"));

        logger.info("{}");

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
