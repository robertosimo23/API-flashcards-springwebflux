package dev.RobertoSimoes.reactiveflashcards.core.extension.server;

import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.extension.*;

@Slf4j
public class WebServerExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {
    private MockWebServer mockWebServer;
    private MockResponse mockResponse;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        try {
            log.info("==== Starting mock web server");
            mockWebServer = new MockWebServer();
            mockResponse = new MockResponse();
            mockWebServer.start();
        } catch (final Exception ex) {
            log.warn("==== ERROR - Can't start mock web sever", ex);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        try {
            mockWebServer.shutdown();
        } catch (Exception ex) {
            log.warn("==== ERROR - Can't finish mock web sever", ex);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().isAnnotationPresent(WebServer.class) ||
                parameterContext.getParameter().isAnnotationPresent(Response.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (parameterContext.getParameter().isAnnotationPresent(WebServer.class)) {
            return mockWebServer;
        } else if (parameterContext.getParameter().isAnnotationPresent(Response.class)) {
            return mockResponse;
        } else {
            return null;
        }
    }
}
