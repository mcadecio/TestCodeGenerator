package com.ddaio.testcodegen.endpoint;

import io.vertx.ext.web.Router;

public interface Endpoint {

    String mountPoint();

    Router getRouter();
}
