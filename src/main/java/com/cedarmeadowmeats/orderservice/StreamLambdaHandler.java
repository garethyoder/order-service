package com.cedarmeadowmeats.orderservice;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;

public class StreamLambdaHandler implements RequestStreamHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final SpringBootLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            handler = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(OrderServiceApplication.class);

            // we use the onStartup method of the handler to register our custom filter
//            handler.onStartup(servletContext -> {
//                FilterRegistration.Dynamic registration = servletContext.addFilter("CognitoIdentityFilter", CognitoIdentityFilter.class);
//                registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
//            });
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            LOGGER.error(e.getMessage());
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    public StreamLambdaHandler() {
        // we enable the timer for debugging. This SHOULD NOT be enabled in production.
        //Timer.enable();
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
    }
}
