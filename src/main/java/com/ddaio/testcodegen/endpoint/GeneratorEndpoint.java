package com.ddaio.testcodegen.endpoint;

import com.ddaio.testcodegen.generator.testcode.TestCodeGenerator;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

import java.util.List;

public class GeneratorEndpoint implements Endpoint {

    private List<TestCodeGenerator> generators;

    public GeneratorEndpoint(List<TestCodeGenerator> generators) {
        this.generators = generators;
    }

    Vertx vertx = Vertx.currentContext().owner();

    @Override
    public String mountPoint() {
        return "/api/generate";
    }

    @Override
    public Router getRouter() {
        Vertx vertx = Vertx.currentContext().owner();
        Router router = Router.router(vertx);

        return router;
    }
}
