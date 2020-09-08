package com.ddaio.testcodegen.handler;

import io.vertx.ext.web.RoutingContext;

public interface RequestHandler {

    void handle(RoutingContext rc);
}
