package com.kasiarakos.gatewayserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping(path = "/contactSupport")
    public Mono<String> contactSupport() {
        return Mono.just("An error occurred. PLease try after some time or contact support team!!!");
    }
}
