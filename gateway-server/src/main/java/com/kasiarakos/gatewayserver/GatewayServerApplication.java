package com.kasiarakos.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(GatewayServerApplication.class, args);
  }

  @Bean
  public RouteLocator routeLocatorConfig(RouteLocatorBuilder builder) {
    return builder
        .routes()
        .route(
            p ->
                p.path("/kasiarakos/account-service/**")
                    .filters(
                        f ->
                            f.rewritePath(
                                    "/kasiarakos/account-service/(?<segment>.*)", "/${segment}")
                                .addResponseHeader(
                                    "X-Response_Time", LocalDateTime.now().toString())
                                .circuitBreaker((config) -> config.setName("accountsCircuitBreaker")))
                    .uri("lb://ACCOUNT-SERVICE"))
        .route(
            p ->
                p.path("/kasiarakos/card-service/**")
                    .filters(
                        f ->
                            f.rewritePath("/kasiarakos/card-service/(?<segment>.*)", "/${segment}")
                                .addResponseHeader(
                                    "X-Response_Time", LocalDateTime.now().toString()))
                    .uri("lb://CARD-SERVICE"))
        .route(
            p ->
                p.path("/kasiarakos/loan-service/**")
                    .filters(
                        f ->
                            f.rewritePath("/kasiarakos/locan-service/(?<segment>.*)", "/${segment}")
                                .addResponseHeader(
                                    "X-Response_Time", LocalDateTime.now().toString()))
                    .uri("lb://LOAN-SERVICE"))
        .build();
  }
}
