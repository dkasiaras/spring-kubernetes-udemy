package com.kasiarakos.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ResponseTraceFilter {
    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    private final FilterUtility filterUtility;

    public ResponseTraceFilter(FilterUtility filterUtility) {
        this.filterUtility = filterUtility;
    }

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) ->
                chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                    if (exchange.getResponse().getHeaders().get(FilterUtility.CORRELATION_ID) != null) {
                        String correlationId = filterUtility.getCorrelationId(requestHeaders);
                        logger.debug("Updated the correlation id to the outbound headers: {}", correlationId);
                        exchange.getResponse().getHeaders().add(FilterUtility.CORRELATION_ID, correlationId);
                    }
                }));

    }
}
