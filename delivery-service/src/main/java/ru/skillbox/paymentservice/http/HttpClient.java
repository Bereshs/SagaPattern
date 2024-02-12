package ru.skillbox.paymentservice.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class HttpClient {
    private final LoadBalancerClient loadBalancerClient;
    private final WebClient webClientBuilder;

    @Autowired
    public HttpClient(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
        this.webClientBuilder = WebClient.create();
    }

    private String getlbNameFromString(String host) {
        ServiceInstance serviceInstance = loadBalancerClient.choose(host);
        return "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort();
    }

    public <T, S> void patch(T requestObject, Class<S> clazz, String id) {
        String requestUrl = getlbNameFromString("order-service") + "/api/order/" + id;


        webClientBuilder
                .patch()
                .uri(requestUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestObject)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(error -> log.info("Request error {}", error.getMessage()))
                .block();
    }

  }
